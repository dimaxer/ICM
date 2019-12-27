package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFormattedTextField;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import Common.Request;
import LogicController.BaseController;
import LogicController.RequestDetailsInitiatorController;
import Utilities.MessageObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class  RequestDetailsInitiatorFX implements BaseFx, Initializable {

	
	
	@FXML
	private JFXTextArea requestID;
	@FXML
	private JFXTextArea status;
	@FXML
	private JFXTextArea stage;
	
	@FXML
	private TextArea requestedInformationSystem;
	@FXML
	private TextArea currentStiuation;
	@FXML
	private TextArea requestedChange;
	@FXML
	private TextArea reason;
	@FXML
	private TextArea notes;
	

	
	
	// Class variables *************************************************
	private RequestDetailsInitiatorController requestDetailsInitiatorController;
	
	@Override
	public void setLogicController(BaseController controller) {
		// TODO Auto-generated method stub
		requestDetailsInitiatorController = (RequestDetailsInitiatorController) controller;
		
	}

	//load the data to the GUI
	public void loadRequest(MessageObject massage) {
		Request request = (Request)(massage.getArgs().get(2));
		
		
		requestID.setText("Request Id: " + request.getRequestID());
		status.setText("Status:" + request.getRequestStatus());
		stage.setText("Stage: " + request.getCurrentStage());
		requestedInformationSystem.setText(request.getInformationSystem());
		currentStiuation.setText(request.getCurrentSituation());
		requestedChange.setText(request.getRequestedChange());
		reason.setText(request.getReasonForRequest());
		notes.setText(request.getNote());
		
	}
	
	public void backWasPressed(ActionEvent event) {

		requestDetailsInitiatorController.switchScene("ViewAllRequests");
	}

	@FXML
	public void homeWasPressed(ActionEvent event) {

		requestDetailsInitiatorController.switchScene("AcademicUserPanel");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
