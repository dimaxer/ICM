package Utilities;

import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Common.Interval;

/** This class represents a Statistics Utility to be used for statistical calculations and management.*/
public class Statistics implements IStatistics {
	
	
	public float GetMedian(ArrayList<Integer> list) {
		if (list == null)
			throw new NullPointerException("ArrayList<Integer> given is null!");
		if (list.isEmpty())
			return 0;
		
		int length = list.size();
		Collections.sort(list);	// Sort list in Ascending Order
	    if(length%2 == 0)		// if number of elements are even
	        return ((float)list.get((length-1)/2) + (float)list.get(length/2))/2f;
	    else					// if number of elements are odd
	        return (float)list.get(length/2);
	}

	
	public float GetStandardDeviation(ArrayList<Integer> list) {
		if (list == null)
			throw new NullPointerException("ArrayList<Integer> given is null!");
		if (list.isEmpty())
			return 0;
		
		float sum = 0, mean = 0, standardDeviation = 0;
		
	    for(int i = 0; i < list.size(); ++i)
	        sum += list.get(i);
	    mean = sum/list.size();
	    
	    for(int i = 0; i < list.size(); ++i)
	        standardDeviation += Math.pow(list.get(i) - mean, 2);
	    
	    return (float) Math.sqrt(standardDeviation / list.size());
	}
	
	public Integer filterStatusByDateRange(LocalDate pickedStartDate, LocalDate pickedEndDate, HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates) {
		if (pickedStartDate == null || pickedEndDate == null || startDates == null || endDates == null) throw new NullPointerException();
		
		Boolean shouldKeep = false;
		
		HashMap<String, Boolean> result = new HashMap<>();
	    Iterator it = endDates.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, LocalDate> endPair = (Map.Entry<String, LocalDate>)it.next();

	        if (endPair.getValue() != null) {
	        	shouldKeep = !(endPair.getValue().isBefore(pickedStartDate) || startDates.get(endPair.getKey()).isAfter(pickedEndDate));
	        	if (shouldKeep)
	        		result.put(endPair.getKey(), true);
	        } else if (!startDates.get(endPair.getKey()).isAfter(pickedEndDate))
	        	result.put(endPair.getKey(), true);
	    }
	    
