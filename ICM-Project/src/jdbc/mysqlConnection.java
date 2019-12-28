package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Common.Request;
import Common.User;
import Utilities.MessageObject;
import Utilities.RequestType;

public class mysqlConnection {

	// Instance variables **********************************************
	private static mysqlConnection singletonInstance = null;
	private Connection con = null;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the server singleton.
	 */
	private mysqlConnection() {
		singletonInstance = this;
	}

	// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * @return mysqlConnection Singleton Instance
	 */
	public static mysqlConnection getInstance() {
		if (singletonInstance == null)
			singletonInstance = new mysqlConnection();
		return singletonInstance;
	}
	
	// Class methods ***************************************************

	/**
	 * this method defines the jdbc driver so that we can connect to the database
	 */
	private void defineDriver() {
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
	public boolean connectToDB(String hostname, String dbname, String username, String password) {
		defineDriver();
		return establishDBConnection(hostname, dbname, username, password);
	}

	/**
	 * a method that connects the braude default db
	 * 
	 * @return
	 */
	public boolean connectToBraudeDefault() {
		defineDriver();
		return establishDBConnection("localhost", "request_db", "root", "Aa123456");
	}

	/**
	 * this method establishes a connection to the database
	 */
	private boolean establishDBConnection(String hostname, String dbname, String username, String password) {
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
	public boolean closeConnectionToDB() {
		try {
			if (con != null)
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
	public void saveUserToDB(String uid, String pwd) {
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
	 * @throws SQLException 
	 */
	// It will not stay boolean, but will change to AcademicUser or another entity.
	public Boolean checkUserCredentials(String uid, String pwd) throws SQLException {

		// SELECT * WILL CHANGE TO SELECT SPECIFIC RELEVANT FIELDS AND NOT PWD - IN
		// ORDER TO FILL AcademicUser Instance
		String query = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, uid);
		stmt.setString(2, pwd);
		System.out.println(query);

		try {
			return stmt.executeQuery().next();
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

	public MessageObject searchRequest(Object msg) {
		MessageObject message = (MessageObject) msg;
		String requestID = (String) message.getArgs().get(0);
		String role = (String)message.getArgs().get(1);
		ResultSet result;

		// new MessageObject to send back to the use the answer
		ArrayList<Object> args = new ArrayList<Object>();
		MessageObject newMessage = new MessageObject(RequestType.View_Req_Details, args);

		// create query to search the requestID
		String query = "SELECT * FROM Requests WHERE RequestID= \'" + requestID + "\'";
		result = executeQuery(query);

		try {
			if (!result.next()) {

				newMessage.getArgs().add(false);
				return newMessage;
			}
			// if request id was found return this data in the arrayList
			newMessage.getArgs().add(true);
			Request request  = new Request(result);
			newMessage.getArgs().add(role);
			newMessage.getArgs().add(request);
			return newMessage;
		} catch (SQLException e) {

			System.out.println("An Error occured while trying to execute the quesry: " + query);
			e.printStackTrace();
		}

		return null;
	}
	

	/**
	 * this method gets from the DB all the relevant requests for user
	 * 
	 * @param msg userID
	 * @return User that contain all relevant requests
	 * @author Shoham Yamin
	 * @LastModified Raz Malka
	 */
	public MessageObject viewUserRequestTable(MessageObject msg) {
		String userID = (String) msg.getArgs().get(0);
		ResultSet userInfoResult, userRequestsResult;
		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(RequestType.viewUserRequestTable, new ArrayList<>());

		// Query for get user information
		String userInfoQuery = "SELECT * FROM Users WHERE UserID= \'" + userID + "\'";

		// Query for getting all the request where the user is relevent to them
		String userRequestsQuery = "SELECT * " + "FROM Requests " + "WHERE \'" + userID + "\' IN (InitiatorID, TesterID, ExecutionLeaderID, CommitteeMember1ID, CommitteeMember2ID, CommitteeChairmenID, EvaluatorID)";

		try {
			userInfoResult = executeQuery(userInfoQuery);
			userRequestsResult = executeQuery(userRequestsQuery);

			// initialize user with is data from the DB
			userInfoResult.next();
			User user = new User((String) userInfoResult.getString("UserID"),
					(String) userInfoResult.getString("Password"), (String) userInfoResult.getString("Name"),
					(String) userInfoResult.getString("Email"), (String) userInfoResult.getString("jobDescription"));
			
			if (!userRequestsResult.next()) {
				newMessage.getArgs().add(user);
				return newMessage;
			}
			
			do {
				// Create request for every request that relevant to the user
				Request request = new Request(userRequestsResult.getString("RequestID"),
						userRequestsResult.getString("InformationSystem"),
						userRequestsResult.getString("CurrentSituation"),
						userRequestsResult.getString("RequestedChange"),
						userRequestsResult.getString("ReasonForRequest"), userRequestsResult.getString("Note"),
						userRequestsResult.getString("AttachFiles"), userRequestsResult.getString("Date"),
						userRequestsResult.getString("CurrentStage"), userRequestsResult.getString("RequestStatus"),
						userRequestsResult.getString("InitiatorID"), userRequestsResult.getString("TesterID"),
						userRequestsResult.getString("ExecutionLeaderID"),
						userRequestsResult.getString("CommitteeMember1ID"),
						userRequestsResult.getString("CommitteeMember2ID"),
						userRequestsResult.getString("CommitteeChairmenID"),
						userRequestsResult.getString("EvaluatorID"));

				user.getRequestArray().add(request);
			} while (userRequestsResult.next());

			newMessage.getArgs().add(user);
			return newMessage;
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this viewUserRequestTable quesrys: ");
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
	public ResultSet executeQuery(String query) {

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
	public MessageObject changeStatus(Object msg) {
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
