package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

public class ReplaceEvaluatorSceneController extends BaseController {

	public void viewISETableWasPressed() {
		{
			
			ArrayList<Object> arrlist = new ArrayList<>();
			MessageObject ISETable = new MessageObject(RequestType.ViewIseTable, arrlist);
			sendMessage(ISETable);
		}
		
	}

}
