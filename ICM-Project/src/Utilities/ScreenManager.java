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
	 * @param currentScene scene
	 * @param primaryStage stage
	 * @param FXMLName name
	 * @throws Exception exception
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
	 * @param scene The FXMLLoader linked to the fxml screen
	 */
	public void addScreen(String name, Scene scene) {
		if (singeltonNotInitialized())
			return;
		screenMap.put(name, scene);
	}

	/** has singleton not been initialized
	 * 
	 * @return was initialized
	 */
	private boolean singeltonNotInitialized() {
		if (singletonInstance == null) {
			System.out.println("DEVELOPER WARNING: YOU HAVE NOT INITIALIZE() THE SCREEN MANAGER!");
			return true;

		}
		return false;
	}

	/** Get current FX
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
	 * @param scene name of fxml to remove
	 */
	public void removeScreen(String scene) {
		if (singeltonNotInitialized())
			return;
		screenMap.remove(scene);
	}

	/**
	 * Switches to the required screen by changing the value of the root parent.
	 * 
	 * @param scene FXML Screen Name as specified in addScreen(...)
	 * @throws Exception exception
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
	 * @throws Exception exception
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
	 * @param fxml_name name
	 * @return Boolean, indicating if the scene exists.
	 */
	public Boolean sceneExists(String fxml_name) {
		return screenMap.containsKey(fxml_name);
	}

	/**
	 * Empties the Screen Map HashMap, should be called only upon log-out.
	 * 
	 */
	public void clearScreenMap() {
		screenMap.clear();
	}

	/**
	 * This function centers the current stage and normalizes the position,
	 * resizability and borders among all scenes.
	 * 
	 */
	public void normalizeAppearance() {
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
	}

	/**
	 * Gets the current scene
	 * @return current scene
	 */
	public Scene getCurrentScene() {
		if (singeltonNotInitialized())
			return null;

		return currentScene;
	}

	/**
	 * This gets the screen map
	 * @return screen map
	 */
	public HashMap<String, Scene> getScreenMap() {
		if (singeltonNotInitialized())
			return null;

		return screenMap;
	}

	/**
	 * This gets the primary stage
	 * @return primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
