package LogicController;

import java.io.IOException;

import Utilities.MessageObject;

import client.Client;

public class BaseController {
	private Client client;

	public BaseController(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void switchScene(String fxml) {
		client.switchScene(fxml);
	}

	public Object getCurrentFX() {
		return client.getCurrentFX();
	}

	/**
	 * A function that sends a MessageObject to the Server and notify's if it was
	 * successfully sent via the console
	 * 
	 * @param response
	 * @param client
	 */
	protected void sendMessage(MessageObject response, Client client) {

		try {
			client.sendToServer((Object) response);
			System.out.println("Message sent: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " from Client");
		} catch (IOException e) {
			System.out.println("An Error occurd while trying to send: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + "to Client");
			e.printStackTrace();
		}
	}
}
