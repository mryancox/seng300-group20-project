package objects;

public class ReviewerObject {

	// unique ID for every user
	public int userID;

	// Username of reviewer
	public String username;

	// Name of reviewer
	public String name;

	// User's type as an integer
	public int userType;

	//below fields to be used for sign up of a reviewer
	public String occupation;

	public String organization;

	public String researchArea;

	/**
	 * Constructor for SubmissionObject
	 * 
	 * @param userID
	 * @param username
	 * @param name
	 * @param email
	 * @param userType
	 */
	public ReviewerObject(int userID, String username, String name, int userType) {
		this.userID = userID;
		this.username = username;
		this.name = name;
		this.userType = userType;
	}

	// empty constructor for the test
	public ReviewerObject() {
	}

	public String toString() {
		return name + " - " + username;
	}

}
