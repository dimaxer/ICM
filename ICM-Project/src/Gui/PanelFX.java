package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import Common.User;
import LogicController.BasePanelController;
import Utilities.MessageObject;
import Utilities.ScreenManager;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/** This class serves as an FX controller to the main panel page.
 */
public class PanelFX extends BaseFX {
	// Class Buttons ***************************************************
	@FXML
	private JFXButton viewRequestDetails;
	@FXML
	private JFXButton viewRequestDetailsSupervisor;
	@FXML
	private JFXButton logOut;
	@FXML
	private JFXButton logOutSupervisor;
	@FXML
	private JFXButton newChangeRequest;
	
	@FXML
	private JFXButton newChangeRequestSupervisor;
	
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

	@FXML
	private Text Name;
	
	public void setName(String Name) {
		this.Name.setText(Name);
	}

	// Class variables *************************************************
	private BasePanelController panelController;
	// Class methods ***************************************************

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		panelController = new BasePanelController();
		setName(  Client.getInstance().getCurrentUser().getName() + "!");
		
		panelController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
	}
	/**
	 * view all request event handler
	 * @param event view all request was pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		panelController.ViewAllRequestsWasPressed(event);
	}

	/**
	 * This event handler switches scenes back to the Login page
	 * @param event logout was pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		panelController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event new change request was pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		panelController.newChangeRequestWasPressed(event);
	}
	/**
	 * Manage Approves event havdle 
	 * @param event Manege Approves was pressed
	 */
	@FXML
	public void ManageApprovesWasPressed(ActionEvent event) {
		panelController.manageAprrovementWasPressed(event);
	}
	
	// ISD START
	/**
	 * Manage permanent roles (supervisor, committee), and Information System's evaluators.
	 * @param event manage permissions event handler
	 * 
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		panelController.managePermissionsWasPressed(event);
	}
	/**
	 * view all system data event handler
	 * @param event view all system data was pressed
	 */
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		panelController.viewAllSystemDataWasPressed(event);
	}
	/**
	 * view all statistics report event handler
	 * @param event view all statistics report was pressed
	 */
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		panelController.viewStatisticsReportWasPressed(event);
	}
	// ISD END
}
