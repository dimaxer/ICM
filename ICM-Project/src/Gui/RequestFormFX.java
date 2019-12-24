package Gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import LogicController.BaseController;
import LogicController.RequestFormController;
import Utilities.MessageObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RequestFormFX implements BaseFx, Initializable {

	@FXML
	JFXTextArea requestid;
	@FXML
	JFXTextArea initatorName;
	@FXML
	JFXTextArea handlerName;
	@FXML
	JFXComboBox<String> status;
	@FXML
	JFXTextArea currentStage;
	@FXML
	JFXTextArea descreaption;
	@FXML
	JFXButton Back;
	@FXML
	JFXButton home;
	@FXML
	JFXButton save;
	@FXML
	Text erorStatus;

	// Class variables *************************************************
	private RequestFormController requestFormController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		status.getItems().add("Active");
		status.getItems().add("Suspended");
		status.getItems().add("Frezze");

	}

	public void loadRequest(Object requestData) {
		MessageObject data = (MessageObject) requestData;
		ArrayList<Object> dataList = data.getArgs();
		initatorName.setText((String) dataList.get(1));
		requestid.setText((String) dataList.get(2));
		currentStage.setText((String) dataList.get(3));
		descreaption.setText((String) dataList.get(4));
		status.setPromptText((String) dataList.get(5));
		handlerName.setText((String) dataList.get(6));
	}

	@FXML
	public void backWasPressed(ActionEvent event) {
		requestFormController.switchScene("SearchRequest");
		SearchRequestFX FX = (SearchRequestFX) requestFormController.getCurrentFX();
		FX.clearFields();
	}

	@FXML
	public void homeWasPressed(ActionEvent event) {
		requestFormController.switchScene("AcademicUserPanel");

	}

	@FXML
	public void editWasPressed(ActionEvent event) {

		requestFormController.editWasPressed(requestid.getText(), status.getSelectionModel().getSelectedItem());

	}

	/**
	 * function to check if the status changed in the DB number greater then zero is
	 * good otherwise no
	 * 
	 */
	public void handleChangeStatus(MessageObject massage) {

		if (!(boolean) massage.getArgs().get(0)) {
			erorStatus.setText("The status hasn't been changed / already is the selected value ");
			erorStatus.setFill(Color.RED);

		} else
			erorStatus.setText("");

		{

		}
	}

	@Override
	public void setLogicController(BaseController controller) {
		requestFormController = (RequestFormController) controller;

	}

}
