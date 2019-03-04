import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public class Admin {

	private JFrame frmAdmin;

	/**
	 * Launch the application.
	 */
	public static void Admin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin window = new Admin();
					window.frmAdmin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Admin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdmin = new JFrame();
		frmAdmin.setResizable(false);
		frmAdmin.setTitle("Admin");
		frmAdmin.setBounds(200, 200, 650, 550);
		frmAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmAdmin.setJMenuBar(menuBar);
		
		JMenuItem mntmLogOut = new JMenuItem("Log out");
		menuBar.add(mntmLogOut);
		
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				frmAdmin.setVisible(false);
				Login.Login();
			}
				
			});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmAdmin.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JTextPane txtpnOpenSubmissions = new JTextPane();
		txtpnOpenSubmissions.setText("Open Submissions ");
		txtpnOpenSubmissions.setToolTipText("");
		tabbedPane.addTab("Open Submissions ", null, txtpnOpenSubmissions, null);
		
		JTextPane txtpnReviwedSubmissions = new JTextPane();
		txtpnReviwedSubmissions.setText("Reviwed Submissions ");
		tabbedPane.addTab("Reviwed Submissions ", null, txtpnReviwedSubmissions, null);
		
		JTextPane txtpnReviwersFeedback = new JTextPane();
		txtpnReviwersFeedback.setText("Applications to be a reviwer ");
		tabbedPane.addTab("Applications", null, txtpnReviwersFeedback, null);
		
		JTextPane txtpnTheTabOn = new JTextPane();
		txtpnTheTabOn.setText("The tab on which the admin accepts/rejects the submission after it has gone through the entire process  ");
		tabbedPane.addTab("Accept/Reject Submission", null, txtpnTheTabOn, null);
	}
}
