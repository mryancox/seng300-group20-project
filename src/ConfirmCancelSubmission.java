import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class ConfirmCancelSubmission {

	private JFrame frmConfirmCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfirmCancelSubmission window = new ConfirmCancelSubmission();
					window.frmConfirmCancel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfirmCancelSubmission() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmConfirmCancel = new JFrame();
		frmConfirmCancel.setTitle("Confirm Cancel");
		frmConfirmCancel.setBounds(100, 100, 524, 109);
		frmConfirmCancel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConfirmCancel.setLocationRelativeTo(null);
		frmConfirmCancel.getContentPane().setLayout(null);
		
		JLabel lblAreYouSure = new JLabel("Are you sure you would like to cancel the submission?");
		lblAreYouSure.setBounds(114, 11, 373, 14);
		frmConfirmCancel.getContentPane().add(lblAreYouSure);
		
		JButton btnNewButton = new JButton("Yes, Cancel Submission");
		btnNewButton.setBounds(293, 36, 205, 23);
		frmConfirmCancel.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("No");
		btnNewButton_1.setBounds(10, 36, 89, 23);
		frmConfirmCancel.getContentPane().add(btnNewButton_1);
	}

	public static void ConfirmCancelSubmission() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfirmCancelSubmission window = new ConfirmCancelSubmission();
					window.frmConfirmCancel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	}

}
