package LogicController;

import java.io.IOException;

import Utilities.MessageObject;
import Utilities.ScreenManager;
import client.Client;

public class BaseController {
	public void switchScene(String fxml_name) {
		try {
			ScreenManager.getInstance().switchScene(fxml_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function returns true if a certain scene already exists, and false
	 * otherwise. It should be utilized regularly before switching scenes in order
	 * to prevent reinitializing FX's and LC's over and over. Regular utilization
	 * will lead to extreme acceleration of scene switching.
	 * 
	 * @author Raz Malka
	 * @param fxml_name
	 * @return Boolean indicating whether the scene already exists.
	 */
	public Boolean sceneExists(String fxml_name) {
		return ScreenManager.getInstance().sceneExists(fxml_name);
	}

	public Object getCurrentFX() {
		return ScreenManager.getInstance().getCurrentFX();
	}

	/**
	 * A function that sends a MessageObject to the Server and notify's if it was
	 * successfully sent via the console
	 * 
	 * @param response
	 * @param client
	 */
	protected void sendMessage(MessageObject response) {
		try {
			Client.getInstance().sendToServer((Object) response);
			System.out.println("Message sent: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " from Client");
		} catch (IOException e) {
			System.out.println("An Error occurd while trying to send: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " to Client");
			e.printStackTrace();
		}
	}
}
