package interfaces;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

import java.awt.Font;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

import objects.GUIObjects;
import objects.ReviewerObject;
import objects.SQLConnection;
import objects.SubmissionObject;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

/*
 * Author contains GUI elements and functionality to implement the requirements described
 * in our user stories. It handles submitting new papers, tracking prior submissions
 * and feedback that has been provided by a reviewer.
 * 
 * @author L01-Group20
 */
public class Author extends JFrame implements Constants {

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
	protected String username;
	protected int userID;
	protected ResultSet reviewerSet;
	protected ResultSet submissionSet;
	protected SubmissionObject[] submissions;
	protected ReviewerObject[] reviewers;
	protected int numOfFeedback = 0;

	protected Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author frame = new Author("Test Author", 8, SQLConnection.getConnection());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the author frame.
	 * 
	 * @param user, User's name (not username)
	 * @param ID, user's userID (invisible to user)
	 * @param conn, The connection passed in from the Login page that is established to the database
	 */
	public Author(String username, int ID, Connection conn) {
		this.userID = ID;
		this.username = username;
		this.conn = conn;

		// Initial pull of user's data
		getSubmissions();
		getReviewers();
		populateSubmissions();
		// getFeedback();

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
		File authorFolder = new File(userFolder);
		File detailsFolder = new File(userDetails);
		File feedbackFolder = new File(userFeedback);
		File feedbackListFile = new File(userFeedbackList);
		File submissionListFile = new File(userSubmissionList);

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

		JLabel authorWelcome = new GUIObjects().welcomeLabel(username);
		welcomePanel.add(authorWelcome);

		JLabel authorIcon = new GUIObjects().icon("/author.png");
		authorIcon.setBounds(252, 212, 200, 200);
		welcomePanel.add(authorIcon);

		/*
		 * Creating the submission panel and filling it with its elements using
		 * GUIObjects
		 */
		JPanel submissionPanel = new GUIObjects().contentPanel();
		contentPanel.add(submissionPanel, "name_46938297334300");

		JLabel submissionTitle = new GUIObjects().contentTitle("New Submission");
		submissionPanel.add(submissionTitle);

		JLabel titleHeader = new GUIObjects().contentHeader("Paper Title", 90);
		submissionPanel.add(titleHeader);

		JTextArea titleTextArea = new GUIObjects().contentTextArea(125);
		titleTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(titleTextArea);

		JPanel titleSeparator = new GUIObjects().separatorPanel(155);
		submissionPanel.add(titleSeparator);

		JLabel authorsHeader = new GUIObjects().contentHeader("Authors", 170);
		submissionPanel.add(authorsHeader);

		JLabel authorsSubHeader = new GUIObjects().contentSubHeader("(seperate with commas)", 190);
		submissionPanel.add(authorsSubHeader);

		JTextArea authorsTextArea = new GUIObjects().contentTextArea(225);
		authorsTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(authorsTextArea);

		JPanel authorSeparator = new GUIObjects().separatorPanel(255);
		submissionPanel.add(authorSeparator);

		JLabel researchHeader = new GUIObjects().contentHeader("Research Subject", 270);
		submissionPanel.add(researchHeader);

		JTextArea researchTextArea = new GUIObjects().contentTextArea(305);
		researchTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(researchTextArea);

		JPanel researchSeparator = new GUIObjects().separatorPanel(335);
		submissionPanel.add(researchSeparator);

		JLabel preferredHeader = new GUIObjects().contentHeader("Preferred Reviewers", 350);
		submissionPanel.add(preferredHeader);

		DefaultListModel<ReviewerObject> prefreviewersModel = new DefaultListModel<>();
		JList<ReviewerObject> prefreviewersList = new JList<ReviewerObject>(prefreviewersModel);
		prefreviewersList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane prefreviewersListScrollPane = new JScrollPane(prefreviewersList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		prefreviewersListScrollPane.setBounds(24, 385, 650, 55);
		prefreviewersListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		submissionPanel.add(prefreviewersListScrollPane);

		JPanel preferredSeparator = new GUIObjects().separatorPanel(440);
		submissionPanel.add(preferredSeparator);

		JLabel filelocationHeader = new GUIObjects().contentHeader("PDF File Location", 455);
		submissionPanel.add(filelocationHeader);

		JTextArea filelocationTextArea = new GUIObjects().contentTextArea(490);
		filelocationTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(filelocationTextArea);

		JPanel submitButton = new GUIObjects().contentButton(555, 520);
		JLabel submitLabel = new GUIObjects().contentButtonLabel("Submit");
		submitButton.add(submitLabel);
		submissionPanel.add(submitButton);

		/*
		 * Creating the list panel and filling it with its elements using GUIObjects
		 */
		JPanel listPanel = new GUIObjects().contentPanel();
		contentPanel.add(listPanel, "name_93462556713100");

		JLabel listTitle = new GUIObjects().contentTitle("Submissions List");
		listPanel.add(listTitle);

		JLabel listHeader = new GUIObjects().contentHeader("Prior Submissions", 90);
		listPanel.add(listHeader);

		DefaultListModel<SubmissionObject> submissionModel = new DefaultListModel<>();
		JList<SubmissionObject> submissionList = new JList<SubmissionObject>(submissionModel);
		submissionList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane submissionListScrollPane = new JScrollPane(submissionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		submissionListScrollPane.setBounds(24, 115, 650, 143);
		submissionListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		listPanel.add(submissionListScrollPane);

		JPanel listSeparator = new GUIObjects().separatorPanel(270);
		listPanel.add(listSeparator);

		JLabel detailsHeader = new GUIObjects().contentHeader("Submission Details", 285);
		listPanel.add(detailsHeader);

		JLabel detailsSubHeader = new GUIObjects().contentSubHeader("(select a paper above)", 305);
		listPanel.add(detailsSubHeader);

		JLabel titleLabel = new GUIObjects().contentLabel("Title", 24, 350);
		listPanel.add(titleLabel);

		JLabel titleSubLabel = new GUIObjects().contentSubLabel(24, 380);
		listPanel.add(titleSubLabel);

		JLabel authorsLabel = new GUIObjects().contentLabel("Authors", 24, 410);
		listPanel.add(authorsLabel);

		JLabel authorsSubLabel = new GUIObjects().contentSubLabel(24, 440);
		listPanel.add(authorsSubLabel);

		JLabel subjectLabel = new GUIObjects().contentLabel("Research Subject", 24, 470);
		listPanel.add(subjectLabel);

		JLabel subjectSubLabel = new GUIObjects().contentSubLabel(24, 500);
		listPanel.add(subjectSubLabel);

		JLabel reviewersLabel = new GUIObjects().contentLabel("Assigned Reviewers", 359, 350);
		listPanel.add(reviewersLabel);

		JLabel reviewersSubLabel = new GUIObjects().contentSubLabel(359, 380);
		listPanel.add(reviewersSubLabel);

		JLabel feedbackavailableLabel = new GUIObjects().contentLabel("Feedback Available", 359, 410);
		listPanel.add(feedbackavailableLabel);

		JLabel feedbackavailableSubLabel = new GUIObjects().contentSubLabel(359, 440);
		listPanel.add(feedbackavailableSubLabel);

		JLabel deadlineLabel = new GUIObjects().contentLabel("Final Submission Deadline", 359, 470);
		listPanel.add(deadlineLabel);

		JLabel deadlineSubLabel = new GUIObjects().contentSubLabel(359, 500);
		listPanel.add(deadlineSubLabel);

		/*
		 * Creating the feedback panel and filling it with its elements using GUIObjects
		 */
		JPanel feedbackPanel = new GUIObjects().contentPanel();
		contentPanel.add(feedbackPanel, "name_47558521480000");

		JLabel feedbackTitle = new GUIObjects().contentTitle("Review Feedback");
		feedbackPanel.add(feedbackTitle);

		JLabel papersHeader = new GUIObjects().contentHeader("Papers With Feedback", 90);
		feedbackPanel.add(papersHeader);

		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> paperList = new JList<SubmissionObject>(paperModel);
		paperList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane feedbackListScrollPane = new JScrollPane(paperList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackListScrollPane.setBounds(24, 115, 650, 143);
		feedbackListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackListScrollPane);

		JPanel feedbackSeparator = new GUIObjects().separatorPanel(270);
		feedbackPanel.add(feedbackSeparator);

		JLabel feedbackHeader = new GUIObjects().contentHeader("Feedback", 285);
		feedbackPanel.add(feedbackHeader);

		JLabel feedbackSubHeader = new GUIObjects().contentSubHeader("(select a paper above)", 305);
		feedbackPanel.add(feedbackSubHeader);

		JTextArea feedbackTextArea = new GUIObjects().contentTextArea(0);
		feedbackTextArea.setEditable(false);
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setBounds(24, 330, 650, 180);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackScrollPane);

		JPanel resubmitButton = new GUIObjects().contentButton(555, 520);
		JLabel resubmitLabel = new GUIObjects().contentButtonLabel("Resubmit");
		resubmitButton.add(resubmitLabel);
		feedbackPanel.add(resubmitButton);
		resubmitButton.setVisible(false);

		/*
		 * Creating menu buttons to switch to different content panels
		 */
		JPanel submissionMenuButton = new GUIObjects().menuButton(80, 30);
		JLabel submissionMenuLabel = new GUIObjects().menuLabel("New Submission", 0);
		submissionMenuButton.add(submissionMenuLabel);
		menuPanel.add(submissionMenuButton);

		JPanel listMenuButton = new GUIObjects().menuButton(115, 30);
		JLabel listMenuLabel = new GUIObjects().menuLabel("Submissions List", 0);
		listMenuButton.add(listMenuLabel);
		menuPanel.add(listMenuButton);

		JPanel feedbackMenuButton = new GUIObjects().menuButton(150, 30);
		JLabel feedbackMenuLabel = new GUIObjects().menuLabel("Review Feedback", 0);
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
		submissionMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submissionMenuButton.setBackground(new Color(255, 219, 5));
				submissionMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				submissionMenuButton.setBackground(new Color(0, 124, 65));
				submissionMenuLabel.setForeground(Color.WHITE);
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

		listMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				listMenuButton.setBackground(new Color(255, 219, 5));
				listMenuLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				listMenuButton.setBackground(new Color(0, 124, 65));
				listMenuLabel.setForeground(Color.WHITE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				paperList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();

				submissionModel.clear();
				getSubmissions();
				populateSubmissions();
				
				// populates list of submissions
				for (int i = 0; i < submissions.length; i++) {
					if (submissions[i] != null)
						submissionModel.addElement(submissions[i]);
				}
				
				contentPanel.add(listPanel);
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

				submissionList.clearSelection();
				contentPanel.removeAll();
				contentPanel.repaint();
				contentPanel.revalidate();
				
				paperModel.clear();
				getSubmissions(5);
				populateSubmissions();
				
				// populates feedback list with papers that have feedback
				for (int i = 0; i < submissions.length; i++) {
					if (submissions[i] != null)
						if (submissions[i].submissionStage == RESUBMIT_STAGE)
							paperModel.addElement(submissions[i]);
				}

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

		// Populate preferred reviewers list
		for (int i = 0; i < reviewers.length; i++) {
			if (reviewers[i] != null)
				prefreviewersModel.addElement(reviewers[i]);
		}

		// FILE LOCATION FOR NEW SUBMISSION
		filelocationTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.CANCEL_OPTION) {

				} else {
					File file = fc.getSelectedFile();
					filename = titleTextArea.getText().replaceAll("\\s+", "").replaceAll("/*", "") + ".pdf";
					filelocationTextArea.setText(file.getAbsolutePath());
				}
			}
		});

		// THIS SECTION HANDLES NEW SUBMISSION LOGIC
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

				// Checks if any text area is empty and displays error code if true
				if (titleTextArea.getText().isEmpty() || authorsTextArea.getText().isEmpty()
						|| researchTextArea.getText().isEmpty() || filelocationTextArea.getText().isEmpty()) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Ensure all fields are filled out correctly",
							"Missing Submission Details", JOptionPane.PLAIN_MESSAGE, null);
				} else {

					// Gets all required details ready for submission into SQL database
					newTitle = titleTextArea.getText();
					newAuthors = authorsTextArea.getText();
					newSubject = researchTextArea.getText();
					int[] reviewerIndices = prefreviewersList.getSelectedIndices();

					StringBuilder reviewerIDs = new StringBuilder();
					String reviewerID = new String();
					// Up to 20 reviewers can be selected
					ReviewerObject[] selectedReviewers = new ReviewerObject[20];

					if (reviewerIndices.length > 0) {

						// iterates over selected reviewers' indices and adds each selected reviewer to
						// the array of reviewer objects, selectedReviewers
						for (int i = 0; i < reviewerIndices.length; i++) {
							selectedReviewers[i] = new ReviewerObject();
							selectedReviewers[i] = prefreviewersModel.getElementAt(reviewerIndices[i]);
						}

						// StringBuilder to build the string that will be sent to the SQL database
						// Will be of the form ID1,ID2,ID3, etc. ending with a reviewer ID and not a
						// comma
						for (int i = 0; i < selectedReviewers.length; i++) {
							if (selectedReviewers[i] != null) {
								reviewerIDs.append(selectedReviewers[i].userID + ",");
							}
						}
						reviewerIDs.setLength(reviewerIDs.length() - 1);

						reviewerID = reviewerIDs.toString();

					} else
						reviewerID = null;

					filelocation = filelocationTextArea.getText();

					// Copy file to user submissions folder
					Path source = Paths.get(filelocation);
					Path dest = Paths.get(userFolder + "/" + filename.replaceAll("\\s+", ""));
					try {
						Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
					}

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					// Submits SQL update with new submission
					makeSubmission(newTitle, newAuthors, newSubject, filename.replaceAll("\\s+", ""), reviewerID);

					JOptionPane.showMessageDialog(null, "Thank you for your submission!\n It will be reviewed shortly.",
							"Submission Accepted", JOptionPane.PLAIN_MESSAGE, null);

					// refresh list of new submissions
					getSubmissions();
					getReviewers();
					populateSubmissions();
					submissionModel.removeAllElements();
					for (int i = 0; i < submissions.length; i++) {
						if (submissions[i] != null)
							submissionModel.addElement(submissions[i]);
					}

				}

			}
		});

	

		// THIS SECTION HANDLES SUBMISSION DETAILS
		submissionList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// get indices of selected papers
				int[] selectedPaper = submissionList.getSelectedIndices();

				// check if only one paper is selected
				if (selectedPaper.length == 1) {

					// index of selected paper
					int paperIndex = selectedPaper[0];

					// displays the details of selected paper
					titleSubLabel.setText(submissions[paperIndex].submissionName);
					authorsSubLabel.setText(submissions[paperIndex].submissionAuthors);
					subjectSubLabel.setText(submissions[paperIndex].subject);

					// the following values may be null so they require checks
					if (submissions[paperIndex].reviewerIDs == null)
						reviewersSubLabel.setText("No Reviewers Assigned");
					else
						reviewersSubLabel.setText(submissions[paperIndex].reviewerNames);

					if (submissions[paperIndex].submissionStage < RESUBMIT_STAGE)
						feedbackavailableSubLabel.setText("No Feedback Available");
					else
						feedbackavailableSubLabel.setText("Feedback Available");

					if (submissions[paperIndex].submissionDeadline == null)
						deadlineSubLabel.setText("Not yet set");
					else
						deadlineSubLabel.setText(submissions[paperIndex].submissionDeadline);

				} else if (selectedPaper.length >= 2) {

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					submissionList.clearSelection();
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected",
							JOptionPane.PLAIN_MESSAGE, null);
				} else if (selectedPaper.length == 0) {

					titleSubLabel.setText("");
					authorsSubLabel.setText("");
					subjectSubLabel.setText("");
					reviewersSubLabel.setText("");
					feedbackavailableSubLabel.setText("");
					deadlineSubLabel.setText("");
				}
			}
		});


		// Gets feedback for selected submission
		paperList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {

				// Clear any existing text in the feedback area
				feedbackTextArea.setText("");

				// get indices of selected submission
				int[] selectedPaper = paperList.getSelectedIndices();

				// check if only one paper is selected
				if (selectedPaper.length == 1) {

					// gets selected paper as an object
					SubmissionObject paperOfInterest = paperModel.getElementAt(selectedPaper[0]);

					// Gets submission name
					paperInDetail = paperOfInterest.submissionName;

					// get filename of submission for resubmit button
					resubmitFilename = paperOfInterest.filename.replaceAll("\\s+", "");

					// filename of feedback file
					String feedbackFile = "submissions/" + userID + "/feedback/" + paperInDetail + ".txt";

					// print feedback to textarea
					Scanner feedback;

					try {

						feedback = new Scanner(new File(feedbackFile));

						while (feedback.hasNext()) {

							feedbackTextArea.append(feedback.nextLine());
							feedbackTextArea.append("\n");
						}

						feedbackTextArea.setCaretPosition(0);
						feedback.close();

						if (paperOfInterest.submissionDeadline != null) {

							// checks if current date is before deadline and makes
							// resubmit button visible if true and not visible if false
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Date today = new Date();
							try {
								Date deadline = format.parse(submissions[selectedPaper[0]].submissionDeadline);
								if (today.before(deadline))
									resubmitButton.setVisible(true);
								else
									resubmitButton.setVisible(false);
							} catch (ParseException e) {
								e.printStackTrace();
							}

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
					Path newdest = Paths.get(userFolder + "/" + resubmitFilename.replaceAll("\\s+", ""));
					try {
						Files.copy(newsource, newdest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {

					}
				}
			}
		});

	}

	/*
	 * Populates the reviewers for the preferred reviewers list
	 */
	private void getReviewers() {
		PreparedStatement ps = null;

		reviewers = new ReviewerObject[50];
		String query = "SELECT * FROM users WHERE userType = 2";
		try {
			ps = conn.prepareStatement(query);

			reviewerSet = ps.executeQuery();
			int count = 0;
			while (reviewerSet.next()) {
				int userID = reviewerSet.getInt("userID");
				String username = reviewerSet.getString("username");
				String name = String.valueOf(username.charAt(0)).toUpperCase() + username.substring(1).split("\\@")[0];
				int userType = 2;

				reviewers[count] = new ReviewerObject(userID, username, name, userType);
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
		PreparedStatement ps = null;

		String query = "SELECT * FROM submission WHERE submissionUserID = ?";

		try {
			ps = conn.prepareStatement(query);

			ps.setInt(1, this.userID);

			submissionSet = ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Failure searching for user submissions");
		}
	}
	
	/**
	 * Gets user's submissions from an sql query and stores in a global ResultSet
	 */
	private void getSubmissions(int submissionStage) {
		PreparedStatement ps = null;

		String query = "SELECT * FROM submission WHERE submissionUserID = ? AND submissionStage = ?";

		try {
			ps = conn.prepareStatement(query);

			ps.setInt(1, this.userID);
			ps.setInt(2, submissionStage);

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
				if (submissionID > 0) {
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
	 * Method for adding a new submission to the database
	 * 
	 * @param submissionName, The name of the paper being submitted
	 * @param submissionAuthors, The names of the authors submitting the paper
	 * @param subject, The subject of the paper being submitted
	 * @param filename, The filename from the location on their drive
	 * @param reviewerIDs, The reviewer's IDs selected from the list of preferred reviewers
	 */
	private void makeSubmission(String submissionName, String submissionAuthors, String subject, String filename,
			String reviewerIDs) {

		PreparedStatement ps = null;
		String query = "INSERT INTO submission (submissionName, submissionAuthors, subject, submissionStage, "
				+ "filename, submissionUserID, preferredReviewerIDs, userEmail) values (? , ? , ? , ? , ? , ?, ?, ?)";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, submissionName);
			ps.setString(2, submissionAuthors);
			ps.setString(3, subject);
			ps.setInt(4, 1);
			ps.setString(5, filename);
			ps.setInt(6, this.userID);
			ps.setString(7, reviewerIDs);
			ps.setString(8, this.username);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
