package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/** Job Description Enum
 *
 */
enum JobDescription {
	Student, AcademicEmployee, Lecturer;
}

/** class for saving all the User data in one place under User
 * 
 * 
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	private String name;
	private String email;
	private String jobDescription;

	private ArrayList<Request> requestArray;

	/** User Constructor
	 * 
	 * @param id id
	 * @param password password
	 * @param name name
	 * @param email email
	 * @param jobDescription role
	 */
	public User(String id, String password, String name, String email, String jobDescription) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.jobDescription = jobDescription;
		this.requestArray = new ArrayList<>();
	}
	
	/** User Constructor
	 * 
	 * @param rs data resultset
	 * @throws SQLException exception
	 */
	public User(ResultSet rs) throws SQLException {
		this.id = rs.getString("UserID");
		this.password = rs.getString("Password");
		this.name = rs.getString("Name");
		this.email = rs.getString("Email");
		this.jobDescription = "Default";
		this.requestArray = new ArrayList<>();
	}

	/** get id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/** set id
	 * 
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/** get password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/** set password
	 * 
	 * @param password password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** get name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/** set name
	 * 
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** get email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/** set email
	 * 
	 * @param email email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** get job description
	 * 
	 * @return job description
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/** set job description
	 * 
	 * @param jobDescription job description
	 */
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	/** get request array
	 * 
	 * @return request array
	 */
	public ArrayList<Request> getRequestArray() {
		return requestArray;
	}

	/** set request array
	 * 
	 * @param requestArray request array
	 */
	public void setRequestArray(ArrayList<Request> requestArray) {
		this.requestArray = requestArray;
	}
	
	@Override
	public String toString() {
		return String.format("USER[%s,%s,%s,%s,%s,{%s}]", id, password, name, email, jobDescription, requestArray.toString());
	}
}