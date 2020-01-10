package LogicController;

import java.util.ArrayList;

import Gui.EvaluatorApproveSceneFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class ManageApprovmentsController extends BaseController {

	
	public void viewEvualuatorTableWasPressed() {
		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		MessageObject EvaluatorTable = new MessageObject(RequestType.ViewEvaluatorTable, arrlist);
		sendMessage(EvaluatorTable);
	}

	public void SendFieldsToNextScene(String requestID, String evaluatorID, String evaluatorName) {
		((EvaluatorApproveSceneFX) getCurrentFX()).SetFeilds(requestID,evaluatorID,evaluatorName);
	}	
	
	
	
	
}
