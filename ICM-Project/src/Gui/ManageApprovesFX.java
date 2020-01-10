package Gui;




import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import Common.EvaluatorAppoitmentTable;
import Common.EvalutorAppoitmentTableSerializble;
import LogicController.ManageApprovmentsController;
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
private TableColumn<EvaluatorAppoitmentTable,String> RequestID;

@FXML
private TableColumn<EvaluatorAppoitmentTable,String> EvaluatorID;

@FXML
private TableColumn<EvaluatorAppoitmentTable,String> EvaluatorName;

ObservableList<EvaluatorAppoitmentTable> EvaluatorList=FXCollections.observableArrayList();

private ManageApprovmentsController manageApprovmentsController;

@Override
public void initialize(URL location, ResourceBundle resources) {
	
	//------------------------Initialize Evaluator Appointment List-------------------------------------------------------------
	manageApprovmentsController=new ManageApprovmentsController();
	RequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
	EvaluatorID.setCellValueFactory(new PropertyValueFactory<>("EvaluatorID"));
	EvaluatorName.setCellValueFactory(new PropertyValueFactory<>("EvaluatorName"));
	setDoubleClickMmouseWasPressed();
}




@FXML
public void backWasPressed(ActionEvent event)
{
	manageApprovmentsController.switchScene("Panel");
	
}
@FXML
public void setDoubleClickMmouseWasPressed()
{
	EvaluatorTable.setOnMouseClicked((MouseEvent event) -> {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
        	OpemApproveEvalutorScene();
        	
         
        }
    });
}

//-------------------------Evaluator appoint Table---------------------------------------------------------


public void loadDataToEvalutorTable()
{
	clearFields();
	manageApprovmentsController.viewEvualuatorTableWasPressed();
}

public void OpemApproveEvalutorScene()
{
	String EvaluatorID= EvaluatorTable.getSelectionModel().getSelectedItem().getEvaluatorID();
	String RequestID=  EvaluatorTable.getSelectionModel().getSelectedItem().getRequestID();
	String EvaluatorName =  EvaluatorTable.getSelectionModel().getSelectedItem().getEvaluatorName();
	manageApprovmentsController.switchScene("EvaluatorApproveScene");
	manageApprovmentsController.SendFieldsToNextScene(RequestID,EvaluatorID,EvaluatorName);
	
}

public void loadEvaluatorTable(MessageObject msg) {
ArrayList<Object> EvaluatorListTemp=msg.getArgs();
EvalutorAppoitmentTableSerializble temp;
if(!(boolean) EvaluatorListTemp.get(0))
	return;
for(int i=1;i<EvaluatorListTemp.size();i++)
{
	temp=(EvalutorAppoitmentTableSerializble) EvaluatorListTemp.get(i);
	EvaluatorList.add(new EvaluatorAppoitmentTable(temp.getRequestID(),temp.getEvalutorid(),temp.getEvalutorName()));
	
}

	EvaluatorTable.setItems(EvaluatorList);
}


//----------------------panel methods------------------------------------------------------------------------

@FXML
public void logOutWasPressed(ActionEvent event) {
	manageApprovmentsController.logOutWasPressed(event);
}

@FXML
public void ViewAllRequestsWasPressed(ActionEvent event) {
	manageApprovmentsController.ViewAllRequestsWasPressed(event);
}

@FXML
public void newChangeRequestWasPressed(ActionEvent event) {
	manageApprovmentsController.newChangeRequestWasPressed(event);
}




public void clearFields() {
	for ( int i = 0; i<EvaluatorTable.getItems().size(); i++) {
		EvaluatorTable.getItems().clear();
	}
	
}
}

