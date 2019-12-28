package client;

import Gui.LoginFX;
import Gui.PanelFX;
import Gui.RequestFormFX;
import Gui.ViewAllRequestsFX;
import Utilities.MessageObject;

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
	
	public void handle(Object msg) {
		MessageObject message = (MessageObject) msg;
		Client.getInstance().printMessageRecieved(message);
		Object currentFX = Client.getInstance().getCurrentFX();
		System.out.println("REACHED CLIENT REQUEST HANDLER! " + message.getTypeRequest() + " " + message.getArgs());
		switch (message.getTypeRequest()) {
		case Login:
			if (currentFX instanceof LoginFX)
				((LoginFX) currentFX).loginHandle(message);
			else
				System.out.println("Controller instance is NOT LoginController!");
			break;
		case View_Req_Details:
			if (currentFX instanceof ViewAllRequestsFX)
				((ViewAllRequestsFX) currentFX).handleSearchRequest(message);
			else
				System.out.println("Controller instance is NOT SearchRequestController!");
			break;
		case viewUserRequestTable:
			if (currentFX instanceof PanelFX)
				((PanelFX) currentFX).handleViewRequestDetailsRequest(message);
			else
				System.out.println("Controller instance is NOT PanelFXController!");
			break;
		case change_Status:
			if (currentFX instanceof RequestFormFX)
				((RequestFormFX) currentFX).handleChangeStatus(message);
			else
				System.out.println("Controller instance is NOT RequestFormController!");
			break;
		default:
			break;
		}
	}
}