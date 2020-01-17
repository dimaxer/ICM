package LogicController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Common.MyFile;
import Common.User;
import Gui.LoginFX;
import Gui.ManageApprovesFX;
import Gui.ManagePermissionsFX;
import Gui.NewChangeRequestFX;
import Gui.ReplaceEvaluatorSceneFX;
import Gui.RequestDetailsFX;
import Gui.StatisticsReportFX;
import Gui.ViewAllRequestsFX;
import Gui.ViewAllSystemDataFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BaseController {
	public void switchScene(String fxml_name) {
		try {
			ScreenManager.getInstance().switchScene(fxml_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function returns true if a certain scene already exists, and false
	 * otherwise. It should be utilized regularly before switching scenes in order
	 * to prevent reinitializing FX's and LC's over and over. Regular utilization
	 * will lead to extreme acceleration of scene switching.
	 * 

	 * @param fxml_name
	 * @return Boolean indicating whether the scene already exists.
	 */
	public Boolean sceneExists(String fxml_name) {
		return ScreenManager.getInstance().sceneExists(fxml_name);
	}

	public Object getCurrentFX() {
		return ScreenManager.getInstance().getCurrentFX();
	}

	/**
	 * A function that sends a MessageObject to the Server and notify's if it was
	 * successfully sent via the console
	 * 
	 * @param response
	 * @param client
	 */
	public void sendMessage(MessageObject response) {
		try {
			Client.getInstance().sendToServer((Object) response);
			System.out.println("Message sent: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " from Client");
		} catch (IOException e) {
			System.out.println("An Error occurd while trying to send: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " to Client");
			JOptionPane.showMessageDialog(null, "Error connection to the server was lost");
			ScreenManager.getInstance().getPrimaryStage().close();
		}
	}

	/**
	 * send to the server the evaluator that was approved by the supervisor delete
	 * Evaluator from AppointEvaluatorTable and connect him to the proper request in
	 * the database
	 * 
	 * @param evaluatorName
	 * @param requestID
	 * @param evaluatorID
	 */
	public void approvedEvaluator(String requestID, String evaluatorID, String evaluatorName) {

		ArrayList<Object> list = new ArrayList<Object>();

		list.add(requestID);
		list.add(evaluatorID);
		list.add(evaluatorName);
		MessageObject evaluatorMessage = new MessageObject(RequestType.ApprovedEvaluator, list);
		sendMessage(evaluatorMessage);
	}

	public void dwonloadFile(String savePath, MyFile file) {

		// add check to see if the user is active or not

		File newFile = new File(savePath + "\\" + file.getFileName());
		try {

			byte[] myByteArray = file.getMybytearray();
			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			bos.write(myByteArray, 0, myByteArray.length);
			bos.flush();
			fos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Error occured while trying to download files");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error occured while trying to download files");
			e.printStackTrace();
		}
	}

	public void loginHandle(MessageObject message) {
		if ((Boolean) message.getArgs().get(0)) {
			Client.getInstance().setCurrentUser((User) message.getArgs().get(1));
			switchScene("Panel");
		}
	}

	public void handleViewRequestDetailsRequest(MessageObject message) {
		switchScene("ViewAllRequests");
		((ViewAllRequestsFX) getCurrentFX()).loadRequests(message);
		((ViewAllRequestsFX) getCurrentFX()).clearFields();
	}

	public void handleSearchRequest(MessageObject message, Object currentFX) {
		String role = (String) message.getArgs().get(1);
		if ((boolean) message.getArgs().get(0)) {
			switchScene("RequestDetails");
			((RequestDetailsFX) getCurrentFX()).loadRequest(message, role);
			((RequestDetailsFX) getCurrentFX()).initializeFields();
		}
		if (currentFX instanceof ViewAllRequestsFX)
			((ViewAllRequestsFX) currentFX).handleSearchRequestDetails(message);
	}

	public void logOutWasPressed(ActionEvent event) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(Client.getInstance().getCurrentUser().getId());
		sendMessage(new MessageObject(RequestType.LogOut, list));

		ScreenManager.getInstance().clearScreenMap();
		switchScene("Login");
		LoginFX controller = (LoginFX) getCurrentFX();
		controller.clearFields();
	}

	public void viewRequestDetailsWasPressed() {
		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(Client.getInstance().getCurrentUser());
		MessageObject searchRequest = new MessageObject(RequestType.viewRequestTable, arrlist);
		sendMessage(searchRequest);
	}

	public void ViewAllRequestsWasPressed(ActionEvent event) {
		if (sceneExists("ViewAllRequests"))
			switchScene("ViewAllRequests");
		else
			viewRequestDetailsWasPressed();
	}

	public void replaceEvalutorWasPressed(ActionEvent event, String ReqestID) {
		switchScene("ReplaceEvaluatorScene");
		((ReplaceEvaluatorSceneFX) getCurrentFX()).setRequstId(ReqestID);
		((ReplaceEvaluatorSceneFX) getCurrentFX()).clearFields();
		((ReplaceEvaluatorSceneFX) getCurrentFX()).loadDataToEvalutorTable();
	}

	public void manageAprrovementWasPressed(ActionEvent event) {
		switchScene("ManageApproves");
		((ManageApprovesFX) getCurrentFX()).refreshTables();
	}

	public void newChangeRequestWasPressed(ActionEvent event) {
		switchScene("NewRequest");
		((NewChangeRequestFX) getCurrentFX()).clearFields();
	}

	public void managePermissionsWasPressed(ActionEvent event) {
		switchScene("ManagePermissions");
		((ManagePermissionsFX) getCurrentFX()).clearFields();
	}

	public void viewStatisticsReportWasPressed(ActionEvent event) {
		switchScene("StatisticsReport");
		((StatisticsReportFX) getCurrentFX()).clearFields();
	}

	public void viewAllSystemDataWasPressed(ActionEvent event) {
		switchScene("ViewAllSystemData");
		((ViewAllSystemDataFX) getCurrentFX()).clearFields();
	}

	public void initPanelHBoxes(AnchorPane isd, VBox all_roles, VBox supervisor) {
		if (Client.getInstance().getCurrentUser().getJobDescription().equals("Supervisor")) {
			supervisor.setVisible(true);
			all_roles.setDisable(false);
		} else if (Client.getInstance().getCurrentUser().getJobDescription().equals("ISD Chief")) {

			isd.setVisible(true);
			supervisor.setVisible(false);
			all_roles.setVisible(true);
		} else {
			supervisor.setVisible(false);
			all_roles.setVisible(true);
			isd.setVisible(false);
		}
	}

	public void initInformationSystemDetails(Boolean idCanBeNull) {
		MessageObject msg = new MessageObject(RequestType.InformationSystem_Details, new ArrayList<>());
		msg.getArgs().add(idCanBeNull);
		sendMessage(msg);
	}
	
	/**
	 * This method sends a message to the server in order to it to send a relevant mail.
	 * @param id
	 * @param title
	 * @param content
	 */
	public void sendMailByID(String id, String title, String content) {
		if (id == null || title == null || content == null) throw new NullPointerException("NULL MAIL INFO");
		MessageObject msg = new MessageObject(RequestType.SendMail, new ArrayList<>());
		msg.getArgs().add(id);
		msg.getArgs().add(title);
		msg.getArgs().add(content);
		sendMessage(msg);
	}
}
