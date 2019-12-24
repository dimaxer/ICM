package server;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jdbc.mysqlConnection;

public class ServerController extends Application {

	private static DBServer dbserver;

	@FXML
	JFXTextField hostname;

	@FXML
	JFXTextField dbname;

	// added username field
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

	public ServerController() {
		dbserver = new DBServer(DBServer.DEFAULT_PORT);
	}

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
			if (DBServer.startServer()) {
				serverStatus.setText("ON");

			} else
				serverStatus.setText("OFF");
		else if (DBServer.closeServer()) {
			serverStatus.setText("OFF");

			// if the server is off that the DB connection must shutdown aswell
			if (mysqlConnection.closeConnectionToDB()) {
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
			if (mysqlConnection.connectToBraudeDefault()) {
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
			if (mysqlConnection.connectToDB(hostname.getText(), dbname.getText(), username.getText(),
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
	 * Method that loads the stage and scene for the Server UI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane vbox;
		FXMLLoader loader = new FXMLLoader();

		try {
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ServerPanel.fxml"));
			vbox = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Scene s = new Scene(vbox);
		primaryStage.setScene(s);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	/**
	 * method that launches the Server UI
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerController gui = new ServerController();
		launch(args);
	}
}
