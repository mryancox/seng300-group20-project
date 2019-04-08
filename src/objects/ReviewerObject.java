package objects;

/*
 * ReviewerObject creates an object for other classes to easily (and quickly) access data
 * from the database. This class helped performance previously when we were using a remote
 * database (MySQL) but was kept for convenience.
 * 
 * @author L01-Group20
 */
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
	 * @param userID, The user's ID from the database
	 * @param username, The username (email) form the database
	 * @param name, The reviewer's name from the database
	 * @param userType, The user's type from the database
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

	// Overwriting toString for displaying in assigning reviewers
	public String toString() {
		return name + " - " + username;
	}

}
