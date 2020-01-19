package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

/** This is the logical controller of evaluation report */
public class EvaluationReportController extends BaseController {

	/** Send report to server
	 * 
	 * @param description description
	 * @param constraints constraints
	 * @param result result
	 * @param requestID id
	 */
	public void sendReportToServer(String description, String constraints, String result,String requestID) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(description);
		list.add(constraints);
		list.add(result);
		list.add(requestID);
		list.add(Client.getInstance().getCurrentUser().getId());

		sendMessage(new MessageObject(RequestType.UploadEvaluatorReport, list));
	}

	/**
	 * load the report from the DB
	 * @param requestID id
	 */
	public void loadReportDetails(String requestID) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		sendMessage(new MessageObject(RequestType.GetEvaluatorReport,args));
	}

	/**
	 * inital info and send them to server
	 * 
	 * @param requestID request ID
	 */
	public void initShowAdditionalInfo(String requestID) {
		MessageObject msg = new MessageObject(RequestType.ShowAdditionalInfo, new ArrayList<>());
		msg.getArgs().add(requestID);
		sendMessage(msg);
	}
}
