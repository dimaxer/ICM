package server;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;
import jdbc.mysqlConnection;
import ocsf.server.ConnectionToClient;

public class RequestHandler {
	private static RequestHandler singletonInstance = null;
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the RequestHandler singleton.
	 */
	private RequestHandler() {
		singletonInstance = this;
	}

	// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * @return RequestHandler Singleton Instance
	 */
	public static RequestHandler getInstance() {
		if (singletonInstance == null)
			singletonInstance = new RequestHandler();
		return singletonInstance;
	}
	
	public void handle(Object msg, ConnectionToClient client) {
		if (msg instanceof MessageObject) {
			// cast to MessageObject and send a print that a message was received
			MessageObject message = (MessageObject) msg;
			System.out.println("REACHED SERVER REQUEST HANDLER! " + message.getTypeRequest() + " " + message.getArgs());
			DBServer.getInstance().printMessageRecieved(message, client);
			switch (message.getTypeRequest()) {
			case Login:
				DBServer.getInstance().sendMessage(handleLogin(message, client), client);
				break;
			case View_Req_Details:
				DBServer.getInstance().sendMessage(handleSearchRequest(message, client), client);
				break;
			case change_Status:
				DBServer.getInstance().sendMessage(handleChangeStatus(message, client), client);
				break;
			case viewUserRequestTable:
				DBServer.getInstance().sendMessage(handleViewUserRequestTable(message, client), client);
				break;
			case NewChangeRequest:
				DBServer.getInstance().sendMessage(handleNewChange(message,client),client);
			default:
				break;
			}

		}
		else {
			System.out.println("Error didnt recieve a MessageObject");
		}
	}
	/**
	 * this method handles a login request from the client by extracting the
	 * username and password from MessageObject checking if its in the the db and
	 * sending back the message to the client if it was found or not
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	public MessageObject handleNewChange(MessageObject message, ConnectionToClient client) {

		ArrayList<Object> formysql = new ArrayList<Object>();
		formysql=message.getArgs();
		String[] strings =new String[8];
		for(int i=0;i<7;i++)
		{
			strings[i]=formysql.get(i).toString();
			System.out.println(strings[i]);

		}
		//
		System.out.println("here2");
		System.out.println("here2");
		//System.out.println(Client.getInstance().getUserID());
		System.out.println("here2");
		

		boolean res= mysqlConnection.getInstance().addCRToDB(strings[0],strings[1],strings[2],"hi","hi",strings[3],strings[4],strings[5],strings[6]);
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(res);

		MessageObject response = new MessageObject(RequestType.NewChangeRequest, args);
		return response;

		//sendMessage(response, client);
	}
	public MessageObject handleLogin(MessageObject message, ConnectionToClient client) {
		try {
			Boolean res = mysqlConnection.getInstance().checkUserCredentials(message.getArgs().get(0).toString(),
					message.getArgs().get(1).toString());

			ArrayList<Object> args = new ArrayList<Object>();
			args.add(res);

			return new MessageObject(RequestType.Login, args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * A method to search a request by id in the db and return its data
	 * 
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	public MessageObject handleSearchRequest(MessageObject message, ConnectionToClient client) {
		try {
		return mysqlConnection.getInstance().searchRequest(message);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * adding function to change status in DB and return true or false to client if
	 * succeeded or not
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	public MessageObject handleChangeStatus(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlConnection.getInstance().changeStatus((message));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public MessageObject handleViewUserRequestTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlConnection.getInstance().viewUserRequestTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
