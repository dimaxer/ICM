package Common;

import javafx.beans.property.SimpleStringProperty;

//class for saving data to put in table in ViewAllReqest FXML
public class ViewAllRequestsRequest {

	private SimpleStringProperty requestId,status,stage,myRole,initiator;

	public ViewAllRequestsRequest(String requestId, String status,
			String stage, String myRole, String initiator) {
		this.requestId = new SimpleStringProperty(requestId);
		this.status = new SimpleStringProperty(status);
		this.stage = new SimpleStringProperty(stage);
		this.myRole = new SimpleStringProperty(myRole);
		this.initiator = new SimpleStringProperty(initiator);
	}

	public String getRequestId() {
		return requestId.get();
	}

	public void setRequestId(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}
	
	public String getStage() {
		return stage.get();
	}

	public void setStage(SimpleStringProperty stage) {
		this.stage = stage;
	}

	public String getMyRole() {
		return myRole.get();
	}

	public void setMyRole(SimpleStringProperty myRole) {
		this.myRole = myRole;
	}

	public String getInitiator() {
		return initiator.get();
	}

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