package Gui;




import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;

import Common.EvaluatorAppoitmentTable;
import Common.EvalutorAppoitmentTableSerializable;
import Common.ExecutionLeaderAppointmentRequest;
import Common.TimeAssessmentRequest;
import Common.TimeExtensionRequest;
import LogicController.ManageApprovmentsController;
import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
/**
 * manage Approves Fxml controller
 *
 */
public class ManageApprovesFX extends BaseFX implements Initializable{

@FXML
JFXButton back;

@FXML
private JFXButton viewRequestDetails;

@FXML JFXButton logOut;

@FXML
private JFXButton newChangeRequest;

//-------------Evaluator Table parameters----------------------------------------//

@FXML
private TableView<EvaluatorAppoitmentTable> EvaluatorTable;
@FXML
private TableColumn<EvaluatorAppoitmentTable,String> EARequestID;
@FXML
private TableColumn<EvaluatorAppoitmentTable,String> EAEvaluatorID;
@FXML
private TableColumn<EvaluatorAppoitmentTable,String> EAEvaluatorName;

ObservableList<EvaluatorAppoitmentTable> EvaluatorList;

//-----------Time Assessment Table parameters-------------------------------------//

@FXML
private TableView<TimeAssessmentRequest> TimeAssessmentTable;
@FXML
private TableColumn<TimeAssessmentRequest,String> TARequestID;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAEvaluatorID;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAEvaluatorName;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAStartDate;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAEndDate;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAAccept;
@FXML
private TableColumn<TimeAssessmentRequest,String> TAReject;

ObservableList<TimeAssessmentRequest> TimeAssessmentList;

//-----------Time Extension Table parameters--------------------------------------//

@FXML
private TableView<TimeExtensionRequest> TimeExtensionTable;
@FXML
private TableColumn<TimeExtensionRequest,String> TERequestID;
@FXML
private TableColumn<TimeExtensionRequest,String> TEUserID;
@FXML
private TableColumn<TimeExtensionRequest,String> TEJobDescription;
@FXML
private TableColumn<TimeExtensionRequest,String> TEStartDate;
@FXML
private TableColumn<TimeExtensionRequest,String> TEEndDate;
@FXML
private TableColumn<TimeExtensionRequest,String> TEAccept;
@FXML
private TableColumn<TimeExtensionRequest,String> TEReject;

ObservableList<TimeExtensionRequest> TimeExtensionList;

//-----------Execution Leader Appintment Table parameters-------------------------//

@FXML
private TableView<ExecutionLeaderAppointmentRequest> ExecutionLeaderTable;
@FXML
private TableColumn<ExecutionLeaderAppointmentRequest,String> ELRequestID;
@FXML
private TableColumn<ExecutionLeaderAppointmentRequest,String> ELStartDate;

ObservableList<ExecutionLeaderAppointmentRequest> ExecutionLeaderList;

private ArrayList<String> executionLeaderDetails = new ArrayList<>();

//-----------Controller-----------------------------------------------------------//

private ManageApprovmentsController manageApprovmentsController;

@Override
public void initialize(URL location, ResourceBundle resources) {
	
	manageApprovmentsController=new ManageApprovmentsController();
	//------------------------Initialize Evaluator Appointment List-------------------------------------------------------------
	
	//------------------------Initialize Execution Leader List------------------------------------------------------------------
	setDoubleClickMouseWasPressed();
}

/**
 * refresh the tabels
 */
@FXML
public void refreshTables() {
	initEvaluatorTable();
	initTimeAssessmentTable();
	initTimeExtensionTable();
	initExecutionLeaderTable();
	initExecutionLeaderDetails();
}
/**
 * inital Execution Leader Details 
 */
public void initExecutionLeaderDetails() {
	MessageObject msg = new MessageObject(RequestType.GetExecutionLeaderOptions, new ArrayList<>());
	manageApprovmentsController.sendMessage(msg);
}
/**
 * Execution Leader Details set from DB
 * @param message Execution Leader Details from the DB
 */
public void handleExecutionLeaderDetails(MessageObject message) {
	executionLeaderDetails.clear();
	executionLeaderDetails.addAll((ArrayList<String>)message.getArgs().get(0));
}
/**
 * inital Evaluator table
 */
public void initEvaluatorTable() {
	manageApprovmentsController.initEvaluatorTable();
}
/**
 * Set Up Evaluator table information from the DB
 * @param msg Message object contain Evaluator table information
 */
public void handleEvaluatorTable(MessageObject msg) {
	//------------------------Initialize Evaluator List-------------------------------------------------------------------
	EARequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
	EAEvaluatorID.setCellValueFactory(new PropertyValueFactory<>("EvaluatorID"));
	EAEvaluatorName.setCellValueFactory(new PropertyValueFactory<>("EvaluatorName"));
	EvaluatorList = manageApprovmentsController.LoadEvaluatorTable(msg);
	EvaluatorTable.getItems().clear();
	EvaluatorTable.setItems(EvaluatorList);
}
/**
 * inital Time Extention table
 */
public void initTimeExtensionTable() {
	manageApprovmentsController.initTimeExtensionTable();
}
/**
 * Set Up time Extension table frim DB into the table
 * @param msg data
 */
public void handleTimeExtensionTable(MessageObject msg) {
	//------------------------Initialize Time Extension List-------------------------------------------------------------------
	TERequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
	TEUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
	TEJobDescription.setCellValueFactory(new PropertyValueFactory<>("JobDescription"));
	TEStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
	TEEndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
	TEAccept.setCellValueFactory(new PropertyValueFactory<>("Accept"));
	TEReject.setCellValueFactory(new PropertyValueFactory<>("Reject"));
	TimeExtensionList = manageApprovmentsController.LoadTimeExtensionTable(msg);
	TimeExtensionTable.getItems().clear();
	TimeExtensionTable.setItems(TimeExtensionList);
}

/**
 * inital Time Assessment Table
 */
public void initTimeAssessmentTable() {
	manageApprovmentsController.initTimeAssessmentTable();
}
/**
 * Set Up Time Assessment table information from the db into the table   
 * @param msg data
 */
public void handleTimeAssessmentTable(MessageObject msg) {
	//------------------------Initialize Time Assessment List-------------------------------------------------------------------
	TARequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
	TAEvaluatorID.setCellValueFactory(new PropertyValueFactory<>("EvaluatorID"));
	TAEvaluatorName.setCellValueFactory(new PropertyValueFactory<>("EvaluatorName"));
	TAStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
	TAEndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
	TAAccept.setCellValueFactory(new PropertyValueFactory<>("Accept"));
	TAReject.setCellValueFactory(new PropertyValueFactory<>("Reject"));
	TimeAssessmentList = manageApprovmentsController.LoadTimeAssessmentTable(msg);
	TimeAssessmentTable.getItems().clear();
	TimeAssessmentTable.setItems(TimeAssessmentList);
}
/**
 * inital Execution Leader table
 */
public void initExecutionLeaderTable() {
	manageApprovmentsController.initExecutionLeaderTable();
}
/**
 * set up all the information of  Execution Leader from the DB into the table
 * @param msg cintain all the Execution Leader information 
 */
public void handleExecutionLeaderTable(MessageObject msg) {
	//------------------------Initialize Execution Leader Appointment List------------------------------------------------------
	ELRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
	ELStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
	ExecutionLeaderList = manageApprovmentsController.LoadExecutionLeaderTable(msg);
	ExecutionLeaderTable.getItems().clear();
	ExecutionLeaderTable.setItems(ExecutionLeaderList);
}

/**
 * back event handler
 * @param event back was pressed
 */
@FXML
public void backWasPressed(ActionEvent event)
{
	manageApprovmentsController.switchScene("Panel");
	
}
/**
 * duble click was press handler
 */
@FXML
public void setDoubleClickMouseWasPressed()
{
	EvaluatorTable.setOnMouseClicked((MouseEvent event) -> {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
        	OpemApproveEvalutorScene();
        }
    });
	
