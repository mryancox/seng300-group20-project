package seng300project;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

public class Author extends JFrame implements Constants{

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
	protected SubmissionObject[] submissions;
	protected FeedbackObject[] feedback = new FeedbackObject[50];
	protected int numOfFeedback = 0;
	private Map<Integer, Integer> submissionIDtoFeedbackApproval = new HashMap<Integer, Integer>();
		

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author frame = new Author("Test Author",8);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param user - User's name (not username)
	 * @param ID - user's userID (invisible to user)
	 */
	public Author(String user, int ID) {
		this.userID=ID;

		//Initial pull of user's data
		getSubmissions();
		getReviewers();
		populateSubmissions();
		//getFeedback();


		setTitle("Journal Submission System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		//check for and create possible folders for user
		String userFolder = "submissions/" + userID;
		String userDetails = "submissions/" + userID + "/details";
		String userFeedback = "submissions/" + userID + "/feedback";
		String userFeedbackList = "submissions/" + userID + "/feedback_list.txt";
		String userSubmissionList = "submissions/" + userID + "/submission_list.txt";
		File authorFolder = new File(userFolder);
		File detailsFolder = new File(userDetails);
		File feedbackFolder = new File(userFeedback);
		File feedbackListFile = new File (userFeedbackList);
		File submissionListFile = new File (userSubmissionList);

		// Checks if the required folders exist already, if not it creates them
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
		 * LOTS of GUI code follows that can be mostly ignored. In general, objects in the
		 * frame were created with certain boundaries and colours to create a cohesive feel.
		 * Buttons are implemented with jpanels instead of jbuttons simply because they did
		 * not appear properly with the colour scheme of the University of Alberta on Linux
		 * and MacOS. Most of the functionality is near the bottom of this class.
		 */
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(0, 124, 65));
		menuPanel.setBounds(0, 0, 180, 571);
		contentPane.add(menuPanel);
		menuPanel.setLayout(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(180, 0, 714, 571);
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

		JLabel authorIcon = new JLabel("");
		Image authorIconImg = new ImageIcon(this.getClass().getResource("/author.png")).getImage();
		authorIcon.setIcon(new ImageIcon(authorIconImg));
		authorIcon.setBounds(252, 212, 200, 200);
		welcomePanel.add(authorIcon);

		JPanel submissionPanel = new JPanel();
		submissionPanel.setBackground(Color.WHITE);
		contentPanel.add(submissionPanel, "name_46938297334300");
		submissionPanel.setLayout(null);

		JLabel submissionTitleLabel = new JLabel("New Submission");
		submissionTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		submissionTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		submissionTitleLabel.setBounds(24, 30, 350, 40);
		submissionPanel.add(submissionTitleLabel);

		JLabel titleLabel = new JLabel("Paper Title");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
		titleLabel.setBounds(24, 90, 350, 18);
		submissionPanel.add(titleLabel);

		JTextArea titleTextArea = new JTextArea();
		titleTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		titleTextArea.setBounds(24, 125, 650, 30);
		titleTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(titleTextArea);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.BLACK);
		titlePanel.setBounds(24, 155, 650, 2);
		submissionPanel.add(titlePanel);

		JLabel authorsLabel = new JLabel("Authors");
		authorsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		authorsLabel.setBounds(24, 170, 350, 18);
		submissionPanel.add(authorsLabel);

		JTextArea authorsTextArea = new JTextArea();
		authorsTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		authorsTextArea.setBounds(24, 225, 650, 30);
		authorsTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);

		JLabel authorsextraLabel = new JLabel("(seperate with commas)");
		authorsextraLabel.setForeground(Color.DARK_GRAY);
		authorsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		authorsextraLabel.setBounds(24, 190, 350, 18);
		submissionPanel.add(authorsextraLabel);
		submissionPanel.add(authorsTextArea);

		JPanel authorPanel = new JPanel();
		authorPanel.setBackground(Color.BLACK);
		authorPanel.setBounds(24, 255, 650, 2);
		submissionPanel.add(authorPanel);

