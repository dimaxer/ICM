package Gui;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.AttachedFile;
import LogicController.BasePanelController;
import LogicController.NewChangeRequestController;
import Utilities.MessageObject;
import Utilities.ScreenManager;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * New change request Fxml controller
 *
 */
public class NewChangeRequestFX extends BaseFX {

	@FXML
	private ComboBox<String> infoSysComboBox;

	/** ArrayList that contains the Names of Information Systems */
	private ArrayList<String> infoSysNames = new ArrayList<>();

	/** ArrayList that contains the IDs of current Evaluators */
	private ArrayList<String> currentEvaluatorIDs = new ArrayList<>();

	@FXML
	private Text fillComboBoxAlert;
	// private JFXTextField jfxinfSys;

	@FXML
	private JFXTextField jfxrequestedChange;

	@FXML
	private JFXTextField jfxsituation;

	@FXML
	private JFXTextField jfxreasons;

	@FXML
	private JFXTextField jfxnotes;

	@FXML
	private Button AddFiles;

	@FXML
	private Text fileName;

	@FXML
	private Button submit;

	@FXML
	private ListView listView;

	@FXML
	private JFXButton attachFiles;

	@FXML
	private JFXButton uploadFiles;

	@FXML
	private JFXButton back;

	@FXML
	private AnchorPane browseFiles;

	@FXML
	private JFXButton viewRequestDetails;

	@FXML
	private JFXButton newChangeRequest;

	// ISD START
	@FXML
	private JFXButton managePermissions;
	@FXML
	private JFXButton viewAllSystemData;
	@FXML
	private JFXButton viewStatisticsReport;

	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;

	@FXML
	private VBox all_roles;

	@FXML
	private JFXButton ManageApproves;

	private List<File> selectedFiles;

	private String date, evaluatorID = "", requestedChange, situation, changeReasons, notes;

	private AttachedFile attachedFile;

	private NewChangeRequestController newChangeRequestController;
	private ArrayList<Object> args;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AddFiles.setDefaultButton(true);
		submit.setDefaultButton(true);
		newChangeRequestController = new NewChangeRequestController();
		// panelController=new BasePanelController();
		args = new ArrayList<Object>();
		if (Client.getInstance().getCurrentUser().getJobDescription().equals("ISD Chief"))
			isdPane.setVisible(true);
		else
			isdPane.setVisible(false);

		newChangeRequestController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		initInformationSystemDetails(false);
	}

	/**
	 * 
	 * This method handles the form when submit was pressed
	 * @param event submit button was pressed
	 */
	@FXML
	public void submitWasPressed(ActionEvent event) {
		setValdiator();
		initStringFieldValues();

		if (infoSysComboBox.getValue() == "" || evaluatorID == "" || situation.isEmpty() || requestedChange.isEmpty())
			validateFields(); // method extracted
		else {
			args.clear();
			addFieldsToArgs();
			args.set(6, attachedFile);
			newChangeRequestController.submitWasPressed(args);

		}
	}
	/**
	 * inital String Fields values
	 */
	private void initStringFieldValues() {
		date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
		// infSys = jfxinfSys.getText();
		situation = jfxsituation.getText();
		requestedChange = jfxrequestedChange.getText();
		changeReasons = jfxreasons.getText();
		notes = jfxnotes.getText();
	}

	/**
	 * A method to initialize the Information Systems ComboBox for picking an
	 * Information System.
	 * @param idCanBeNull can id be null
	 */
	public void initInformationSystemDetails(Boolean idCanBeNull) {
		newChangeRequestController.initInformationSystemDetails(idCanBeNull);
	}

	/**
	 *  A method to handle the initialization of the Information Systems ComboBox for
	 * picking an Information System.
	 *
	 * @param message message object contain all inforamations of the information Systems
	 */
	public void handleInformationSystemComboBox(MessageObject message) {
		infoSysComboBox.getItems().clear();
		infoSysNames = (ArrayList<String>) message.getArgs().get(0);
		currentEvaluatorIDs = (ArrayList<String>) message.getArgs().get(1);
		infoSysComboBox.getItems().addAll(infoSysNames);
	}

	/**
	 * This method updates the Current Evaluator TextField whenever an item was
	 * chosen
	 * @param event the combo box was pressed
	 */
	@FXML
	public void updateCurrentEvaluator(ActionEvent event) {
		if (infoSysComboBox.getValue() == "")
			return;
		String currentEvaluatorDetails = infoSysComboBox.getValue();
		
		int currentEvaluatorDetailsIndex = infoSysNames.indexOf(currentEvaluatorDetails);
		
		if (currentEvaluatorDetailsIndex != -1) {
			currentEvaluatorDetails = currentEvaluatorIDs.get(currentEvaluatorDetailsIndex);
			evaluatorID = currentEvaluatorDetails;
		
			
		}
	}

	/**
	 * 	 This method adds the field values to args ArrayList
	 * 
	 * 
	 */	 
	private void addFieldsToArgs() {
		args.add(date);
		args.add(infoSysComboBox.getValue());
		args.add(requestedChange);
		args.add(situation);
		args.add(changeReasons);
		args.add(notes);
		args.add(null); // Attached File
		args.add("Initial");
		args.add("Active");
		args.add(Client.getInstance().getCurrentUser().getId());
		args.add(evaluatorID);
	}

	/**
	 * This method validates the fields
	 */
	private void validateFields() {
		// jfxinfSys.validate();
		if (infoSysComboBox.getValue() == "" || evaluatorID == "")
			fillComboBoxAlert.setVisible(true);
		else
			fillComboBoxAlert.setVisible(false);
		jfxsituation.validate();
		jfxrequestedChange.validate();
		jfxreasons.validate();
		jfxnotes.validate();
	}

	/**
	 * This method sets field validators
	 */
	public void setValdiator() {
		RequiredFieldValidator validator = new RequiredFieldValidator();

		// jfxinfSys.getValidators().add(validator);
		jfxsituation.getValidators().add(validator);
		jfxrequestedChange.getValidators().add(validator);
		jfxreasons.getValidators().add(validator);
		jfxnotes.getValidators().add(validator);

		validator.setMessage("*required");
	}
	/**
	 * back was pressed event handler
	 * @param event back button was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		newChangeRequestController.switchScene("Panel");
	}

	/**
	 * if the request was created successfully this method uploads the files to the
	 * server if there are any
	 * 
	 * @param msg contain all the information about the new request
	 */
	public void newCRHandler(MessageObject msg) {
		MessageObject message = (MessageObject) msg;
		if ((Boolean) message.getArgs().get(0))// [True|False]
		{
			String requestID = (String) message.getArgs().get(1);
			// upload all the files that were attached to the request
			if (selectedFiles != null)
				for (File file : selectedFiles)
					newChangeRequestController.sendFileToServer(file, requestID);

			newChangeRequestController.switchScene("Panel");
		}

	}
