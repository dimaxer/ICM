package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/** class for saving all the request data in one place under Request */
public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	// Form info
	private String requestID;
	private String informationSystem;
	private String currentSituation;
	private String requestedChange;
	private String reasonForRequest;
	private String note;
	private Boolean attachFiles;
	private String date;
	private Boolean adhoc;

	// request info
	private String currentStage;
	private String requestStatus;

	// initiator info

	private String initiatorID;
	// rolls in request info

	private String testerID;
	private String exequtionLeaderID;
	private String evaluatorID;
	
	/** Constructor of Request
	 * 
	 * @param requestID id
	 * @param informationSystem info system
	 * @param currentSituation situation
	 * @param requestedChange requested change
	 * @param reasonForRequest reason for change
	 * @param note notes
	 * @param attachFiles were there attached files
	 * @param date date
	 * @param currentStage stage
	 * @param requestStatus status
	 * @param initiatorID initiator
	 * @param testerID tester
	 * @param exequtionLeaderID execution leader
	 * @param evaluatorID evaluator
	 * @param adhoc was appointed adhoc
	 */
	public Request(String requestID, String informationSystem, String currentSituation, String requestedChange,
			String reasonForRequest, String note, Boolean attachFiles, String date, String currentStage,
			String requestStatus, String initiatorID, String testerID, String exequtionLeaderID, String evaluatorID, Boolean adhoc) {
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
		this.initiatorID = initiatorID;
		this.testerID = testerID;
		this.exequtionLeaderID = exequtionLeaderID;
		this.evaluatorID = evaluatorID;
		this.setAdhoc(adhoc);
	}

	/** get ervaluator id
	 * 
	 * @return id
	 */
	public String getEvaluatorID() {
		return evaluatorID;
	}

	/** set evaluator id
	 * 
	 * @param evaluatorID id
	 */
	public void setEvaluatorID(String evaluatorID) {
		this.evaluatorID = evaluatorID;
	}

	/**
	 * Constructor that get ResultSet of single Request and create the request make
	 * it easy to create a Request by simply give him the result from the DB
	 * @param result resultset of data
	 */
	public Request(ResultSet result) {
		try {
			this.requestID = result.getString("RequestID");
			this.informationSystem = result.getString("InformationSystem");
			this.currentSituation = result.getString("CurrentSituation");
			this.requestedChange = result.getString("RequestedChange");
			this.reasonForRequest = result.getString("RequestReason");
			this.note = result.getString("Note");
			this.attachFiles = result.getBoolean("AttachFiles");
			this.date = result.getString("Date");
			this.currentStage = result.getString("Stage");
			this.requestStatus = result.getString("Status");
			this.initiatorID = result.getString("InitiatorID");
			this.testerID = result.getString("TesterID");
			this.exequtionLeaderID = result.getString("ExecutionLeaderID");
			this.evaluatorID = result.getString("EvaluatorID");
			this.setAdhoc(result.getBoolean("adhoc"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** get request id
	 * 
	 * @return id
	 */
	public String getRequestID() {
		return requestID;
	}

	/** set request id
	 * 
	 * @param requestID id
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	/** get information system
	 * 
	 * @return info system
	 */
	public String getInformationSystem() {
		return informationSystem;
	}

	/** set information system
	 * 
	 * @param informationSystem info system
	 */
	public void setInformationSystem(String informationSystem) {
		this.informationSystem = informationSystem;
	}

	/** get situation
	 * 
	 * @return situation
	 */
	public String getCurrentSituation() {
		return currentSituation;
	}

	/** set situation
	 * 
	 * @param currentSituation situation
	 */
	public void setCurrentSituation(String currentSituation) {
		this.currentSituation = currentSituation;
	}

	/** get requested change
	 * 
	 * @return requested change
	 */
	public String getRequestedChange() {
		return requestedChange;
	}

	/** set requested change
	 * 
	 * @param requestedChange requested change
	 */
	public void setRequestedChange(String requestedChange) {
		this.requestedChange = requestedChange;
	}

	/** get reason for request
	 * 
	 * @return reason for request
	 */
	public String getReasonForRequest() {
		return reasonForRequest;
	}

	/** set reason for request
	 * 
	 * @param reasonForRequest reason for request
	 */
	public void setReasonForRequest(String reasonForRequest) {
		this.reasonForRequest = reasonForRequest;
	}

	/** get note
	 * 
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/** set note
	 * 
	 * @param note note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/** get attached files true or false
	 * 
	 * @return were there attached files
	 */
	public Boolean getAttachFiles() {
		return attachFiles;
	}

	/** set attached files
	 * 
	 * @param attachFiles were there attached files
	 */
	public void setAttachFiles(Boolean attachFiles) {
		this.attachFiles = attachFiles;
	}

	/** get date
	 * 
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/** set date
	 * 
	 * @param date date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/** get stage
	 * 
	 * @return stage
	 */
	public String getCurrentStage() {
		return currentStage;
	}

	/** set stage
	 * 
	 * @param currentStage stage
	 */
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	/** get request status
	 * 
	 * @return request stauts
	 */
	public String getRequestStatus() {
		return requestStatus;
	}

	/** set request status
	 * 
	 * @param requestStatus request status
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	/** get initiator id
	 * 
	 * @return id
	 */
	public String getInitaitorID() {
		return initiatorID;
	}

	/** set initiator id
	 * 
	 * @param initiatorID id
	 */
	public void setInitiatorID(String initiatorID) {
		this.initiatorID = initiatorID;
	}

	/** get tester id
	 * 
	 * @return id
	 */
	public String getTesterID() {
		return testerID;
	}

	/** set tester id
	 * 
	 * @param testerID id
	 */
	public void setTesterID(String testerID) {
		this.testerID = testerID;
	}

	/** get execution leader id
	 * 
	 * @return id
	 */
	public String getExequtionLeaderID() {
		return exequtionLeaderID;
	}

	/** set execution leader id
	 * 
	 * @param exequtionLeaderID id
	 */
	public void setExequtionLeaderID(String exequtionLeaderID) {
		this.exequtionLeaderID = exequtionLeaderID;
	}

	/** get appointed adhoc
	 * 
	 * @return appointed adhoc
	 */
	public Boolean getAdhoc() {
		return adhoc;
	}

	/** set appointed adhoc
	 * 
	 * @param adhoc appointed adhoc
	 */
	public void setAdhoc(Boolean adhoc) {
		this.adhoc = adhoc;
	}
}