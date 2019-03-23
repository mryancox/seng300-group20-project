package seng300project;

public class ReviewerObject {

	public int userID;

	public String username;

	public String name;

	public String email;

	public int userType;

	public ReviewerObject(int userID, String username, String name, String email, int userType) {
		this.userID = userID;
		this.username = username;
		this.name = name;
		this.email = email;
		this.userType = userType;

	}

	public String toString() {
		return name + " - " + email;
	}
	
}
