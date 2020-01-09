package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class ApprovementController extends BaseController {

	/**
	 * send to the server the evaluator that was approved by the supervisor
	 * 
	 * @param requestID
	 * @param evaluatorID
	 */
	public void approvedEvaluator(String requestID, String evaluatorID) {

		ArrayList<Object> list = new ArrayList<Object>();

		list.add(requestID);
		list.add(evaluatorID);

		MessageObject evaluatorMessage = new MessageObject(RequestType.ApprovedEvaluator, list);
		sendMessage(evaluatorMessage);

	}
}
