package Gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import LogicController.BaseController;
import LogicController.ConnectServerManualyController;
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

	/**
	 * if user press connect so send new ip to ClientUI class to set the new client
	 * with new IP
	 * 
	 */
	@FXML
	public void connectWasPressed(ActionEvent event) {
		if (!connectManualyController.setClient(ip.getText())) {
			errorMessage.setText("Could not connect, please insert the IP again.");
			ip.setText("");
		} else {
			connectManualyController.switchScene("LoginPage");
		}

	}

	@FXML
	public void connectToDefaultWasPressed(ActionEvent event) throws Exception {
		if (!connectManualyController.setClient(ClientUI.DEFAULT_SERVER))

		{
			try {
				errorMessage.setText("Could not connect to the Default Server: " + ClientUI.DEFAULT_SERVER + ". " + '\n'
						+ "Please insert the Server IP manually");
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			ip.setText("");
		} else {
			connectManualyController.switchScene("LoginPage");
		}

	}

	@Override
	public void setLogicController(BaseController controller) {
		connectManualyController = (ConnectServerManualyController) controller;

	}

}
