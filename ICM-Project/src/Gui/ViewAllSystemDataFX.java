package Gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.Employee;
import Common.ExecutionLeaderAppointmentRequest;
import Common.Request;
import Common.ViewAllRequestsRequest;
import LogicController.StatisticsReportController;
import LogicController.ViewAllRequestsController;
import LogicController.ViewAllSystemDataController;
import Utilities.MessageObject;
import Utilities.RequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** This class represents the graphical controller of view all system data page */
public class ViewAllSystemDataFX extends BaseFX {
	// Side-Panel *******************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;
	
	@FXML
	private VBox all_roles;
	
	//***************EmployeeTable***********//
	@FXML
	private TableView<Employee> EmployeeTable;
	@FXML
	private TableColumn<Employee,String> TAEmployeeID;
	@FXML
	private TableColumn<Employee,String> TAName;
	@FXML
	private TableColumn<Employee,String> TAEmail;
	
	ObservableList<Employee> employeeList;
	
	//*************RequestTable**************//
	@FXML
	private TableView<Request> RequestTable;
	@FXML
	private TableColumn<Request,String> TARequestID;
	@FXML
	private TableColumn<Request,String> TAInfoSystem;
	@FXML
	private TableColumn<Request,String> TAStage;
	@FXML
	private TableColumn<Request,String> TAStatus;
	@FXML
	private TableColumn<Request,String> TAInitiatorID;

	ObservableList<Request> requestList;
	// Controller *********************************
	private ViewAllSystemDataController controller;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controller = new ViewAllSystemDataController();
		controller.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		
		TAEmployeeID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		TAName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		TAEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		
		TARequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
		TAInfoSystem.setCellValueFactory(new PropertyValueFactory<>("InformationSystem"));
		TAStage.setCellValueFactory(new PropertyValueFactory<>("CurrentStage"));
		TAStatus.setCellValueFactory(new PropertyValueFactory<>("RequestStatus"));
		TAInitiatorID.setCellValueFactory(new PropertyValueFactory<>("InitaitorID"));
		onRowDoubleClick();
	}
	
	/** This function sets an event handler on mouse double click on a row.
	 */
	private void onRowDoubleClick() {
		RequestTable.setRowFactory( tv -> {
		    TableRow<Request> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
		        	Request rowData = row.getItem();
		        	new ViewAllRequestsController().searchWasPressed(rowData.getRequestID(), "Initiator");
		        }
		    });
		    return row ;
		});
	}
	
	/**
	 * Clears all Fields
	 */
	public void clearFields() {
		initEmployeeTable();
		initRequestTable();
	}
	
	private void initRequestTable() {
		MessageObject msg = new MessageObject(RequestType.GetRequestTable, new ArrayList());
		controller.sendMessage(msg);
	}
	
	/**
	 * This method handles the filling of request table
	 * @param message data
	 */
	public void handleRequestTable(MessageObject message) {
		RequestTable.getItems().clear();
		ArrayList<Request> list = (ArrayList<Request>)message.getArgs().get(0);
		requestList = FXCollections.observableArrayList(list);
		RequestTable.setItems(requestList);
	}

	private void initEmployeeTable() {
		MessageObject msg = new MessageObject(RequestType.GetEmployeeTable, new ArrayList());
		controller.sendMessage(msg);
	}
	
	/**
	 * This method handles filling of employee table
	 * @param message data
	 */
	public void handleEmployeeTable(MessageObject message) {
		EmployeeTable.getItems().clear();
		ArrayList<Employee> list = (ArrayList<Employee>)message.getArgs().get(0);
		employeeList = FXCollections.observableArrayList(list);
		EmployeeTable.setItems(employeeList);
	}

	/** This method switches the scene back to the main panel page
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		controller.switchScene("Panel");
	}
	
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event button was pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		controller.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event button was pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		controller.newChangeRequestWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to view all requests page
	 * @param event button was pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		controller.ViewAllRequestsWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to manage permissions
	 * @param event button was pressed
	 */
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		controller.managePermissionsWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to view statistics
	 * @param event button was pressed
	 */
	@FXML
	public void viewStatisticsReportWasPressed(ActionEvent event) {
		controller.viewStatisticsReportWasPressed(event);
	}
}
