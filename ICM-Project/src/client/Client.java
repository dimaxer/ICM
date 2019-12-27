package client;

import ocsf.client.*;

import java.io.*;

import Gui.ClientUI;

import Gui.LoginFX;
import Gui.PanelFX;
import Gui.RequestFormFX;

import Gui.SearchRequestFX;
import Gui.ViewAllRequestsFX;
import Utilities.MessageObject;
import Utilities.ScreenManager;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 */
public class Client extends AbstractClient {
	// Instance variables **********************************************

	private String userID;
	/**
	 * Instance of screenManager witch saves all scenes and the stage with this
	 * screenManager you can add new scenes and switch between them
	 */
	private ScreenManager screenManager;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public Client(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server. messages from
	 * server should only come as MessageObjects. this method checks what kind of
	 * Request was handled by the server and calls the right controller into action
	 * 
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {

		MessageObject message = (MessageObject) msg;
		printMessageRecieved(message);
		Object currentFX = getCurrentFX();

		switch (message.getTypeRequest()) {
		case Login: {
			if (currentFX instanceof LoginFX) {

				((LoginFX) currentFX).loginHandle(message);
			} else
				System.out.println("Controller instance is NOT LoginController!");
			break;
		}
		case View_Req_Details: {

			if (currentFX instanceof ViewAllRequestsFX) {

				((ViewAllRequestsFX) currentFX).handleSearchRequest(message);

			} else {
				System.out.println("Controller instance is NOT SearchRequestController!");
			}

			break;
		}
		case viewUserRequestTable: {
			if (currentFX instanceof PanelFX) {

				((PanelFX) currentFX).handleViewRequestDetailsRequest(message);

			} else {
				System.out.println("Controller instance is NOT PanelFXController!");
			}
			break;
			
		}
		case change_Status: {
			if (currentFX instanceof RequestFormFX) {

				((RequestFormFX) currentFX).handleChangeStatus(message);

			} else {
				System.out.println("Controller instance is NOT RequestFormController!");

			}
			break;
		}
		default:
			break;
		}

	}

	/**
	 * A method to switch Scene's on the main stage method needs the fxml file name
	 * (without .fxml)
	 * 
	 * @param fxml
	 */
	public void switchScene(String fxml) {
		try {
			screenManager.switchScene(fxml);
		} catch (Exception e) {
			System.out.println("Error occured while trying to switch to  scene: " + fxml);
			e.printStackTrace();
		}
	}

	/**
	 * returns the instance of the current controller that is being used by a scene
	 * it is important in order to use the controllers methods when you switch
	 * between new scene's
	 * 
	 * @return
	 */
	public Object getCurrentFX() {
		return screenManager.getCurrentFX();
	}

	public void setScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;

	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			sendToServer(message);

			System.out.println("Message sent: " + message + "from Client");

		} catch (IOException e) {
			System.out.println("Error Message wasnt sent to the server");
			e.printStackTrace();
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * this method prints the message that was received from the server
	 * 
	 * @param message
	 */
	public void printMessageRecieved(MessageObject message) {
		System.out.println("Message recieved: " + (message).getTypeRequest().toString() + " | "
				+ (message).getArgs().toString() + " from server");

	}

	public void setUserID(String id) {
		userID = id;
	}

	public String getUserID() {
		return userID;
	}
}
//End of ChatClient class
