package seng300project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SubmissionObject {

	//unique ID for every submission
	public int submissionID;

	//Title of submission
	public String submissionName;

	//Authors of submission
	public String submissionAuthors;

	//Submission subject/field
	public String subject;

	//Date submission was submitted
	public String submissionDate;

	//Deadline for resubmission
	public String submissionDeadline;

	//Stage of submission (e.g. 1 = first revision, 2 = second, 3 = final or whatever)
	public int submissionStage;

	//filename of submission
	public String filename;

	//String representing IDs of assigned reviewers (e.g. 4,5,6)
	public String reviewerIDs;

	//IDs of feedback for a submission
	public String feedbackIDs;

	//IDs of preferred reviewers for a submission
	public String preferredReviewerIDs;

	//ID of user who made the submission
	public int submissionUserID;

	//Names of reviewers assigned to the submission
	//May be null if reviewerIDs is null
	public String reviewerNames;

	//Map for reviewer assigned papers logic to check if
	//a paper is assigned to a reviewer
	public Map<Integer, Integer> reviewers = new HashMap<Integer, Integer>();
	

	/**
	 * Test main method
	 * @param args
	 */
	public static void main(String[] args) {
		SubmissionObject test = new SubmissionObject();
		test.reviewerIDs="6";
		test.setReviewerNames();

	}

	//empty constructor for the test
	public SubmissionObject() {}

	/**
	 * Constructor for SubmissionObject
	 * @param submissionID
	 * @param submissionName
	 * @param submissionAuthors
	 * @param subject
	 * @param submissionDate
	 * @param submissionStage
	 * @param filename
	 * @param submissionUserID
	 */
	public SubmissionObject(int submissionID, String submissionName, String submissionAuthors, String subject, String submissionDate,
			 int submissionStage, String filename,  int submissionUserID) {
		this.submissionID=submissionID;
		this.submissionName=submissionName;
		this.submissionAuthors=submissionAuthors;
		this.subject=subject;
		this.submissionDate=submissionDate;
		//this.submissionDeadline = submissionDeadline;
		this.submissionStage=submissionStage;
		this.filename=filename;
		//this.reviewerIDs=reviewerIDs;
		//this.feedbackIDs=feedbackIDs;
		//this.preferredReviewerIDs=preferredReviewerIDs;
		this.submissionUserID=submissionUserID;
	}


	/**
	 * Updates each submission's String of reviewer names if reviewers exist for it
	 */
	public void setReviewerNames() {
		if(reviewerIDs!=null) {
			getSubReviewers(this.reviewerIDs);
		}
	}

	/**
	 * Gets assigned reviewers for a particular submission
	 * @param reviewerIDs String list of reviewer ID numbers, separated by commas
	 * @return returns complete StringBuilder of reviewers
	 */
	private void getSubReviewers(String reviewerIDs) {
		PreparedStatement ps;
		ResultSet rs;

		//Array of reviewer IDs
		String[] IDs = reviewerIDs.split("[,]");
		StringBuilder reviewerNames = new StringBuilder();

		//populate reviewers map 
		for(int i=0;i<IDs.length;i++)
			reviewers.put(Integer.parseInt(IDs[i]), 1);
		
		//the query string (check Login class for a more detailed explanation)
		String query = "SELECT * FROM users WHERE userID = ?";

		//Loops through the array of reviewer IDs,
		//adding the name to the StringBuilder until no more IDs remain
		for(int i=0; i<IDs.length; i++) {
			try {
				ps = SQLConnection.getConnection().prepareStatement(query);
				ps.setString(1, IDs[i]);

				rs = ps.executeQuery();
				rs.next();

				reviewerNames.append(rs.getString("name") + ", ");

			}catch(Exception e) {}
		}
		
		//remove last 2 characters in names (a space and comma))
		reviewerNames.setLength(reviewerNames.length()-2);
		
		this.reviewerNames = reviewerNames.toString();
	}


}
