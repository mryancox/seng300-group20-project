# Group 20's Paper Submission System for a Journal

**Start the program by running the _JournalSubmissionSystem_ jar file.**

**Please ensure that the _journalsystem.db_ file is in the same folder as the jar file when running it**

**If necessary, use the included sqlite3.exe to open (.open journalsystem.db) to manage the submission and users tables**

To access each account and view its functionality, enter one the following usernames and passwords,

  - Author:
    - usernames: lucas.delvillar@ucalgary.ca, ahmed.zahran1@ucalgary.ca, shahmir.khan@ucalgary.ca
    - password: test
  - Reviewer:
    - usernames: anas.alawa@ucalgary.ca, matthew.cox@ucalgary.ca, ren.xu@ucalgary.ca
    - password: test
  - Admin:
    - username: admin
    - password: test

![login](https://user-images.githubusercontent.com/40903042/55686890-09d29080-5924-11e9-90b3-6218c5af35c7.jpg)

**Submissions go through these stages:**

1. A new submission is made by an author. This is then passed to the admin for approval.

2. If approved, go to stage 3. Otherwise the submission is rejected and does not continue.

3. Once a new submission is approved, it goes to the admin's "Assign reviewers" page. In this page, the admin assigns reviewers.
Once reviewers are assigned, the submission disappears from this page and is passed to the assigned reviewers' accounts.

4. The submission is now in reviewers' pages and they can submit feedback. Once a reviewer submits feedback, the submission disappears from
the reviewer's page and goes back to the admin for feedback approval.

5. Once feedback is approved by the admin, the feedback is visible by the author. The author then has until the set deadline to resubmit.

6. Once the deadline is passed, the resubmit button disappears from the author's page and the submission appears in the admin's final submissions page.
At this point, the admin can approve or deny the final submission.



**New users signup process:**

1. If a new user signs up as an author, the account is automatically available for sign in.

2. Otherwise a new user must have signed up as a reviewer. If this is the case, they can enter their occupation, affiliated organization, and research area.
This information is passed to the admin and their account appears in the "verify reviewers" page.

3. The admin can choose to approve or reject the new reviewer. If approved, the account is unlocked for login. Otherwise the account is marked as rejected.

---

## Summary of page functions:

**Author:**

	New Submission:
		The author must enter information into Paper Title, Authors, and Research Subject. The author must also specify a file location by clicking the box and selecting a file.
		Optionally the author may select one or more preferred reviewers they wish to have review their paper.
		Once these conditions are satisfied, the author can click the Submit button and the paper will be submitted.

	Submissions List:
		This page lists all previous papers submitted by the currently logged in author.
		The author may select up to one submission from this page. Details on the selected paper will be displayed on the bottom half of the page.

	Review Feedback:
		This page lists all feedback available for papers by this author, if they are available.
		The author may select up to one submission from this page. Feedback will be displayed on the bottom half of the page.


**Reviewer:**

	Browse Journals:
		This page allows a reviewer to self-nominate to review a paper.
		The top half of the page lists all subjects recorded in the database.
		The reviewer may select up to one subject. When selected, the bottom half of the page is populated with papers within that subject.
		The reviewer may select up to one paper. Once selected, the reviewer may click the Nominate button which will put them on the list of self-nominated reviewers for the selected paper.

	Provide Feedback:
		This page displays all papers the reviewer has been assigned to review and allows the reviewer to enter feedback.
		The top half of the page displays the assigned papers.
		The reviewer may select up to one paper to review at a time. Feedback can be entered in the bottom half of the page.
		Once a paper is selected and feedback is provided, the reviewer may click the Submit Feedback button and the feedback will be submitted.


**Admin:**

	Review Submissions:
		This page lists all new submissions and allows the admin to open the paper and approve/reject it.
		Up to one paper may be selected, and this will display details about it on the page.
		The admin must input a deadline date in the provided box if they wish to approve a paper.
		Denying a paper does not require a deadline date.

	Verify Reviewers:
		This page lists all newly signed up reviewers.
		The admin may select up to one reviewer and their details will be listed on the page.
		The admin may then approve or reject the reviewer.

	Assign Reviewers:
		This page allows the admin to assign reviewers to submissions which have no assigned reviewers.
		The top half lists all submissions in need of reviewers and the bottom half lists reviewers.
		Once a paper is selected, the admin can sort reviewers by all, nominated by author, or self-nominated (nominated by reviewer).
		The admin can select up to one paper and as many reviewers as needed.
		Once the assign button is clicked, the reviewers are assigned.

	Review Feedback:
		This page allows the admin to review submitted feedback and edit if needed.
		The top half lists all papers with feedback available for review. The bottom half displays an editor for the feedback on the selected paper.
		The admin may edit the feedback however they wish and once done, may click the release feedback button to release the feedback to the author.

	Review Final Submissions:
		This page displays all submissions that have passed their deadline.
		The top half displays all submissions past deadline and allows the admin to open a paper to view.
		The bottom half allows the admin to provide any final feedback.
		Up to one paper may be selected at a time.
		Once a paper is selected, the admin can approve, reject, or extend the paper's deadline by clicking the respective buttons.







