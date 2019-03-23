package seng300project;

public class FeedbackObject {

	//unique ID for every feedback entry
	public int feedbackID;

	//date feedback was submitted
	public String feedbackDate;

	//filename for feedback
	public String filename;

	//ID of user who submitted feedback
	public int userID;

	//ID of submission feedback is for
	public int submissionID;

	//check for admin approval
	public int approval;

	//stage of paper feedback was submitted
	//feedbackStage and submissionStage should match
	//to display feedback for a paper
	public int feedbackStage;

	public FeedbackObject() {}

	public FeedbackObject(int feedbackID, String feedbackDate, String filename, int userID, int submissionID, int feedbackStage, int approval) {
		this.feedbackID = feedbackID;
		this.feedbackDate = feedbackDate;
		this.filename = filename;
		this.userID = userID;
		this.submissionID = submissionID;
		this.feedbackStage = feedbackStage;
		this.approval = approval;
	}



}
