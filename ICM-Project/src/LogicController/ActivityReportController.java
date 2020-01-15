package LogicController;

import java.util.ArrayList;

import com.jfoenix.controls.JFXDatePicker;

import Utilities.MessageObject;
import Utilities.RequestType;

public class ActivityReportController extends BaseController {

	public void createNewReport(String calculationByText, JFXDatePicker startDate, JFXDatePicker endDate) {
		ArrayList<Object> arrlist = new ArrayList<>();
		arrlist.add(calculationByText);
		arrlist.add(startDate);
		arrlist.add(endDate);
		MessageObject activityReport = new MessageObject(RequestType.activityReport, arrlist);
		sendMessage(activityReport);
		
	}

	
	
}