		JLabel researchLabel = new JLabel("Research Subject");
		researchLabel.setFont(new Font("Arial", Font.BOLD, 14));
		researchLabel.setBounds(24, 270, 350, 18);
		submissionPanel.add(researchLabel);

		JTextArea researchTextArea = new JTextArea();
		researchTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		researchTextArea.setBounds(24, 305, 650, 30);
		researchTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(researchTextArea);

		JPanel researchPanel = new JPanel();
		researchPanel.setBackground(Color.BLACK);
		researchPanel.setBounds(24, 335, 650, 2);
		submissionPanel.add(researchPanel);

		JLabel prefreviewersLabel = new JLabel("Preferred Reviewers");
		prefreviewersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		prefreviewersLabel.setBounds(24, 350, 350, 18);
		submissionPanel.add(prefreviewersLabel);
		
		DefaultListModel<SubmissionObject> prefreviewersModel = new DefaultListModel<>();
		JList<SubmissionObject> prefreviewersList = new JList<SubmissionObject>(prefreviewersModel);

		prefreviewersList.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane prefreviewersListScrollPane = new JScrollPane(prefreviewersList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		prefreviewersListScrollPane.setSize(650, 55);
		prefreviewersListScrollPane.setLocation(24, 385);
		prefreviewersListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		submissionPanel.add(prefreviewersListScrollPane);

		JPanel prefreviewersPanel = new JPanel();
		prefreviewersPanel.setBackground(Color.BLACK);
		prefreviewersPanel.setBounds(24, 440, 650, 2);
		submissionPanel.add(prefreviewersPanel);

		JLabel filelocationLabel = new JLabel("File Location");
		filelocationLabel.setFont(new Font("Arial", Font.BOLD, 14));
		filelocationLabel.setBounds(24, 455, 350, 18);
		submissionPanel.add(filelocationLabel);

		
		JTextArea filelocationTextArea = new JTextArea();

		filelocationTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		filelocationTextArea.setBounds(24, 490, 650, 30);
		submissionPanel.add(filelocationTextArea);

		JPanel listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);
		contentPanel.add(listPanel, "name_93462556713100");
		listPanel.setLayout(null);

		JLabel listTitleLabel = new JLabel("Submissions List");
		listTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		listTitleLabel.setBounds(24, 30, 350, 40);
		listPanel.add(listTitleLabel);

		JLabel priorsubLabel = new JLabel("Prior Submissions");
		priorsubLabel.setFont(new Font("Arial", Font.BOLD, 14));
		priorsubLabel.setBounds(24, 90, 350, 18);
		listPanel.add(priorsubLabel);

		JPanel listSepPanel = new JPanel();
		listSepPanel.setBackground(Color.BLACK);
		listSepPanel.setBounds(24, 270, 650, 2);
		listPanel.add(listSepPanel);

		JLabel detailsLabel = new JLabel("Submission Details");
		detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		detailsLabel.setBounds(24, 285, 350, 18);
		listPanel.add(detailsLabel);

		JLabel detailsextraLabel = new JLabel("(select a paper above)");
		detailsextraLabel.setForeground(Color.DARK_GRAY);
		detailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		detailsextraLabel.setBounds(24, 305, 350, 18);
		listPanel.add(detailsextraLabel);

		JLabel titleListLabel = new JLabel("Title");
		titleListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		titleListLabel.setBounds(24, 350, 325, 14);
		listPanel.add(titleListLabel);

		JLabel authorsListLabel = new JLabel("Authors");
		authorsListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		authorsListLabel.setBounds(24, 410, 325, 14);
		listPanel.add(authorsListLabel);

		JLabel reviewersListLabel = new JLabel("Assigned Reviewers");
		reviewersListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		reviewersListLabel.setBounds(359, 350, 315, 14);
		listPanel.add(reviewersListLabel);

		JLabel feedbackListLabel = new JLabel("Feedback Available");
		feedbackListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		feedbackListLabel.setBounds(359, 410, 315, 14);
		listPanel.add(feedbackListLabel);

