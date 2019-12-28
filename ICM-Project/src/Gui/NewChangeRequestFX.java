package Gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import LogicController.NewChangeRequestController;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import client.Client;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;

public class NewChangeRequestFX implements BaseFx {

	@FXML
	private JFXTextField jfxdate;
	@FXML
	private JFXTextField jfxname;
	@FXML
	private JFXTextField jfxemail;
	@FXML
	private JFXTextField jfxposition;
	@FXML
	private JFXTextField jfxinfSys;
	@FXML
	private JFXTextField jfxsituation;
	@FXML
	private JFXTextField jfxwantedChange;
	@FXML
	private JFXTextField jfxnotes;
	@FXML
	private ImageView error;
	@FXML
	private Button attach;
	@FXML
	private Button submit;

	private String date, name, email, position, infSys, situation, wantedChange, notes;

	private String filePpath = "";

	private NewChangeRequestController newChangeRequestController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		attach.setDefaultButton(true);
		submit.setDefaultButton(true);
		newChangeRequestController = new NewChangeRequestController();
	}

	@FXML
	public void submitWasPressed(ActionEvent event) {
		setValdiator();
		date = jfxdate.getText();
		name = jfxname.getText();
		email = jfxemail.getText();
		position = jfxposition.getText();
		infSys = jfxinfSys.getText();
		situation = jfxsituation.getText();
		wantedChange = jfxwantedChange.getText();
		notes = jfxnotes.getText();

		if (date.isEmpty() || name.isEmpty() || email.isEmpty() || position.isEmpty() || infSys.isEmpty()
				|| situation.isEmpty() || wantedChange.isEmpty()) {
			jfxdate.validate();
			jfxname.validate();
			jfxemail.validate();
			jfxposition.validate();
			jfxinfSys.validate();
			jfxsituation.validate();
			jfxwantedChange.validate();
		} else {
			ArrayList<Object> args = new ArrayList<>();
			args.add(date);
			args.add(infSys);
			args.add(name);
			args.add(position);
			args.add(email);
			args.add(situation);
			args.add(wantedChange);
			args.add(notes);

			newChangeRequestController.submitWasPressed(args);

		}

	}
	@FXML
	public void backWasPressed(ActionEvent event) {
		newChangeRequestController.switchScene("AcademicUserPanel");
		
	}

	public void newCRHandler(MessageObject msg) {
		MessageObject message = (MessageObject) msg;
		if ((Boolean) message.getArgs().get(0))// [True|False]
			newChangeRequestController.switchScene("AcademicUserPanel");
	}

	@FXML
	public void attachWasPressed(ActionEvent event) {
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		int returnVal = jfc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			System.out.println("You chose to open this file: " + jfc.getSelectedFile().getName());
	}

	public void setValdiator() {
		RequiredFieldValidator validator = new RequiredFieldValidator();

		jfxdate.getValidators().add(validator);
		jfxname.getValidators().add(validator);
		jfxposition.getValidators().add(validator);
		jfxsituation.getValidators().add(validator);
		jfxwantedChange.getValidators().add(validator);
		jfxinfSys.getValidators().add(validator);
		jfxemail.getValidators().add(validator);

		validator.setMessage("*required");
	}

}
