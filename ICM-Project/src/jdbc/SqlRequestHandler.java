package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import Common.EvaluatorReport;
import Common.Request;
import Common.User;
import Common.EvaluatorAppoitmentTable.EvalutorAppoitmentTableSerializble;
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
	 * @author Raz Malka
	 * @param args Arguments of New Request
	 * @return Success or Failure of the adding.
	 */
	public Boolean addCRToDB(ArrayList<Object> args) {
		PreparedStatement stmt;

		try {
			String query = "INSERT INTO Requests (Date, InformationSystem, RequestedChange, CurrentSituation, RequestReason, Note, AttachFiles, Stage, Status, InitiatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);
			for (int i = 0; i < 10; i++)
				if (i == 6)
					stmt.setBoolean(i + 1, (args.get(i) != null));
				else
					stmt.setString(i + 1, args.get(i).toString());

			stmt.executeUpdate();

			insertNewEvaluatorToEvaluatorTable(args.get(10).toString()); /// insert New Evaluator to "Evaluator
																			/// Appointment Table"
			System.out.println("Query of Create New Request Executed Successfully!");

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
	 * @author Raz Malka
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
	 * @author Raz Malka
	 */
	public MessageObject viewRequestTable(MessageObject msg) throws SQLException {
		User currentUser = (User) msg.getArgs().get(0);
		MessageObject result = null;

		switch (getUserJobDescription(currentUser)) {
		case "Supervisor":
			result = viewAllRequestTable(msg.getTypeRequest(), currentUser);
			break;
		case "ISD Chief":
			result = viewAllRequestTable(msg.getTypeRequest(), currentUser);
			break;
		case "Committee Member":
			result = viewChairManRequestTable(msg.getTypeRequest(), currentUser);
			break;
		case "Committee Chairman":
			result = viewChairManRequestTable(msg.getTypeRequest(), currentUser);
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
	public void insertNewEvaluatorToEvaluatorTable(String EvaluatorID) {

		ResultSet EvaluatorRs = executeQuery(new String("SELECT Name FROM Users WHERE UserID=" + EvaluatorID));
		try {
			EvaluatorRs.next();
			String EvaluatorName = EvaluatorRs.getString("Name");
			String requestID = getLastInsertID();
			PreparedStatement stmt;
			String insertEvaluatorQuery = "INSERT INTO EvaluatorAppointment (RequestID, EvaluatorID,EvaulatorName) VALUES (?, ?, ?);";

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
	public void updateRequestEvaluator(String requestID, String evaluatorID) {

		String updateEvaloeytor = "Update Requests SET EvaluatorID =?  WHERE RequestID =? ";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateEvaloeytor);
			stmt.setString(1, evaluatorID);
			stmt.setString(2, requestID);
			// Update EvaloeytorID in the DB
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Update the stage of requests table for RequestID Insert new Evaluation Stage
	 * in StageTable in the DB
	 * 
	 * @param requestID
	 */
	public void insertStageEvaluator(String requestID) {

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
		String insertEvaluationStage = "INSERT INTO StageTable (requestId, stage, startTime, endTime, late) VALUES (?, ?, ?, ?, ?)";
		try {
			java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection()
					.prepareStatement(insertEvaluationStage);
			stmt.setString(1, requestID);
			stmt.setString(2, "Evaluation");
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setInt(5, 0);

			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This methods gets the Name and Evaluator ID of Information Systems
	 * 
	 * @author Raz Malka
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
	 * @author Raz Malka
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
	 * @author Raz Malka
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

	/** This method updates the evaluator of a certain Information System */
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

	/** This method updates the permanent roles holders */
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

	public MessageObject viewEvaluatorTable(MessageObject message) {
		ResultSet rs = executeQuery(new String("Select RequestID,EvaluatorID,EvaulatorName from EvaluatorAppointment"));

		try {
			if (!(rs.next())) {
				message.getArgs().add(false);
				return message;
			} else
				message.getArgs().add(true);
			do {
				message.getArgs().add(new EvalutorAppoitmentTableSerializble(rs.getString("RequestID"),
						rs.getString("EvaluatorID"), rs.getString("EvaulatorName")));

			} while (rs.next());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;

	}

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
						.add(new EvalutorAppoitmentTableSerializble(rs.getString("UserID"), rs.getString("Name")));
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
		String insertEvaluationReport = "INSERT INTO EvaluationReport (RequestId, EvaluatorID, Description, Constraints, Result) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertEvaluationReport);

			stmt.setString(1, requestID);
			stmt.setString(2, evaluatorID);
			stmt.setString(3, description);
			stmt.setString(4, constraints);
			stmt.setString(5, result);

			stmt.executeUpdate();
			return changeStageToInspectonAndDecision(requestID, evaluatorID);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}

	}

	/**
	 * this method changes the stage of the request to inspection And Decision by
	 * removing from the request the evaluator id and adding committee members to
	 * the request
	 * 
	 * @param requestID
	 * @param EvaluatorID
	 * @return
	 */
	public boolean changeStageToInspectonAndDecision(String requestID, String EvaluatorID) {

		String query = "UPDATE Requests SET EvaluatorID = ?,Stage= 'Decision' WHERE EvaluatorID = ? AND RequestID = ? ";

		try {
			PreparedStatement stmt = mysqlConnection.getInstance().getConnection().prepareStatement(query);

			stmt.setNull(1, java.sql.Types.VARCHAR);
			stmt.setString(2, EvaluatorID);
			stmt.setString(3, requestID);

			stmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}
	/**
	 * getting from the DB all the information of the report
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
	 * update the Stage of the request in the DB and insert new Execution Stage in StageTable
	 * @param message ArrayList that contain the requestID of the report
	 */
	public void updateStageToExecution(MessageObject message) {
		
		String requestID = (String) message.getArgs().get(0);
		String updateStageInRequests ="UPDATE Requests SET Stage= 'Execution' WHERE RequestID = ? ";;
		String insertExecutionStageToStageTable = "INSERT INTO StageTable (requestId, stage, startTime, endTime, late) VALUES (?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(updateStageInRequests);
			stmt.setString(1, requestID);
			stmt.executeUpdate();
			
			stmt = mysqlConnection.getInstance().getConnection().prepareStatement(insertExecutionStageToStageTable);
			
			//get the current date
			java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			
			stmt.setString(1, requestID);
			stmt.setString(2,"Execution");
			stmt.setString(3, ourJavaDateObject.toString());
			stmt.setString(4, null);
			stmt.setInt(5, 0);
			stmt.executeUpdate();
			
			
			return;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
		
		
	}
}
