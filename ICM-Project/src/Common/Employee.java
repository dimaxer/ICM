package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

//class for saving all the request data in one place under Request
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;
	// Form info
	private String UserID;
	private String Name;
	private String Email;

	public Employee(String UserID, String Name, String Email) {
		super();
		this.UserID = UserID;
		this.Name = Name;
		this.Email = Email;
	}
	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}
}