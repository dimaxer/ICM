package Common;
/** This class represents an academic employee */
public class AcademicEmployee extends User {
	private static final long serialVersionUID = 1L;
	private String department;
	
	/** Constuctor of Academic Employee
	 * 
	 * @param id id
	 * @param password password
	 * @param name name
	 * @param email email
	 * @param jobDescription role
	 */
	public AcademicEmployee(String id, String password, String name, String email, String jobDescription) {
		super(id, password, name, email, jobDescription);
		// TODO Auto-generated constructor stub
	}
}