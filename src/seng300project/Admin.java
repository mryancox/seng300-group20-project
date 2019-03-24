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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

public class Admin extends JFrame implements Constants {

	/**
	 *
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
	protected SubmissionObject[] newSubmissions;
	protected SubmissionObject[] finalSubmissions;
	protected FeedbackObject[] feedback;
	protected ReviewerObject[] reviewers;
	protected String[] subjects = new String[100];
	private int[] selectedPaper = new int[0];
	private int[] selected = 	new int[0];
	private int[] fbAreaSelected = new int[0];
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin("Admin2",9);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param user
	 */
	public Admin(String user, int ID) {
		this.userID=ID;

		getSubmissions(1);
		newSubmissions = populateSubmissions(newSubmissions);
		getReviewers();
		
		setTitle("Journal Submission System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String userFolder = "submissions/" + userID;
		String userDetails = "submissions/" + userID + "/details";
		String userFeedback = "submissions/" + userID + "/feedback";
		String userFeedbackList = "submissions/" + userID + "/feedback_list.txt";
		String userSubmissionList = "submissions/" + userID + "/submission_list.txt";
		String acceptedSubmissionsList = "submissions/acceptedsubmissions/";
		File authorFolder = new File(userFolder);
		File detailsFolder = new File(userDetails);
		File feedbackFolder = new File(userFeedback);
		File feedbackListFile = new File (userFeedbackList);
		File submissionListFile = new File (userSubmissionList);
		File acceptedSubmissionsFolder = new File (acceptedSubmissionsList);

		if(!acceptedSubmissionsFolder.exists())
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
		String niceUsername = user;

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
		newsubLabel.setBounds(24, 90, 230, 14);
		submissionsPanel.add(newsubLabel);
		
		
				
		DefaultListModel<SubmissionObject> newSubmissionModel = new DefaultListModel<>();
		JList<SubmissionObject> submissionsList = new JList<SubmissionObject>(newSubmissionModel);
		
		
		
		
		
		submissionsList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane submissionsListScrollPane = new JScrollPane(submissionsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
		detailsLabel.setBounds(24, 285, 291, 14);
		submissionsPanel.add(detailsLabel);
		
		JLabel detailsextraLabel = new JLabel("(select a paper above)");
		detailsextraLabel.setForeground(Color.DARK_GRAY);
		detailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		detailsextraLabel.setBounds(24, 305, 151, 14);
		submissionsPanel.add(detailsextraLabel);
		
		JLabel titleSubLabel = new JLabel("Title");
		titleSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		titleSubLabel.setBounds(24, 350, 325, 14);
		submissionsPanel.add(titleSubLabel);
		
		JLabel detailtitleSubLabel = new JLabel("");
		detailtitleSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailtitleSubLabel.setBounds(24, 380, 325, 14);
		submissionsPanel.add(detailtitleSubLabel);
		
		JLabel authorsSubLabel = new JLabel("Author");
		authorsSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		authorsSubLabel.setBounds(24, 410, 325, 14);
		submissionsPanel.add(authorsSubLabel);
		
		JLabel detailauthorsSubLabel = new JLabel("");
		detailauthorsSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailauthorsSubLabel.setBounds(24, 440, 325, 14);
		submissionsPanel.add(detailauthorsSubLabel);
		
		JLabel subjectSubLabel = new JLabel("Research Subject");
		subjectSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		subjectSubLabel.setBounds(359, 350, 315, 14);
		submissionsPanel.add(subjectSubLabel);
		
		JLabel detailsubjectSubLabel = new JLabel("");
		detailsubjectSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsubjectSubLabel.setBounds(359, 380, 315, 14);
		submissionsPanel.add(detailsubjectSubLabel);
		
		JLabel prefreviewersSubLabel = new JLabel("Preferred Reviewers");
		prefreviewersSubLabel.setFont(new Font("Arial", Font.BOLD, 12));
		prefreviewersSubLabel.setBounds(359, 410, 315, 14);
		submissionsPanel.add(prefreviewersSubLabel);
		
		JLabel detailprefreviewerSubLabel = new JLabel("");
		detailprefreviewerSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailprefreviewerSubLabel.setBounds(359, 440, 315, 14);
		submissionsPanel.add(detailprefreviewerSubLabel);
		
		JPanel detailsSepPanel = new JPanel();
		detailsSepPanel.setBackground(Color.BLACK);
		detailsSepPanel.setBounds(24, 470, 650, 2);
		submissionsPanel.add(detailsSepPanel);
		
		JLabel deadlineLabel = new JLabel("Set Deadline");
		deadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
		deadlineLabel.setBounds(24, 485, 291, 14);
		submissionsPanel.add(deadlineLabel);
		
		JTextArea deadlineTextArea = new JTextArea();
		deadlineTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		deadlineTextArea.setBounds(24, 510, 291, 30);
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
		verifyTitleLabel.setBounds(24, 30, 230, 40);
		verifyPanel.add(verifyTitleLabel);
		
		JLabel applicantsLabel = new JLabel("Applicants");
		applicantsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		applicantsLabel.setBounds(24, 90, 230, 14);
		verifyPanel.add(applicantsLabel);
		
		JScrollPane applicantListScrollPane = new JScrollPane();
		applicantListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		applicantListScrollPane.setBounds(24, 115, 650, 178);
		verifyPanel.add(applicantListScrollPane);
		
		JList applicantList = new JList();
		applicantList.setFont(new Font("Arial", Font.PLAIN, 12));
		applicantListScrollPane.setViewportView(applicantList);
		
		JPanel verifySepPanel = new JPanel();
		verifySepPanel.setBackground(Color.BLACK);
		verifySepPanel.setBounds(24, 305, 650, 2);
		verifyPanel.add(verifySepPanel);
		
		JLabel appdetailsLabel = new JLabel("Applicant Details");
		appdetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		appdetailsLabel.setBounds(24, 320, 230, 14);
		verifyPanel.add(appdetailsLabel);
		
		JLabel appdetailsextraLabel = new JLabel("(select a new applicant above)");
		appdetailsextraLabel.setForeground(Color.DARK_GRAY);
		appdetailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		appdetailsextraLabel.setBounds(24, 340, 209, 14);
		verifyPanel.add(appdetailsextraLabel);
		
		JLabel occupationLabel = new JLabel("Occupation");
		occupationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		occupationLabel.setBounds(24, 385, 325, 14);
		verifyPanel.add(occupationLabel);
		
		JLabel detailsoccupationLabel = new JLabel("");
		detailsoccupationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsoccupationLabel.setBounds(24, 415, 325, 14);
		verifyPanel.add(detailsoccupationLabel);
		
		JLabel organizationLabel = new JLabel("Organization");
		organizationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		organizationLabel.setBounds(24, 445, 325, 14);
		verifyPanel.add(organizationLabel);
		
		JLabel detailsorganizationLabel = new JLabel("");
		detailsorganizationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsorganizationLabel.setBounds(24, 475, 325, 14);
		verifyPanel.add(detailsorganizationLabel);
		
		JLabel researchareaLabel = new JLabel("Research Area");
		researchareaLabel.setFont(new Font("Arial", Font.BOLD, 12));
		researchareaLabel.setBounds(359, 385, 315, 14);
		verifyPanel.add(researchareaLabel);
		
		JLabel detailsresearchareaLabel = new JLabel("");
		detailsresearchareaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailsresearchareaLabel.setBounds(359, 415, 315, 14);
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
		assignTitleLabel.setBounds(24, 30, 230, 40);
		assignPanel.add(assignTitleLabel);
		
		JLabel reviewersLabel = new JLabel("Reviewer List");
		reviewersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		reviewersLabel.setBounds(24, 90, 230, 14);
		assignPanel.add(reviewersLabel);
		
		JScrollPane reviewerListScrollPane = new JScrollPane();
		reviewerListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		reviewerListScrollPane.setBounds(24, 115, 650, 110);
		assignPanel.add(reviewerListScrollPane);
		
		DefaultListModel<ReviewerObject> reviewerModel = new DefaultListModel<ReviewerObject>();
		JList <ReviewerObject>reviewerList = new JList<>(reviewerModel);
		reviewerListScrollPane.setViewportView(reviewerList);
		
		JPanel sortselfButton = new JPanel();
		JLabel sortselfLabel = new JLabel("Self-Nominated");
		sortselfLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortselfLabel.setForeground(Color.WHITE);
		sortselfLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortselfLabel.setBounds(0, 0, 135, 20);
		sortselfButton.add(sortselfLabel);

		sortselfButton.setLayout(null);
		sortselfButton.setBackground(new Color(0, 124, 65));
		sortselfButton.setBounds(394, 90, 135, 20);
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
		sortauthorButton.setBounds(539, 90, 135, 20);
		assignPanel.add(sortauthorButton);

		JPanel assignSepPanel = new JPanel();
		assignSepPanel.setBackground(Color.BLACK);
		assignSepPanel.setBounds(24, 235, 650, 2);
		assignPanel.add(assignSepPanel);
		
		JLabel chosenLabel = new JLabel("Chosen Paper(s)");
		chosenLabel.setFont(new Font("Arial", Font.BOLD, 14));
		chosenLabel.setBounds(24, 250, 230, 14);
		assignPanel.add(chosenLabel);
		
		JLabel chosenextraLabel = new JLabel("(by self-nomination or author)");
		chosenextraLabel.setForeground(Color.DARK_GRAY);
		chosenextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		chosenextraLabel.setBounds(24, 270, 209, 14);
		assignPanel.add(chosenextraLabel);
		
		JLabel detailschosenLabel = new JLabel("");
		detailschosenLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		detailschosenLabel.setBounds(24, 300, 650, 14);
		assignPanel.add(detailschosenLabel);
		
		JPanel chosenSepPanel = new JPanel();
		chosenSepPanel.setBackground(Color.BLACK);
		chosenSepPanel.setBounds(24, 325, 650, 2);
		assignPanel.add(chosenSepPanel);
		
		JLabel assignpaperLabel = new JLabel("Assign Paper From List To Selected Reviewer");
		assignpaperLabel.setFont(new Font("Arial", Font.BOLD, 14));
		assignpaperLabel.setBounds(24, 340, 453, 14);
		assignPanel.add(assignpaperLabel);
		
		JLabel assignpaperextraLabel = new JLabel("(choose reviewer from above list)");
		assignpaperextraLabel.setForeground(Color.DARK_GRAY);
		assignpaperextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		assignpaperextraLabel.setBounds(24, 360, 209, 14);
		assignPanel.add(assignpaperextraLabel);
		
		JScrollPane assignpaperListScrollPane = new JScrollPane();
		assignpaperListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		assignpaperListScrollPane.setBounds(24, 390, 650, 110);
		assignPanel.add(assignpaperListScrollPane);
		
		DefaultListModel<SubmissionObject> assignpaperModel = new DefaultListModel<SubmissionObject>();
		JList<SubmissionObject> assignpaperList = new JList<>(assignpaperModel);
		assignpaperListScrollPane.setViewportView(assignpaperList);
		
		
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
		feedbackTitleLabel.setBounds(24, 30, 230, 40);
		feedbackPanel.add(feedbackTitleLabel);
		feedbackTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JLabel reviewLabel = new JLabel("Papers With Feedback Waiting For Review");
		reviewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		reviewLabel.setBounds(24, 90, 326, 14);
		feedbackPanel.add(reviewLabel);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> feedbackList = new JList<SubmissionObject>(paperModel);

		feedbackList.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane feedbackListScrollPane = new JScrollPane(feedbackList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
		provideLabel.setBounds(24, 285, 230, 14);
		feedbackPanel.add(provideLabel);

		JLabel provideextraLabel = new JLabel("(for the paper you have selected above)");
		provideextraLabel.setForeground(Color.DARK_GRAY);
		provideextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		provideextraLabel.setBounds(24, 305, 267, 14);
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
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
		finalTitleLabel.setBounds(24, 30, 348, 40);
		finalPanel.add(finalTitleLabel);
		
		JLabel pastdeadlineLabel = new JLabel("Papers Past Deadline");
		pastdeadlineLabel.setFont(new Font("Arial", Font.BOLD, 14));
		pastdeadlineLabel.setBounds(24, 90, 326, 14);
		finalPanel.add(pastdeadlineLabel);
		
		JScrollPane deadlineListScrollPane = new JScrollPane();
		deadlineListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		deadlineListScrollPane.setBounds(24, 115, 650, 110);
		finalPanel.add(deadlineListScrollPane);
		
		JList deadlineList = new JList();
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
		finalcommentsLabel.setBounds(24, 291, 326, 14);
		finalPanel.add(finalcommentsLabel);
		
		JScrollPane commentsTextAreaScrollPane = new JScrollPane();
		commentsTextAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		commentsTextAreaScrollPane.setBounds(24, 326, 650, 180);
		finalPanel.add(commentsTextAreaScrollPane);
		
		JTextArea commentsTextArea = new JTextArea();
		commentsTextAreaScrollPane.setViewportView(commentsTextArea);
		
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
				
				//refreshes new submissions when its panel is opened
				getSubmissions(1);
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
				//get submissions with stage = 2
				getSubmissions(APPROVED_SUBMISSION_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);
				
				for (int i = 0; i < newSubmissions.length; i++) {
					assignpaperModel.addElement(newSubmissions[i]);
				}
				for(int i=0;i<reviewers.length;i++) {
					if(reviewers[i]!=null)
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

				setVisible(false);
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
		
		
		
		
		// populates new submissions list
		for (int i = 0; i < newSubmissions.length; i++) {
			newSubmissionModel.addElement(newSubmissions[i]);
		}

		// REVIEW NEW SUBMISSIONS LIST LOGIC
		submissionsList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				selectedPaper = submissionsList.getSelectedIndices();
				if (selectedPaper.length == 1) {

					int paperIndex = selectedPaper[0];

					detailtitleSubLabel.setText(newSubmissions[paperIndex].submissionName);
					detailauthorsSubLabel.setText(newSubmissions[paperIndex].submissionAuthors);
					detailsubjectSubLabel.setText(newSubmissions[paperIndex].subject);

					if (newSubmissions[paperIndex].preferredReviewerIDs == null)
						detailprefreviewerSubLabel.setText("No Preferred Reviewers");
					else
						detailprefreviewerSubLabel.setText(newSubmissions[paperIndex].preferredReviewerNames);

				} else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					submissionsList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (selectedPaper.length == 0) {

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

					
					SubmissionObject paperOfInterest = newSubmissionModel.getElementAt(selectedPaper[0]);

					
					//String paperName = paperOfInterest.submissionName;

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
					String fileLocation = "file:///" + filepath.toString() + "submissions/"
							+ paperOfInterest.submissionUserID + "/" + filename;

					try {
						Desktop.getDesktop().browse(new URI(fileLocation));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}

					// example final string format
					// file:///D:/Documents/GitHub/seng300-group20-project/submissions/3/Example%20paper2.pdf
				}
				
				
			}
		});
		
		//private void setSubmissionStage(String submissionName, String subject, int stage) 

		// APPROVE NEW SUBMISSION BUTTON LOGIC
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
				
				if(selectedPaper.length==1) {
					int paperIndex = selectedPaper[0];
					String submissionName = newSubmissionModel.getElementAt(paperIndex).submissionName;
					String subject = detailsubjectSubLabel.getText();
					
					setSubmissionStage(submissionName, subject, 2);

					getSubmissions(1);
					newSubmissions = populateSubmissions(newSubmissions);
					
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						newSubmissionModel.addElement(newSubmissions[i]);
					}
				}
				else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected to approve.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}
				// Add approve new submission here
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
				
