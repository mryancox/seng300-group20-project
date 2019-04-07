package objects;

public class ReviewerObject {

	//unique ID for every user
	public int userID;

	// Username of reviewer
	public String username;

	// Name of reviewer
	public String name;

	// Email of reviewer
	public String email;

	// User's type as an integer
	public int userType;
	
	public String occupation;
	
	public String organization;
	
	public String researchArea;

	/**
	 * Constructor for SubmissionObject
	 * @param userID
	 * @param username
	 * @param name
	 * @param email
	 * @param userType
	 */
	public ReviewerObject(int userID, String username, String name, String email, int userType) {
		this.userID = userID;
		this.username = username;
		this.name = name;
		this.email = email;
		this.userType = userType;
	}

	//empty constructor for the test
	public ReviewerObject() {
	}

	public String toString() {
		return name + " - " + email;
	}
	
}
