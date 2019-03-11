import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.SpringLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Author {

	private JFrame frmAuthor;
	protected Object frmLogin;
	private String authorname;
	private ArrayList<String> waitingList;
	private ArrayList<String> readyList;
	
	/**
	 * Launch the application.
	 */
	public static void Author(String user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author window = new Author(user);
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
	public Author(String user) {
		initialize(user);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String user) {
		authorname = user;
		
		populateLists();
		
		frmAuthor = new JFrame();
		frmAuthor.setResizable(false);
		frmAuthor.setTitle("Author");
		frmAuthor.setBounds(100, 100, 650, 550);
		frmAuthor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAuthor.setLocationRelativeTo(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(frmAuthor.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
		);
		
		JList<String> list_1 = new JList<String>();
		DefaultListModel<String> waitingdlm = new DefaultListModel<String>();
		list_1.setModel(waitingdlm);
		tabbedPane.addTab("Waiting for feedback", null, list_1, null);
		
		JList<String> list_2 = new JList<String>();
		DefaultListModel<String> readydlm = new DefaultListModel<String>();
		list_2.setModel(readydlm);
		tabbedPane.addTab("Ready for feedback", null, list_2, null);
		frmAuthor.getContentPane().setLayout(groupLayout);
		
		displayLists(waitingdlm,readydlm);
		
		JMenuBar menuBar = new JMenuBar();
		frmAuthor.setJMenuBar(menuBar);
		
		JMenuItem mntmLogOut = new JMenuItem("Log out - " + user.split("\\@")[0]);
		mntmLogOut.addActionListener(new ActionListener() {					//logout button logic
			public void actionPerformed(ActionEvent arg0) {
				frmAuthor.setVisible(false);
				Login.Login();
			}
				
		});
		menuBar.add(mntmLogOut);
		
		JMenuItem mntmNewPaper = new JMenuItem("New Submission");
		mntmNewPaper.addActionListener(new ActionListener() {			//new paper button logic
			public void actionPerformed(ActionEvent arg0) {
				NewSubmission.NewSubmission(user);
			}
			
		});
		menuBar.add(mntmNewPaper);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {		//refresh button logic
			@Override
			public void mouseClicked(MouseEvent e) {
				populateLists();
				displayLists(waitingdlm,readydlm);
			}
		});
		menuBar.add(btnRefresh);
	}
	
	private void displayLists(DefaultListModel<String> waitingdlm, DefaultListModel<String> readydlm) {
		waitingdlm.clear();
		readydlm.clear();
		for(int i=0;i<waitingList.size();i++) {
			String[] entry = waitingList.get(i).split("[|]");
			waitingdlm.addElement(entry[1] + "  " + entry[0] + "  " + entry[2]);
		}
		
		for(int i=0;i<readyList.size();i++) {
			String[] entry = readyList.get(i).split("[|]");
			readydlm.addElement(entry[1] + "  " + entry[0] + "  " + entry[2]);
		}
	}

	public void populateLists() {
		String foldername = authorname+"Submissions/";
		String waitingname = "waitinglist.txt";
		String readyname = "readylist.txt";
		waitingList = new ArrayList<String>();
		readyList = new ArrayList<String>();
		
		//populate list of papers waiting for feedback
		try {
			BufferedReader in = new BufferedReader(new FileReader(foldername+waitingname));
			String i = in.readLine();
			while(i!=null && !i.isEmpty()) {
				waitingList.add(i);
				i = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException e) {} catch (IOException e) {}
		
		//populate list of papers with feedback ready
		try {
			BufferedReader in = new BufferedReader(new FileReader(foldername+readyname));
			String i = in.readLine();
			while(i!=null && !i.isEmpty()) {
				readyList.add(i);
				i = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException e) {} catch (IOException e) {}
		
	}
}
