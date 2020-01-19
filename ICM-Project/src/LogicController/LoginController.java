package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

/** This is the logical controller of login */
public class LoginController extends BaseController {

	/**
	 * ligin to server event, send to server the information
	 * @param IDText user id
	 * @param passwordText password
	 */
	public void loginWasPressed(String IDText, String passwordText) {
		ArrayList<Object> args = new ArrayList<>();
		args.add(IDText);
		args.add(passwordText);
		MessageObject msg = new MessageObject(RequestType.Login, args);
		sendMessage(msg);
	}
}
