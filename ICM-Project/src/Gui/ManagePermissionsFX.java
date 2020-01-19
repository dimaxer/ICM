package Gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.AttachedFile;
import LogicController.ManagePermissionsController;
import Utilities.MessageObject;
import client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This class serves as a FX controller for Manage Permissions Page.
 * @author Malka
 * @since 06/01/2020
 */
public class ManagePermissionsFX extends BaseFX {

	// Side-Panel *******************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;
	
	@FXML
	private VBox all_roles;
	
	// User List ********************************
	@FXML
	private JFXListView<String> userListView;
	
	// Non-Editable TextFields ******************
	@FXML
	private TextField supervisorText;
	
	@FXML
	private TextField committeeChairmanText;
	
	@FXML
	private TextField committeeMember1Text;
	
	@FXML
	private TextField committeeMember2Text;
	
	@FXML
	private TextField infoSysEvaluatorText;
	
	// Combo Boxes ******************************
	@FXML
	private ComboBox<String> infoSysComboBox;
	
	// Set Buttons ******************************
	@FXML
	private Button supervisorSetBtn;
	
	@FXML
	private Button committeeChairmanSetBtn;
	
	@FXML
	private Button committeeMember1SetBtn;
	
	@FXML
	private Button committeeMember2SetBtn;
	
	@FXML
	private Button infoSysEvaluatorSetBtn;
	
	// Main Buttons ******************************
	@FXML
	private Button updatePermRolesBtn;
	
	@FXML
	private Button updateEvaluatorBtn;
	
	@FXML
	private Text statusLabel;
	
	// Panes **************************************
	@FXML
	private AnchorPane permanentRolesPane;
	
	@FXML
	private AnchorPane evaluatorsPane;
	
	// Data-Storage Sets **************************
	/** HashMap that contains the IDs and Names of all users */
	HashMap<String, String> userDetails = new HashMap<>();
	/** HashMap that contains the IDs and Names of all users with permanent roles */
	HashMap<String, String> permanentRolesDetails = new HashMap<>();
	/** ArrayList that contains the Names of Information Systems */
	ArrayList<String> infoSysNames = new ArrayList<>();
	/** ArrayList that contains the IDs of current Evaluators */
	ArrayList<String> currentEvaluatorIDs = new ArrayList<>();
	/** CONSTANT DEFINING NULL */
	private static final String undefined = "NOT DEFINED";

	// Controller *********************************
	private ManagePermissionsController managePermissionsController;

	/** A method to clear all the fields in this form */
	public void clearFields() {
		infoSysEvaluatorText.setText("");
		infoSysComboBox.setValue("");
		statusLabel.setText("");
	}
	
