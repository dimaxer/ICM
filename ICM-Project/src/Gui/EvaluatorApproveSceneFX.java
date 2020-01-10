package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import LogicController.EvaluatorApproveSceneController;
import LogicController.ManageApprovmentsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;

public class EvaluatorApproveSceneFX extends BaseFX {

	@FXML
	private JFXButton back;

	@FXML
	private JFXButton Approve;

	@FXML 
	private JFXButton replaceEvaluator;
	
	@FXML
	private JFXButton viewRequestDetails;

	@FXML
	JFXButton logOut;

	@FXML
	private JFXButton newChangeRequest;

	@FXML
	private JFXTextField RequestID;

	@FXML
	private JFXTextField Evaluatorid;
	@FXML
	private JFXTextField EvaluatorName;

	private EvaluatorApproveSceneController evaluatorApproveSceneController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		evaluatorApproveSceneController = new EvaluatorApproveSceneController();

	}

	public void SetFeilds(String RequestID, String Evaluatorid, String EvaluatorName) {
		this.RequestID.setText(RequestID);
		this.Evaluatorid.setText(Evaluatorid);
		this.EvaluatorName.setText(EvaluatorName);
	}

	public void backWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.switchScene("ManageApproves");
	}

//-----------------------------Panel Methods--------------------------------------------------
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.logOutWasPressed(event);
	}

	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.ViewAllRequestsWasPressed(event);
	}

	@FXML
	public void loadEvaluatorTable() {
		evaluatorApproveSceneController.switchScene("ManageApproves");
		ManageApprovesFX manageApprovment;
		manageApprovment = (ManageApprovesFX) evaluatorApproveSceneController.getCurrentFX();
		manageApprovment.loadDataToEvalutorTable();
	}

	
	@FXML
	public void replaceEvalutorWasPressed(ActionEvent event)
	{
		evaluatorApproveSceneController.replaceEvalutorWasPressed(event,RequestID.getText());
	}
	
	@FXML
	public void ApproveWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.approvedEvaluator(RequestID.getText(), Evaluatorid.getText(),
				EvaluatorName.getText());

	}

	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.newChangeRequestWasPressed(event);
	}
}
