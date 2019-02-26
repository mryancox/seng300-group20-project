import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

public class Reviewer {

	private JFrame frmReviewer;

	/**
	 * Launch the application.
	 */
	public static void Reviewer() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reviewer window = new Reviewer();
					window.frmReviewer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Reviewer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReviewer = new JFrame();
		frmReviewer.setResizable(false);
		frmReviewer.setTitle("Reviewer");
		frmReviewer.setBounds(100, 100, 450, 300);
		frmReviewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReviewer.getContentPane().setBackground(Color.white);
		
		JLabel reviewerLabel = new JLabel("Reviewer's View");
		GroupLayout groupLayout = new GroupLayout(frmReviewer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(173)
					.addComponent(reviewerLabel)
					.addContainerGap(194, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(113)
					.addComponent(reviewerLabel)
					.addContainerGap(144, Short.MAX_VALUE))
		);
		frmReviewer.getContentPane().setLayout(groupLayout);
	}
}
