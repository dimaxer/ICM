package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class EvaluationReportController extends BaseController {

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
	 * @param requestID
	 */
	public void loadReportDetails(String requestID) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		sendMessage(new MessageObject(RequestType.GetEvaluatorReport,args));
	}

	public void initShowAdditionalInfo(String requestID) {
		MessageObject msg = new MessageObject(RequestType.ShowAdditionalInfo, new ArrayList<>());
		msg.getArgs().add(requestID);
		sendMessage(msg);
	}
}
