package Utilities;

import java.util.HashMap;

import Gui.ClientUI;
import Gui.ConnectServerManualyFX;
import Gui.LoginFX;
import Gui.PanelFX;
import Gui.RequestFormFX;
import Gui.SearchRequestFX;
import LogicController.BasePanelController;
import LogicController.ConnectServerManualyController;
import LogicController.LoginController;
import LogicController.RequestFormController;
import LogicController.SearchRequestController;
import client.Client;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class is made to manage a collection of screens and the switching
 * between them.
 * 
 * @author RazMalka
 * @since 04-12-19
 */
public class ScreenManager {
	private HashMap<String, Scene> screenMap = new HashMap<>();
	private Scene currentScene;
	private Stage primaryStage;
	private Client client;
	private ClientUI clientUI;

	public ScreenManager(Scene currentScene, Stage primaryStage, String FXMLName, Client client, ClientUI clientUI) {

		this.currentScene = currentScene;
		this.primaryStage = primaryStage;
		addScreen(FXMLName, currentScene);
		this.client = client;
		this.clientUI = clientUI;
	}

	/**
	 * Adds a screen to the screen manager
	 * 
	 * @param name   name of fxml to add
	 * @param loader The FXMLLoader linked to the fxml screen
	 */
	public void addScreen(String name, Scene scene) {
		screenMap.put(name, scene);
	}

	/**
	 * @return current Controller instance
	 */
	public Object getCurrentFX() {

		return ((FXMLLoader) currentScene.getUserData()).getController();

	}

	/**
	 * Removes a screen from the screen manager
	 * 
	 * @param name name of fxml to remove
	 */
	public void removeScreen(String scene) {
		screenMap.remove(scene);
	}

	/**
	 * Switches to the required screen by changing the value of the root parent.
	 * 
	 * @param name FXML Screen Name as specified in addScreen(...)
	 * @throws Exception
	 */
	public void activate(String scene) throws Exception {
		if (!screenMap.containsKey(scene)) {

			Alert.showAlert(AlertType.ERROR, "Non-Existent Screen", "Screen does not exist in ScreenManager.");

		}

		currentScene = screenMap.get(scene);
		System.out.println("123123");
		System.out.println(scene);
		primaryStage.setScene(currentScene);

	}

	/**
	 * This static function switches between screens using the ScreenManager and
	 * fxml templates.
	 * 
	 * @param fxml_name name of the fxml page
	 * @param ui_class  class of the source via this.getClass()
	 */
	public void switchScene(String fxml_name) throws Exception {

		if (screenMap.containsKey(fxml_name)) {

			currentScene = screenMap.get(fxml_name);

			Platform.runLater(() -> {
				primaryStage.setScene(currentScene);
			});

		} else {

			Parent parent;
			FXMLLoader loader = new FXMLLoader();
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/Gui/" + fxml_name + ".fxml"));
			parent = loader.load();
			Scene scene = new Scene(parent);
			scene.setUserData(loader);
			currentScene = scene;
			addScreen(fxml_name, currentScene);
			injectLogicController(fxml_name);

			Platform.runLater(() -> {
				primaryStage.setScene(currentScene);
			});

		}

	}

	public Scene getCurrentScene() {
		return currentScene;
	}

	public HashMap<String, Scene> getScreenMap() {
		return screenMap;
	}

	public void injectLogicController(String fxml_name) {
		Object FX = getCurrentFX();

		switch (fxml_name) {
		case "AcademicUserPanel":
			((PanelFX) FX).setLogicController(new BasePanelController(client));
			break;
		case "ConnectServerManualy":
			((ConnectServerManualyFX) FX).setLogicController(new ConnectServerManualyController(client, clientUI));
			break;
		case "LoginPage":
			((LoginFX) FX).setLogicController(new LoginController(client));

			break;

		case "RequestForm":
			((RequestFormFX) FX).setLogicController(new RequestFormController(client));
			break;
		case "SearchRequest":
			((SearchRequestFX) FX).setLogicController(new SearchRequestController(client));
			break;
		default:

		}

	}
}
