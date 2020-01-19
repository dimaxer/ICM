package jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import Common.Employee;
import Common.EvaluatorReport;
import Common.Request;
import Common.TimeAssessmentRequest;
import Common.TimeExtensionRequest;
import Common.User;
import Common.EvalutorAppoitmentTableSerializable;
import Common.Interval;
import Utilities.MailSender;
import Utilities.MessageObject;
import Utilities.RequestType;
import ocsf.server.ConnectionToClient;
import server.DBServer;
import server.UserConnectivityManager;

/**
 * This class handles the requests to the Database through sql.
 *
 */
public class SqlRequestHandler {
	/**
	 * a method that saves a new user in to the db
	 * 
	 * @param uid user id
	 * @param pwd password
	 */
	public void saveUserToDB(String uid, String pwd) {
		Statement stmt;
		String query = "INSERT INTO sample_login (uid, upassword) VALUES (\'" + uid + "\', \'" + pwd + "\')";
		try {
			stmt = mysqlConnection.getInstance().getConnection().createStatement();
			stmt.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method adds a new Change Request.
	 * 

	 * @param args Arguments of New Request
	 * @return Success or Failure of the adding.
	 */
	public Boolean addCRToDB(ArrayList<Object> args) {
		PreparedStatement stmt;
		String insertStatusToStatusTable = "INSERT INTO StatusTable (requestId, status, startTime, endTime) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE startTime = ?, endTime = ?";
		try {
			String query = "INSERT INTO Requests (Date, InformationSystem, RequestedChange, CurrentSituation, RequestReason, Note, AttachFiles, Stage, Status, InitiatorID, EvaluatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			for (int i = 0; i < 11; i++)
				if (i == 6) stmt.setBoolean(i+1, (args.get(i) != null));
				else stmt.setString(i+1, args.get(i).toString());
			
			stmt.executeUpdate();
			System.out.println("Query of Create New Request Executed Successfully!");

			String requestID = getLastInsertID();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate now = LocalDate.now();
			
			insertNewEvaluatorToEvaluatorTable(args.get(10).toString(), requestID); /// insert New Evaluator to "Evaluator
																			/// Appointment Table"
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertStatusToStatusTable);
			stmt.setString(1, requestID);
			stmt.setString(2, "Active");
			stmt.setDate(3, java.sql.Date.valueOf(LocalDate.parse(dtf.format(now), dtf)));
			stmt.setDate(4, null);
			stmt.setDate(5, java.sql.Date.valueOf(LocalDate.parse(dtf.format(now), dtf)));
			stmt.setDate(6, null);
			stmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("Query of Create New Request Failed to be Executed!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Using a well-known mySQL procedure to get the latest ID after INSERT call.
	 * 

	 * @return Last Inserted Auto-Incremented ID
	 */
	public String getLastInsertID() {
		Statement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");

			if (rs.next())
				return rs.getString(1);
		} catch (Exception ex) {
		}
		return "";
	}

	/**
	 * this method checks if the user exists in the db return true if it does and
	 * false if not also checks if the user is already logged in or not are returns
	 * the appropriate message to the client
	 * 
	 * @param userID id
	 * @param password password
	 * @param client client
	 * @return data
	 * @throws SQLException exception
	 */
	// It will not stay boolean, but will change to AcademicUser or another entity.
	public MessageObject checkUserCredentials(String userID, String password, ConnectionToClient client)
			throws SQLException {

		// SELECT * WILL CHANGE TO SELECT SPECIFIC RELEVANT FIELDS AND NOT PWD - IN
		// ORDER TO FILL AcademicUser Instance
		String query = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
		stmt.setString(1, userID);
		stmt.setString(2, password);
		System.out.println(query);

		ResultSet rs = stmt.executeQuery();
		Boolean res = rs.next();

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(res);
		if (res) {

			if (UserConnectivityManager.getInstance().addLoggedOnUser(userID, client))// check if this user is already
																						// logged in or not
			{
				// not logged in return user information
				User user = new User(rs);
				String jobDescription = getUserJobDescription(user);
				user.setJobDescription(jobDescription);
				args.add(user);

			} else
				return new MessageObject(RequestType.ErrorThisUserIsAllreadyLoggedIn, args);// send error message that
																							// user is already loged in

		}

		return new MessageObject(RequestType.Login, args);// return the user information because he successfully logged
															// in
	}

	/**
	 * this method searches a request with the request id and returns the all the
	 * requests information if it was found in the db the first value in the
	 * MessageObject Array list indicates if it was found or not [True|False]
	 * 
	 * @param msg data
	 * @return data
	 */
	public MessageObject searchRequest(Object msg) {
		MessageObject message = (MessageObject) msg;
		String requestID = (String) message.getArgs().get(0);
		String role = (String) message.getArgs().get(1);
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
			Request request = new Request(result);
			newMessage.getArgs().add(role);
			newMessage.getArgs().add(request);
			return newMessage;
		} catch (Exception e) {

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
		} catch (Exception e) {
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
	 * @throws SQLException exception
	 */
	public MessageObject viewRequestTable(MessageObject msg) throws SQLException {
		User currentUser = (User) msg.getArgs().get(0);
		MessageObject result = null;

		switch (getUserJobDescription(currentUser)) {
		case "Supervisor":
		case "Committee Member":
		case "Committee Chairman":
			result = viewAllRequestTable(msg.getTypeRequest(), currentUser);
			break;
		default:
			result = viewUserRequestTable(msg.getTypeRequest(), currentUser);
			break;
		}
		return result;
	}

	/**
	 * returns all requests
	 * 
	 * @param requestType
	 * @param currentUser
	 * @return
	 */
	private MessageObject viewAllRequestTable(RequestType requestType, User currentUser) {

		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(requestType, new ArrayList<>());

		String requestsQuery = "SELECT * FROM Requests";

		return getRequestData(currentUser, newMessage, requestsQuery);
	}

	/**
	 * returns only requests that are in the decision stage so that the committee
	 * members could start working
	 * 
	 * @param requestType
	 * @param currentUser
	 * @deprecated
	 * @return
	 */
	private MessageObject viewChairManRequestTable(RequestType requestType, User currentUser) {

		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(requestType, new ArrayList<>());

		// Query for getting all the request where the user is relevent to them
		String userRequestsQuery = "SELECT * FROM Requests WHERE Stage='Decision'";

		return getRequestData(currentUser, newMessage, userRequestsQuery);
	}

	/**
	 * gets request data by using a specific query returns MessageObject
	 * 
	 * @param currentUser
	 * @param newMessage
	 * @param userRequestsQuery
	 * @return
	 */
	private MessageObject getRequestData(User currentUser, MessageObject newMessage, String userRequestsQuery) {
		ResultSet userRequestsResult;
		try {
			userRequestsResult = executeQuery(userRequestsQuery);

			if (!userRequestsResult.next()) {
				newMessage.getArgs().add(currentUser);
				return newMessage;
			}

			do
				currentUser.getRequestArray().add(new Request(userRequestsResult));
			while (userRequestsResult.next());
			newMessage.getArgs().add(currentUser);
			return newMessage;
		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this viewUserRequestTable query: ");
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject viewUserRequestTable(RequestType requestType, User currentUser) {
		String userID = currentUser.getId();

		// new MessageObject to send back to the use the answer
		MessageObject newMessage = new MessageObject(requestType, new ArrayList<>());

		// Query for getting all the request where the user is relevent to them
		String userRequestsQuery = "SELECT * " + "FROM Requests " + "WHERE \'" + userID
				+ "\' IN (InitiatorID, TesterID, ExecutionLeaderID, EvaluatorID)";

		return getRequestData(currentUser, newMessage, userRequestsQuery);
	}

	/**
	 * a Method to execute query's in the db
	 * 
	 * @param query query
	 * @return data
	 */
	public ResultSet executeQuery(String query) {

		ResultSet result = null;
		Statement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().createStatement();
			stmt.executeQuery(query);
			result = stmt.getResultSet();

		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute the quesry: " + query);
			e.printStackTrace();

		}
		return result;
	}

	/**
	 * this method enables to change the status of a request in the db
	 * 
	 * @param msg data
	 * @return data
	 */
	public MessageObject changeStatus(Object msg) {
		int result = 0;
		MessageObject message = (MessageObject) msg;

		// a MessageObject to send back to the client
		ArrayList<Object> args = new ArrayList<Object>();
		MessageObject newMessage = new MessageObject(RequestType.change_Status, args);

		// executing update query
		try {
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(
					"UPDATE RequestInformation SET Status=? WHERE RequestID=? AND Status!=? ",
					Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, (String) message.getArgs().get(1));
			stmt.setString(2, (String) message.getArgs().get(0));
			stmt.setString(3, (String) message.getArgs().get(1));
			result = stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
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

	/**
	 * appoint evaluator by the system shoham
	 * 
	 * @deprecated
	 * @param requestID id
	 */
	public void automaticAppointmentEvaluator(String requestID) {
		ResultSet allISDUsers;
		ArrayList<String> allISDUsersArray = new ArrayList<String>();
		PreparedStatement stmt;
		// get all the users that are in ISD Department

		try {
			String getAllISDUsers = "SELECT UserID FROM Users WHERE userType = \"ISE\"";
			allISDUsers = executeQuery(getAllISDUsers);

			// check is their is a USER that is ISE
			if (!allISDUsers.next()) {
				System.out.println("we dont have Users");
				return;
			}

			do {
				allISDUsersArray.add((String) allISDUsers.getString("UserID"));
			} while (allISDUsers.next());

			Random rand = new Random();
			int randomUserID = rand.nextInt(allISDUsersArray.size() - 1);
			String EvaluatorID = allISDUsersArray.get(randomUserID);
			ResultSet evaluatorNameRs = executeQuery(new String("SELECT Name FROM Users WHERE UserID=" + EvaluatorID));
			evaluatorNameRs.next();
			String insertEvaluatorQuery = "INSERT INTO EvaluatorAppointment (RequestID, EvaluatorID,EvaulatorName) VALUES (?, ?, ?);";
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertEvaluatorQuery);
			stmt.setString(1, requestID);
			stmt.setString(2, EvaluatorID);
			stmt.setString(3, evaluatorNameRs.getString("Name"));
			stmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute query in automatiAppointmentEvaloeytor");
			e.printStackTrace();
		}

	}

	/**
	 * insert the current Evaluator to Evaluator Appointment Table
	 * 
	 * @param EvaluatorID id
	 * @param requestID id
	 */
	public void insertNewEvaluatorToEvaluatorTable(String EvaluatorID, String requestID) {

		ResultSet EvaluatorRs = executeQuery(new String("SELECT Name FROM Users WHERE UserID=" + EvaluatorID));
		try {
			EvaluatorRs.next();
			String EvaluatorName = EvaluatorRs.getString("Name");
			PreparedStatement stmt;
			String insertEvaluatorQuery = "INSERT INTO EvaluatorAppointment (RequestID, EvaluatorID,EvaluatorName) VALUES (?, ?, ?);";

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertEvaluatorQuery);

			stmt.setString(1, requestID);
			stmt.setString(2, EvaluatorID);
			stmt.setString(3, EvaluatorName);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete evaluator from EvaluatorAppointment because he was approved by the
	 * Supervisor
	 * 
	 * @param requestID id
	 * 
	 */
	public void deleteApprovedEvaluator(String requestID) {
		PreparedStatement stmt;

		String deleteQuery = "DELETE FROM EvaluatorAppointment WHERE RequestID = ?";
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, requestID);
			stmt.execute();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Update the EvaluatorID in the requests table to the given evaluatrorID
	 * 
	 * @param requestID id
	 * @param evaluatorID id
	 * @return data
	 */
	public MessageObject updateRequestEvaluator(String requestID, String evaluatorID) {

		String updateEvaloeytor = "Update Requests SET EvaluatorID =?  WHERE RequestID =? ";
		String updateStageInRequests ="UPDATE Requests SET Stage = ? WHERE RequestID = ?";
		String insertStageToStageTable = "INSERT INTO StageTable (requestId, stage, startTime, endTime, deadlineTime) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE startTime = ?, endTime = ?";

		PreparedStatement stmt;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate nowLocalDate = LocalDate.parse(LocalDate.now().format(dtf), dtf);
			java.sql.Date ourJavaDateObject = java.sql.Date.valueOf(LocalDate.parse(dtf.format(nowLocalDate), dtf));
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateEvaloeytor);
			
			stmt.setString(1, evaluatorID);
			stmt.setString(2, requestID);
			// Update EvaloeytorID in the DB
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageInRequests);
			
			stmt.setString(1, "Evaluation");
			stmt.setString(2, requestID);
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertStageToStageTable);
			
			stmt.setString(1, requestID);
			stmt.setString(2, "Evaluation");
			stmt.setDate(3, ourJavaDateObject);
			stmt.setDate(4, null);
			stmt.setDate(5, null);
			stmt.setDate(6, ourJavaDateObject);
			stmt.setDate(7, null);
			stmt.executeUpdate();
			deleteApprovedEvaluator(requestID);
			return new MessageObject(RequestType.ApprovedEvaluator, new ArrayList<>());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Update the stage of requests table for RequestID Insert new Evaluation Stage
	 * in StageTable in the DB
	 * @deprecated
	 * @param requestID id
	 */
	public void sinsertStageEvaluator(String requestID) {

		// update the stage in requests
		String updateStageInRequestsTable = "Update Requests SET Stage =? WHERE RequestID =?";
		try {
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection()
					.prepareStatement(updateStageInRequestsTable);
			stmt.setString(1, "Evaluation");
			stmt.setString(2, requestID);
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// insert Evaluation Stage to StageTable
		String insertEvaluationStage = "INSERT INTO StageTable (requestId, stage, startTime, endTime, deadlineTime) VALUES (?, ?, ?, ?, ?)";
		try {
			java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection()
					.prepareStatement(insertEvaluationStage);
			stmt.setString(1, requestID);
			stmt.setString(2, "Evaluation");
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setString(5, null);

			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This methods gets the Name and Evaluator ID of Information Systems
	 * 
	 * @param message data
	 * @throws SQLException exception
	 * @return data
	 */
	public MessageObject getInformationSystemDetails(MessageObject message) throws SQLException {
		ResultSet rs;
		MessageObject response = new MessageObject(RequestType.InformationSystem_Details, new ArrayList<>());
		ArrayList<String> infoSysNames = new ArrayList<>();
		ArrayList<String> currentEvaluatorIDs = new ArrayList<>();
		Boolean idCanBeNull = (Boolean) message.getArgs().get(0);
		String query = "";
		if (idCanBeNull)
			query = "SELECT * FROM InformationSystem";
		else
			query = "SELECT * FROM InformationSystem WHERE evaluatorID IS NOT NULL";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

		try {
			rs = stmt.executeQuery();

			while (rs.next()) {
				infoSysNames.add(rs.getString("infoSysName"));
				currentEvaluatorIDs.add(rs.getString("evaluatorID"));
			}
			response.getArgs().add(infoSysNames);
			response.getArgs().add(currentEvaluatorIDs);

			return response;
		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this getInformationSystemDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the ID and Name of each User
	 * 
	 * @param message data
	 * @throws SQLException exception
	 * @return data
	 */
	public MessageObject getAllUserDetails(MessageObject message) throws SQLException {
		ResultSet rs;
		MessageObject response = new MessageObject(RequestType.AllUserDetails, new ArrayList<>());
		HashMap<String, String> userData = new HashMap<>();
		String query = "SELECT UserID, Name FROM Users WHERE userType = 'ISE'";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

		try {
			rs = stmt.executeQuery();

			while (rs.next())
				userData.put(rs.getString("UserID"), rs.getString("Name"));
			response.getArgs().add(userData);

			return response;
		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this getAllUserDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the Details of Users with Permanent Roles (roleName, ID)
	 * 
	 * @param message data
	 * @throws SQLException exception
	 * @return data
	 */
	public MessageObject getPermanentRolesDetails(MessageObject message) throws SQLException {
		ResultSet rs;
		MessageObject response = new MessageObject(RequestType.PermanentRoles_Details, new ArrayList<>());
		HashMap<String, String> userData = new HashMap<>();
		String query = "SELECT * FROM PermanentEmployee";
		PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

		try {
			rs = stmt.executeQuery();

			while (rs.next())
				userData.put(rs.getString("userID"), rs.getString("jobDescription"));
			response.getArgs().add(userData);

			return response;
		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this getPermanentRolesDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/** This method updates the evaluator of a certain Information System
	 * 
	 * @param message data
	 * @throws SQLException exception
	 */
	public void updateEvaluator(MessageObject message) throws SQLException {
		String queryInfoSys = "UPDATE InformationSystem SET evaluatorID = ? WHERE infoSysName = ?";
		PreparedStatement stmtInfoSys = mysqlConnection.getInstance().getConnection().prepareStatement(queryInfoSys);

		String queryRequests = "UPDATE Requests SET EvaluatorID = ? WHERE InformationSystem = ?";
		PreparedStatement stmtRequests = mysqlConnection.getInstance().getConnection().prepareStatement(queryRequests);

		try {
			stmtInfoSys.setString(1, message.getArgs().get(1).toString());
			stmtInfoSys.setString(2, message.getArgs().get(0).toString());
			stmtInfoSys.executeUpdate();

			stmtRequests.setString(1, message.getArgs().get(1).toString());
			stmtRequests.setString(2, message.getArgs().get(0).toString());
			stmtRequests.executeUpdate();

		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this updateEvaluator query: ");
			e.printStackTrace();
		}
	}

	/** This method updates the permanent roles holders
	 * 
	 * @param message data
	 * @throws SQLException exception
	 */
	public void updatePermanentRoles(MessageObject message) throws SQLException {
		ArrayList<Object> args = message.getArgs();

		String querySupervisor = "UPDATE PermanentEmployee SET userID = ? WHERE jobDescription = 'Supervisor'";
		String queryChairman = "UPDATE PermanentEmployee SET userID = ? WHERE jobDescription = 'Committee Chairman'";
		String queryCommittee1 = "UPDATE PermanentEmployee SET userID = ? WHERE jobDescription = 'Committee Member' AND roleCount = 1";
		String queryCommittee2 = "UPDATE PermanentEmployee SET userID = ? WHERE jobDescription = 'Committee Member' AND roleCount = 2";

		PreparedStatement stmtSupervisor = mysqlConnection.getInstance().getConnection()
				.prepareStatement(querySupervisor);
		PreparedStatement stmtChairman = mysqlConnection.getInstance().getConnection().prepareStatement(queryChairman);
		PreparedStatement stmtCommittee1 = mysqlConnection.getInstance().getConnection()
				.prepareStatement(queryCommittee1);
		PreparedStatement stmtCommittee2 = mysqlConnection.getInstance().getConnection()
				.prepareStatement(queryCommittee2);

		try {
			stmtSupervisor.setString(1, args.get(0).toString());
			stmtChairman.setString(1, args.get(1).toString());
			stmtCommittee1.setString(1, args.get(2).toString());
			stmtCommittee2.setString(1, args.get(3).toString());

			stmtSupervisor.executeUpdate();
			stmtChairman.executeUpdate();
			stmtCommittee1.executeUpdate();
			stmtCommittee2.executeUpdate();

		} catch (Exception e) {
			System.out.println("An Error occured while trying to execute this updatePermanentRoles query: ");
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the Evaluator Table and returns it
	 * @param message data
	 * @return data
	 */
	public MessageObject viewEvaluatorTable(MessageObject message) {
		ResultSet rs = executeQuery(new String("Select RequestID,EvaluatorID,EvaulatorName from EvaluatorAppointment"));

		try {
			if (!(rs.next())) {
				message.getArgs().add(false);
				return message;
			} else
				message.getArgs().add(true);
			do {
				message.getArgs().add(new EvalutorAppoitmentTableSerializable(rs.getString("RequestID"),
						rs.getString("EvaluatorID"), rs.getString("EvaulatorName")));

			} while (rs.next());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;

	}

	/**
	 * This method gets the ISE Workers Table and returns it
	 * @param message data
	 * @return data
	 */
	public MessageObject viewIseTable(MessageObject message) {
		String Quary1 = "SELECT userID from PermanentEmployee )";
		String Quary2 = "SELECT evaluatorID FROM InformationSystem  )";
		ResultSet rs = executeQuery(
				new String("SELECT UserID,Name FROM Users WHERE userType = \"ISE\" AND UserID NOT IN (  ") + Quary1
						+ "AND UserID NOT IN ( " + Quary2);
		try {
			if (!(rs.next())) {
				message.getArgs().add(false);
				return message;
			} else
				message.getArgs().add(true);
			do {
				message.getArgs()
						.add(new EvalutorAppoitmentTableSerializable(rs.getString("UserID"), rs.getString("Name")));
			} while (rs.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * this method uploads the evaluator report to the db
	 * 
	 * @param requestID id
	 * @param description description
	 * @param constraints constraints
	 * @param result result
	 * @param evaluatorID id
	 * @return was successful
	 */
	public boolean uploadEvaluatorReport(String requestID, String description, String constraints, String result,
			String evaluatorID) {
		String insertEvaluationReport = "INSERT INTO EvaluationReport (RequestId, EvaluatorID, Description, Constraints, Result) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Description = ?, Constraints = ?, Result = ?";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertEvaluationReport);

			stmt.setString(1, requestID);
			stmt.setString(2, evaluatorID);
			stmt.setString(3, description);
			stmt.setString(4, constraints);
			stmt.setString(5, result);
			stmt.setString(6, description);
			stmt.setString(7, constraints);
			stmt.setString(8, result);

			stmt.executeUpdate();
			
			ArrayList<Object> args = new ArrayList<>();
			args.add(requestID);
			args.add("Decision");
			args.add("Evaluation");
			swapStage(new MessageObject(RequestType.swapStage, args));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * this method changes the stage of the request to inspection And Decision by
	 * removing from the request the evaluator id and adding committee members to
	 * the request
	 * 
	 * @param requestID id
	 * @param EvaluatorID id
	 * @deprecated
	 * @return was successful
	 */
	public boolean changeStageToInspectonAndDecision(String requestID, String EvaluatorID) {

		String query = "UPDATE Requests SET Stage= 'Decision' WHERE EvaluatorID = ? AND RequestID = ? ";

		try {
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

			//stmt.setNull(1, java.sql.Types.VARCHAR);
			stmt.setString(1, EvaluatorID);
			stmt.setString(2, requestID);

			stmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}
	/**
	 * getting from the DB all the information of the report
	 * @param message ArrayList that contain the requestID of the report
	 * @return Evaluator Report
	 */
	public EvaluatorReport getEvaluatorReport(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String reportQuery = "SELECT * FROM EvaluationReport WHERE RequestID =?";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(reportQuery);
			stmt.setString(1, requestID);
			EvaluatorReport evaluatorReport = new EvaluatorReport(stmt.executeQuery());
			
			return evaluatorReport;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * This method sets an additional info to be asked by the evaluator by the committee
	 * @param message data
	 */
	public void askForAdditionalInfo(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String additionalInfo = (String) message.getArgs().get(1);
		String query = "INSERT INTO AdditionalInfo VALUES (?,?) ON DUPLICATE KEY UPDATE additionalInfo = ?";
		String updateQuery = "UPDATE StageTable SET deadlineTime = NULL WHERE requestId = ? AND Stage = 'Evaluation'";
		
		String updateStageQuery = "UPDATE Requests SET Stage = 'Evaluation' WHERE RequestID = ?";
		PreparedStatement stmt;
		PreparedStatement updateStage;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			updateStage = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageQuery);
			
			// Update into table the Additional Info Required
			stmt.setString(1, requestID);
			stmt.setString(2, additionalInfo);
			stmt.setString(3, additionalInfo);
			
			stmt.executeUpdate();
			
			// Update stage back to Evaluation
			updateStage.setString(1, requestID);
			
			updateStage.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateQuery);
			stmt.setString(1, requestID);
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the additional info required by the committee and returns it
	 * @param message data
	 * @return data
	 */
	public MessageObject getAdditionalInfo(MessageObject message) {
		ResultSet rs;
		String requestID = (String) message.getArgs().get(0);
		String query = "SELECT additionalInfo FROM AdditionalInfo WHERE requestID = ?";
		String previousReport = "SELECT * FROM EvaluationReport WHERE RequestID = ?";
		MessageObject response = new MessageObject(RequestType.ShowAdditionalInfo, new ArrayList<>());
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			// Get Additional Info
			stmt.setString(1, requestID);
			rs = stmt.executeQuery();
			
			if (!rs.next())
				response.getArgs().add("");
			else {
				response.getArgs().add(rs.getString("additionalInfo"));
				stmt = mysqlConnection.getInstance().getConnection().prepareStatement(previousReport);
				stmt.setString(1, requestID);
				rs = stmt.executeQuery();
				
				if (rs.next()) {
					response.getArgs().add(rs.getString("Description"));
					response.getArgs().add(rs.getString("Constraints"));
					response.getArgs().add(rs.getString("Result"));
				}
			}
			
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method swaps between gives stages
	 * @param message data
	 */
	public void swapStage(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String newStage = (String) message.getArgs().get(1);
		String currentStage = (String) message.getArgs().get(2);
		String updateStageInRequests ="UPDATE Requests SET Stage = ? WHERE RequestID = ?";
		String insertStageToStageTable = "INSERT INTO StageTable (requestId, stage, startTime, endTime, deadlineTime) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE startTime = ?, endTime = ?";
		String insertCurrentStageToStageTableLog = "INSERT INTO StageTableLog (requestId, stage, startTime, endTime, deadlineTime) SELECT * FROM StageTable WHERE requestId = ? AND stage = ?";
		String updateStageTableLogEndDate = "UPDATE StageTableLog SET endTime = ? WHERE requestId = ? AND stage = ? AND endTime IS NULL";
		String updateStageToStageTable = "UPDATE StageTable SET endTime = ? WHERE requestId = ? AND stage = ?";
		
		System.out.println(newStage + " : " + currentStage);
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageInRequests);
			stmt.setString(1, newStage);
			stmt.setString(2, requestID);
			stmt.executeUpdate();
			
			executeStage_Status_Swap(requestID, newStage, currentStage, insertStageToStageTable,
					insertCurrentStageToStageTableLog, updateStageTableLogEndDate);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageToStageTable);
			
			java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			
			stmt.setString(1, ourJavaDateObject.toString());
			stmt.setString(2, requestID);
			stmt.setString(3, currentStage);
			stmt.executeUpdate();

			return;
		} catch (Exception e) { e.printStackTrace(); }
		return;
	}

	/**
	 * This method swaps between gives statuses
	 * @param message data
	 */
	public void swapStatus(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String newStatus = (String) message.getArgs().get(1);
		String currentStatus = (String) message.getArgs().get(2);
		String updateStageInRequests ="UPDATE Requests SET Status = ? WHERE RequestID = ?";
		String insertStatusToStatusTable = "INSERT INTO StatusTable (requestId, status, startTime, endTime) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE startTime = ?, endTime = ?";
		String insertCurrentStatusToStatusTableLog = "INSERT INTO StatusTableLog (requestId, status, startTime, endTime) SELECT * FROM StatusTable WHERE requestId = ? AND status = ?";
		String updateStatusTableLogEndDate = "UPDATE StatusTableLog SET endTime = ? WHERE requestId = ? AND status = ? AND endTime IS NULL";
		
		// For Closing/Closed Status and Status
		String updateStatusToStatusTableClosing = "UPDATE StatusTable SET endTime = ? WHERE requestId = ? AND status = ?";
		String insertCurrentStatusToStatusTableLogClosing = "INSERT INTO StatusTableLog (requestId, status, startTime, endTime) SELECT * FROM StatusTable WHERE requestId = ? AND status = ?";
		String updateStatusTableLogEndDateClosing = "UPDATE StatusTableLog SET endTime = ? WHERE requestId = ? AND status = ? AND endTime IS NULL";
		
		String updateStageToStageTable = "UPDATE StageTable SET endTime = ? WHERE requestId = ? AND stage = ?";
		String insertCurrentStageToStageTableLog = "INSERT INTO StageTableLog (requestId, stage, startTime, endTime, deadlineTime) SELECT * FROM StageTable WHERE requestId = ? AND stage = ?";
		String updateStageTableLogEndDate = "UPDATE StageTableLog SET endTime = ? WHERE requestId = ? AND stage = ? AND endTime IS NULL";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageInRequests);
			stmt.setString(1, newStatus);
			stmt.setString(2, requestID);
			stmt.executeUpdate();
			
			executeStage_Status_Swap(requestID, newStatus, currentStatus, insertStatusToStatusTable,
					insertCurrentStatusToStatusTableLog, updateStatusTableLogEndDate);
			if (!newStatus.equals("Closed")) return;
			
			executeClosingPhaseUpdates(requestID, updateStatusToStatusTableClosing,
					insertCurrentStatusToStatusTableLogClosing, updateStatusTableLogEndDateClosing,
					updateStageToStageTable, insertCurrentStageToStageTableLog, updateStageTableLogEndDate);

			return;
		} catch (Exception e) { e.printStackTrace(); }
		return;
	}

	/**
	 * This method executes the closing phase updates
	 * @param requestID id
	 * @param updateStatusToStatusTableClosing query
	 * @param insertCurrentStatusToStatusTableLogClosing query
	 * @param updateStatusTableLogEndDateClosing query
	 * @param updateStageToStageTable query
	 * @param insertCurrentStageToStageTableLog query
	 * @param updateStageTableLogEndDate query
	 * @throws SQLException exception
	 */
	private void executeClosingPhaseUpdates(String requestID, String updateStatusToStatusTableClosing,
			String insertCurrentStatusToStatusTableLogClosing, String updateStatusTableLogEndDateClosing,
			String updateStageToStageTable, String insertCurrentStageToStageTableLog, String updateStageTableLogEndDate)
			throws SQLException {
		PreparedStatement stmt;
		
		String status = "Closed", stage = "Closing";
		java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		
		updateClosingPhase(requestID, updateStatusToStatusTableClosing, insertCurrentStatusToStatusTableLogClosing,
				updateStatusTableLogEndDateClosing, status, ourJavaDateObject);
		
		updateClosingPhase(requestID, updateStageToStageTable, insertCurrentStageToStageTableLog,
				updateStageTableLogEndDate, stage, ourJavaDateObject);
	}

	/**
	 * This method updates the closing phase of the 
	 * @param requestID id
	 * @param updateStatusToStatusTableClosing query
	 * @param insertCurrentStatusToStatusTableLogClosing query
	 * @param updateStatusTableLogEndDateClosing query
	 * @param status status
	 * @param ourJavaDateObject date
	 * @throws SQLException exception
	 */
	private void updateClosingPhase(String requestID, String updateStatusToStatusTableClosing,
			String insertCurrentStatusToStatusTableLogClosing, String updateStatusTableLogEndDateClosing, String status,
			java.sql.Date ourJavaDateObject) throws SQLException {
		PreparedStatement stmt;
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStatusToStatusTableClosing);
		
		stmt.setString(1, ourJavaDateObject.toString());
		stmt.setString(2, requestID);
		stmt.setString(3, status);
		stmt.executeUpdate();
		
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertCurrentStatusToStatusTableLogClosing);
		
		stmt.setString(1, requestID);
		stmt.setString(2, status);
		stmt.executeUpdate();
		
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStatusTableLogEndDateClosing);
		
		stmt.setString(1, ourJavaDateObject.toString());
		stmt.setString(2, requestID);
		stmt.setString(3, status);
		stmt.executeUpdate();
	}
	
	/** This method swaps between the given stages / statuses
	 * 
	 * @param requestID id
	 * @param newStage stage
	 * @param currentStage stage
	 * @param insertStageToStageTable query
	 * @param insertCurrentStageToStageTableLog query
	 * @param updateStageTableLogEndDate query
	 * @throws SQLException exception
	 */
	private void executeStage_Status_Swap(String requestID, String newStage, String currentStage,
			String insertStageToStageTable, String insertCurrentStageToStageTableLog, String updateStageTableLogEndDate)
			throws SQLException {
		PreparedStatement stmt;
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertStageToStageTable);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate nowLocalDate = LocalDate.parse(LocalDate.now().format(dtf), dtf);
		java.sql.Date ourJavaDateObject = java.sql.Date.valueOf(LocalDate.parse(dtf.format(nowLocalDate), dtf));
		
		// AUTO TIMED STAGES:
		if (insertStageToStageTable.contains("StatusTable")) {
			stmt.setString(1, requestID);
			stmt.setString(2, newStage);
			stmt.setDate(3, ourJavaDateObject);
			stmt.setDate(4, null);
			stmt.setDate(5, ourJavaDateObject);
			stmt.setDate(6, null);
			stmt.executeUpdate();
		} else {
			java.sql.Date deadlineTime = null;
			LocalDate now = LocalDate.parse(LocalDate.now().format(dtf), dtf);
			LocalDate seven_days_later_ldt = LocalDate.parse(dtf.format(LocalDate.now().plusDays(7)), dtf);
			switch (newStage) {
			case "Decision":
			case "Testing":
				deadlineTime = java.sql.Date.valueOf(seven_days_later_ldt);
				break;
			case "Closing":
				deadlineTime = java.sql.Date.valueOf(now);
				break;
			}
			stmt.setString(1, requestID);
			stmt.setString(2, newStage);
			stmt.setDate(3, ourJavaDateObject);
			stmt.setDate(4, null);
			stmt.setDate(5, deadlineTime);
			stmt.setDate(6, ourJavaDateObject);
			stmt.setDate(7, null);
			stmt.executeUpdate();
		}

		
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertCurrentStageToStageTableLog);
		
		stmt.setString(1, requestID);
		stmt.setString(2, currentStage);
		stmt.executeUpdate();
		
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageTableLogEndDate);
		
		stmt.setString(1, ourJavaDateObject.toString());
		stmt.setString(2, requestID);
		stmt.setString(3, currentStage);
		stmt.executeUpdate();
	}

	/** This method gets test rejection info
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getTestRejectionInfo(MessageObject message) {
		ResultSet rs;
		String requestID = (String) message.getArgs().get(0);
		String query = "SELECT additionalInfo FROM TestRejectionInfo WHERE requestID = ?";

		MessageObject response = new MessageObject(RequestType.TestRejectionInfo, new ArrayList<>());
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			// Get Additional Info
			stmt.setString(1, requestID);
			rs = stmt.executeQuery();
			
			if (!rs.next())
				response.getArgs().add("");
			else
				response.getArgs().add(rs.getString("additionalInfo"));
			
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method updates test rejection info
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject updateTestRejectionInfo(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String additionalInfo = (String) message.getArgs().get(1);
		String query = "INSERT INTO TestRejectionInfo VALUES (?,?) ON DUPLICATE KEY UPDATE additionalInfo = ?";
		String updateQuery = "UPDATE StageTable SET deadlineTime = NULL WHERE requestId = ? AND Stage = 'Execution'";
		

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			// Update into table the Additional Info Required
			stmt.setString(1, requestID);
			stmt.setString(2, additionalInfo);
			stmt.setString(3, additionalInfo);
			
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateQuery);
			stmt.setString(1, requestID);
			stmt.executeUpdate();
			
			message.getArgs().clear();
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method gets the time extension table rows
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getTimeExtensionTable(MessageObject message) {
		ResultSet rs;
		String query = "SELECT * FROM TimeExtension";

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String requestID = rs.getString("RequestID");
				String userID = rs.getString("UserID");
				String jobDescription = rs.getString("JobDescription");
				String startTime = rs.getString("StartTime");
				String endTime = rs.getString("EndTime");
				String Stage = rs.getString("Stage");

				ArrayList<String> row = new ArrayList<>();
				row.add(requestID);
				row.add(userID);
				row.add(jobDescription);
				row.add(startTime);
				row.add(endTime);
				row.add(Stage);
				message.getArgs().add(row);
			}
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method gets the time assessment table rows
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getTimeAssessmentTable(MessageObject message) {
		ResultSet rs;
		String query = "SELECT * FROM TimeAssessment";

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String requestID = rs.getString("RequestID");
				String userID = rs.getString("UserID");
				String jobDescription = rs.getString("JobDescription");
				String startTime = rs.getString("StartTime");
				String endTime = rs.getString("EndTime");
				
				ArrayList<String> row = new ArrayList<>();
				row.add(requestID);
				row.add(userID);
				row.add(jobDescription);
				row.add(startTime);
				row.add(endTime);
				message.getArgs().add(row);
			}
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method gets the execution leader table rows
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getExecutionLeaderTable(MessageObject message) {
		ResultSet rs;
		String query = "SELECT * FROM ExecutionLeaderAppointment";

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String requestID = rs.getString("RequestID");
				String startTime = rs.getString("StartTime");
				
				ArrayList<String> row = new ArrayList<>();
				row.add(requestID);
				row.add(startTime);
				message.getArgs().add(row);
			}
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** This method gets the evaluator table rows
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getEvaluatorTable(MessageObject message) {
		ResultSet rs;
		String query = "SELECT * FROM EvaluatorAppointment";

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String requestID = rs.getString("RequestID");
				String evaluatorID = rs.getString("EvaluatorID");
				String evaluatorName = rs.getString("EvaluatorName");
				
				ArrayList<String> row = new ArrayList<>();
				row.add(requestID);
				row.add(evaluatorID);
				row.add(evaluatorName);
				message.getArgs().add(row);
			}
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method handles a new time assessment acceptance
	 * 
	 * @param message data
	 */
	public void timeAssessmentAccepted(MessageObject message) {
		String addQuery = "INSERT INTO TimeAssessmentLog (RequestID, UserID, JobDescription, StartTime, EndTime) SELECT * FROM TimeAssessment WHERE RequestID = ?";
		String deleteQuery = "DELETE FROM TimeAssessment WHERE RequestID = ?";
		String setQuery = "UPDATE StageTable SET deadlineTime = ? WHERE requestId = ? AND deadlineTime IS NULL";
		
		PreparedStatement stmt;
		try {
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate deadlineLocalDate = LocalDate.parse(message.getArgs().get(1).toString(), dtf);
			java.sql.Date deadline = java.sql.Date.valueOf(LocalDate.parse(dtf.format(deadlineLocalDate), dtf));
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setDate(1, deadline);
			stmt.setString(2, message.getArgs().get(0).toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** This method handles a new time assessment rejection
	 * 
	 * @param message data
	 */
	public void timeAssessmentRejected(MessageObject message) {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeAssessmentRejectionInfo VALUES (?,?,?) ON DUPLICATE KEY UPDATE RejectionInfo = ?";
		String deleteQuery = "DELETE FROM TimeAssessment WHERE RequestID = ?";
		
		PreparedStatement stmt;
		try {
			
			String UserID = message.getArgs().get(2).toString();
			String RejectionInfo = message.getArgs().get(1).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.setString(2, UserID);
			stmt.setString(3, RejectionInfo);
			stmt.setString(4, RejectionInfo);
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			MailSender.getInstance().send(UserID, "TIME ASSESSMENT REJECTION", "Your time assessment request for Request ID " + message.getArgs().get(0).toString() + " has been rejected. Reason: " + RejectionInfo, DBServer.getInstance().isPopup());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** This method handles a new time extension acceptance
	 * 
	 * @param message data
	 */
	public void timeExtensionAccepted(MessageObject message) {
		String addQuery = "INSERT INTO TimeExtensionLog (RequestID, UserID, JobDescription, StartTime, EndTime, Stage) SELECT * FROM TimeExtension WHERE RequestID = ?";
		String deleteQuery = "DELETE FROM TimeExtension WHERE RequestID = ?";
		String setQuery = "UPDATE StageTable SET deadlineTime = ? WHERE requestId = ? AND stage = ?";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate deadlineLocalDate = LocalDate.parse(message.getArgs().get(1).toString(), dtf);
			java.sql.Date deadline = java.sql.Date.valueOf(LocalDate.parse(dtf.format(deadlineLocalDate), dtf));
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setDate(1, deadline);
			stmt.setString(2, message.getArgs().get(0).toString());
			stmt.setString(3, message.getArgs().get(2).toString());
			stmt.executeUpdate();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** This method handles a new time extension rejection
	 * 
	 * @param message data
	 */
	public void timeExtensionRejected(MessageObject message) {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeExtensionRejectionInfo VALUES (?,?,?) ON DUPLICATE KEY UPDATE RejectionInfo = ?";
		String deleteQuery = "DELETE FROM TimeExtension WHERE RequestID = ?";
		
		PreparedStatement stmt;
		try {
			
			String RequestID = message.getArgs().get(0).toString();
			String UserID = message.getArgs().get(1).toString();
			String RejectionInfo = message.getArgs().get(2).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, RejectionInfo);
			stmt.setString(4, RejectionInfo);
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
			MailSender.getInstance().send(UserID, "TIME EXTENSION REJECTION", "Your time assessment request for Request ID " + message.getArgs().get(0).toString() + " has been rejected. Reason: " + RejectionInfo, DBServer.getInstance().isPopup());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** This method creates a new time extension and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @throws ParseException exception
	 * @return data
	 */
	public MessageObject NewTimeExtension(MessageObject message) throws ParseException {
		ResultSet rs;
		String getQuery = "SELECT * FROM StageTable WHERE requestId = ? AND stage = ?";
		String addQuery = "INSERT INTO TimeExtension VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE UserID = ?, JobDescription = ?, StartTime = ?, EndTime = ?, Stage = ?";
		String getISDQuery = "SELECT PermanentEmployee.userID, Users.Email FROM PermanentEmployee "
				+ "INNER JOIN Users ON PermanentEmployee.userID = Users.UserID WHERE jobDescription = 'ISD Chief'";
		
		PreparedStatement stmt;
		try {
			// get query
			String RequestID = message.getArgs().get(0).toString();
			String UserID = message.getArgs().get(1).toString();
			String JobDescription = message.getArgs().get(2).toString();
			String Stage = message.getArgs().get(3).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, Stage);
			rs = stmt.executeQuery();
			rs.next();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.sql.Date StartTime = java.sql.Date.valueOf(LocalDate.parse(dtf.format(LocalDate.parse(rs.getString("startTime"), dtf))));
			LocalDate EndDateLocalDate = LocalDate.parse(rs.getString("deadlineTime"), dtf);
			EndDateLocalDate = EndDateLocalDate.plusDays(Integer.parseInt(message.getArgs().get(4).toString()));
			java.sql.Date EndTime = java.sql.Date.valueOf(LocalDate.parse(dtf.format(EndDateLocalDate), dtf));
			// add query
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, JobDescription);
			stmt.setDate(4, StartTime);
			stmt.setDate(5, EndTime);
			stmt.setString(6, Stage);
			stmt.setString(7, UserID);
			stmt.setString(8, JobDescription);
			stmt.setDate(9, StartTime);
			stmt.setDate(10, EndTime);
			stmt.setString(11, Stage);
			
			stmt.executeUpdate();
			
			message.getArgs().clear();
			message.getArgs().add(true);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getISDQuery);
			rs = stmt.executeQuery();
			rs.next();
			
			String subject = "EXTENSION REQUESTED - ICM Request ID " + RequestID + " " + Stage;
			String content = "Your request is currently at stage of " + Stage + 
					",\nand an extension was requested today: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MMMM yyyy")) + ".";
			MailSender.getInstance().send(rs.getString("Email"), subject, content, DBServer.getInstance().isPopup());
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.getArgs().clear();
			message.getArgs().add(false);
			
			return message;
		}
	}

	/** This method creates a new time assessment and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @throws ParseException exception
	 * @return data
	 */
	public MessageObject NewTimeAssessment(MessageObject message) throws ParseException {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeAssessment VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE UserID = ?, JobDescription = ?, StartTime = ?, EndTime = ?";
		PreparedStatement stmt;
		try {
			String RequestID = message.getArgs().get(0).toString();
			String UserID = message.getArgs().get(1).toString();
			String JobDescription = message.getArgs().get(2).toString();
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate ldt = LocalDate.parse(LocalDate.now().format(dtf), dtf);
			
			java.sql.Date StartTime = java.sql.Date.valueOf(LocalDate.parse(dtf.format(ldt), dtf)); // NOW
			LocalDate endTimeLocalDate = ldt.plusDays(Integer.parseInt(message.getArgs().get(3).toString()));
			java.sql.Date EndTime = java.sql.Date.valueOf(LocalDate.parse(dtf.format(endTimeLocalDate), dtf));
			
			// add query
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, JobDescription);
			stmt.setDate(4, StartTime);
			stmt.setDate(5, EndTime);
			stmt.setString(6, UserID);
			stmt.setString(7, JobDescription);
			stmt.setDate(8, StartTime);
			stmt.setDate(9, EndTime);
			stmt.executeUpdate();
			message.getArgs().clear();
			message.getArgs().add(true);
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.getArgs().clear();
			message.getArgs().add(false);
			
			return message;
		}
	}
	
	/** Was time assessment accepted for this request stage
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getStageDeadline(MessageObject message) {
		ResultSet rs;
		String getQuery = "SELECT * FROM StageTable WHERE requestId = ? AND stage = ? AND deadlineTime IS NOT NULL";
		String alreadySentAssessmentQuery = "SELECT * FROM TimeAssessment WHERE RequestID = ?";
		String alreadySentExtensionQuery = "SELECT * FROM TimeExtension WHERE RequestID = ? AND Stage = ?";
		
		PreparedStatement stmt;
		try {
			// get query
			String RequestID = message.getArgs().get(0).toString();
			String Stage = message.getArgs().get(1).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, Stage);
			
			rs = stmt.executeQuery();
			message.getArgs().clear();
			if (rs.next()) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate deadlineLocalDate = LocalDate.parse(rs.getString("deadlineTime"), dtf);
				 message.getArgs().add(dtf.format(deadlineLocalDate));
			}
			else message.getArgs().add(null);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(alreadySentAssessmentQuery);
			stmt.setString(1, RequestID);
			
			rs = stmt.executeQuery();
			if (rs.next())
				 message.getArgs().add(true);
			else message.getArgs().add(false);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(alreadySentExtensionQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, Stage);
			
			rs = stmt.executeQuery();
			if (rs.next())
				 message.getArgs().add(true);
			else message.getArgs().add(false);
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.getArgs().clear();
			message.getArgs().add(null);
			
			return message;
		}
	}

	/** This method gets the committee details and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getCommittee(MessageObject message) {
		ResultSet get_rs, name_rs;
		String getQuery = "SELECT userID FROM PermanentEmployee WHERE jobDescription = 'Committee Chairman' OR jobDescription = 'Committee Member'";
		String nameQuery = "SELECT Name FROM Users WHERE UserID = ?";
		
		PreparedStatement stmt;
		try {
			// get query
			message.getArgs().clear();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			get_rs = stmt.executeQuery();
			
			ArrayList<String> args = new ArrayList<String>();
			while (get_rs.next()) {
				String id = get_rs.getString("userID");
				String name = "";
				stmt = mysqlConnection.getInstance().getConnection().prepareStatement(nameQuery);
				stmt.setString(1, id);
				name_rs = stmt.executeQuery();
				name_rs.next();
				name = name_rs.getString("Name");
				args.add(id);
				args.add(name);
			}
			message.getArgs().add(args);
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** This method sets a tester to the request returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject setTester(MessageObject message) {
		String setQuery = "UPDATE Requests SET TesterID = ?, adhoc = TRUE WHERE RequestID = ?";
		PreparedStatement stmt;
		try {
			String requestID = message.getArgs().get(0).toString();
			String testerID = message.getArgs().get(1).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, testerID);
			stmt.setString(2, requestID);
			stmt.executeUpdate();
			
			message.getArgs().clear();
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method gets the 'waits for execution leader appointment' state and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getWaitsExecutionLeaderAppointment(MessageObject message) {
		ResultSet rs;
		String setQuery = "SELECT * FROM ExecutionLeaderAppointment WHERE RequestID = ?";
		PreparedStatement stmt;
		try {
			String requestID = message.getArgs().get(0).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, requestID);
			rs = stmt.executeQuery();
			
			message.getArgs().clear();
			if (rs.next())
				 message.getArgs().add(true);
			else message.getArgs().add(false);
			
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** This method sets the 'waits for execution leader appointment' state and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject setWaitsExecutionLeaderAppointment(MessageObject message) {
		String setQuery = "INSERT INTO ExecutionLeaderAppointment VALUES (?,?) ON DUPLICATE KEY UPDATE StartTime = ?";
		PreparedStatement stmt;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate now = LocalDate.now();
			String requestID = message.getArgs().get(0).toString();
			String StartTime = dtf.format(now);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, requestID);
			stmt.setString(2, StartTime);
			stmt.setString(3, StartTime);
			stmt.executeUpdate();
			
			message.getArgs().clear();
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** This method gets the options for execution leaders and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getExecutionLeaderOptions(MessageObject message) {
		ResultSet get_rs, name_rs;
		String setQuery = "SELECT * FROM Users WHERE userType = 'ISE' AND UserID NOT IN (SELECT userID FROM PermanentEmployee)";
		String nameQuery = "SELECT Name FROM Users WHERE UserID = ?";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			get_rs = stmt.executeQuery();
			
			message.getArgs().clear();
			ArrayList<String> args = new ArrayList<>();
			while (get_rs.next()) {
				String id = get_rs.getString("UserID");
				String name = "";
				stmt = mysqlConnection.getInstance().getConnection().prepareStatement(nameQuery);
				stmt.setString(1, id);
				name_rs = stmt.executeQuery();
				name_rs.next();
				name = name_rs.getString("Name");
				args.add(id);
				args.add(name);
			}
			message.getArgs().add(args);
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** This method sets the execution leader and returns an appropriate messageObject
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject setExecutionLeader(MessageObject message) {
		String setQuery = "UPDATE Requests SET ExecutionLeaderID = ? WHERE RequestID = ?";
		String deleteQuery = "DELETE FROM ExecutionLeaderAppointment WHERE RequestID = ?";
		PreparedStatement stmt;
		try {
			String requestID = message.getArgs().get(0).toString();
			String executionLeaderID = message.getArgs().get(1).toString();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, executionLeaderID);
			stmt.setString(2, requestID);
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, requestID);
			stmt.executeUpdate();
			
			message.getArgs().clear();
			return message;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method sets the chairman to initiate Testing stage
	 * @param message data
	 */
	public void setTesterBeforeAdhoc(MessageObject message) {
		ResultSet rs;
		String getQuery = "SELECT userID FROM PermanentEmployee WHERE jobDescription = 'Committee Chairman'";
		String setQuery = "UPDATE Requests SET TesterID = ? WHERE RequestID = ?";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			rs = stmt.executeQuery();
			rs.next();
			
			String InitialTesterID = rs.getString("userID");
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, InitialTesterID);
			stmt.setString(2, message.getArgs().get(0).toString());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the Employee ISE Table
	 * @param message data
	 * @return data
	 */
	public MessageObject getEmployeeTable(MessageObject message) {
		ResultSet rs;
		String getQuery = "SELECT UserID, Name, Email FROM Users WHERE userType = 'ISE'";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			rs = stmt.executeQuery();
			
			ArrayList<Employee> args = new ArrayList<>();
			while (rs.next()) {
				String UserID = rs.getString("UserID");
				String Name = rs.getString("Name");
				String Email = rs.getString("Email");
				Employee e = new Employee(UserID, Name, Email);
				args.add(e);
			}
			message.getArgs().add(args);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the current request table
	 * @param message data
	 * @return data
	 */
	public MessageObject getRequestTable(MessageObject message) {
		ResultSet rs;
		String getQuery = "SELECT * FROM Requests";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			rs = stmt.executeQuery();
			
			ArrayList<Request> args = new ArrayList<>();
			while (rs.next()) {
				Request e = new Request(rs);
				args.add(e);
			}
			message.getArgs().add(args);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method gets the report status data.
	 * @param message data
	 * @return data
	 */
	public MessageObject getReportStatusData(MessageObject message) {
		ResultSet rs;
		String getStatusLogQuery = "(SELECT requestId, status, startTime, endTime FROM StatusTableLog WHERE status=?) "
				+ "UNION (SELECT * FROM StatusTable WHERE status = ? AND endTime IS NULL AND requestId NOT IN "
				+ "(SELECT requestId FROM StatusTableLog WHERE status = ?))";
		
		PreparedStatement stmt;
		try {
			String status = message.getArgs().get(0).toString();
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getStatusLogQuery);
			stmt.setString(1, status);
			stmt.setString(2, status);
			stmt.setString(3, status);
			rs = stmt.executeQuery();
			
			HashMap<String, LocalDate> startTime = new HashMap<>();
			HashMap<String, LocalDate> endTime = new HashMap<>();
			while (rs.next()) {
				startTime.put(rs.getString("requestId"), rs.getDate("startTime").toLocalDate());
				if (rs.getDate("endTime") == null)
					 endTime.put(rs.getString("requestId"), null);
				else endTime.put(rs.getString("requestId"), rs.getDate("endTime").toLocalDate());
			}
			
			message.getArgs().add(startTime);
			message.getArgs().add(endTime);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the rejection report data.
	 * @param message data
	 * @return data
	 */
	public MessageObject getReportRejectedData(MessageObject message) {
		ResultSet rs1, rs2;
		String getRejectedLogQuery = "SELECT requestId, startTime, endTime FROM StageTable WHERE stage = 'Closing'";
		String getRejectedNoExecutionQuery = "SELECT * FROM StageTable WHERE NOT EXISTS (SELECT * FROM StageTable WHERE stage = 'Execution' AND requestId = ?)";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getRejectedLogQuery);
			rs1 = stmt.executeQuery();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getRejectedNoExecutionQuery);
			
			
			HashMap<String, LocalDate> startTime = new HashMap<>();
			HashMap<String, LocalDate> endTime = new HashMap<>();
			while (rs1.next()) {
				String requestId = rs1.getString("requestId");
				stmt.setString(1, requestId);
				rs2 = stmt.executeQuery();
				if (!rs2.next()) continue;
					
				startTime.put(rs1.getString("requestId"), rs1.getDate("startTime").toLocalDate());
				if (rs1.getDate("endTime") == null)
					 endTime.put(rs1.getString("requestId"), rs1.getDate("startTime").toLocalDate());
				else endTime.put(rs1.getString("requestId"), rs1.getDate("endTime").toLocalDate());
			}
			
			message.getArgs().add(startTime);
			message.getArgs().add(endTime);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method gets the extensions report data.
	 * @param message data
	 * @return data
	 */
	public MessageObject getReportExtensionsData(MessageObject message) {
		ResultSet rs;
		String getExtensionLogQuery = "SELECT * FROM TimeExtensionLog";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getExtensionLogQuery);
			rs = stmt.executeQuery();
			
			HashMap<String, LocalDate> startTime = new HashMap<>();
			HashMap<String, LocalDate> endTime = new HashMap<>();
			while (rs.next()) {
				startTime.put(rs.getString("RequestID"), rs.getDate("StartTime").toLocalDate().plusDays(1));
				endTime.put(rs.getString("RequestID"), rs.getDate("EndTime").toLocalDate().plusDays(1));
			}
			
			message.getArgs().add(startTime);
			message.getArgs().add(endTime);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the durations report data.
	 * @param message data
	 * @return data
	 */
	public MessageObject getReportDurationsData(MessageObject message) {
		ResultSet rs_finalEndTime, rs_firstAssessments;
		String getDurationQuery = "SELECT * FROM StageTable WHERE endTime IS NOT NULL AND (stage = 'Evaluation' OR stage = 'Execution')";
		String getTimeAssessmentsLogQuery = "SELECT * FROM TimeAssessmentLog WHERE rowIndex IN (" + 
				"SELECT MIN(rowIndex) AS rowIndex FROM TimeAssessmentLog GROUP BY RequestID, JobDescription)";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getDurationQuery);
			rs_finalEndTime = stmt.executeQuery();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getTimeAssessmentsLogQuery);
			rs_firstAssessments = stmt.executeQuery();
			
			String requestID = "", stage = "";
			LocalDate endTime = null;
			HashMap<String, LocalDate> evaluationFinalEndTimes = new HashMap<>();
			HashMap<String, LocalDate> executionFinalEndTimes = new HashMap<>();
			while (rs_finalEndTime.next()) {
				requestID = rs_finalEndTime.getString("RequestID");
				stage = rs_finalEndTime.getString("stage");
				endTime = rs_finalEndTime.getDate("EndTime").toLocalDate().plusDays(1);
				
				if (stage.equals("Evaluation"))
					 evaluationFinalEndTimes.put(requestID, endTime);
				else executionFinalEndTimes.put(requestID, endTime);
			}
			
			HashMap<String, LocalDate> evaluationAssessmentEndTimes = new HashMap<>();
			HashMap<String, LocalDate> executionAssessmentEndTimes = new HashMap<>();
			while (rs_firstAssessments.next()) {
				requestID = rs_firstAssessments.getString("RequestID");
				stage = rs_firstAssessments.getString("JobDescription");
				endTime = rs_firstAssessments.getDate("EndTime").toLocalDate().plusDays(1);
				
				if (stage.equals("Evaluator"))
					 evaluationAssessmentEndTimes.put(requestID, endTime);
				else executionAssessmentEndTimes.put(requestID, endTime);
			}
			
			message.getArgs().add(executionAssessmentEndTimes);
			message.getArgs().add(executionFinalEndTimes);
			message.getArgs().add(evaluationAssessmentEndTimes);
			message.getArgs().add(evaluationFinalEndTimes);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method gets the delays report data.
	 * @param message data
	 * @return data
	 */
	public MessageObject getReportDelaysData(MessageObject message) {
		ResultSet rs;
		Object informationSystem = message.getArgs().get(0);
		String getDelayLogQuery = "SELECT deadlineTime, endTime FROM StageTable WHERE endTime IS NOT NULL AND deadlineTime IS NOT NULL";
		if (!informationSystem.toString().equalsIgnoreCase("All"))
			 getDelayLogQuery += " AND requestId IN (SELECT RequestID FROM Requests WHERE InformationSystem = ?)";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getDelayLogQuery);
			if (!informationSystem.toString().equalsIgnoreCase("All"))
				stmt.setString(1, informationSystem.toString());
			rs = stmt.executeQuery();
			
			Integer key = 0;
			HashMap<String, LocalDate> deadlineTime = new HashMap<>();
			HashMap<String, LocalDate> endTime = new HashMap<>();
			while (rs.next()) {
				LocalDate deadlineDate = rs.getDate("deadlineTime").toLocalDate().plusDays(1);
				LocalDate endDate = rs.getDate("endTime").toLocalDate().plusDays(1);
				if (deadlineDate.isAfter(endDate)) continue;
				
				deadlineTime.put(key.toString(), deadlineDate);
				endTime.put(key.toString(), endDate);
				key++;
			}
			message.getArgs().clear();
			message.getArgs().add(deadlineTime);
			message.getArgs().add(endTime);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets mail string by id
	 * @param id id
	 * @return mail
	 */
	public String getMailByUID(String id) {
		ResultSet rs;
		String getQuery = "SELECT * FROM Users WHERE UserID = ?";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getQuery);
			stmt.setString(1, id);
			rs = stmt.executeQuery();

			if (rs.next())
				 return rs.getString("Email");
			else return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method sends mails for one day before stage end.
	 */
	public void SendMailsOneDayBeforeStageEnd() {
		ResultSet rs;
		String getStagesQuery = "SELECT Users.UserID, Users.Email, Requests.RequestID, Requests.Stage, StageTable.deadlineTime "
				+ "FROM Requests INNER JOIN StageTable ON Requests.RequestID = StageTable.requestId "
				+ "AND Requests.Stage = StageTable.stage INNER JOIN Users ON "
				+ "IF(Requests.Stage = 'Evaluation', Requests.EvaluatorID = Users.UserID, "
				+ "IF (Requests.Stage = 'Execution', Requests.ExecutionLeaderID = Users.UserID, "
				+ "IF (Requests.Stage = 'Testing', Requests.TesterID = Users.UserID, Requests.TesterID = Users.UserID))) "
				+ "WHERE StageTable.endTime IS NULL AND StageTable.deadlineTime IS NOT NULL";

		String getPermanent = "SELECT PermanentEmployee.userID, PermanentEmployee.jobDescription, Users.Email "
				+ "FROM PermanentEmployee INNER JOIN Users ON PermanentEmployee.userID = Users.UserID "
				+ "WHERE jobDescription = 'Committee Chairman' OR jobDescription = 'Supervisor'";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getPermanent);
			rs = stmt.executeQuery();
			
			String supervisor = "", supervisorMail = "", chairman = "", chairmanMail = "";
			while (rs.next()) {
				if (rs.getString("jobDescription").equals("Supervisor")) {
					 supervisor = rs.getString("userID");
					 supervisorMail = rs.getString("Email");
				}
				else {
					chairman = rs.getString("userID");
					chairmanMail = rs.getString("Email");
				}
			}
			if (supervisorMail == null || chairmanMail == null) return;
			
			// Get Data
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getStagesQuery);
			rs = stmt.executeQuery();
			String requestID = "", stage = "", email = "";
			LocalDate now = LocalDate.now(), deadline;
			// Process Data
			while (rs.next()) {
				deadline = rs.getDate("deadlineTime").toLocalDate();
				System.out.println(now);
				System.out.println(deadline);
				if (now.isEqual(deadline)) {
					requestID = rs.getString("RequestID");
					stage = rs.getString("Stage");
					email = rs.getString("Email");
					
					if (stage.equals("Closing"))
						email = supervisorMail;
					if (stage.equals("Decision"))
						email = chairmanMail;
					
					// Send Mail
					MailSender.getInstance().send(email, "DEADLINE TOMMOROW - ICM Request ID " + requestID + " " + stage, 
					"Your request is currently at stage of " + stage + ",\nand its deadline is due tommorow: " + now.format(DateTimeFormatter.ofPattern("dd.MMMM yyyy")) + ".", DBServer.getInstance().isPopup());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends a mail of exception from deadline
	 */
	public void SendMailsExceptionFromDeadline() {
		ResultSet rs;
		String getStagesQuery = "SELECT Users.UserID, Users.Email, Requests.RequestID, Requests.Stage, StageTable.deadlineTime "
				+ "FROM Requests INNER JOIN StageTable ON Requests.RequestID = StageTable.requestId "
				+ "AND Requests.Stage = StageTable.stage INNER JOIN Users ON "
				+ "IF(Requests.Stage = 'Evaluation', Requests.EvaluatorID = Users.UserID, "
				+ "IF (Requests.Stage = 'Execution', Requests.ExecutionLeaderID = Users.UserID, "
				+ "IF (Requests.Stage = 'Testing', Requests.TesterID = Users.UserID, Requests.TesterID = Users.UserID))) "
				+ "WHERE StageTable.endTime IS NULL AND StageTable.deadlineTime IS NOT NULL";

		String getPermanent = "SELECT PermanentEmployee.userID, PermanentEmployee.jobDescription, Users.Email "
				+ "FROM PermanentEmployee INNER JOIN Users ON PermanentEmployee.userID = Users.UserID "
				+ "WHERE jobDescription = 'ISD Chief' OR jobDescription = 'Supervisor'";
		
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getPermanent);
			rs = stmt.executeQuery();
			
			String supervisor = "", supervisorMail = "", isd = "", isdMail = "", chairman = "", chairmanMail = "";
			while (rs.next()) {
				if (rs.getString("jobDescription").equals("Supervisor")) {
					 supervisor = rs.getString("userID");
					 supervisorMail = rs.getString("Email");
				}
				else if (rs.getString("jobDescription").equals("ISD Chief")){
					isd = rs.getString("userID");
					isdMail = rs.getString("Email");
				} else {
					chairman = rs.getString("userID");
					chairmanMail = rs.getString("Email");
				}
			}
			if (supervisorMail == null || isdMail == null || chairmanMail == null) return;
			
			// Get Data
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(getStagesQuery);
			rs = stmt.executeQuery();
			String requestID = "", stage = "", email = "";
			LocalDate now = LocalDate.now(), deadline;
			// Process Data
			while (rs.next()) {
				deadline = rs.getDate("deadlineTime").toLocalDate().plusDays(1);
				System.out.println(now);
				System.out.println(deadline);
				if (now.isEqual(deadline)) {
					requestID = rs.getString("RequestID");
					stage = rs.getString("Stage");
					email = rs.getString("Email");
					
					String title = "DEADLINE EXCEPTION - ICM Request ID " + requestID + " " + stage;
					String message = "Your request is currently at stage of " + stage + 
							",\nand its deadline has passed today: " + now.format(DateTimeFormatter.ofPattern("dd.MMMM yyyy")) + ".";
					
					if (stage.equals("Closing")) {}
					else if (stage.equals("Decision"))
						MailSender.getInstance().send(chairmanMail, title, message, DBServer.getInstance().isPopup());
					else MailSender.getInstance().send(email, title, message, DBServer.getInstance().isPopup());
					
					// Send Mail
					MailSender.getInstance().send(supervisorMail, title, message, DBServer.getInstance().isPopup());
					MailSender.getInstance().send(isdMail, title, message, DBServer.getInstance().isPopup());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method saves the report to the database.
	 * @param message data
	 */
	public void saveReport(MessageObject message) {
		String query = "INSERT INTO IssuedGraph (graphName, issueDate) VALUES (?,?)";
		String queryLog = "INSERT INTO IssuedGraphLog (issuedIndex, category, value) VALUES (?,?, ?)";
		PreparedStatement stmt;
		try {
			String name = message.getArgs().get(0).toString();
			java.sql.Date date = (java.sql.Date)message.getArgs().get(1);
			ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(2);
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			stmt.setString(1, name);
			stmt.setDate(2, date);
			stmt.executeUpdate();
			
			String issuedIndex = getLastInsertID();
			for (int i = 0; i < intervals.size(); i++) {
				stmt = mysqlConnection.getInstance().getConnection().prepareStatement(queryLog);
				stmt.setString(1, issuedIndex);
				stmt.setString(2, intervals.get(i).getCategoryName());
				stmt.setInt(3, intervals.get(i).getValue());
				stmt.executeUpdate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get Statistics Reports
	 * 
	 * @param message data
	 * @return data
	 */
	public MessageObject getReports(MessageObject message) {
		ResultSet rs, rs_log;
		String query = "SELECT * FROM IssuedGraph";
		String queryLog = "SELECT * FROM IssuedGraphLog WHERE issuedIndex = ?";
		PreparedStatement stmt;
		try {
			ArrayList<ArrayList<Interval>> allIntervals = new ArrayList<>();
			
			ArrayList<String> names = new ArrayList<>();
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				ArrayList<Interval> intervals = new ArrayList<>();
				String issuedIndex = rs.getString("rowIndex");
				stmt = mysqlConnection.getInstance().getConnection().prepareStatement(queryLog);
				stmt.setString(1, issuedIndex);
				rs_log = stmt.executeQuery();
					
				while (rs_log.next()) {
					intervals.add(new Interval(rs_log.getInt("value"), 0, null, null, rs_log.getString("category")));
				}
				names.add(rs.getString("graphName"));
				allIntervals.add(intervals);
			}
			
			
			message.getArgs().clear();
			message.getArgs().add(allIntervals);
			message.getArgs().add(names);
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
