package jdbc;

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
import java.util.Random;

import Common.EvaluatorReport;
import Common.Request;
import Common.TimeAssessmentRequest;
import Common.TimeExtensionRequest;
import Common.User;
import Common.EvalutorAppoitmentTableSerializable;
import Utilities.MessageObject;
import Utilities.RequestType;
import ocsf.server.ConnectionToClient;
import server.UserConnectivityManager;

public class SqlRequestHandler {
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
	 * 
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
				if (i == 6)
					stmt.setBoolean(i + 1, (args.get(i) != null));
				else
					stmt.setString(i + 1, args.get(i).toString());

			stmt.executeUpdate();
			System.out.println("Query of Create New Request Executed Successfully!");

			String requestID = getLastInsertID();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate now = LocalDate.now();

			insertNewEvaluatorToEvaluatorTable(args.get(10).toString(), requestID); /// insert New Evaluator to
																					/// "Evaluator
			/// Appointment Table"
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertStatusToStatusTable);
			stmt.setString(1, requestID);
			stmt.setString(2, "Active");
			stmt.setString(3, dtf.format(now));
			stmt.setString(4, null);
			stmt.setString(5, dtf.format(now));
			stmt.setString(6, null);
			stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Query of Create New Request Failed to be Executed!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Using a well-known mySQL procedure to get the latest ID after INSERT call.
	 * 
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
	 * @param userID
	 * @param password
	 * @return
	 * @throws SQLException
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
	 * @param msg
	 * @return
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
	 * @throws SQLException
	 * 
	 */
	public MessageObject viewRequestTable(MessageObject msg) throws SQLException {
		User currentUser = (User) msg.getArgs().get(0);
		MessageObject result = null;

		switch (getUserJobDescription(currentUser)) {
		case "Supervisor":
		case "ISD Chief":
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
		} catch (SQLException e) {
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
	 * @param query
	 * @return
	 */
	public ResultSet executeQuery(String query) {

		ResultSet result = null;
		Statement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().createStatement();
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
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(
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

	/**
	 * appoint evaluator by the system shoham
	 * 
	 * @deprecated
	 * @param requestID
	 * 
	 * @return
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

		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute query in automatiAppointmentEvaloeytor");
			e.printStackTrace();
		}

	}

	/**
	 * insert the current Evaluator to Evaluator Appointment Table
	 * 
	 * @param EvaluatorID
	 * @return
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete evaluator from EvaluatorAppointment because he was approved by the
	 * Supervisor
	 * 
	 * @param requestID
	 * @param evaluatrorID
	 * 
	 */
	public void deleteApprovedEvaluator(String requestID) {
		PreparedStatement stmt;

		String deleteQuery = "DELETE FROM EvaluatorAppointment WHERE RequestID = ?";
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, requestID);
			stmt.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Update the EvaluatorID in the requests table to the given evaluatrorID
	 * 
	 * @param requestID
	 * @param evaluatrorID
	 */
	public MessageObject updateRequestEvaluator(String requestID, String evaluatorID) {

		String updateEvaloeytor = "Update Requests SET EvaluatorID =?  WHERE RequestID =? ";
		String updateStageInRequests = "UPDATE Requests SET Stage = ? WHERE RequestID = ?";
		String insertStageToStageTable = "INSERT INTO StageTable (requestId, stage, startTime, endTime, deadlineTime) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE startTime = ?, endTime = ?";

		PreparedStatement stmt;
		try {
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

			java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());

			stmt.setString(1, requestID);
			stmt.setString(2, "Evaluation");
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setString(5, null);
			stmt.setString(6, ourJavaDateObject.toString());
			stmt.setString(7, null);
			stmt.executeUpdate();
			deleteApprovedEvaluator(requestID);
			return new MessageObject(RequestType.ApprovedEvaluator, new ArrayList<>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Update the stage of requests table for RequestID Insert new Evaluation Stage
	 * in StageTable in the DB
	 * 
	 * @deprecated
	 * @param requestID
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
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This methods gets the Name and Evaluator ID of Information Systems
	 * 
	 * 
	 * @throws SQLException
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
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this getInformationSystemDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the ID and Name of each User
	 * 
	 * 
	 * @throws SQLException
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
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this getAllUserDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the Details of Users with Permanent Roles (roleName, ID)
	 * 
	 * 
	 * @throws SQLException
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
		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this getPermanentRolesDetails query: ");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method updates the evaluator of a certain Information System
	 * 
	 * @param message
	 * @throws SQLException
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

		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this updateEvaluator query: ");
			e.printStackTrace();
		}
	}

	/**
	 * This method updates the permanent roles holders
	 * 
	 * @param message
	 * @throws SQLException
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

		} catch (SQLException e) {
			System.out.println("An Error occured while trying to execute this updatePermanentRoles query: ");
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the Evaluator Table and returns it
	 * 
	 * @param message
	 * @return
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;

	}

	/**
	 * This method gets the ISE Workers Table and returns it
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * this method uploads the evaluator report to the db
	 * 
	 * @param requestID
	 * @param description
	 * @param constraints
	 * @param result
	 * @param evaluatorID
	 * @return
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
		} catch (SQLException e) {
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
	 * @param requestID
	 * @param EvaluatorID
	 * @deprecated
	 * @return
	 */
	public boolean changeStageToInspectonAndDecision(String requestID, String EvaluatorID) {

		String query = "UPDATE Requests SET Stage= 'Decision' WHERE EvaluatorID = ? AND RequestID = ? ";

		try {
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

			// stmt.setNull(1, java.sql.Types.VARCHAR);
			stmt.setString(1, EvaluatorID);
			stmt.setString(2, requestID);

			stmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * getting from the DB all the information of the report
	 * 
	 * @param message ArrayList that contain the requestID of the report
	 * @return
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * This method sets an additional info to be asked by the evaluator by the
	 * committee
	 * 
	 * @param message
	 */
	public void askForAdditionalInfo(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String additionalInfo = (String) message.getArgs().get(1);
		String query = "INSERT INTO AdditionalInfo VALUES (?,?) ON DUPLICATE KEY UPDATE additionalInfo = ?";

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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the additional info required by the committee and returns it
	 * 
	 * @param message
	 * @return
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

				rs.next();
				response.getArgs().add(rs.getString("Description"));
				response.getArgs().add(rs.getString("Constraints"));
				response.getArgs().add(rs.getString("Result"));
			}

			return response;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method swaps between gives stages
	 * 
	 * @param message
	 */
	public void swapStage(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String newStage = (String) message.getArgs().get(1);
		String currentStage = (String) message.getArgs().get(2);
		String updateStageInRequests = "UPDATE Requests SET Stage = ? WHERE RequestID = ?";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method swaps between gives statuses
	 * 
	 * @param message
	 */
	public void swapStatus(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String newStatus = (String) message.getArgs().get(1);
		String currentStatus = (String) message.getArgs().get(2);
		String updateStageInRequests = "UPDATE Requests SET Status = ? WHERE RequestID = ?";
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
			if (!newStatus.equals("Closed"))
				return;

			executeClosingPhaseUpdates(requestID, updateStatusToStatusTableClosing,
					insertCurrentStatusToStatusTableLogClosing, updateStatusTableLogEndDateClosing,
					updateStageToStageTable, insertCurrentStageToStageTableLog, updateStageTableLogEndDate);

			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method executes the closing phase updates
	 * 
	 * @param requestID
	 * @param updateStatusToStatusTableClosing
	 * @param insertCurrentStatusToStatusTableLogClosing
	 * @param updateStatusTableLogEndDateClosing
	 * @param updateStageToStageTable
	 * @param insertCurrentStageToStageTableLog
	 * @param updateStageTableLogEndDate
	 * @throws SQLException
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
	 * 
	 * @param requestID
	 * @param updateStatusToStatusTableClosing
	 * @param insertCurrentStatusToStatusTableLogClosing
	 * @param updateStatusTableLogEndDateClosing
	 * @param status
	 * @param ourJavaDateObject
	 * @throws SQLException
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

		stmt = mysqlConnection.getInstance().getConnection()
				.prepareStatement(insertCurrentStatusToStatusTableLogClosing);

		stmt.setString(1, requestID);
		stmt.setString(2, status);
		stmt.executeUpdate();

		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStatusTableLogEndDateClosing);

		stmt.setString(1, ourJavaDateObject.toString());
		stmt.setString(2, requestID);
		stmt.setString(3, status);
		stmt.executeUpdate();
	}

	/**
	 * This method swaps between the given stages / statuses
	 * 
	 * @param requestID
	 * @param newStage
	 * @param currentStage
	 * @param insertStageToStageTable
	 * @param insertCurrentStageToStageTableLog
	 * @param updateStageTableLogEndDate
	 * @throws SQLException
	 */
	private void executeStage_Status_Swap(String requestID, String newStage, String currentStage,
			String insertStageToStageTable, String insertCurrentStageToStageTableLog, String updateStageTableLogEndDate)
			throws SQLException {
		PreparedStatement stmt;
		stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertStageToStageTable);

		java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());

		// AUTO TIMED STAGES:
		if (insertStageToStageTable.contains("StatusTable")) {
			stmt.setString(1, requestID);
			stmt.setString(2, newStage);
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setString(5, ourJavaDateObject.toString());
			stmt.setString(6, null);
			stmt.executeUpdate();
		} else {
			String deadlineTime = null;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate now = LocalDate.now();
			LocalDate seven_days_later_ldt = LocalDate.now().plusDays(7);
			switch (newStage) {
			case "Decision":
			case "Testing":
				deadlineTime = dtf.format(seven_days_later_ldt);
				break;
			case "Closing":
				deadlineTime = dtf.format(now);
				break;
			}
			stmt.setString(1, requestID);
			stmt.setString(2, newStage);
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setString(5, deadlineTime);
			stmt.setString(6, ourJavaDateObject.toString());
			stmt.setString(7, null);
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

	/**
	 * This method gets test rejection info
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method updates test rejection info
	 * 
	 * @param message
	 * @return
	 */
	public MessageObject updateTestRejectionInfo(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String additionalInfo = (String) message.getArgs().get(1);
		String query = "INSERT INTO TestRejectionInfo VALUES (?,?) ON DUPLICATE KEY UPDATE additionalInfo = ?";

		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

			// Update into table the Additional Info Required
			stmt.setString(1, requestID);
			stmt.setString(2, additionalInfo);
			stmt.setString(3, additionalInfo);

			stmt.executeUpdate();

			message.getArgs().clear();
			return message;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the time extension table rows
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the time assessment table rows
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the execution leader table rows
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the evaluator table rows
	 * 
	 * @param message
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method handles a new time assessment acceptance
	 * 
	 * @param message
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

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, message.getArgs().get(1).toString());
			stmt.setString(2, message.getArgs().get(0).toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method handles a new time assessment rejection
	 * 
	 * @param message
	 */
	public void timeAssessmentRejected(MessageObject message) {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeAssessmentRejectionInfo VALUES (?,?,?)";
		String deleteQuery = "DELETE FROM TimeAssessment WHERE RequestID = ?";

		PreparedStatement stmt;
		try {

			String UserID = message.getArgs().get(2).toString();
			String RejectionInfo = message.getArgs().get(1).toString();

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.setString(2, UserID);
			stmt.setString(3, RejectionInfo);
			stmt.executeUpdate();

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method handles a new time extension acceptance
	 * 
	 * @param message
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

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, message.getArgs().get(1).toString());
			stmt.setString(2, message.getArgs().get(0).toString());
			stmt.setString(3, message.getArgs().get(2).toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method handles a new time extension rejection
	 * 
	 * @param message
	 */
	public void timeExtensionRejected(MessageObject message) {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeExtensionRejectionInfo VALUES (?,?,?,?)";
		String deleteQuery = "DELETE FROM TimeExtension WHERE RequestID = ?";

		PreparedStatement stmt;
		try {

			String RequestID = message.getArgs().get(0).toString();
			String UserID = message.getArgs().get(2).toString();
			String RejectionInfo = message.getArgs().get(1).toString();
			String Stage = message.getArgs().get(3).toString();

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, RejectionInfo);
			stmt.setString(4, Stage);
			stmt.executeUpdate();

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(deleteQuery);
			stmt.setString(1, message.getArgs().get(0).toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method creates a new time extension and returns an appropriate
	 * messageObject
	 * 
	 * @param message
	 * @return
	 */
	public MessageObject NewTimeExtension(MessageObject message) throws ParseException {
		ResultSet rs;
		String getQuery = "SELECT * FROM StageTable WHERE requestId = ? AND stage = ?";
		String addQuery = "INSERT INTO TimeExtension VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE UserID = ?, JobDescription = ?, StartTime = ?, EndTime = ?, Stage = ?";

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
			String StartTime = rs.getString("startTime");
			String endDateString = rs.getString("deadlineTime");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate ldt = LocalDate.parse(endDateString, dtf);
			ldt = ldt.plusDays(Integer.parseInt(message.getArgs().get(4).toString()));
			String EndTime = dtf.format(ldt);
			// add query

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, JobDescription);
			stmt.setString(4, StartTime);
			stmt.setString(5, EndTime);
			stmt.setString(6, Stage);
			stmt.setString(7, UserID);
			stmt.setString(8, JobDescription);
			stmt.setString(9, StartTime);
			stmt.setString(10, EndTime);
			stmt.setString(11, Stage);

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

	/**
	 * This method creates a new time assessment and returns an appropriate
	 * messageObject
	 * 
	 * @param message
	 * @return
	 */
	public MessageObject NewTimeAssessment(MessageObject message) throws ParseException {
		ResultSet rs;
		String addQuery = "INSERT INTO TimeAssessment VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE UserID = ?, JobDescription = ?, StartTime = ?, EndTime = ?";
		PreparedStatement stmt;
		try {
			String RequestID = message.getArgs().get(0).toString();
			String UserID = message.getArgs().get(1).toString();
			String JobDescription = message.getArgs().get(2).toString();

			LocalDateTime ldt = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");

			String StartTime = dtf.format(ldt); // NOW
			String EndTime = dtf.format(ldt.plusDays(Integer.parseInt(message.getArgs().get(3).toString())));

			// add query

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(addQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, UserID);
			stmt.setString(3, JobDescription);
			stmt.setString(4, StartTime);
			stmt.setString(5, EndTime);
			stmt.setString(6, UserID);
			stmt.setString(7, JobDescription);
			stmt.setString(8, StartTime);
			stmt.setString(9, EndTime);
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

	/** Was time assessment accepted for this request stage? */
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
			if (rs.next())
				message.getArgs().add(rs.getString("deadlineTime"));
			else
				message.getArgs().add(null);

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(alreadySentAssessmentQuery);
			stmt.setString(1, RequestID);

			rs = stmt.executeQuery();
			if (rs.next())
				message.getArgs().add(true);
			else
				message.getArgs().add(false);

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(alreadySentExtensionQuery);
			stmt.setString(1, RequestID);
			stmt.setString(2, Stage);

			rs = stmt.executeQuery();
			if (rs.next())
				message.getArgs().add(true);
			else
				message.getArgs().add(false);

			return message;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.getArgs().clear();
			message.getArgs().add(null);

			return message;
		}
	}

	/**
	 * This method gets the committee details and returns an appropriate
	 * messageObject
	 * 
	 * @param message
	 * @return
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

	/**
	 * This method sets a tester to the request returns an appropriate messageObject
	 * 
	 * @param message
	 * @return
	 */
	public MessageObject setTester(MessageObject message) {
		String setQuery = "UPDATE Requests SET TesterID = ? WHERE RequestID = ?";
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the 'waits for execution leader appointment' state and
	 * returns an appropriate messageObject
	 * 
	 * @param message
	 * @return
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
			else
				message.getArgs().add(false);

			return message;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method sets the 'waits for execution leader appointment' state and
	 * returns an appropriate messageObject
	 * 
	 * @param message
	 * @return
	 */
	public MessageObject setWaitsExecutionLeaderAppointment(MessageObject message) {
		String setQuery = "INSERT INTO ExecutionLeaderAppointment VALUES (?,?) ON DUPLICATE KEY UPDATE StartTime = ?";
		PreparedStatement stmt;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method gets the options for execution leaders and returns an appropriate
	 * messageObject
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method sets the execution leader and returns an appropriate
	 * messageObject
	 * 
	 * @param message
	 * @return
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method returns a resultSet with relevant dates for all kinds of reports
	 * 
	 * @param table
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public ResultSet returnRelaventDates(String table, String startTime, String endTime) {

		String setQuery = "SELECT * FROM ? WHERE startTime between ? and ? and endTime between ? and ?";

		ResultSet result;
		PreparedStatement stmt;
		try {

			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(setQuery);
			stmt.setString(1, table);
			stmt.setString(2, startTime);
			stmt.setString(3, endTime);
			stmt.setString(4, startTime);
			stmt.setString(5, endTime);
			result = stmt.executeQuery();

			while (result.next()) {

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;

	}
}
