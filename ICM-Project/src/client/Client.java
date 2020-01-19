package client;

import ocsf.client.*;
import java.io.*;

import Common.User;
import Utilities.MessageObject;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 */
public class Client extends AbstractClient {
	// Instance variables **********************************************
	private User currentUser;
	private static Client singletonInstance = null;
	private ClientRequestHandler requestHandler = null;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 */

	private Client(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
		requestHandler = new ClientRequestHandler();
	}

	/**
	 * Get the Singleton's Instance
	 * 
	 * @return Client Singleton Instance
	 */
	public static Client getInstance() {
		if (singletonInstance == null)
			try {
				singletonInstance = new Client(ClientUI.DEFAULT_SERVER, ClientUI.DEFAULT_PORT);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		return singletonInstance;
	}

	/** This method initializes this instance
	 * 
	 * @param host host name
	 * @param port port name
	 * @throws IOException exception
	 */
	public static void initialize(String host, int port) throws IOException {
		singletonInstance = new Client(host, port);
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
		if (!msg.equals("check if alive"))
			try {
				requestHandler.handle(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	 * @param message data
	 */
	public void printMessageRecieved(MessageObject message) {
		System.out.println("Message recieved: " + (message).getTypeRequest().toString() + " | "
				+ (message).getArgs().toString() + " from server");

	}

	/**
	 * This gets the current user
	 * @return current user
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * This sets the current user
	 * @param currentUser user
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
