package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Common.AttachedFile;
import Common.EvaluatorReport;
import Common.Interval;
import Common.MyFile;
import Common.User;
import Gui.ManageApprovesFX;
import Gui.ViewAllSystemDataFX;
import Utilities.MailSender;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.Statistics;
import jdbc.mysqlConnection;
import ocsf.server.ConnectionToClient;

/**
 * This class handles requests to server.
 * 
 */
public class ServerRequestHandler {
	private jdbc.SqlRequestHandler mysqlRequestHandler = null;

	// Instance methods ************************************************
	/**
	 * This function handles messages from clients
	 * 
	 * @param msg    Message from Client
	 * @param client Client that sent the message
	 */
	public void handle(Object msg, ConnectionToClient client) {
		if (msg instanceof MessageObject) {
			// cast to MessageObject and send a print that a message was received
			MessageObject message = (MessageObject) msg;

			DBServer.getInstance().printMessageRecieved(message, client);

			mysqlRequestHandler = mysqlConnection.getInstance().handle();

			MessageObject responseMessage = null;
			switch (message.getTypeRequest()) {
			case LogOut:
				handleLogOut(message, client);
				break;
			case Login:
				responseMessage = handleLogin(message, client);
				break;
			case View_Req_Details:
				responseMessage = handleSearchRequest(message, client);
				break;
			case change_Status:
				responseMessage = handleChangeStatus(message, client);
				break;
			case viewRequestTable:
				responseMessage = handleViewRequestTable(message, client);
				responseMessage.setTypeRequest(RequestType.viewRequestTable);
				break;
			case refreshViewUserRequestTable:
				responseMessage = handleViewRequestTable(message, client);
				responseMessage.setTypeRequest(RequestType.refreshViewUserRequestTable);
				break;
			case NewChangeRequest:
				responseMessage = handleNewChange(message, client);
				break;
			case AttachFile:
				handleAttachFile(message, client);
				break;
			case ViewAttachedFiles:
				responseMessage = handleViewAttachedFiles(message);
				break;
			case DownloadAttachedFiles:
				responseMessage = handleDownloadFile(message);
				break;
			case ApprovedEvaluator:
				responseMessage = handleApprovedEvaluator(message);
				break;
			case InformationSystem_Details:
				responseMessage = handleInformationSystemDetails(message, client);
				break;
			case AllUserDetails:
				responseMessage = handleAllUserDetails(message, client);
				break;
			case PermanentRoles_Details:
				responseMessage = handlePermanentRolesDetails(message, client);
				break;
			case UpdateEvaluator:
				handleUpdateEvaluator(message, client);
				break;
			case UpdatePermanentRoles:
				handleUpdatePermanentRoles(message, client);
				break;
			case UploadEvaluatorReport:
				responseMessage = handleUploadEvaluatorReport(message, client);
				break;
			case ViewIseTable:
				responseMessage = handleISETable(message, client);
				break;
			case GetEvaluatorReport:
				responseMessage = handleGetEvaluatorReport(message,client);
				break;
			case swapStage:
				responseMessage = handleSwapStage(message,client);
				break;
			case swapStatus:
				responseMessage = handleSwapStatus(message,client);
				break;
			case AdditionalInfo:
				handleAdditionalInfo(message,client);
				break;
			case ShowAdditionalInfo:
				responseMessage = handleShowAdditionalInfo(message,client);
				break;
			case TestRejectionInfo:
				responseMessage = handleGetTestRejectionInfo(message,client);
				break;
			case updateTesterRejectionDetails:
				responseMessage = handleUpdateTestRejectionInfo(message,client);
				break;
			case GetEvaluatorTable:
				responseMessage = handleGetEvaluatorTable(message,client);
				break;
			case GetTimeAssessmentTable:
				responseMessage = handleTimeAssessmentTable(message,client);
				break;
			case GetTimeExtensionTable:
				responseMessage = handleTimeExtensionTable(message,client);
				break;
			case GetExecutionLeaderTable:
				responseMessage = handleExecutionLeaderTable(message,client);
				break;
			case AcceptTimeAssessment:
				handleAcceptTimeAssessment(message,client);
				break;
			case RejectTimeAssessment:
				handleRejectTimeAssessment(message,client);
				break;
			case AcceptTimeExtension:
				handleAcceptTimeExtension(message,client);
				break;
			case RejectTimeExtension:
				handleRejectTimeExtension(message,client);
				break;
			case NewTimeAssessment:
				responseMessage = handleNewTimeAssessment(message,client);
				break;
			case NewTimeExtension:
				responseMessage = handleNewTimeExtension(message,client);
				break;
			case GetDeadline:
				responseMessage = handleGetStageDeadline(message,client);
				break;
			case GetCommittee:
				responseMessage = handleGetCommittee(message,client);
				break;
			case SetTester:
				responseMessage = handleSetTester(message,client);
				break;
			case WaitsExecutionLeaderAppointment:
				responseMessage = handleWaitsExecutionLeaderAppointment(message,client);
				break;
			case SetWaitsExecutionLeaderAppointment:
				responseMessage = handleSetWaitsExecutionLeaderAppointment(message,client);
				break;
			case GetExecutionLeaderOptions:
				responseMessage = handleGetExecutionLeaderOptions(message,client);
				break;
			case SetExecutionLeader:
				responseMessage = handleSetExecutionLeader(message,client);
				break;
			case SetTesterBeforeAdhoc:
				handleSetTesterBeforeAdhoc(message,client);
				break;
			case GetEmployeeTable:
				responseMessage = handleGetEmployeeTable(message,client);
				break;
			case GetRequestTable:
				responseMessage = handleGetRequestTable(message,client);
				break;
			case GetReportStatusData:
				responseMessage = handleGetReportStatusData(message,client);
				break;
			case GetReportRejectedData:
				responseMessage = handleGetReportRejectedData(message,client);
				break;
			case GetReportActiveDays:
				responseMessage = handleGetReportActiveDaysData(message,client);
				break;
			case GetReportExtensions:
				responseMessage = handleGetReportExtensionsData(message,client);
				break;
			case GetReportDurations:
				responseMessage = handleGetReportDurationsData(message,client);
				break;
			case GetReportDelays:
				responseMessage = handleGetReportDelaysData(message,client);
				break;
			case SendMail:
				handleSendMail(message,client);
				break;
			case SaveReport:
				handleSaveReport(message,client);
				break;
			case GetReports:
				responseMessage = handleGetReports(message,client);
				break;
			default:
				break;
			}

			if (responseMessage != null)
				DBServer.getInstance().sendMessage(responseMessage, client);
		} else
			System.out.println("Error - Message rechieved is not a MessageObject");
	}


