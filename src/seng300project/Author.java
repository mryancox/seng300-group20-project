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
import java.util.Scanner;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

public class Author extends JFrame {

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author frame = new Author(args[0]);
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
	public Author(String user) {
		setTitle("Journal Submission System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String userFolder = "submissions/" + user;
		String userDetails = "submissions/" + user + "/details";
		String userFeedback = "submissions/" + user + "/feedback";
		String userFeedbackList = "submissions/" + user + "/feedback_list.txt";
		String userSubmissionList = "submissions/" + user + "/submission_list.txt";
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
		String niceUsername = String.valueOf(user.charAt(0)).toUpperCase() + user.substring(1).split("\\@")[0];
		
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
		submissionTitleLabel.setBounds(24, 30, 230, 40);
		submissionPanel.add(submissionTitleLabel);
		
		JLabel titleLabel = new JLabel("Paper Title");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
		titleLabel.setBounds(24, 90, 101, 14);
		submissionPanel.add(titleLabel);
		
		JTextArea titleTextArea = new JTextArea();
		titleTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		titleTextArea.setBounds(24, 125, 650, 30);
		titleTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(titleTextArea);
		
		JSeparator titleSeparator = new JSeparator();
		titleSeparator.setForeground(Color.BLACK);
		titleSeparator.setBounds(24, 155, 650, 2);
		submissionPanel.add(titleSeparator);
		
		JLabel authorsLabel = new JLabel("Authors");
		authorsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		authorsLabel.setBounds(24, 170, 81, 14);
		submissionPanel.add(authorsLabel);
		
		JTextArea authorsTextArea = new JTextArea();
		authorsTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		authorsTextArea.setBounds(24, 225, 650, 30);
		authorsTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(authorsTextArea);
		
		JSeparator authorsSeparator = new JSeparator();
		authorsSeparator.setForeground(Color.BLACK);
		authorsSeparator.setBounds(24, 255, 650, 2);
		submissionPanel.add(authorsSeparator);
		
		JPanel submitButton = new JPanel();
		submitButton.setBackground(new Color(0, 124, 65));
		submitButton.setBounds(555, 520, 119, 30);
		submissionPanel.add(submitButton);
		submitButton.setLayout(null);
		
		JLabel authorsextraLabel = new JLabel("(seperate with commas)");
		authorsextraLabel.setForeground(Color.DARK_GRAY);
		authorsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		authorsextraLabel.setBounds(24, 190, 146, 14);
		submissionPanel.add(authorsextraLabel);
		
		JLabel prefreviewersLabel = new JLabel("Preferred Reviewers");
		prefreviewersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		prefreviewersLabel.setBounds(24, 350, 170, 14);
		submissionPanel.add(prefreviewersLabel);
		
		JLabel researchLabel = new JLabel("Research Subject");
		researchLabel.setFont(new Font("Arial", Font.BOLD, 14));
		researchLabel.setBounds(24, 270, 146, 14);
		submissionPanel.add(researchLabel);
		
		JLabel filelocationLabel = new JLabel("File Location");
		filelocationLabel.setFont(new Font("Arial", Font.BOLD, 14));
		filelocationLabel.setBounds(24, 450, 119, 14);
		submissionPanel.add(filelocationLabel);
		
		JLabel prefreviewersextraLabel = new JLabel("(seperate with commas)");
		prefreviewersextraLabel.setForeground(Color.DARK_GRAY);
		prefreviewersextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		prefreviewersextraLabel.setBounds(24, 370, 146, 14);
		submissionPanel.add(prefreviewersextraLabel);
		
		JTextArea researchTextArea = new JTextArea();
		researchTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		researchTextArea.setBounds(24, 305, 650, 30);
		researchTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(researchTextArea);
		
		JTextArea prefreviewersTextArea = new JTextArea();
		prefreviewersTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		prefreviewersTextArea.setBounds(24, 405, 650, 30);
		prefreviewersTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		submissionPanel.add(prefreviewersTextArea);
		
		JSeparator researchSeparator = new JSeparator();
		researchSeparator.setForeground(Color.BLACK);
		researchSeparator.setBounds(24, 335, 650, 2);
		submissionPanel.add(researchSeparator);
		
		JSeparator prefreviewersSeparator = new JSeparator();
		prefreviewersSeparator.setForeground(Color.BLACK);
		prefreviewersSeparator.setBounds(24, 435, 650, 2);
		submissionPanel.add(prefreviewersSeparator);
		
		JTextArea filelocationTextArea = new JTextArea();
		filelocationTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if(returnVal == JFileChooser.CANCEL_OPTION) {
					
				} else {
					File file = fc.getSelectedFile();
					filename = file.getName();
					filelocationTextArea.setText(file.getAbsolutePath());
				}
			}
		});
		filelocationTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		filelocationTextArea.setBounds(24, 485, 650, 30);
		submissionPanel.add(filelocationTextArea);
		
		JPanel listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);
		contentPanel.add(listPanel, "name_93462556713100");
		listPanel.setLayout(null);
		
		JLabel listTitleLabel = new JLabel("Submissions List");
		listTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		listTitleLabel.setBounds(24, 30, 230, 40);
		listPanel.add(listTitleLabel);
		
		JLabel priorsubLabel = new JLabel("Prior Submissions");
		priorsubLabel.setFont(new Font("Arial", Font.BOLD, 14));
		priorsubLabel.setBounds(24, 90, 151, 14);
		listPanel.add(priorsubLabel);
		
		JSeparator listSeparator = new JSeparator();
		listSeparator.setForeground(Color.BLACK);
		listSeparator.setBounds(24, 270, 650, 2);
		listPanel.add(listSeparator);
		
		JLabel detailsLabel = new JLabel("Submission Details");
		detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		detailsLabel.setBounds(24, 285, 151, 14);
		listPanel.add(detailsLabel);
		
		JLabel detailsextraLabel = new JLabel("(select a paper above)");
		detailsextraLabel.setForeground(Color.DARK_GRAY);
		detailsextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		detailsextraLabel.setBounds(24, 305, 151, 14);
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
		
		DefaultListModel<String> submissionModel = new DefaultListModel<>();
		JList<String> submissionList = new JList<String>(submissionModel);
		String papersFile = "submissions/" + user + "/submission_list.txt";
		Scanner papers;
		try {
	    	
			papers = new Scanner(new File(papersFile));
	    	
	    	while (papers.hasNext()) {
	    		
	    		submissionModel.addElement(papers.nextLine());
	    	}
	    	
	    	papers.close();
		} catch (FileNotFoundException e) {
	    	
	    }
		submissionList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				int[] selectedPaper = submissionList.getSelectedIndices();
				if (selectedPaper.length == 1) {
					
					paperInDetail = submissionModel.getElementAt(selectedPaper[0]).split("\\.")[0];
					String detailFile = "submissions/" + user + "/details/" + paperInDetail + ".txt";
					Scanner details;
					
					try {
				    	
						details = new Scanner(new File(detailFile));
				    	
				    	while (details.hasNext()) {
				    		
				    		String row = details.nextLine();
				    		String[] elements = row.split("\\|");
				    		
				    		datatitleListLabel.setText(elements[0]);	
				    		dataauthorsListLabel.setText(elements[1]);
				    		datasubjectListLabel.setText(elements[2]);
				    		
				    		if (elements.length >= 4) {
				    			datareviewersListLabel.setText(elements[3]);
				    		} else {
				    			datareviewersListLabel.setText("No Reviewers Assigned");
				    		}
				    		
				    		if (elements.length >= 5) {
				    			datafeedbackListLabel.setText(elements[4]);
				    		} else {
				    			datafeedbackListLabel.setText("None");
				    		}
							
				    		if (elements.length >= 6) {
				    			datadeadlineListLabel.setText(elements[5]);
				    		} else {
				    			datadeadlineListLabel.setText("Not Yet Set");
				    		}
							
				    	}
				    	
				    	details.close();
					} catch (FileNotFoundException e) {
				    	
				    }
					
				} else if (selectedPaper.length >=2) {
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected", JOptionPane.PLAIN_MESSAGE, null);
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
		feedbackTitleLabel.setBounds(24, 30, 230, 40);
		feedbackPanel.add(feedbackTitleLabel);
		feedbackTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		
		JLabel papersLabel = new JLabel("Papers With Feedback");
		papersLabel.setFont(new Font("Arial", Font.BOLD, 14));
		papersLabel.setBounds(24, 90, 180, 14);
		feedbackPanel.add(papersLabel);
		
		JTextArea feedbackTextArea = new JTextArea();
		feedbackTextArea.setEditable(false);
		feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackScrollPane.setSize(650, 180);
		feedbackScrollPane.setLocation(24, 330);
		feedbackScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		feedbackPanel.add(feedbackScrollPane);
		
		DefaultListModel<String> paperModel = new DefaultListModel<>();
		JList<String> paperList = new JList<String>(paperModel);
		Scanner feedbackList;
		try {
	    	
			feedbackList = new Scanner(new File(userFeedbackList));
	    	
	    	while (feedbackList.hasNext()) {
	    		
	    		paperModel.addElement(feedbackList.nextLine());
	    	}
	    	
	    	feedbackList.close();
		} catch (FileNotFoundException e) {
	    	
	    }
		paperList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("static-access")
			public void valueChanged(ListSelectionEvent arg0) {
				
				feedbackTextArea.setText("");
				int[] selectedPaper = paperList.getSelectedIndices();
				if (selectedPaper.length == 1) {
					
					paperInDetail = paperModel.getElementAt(selectedPaper[0]).split("\\.")[0];
					resubmitFilename = paperModel.getElementAt(selectedPaper[0]);
					String feedbackFile = "submissions/" + user + "/feedback/" + paperInDetail + ".txt";
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
					
				} else if (selectedPaper.length >=2) {
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					
					JOptionPane.showMessageDialog(null, "Please Select Only 1 Paper", "Too Many Papers Selected", JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		});
		paperList.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JScrollPane feedbackListScrollPane = new JScrollPane(paperList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		feedbackListScrollPane.setSize(650, 143);
		feedbackListScrollPane.setLocation(24, 115);
		feedbackListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		feedbackPanel.add(feedbackListScrollPane);

		
		JSeparator feedbackSeparator = new JSeparator();
		feedbackSeparator.setForeground(Color.BLACK);
		feedbackSeparator.setBounds(24, 270, 650, 2);
		feedbackPanel.add(feedbackSeparator);
		
		JLabel paperfeedbackLabel = new JLabel("Feedback");
		paperfeedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
		paperfeedbackLabel.setBounds(24, 285, 180, 14);
		feedbackPanel.add(paperfeedbackLabel);
		
		JLabel paperfeedbackextraLabel = new JLabel("(select a paper above)");
		paperfeedbackextraLabel.setForeground(Color.DARK_GRAY);
		paperfeedbackextraLabel.setFont(new Font("Arial", Font.BOLD, 12));
		paperfeedbackextraLabel.setBounds(24, 305, 151, 14);
		feedbackPanel.add(paperfeedbackextraLabel);
		
		JPanel resubmitButton = new JPanel();
		JLabel resubmitLabel = new JLabel("Resubmit");
		resubmitLabel.setForeground(Color.WHITE);
		resubmitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		resubmitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resubmitLabel.setBounds(0, 0, 119, 30);
		resubmitButton.add(resubmitLabel);
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
								
				if(returnVal == JFileChooser.CANCEL_OPTION) {
					
				} else {
					File resubFile = fc.getSelectedFile();
					Path newsource = Paths.get(resubFile.getAbsolutePath());
					Path newdest = Paths.get(userFolder+"/" + resubmitFilename);
					try {
						Files.copy(newsource, newdest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						
					}
				}
			}
		});
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
		
		JSeparator menuSeparator = new JSeparator();
		menuSeparator.setForeground(Color.WHITE);
		menuSeparator.setBounds(10, 70, 160, 2);
		menuPanel.add(menuSeparator);
		
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
		
		JLabel submitLabel = new JLabel("Submit");
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
				
				if (titleTextArea.getText().isEmpty() || authorsTextArea.getText().isEmpty() || researchTextArea.getText().isEmpty() || prefreviewersTextArea.getText().isEmpty() || filelocationTextArea.getText().isEmpty()) {
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					
					JOptionPane.showMessageDialog(null, "Ensure all fields are filled out correctly", "Missing Submission Details", JOptionPane.PLAIN_MESSAGE, null);
				} else {
					
					newTitle = titleTextArea.getText();
					newAuthors = authorsTextArea.getText();
					newAuthorsArray = newAuthors.split("\\s*,\\s*");
					newSubject = researchTextArea.getText();
					newPrefreviewers = prefreviewersTextArea.getText();
					newPrefreviewersArray = newPrefreviewers.split("\\s*,\\s*");
					filelocation = filelocationTextArea.getText();
					
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
					
					Path source = Paths.get(filelocation);
					Path dest = Paths.get(userFolder+"/"+filename);
					try {
						Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						
					}
					
					Scanner papers;
					submissionModel.removeAllElements();
					try {
				    	
						papers = new Scanner(new File(papersFile));
				    	while (papers.hasNext()) {
				    		
				    		submissionModel.addElement(papers.nextLine());
				    	}
				    	
				    	papers.close();
					} catch (FileNotFoundException e) {
				    	
				    }
					
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					
					JOptionPane.showMessageDialog(null, "Thank you for your submission!\n It will be reviewed shortly.", "Submission Accepted", JOptionPane.PLAIN_MESSAGE, null);
				}
				
			}
		});
		submitLabel.setBounds(0, 0, 119, 30);
		submitButton.add(submitLabel);
		submitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitLabel.setForeground(Color.WHITE);
		submitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		
	}
}