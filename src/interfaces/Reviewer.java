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
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;

import objects.GUIObjects;
import objects.SQLConnection;
import objects.SubmissionObject;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

/*
 * Reviewer contains GUI elements and functionality to implement the requirements described
 * in our user stories. It handles reviewer nominations as well as feedback for the papers
 * that they are assigned to.
 * 
 * @author L01-Group20
 */
public class Reviewer extends JFrame implements Constants {

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
	protected String[] subjects = new String[100];
	private int[] selectedPaper = new int[0];
	private int[] selected = new int[0];
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reviewer frame = new Reviewer("Test Reviewer", 8, SQLConnection.getConnection());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the reviewer frame.
	 * 
	 * @param user, User's name (not username)
	 * @param ID, user's userID (invisible to user)
	 * @param conn, The connection passed in from the Login page that is established to the database
	 */
	public Reviewer(String user, int ID, Connection conn) {
		this.userID = ID;
		this.conn = conn;
		
		// get list of subjects and submissions, for each subject
		getSubjects();
		getSubmissions();
		populateSubmissions();

		setTitle("Journal Submission System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

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

		JLabel reviewerWelcome = new GUIObjects().welcomeLabel(user);
		welcomePanel.add(reviewerWelcome);

		JLabel reviewerIcon = new GUIObjects().icon("/reviewer.jpg");
		reviewerIcon.setBounds(184, 212, 346, 200);
		welcomePanel.add(reviewerIcon);

		/*
		 * Creating the browse panel and filling it with its elements using GUIObjects
		 */
		JPanel browsePanel = new GUIObjects().contentPanel();
		contentPanel.add(browsePanel, "name_35648043125700");

		JLabel browseTitle = new GUIObjects().contentTitle("Browse Journals");
		browsePanel.add(browseTitle);

		JLabel subjectHeader = new GUIObjects().contentHeader("Research Subjects", 90);
		browsePanel.add(subjectHeader);

		DefaultListModel<String> subjectModel = new DefaultListModel<>();
		JList<String> subjectList = new JList<String>(subjectModel);
		subjectList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane subjectListScrollPane = new JScrollPane(subjectList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		subjectListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		subjectListScrollPane.setBounds(24, 115, 650, 143);
		browsePanel.add(subjectListScrollPane);

		JPanel browseSeparator = new GUIObjects().separatorPanel(270);
		browsePanel.add(browseSeparator);

		JLabel reviewHeader = new GUIObjects().contentHeader("Choose A Paper You Want To Review", 285);
		browsePanel.add(reviewHeader);

		JLabel reviewSubHeader = new GUIObjects().contentSubHeader("(select a subject above)", 305);
		browsePanel.add(reviewSubHeader);

		DefaultListModel<SubmissionObject> nominateModel = new DefaultListModel<>();
		JList<SubmissionObject> nominateList = new JList<SubmissionObject>(nominateModel);
		nominateList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane nominateListScrollPane = new JScrollPane(nominateList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		nominateListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		nominateListScrollPane.setBounds(24, 330, 650, 180);
		nominateList.removeAll();
		browsePanel.add(nominateListScrollPane);

		JPanel nominateButton = new GUIObjects().contentButton(555, 520);
		JLabel nominateLabel = new GUIObjects().contentButtonLabel("Nominate");
		nominateButton.add(nominateLabel);
		browsePanel.add(nominateButton);

		/*
		 * Creating the feedback panel and filling it with its elements using GUIObjects
		 */
		JPanel feedbackPanel = new GUIObjects().contentPanel();
		contentPanel.add(feedbackPanel, "name_47558521480000");

		JLabel feedbackTitle = new GUIObjects().contentTitle("Provide Feedback");
		feedbackPanel.add(feedbackTitle);

		JLabel paperHeader = new GUIObjects().contentHeader("Assigned Papers", 90);
		feedbackPanel.add(paperHeader);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> assignedList = new JList<SubmissionObject>(paperModel);
		assignedList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane assignedListScrollPane = new JScrollPane(assignedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		assignedListScrollPane.setBounds(24, 115, 650, 110);
		assignedListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(assignedListScrollPane);

		JPanel openButton = new GUIObjects().contentButton(555, 235);
		JLabel openLabel = new GUIObjects().contentButtonLabel("Open Paper");
		openButton.add(openLabel);
		feedbackPanel.add(openButton);

		JPanel feedbackSeparator = new GUIObjects().separatorPanel(270);
		feedbackPanel.add(feedbackSeparator);

		JLabel provideHeader = new GUIObjects().contentHeader("Provide Feedback Below", 285);
		feedbackPanel.add(provideHeader);

		JLabel provideSubHeader = new GUIObjects().contentSubHeader("(for the paper you have selected above)", 305);
		feedbackPanel.add(provideSubHeader);

		JTextArea feedbackTextArea = new GUIObjects().contentTextArea(0);
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setBounds(24, 330, 650, 180);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackScrollPane);

		JPanel submitButton = new GUIObjects().contentButton(555, 520);
		JLabel submitLabel = new GUIObjects().contentButtonLabel("Submit Feedback");
		submitButton.add(submitLabel);
		feedbackPanel.add(submitButton);

		/*
		 * Creating menu buttons to switch to different content panels
		 */
		JPanel browseMenuButton = new GUIObjects().menuButton(80, 30);
		JLabel browseMenuLabel = new GUIObjects().menuLabel("Browse Journals", 0);
		browseMenuButton.add(browseMenuLabel);
		menuPanel.add(browseMenuButton);

		JPanel feedbackMenuButton = new GUIObjects().menuButton(115, 30);
		JLabel feedbackMenuLabel = new GUIObjects().menuLabel("Provide Feedback", 0);
		feedbackMenuButton.add(feedbackMenuLabel);
		menuPanel.add(feedbackMenuButton);

		JPanel logoutMenuButton = new GUIObjects().menuButton(380, 30);
		JLabel logoutMenuLabel = new GUIObjects().menuLabel("Logout", 0);
		logoutMenuButton.add(logoutMenuLabel);
		menuPanel.add(logoutMenuButton);

		/*
		 * Menu button logic follows for each menu button. Each one switches to their
		 * respective card/panel containing all the GUI elements.
		 */
		browseMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				browseMenuButton.setBackground(new Color(255, 219, 5));
				browseMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				browseMenuButton.setBackground(new Color(0, 124, 65));
				browseMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				assignedList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				contentPanel.add(browsePanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

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

				subjectList.clearSelection();
				nominateList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				contentPanel.add(feedbackPanel);
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});

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
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				dispose();
				Login.main(null);
			}
		});

