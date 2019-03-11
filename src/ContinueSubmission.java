import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ContinueSubmission {

	private JFrame frmContinueSubmission;
	private NewSubmission newsubmission;

	/**
	 * Launch the application.
	 */
	public static void main(NewSubmission newsubmission) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContinueSubmission window = new ContinueSubmission(newsubmission);
					window.frmContinueSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ContinueSubmission(NewSubmission newsubmission) {
		initialize(newsubmission);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(NewSubmission newsubmission) {
		this.newsubmission = newsubmission;
		
		frmContinueSubmission = new JFrame();
		frmContinueSubmission.setResizable(false);
		frmContinueSubmission.setTitle("Select Reviewers");
		frmContinueSubmission.setBounds(100, 100, 550, 500);
		frmContinueSubmission.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmContinueSubmission.setLocationRelativeTo(null);
		frmContinueSubmission.getContentPane().setLayout(null);
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {		//back button logic
			@Override
			public void mouseClicked(MouseEvent e) {
				//NewSubmission.NewSubmission(user);
				newsubmission.frmNewSubmission.setVisible(true);
				frmContinueSubmission.setVisible(false);
			}
		});
		btnBack.setBounds(10, 427, 89, 23);
		frmContinueSubmission.getContentPane().add(btnBack);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {			//submit button logic

			@Override
			public void mouseClicked(MouseEvent e) {
				mkNewSubmission();
				frmContinueSubmission.setVisible(false);
				newsubmission.frmNewSubmission.setVisible(false);
			}
		});
		btnSubmit.setBounds(435, 427, 89, 23);
		frmContinueSubmission.getContentPane().add(btnSubmit);
		
		JList<String> reviewerList = new JList<String>();
		reviewerList.setBounds(124, 86, 400, 330);
		frmContinueSubmission.getContentPane().add(reviewerList);
		
		JLabel lblSelectPreferredReviewers = new JLabel("Select preferred reviewers.");
		lblSelectPreferredReviewers.setBounds(10, 11, 196, 14);
		frmContinueSubmission.getContentPane().add(lblSelectPreferredReviewers);
		
		JLabel lblAvailableReviewers = new JLabel("Available reviewers");
		lblAvailableReviewers.setBounds(10, 86, 104, 14);
		frmContinueSubmission.getContentPane().add(lblAvailableReviewers);
		
		JLabel lblNotePreferredReviewers = new JLabel("Note: Preferred reviewers may not be assigned in final stage.");
		lblNotePreferredReviewers.setBounds(10, 36, 482, 14);
		frmContinueSubmission.getContentPane().add(lblNotePreferredReviewers);
		
		JLabel lblNewLabel = new JLabel("Final reviewers will be assigned by administration.");
		lblNewLabel.setBounds(10, 48, 295, 23);
		frmContinueSubmission.getContentPane().add(lblNewLabel);
	}

	public static void ContinueSubmission(NewSubmission newsubmission) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContinueSubmission window = new ContinueSubmission(newsubmission);
					window.frmContinueSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * make new submission function which takes data from the window 
	 * and adds it to a "database" list of submissions, as well as 
	 * copying the input file to an application directory.
	 */
	private void mkNewSubmission() {
		String sublist = newsubmission.authorname+"Submissions/submissionsList.txt";
		String foldername = newsubmission.authorname+"Submissions";
		String title = newsubmission.titleField.getText();
		String fileloc = newsubmission.filename;
		String waitingname = "waitinglist.txt";
		
		//get list of authors and split by newline
		String authorList = "";
		for(String line : newsubmission.authorField.getText().split("\\n")) 
			authorList = authorList + line.replace("\n", "") + ",";
	
		//check if subfolder of submissions exists and create if not
		File authSubFolder = new File(foldername);
		if(!authSubFolder.exists()) 
			authSubFolder.mkdirs();
		
		//check if list of submissions exists and create if not
		File submissionsList = new File(sublist);
		if(!submissionsList.exists())
			try {
				submissionsList.createNewFile();
			} catch (IOException e) {}
		
		//Write data to submissions list
		try {
			FileWriter fw = new FileWriter(sublist,true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(authorList + "|" + title + "|" + fileloc);
			pw.close();
		} catch (IOException e) {}
		
		
		File waitingList = new File(waitingname);
		if(!waitingList.exists())
			try {
				waitingList.createNewFile();
			} catch (IOException e) {}
		
		//Write data to submissions list
				try {
					FileWriter fw = new FileWriter(newsubmission.authorname+"Submissions/waitinglist.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw);
					pw.println(authorList + "|" + title + "|" + fileloc);
					pw.close();
				} catch (IOException e) {}
				
		//copy document file to application directory
		Path source = Paths.get(newsubmission.fileLocationBox.getText());
		Path dest = Paths.get(foldername+"/"+fileloc);
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {}
		
	}
}
