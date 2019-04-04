package seng300project;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;

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
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
	protected FeedbackObject[] feedback;
	protected ReviewerObject[] reviewers;
	protected ReviewerObject[] applicants;
	protected String[] subjects = new String[100];
	private int[] selectedPaper = new int[0];
	private int[] selected = new int[0];
	private int[] fbAreaSelected = new int[0];
	private int[] selectedApplicant = new int[0];
	private Map<Integer, ReviewerObject> reviewerIDtoObject = new HashMap<Integer, ReviewerObject>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin("Admin2", 9);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @param user
	 *            - User's name (not username)
	 * @param ID
	 *            - user's userID (invisible to user)
	 */
	public Admin(String user, int ID) {
		this.userID = ID;

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
		String userFolder = "submissions/" + userID;
		String userDetails = "submissions/" + userID + "/details";
		String userFeedback = "submissions/" + userID + "/feedback";
		String userFeedbackList = "submissions/" + userID + "/feedback_list.txt";
		String userSubmissionList = "submissions/" + userID + "/submission_list.txt";
		String acceptedSubmissionsList = "submissions/acceptedsubmissions/";
		File authorFolder = new File(userFolder);
		File detailsFolder = new File(userDetails);
		File feedbackFolder = new File(userFeedback);
		File feedbackListFile = new File(userFeedbackList);
		File submissionListFile = new File(userSubmissionList);
		File acceptedSubmissionsFolder = new File(acceptedSubmissionsList);

		// Checks if the required folders exist already, if not it creates them
		if (!acceptedSubmissionsFolder.exists())
			acceptedSubmissionsFolder.mkdirs();
		if (!authorFolder.exists()) {
			authorFolder.mkdirs();
		}
		if (!detailsFolder.exists()) {
			detailsFolder.mkdirs();
		}
		if (!feedbackFolder.exists()) {
			feedbackFolder.mkdirs();
		}
		if (!feedbackListFile.exists()) {
			try {
				feedbackListFile.createNewFile();
			} catch (IOException e) {

			}
		}
		if (!submissionListFile.exists()) {
			try {
				submissionListFile.createNewFile();
			} catch (IOException e) {

			}
		}

		// Nicely formated version of username
		String niceUsername = String.valueOf(user.charAt(0)).toUpperCase() + user.substring(1).split("\\@")[0];

		/*
		 * LOTS of GUI code follows that can be mostly ignored. In general, objects in
		 * the frame were created with certain boundaries and colours to create a
		 * cohesive feel. Buttons are implemented with jpanels instead of jbuttons
		 * simply because they did not appear properly with the colour scheme of the
		 * University of Alberta on Linux and MacOS. Most of the functionality is near
		 * the bottom of this class.
		 */
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(0, 124, 65));
		menuPanel.setBounds(0, 0, 180, 580);
		contentPane.add(menuPanel);
		menuPanel.setLayout(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(180, 0, 725, 580);
		contentPane.add(contentPanel);
		contentPanel.setLayout(new CardLayout(0, 0));

		JPanel welcomePanel = new JPanel();
		welcomePanel.setBackground(Color.WHITE);
		contentPanel.add(welcomePanel, "name_46926967448500");
		welcomePanel.setLayout(null);

		JLabel welcomeLabel = new JLabel("Welcome " + niceUsername + "!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
		welcomeLabel.setBounds(0, 124, 714, 40);
		welcomePanel.add(welcomeLabel);

		JLabel adminIcon = new JLabel("");
		Image authorIconImg = new ImageIcon(this.getClass().getResource("/admin.jpg")).getImage();
		adminIcon.setIcon(new ImageIcon(authorIconImg));
		adminIcon.setBounds(184, 212, 346, 200);
		welcomePanel.add(adminIcon);

		JPanel submissionsPanel = new JPanel();
		contentPanel.add(submissionsPanel, "name_35648043125700");
		submissionsPanel.setLayout(null);
		submissionsPanel.setBackground(Color.WHITE);

		JLabel submissionsTitleLabel = new JLabel("Review Submissions");
		submissionsTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		submissionsTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		submissionsTitleLabel.setBounds(24, 30, 325, 40);
		submissionsPanel.add(submissionsTitleLabel);

		JLabel newsubLabel = new JLabel("New Submissions");
		newsubLabel.setFont(new Font("Arial", Font.BOLD, 14));
		newsubLabel.setBounds(24, 90, 350, 18);
		submissionsPanel.add(newsubLabel);

		DefaultListModel<SubmissionObject> newSubmissionModel = new DefaultListModel<>();
		JList<SubmissionObject> submissionsList = new JList<SubmissionObject>(newSubmissionModel);

		submissionsList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane submissionsListScrollPane = new JScrollPane(submissionsList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		submissionsListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		submissionsListScrollPane.setBounds(24, 115, 650, 110);
		submissionsPanel.add(submissionsListScrollPane);

		JPanel opennewButton = new JPanel();
		JLabel opennewLabel = new JLabel("Open Paper");
		opennewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		opennewLabel.setForeground(Color.WHITE);
		opennewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		opennewLabel.setBounds(0, 0, 119, 30);
		opennewButton.add(opennewLabel);

		opennewButton.setLayout(null);
		opennewButton.setBackground(new Color(0, 124, 65));
		opennewButton.setBounds(555, 235, 119, 30);
		submissionsPanel.add(opennewButton);

		JPanel subSepPanel = new JPanel();
		subSepPanel.setBackground(Color.BLACK);
		subSepPanel.setBounds(24, 270, 650, 2);
		submissionsPanel.add(subSepPanel);

		JLabel detailsLabel = new JLabel("Submission Details");
		detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		detailsLabel.setBounds(24, 285, 350, 18);
		submissionsPanel.add(detailsLabel);

		JLabel detailsextraLabel = new JLabel("(select a paper above)");
		detailsextraLabel.setForeground(Color.DARK_GRAY);
		detailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		detailsextraLabel.setBounds(24, 305, 350, 18);
		submissionsPanel.add(detailsextraLabel);

		JLabel titleSubLabel = new JLabel("Title");
		titleSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		titleSubLabel.setBounds(24, 350, 325, 18);
		submissionsPanel.add(titleSubLabel);

		JLabel detailtitleSubLabel = new JLabel("");
		detailtitleSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailtitleSubLabel.setBounds(24, 380, 325, 18);
		submissionsPanel.add(detailtitleSubLabel);

		JLabel authorsSubLabel = new JLabel("Author");
		authorsSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		authorsSubLabel.setBounds(24, 410, 325, 18);
		submissionsPanel.add(authorsSubLabel);

		JLabel detailauthorsSubLabel = new JLabel("");
		detailauthorsSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailauthorsSubLabel.setBounds(24, 440, 325, 18);
		submissionsPanel.add(detailauthorsSubLabel);

		JLabel subjectSubLabel = new JLabel("Research Subject");
		subjectSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		subjectSubLabel.setBounds(359, 350, 315, 18);
		submissionsPanel.add(subjectSubLabel);

		JLabel detailsubjectSubLabel = new JLabel("");
		detailsubjectSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsubjectSubLabel.setBounds(359, 380, 315, 18);
		submissionsPanel.add(detailsubjectSubLabel);

		JLabel prefreviewersSubLabel = new JLabel("Preferred Reviewers");
		prefreviewersSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		prefreviewersSubLabel.setBounds(359, 410, 315, 18);
		submissionsPanel.add(prefreviewersSubLabel);

		JLabel detailprefreviewerSubLabel = new JLabel("");
		detailprefreviewerSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailprefreviewerSubLabel.setBounds(359, 440, 315, 18);
		submissionsPanel.add(detailprefreviewerSubLabel);

		JPanel detailsSepPanel = new JPanel();
		detailsSepPanel.setBackground(Color.BLACK);
		detailsSepPanel.setBounds(24, 470, 650, 2);
		submissionsPanel.add(detailsSepPanel);

		JLabel deadlineLabel = new JLabel("Set Deadline");
		deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
		deadlineLabel.setBounds(24, 485, 350, 18);
		submissionsPanel.add(deadlineLabel);

		JTextArea deadlineTextArea = new JTextArea();
		deadlineTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_TAB) {
					deadlineTextArea.transferFocus();
				}
				arg0.consume();
			}
		});
		deadlineTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		deadlineTextArea.setBounds(24, 510, 350, 30);
		submissionsPanel.add(deadlineTextArea);
		deadlineTextArea.append("YYYY-MM-DD");

		JPanel approveButton = new JPanel();
		JLabel approveLabel = new JLabel("Approve");
		approveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		approveLabel.setForeground(Color.WHITE);
		approveLabel.setFont(new Font("Arial", Font.BOLD, 12));
		approveLabel.setBounds(0, 0, 119, 30);
		approveButton.add(approveLabel);

		approveButton.setLayout(null);
		approveButton.setBackground(new Color(0, 124, 65));
		approveButton.setBounds(425, 520, 119, 30);
		submissionsPanel.add(approveButton);

		JPanel rejectButton = new JPanel();
		JLabel rejectLabel = new JLabel("Reject");
		rejectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rejectLabel.setForeground(Color.WHITE);
		rejectLabel.setFont(new Font("Arial", Font.BOLD, 12));
		rejectLabel.setBounds(0, 0, 119, 30);
		rejectButton.add(rejectLabel);

		rejectButton.setLayout(null);
		rejectButton.setBackground(new Color(0, 124, 65));
		rejectButton.setBounds(555, 520, 119, 30);
		submissionsPanel.add(rejectButton);

		JPanel verifyPanel = new JPanel();
		verifyPanel.setBackground(Color.WHITE);
		contentPanel.add(verifyPanel, "name_95100187161800");
		verifyPanel.setLayout(null);

		JLabel verifyTitleLabel = new JLabel("Verify Reviewers");
		verifyTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		verifyTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		verifyTitleLabel.setBounds(24, 30, 350, 40);
		verifyPanel.add(verifyTitleLabel);

		JLabel applicantsLabel = new JLabel("Applicants");
		applicantsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		applicantsLabel.setBounds(24, 90, 350, 18);
		verifyPanel.add(applicantsLabel);

		JScrollPane applicantListScrollPane = new JScrollPane();
		applicantListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		applicantListScrollPane.setBounds(24, 115, 650, 178);
		verifyPanel.add(applicantListScrollPane);

		DefaultListModel<ReviewerObject> applicantModel = new DefaultListModel<>();
		JList<ReviewerObject> applicantList = new JList<ReviewerObject>(applicantModel);

		applicantList.setFont(new Font("Arial", Font.PLAIN, 12));
		applicantListScrollPane.setViewportView(applicantList);

		JPanel verifySepPanel = new JPanel();
		verifySepPanel.setBackground(Color.BLACK);
		verifySepPanel.setBounds(24, 305, 650, 2);
		verifyPanel.add(verifySepPanel);

		JLabel appdetailsLabel = new JLabel("Applicant Details");
		appdetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		appdetailsLabel.setBounds(24, 320, 350, 18);
		verifyPanel.add(appdetailsLabel);

		JLabel appdetailsextraLabel = new JLabel("(select a new applicant above)");
		appdetailsextraLabel.setForeground(Color.DARK_GRAY);
		appdetailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		appdetailsextraLabel.setBounds(24, 340, 350, 18);
		verifyPanel.add(appdetailsextraLabel);

		JLabel occupationLabel = new JLabel("Occupation");
		occupationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		occupationLabel.setBounds(24, 385, 325, 18);
		verifyPanel.add(occupationLabel);

		JLabel detailsoccupationLabel = new JLabel("");
		detailsoccupationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsoccupationLabel.setBounds(24, 415, 325, 18);
		verifyPanel.add(detailsoccupationLabel);

		JLabel organizationLabel = new JLabel("Organization");
		organizationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		organizationLabel.setBounds(24, 445, 325, 18);
		verifyPanel.add(organizationLabel);

		JLabel detailsorganizationLabel = new JLabel("");
		detailsorganizationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsorganizationLabel.setBounds(24, 475, 325, 18);
		verifyPanel.add(detailsorganizationLabel);

		JLabel researchareaLabel = new JLabel("Research Area");
		researchareaLabel.setFont(new Font("Arial", Font.BOLD, 12));
		researchareaLabel.setBounds(359, 385, 315, 18);
		verifyPanel.add(researchareaLabel);

		JLabel detailsresearchareaLabel = new JLabel("");
		detailsresearchareaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsresearchareaLabel.setBounds(359, 415, 315, 18);
		verifyPanel.add(detailsresearchareaLabel);

		JPanel approveappButton = new JPanel();
		JLabel approveappLabel = new JLabel("Approve");
		approveappLabel.setHorizontalAlignment(SwingConstants.CENTER);
		approveappLabel.setForeground(Color.WHITE);
		approveappLabel.setFont(new Font("Arial", Font.BOLD, 12));
		approveappLabel.setBounds(0, 0, 119, 30);
		approveappButton.add(approveappLabel);

		approveappButton.setLayout(null);
		approveappButton.setBackground(new Color(0, 124, 65));
		approveappButton.setBounds(425, 520, 119, 30);
		verifyPanel.add(approveappButton);

		JPanel rejectappButton = new JPanel();
		JLabel rejectappLabel = new JLabel("Reject");
		rejectappLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rejectappLabel.setForeground(Color.WHITE);
		rejectappLabel.setFont(new Font("Arial", Font.BOLD, 12));
		rejectappLabel.setBounds(0, 0, 119, 30);
		rejectappButton.add(rejectappLabel);

		rejectappButton.setLayout(null);
		rejectappButton.setBackground(new Color(0, 124, 65));
		rejectappButton.setBounds(555, 520, 119, 30);
		verifyPanel.add(rejectappButton);

		JPanel assignPanel = new JPanel();
		assignPanel.setBackground(Color.WHITE);
		contentPanel.add(assignPanel, "name_100789627501200");
		assignPanel.setLayout(null);

		JLabel assignTitleLabel = new JLabel("Assign Reviewers");
		assignTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		assignTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		assignTitleLabel.setBounds(24, 30, 350, 40);
		assignPanel.add(assignTitleLabel);

		JLabel assignpaperLabel = new JLabel("Choose Paper To Assign Reviewers To");
		assignpaperLabel.setFont(new Font("Arial", Font.BOLD, 14));
		assignpaperLabel.setBounds(24, 90, 350, 18);
		assignPanel.add(assignpaperLabel);

		DefaultListModel<SubmissionObject> assignpaperModel = new DefaultListModel<SubmissionObject>();

		JScrollPane assignpaperListScrollPane = new JScrollPane();
		assignpaperListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		assignpaperListScrollPane.setBounds(24, 115, 650, 170);
		assignPanel.add(assignpaperListScrollPane);
		JList<SubmissionObject> assignpaperList = new JList<>(assignpaperModel);
		assignpaperListScrollPane.setViewportView(assignpaperList);

		

		JPanel assignSepPanel = new JPanel();
		assignSepPanel.setBackground(Color.BLACK);
		assignSepPanel.setBounds(24, 305, 650, 2);
		assignPanel.add(assignSepPanel);

		JLabel reviewersLabel = new JLabel("Reviewer List");
		reviewersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		reviewersLabel.setBounds(24, 320, 350, 18);
		assignPanel.add(reviewersLabel);

		DefaultListModel<ReviewerObject> reviewerModel = new DefaultListModel<ReviewerObject>();

		JPanel sortallButton = new JPanel();
		JLabel sortallLabel = new JLabel("All");
		sortallLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortallLabel.setForeground(Color.WHITE);
		sortallLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortallLabel.setBounds(0, 0, 70, 20);
		sortallButton.add(sortallLabel);

		sortallButton.setLayout(null);
		sortallButton.setBackground(new Color(0, 124, 65));
		sortallButton.setBounds(315, 320, 70, 20);
		assignPanel.add(sortallButton);

		JPanel sortselfButton = new JPanel();
		JLabel sortselfLabel = new JLabel("Self-Nominated");
		sortselfLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortselfLabel.setForeground(Color.WHITE);
		sortselfLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortselfLabel.setBounds(0, 0, 135, 20);
		sortselfButton.add(sortselfLabel);

		sortselfButton.setLayout(null);
		sortselfButton.setBackground(new Color(0, 124, 65));
		sortselfButton.setBounds(394, 320, 135, 20);
		assignPanel.add(sortselfButton);

		JPanel sortauthorButton = new JPanel();
		JLabel sortauthorLabel = new JLabel("Nominated By Author");
		sortauthorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortauthorLabel.setForeground(Color.WHITE);
		sortauthorLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortauthorLabel.setBounds(0, 0, 135, 20);
		sortauthorButton.add(sortauthorLabel);

		sortauthorButton.setLayout(null);
		sortauthorButton.setBackground(new Color(0, 124, 65));
		sortauthorButton.setBounds(539, 320, 135, 20);
		assignPanel.add(sortauthorButton);

		JScrollPane reviewerListScrollPane = new JScrollPane();
		reviewerListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		reviewerListScrollPane.setBounds(24, 345, 650, 170);
		assignPanel.add(reviewerListScrollPane);
		JList<ReviewerObject> reviewerList = new JList<>(reviewerModel);
		reviewerListScrollPane.setViewportView(reviewerList);

		JPanel assignreviewerButton = new JPanel();
		JLabel assignreviewerLabel = new JLabel("Assign");
		assignreviewerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		assignreviewerLabel.setForeground(Color.WHITE);
		assignreviewerLabel.setFont(new Font("Arial", Font.BOLD, 12));
		assignreviewerLabel.setBounds(0, 0, 119, 30);
		assignreviewerButton.add(assignreviewerLabel);

		assignreviewerButton.setLayout(null);
		assignreviewerButton.setBackground(new Color(0, 124, 65));
		assignreviewerButton.setBounds(555, 520, 119, 30);
		assignPanel.add(assignreviewerButton);

		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setBackground(Color.WHITE);
		contentPanel.add(feedbackPanel, "name_47558521480000");
		feedbackPanel.setLayout(null);

		JLabel feedbackTitleLabel = new JLabel("Review Feedback");
		feedbackTitleLabel.setBounds(24, 30, 350, 40);
		feedbackPanel.add(feedbackTitleLabel);
		feedbackTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JLabel reviewLabel = new JLabel("Papers With Feedback Waiting For Review");
		reviewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		reviewLabel.setBounds(24, 90, 350, 18);
		feedbackPanel.add(reviewLabel);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> feedbackList = new JList<SubmissionObject>(paperModel);

		feedbackList.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane feedbackListScrollPane = new JScrollPane(feedbackList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackListScrollPane.setSize(650, 143);
		feedbackListScrollPane.setLocation(24, 115);
		feedbackListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackListScrollPane);

		JPanel feedbackSepPanel = new JPanel();
		feedbackSepPanel.setBackground(Color.BLACK);
		feedbackSepPanel.setBounds(24, 270, 650, 2);
		feedbackPanel.add(feedbackSepPanel);

		JLabel provideLabel = new JLabel("Review And Edit Feedback");
		provideLabel.setFont(new Font("Arial", Font.BOLD, 14));
		provideLabel.setBounds(24, 285, 350, 18);
		feedbackPanel.add(provideLabel);

		JLabel provideextraLabel = new JLabel("(for the paper you have selected above)");
		provideextraLabel.setForeground(Color.DARK_GRAY);
		provideextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		provideextraLabel.setBounds(24, 305, 350, 18);
		feedbackPanel.add(provideextraLabel);

		JPanel releaseButton = new JPanel();
		JLabel releaseLabel = new JLabel("Release Feedback");
		releaseLabel.setForeground(Color.WHITE);
		releaseLabel.setFont(new Font("Arial", Font.BOLD, 12));
		releaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		releaseLabel.setBounds(0, 0, 119, 30);
		releaseButton.add(releaseLabel);
		releaseButton.setVisible(false);

		releaseButton.setBackground(new Color(0, 124, 65));
		releaseButton.setBounds(555, 520, 119, 30);
		feedbackPanel.add(releaseButton);
		releaseButton.setLayout(null);

		JTextArea feedbackTextArea = new JTextArea();
		feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setSize(650, 180);
		feedbackScrollPane.setLocation(24, 330);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackScrollPane);

		JPanel finalPanel = new JPanel();
		finalPanel.setBackground(Color.WHITE);
		contentPanel.add(finalPanel, "name_95139545658000");
		finalPanel.setLayout(null);

		JLabel finalTitleLabel = new JLabel("Review Final Submissions");
		finalTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		finalTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		finalTitleLabel.setBounds(24, 30, 350, 40);
		finalPanel.add(finalTitleLabel);

		JLabel pastdeadlineLabel = new JLabel("Papers Past Deadline");
		pastdeadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
		pastdeadlineLabel.setBounds(24, 90, 350, 18);
		finalPanel.add(pastdeadlineLabel);

		JScrollPane deadlineListScrollPane = new JScrollPane();
		deadlineListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		deadlineListScrollPane.setBounds(24, 115, 650, 110);
		finalPanel.add(deadlineListScrollPane);

		DefaultListModel<SubmissionObject> deadlineModel = new DefaultListModel<SubmissionObject>();
		JList<SubmissionObject> deadlineList = new JList<SubmissionObject>(deadlineModel);
		deadlineListScrollPane.setViewportView(deadlineList);

		JPanel openfinalpaperButton = new JPanel();
		JLabel openfinalpaperLabel = new JLabel("Open Paper");
		openfinalpaperLabel.setHorizontalAlignment(SwingConstants.CENTER);
		openfinalpaperLabel.setForeground(Color.WHITE);
		openfinalpaperLabel.setFont(new Font("Arial", Font.BOLD, 12));
		openfinalpaperLabel.setBounds(0, 0, 119, 30);
		openfinalpaperButton.add(openfinalpaperLabel);

		openfinalpaperButton.setLayout(null);
		openfinalpaperButton.setBackground(new Color(0, 124, 65));
		openfinalpaperButton.setBounds(555, 235, 119, 30);
		finalPanel.add(openfinalpaperButton);

		JPanel finalSepPanel = new JPanel();
		finalSepPanel.setBackground(Color.BLACK);
		finalSepPanel.setBounds(24, 270, 650, 2);
		finalPanel.add(finalSepPanel);

		JLabel finalcommentsLabel = new JLabel("Final Comments");
		finalcommentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		finalcommentsLabel.setBounds(24, 291, 350, 18);
		finalPanel.add(finalcommentsLabel);

		JScrollPane commentsTextAreaScrollPane = new JScrollPane();
		commentsTextAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		commentsTextAreaScrollPane.setBounds(24, 326, 650, 180);
		finalPanel.add(commentsTextAreaScrollPane);

		JTextArea commentsTextArea = new JTextArea();
		commentsTextAreaScrollPane.setViewportView(commentsTextArea);

		JPanel extendButton = new JPanel();
		JLabel extendLabel = new JLabel("Extend Deadline");
		extendLabel.setHorizontalAlignment(SwingConstants.CENTER);
		extendLabel.setForeground(Color.WHITE);
		extendLabel.setFont(new Font("Arial", Font.BOLD, 12));
		extendLabel.setBounds(0, 0, 119, 30);
		extendButton.add(extendLabel);

		extendButton.setLayout(null);
		extendButton.setBackground(new Color(0, 124, 65));
		extendButton.setBounds(295, 520, 119, 30);
		finalPanel.add(extendButton);

		JPanel finalapproveButton = new JPanel();
		JLabel finalapproveLabel = new JLabel("Approve");
		finalapproveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		finalapproveLabel.setForeground(Color.WHITE);
		finalapproveLabel.setFont(new Font("Arial", Font.BOLD, 12));
		finalapproveLabel.setBounds(0, 0, 119, 30);
		finalapproveButton.add(finalapproveLabel);

		finalapproveButton.setLayout(null);
		finalapproveButton.setBackground(new Color(0, 124, 65));
		finalapproveButton.setBounds(425, 520, 119, 30);
		finalPanel.add(finalapproveButton);

		JPanel finalrejectButton = new JPanel();
		JLabel finalrejectLabel = new JLabel("Reject");
		finalrejectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		finalrejectLabel.setForeground(Color.WHITE);
		finalrejectLabel.setFont(new Font("Arial", Font.BOLD, 12));
		finalrejectLabel.setBounds(0, 0, 119, 30);
		finalrejectButton.add(finalrejectLabel);

		finalrejectButton.setLayout(null);
		finalrejectButton.setBackground(new Color(0, 124, 65));
		finalrejectButton.setBounds(555, 520, 119, 30);
		finalPanel.add(finalrejectButton);

		JLabel menuLabel = new JLabel("MENU");
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(0, 30, 180, 40);
		menuPanel.add(menuLabel);

		JPanel submissionsButton = new JPanel();
		JLabel submissionsLabel = new JLabel("Review Submissions");
		submissionsLabel.setForeground(Color.WHITE);
		submissionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		submissionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submissionsLabel.setBounds(0, 0, 180, 30);
		submissionsButton.add(submissionsLabel);

		// MouseListener for new submissions tab that opens submission panel and
		// refreshes for new submissions
		submissionsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submissionsButton.setBackground(new Color(255, 219, 5));
				submissionsLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				submissionsButton.setBackground(new Color(0, 124, 65));
				submissionsLabel.setForeground(Color.WHITE);
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
					newSubmissionModel.addElement(newSubmissions[i]);
				}
			}
		});

		JPanel menuSepPanel = new JPanel();
		menuSepPanel.setBounds(10, 70, 160, 2);
		menuPanel.add(menuSepPanel);
		submissionsButton.setBackground(new Color(0, 124, 65));
		submissionsButton.setBounds(0, 80, 180, 30);
		menuPanel.add(submissionsButton);
		submissionsButton.setLayout(null);

		JPanel feedbackButton = new JPanel();
		JLabel feedbackLabel = new JLabel("Review Feedback");
		feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		feedbackLabel.setForeground(Color.WHITE);
		feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
		feedbackLabel.setBounds(0, 0, 180, 30);
		feedbackButton.add(feedbackLabel);

		// MouseListener for review feedback tab that refreshes for new feedback when
		// clicked
		feedbackButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				feedbackButton.setBackground(new Color(255, 219, 5));
				feedbackLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				feedbackButton.setBackground(new Color(0, 124, 65));
				feedbackLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				paperModel.clear();
				getSubmissions(FEEDBACK_REVIEW_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);

				for (int i = 0; i < newSubmissions.length; i++) {
					paperModel.addElement(newSubmissions[i]);
				}

				contentPanel.add(feedbackPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});
		feedbackButton.setLayout(null);
		feedbackButton.setBackground(new Color(0, 124, 65));
		feedbackButton.setBounds(0, 185, 180, 30);
		menuPanel.add(feedbackButton);

		JPanel verifyButton = new JPanel();
		JLabel verifyLabel = new JLabel("Verify Reviewers");
		verifyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		verifyLabel.setFont(new Font("Arial", Font.BOLD, 14));
		verifyLabel.setForeground(Color.WHITE);
		verifyLabel.setBounds(0, 0, 180, 30);
		verifyButton.add(verifyLabel);

		// MouseListener for verify new reviewers tab
		verifyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				verifyButton.setBackground(new Color(255, 219, 5));
				verifyLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				verifyButton.setBackground(new Color(0, 124, 65));
				verifyLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				contentPanel.add(verifyPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});
		verifyButton.setBackground(new Color(0, 124, 65));
		verifyButton.setBounds(0, 115, 180, 30);
		menuPanel.add(verifyButton);
		verifyButton.setLayout(null);

		JPanel assignButton = new JPanel();
		JLabel assignLabel = new JLabel("Assign Reviewers");
		assignLabel.setHorizontalAlignment(SwingConstants.CENTER);
		assignLabel.setForeground(Color.WHITE);
		assignLabel.setFont(new Font("Arial", Font.BOLD, 14));
		assignLabel.setBounds(0, 0, 180, 30);
		assignButton.add(assignLabel);

		// MouseListener for assigning reviewers tab that refreshes submissions in that
		// stage
		assignButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				assignButton.setBackground(new Color(255, 219, 5));
				assignLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				assignButton.setBackground(new Color(0, 124, 65));
				assignLabel.setForeground(Color.WHITE);
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

				for (int i = 0; i < newSubmissions.length; i++) {
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
		assignButton.setLayout(null);
		assignButton.setBackground(new Color(0, 124, 65));
		assignButton.setBounds(0, 150, 180, 30);
		menuPanel.add(assignButton);

		JPanel logoutButton = new JPanel();
		JLabel logoutLabel = new JLabel("Logout");
		logoutLabel.setFont(new Font("Arial", Font.BOLD, 14));
		logoutLabel.setForeground(Color.WHITE);
		logoutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoutLabel.setBounds(0, 0, 180, 30);
		logoutButton.add(logoutLabel);

		// Logout button mouselistener
		logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				logoutButton.setBackground(new Color(255, 219, 5));
				logoutLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				logoutButton.setBackground(new Color(0, 124, 65));
				logoutLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				dispose();
				Login.main(null);
			}
		});

		JPanel finalButton = new JPanel();
		JLabel final1Label = new JLabel("Review");
		final1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		final1Label.setHorizontalAlignment(SwingConstants.CENTER);
		final1Label.setForeground(Color.WHITE);
		final1Label.setFont(new Font("Arial", Font.BOLD, 14));
		final1Label.setBounds(0, 0, 180, 30);
		finalButton.add(final1Label);
		JLabel final2Label = new JLabel("Final Submissions");
		final2Label.setVerticalAlignment(SwingConstants.TOP);
		final2Label.setHorizontalAlignment(SwingConstants.CENTER);
		final2Label.setFont(new Font("Arial", Font.BOLD, 14));
		final2Label.setForeground(Color.WHITE);
		final2Label.setBounds(0, 30, 180, 30);
		finalButton.add(final2Label);

		// MouseListener for final submissions tab
		finalButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				finalButton.setBackground(new Color(255, 219, 5));
				final1Label.setForeground(Color.BLACK);
				final2Label.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				finalButton.setBackground(new Color(0, 124, 65));
				final1Label.setForeground(Color.WHITE);
				final2Label.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				getSubmissions(RESUBMIT_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date today = new Date();
				try {
					for (int i = 0; i < newSubmissions.length; i++) {
						Date deadline;
						deadline = format.parse(newSubmissions[i].submissionDeadline);
						if (today.after(deadline))
							deadlineModel.addElement(newSubmissions[i]);
				}
				}catch (ParseException e) {e.printStackTrace();}

				contentPanel.add(finalPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});
		finalButton.setLayout(null);
		finalButton.setBackground(new Color(0, 124, 65));
		finalButton.setBounds(0, 220, 180, 60);
		menuPanel.add(finalButton);
		logoutButton.setBackground(new Color(0, 124, 65));
		logoutButton.setBounds(0, 379, 180, 30);
		menuPanel.add(logoutButton);
		logoutButton.setLayout(null);

		JLabel ualogo = new JLabel("");
		Image ualogoImg = new ImageIcon(this.getClass().getResource("/ualogo.jpg")).getImage();
		ualogo.setBounds(40, 420, 100, 100);
		menuPanel.add(ualogo);
		ualogo.setIcon(new ImageIcon(ualogoImg));

		//populate new applicants list
		for (int i = 0; i < applicants.length; i++) {
			if (applicants[i] != null)
				applicantModel.addElement(applicants[i]);
		}

		// populates new submissions list
		for (int i = 0; i < newSubmissions.length; i++) {
			if (newSubmissions[i] != null)
				newSubmissionModel.addElement(newSubmissions[i]);
		}

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
					detailtitleSubLabel.setText(newSubmissions[paperIndex].submissionName);
					detailauthorsSubLabel.setText(newSubmissions[paperIndex].submissionAuthors);
					detailsubjectSubLabel.setText(newSubmissions[paperIndex].subject);

					// Checks if any preferred reviewers were specified and displays a message if
					// none
					if (newSubmissions[paperIndex].preferredReviewerIDs == null)
						detailprefreviewerSubLabel.setText("No Preferred Reviewers");
					else
						detailprefreviewerSubLabel.setText(newSubmissions[paperIndex].preferredReviewerNames);

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
					detailtitleSubLabel.setText("");
					detailauthorsSubLabel.setText("");
					detailsubjectSubLabel.setText("");
					detailprefreviewerSubLabel.setText("");
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
					String fileLocation = "submissions/"
							+ paperOfInterest.submissionUserID + "/" + paperOfInterest.filename;

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

					// get selected paper's submissionID
					int submissionID = newSubmissions[paperIndex].submissionID;

					// send SQL update
					approveSubmission(submissionID, submissionDeadline);

					// refresh new submissions list after approving
					getSubmissions(NEW_SUBMISSION_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						newSubmissionModel.addElement(newSubmissions[i]);
					}
					
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
					ps = SQLConnection.getConnection().prepareStatement(query);
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

					// get selected paper's unique ID
					int submissionID = newSubmissions[paperIndex].submissionID;

					// set submissionStage to rejected stage
					setSubmissionStage(submissionID, REJECTED_SUBMISSION_STAGE);

					// refresh list of new submissions after rejection
					getSubmissions(1);
					newSubmissions = populateSubmissions(newSubmissions);
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						newSubmissionModel.addElement(newSubmissions[i]);
					}
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
					
					//populates detail text labels for selected applicant
					detailsoccupationLabel.setText(applicants[selected].occupation);
					detailsorganizationLabel.setText(applicants[selected].organization);
					detailsresearchareaLabel.setText(applicants[selected].researchArea);
					
				} else if (selectedApplicant.length >= 2) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Applicant",
							"Too Many Applicants Selected", JOptionPane.PLAIN_MESSAGE, null);
					applicantList.clearSelection();
				}
				else {
					detailsoccupationLabel.setText("");
					detailsorganizationLabel.setText("");
					detailsresearchareaLabel.setText("");

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

					approveApplicant(applicantID);

					// update applicants list
					applicantModel.clear();
					getApplicants();
					for (int i = 0; i < applicants.length; i++) {
						if (applicants[i] != null)
							applicantModel.addElement(applicants[i]);
					}
				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please select an applicant", "No applicant selected",
							JOptionPane.PLAIN_MESSAGE, null);

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

				rejectButton.setBackground(new Color(0, 124, 65));
				rejectappLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedApplicant.length == 1) {
					int selectedApplicantIndex = selectedApplicant[0];
					int applicantID = applicants[selectedApplicantIndex].userID;

					rejectApplicant(applicantID);

					// update applicants list
					applicantModel.clear();
					getApplicants();
					for (int i = 0; i < applicants.length; i++) {
						if (applicants[i] != null)
							applicantModel.addElement(applicants[i]);
					}
				} else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please select an applicant", "No applicant selected",
							JOptionPane.PLAIN_MESSAGE, null);

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
		sortallButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				sortallButton.setBackground(new Color(255, 219, 5));
				sortallLabel.setForeground(Color.BLACK);

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				sortallButton.setBackground(new Color(0, 124, 65));

				sortallLabel.setForeground(Color.WHITE);

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
		sortselfButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				sortselfButton.setBackground(new Color(255, 219, 5));
				sortselfLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				sortselfButton.setBackground(new Color(0, 124, 65));
				sortselfLabel.setForeground(Color.WHITE);
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

					if (selectedSubmission.nominatedReviewers != null) {

						// Split the list of preferred reviewer IDs by comma
						String[] nominated = selectedSubmission.nominatedReviewers.split("[,]");

						// store those split strings as integers
						for (int i = 0; i < nominated.length; i++) {
							nominatedIDs[i] = Integer.parseInt(nominated[i]);
						}
					}

					// Populate the reviewer list by preferred reviewer IDs
					for (int i = 0; i < nominatedIDs.length; i++) {
						if (nominatedIDs[i] != 0)
							reviewerModel.addElement(reviewerIDtoObject.get(nominatedIDs[i]));
					}
				}
			}
		});

		// SORT REVIEWERS BY AUTHOR NOMINATED BUTTON
		sortauthorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				sortauthorButton.setBackground(new Color(255, 219, 5));
				sortauthorLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				sortauthorButton.setBackground(new Color(0, 124, 65));
				sortauthorLabel.setForeground(Color.WHITE);
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

						// refresh list of papers that need reviewers assigned
						assignpaperModel.clear();
						getSubmissions(APPROVED_SUBMISSION_STAGE);
						newSubmissions = populateSubmissions(newSubmissions);
						for (int i = 0; i < newSubmissions.length; i++) {
							assignpaperModel.addElement(newSubmissions[i]);
						}
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
							+ paperInDetail.submissionName.split("\\.")[0] + ".txt";

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

						JOptionPane.showMessageDialog(null, "Feedback approved!\n Available for author's viewing.",
								"Feedback Approved", JOptionPane.PLAIN_MESSAGE, null);

						// updates feedback area
						paperModel.clear();
						getSubmissions(FEEDBACK_REVIEW_STAGE);
						newSubmissions = populateSubmissions(newSubmissions);

						for (int i = 0; i < newSubmissions.length; i++) {
							paperModel.addElement(newSubmissions[i]);
						}

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
					String fileLocation = "submissions/"
							+ paperOfInterest.submissionUserID + "/" + paperOfInterest.filename;

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

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 1) {
					int selectedIndex = selectedPaper[0];
					int ID = deadlineModel.getElementAt(selectedIndex).submissionID;

					setSubmissionStage(ID, FINAL_APPROVED_STAGE);

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							deadline = format.parse(newSubmissions[i].submissionDeadline);
							if (today.after(deadline))
								deadlineModel.addElement(newSubmissions[i]);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
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

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (selectedPaper.length == 1) {
					int selectedIndex = selectedPaper[0];
					int ID = deadlineModel.getElementAt(selectedIndex).submissionID;

					setSubmissionStage(ID, REJECTED_SUBMISSION_STAGE);

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							deadline = format.parse(newSubmissions[i].submissionDeadline);
							if (today.after(deadline))
								deadlineModel.addElement(newSubmissions[i]);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Extend deadline button
		extendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				extendButton.setBackground(new Color(255, 219, 5));
				extendLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				extendButton.setBackground(new Color(0, 124, 65));
				extendLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (selectedPaper.length == 1) {
					// add 1 month to deadline
					int selectedIndex = selectedPaper[0];
					int submissionID = deadlineModel.getElementAt(selectedIndex).submissionID;

					String dl = deadlineModel.getElementAt(selectedIndex).submissionDeadline;
					String[] oldDl = dl.split("[-]");
					LocalDate oldDeadline = LocalDate.of(Integer.parseInt(oldDl[0]), Integer.parseInt(oldDl[1]),
							Integer.parseInt(oldDl[2]));
					oldDeadline.plusMonths(1);

					setDeadline(submissionID, oldDeadline.toString());

					// refresh final submissions list
					getSubmissions(RESUBMIT_STAGE);
					newSubmissions = populateSubmissions(newSubmissions);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					try {
						for (int i = 0; i < newSubmissions.length; i++) {
							Date deadline;
							deadline = format.parse(newSubmissions[i].submissionDeadline);
							if (today.after(deadline))
								deadlineModel.addElement(newSubmissions[i]);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
			}
		});

	}

	/**
	 * Gets user's submissions from an sql query and stores in a global ResultSet
	 */
	private void getSubmissions(int stage) {
		PreparedStatement ps;

		String query = "SELECT * FROM submission WHERE submissionStage  = ?";

		try {
			ps = SQLConnection.getConnection().prepareStatement(query);

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
	 */
	private SubmissionObject[] populateSubmissions(SubmissionObject[] submissions) {

		try {
			int numOfSubmissions = 0;
			while (submissionSet.next())
				numOfSubmissions++;

			int i = 0;

			submissions = new SubmissionObject[numOfSubmissions];
			submissionSet.beforeFirst();

			while (submissionSet.next()) {
				int submissionID = submissionSet.getInt("submissionID");
				String submissionName = submissionSet.getString("submissionName");
				String submissionAuthors = submissionSet.getString("submissionAuthors");
				String subject = submissionSet.getString("subject");
				String submissionDate = submissionSet.getString("submissionDate");
				int submissionStage = submissionSet.getInt("submissionStage");
				String filename = submissionSet.getString("filename");
				int submissionUserID = submissionSet.getInt("submissionUserID");
				submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject,
						submissionDate, submissionStage, filename, submissionUserID);

				String submissionDeadline = submissionSet.getString("submissionDeadline");
				String reviewerIDs = submissionSet.getString("reviewerIDs");
				String feedbackIDs = submissionSet.getString("feedbackIDs");
				String preferredReviewerIDs = submissionSet.getString("preferredReviewerIDs");
				String nominatedReviewers = submissionSet.getString("nominatedReviewers");
				
				if (submissionDeadline == null)
					submissions[i].submissionDeadline = null;
				else
					submissions[i].submissionDeadline = submissionDeadline;

				if (reviewerIDs == null)
					submissions[i].reviewerIDs = null;
				else
					submissions[i].reviewerIDs = reviewerIDs;

				if (feedbackIDs == null)
					submissions[i].feedbackIDs = null;
				else
					submissions[i].feedbackIDs = feedbackIDs;

				if (preferredReviewerIDs == null)
					submissions[i].preferredReviewerIDs = null;
				else
					submissions[i].preferredReviewerIDs = preferredReviewerIDs;
				
				if (nominatedReviewers == null)
					submissions[i].nominatedReviewers = null;
				else
					submissions[i].nominatedReviewers = nominatedReviewers;
				
				submissions[i].setReviewerNames();
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return submissions;
	}

	/**
	 * Sets the submission stage in the database, stored as an int. 
	 * The stages are defined in Constants.java
	 * @param submissionID
	 * @param stage
	 */
	private void setSubmissionStage(int submissionID, int stage) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionStage = ? WHERE submissionID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
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
			ps = SQLConnection.getConnection().prepareStatement(query);

			reviewerSet = ps.executeQuery();
			int count = 0;
			while (reviewerSet.next()) {
				int userID = reviewerSet.getInt("userID");
				String username = reviewerSet.getString("username");
				String name = String.valueOf(username.charAt(0)).toUpperCase() + username.substring(1).split("\\@")[0];
				String email = reviewerSet.getString("email");
				int userType = 2;

				reviewers[count] = new ReviewerObject(userID, username, name, email, userType);
				reviewerIDtoObject.put(userID, reviewers[count]);
				
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
			ps = SQLConnection.getConnection().prepareStatement(query);

			applicantSet = ps.executeQuery();
			int count = 0;
			while (applicantSet.next()) {
				int userID = applicantSet.getInt("userID");
				String username = applicantSet.getString("username");
				String name = String.valueOf(username.charAt(0)).toUpperCase() + username.substring(1).split("\\@")[0];
				String email = applicantSet.getString("email");
				int userType = 3;

				applicants[count] = new ReviewerObject(userID, username, name, email, userType);

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
	 * @param submissionID
	 * @param reviewerIDs
	 */
	private void assignReviewers(int submissionID, String reviewerIDs) {
		PreparedStatement ps;

		String query = "UPDATE submission SET reviewerIDs = ? , submissionStage = ? WHERE submissionID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
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
	 * @param submissionID
	 */
	private void approveFeedback(int submissionID) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionStage = ? WHERE submissionID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, RESUBMIT_STAGE);
			ps.setInt(2, submissionID);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Approves a reviewer who applied
	 * @param userID
	 */
	private void approveApplicant(int userID) {
		PreparedStatement ps;

		String query = "UPDATE users SET usertype = ? WHERE userID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, 2);
			ps.setInt(2, userID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Rejects a reviewer who applied
	 * @param userID
	 */
	private void rejectApplicant(int userID) {

		PreparedStatement ps;

		String query = "UPDATE users SET usertype = ? WHERE userID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, 4);
			ps.setInt(2, userID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	/**
	 * Sends an email to a specified account with a subject and body
	 * @param to, the address the email is being sent to
	 * @param subject, the email's subject
	 * @param body, the email's text contents
	 */
	private void sendEmail(String subject, String body) {
		
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
		
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);
		
		try {
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress("jss.ualberta@gmail.com");
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
	}
	
	/**
	 * Set a new deadline for submission
	 * @param submissionID
	 * @param newDeadline
	 */
	private void setDeadline(int submissionID, String newDeadline) {
		PreparedStatement ps;

		String query = "UPDATE submission SET submissionDeadline = ? WHERE submissionID = ?";

		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setString(1, newDeadline);
			ps.setInt(2, submissionID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
