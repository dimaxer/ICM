package client;

import java.io.*;
import Utilities.ScreenManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class constructs the UI for a JavaFX client.
 * 
 * @author Team16 Braude
 * @version Nov2019
 */
public class ClientUI extends Application {
//Class variables *************************************************

	/**
	 * The First scene to load can be changed if the creation of new client failed
	 */
	public String FirstScene;

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	/**
	 * The default server to connect to.
	 */
	final public static String DEFAULT_SERVER = "localhost";

	private static ClientUI singletonInstance = null;

//Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientUI singleton. Must stay public as
	 * required by those whom extend Application
	 */
	public ClientUI() {
		if (singletonInstance == null)
			singletonInstance = this;
	}

// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * 
	 * @return ClientUI Singleton Instance
	 */
	public static ClientUI getInstance() {
		if (singletonInstance == null)
			singletonInstance = new ClientUI();
		return singletonInstance;
	}

//Class methods ***************************************************

	/**
	 * this method loads and runs the stage and first scene, after it loads you can
	 * switch scenes by using the switchScene method
	 * @param primaryStage stage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			Client.initialize(DEFAULT_SERVER, DEFAULT_PORT);

			FirstScene = "/Gui/Login.fxml";
		} catch (IOException exception) {
			FirstScene = "/Gui/ConnectServerManualy.fxml";
			System.out.println("Error: Can't setup connection!" + " Terminating client.");

		}

		Parent root;
		FXMLLoader loader = new FXMLLoader();

		try {
			loader = new FXMLLoader();

			loader.setLocation(getClass().getResource(FirstScene));

			root = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Scene scene = new Scene(root);
		scene.setUserData(loader);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.show();

		ScreenManager.initialize(scene, primaryStage, FirstScene.substring(0, FirstScene.length() - 5));
	}

	/**
	 * A method to set client manually in case you connect to another server
	 * @param ip ip
	 * @return was set successfully
	 */

	public Boolean setClient(String ip) {
		try {
			Client.initialize(ip, DEFAULT_PORT);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/** This method is responsible for the creation of the Client UI.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {

		launch(args);
	}
}