	private MessageObject handleGetReports(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getReports(message);
	}


	private void handleSaveReport(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.saveReport(message);
	}

	private void handleSendMail(MessageObject message, ConnectionToClient client) {
		String id = message.getArgs().get(0).toString();
		String subject = message.getArgs().get(1).toString();
		String content = message.getArgs().get(2).toString();
		String recipient = mysqlRequestHandler.getMailByUID(id);
		if (recipient == null) return;
		
		MailSender.getInstance().send(recipient, subject, content, DBServer.getInstance().isPopup());
	}

	private MessageObject handleGetReportDelaysData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportDelaysData(message);
			Statistics utility = new Statistics();
			ArrayList<Interval> intervals = utility.getIntervals(response, "Delays");
			
			message.getArgs().clear();
			message.getArgs().add(intervals);
			message.getArgs().add("Delays");
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private MessageObject handleGetReportDurationsData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportDurationsData(message);
			Statistics utility = new Statistics();
			ArrayList<Interval> executionIntervals = utility.getIntervals(response, "Durations");
			response.getArgs().set(0, response.getArgs().get(2));
			response.getArgs().set(1, response.getArgs().get(3));
			ArrayList<Interval> evaluationIntervals = utility.getIntervals(response, "Durations");
			
			message.getArgs().clear();
			message.getArgs().add(Interval.merge(executionIntervals, evaluationIntervals));
			message.getArgs().add("Durations");
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private MessageObject handleGetReportExtensionsData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportExtensionsData(message);
			Statistics utility = new Statistics();
			ArrayList<Interval> intervals = utility.getIntervals(response, "Extensions");
			
