package seng300project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class Login {

	private JFrame login;
	private JTextField username;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.login.setVisible(true);
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
		login = new JFrame();
		login.setTitle("Journal Submission System");
		login.setResizable(false);
		login.setBounds(100, 100, 900, 600);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setLocationRelativeTo(null);
		login.getContentPane().setLayout(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(0, 124, 65));
		leftPanel.setBounds(0, 0, 450, 571);
		login.getContentPane().add(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel openbookPicture = new JLabel("");
		Image openbookImg = new ImageIcon(this.getClass().getResource("/openbook.png")).getImage();
		openbookPicture.setIcon(new ImageIcon(openbookImg));
		openbookPicture.setBounds(-97, -106, 547, 677);
		leftPanel.add(openbookPicture);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBounds(449, 0, 445, 571);
		login.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);
		
		JLabel unameLabel = new JLabel("Username");
		unameLabel.setForeground(new Color(0, 124, 65));
		unameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		unameLabel.setBounds(70, 204, 294, 14);
		rightPanel.add(unameLabel);
		
		JSeparator unameSeparator = new JSeparator();
		unameSeparator.setForeground(new Color(0, 124, 65));
		unameSeparator.setBackground(new Color(0, 124, 65));
		unameSeparator.setBounds(70, 260, 294, 2);
		rightPanel.add(unameSeparator);
		
		username = new JTextField();
		username.setFont(new Font("Arial", Font.PLAIN, 12));
		username.setBounds(70, 229, 294, 20);
		username.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		rightPanel.add(username);
		username.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(new Color(0, 124, 65));
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
		passwordLabel.setBounds(70, 273, 294, 14);
		rightPanel.add(passwordLabel);
		
		password = new JPasswordField();
		password.setFont(new Font("Arial", Font.PLAIN, 12));
		password.setBounds(70, 298, 294, 20);
		password.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		rightPanel.add(password);
		
		JButton loginButton = new JButton("Login");
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				loginButton.setBackground(new Color(255, 219, 5));
				loginButton.setForeground(new Color(0, 0, 0));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				
				loginButton.setBackground(new Color(0, 124, 65));
				loginButton.setForeground(new Color(255, 255, 255));
			}
		});
		login.getRootPane().setDefaultButton(loginButton);
		loginButton.setFont(new Font("Arial", Font.BOLD, 12));
		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				
				String user = username.getText();
				String pw = String.valueOf(password.getPassword());
				String authorFile = "login_credentials/authors.txt";
				String reviewerFile = "login_credentials/reviewers.txt";
				String adminFile = "login_credentials/admins.txt";
				boolean userFound = false;
				Scanner authors, reviewers, admins;

			    try {
			    	
			    	authors = new Scanner(new File(authorFile));
			    	authors.nextLine(); // skips the header at the beginning of the file
			    	
			    	while (authors.hasNext() && !userFound) {
			    		
			    		String row = authors.nextLine();
			    		String[] elements = row.split("\\|");
			    		
			    		if (user.equals(elements[0]) && pw.equals(elements[1])) {
			    			
			    			login.setVisible(false);
			    			Author author = new Author(user);
			    			author.setVisible(true);
			    			author.setLocationRelativeTo(null);
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
			    			
			    			//Reviewer.Reviewer(user);
			    			//frmLogin.setVisible(false);
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
			    			
			    			//Admin.Admin(user);
			    			//frmLogin.setVisible(false);
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
					password.setText("");
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);
					
					JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Error", JOptionPane.PLAIN_MESSAGE, null);
					
				}
				
			}
		});
		loginButton.setBackground(new Color(0, 124, 65));
		loginButton.setBorderPainted(false);
		loginButton.setFocusPainted(false);
		loginButton.setBounds(142, 370, 148, 23);
		rightPanel.add(loginButton);
		
		JSeparator passwordSeparator = new JSeparator();
		passwordSeparator.setForeground(new Color(0, 124, 65));
		passwordSeparator.setBackground(new Color(0, 124, 65));
		passwordSeparator.setBounds(70, 329, 294, 2);
		rightPanel.add(passwordSeparator);
		
		JLabel ualbertaLogo = new JLabel("");
		Image ualbertaLogoImg = new ImageIcon(this.getClass().getResource("/ualogowtext.jpg")).getImage();
		ualbertaLogo.setIcon(new ImageIcon(ualbertaLogoImg));
		ualbertaLogo.setBounds(18, 24, 372, 87);
		rightPanel.add(ualbertaLogo);
		
		JLabel githubIcon = new JLabel("");
		githubIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
				try {
                    String url = "https://github.com/mryancox/seng300-group20-project";
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
				}

			}
		});
		Image githubIconImg = new ImageIcon(this.getClass().getResource("/github.png")).getImage();
		githubIcon.setIcon(new ImageIcon(githubIconImg));
		githubIcon.setBounds(201, 487, 40, 40);
		rightPanel.add(githubIcon);
		
		JLabel githubLabel = new JLabel("Click Octocat To Visit Our GitHub Repository");
		githubLabel.setFont(new Font("Arial", Font.BOLD, 10));
		githubLabel.setHorizontalAlignment(SwingConstants.CENTER);
		githubLabel.setBounds(10, 538, 425, 14);
		rightPanel.add(githubLabel);
	}

}
