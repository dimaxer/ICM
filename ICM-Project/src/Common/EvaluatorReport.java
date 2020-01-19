package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to save all the information of the EvaluatorReport
 * @author Shoham
 */
public class EvaluatorReport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String requestID;
	private String evaluatorID;
	private String description;
	private String constraints;
	private String result;
	
	/** Evaluator Report Constructor
	 * 
	 * @param requestID id
	 * @param evaluatorID id
	 * @param description description
	 * @param constraints constraints
	 * @param result result
	 */
	public EvaluatorReport(String requestID, String evaluatorID, String description, String constraints,
			String result) {
		super();
		this.requestID = requestID;
		this.evaluatorID = evaluatorID;
		this.description = description;
		this.constraints = constraints;
		this.result = result;
	}
	/**
	 * Evaluator Report Constructor
	 * @param report ResultSet that contain all the information from the DB about EvaluatorReport
	 */
	public EvaluatorReport(ResultSet report) {
		try {
			report.next();
			this.requestID = report.getString("RequestID");
			this.evaluatorID = report.getString("EvaluatorID");
			this.description = report.getString("Description");
			this.constraints = report.getString("Constraints");
			this.result = report.getString("Result");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	
	/** get evaluator id
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
	
	/** get description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/** set description
	 * 
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** get constraints
	 * 
	 * @return constraints
	 */
	public String getConstraints() {
		return constraints;
	}
	
	/** set constraints
	 * 
	 * @param constraints constraints
	 */
	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}
	
	/** get result
	 * 
	 * @return result
	 */
	public String getResult() {
		return result;
	}
	
	/** set result
	 * 
	 * @param result result
	 */
	public void setResult(String result) {
		this.result = result;
	}
}
