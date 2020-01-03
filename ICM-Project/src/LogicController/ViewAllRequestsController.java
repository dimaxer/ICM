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
			fillAllRequestTable(user, request); break;
		default: fillUserRequestTable(user, request, uid); break;
		}
		return request;
	}

	private void fillUserRequestTable(User user, ObservableList<ViewAllRequestsRequest> request, String uid) {
		for (Request r : user.getRequestArray()) {
			if (uid.equals(r.getTesterID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Tester",
						r.getInitaitorID()));
			} else if (uid.equals(r.getExequtionLeaderID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Execution Leader",
						r.getInitaitorID()));
			}else if (uid.equals(r.getEvaluatorID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Evaluator",
						r.getInitaitorID()));
			}else if (uid.equals(r.getInitaitorID())) {
				request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "Initiator",
						r.getInitaitorID()));
			}
		}
	}
	
	private void fillAllRequestTable(User user, ObservableList<ViewAllRequestsRequest> request) {
		for (Request r : user.getRequestArray()) {
			request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), Client.getInstance().getCurrentUser().getJobDescription(),
					r.getInitaitorID()));
		}
	}

	/**
	 * @author Raz Malka
	 */
	public void refresh() {
		// TODO Auto-generated method stub
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(Client.getInstance().getCurrentUser());
		MessageObject searchRequest = new MessageObject(RequestType.refreshViewUserRequestTable, arrlist);
		sendMessage(searchRequest);
	}

	/**
	 * @author Raz Malka
	 */
	public ObservableList<ViewAllRequestsRequest> loadPendingRequests(MessageObject message, TableView<ViewAllRequestsRequest> tableView) {
			ObservableList<ViewAllRequestsRequest> requests = getPendingObservableListRequest((User)message.getArgs().get(0));
			tableView.setItems(requests);
			return requests;
	}
	
	/**
	 * @author Raz Malka
	 */
	private ObservableList<ViewAllRequestsRequest> getPendingObservableListRequest(User user) {
		ObservableList<ViewAllRequestsRequest> request = FXCollections.observableArrayList();
		for (Request r : user.getRequestArray())
			request.add(new ViewAllRequestsRequest(r.getRequestID(), r.getRequestStatus(), "ISD Chief", r.getInitaitorID()));
		return request;
	}

}
