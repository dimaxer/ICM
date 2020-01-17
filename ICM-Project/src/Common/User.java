package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

enum JobDescription {
	Student, AcademicEmployee, Lecturer;
}

//class for saving all the User data in one place under User
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	private String name;
	private String email;
	private String jobDescription;

	private ArrayList<Request> requestArray;

	public User(String id, String password, String name, String email, String jobDescription) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.jobDescription = jobDescription;
		this.requestArray = new ArrayList<>();
	}
	
	public User(ResultSet rs) throws SQLException {
		this.id = rs.getString("UserID");
		this.password = rs.getString("Password");
		this.name = rs.getString("Name");
		this.email = rs.getString("Email");
		this.jobDescription = "Default";
		this.requestArray = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public ArrayList<Request> getRequestArray() {
		return requestArray;
	}

	public void setRequestArray(ArrayList<Request> requestArray) {
		this.requestArray = requestArray;
	}
	
	@Override
	public String toString() {
		return String.format("USER[%s,%s,%s,%s,%s,{%s}]", id, password, name, email, jobDescription, requestArray.toString());
	}
}