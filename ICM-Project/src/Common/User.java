package Common;

import java.io.Serializable;
import java.util.ArrayList;

enum JobDescription {
	Student, AcademicEmployee, Lecturer;
}

//class for saving all the User data in one place under User
public class User implements Serializable{

	private String id;
	private String password;
	private String name;
	private String email;

	private String jobDescription;

	private ArrayList<Request> requestArray;

	public User(String id, String password, String name, String email, String jobDescription) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.jobDescription = jobDescription;
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
}
