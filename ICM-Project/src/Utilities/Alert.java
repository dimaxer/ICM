package Utilities;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * This utility class pushes an alert to the screen.
 */
public class Alert {
	
	/**
	 * This method shows the requested alert.
	 * @param alertType type
	 */
	public static void showAlert(AlertType alertType) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK);
	}
	
	/**
	 * This method shows the requested alert.
	 * @param alertType type
	 * @param header title of alert
	 * @param content content of alert
	 */
	public static void showAlert(AlertType alertType, String header, String content) {
		Platform.runLater(() -> {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
		alert.setHeaderText(header);
		alert.setContentText(content);
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK);
		});
	}
}
