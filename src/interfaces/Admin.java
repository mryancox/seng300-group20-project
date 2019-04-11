package interfaces;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;

import objects.GUIObjects;
import objects.ReviewerObject;
import objects.SQLConnection;
import objects.SubmissionObject;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/*
 * Admin contains GUI elements and functionality to implement the requirements described
 * in our user stories. It handles approving new and final submissions, approving new
 * reviewers, and management of feedback provided by reviewers.
 * 
 * @author L01-Group20
 */
@SuppressWarnings("unused")
public class Admin extends JFrame implements Constants {

	/**
	 * Variables to enable database and file I/O functionality
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	protected String newTitle;
	protected String newAuthors;
	protected String[] newAuthorsArray;
	protected String newSubject;
	protected String newPrefreviewers;
	protected String[] newPrefreviewersArray;
	protected String filename;
	protected String resubmitFilename;
	protected String paperInDetail;
	protected String filelocation;
	protected int userID;
	protected ResultSet reviewerSet;
	protected ResultSet submissionSet;
	protected ResultSet applicantSet;
	protected SubmissionObject[] newSubmissions;
	protected SubmissionObject[] finalSubmissions;
	protected ReviewerObject[] reviewers;
	protected ReviewerObject[] applicants;
	protected String[] subjects = new String[100];
	private int[] selectedPaper = new int[0];
	private int[] selected = new int[0];
	private int[] fbAreaSelected = new int[0];
	private int[] selectedApplicant = new int[0];
	private Map<Integer, ReviewerObject> reviewerIDtoObject = new HashMap<Integer, ReviewerObject>();
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin("Admin2", 9, SQLConnection.getConnection());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the administrator frame.
	 * 
	 * @param user, User's name (not username)
	 * @param ID, user's userID (invisible to user)
	 * @param conn, The connection passed in from the Login page that is established to the database
	 */
	public Admin(String user, int ID, Connection conn) {
		this.userID = ID;
		this.conn = conn;

		// get resultset of new submissions
		getSubmissions(NEW_SUBMISSION_STAGE);

		// populate an array of new submissions
		newSubmissions = populateSubmissions(newSubmissions);

		// get array of reviewers
		getReviewers();
		getApplicants();

		setTitle("Journal Submission System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// check for and create possible folders for user
		String acceptedSubmissionsList = "submissions/acceptedsubmissions/";
		File acceptedSubmissionsFolder = new File(acceptedSubmissionsList);

		// Checks if the required folders exist already, if not it creates them
		if (!acceptedSubmissionsFolder.exists())
			acceptedSubmissionsFolder.mkdirs();

		/*
		 * A lot of GUI elements follow. The functionality that is associated with all
		 * of these panels, textareas, etc. are at the bottom of this class.
		 * 
		 * Creating the two main GUI panels
		 */
		JPanel menuPanel = new GUIObjects().menuPanel();
		contentPane.add(menuPanel);

		JPanel contentPanel = new GUIObjects().mainContentPanel();
		contentPane.add(contentPanel);

		/*
		 * Creating the welcome panel
		 */
		JPanel welcomePanel = new GUIObjects().contentPanel();
		contentPanel.add(welcomePanel, "name_46926967448500");

		JLabel adminWelcome = new GUIObjects().welcomeLabel(user);
		welcomePanel.add(adminWelcome);

		JLabel adminIcon = new GUIObjects().icon("/admin.jpg");
		adminIcon.setBounds(184, 212, 346, 200);
		welcomePanel.add(adminIcon);

		/*
		 * Creating the submission panel and filling it with its elements using
		 * GUIObjects
		 */
		JPanel submissionsPanel = new GUIObjects().contentPanel();
		contentPanel.add(submissionsPanel, "name_35648043125700");

		JLabel submissionsTitle = new GUIObjects().contentTitle("Review Submissions");
		submissionsPanel.add(submissionsTitle);

		JLabel newHeader = new GUIObjects().contentHeader("New Submissions", 90);
		submissionsPanel.add(newHeader);

		DefaultListModel<SubmissionObject> newSubmissionModel = new DefaultListModel<>();
		JList<SubmissionObject> submissionsList = new JList<SubmissionObject>(newSubmissionModel);
		submissionsList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane submissionsListScrollPane = new JScrollPane(submissionsList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		submissionsListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		submissionsListScrollPane.setBounds(24, 115, 650, 110);
		submissionsPanel.add(submissionsListScrollPane);

		JPanel opennewButton = new GUIObjects().contentButton(555, 235);
		JLabel opennewLabel = new GUIObjects().contentButtonLabel("Open Paper");
		opennewButton.add(opennewLabel);
		submissionsPanel.add(opennewButton);

		JPanel subSeparator = new GUIObjects().separatorPanel(270);
		submissionsPanel.add(subSeparator);

		JLabel detailsHeader = new GUIObjects().contentHeader("Submission Details", 285);
		submissionsPanel.add(detailsHeader);

		JLabel detailsSubHeader = new GUIObjects().contentSubHeader("(select a paper above)", 305);
		submissionsPanel.add(detailsSubHeader);

		JLabel titleLabel = new GUIObjects().contentLabel("Title", 24, 350);
		submissionsPanel.add(titleLabel);

		JLabel titleSubLabel = new GUIObjects().contentSubLabel(24, 380);
		submissionsPanel.add(titleSubLabel);

		JLabel authorsLabel = new GUIObjects().contentLabel("Author", 24, 410);
		submissionsPanel.add(authorsLabel);

		JLabel authorsSubLabel = new GUIObjects().contentSubLabel(24, 440);
		submissionsPanel.add(authorsSubLabel);

		JLabel subjectLabel = new GUIObjects().contentLabel("Research Subject", 359, 350);
		submissionsPanel.add(subjectLabel);

		JLabel subjectSubLabel = new GUIObjects().contentSubLabel(359, 380);
		submissionsPanel.add(subjectSubLabel);

		JLabel preferredLabel = new GUIObjects().contentLabel("Preferred Reviewers", 359, 410);
		submissionsPanel.add(preferredLabel);

		JLabel preferredSubLabel = new GUIObjects().contentSubLabel(359, 440);
		submissionsPanel.add(preferredSubLabel);

		JPanel detailsSeparator = new GUIObjects().separatorPanel(470);
		submissionsPanel.add(detailsSeparator);

		JLabel deadlineHeader = new GUIObjects().contentHeader("Set Deadline", 485);
		submissionsPanel.add(deadlineHeader);

		JTextArea deadlineTextArea = new GUIObjects().contentTextArea(510);
		deadlineTextArea.setText("YYYY-MM-DD");
		deadlineTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionsPanel.add(deadlineTextArea);

		JPanel approveButton = new GUIObjects().contentButton(425, 520);
		JLabel approveLabel = new GUIObjects().contentButtonLabel("Approve");
		approveButton.add(approveLabel);
		submissionsPanel.add(approveButton);

		JPanel rejectButton = new GUIObjects().contentButton(555, 520);
		JLabel rejectLabel = new GUIObjects().contentButtonLabel("Reject");
		rejectButton.add(rejectLabel);
		submissionsPanel.add(rejectButton);

		/*
		 * Creating the verify panel and filling it with its elements using GUIObjects
		 */
		JPanel verifyPanel = new GUIObjects().contentPanel();
		contentPanel.add(verifyPanel, "name_95100187161800");

		JLabel verifyTitle = new GUIObjects().contentTitle("Verify Reviewers");
		verifyPanel.add(verifyTitle);

		JLabel applicantsHeader = new GUIObjects().contentHeader("Applicants", 90);
		verifyPanel.add(applicantsHeader);

		DefaultListModel<ReviewerObject> applicantModel = new DefaultListModel<>();
		JList<ReviewerObject> applicantList = new JList<ReviewerObject>(applicantModel);
		applicantList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane applicantListScrollPane = new JScrollPane(applicantList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		applicantListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		applicantListScrollPane.setBounds(24, 115, 650, 178);
		verifyPanel.add(applicantListScrollPane);

		JPanel verifySeparator = new GUIObjects().separatorPanel(305);
		verifyPanel.add(verifySeparator);

		JLabel appdetailsHeader = new GUIObjects().contentHeader("Applicants Details", 320);
		verifyPanel.add(appdetailsHeader);

		JLabel appdetailsSubHeader = new GUIObjects().contentSubHeader("(select a new applicant above)", 340);
		verifyPanel.add(appdetailsSubHeader);

		JLabel occupationLabel = new GUIObjects().contentLabel("Occupation", 24, 385);
		verifyPanel.add(occupationLabel);

		JLabel occupationSubLabel = new GUIObjects().contentSubLabel(24, 415);
		verifyPanel.add(occupationSubLabel);

		JLabel organizationLabel = new GUIObjects().contentLabel("Organization", 24, 445);
		verifyPanel.add(organizationLabel);

		JLabel organizationSubLabel = new GUIObjects().contentSubLabel(24, 475);
		verifyPanel.add(organizationSubLabel);

		JLabel researchareaLabel = new GUIObjects().contentLabel("Research Area", 359, 385);
		verifyPanel.add(researchareaLabel);

		JLabel researchareaSubLabel = new GUIObjects().contentSubLabel(359, 415);
		verifyPanel.add(researchareaSubLabel);

		JPanel approveappButton = new GUIObjects().contentButton(425, 520);
		JLabel approveappLabel = new GUIObjects().contentButtonLabel("Approve");
		approveappButton.add(approveappLabel);
		verifyPanel.add(approveappButton);

		JPanel rejectappButton = new GUIObjects().contentButton(555, 520);
		JLabel rejectappLabel = new GUIObjects().contentButtonLabel("Reject");
		rejectappButton.add(rejectappLabel);
		verifyPanel.add(rejectappButton);

		/*
		 * Creating the assign panel and filling it with its elements using GUIObjects
		 */
		JPanel assignPanel = new GUIObjects().contentPanel();
		contentPanel.add(assignPanel, "name_100789627501200");

		JLabel assignTitle = new GUIObjects().contentTitle("Assign Reviewers");
		assignPanel.add(assignTitle);

		JLabel assignpaperHeader = new GUIObjects().contentHeader("Choose Paper To Assign Reviewers To", 90);
		assignPanel.add(assignpaperHeader);

		DefaultListModel<SubmissionObject> assignpaperModel = new DefaultListModel<SubmissionObject>();
		JList<SubmissionObject> assignpaperList = new JList<>(assignpaperModel);
		assignpaperList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane assignpaperListScrollPane = new JScrollPane(assignpaperList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		assignpaperListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		assignpaperListScrollPane.setBounds(24, 115, 650, 170);
		assignPanel.add(assignpaperListScrollPane);

		JPanel assignSeparator = new GUIObjects().separatorPanel(305);
		assignPanel.add(assignSeparator);

		JLabel reviewersHeader = new GUIObjects().contentHeader("Reviewer List", 320);
		assignPanel.add(reviewersHeader);

		DefaultListModel<ReviewerObject> reviewerModel = new DefaultListModel<ReviewerObject>();
		JList<ReviewerObject> reviewerList = new JList<>(reviewerModel);
		reviewerList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane reviewerListScrollPane = new JScrollPane(reviewerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		reviewerListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		reviewerListScrollPane.setBounds(24, 345, 650, 170);
		assignPanel.add(reviewerListScrollPane);

		JPanel allSortButton = new GUIObjects().contentSortButton(70, 315, 320);
		JLabel allSortButtonLabel = new GUIObjects().contentSortButtonLabel("All", 70);
		allSortButton.add(allSortButtonLabel);
		assignPanel.add(allSortButton);

		JPanel selfSortButton = new GUIObjects().contentSortButton(135, 394, 320);
		JLabel selfSortButtonLabel = new GUIObjects().contentSortButtonLabel("Self-Nominated", 135);
		selfSortButton.add(selfSortButtonLabel);
		assignPanel.add(selfSortButton);

		JPanel authorSortButton = new GUIObjects().contentSortButton(135, 539, 320);
		JLabel authorSortButtonLabel = new GUIObjects().contentSortButtonLabel("Nominated By Author", 135);
		authorSortButton.add(authorSortButtonLabel);
		assignPanel.add(authorSortButton);

		JPanel assignreviewerButton = new GUIObjects().contentButton(555, 520);
		JLabel assignreviewerLabel = new GUIObjects().contentButtonLabel("Assign");
		assignreviewerButton.add(assignreviewerLabel);
		assignPanel.add(assignreviewerButton);

		/*
		 * Creating the feedback panel and filling it with its elements using GUIObjects
		 */
		JPanel feedbackPanel = new GUIObjects().contentPanel();
		contentPanel.add(feedbackPanel, "name_47558521480000");

		JLabel feedbackTitle = new GUIObjects().contentTitle("Review Feedback");
		feedbackPanel.add(feedbackTitle);

		JLabel reviewHeader = new GUIObjects().contentHeader("Papers With Feedback Waiting For Review", 90);
		feedbackPanel.add(reviewHeader);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> feedbackList = new JList<SubmissionObject>(paperModel);
		feedbackList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane feedbackListScrollPane = new JScrollPane(feedbackList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackListScrollPane.setSize(650, 143);
		feedbackListScrollPane.setLocation(24, 115);
		feedbackListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackListScrollPane);

		JPanel feedbackSeparator = new GUIObjects().separatorPanel(270);
		feedbackPanel.add(feedbackSeparator);

		JLabel provideHeader = new GUIObjects().contentHeader("Review And Edit Feedback", 285);
		feedbackPanel.add(provideHeader);

		JLabel provideSubHeader = new GUIObjects().contentSubHeader("(for the paper you have selected above)", 305);
		feedbackPanel.add(provideSubHeader);

		JTextArea feedbackTextArea = new GUIObjects().contentTextArea(0);
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setBounds(24, 330, 650, 180);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackScrollPane);

		JPanel releaseButton = new GUIObjects().contentButton(555, 520);
		JLabel releaseLabel = new GUIObjects().contentButtonLabel("Release Feedback");
		releaseButton.add(releaseLabel);
		feedbackPanel.add(releaseButton);

		/*
		 * Creating the final panel and filling it with its elements using GUIObjects
		 */
		JPanel finalPanel = new GUIObjects().contentPanel();
		contentPanel.add(finalPanel, "name_95139545658000");

		JLabel finalTitle = new GUIObjects().contentTitle("Review Final Submissions");
		finalPanel.add(finalTitle);

		JLabel pastdeadlineHeader = new GUIObjects().contentHeader("Papers Past Deadline", 90);
		finalPanel.add(pastdeadlineHeader);

		DefaultListModel<SubmissionObject> deadlineModel = new DefaultListModel<SubmissionObject>();
		JList<SubmissionObject> deadlineList = new JList<SubmissionObject>(deadlineModel);
		deadlineList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane deadlineListScrollPane = new JScrollPane(deadlineList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		deadlineListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		deadlineListScrollPane.setBounds(24, 115, 650, 110);
		finalPanel.add(deadlineListScrollPane);

		JPanel openfinalpaperButton = new GUIObjects().contentButton(555, 235);
		JLabel openfinalpaperLabel = new GUIObjects().contentButtonLabel("Open Paper");
		openfinalpaperButton.add(openfinalpaperLabel);
		finalPanel.add(openfinalpaperButton);

		JPanel finalSeparator = new GUIObjects().separatorPanel(270);
		finalPanel.add(finalSeparator);

		JLabel finalcommentsHeader = new GUIObjects().contentHeader("Final Comments", 290);
		finalPanel.add(finalcommentsHeader);

		JTextArea commentsTextArea = new GUIObjects().contentTextArea(0);
		JScrollPane commentsTextAreaScrollPane = new JScrollPane(commentsTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		commentsTextAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		commentsTextAreaScrollPane.setBounds(24, 325, 650, 180);
		finalPanel.add(commentsTextAreaScrollPane);

		JPanel extendButton = new GUIObjects().contentButton(295, 520);
		JLabel extendLabel = new GUIObjects().contentButtonLabel("Extend Deadline");
		extendButton.add(extendLabel);
		finalPanel.add(extendButton);

		JPanel finalapproveButton = new GUIObjects().contentButton(425, 520);
		JLabel finalapproveLabel = new GUIObjects().contentButtonLabel("Approve");
		finalapproveButton.add(finalapproveLabel);
		finalPanel.add(finalapproveButton);

		JPanel finalrejectButton = new GUIObjects().contentButton(555, 520);
		JLabel finalrejectLabel = new GUIObjects().contentButtonLabel("Reject");
		finalrejectButton.add(finalrejectLabel);
		finalPanel.add(finalrejectButton);

		/*
		 * Creating menu buttons to switch to different content panels
		 */
		JPanel submissionsMenuButton = new GUIObjects().menuButton(80, 30);
		JLabel submissionsMenuLabel = new GUIObjects().menuLabel("Review Submissions", 0);
		submissionsMenuButton.add(submissionsMenuLabel);
		menuPanel.add(submissionsMenuButton);

		JPanel verifyMenuButton = new GUIObjects().menuButton(115, 30);
		JLabel verifyMenuLabel = new GUIObjects().menuLabel("Verify Reviewers", 0);
		verifyMenuButton.add(verifyMenuLabel);
		menuPanel.add(verifyMenuButton);

		JPanel assignMenuButton = new GUIObjects().menuButton(150, 30);
		JLabel assignMenuLabel = new GUIObjects().menuLabel("Assign Reviewers", 0);
		assignMenuButton.add(assignMenuLabel);
		menuPanel.add(assignMenuButton);

		JPanel feedbackMenuButton = new GUIObjects().menuButton(185, 30);
		JLabel feedbackMenuLabel = new GUIObjects().menuLabel("Review Feedback", 0);
		feedbackMenuButton.add(feedbackMenuLabel);
		menuPanel.add(feedbackMenuButton);

		JPanel finalMenuButton = new GUIObjects().menuButton(220, 60);
		JLabel final1MenuLabel = new GUIObjects().menuLabel("Review", 0);
		JLabel final2MenuLabel = new GUIObjects().menuLabel("Final Submissions", 30);
		final1MenuLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		final2MenuLabel.setVerticalAlignment(SwingConstants.TOP);
		finalMenuButton.add(final1MenuLabel);
		finalMenuButton.add(final2MenuLabel);
		menuPanel.add(finalMenuButton);

		JPanel logoutMenuButton = new GUIObjects().menuButton(380, 30);
		JLabel logoutMenuLabel = new GUIObjects().menuLabel("Logout", 0);
		logoutMenuButton.add(logoutMenuLabel);
		menuPanel.add(logoutMenuButton);

		/*
		 * Menu button logic follows for each menu button. Each one switches to their
		 * respective card/panel containing all the GUI elements.
		 */
		// MouseListener for new submissions tab that opens submission panel and
		// refreshes for new submissions
		submissionsMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submissionsMenuButton.setBackground(new Color(255, 219, 5));
				submissionsMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				submissionsMenuButton.setBackground(new Color(0, 124, 65));
				submissionsMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				contentPanel.add(submissionsPanel);
				contentPanel.repaint();
				contentPanel.revalidate();

				// refreshes new submissions when its panel is opened
				getSubmissions(NEW_SUBMISSION_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);
				newSubmissionModel.clear();
				for (int i = 0; i < newSubmissions.length; i++) {
					if (newSubmissions[i] != null)
						newSubmissionModel.addElement(newSubmissions[i]);
				}

			}
		});

		// MouseListener for verify new reviewers tab
		verifyMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				verifyMenuButton.setBackground(new Color(255, 219, 5));
				verifyMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				verifyMenuButton.setBackground(new Color(0, 124, 65));
				verifyMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				applicantModel.clear();
				getApplicants();
				// populate new applicants list
				for (int i = 0; i < applicants.length; i++) {
					if (applicants[i] != null)
						applicantModel.addElement(applicants[i]);
				}
				contentPanel.add(verifyPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		// MouseListener for assigning reviewers tab that refreshes submissions in that
		// stage
		assignMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				assignMenuButton.setBackground(new Color(255, 219, 5));
				assignMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				assignMenuButton.setBackground(new Color(0, 124, 65));
				assignMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				reviewerModel.clear();
				assignpaperModel.clear();
				// get submissions with stage = 2
				getSubmissions(APPROVED_SUBMISSION_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);
				getReviewers();

				for (int i = 0; i < newSubmissions.length; i++) {
					if (newSubmissions[i] != null)
						assignpaperModel.addElement(newSubmissions[i]);
				}
				for (int i = 0; i < reviewers.length; i++) {
					if (reviewers[i] != null)
						reviewerModel.addElement(reviewers[i]);
				}
				contentPanel.add(assignPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		// MouseListener for review feedback tab that refreshes for new feedback when
		// clicked
		feedbackMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				feedbackMenuButton.setBackground(new Color(255, 219, 5));
				feedbackMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				feedbackMenuButton.setBackground(new Color(0, 124, 65));
				feedbackMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				paperModel.clear();
				newSubmissions = getFeedback();

				for (int i = 0; i < newSubmissions.length; i++) {
					if (newSubmissions[i] != null)
						paperModel.addElement(newSubmissions[i]);
				}

				contentPanel.add(feedbackPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		// MouseListener for final submissions tab
		finalMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				finalMenuButton.setBackground(new Color(255, 219, 5));
				final1MenuLabel.setForeground(Color.BLACK);
				final2MenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				finalMenuButton.setBackground(new Color(0, 124, 65));
				final1MenuLabel.setForeground(Color.WHITE);
				final2MenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				getSubmissions(RESUBMIT_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);

				deadlineModel.clear();

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date today = new Date();
				try {
					for (int i = 0; i < newSubmissions.length; i++) {
						if (newSubmissions[i] != null) {
							Date deadline;
							deadline = format.parse(newSubmissions[i].submissionDeadline);
							if (today.after(deadline))
								deadlineModel.addElement(newSubmissions[i]);
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				contentPanel.add(finalPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		// Logout button mouselistener
		logoutMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				logoutMenuButton.setBackground(new Color(255, 219, 5));
				logoutMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				logoutMenuButton.setBackground(new Color(0, 124, 65));
				logoutMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
				Login.main(null);
			}
		});

		// NEW SUBMISSIONS LIST LOGIC
		submissionsList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// gets selected indices in the list to validate if only one is selected at a
				// time
				selectedPaper = submissionsList.getSelectedIndices();

				if (selectedPaper.length == 1) {

					// index of selected paper
					int paperIndex = selectedPaper[0];

					// shows details for selected paper
					titleSubLabel.setText(newSubmissions[paperIndex].submissionName);
					authorsSubLabel.setText(newSubmissions[paperIndex].submissionAuthors);
					subjectSubLabel.setText(newSubmissions[paperIndex].subject);

					// Checks if any preferred reviewers were specified and displays a message if
					// none
					if (newSubmissions[paperIndex].preferredReviewerIDs == null)
						preferredSubLabel.setText("No Preferred Reviewers");
					else
						preferredSubLabel.setText(newSubmissions[paperIndex].preferredReviewerNames);

				} else if (selectedPaper.length >= 2) {

					// Show an error message if more than 2 papers are selected
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					submissionsList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (selectedPaper.length == 0) {

					// clear details section if no paper is selected
					titleSubLabel.setText("");
					authorsSubLabel.setText("");
					subjectSubLabel.setText("");
					preferredSubLabel.setText("");
				}
			}
		});

		// NEW SUBMISSIONS OPEN PAPER BUTTON LOGIC
		opennewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				opennewButton.setBackground(new Color(255, 219, 5));
				opennewLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				opennewButton.setBackground(new Color(0, 124, 65));
				opennewLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {

					// gets specific paper selected as an object for easy retrieval of details
					SubmissionObject paperOfInterest = newSubmissionModel.getElementAt(selectedPaper[0]);

					// final string containing file location
					String fileLocation = "submissions/" + paperOfInterest.submissionUserID + "/"
							+ paperOfInterest.filename;

					File paperToOpen = new File(fileLocation);

					try {
						Desktop.getDesktop().open(paperToOpen);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});

		// APPROVE NEW SUBMISSION BUTTON LOGIC
		// When button is clicked, the selected paper is deleted from the list,
		// and its submissionStage in SQL database is changed, as well as its deadline
		approveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				approveButton.setBackground(new Color(255, 219, 5));
				approveLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				approveButton.setBackground(new Color(0, 124, 65));
				approveLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// boolean to check if entered deadline date is valid
				boolean validDate = false;

				// get user input date
				String submissionDeadline = deadlineTextArea.getText();

				// set format for date
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				format.setLenient(false);
				// get current date to check against
				Date today = new Date();

				try {
					// check if entered date is before current date
					if (format.parse(submissionDeadline).after(today))
						validDate = true;
				} catch (ParseException e) {
					validDate = false;
				}
				// either improper format or date before today sets validDate to false, which
				// then shows an error message

				if (selectedPaper.length == 1 && validDate) {

					// gets index of selected paper in list
					int paperIndex = selectedPaper[0];
					SubmissionObject paperOfInterest = newSubmissionModel.getElementAt(paperIndex);

					// get selected paper's submissionID
					int submissionID = newSubmissions[paperIndex].submissionID;

					// send SQL update
					approveSubmission(submissionID, submissionDeadline);

					// refresh new submissions list after approving
					getSubmissions(NEW_SUBMISSION_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						if (newSubmissions[i] != null)
							newSubmissionModel.addElement(newSubmissions[i]);
					}

					String to = emailName(paperOfInterest.userEmail);
					String subject = "Journal Submission System - Paper Approved";
					String body = "Dear " + to
							+ ",\n\nA new paper you have submitted has been approved to be considered for the journal.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(paperOfInterest.userEmail, subject, body);

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Author notified via email that their paper was approved.", "Submission Approved",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (selectedPaper.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected to approve.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (!validDate) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null,
							"Please enter a valid date in the format YYYY-MM-DD, after today.", "Invalid deadline",
							JOptionPane.PLAIN_MESSAGE, null);

				}
			}

			/**
			 * Sends an sql update instruction to change submissionStage and
			 * submissionDeadline
			 * 
			 * @param submissionID
			 * @param submissionDeadline
			 */
			private void approveSubmission(int submissionID, String submissionDeadline) {
				PreparedStatement ps;
				String query = "update submission set submissionStage = 2, submissionDeadline = ? where submissionID = ?";

				try {
					ps = conn.prepareStatement(query);
					ps.setString(1, submissionDeadline);
					ps.setInt(2, submissionID);

					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		// REJECT NEW SUBMISSION BUTTON LOGIC
		rejectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				rejectButton.setBackground(new Color(255, 219, 5));
				rejectLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				rejectButton.setBackground(new Color(0, 124, 65));
				rejectLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// check if only one paper selected
				if (selectedPaper.length == 1) {

					// get index of selected paper
					int paperIndex = selectedPaper[0];
					SubmissionObject paperOfInterest = newSubmissionModel.getElementAt(paperIndex);

					// get selected paper's unique ID
					int submissionID = newSubmissions[paperIndex].submissionID;

					// set submissionStage to rejected stage
					setSubmissionStage(submissionID, REJECTED_SUBMISSION_STAGE);

					// refresh list of new submissions after rejection
					getSubmissions(1);
					newSubmissions = populateSubmissions(newSubmissions);
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						if (newSubmissions[i] != null)
							newSubmissionModel.addElement(newSubmissions[i]);
					}

					String to = emailName(paperOfInterest.userEmail);
					String subject = "Journal Submission System - Paper Rejected";
					String body = "Dear " + to
							+ ",\n\nUnfortunately a new paper you have submitted has been rejected.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(paperOfInterest.userEmail, subject, body);

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Author notified that their paper was rejected.", "Submission Rejected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected to reject.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});

		// Applicant list listener
		// checks if only one applicant selected
		applicantList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// get indices of selected papers
				selectedApplicant = applicantList.getSelectedIndices();

				if (selectedApplicant.length == 1) {
					int selected = selectedApplicant[0];

					// populates detail text labels for selected applicant
					occupationSubLabel.setText(applicants[selected].occupation);
					organizationSubLabel.setText(applicants[selected].organization);
					researchareaSubLabel.setText(applicants[selected].researchArea);

				} else if (selectedApplicant.length >= 2) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Applicant",
							"Too Many Applicants Selected", JOptionPane.PLAIN_MESSAGE, null);
					applicantList.clearSelection();
				} else {
					occupationSubLabel.setText("");
					organizationSubLabel.setText("");
					researchareaSubLabel.setText("");

				}

			}
		});

		// APPROVE NEW REVIEWER APPLICATION BUTTON
		approveappButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				approveappButton.setBackground(new Color(255, 219, 5));
				approveappLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				approveappButton.setBackground(new Color(0, 124, 65));
				approveappLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedApplicant.length == 1) {
					int selectedApplicantIndex = selectedApplicant[0];
					int applicantID = applicants[selectedApplicantIndex].userID;
					String applicantEmail = applicants[selectedApplicantIndex].username;

					approveApplicant(applicantID);

					// update applicants list
					applicantModel.clear();
					getApplicants();
					for (int i = 0; i < applicants.length; i++) {
						if (applicants[i] != null)
							applicantModel.addElement(applicants[i]);
					}

					String to = emailName(applicantEmail);
					String subject = "Journal Submission System - Reviewer Application Approved";
					String body = "Dear " + to + ",\n\nWe are happy to inform you that your application"
							+ " to become a University of Alberta reviewer has been accepted!\n\n"
							+ "You may now login with your submitted email and password.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(applicantEmail, subject, body);

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "New reviewer approved and notified via email.", "Applicant Approved",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please select one applicant",
							"No or multiple applicants selected", JOptionPane.PLAIN_MESSAGE, null);

				}
			}
		});

		// REJECT NEW REVIEWER APPLICATION BUTTON
		rejectappButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				rejectappButton.setBackground(new Color(255, 219, 5));
				rejectappLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				rejectappButton.setBackground(new Color(0, 124, 65));
				rejectappLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedApplicant.length == 1) {
					int selectedApplicantIndex = selectedApplicant[0];
					int applicantID = applicants[selectedApplicantIndex].userID;
					String applicantEmail = applicants[selectedApplicantIndex].username;

					rejectApplicant(applicantID);

					// update applicants list
					applicantModel.clear();
					getApplicants();
					for (int i = 0; i < applicants.length; i++) {
						if (applicants[i] != null)
							applicantModel.addElement(applicants[i]);
					}

					String to = emailName(applicantEmail);
					String subject = "Journal Submission System - Reviewer Application Rejected";
					String body = "Dear " + to + ",\n\nWe are sorry to inform you that your application"
							+ " to become a University of Alberta reviewer has been rejected.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(applicantEmail, subject, body);
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "New reviewer rejected and notified via email.", "Applicant Rejected",
							JOptionPane.PLAIN_MESSAGE, null);

				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please select one applicant",
							"No or multiple applicants selected", JOptionPane.PLAIN_MESSAGE, null);

				}
			}
		});

		// assign reviewers paper list logic
		assignpaperList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				selected = assignpaperList.getSelectedIndices();
				if (selected.length == 1) {

					// no details to show for a selected paper in this screen

				} else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					assignpaperList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}

		});

		// Get all reviewers
		allSortButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				allSortButton.setBackground(new Color(255, 219, 5));
				allSortButtonLabel.setForeground(Color.BLACK);

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				allSortButton.setBackground(new Color(0, 124, 65));

				allSortButtonLabel.setForeground(Color.WHITE);

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				// just clear and add all reviewers to the list
				reviewerModel.clear();
				for (int i = 0; i < reviewers.length; i++) {
					if (reviewers[i] != null)
						reviewerModel.addElement(reviewers[i]);
				}

			}
		});

		// SORT REVIEWERS BY REVIEWER NOMINATED BUTTON
		selfSortButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				selfSortButton.setBackground(new Color(255, 219, 5));
				selfSortButtonLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				selfSortButton.setBackground(new Color(0, 124, 65));
				selfSortButtonLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selected.length > 0) {

					// first clear the list
					reviewerModel.clear();

					// get the selected submission
					int selectedIndex = selected[0];
					SubmissionObject selectedSubmission = assignpaperModel.getElementAt(selectedIndex);

					// array of ints for reviewer IDs
					int[] nominatedIDs = new int[100];

					if (selectedSubmission.nominatedReviewerIDs != null) {

						// Split the list of preferred reviewer IDs by comma
						String[] nominated = selectedSubmission.nominatedReviewerIDs.split("[,]");

						// store those split strings as integers
						for (int i = 0; i < nominated.length; i++) {
							nominatedIDs[i] = Integer.parseInt(nominated[i]);
						}
					}

					// Populate the reviewer list by preferred reviewer IDs
					for (int i = 0; i < nominatedIDs.length; i++) {
						if (nominatedIDs[i] != 0) {
							reviewerModel.addElement(reviewerIDtoObject.get(nominatedIDs[i]));
						}
					}
				}
			}
		});

		// SORT REVIEWERS BY AUTHOR NOMINATED BUTTON
		authorSortButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				authorSortButton.setBackground(new Color(255, 219, 5));
				authorSortButtonLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				authorSortButton.setBackground(new Color(0, 124, 65));
				authorSortButtonLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selected.length > 0) {

					// first clear the list
					reviewerModel.clear();

					// get the selected submission
					int selectedIndex = selected[0];
					SubmissionObject selectedSubmission = assignpaperModel.getElementAt(selectedIndex);

					// array of ints for reviewer IDs
					int[] preferredReviewerIDs = new int[100];

					if (selectedSubmission.preferredReviewerIDs != null) {

						// Split the list of preferred reviewer IDs by comma
						String[] nominated = selectedSubmission.preferredReviewerIDs.split("[,]");

						// store those split strings as integers
						for (int i = 0; i < nominated.length; i++) {
							preferredReviewerIDs[i] = Integer.parseInt(nominated[i]);
						}
					}

					// Populate the reviewer list by preferred reviewer IDs
					for (int i = 0; i < preferredReviewerIDs.length; i++) {
						if (preferredReviewerIDs[i] != 0)
							reviewerModel.addElement(reviewerIDtoObject.get(preferredReviewerIDs[i]));
					}
				}

			}
		});

		// ASSIGN REVIEWER BUTTON
		assignreviewerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				assignreviewerButton.setBackground(new Color(255, 219, 5));
				assignreviewerLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				assignreviewerButton.setBackground(new Color(0, 124, 65));
				assignreviewerLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// checks that only one paper is selected
				if (selected.length == 1) {

					// get selected paper as an object
					SubmissionObject paperOfInterest = assignpaperModel.getElementAt(selected[0]);

					// Up to 20 reviewers can be selected
					ReviewerObject[] selectedReviewers = new ReviewerObject[20];

					// get the indices of reviewers selected
					int[] reviewerIndices = reviewerList.getSelectedIndices();

					// check if at least one reviewer is selected
					if (reviewerIndices.length > 0) {

						// iterates over selected reviewers' indices and adds each selected reviewer to
						// the array of reviewer objects, selectedReviewers
						for (int i = 0; i < reviewerIndices.length; i++) {
							selectedReviewers[i] = new ReviewerObject();
							selectedReviewers[i] = reviewerModel.getElementAt(reviewerIndices[i]);
						}

						// StringBuilder to build the string that will be sent to the SQL database
						// Will be of the form ID1,ID2,ID3, etc. ending with a reviewer ID and not a
						// comma
						StringBuilder reviewerIDs = new StringBuilder();
						for (int i = 0; i < selectedReviewers.length; i++) {
							if (selectedReviewers[i] != null) {
								reviewerIDs.append(selectedReviewers[i].userID + ",");
							}
						}
						reviewerIDs.setLength(reviewerIDs.length() - 1);

						// send SQL update
						assignReviewers(paperOfInterest.submissionID, reviewerIDs.toString());

						String subject = "Journal Submission System - New Paper To Review";

						for (int i = 0; i < selectedReviewers.length; i++) {

							if (selectedReviewers[i] != null) {
								String to = emailName(selectedReviewers[i].username);
								String body = "Dear " + to + ",\n\nThere is a new paper for you to review!\n\n"
										+ "Sincerely,\n\nThe University of Alberta";
								sendEmail(selectedReviewers[i].username, subject, body);
							}
						}

						// refresh list of papers that need reviewers assigned

						assignpaperModel.clear();
						getSubmissions(APPROVED_SUBMISSION_STAGE);
						newSubmissions = populateSubmissions(newSubmissions);
						for (int i = 0; i < newSubmissions.length; i++) {
							if (newSubmissions[i] != null)
								assignpaperModel.addElement(newSubmissions[i]);
						}
						
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);
						JOptionPane.showMessageDialog(null, "Reviewers assigned and notified via email.", "Reviewers Assigned",
								JOptionPane.PLAIN_MESSAGE, null);
					}
				} else if (selected.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					feedbackList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}
		});

		// REVIEW FEEDBACK LIST LOGIC
		feedbackList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// Clear the textarea to prepare for new feedback
				feedbackTextArea.setText("");
				fbAreaSelected = feedbackList.getSelectedIndices();

				// If no paper is selected, removes the release feedback button
				if (fbAreaSelected.length == 0)
					releaseButton.setVisible(false);

				// Adds it back if a paper is selected
				else if (fbAreaSelected.length == 1) {
					releaseButton.setVisible(true);

					// get selected paper as an object for easy retrieval of details
					SubmissionObject paperInDetail = newSubmissions[fbAreaSelected[0]];
					
					// feedback file directory + filename
					String feedbackFile = "submissions/" + paperInDetail.submissionUserID + "/feedback/"
							+ paperInDetail.filename.split("\\.")[0] + ".txt";

					// opening and appending feedback file to the textarea
					Scanner feedback;

					try {

						feedback = new Scanner(new File(feedbackFile));

						while (feedback.hasNext()) {

							feedbackTextArea.append(feedback.nextLine());
							feedbackTextArea.append("\n");
						}

						feedbackTextArea.setCaretPosition(0);
						feedback.close();

					} catch (FileNotFoundException e) {
					}

				} else if (fbAreaSelected.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					feedbackList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}

		});

		// RELEASE FEEDBACK BUTTON
		releaseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				releaseButton.setBackground(new Color(255, 219, 5));
				releaseLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				releaseButton.setBackground(new Color(0, 124, 65));
				releaseLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (fbAreaSelected.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (fbAreaSelected.length == 1) {
					if (!feedbackTextArea.getText().equals("")) {

						SubmissionObject paperOfInterest = paperModel.getElementAt(fbAreaSelected[0]);

						String papername = paperOfInterest.submissionName;

						// gets filename of paper
						String filename = paperOfInterest.filename;

						// get filepath of project folder
						File f = new File(filename);
						StringBuilder filepath = new StringBuilder(f.getAbsolutePath());
						filepath.setLength(filepath.length() - filename.length());

						// replaces instances of "\" with "\\" (java requires this)
						String separator = "\\";
						String[] intermediate = filepath.toString().replaceAll(Pattern.quote(separator), "\\\\")
								.split("\\\\");

						// builds new filepath string, inserting / instead of \
						filepath = new StringBuilder(intermediate[0] + "/");
						for (int i = 1; i < intermediate.length; i++) {
							filepath.append(intermediate[i] + "/");
						}

						// final string containing file location
						String fileLocation = filepath.toString() + "submissions/" + paperOfInterest.submissionUserID
								+ "/feedback/" + papername + ".txt";

						// replacing original feedback text with new feedback text
						try {
							FileWriter fw = new FileWriter(fileLocation, false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter pw = new PrintWriter(bw);
							pw.println(feedbackTextArea.getText());
							pw.close();

						} catch (Exception e) {
							e.printStackTrace();
						}
						// example final string format
						// file:///D:/Documents/GitHub/seng300-group20-project/submissions/3/Example%20paper2.pdf

						// Updates submission stage in database
						approveFeedback(paperOfInterest.submissionID);

						String to = emailName(paperOfInterest.userEmail);
						String subject = "Journal Submission System - Feedback Available";
						String body = "Dear " + to + ",\n\nThere is a feedback for a paper you submitted!\n\n"
								+ "Sincerely,\n\nThe University of Alberta";

						sendEmail(paperOfInterest.userEmail, subject, body);
						JOptionPane.showMessageDialog(null, "Feedback approved!\n Available for author's viewing.",
								"Feedback Approved", JOptionPane.PLAIN_MESSAGE, null);

						// updates feedback area
						paperModel.clear();
						newSubmissions = getFeedback();

						for (int i = 0; i < newSubmissions.length; i++) {
							if (newSubmissions[i] != null)
								paperModel.addElement(newSubmissions[i]);
						}
						
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);
						JOptionPane.showMessageDialog(null, "Author notified via email that they have feedback.", "Feedback Released",
								JOptionPane.PLAIN_MESSAGE, null);

					} else {

						JOptionPane.showMessageDialog(null, "Please ensure the feedback field is not blank",
								"Missing Feedback Details", JOptionPane.PLAIN_MESSAGE, null);

					}

				}

				// Add submit logic that grabs from feedbackTextArea and appends to a file
			}
		});

		// FINAL SUBMISSIONS LIST LOGIC
		deadlineList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// gets selected indices in the list to validate if only one is selected at a
				// time
				selectedPaper = deadlineList.getSelectedIndices();

				if (selectedPaper.length == 1) {

				} else if (selectedPaper.length >= 2) {

					// Show an error message if more than 2 papers are selected
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					submissionsList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});

		// OPEN PAPER BUTTON IN FINAL SUBMISSIONS
		openfinalpaperButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				openfinalpaperButton.setBackground(new Color(255, 219, 5));
				openfinalpaperLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				openfinalpaperButton.setBackground(new Color(0, 124, 65));
				openfinalpaperLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {

					// gets specific paper selected as an object for easy retrieval of details
					SubmissionObject paperOfInterest = deadlineModel.getElementAt(selectedPaper[0]);

					// final string containing file location
					String fileLocation = "submissions/" + paperOfInterest.submissionUserID + "/"
							+ paperOfInterest.filename;

					File paperToOpen = new File(fileLocation);

					try {
						Desktop.getDesktop().open(paperToOpen);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// example final string format
					// file:///D:/Documents/GitHub/seng300-group20-project/submissions/3/Example%20paper2.pdf
				}

			}
		});

		// APPROVE BUTTON FINAL SUBMISSION
		finalapproveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				finalapproveButton.setBackground(new Color(255, 219, 5));
				finalapproveLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				finalapproveButton.setBackground(new Color(0, 124, 65));
				finalapproveLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 1) {
					int selectedIndex = selectedPaper[0];

					SubmissionObject paperOfInterest = deadlineModel.getElementAt(selectedIndex);

					int ID = paperOfInterest.submissionID;

					// Change submission stage to approved
					setSubmissionStage(ID, FINAL_APPROVED_STAGE);

					// move submission file to approved submissions folder
					File paperFile = new File(
							"submissions/" + paperOfInterest.submissionUserID + "/" + paperOfInterest.filename);
					Path source = Paths.get(paperFile.getAbsolutePath());
					Path dest = Paths.get("submissions/acceptedsubmissions/" + paperOfInterest.submissionUserID
							+ paperOfInterest.filename);
					try {
						Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
					}

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();

					String to = emailName(paperOfInterest.userEmail);
					String subject = "Journal Submission System - Final Paper Approved";
					String body = "Dear " + to
							+ ",\n\nA paper you have submitted has been approved to be published in the journal!\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(paperOfInterest.userEmail, subject, body);

					deadlineModel.clear();

					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							if (newSubmissions[i] != null) {
								deadline = format.parse(newSubmissions[i].submissionDeadline);
								if (today.after(deadline))
									deadlineModel.addElement(newSubmissions[i]);
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Author notified via email of final approval", "Final Paper Approved",
							JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});

		// REJECT BUTTON FINAL SUBMISSION
		finalrejectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				finalrejectButton.setBackground(new Color(255, 219, 5));
				finalrejectLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				finalrejectButton.setBackground(new Color(0, 124, 65));
				finalrejectLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 1) {
					int selectedIndex = selectedPaper[0];
					int ID = deadlineModel.getElementAt(selectedIndex).submissionID;
					SubmissionObject paperOfInterest = deadlineModel.getElementAt(selectedIndex);

					setSubmissionStage(ID, REJECTED_SUBMISSION_STAGE);

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();

					String to = emailName(paperOfInterest.userEmail);
					String subject = "Journal Submission System - Final Paper Rejected";
					String body = "Dear " + to + ",\n\nUnfortunately a paper you have submitted has been rejected.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(paperOfInterest.userEmail, subject, body);

					deadlineModel.clear();

					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							if (newSubmissions[i] != null) {
								deadline = format.parse(newSubmissions[i].submissionDeadline);
								if (today.after(deadline))
									deadlineModel.addElement(newSubmissions[i]);
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Author notified via email of final rejection", "Final Paper Rejected",
							JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});

		// Extend deadline button
		extendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				extendButton.setBackground(new Color(255, 219, 5));
				extendLabel.setForeground(Color.BLACK);
				extendLabel.setText("Add One Month");
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				extendButton.setBackground(new Color(0, 124, 65));
				extendLabel.setForeground(Color.WHITE);
				extendLabel.setText("Extend Deadline");
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 1) {
					// add 1 month to deadline
					int selectedIndex = selectedPaper[0];
					int submissionID = deadlineModel.getElementAt(selectedIndex).submissionID;
					SubmissionObject paperOfInterest = deadlineModel.getElementAt(selectedIndex);

					String dl = deadlineModel.getElementAt(selectedIndex).submissionDeadline;
					String[] oldDl = dl.split("[-]");
					LocalDate oldDeadline = LocalDate.of(Integer.parseInt(oldDl[0]), Integer.parseInt(oldDl[1]) + 1,
							Integer.parseInt(oldDl[2]));
					oldDeadline.plusMonths(1);

					setDeadline(submissionID, oldDeadline.toString());

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();

					String to = emailName(paperOfInterest.userEmail);
					String subject = "Journal Submission System - Deadline Extended";
					String body = "Dear " + to
							+ ",\n\nA paper you submitted has had its deadline extended one month.\n\n"
							+ "Sincerely,\n\nThe University of Alberta";

					sendEmail(paperOfInterest.userEmail, subject, body);

					deadlineModel.clear();

					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							if (newSubmissions[i] != null) {
								deadline = format.parse(newSubmissions[i].submissionDeadline);
								if (today.after(deadline))
									deadlineModel.addElement(newSubmissions[i]);
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Author notified via email of extension", "Deadline Extended",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}
		});

	}

	/**
	 * Gets submissions with unapproved feedback
	 * 
	 * @return poulateSubmissions A list of submission objects based on toReturn
	 */
	protected SubmissionObject[] getFeedback() {
		SubmissionObject[] toReturn = new SubmissionObject[100];

		PreparedStatement ps;

		String query = "SELECT * FROM submission WHERE feedbackReceived = 1 AND submissionStage = 3";

		try {
			ps = conn.prepareStatement(query);

			submissionSet = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return populateSubmissions(toReturn);

	}

	/**
	 * Gets user's submissions from an sql query and stores in a global ResultSet
	 * 
	 * @param stage What stage the submission currently is in
	 */
	private void getSubmissions(int stage) {
		PreparedStatement ps;

		String query = "SELECT * FROM submission WHERE submissionStage  = ?";

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, stage);

			submissionSet = ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Failure searching for user submissions");
		}
	}

	/**
	 * Populates a global array of SubmissionObjects to store results of initial SQL
	 * query for user submissions, eliminating the need to constantly query SQL
	 * database
	 * 
	 * @param submissions A list of submission objects that need to be populated
	 * @return submissions The new list with the correct information
	 */
	private SubmissionObject[] populateSubmissions(SubmissionObject[] submissions) {

		try {

			submissions = new SubmissionObject[200];

			int i = 0;
			while (submissionSet.next()) {
				int submissionID = submissionSet.getInt("submissionID");
				if (submissionID > 0) {
					String submissionName = submissionSet.getString("submissionName");
					String submissionAuthors = submissionSet.getString("submissionAuthors");
					String subject = submissionSet.getString("subject");
					int submissionStage = submissionSet.getInt("submissionStage");
					String filename = submissionSet.getString("filename");
					int submissionUserID = submissionSet.getInt("submissionUserID");
					String userEmail = submissionSet.getString("userEmail");

					submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject,
							submissionStage, filename, submissionUserID, userEmail);

					String submissionDeadline = submissionSet.getString("submissionDeadline");
					String reviewerIDs = submissionSet.getString("reviewerIDs");
					String preferredReviewerIDs = submissionSet.getString("preferredReviewerIDs");
					String nominatedReviewerIDs = submissionSet.getString("nominatedReviewerIDs");

					if (submissionDeadline == null)
						submissions[i].submissionDeadline = null;
					else
						submissions[i].submissionDeadline = submissionDeadline;

					if (reviewerIDs == null)
						submissions[i].reviewerIDs = null;
					else
						submissions[i].reviewerIDs = reviewerIDs;

					if (preferredReviewerIDs == null)
						submissions[i].preferredReviewerIDs = null;
					else
						submissions[i].preferredReviewerIDs = preferredReviewerIDs;

					if (nominatedReviewerIDs == null)
						submissions[i].nominatedReviewerIDs = null;
					else
						submissions[i].nominatedReviewerIDs = nominatedReviewerIDs;

					submissions[i].setReviewerNames();
				}
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return submissions;
	}

	/**
	 * Sets the submission stage in the database, stored as an int. The stages are
	 * defined in Constants.java
	 * 
	 * @param submissionID
	 * @param stage
	 */
	private void setSubmissionStage(int submissionID, int stage) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionStage = ? WHERE submissionID = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, stage);
			ps.setInt(2, submissionID);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populates global array of all approved reviewers
	 */
	private void getReviewers() {
		PreparedStatement ps;

		reviewers = new ReviewerObject[50];
		String query = "SELECT * FROM users WHERE userType = 2";
		try {
			ps = conn.prepareStatement(query);

			reviewerSet = ps.executeQuery();
			int count = 0;
			while (reviewerSet.next()) {
				int userID = reviewerSet.getInt("userID");
				if (userID > 0) {
					String username = reviewerSet.getString("username");
					String name = String.valueOf(username.charAt(0)).toUpperCase()
							+ username.substring(1).split("\\@")[0];
					int userType = 2;

					reviewers[count] = new ReviewerObject(userID, username, name, userType);
					reviewerIDtoObject.put(userID, reviewers[count]);
				}
				count++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Populates global array of reviewers not yet approved
	 */
	private void getApplicants() {
		PreparedStatement ps;

		applicants = new ReviewerObject[50];
		String query = "SELECT * FROM users WHERE userType = 3";
		try {
			ps = conn.prepareStatement(query);

			applicantSet = ps.executeQuery();
			int count = 0;
			while (applicantSet.next()) {
				int userID = applicantSet.getInt("userID");
				String username = applicantSet.getString("username");
				String name = String.valueOf(username.charAt(0)).toUpperCase() + username.substring(1).split("\\@")[0];
				int userType = 3;

				applicants[count] = new ReviewerObject(userID, username, name, userType);

				applicants[count].occupation = applicantSet.getString("occupation");
				applicants[count].organization = applicantSet.getString("organization");
				applicants[count].researchArea = applicantSet.getString("research");

				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Assigns reviewers by their userID to a submission
	 * 
	 * @param submissionID, The associated submissionID that reviewers are being assigned to
	 * @param reviewerIDs, The reviwer's ids that are being assigned
	 */
	private void assignReviewers(int submissionID, String reviewerIDs) {
		PreparedStatement ps;

		String query = "UPDATE submission SET reviewerIDs = ? , submissionStage = ? WHERE submissionID = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, reviewerIDs);
			ps.setInt(2, FEEDBACK_GATHERING_STAGE);
			ps.setInt(3, submissionID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Approves feedback on a submission, causing it to be released to the author
	 * 
	 * @param submissionID, The associated submissionID that reviewers are being assigned to
	 */
	private void approveFeedback(int submissionID) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionStage = ?, feedbackReceived = 0 WHERE submissionID = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, RESUBMIT_STAGE);
			ps.setInt(2, submissionID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Approves a reviewer who applied
	 * 
	 * @param userID, The id associated with the applicant reviewer
	 */
	private void approveApplicant(int userID) {
		PreparedStatement ps;

		String query = "UPDATE users SET usertype = ? WHERE userID = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, 2);
			ps.setInt(2, userID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Rejects a reviewer who applied
	 * 
	 * @param userID, The id associated with the applicant reviewer
	 */
	private void rejectApplicant(int userID) {

		PreparedStatement ps;

		String query = "UPDATE users SET usertype = ? WHERE userID = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, 4);
			ps.setInt(2, userID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sends an email to a specified account with a subject and body
	 * 
	 * @param to The address the email is being sent to
	 * @param subject The email's subject
	 * @param body The email's text contents
	 */
	private void sendEmail(String to, String subject, String body) {

		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		String from = "jss.ualberta";
		String pass = "UAlbertaJSS";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.mime.charset", "utf-8");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress toAddress = new InternetAddress(to);
			message.addRecipient(Message.RecipientType.TO, toAddress);
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

	/**
	 * Set a new deadline for submission
	 * 
	 * @param submissionID, The submissionID associated with the selected paper
	 * @param newDeadline, The deadline entered by the administrator
	 */
	private void setDeadline(int submissionID, String newDeadline) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionDeadline = ? WHERE submissionID = ?";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, newDeadline);
			ps.setInt(2, submissionID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * emailName returns a formatted string of the user's name before the @ symbol
	 * of their email address
	 * 
	 * @param recepient The recepient's email address
	 * 
	 * @return formattedUser The string of the user's nicely formatted name
	 */
	private String emailName(String recepient) {

		String removeProvider = recepient.split("\\@")[0];
		String[] nameArray = removeProvider.split("\\.");
		StringBuilder formattedUser = new StringBuilder("");
		for (int i = 0; i < nameArray.length; i++) {
			formattedUser.append(String.valueOf(nameArray[i].charAt(0)).toUpperCase() + nameArray[i].substring(1));
			if (i < nameArray.length - 1) {
				formattedUser.append(" ");
			}
		}

		return formattedUser.toString();
	}
}
