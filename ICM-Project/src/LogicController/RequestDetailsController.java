package LogicController;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import Common.AttachedFile;
import Common.Request;
import Gui.EvaluatorReportSubmitionFX;
import Gui.ViewAllRequestsFX;
import Gui.ViewAttachedFilesFX;
import Utilities.MailSender;
import Utilities.MessageObject;
import Utilities.RequestType;
import javafx.scene.Node;
import jdbc.mysqlConnection;

/**
 * This Logic Controller class represents a generic Request Details,
 * which is hard-coded altogether with the FXML to provide support for all Roles.

 * @since 01/01/2020
 */
public class RequestDetailsController extends BaseController 
{
	// Non Role-Specific Methods ***************************************
	/**
	 * Returns the Attached File if there is one
	 * @param attachmentName name
	 * @return attached file
	 */
	public AttachedFile getAttachedFile(String attachmentName) {
			try {
				File file = AttachedFile.getFileAttachmentByName(attachmentName);
				if (file != null)
					return new AttachedFile(file);
			} catch (Exception ex) {
				System.out.println("FILE DOES NOT EXIST!");
				ex.printStackTrace();
			}
		return null;
	}
	
	// Evaluator Methods ***********************************************
	/**
	 * Returns the Attached File if there is one
	 * @param request request
	 * @return attached file
	 */
	public AttachedFile getAttachedFile(Request request) {
		if (request.getAttachFiles()) // if file was initially attached
			return getAttachedFile(request.getRequestID());
		return null;
	}
	
	/**
	 * This method handles the case that 'attach file' button was pressed
	 * @param args arguments
	 */
	public void attachWasPressed(ArrayList<Object> args ) {
		AttachedFile af = (AttachedFile)args.get(0);
		String fileName = "E" + args.get(1).toString();
		af.setFileName(fileName);
		args.clear(); args.add(af);
		
		MessageObject msg = new MessageObject(RequestType.AttachFile, args);
		sendMessage(msg);	
	}
	/**
	 * submit morre details hevent
	 * @param requestID request id
	 * @param additionalInfo the addittional information
	 */
	public void submitMoreDetails(String requestID, String additionalInfo) {
		MessageObject msg = new MessageObject(RequestType.AdditionalInfo, new ArrayList<>());
		msg.getArgs().add(requestID);
		msg.getArgs().add(additionalInfo);
		sendMessage(msg);
		swapStage(requestID, "Evaluation", "Decision");
		switchScene("ViewAllRequests");
		((ViewAllRequestsFX)getCurrentFX()).refreshWasPressed(null);
	}
	/**
	 * view evaluator report handler
	 * @param requestID the request id 
	 */
	public void viewEvaluatorReport(String requestID) {
		switchScene("EvaluatorReportSubmition");
		EvaluatorReportSubmitionFX currentScene = ((EvaluatorReportSubmitionFX) getCurrentFX());
		currentScene.setRequestID(requestID);
		currentScene.clearFieldsWasPressed(null);
		currentScene.adjustScreenToViewEvaluatorReport();
	}
	/**
	 * set the componet visibility 
	 * @param visibility boolean visibility
	 * @param items the item that need to change visibility
	 */
	public void setComponentVisibility(boolean visibility, Node... items) {
		try {
		for (Node item : items)
			item.setVisible(visibility);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * set the componenent disability
	 * @param disability get boolean disability
	 * @param items the items that need to be changed
	 */
	public void setComponentDisability(boolean disability, Node... items) {
		for (Node item : items)
			item.setDisable(disability);
	}
/**
 * submit evaluation report handler
 * @param requestID request id
 */
	public void submitEvaluationReport(String requestID) {
		switchScene("EvaluatorReportSubmition");
		EvaluatorReportSubmitionFX currentScene = ((EvaluatorReportSubmitionFX) getCurrentFX());
		currentScene.setRequestID(requestID);
		currentScene.clearFieldsWasPressed(null);
		currentScene.initShowAdditionalInfo();
	}
	/**
	 * view attached File event handler
	 * @param requestID request id
	 */
	public void viewAttachedFiles(String requestID) {
		switchScene("ViewAttachedFiles");
		ViewAttachedFilesFX currentFX = (ViewAttachedFilesFX) getCurrentFX();
		currentFX.showAttachedFiles(requestID);
	}
	
	/**
	 * This methods swaps the current stage to a new stage
	 * @param requestID request id	
	 * @param newStage the new stage
	 * @param currentStage the current stage
	 */
	public void swapStage(String requestID, String newStage, String currentStage) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		args.add(newStage);
		args.add(currentStage);
		MessageObject msg = new MessageObject(RequestType.swapStage, args);
		sendMessage(msg);
	}
	/**
	 * committee approve requet event handler
	 * @param requestID id
	 * @param currentStage stage
	 */
	public void committeeApprovesRequest(String requestID, String currentStage) {
		//switchScene("ViewAllRequests");
		//swapStage(requestID, "Execution", currentStage);
	}
	/**
	 * change aprove
	 * @param requestID request id
	 * @param currentStage the current stage
	 * @param adhoc the selected ad hoc
	 */
	public void changeApproval(String requestID, String currentStage, boolean adhoc) {
		switchScene("ViewAllRequests");
		swapStage(requestID, "Testing", currentStage);
		if (!adhoc) {
			MessageObject msg = new MessageObject(RequestType.SetTesterBeforeAdhoc, new ArrayList<>());
			msg.getArgs().add(requestID);
			sendMessage(msg);
		}
	}
	/**
	 * tester report aprove event handler
	 * @param requestID request id
	 * @param currentStage the current stage
	 * @param initiatorID the initiator id
	 */
	public void testerReportApproved(String requestID, String currentStage, String initiatorID) {
		switchScene("ViewAllRequests");
		swapStage(requestID, "Closing", currentStage);
		sendMailByID(initiatorID, "ICM Request ID " + requestID + " Closing", "Your request passed to closing stage.\nPlease contact the supervisor to permit him to close it");
	}
	/**
	 * tester report reject event handle
	 * @param requestID request id	
	 * @param rejectionDetails reject details
	 * @param currentStage the current stage
	 */
	public void testerReportRejected(String requestID, String rejectionDetails, String currentStage) {
		switchScene("ViewAllRequests");
		swapStage(requestID, "Execution", currentStage);
		// handle rejection details
		MessageObject msg = new MessageObject(RequestType.updateTesterRejectionDetails, new ArrayList<>());
		msg.getArgs().add(requestID);
		msg.getArgs().add(rejectionDetails);
		sendMessage(msg);
	}
	
	/** This methods swaps the current status to a new status
	 * 
	 * @param requestID request id
	 * @param newStatus the new status
	 * @param currentStatus the current stage
	 */
	private void swapStatus(String requestID, String newStatus, String currentStatus) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		args.add(newStatus);
		args.add(currentStatus);
		MessageObject msg = new MessageObject(RequestType.swapStatus, args);
		sendMessage(msg);
	}

