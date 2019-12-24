package Gui;

import java.net.URL;

import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import LogicController.BaseController;
import LogicController.SearchRequestController;
import Utilities.MessageObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SearchRequestFX implements BaseFx, Initializable {
	// Class Buttons ***************************************************
	@FXML
	private JFXButton search;

	@FXML
	private JFXButton back;

	@FXML
	private Text requestIdNotFound;

	@FXML
	private ImageView eror;

	@FXML
	private JFXTextField requestID;

	// Class variables *************************************************
	private String requestIDText;
	private SearchRequestController searchRequestController;

	// Class methods ***************************************************
	/**
	 * event handler for the search button receives the text that was written into
	 * the text field and sends the request id to the server so that it can check if
	 * its the db or not
	 * 
	 * @param event
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		eror.setVisible(false);
	}

	@FXML
	public void searchWasPressed(ActionEvent event) {
		setValdiator();
		requestIDText = requestID.getText();

		// check for empty text
		if (requestIDText.length() <= 0) {
			requestID.validate();
			eror.setVisible(true);
			return;
		}

		// send the request id to the server
		searchRequestController.searchWasPressed(requestID.getText());

	}

	/**
	 * This method checks if the request id was found in the db or not by checking
	 * the boolean and switches screens to the request form with the message object
	 * so that it could be initialized with the request's information
	 * 
	 * @param massage
	 */
	public void handleSearchRequest(MessageObject massage) {

		if ((boolean) massage.getArgs().get(0)) {
			searchRequestController.switchScene("RequestForm");
			RequestFormFX FX = (RequestFormFX) searchRequestController.getCurrentFX();
			FX.loadRequest(massage);
		} else {
			requestIdNotFound.setFill(Color.RED);
			requestIdNotFound.setText("Request id was not found");
		}

	}

	/**
	 * event handler for the back button goes back to the previous screen
	 * AcademicUserPanel
	 * 
	 * 
	 * @param event
	 */

	@FXML
	public void backWasPressed(ActionEvent event) {

		searchRequestController.switchScene("AcademicUserPanel");
	}

	/**
	 * a method to clear the requesID
	 */
	@FXML
	public void clearFields() {
		requestID.setText("");
		requestIdNotFound.setText("");
		eror.setVisible(false);
	}

	/**
	 * a method that shows a Massage when no input is set in the UserName or
	 * Password
	 * 
	 */
	public void setValdiator() {
		RequiredFieldValidator validatorRequestId = new RequiredFieldValidator();
		requestID.getValidators().add(validatorRequestId);
		validatorRequestId.setMessage("No Input Given");
		requestID.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					requestID.validate();
					requestIdNotFound.setText("");
				}

			}
		});

	}

	@Override
	public void setLogicController(BaseController controller) {
		searchRequestController = (SearchRequestController) controller;
	}

}
