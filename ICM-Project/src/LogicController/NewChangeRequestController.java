package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class NewChangeRequestController extends BaseController 
{
	public void submitWasPressed(ArrayList<Object> args)
	{
		System.out.println("here3");
		System.out.println("here3");
		System.out.println("here3");

		MessageObject msg = new MessageObject(RequestType.NewChangeRequest, args);
		sendMessage(msg);
	}
}
