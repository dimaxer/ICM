package Common;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.jfoenix.controls.JFXAlert;

import Gui.ManageApprovesFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import client.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class TimeExtensionRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private SimpleStringProperty requestId,userID,jobDescription,startTime,endTime;
	private String Stage = "";

	public TimeExtensionRequest(String requestId, String userID,
			String jobDescription, String startTime, String endTime, String Stage) {
		this.requestId = new SimpleStringProperty(requestId);
		this.userID = new SimpleStringProperty(userID);
		this.jobDescription = new SimpleStringProperty(jobDescription);
		this.startTime = new SimpleStringProperty(startTime);
		this.endTime = new SimpleStringProperty(endTime);
		this.setStage(Stage);
	}
	
	public TimeExtensionRequest(ArrayList<String> row) {
		this.requestId = new SimpleStringProperty(row.get(0));
		this.userID = new SimpleStringProperty(row.get(1));
		this.jobDescription = new SimpleStringProperty(row.get(2));
		this.startTime = new SimpleStringProperty(row.get(3));
		this.endTime = new SimpleStringProperty(row.get(4));
		this.setStage(row.get(5));
	}

	public ObservableValue<String> RequestIDProperty() {
		return requestId;
	}
	
	public ObservableValue<String> StartDateProperty() {
		return startTime;
	}
	
	public String getRequestID() {
		return requestId.get();
	}

	public void setRequestID(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	public String getUserID() {
		return userID.get();
	}

	public void setUserID(SimpleStringProperty userID) {
		this.userID = userID;
	}

	public String getJobDescription() {
		return jobDescription.get();
	}

	public void setJobDescription(SimpleStringProperty jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getStartDate() {
		return startTime.get();
	}

	public void setStartDate(SimpleStringProperty startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endTime.get();
	}

	public void setEndDate(SimpleStringProperty endTime) {
		this.endTime = endTime;
	}
	
	public Button getAccept() {
		Button accept = new Button("Accept");
		accept.setPrefWidth(128);
		accept.setOnAction((ActionEvent event) -> {
			MessageObject response = new MessageObject(RequestType.AcceptTimeExtension, new ArrayList());
			response.getArgs().add(requestId.get());
			response.getArgs().add(endTime.get());
			response.getArgs().add(Stage);
			try {
				Client.getInstance().sendToServer(response);
			} catch (IOException e) {e.printStackTrace();}
			((ManageApprovesFX) ScreenManager.getInstance().getCurrentFX()).initTimeExtensionTable();
		});
		return accept;
	}
	
	public Button getReject() {
		Button reject = new Button("Reject");
		reject.setPrefWidth(128);
		reject.setOnAction((ActionEvent event) -> {
			JFXAlert<Void> rejectionDetailsPopup = new JFXAlert<Void>();
			AnchorPane anchorPane = new AnchorPane();
			TextArea node = new TextArea();
			
			// Set TextArea node
			node.setPromptText("Please Enter Rejection Details");
			node.setPrefHeight(200);
			node.setWrapText(true);
			node.setEditable(true);
			node.setLayoutX(7);
			node.setLayoutY(7);
			
			// Set Send Button
			Button send = new Button("Send");
			send.setOnAction((ActionEvent sendEvent) -> {
				if (node.getText().equals("")) {
					send.requestFocus();
					return;
				}
				MessageObject response = new MessageObject(RequestType.RejectTimeExtension, new ArrayList());
				response.getArgs().add(requestId.get());
				response.getArgs().add(userID.get());
				response.getArgs().add(node.getText());
				response.getArgs().add(Stage);
				try {
					Client.getInstance().sendToServer(response);
				} catch (IOException e) {e.printStackTrace();}
				rejectionDetailsPopup.close();
			});
			send.setLayoutX(220);
			send.setLayoutY(220);
			
			anchorPane.getChildren().addAll(node, send);
			
			// Set JFXAlert Dialogue
			rejectionDetailsPopup.setTitle("Time Extension Rejection Details: ");
			rejectionDetailsPopup.setContent(anchorPane);
			rejectionDetailsPopup.setSize(492, 250);
			rejectionDetailsPopup.showAndWait();
			((ManageApprovesFX) ScreenManager.getInstance().getCurrentFX()).initTimeExtensionTable();
		});
		return reject;
	}
	
	@Override
	public boolean equals(Object request_obj) {
		if (!(request_obj instanceof TimeExtensionRequest)) return false;
		TimeExtensionRequest request = (TimeExtensionRequest) request_obj;
		return requestId.get().equals(request.requestId.get());
	}

	public String getStage() {
		return Stage;
	}

	public void setStage(String stage) {
		Stage = stage;
	}
	
}