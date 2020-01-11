package Utilities;

//getLinkedRequests is for get all the requests that involve the user ID;
public enum RequestType {
	Login, View_Req_Details, Submit_Cng, change_Status, NewChangeRequest, refreshViewUserRequestTable, AttachFile,
	viewISDRequestTable, viewRequestTable, ViewAttachedFiles, DownloadAttachedFiles, ApprovedEvaluator, AllUserDetails,
	InformationSystem_Details, PermanentRoles_Details, UpdateEvaluator, UpdatePermanentRoles,
	ErrorThisUserIsAllreadyLoggedIn, LogOut, ViewEvaluatorTable, UploadEvaluatorReport, ViewIseTable
}
