package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class SearchRequestController extends BaseController {

	public SearchRequestController(Client client) {
		super(client);

	}

	public void searchWasPressed(String requestID) {

		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(requestID);
		MessageObject searchRequest = new MessageObject(RequestType.View_Req_Details, arrlist);
		sendMessage(searchRequest, getClient());
	}

}
