package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;

public class LoginController extends BaseController {

	public LoginController(Client client) {
		super(client);

	}

	public void loginWasPressed(String IDText, String passwordText) {

		getClient().setUserID(IDText);// save the user id for future refrences
		ArrayList<Object> args = new ArrayList<>();
		args.add(IDText);
		args.add(passwordText);
		MessageObject msg = new MessageObject(RequestType.Login, args);
		sendMessage(msg, getClient());
	}

}
