package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class RequestFormController extends BaseController {

	public void editWasPressed(String requestID, String status) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(requestID);
		list.add(status);
		MessageObject changeStatus = new MessageObject(RequestType.change_Status, list);
		sendMessage(changeStatus);
	}

}
