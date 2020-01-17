package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class LoginController extends BaseController {

	public void loginWasPressed(String IDText, String passwordText) {
		ArrayList<Object> args = new ArrayList<>();
		args.add(IDText);
		args.add(passwordText);
		MessageObject msg = new MessageObject(RequestType.Login, args);
		sendMessage(msg);
	}
}
