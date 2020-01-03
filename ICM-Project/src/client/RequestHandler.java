package client;

import Gui.LoginFX;
import Gui.NewChangeRequestFX;
import Gui.PanelFX;
import Gui.ViewAllRequestsFX;
import Utilities.MessageObject;
import Utilities.ScreenManager;

public class RequestHandler {
	// Instance methods ************************************************
	public void handle(Object msg) {
		MessageObject message = (MessageObject) msg;
		Client.getInstance().printMessageRecieved(message);
		Object currentFX = ScreenManager.getInstance().getCurrentFX();
		System.out.println("REACHED CLIENT REQUEST HANDLER! " + message.getTypeRequest() + " " + message.getArgs());
		switch (message.getTypeRequest()) {
		case Login:
			if (currentFX instanceof LoginFX)
				((LoginFX) currentFX).loginHandle(message);
			else
				System.out.println("FX instance is NOT LoginFX!");
			break;
		case View_Req_Details:
			if (currentFX instanceof ViewAllRequestsFX)
				((ViewAllRequestsFX) currentFX).handleSearchRequest(message);
			else
				System.out.println("FX instance is NOT SearchRequestFX!");
			break;
		case refreshViewUserRequestTable:
			if (currentFX instanceof ViewAllRequestsFX)
				((ViewAllRequestsFX) currentFX).handleRefresh(message);
			else
				System.out.println("FX instance is NOT ViewAllRequestsFX! " + currentFX.getClass().toString());
			break;
		case viewRequestTable:
			if (currentFX instanceof PanelFX)
				((PanelFX) currentFX).handleViewRequestDetailsRequest(message);
			else
				System.out.println("FX instance is NOT PanelFX!");
			break;
		case NewChangeRequest:
			if(currentFX instanceof NewChangeRequestFX)
				((NewChangeRequestFX) currentFX).newCRHandler(message);
			else System.out.println("FX instance is Not NewChangeRequestFX");
		default:
			break;
		}
	}
}
