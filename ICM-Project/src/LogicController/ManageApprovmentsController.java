package LogicController;

import java.util.ArrayList;

import Common.EvaluatorAppoitmentTable;
import Common.ExecutionLeaderAppointmentRequest;
import Common.TimeAssessmentRequest;
import Common.TimeExtensionRequest;
import Common.User;
import Common.ViewAllRequestsRequest;
import Gui.EvaluatorApproveSceneFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/** This is the logical controller of manage approvements */
public class ManageApprovmentsController extends BaseController {

	/**
	 * send the fields to the next scene
	 * @param requestID request id
	 * @param evaluatorID eveluator id
	 * @param evaluatorName evaluator id
	 */
	public void SendFieldsToNextScene(String requestID, String evaluatorID, String evaluatorName) {
		((EvaluatorApproveSceneFX) getCurrentFX()).SetFeilds(requestID,evaluatorID,evaluatorName);
	}	
	
	//***** Evaluator Appointment *****//
	/**
	 * load evaluator table
	 * @param msg contain the table information
	 * @return observable list
	 */
	public ObservableList<EvaluatorAppoitmentTable> LoadEvaluatorTable(MessageObject msg) {
		return getObservableListEvaluatorRequest((ArrayList<Object>)msg.getArgs());
	}

	/**
	 * get List evaluator from the db
	 * @param evaluatorList all the evaluators
	 * @return list of requests
	 */
	private ObservableList<EvaluatorAppoitmentTable> getObservableListEvaluatorRequest(ArrayList<Object> evaluatorList) {
		ObservableList<EvaluatorAppoitmentTable> request = FXCollections.observableArrayList();
		for (int i = 0; i < evaluatorList.size(); i++) {
			ArrayList<String> row = (ArrayList<String>)evaluatorList.get(i);
			request.add(new EvaluatorAppoitmentTable(row));
		}
		return request;
	}
	
	//***** Time Assessment *****//
	/**
	 * load the time assesment table
	 * @param msg time asssasment information
	 * @return request list
	 */
	public ObservableList<TimeAssessmentRequest> LoadTimeAssessmentTable(MessageObject msg) {
		return getObservableListTimeAssessmentRequest((ArrayList<Object>)msg.getArgs());
	}

	private ObservableList<TimeAssessmentRequest> getObservableListTimeAssessmentRequest(ArrayList<Object> timeAssessmentList) {
		ObservableList<TimeAssessmentRequest> request = FXCollections.observableArrayList();
		for (int i = 0; i < timeAssessmentList.size(); i++) {
			ArrayList<String> row = (ArrayList<String>)timeAssessmentList.get(i);
			request.add(new TimeAssessmentRequest(row));
		}
		return request;
	}

	//***** Time Extension *****//
	/**
	 * load the time extention table
	 * @param msg time asssasment information
	 * @return request list
	 */
	public ObservableList<TimeExtensionRequest> LoadTimeExtensionTable(MessageObject msg) {
		return getObservableListTimeExtensionRequest((ArrayList<Object>)msg.getArgs());
	}

	private ObservableList<TimeExtensionRequest> getObservableListTimeExtensionRequest(ArrayList<Object> timeExtensionList) {
		ObservableList<TimeExtensionRequest> request = FXCollections.observableArrayList();
		for (int i = 0; i < timeExtensionList.size(); i++) {
			ArrayList<String> row = (ArrayList<String>)timeExtensionList.get(i);
			request.add(new TimeExtensionRequest(row));
		}
		return request;
	}
	
	//***** Execution Leader Appointment *****//
	/**
	 * execution leader appointtment
	 * @param msg contain execution leader details
	 * @return request list
	 */
	public ObservableList<ExecutionLeaderAppointmentRequest> LoadExecutionLeaderTable(MessageObject msg) {
		return getObservableListExecutionLeaderRequest((ArrayList<Object>)msg.getArgs());
	}

	private ObservableList<ExecutionLeaderAppointmentRequest> getObservableListExecutionLeaderRequest(ArrayList<Object> executionLeaderList) {
		ObservableList<ExecutionLeaderAppointmentRequest> request = FXCollections.observableArrayList();
		for (int i = 0; i < executionLeaderList.size(); i++) {
			ArrayList<String> row = (ArrayList<String>)executionLeaderList.get(i);
			request.add(new ExecutionLeaderAppointmentRequest(row));
		}
		return request;
	}
	
	//***** Init Methods *****//

	/**
	 * inital evaluator table
	 */
	public void initEvaluatorTable() {
		sendMessage(new MessageObject(RequestType.GetEvaluatorTable, new ArrayList<>()));
	}
	/**
	 * inital time extension table
	 */
	public void initTimeExtensionTable() {
		sendMessage(new MessageObject(RequestType.GetTimeExtensionTable, new ArrayList<>()));
	}
	/**
	 * inital time assesment table
	 */
	public void initTimeAssessmentTable() {
		sendMessage(new MessageObject(RequestType.GetTimeAssessmentTable, new ArrayList<>()));
	}
	/**
	 * inital execution Table
	 */
	public void initExecutionLeaderTable() {
		sendMessage(new MessageObject(RequestType.GetExecutionLeaderTable, new ArrayList<>()));
	}
	
	/** This methods swaps the current stage to a new stage
	 *  
	 * @param requestID request id
	 * @param newStage the new stage
	 * @param currentStage the current stage
	 */
	public void swapStage(String requestID, String newStage, String currentStage) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		args.add(newStage);
		args.add(currentStage);
		MessageObject msg = new MessageObject(RequestType.swapStage, args);
		sendMessage(msg);
	}
}
