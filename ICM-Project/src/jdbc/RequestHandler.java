package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Common.Request;
import Common.User;
import Utilities.MessageObject;
import Utilities.RequestType;

public class RequestHandler {
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
			stmt = mysqlConnection.getInstance().getConnection().createStatement();
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method adds a new Change Request.
	 * @author Raz Malka
	 * @param args Arguments of New Request
	 * @return Success or Failure of the adding.
	 */
	public Boolean addCRToDB(ArrayList<Object> args) 
	{
		PreparedStatement stmt;

		try 
		{
			String query = "INSERT INTO Requests (Date, InformationSystem, RequestedChange, CurrentSituation, RequestReason, Note, AttachFiles, Stage, Status, InitiatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			stmt =mysqlConnection.getInstance().getConnection().prepareStatement(query);
			for (int i = 0; i < 10; i++)
				if (i == 6) stmt.setBoolean(i+1, (args.get(i) != null));
				else stmt.setString(i+1, args.get(i).toString());
			
			stmt.executeUpdate();
			System.out.println("Query of Create New Request Executed Successfully!");
		} 
		catch (SQLException e)
		{
			System.out.println("Query of Create New Request Failed to be Executed!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Using a well-known mySQL procedure to get the latest ID after INSERT call.
	 * @author Raz Malka
	 * @return Last Inserted Auto-Incremented ID
	 */
	public String getLastInsertID() {
		Statement stmt;
		try {
			stmt =mysqlConnection.getInstance().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");
			if (rs.next())
				return rs.getString(1);
		} catch (Exception ex) {}
		return "";
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
	public MessageObject checkUserCredentials(String uid, String pwd) throws SQLException {

		// SELECT * WILL CHANGE TO SELECT SPECIFIC RELEVANT FIELDS AND NOT PWD - IN
		// ORDER TO FILL AcademicUser Instance
		String query = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
		stmt.setString(1, uid);
		stmt.setString(2, pwd);
		System.out.println(query);

		ResultSet rs = stmt.executeQuery();
		Boolean res = rs.next();
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(res);
		if (res) {
			User user = new User(rs);
			String jobDescription = getUserJobDescription(user);
			user.setJobDescription(jobDescription);
			args.add(user);
		}
		
		return new MessageObject(RequestType.Login, args);
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
	
	private String getUserJobDescription(User currentUser) throws SQLException {
		ResultSet requestsResult;
		String query = "SELECT JobDescription FROM PermanentEmployee WHERE userID = ?";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

		try {
			stmt.setString(1, currentUser.getId());
			requestsResult = stmt.executeQuery();
			
			if (!requestsResult.next())
				return "Default";
			
			return requestsResult.getString("JobDescription");
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this viewUserRequestTable query: ");
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
	 * @throws SQLException 
	 * @LastModified Raz Malka
	 */
	public MessageObject viewRequestTable(MessageObject msg) throws SQLException {
		User currentUser = (User) msg.getArgs().get(0);
		MessageObject result = null;
		
		switch (getUserJobDescription(currentUser)) {
			case "Supervisor": result = viewAllRequestTable(msg.getTypeRequest(), currentUser); break;
			case "ISD Chief": result = viewAllRequestTable(msg.getTypeRequest(), currentUser); break; // Should be changed
			case "Committee Member": result = viewAllRequestTable(msg.getTypeRequest(), currentUser); break;  // Should be changed
			case "Committee Chairman": result = viewAllRequestTable(msg.getTypeRequest(), currentUser); break;  // Should be changed
			default: result = viewUserRequestTable(msg.getTypeRequest(), currentUser); break;
		}
		return result;
	}

	private MessageObject viewAllRequestTable(RequestType requestType, User currentUser) {
		ResultSet requestsResult;
		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(requestType, new ArrayList<>());

		String requestsQuery = "SELECT * FROM Requests";

		try {
			requestsResult = executeQuery(requestsQuery);
			
			if (!requestsResult.next()) {
				newMessage.getArgs().add(currentUser);
				return newMessage;
			}
			
			do currentUser.getRequestArray().add(new Request(requestsResult));
			while (requestsResult.next());
			newMessage.getArgs().add(currentUser);
			return newMessage;
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this viewUserRequestTable query: ");
			e.printStackTrace();
		}
		return null;
	}
	
	private MessageObject viewUserRequestTable(RequestType requestType, User currentUser) {
		String userID = currentUser.getId();
		ResultSet userRequestsResult;
		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(requestType, new ArrayList<>());

		// Query for getting all the request where the user is relevent to them
		String userRequestsQuery = "SELECT * " + "FROM Requests " + "WHERE \'" + userID + "\' IN (InitiatorID, TesterID, ExecutionLeaderID, EvaluatorID)";

		try {
			userRequestsResult = executeQuery(userRequestsQuery);
			
			if (!userRequestsResult.next()) {
				newMessage.getArgs().add(currentUser);
				return newMessage;
			}
			
			do currentUser.getRequestArray().add(new Request(userRequestsResult));
			while (userRequestsResult.next());
			newMessage.getArgs().add(currentUser);
			return newMessage;
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this viewUserRequestTable query: ");
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
			stmt =mysqlConnection.getInstance().getConnection().createStatement();
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
			PreparedStatement stmt =mysqlConnection.getInstance().getConnection().prepareStatement(
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
