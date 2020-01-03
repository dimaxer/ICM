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
import javafx.scene.text.Text;

/**
 * @LastChanged Raz Malka
 */
public class PanelFX implements BaseFx {
	// Class Buttons ***************************************************
	@FXML
	private JFXButton viewRequestDetails;
	@FXML
	private JFXButton logOut;
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
	// ISD END
	@FXML
	private Text Name;

	public void setName(String Name) {
		this.Name.setText(Name);
	}

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
		panelController = new BasePanelController();
		setName("Welcome, " + Client.getInstance().getCurrentUser().getName() + "!");
		
		if (Client.getInstance().getCurrentUser().getJobDescription().equals("ISD Chief"))
			 isdPane.setVisible(true);
		else isdPane.setVisible(false);
	}

	/**
	 * This event handler switches scene to View Request Details page
	 * @param event
	 */
	@FXML
	public void viewRequestDetailsWasPressed(ActionEvent event) {
		if (panelController.sceneExists("ViewAllRequests"))
			 panelController.switchScene("ViewAllRequests");
		else panelController.viewRequestDetailsWasPressed();
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
		panelController.switchScene("Login");
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
	
	// ISD START
	/**
	 * Manage roles of Supervisor and Committee
	 * @param event
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {

	}
	
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		
	}
	
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		
	}
	// ISD END

}