	// Initialization Methods *********************
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updatePermRolesBtn.setDefaultButton(true);
		updateEvaluatorBtn.setDefaultButton(true);
		managePermissionsController = new ManagePermissionsController();
		managePermissionsController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		
		initUserDetails();
	}
	
	/** A method to initialize the Information Systems ComboBox for picking an Information System. 
	 * 
	 * @param idCanBeNull can id be null
	 */
	public void initInformationSystemDetails(Boolean idCanBeNull) {
		managePermissionsController.initInformationSystemDetails(idCanBeNull);
	}
	
	/** A method to handle the initialization of the Information Systems ComboBox for picking an Information System. 
	 * 
	 * @param message data
	 */
	public void handleInformationSystemComboBox(MessageObject message) {
		infoSysComboBox.getItems().clear();
		infoSysNames = (ArrayList<String>)message.getArgs().get(0);
		currentEvaluatorIDs = (ArrayList<String>)message.getArgs().get(1);
		infoSysComboBox.getItems().addAll(infoSysNames);
	}
	
	/** A method to initialize the Information Systems ComboBox for picking an Information System. */
	public void initPermanentRoles() {
		managePermissionsController.initPermanentRoles();
	}
	
	/** A method to handle the initialization of the Information Systems ComboBox for picking an Information System. 
	 * 
	 * @param message data
	 */
	public void handlePermanentRoles(MessageObject message) {
		permanentRolesDetails = (HashMap<String, String>)message.getArgs().get(0);
		updatePermRolesText();
	}
	
	/** A method to initialize the User Data HashMap. */
	public void initUserDetails() {
		if (userListView.getItems().size() > 0)
			userListView.getItems().clear();
		initInformationSystemDetails(true);
		initPermanentRoles();
		managePermissionsController.initUserDetails();
	}
	
	/** A method to handle the initialization of the User Data HashMap. 
	 * 
	 * @param message data
	 */
	public void handleUserDetails(MessageObject message) {
		userDetails = ((HashMap<String, String>)message.getArgs().get(0));
		Platform.runLater(() -> {
			initListView();
			initInformationSystemDetails(true);
			initPermanentRoles();
		});
	}
	
	/** A method to initialize the User List. */
	private void initListView() {
		Object[] userDetailsKeySet = (Object[]) userDetails.keySet().toArray();
		Platform.runLater(() -> {
			String item = "";
			for (Object key : userDetailsKeySet) {
				if (currentEvaluatorIDs.contains(key) || permanentRolesDetails.containsKey(key)) continue;
				item = key.toString() + " - " + userDetails.get(key.toString());
				userListView.getItems().add(item);
			}
		});
	}
	
	// Set methods ********************************
	/** This method handles setting the value of some textField when button was pressed 
	 * 
	 * @param textField text field
	 */
	public void textFieldSetBtnWasPressed(TextField textField) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (textField.getText().length() > 0 && !textField.getText().equalsIgnoreCase(undefined))
				userListView.getItems().add(textField.getText());
			userListView.getItems().remove(selected);
			textField.setText(selected);
		}
	}
	
	/** This method handles setting the value of supervisorText when button was pressed 
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void supervisorSetBtnWasPressed(ActionEvent event) {
		textFieldSetBtnWasPressed(supervisorText);
	}
	
	/** This method handles setting the value of committeeChairmanText when button was pressed 
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void committeeChairmanBtnWasPressed(ActionEvent event) {
		textFieldSetBtnWasPressed(committeeChairmanText);
	}
	
	/** This method handles setting the value of committeeMember1Text when button was pressed 
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void committeeMember1BtnWasPressed(ActionEvent event) {
		textFieldSetBtnWasPressed(committeeMember1Text);
	}
	
	/** This method handles setting the value of committeeMember2Text when button was pressed 
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void committeeMember2BtnWasPressed(ActionEvent event) {
		textFieldSetBtnWasPressed(committeeMember2Text);
	}
	
	/** This method handles setting the value of infoSysEvaluatorText when button was pressed
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void infoSysEvaluatorSetBtnWasPressed(ActionEvent event) {
		if (infoSysComboBox.getValue().length() > 0)
			textFieldSetBtnWasPressed(infoSysEvaluatorText);
	}
	
	// Update methods *****************************
	/** This method updates the Current Evaluator TextField whenever an item was chosen
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void updateCurrentEvaluator(ActionEvent event) {
		if (infoSysComboBox.getValue() == "")
			return;
		String currentEvaluatorDetails = infoSysComboBox.getValue();
		int currentEvaluatorDetailsIndex = infoSysNames.indexOf(currentEvaluatorDetails);
		if (currentEvaluatorDetailsIndex != -1) {
			currentEvaluatorDetails = currentEvaluatorIDs.get(currentEvaluatorDetailsIndex);
			if (!isValidTextField(currentEvaluatorDetails))
				infoSysEvaluatorText.setText(undefined);
			else {
				currentEvaluatorDetails = currentEvaluatorDetails + " - " + userDetails.get(currentEvaluatorDetails);
				infoSysEvaluatorText.setText(currentEvaluatorDetails);
			}
		}
	}
	
	/** This method updates the Permanent Roles current holders.
	 * 
	 */
	public void updatePermRolesText() {
		Platform.runLater(() -> {
		supervisorText.setText(undefined);
		committeeMember1Text.setText(undefined);
		committeeMember2Text.setText(undefined);
		committeeChairmanText.setText(undefined);
		String item = ""; // String value to set as text
		Boolean foundCommitteeMember = false; // Because there are two committee members
		Object[] keys = permanentRolesDetails.keySet().toArray();
		Object[] values = permanentRolesDetails.values().toArray();
		for (int i = 0; i < values.length; i++) {
			if (keys[i] == null)
				item = undefined;
			else
				item = keys[i].toString() + " - " + userDetails.get(keys[i]);
			switch (values[i].toString().toLowerCase()) {
				case "supervisor": supervisorText.setText(item); break;
				case "committee member":
					if (!foundCommitteeMember) {
 						 committeeMember1Text.setText(item);
 						 foundCommitteeMember = true;
					}
					else committeeMember2Text.setText(item);
					break;
				case "committee chairman": committeeChairmanText.setText(item); break;
			}
		}
		});
	}
	
	/** is valid text field
	 * 
	 * @param textField text field
	 * @return is valid
	 */
	public Boolean isValidTextField(TextField textField) {
		String value = textField.getText();
		return isValidTextField(value);
	}
	
	/** is valid text field
	 * 
	 * @param value text field value
	 * @return is valid
	 */
	public Boolean isValidTextField(String value) {
		if (value == null) return false;
		return !(value.equals(undefined) || value.equals(""));
	}
	
	/** This method handles the permanent roles form when update was pressed 
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void updatePermRolesWasPressed(ActionEvent event) {
		
		if (!isValidTextField(supervisorText) || !isValidTextField(committeeChairmanText) ||
			!isValidTextField(committeeMember1Text) || !isValidTextField(committeeMember2Text)) {
			statusLabel.setText("Please fill all permanent roles");
			return;
		}
		
		String supervisorID = getIDFromText(supervisorText),
			   committeeChairmanID = getIDFromText(committeeChairmanText),
			   committeeMember1ID = getIDFromText(committeeMember1Text),
			   committeeMember2ID = getIDFromText(committeeMember2Text);
		
		statusLabel.setText("Permanent Roles Updated!");
		managePermissionsController.updatePermanentRoles(supervisorID, committeeChairmanID, committeeMember1ID, committeeMember2ID);
	}
	
	/** This method handles the information system evaluators form when update was pressed
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void updateEvaluatorBtnWasPressed(ActionEvent event) {
		if (!isValidTextField(infoSysEvaluatorText) || !isValidTextField(infoSysComboBox.getValue())) {
			statusLabel.setText("Please select an Information System\nor set a valid evaluator");
			return;
		}
		
		String infoSysEvaluatorID = getIDFromText(infoSysEvaluatorText),
			   infoSysName = infoSysComboBox.getValue();
		
		statusLabel.setText("Evaluator Updated!");		
		managePermissionsController.updateEvaluator(infoSysName, infoSysEvaluatorID);
	}
	
	public String getIDFromText(TextField textField) {
		if (textField.getText().length() > 0) {
			return textField.getText().substring(0, textField.getText().indexOf(' '));
		} else
			return "";
	}
	
	// Switching methods **************************
	/** This method handles the switching between panes whenever a switch button was pressed
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void switchPaneBtnWasPressed(ActionEvent event) {
		clearFields();
		if (evaluatorsPane.isVisible()) {
			initPermanentRoles();
			permanentRolesPane.setVisible(true);
			evaluatorsPane.setVisible(false);
		} else {
			initInformationSystemDetails(true);
			permanentRolesPane.setVisible(false);
			evaluatorsPane.setVisible(true);
		}
		initUserDetails();
	}
	
	/** This method switches the scene back to the main panel page
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		managePermissionsController.switchScene("Panel");
	}
	
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event button was pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		managePermissionsController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event button was pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		managePermissionsController.newChangeRequestWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to view all requests
	 * @param event button was pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		managePermissionsController.ViewAllRequestsWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to view all system data
	 * @param event button was pressed
	 */
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		managePermissionsController.viewAllSystemDataWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to view statistics
	 * @param event button was pressed
	 */
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		managePermissionsController.viewStatisticsReportWasPressed(event);
	}
}
