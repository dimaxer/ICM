package Common;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
/** This class represents an execution leader appointment request */
public class ExecutionLeaderAppointmentRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private SimpleStringProperty requestId, startTime;

	/** Constructor of execution leader appointment request
	 * 
	 * @param requestId id
	 * @param startTime startTime
	 */
	public ExecutionLeaderAppointmentRequest(String requestId, String startTime) {
		this.requestId = new SimpleStringProperty(requestId);
		this.startTime = new SimpleStringProperty(startTime);
	}
	
	/** Constructor of execution leader appointment request
	 * 
	 * @param row row
	 */
	public ExecutionLeaderAppointmentRequest(ArrayList<String> row) {
		this.requestId = new SimpleStringProperty(row.get(0));
		this.startTime = new SimpleStringProperty(row.get(1));
	}

	/** get request id
	 * 
	 * @return id
	 */
	public String getRequestID() {
		return requestId.get();
	}

	/** set request id
	 * 
	 * @param requestId id
	 */
	public void setRequestID(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	/** get start date
	 * 
	 * @return date
	 */
	public String getStartDate() {
		return startTime.get();
	}

	/**
	 * set start time
	 * @param startTime date
	 */
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