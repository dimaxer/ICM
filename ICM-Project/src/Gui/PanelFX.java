package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import LogicController.BasePanelController;
import Utilities.MessageObject;
import Utilities.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PanelFX implements BaseFx {
	// Class Buttons ***************************************************
	@FXML
	private JFXButton viewRequestDetails;

	@FXML
	private JFXButton logOut;
	@FXML
	private JFXButton newChangeRequest;

	@FXML
	private Text firstName;
	// Class variables *************************************************
	private BasePanelController panelController;
	// Class methods ***************************************************

	/**
	 * a method that intializes text On Load
	 * 
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		firstName.setText("Welcome, " + getFirstNameFromSQL());
		panelController = new BasePanelController();
	}

	/**
	 * THIS FUNCTION WILL BE DELETED AND REPLACED WITH AN ACTUAL GET FUNCTION FROM
	 * SQL CONTROLLER STATIC CLASS
	 * @return
	 */
	private String getFirstNameFromSQL() {
		return "David";
	}

	/**
	 * This event handler switches scene to View Request Details page
	 * @param event
	 */
	@FXML
	public void viewRequestDetailsWasPressed(ActionEvent event) {
		if (panelController.sceneExists("ViewAllRequests"))
			panelController.switchScene("ViewAllRequests");
		else
			panelController.viewRequestDetailsWasPressed();
	}

	/**
	 * This method is called by server request,
	 * switches scenes to View Request Details page and initializes the new page
	 * @param event
	 */
	public void handleViewRequestDetailsRequest(MessageObject message) {
		panelController.switchScene("ViewAllRequests");
		((ViewAllRequestsFX) ScreenManager.getInstance().getCurrentFX()).clearFields();
		((ViewAllRequestsFX) ScreenManager.getInstance().getCurrentFX()).loadRequests(message);
	}

	/**
	 * This event handler switches scenes back to the Login page
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		ScreenManager.getInstance().clearScreenMap();
		panelController.switchScene("LoginPage");
		LoginFX controller = (LoginFX) panelController.getCurrentFX();
		controller.clearFields();
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		panelController.switchScene("NewRequest");
		((NewChangeRequestFX) ScreenManager.getInstance().getCurrentFX()).clearFields();
	}

}
