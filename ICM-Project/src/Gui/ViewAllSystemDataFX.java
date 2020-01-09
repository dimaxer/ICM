package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import LogicController.StatisticsReportController;
import LogicController.ViewAllRequestsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ViewAllSystemDataFX extends BaseFX {
	// Side-Panel *******************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;
	
	@FXML
	private VBox all_roles;
	
	// Main Buttons ******************************
	
	// Panes **************************************
	
	// Data-Storage Sets **************************

	// Controller *********************************
	private ViewAllRequestsController viewAllRequestsController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewAllRequestsController = new ViewAllRequestsController();
		// Content Here
		
		viewAllRequestsController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
	}
	
	public void clearFields() {
		// TODO Auto-generated method stub
		
	}
	
	
	/** This method switches the scene back to the main panel page */
	@FXML
	public void backWasPressed(ActionEvent event) {
		viewAllRequestsController.switchScene("Panel");
	}
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		viewAllRequestsController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		viewAllRequestsController.newChangeRequestWasPressed(event);
	}
	
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		viewAllRequestsController.ViewAllRequestsWasPressed(event);
	}
	
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		viewAllRequestsController.managePermissionsWasPressed(event);
	}
	
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		viewAllRequestsController.viewStatisticsReportWasPressed(event);
	}
}
