package Utilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Common.Interval;

/** This injectable interface represents a Statistics Utility to be used for statistical calculations and management. */
public interface IStatistics {
	
	/**
	 * This method finds the median out of a list of Integers.
	 * @param list ArrayList of Integers
	 * @return Value of Median
	 * @throws NullPointerException list is null
	 * @throws IllegalArgumentException list is empty
	 */
	public float GetMedian(ArrayList<Integer> list);
	
	/**
	 * This method finds the standard deviation out of a list of Integers.
	 * @param list ArrayList of Integers
	 * @return Value of Standard Deviation
	 * @throws NullPointerException list is null
	 * @throws IllegalArgumentException list is empty
	 */
	public float GetStandardDeviation(ArrayList<Integer> list);
	
	/**
	 * This method filters the active/frozen/closed requests during the selected sub-period,
	 * keeps only the requests for which the status' end date is set later than,
	 * or on the same day, of picked end date.
	 * @param pickedStartDate Start Date of Report Issued
	 * @param pickedEndDate End Date of Report Issued
	 * @param startDates Request ID, Status Start Date Start Dates of active/frozen/closed request statuses
	 * @param endDates Request ID, Status End Date End Dates of active/frozen/closed request statuses
	 * @return Amount of Requests after Filtering
	 */
	public Integer filterStatusByDateRange(LocalDate pickedStartDate, LocalDate pickedEndDate, HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates);
	
	/**
	 * This method filters the active requests during the selected sub-period,
	 * keeps the requests and retrieves the work days (active days) out of them.
	 * @param pickedStartDate Start Date of Report Issued
	 * @param pickedEndDate End Date of Report Issued
	 * @param startDates Request ID, Status Start Date Start Dates of active requests
	 * @param endDates Request ID, Status End Date End Dates of active requests
	 * @return Amount of Requests after Filtering
	 */
	public Integer filterActiveDaysByDateRange(LocalDate pickedStartDate, LocalDate pickedEndDate, HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates);
	
	/**
	 * This method gets the intervals and their count values.
	 * @param message Message containing from, to, startDates and endDates
	 * @param requestType Type of required report request
	 * @return ArrayList of Intervals
	 */
	public ArrayList<Interval> getIntervals(MessageObject message, String requestType);
	
	/**
	 * This method filters a date range according to a request type.
	 * @param startDates date
	 * @param endDates date
	 * @param requestType type
	 * @param currentDate date
	 * @param nextDate date
	 * @return amount
	 */
	public Integer filterDateRange(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			String requestType, LocalDate currentDate, LocalDate nextDate);
	
	/**
	 * This method adds a certain value to a key in arrayList.
	 * @param pickedStartDate date
	 * @param result result
	 * @param endPair endPair
	 * @param end end
	 */
	public void addValueToKey(LocalDate pickedStartDate, HashMap<String, Integer> result,
			Map.Entry<String, LocalDate> endPair, LocalDate end);
	
	/**
	 * This method gets the interval by a predefined period of time.
	 * @param requestType type
	 * @param message datat
	 * @param intervals intervals
	 */
	public void getIntervalByPeriod(String requestType, MessageObject message, ArrayList<Interval> intervals);
}