		JLabel deadlineListLabel = new JLabel("Final Submission Deadline");
		deadlineListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		deadlineListLabel.setBounds(359, 470, 315, 14);
		listPanel.add(deadlineListLabel);

		JLabel subjectListLabel = new JLabel("Research Subject");
		subjectListLabel.setFont(new Font("Arial", Font.BOLD, 12));
		subjectListLabel.setBounds(24, 470, 325, 14);
		listPanel.add(subjectListLabel);

		JLabel datatitleListLabel = new JLabel("");
		datatitleListLabel.setForeground(new Color(0, 0, 0));
		datatitleListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		datatitleListLabel.setBounds(24, 380, 325, 14);
		listPanel.add(datatitleListLabel);

		JLabel datareviewersListLabel = new JLabel("");
		datareviewersListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		datareviewersListLabel.setBounds(359, 380, 315, 14);
		listPanel.add(datareviewersListLabel);

		JLabel dataauthorsListLabel = new JLabel("");
		dataauthorsListLabel.setForeground(Color.BLACK);
		dataauthorsListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		dataauthorsListLabel.setBounds(24, 440, 325, 14);
		listPanel.add(dataauthorsListLabel);

		JLabel datafeedbackListLabel = new JLabel("");
		datafeedbackListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		datafeedbackListLabel.setBounds(359, 440, 315, 14);
		listPanel.add(datafeedbackListLabel);

		JLabel datadeadlineListLabel = new JLabel("");
		datadeadlineListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		datadeadlineListLabel.setBounds(359, 500, 315, 14);
		listPanel.add(datadeadlineListLabel);

		JLabel datasubjectListLabel = new JLabel("");
		datasubjectListLabel.setForeground(Color.BLACK);
		datasubjectListLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		datasubjectListLabel.setBounds(24, 500, 325, 14);
		listPanel.add(datasubjectListLabel);

		DefaultListModel<SubmissionObject> submissionModel = new DefaultListModel<>();
		JList<SubmissionObject> submissionList = new JList<SubmissionObject>(submissionModel);

		submissionList.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane submissionListScrollPane = new JScrollPane(submissionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		submissionListScrollPane.setSize(650, 143);
		submissionListScrollPane.setLocation(24, 115);
		submissionListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		listPanel.add(submissionListScrollPane);

		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setBackground(Color.WHITE);
		contentPanel.add(feedbackPanel, "name_47558521480000");
		feedbackPanel.setLayout(null);

		JLabel feedbackTitleLabel = new JLabel("Review Feedback");
		feedbackTitleLabel.setBounds(24, 30, 350, 40);
		feedbackPanel.add(feedbackTitleLabel);
		feedbackTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JLabel papersLabel = new JLabel("Papers With Feedback");
		papersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		papersLabel.setBounds(24, 90, 350, 18);
		feedbackPanel.add(papersLabel);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> paperList = new JList<SubmissionObject>(paperModel);
		JTextArea feedbackTextArea = new JTextArea();
		feedbackTextArea.setEditable(false);
		feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		

		
		paperList.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane feedbackListScrollPane = new JScrollPane(paperList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackListScrollPane.setSize(650, 143);
		feedbackListScrollPane.setLocation(24, 115);
		feedbackListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackListScrollPane);

		JPanel feedbackSepPanel = new JPanel();
		feedbackSepPanel.setBackground(Color.BLACK);
		feedbackSepPanel.setBounds(24, 270, 650, 2);
		feedbackPanel.add(feedbackSepPanel);

		JLabel paperfeedbackLabel = new JLabel("Feedback");
		paperfeedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
		paperfeedbackLabel.setBounds(24, 285, 350, 18);
		feedbackPanel.add(paperfeedbackLabel);

		JLabel paperfeedbackextraLabel = new JLabel("(select a paper above)");
		paperfeedbackextraLabel.setForeground(Color.DARK_GRAY);
		paperfeedbackextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		paperfeedbackextraLabel.setBounds(24, 305, 350, 18);
		feedbackPanel.add(paperfeedbackextraLabel);

		JPanel resubmitButton = new JPanel();
		JLabel resubmitLabel = new JLabel("Resubmit");
		resubmitLabel.setForeground(Color.WHITE);
		resubmitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		resubmitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resubmitLabel.setBounds(0, 0, 119, 30);
		resubmitButton.add(resubmitLabel);
		
		resubmitButton.setVisible(false);

		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setSize(650, 180);
		feedbackScrollPane.setLocation(24, 330);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());

