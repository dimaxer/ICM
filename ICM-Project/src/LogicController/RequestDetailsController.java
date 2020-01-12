package LogicController;

import java.io.File;
import java.util.ArrayList;

import Common.AttachedFile;
import Common.Request;
import Utilities.MessageObject;
import Utilities.RequestType;

/**
 * This Logic Controller class represents a generic Request Details,
 * which is hard-coded altogether with the FXML to provide support for all Roles.
 * @author Raz Malka
 * @since 01/01/2020
 */
public class RequestDetailsController extends BaseController 
{
	// Non Role-Specific Methods ***************************************
	/**
	 * Returns the Attached File if there is one
	 * @param event
	 */
	public AttachedFile getAttachedFile(String attachmentName) {
			try {
				File file = AttachedFile.getFileAttachmentByName(attachmentName);
				if (file != null)
					return new AttachedFile(file);
			} catch (Exception ex) {
				System.out.println("FILE DOES NOT EXIST!");
				ex.printStackTrace();
			}
		return null;
	}
	
	// Evaluator Methods ***********************************************
	/**
	 * Returns the Attached File if there is one
	 * @param event
	 */
	public AttachedFile getAttachedFile(Request request) {
		if (request.getAttachFiles()) // if file was initially attached
			return getAttachedFile(request.getRequestID());
		return null;
	}
	
	/**
	 * This method handles the case that 'attach file' button was pressed
	 * @param event
	 * @role Evaluator
	 */
	public void attachWasPressed(ArrayList<Object> args ) {
		AttachedFile af = (AttachedFile)args.get(0);
		String fileName = "E" + args.get(1).toString();
		af.setFileName(fileName);
		args.clear(); args.add(af);
		
		MessageObject msg = new MessageObject(RequestType.AttachFile, args);
		sendMessage(msg);	
	}
	/**
	 * send messege to the Server to move the request to Execution Stage
	 * @param requestID 
	 */
	public void moveRequestToExecutionStage(String requestID) {
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(requestID);
		MessageObject msg = new MessageObject(RequestType.moveRequestToExecutionStage, args);
		sendMessage(msg);
	}
}
