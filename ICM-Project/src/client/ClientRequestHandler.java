package client;

import Gui.ApprovementFX;
import Gui.BaseFX;
import Gui.ManagePermissionsFX;
import Gui.NewChangeRequestFX;
import Gui.ViewAllRequestsFX;
import Gui.ViewAttachedFilesFX;
import Utilities.MessageObject;
import Utilities.ScreenManager;

public class ClientRequestHandler {
	// Instance methods ************************************************
	public void handle(Object msg) {
		MessageObject message = (MessageObject) msg;
		Client.getInstance().printMessageRecieved(message);
		Object currentFX = ScreenManager.getInstance().getCurrentFX();
		BaseFX baseFX = (BaseFX) ScreenManager.getInstance().getCurrentFX();

		switch (message.getTypeRequest()) {
		case ErrorThisUserIsAllreadyLoggedIn:
			baseFX.loginErrorAllreadyLoggedIn(message, currentFX);
			break;
		case Login:
			baseFX.loginHandle(message, currentFX);
			break;
		case View_Req_Details:
			baseFX.handleSearchRequest(message, currentFX);
			break;
		case refreshViewUserRequestTable:
			((ViewAllRequestsFX) currentFX).handleRefresh(message);
			break;
		case viewRequestTable:
			baseFX.handleViewRequestDetailsRequest(message);
			break;
		case NewChangeRequest:
			((NewChangeRequestFX) currentFX).newCRHandler(message);
			break;
		case ViewAttachedFiles:
			((ViewAttachedFilesFX) currentFX).handleShowAttachedFiles(message);
			break;
		case DownloadAttachedFiles:
			((ViewAttachedFilesFX) currentFX).handleDownloadAttachedFiles(message);
			break;
		case ApprovedEvaluator:
			((ApprovementFX) currentFX).handleDeleteEvaluatorFromTable(message);
			break;
		case InformationSystem_Details:
			if (currentFX instanceof ManagePermissionsFX)
				((ManagePermissionsFX) currentFX).handleInformationSystemComboBox(message);
			else if (currentFX instanceof NewChangeRequestFX)
				((NewChangeRequestFX) currentFX).handleInformationSystemComboBox(message);
			break;
		case AllUserDetails:
			((ManagePermissionsFX) currentFX).handleUserDetails(message);
			break;
		case PermanentRoles_Details:
			((ManagePermissionsFX) currentFX).handlePermanentRoles(message);
			break;

		default:
			break;
		}
	}
}
