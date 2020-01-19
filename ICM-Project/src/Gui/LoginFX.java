package Gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.User;
import LogicController.LoginController;
import Utilities.MessageObject;
import client.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
/**
 * Login Fxml controller
 *
 */
public class LoginFX extends BaseFX {

	// Class Buttons ***************************************************
	@FXML
	private Button login;

	@FXML
	private JFXTextField id;

	@FXML
	private Text wrongPassword;
	@FXML
	private JFXPasswordField password;

	@FXML
	private Text date;
	// Class variables *************************************************
	private String IDText;
	private String passwordText;

	private LoginController loginController;

	// Class methods ***************************************************

	/**
	 * This method initializes parameters for: login button to be able to be pressed
	 * by pressing enter in the keyboard, also allows the visibility of the no input
	 * given text
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		login.setDefaultButton(true);
		loginController = new LoginController();

		Date valDate = new Date(); // Current System Date
		SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy"); // Date Format
		date.setText(formatDate.format(valDate)); // Set Date Text
	}

	/**
	 * Event handler for the search button receives the text that was written into
	 * the text field and sends the request id as a MessageObject to the server
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void loginWasPressed(ActionEvent event) {

		setValdiator();

		// Getting the strings from the gui
		IDText = id.getText();
		passwordText = password.getText();

		// checking if any fields are empty // and alerting if they are if
		if ((IDText.isEmpty() && passwordText.isEmpty())) {
			password.validate();
			id.validate();
		} else if (!IDText.isEmpty() && passwordText.isEmpty()) {
			password.validate();
		}
		if (IDText.isEmpty() && !passwordText.isEmpty()) {
			id.validate();

			// if everything is ok send a MessageObject to the server
		} else if (!IDText.isEmpty() && !passwordText.isEmpty())
			loginController.loginWasPressed(IDText, passwordText);
		{
		}
	}

	/**
	 * this method handles the information that returned from the server after login
	 * was pressed, by checking the boolean argument for true or false the method
	 * knows if the user and password was in the db or not
	 * 
	 * @param msg data
	 */

	public void loginHandle(MessageObject msg) {
		if (!(Boolean) msg.getArgs().get(0)) // check if the user exist or not [True|False]
			wrongPassword.setText("Username/Password is Wrong");
	}

	/**
	 * a method for clearing the id and password fields
	 */
	@FXML
	public void clearFields() {
		id.setText("");
		password.setText("");
	}

	/**
	 * a method that shows a Massage when no input is set in the UserName or
	 * Password
	 * 
	 */
	public void setValdiator() {
		RequiredFieldValidator validatorId = new RequiredFieldValidator();

		RequiredFieldValidator validatorPassword = new RequiredFieldValidator();

		password.getValidators().add(validatorPassword);
		validatorPassword.setMessage("No Password Given");
		id.getValidators().add(validatorId);
		validatorId.setMessage("No Input Given");

		id.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					id.validate();
				}

			}

		});
		Image icon1;
		try {
			String dir = System.getProperty("user.dir");
			icon1 = new Image(new FileInputStream(dir + "\\src\\Gui\\Images\\icons8_cancel_22px_2.png"));
			validatorId.setIcon(new ImageView(icon1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					password.validate();
				}

			}
		});
		Image icon2;
		try {
			String dir = System.getProperty("user.dir");
			icon2 = new Image(new FileInputStream(dir + "\\src\\Gui\\Images\\icons8_cancel_22px_2.png"));
			validatorPassword.setIcon(new ImageView(icon2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
