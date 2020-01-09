package Common;

import javafx.beans.property.SimpleStringProperty;

public class EvaluatorAppoitmentTable {

	private SimpleStringProperty RequestID, EvaluatorID, EvaluatorName;

	public EvaluatorAppoitmentTable(String requestId, String EvaluatorID, String EvaluatorName) {
		this.RequestID = new SimpleStringProperty(requestId);
		this.EvaluatorID = new SimpleStringProperty(EvaluatorID);
		this.EvaluatorName = new SimpleStringProperty(EvaluatorName);
	}

	public String getRequestID() {
		return RequestID.get();
	}

	public void setRequestID(SimpleStringProperty requestId) {
		this.RequestID = requestId;
	}

	public String getEvaluatorID() {
		return EvaluatorID.get();
	}

	public void setEvaluatorID(SimpleStringProperty EvaluatorID) {
		this.EvaluatorID = EvaluatorID;
	}

	public String getEvaluatorName() {
		return EvaluatorName.get();
	}

	public void setEvaluatorName(SimpleStringProperty EvaluatorName) {
		this.EvaluatorName = EvaluatorName;
	}
}