				if(selectedPaper.length==1) {
					int paperIndex = selectedPaper[0];
					String submissionName = newSubmissionModel.getElementAt(paperIndex).submissionName;
					String subject = detailsubjectSubLabel.getText();
					
					setSubmissionStage(submissionName, subject, 0);
					

					getSubmissions(1);
					newSubmissions = populateSubmissions(newSubmissions);
					newSubmissionModel.clear();
					for (int i = 0; i < newSubmissions.length; i++) {
						newSubmissionModel.addElement(newSubmissions[i]);
					}
				}
				else {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected to reject.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
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

			@Override
			public void mouseClicked(MouseEvent arg0) {

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

			@Override
			public void mouseClicked(MouseEvent arg0) {

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

			}
		});

		//assign reviewers paper list logic
		assignpaperList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				selected = assignpaperList.getSelectedIndices();
				if(selected.length==1) {
					//nothing here
					
				}else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					assignpaperList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
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

			@Override
			public void mouseClicked(MouseEvent arg0) {
				SubmissionObject paperOfInterest = assignpaperModel.getElementAt(selected[0]);
				ReviewerObject[] selectedReviewers = new ReviewerObject[20];
				
				int[] reviewerIndices = reviewerList.getSelectedIndices();
				for(int i=0; i<reviewerIndices.length; i++) {
					selectedReviewers[i] = new ReviewerObject();
					selectedReviewers[i] = reviewerModel.getElementAt(reviewerIndices[i]);
				}
				
				StringBuilder reviewerIDs = new StringBuilder();
				for(int i=0; i<selectedReviewers.length; i++) {
					if(selectedReviewers[i]!=null) {
						reviewerIDs.append(selectedReviewers[i].userID + ",");
					}
				}
				reviewerIDs.setLength(reviewerIDs.length()-1);
				
				assignReviewers(paperOfInterest.submissionID, reviewerIDs.toString());
				
				assignpaperModel.clear();
				//get submissions with stage = 2
				getSubmissions(APPROVED_SUBMISSION_STAGE);
				newSubmissions = populateSubmissions(newSubmissions);
				
				for (int i = 0; i < newSubmissions.length; i++) {
					assignpaperModel.addElement(newSubmissions[i]);
				}
				
				
				
			}	
		});

		// REVIEW FEEDBACK LIST LOGIC
		feedbackList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				feedbackTextArea.setText("");
				fbAreaSelected = feedbackList.getSelectedIndices();

				if(fbAreaSelected.length==0)
					releaseButton.setVisible(false);
				
				if (fbAreaSelected.length == 1) {
					releaseButton.setVisible(true);

					
					SubmissionObject paperInDetail = newSubmissions[fbAreaSelected[0]];

					String feedbackFile = "submissions/" + paperInDetail.submissionUserID + "/feedback/" + paperInDetail.submissionName.split("\\.")[0] + ".txt";
					
					System.out.println(feedbackFile);
					
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
				// Add code to handle paperList
			
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

						// replacing file
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

						JOptionPane.showMessageDialog(null,
								"Feedback approved!\n Available for author's viewing.", "Feedback Approved",
								JOptionPane.PLAIN_MESSAGE, null);

						//updates feedback area
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

			@Override
			public void mouseClicked(MouseEvent arg0) {

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

				// Add submit logic that grabs from feedbackTextArea and appends to a file
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

				// Add submit logic that grabs from feedbackTextArea and appends to a file
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
			ps=SQLConnection.getConnection().prepareStatement(query);

			ps.setInt(1, stage);

			submissionSet=ps.executeQuery();
		}catch(Exception e) {System.out.println(e); System.out.println("Failure searching for user submissions");}
	}
	
	
	/**
	 * Populates a global array of SubmissionObjects to store results of initial SQL query
	 * for user submissions, eliminating the need to constantly query SQL database
	 */
	private SubmissionObject[] populateSubmissions(SubmissionObject[] submissions) {

		try {
			int numOfSubmissions = 0;
			while(submissionSet.next())
				numOfSubmissions++;

			int i=0;

			submissions = new SubmissionObject[numOfSubmissions];
			submissionSet.beforeFirst();

			while(submissionSet.next()) {
				int submissionID = submissionSet.getInt("submissionID");
				String submissionName = submissionSet.getString("submissionName");
				String submissionAuthors = submissionSet.getString("submissionAuthors");
				String subject = submissionSet.getString("subject");
				String submissionDate = submissionSet.getString("submissionDate");
				int submissionStage = submissionSet.getInt("submissionStage");
				String filename= submissionSet.getString("filename");
				int submissionUserID = submissionSet.getInt("submissionUserID");
				submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject, submissionDate, submissionStage, filename, submissionUserID);

				String submissionDeadline = submissionSet.getString("submissionDeadline");
				String reviewerIDs = submissionSet.getString("reviewerIDs");
				String feedbackIDs = submissionSet.getString("feedbackIDs");
				String preferredReviewerIDs = submissionSet.getString("preferredReviewerIDs");

				if(submissionDeadline==null)
					submissions[i].submissionDeadline = null;
				else
					submissions[i].submissionDeadline = submissionDeadline;

				if(reviewerIDs==null)
					submissions[i].reviewerIDs = null;
				else
					submissions[i].reviewerIDs = reviewerIDs;

				if(feedbackIDs==null)
					submissions[i].feedbackIDs = null;
				else
					submissions[i].feedbackIDs = feedbackIDs;

				if(preferredReviewerIDs==null)
					submissions[i].preferredReviewerIDs = null;
				else
					submissions[i].preferredReviewerIDs = preferredReviewerIDs;
				submissions[i].setReviewerNames();
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return submissions;
	}
	
	
	private void setSubmissionStage(String submissionName, String subject, int stage) {
		PreparedStatement ps;
		
		String query = "UPDATE submission SET submissionStage = ? WHERE submissionName = ? AND subject = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, stage);
			ps.setString(2, submissionName);
			ps.setString(3, subject);
			
			ps.executeUpdate();
			
			
		}catch(Exception e) {e.printStackTrace();}
	}
	
	private void getReviewers() {
		PreparedStatement ps;
		
		reviewers = new ReviewerObject[50];
		String query = "SELECT * FROM users WHERE userType = 2";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			
			reviewerSet = ps.executeQuery();
			int count = 0;
			while(reviewerSet.next()) {
				int userID = reviewerSet.getInt("userID");
				String username = reviewerSet.getString("username");
				String name = reviewerSet.getString("name");
				String email = reviewerSet.getString("email");
				int userType = 2;
				
				reviewers[count] = new ReviewerObject(userID, username, name, email, userType);
				count++;
				
				
				
			}
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	private void getReviewers(int submissionID) {

	}
	
	private void assignReviewers(int submissionID, String reviewerIDs) {
		PreparedStatement ps;
		
		String query = "UPDATE submission SET reviewerIDs = ? , submissionStage = ? WHERE submissionID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setString(1, reviewerIDs);
			ps.setInt(2, FEEDBACK_GATHERING_STAGE);
			ps.setInt(3, submissionID);
			
			ps.executeUpdate();
			
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	private void approveFeedback(int submissionID) {
		PreparedStatement ps;
		
		String query = "UPDATE submission SET submissionStage = ? WHERE submissionID = ?";
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, RESUBMIT_STAGE);
			ps.setInt(2, submissionID);
			
			
			ps.executeUpdate();
			
		}catch(Exception e) {e.printStackTrace();}
	}
	
}

