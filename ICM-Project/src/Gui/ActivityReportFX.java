package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;


import LogicController.ActivityReportController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ActivityReportFX extends BaseFX {
	
	
@FXML	
private JFXComboBox<String> calculationBy;
	

	// --------------------Date Parmeters------------------------------
	@FXML
	private JFXDatePicker startDate;

	@FXML
	private JFXDatePicker endDate;

	@FXML
	private JFXButton createNewReport;
	// -------------------------------Panel parmeters------------------------------
	// Side-Panel *******************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;

	@FXML
	private VBox all_roles;

	private ActivityReportController activityReportController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		activityReportController = new ActivityReportController();
		activityReportController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		initCalculationBy();
		
	}

	public void initCalculationBy()
	{
		ObservableList<String> List=FXCollections.observableArrayList("Day","Month","Year");
		calculationBy.getItems().addAll(List);
	}
	@FXML
	public void createNewReportWasPressed(ActionEvent event) {
		String calculationByText=calculationBy.getSelectionModel().getSelectedItem();
		if(calculationByText.equals("Month"))
		activityReportController.createNewReport(calculationByText,startDate,endDate);
		
	}

	@FXML
	public void clearFields() {
		initCalculationBy();
		startDate.getEditor().clear();
		startDate.setValue(null);
		endDate.getEditor().clear();
		endDate.setValue(null);
	}
	
	@FXML
	public void backWasPressed(ActionEvent event) {
		activityReportController.switchScene("StatisticsReport");
		
	}

//----------------------------------------------PanelMethods-------------------------------------
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		activityReportController.newChangeRequestWasPressed(event);
	}

	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		activityReportController.ViewAllRequestsWasPressed(event);
	}

	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		activityReportController.managePermissionsWasPressed(event);
	}

	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		activityReportController.viewAllSystemDataWasPressed(event);
	}

}
