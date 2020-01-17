package LogicController;

import Gui.ActivityReportFX;
import Gui.ViewAllRequestsFX;
import javafx.event.ActionEvent;

public class StatisticsReportController extends BaseController {

	public void ActivityReportWasPressed(ActionEvent event) {
		switchScene("ActivityReport");
		((ActivityReportFX) getCurrentFX()).clearFields();
	}

	public void statisticsReportController(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	

}
