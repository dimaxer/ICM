package Gui;

import java.awt.TextComponent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;

import Common.User;
import LogicController.BaseController;
import Utilities.MessageObject;
import Utilities.ScreenManager;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This abstract class serves as a base FX controller for all the other FX
 * controllers.
 * 
 * @author Malka
 * @author Noam
 */




public abstract class BaseFX implements Initializable {

	private BaseController baseController = new BaseController();
	
	@Override
	public abstract void initialize(URL arg0, ResourceBundle arg1);

	public void handleViewRequestDetailsRequest(MessageObject message) {
		baseController.handleViewRequestDetailsRequest(message);
	}

	public void loginHandle(MessageObject message, Object currentFX) {
		baseController.loginHandle(message);
		((LoginFX) currentFX).loginHandle(message);
	}

	/**
	 * This method checks if the request id was found in the db or not by checking
	 * the boolean and switches screens to the request form with the message object
	 * so that it could be initialized with the request's information
	 * 
	 * @param message in args(0) : boolean request exist, args(1) : String user role
	 *                in request, args(2) Request the request
	 */
	public void handleSearchRequest(MessageObject message, Object currentFX) {

		baseController.handleSearchRequest(message, currentFX);
	}

	/**
	 * this method show an error message dialog because the some one else is already
	 * logged in with this userID
	 * 
	 * @param message
	 * @param currentFX
	 */
	public void loginErrorAllreadyLoggedIn(MessageObject message, Object currentFX) {
		JOptionPane.showMessageDialog(null, "Error this user is already logged in");

	}
}
