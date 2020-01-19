package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class constructs the UI for a JavaFX client.
 * 
 * @author Team16 Braude

 * @version Dec2019
 */
public class ServerUI extends Application {
	private static ServerUI singletonInstance = null;
	
// Constructors ****************************************************

	/**
	 * Constructs an instance of the ServerUI singleton.
	 * Must stay public as required by those whom extend Application
	 */
	public ServerUI() {
		if (singletonInstance == null)
			singletonInstance = this;
	}

// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * @return ServerUI Singleton Instance
	 */
	public static ServerUI getInstance() {
		if (singletonInstance == null)
			singletonInstance = new ServerUI();
		return singletonInstance;
	}
	
// Class Methods ***************************************************
	
	/** Method that loads the stage and scene for the Server UI
	 * @param primaryStage stage
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
		primaryStage.centerOnScreen();
	}
	
	/**
	 * method that launches the Server UI
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
