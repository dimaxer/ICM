package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class mysqlConnection {

	// Instance variables **********************************************
	private static Connection con;

	// Class methods ***************************************************

	/**
	 * this method defines the jdbc driver so that we can connect to the database
	 */
	private static void defineDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

	}

	/**
	 * a method to connect to a db using custom values
	 * 
	 * @param hostname
	 * @param dbname
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean connectToDB(String hostname, String dbname, String username, String password) {
		defineDriver();
		return establishDBConnection(hostname, dbname, username, password);
	}

	/**
	 * a method that connects the braude default db
	 * 
	 * @return
	 */
	public static boolean connectToBraudeDefault() {
		defineDriver();
		return establishDBConnection("localhost", "request_db", "root", "Aa123456");
	}

	/**
	 * this method establishes a connection to the database
	 */
	private static boolean establishDBConnection(String hostname, String dbname, String username, String password) {
		try {
			// if any of the fields is empty connect to this db
			if (hostname.isEmpty() || dbname.isEmpty() || password.isEmpty() || username.isEmpty())
				// change this line if you want to connect to a different db using the custom db
				// button
				con = DriverManager.getConnection("jdbc:mysql://remotemysql.com/ZZUehgwcK6?serverTimezone=IST",
						"ZZUehgwcK6", "hfwrGUeVv7");
			else
				// if the fields are filled connect to the db using those fields
				con = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + dbname + "?serverTimezone=IST",
						username, password);
			System.out.println("SQL connection succeed! : " + con);

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
		return true;
	}

	/**
	 * This method close's the db connection
	 * 
	 * @return
	 */
	public static boolean closeConnectionToDB() {
		try {
			con.close();
		} catch (SQLException ex) {
			System.out.println("an Error occured while trying to close the DB connection");
			return false;
		}
		return true;
	}

	/**
	 * a method that saves a new user in to the db
	 * 
	 * @param uid
	 * @param pwd
	 */
	public static void saveUserToDB(String uid, String pwd) {
		Statement stmt;
		String query = "INSERT INTO sample_login (uid, upassword) VALUES (\'" + uid + "\', \'" + pwd + "\')";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method checks if the user exists in the db return true if it does and
	 * false if not
	 * 
	 * @param uid
	 * @param pwd
	 * @return
	 */
	// It will not stay boolean, but will change to AcademicUser or another entity.
	public static Boolean checkUserCredentials(String uid, String pwd) {

		// SELECT * WILL CHANGE TO SELECT SPECIFIC RELEVANT FIELDS AND NOT PWD - IN
		// ORDER TO FILL AcademicUser Instance
		String query = "SELECT * FROM sample_login WHERE uid = \'" + uid + "\' AND upassword = \'" + pwd + "\'";
		System.out.println(query);

		try {
			return executeQuery(query).next();
		} catch (SQLException e) {

			System.out.println("An Error occured while trying to execute the quesry: " + query);
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * this method searches a request with the request id and returns the all the
	 * requests information if it was found in the db the first value in the
	 * MessageObject Array list indicates if it was found or not [True|False]
	 * 
	 * @param msg
	 * @return
	 */

	public static MessageObject searchRequest(Object msg) {
		MessageObject message = (MessageObject) msg;
		String requestID = (String) message.getArgs().get(0);
		ResultSet result;

		// new MessageObject to send back to the use the answer
		ArrayList<Object> args = new ArrayList<Object>();
		MessageObject newMessage = new MessageObject(RequestType.View_Req_Details, args);

		// create query to search the requestID
		String query = "SELECT * FROM RequestInformation WHERE RequestID= \'" + requestID + "\'";
		result = executeQuery(query);

		try {
			if (!result.next()) {

				newMessage.getArgs().add(false);
				return newMessage;
			}
			// if request id was found return this data in the arrayList
			newMessage.getArgs().add(true);
			newMessage.getArgs().add(result.getString("InitiatorName"));
			newMessage.getArgs().add(result.getString("RequestID"));
			newMessage.getArgs().add(result.getString("CurrentStageDetails"));
			newMessage.getArgs().add(result.getString("Description"));
			newMessage.getArgs().add(result.getString("Status"));
			newMessage.getArgs().add(result.getString("HandlerName"));
			return newMessage;
		} catch (SQLException e) {

			System.out.println("An Error occured while trying to execute the quesry: " + query);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * a Method to execute query's in the db
	 * 
	 * @param query
	 * @return
	 */
	public static ResultSet executeQuery(String query) {

		ResultSet result = null;
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeQuery(query);
			result = stmt.getResultSet();

		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute the quesry: " + query);
			e.printStackTrace();

		}
		return result;
	}

	/**
	 * this method enables to change the status of a request in the db
	 * 
	 * @param msg
	 * @return
	 */
	public static MessageObject changeStatus(Object msg) {
		int result = 0;
		MessageObject message = (MessageObject) msg;

		// a MessageObject to send back to the client
		ArrayList<Object> args = new ArrayList<Object>();
		MessageObject newMessage = new MessageObject(RequestType.change_Status, args);

		// executing update query
		try {
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE RequestInformation SET Status=? WHERE RequestID=? AND Status!=? ",
					Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, (String) message.getArgs().get(1));
			stmt.setString(2, (String) message.getArgs().get(0));
			stmt.setString(3, (String) message.getArgs().get(1));
			result = stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("An Error Occured During Change Status request int the db");
		}

		// this check is to know the field was changed in the db or not
		if (result == 0) {
			newMessage.getArgs().add(false);
			return newMessage;
		} else {
			System.out.println("the keys is:" + result);
			newMessage.getArgs().add(true);
			return newMessage;
		}

	}
}
