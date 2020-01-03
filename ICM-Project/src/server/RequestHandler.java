package server;

import java.util.ArrayList;

import Common.AttachedFile;
import Utilities.MessageObject;
import Utilities.RequestType;
import jdbc.mysqlConnection;
import ocsf.server.ConnectionToClient;

public class RequestHandler {
	private jdbc.RequestHandler mysqlRequestHandler = null;
	
	// Instance methods ************************************************
	/**
	 * This function handles messages from clients
	 * @param msg Message from Client
	 * @param client Client that sent the message
	 */
	public void handle(Object msg, ConnectionToClient client) {
		if (msg instanceof MessageObject) {
			// cast to MessageObject and send a print that a message was received
			MessageObject message = (MessageObject) msg;
			System.out.println("REACHED SERVER REQUEST HANDLER! " + message.getTypeRequest() + " " + message.getArgs());
			DBServer.getInstance().printMessageRecieved(message, client);
			
			mysqlRequestHandler = mysqlConnection.getInstance().handle();
			
			MessageObject responseMessage = null;
			switch (message.getTypeRequest()) {
			case Login:
				responseMessage = handleLogin(message, client);
				break;
			case View_Req_Details:
				responseMessage = handleSearchRequest(message, client);
				break;
			case change_Status:
				responseMessage = handleChangeStatus(message, client);
				break;
			case viewRequestTable:
			case refreshViewUserRequestTable:
				responseMessage = handleViewRequestTable(message, client);
				break;
			case NewChangeRequest:
				responseMessage = handleNewChange(message,client);
				break;
			case AttachedFile:
				handleAttachedFile(message, client);
				break;
			default:
				break;
			}
			
			if (responseMessage != null)
				DBServer.getInstance().sendMessage(responseMessage, client);
		}
		else
			System.out.println("Error - Message rechieved is not a MessageObject");
	}

	/**
	 * this method handles a creation of new change request
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	public MessageObject handleNewChange(MessageObject message, ConnectionToClient client) {
		boolean res= mysqlRequestHandler.addCRToDB(message.getArgs());
		
		ArrayList<Object> args = new ArrayList<Object>();
		if (res && message.getArgs().get(6) != null) {
			AttachedFile attachedFile = (AttachedFile) message.getArgs().get(6);
			attachedFile.setFileName(mysqlRequestHandler.getLastInsertID());
			args.add(attachedFile);
			
			message.setTypeRequest(RequestType.AttachedFile);
			message.setArgs(args);
			handleAttachedFile(message, client);
		}
		args.clear();
		args.add(res);

		message.setTypeRequest(RequestType.NewChangeRequest);
		message.setArgs(args);
		return message;
	}
	
	  /**
	   * This method handles any attached files received from the client.
	   *
	   * @param msg The message received from the client.
	   * @param client The connection from which the message originated.
	   * @author Raz Malka
	   */
	  public void handleAttachedFile(MessageObject message, ConnectionToClient client) {
		  if (message.getArgs().get(0) instanceof AttachedFile) {
			  AttachedFile attachedFile = (AttachedFile)message.getArgs().get(0);
			  attachedFile.copy();
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
	public MessageObject handleLogin(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.checkUserCredentials(message.getArgs().get(0).toString(),
					message.getArgs().get(1).toString());
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
		return mysqlRequestHandler.searchRequest(message);
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
			return mysqlRequestHandler.changeStatus((message));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public MessageObject handleViewRequestTable(MessageObject message, ConnectionToClient client) {
		try {
			return mysqlRequestHandler.viewRequestTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
