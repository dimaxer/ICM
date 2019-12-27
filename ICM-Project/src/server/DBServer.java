package server;

import java.io.*;
import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import jdbc.mysqlConnection;
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
	final public static int DEFAULT_PORT = 5555;

	private static DBServer serverInstance;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public DBServer(int port) {
		super(port);
		serverInstance = this;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		if (msg instanceof MessageObject) {

			// cast to MessageObject and send a print that a message was received
			MessageObject message = (MessageObject) msg;
			printMessageRecieved(message, client);

			switch (message.getTypeRequest()) {
			case Login:
				handleLogin(message, client);
				break;
			case View_Req_Details:
				handleSearchRequest(message, client);
				break;
			case change_Status:
				handleChangeStatus(message, client);
				break;
			default:
				break;
			}

		}

		else {
			System.out.println("Error didnt recieve a MEssageObject");

		}

	}

	/**
	 * this method handles a login request from the client by extracting the
	 * username and password from MessageObject checking if its in the the db and
	 * sending back the message to the client if it was found or not
	 * 
	 * @param message
	 * @param client
	 */
	public void handleLogin(MessageObject message, ConnectionToClient client) {

		Boolean res = mysqlConnection.checkUserCredentials(message.getArgs().get(0).toString(),
				message.getArgs().get(1).toString());

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(res);

		MessageObject response = new MessageObject(RequestType.Login, args);

		sendMessage(response, client);
	}

	/**
	 * A method to search a request by id in the db and return its data
	 * 
	 * @param message
	 * @param client
	 */
	public void handleSearchRequest(MessageObject message, ConnectionToClient client) {

		MessageObject response = mysqlConnection.searchRequest(message);

		sendMessage(response, client);

	}

	/**
	 * adding function to change status in DB and return true or false to client if
	 * succeeded or not
	 */
	public void handleChangeStatus(MessageObject message, ConnectionToClient client) {

		MessageObject response = mysqlConnection.changeStatus((message));

		sendMessage(response, client);

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
	public static boolean startServer() {

		try {
			serverInstance.listen(); // Start listening for connections
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
	public static boolean closeServer() {
		try {
			serverInstance.close();
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
	private void sendMessage(MessageObject response, ConnectionToClient client) {

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
	private void printMessageRecieved(MessageObject message, ConnectionToClient client) {
		System.out.println("Message recieved: " + message.getTypeRequest().toString() + " | "
				+ message.getArgs().toString() + " from " + client);
	}
}