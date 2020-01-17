package Common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Interval implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer value, duration;
	private String categoryName;

	private LocalDate startDate, endDate;
	
	public Interval(Integer value, Integer duration, LocalDate startDate, LocalDate endDate, String categoryName) {
		super();
		this.value = value;
		this.duration = duration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.categoryName = categoryName;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Override
	public String toString() {
		return "[" + categoryName + "," + value.toString() + "]";
	}

	public static ArrayList<Interval> merge(ArrayList<Interval> executionIntervals, ArrayList<Interval> evaluationIntervals) {
		if (executionIntervals == null) throw new NullPointerException();
		if (evaluationIntervals == null) throw new NullPointerException();
		
		Interval current;
		for (int i = 0; i < evaluationIntervals.size(); i++) {
			current = evaluationIntervals.get(i);
			for (int j = 0; j < executionIntervals.size(); j++)
				if (executionIntervals.get(j).getCategoryName().equals(current.getCategoryName()))
					executionIntervals.get(j).value += current.value;
		}
		
		return executionIntervals;
	}
}
