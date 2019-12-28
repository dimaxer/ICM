package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class SearchRequestController extends BaseController {

	public void searchWasPressed(String requestID) {

		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(requestID);
		MessageObject searchRequest = new MessageObject(RequestType.View_Req_Details, arrlist);
		sendMessage(searchRequest);
	}

}
