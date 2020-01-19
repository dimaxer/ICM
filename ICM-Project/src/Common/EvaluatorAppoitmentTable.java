package Common;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** This class represents an evaluator appointment table 
 */
public class EvaluatorAppoitmentTable   {

	private SimpleStringProperty RequestID, EvaluatorID, EvaluatorName;
	 
	/** Constructs an evaluator appointment table 
	 * 
	 * @param requestId id
	 * @param EvaluatorID id
	 * @param EvaluatorName name
	 */
	public EvaluatorAppoitmentTable(String requestId, String EvaluatorID, String EvaluatorName) {
		this.RequestID = new SimpleStringProperty(requestId);
		this.EvaluatorID = new SimpleStringProperty(EvaluatorID);
		this.EvaluatorName = new SimpleStringProperty(EvaluatorName);
	}

	/** Constructs an evaluator appointment table 
	 * 
	 * @param row row
	 */
	public EvaluatorAppoitmentTable(ArrayList<String> row) {
		this.RequestID = new SimpleStringProperty(row.get(0));
		this.EvaluatorID = new SimpleStringProperty(row.get(1));
		this.EvaluatorName = new SimpleStringProperty(row.get(2));
	}

	/**
	 * get request id
	 * @return id
	 */
	public String getRequestID() {
		return RequestID.get();
	}

	/** set request id
	 * 
	 * @param requestId id
	 */
	public void setRequestID(SimpleStringProperty requestId) {
		this.RequestID = requestId;
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
	 * @return name
	 */
	public String getEvaluatorName() {
		return EvaluatorName.get();
	}

	/** set evaluator name
	 * 
	 * @param EvaluatorName name
	 */
	public void setEvaluatorName(SimpleStringProperty EvaluatorName) {
		this.EvaluatorName = EvaluatorName;
	}
}
