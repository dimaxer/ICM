package server;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import jdbc.mysqlConnection;

/**
 * This class is the graphical controller to Server UI.
 *
 */
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
	
	@FXML
	JFXToggleButton email_or_popup;


	/**
	 * this button is responsible for the server power when its off you can't
	 * connect to any db only when its you can connect to a db when power turns on
	 * theserver starts listening for clients
	 * 
	 * @param event Button was Pressed
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
	 * @param event Button was Pressed
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

	/**
	 * This method connects the server to custom db
	 * @param event Button was Pressed
	 */
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
	
	/**
	 * This method changes the value of isPopup.
	 * @param event Button was Pressed
	 */
	@FXML
	public void email_or_popupToggled(ActionEvent event) {
		if (DBServer.getInstance().isPopup())
			 DBServer.getInstance().setPopup(false);
		else DBServer.getInstance().setPopup(true);
	}
}
