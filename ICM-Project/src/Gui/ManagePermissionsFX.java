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
	/** Thread that initializes the user list, comboboxes and TextFields in parrallel */
	/** ArrayList that contains the Names of Information Systems */
	ArrayList<String> infoSysNames = new ArrayList<>();
	/** ArrayList that contains the IDs of current Evaluators */
	ArrayList<String> currentEvaluatorIDs = new ArrayList<>();

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
	
	/** A method to initialize the Information Systems ComboBox for picking an Information System. */
	public void initInformationSystemDetails(Boolean idCanBeNull) {
		managePermissionsController.initInformationSystemDetails(idCanBeNull);
	}
	
	/** A method to handle the initialization of the Information Systems ComboBox for picking an Information System. */
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
	
	/** A method to handle the initialization of the Information Systems ComboBox for picking an Information System. */
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
	
	/** A method to handle the initialization of the User Data HashMap. */
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
	/** This method handles setting the value of supervisorText when <- was pressed */
	@FXML
	public void supervisorSetBtnWasPressed(ActionEvent event) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (supervisorText.getText().length() > 0) {
				String supervisorID = supervisorText.getText().substring(0, supervisorText.getText().indexOf(' '));
				userListView.getItems().add(supervisorID + " - " + userDetails.get(supervisorID));
			}
			userListView.getItems().remove(selected);
			supervisorText.setText(selected);
		}
	}
	
	/** This method handles setting the value of committeeChairmanText when <- was pressed */
	@FXML
	public void committeeChairmanBtnWasPressed(ActionEvent event) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (committeeChairmanText.getText().length() > 0) {
				String committeeChairmanID = committeeChairmanText.getText().substring(0, committeeChairmanText.getText().indexOf(' '));
				userListView.getItems().add(committeeChairmanID + " - " + userDetails.get(committeeChairmanID));
			}
			userListView.getItems().remove(selected);
			committeeChairmanText.setText(selected);
		}
	}
	
	/** This method handles setting the value of committeeMember1Text when <- was pressed */
	@FXML
	public void committeeMember1BtnWasPressed(ActionEvent event) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (committeeMember1Text.getText().length() > 0) {
				String committeeMember1ID = committeeMember1Text.getText().substring(0, committeeMember1Text.getText().indexOf(' '));
				userListView.getItems().add(committeeMember1ID + " - " + userDetails.get(committeeMember1ID));
			}
			userListView.getItems().remove(selected);
			committeeMember1Text.setText(selected);
		}
	}
	
	/** This method handles setting the value of committeeMember2Text when <- was pressed */
	@FXML
	public void committeeMember2BtnWasPressed(ActionEvent event) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (committeeMember2Text.getText().length() > 0) {
				String committeeMember2ID = committeeMember2Text.getText().substring(0, committeeMember2Text.getText().indexOf(' '));
				userListView.getItems().add(committeeMember2ID + " - " + userDetails.get(committeeMember2ID));
			}
			userListView.getItems().remove(selected);
			committeeMember2Text.setText(selected);
		}
	}
	
	/** This method handles setting the value of infoSysEvaluatorText when <- was pressed */
	@FXML
	public void infoSysEvaluatorSetBtnWasPressed(ActionEvent event) {
		String selected = userListView.getSelectionModel().getSelectedItem();
		if (selected != null && infoSysComboBox.getValue().length() > 0) {
			if (infoSysEvaluatorText.getText().length() > 0) {
				String infoSysEvaluatorID = infoSysEvaluatorText.getText().substring(0, infoSysEvaluatorText.getText().indexOf(' '));
				userListView.getItems().add(infoSysEvaluatorID + " - " + userDetails.get(infoSysEvaluatorID));
			}
			userListView.getItems().remove(selected);
			infoSysEvaluatorText.setText(selected);
		}
	}
	
	// Update methods *****************************
	/** This method updates the Current Evaluator TextField whenever an item was chosen */
	@FXML
	public void updateCurrentEvaluator(ActionEvent event) {
		if (infoSysComboBox.getValue() == "")
			return;
		String currentEvaluatorDetails = infoSysComboBox.getValue();
		int currentEvaluatorDetailsIndex = infoSysNames.indexOf(currentEvaluatorDetails);
		if (currentEvaluatorDetailsIndex != -1) {
			currentEvaluatorDetails = currentEvaluatorIDs.get(currentEvaluatorDetailsIndex);
			currentEvaluatorDetails = currentEvaluatorDetails + " - " + userDetails.get(currentEvaluatorDetails);
			infoSysEvaluatorText.setText(currentEvaluatorDetails);
		}
	}
	
	/** This method updates the Permanent Roles current holders. */
	public void updatePermRolesText() {
		String item = ""; // String value to set as text
		Boolean foundCommitteeMember = false; // Because there are two committee members
		Object[] keys = permanentRolesDetails.keySet().toArray();
		Object[] values = permanentRolesDetails.values().toArray();
		for (int i = 0; i < values.length; i++) {
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
	}
	
	/** This method handles the permanent roles form when update was pressed */
	@FXML
	public void updatePermRolesWasPressed(ActionEvent event) {
		if (supervisorText.getText().length() <= 0 || committeeChairmanText.getText().length() <= 0 ||
			committeeMember1Text.getText().length() <= 0 || committeeMember2Text.getText().length() <= 0) {
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
	
	/** This method handles the information system evaluators form when update was pressed */
	@FXML
	public void updateEvaluatorBtnWasPressed(ActionEvent event) {
		if (infoSysEvaluatorText.getText().length() <= 0 || infoSysComboBox.getValue() == "") {
			statusLabel.setText("Please select an Information System");
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
	/** This method handles the switching between panes whenever a switch button was pressed */
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
	
	/** This method switches the scene back to the main panel page */
	@FXML
	public void backWasPressed(ActionEvent event) {
		managePermissionsController.switchScene("Panel");
	}
	
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		managePermissionsController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		managePermissionsController.newChangeRequestWasPressed(event);
	}
	
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		managePermissionsController.ViewAllRequestsWasPressed(event);
	}
	
	
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		managePermissionsController.viewAllSystemDataWasPressed(event);
	}
	
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		managePermissionsController.viewStatisticsReportWasPressed(event);
	}
}
