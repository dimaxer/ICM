package Common;

public class AcademicEmployee extends User {
	private static final long serialVersionUID = 1L;
	private String department;
	
	public AcademicEmployee(String id, String password, String name, String email, String jobDescription) {
		super(id, password, name, email, jobDescription);
		// TODO Auto-generated constructor stub
	}
}