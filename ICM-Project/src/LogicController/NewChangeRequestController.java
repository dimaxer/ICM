package LogicController;

import java.util.ArrayList;
import Utilities.MessageObject;
import Utilities.RequestType;

public class NewChangeRequestController extends BaseController 
{
	/**
	 * This method handles the event where submit was pressed logically.
	 * @author Raz Malka
	 */
	public void submitWasPressed(ArrayList<Object> args)
	{	
		MessageObject msg = new MessageObject(RequestType.NewChangeRequest, args);
		sendMessage(msg);
	}
}
