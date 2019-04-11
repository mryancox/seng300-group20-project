package interfaces;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import objects.SQLConnection;

import javax.swing.JPasswordField;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Login is the main class of the Journal Submission System. It handles new signups of Authors and
 * Reviewers as well as logging in approved users within an SQLite database.
 * 
 * @author L01-Group20
 */
public class Login {

	private JFrame login;
	private JTextField username;
	private JPasswordField password;
	private Connection conn;

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
		
		// Setup of the frame
		login = new JFrame();
		login.setTitle("Journal Submission System");
		login.setResizable(false);
		login.setBounds(100, 100, 900, 600);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setLocationRelativeTo(null);
		login.getContentPane().setLayout(null);

		// Checks if there is a current conenction to the SQLite database
		if (conn == null)
			conn = SQLConnection.getConnection();

		/*
		 * LOTS of GUI code follows that can be mostly ignored. In general, objects in
		 * the frame were created with certain boundaries and colours to create a
		 * cohesive feel. Buttons are implemented with jpanels instead of jbuttons
		 * simply because they did not appear properly with the colour scheme of the
		 * University of Alberta on Linux and MacOS. Most of the functionality is near
		 * the bottom of this class.
		 */
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(0, 124, 65));
		leftPanel.setBounds(0, 0, 450, 580);
		login.getContentPane().add(leftPanel);
		leftPanel.setLayout(null);

		JLabel openbookPicture = new JLabel("");
		Image openbookImg = new ImageIcon(this.getClass().getResource("/openbook.png")).getImage();
		openbookPicture.setIcon(new ImageIcon(openbookImg));
		openbookPicture.setBounds(-97, -106, 547, 686);
		leftPanel.add(openbookPicture);

		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBounds(449, 0, 455, 580);
		login.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);

		JLabel unameLabel = new JLabel("Username");
		unameLabel.setForeground(new Color(0, 124, 65));
		unameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		unameLabel.setBounds(70, 205, 294, 14);
		rightPanel.add(unameLabel);

		username = new JTextField();
		username.setFont(new Font("Arial", Font.PLAIN, 12));
		username.setBounds(70, 230, 305, 30);
		username.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		username.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("static-access")
			@Override
			public void keyPressed(KeyEvent arg0) {

				keyeventLogic(arg0);
			}
		});
		rightPanel.add(username);

		JPanel usernamePanel = new JPanel();
		usernamePanel.setBackground(new Color(0, 124, 65));
		usernamePanel.setBounds(70, 260, 305, 2);
		rightPanel.add(usernamePanel);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(new Color(0, 124, 65));
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
		passwordLabel.setBounds(70, 275, 294, 14);
		rightPanel.add(passwordLabel);

		password = new JPasswordField();
		password.setFont(new Font("Arial", Font.PLAIN, 12));
		password.setBounds(70, 300, 305, 30);
		password.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		password.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("static-access")
			@Override
			public void keyPressed(KeyEvent arg0) {

				keyeventLogic(arg0);
			}
		});
		rightPanel.add(password);

		JLabel ualbertaLogo = new JLabel("");
		Image ualbertaLogoImg = new ImageIcon(this.getClass().getResource("/ualogowtext.jpg")).getImage();

		JPanel passwordPanel = new JPanel();
		passwordPanel.setBackground(new Color(0, 124, 65));
		passwordPanel.setBounds(70, 330, 305, 2);
		rightPanel.add(passwordPanel);
		ualbertaLogo.setIcon(new ImageIcon(ualbertaLogoImg));
		ualbertaLogo.setBounds(18, 24, 372, 87);
		rightPanel.add(ualbertaLogo);

		JLabel githubIcon = new JLabel("");

		// opens github repo when github icon is clicked
		githubIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

				try {
					String url = "https://github.com/mryancox/seng300-group20-project";
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
					// It looks like there's a problem
				}

			}
		});
		Image githubIconImg = new ImageIcon(this.getClass().getResource("/github.png")).getImage();
		githubIcon.setIcon(new ImageIcon(githubIconImg));
		githubIcon.setBounds(202, 487, 40, 40);
		rightPanel.add(githubIcon);

		JLabel githubLabel = new JLabel("Click Octocat To Visit Our GitHub Repository");
		githubLabel.setFont(new Font("Arial", Font.BOLD, 10));
		githubLabel.setHorizontalAlignment(SwingConstants.CENTER);
		githubLabel.setBounds(10, 538, 425, 14);
		rightPanel.add(githubLabel);

		JPanel loginButton = new JPanel();
		JLabel loginLabel = new JLabel("Login");

		loginLabel.setForeground(Color.WHITE);
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginLabel.setFont(new Font("Arial", Font.BOLD, 12));
		loginLabel.setBounds(0, 0, 119, 30);
		loginButton.add(loginLabel);
		
		// LoginButton logic that consumes textfields and logs a user in via the SQLite database
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				loginButton.setBackground(new Color(255, 219, 5));
				loginLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				loginButton.setBackground(new Color(0, 124, 65));
				loginLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String user = username.getText();
				String pw = String.valueOf(password.getPassword());

				// retrieves login details (checkLogin returns -1 -1 for nonmatching username +
				// password combo)
				// loginDetails[0]=usertype [1]=userID [2]=name
				String[] loginDetails = checkLogin(user, pw);

				if (loginDetails[0].equals("0")) {
					login.dispose();
					Admin admin = new Admin(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
					admin.setVisible(true);
					admin.setLocationRelativeTo(null);
				} else if (loginDetails[0].equals("1")) {
					login.dispose();
					Author author = new Author(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
					author.setVisible(true);
					author.setLocationRelativeTo(null);
				} else if (loginDetails[0].equals("2")) {
					login.dispose();
					Reviewer reviewer = new Reviewer(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
					reviewer.setVisible(true);
					reviewer.setLocationRelativeTo(null);
				} else if (loginDetails[0].equals("3")) {
					password.setText("");
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Your account is currently in review by the administrator",
							"Waiting for Approval", JOptionPane.PLAIN_MESSAGE, null);
				} else {
					password.setText("");
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Error",
							JOptionPane.PLAIN_MESSAGE, null);
				}

			}
		});
		loginButton.setBackground(new Color(0, 124, 65));
		loginButton.setBounds(163, 360, 119, 30);
		rightPanel.add(loginButton);
		loginButton.setLayout(null);

		JLabel signupLabel = new JLabel("Sign Up");
		signupLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				signupLabel.setForeground(new Color(0, 124, 65));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				signupLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				Signup signup = new Signup(conn);

				signup.setVisible(true);
				signup.setLocationRelativeTo(null);

			}
		});
		signupLabel.setForeground(Color.BLACK);
		signupLabel.setFont(new Font("Arial", Font.BOLD, 12));
		signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
		signupLabel.setBounds(163, 430, 119, 20);
		rightPanel.add(signupLabel);
	}

	/**
	 * Queries mySQL database for matching username and password and returns account
	 * type as an int
	 *
	 * @param username, The username of the user trying to log in
	 * @param password, The password of the user trying to log in
	 * @return loginInfo, The account type as an int (0=admin, 1=author, 2=reviewer)
	 */
	private String[] checkLogin(String username, String password) {
		// ps is the statement of the sql query
		PreparedStatement ps = null;

		// rs is the result of the query
		ResultSet rs;

		// Array of details to be returned
		String[] loginInfo = new String[3];

		// initializing the array so there are no nulls
		loginInfo[0] = "-1";
		loginInfo[1] = "-1";
		loginInfo[2] = "";

		// the string to be used to query. ? indicates a parameter
		// this query finds entries that match the input username and password
		// the BINARY operator forces byte by byte comparison (case sensitivity)
		String query = "SELECT * FROM users WHERE username =? COLLATE NOCASE AND password = ?";

		try {
			ps = conn.prepareStatement(query);

			// set first parameter to be username
			ps.setString(1, username);
			// set second parameter to password
			ps.setString(2, password);

			// executes the query and receives whatever result is returned
			rs = ps.executeQuery();

			// checks if any entries resulted from query
			// since there exists a matching username and password entry
			// it sets checkUser to 1, indicating a match
			if (rs.next()) {
				// loginExists gets the usertype field from the query
				// 0=admin, 1=author, 2=reviewer
				if (Integer.toString(rs.getInt("usertype")) != null)
					loginInfo[0] = Integer.toString(rs.getInt("usertype"));
				loginInfo[1] = Integer.toString(rs.getInt("userID"));
				loginInfo[2] = rs.getString("username");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return loginInfo;
	}

	/**
	 * Logic for pressing enter key on login screen Exhibits the same behavior as
	 * clicking login button
	 * 
	 * @param arg0, A keyboard press
	 */
	@SuppressWarnings("static-access")
	public void keyeventLogic(KeyEvent arg0) {

		// Checks if the Enter key was hit
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {

			String user = username.getText();
			String pw = String.valueOf(password.getPassword());

			// loginDetails[0]=usertype [1]=userID [2]=name
			String[] loginDetails = checkLogin(user, pw);

			if (loginDetails[0].equals("0")) {
				login.setVisible(false);
				Admin admin = new Admin(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
				admin.setVisible(true);
				admin.setLocationRelativeTo(null);
			} else if (loginDetails[0].equals("1")) {
				login.setVisible(false);
				Author author = new Author(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
				author.setVisible(true);
				author.setLocationRelativeTo(null);
			} else if (loginDetails[0].equals("2")) {
				login.setVisible(false);
				Reviewer reviewer = new Reviewer(loginDetails[2], Integer.parseInt(loginDetails[1]), conn);
				reviewer.setVisible(true);
				reviewer.setLocationRelativeTo(null);
			} else if (loginDetails[0].equals("3")) {
				password.setText("");
				UIManager UI = new UIManager();
				UI.put("OptionPane.background", Color.WHITE);
				UI.put("Panel.background", Color.WHITE);

				JOptionPane.showMessageDialog(null, "Your account is currently in review by the administrator",
						"Waiting for Approval", JOptionPane.PLAIN_MESSAGE, null);
			} else {
				password.setText("");
				UIManager UI = new UIManager();
				UI.put("OptionPane.background", Color.WHITE);
				UI.put("Panel.background", Color.WHITE);

				JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Error",
						JOptionPane.PLAIN_MESSAGE, null);
			}

		}
	}

}
