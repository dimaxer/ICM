package Utilities;

/**
 * getLinkedRequests is for get all the requests that involve the user ID;
 * 
 */
public enum RequestType {
	Login, View_Req_Details, Submit_Cng, change_Status, NewChangeRequest, refreshViewUserRequestTable, AttachFile,
	viewISDRequestTable, viewRequestTable, ViewAttachedFiles, DownloadAttachedFiles, ApprovedEvaluator, AllUserDetails,
	InformationSystem_Details, PermanentRoles_Details, UpdateEvaluator, UpdatePermanentRoles,
	ErrorThisUserIsAllreadyLoggedIn, LogOut, ViewEvaluatorTable, UploadEvaluatorReport, ViewIseTable, GetEvaluatorReport, AdditionalInfo, ShowAdditionalInfo, swapStage, swapStatus, TestRejectionInfo, updateTesterRejectionDetails, GetTimeAssessmentTable, GetTimeExtensionTable, GetExecutionLeaderTable, GetEvaluatorTable, AcceptTimeAssessment, RejectTimeAssessment, RejectTimeExtension, AcceptTimeExtension, NewTimeAssessment, NewTimeExtension, GetDeadline, GetCommittee, SetTester, WaitsExecutionLeaderAppointment, SetWaitsExecutionLeaderAppointment, SetExecutionLeader, GetExecutionLeaderOptions, SetTesterBeforeAdhoc, GetRequestTable, GetEmployeeTable, GetReportStatusData, GetReportRejectedData, GetReportActiveDays, GetReportExtensions, GetReportDurations, GetReportDelays, SendMail, SaveReport, GetReports
}
