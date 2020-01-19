package LogicController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import Common.Interval;
import Utilities.IStatistics;
import Utilities.MessageObject;
import Utilities.RequestType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

/** This is the logical controller of statistics report */
public class StatisticsReportController extends BaseController {
	// Statistics Utility *****************************************************
	/** Statistics Utility to be used for statistical calculations and management.*/
	private IStatistics statisticsUtility;
	
	/**
	 * Constructor of {@link StatisticsReportController},
	 * Enables an injection of Statistics Utility through IStatistics.
	 * @param statisticsUtility statistics utility to be injected
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
	 * @param status status
	 * @param to  date
	 * @param from date
	 */
	public void initGetReportStatusData(String status, LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportStatusData, new ArrayList<>());
		msg.getArgs().add(status);
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}

	/** This method Initializes the Status Combo Box
	 * 
	 * @param statusComboBox combo box of statuses
	 */
	public void initStatusComboBox(ComboBox<String> statusComboBox) {
		statusComboBox.getItems().addAll("Active", "Frozen", "Closed");
	}

	
	/**
	 * get data for report status for reject status between the dates
	 * @param from date from
	 * @param to date to
	 */
	public void initGetReportStatusData(LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportRejectedData, new ArrayList<>());
		msg.getArgs().add("Rejection");
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}

	// Request Report by Active Days **********************************
	/**
	 * /**
	 * get data for report status for Active status between the dates
	 * @param from date from
	 * @param to date to
	 */
	public void initGetReportActiveDaysData(LocalDate from, LocalDate to) {
		MessageObject msg = new MessageObject(RequestType.GetReportActiveDays, new ArrayList<>());
		msg.getArgs().add("Active");
		msg.getArgs().add(from.minusDays(1));
		msg.getArgs().add(to);
		sendMessage(msg);
	}
	
	// Request Report by Extensions ************************************
	/**
	 * get data for the  Report by Extensions
	 */
	public void initGetReportExtensionsData() {
		MessageObject msg = new MessageObject(RequestType.GetReportExtensions, new ArrayList<>());
		sendMessage(msg);
	}

	// Request Report by Durations ************************************
	/**
	 * get data for the  Report by Durations
	 */
	public void initGetReportDurationsData() {
		MessageObject msg = new MessageObject(RequestType.GetReportDurations, new ArrayList<>());
		sendMessage(msg);
	}
	
	// Request Report by Durations ************************************
	/** get data for the  Report by delays
	 * 
	 * @param infoSys information system
	 */
	public void initGetReportDelaysData(String infoSys) {
		MessageObject msg = new MessageObject(RequestType.GetReportDelays, new ArrayList<>());
		msg.getArgs().add(infoSys);
		sendMessage(msg);
	}

	// Saving and Getting Reports *************************************
	/**
	 * save the report 
	 * @param name name of the report
	 * @param intervals the interval
	 */
	public void saveReport(String name, ArrayList<Interval> intervals) {
		MessageObject msg = new MessageObject(RequestType.SaveReport, new ArrayList<>());
		msg.getArgs().add(name);
		msg.getArgs().add(Date.valueOf(LocalDate.now()));
		msg.getArgs().add(intervals);
		sendMessage(msg);
	}
	/**
	 * get the reports from the db
	 */
	public void getReports() {
		MessageObject msg = new MessageObject(RequestType.GetReports, new ArrayList<>());
		sendMessage(msg);
	}
}
