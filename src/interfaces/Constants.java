package interfaces;

/*
 * Constants contains a list of final variables that describe what stage a submission
 * is in and help show the flow a submission follows for it to be approved. Each major view
 * implements this class.
 * 
 * @author L01-Group20
 */
public interface Constants {

	// A new submission starts in stage 1 visible only in admin's new submission
	// tab.

	// If accepted it moves to stage 2, where it can be assigned reviewers in the
	// admin's assign reviewers tab.

	// After assigning reviewers, it moves to stage 3 and becomes visible to
	// reviewers.

	// After receiving feedback from at least one reviewer, it moves to stage 4 and
	// becomes
	// visible in the admin's review feedback page, disappearing from reviewer's
	// feedback page.

	// After feedback is reviewed and released, it moves to stage 5, visible in the
	// author's review feedback page.

	// The author can start resubmitting here until the deadline. After
	// resubmitting, it moves back to stage 3.

	// After passing deadline, it moves to stage 6, appearing in the admin's review
	// final submissions page.

	// If accepted, it reaches stage 7 and disappears from all lists. It is still in
	// the database.
	// If rejected it should move to either stage 0 or
	final static int REJECTED_SUBMISSION_STAGE = 0;
	final static int NEW_SUBMISSION_STAGE = 1;
	final static int APPROVED_SUBMISSION_STAGE = 2;
	final static int FEEDBACK_GATHERING_STAGE = 3;
	final static int FEEDBACK_REVIEW_STAGE = 4;
	final static int RESUBMIT_STAGE = 5;
	final static int FINAL_APPROVED_STAGE = 6;

	// levels for feedback currently unused
	final static int INITIAL_FEEDBACK_LEVEL = 0;
	final static int RELEASED_FEEDBACK = 1;
}
