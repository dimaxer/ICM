package Common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/** class for saving all the request data in one place under Request */
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;
	// Form info
	private String UserID;
	private String Name;
	private String Email;

	/** Employee Constructor
	 * 
	 * @param UserID id
	 * @param Name name
	 * @param Email email
	 */
	public Employee(String UserID, String Name, String Email) {
		super();
		this.UserID = UserID;
		this.Name = Name;
		this.Email = Email;
	}
	
	/** get user id
	 * 
	 * @return id
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * set user id
	 * @param userID id
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

	/** get user name
	 * 
	 * @return name
	 */
	public String getName() {
		return Name;
	}

	/** set user name
	 * 
	 * @param name name
	 */
	public void setName(String name) {
		Name = name;
	}

	/** get email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * set email
	 * @param email email
	 */
	public void setEmail(String email) {
		Email = email;
	}
}