package Utilities;

import java.util.HashMap;

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
	private static ScreenManager singletonInstance = null;
	private HashMap<String, Scene> screenMap = new HashMap<>();
	private Scene currentScene;
	private Stage primaryStage;

	private ScreenManager() {
	}

	/**
	 * Get the Singleton's Instance
	 * 
	 * @return ScreenManager Singleton Instance
	 */
	public static ScreenManager getInstance() {
		return singletonInstance;
	}

	/**
	 * Initialization of Screen Manager Singleton May be called only once.
	 * 
	 * @param currentScene
	 * @param primaryStage
	 * @param FXMLName
	 * @param Client.getInstance()
	 * @param clientUI
	 * @throws Exception
	 */
	public static void initialize(Scene currentScene, Stage primaryStage, String FXMLName) throws Exception {
		if (singletonInstance != null)
			return;

		singletonInstance = new ScreenManager();
		singletonInstance.currentScene = currentScene;
		singletonInstance.primaryStage = primaryStage;
		singletonInstance.addScreen(FXMLName, currentScene);
		singletonInstance.normalizeAppearance();
	}

	/**
	 * Adds a screen to the screen manager
	 * 
	 * @param name   name of fxml to add
	 * @param loader The FXMLLoader linked to the fxml screen
	 */
	public void addScreen(String name, Scene scene) {
		if (singeltonNotInitialized())
			return;
		screenMap.put(name, scene);
	}

	private boolean singeltonNotInitialized() {
		if (singletonInstance == null) {
			System.out.println("DEVELOPER WARNING: YOU HAVE NOT INITIALIZE() THE SCREEN MANAGER!");
			return true;

		}
		return false;
	}

	/**
	 * @return current Controller instance
	 */
	public Object getCurrentFX() {
		if (singeltonNotInitialized())
			return null;

		FXMLLoader userData = (FXMLLoader) currentScene.getUserData();
		Object currentFX = userData.getController();
		return currentFX;
	}

	/**
	 * Removes a screen from the screen manager
	 * 
	 * @param name name of fxml to remove
	 */
	public void removeScreen(String scene) {
		if (singeltonNotInitialized())
			return;
		screenMap.remove(scene);
	}

	/**
	 * Switches to the required screen by changing the value of the root parent.
	 * 
	 * @param name FXML Screen Name as specified in addScreen(...)
	 * @throws Exception
	 */
	public void activate(String scene) throws Exception {
		if (singeltonNotInitialized())
			return;

		if (!screenMap.containsKey(scene)) {
			Alert.showAlert(AlertType.ERROR, "Non-Existent Screen", "Screen does not exist in ScreenManager.");
		}

		currentScene = screenMap.get(scene);
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
		if (singeltonNotInitialized())
			return;

		if (screenMap.containsKey(fxml_name)) {
			try {
				currentScene = screenMap.get(fxml_name);
				Platform.runLater(() -> {
					primaryStage.setScene(currentScene);
					normalizeAppearance();
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

			Platform.runLater(() -> {
				primaryStage.setScene(currentScene);
				normalizeAppearance();
			});
		}

	}

	/**
	 * Returns whether a specific scene exists.
	 * 
	 * @author Raz Malka
	 * @param fxml_name
	 * @return Boolean, indicating if the scene exists.
	 */
	public Boolean sceneExists(String fxml_name) {
		return screenMap.containsKey(fxml_name);
	}

	/**
	 * Empties the Screen Map HashMap, should be called only upon log-out.
	 * 
	 * @author Raz Malka
	 */
	public void clearScreenMap() {
		screenMap.clear();
	}

	/**
	 * This function centers the current stage and normalizes the position,
	 * resizability and borders among all scenes.
	 * 
	 * @author Raz Malka
	 */
	public void normalizeAppearance() {
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
	}

	public Scene getCurrentScene() {
		if (singeltonNotInitialized())
			return null;

		return currentScene;
	}

	public HashMap<String, Scene> getScreenMap() {
		if (singeltonNotInitialized())
			return null;

		return screenMap;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
