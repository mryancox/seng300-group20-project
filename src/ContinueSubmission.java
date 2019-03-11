import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContinueSubmission {

	private JFrame frmNewSubmission;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContinueSubmission window = new ContinueSubmission();
					window.frmNewSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ContinueSubmission() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmNewSubmission = new JFrame();
		frmNewSubmission.setResizable(false);
		frmNewSubmission.setTitle("New Submission");
		frmNewSubmission.setBounds(100, 100, 550, 500);
		frmNewSubmission.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNewSubmission.setLocationRelativeTo(null);
		frmNewSubmission.getContentPane().setLayout(null);
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				NewSubmission.NewSubmission();
				frmNewSubmission.setVisible(false);
			}
		});
		btnBack.setBounds(10, 427, 89, 23);
		frmNewSubmission.getContentPane().add(btnBack);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(435, 427, 89, 23);
		frmNewSubmission.getContentPane().add(btnSubmit);
		
		JList<String> reviewerList = new JList<String>();
		reviewerList.setBounds(124, 86, 400, 330);
		frmNewSubmission.getContentPane().add(reviewerList);
		
		JLabel lblSelectPreferredReviewers = new JLabel("Select preferred reviewers.");
		lblSelectPreferredReviewers.setBounds(10, 11, 196, 14);
		frmNewSubmission.getContentPane().add(lblSelectPreferredReviewers);
		
		JLabel lblAvailableReviewers = new JLabel("Available reviewers");
		lblAvailableReviewers.setBounds(10, 86, 104, 14);
		frmNewSubmission.getContentPane().add(lblAvailableReviewers);
		
		JLabel lblNotePreferredReviewers = new JLabel("Note: Preferred reviewers may not be assigned in final stage.");
		lblNotePreferredReviewers.setBounds(10, 36, 482, 14);
		frmNewSubmission.getContentPane().add(lblNotePreferredReviewers);
		
		JLabel lblNewLabel = new JLabel("Final reviewers will be assigned by administration.");
		lblNewLabel.setBounds(10, 48, 295, 23);
		frmNewSubmission.getContentPane().add(lblNewLabel);
	}

	public static void ContinueSubmission() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContinueSubmission window = new ContinueSubmission();
					window.frmNewSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
