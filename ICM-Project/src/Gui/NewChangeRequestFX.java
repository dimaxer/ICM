package Gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import Common.AttachedFile;
import LogicController.NewChangeRequestController;
import Utilities.MessageObject;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class NewChangeRequestFX implements BaseFx {

	@FXML
	private JFXTextField jfxinfSys;
	
	@FXML
	private JFXTextField jfxrequestedChange;
	
	@FXML
	private JFXTextField jfxsituation;
	
	@FXML
	private JFXTextField jfxreasons;
	
	@FXML
	private JFXTextField jfxnotes;
	
	@FXML
	private ImageView error;
	
	@FXML
	private Button attach;
	
	@FXML
	private Text fileName;
	
	@FXML
	private Button submit;

	private String date, infSys, requestedChange, situation, changeReasons, notes;
	
	private AttachedFile attachedFile;

	private NewChangeRequestController newChangeRequestController;
	
	private ArrayList<Object> args;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		attach.setDefaultButton(true);
		submit.setDefaultButton(true);
		newChangeRequestController = new NewChangeRequestController();
		args = new ArrayList<Object>();
	}

	/**
	 * This method handles the form when submit was pressed
	 */
	@FXML
	public void submitWasPressed(ActionEvent event) {
		setValdiator();
		initStringFieldValues();
		
		if (infSys.isEmpty() || situation.isEmpty() || requestedChange.isEmpty())
			validateFields(); // method extracted
		else {
			addFieldsToArgs();
			args.set(6, attachedFile);
			newChangeRequestController.submitWasPressed(args);
		}
	}

	private void initStringFieldValues() {
		date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now());
		infSys = jfxinfSys.getText();
		situation = jfxsituation.getText();
		requestedChange = jfxrequestedChange.getText();
		changeReasons = jfxreasons.getText();
		notes = jfxnotes.getText();
	}

	/**
	 * This method adds the field values to args ArrayList
	 * @param args
	 * @return ArrayList that includes the field arguments
	 */
	private void addFieldsToArgs() {
		args.add(date);
		args.add(infSys);
		args.add(requestedChange);
		args.add(situation);
		args.add(changeReasons);
		args.add(notes);
		args.add(null); // Attached File
		args.add("New");
		args.add("Active");
		args.add(Client.getInstance().getCurrentUser().getId());
	}

	/**
	 * This method validates the fields
	 */
	private void validateFields() {
		jfxinfSys.validate();
		jfxsituation.validate();
		jfxrequestedChange.validate();
		jfxreasons.validate();
		jfxnotes.validate();
	}
	
	/**
	 * This method sets field validators
	 */
	public void setValdiator() {
		RequiredFieldValidator validator = new RequiredFieldValidator();

		jfxinfSys.getValidators().add(validator);
		jfxsituation.getValidators().add(validator);
		jfxrequestedChange.getValidators().add(validator);
		jfxreasons.getValidators().add(validator);
		jfxnotes.getValidators().add(validator);

		validator.setMessage("*required");
	}
	
	@FXML
	public void backWasPressed(ActionEvent event) {
		newChangeRequestController.switchScene("Panel");
		
	}

	public void newCRHandler(MessageObject msg) {
		MessageObject message = (MessageObject) msg;
		if ((Boolean) message.getArgs().get(0))// [True|False]
			newChangeRequestController.switchScene("Panel");
	}

	@FXML
	public void attachWasPressed(ActionEvent event) {
		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			attachedFile = new AttachedFile(jfc.getSelectedFile().getAbsolutePath());
			fileName.setText(jfc.getSelectedFile().getName());
		}
	}

	/**
	 * A method to clear all the fields in this form
	 * @author Raz Malka
	 */
	public void clearFields() {
		// TODO Auto-generated method stub
		jfxinfSys.setText("");
		jfxsituation.setText("");
		jfxrequestedChange.setText("");
		jfxreasons.setText("");
		jfxnotes.setText("");
		fileName.setText("");
		attachedFile = null;
	}
}
