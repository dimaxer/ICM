package Common;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class ExecutionLeaderAppointmentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private SimpleStringProperty requestId, startTime;

	public ExecutionLeaderAppointmentRequest(String requestId, String startTime) {
		this.requestId = new SimpleStringProperty(requestId);
		this.startTime = new SimpleStringProperty(startTime);
	}
	
	public ExecutionLeaderAppointmentRequest(ArrayList<String> row) {
		this.requestId = new SimpleStringProperty(row.get(0));
		this.startTime = new SimpleStringProperty(row.get(1));
	}

	public String getRequestID() {
		return requestId.get();
	}

	public void setRequestID(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	public String getStartDate() {
		return startTime.get();
	}

	public void setStartDate(SimpleStringProperty startTime) {
		this.startTime = startTime;
	}
	
	@Override
	public boolean equals(Object request_obj) {
		if (!(request_obj instanceof ExecutionLeaderAppointmentRequest)) return false;
		ExecutionLeaderAppointmentRequest request = (ExecutionLeaderAppointmentRequest) request_obj;
		return requestId.get().equals(request.requestId.get());
	}
	
}