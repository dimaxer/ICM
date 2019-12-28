package server;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import jdbc.mysqlConnection;

public class ServerController {

	@FXML
	JFXTextField hostname;

	@FXML
	JFXTextField dbname;

	// added user name field
	@FXML
	JFXTextField username;

	@FXML
	JFXTextField dbpassword;

	@FXML
	JFXToggleButton serverPower;

	@FXML
	Text serverStatus;

	@FXML
	Text databaseStatus;

	@FXML
	JFXButton connectToBraude;

	@FXML
	JFXButton connectToCustom;


	/**
	 * this button is responsible for the server power when its off you can't
	 * connect to any db only when its you can connect to a db when power turns on
	 * theserver starts listening for clients
	 * 
	 * @param event
	 */
	@FXML
	public void serverPowerToggled(ActionEvent event) {
		System.out.println("serverPowerToggeled Event!");
		if (serverPower.isSelected())
			if (DBServer.getInstance().startServer()) {
				serverStatus.setText("ON");

			} else
				serverStatus.setText("OFF");
		else if (DBServer.getInstance().closeServer()) {
			serverStatus.setText("OFF");

			// if the server is off that the DB connection must shutdown aswell
			if (mysqlConnection.getInstance().closeConnectionToDB()) {
				databaseStatus.setText("DISCONNECTED");
				databaseStatus.setFill(Color.RED);
			}
		} else
			serverStatus.setText("ON");
	}

	/**
	 * when this button is pressed server connects to the braude default db
	 * 
	 * @param event
	 */
	@FXML
	public void connectToBraudePressed(ActionEvent event) {
		System.out.println("connectToBraudePressed Event!");
		if (serverPower.isSelected()) // only if the server is on it can connect to a db
		{
			if (mysqlConnection.getInstance().connectToBraudeDefault()) {
				databaseStatus.setText("CONNECTED");
				databaseStatus.setFill(Color.GREEN);
			} else {
				databaseStatus.setText("FAILED");
				databaseStatus.setFill(Color.RED);
			}
		}
	}

	@FXML
	public void connectToCustomPressed(ActionEvent event) {
		System.out.println("connectToCustomPressed Event!");
		if (serverPower.isSelected()) // only if the server is on it can connect to a db
		{
			if (mysqlConnection.getInstance().connectToDB(hostname.getText(), dbname.getText(), username.getText(),
					dbpassword.getText())) {
				databaseStatus.setText("CONNECTED");
				databaseStatus.setFill(Color.GREEN);

			} else {
				databaseStatus.setText("FAILED");
				databaseStatus.setFill(Color.RED);
			}
		}

	}
}
