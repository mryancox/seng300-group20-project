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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

public class Reviewer extends JFrame implements Constants{

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
	protected SubmissionObject[] submissions;
	protected FeedbackObject[] feedback;
	protected String[] subjects = new String[100];
	private int[] selectedPaper = new int[0];
	private int[] selected = 	new int[0];
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reviewer frame = new Reviewer("Test Reviewer",8);
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
	public Reviewer(String user, int ID) {
		this.userID=ID;
		
		//get list of subjects and submissions, for each subject
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

		JLabel reviewerIcon = new JLabel("");
		Image authorIconImg = new ImageIcon(this.getClass().getResource("/reviewer.jpg")).getImage();
		reviewerIcon.setIcon(new ImageIcon(authorIconImg));
		reviewerIcon.setBounds(184, 212, 346, 200);
		welcomePanel.add(reviewerIcon);
		
		JPanel browsePanel = new JPanel();
		contentPanel.add(browsePanel, "name_35648043125700");
		browsePanel.setLayout(null);
		browsePanel.setBackground(Color.WHITE);
		
		JLabel browseTitleLabel = new JLabel("Browse Journals");
		browseTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		browseTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		browseTitleLabel.setBounds(24, 30, 230, 40);
		browsePanel.add(browseTitleLabel);
		
		JLabel subjectLabel = new JLabel("Research Subjects");
		subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
		subjectLabel.setBounds(24, 90, 230, 14);
		browsePanel.add(subjectLabel);
		
		DefaultListModel<String> subjectModel = new DefaultListModel<>();
		JList<String> subjectList = new JList<String>(subjectModel);
		
		
		
		subjectList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane subjectListScrollPane = new JScrollPane(subjectList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		subjectListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		subjectListScrollPane.setBounds(24, 115, 650, 143);
		browsePanel.add(subjectListScrollPane);
		
		JPanel browseSepPanel = new JPanel();
		browseSepPanel.setBackground(Color.BLACK);
		browseSepPanel.setBounds(24, 270, 650, 2);
		browsePanel.add(browseSepPanel);
		
		JLabel reviewLabel = new JLabel("Choose A Paper You Want To Review");
		reviewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		reviewLabel.setBounds(24, 285, 291, 14);
		browsePanel.add(reviewLabel);
		
		JLabel reviewextraLabel = new JLabel("(select a subject above)");
		reviewextraLabel.setForeground(Color.DARK_GRAY);
		reviewextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		reviewextraLabel.setBounds(24, 305, 151, 14);
		browsePanel.add(reviewextraLabel);
		
		DefaultListModel<SubmissionObject> nominateModel = new DefaultListModel<>();
		JList<SubmissionObject> nominateList = new JList<SubmissionObject>(nominateModel);
		
		nominateList.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane nominateListScrollPane = new JScrollPane(nominateList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		nominateListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		nominateListScrollPane.setBounds(24, 330, 650, 180);
		browsePanel.add(nominateListScrollPane);
		nominateList.removeAll();
		
		JPanel nominateButton = new JPanel();
		JLabel nominateLabel = new JLabel("Nominate");
		nominateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nominateLabel.setForeground(Color.WHITE);
		nominateLabel.setFont(new Font("Arial", Font.BOLD, 12));
		nominateLabel.setBounds(0, 0, 119, 30);
		nominateButton.add(nominateLabel);

		nominateButton.setLayout(null);
		nominateButton.setBackground(new Color(0, 124, 65));
		nominateButton.setBounds(555, 520, 119, 30);
		browsePanel.add(nominateButton);

		

		
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setBackground(Color.WHITE);
		contentPanel.add(feedbackPanel, "name_47558521480000");
		feedbackPanel.setLayout(null);

		JLabel feedbackTitleLabel = new JLabel("Provide Feedback");
		feedbackTitleLabel.setBounds(24, 30, 230, 40);
		feedbackPanel.add(feedbackTitleLabel);
		feedbackTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JLabel papersLabel = new JLabel("Assigned Papers");
		papersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		papersLabel.setBounds(24, 90, 180, 14);
		feedbackPanel.add(papersLabel);

		
		DefaultListModel<SubmissionObject> paperModel = new DefaultListModel<>();
		JList<SubmissionObject> assignedList = new JList<SubmissionObject>(paperModel);

		assignedList.setFont(new Font("Arial", Font.PLAIN, 12));

		

		
		
		JScrollPane assignedListScrollPane = new JScrollPane(assignedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		assignedListScrollPane.setSize(650, 110);
		assignedListScrollPane.setLocation(24, 115);
		assignedListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(assignedListScrollPane);
		
		JPanel openButton = new JPanel();
		JLabel openLabel = new JLabel("Open Paper");
		openLabel.setHorizontalAlignment(SwingConstants.CENTER);
		openLabel.setForeground(Color.WHITE);
		openLabel.setFont(new Font("Arial", Font.BOLD, 12));
		openLabel.setBounds(0, 0, 119, 30);
		openButton.add(openLabel);
		
		


		
		

		
		openButton.setLayout(null);
		openButton.setBackground(new Color(0, 124, 65));
		openButton.setBounds(555, 235, 119, 30);
		feedbackPanel.add(openButton);

		JPanel feedbackSepPanel = new JPanel();
		feedbackSepPanel.setBackground(Color.BLACK);
		feedbackSepPanel.setBounds(24, 270, 650, 2);
		feedbackPanel.add(feedbackSepPanel);

		JLabel provideLabel = new JLabel("Provide Feedback Below");
		provideLabel.setFont(new Font("Arial", Font.BOLD, 14));
		provideLabel.setBounds(24, 285, 180, 14);
		feedbackPanel.add(provideLabel);

		JLabel provideextraLabel = new JLabel("(for the paper you have selected above)");
		provideextraLabel.setForeground(Color.DARK_GRAY);
		provideextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		provideextraLabel.setBounds(24, 305, 267, 14);
		feedbackPanel.add(provideextraLabel);

		JTextArea feedbackTextArea = new JTextArea();
		feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JPanel submitButton = new JPanel();
		JLabel submitLabel = new JLabel("Submit Feedback");
		submitLabel.setForeground(Color.WHITE);
		submitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		submitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitLabel.setBounds(0, 0, 119, 30);
		submitButton.add(submitLabel);
		
		

		
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setSize(650, 180);
		feedbackScrollPane.setLocation(24, 330);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());

		feedbackPanel.add(feedbackScrollPane);
		submitButton.setBackground(new Color(0, 124, 65));
		submitButton.setBounds(555, 520, 119, 30);
		feedbackPanel.add(submitButton);
		submitButton.setLayout(null);

		JLabel menuLabel = new JLabel("MENU");
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(0, 30, 180, 40);
		menuPanel.add(menuLabel);

		JPanel browseButton = new JPanel();
		JLabel browseLabel = new JLabel("Browse Journals");
		browseLabel.setForeground(Color.WHITE);
		browseLabel.setFont(new Font("Arial", Font.BOLD, 14));
		browseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		browseLabel.setBounds(0, 0, 180, 30);
		browseButton.add(browseLabel);
		browseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				browseButton.setBackground(new Color(255, 219, 5));
				browseLabel.setForeground(Color.BLACK);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {

				browseButton.setBackground(new Color(0, 124, 65));
				browseLabel.setForeground(Color.WHITE);
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

		JPanel menuSepPanel = new JPanel();
		menuSepPanel.setBounds(10, 70, 160, 2);
		menuPanel.add(menuSepPanel);
		browseButton.setBackground(new Color(0, 124, 65));
		browseButton.setBounds(0, 80, 180, 30);
		menuPanel.add(browseButton);
		browseButton.setLayout(null);

		JPanel feedbackButton = new JPanel();
		JLabel feedbackLabel = new JLabel("Provide Feedback");
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
		feedbackButton.setLayout(null);
		feedbackButton.setBackground(new Color(0, 124, 65));
		feedbackButton.setBounds(0, 115, 180, 30);
		menuPanel.add(feedbackButton);



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
		
		// populates list of subjects
		for (int i = 0; i < subjects.length; i++)
			if (subjects[i] != null)
				subjectModel.addElement(subjects[i]);

		// SUBJECT LIST LOGIC
		subjectList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				//Ensure mouse click causes an update only once
				if (!subjectList.getValueIsAdjusting()) {
					
					//clear elements in list of submissions
					nominateModel.removeAllElements();
					
					//get indices of selected subject
					int[] selectedSubject = subjectList.getSelectedIndices();

					//Ensure only one subject is selected and display the papers in that subject
					if (selectedSubject.length == 1) {
						for (int i = 0; i < submissions.length; i++)
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
				
				//get indices of selected papers
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
			if (submissions[i].reviewers.get(this.userID) != null && submissions[i].submissionStage == FEEDBACK_GATHERING_STAGE) {
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

					//Opens the file
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

		// NOMINATE PAPER LIST LOGIC
		// just checks if more than 1 paper is selected and shows a warning
		nominateList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				//get indices of selected papers
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

					//call method that sends sql update adding userID to nominated reviewers
					nominateReview(submissionID);
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

						//Puts an entry into database
						submitFeedback(paperOfInterest.submissionID, paperOfInterest.submissionName + ".txt");
						
						JOptionPane.showMessageDialog(null,
								"Thank you for your feedback!\n It will be reviewed shortly.", "Feedback Accepted",
								JOptionPane.PLAIN_MESSAGE, null);
						
						//refresh list of papers requiring feedback
						paperModel.clear();
						getSubmissions();
						populateSubmissions();
						for (int i = 0; i < submissions.length; i++) {
							if (submissions[i].reviewers.get(userID) != null && submissions[i].submissionStage==FEEDBACK_GATHERING_STAGE) {
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
			ps = SQLConnection.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			int count = 0;
			
			//iterates over returned subjects and populates an array with subjects
			while(rs.next()) {
				subjects[count] = rs.getString("subject");
				count++;
			}
		}catch(Exception e) {
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
			ps=SQLConnection.getConnection().prepareStatement(query);


			submissionSet=ps.executeQuery();
		}catch(Exception e) {System.out.println(e); System.out.println("Failure searching for user submissions");}
	}
	
	
	/**
	 * Populates a global array of SubmissionObjects to store results of initial SQL query
	 * for user submissions, eliminating the need to constantly query SQL database
	 */
	private void populateSubmissions() {

		try {
			
			// Get number of submissions retrieved in order to set size of submissions array
			int numOfSubmissions = 0;
			while(submissionSet.next())
				numOfSubmissions++;

			// i is a counter for iterating over submissions array
			int i=0;
			
			// sets size of submissions array
			submissions = new SubmissionObject[numOfSubmissions];
			
			// resets position of submissionSet ResultSet
			submissionSet.beforeFirst();

			// iterates over submission ResultSet and submissions array, populating the
			// array
			while(submissionSet.next()) {
				// get all details of each submission for constructor
				int submissionID = submissionSet.getInt("submissionID");
				String submissionName = submissionSet.getString("submissionName");
				String submissionAuthors = submissionSet.getString("submissionAuthors");
				String subject = submissionSet.getString("subject");
				String submissionDate = submissionSet.getString("submissionDate");
				int submissionStage = submissionSet.getInt("submissionStage");
				String filename= submissionSet.getString("filename");
				int submissionUserID = submissionSet.getInt("submissionUserID");
				
				// SubmissionObject constructor call
				submissions[i] = new SubmissionObject(submissionID, submissionName, submissionAuthors, subject, submissionDate, submissionStage, filename, submissionUserID);

				// get extra details which may be null
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
				
				// call SubmissionObject method that retrieves reviewer names from reviewer IDs
				submissions[i].setReviewerNames();
				
				//increment counter
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Gets the submission matching subject and submissionName and appends the userID to the 
	 * nominatedReviewers field
	 * @param subject - the selected subject
	 * @param submissionName - the selected submission name
	 */
	private void nominateReview(int submissionID) {
		PreparedStatement ps;
		ResultSet rs;
		
		String query1 = "SELECT * FROM submission WHERE submissionID = ?";
		String query2 = "UPDATE submission SET nominatedReviewers = ? WHERE submissionID = ?";
		
		try {
			ps = SQLConnection.getConnection().prepareStatement(query1);
			ps.setInt(1, submissionID);
			
			rs = ps.executeQuery();
			
			rs.next();
			

			//check if this user is already nominated
			boolean isAlreadyNominated = false;
			//check if initial value of nominatedReviewers is null
			boolean isNull = false;
			
			if(rs.getString("nominatedReviewers")!=null) {
				String[] nominated = rs.getString("nominatedReviewers").split("[,]");
				for(int i=0;i<nominated.length;i++) 
					if(nominated[i].equals(Integer.toString(this.userID)))
						isAlreadyNominated=true;
			}
			else {
				isNull=true;
			}
			
			//appends userID if not already nominated
			if(!isAlreadyNominated) {
				String allNominated = new String();
				
				if(!isNull)
					allNominated = rs.getString("nominatedReviewers") + "," + this.userID;
				else
					allNominated = Integer.toString(this.userID);

				ps = SQLConnection.getConnection().prepareStatement(query2);
				ps.setString(1, allNominated);
				ps.setInt(2, submissionID);
				
				//To issue INSERT, UPDATE, or DELETE, java requires using executeUpdate()
				ps.executeUpdate();
			}
			
			
		}catch(Exception e) { e.printStackTrace();}	
	}
	
	/**
	 * Submits a feedback item into SQL database if a feedback item for 
	 * the submission in question does not already exist.
	 * 
	 * Performs 3 possible queries:
	 * 1. gets feedback entry to check if feedback already exists for the submission. If already exists, skip 2.
	 * 2. if no feedback entry already exists, inserts a new entry
	 * 3. updates submissionStage of the submission 
	 * 
	 * NOTE that currently the feedback list is not used for anything
	 * 
	 * @param submissionID
	 * @param filename - filename of feedback file
	 */ 
	private void submitFeedback(int submissionID, String filename) {
		PreparedStatement ps;
		ResultSet rs;

		String query = "SELECT * FROM feedback WHERE submissionID = ?";
		String query1 = "INSERT INTO feedback (filename, submissionID, userID) values (?, ?, ?)";
		String query2 = "UPDATE submission SET submissionStage = 4 WHERE submissionID = ?";
		
		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setInt(1, submissionID);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				ps = SQLConnection.getConnection().prepareStatement(query1);
				ps.setString(1, filename);
				ps.setInt(2, submissionID);
				ps.setInt(3,  userID);
				
				ps.executeUpdate();
			}
			
			ps = SQLConnection.getConnection().prepareStatement(query2);
			ps.setInt(1, submissionID);
			
			ps.executeUpdate();
		}catch(Exception e) {e.printStackTrace();}
	}

}

