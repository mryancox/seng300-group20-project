import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class NewSubmission {

	protected JFrame frmNewSubmission;
	protected JTextField fileLocationBox;
	protected JTextArea authorField;
	protected JTextArea titleField;
	protected String filename;
	protected String authorname;
	/**
	 * Launch the application.
	 */
	public static void NewSubmission(String user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSubmission window = new NewSubmission(user);
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
	public NewSubmission(String user) {
		initialize(user);
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String user) {
		authorname = user;
		
		
		frmNewSubmission = new JFrame();
		frmNewSubmission.setResizable(false);
		frmNewSubmission.setTitle("New Submission");
		frmNewSubmission.setBounds(100, 100, 550, 500);
		frmNewSubmission.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewSubmission.setLocationRelativeTo(null);
		
		//logic for cancel button
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
		frmNewSubmission.getContentPane().setLayout(null);
		frmNewSubmission.getContentPane().add(btnCancel);
		
		//Logic for continue button
		JButton btnNewButton = new JButton("Continue");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(fileLocationBox.getText().isEmpty())
					JOptionPane.showMessageDialog(frmNewSubmission, "No file selected");
				else {
					//mkNewSubmission();
					frmNewSubmission.setVisible(false);
					ContinueSubmission.ContinueSubmission(NewSubmission.this);
					
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
		
		
		//Add logic for browse button
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frmNewSubmission);
				if(returnVal == JFileChooser.CANCEL_OPTION) {}
				else {
					File file = fc.getSelectedFile();
					filename = file.getName();
					fileLocationBox.setText(file.getAbsolutePath());
				}
			}
		});
		
		
	}
	
}

