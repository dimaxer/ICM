package server;

import java.io.*;

import Utilities.MessageObject;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 */
public class DBServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final private static int DEFAULT_PORT = 5555;
	private static DBServer singletonInstance = null;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the server singleton.
	 *
	 * @param port The port number to connect on.
	 */
	private DBServer(int port) {
		super(port);
		singletonInstance = this;
	}

	// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * @return DBServer Singleton Instance
	 */
	public static DBServer getInstance() {
		if (singletonInstance == null)
			singletonInstance = new DBServer(DEFAULT_PORT);
		return singletonInstance;
	}
	
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		RequestHandler.getInstance().handle(msg, client);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	public void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	public void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * A method to start the server and listen for client
	 * 
	 * @return
	 */
	public boolean startServer() {

		try {
			singletonInstance.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			return false;
		}

		return true;
	}

	/**
	 * a method to shut down the server
	 * 
	 * @return
	 */
	public boolean closeServer() {
		try {
			singletonInstance.close();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * A function that sends a MessageObject to the Client and notify's if it was
	 * successfully sent via the console
	 * 
	 * @param response
	 * @param client
	 */
	public void sendMessage(MessageObject response, ConnectionToClient client) {

		try {
			client.sendToClient((Object) response);
			System.out.println("Message sent: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + " from Server");
		} catch (IOException e) {
			System.out.println("An Error occurd while trying to send: " + response.getTypeRequest().toString() + " | "
					+ response.getArgs().toString() + "to Client");
			e.printStackTrace();
		}
	}

	/**
	 * a printing function to print that a message was successfully received from
	 * the client
	 * 
	 * @param message
	 */
	public void printMessageRecieved(MessageObject message, ConnectionToClient client) {
		System.out.println("Message recieved: " + message.getTypeRequest().toString() + " | "
				+ message.getArgs().toString() + " from " + client);
	}
}