		// populates list of subjects
		for (int i = 0; i < subjects.length; i++)
			if (subjects[i] != null)
				subjectModel.addElement(subjects[i]);

		// SUBJECT LIST LOGIC
		subjectList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// Ensure mouse click causes an update only once
				if (!subjectList.getValueIsAdjusting()) {

					// clear elements in list of submissions
					nominateModel.removeAllElements();

					// get indices of selected subject
					int[] selectedSubject = subjectList.getSelectedIndices();

					// Ensure only one subject is selected and display the papers in that subject
					if (selectedSubject.length == 1) {
						for (int i = 0; i < submissions.length; i++)
							if(submissions[i]!=null)
								if (submissions[i].subject.equals(subjects[selectedSubject[0]]))
									nominateModel.addElement(submissions[i]);
								

					} else if (selectedSubject.length >= 2) {
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);
						JOptionPane.showMessageDialog(null, "Please Select Only 1 Subject",
								"Too Many Subjects Selected", JOptionPane.PLAIN_MESSAGE, null);
					}
				}
			}
		});

		// LOGIC FOR ASSIGNED PAPERS LIST
		// Only needs to ensure one paper is selected
		assignedList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// get indices of selected papers
				selectedPaper = assignedList.getSelectedIndices();
				if (selectedPaper.length == 1) {

					// nothing to put here really

				} else if (selectedPaper.length >= 2) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}
		});

		// populates list of assigned papers
		for (int i = 0; i < submissions.length; i++) {
			if(submissions[i]!=null)
				if (submissions[i].reviewers.get(this.userID) != null
						&& submissions[i].submissionStage == FEEDBACK_GATHERING_STAGE) {
					paperModel.addElement(submissions[i]);
			}
		}

		// OPEN PAPER BUTTON LOGIC
		openButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				openButton.setBackground(new Color(255, 219, 5));
				openLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				openButton.setBackground(new Color(0, 124, 65));
				openLabel.setForeground(Color.WHITE);
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
					SubmissionObject paperOfInterest = paperModel.getElementAt(selectedPaper[0]);

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

		// NOMINATE PAPER LIST LOGIC
		// just checks if more than 1 paper is selected and shows a warning
		nominateList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// get indices of selected papers
				selected = nominateList.getSelectedIndices();
				if (selected.length >= 2) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});

		// NOMINATE BUTTON LOGIC
		nominateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				nominateButton.setBackground(new Color(255, 219, 5));
				nominateLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				nominateButton.setBackground(new Color(0, 124, 65));
				nominateLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (selected.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected to nominate to.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {
					int submissionID = nominateModel.getElementAt(selected[0]).submissionID;

					// call method that sends sql update adding userID to nominated reviewers
					try {
						nominateReview(submissionID);

						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);
						JOptionPane.showMessageDialog(null, "Nomination received!.", "Nominated",
								JOptionPane.PLAIN_MESSAGE, null);
					} catch (Exception e) {
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);
						JOptionPane.showMessageDialog(null, "Failed to nominate.", "Error Nominating",
								JOptionPane.PLAIN_MESSAGE, null);
					}
				}
			}
		});

		// SUBMIT FEEDBACK BUTTON LOGIC
		submitButton.addMouseListener(new MouseAdapter() {
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
				if (selectedPaper.length == 0) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					JOptionPane.showMessageDialog(null, "No paper selected.", "No paper selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {
					if (!feedbackTextArea.getText().equals("")) {

						// gets specific paper selected as an object for easy retrieval of details
						SubmissionObject paperOfInterest = paperModel.getElementAt(selectedPaper[0]);

						// gets name of paper
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

						// appending to the feedback file
						try {
							FileWriter fw = new FileWriter(fileLocation, true);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter pw = new PrintWriter(bw);
							pw.println("\n");
							pw.println("Feedback from " + user + "\n");
							pw.println(feedbackTextArea.getText());
							pw.println("----------end of feedback from " + user + "----------");
							pw.close();

						} catch (Exception e) {
							e.printStackTrace();
						}
						// example final string format
						// file:///D:/Documents/GitHub/seng300-group20-project/submissions/3/Example%20paper2.pdf

						// Puts an entry into database
						submitFeedback(paperOfInterest.submissionID);

						JOptionPane.showMessageDialog(null,
								"Thank you for your feedback!\n It will be reviewed shortly.", "Feedback Accepted",
								JOptionPane.PLAIN_MESSAGE, null);

						// refresh list of papers requiring feedback
						paperModel.clear();
						getSubmissions();
						populateSubmissions();
						for (int i = 0; i < submissions.length; i++) {
							if(submissions[i]!=null)
								if (submissions[i].reviewers.get(userID) != null
										&& submissions[i].submissionStage == FEEDBACK_GATHERING_STAGE) {
									paperModel.addElement(submissions[i]);
							}
						}

					} else {

						JOptionPane.showMessageDialog(null, "Please ensure the feedback field is not blank",
								"Missing Feedback Details", JOptionPane.PLAIN_MESSAGE, null);

					}

				}

			}
		});

	}

	/**
	 * Get list of subjects by querying for unique strings in the sql column
	 */
	private void getSubjects() {
		PreparedStatement ps;
		ResultSet rs;

		String query = "SELECT DISTINCT subject FROM submission";

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			int count = 0;

			// iterates over returned subjects and populates an array with subjects
			while (rs.next()) {
				subjects[count] = rs.getString("subject");
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets user's submissions from an sql query and stores in a global ResultSet
	 */
	private void getSubmissions() {
		PreparedStatement ps;

		String query = "SELECT * FROM submission";

		try {
			ps = conn.prepareStatement(query);

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
	private void populateSubmissions() {

		try {
			// i is a counter for iterating over submissions array
			int i = 0;

			// sets size of submissions array
			submissions = new SubmissionObject[200];

			// resets position of submissionSet ResultSet

			// iterates over submission ResultSet and submissions array, populating the
			// array
			while (submissionSet.next()) {
				// get all details of each submission for constructor
				int submissionID = submissionSet.getInt("submissionID");
				if(submissionID>0) {
					String submissionName = submissionSet.getString("submissionName");
					String submissionAuthors = submissionSet.getString("submissionAuthors");
					String subject = submissionSet.getString("subject");
					int submissionStage = submissionSet.getInt("submissionStage");
					String filename = submissionSet.getString("filename");
					int submissionUserID = submissionSet.getInt("submissionUserID");
					String userEmail = submissionSet.getString("userEmail");
	
					// SubmissionObject constructor call
					submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject,
							submissionStage, filename, submissionUserID, userEmail);
	
					// get extra details which may be null
					String submissionDeadline = submissionSet.getString("submissionDeadline");
					String reviewerIDs = submissionSet.getString("reviewerIDs");
					String preferredReviewerIDs = submissionSet.getString("preferredReviewerIDs");
	
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
	
					// call SubmissionObject method that retrieves reviewer names from reviewer IDs
					submissions[i].setReviewerNames();
				}
				// increment counter
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the submission matching subject and submissionName and appends the
	 * userID to the nominatedReviewerIDs field
	 * 
	 * @param submissionID, The selected paper's associated submissionID
	 */
	private void nominateReview(int submissionID) {
		PreparedStatement ps;
		ResultSet rs;

		String query1 = "SELECT * FROM submission WHERE submissionID = ?";
		String query2 = "UPDATE submission SET nominatedReviewerIDs = ? WHERE submissionID = ?";

		try {
			ps = conn.prepareStatement(query1);
			ps.setInt(1, submissionID);

			rs = ps.executeQuery();

			rs.next();

			// check if this user is already nominated
			boolean isAlreadyNominated = false;
			// check if initial value of nominatedReviewerIDs is null
			boolean isNull = false;

			if (rs.getString("nominatedReviewerIDs") != null) {
				String[] nominated = rs.getString("nominatedReviewerIDs").split("[,]");
				for (int i = 0; i < nominated.length; i++)
					if (nominated[i].equals(Integer.toString(this.userID)))
						isAlreadyNominated = true;
			} else {
				isNull = true;
			}

			// appends userID if not already nominated
			if (!isAlreadyNominated) {
				String allNominated = new String();

				if (!isNull)
					allNominated = rs.getString("nominatedReviewerIDs") + "," + this.userID;
				else
					allNominated = Integer.toString(this.userID);

				ps = conn.prepareStatement(query2);
				ps.setString(1, allNominated);
				ps.setInt(2, submissionID);

				// To issue INSERT, UPDATE, or DELETE, java requires using executeUpdate()
				ps.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Submits a feedback item into SQL database if a feedback item for the
	 * submission in question does not already exist.
	 * 
	 * Performs 3 possible queries: 1. gets feedback entry to check if feedback
	 * already exists for the submission. If already exists, skip 2. 2. if no
	 * feedback entry already exists, inserts a new entry 3. updates submissionStage
	 * of the submission
	 * 
	 * NOTE that currently the feedback list is not used for anything
	 * 
	 * @param submissionID, The associated paper's submissionID that is receiving feedback
	 */
	private void submitFeedback(int submissionID) {
		PreparedStatement ps;

		String query2 = "UPDATE submission SET feedbackReceived = 1 WHERE submissionID = ?";

		try {
			ps = conn.prepareStatement(query2);
			ps.setInt(1, submissionID);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
