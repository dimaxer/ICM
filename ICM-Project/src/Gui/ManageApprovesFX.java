package Gui;



import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import LogicController.ManageApprovesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ManageApprovesFX extends BaseFX{

@FXML
JFXButton back;



private ManageApprovesController manageApprovesController;

@Override
public void initialize(URL location, ResourceBundle resources) {
	manageApprovesController=new ManageApprovesController();
	
}
@FXML
public void backWasPressed(ActionEvent event)
{
	manageApprovesController.switchScene("Panel");
	
}

	
}
