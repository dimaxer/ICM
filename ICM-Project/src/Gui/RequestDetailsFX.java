package Gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.AttachedFile;
import Common.Request;
import LogicController.RequestDetailsController;
import Utilities.MessageObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * This FX class represents a generic Request Details, which is hard-coded
 * altogether with the FXML to provide support for all Roles.
 * 
 * @author Raz Malka
 * @since 01/01/2020
 */
public class RequestDetailsFX extends BaseFX {

	// ____ _ _
	// | _ \ _ __ ___ _ __ ___ _ __ | |_ (_) ___ ___
	// | |_) | | '__| / _ \ | '_ \ / _ \ | '__| | __| | | / _ \ / __|
	// | __/ | | | (_) | | |_) | | __/ | | | |_ | | | __/ \__ \
	// |_| |_| \___/ | .__/ \___| |_| \__| |_| \___| |___/
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

	// Non Role-Specific Properties ************************************
	/*
	 * @FXML private Hyperlink attachment;
	 * 
	 * @FXML private Hyperlink evaluatorReport;
	 */

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
	private Text timeAssessmentLabel;
	@FXML
	private JFXButton changesApprovalBtn;
	@FXML
	private JFXButton timeAssessmentBtn;
	@FXML
	private JFXTextField extensionDays;

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
	private TextArea moreDetails;
	@FXML
	private Text executionLeaderReportLabel;

	// Supervisor Properties *******************************************
	@FXML
	private JFXButton freeze;
	@FXML
	private JFXButton unfreeze;
	@FXML
	private JFXButton closeRequest;
	@FXML
	private Tab timeAssessmentTab;
	@FXML
	private TableView<?> timeAssessmentTable;
	@FXML
	private JFXButton appointEmployeeBtn;
	@FXML
	private Text appointLabel;
	@FXML
	private Text supervisorReportLabel;
	@FXML
	private ComboBox<String> pickExecutionLeaderCBox;
	@FXML
	private ComboBox<String> pickEvaluatorCBox;
	@FXML
	private ComboBox<String> pickTesterCBox;

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

