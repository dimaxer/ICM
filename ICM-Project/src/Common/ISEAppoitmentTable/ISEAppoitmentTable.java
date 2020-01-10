package Common.ISEAppoitmentTable;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ISEAppoitmentTable   {

	private SimpleStringProperty EvaluatorID, EvaluatorName;
	 
	 
	public ISEAppoitmentTable(String EvaluatorID, String EvaluatorName) {
		this.EvaluatorID = new SimpleStringProperty(EvaluatorID);
		this.EvaluatorName = new SimpleStringProperty(EvaluatorName);
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
