package Gui;

import com.jfoenix.controls.JFXButton;
import LogicController.BaseController;
import LogicController.BasePanelController;
import Utilities.MessageObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PanelFX implements BaseFx {
	// Class Buttons ***************************************************
	@FXML
	private JFXButton viewRequestDetails;

	@FXML
	private JFXButton logOut;

	@FXML
	private Text firstName;
	// Class variables *************************************************
	private BasePanelController panelController;
	// Class methods ***************************************************

	/**
	 * a method that intializes text On Load
	 * 
	 */

	@FXML
	public void initialize() {
		firstName.setText("Welcome, " + getFirstNameFromSQL());
	}

	// THIS FUNCTION WILL BE DELETED AND REPLACED WITH AN ACTUAL GET FUNCTION FROM
	// SQL CONTROLLER STATIC CLASS
	private String getFirstNameFromSQL() {
		return "David";
	}

	/**
	 * This event handler switches scene to SearchReaquest
	 * 
	 * @param event
	 */
	@FXML
	public void viewRequestDetailsWasPressed(ActionEvent event) {


		
		// send the User id to the server
		panelController.viewRequestDetailsWasPressed();

	}
	
	
	
	public void handleViewRequestDetailsRequest(MessageObject massage) {

		panelController.switchScene("ViewAllRequests");
		ViewAllRequestsFX FX = (ViewAllRequestsFX) panelController.getCurrentFX();
		FX.clearFields();
		FX.loadRequests(massage);
		}

	
	

	/**
	 * This event handler switches scenes back to the login page
	 * 
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		panelController.switchScene("LoginPage");
		LoginFX controller = (LoginFX) panelController.getCurrentFX();
		controller.clearFields();
	}

	@Override
	public void setLogicController(BaseController controller) {
		panelController = (BasePanelController) controller;

	}

}
