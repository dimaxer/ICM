package LogicController;

import java.util.ArrayList;

import Common.Request;
import Common.User;
import Common.ViewAllRequestsRequest;
import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class ViewAllRequestsController extends BaseController {

	public void searchWasPressed(String requestID, String role) {

		// send the request id to the server
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(requestID);
		arrlist.add(role);
		MessageObject viewAllRequest = new MessageObject(RequestType.View_Req_Details, arrlist);
		sendMessage(viewAllRequest);
	}

	//load all the requests to the table
	public ObservableList<ViewAllRequestsRequest> loadRequests(MessageObject message, TableView<ViewAllRequestsRequest> tableView) {
		ObservableList<ViewAllRequestsRequest> requests = getObservableListRequest((User)message.getArgs().get(0));
		tableView.setItems(requests);
		return requests;

	}
	
	//Filing the right information to the columns
	private ObservableList<ViewAllRequestsRequest> getObservableListRequest(User user) {
		ObservableList<ViewAllRequestsRequest> request = FXCollections.observableArrayList();
		// Getting the right value for myRole field
		for (Request r : user.getRequestArray()) {
			if (Client.getInstance().getUserID().equals(r.getInitaitorID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Initiator",
						r.getInitaitorID()));
			} else if (Client.getInstance().getUserID().equals(r.getTesterID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Tester",
						r.getInitaitorID()));
			} else if (Client.getInstance().getUserID().equals(r.getCommitteeChairmenID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Committee Chairman",
						r.getInitaitorID()));
			} else if (Client.getInstance().getUserID().equals(r.getCommitteeMember1ID())
					|| Client.getInstance().getUserID().equals(r.getCommitteeMember2ID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Committee Member",
						r.getInitaitorID()));
			} else if (Client.getInstance().getUserID().equals(r.getExequtionLeaderID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Exeqution Leader",
						r.getInitaitorID()));
			}else if (Client.getInstance().getUserID().equals(r.getEvaluatorID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Evaluator",
						r.getInitaitorID()));
			}else if (user.getJobDescription().equals("Supervisor")) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Supervisor",
						r.getInitaitorID()));
			} else {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "ISD Chif",
						r.getInitaitorID()));
			}
		}
		return request;

	}

	public void refresh() {
		// TODO Auto-generated method stub
		System.out.println("P1");
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(Client.getInstance().getUserID());
		MessageObject searchRequest = new MessageObject(RequestType.refreshViewUserRequestTable, arrlist);
		sendMessage(searchRequest);
	}

}
