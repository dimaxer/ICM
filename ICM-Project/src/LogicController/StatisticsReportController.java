package LogicController;

import java.time.LocalDate;
import java.util.ArrayList;

import Utilities.IStatistics;
import Utilities.MessageObject;
import Utilities.RequestType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class StatisticsReportController extends BaseController {
	// Statistics Utility *****************************************************
	/** Statistics Utility to be used for statistical calculations and management.*/
	private IStatistics statisticsUtility;
	
	/**
	 * Constructor of {@link StatisticsReportController},
	 * Enables an injection of Statistics Utility through IStatistics.
	 * @param statisticsUtility
	 */
	public StatisticsReportController(IStatistics statisticsUtility) {
		this.statisticsUtility = statisticsUtility;
	}

	/**
	 * This method gets the current statistics utility
	 * @return current statistics utility
	 */
	public IStatistics getStatisticsUtility() {
		return statisticsUtility;
	}

	public void initGetInfoSystems() {
		MessageObject msg = new MessageObject(RequestType.InformationSystem_Details, new ArrayList<>());
		msg.getArgs().add(true);
		sendMessage(msg);
	}
	
	// Request Report by Status ************************************************
	/**
	 * This method checks whether the given DatePicker values are valid
	 * @param from DatePicker with value From
	 * @param to DatePicker with value To
	 * @return Are those DatePicker Values Valid
	 */
	public boolean isValidDates(DatePicker from, DatePicker to) {
		if (from.getValue() == null) return false;
		if (to.getValue() == null) return false;
		if (!from.getValue().isBefore(to.getValue())) return false;
		return true;
	}

	/**
	 * Initializes sending of message to get report status data
	 * @param status
	 * @param to 
	 * @param from 
	 */
	public void initGetReportStatusData(String status, LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportStatusData, new ArrayList<>());
		msg.getArgs().add(status);
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}

	/**
	 * This method Initializes the Status Combo Box
	 */
	public void initStatusComboBox(ComboBox<String> statusComboBox) {
		statusComboBox.getItems().addAll("Active", "Frozen", "Closed");
	}

	// Request Report by Rejection ************************************
	public void initGetReportStatusData(LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportRejectedData, new ArrayList<>());
		msg.getArgs().add("Rejection");
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}

	// Request Report by Active Days **********************************
	public void initGetReportActiveDaysData(LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportActiveDays, new ArrayList<>());
		msg.getArgs().add("Active");
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}
	
	// Request Report by Extensions ************************************
	public void initGetReportExtensionsData() {
		MessageObject msg = new MessageObject(RequestType.GetReportExtensions, new ArrayList<>());
		sendMessage(msg);
	}

	// Request Report by Durations ************************************
	public void initGetReportDurationsData() {
		MessageObject msg = new MessageObject(RequestType.GetReportDurations, new ArrayList<>());
		sendMessage(msg);
	}
	
	// Request Report by Durations ************************************
	public void initGetReportDelaysData(String infoSys) {
		MessageObject msg = new MessageObject(RequestType.GetReportDelays, new ArrayList<>());
		msg.getArgs().add(infoSys);
		sendMessage(msg);
	}
}
