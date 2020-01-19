package Gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXAlert;

import Common.Interval;
import LogicController.StatisticsReportController;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import Utilities.Statistics;
import client.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/** This class represents FX Controller of Statistics */
public class StatisticsReportFX extends BaseFX {
	// Side-Panel *********************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;
	
	@FXML
	private VBox all_roles;
	
	// Request Report by Status *******************
	@FXML
	private StackPane statusGraphPane;
	
	@FXML
	private DatePicker statusFrom;
	@FXML
	private DatePicker statusTo;
	@FXML
	private ComboBox<String> statusComboBox;
	@FXML
	private Button statusIssueReportBtn;
	@FXML
	private Text statusIssueReportLabel;
	@FXML
	private Text statusStdLabel;
	@FXML
	private Text statusMedianLabel;
	
	// Request Report by Rejection ****************
	@FXML
	private StackPane rejectionGraphPane;
	
	@FXML
	private DatePicker rejectionFrom;
	@FXML
	private DatePicker rejectionTo;
	@FXML
	private Button rejectionIssueReportBtn;
	@FXML
	private Text rejectionIssueReportLabel;
	@FXML
	private Text rejectionStdLabel;
	@FXML
	private Text rejectionMedianLabel;
	
	// Request Report by Active Days ****************
	@FXML
	private StackPane activeDaysGraphPane;
	
	@FXML
	private DatePicker activeDaysFrom;
	@FXML
	private DatePicker activeDaysTo;
	@FXML
	private Button activeDaysIssueReportBtn;
	@FXML
	private Text activeDaysIssueReportLabel;
	@FXML
	private Text activeDaysStdLabel;
	@FXML
	private Text activeDaysMedianLabel;
	
	// Request Report by Extensions ***************
	@FXML
	private StackPane extensionsGraphPane;
	@FXML
	private Button extensionsIssueReportBtn;
	@FXML
	private Text extensionsIssueReportLabel;
	@FXML
	private Text extensionsStdLabel;
	@FXML
	private Text extensionsMedianLabel;
	
	// Request Report by Durations ****************
	@FXML
	private StackPane durationsGraphPane;
	@FXML
	private Button durationsIssueReportBtn;
	@FXML
	private Text durationsIssueReportLabel;
	@FXML
	private Text durationsStdLabel;
	@FXML
	private Text durationsMedianLabel;
	
	// Request Report by Delays *******************
	@FXML
	private StackPane delaysGraphPane;
	@FXML
	private Button delaysIssueReportBtn;
	@FXML
	private Text delaysIssueReportLabel;
	@FXML
	private Text delaysStdLabel;
	@FXML
	private Text delaysMedianLabel;
	@FXML
	private ComboBox<String> delaysComboBox;
	
	// Tab Panes **********************************
	@FXML
	private TabPane activityTabPane;
	@FXML
	private TabPane performancesTabPane;
	@FXML
	private TabPane delaysTabPane;
	@FXML
	private TabPane historyTabPane;
	
	// Tab Pane Buttons ***************************
	@FXML
	private Button activityBtn;
	@FXML
	private Button performanceBtn;
	@FXML
	private Button delayBtn;
	@FXML
	private Button historyBtn;
	
	// History ************************************
	@FXML
	private StackPane historyGraphPane;
	@FXML
	private Button historyIssueReportBtn;
	@FXML
	private Text historyIssueReportLabel;
	@FXML
	private Text historyStdLabel;
	@FXML
	private Text historyMedianLabel;

	// Reports ArrayList **************************
	ArrayList<ArrayList<Interval>> allReports = new ArrayList<>();
	ArrayList<String> allReportsNames = new ArrayList<>();
	
	// Controller *********************************
	/** Logical controller of {@link StatisticsReportFX}*/
	private StatisticsReportController statisticsReportController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		statisticsReportController = new StatisticsReportController(new Statistics());
		statisticsReportController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		statisticsReportController.initStatusComboBox(statusComboBox);
		
