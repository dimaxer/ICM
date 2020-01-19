package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

/** This is the logical controller of replace evaluator scene */
public class ReplaceEvaluatorSceneController extends BaseController {
	/**
	 * view ISE table event handler
	 */
	public void viewISETableWasPressed() {
		{
			
			ArrayList<Object> arrlist = new ArrayList<>();
			MessageObject ISETable = new MessageObject(RequestType.ViewIseTable, arrlist);
			sendMessage(ISETable);
		}
		
	}

}