	/**
	 * committee reject request event handler
	 * @param requestID request id	
	 * @param currentStage the current stage
	 * @param initiatorID the initiator id
	 */
	public void committeeRejectRequest(String requestID, String currentStage, String initiatorID) {
		switchScene("ViewAllRequests");
		swapStage(requestID, "Closing", currentStage);
		sendMailByID(initiatorID, "ICM Request ID " + requestID + " Closing", "Your request passed to closing stage.\nPlease contact the supervisor to permit him to close it");
	}
	/**
	 * supervisor close request event handler
	 * @param requestID request id
	 * @param currentStatus the curent status
	 */
	public void supervisorClosesRequest(String requestID, String currentStatus) {
		switchScene("ViewAllRequests");
		swapStatus(requestID, "Closed", currentStatus);
	}
	/**
	 * superviso freez event handler
	 * @param requestID request id
	 * @param currentStatus the current status
	 */
	public void supervisorFreeze(String requestID, String currentStatus) {
		switchScene("ViewAllRequests");
		swapStatus(requestID, "Frozen", currentStatus);
	}

	/**
	 * supervisor Unfreeze
	 * @param requestID request id
	 * @param currentStatus the current status
	 */
	public void supervisorUnfreeze(String requestID, String currentStatus) {
		switchScene("ViewAllRequests");
		swapStatus(requestID, "Active", currentStatus);
	}
	
	/**
	 * inital test reject info
	 * @param requestID reuest id
	 */
	public void initTestRejectionInfo(String requestID) {
		ArrayList<Object> args = new ArrayList<>();
		args.add(requestID);
		MessageObject msg = new MessageObject(RequestType.TestRejectionInfo, args);
		sendMessage(msg);
	}
	/**
	 * inital committee event
	 */
	public void initCommittee() {
		MessageObject msg = new MessageObject(RequestType.GetCommittee, new ArrayList<>());
		sendMessage(msg);
	}
}