		initGetInfoSystems();
	}

	/**
	 * clears the fields
	 */
	public void clearFields() {
	}
	
	@FXML
	private void activityBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(true);
		performancesTabPane.setVisible(false);
		delaysTabPane.setVisible(false);
		historyTabPane.setVisible(false);
	}
	
	@FXML
	private void performanceBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(false);
		performancesTabPane.setVisible(true);
		delaysTabPane.setVisible(false);
		historyTabPane.setVisible(false);
	}
	
	@FXML
	private void delayBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(false);
		performancesTabPane.setVisible(false);
		delaysTabPane.setVisible(true);
		historyTabPane.setVisible(false);
	}
	
	@FXML
	private void historyBtnWasPressed(ActionEvent event) {
		statisticsReportController.getReports();
		activityTabPane.setVisible(false);
		performancesTabPane.setVisible(false);
		delaysTabPane.setVisible(false);
		historyTabPane.setVisible(true);
	}
	
	/** Initialize get info systems */
	public void initGetInfoSystems() {
		statisticsReportController.initGetInfoSystems();
	}
	
	/** Handle get info systems
	 * 
	 * @param message message
	 */
	public void handleGetInfoSystems(MessageObject message) {
		delaysComboBox.getItems().clear();
		delaysComboBox.getItems().add("All");
		delaysComboBox.getItems().addAll((ArrayList<String>)message.getArgs().get(0));
	}
	
	// Request Report by Status ***************************************
	private void initGetReportStatusData(String status, LocalDate from, LocalDate to) {
		statusGraphPane.getChildren().clear();
		statisticsReportController.initGetReportStatusData(status, from, to);
	}
	
	/**
	 * Handle get report status
	 * @param message data
	 */
	public void handleGetReportStatusData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateStatusReportLabels(intervals);
		fillGraph(statusGraphPane, intervals, status, status + " Report", "Status");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	private void updateStatusReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		statusStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		statusMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	/**
	 * Handle status issue report button event
	 * @param event button was pressed
	 */
	@FXML
	public void statusIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(statusFrom, statusTo)) {
			statusIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		if (statusComboBox.getValue() == null) {
			statusIssueReportLabel.setText("Choose Status!");
			return;
		}
		initGetReportStatusData(statusComboBox.getValue(), statusFrom.getValue(), statusTo.getValue());
		statusIssueReportLabel.setText("Report Issued!");
	}
	
	// Request Report by Rejection ************************************
	private void initGetReportRejectedData(LocalDate from, LocalDate to) {
		rejectionGraphPane.getChildren().clear();
		statisticsReportController.initGetReportStatusData(from, to);
	}
	
	/**
	 * This method handles getting report rejected data
	 * @param message report rejection data
	 */
	public void handleGetReportRejectedData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateRejectionReportLabels(intervals);
		fillGraph(rejectionGraphPane,intervals, status, status + " Report", "Rejection");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	private void updateRejectionReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		rejectionStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		rejectionMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	/**
	 * Rejection issue report button
	 * @param event button was pressed
	 */
	@FXML
	public void rejectionIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(rejectionFrom, rejectionTo)) {
			rejectionIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		initGetReportRejectedData(rejectionFrom.getValue(), rejectionTo.getValue());
		rejectionIssueReportLabel.setText("Report Issued!");
	}
	
	// Request Report by Active Days **********************************
	private void initGetReportActiveDaysData(LocalDate from, LocalDate to) {
		activeDaysGraphPane.getChildren().clear();
		statisticsReportController.initGetReportActiveDaysData(from, to);
	}
	
	/**
	 * Handles getting report active days data
	 * @param message data
	 */
	public void handleGetReportActiveDaysData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateActiveDaysReportLabels(intervals);
		fillGraph(activeDaysGraphPane, intervals, status, status + " Report", "Active Days");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	/**
	 * Active days issue report button was pressed event handler
	 * @param event button was pressed
	 */
	@FXML
	public void activeDaysIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(activeDaysFrom, activeDaysTo)) {
			rejectionIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		initGetReportActiveDaysData(activeDaysFrom.getValue(), activeDaysTo.getValue());
		activeDaysIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateActiveDaysReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		activeDaysStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		activeDaysMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Extensions ***********************************
	private void initGetReportExtensionsData() {
		extensionsGraphPane.getChildren().clear();
		statisticsReportController.initGetReportExtensionsData();
	}
	
	/**
	 * This method handles getting report extensions data event
	 * @param message data
	 */
	public void handleGetReportExtensionsData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateExtensionsReportLabels(intervals);
		fillGraph(extensionsGraphPane, intervals, status, status + " Report", "Extensions");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	/**
	 * This method handles extensions issue report data sending
	 * @param event button was pressed
	 */
	@FXML
	public void extensionsIssueReportBtnWasPressed(ActionEvent event) {
		initGetReportExtensionsData();
		extensionsIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateExtensionsReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		extensionsStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		extensionsMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Durations ***********************************
	private void initGetReportDurationsData() {
		durationsGraphPane.getChildren().clear();
		statisticsReportController.initGetReportDurationsData();
	}
	
	/**
	 * This method handles getting report durations data
	 * @param message data
	 */
	public void handleGetReportDurationsData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateDurationsReportLabels(intervals);
		fillGraph(durationsGraphPane, intervals, status, status + " Report", "Durations");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	/**
	 * Duration Issue Report event handler
	 * @param event button was pressed
	 */
	@FXML
	public void durationsIssueReportBtnWasPressed(ActionEvent event) {
		initGetReportDurationsData();
		durationsIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateDurationsReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		durationsStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		durationsMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Delays ***********************************
	private void initGetReportDelaysData() {
		delaysGraphPane.getChildren().clear();
		statisticsReportController.initGetReportDelaysData(delaysComboBox.getValue());
	}
	
	/**
	 * This method handles getting report delays data
	 * @param message data
	 */
	public void handleGetReportDelaysData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateDelaysReportLabels(intervals);
		fillGraph(delaysGraphPane, intervals, status, status + " Report", "Delays");
		statisticsReportController.saveReport(status + " Report", intervals);
	}
	
	/**
	 * This method handles delays issue report button pressed
	 * @param event button was pressed
	 */
	@FXML
	public void delaysIssueReportBtnWasPressed(ActionEvent event) {
		if (delaysComboBox.getValue() == null)
			 delaysIssueReportLabel.setText("Choose Info System!");	 
		else {
			delaysIssueReportLabel.setText("Report Issued!");
			initGetReportDelaysData();
		}
	}
	
	private void updateDelaysReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		delaysStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		delaysMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by History ***********************************
	private void initGetReportHistoryData(ArrayList<Interval> intervals) {
		
		
		String status = "status";

		updateHistoryReportLabels(intervals);
		fillGraph(historyGraphPane, intervals, status, status + " Report", "History");
		historyIssueReportLabel.setText("Report Issued!");
	}
	
	/**
	 * This method handles history issue report button pressed
	 * @param event button was pressed
	 */
	@FXML
	public void historyIssueReportBtnWasPressed(ActionEvent event) {
		historyGraphPane.getChildren().clear();
		// TODO
		JFXAlert<Void> popup = new JFXAlert<Void>();
		AnchorPane anchorPane = new AnchorPane();
		ListView<String> node = new ListView<String>();
		
		// Set ListView node
		node.setLayoutX(7);
		node.setLayoutY(7);
		node.setPrefHeight(250);
		for (int i = 0; i < allReportsNames.size(); i++)
			node.getItems().add(i + " - " + allReportsNames.get(i));
		
		// Set Send Button
		Button send = new Button("Select");
		send.setOnAction((ActionEvent sendEvent) -> {
			if (node.getSelectionModel().getSelectedItem().equals("") || node.getSelectionModel().getSelectedItem() == null) {
				send.requestFocus();
				return;
			}
			initGetReportHistoryData(allReports.get(node.getSelectionModel().getSelectedIndex()));
			popup.close();
		});
		send.setLayoutX(100);
		send.setLayoutY(265);
		
		anchorPane.getChildren().addAll(node, send);
		
		// Set JFXAlert Dialogue
		popup.setTitle("Select History Report: ");
		popup.setContent(anchorPane);
		popup.setSize(264, 300);
		popup.showAndWait();
		
	}
	
	private void updateHistoryReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		historyStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		historyMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Graphical ******************************************************
	private void fillGraph(StackPane graphPane, ArrayList<Interval> intervals, String seriesName, String graphTitle, String xLabel) {
		CategoryAxis xAxis = new CategoryAxis();
    	xAxis.setLabel(xLabel);
    	
    	ArrayList<String> categories = new ArrayList<>();
    	for (int i = 0; i < intervals.size(); i++)
    		if (!categories.contains(intervals.get(i).getCategoryName()))
    			categories.add(intervals.get(i).getCategoryName());
    	xAxis.setCategories(FXCollections.<String>observableArrayList(categories));
    	
    	NumberAxis yAxis = new NumberAxis();
    	yAxis.setLabel("Amount");
    	
    	StackedBarChart<String, Number> barChart = new StackedBarChart<String, Number>(xAxis, yAxis);
    	barChart.setTitle(graphTitle);
        
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        
        
        series.setName(seriesName);
        for (int i = 0; i < intervals.size(); i++)
        	series.getData().add(new XYChart.Data<String, Number>(intervals.get(i).getCategoryName(), intervals.get(i).getValue()));
        
        Platform.runLater(() -> {
        barChart.getData().add(series);
        graphPane.getChildren().add(barChart);
        StackPane.setAlignment(barChart, Pos.CENTER);
        });
	}

	/**
	 * This method handles getting reports data
	 * @param msg data
	 */
	public void handleGetReports(MessageObject msg) {
		allReports = (ArrayList<ArrayList<Interval>>)msg.getArgs().get(0);
		allReportsNames = (ArrayList<String>)msg.getArgs().get(1);
	}
	
	// Side Panel *****************************************************
	/** This method switches the scene back to the main panel page
	 * 
	 * @param event back was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		statisticsReportController.switchScene("Panel");
	}
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event logout was pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		statisticsReportController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event new change request was pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		statisticsReportController.newChangeRequestWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to View All Requests
	 * @param event view all requests was pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		statisticsReportController.ViewAllRequestsWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Manage Permissions
	 * @param event manage permissions was pressed
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		statisticsReportController.managePermissionsWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to View all system Data
	 * @param event view all system data was pressed
	 */
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		statisticsReportController.viewAllSystemDataWasPressed(event);
	}
}
