package Gui;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import Common.EvalutorAppoitmentTableSerializable;
import Common.ISEAppoitmentTable;
import LogicController.ReplaceEvaluatorSceneController;
import Utilities.MessageObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;



/**
 *  Replace Evaluator Scene Fxml controller
 *
 */
public class ReplaceEvaluatorSceneFX extends BaseFX implements Initializable {
@FXML
	private JFXButton back; 
	
@FXML
private JFXButton AppointNewEvaluator;

//------------------textField parameters-------------------------------
@FXML
private JFXTextField Name;

@FXML
private JFXTextField id;

@FXML
private JFXTextField RequestID;

//-----------------------------------------------------------Panel Parameters----------------------------------------
@FXML
private JFXButton viewRequestDetails;

@FXML
JFXButton logOut;

@FXML
private JFXButton newChangeRequest;
	


//-------------------------------------------------------ISE Table parameters----------------------------------------

@FXML
private TableView<ISEAppoitmentTable> ISETable;

@FXML
private TableColumn<ISEAppoitmentTable,String> EvaluatorID;

@FXML
private TableColumn<ISEAppoitmentTable,String> EvaluatorName;

ObservableList<ISEAppoitmentTable> ISEList=FXCollections.observableArrayList();

//Logic controller 
private ReplaceEvaluatorSceneController replaceEvaluatorSceneController;


@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	//------------------------Initialize Evaluator Appointment List-------------------------------------------------------------
	replaceEvaluatorSceneController=new ReplaceEvaluatorSceneController();
	    EvaluatorID.setCellValueFactory(new PropertyValueFactory<>("EvaluatorID"));
	    EvaluatorName.setCellValueFactory(new PropertyValueFactory<>("EvaluatorName"));
	    setDoubleClickMmouseWasPressed();
}



/**
 * when user clickTwice on the table show the name and id in the text Fields ("id","name")
 * 
 */
@FXML
public void setDoubleClickMmouseWasPressed()
{
	ISETable.setOnMouseClicked((MouseEvent event) -> {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
        	 id.setText(ISETable.getSelectionModel().getSelectedItem().getEvaluatorID());
        	 Name.setText(ISETable.getSelectionModel().getSelectedItem().getEvaluatorName());;
        }
    });
}

/**
 * set request ID 
 * @param RequestID request id
 */
public void setRequstId(String RequestID)
{
	this.RequestID.setText(RequestID);
}
/**
 * back event handle
 * @param event back button was pressed
 */
@FXML
public void backWasPressed(ActionEvent event)
{
	replaceEvaluatorSceneController.switchScene("EvaluatorApproveScene");
}


/**
 * reload the data to the table when someone call method
 *
 * @param event appoint new evaluator button was pressed
 */

public void AppointNewEvaluatorWasPressed(ActionEvent event)
{
	replaceEvaluatorSceneController.approvedEvaluator(RequestID.getText(),id.getText(),
			Name.getText());
	replaceEvaluatorSceneController.manageAprrovementWasPressed(event);
}

/**
 * load eveluator table
 */
@FXML
public void loadEvaluatorTable() {
	replaceEvaluatorSceneController.switchScene("ManageApproves");
	ManageApprovesFX manageApprovment;
	manageApprovment = (ManageApprovesFX) replaceEvaluatorSceneController.getCurrentFX();
	manageApprovment.refreshTables();
}

/**
 * load data to evaluator table
 */
public void loadDataToEvalutorTable()
{
	clearFields();
	replaceEvaluatorSceneController.viewISETableWasPressed();
}

/**
 * clear the fxml fields
 */
public void clearFields() {
	for ( int i = 0; i<ISETable.getItems().size(); i++) {
		ISETable.getItems().clear();
	}
	
}

//-----------------------------------------------------------Panel Methods----------------------------------------
/**
 * view all requests event handler
 * @param event view all request was pressed
 */
@FXML
public void ViewAllRequestsWasPressed(ActionEvent event) {
	replaceEvaluatorSceneController.ViewAllRequestsWasPressed(event);
}


/**
 * new chande request event handler
 * @param event new change request button was presssed
 */
@FXML
public void newChangeRequestWasPressed(ActionEvent event) {
	replaceEvaluatorSceneController.newChangeRequestWasPressed(event);
}

/**
 * log out event handler
 * @param event kog out was pressed
 */
@FXML
public void logOutWasPressed(ActionEvent event) {
	replaceEvaluatorSceneController.logOutWasPressed(event);
}
/**
 * load ISE table 
 * @param message contain all the information
 */
public void loadISETable(MessageObject message) {
		ArrayList<Object> ISEListTemp=message.getArgs();
		EvalutorAppoitmentTableSerializable temp;
		if(!(boolean) ISEListTemp.get(0))
			return;
		for(int i=1;i<ISEListTemp.size();i++)
		{
			temp=(EvalutorAppoitmentTableSerializable) ISEListTemp.get(i);
			ISEList.add(new ISEAppoitmentTable(temp.getEvalutorid(),temp.getEvalutorName()));
		}

		ISETable.setItems(ISEList);
		}
	
}




