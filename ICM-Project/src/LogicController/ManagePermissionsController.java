package LogicController;

import java.util.ArrayList;

import Utilities.MessageObject;
import Utilities.RequestType;

/** This is the logical controller of manage permissions */
public class ManagePermissionsController extends BaseController {

	public void initUserDetails() {
		MessageObject msg = new MessageObject(RequestType.AllUserDetails, new ArrayList<>());
		sendMessage(msg);
	}

	public void initPermanentRoles() {
		MessageObject msg = new MessageObject(RequestType.PermanentRoles_Details, new ArrayList<>());
		sendMessage(msg);
	}

	public void updateEvaluator(String infoSysName, String infoSysEvaluatorID) {
		ArrayList<Object> args = new ArrayList<>();
		args.add(infoSysName);
		args.add(infoSysEvaluatorID);
		MessageObject msg = new MessageObject(RequestType.UpdateEvaluator, args);
		
		sendMessage(msg);
	}

	public void updatePermanentRoles(String supervisorID, String committeeChairmanID, String committeeMember1ID,
			String committeeMember2ID) {
		ArrayList<Object> args = new ArrayList<>();
		args.add(supervisorID);
		args.add(committeeChairmanID);
		args.add(committeeMember1ID);
		args.add(committeeMember2ID);
		MessageObject msg = new MessageObject(RequestType.UpdatePermanentRoles, args);
		
		sendMessage(msg);
	}
}
