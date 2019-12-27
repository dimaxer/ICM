package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class BasePanelController extends BaseController {

	public BasePanelController(Client client) {
		super(client);
		
	}
	
	public void viewRequestDetailsWasPressed() {

		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(getClient().getUserID());
		MessageObject searchRequest = new MessageObject(RequestType.viewUserRequestTable, arrlist);
		sendMessage(searchRequest, getClient());
	}
	
}
