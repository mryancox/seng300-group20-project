import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login {

	private JFrame frmLogin;
	private JTextField username;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmLogin = new JFrame();
		frmLogin.setResizable(false);
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 450, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.setLocationRelativeTo(null);
		frmLogin.getContentPane().setBackground(Color.white);
		
		username = new JTextField();
		username.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username:");
		
		JLabel passwordLabel = new JLabel("Password:");
		
		JLabel promptLabel = new JLabel("Enter your username and password to login");
		
		JButton loginButton = new JButton("Login");
		frmLogin.getRootPane().setDefaultButton(loginButton);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String user = username.getText();
				String pw = passwordField.getText();
				String fileUser, filePassword;
				String authorFile = "authors.txt";
				String reviewerFile = "reviewers.txt";
				String adminFile = "admins.txt";
				boolean userFound = false;
				Scanner authors, reviewers, admins;

			    try {
			    	
			    	authors = new Scanner(new File(authorFile));
			    	authors.nextLine(); // skips the header at the beginning of the file
			    	
			    	while (authors.hasNext() && !userFound) {
			    		
			    		String row = authors.nextLine();
			    		String[] elements = row.split("\\|");
			    		
			    		if (user.equals(elements[0]) && pw.equals(elements[1])) {
			    			
			    			Author.Author(user);
			    			frmLogin.setVisible(false);
			    			userFound = true;
			    			break;
			    		}
			    		
			    	}
			    	
			    	authors.close();
			    	
			    	reviewers = new Scanner(new File(reviewerFile));
			    	reviewers.nextLine(); // skips the header at the beginning of the file
			    	
			    	while (reviewers.hasNext() && !userFound) {
			    		
			    		String row = reviewers.nextLine();
			    		String[] elements = row.split("\\|");
			    		
			    		if (user.equals(elements[0]) && pw.equals(elements[1])) {
			    			
			    			Reviewer.Reviewer(user);
			    			frmLogin.setVisible(false);
			    			userFound = true;
			    			break;
			    		}
			    		
			    	}
			    	
			    	reviewers.close();
			    	
			    	admins = new Scanner(new File(adminFile));
			    	admins.nextLine(); // skips the header at the beginning of the file
			    	
			    	while (admins.hasNext() && !userFound) {
			    		
			    		String row = admins.nextLine();
			    		String[] elements = row.split("\\|");
			    		
			    		if (user.equals(elements[0]) && pw.equals(elements[1])) {
			    			
			    			Admin.Admin(user);
			    			frmLogin.setVisible(false);
			    			userFound = true;
			    			break;
			    		}
			    		
			    	}
			    	
			    	admins.close();
			    
			    } catch (FileNotFoundException e) {
			    	
			    	userFound = false;
			    	
			    }
			    
			    if (!userFound) {
					
					username.setText("");
					passwordField.setText("");
					JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Error", JOptionPane.PLAIN_MESSAGE, null);
					
				}
				
			}
		});
		
		JLabel ualogoLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/ualogo.jpg")).getImage();
		ualogoLabel.setIcon(new ImageIcon(img));
		
		passwordField = new JPasswordField();
		GroupLayout groupLayout = new GroupLayout(frmLogin.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(ualogoLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(186)
							.addComponent(loginButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(108)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(promptLabel, Alignment.LEADING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(passwordLabel)
										.addComponent(usernameLabel))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
										.addComponent(username, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))))))
					.addGap(69))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ualogoLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(45)
					.addComponent(promptLabel)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(usernameLabel)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(loginButton)
					.addContainerGap(33, Short.MAX_VALUE))
		);
		frmLogin.getContentPane().setLayout(groupLayout);
	}

	public static void Login() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