		return result.size();
	}
	
	public Integer filterActiveDaysByDateRange(LocalDate pickedStartDate, LocalDate pickedEndDate, HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates) {
		if (pickedStartDate == null || pickedEndDate == null || startDates == null || endDates == null) throw new NullPointerException();
		
		Boolean shouldKeep = false;
		
		HashMap<String, Integer> result = new HashMap<>();
	    Iterator it = endDates.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, LocalDate> endPair = (Map.Entry<String, LocalDate>)it.next();
	        LocalDate end = endPair.getValue();
	        if (end == null)
	        	end = pickedEndDate;
	        LocalDate start = startDates.get(endPair.getKey());
	        if (pickedEndDate.isBefore(start) || pickedStartDate.isAfter(end)) continue;
	        if (!start.isAfter(pickedStartDate) && !pickedStartDate.isAfter(end) && !end.isAfter(pickedEndDate))
	        	addValueToKey(pickedStartDate, result, endPair, end);
	        else if (!start.isBefore(pickedStartDate) && !end.isAfter(pickedEndDate))
	        	addValueToKey(start, result, endPair, end);
	        else if (!start.isBefore(pickedStartDate) && end.isAfter(pickedEndDate))
	        	addValueToKey(start, result, endPair, pickedEndDate);
	    }
	    
	    int amount = 0;
	    it = result.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> resultPair = (Map.Entry<String, Integer>)it.next();
	        amount += resultPair.getValue();
	    }
	    
		return amount;
	}

	public void addValueToKey(LocalDate pickedStartDate, HashMap<String, Integer> result,
			Map.Entry<String, LocalDate> endPair, LocalDate end) {
		int addedValue = (int)Math.abs((Duration.between(end.atStartOfDay(), pickedStartDate.atStartOfDay()).toDays())) + 1;
		if (result.containsKey(endPair.getKey()))
			result.put(endPair.getKey(), result.get(endPair.getKey()) + addedValue);
		else
			result.put(endPair.getKey(), addedValue);
	}
	
	public ArrayList<Interval> getIntervals(MessageObject message, String requestType) {
		if (requestType.equals("Extensions") || requestType.equals("Durations") || requestType.equals("Delays"))
			return getExtensionsIntervals(message);

		ArrayList<Interval> intervals = new ArrayList<>();

		getIntervalByPeriod(requestType, message, intervals);
		return intervals;
	}

	public void getIntervalByPeriod(String requestType, MessageObject message, ArrayList<Interval> intervals) {
		LocalDate from = (LocalDate)message.getArgs().get(1);
		LocalDate to = (LocalDate)message.getArgs().get(2);
		HashMap<String, LocalDate> startDates = (HashMap<String, LocalDate>)message.getArgs().get(3);
		HashMap<String, LocalDate> endDates = (HashMap<String, LocalDate>)message.getArgs().get(4);
		int daysBetweenDates = (int)Math.abs(Duration.between(to.atStartOfDay(), from.atStartOfDay()).toDays());
		
		if (daysBetweenDates <= 7)
			getDaysInterval(startDates, endDates, intervals, daysBetweenDates, from, to, requestType);
		else if (daysBetweenDates <=28)
			getWeeksInterval(startDates, endDates, intervals, daysBetweenDates, from, to, requestType);
		else if (daysBetweenDates <=365)
			getMonthsInterval(startDates, endDates, intervals, daysBetweenDates, from, to, requestType);
		else
			getYearsInterval(startDates, endDates, intervals, daysBetweenDates, from, to, requestType);
	}

	private ArrayList<Interval> getExtensionsIntervals(MessageObject message) {
		try {
		HashMap<String, LocalDate> startDates = (HashMap<String, LocalDate>)message.getArgs().get(0);
		HashMap<String, LocalDate> endDates = (HashMap<String, LocalDate>)message.getArgs().get(1);
		
		Integer key = 0, max = 0, amount = 0, interval = 0;
		HashMap<Integer, Integer> result = new HashMap<>();
		
		ArrayList<Integer> keyList = new ArrayList<>();
		ArrayList<Interval> intervals = new ArrayList<>();
		Iterator it = endDates.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, LocalDate> endPair = (Map.Entry<String, LocalDate>)it.next();
	        LocalDate end = endPair.getValue();
	        LocalDate start = startDates.get(endPair.getKey());
	        key = (int)Math.abs((Duration.between(end.atStartOfDay(), start.atStartOfDay()).toDays()));
	        
	        if (max < key)
	        	max = key;
	        
	        if (result.containsKey(key))
	        	 result.put(key, result.get(key) + 1);
	        else result.put(key, 1);
	        
	        if (!keyList.contains(key))
	        	keyList.add(key);
	    }
	    
	    Collections.sort(keyList);
	    amount = result.size();
	    if (amount == 0)
	    	return intervals;
	    interval = Math.floorDiv(max,amount)+1;
	    
	    for (int i = 0; i < Math.floorDiv(max,interval)+1; i++)
	    	intervals.add(new Interval(0, 0, null, null, i*interval + "-" + ((i+1)*interval-1)));
	    
	    for (int i = 0; i < keyList.size(); i++) {
	    	int index = Math.floorDiv(keyList.get(i),interval);
	    	Interval currentInterval = intervals.get(index);
	    	currentInterval.setValue(currentInterval.getValue() + result.get(keyList.get(i)));
	    	intervals.set(index, currentInterval);
	    }
		
		return intervals;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	private void getDaysInterval(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			ArrayList<Interval> intervals, int daysBetweenDates, LocalDate from, LocalDate to, String requestType) {
		Integer value;
		LocalDate currentDate = from;
		
		for (int i = 0; i < daysBetweenDates; i++) {
			value = filterDateRange(startDates, endDates, requestType, currentDate, currentDate);
			intervals.add(new Interval(value, 1, currentDate, currentDate, currentDate.plusDays(1).getDayOfWeek().name()));
			currentDate = currentDate.plusDays(1);
		}
	}
	
	private void getWeeksInterval(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			ArrayList<Interval> intervals, int daysBetweenDates, LocalDate from, LocalDate to, String requestType) {
		Integer value;
		LocalDate currentDate = from.plusDays(1);
		LocalDate nextDate;
		for (int i = 0; i < (int)Math.ceil((double)daysBetweenDates/7); i++) {
			int daysLeft = (int)Math.abs(Duration.between(to.atStartOfDay(), currentDate.atStartOfDay()).toDays());
			if (daysLeft < 7)
				 nextDate = currentDate.plusDays(daysLeft - 1);
			else nextDate = currentDate.plusDays(6);
			
			value = filterDateRange(startDates, endDates, requestType, currentDate, nextDate);
			intervals.add(new Interval(value, 6, currentDate, nextDate, "Week " + i));
			currentDate = nextDate.plusDays(1);
			if (!currentDate.isBefore(to)) return;
		}
	}

	public Integer filterDateRange(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			String requestType, LocalDate currentDate, LocalDate nextDate) {
		Integer value;
		if (requestType.equals("FilterStatus"))
			value = filterStatusByDateRange(currentDate, nextDate, startDates, endDates);
		else
			value = filterActiveDaysByDateRange(currentDate, nextDate, startDates, endDates);
		return value;
	}
	
	private void getMonthsInterval(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			ArrayList<Interval> intervals, int daysBetweenDates, LocalDate from, LocalDate to, String requestType) {
		Integer value;
		LocalDate currentDate = from.plusDays(1);
		
		for (int i = 0; i < (int)Math.ceil((double)daysBetweenDates/30); i++) {
			LocalDate nextDate;
			if (currentDate.getMonthValue() == 12)
				 nextDate = currentDate.plusYears(1).withMonth(1).withDayOfMonth(1);
			else nextDate = currentDate.withMonth(currentDate.getMonthValue()+1).withDayOfMonth(1);
			
			value = filterDateRange(startDates, endDates, requestType, currentDate, nextDate);
			intervals.add(new Interval(value, 30, currentDate, nextDate, currentDate.getMonth().name()));
			currentDate = nextDate;
		}
	}

	private void getYearsInterval(HashMap<String, LocalDate> startDates, HashMap<String, LocalDate> endDates,
			ArrayList<Interval> intervals, int daysBetweenDates, LocalDate from, LocalDate to, String requestType) {
		Integer value;
		LocalDate currentDate = from.plusDays(1);
		LocalDate nextDate = currentDate.plusYears(1);
		for (int i = 0; i < (int)Math.ceil((double)daysBetweenDates/365); i++) {
			int daysLeft = (int)Math.abs(Duration.between(to.atStartOfDay(), currentDate.atStartOfDay()).toDays());
			
			if (daysLeft < 365)
				nextDate = currentDate.plusDays(daysLeft);
			else nextDate = currentDate.plusYears(1).withMonth(1).withDayOfMonth(1);
			
			value = filterDateRange(startDates, endDates, requestType, currentDate, nextDate);
			intervals.add(new Interval(value, 365, currentDate, nextDate, String.format("%d", currentDate.getYear())));
			currentDate = nextDate;
		}
	}
}
