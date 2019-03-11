import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA_2_3.portable.OutputStream;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class NewSubmission {

	private JFrame frmNewSubmission;
	private static JTextField fileLocationBox;
	private static JTextArea authorField;
	private static JTextArea titleField;
	private static String filename;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSubmission window = new NewSubmission();
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
	public NewSubmission() {
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
		frmNewSubmission.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewSubmission.setLocationRelativeTo(null);
		
		JButton btnCancel = new JButton("Cancel Submission");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int response = JOptionPane.showConfirmDialog(null,"Cancel submission?","Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_OPTION) {
					frmNewSubmission.setVisible(false);
				}
			}
		});
		btnCancel.setBounds(10, 427, 148, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmNewSubmission.getContentPane().setLayout(null);
		frmNewSubmission.getContentPane().add(btnCancel);
		
		JButton btnNewButton = new JButton("Continue");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(fileLocationBox.getText().isEmpty())
					JOptionPane.showMessageDialog(frmNewSubmission, "No file selected");
				else {
					mkNewSubmission();
					frmNewSubmission.setVisible(false);
					ContinueSubmission.ContinueSubmission();
				}
			}
		});
		btnNewButton.setBounds(427, 427, 97, 23);
		frmNewSubmission.getContentPane().add(btnNewButton);
		
		JLabel lblTitle = new JLabel("Notice: You are creating a NEW submission");
		lblTitle.setFont(lblTitle.getFont().deriveFont(lblTitle.getFont().getStyle() | Font.BOLD));
		lblTitle.setBounds(10, 11, 280, 23);
		frmNewSubmission.getContentPane().add(lblTitle);
		
		fileLocationBox = new JTextField();
		fileLocationBox.setBounds(124, 347, 400, 20);
		frmNewSubmission.getContentPane().add(fileLocationBox);
		fileLocationBox.setColumns(10);
		
		JLabel lblFileLocation = new JLabel("File location");
		lblFileLocation.setBounds(10, 350, 97, 14);
		frmNewSubmission.getContentPane().add(lblFileLocation);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(435, 378, 89, 23);
		frmNewSubmission.getContentPane().add(btnBrowse);
		
		JLabel lblNewLabel = new JLabel("Authors");
		lblNewLabel.setBounds(10, 170, 48, 14);
		frmNewSubmission.getContentPane().add(lblNewLabel);
		
		authorField = new JTextArea();
		authorField.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		authorField.setLineWrap(true);
		authorField.setWrapStyleWord(true);
		authorField.setText("Enter a list of authors here, separated by a new line");
		authorField.setToolTipText("Enter a list of authors here, separated by a new line");
		authorField.setBounds(124, 164, 400, 159);
		frmNewSubmission.getContentPane().add(authorField);
		
		JLabel lblTitle_1 = new JLabel("Title");
		lblTitle_1.setBounds(10, 66, 48, 14);
		frmNewSubmission.getContentPane().add(lblTitle_1);
		
		titleField = new JTextArea();
		titleField.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		titleField.setText("Enter the title of your submission here");
		titleField.setBounds(124, 60, 400, 84);
		frmNewSubmission.getContentPane().add(titleField);
		
		
		
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frmNewSubmission);
				File file = fc.getSelectedFile();
				filename = file.getName();
				fileLocationBox.setText(file.getAbsolutePath());
			}
		});
		
		
	}

	public static void NewSubmission() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSubmission window = new NewSubmission();
					window.frmNewSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	}
	
	public static void NewSubmission(String title, String author, String filepath) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSubmission window = new NewSubmission();
					window.frmNewSubmission.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
		titleField.setText(title);
		authorField.setText(author);
		fileLocationBox.setText(filepath);
		
	}
	
	private static void mkNewSubmission() {
		String sublist = "AuthorSubmissions/submissionsList.txt";
		String auth = authorField.getText();
		String title = titleField.getText();
		String fileloc = filename;
		
		//get list of authors and split by newline
		String authorList = "";
		for(String line : authorField.getText().split("\\n")) 
			authorList = authorList + line.replace("\n", "") + ",";
	
		//check if subfolder of submissions exists and create if not
		File authSubFolder = new File("AuthorSubmissions");
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
		
		//copy document file to application directory
		Path source = Paths.get(fileLocationBox.getText());
		Path dest = Paths.get("AuthorSubmissions/"+filename);
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {}
		
	}

}

