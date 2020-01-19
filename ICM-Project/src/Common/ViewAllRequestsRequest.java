package Common;

import javafx.beans.property.SimpleStringProperty;

/** class for saving data to put in table in ViewAllReqest FXML */
public class ViewAllRequestsRequest {

	private SimpleStringProperty requestId,status,stage,myRole,initiator;

	/** View All Request Constructor
	 * 
	 * @param requestId id
	 * @param status status
 	 * @param stage stage
	 * @param myRole role
	 * @param initiator initiator id
	 */
	public ViewAllRequestsRequest(String requestId, String status,
			String stage, String myRole, String initiator) {
		this.requestId = new SimpleStringProperty(requestId);
		this.status = new SimpleStringProperty(status);
		this.stage = new SimpleStringProperty(stage);
		this.myRole = new SimpleStringProperty(myRole);
		this.initiator = new SimpleStringProperty(initiator);
	}

	/** get request id
	 * 
	 * @return id
	 */
	public String getRequestId() {
		return requestId.get();
	}

	/** set request id
	 * 
	 * @param requestId request id
	 */
	public void setRequestId(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	/** get status
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status.get();
	}

	/** set status
	 * 
	 * @param status status
	 */
	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}
	
	/** get stage
	 * 
	 * @return stage
	 */
	public String getStage() {
		return stage.get();
	}

	/** set stage
	 * 
	 * @param stage stage
	 */
	public void setStage(SimpleStringProperty stage) {
		this.stage = stage;
	}

	/** get role
	 * 
	 * @return role
	 */
	public String getMyRole() {
		return myRole.get();
	}

	/** set role
	 * 
	 * @param myRole role
	 */
	public void setMyRole(SimpleStringProperty myRole) {
		this.myRole = myRole;
	}

	/** get initiator id
	 * 
	 * @return initiator id
	 */
	public String getInitiator() {
		return initiator.get();
	}

	/** set initiator id
	 * 
	 * @param initiator initiator id
	 */
	public void setInitiator(SimpleStringProperty initiator) {
		this.initiator = initiator;
	}
	
	@Override
	public boolean equals(Object request_obj) {
		if (!(request_obj instanceof ViewAllRequestsRequest)) return false;
		ViewAllRequestsRequest request = (ViewAllRequestsRequest) request_obj;
		return requestId.get().equals(request.requestId.get());
	}
	
}