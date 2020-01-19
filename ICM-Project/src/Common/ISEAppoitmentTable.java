package Common;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** This class represents an ISE Appointment Table */
public class ISEAppoitmentTable   {

	private SimpleStringProperty EvaluatorID, EvaluatorName;
	 
	/** Constructor of ISE Appointment Table
	 * 
	 * @param EvaluatorID evaluator id
	 * @param EvaluatorName evaluator name
	 */
	public ISEAppoitmentTable(String EvaluatorID, String EvaluatorName) {
		this.EvaluatorID = new SimpleStringProperty(EvaluatorID);
		this.EvaluatorName = new SimpleStringProperty(EvaluatorName);
	}
	
	/** get evaluator id
	 * 
	 * @return id
	 */
	public String getEvaluatorID() {
		return EvaluatorID.get();
	}

	/** set evaluator id
	 * 
	 * @param EvaluatorID id
	 */
	public void setEvaluatorID(SimpleStringProperty EvaluatorID) {
		this.EvaluatorID = EvaluatorID;
	}

	/** get evaluator name
	 * 
	 * @return evaluator name
	 */
	public String getEvaluatorName() {
		return EvaluatorName.get();
	}

	/** set evaluator name
	 * 
	 * @param EvaluatorName evaluator name
	 */
	public void setEvaluatorName(SimpleStringProperty EvaluatorName) {
		this.EvaluatorName = EvaluatorName;
	}
}
