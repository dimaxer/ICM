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
/**
 * 
 * 
 *Evaluator Approve Scene Fxml controller
 */
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
	/**
	 * set the fields
	 * @param RequestID request id
	 * @param Evaluatorid evvaluator id
	 * @param EvaluatorName the evaluator name
	 */
	public void SetFeilds(String RequestID, String Evaluatorid, String EvaluatorName) {
		this.RequestID.setText(RequestID);
		this.Evaluatorid.setText(Evaluatorid);
		this.EvaluatorName.setText(EvaluatorName);
	}
	/**
	 * back button event handler
	 * @param event back button was pressed
	 */
	public void backWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.manageAprrovementWasPressed(null);
	}

//-----------------------------Panel Methods--------------------------------------------------
	/**
	 * logout event handler
	 * @param event logout button was pressed
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.logOutWasPressed(event);
	}
	/**
	 * view all request event handler
	 * @param event view all request pressed
	 */
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.ViewAllRequestsWasPressed(event);
	}

	/**
	 * load evaluator table to the screen
	 */
	@FXML
	public void loadEvaluatorTable() {
		evaluatorApproveSceneController.switchScene("ManageApproves");
		ManageApprovesFX manageApprovment;
		manageApprovment = (ManageApprovesFX) evaluatorApproveSceneController.getCurrentFX();
		manageApprovment.refreshTables();
	}

	/**
	 * replacr evaluator event handler
	 * @param event replace evaluator event
	 */
	@FXML
	public void replaceEvalutorWasPressed(ActionEvent event)
	{
		evaluatorApproveSceneController.replaceEvalutorWasPressed(event,RequestID.getText());
	}
	
	/**
	 * approve event handler
	 * @param event aprrove button was pressed 
	 */
	@FXML
	public void ApproveWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.approvedEvaluator(RequestID.getText(), Evaluatorid.getText(),
				EvaluatorName.getText());

	}
	/**
	 * new change request event handler
	 * @param event new change request button waas pressed
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		evaluatorApproveSceneController.newChangeRequestWasPressed(event);
	}
}