/**
 * add Files event handler
 * @param event add file button was pressed
 */
	@FXML
	public void addFilesWasPressed(ActionEvent event) {
		browseFiles.setVisible(true);
		if (listView.getItems() != null)
			listView.getItems().clear();
		if (selectedFiles != null)
			selectedFiles = null;

	}

	/**
	 * create a list of files that are chosen by the user
	 * 
	 * @param event attach file was pressed
	 */
	@FXML
	public void attachFilesWasPressed(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		selectedFiles = fileChooser.showOpenMultipleDialog(null);
		if (selectedFiles != null) {
			for (int i = 0; i < selectedFiles.size(); i++)
				listView.getItems().add(selectedFiles.get(i).getName());

		} else {
			System.out.println("no files were selected");
		}

	}
	/**
	 * close Attach files event handler
	 * @param event close attach files was pressed
	 */
	@FXML
	public void closeAttachFilesWasPressed(ActionEvent event) {
		browseFiles.setVisible(false);
	}

	/**
	 * A method to clear all the fields in this form
	 * 
	 */
	public void clearFields() {
		// TODO Auto-generated method stub
		// jfxinfSys.setText("");
		jfxsituation.setText("");
		jfxrequestedChange.setText("");
		jfxreasons.setText("");
		jfxnotes.setText("");
		fileName.setText("");
		selectedFiles = null;
		fillComboBoxAlert.setVisible(false);
	}

	// ---------------------------------side panel methods--------------------
/**
 * Manage approves event handler
 * @param event manage approves was pressed
 */
	@FXML
	public void ManageApprovesWasPressed(ActionEvent event) {
		newChangeRequestController.manageAprrovementWasPressed(event);
	}
	/**
	 * view all request event handler
	 * @param event view all request button was pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		newChangeRequestController.ViewAllRequestsWasPressed(event);
	}
	/**
	 * log out event handler
	 * @param event logout button was pressed 
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		newChangeRequestController.logOutWasPressed(event);
	}

	// ISD START
	/**
	 * Manage permanent roles (supervisor, committee), and Information System's
	 * evaluators.
	 * 
	 * @param event manage permisson was pressed
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		newChangeRequestController.managePermissionsWasPressed(event);
	}
/**
 * view all system data event handler
 * @param event view all system data button was pressed
 */
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		newChangeRequestController.viewAllSystemDataWasPressed(event);
	}
	/**
	 * view Statistics Report event handler
	 * @param event view staatistics report was pressed
	 */
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		newChangeRequestController.viewStatisticsReportWasPressed(event);
	}
	// ISD END
}