			message.getArgs().clear();
			message.getArgs().add(intervals);
			message.getArgs().add("Extensions");
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private MessageObject handleGetReportRejectedData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportRejectedData(message);
			Statistics utility = new Statistics();
			ArrayList<Interval> intervals = utility.getIntervals(response, "FilterStatus");
			
			message.getArgs().clear();
			message.getArgs().add(intervals);
			message.getArgs().add("Rejection");
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private MessageObject handleGetReportStatusData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportStatusData(message);
	
			Statistics utility = new Statistics();
			ArrayList<Interval> intervals = utility.getIntervals(response, "FilterStatus");
			
			String status = response.getArgs().get(0).toString();
			message.getArgs().clear();
			message.getArgs().add(intervals);
			message.getArgs().add(status);
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private MessageObject handleGetReportActiveDaysData(MessageObject message, ConnectionToClient client) {
		try {
			MessageObject response = mysqlRequestHandler.getReportStatusData(message);
	
			Statistics utility = new Statistics();
			ArrayList<Interval> intervals = utility.getIntervals(response, "FilterActiveDays");
			
			String status = response.getArgs().get(0).toString();
			message.getArgs().clear();
			message.getArgs().add(intervals);
			message.getArgs().add("Active Days");
			return message;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private MessageObject handleGetRequestTable(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getRequestTable(message);
	}

	private MessageObject handleGetEmployeeTable(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getEmployeeTable(message);
	}

	private void handleSetTesterBeforeAdhoc(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.setTesterBeforeAdhoc(message);
	}

	private MessageObject handleSetExecutionLeader(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.setExecutionLeader(message);
	}

	private MessageObject handleGetExecutionLeaderOptions(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getExecutionLeaderOptions(message);
	}

	private MessageObject handleWaitsExecutionLeaderAppointment(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getWaitsExecutionLeaderAppointment(message);
	}
	
	private MessageObject handleSetWaitsExecutionLeaderAppointment(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.setWaitsExecutionLeaderAppointment(message);
	}

	private MessageObject handleSetTester(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.setTester(message);
	}

	private MessageObject handleGetCommittee(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getCommittee(message);
	}

	private MessageObject handleGetStageDeadline(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.getStageDeadline(message);
	}

	private void handleRejectTimeExtension(MessageObject message, ConnectionToClient client) {
		// TODO Auto-generated method stub
		mysqlRequestHandler.timeExtensionRejected(message);
	}

	private void handleAcceptTimeExtension(MessageObject message, ConnectionToClient client) {
		// TODO Auto-generated method stub
		mysqlRequestHandler.timeExtensionAccepted(message);
	}

	private MessageObject handleNewTimeExtension(MessageObject message, ConnectionToClient client) {
		// TODO Auto-generated method stub
		try {
			return mysqlRequestHandler.NewTimeExtension(message);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject handleNewTimeAssessment(MessageObject message, ConnectionToClient client) {
		// TODO Auto-generated method stub
		try {
			return mysqlRequestHandler.NewTimeAssessment(message);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void handleRejectTimeAssessment(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.timeAssessmentRejected(message);
	}

	private void handleAcceptTimeAssessment(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.timeAssessmentAccepted(message);
	}

	private MessageObject handleGetEvaluatorTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getEvaluatorTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject handleExecutionLeaderTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getExecutionLeaderTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject handleTimeExtensionTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getTimeExtensionTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject handleTimeAssessmentTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getTimeAssessmentTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private MessageObject handleSwapStage(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.swapStage(message);
		message.getArgs().clear();
		return message;
	}
	
	private MessageObject handleSwapStatus(MessageObject message, ConnectionToClient client) {
		mysqlRequestHandler.swapStatus(message);
		message.getArgs().clear();
		return message;
	}
	
	/**
	 * 
	 * @param message ArrayList that contain the requestID of the report
	 * @param client
	 * @return
	 */
	private MessageObject handleGetEvaluatorReport(MessageObject message, ConnectionToClient client) {
		
		
		EvaluatorReport report = mysqlRequestHandler.getEvaluatorReport(message);
		message.getArgs().clear();
		message.getArgs().add(report);
		
		return message;
	}

	/**
	 * bring from the database Table of all ISE workers for Supervisor to pick new
	 * Evaluator when he reject the automatic Appoitment
	 * 
	 */
	private MessageObject handleISETable(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.viewIseTable(message);
	}

	/**
	 * this method uploads the evaluator report to the relevant table in the db
	 * 
	 * @param message
	 * @param client
	 * @return
	 */
	private MessageObject handleUploadEvaluatorReport(MessageObject message, ConnectionToClient client) {

		boolean result = mysqlRequestHandler.uploadEvaluatorReport((String) message.getArgs().get(3),
				(String) message.getArgs().get(0), (String) message.getArgs().get(1), (String) message.getArgs().get(2),
				(String) message.getArgs().get(4));
		message.getArgs().clear();
		message.getArgs().add(result);
		return message;

	}

	/**
	 * when a user presses log out this method logges him out from the server
	 * 
	 * @param message
	 * @param client
	 */
	private void handleLogOut(MessageObject message, ConnectionToClient client) {
		String userID = (String) message.getArgs().get(0);

		if (UserConnectivityManager.getInstance().logOutUser(userID)) {
		} else
			System.out.println("Error some one hacked the system");
	}

	private void handleUpdatePermanentRoles(MessageObject message, ConnectionToClient client) {
		try {
			mysqlRequestHandler.updatePermanentRoles(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private MessageObject handleEvaluatorTable(MessageObject message, ConnectionToClient client) {
		return mysqlRequestHandler.viewEvaluatorTable(message);
	}

	private void handleUpdateEvaluator(MessageObject message, ConnectionToClient client) {
		try {
			mysqlRequestHandler.updateEvaluator(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * delete Approved Evaluator from Evaluator Appointment Table update Request
	 * Evaluator in Requests table Insert Stage Evaluator in StageTable
	 * 
	 * @param message contains requestID and
	 * @return messege object contains requestID and
	 */
	private MessageObject handleApprovedEvaluator(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		String evaluatrorID = (String) message.getArgs().get(1);

		return mysqlRequestHandler.updateRequestEvaluator(requestID, evaluatrorID);
	}

	private MessageObject handleDownloadFile(MessageObject message) {
		String[] fileNames = (String[]) message.getArgs().get(0);
		String requestID = (String) message.getArgs().get(1);
		String dir = System.getProperty("user.dir");
		File file;

		MyFile[] downloadFileList = new MyFile[fileNames.length];
		int i = 0;
		for (String fileName : fileNames) {
			file = new File(dir + "\\RequestsAttachedFiles\\" + requestID + "\\" + fileName);
			downloadFileList[i] = downloadFile(file);
			i++;
		}
		message.getArgs().clear();
		message.setTypeRequest(RequestType.DownloadAttachedFiles);
		message.getArgs().add(downloadFileList);
		return message;

	}


	private MyFile downloadFile(File file) {
		MyFile downloadedFile = new MyFile(file.getName());
		String localFilePath = file.getAbsolutePath();

		byte[] myByteArray = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

			downloadedFile.initArray(myByteArray.length);
			downloadedFile.setSize(myByteArray.length);

			bufferedInputStream.read(downloadedFile.getMybytearray(), 0, myByteArray.length);

			return downloadedFile;
		} catch (Exception e) {

		}
		return null;

	}

	/**
	 * this method returns a string array to the client with all the names of the
	 * attached files of this request
	 * 
	 * @param message
	 * @return
	 */
	private MessageObject handleViewAttachedFiles(MessageObject message) {
		String requestID = (String) message.getArgs().get(0);
		 String dir = System.getProperty("user.dir");
		File file = new File(dir + "\\RequestsAttachedFiles\\" + requestID);

		String[] stringFileArray = null;
		if (file.exists())
			stringFileArray = file.list();

		message.getArgs().remove(0);
		message.getArgs().add((Object) stringFileArray);

		return message;

	}


	/**
	 * this method handles a creation of new change request
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating
	 *         specific request response.
	 */
	private MessageObject handleNewChange(MessageObject message, ConnectionToClient client) {
		boolean res = mysqlRequestHandler.addCRToDB(message.getArgs());

		ArrayList<Object> args = new ArrayList<Object>();

		String requestID = mysqlRequestHandler.getLastInsertID();

		args.add(res);
		args.add(requestID);

		message.setTypeRequest(RequestType.NewChangeRequest);
		message.setArgs(args);
		return message;
	}

	/**
	 * This method handles any attached files received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	private void handleAttachFile(MessageObject message, ConnectionToClient client) {
		if (message.getArgs().get(0) instanceof MyFile) {
			MyFile attachedFile = (MyFile) message.getArgs().get(0);
			String dir = System.getProperty("user.dir");
			// add check to see if the user is active or not
			String requestID = (String) message.getArgs().get(1);

			createFolder(requestID);

			File newFile = new File(dir + "\\RequestsAttachedFiles\\" + requestID + "\\" + attachedFile.getFileName());
			try {

				byte[] myByteArray = attachedFile.getMybytearray();
				FileOutputStream fos = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				bos.write(myByteArray, 0, myByteArray.length);
				bos.flush();
				fos.flush();
				bos.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	/**
	 * this method handles a login request from the client by extracting the
	 * username and password from MessageObject checking if its in the the db and
	 * sending back the message to the client if it was found or not
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating
	 *         specific request response.
	 */
	private MessageObject handleLogin(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.checkUserCredentials(message.getArgs().get(0).toString(),
					message.getArgs().get(1).toString(), client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * A method to search a request by id in the db and return its data
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating
	 *         specific request response.
	 */
	private MessageObject handleSearchRequest(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.searchRequest(message);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * adding function to change status in DB and return true or false to client if
	 * succeeded or not
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating
	 *         specific request response.
	 */
	private MessageObject handleChangeStatus(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.changeStatus((message));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private MessageObject handleViewRequestTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.viewRequestTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void createFolder(String path) {

		String dir = System.getProperty("user.dir");
		File file = new File(dir + "\\RequestsAttachedFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created: " + dir + " \\RequestsAttachedFiles");
			} else {
				System.out.println("Failed to create directory!: " + path);
			}
		}
		file = new File(dir + "\\RequestsAttachedFiles\\" + path);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created: " + dir + " \\RequestsAttachedFiles\\" + path);
			} else {
				System.out.println("Failed to create directory!: " + path);
			}
		}

	}



	/**
	 * This method handles the Details of Information Systems
	 * (informationSystemName, currentEvaluatorID)
	 */
	private MessageObject handleInformationSystemDetails(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getInformationSystemDetails(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/** This method handles the Details of All Users (Name, ID) */
	private MessageObject handleAllUserDetails(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getAllUserDetails(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method handles the Details of Users with Permanent Roles (roleName, ID)
	 */
	private MessageObject handlePermanentRolesDetails(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getPermanentRolesDetails(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/** This method handles asking for Additional Info by Committee */
	private void handleAdditionalInfo(MessageObject message, ConnectionToClient client) {
			mysqlRequestHandler.askForAdditionalInfo(message);
	}
	
	private MessageObject handleShowAdditionalInfo(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getAdditionalInfo(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private MessageObject handleGetTestRejectionInfo(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.getTestRejectionInfo(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private MessageObject handleUpdateTestRejectionInfo(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.updateTestRejectionInfo(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
