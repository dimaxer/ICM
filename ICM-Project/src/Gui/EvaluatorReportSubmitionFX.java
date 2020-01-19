package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.EvaluatorReport;
import LogicController.EvaluationReportController;
import Utilities.MessageObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
/**
 * Evaluator Report Submition Fxml controller
 *
 */
public class EvaluatorReportSubmitionFX extends BaseFX {

	// Main Buttons ******************************
	@FXML
	private JFXButton submit;

	@FXML
	private JFXButton clear;

	@FXML
	private JFXButton back;

	@FXML
	private JFXTextField description;

	@FXML
	private JFXTextField constraints;

	@FXML
	private JFXTextField result;

	@FXML
	private AnchorPane additionalInfoPane;
	
	@FXML
	private JFXTextField additionalInfo;
	
	@FXML
	private JFXButton newChangeRequest;
	// class variables****************************

	private String requestID;

	// Controller ********************************

	EvaluationReportController evaluationReportController;

	// FX Methods ********************************
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setValdiator();
		evaluationReportController = new EvaluationReportController();
	}
	/**
	 * inital the additional information
	 */
	public void initShowAdditionalInfo() {
		evaluationReportController.initShowAdditionalInfo(requestID);
	}
	/**
	 * back event handler
	 * @param event back was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		evaluationReportController.switchScene("RequestDetails");
	}

	/**
	 * this method submit the evaluation report send relevant data to the Evaluation
	 * report table
	 * 
	 * @param event Button was Pressed
	 */
	@FXML
	public void submitWasPressed(ActionEvent event) {
		setValdiator();
		if (description.getText().equals("") || constraints.getText().equals("") || result.getText().equals(""))
			validateFields(); // method extracted
		else
			evaluationReportController.sendReportToServer(description.getText(), constraints.getText(),
					result.getText(), requestID);

	}
	/**
	 * clear Fields event handler
	 * @param event clear fields was pressed
	 */
	@FXML
	public void clearFieldsWasPressed(ActionEvent event) {
		additionalInfoPane.setVisible(false);
		description.clear();
		constraints.clear();
		result.clear();
		if (event == null)
			additionalInfo.clear();
	}

	/**
	 * method set validator
	 */
	private void setValdiator() {
		RequiredFieldValidator validator = new RequiredFieldValidator();

		description.getValidators().add(validator);
		constraints.getValidators().add(validator);
		result.getValidators().add(validator);

		validator.setMessage("*required");
	}

	/**
	 * This method validates the fields
	 */
	private void validateFields() {

		description.validate();
		constraints.validate();
		result.validate();

	}
	/**
	 * 
	 * set request id
	 * @param requestID request id
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	/**
	 * report event handler
	 * @param message message object cntain the request type
	 */
	public void reportWasSent(MessageObject message) {
		if ((boolean) message.getArgs().get(0)) {
			evaluationReportController.switchScene("ViewAllRequests");
			((ViewAllRequestsFX) evaluationReportController.getCurrentFX()).refreshWasPressed(null);
		} else
			JOptionPane.showMessageDialog(null, "Error occurred while trying to upload the report");
	}

	// --------------------------------------------------------------SidePanel
	// Buttons-----------------------------------------------------------------------------------------------------
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		evaluationReportController.ViewAllRequestsWasPressed(event);
	}

	/**
	 * This event handler switches scenes back to the Login page
	 * 
	 * @param event Button was Pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		evaluationReportController.logOutWasPressed(event);
	}

	/**
	 * This event handler switches scenes to Create New Request page
	 * 
	 * @param event Button was Pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		evaluationReportController.newChangeRequestWasPressed(event);
	}

	/**
	 * The Method adjust the report screen to view Report only
	 * Get the information of the report from the DB
	 */
	public void adjustScreenToViewEvaluatorReport() {
		this.submit.setVisible(false);
		this.submit.setDisable(true);
		this.clear.setVisible(false);
		this.clear.setDisable(true);
		evaluationReportController.loadReportDetails(this.requestID);
		
	}
	/**
	 * fill the report fields
	 * @param message contain EvaluatorReport of the request
	 */
	public void fillReportFilds(MessageObject message) {

		Platform.runLater(() -> {
		EvaluatorReport report = (EvaluatorReport) message.getArgs().get(0);
		description.setText(report.getDescription());
		constraints.setText(report.getConstraints());
		result.setText(report.getResult());
		description.setEditable(false);
		constraints.setEditable(false);
		result.setEditable(false);
		});
	}
	/**
	 * handle show additional information from DB
	 * @param message cintain the information from the server
	 */
	public void handleShowAdditionalInfo(MessageObject message) {
		String info = message.getArgs().get(0).toString();
		if (info == "") {
			additionalInfoPane.setVisible(false);
			return;
		}
		Platform.runLater(() -> {
			if (message.getArgs().size() == 4) {
				additionalInfo.setText(info);
				additionalInfoPane.setVisible(true);
				
				// Load Previously Written Info
				description.setText(message.getArgs().get(1).toString());
				constraints.setText(message.getArgs().get(2).toString());
				result.setText(message.getArgs().get(3).toString());
			}
		});
	}
}