		feedbackPanel.add(feedbackScrollPane);
		resubmitButton.setBackground(new Color(0, 124, 65));
		resubmitButton.setBounds(555, 520, 119, 30);
		feedbackPanel.add(resubmitButton);
		resubmitButton.setLayout(null);

		JLabel menuLabel = new JLabel("MENU");
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(0, 30, 180, 40);
		menuPanel.add(menuLabel);

		JPanel submissionButton = new JPanel();
		JLabel submissionLabel = new JLabel("New Submission");
		submissionLabel.setForeground(Color.WHITE);
		submissionLabel.setFont(new Font("Arial", Font.BOLD, 14));
		submissionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submissionLabel.setBounds(0, 0, 180, 30);
		submissionButton.add(submissionLabel);
		submissionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submissionButton.setBackground(new Color(255, 219, 5));
				submissionLabel.setForeground(Color.BLACK);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {

				submissionButton.setBackground(new Color(0, 124, 65));
				submissionLabel.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {

				submissionList.clearSelection();
				paperList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();


				contentPanel.add(submissionPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		JPanel menuSepPanel = new JPanel();
		menuSepPanel.setBounds(10, 70, 160, 2);
		menuPanel.add(menuSepPanel);
		submissionButton.setBackground(new Color(0, 124, 65));
		submissionButton.setBounds(0, 80, 180, 30);
		menuPanel.add(submissionButton);
		submissionButton.setLayout(null);

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

				submissionList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();
				
				contentPanel.add(feedbackPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

		JPanel listButton = new JPanel();
		JLabel listLabel = new JLabel("Submissions List");
		listLabel.setHorizontalAlignment(SwingConstants.CENTER);
		listLabel.setForeground(Color.WHITE);
		listLabel.setFont(new Font("Arial", Font.BOLD, 14));
		listLabel.setBounds(0, 0, 180, 30);
		listButton.add(listLabel);
		feedbackButton.setLayout(null);
		feedbackButton.setBackground(new Color(0, 124, 65));
		feedbackButton.setBounds(0, 150, 180, 30);
		menuPanel.add(feedbackButton);
		listButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				listButton.setBackground(new Color(255, 219, 5));
				listLabel.setForeground(Color.BLACK);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {

				listButton.setBackground(new Color(0, 124, 65));
				listLabel.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {

				paperList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();


				contentPanel.add(listPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});
		listButton.setLayout(null);
		listButton.setBackground(new Color(0, 124, 65));
		listButton.setBounds(0, 115, 180, 30);
		menuPanel.add(listButton);



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
		logoutButton.setBackground(new Color(0, 124, 65));
		logoutButton.setBounds(0, 379, 180, 30);
		menuPanel.add(logoutButton);
		logoutButton.setLayout(null);

		JLabel ualogo = new JLabel("");
		Image ualogoImg = new ImageIcon(this.getClass().getResource("/ualogo.jpg")).getImage();
		ualogo.setBounds(40, 420, 100, 100);
		menuPanel.add(ualogo);
		ualogo.setIcon(new ImageIcon(ualogoImg));

		JPanel submitButton = new JPanel();
		submitButton.setBackground(new Color(0, 124, 65));
		submitButton.setBounds(555, 520, 119, 30);
		submissionPanel.add(submitButton);
		submitButton.setLayout(null);

		JLabel submitLabel = new JLabel("Submit");
		

		submitLabel.setBounds(0, 0, 119, 30);
		submitButton.add(submitLabel);
		submitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitLabel.setForeground(Color.WHITE);
		submitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		
		// FILE LOCATION FOR NEW SUBMISSION
		filelocationTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.CANCEL_OPTION) {

				} else {
					File file = fc.getSelectedFile();
					filename = file.getName();
					filelocationTextArea.setText(file.getAbsolutePath());
				}
			}
		});

		
		//populates list of submissions
		for(int i=0;i<submissions.length;i++) {
			submissionModel.addElement(submissions[i]);
		}
		
		// THIS SECTION HANDLES SUBMISSION DETAILS
		submissionList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				//get indices of selected papers
				int[] selectedPaper = submissionList.getSelectedIndices();
				
				//check if only one paper is selected
				if (selectedPaper.length == 1) {

					//index of selected paper
					int paperIndex = selectedPaper[0];

					//displays the details of selected paper
					datatitleListLabel.setText(submissions[paperIndex].submissionName);
					dataauthorsListLabel.setText(submissions[paperIndex].submissionAuthors);
					datasubjectListLabel.setText(submissions[paperIndex].subject);

					//the following values may be null so they require checks
					if (submissions[paperIndex].reviewerIDs == null)
						datareviewersListLabel.setText("No Reviewers Assigned");
					else
						datareviewersListLabel.setText(submissions[paperIndex].reviewerNames);

					if (submissions[paperIndex].feedbackIDs == null)
						datafeedbackListLabel.setText("No Feedback Available");
					else
						datafeedbackListLabel.setText("Feedback Available");

					if (submissions[paperIndex].submissionDeadline == null)
						datadeadlineListLabel.setText("Not yet set");
					else
						datadeadlineListLabel.setText(submissions[paperIndex].submissionDeadline);

				} else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					submissionList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (selectedPaper.length == 0) {

					datatitleListLabel.setText("");
					dataauthorsListLabel.setText("");
					datasubjectListLabel.setText("");
					datareviewersListLabel.setText("");
					datafeedbackListLabel.setText("");
					datadeadlineListLabel.setText("");
				}
			}
		});

		// populates feedback list with papers that have feedback
		for(int i=0;i<submissions.length;i++) {
			if(submissions[i].submissionStage == RESUBMIT_STAGE)
				paperModel.addElement(submissions[i]);
		}

		//Gets feedback for selected submission
		paperList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				//Clear any existing text in the feedback area
				feedbackTextArea.setText("");
				
				//get indices of selected submission
				int[] selectedPaper = paperList.getSelectedIndices();
				
				//check if only one paper is selected
				if (selectedPaper.length == 1) {

					//gets selected paper as an object
					SubmissionObject paperOfInterest = paperModel.getElementAt(selectedPaper[0]);
					
					//Gets submission name
					paperInDetail = paperOfInterest.submissionName;
					
					//get filename of submission for resubmit button
					resubmitFilename = paperOfInterest.filename;
					
					//filename of feedback file
					String feedbackFile = "submissions/" + userID + "/feedback/" + paperInDetail + ".txt";


					
					//print feedback to textarea
					Scanner feedback;

					try {

						feedback = new Scanner(new File(feedbackFile));

						while (feedback.hasNext()) {

							feedbackTextArea.append(feedback.nextLine());
							feedbackTextArea.append("\n");
						}

						feedbackTextArea.setCaretPosition(0);
						feedback.close();
						
						if(paperOfInterest.submissionDeadline != null) {
							
							//checks if current date is before deadline and makes
							//resubmit button visible if true and not visible if false
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Date today = new Date();
							try {
								Date deadline = format.parse(submissions[selectedPaper[0]].submissionDeadline);
								if(today.before(deadline))
									resubmitButton.setVisible(true);
								else
									resubmitButton.setVisible(false);
								
							} catch (ParseException e) {e.printStackTrace();}
							
							}
						
					} catch (FileNotFoundException e) {
					}

				} else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					paperList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}

		});

		// RESUBMIT BUTTON LOGIC
		resubmitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				resubmitButton.setBackground(new Color(255, 219, 5));
				resubmitLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				resubmitButton.setBackground(new Color(0, 124, 65));
				resubmitLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.CANCEL_OPTION) {

				} else {
					File resubFile = fc.getSelectedFile();
					Path newsource = Paths.get(resubFile.getAbsolutePath());
					Path newdest = Paths.get(userFolder + "/" + resubmitFilename);
					try {
						Files.copy(newsource, newdest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {

					}
				}
			}
		});

		// THIS SECTION HANDLES NEW SUBMISSION LOGIC
		submitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submitButton.setBackground(new Color(255, 219, 5));
				submitLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				submitButton.setBackground(new Color(0, 124, 65));
				submitLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//Checks if any text area is empty and displays error code if true
				if (titleTextArea.getText().isEmpty() || authorsTextArea.getText().isEmpty()
						|| researchTextArea.getText().isEmpty() || filelocationTextArea.getText().isEmpty()) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Ensure all fields are filled out correctly",
							"Missing Submission Details", JOptionPane.PLAIN_MESSAGE, null);
				} else {

					//Gets all required details ready for submission into SQL database
					newTitle = titleTextArea.getText();
					newAuthors = authorsTextArea.getText();
					//newAuthorsArray = newAuthors.split("\\s*,\\s*");
					newSubject = researchTextArea.getText();
					//newPrefreviewers = prefreviewersTextArea.getText();
					//newPrefreviewersArray = newPrefreviewers.split("\\s*,\\s*");
					filelocation = filelocationTextArea.getText();

					/*
					//Legacy code from file i/o era
					if (!submissionModel.contains(filename)) {
						try {
							FileWriter fw = new FileWriter(userSubmissionList, true);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter pw = new PrintWriter(bw);
							pw.println(filename);
							pw.close();
						} catch (IOException e) {

						}
					}

					try {
						FileWriter fw = new FileWriter(userDetails + "/" + filename.split("\\.")[0] + ".txt", false);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter pw = new PrintWriter(bw);
						pw.println(newTitle + "|" + newAuthors + "|" + newSubject);
						pw.close();
					} catch (IOException e) {

					}
					 */
					
					//Copy file to user submissions folder
					Path source = Paths.get(filelocation);
					Path dest = Paths.get(userFolder + "/" + filename);
					try {
						Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
					}

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					//Submits SQL update with new submission
					makeSubmission(newTitle, newAuthors, newSubject, filename);

					JOptionPane.showMessageDialog(null, "Thank you for your submission!\n It will be reviewed shortly.",
							"Submission Accepted", JOptionPane.PLAIN_MESSAGE, null);

					//refresh list of new submissions
					getSubmissions();
					getReviewers();
					populateSubmissions();
					submissionModel.removeAllElements();
					for(int i=0;i<submissions.length;i++) {
						submissionModel.addElement(submissions[i]);
					}

				}

			}
		});

	}


	/**
	 * Gets reviewers from an sql query and stores in a global ResultSet
	 */
	private void getReviewers() {
		PreparedStatement ps;

		String query = "SELECT * FROM users WHERE usertype = ?";

		try {
			ps=SQLConnection.getConnection().prepareStatement(query);

			ps.setInt(1, 2);

			reviewerSet=ps.executeQuery();
		}catch(Exception e) {System.out.println(e); System.out.println("Failure searching for reviewers");}
	}


	/**
	 * Gets user's submissions from an sql query and stores in a global ResultSet
	 */
	private void getSubmissions() {
		PreparedStatement ps;

		String query = "SELECT * FROM submission WHERE submissionUserID = ?";

		try {
			ps=SQLConnection.getConnection().prepareStatement(query);

			ps.setInt(1, this.userID);

			submissionSet=ps.executeQuery();
		}catch(Exception e) {System.out.println(e); System.out.println("Failure searching for user submissions");}
	}


	/**
	 * Populates a global array of SubmissionObjects to store results of initial SQL
	 * query for user submissions, eliminating the need to constantly query SQL
	 * database
	 */
	private void populateSubmissions() {

		try {

			// Get number of submissions retrieved in order to set size of submissions array
			int numOfSubmissions = 0;
			while (submissionSet.next())
				numOfSubmissions++;

			// i is a counter for iterating over submissions array
			int i = 0;

			// sets size of submissions array
			submissions = new SubmissionObject[numOfSubmissions];

			// resets position of submissionSet ResultSet
			submissionSet.beforeFirst();

			// iterates over submission ResultSet and submissions array, populating the
			// array
			while (submissionSet.next()) {
				// get all details of each submission for constructor
				int submissionID = submissionSet.getInt("submissionID");
				String submissionName = submissionSet.getString("submissionName");
				String submissionAuthors = submissionSet.getString("submissionAuthors");
				String subject = submissionSet.getString("subject");
				String submissionDate = submissionSet.getString("submissionDate");
				int submissionStage = submissionSet.getInt("submissionStage");
				String filename = submissionSet.getString("filename");
				int submissionUserID = submissionSet.getInt("submissionUserID");
				
				// SubmissionObject constructor call
				submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject,
						submissionDate, submissionStage, filename, submissionUserID);

				// get extra details which may be null
				String submissionDeadline = submissionSet.getString("submissionDeadline");
				String reviewerIDs = submissionSet.getString("reviewerIDs");
				String feedbackIDs = submissionSet.getString("feedbackIDs");
				String preferredReviewerIDs = submissionSet.getString("preferredReviewerIDs");

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

				// call SubmissionObject method that retrieves reviewer names from reviewer IDs
				submissions[i].setReviewerNames();

				// increment counter
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Populates a global array of FeedbackObjects to store on startup so loading
	 * between feedback is fast. 
	 * 
	 * Currently unused
	 */
	private void getFeedback() {
		
		PreparedStatement ps;
		ResultSet feedbackSet;
		String query = "SELECT * FROM feedback WHERE submissionID = ?";
		
		try {
			
			for(int i=0;i<submissions.length;i++) {
				ps = SQLConnection.getConnection().prepareStatement(query);
				ps.setInt(1, submissions[i].submissionID);
				feedbackSet = ps.executeQuery();
				
				int counter = 0;
				
				//populates array of feedback
				while(feedbackSet.next()) {
					int feedbackID = feedbackSet.getInt("feedbackID");
					String feedbackDate = feedbackSet.getString("feedbackDate");
					String filename = feedbackSet.getString("filename");
					int userID = feedbackSet.getInt("userID");
					int submissionID = feedbackSet.getInt("submissionID");
					int approval = feedbackSet.getInt("approval");
					int feedbackStage = feedbackSet.getInt("feedbackStage");
					
					feedback[counter] = new FeedbackObject(feedbackID, feedbackDate, filename, userID, submissionID, feedbackStage, approval);

					submissionIDtoFeedbackApproval.put(submissions[i].submissionID, feedbackSet.getInt("approval"));

					numOfFeedback++;
					counter++;
				}
			}
		}catch(Exception e) {System.out.println("Failed fetching feedback");
		e.printStackTrace();}

	}
	
	
	/**
	 * Method for adding a new submission to the database
	 * 
	 * @param submissionName
	 * @param submissionAuthors
	 * @param subject
	 * @param filename
	 * @return
	 */
	private int makeSubmission(String submissionName, String submissionAuthors, String subject, String filename) {
		PreparedStatement ps;
		String query = "INSERT INTO submission (submissionName, submissionAuthors, subject, submissionStage, "
				+ "filename, submissionUserID) values (? , ? , ? , ? , ? , ?)";
		

		
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setString(1, submissionName);
			ps.setString(2, submissionAuthors);
			ps.setString(3, subject);
			ps.setInt(4, 1);
			ps.setString(5, filename);
			ps.setInt(6, this.userID);
			
			ps.execute();
			
		}catch(Exception e) {e.printStackTrace();}
		
		
		return 0;
	}
}

	
