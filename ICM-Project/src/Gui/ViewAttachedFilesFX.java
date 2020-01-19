package Gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import LogicController.ViewAttachedFilesController;
import Utilities.MessageObject;
import Utilities.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/** This class represents graphical controller for view attached files page */
public class ViewAttachedFilesFX extends BaseFX {

	// FXML Properties *************************************************

	@FXML
	private JFXButton download;

	@FXML
	private JFXButton back;

	@FXML
	private JFXListView<String> listView;

	private String requestID;

	private ViewAttachedFilesController viewAttachedFilesController;

	// Methods **********************************************************

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);// this command allows to select multiple
																				// rows in the list view
		viewAttachedFilesController = new ViewAttachedFilesController();
	}

	/**
	 * this method shows in the list view the names of the attached files of this
	 * request
	 * 
	 * @param requestID id
	 */
	public void showAttachedFiles(String requestID) {
		if (listView.getItems() != null)
			listView.getItems().clear();

		this.requestID = requestID;

		// this method asks the server for the file names

		viewAttachedFilesController.showAttachedFiles(requestID);

	}

	/**
	 * this method receives from the server an array of string which contains the
	 * names of the attached files of this request
	 * 
	 * @param message data
	 */
	public void handleShowAttachedFiles(MessageObject message) {
		String[] stringFileArray = (String[]) message.getArgs().get(0);

		if (stringFileArray != null)
			listView.getItems().addAll(stringFileArray);
	}

	/**
	 * this method asks the server to send the files that were selected in the list
	 * view do the designated location on the clients computer
	 * 
	 * @param event button was pressed
	 */
	@FXML
	public void downloadWasPressed(ActionEvent event){
		try {
			String[] fileNames = null;
	
			if (listView.getItems() != null) {
				List<String> nameList = listView.getSelectionModel().getSelectedItems();
				fileNames = nameList.toArray(new String[nameList.size()]);
	
				if (nameList.size() != 0) {// only if any file were selected or exist at all can you download
					DirectoryChooser directoryChooser = new DirectoryChooser();
					File directory = directoryChooser.showDialog(null);
	
					if (directory != null) {// only if you chose the directory can you download
						String savePath = directory.getAbsolutePath();
						viewAttachedFilesController.setSavePath(savePath);
						viewAttachedFilesController.askTheServerToDownloadFiles(fileNames, requestID);
					}
				} else
					JOptionPane.showMessageDialog(null, "Error no files were chosen");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * server sent the files this method downloads them to the client computer
	 * 
	 * @param message data
	 */
	public void handleDownloadAttachedFiles(MessageObject message) {
		viewAttachedFilesController.handleDownloadAttachedFiles(message);
		JOptionPane.showMessageDialog(null, "Files were downloaded successfully");
	}

	/**
	 * This event handler switches scene to home
	 * @param event button was pressed
	 */
	@FXML
	public void backWasPressed(ActionEvent event) {
		viewAttachedFilesController.switchScene("RequestDetails");
	}

}
