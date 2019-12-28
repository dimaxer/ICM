package Gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import LogicController.ConnectServerManualyController;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ConnectServerManualyFX implements BaseFx {

	private ConnectServerManualyController connectManualyController;
	@FXML
	private JFXButton connect;

	@FXML
	private JFXButton connectDefault;

	@FXML
	private JFXTextField ip;

	@FXML
	private Text errorMessage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connectManualyController = new ConnectServerManualyController();
	}
	
	/**
	 * if user press connect so send new ip to ClientUI class to set the new client
	 * with new IP
	 * @throws IOException 
	 * 
	 */
	@FXML
	public void connectWasPressed(ActionEvent event) throws IOException {
		Client.initialize(ip.getText(), ClientUI.DEFAULT_PORT);
		if (!Client.getInstance().isConnected()) {
			errorMessage.setText("Could not connect, please insert the IP again.");
			ip.setText("");
		} else {
			connectManualyController.switchScene("LoginPage");
		}
	}

	@FXML
	public void connectToDefaultWasPressed(ActionEvent event) {
		
			try {
				if (Client.getInstance().isConnected())
					connectManualyController.switchScene("LoginPage");
			}
			catch (Exception ex) {
				errorMessage.setText("Could not connect to the Default Server: " + ClientUI.DEFAULT_SERVER + ". " + '\n'
						+ "Please insert the Server IP manually");
				ip.setText("");
			}
	}
}