	ExecutionLeaderTable.setOnMouseClicked((MouseEvent event) -> {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
        	OpenExecutionLeaderAppointment();
        }
    });
}

//-------------------------Evaluator appoint Table---------------------------------------------------------


public void OpemApproveEvalutorScene()
{
	String EvaluatorID= EvaluatorTable.getSelectionModel().getSelectedItem().getEvaluatorID();
	String RequestID=  EvaluatorTable.getSelectionModel().getSelectedItem().getRequestID();
	String EvaluatorName =  EvaluatorTable.getSelectionModel().getSelectedItem().getEvaluatorName();
	manageApprovmentsController.switchScene("EvaluatorApproveScene");
	manageApprovmentsController.SendFieldsToNextScene(RequestID,EvaluatorID,EvaluatorName);
}

//------------------------Execution Leader Appoint table---------------------------------------------------
/**
 * open Execution Leader Appointment event
 */
public void OpenExecutionLeaderAppointment() {
	ExecutionLeaderAppointmentRequest request = ExecutionLeaderTable.getSelectionModel().getSelectedItem();
	
	JFXAlert<Void> popup = new JFXAlert<Void>();
	AnchorPane anchorPane = new AnchorPane();
	ListView<String> node = new ListView<String>();
	
	// Set ListView node
	node.setLayoutX(7);
	node.setLayoutY(7);
	node.setPrefHeight(250);
	for (int i = 0; i < executionLeaderDetails.size(); i += 2)
		node.getItems().add(executionLeaderDetails.get(i) + " - " + executionLeaderDetails.get(i+1));
	
	// Set Send Button
	Button send = new Button("Update");
	send.setOnAction((ActionEvent sendEvent) -> {
		if (node.getSelectionModel().getSelectedItem().equals("")) {
			send.requestFocus();
			return;
		}
		MessageObject response = new MessageObject(RequestType.SetExecutionLeader, new ArrayList());
		response.getArgs().add(request.getRequestID());
		response.getArgs().add(executionLeaderDetails.get(2*node.getSelectionModel().getSelectedIndex()));
		try {
			Client.getInstance().sendToServer(response);
			manageApprovmentsController.swapStage(request.getRequestID(), "Execution", "Decision");
		} catch (IOException e) {e.printStackTrace();}
		popup.close();
	});
	send.setLayoutX(100);
	send.setLayoutY(265);
	
	anchorPane.getChildren().addAll(node, send);
	
	// Set JFXAlert Dialogue
	popup.setTitle("Select Execution Leader: ");
	popup.setContent(anchorPane);
	popup.setSize(264, 300);
	popup.showAndWait();
}

//----------------------panel methods----------------------------------------------------------------------
/**
 * log out event handler
 * @param event log out button was pressed
 */
@FXML
public void logOutWasPressed(ActionEvent event) {
	manageApprovmentsController.logOutWasPressed(event);
}
/**
 * view all request event handler
 * @param event View all request button was pressed
 */
@FXML
public void ViewAllRequestsWasPressed(ActionEvent event) {
	manageApprovmentsController.ViewAllRequestsWasPressed(event);
}
/**
 * new change request event handler
 * @param event new change request button was presed
 */
@FXML
public void newChangeRequestWasPressed(ActionEvent event) {
	manageApprovmentsController.newChangeRequestWasPressed(event);
}
}

