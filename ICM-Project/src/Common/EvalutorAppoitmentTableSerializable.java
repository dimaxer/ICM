package Common;

import java.io.Serializable;

/** This class represents a serializable evaluator appointment table */
public class EvalutorAppoitmentTableSerializable implements Serializable {

private String RequestID;
private String  Evalutorid;
private String  EvalutorName;

/** Constructor of serializable evaluator appointment table
 * 
 * @param requestID id
 * @param evalutorid id
 * @param evalutorName name
 */
public EvalutorAppoitmentTableSerializable (String requestID, String evalutorid, String evalutorName) {
	super();
	RequestID = requestID;
	Evalutorid = evalutorid;
	EvalutorName = evalutorName;
}

/** Constructor of serializable evaluator appointment table
 * 
 * @param evalutorid id
 * @param evalutorName name
 */
public EvalutorAppoitmentTableSerializable(String evalutorid,String evalutorName)
{
	Evalutorid = evalutorid;
	EvalutorName = evalutorName;
}

/** get request id
 * 
 * @return id
 */
public String getRequestID() {
	return RequestID;
}

/** set request id
 * 
 * @param requestID id
 */
public void setRequestID(String requestID) {
	RequestID = requestID;
}

/** get evaluator id
 * 
 * @return id
 */
public String getEvalutorid() {
	return Evalutorid;
}

/** set evaluator id
 * 
 * @param evalutorid id
 */
public void setEvalutorid(String evalutorid) {
	Evalutorid = evalutorid;
}

/** get evaluator name
 * 
 * @return name
 */
public String getEvalutorName() {
	return EvalutorName;
}

/** set evaluator name
 * 
 * @param evalutorName name
 */
public void setEvalutorName(String evalutorName) {
	EvalutorName = evalutorName;
}
}
