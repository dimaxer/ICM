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
import Gui.StatisticsReportFX;
import Gui.ViewAllRequestsFX;
import Gui.ViewAllSystemDataFX;
import Gui.ViewAttachedFilesFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;

/** This class is the request handler of the Client UI */
public class ClientRequestHandler {
	
	// Instance methods ************************************************
	/** This method handles the requests of the Client UI
	 * 
	 * @param msg data
	 * @throws Exception exception
	 */
	public void handle(Object msg) throws Exception {
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
			if (currentFX instanceof ViewAllRequestsFX)
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
			else if (currentFX instanceof StatisticsReportFX)
				((StatisticsReportFX) currentFX).handleGetInfoSystems(message);
			break;
		case AllUserDetails:
			((ManagePermissionsFX) currentFX).handleUserDetails(message);
			break;
		case PermanentRoles_Details:
			((ManagePermissionsFX) currentFX).handlePermanentRoles(message);
			break;
		case ViewIseTable:
			((ReplaceEvaluatorSceneFX) currentFX).loadISETable(message);
			break;
		case UploadEvaluatorReport:
			((EvaluatorReportSubmitionFX) currentFX).reportWasSent(message);
			break;
		case GetEvaluatorReport:
			((EvaluatorReportSubmitionFX) currentFX).fillReportFilds(message);
			break;
		case swapStage:
			if (currentFX instanceof ViewAllRequestsFX)
				 ((ViewAllRequestsFX) currentFX).refreshWasPressed(null);
			else if (currentFX instanceof ManageApprovesFX) {
				((ManageApprovesFX) currentFX).initExecutionLeaderTable();
				((ManageApprovesFX) currentFX).initEvaluatorTable();
			}
			break;
		case swapStatus:
			((ViewAllRequestsFX) currentFX).refreshWasPressed(null);
			break;
		case ShowAdditionalInfo:
			((EvaluatorReportSubmitionFX) currentFX).handleShowAdditionalInfo(message);
			break;
		case TestRejectionInfo:
			if (currentFX instanceof RequestDetailsFX)
				((RequestDetailsFX) currentFX).handleTestRejectionInfo(message);
			break;
		case updateTesterRejectionDetails:
			((ViewAllRequestsFX) currentFX).refreshWasPressed(null);
			break;
		case GetEvaluatorTable:
			((ManageApprovesFX) currentFX).handleEvaluatorTable(message);
			break;
		case GetTimeAssessmentTable:
			((ManageApprovesFX) currentFX).handleTimeAssessmentTable(message);
			break;
		case GetTimeExtensionTable:
			if (currentFX instanceof ManageApprovesFX)
				((ManageApprovesFX) currentFX).handleTimeExtensionTable(message);
			break;
		case GetExecutionLeaderTable:
			if (currentFX instanceof ManageApprovesFX)
				((ManageApprovesFX) currentFX).handleExecutionLeaderTable(message);
			break;
		case GetDeadline:
			((RequestDetailsFX) currentFX).handleGetDeadline(message);
			break;
		case GetCommittee:
			if (currentFX instanceof RequestDetailsFX)
				((RequestDetailsFX) currentFX).handleCommittee(message);
			break;
		case SetTester:
			((RequestDetailsFX) currentFX).handleSetTester();
			break;
		case WaitsExecutionLeaderAppointment:
			if (currentFX instanceof RequestDetailsFX)
				((RequestDetailsFX) currentFX).handleWaitsExecutionLeader(message);
			break;
		case SetWaitsExecutionLeaderAppointment:
			if (currentFX instanceof RequestDetailsFX)
				((RequestDetailsFX) currentFX).handleSetWaitsExecutionLeader(message);
			break;
		case GetExecutionLeaderOptions:
			if (currentFX instanceof ManageApprovesFX)
				((ManageApprovesFX) currentFX).handleExecutionLeaderDetails(message);
			break;
		case SetExecutionLeader:
			((ManageApprovesFX) currentFX).refreshTables();
			break;
		case GetEmployeeTable:
			((ViewAllSystemDataFX) currentFX).handleEmployeeTable(message);
			break;
		case GetRequestTable:
			((ViewAllSystemDataFX) currentFX).handleRequestTable(message);
			break;
		case GetReportStatusData:
			((StatisticsReportFX) currentFX).handleGetReportStatusData(message);
			break;
		case GetReportRejectedData:
			((StatisticsReportFX) currentFX).handleGetReportRejectedData(message);
			break;
		case GetReportActiveDays:
			((StatisticsReportFX) currentFX).handleGetReportActiveDaysData(message);
			break;
		case GetReportExtensions:
			((StatisticsReportFX) currentFX).handleGetReportExtensionsData(message);
			break;
		case GetReportDurations:
			((StatisticsReportFX) currentFX).handleGetReportDurationsData(message);
			break;
		case GetReportDelays:
			((StatisticsReportFX) currentFX).handleGetReportDelaysData(message);
			break;
		case GetReports:
			((StatisticsReportFX) currentFX).handleGetReports(message);
			break;
		default:
			break;
		}
	}
}
