package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import LogicController.StatisticsReportController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class StatisticsReportFX extends BaseFX {
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
	private StatisticsReportController statisticsReportController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		statisticsReportController = new StatisticsReportController();
		// Content Here
		
		statisticsReportController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
	}
	
	public void clearFields() {
		// TODO Auto-generated method stub
		
	}

	@FXML
	public void ActivityReportWasPressed(ActionEvent event)
	{
		statisticsReportController.ActivityReportWasPressed(event);
	}
	
	/** This method switches the scene back to the main panel page */
	@FXML
	public void backWasPressed(ActionEvent event) {
		statisticsReportController.switchScene("Panel");
	}
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		statisticsReportController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		statisticsReportController.newChangeRequestWasPressed(event);
	}
	
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		statisticsReportController.ViewAllRequestsWasPressed(event);
	}
	
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		statisticsReportController.managePermissionsWasPressed(event);
	}
	
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		statisticsReportController.viewAllSystemDataWasPressed(event);
	}
}
