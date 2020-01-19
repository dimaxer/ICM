package Common;

import java.io.Serializable;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
/** This class represents an interval */
public class Interval implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer value, duration;
	private String categoryName;

	private LocalDate startDate, endDate;
	/** Constructor of Interval
	 * 
	 * @param value value
	 * @param duration duration
	 * @param startDate date
	 * @param endDate date
	 * @param categoryName category
	 */
	public Interval(Integer value, Integer duration, LocalDate startDate, LocalDate endDate, String categoryName) {
		super();
		this.value = value;
		this.duration = duration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.categoryName = categoryName;
	}
	
	/** get value
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	/** set value
	 * 
	 * @param value value
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/** get duration
	 * 
	 * @return duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/** set duration
	 * 
	 * @param duration duration
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/** get start date
	 * 
	 * @return date
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/** set start date
	 * 
	 * @param startDate start date
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/** get end date
	 * 
	 * @return end date
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/** set end date
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/** get category name
	 * 
	 * @return category name
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/** set category name
	 * 
	 * @param categoryName category name
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Override
	public String toString() {
		return "[" + categoryName + "," + value.toString() + "]";
	}

	/** This method merges two arraylists of intervals
	 * 
	 * @param executionIntervals array list A
	 * @param evaluationIntervals array list B
	 * @return unified array list
	 */
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
