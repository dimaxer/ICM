package Common;

import java.io.Serializable;

public class EvalutorAppoitmentTableSerializable implements Serializable {



private String RequestID;
private String  Evalutorid;
private String  EvalutorName;

public EvalutorAppoitmentTableSerializable (String requestID, String evalutorid, String evalutorName) {
	super();
	RequestID = requestID;
	Evalutorid = evalutorid;
	EvalutorName = evalutorName;
}

public EvalutorAppoitmentTableSerializable(String evalutorid,String evalutorName)
{
	Evalutorid = evalutorid;
	EvalutorName = evalutorName;
}

public String getRequestID() {
	return RequestID;
}

public void setRequestID(String requestID) {
	RequestID = requestID;
}

public String getEvalutorid() {
	return Evalutorid;
}

public void setEvalutorid(String evalutorid) {
	Evalutorid = evalutorid;
}

public String getEvalutorName() {
	return EvalutorName;
}

public void setEvalutorName(String evalutorName) {
	EvalutorName = evalutorName;
}
}
