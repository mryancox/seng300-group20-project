import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Author {

	private JFrame frmAuthor;
	protected Object frmLogin;

	/**
	 * Launch the application.
	 */
	public static void Author() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author window = new Author();
					window.frmAuthor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Author() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAuthor = new JFrame();
		frmAuthor.setResizable(false);
		frmAuthor.setTitle("Author");
		frmAuthor.setBounds(100, 100, 499, 449);
		frmAuthor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAuthor.getContentPane().setBackground(Color.white);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(frmAuthor.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(32, Short.MAX_VALUE)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JTextPane txtpnSubmittedPaperView = new JTextPane();
		txtpnSubmittedPaperView.setText("No papers here");
		tabbedPane.addTab("Waiting for feedback", null, txtpnSubmittedPaperView, null);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("No papers here");
		tabbedPane.addTab("Feedback available", null, textPane, null);
		frmAuthor.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmAuthor.setJMenuBar(menuBar);
		
		JMenuItem mntmLogOut = new JMenuItem("Log out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				frmAuthor.setVisible(false);
				Login.Login();
			}
				
		});
		menuBar.add(mntmLogOut);
		
		JMenuItem mntmNewPaper = new JMenuItem("New Submission");
		mntmNewPaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				frmAuthor.setVisible(false);
				//NewSubmission.NewSubmission(); //commented out for now while code is being developed
			}
			
		});
		menuBar.add(mntmNewPaper);
		
		
	}
}
