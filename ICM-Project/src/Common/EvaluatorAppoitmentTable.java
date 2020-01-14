package Common;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EvaluatorAppoitmentTable   {


	
	private SimpleStringProperty RequestID, EvaluatorID, EvaluatorName;
	 
	 
	public EvaluatorAppoitmentTable(String requestId, String EvaluatorID, String EvaluatorName) {
		this.RequestID = new SimpleStringProperty(requestId);
		this.EvaluatorID = new SimpleStringProperty(EvaluatorID);
		this.EvaluatorName = new SimpleStringProperty(EvaluatorName);
	}

	
	
	public EvaluatorAppoitmentTable(ArrayList<String> row) {
		this.RequestID = new SimpleStringProperty(row.get(0));
		this.EvaluatorID = new SimpleStringProperty(row.get(1));
		this.EvaluatorName = new SimpleStringProperty(row.get(2));
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
