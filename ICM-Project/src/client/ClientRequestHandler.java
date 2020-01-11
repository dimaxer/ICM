package client;

import Gui.BaseFX;
import Gui.EvaluatorApproveSceneFX;
import Gui.EvaluatorReportSubmitionFX;
import Gui.LoginFX;
import Gui.ManageApprovesFX;
import Gui.ManagePermissionsFX;
import Gui.NewChangeRequestFX;
import Gui.ReplaceEvaluatorSceneFX;
import Gui.RequestDetailsFX;
import Gui.ViewAllRequestsFX;
import Gui.ViewAttachedFilesFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;

public class ClientRequestHandler {
	// Instance methods ************************************************
	public void handle(Object msg) {
		MessageObject message = (MessageObject) msg;
		Client.getInstance().printMessageRecieved(message);
		Object currentFX = ScreenManager.getInstance().getCurrentFX();
		BaseFX baseFX = (BaseFX) ScreenManager.getInstance().getCurrentFX();
		// if(message.getTypeRequest()!=RequestType.viewRequestTable)
		// checkInstance(message.getTypeRequest(),currentFX);
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
			if (currentFX instanceof EvaluatorApproveSceneFX)
				((EvaluatorApproveSceneFX) currentFX).loadEvaluatorTable();
			else if (currentFX instanceof ReplaceEvaluatorSceneFX)
				((ReplaceEvaluatorSceneFX) currentFX).loadEvaluatorTable();
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
		case ViewEvaluatorTable:
			((ManageApprovesFX) currentFX).loadEvaluatorTable(message);
			break;
		case ViewIseTable:
			((ReplaceEvaluatorSceneFX) currentFX).loadISETable(message);
			break;
		case UploadEvaluatorReport:
			((EvaluatorReportSubmitionFX) currentFX).reportWasSent(message);
		default:
			break;
		}
	}
	/**
	 * public void checkInstance(RequestType type, Object currentFX) { switch (type)
	 * { case Login: if (!(currentFX instanceof LoginFX)) System.out.println("ERROR
	 * CURRENT_FX NOT INSTANCE OF LoginFX"); break; case View_Req_Details: if
	 * (!(currentFX instanceof RequestDetailsFX)) System.out.println("ERROR
	 * CURRENT_FX NOT INSTANCE OF RequestDetailsFX"); break; case
	 * refreshViewUserRequestTable: if (!(currentFX instanceof ViewAllRequestsFX))
	 * System.out.println("ERROR CURRENT_FX NOT INSTANCE OF ViewAllRequestsFX");
	 * break; case viewRequestTable: if (!(currentFX instanceof ViewAllRequestsFX))
	 * System.out.println("ERROR CURRENT_FX NOT INSTANCE OF ViewAllRequestsFX");
	 * break; case NewChangeRequest: if (!(currentFX instanceof NewChangeRequestFX))
	 * System.out.println("ERROR CURRENT_FX NOT INSTANCE OF NewChangeRequestFX");
	 * break; case ViewAttachedFiles: if (!(currentFX instanceof
	 * ViewAttachedFilesFX)) System.out.println("ERROR CURRENT_FX NOT INSTANCE OF
	 * ViewAttachedFilesFX"); case DownloadAttachedFiles: if (!(currentFX instanceof
	 * ViewAttachedFilesFX)) System.out.println("ERROR CURRENT_FX NOT INSTANCE OF
	 * ViewAttachedFilesFX"); break; case ApprovedEvaluator: if (!(currentFX
	 * instanceof LoginFX)) System.out.println("ERROR CURRENT_FX NOT INSTANCE OF
	 * EvaluatorApproveSceneFX"); break; case InformationSystem_Details: if
	 * (!(currentFX instanceof ManagePermissionsFX)) if (!(currentFX instanceof
	 * NewChangeRequestFX)) System.out.println("ERROR While trying to switch scene
	 * from main panelS"); break; case AllUserDetails: if (!(currentFX instanceof
	 * ManagePermissionsFX)) System.out.println("ERROR CURRENT_FX NOT INSTANCE OF
	 * ManagePermissionsFX"); break; case PermanentRoles_Details: if (!(currentFX
	 * instanceof ManagePermissionsFX)) System.out.println("ERROR CURRENT_FX NOT
	 * INSTANCE OF ManagePermissionsFX"); break; case ViewEvaluatorTable: if
	 * (!(currentFX instanceof ManageApprovesFX)) System.out.println("ERROR
	 * CURRENT_FX NOT INSTANCE OF ManageApprovesFX"); break; default: break; } }
	 */

}
