package Utilities;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Alert {
	
	public static void showAlert(AlertType alertType) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK);
	}
	
	public static void showAlert(AlertType alertType, String header, String content) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
		alert.setHeaderText(header);
		alert.setContentText(content);
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK);
	}
}
