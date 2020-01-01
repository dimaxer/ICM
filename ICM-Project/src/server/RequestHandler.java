package server;

import java.util.ArrayList;

import Common.AttachedFile;
import Utilities.MessageObject;
import Utilities.RequestType;
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
			case viewUserRequestTable:
				responseMessage = handleViewUserRequestTable(message, client);
				break;
			case refreshViewUserRequestTable:
				responseMessage = handleRefreshViewUserRequestTable(message, client);
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
	 * this method handles a refresh of View User Request Table
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	private MessageObject handleRefreshViewUserRequestTable(MessageObject message, ConnectionToClient client) {
		// TODO Auto-generated method stub
		try {
			return mysqlConnection.getInstance().viewUserRequestTable(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * this method handles a creation of new change request
	 * @param message
	 * @param client
	 * @return MessageObject that should be sent back to the client, indicating specific request response.
	 */
	public MessageObject handleNewChange(MessageObject message, ConnectionToClient client) {
		boolean res= mysqlConnection.getInstance().addCRToDB(message.getArgs());
		
		ArrayList<Object> args = new ArrayList<Object>();
		if (res && message.getArgs().get(6) != null) {
			AttachedFile attachedFile = (AttachedFile) message.getArgs().get(6);
			attachedFile.setFileName(mysqlConnection.getInstance().getLastInsertID());
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
			  attachedFile.copy("server\\RequestAttachments");
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