		// Supervisor - Hey Dima, Fill here the tableView in Time Assessment Tab (id:
		// timeAssessmentTable)
		requestDetailsController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
	}

	/**
	 * Initializes the Combo Boxes of this FX.
	 */
	private void initializeComboBoxes() {
		testStatusComboBox.getItems().addAll("Approved", "Rejected");

		// Supervisor - Hey Dima, Fill those Comboboxes
		/*
		 * pickExecutionLeaderCBox.getItems().addAll(collection);
		 * pickEvaluatorCBox.getItems().addAll(collection);
		 * pickTesterCBox.getItems().addAll(collection);
		 */
	}

	@FXML
	public void backWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("ViewAllRequests");
	}

	@FXML
	public void homeWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("Panel");
	}

	/**
	 * this method switches scene to view attached files of this specific request
	 * 
	 * @param event
	 */
	@FXML
	public void viewAttachedFilesWasPressed(ActionEvent event) {

		requestDetailsController.switchScene("ViewAttachedFiles");
		ViewAttachedFilesFX currentFX = (ViewAttachedFilesFX) requestDetailsController.getCurrentFX();
		currentFX.showAttachedFiles(requestID);
	}

	/**
	 * This method handles the case that the file attachment hyperlink was pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	/*
	 * @FXML public void attachmentHyperlinkWasPressed(ActionEvent event) throws
	 * IOException { if (attachedFile == null) return; attachedFile.open(); }
	 */

	/**
	 * This method handles the case that the evaluator report attachment hyperlink
	 * was pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	/*
	 * @FXML public void evaluatorReportHyperlinkWasPressed(ActionEvent event)
	 * throws IOException { if (evaluatorReportFile == null) return;
	 * evaluatorReportFile.open(); }
	 */

	/**
	 * Load all of the data into the form's fields
	 * 
	 * @param massage
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

		// loadAttachedFile(request);
	}

	/**
	 * Clears initializes role-specific fields
	 */
	public void clearFields() {
		// attachedFile = null;
		// evaluatorReportFile = null;

		clearComboBoxes();
		clearLabels();
		clearContainers();

	}

	/**
	 * Sub-method of clearFields(). Clears the ComboBoxes.
	 */
	private void clearComboBoxes() {
		pickExecutionLeaderCBox.setValue("");
		pickEvaluatorCBox.setValue("");
		pickTesterCBox.setValue("");
		testStatusComboBox.setValue("");
	}

	/**
	 * Sub-method of clearFields(). Clears the Labels.
	 */
	private void clearLabels() {
		rejectionDetailsLabel.setVisible(false);
		rejectionDetails.setVisible(false);
		fileName.setText("");
		supervisorReportLabel.setText("");
		testerReportSubmittionStatus.setText("");
		/*
		 * attachment.setText("None"); evaluatorReport.setText("None");
		 */
	}

	/**
	 * Sub-method of clearFields(). Clears the containers.
	 */
	private void clearContainers() {
		evaluatorPane.setVisible(false);
		testerPane.setVisible(false);
		executionLeaderPane.setVisible(false);
		committeePane.setVisible(false);
		evaluatorReportPane.setVisible(false);
		supervisorPane.setVisible(false);
		timeAssessmentTab.setDisable(true);
	}

	/**
	 * Load all non role-specific fields.
	 * 
	 * @param request
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

	}

	/**
	 * Loads the Attached File if there is one
	 * 
	 * @param event
	 */
	/*
	 * private void loadAttachedFile(Request request) { AttachedFile attachedFile =
	 * requestDetailsController.getAttachedFile(request); if (attachedFile == null)
	 * return;
	 * 
	 * this.attachedFile = attachedFile;
	 * 
	 * Platform.runLater (() ->
	 * {attachment.setText(attachedFile.getFile().getName());}); }
	 */

	/**
	 * Loads the Attached Evaluator Report File if there is one
	 * 
	 * @param event
	 */
	/*
	 * private void loadAttachedEvaluatorReportFile(String requestID) { AttachedFile
	 * attachedEvaluatorReportFile = requestDetailsController.getAttachedFile("E" +
	 * requestID); if (attachedEvaluatorReportFile == null) return;
	 * 
	 * this.evaluatorReportFile = attachedEvaluatorReportFile; Platform.runLater (()
	 * -> {evaluatorReport.setText(evaluatorReportFile.getFile().getName());}); }
	 */

	// Evaluator Methods ***********************************************
	/**
	 * Load all of the evaluator's fields.
	 * 
	 * @param request
	 */
	private void loadEvaluatorFields(Request request) {
		evaluatorPane.setVisible(true);
		reqIDForEvalReport = request.getRequestID();
	}

	/**
	 * This method handles the case that 'Submit Evaluator Report' button was
	 * pressed by the Evaluator
	 * 
	 * @param event
	 * @role Evaluator
	 */
	@FXML
	public void submitEvaluationReportWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("EvaluatorReportSubmition");
		((EvaluatorReportSubmitionFX) requestDetailsController.getCurrentFX()).setRequestID(requestID);

	}

	// Tester Methods **************************************************
	private void loadTesterFields(Request request) {
		testerPane.setVisible(true);
		evaluatorReportPane.setVisible(true);
		// loadAttachedEvaluatorReportFile(request.getRequestID());
	}

	@FXML
	private void testerReportButtonWasPressed(ActionEvent event) {
		if (testStatusComboBox.getValue() == "") {
			testerReportSubmittionStatus.setText("Please choose a status!");
			return;
		}
		if (testStatusComboBox.getValue() == "Rejected" && rejectionDetails.getText().isEmpty()) {
			testerReportSubmittionStatus.setText("Please Fill Details");
			return;
		}
		// SEND MSG
		// TO DB AND SUCH .... UPDATE STATUS AND DESCRIPTION
		testerReportSubmittionStatus.setText("Status Updated!");
	}

	@FXML
	private void testStatusComboBoxChanged(ActionEvent event) {
		switch (testStatusComboBox.getValue()) {
		case "Approved":
			rejectionDetailsLabel.setVisible(false);
			rejectionDetails.setVisible(false);
			testerReportSubmittionStatus.setText("");
			break;
		case "Rejected":
			rejectionDetailsLabel.setVisible(true);
			rejectionDetails.setVisible(true);
			testerReportSubmittionStatus.setText("Please Elaborate!");
			break;
		}
	}

	// Execution Leader Methods *******************************************
	private void loadExecutionLeaderFields(Request request) {
		executionLeaderPane.setVisible(true);
		// loadAttachedEvaluatorReportFile(request.getRequestID());
	}

	@FXML
	private void changesApprovalWasPressed(ActionEvent event) {
		changesApprovalLabel.setText("Changes Approval Updated!");
	}

	@FXML
	private void timeAssessmentBtnPressed(ActionEvent event) {
		timeAssessmentLabel.setText("Time Assessment Updated!");
	}

	// Committee Methods **************************************************
	private void loadCommitteeFields(Request request, String role) {
		committeePane.setVisible(true);
		evaluatorReportPane.setVisible(true);
		// loadAttachedEvaluatorReportFile(request.getRequestID());

		if (role.equals("Committee Chairman")) {
			appointAdhocBtn.setVisible(true);
			rejectRequestBtn.setVisible(true);
			approveRequestBtn.setVisible(true);
		} else {
			appointAdhocBtn.setVisible(false);
			rejectRequestBtn.setVisible(false);
			approveRequestBtn.setVisible(false);
		}
	}

	@FXML
	private void appointAdhocWasPressed(ActionEvent event) {
		executionLeaderReportLabel.setText("Appoint Ad-Hoc was Pressed!");
	}

	@FXML
	private void rejectRequestWasPressed(ActionEvent event) {
		executionLeaderReportLabel.setText("Reject Request was Pressed!");
	}

	@FXML
	private void approveRequestWasPressed(ActionEvent event) {
		executionLeaderReportLabel.setText("Approve Request was Pressed!");
	}

	@FXML
	private void submitMoreDetailsWasPressed(ActionEvent event) {
		executionLeaderReportLabel.setText("Request More Details was Pressed!");
	}

	// Supervisor Methods *************************************************
	private void loadSupervisorFields(Request request) {
		supervisorPane.setVisible(true);
		timeAssessmentTab.setDisable(false);
	}

	@FXML
	private void closeRequestWasPressed(ActionEvent event) {

	}

	@FXML
	private void freezeWasPressed(ActionEvent event) {

	}

	@FXML
	private void unfreezeWasPressed(ActionEvent event) {

	}

	@FXML
	private void appointEmployeeBtnWasPressed(ActionEvent event) {

	}

	@FXML
	private void employeeComboBoxChanged(ActionEvent event) {

	}

	@FXML
	private void roleComboBoxChanged(ActionEvent event) {

	}

	// Side Panel Methods *************************************************
	/**
	 * This event handler switches scenes to Create New Request page
	 * 
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		requestDetailsController.newChangeRequestWasPressed(event);
	}

	/**
	 * This event handler switches scenes to Login scene
	 * 
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		requestDetailsController.logOutWasPressed(event);
	}

	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		requestDetailsController.ViewAllRequestsWasPressed(event);
	}

	@FXML
	public void ManageApprovesWasPressed(ActionEvent event) {
		requestDetailsController.switchScene("ManageApproves");
	}

	// ISD START
	/**
	 * Manage permanent roles (supervisor, committee), and Information System's
	 * evaluators.
	 * 
	 * @author Raz Malka
	 * @param event
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		requestDetailsController.managePermissionsWasPressed(event);
	}

	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		requestDetailsController.viewAllSystemDataWasPressed(event);
	}

	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		requestDetailsController.viewStatisticsReportWasPressed(event);
	}
	// ISD END
}
