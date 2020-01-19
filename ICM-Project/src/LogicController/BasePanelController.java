package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

/** This is the logical controller of panel */
public class BasePanelController extends BaseController {
	/**
	 * view Request detail ssearch request
	 */
	public void viewRequestDetailsWasPressed() {

		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(Client.getInstance().getCurrentUser());
		MessageObject searchRequest = new MessageObject(RequestType.viewRequestTable, arrlist);
		sendMessage(searchRequest);
	}
}
