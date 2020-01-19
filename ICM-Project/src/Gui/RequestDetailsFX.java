package Gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.sound.midi.ControllerEventListener;
import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.AttachedFile;
import Common.Request;
import LogicController.RequestDetailsController;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import client.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * This FX class represents a generic Request Details, which is hard-coded
 * altogether with the FXML to provide support for all Roles.
 * 
 * @since 01/01/2020
 */
public class RequestDetailsFX extends BaseFX {

	// ____ _ _
	// | _ \ _ __ ___ _ __ ___ _ __ | |_ (_) ___ ___
	// | |_) | | '__| / _ \ | '_ \ / _ \ | '__| | __| | | / _ \ / __|
	// | __/ | | | (_) | | |_) | | __/ | | | |_ | | | __/ \__ \
	// |_| 
	// Class Properties ************************************************
	private RequestDetailsController requestDetailsController;

	// Side Bar Properties *********************************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;

	@FXML
	private VBox all_roles;

	@FXML
	private JFXButton ManageApproves;

	// FXML Properties *************************************************
	@FXML
	private TabPane tabPane;
	@FXML
	private JFXButton back;
	@FXML
	private JFXButton home;
	@FXML
	private JFXButton viewAttachedFiles;
	@FXML
	private Text textRequestID;
	@FXML
	private Text status;
	@FXML
	private Text stage;
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

	private String requestID;
	private String role;
	
	@FXML
	private JFXButton timeAssessmentBtn;
	@FXML
	private JFXButton timeExtensionBtn;
	@FXML
	private Text deadlineDateLabel;

	// Role AnchorPane Properties **************************************
	@FXML
	private AnchorPane evaluatorPane;
	@FXML
	private AnchorPane testerPane;
	@FXML
	private AnchorPane executionLeaderPane;
	@FXML
	private AnchorPane committeePane;
	@FXML
	private AnchorPane evaluatorReportPane;
	@FXML
	private AnchorPane supervisorPane;
	
	// Current Request *************************************************
	@FXML
	private Request currentRequest;

	// Evaluator Properties ********************************************
	@FXML
	private Text fileName;
	@FXML
	private JFXButton attach;
	private String reqIDForEvalReport = "";

	// Tester Properties ***********************************************
	@FXML
	private ComboBox<String> testStatusComboBox;
	@FXML
	private JFXButton submitTesterReportButton;
	@FXML
	private Text testerReportSubmittionStatus;
	@FXML
	private Text rejectionDetailsLabel;
	@FXML
	private TextArea rejectionDetails;

	// Execution Leader Properties *************************************
	@FXML
	private Text changesApprovalLabel;
	@FXML
	private JFXButton changesApprovalBtn;
	@FXML
	private JFXButton rejectionDetailsBtn;
	@FXML
	private AnchorPane rejectionDetailsPane;
	private String rejectionInfo;
	

	// Committee Properties ********************************************
	@FXML
	private JFXButton appointAdhocBtn;
	@FXML
	private JFXButton rejectRequestBtn;
	@FXML
	private JFXButton approveRequestBtn;
	@FXML
	private JFXButton submitMoreDetailsBtn;
	@FXML
	private JFXButton viewEvaluatorReport;
	@FXML
	private TextArea moreDetails;
	@FXML
	private Text committeeReportLabel;

	// Supervisor Properties *******************************************
	@FXML
	private JFXButton freeze;
	@FXML
	private JFXButton unfreeze;
	@FXML
	private JFXButton closeRequest;
	@FXML
	private Text supervisorReportLabel;

	private ArrayList<String> committeeDetails = new ArrayList<>();

	// __ __ _ _ _
	// | \/ | ___ | |_ | |__ ___ __| | ___
	// | |\/| | / _ \ | __| | '_ \ / _ \ / _` | / __|
	// | | | | | __/ | |_ | | | | | (_) | | (_| | \__ \
	// |_| |_| \___| \__| |_| |_| \___/ \__,_| |___/

