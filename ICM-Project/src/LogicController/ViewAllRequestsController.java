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
		String uid = Client.getInstance().getCurrentUser().getId();
		// Getting the right value for myRole field
		switch (user.getJobDescription()) {
		case "Supervisor":
		case "ISD Chief":
		case "Committee Member":
		case "Committee Chairman":
			fillAllRequestTable(user, request, uid); break;
		default: fillUserRequestTable(user, request, uid); break;
		}
		return request;
	}

	private void fillUserRequestTable(User user, ObservableList<ViewAllRequestsRequest> request, String uid) {
		for (Request r : user.getRequestArray()) {
			if (uid.equals(r.getTesterID()) && r.getCurrentStage().equals("Testing")) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), r.getCurrentStage(), "Tester",
						r.getInitaitorID()));
			} else if (uid.equals(r.getExequtionLeaderID()) && r.getCurrentStage().equals("Execution")) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), r.getCurrentStage(), "Execution Leader",
						r.getInitaitorID()));
			}else if (uid.equals(r.getEvaluatorID()) && r.getCurrentStage().equals("Evaluation")) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), r.getCurrentStage(), "Evaluator",
						r.getInitaitorID()));
			}else if (uid.equals(r.getInitaitorID())) {
				if (r.getCurrentStage().equals("Decision") && (user.getJobDescription().equals("Committee Chairman") || user.getJobDescription().equals("Committee Member"))) continue;
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), r.getCurrentStage(), "Initiator",
						r.getInitaitorID()));
			}
		}
	}
	
	private void fillAllRequestTable(User user, ObservableList<ViewAllRequestsRequest> request, String uid) {
		if (!user.getJobDescription().equals("Supervisor"))
			fillUserRequestTable(user, request, uid);
		for (Request r : user.getRequestArray()) {
			if (!r.getCurrentStage().equals("Decision") &&
				(user.getJobDescription().equals("Committee Member") || user.getJobDescription().equals("Committee Chairman")))
				continue;
			
			ViewAllRequestsRequest requestToAdd = new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), r.getCurrentStage(),
					Client.getInstance().getCurrentUser().getJobDescription(), r.getInitaitorID());
			if (!request.contains(requestToAdd)) // Uncomment this if you want to skip rows for main role ~ Malka
				request.add(requestToAdd);
		}
	}

	/**

	 */
	public void refresh() {
		// TODO Auto-generated method stub
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(Client.getInstance().getCurrentUser());
		MessageObject searchRequest = new MessageObject(RequestType.refreshViewUserRequestTable, arrlist);
		sendMessage(searchRequest);
	}

}
