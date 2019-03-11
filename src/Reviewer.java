import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Reviewer {

	private JFrame frmReviewer;

	/**
	 * Launch the application.
	 */
	public static void Reviewer(String user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reviewer window = new Reviewer(user);
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
	public Reviewer(String user) {
		initialize(user);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String user) {
		
		frmReviewer = new JFrame();
		frmReviewer.setResizable(false);
		frmReviewer.setTitle("Reviewer");
		frmReviewer.setBounds(100, 100, 650, 550);
		frmReviewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReviewer.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frmReviewer.setJMenuBar(menuBar);
		
		JMenuItem menuItem = new JMenuItem("Log out - " + user.split("\\@")[0]);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				frmReviewer.setVisible(false);
				Login.Login();
				
			}
		});
		menuBar.add(menuItem);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(frmReviewer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
		);
		
		JTextPane journalList = new JTextPane();
		journalList.setText("List of journals a reviewer has been assigned that they can accept or reject, also shows each deadline");
		tabbedPane.addTab("Assigned Journal List", null, journalList, null);
		
		JTextPane journalSearch = new JTextPane();
		journalSearch.setText("A pane where the reviewer can search other journals to nominate themselves to review");
		tabbedPane.addTab("Browse All Journals", null, journalSearch, null);
		
		JTextPane downloads = new JTextPane();
		downloads.setText("A list of journals the reviewer currently is reviewing with download buttons");
		tabbedPane.addTab("Currently Reviewing", null, downloads, null);
		
		JTextPane feedback = new JTextPane();
		feedback.setText("Where feedback is given on a chosen Journal");
		tabbedPane.addTab("Provide Feedback", null, feedback, null);
		frmReviewer.getContentPane().setLayout(groupLayout);
	}
}
