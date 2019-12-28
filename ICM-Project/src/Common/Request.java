package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

//class for saving all the request data in one place under Request
public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	// Form info
	private String requestID;
	private String informationSystem;
	private String currentSituation;
	private String requestedChange;
	private String reasonForRequest;
	private String note;
	private String attachFiles;
	private String date;

	// request info
	private String currentStage;
	private String requestStatus;

	// initiator info

	private String initaitorID;
	// rolls in request info

	private String testerID;
	private String exequtionLeaderID;
	private String committeeMember1ID;
	private String committeeMember2ID;
	private String committeeChairmenID;
	private String evaluatorID;

	public Request(String requestID, String informationSystem, String currentSituation, String requestedChange,
			String reasonForRequest, String note, String attachFiles, String date, String currentStage,
			String requestStatus, String initaitorID, String testerID, String exequtionLeaderID,
			String committeeMember1ID, String committeeMember2ID, String committeeChairmenID, String evaluatorID) {
		super();
		this.requestID = requestID;
		this.informationSystem = informationSystem;
		this.currentSituation = currentSituation;
		this.requestedChange = requestedChange;
		this.reasonForRequest = reasonForRequest;
		this.note = note;
		this.attachFiles = attachFiles;
		this.date = date;
		this.currentStage = currentStage;
		this.requestStatus = requestStatus;
		this.initaitorID = initaitorID;
		this.testerID = testerID;
		this.exequtionLeaderID = exequtionLeaderID;
		this.committeeMember1ID = committeeMember1ID;
		this.committeeMember2ID = committeeMember2ID;
		this.committeeChairmenID = committeeChairmenID;
		this.evaluatorID = evaluatorID;
	}

	/*
	 * Contractor that get ResultSet of single Request and create the request make
	 * it easy to create a Request by simply give him the result from the DB
	 */
	public Request(ResultSet result) {
		try {
			this.requestID = result.getString("RequestID");
			this.informationSystem = result.getString("InformationSystem");
			this.currentSituation = result.getString("CurrentSituation");
			this.requestedChange = result.getString("RequestedChange");
			this.reasonForRequest = result.getString("ReasonForRequest");
			this.note = result.getString("Note");
			this.attachFiles = result.getString("AttachFiles");
			this.date = result.getString("Date");
			this.currentStage = result.getString("CurrentStage");
			this.requestStatus = result.getString("RequestStatus");
			this.initaitorID = result.getString("InitiatorID");
			this.testerID = result.getString("TesterID");
			this.exequtionLeaderID = result.getString("ExecutionLeaderID");
			this.committeeMember1ID = result.getString("CommitteeMember1ID");
			this.committeeMember2ID = result.getString("CommitteeMember2ID");
			this.committeeChairmenID = result.getString("CommitteeChairmenID");
			this.evaluatorID = result.getString("CommitteeChairmenID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getInformationSystem() {
		return informationSystem;
	}

	public void setInformationSystem(String informationSystem) {
		this.informationSystem = informationSystem;
	}

	public String getCurrentSituation() {
		return currentSituation;
	}

	public void setCurrentSituation(String currentSituation) {
		this.currentSituation = currentSituation;
	}

	public String getRequestedChange() {
		return requestedChange;
	}

	public void setRequestedChange(String requestedChange) {
		this.requestedChange = requestedChange;
	}

	public String getReasonForRequest() {
		return reasonForRequest;
	}

	public void setReasonForRequest(String reasonForRequest) {
		this.reasonForRequest = reasonForRequest;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getInitaitorID() {
		return initaitorID;
	}

	public void setInitaitorID(String initaitorID) {
		this.initaitorID = initaitorID;
	}

	public String getTesterID() {
		return testerID;
	}

	public void setTesterID(String testerID) {
		this.testerID = testerID;
	}

	public String getExequtionLeaderID() {
		return exequtionLeaderID;
	}

	public void setExequtionLeaderID(String exequtionLeaderID) {
		this.exequtionLeaderID = exequtionLeaderID;
	}

	public String getCommitteeMember1ID() {
		return committeeMember1ID;
	}

	public void setCommitteeMember1ID(String committeeMember1ID) {
		this.committeeMember1ID = committeeMember1ID;
	}

	public String getCommitteeMember2ID() {
		return committeeMember2ID;
	}

	public void setCommitteeMember2ID(String committeeMember2ID) {
		this.committeeMember2ID = committeeMember2ID;
	}

	public String getCommitteeChairmenID() {
		return committeeChairmenID;
	}

	public void setCommitteeChairmenID(String committeeChairmenID) {
		this.committeeChairmenID = committeeChairmenID;
	}

}