	// Non Role-Specific Methods ***************************************
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		requestDetailsController = new RequestDetailsController();
		tabPane.getStyleClass().add("floating");

		initializeComboBoxes();

		requestDetailsController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
	}

	/**
	 * Initializes the Combo Boxes of this FX.
	 */
	private void initializeComboBoxes() {
		testStatusComboBox.getItems().addAll("Approved", "Rejected");
	}

	/**
	 * Back button was pressed
	 * @param event back
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("ViewAllRequests");
	}

	/**
	 * Home button was pressed
	 * @param event home
	 */
	@FXML
	public void homeWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("Panel");
	}

	/**
	 * this method switches scene to view attached files of this specific request
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void viewAttachedFilesWasPressed(ActionEvent event) {
		requestDetailsController.viewAttachedFiles(requestID);
	}

	/** Load all of the data into the form's fields\
	 * 
	 * @param message message
	 * @param role role
	 */
	public void loadRequest(MessageObject message, String role) {
		Request request = (Request) (message.getArgs().get(2));

		clearFields();
		loadNonRoleSpecificFields(request);
		
		switch (role) {
		case "Tester":
			loadTesterFields(request);
			break;
		case "Committee Chairman":
			loadCommitteeFields(request, role);
			break;
		case "Committee Member":
			loadCommitteeFields(request, role);
			break;
		case "Execution Leader":
			loadExecutionLeaderFields(request);
			break;
		case "Evaluator":
			loadEvaluatorFields(request);
			break;
		case "Supervisor":
			loadSupervisorFields(request);
			break;
		}
		this.role = role;
		initGetDeadline();
		initCommittee();
		initWaitsExecutionLeader();
	}

	/**
	 * Clears initializes role-specific fields
	 */
	public void clearFields() {
		clearComboBoxes();
		clearLabels();
		clearContainers();
	}
	
	/**
	 * Initialize the fields
	 */
	public void initializeFields() {
		initButtons();
		initTestRejectionInfo();
	}
	
	/** A method to initialize the Information Systems ComboBox for picking an Information System. */
	public void initCommittee() {
		requestDetailsController.initCommittee();
	}
	
	/** A method to handle the initialization of the Information Systems ComboBox for picking an Information System.
	 * 
	 * @param message message
	 */
	public void handleCommittee(MessageObject message) {
		committeeDetails.clear();
		committeeDetails.addAll((ArrayList<String>)message.getArgs().get(0));
		if (!currentRequest.getAdhoc()) {
			appointAdhocBtn.setDisable(false);
			requestDetailsController.setComponentDisability(true, submitTesterReportButton, testStatusComboBox, rejectionDetails);
		} else {
			appointAdhocBtn.setDisable(true);
			requestDetailsController.setComponentDisability(false, submitTesterReportButton, testStatusComboBox, rejectionDetails);
		}
	}
	
	/**
	 * Set Tester was pressed
	 */
	public void handleSetTester() {
		appointAdhocBtn.setDisable(true);
		requestDetailsController.setComponentDisability(false, submitTesterReportButton, testStatusComboBox, rejectionDetails);
	}
	
	/**
	 * Initialize set waiting for execution leader
	 */
	public void initSetWaitsExecutionLeader() {
		MessageObject msg = new MessageObject(RequestType.SetWaitsExecutionLeaderAppointment, new ArrayList<>());
		msg.getArgs().add(requestID);
		requestDetailsController.sendMessage(msg);
	}
	
	/**
	 * handle set waiting for execution leader
	 * @param message message
	 */
	public void handleSetWaitsExecutionLeader(MessageObject message) {
		requestDetailsController.setComponentDisability(true, approveRequestBtn, rejectRequestBtn, appointAdhocBtn, submitMoreDetailsBtn, moreDetails);
	}

	/**
	 * Initialize waiting for execution leader
	 */
	public void initWaitsExecutionLeader() {
		MessageObject msg = new MessageObject(RequestType.WaitsExecutionLeaderAppointment, new ArrayList<>());
		msg.getArgs().add(requestID);
		requestDetailsController.sendMessage(msg);
	}
	
	/**
	 * handle waiting for execution leader
	 * @param message message
	 */
	public void handleWaitsExecutionLeader(MessageObject message) {
		if (!(boolean)message.getArgs().get(0)) return;
		handleSetWaitsExecutionLeader(message);
	}
	
	/**
	 * Initialize deadline field
	 */
	public void initGetDeadline() {
		MessageObject msg = new MessageObject(RequestType.GetDeadline, new ArrayList<>());
		msg.getArgs().add(requestID);
		msg.getArgs().add(currentRequest.getCurrentStage());
		requestDetailsController.sendMessage(msg);
	}
	
	/**
	 * handle initialization of deadline field
	 * @param message message
	 * @throws ParseException exception
	 */
	public void handleGetDeadline(MessageObject message) throws ParseException {
		boolean shouldShow = !(role.equalsIgnoreCase("Supervisor") || role.equalsIgnoreCase("Initiator"));
		requestDetailsController.setComponentVisibility(false, timeAssessmentBtn, timeExtensionBtn);
		deadlineDateLabel.setText("Deadline Date:");

		if (message.getArgs().get(0) == null) {
			if (shouldShow) {
			requestDetailsController.setComponentVisibility(true, timeAssessmentBtn); // add if was rejected send mail / message (like noam did)
			if ((boolean)message.getArgs().get(1) && shouldShow)
				 timeAssessmentBtn.setDisable(true); // already sent
			else timeAssessmentBtn.setDisable(false);
			
			requestDetailsController.setComponentDisability(true, attach, changesApprovalBtn, rejectionDetailsBtn, submitTesterReportButton,
					testStatusComboBox, rejectionDetails, approveRequestBtn, rejectRequestBtn, appointAdhocBtn, submitMoreDetailsBtn, moreDetails);
			}
		}
		else {
			deadlineDateLabel.setVisible(true);
			deadlineDateLabel.setText("Deadline Date: " + message.getArgs().get(0).toString());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate deadline = LocalDate.parse(message.getArgs().get(0).toString(), dtf);
			LocalDate three_days_later_ldt = LocalDate.now().plusDays(3);
			if (deadline.isBefore(three_days_later_ldt) && shouldShow) {
				requestDetailsController.setComponentVisibility(true, timeExtensionBtn); // add if was rejected send mail / message (like noam did)
				if ((boolean)message.getArgs().get(2))
					 timeExtensionBtn.setDisable(true); // already sent
				else timeExtensionBtn.setDisable(false);
			}
			
			requestDetailsController.setComponentDisability(false, attach, changesApprovalBtn, rejectionDetailsBtn, submitTesterReportButton,
					testStatusComboBox, rejectionDetails, approveRequestBtn, rejectRequestBtn, appointAdhocBtn, submitMoreDetailsBtn, moreDetails);
		}
	}
	
	/**
	 * handles time assessment press
	 * @param event time assessment
	 */
	public void timeAssessmentWasPressed(ActionEvent event) {
		try {
		JFXAlert<Void> timeAssessmentPopup = new JFXAlert<Void>();
		AnchorPane anchorPane = new AnchorPane();
		TextArea node = new TextArea();
		
		// Set TextArea node
		node.textProperty().addListener(new ChangeListener<String>() { @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) node.setText(newValue.replaceAll("[^\\d]", ""));
		        if (newValue.length() > 2) node.setText(newValue.substring(0, 2)); }});
		node.setPrefHeight(20); node.setPrefWidth(30); node.setWrapText(true); node.setEditable(true); node.setLayoutX(50); node.setLayoutY(7);
		
		// Set Send Button
		Button send = new Button("Send");
		send.setOnAction((ActionEvent sendEvent) -> {
			if (node.getText().equals("")) { send.requestFocus(); return; }
			MessageObject response = new MessageObject(RequestType.NewTimeAssessment, new ArrayList());
			response.getArgs().add(requestID);
			response.getArgs().add(Client.getInstance().getCurrentUser().getId());
			switch (currentRequest.getCurrentStage()) {
			case "Initial": timeAssessmentPopup.close(); return;
			case "Evaluation": response.getArgs().add("Evaluator"); break;
			case "Decision": response.getArgs().add("Committee"); break;
			case "Execution": response.getArgs().add("Execution Leader"); break;
			case "Testing": response.getArgs().add("Tester"); break;
			default: break;
			}
			response.getArgs().add(node.getText());
			try {
				Client.getInstance().sendToServer(response);
			} catch (IOException e) {e.printStackTrace();}
			timeAssessmentPopup.close();
			requestDetailsController.switchScene("ViewAllRequests");
			((ViewAllRequestsFX) ScreenManager.getInstance().getCurrentFX()).refreshWasPressed(null);
		});
		send.setLayoutX(100); send.setLayoutY(12);
		anchorPane.getChildren().addAll(node, send);
		
		// Set JFXAlert Dialogue
		timeAssessmentPopup.setTitle("Assessment (Days):"); timeAssessmentPopup.setContent(anchorPane); timeAssessmentPopup.setSize(180, 50); timeAssessmentPopup.showAndWait();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * handles time extension press
	 * @param event time extension
	 */
	public void timeExtensionWasPressed(ActionEvent event) {
		try {
		JFXAlert<Void> timeExtensionPopup = new JFXAlert<Void>();
		AnchorPane anchorPane = new AnchorPane();
		TextArea node = new TextArea();
		
		// Set TextArea node
		node.textProperty().addListener(new ChangeListener<String>() { @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) node.setText(newValue.replaceAll("[^\\d]", ""));
		        if (newValue.length() > 2) node.setText(newValue.substring(0, 2)); }});
		node.setPrefHeight(20); node.setPrefWidth(30); node.setWrapText(true); node.setEditable(true); node.setLayoutX(50); node.setLayoutY(7);
		
		// Set Send Button
		Button send = new Button("Send");
		send.setOnAction((ActionEvent sendEvent) -> {
			if (node.getText().equals("")) { send.requestFocus(); return; }
			MessageObject response = new MessageObject(RequestType.NewTimeExtension, new ArrayList());
			response.getArgs().add(requestID);
			response.getArgs().add(Client.getInstance().getCurrentUser().getId());
			switch (currentRequest.getCurrentStage()) {
			case "Initial": timeExtensionPopup.close(); return;
			case "Evaluation": response.getArgs().add("Evaluator"); break;
			case "Decision": response.getArgs().add("Committee"); break;
			case "Execution": response.getArgs().add("Execution Leader"); break;
			case "Testing": response.getArgs().add("Tester"); break;
			default: break;
			}
			response.getArgs().add(currentRequest.getCurrentStage());
			response.getArgs().add(node.getText());
			try {
				Client.getInstance().sendToServer(response);
			} catch (IOException e) {e.printStackTrace();}
			
			timeExtensionPopup.close();
			requestDetailsController.switchScene("ViewAllRequests");
			((ViewAllRequestsFX) ScreenManager.getInstance().getCurrentFX()).refreshWasPressed(null);
		});
		send.setLayoutX(100); send.setLayoutY(12);
		anchorPane.getChildren().addAll(node, send);
		
		// Set JFXAlert Dialogue
		timeExtensionPopup.setTitle("Extension (Days):"); timeExtensionPopup.setContent(anchorPane); timeExtensionPopup.setSize(180, 50); timeExtensionPopup.showAndWait();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Initialize test rejection info
	 */
	private void initTestRejectionInfo() {
		requestDetailsController.initTestRejectionInfo(requestID);
	}
	
	/**
	 * handle test rejection info
	 * @param message message
	 */
	public void handleTestRejectionInfo(MessageObject message) {
		try {
		rejectionInfo = (String) message.getArgs().get(0).toString();
		if (!rejectionInfo.equalsIgnoreCase("null") && rejectionInfo.length() > 0)
			rejectionDetailsPane.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** This method initializes the methods according to the relevant stage and status
	 * 
	 */
	private void initButtons() {
		if (currentRequest == null) return;
		if (currentRequest.getCurrentStage().equals("Closing")) {
			if (currentRequest.getRequestStatus().equals("Closed"))
				requestDetailsController.setComponentVisibility(false, unfreeze, freeze, closeRequest);
			else {
				 requestDetailsController.setComponentVisibility(false, unfreeze, freeze);
				 requestDetailsController.setComponentVisibility(true, closeRequest);
			}
		}
		else {
			requestDetailsController.setComponentVisibility(true, unfreeze, freeze);
			requestDetailsController.setComponentVisibility(false, closeRequest);
		}
	}

	/**
	 * Sub-method of clearFields(). Clears the ComboBoxes.
	 */
	private void clearComboBoxes() {
		testStatusComboBox.setValue("");
	}

	/**
	 * Sub-method of clearFields(). Clears the Labels.
	 */
	private void clearLabels() {
		requestDetailsController.setComponentVisibility(false, rejectionDetails, rejectionDetailsLabel);
		fileName.setText("");
		supervisorReportLabel.setText("");
		testerReportSubmittionStatus.setText("");
	}

	/**
	 * Sub-method of clearFields(). Clears the containers.
	 */
	private void clearContainers() {
		requestDetailsController.setComponentVisibility(false, evaluatorPane, testerPane, executionLeaderPane,
														committeePane, evaluatorReportPane, supervisorPane, rejectionDetailsPane);
	}

	/**
	 * Load all non role-specific fields.
	 * 
	 * @param request request
	 */
	private void loadNonRoleSpecificFields(Request request) {
		textRequestID.setText("Request ID:	" + request.getRequestID());
		status.setText("Status:		" + request.getRequestStatus());
		stage.setText("Stage:		" + request.getCurrentStage());

		requestedInformationSystem.setText(request.getInformationSystem());
		currentStiuation.setText(request.getCurrentSituation());
		requestedChange.setText(request.getRequestedChange());
		reason.setText(request.getReasonForRequest());
		notes.setText(request.getNote());
		requestID = request.getRequestID();
		currentRequest = request;
	}
	
	// Evaluator Methods ***********************************************
	/**
	 * Load all of the evaluator's fields.
	 * 
	 * @param request request
	 */
	private void loadEvaluatorFields(Request request) {
		evaluatorPane.setVisible(true);
		reqIDForEvalReport = request.getRequestID();
	}

	/**
	 * This method handles the case that 'Submit Evaluator Report' button was
	 * pressed by the Evaluator
	 * 
	 * @param event event
	 */
	@FXML
	public void submitEvaluationReportWasPressed(ActionEvent event) {
		requestDetailsController.submitEvaluationReport(requestID);
	}

	// Tester Methods **************************************************
	private void loadTesterFields(Request request) {
		requestDetailsController.setComponentVisibility(true, testerPane, evaluatorReportPane);
	}

	@FXML
	private void testerReportButtonWasPressed(ActionEvent event) {
		if (testStatusComboBox.getValue().equals("")) {
			testerReportSubmittionStatus.setText("Please choose a status!"); return;
		}
		if (testStatusComboBox.getValue().equals("Rejected"))
			if (rejectionDetails.getText().isEmpty()) { testerReportSubmittionStatus.setText("Please Fill Details"); return; }
			else { requestDetailsController.testerReportRejected(requestID, rejectionDetails.getText(), currentRequest.getCurrentStage()); return; }
		else if (testStatusComboBox.getValue().equals("Approved"))
			requestDetailsController.testerReportApproved(requestID, currentRequest.getCurrentStage(), currentRequest.getInitaitorID());
		
		testerReportSubmittionStatus.setText("Status Updated!");
	}

	@FXML
	private void testStatusComboBoxChanged(ActionEvent event) {
		switch (testStatusComboBox.getValue()) {
		case "Approved":
			requestDetailsController.setComponentVisibility(false, rejectionDetails, rejectionDetailsLabel);
			testerReportSubmittionStatus.setText("");
			break;
		case "Rejected":
			requestDetailsController.setComponentVisibility(true, rejectionDetails, rejectionDetailsLabel);
			testerReportSubmittionStatus.setText("Please Elaborate!");
			break;
		}
	}

	// Execution Leader Methods *******************************************
	private void loadExecutionLeaderFields(Request request) {
		executionLeaderPane.setVisible(true);
	}

	@FXML
	private void changesApprovalWasPressed(ActionEvent event) {
		changesApprovalLabel.setText("Changes Approval Updated!");
		requestDetailsController.changeApproval(requestID, currentRequest.getCurrentStage(), currentRequest.getAdhoc());
	}
	
	/** This method displays to the execution leader the details of test rejection */
	@FXML
	private void rejectionDetailsBtnPressed(ActionEvent event) {
		TextArea node = new TextArea(rejectionInfo);
		node.setPrefHeight(300);
		node.setWrapText(true);
		node.setEditable(false);
		
		JFXAlert<Void> rejectionDetailsPopup = new JFXAlert<Void>();
		rejectionDetailsPopup.setTitle("Test Rejection Details: ");
		rejectionDetailsPopup.setContent(node);
		rejectionDetailsPopup.setSize(400, 200);
		rejectionDetailsPopup.showAndWait();
	}

	// Committee Methods **************************************************
	private void loadCommitteeFields(Request request, String role) {
		requestDetailsController.setComponentVisibility(true, committeePane, evaluatorReportPane);

		if (role.equals("Committee Chairman"))
			 requestDetailsController.setComponentVisibility(true, rejectRequestBtn, approveRequestBtn);
		else requestDetailsController.setComponentVisibility(false, rejectRequestBtn, approveRequestBtn);
	}

	@FXML
	private void appointAdhocWasPressed(ActionEvent event) {
		JFXAlert<Void> rejectionDetailsPopup = new JFXAlert<Void>();
		AnchorPane anchorPane = new AnchorPane();
		ComboBox node = new ComboBox();
		
		// Set ComboBox node
		node.setPrefWidth(150);
		node.setLayoutX(7);
		node.setLayoutY(7);
		for (int i = 0; i < committeeDetails.size(); i += 2)
			node.getItems().add(committeeDetails.get(i) + " - " + committeeDetails.get(i+1));
		
		// Set Send Button
		Button send = new Button("Update");
		send.setOnAction((ActionEvent sendEvent) -> {
			if (node.getValue().equals("")) {
				send.requestFocus();
				return;
			}
			MessageObject response = new MessageObject(RequestType.SetTester, new ArrayList());
			response.getArgs().add(requestID);
			response.getArgs().add(committeeDetails.get(2*node.getSelectionModel().getSelectedIndex()));
			try {
				Client.getInstance().sendToServer(response);
			} catch (IOException e) {e.printStackTrace();}
			rejectionDetailsPopup.close();
		});
		send.setLayoutX(50);
		send.setLayoutY(50);
		
		anchorPane.getChildren().addAll(node, send);
		
		// Set JFXAlert Dialogue
		rejectionDetailsPopup.setTitle("Select Tester: ");
		rejectionDetailsPopup.setContent(anchorPane);
		rejectionDetailsPopup.setSize(160, 100);
		rejectionDetailsPopup.showAndWait();
		requestDetailsController.switchScene("ViewAllRequests");
		((ViewAllRequestsFX) requestDetailsController.getCurrentFX()).refreshWasPressed(null);
	}

	@FXML
	private void rejectRequestWasPressed(ActionEvent event) {
		requestDetailsController.committeeRejectRequest(requestID, currentRequest.getCurrentStage(), currentRequest.getInitaitorID());
	}

	/**
	 * switch scene to view all request and remove the access of the commity to the request
	 * @param event
	 */
	@FXML
	private void approveRequestWasPressed(ActionEvent event) {
		//requestDetailsController.committeeApprovesRequest(requestID, currentRequest.getCurrentStage());
		initSetWaitsExecutionLeader();
	}

	@FXML
	private void submitMoreDetailsWasPressed(ActionEvent event) {
		if (moreDetails.getText().toString().equalsIgnoreCase("null") || moreDetails.getText().toString().equalsIgnoreCase("")) {
			moreDetails.setPromptText("Please fill what details you want.");
			submitMoreDetailsBtn.requestFocus();
			return;
		}
		requestDetailsController.submitMoreDetails(requestID, moreDetails.getText());
	}
	
	/**
	 * switch scene to EvaluatorReportSubmition where we can only view the report
	 * @param event
	 */
	@FXML
	private void viewEvaluatorReportWasPressed(ActionEvent event) {
		requestDetailsController.viewEvaluatorReport(requestID);
	}
	
	

	// Supervisor Methods *************************************************
	private void loadSupervisorFields(Request request) {
		supervisorPane.setVisible(true);
	}

	@FXML
	private void closeRequestWasPressed(ActionEvent event) {
		status.setText(status.getText().replace("Frozen", "Closed"));
		status.setText(status.getText().replace("Active", "Closed"));
		requestDetailsController.supervisorClosesRequest(requestID, currentRequest.getRequestStatus());
	}

	@FXML
	private void freezeWasPressed(ActionEvent event) {
		status.setText(status.getText().replace("Active", "Frozen"));
		requestDetailsController.supervisorFreeze(requestID, currentRequest.getRequestStatus());
	}

	@FXML
	private void unfreezeWasPressed(ActionEvent event) {
		status.setText(status.getText().replace("Frozen", "Active"));
		requestDetailsController.supervisorUnfreeze(requestID, currentRequest.getRequestStatus());
	}

	// Side Panel Methods *************************************************
	/**
	 * This event handler switches scenes to Create New Request page
	 * 
	 * @param event event new change request
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		requestDetailsController.newChangeRequestWasPressed(event);
	}

	/**
	 * This event handler switches scenes to Login scene
	 * 
	 * @param event event logout
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		requestDetailsController.logOutWasPressed(event);
	}

	/**
	 * This event handles switches scene to view all requests
	 * @param event view all requests
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		requestDetailsController.ViewAllRequestsWasPressed(event);
	}

	/**
	 * This event handles switches scene to manage approves
	 * @param event manage approves
	 */
	@FXML
	public void ManageApprovesWasPressed(ActionEvent event) {
		requestDetailsController.manageAprrovementWasPressed(null);
	}

	// ISD START
	/**
	 * Manage permanent roles (supervisor, committee), and Information System's
	 * evaluators.
	 * 

	 * @param event manage permissions
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		requestDetailsController.managePermissionsWasPressed(event);
	}

	/**
	 * This event handles switches scene to view all system data
	 * @param event view all system data
	 */
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		requestDetailsController.viewAllSystemDataWasPressed(event);
	}

	/**
	 * This event handles switches scene to view all statistics
	 * @param event view all statistics
	 */
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		requestDetailsController.viewStatisticsReportWasPressed(event);
	}
	// ISD END
}
