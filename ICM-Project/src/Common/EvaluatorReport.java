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
	 * 
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
	
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getEvaluatorID() {
		return evaluatorID;
	}
	public void setEvaluatorID(String evaluatorID) {
		this.evaluatorID = evaluatorID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getConstraints() {
		return constraints;
	}
	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
