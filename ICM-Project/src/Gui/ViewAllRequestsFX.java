package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.Request;
import Common.User;
import Common.ViewAllRequestsRequest;
import LogicController.BaseController;
import LogicController.RequestDetailsInitiatorController;
import LogicController.SearchRequestController;
import LogicController.ViewAllRequestsController;
import Utilities.MessageObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ViewAllRequestsFX implements BaseFx, Initializable {

	// Class Buttons ***************************************************
	@FXML
	private JFXButton search;

	@FXML
	private JFXButton back;

	@FXML
	private JFXButton home;

	@FXML
	private Text requestIdNotFound;

	@FXML
	private ImageView eror;

	@FXML
	private JFXTextField requestIDField;

	@FXML
	private TableView<ViewAllRequestsRequest> tableView;

	//FXMLs that related to the table
	@FXML
	private TableColumn<ViewAllRequestsRequest, String> statusColumn;
	@FXML
	private TableColumn<ViewAllRequestsRequest, String> requestIdColumn;
	@FXML
	private TableColumn<ViewAllRequestsRequest, String> myRoleColumn;
	@FXML
	private TableColumn<ViewAllRequestsRequest, String> initiatorColumn;

	// Class variables *************************************************
	private String requestIDText;
	private ViewAllRequestsController viewAllRequestsController;
	
	private ObservableList<ViewAllRequestsRequest> allTableInfo;

	// Class methods ***************************************************
	/**
	 * event handler for the search button receives the text that was written into
	 * the text field and sends the request id to the server so that it can check if
	 * its the db or not
	 * 
	 * @param event
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void setLogicController(BaseController controller) {
		viewAllRequestsController = (ViewAllRequestsController) controller;

	}
	
	/*
	 * move the user to request details panel
	 * */

	@FXML
	public void searchWasPressed(ActionEvent event) {
		setValdiator();
		requestIDText = requestIDField.getText();

		// check for empty text
		if (requestIDText.length() <= 0) {
			requestIDField.validate();
			eror.setVisible(true);
			return;
		}
		
		
		// send the request id to the server
		
		String role = findRole(requestIDField.getText());
		if(role.equals("NotExist")) {
			requestIdNotFound.setFill(Color.RED);
			requestIdNotFound.setText("Request id was not found");
			return;
		}
		
		viewAllRequestsController.searchWasPressed(requestIDField.getText(),role);

	}

	private String findRole(String text) {
		for (ViewAllRequestsRequest viewAllRequestsRequest : allTableInfo) {
			if(viewAllRequestsRequest.getRequestId().equals(text)) {
				return myRoleColumn.getCellData(viewAllRequestsRequest);
			}	
		}
		return "NotExist";
		
	}

	/**
	 * This method checks if the request id was found in the db or not by checking
	 * the boolean and switches screens to the request form with the message object
	 * so that it could be initialized with the request's information
	 * 
	 * @param massage in args(0) : boolean request exist, args(1) : String user role in request, args(2) Request the request
	 */
	public void handleSearchRequest(MessageObject massage) {

		String role = (String) massage.getArgs().get(1);
		if ((boolean) massage.getArgs().get(0)) {
			
			//switch case manager which RequestDetails window will open depending on the roll of the user in the request
			//TODO : all the others ReqestsDetail for the others roles
			switch (role) {
			case "Initiator":{
				viewAllRequestsController.switchScene("RequestDetailsInitiator");
				RequestDetailsInitiatorFX FX = (RequestDetailsInitiatorFX) viewAllRequestsController.getCurrentFX();
				FX.loadRequest(massage);
				
				break;
			}
			case "Tester":{
				
//				viewAllRequestsController.switchScene("RequestDetailsTester");
//				RequestDetailsTesterFX FX = (RequestDetailsTesterFX) viewAllRequestsController.getCurrentFX();
//				FX.loadRequest(massage);
				
				break;
			}
			case "Committee Chairmen":{
				
				
				break;
			}
			case "Committee Member":{
				
				
				break;
			}
			case "Exeqution Leader":{
				
				
				break;
			}
			case "Supervisor":{
				
				
				break;
			}
			case "ISD Chif":{
				
				
				break;
			}
			case "EvaluatorID":{
				
				
				break;
			}
				
				
				

			default:
				break;
			}
		} else {
			requestIdNotFound.setFill(Color.RED);
			requestIdNotFound.setText("Request id was not found");
		}

	}

	@FXML
	public void backWasPressed(ActionEvent event) {

		viewAllRequestsController.switchScene("AcademicUserPanel");
	}

	@FXML
	public void homeWasPressed(ActionEvent event) {

		viewAllRequestsController.switchScene("AcademicUserPanel");
	}

	/**
	 * a method to clear the requesID
	 */
	@FXML
	public void clearFields() {
		requestIDField.setText("");
		requestIdNotFound.setText("");
		eror.setVisible(false);
	}

	/**
	 * a method that shows a Massage when no input is set in the UserName or
	 * Password
	 * 
	 */
	public void setValdiator() {
		RequiredFieldValidator validatorRequestId = new RequiredFieldValidator();
		requestIDField.getValidators().add(validatorRequestId);
		validatorRequestId.setMessage("No Input Given");
		requestIDField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					requestIDField.validate();
					requestIdNotFound.setText("");
				}

			}
		});

	}

	// load the data from the server
	public void loadRequests(MessageObject message) {
		// TODO Auto-generated method stub
		statusColumn.setCellValueFactory(new PropertyValueFactory<ViewAllRequestsRequest, String>("Status"));
		myRoleColumn.setCellValueFactory(new PropertyValueFactory<ViewAllRequestsRequest, String>("MyRole"));
		initiatorColumn.setCellValueFactory(new PropertyValueFactory<ViewAllRequestsRequest, String>("Initiator"));
		requestIdColumn.setCellValueFactory(new PropertyValueFactory<ViewAllRequestsRequest, String>("RequestId"));
		
		allTableInfo = viewAllRequestsController.loadRequests(message, tableView);

	}

}
