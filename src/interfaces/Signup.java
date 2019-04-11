package interfaces;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import objects.SQLConnection;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.CardLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/*
 * Signup creates a frame that handles author and reviewer signup.
 * 
 * @author - L01-Group20
 */
public class Signup extends JFrame implements Constants {

	/**
	 * Textfields to gather information from the new user.
	 */
	private static final long serialVersionUID = 1L;
	private JTextField newusernameTextField;
	private JPasswordField newPasswordField;
	private JTextField occupationTextField;
	private JTextField orgTextField;
	private JTextField areaTextField;
	private String reviewerUsername;
	private String reviewerPassword;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signup frame = new Signup(SQLConnection.getConnection());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the Signup frame.
	 * 
	 * @param conn The connection to the local database initialized in the Login class
	 */
	public Signup(Connection conn) {
		
		// Adds the connection passed in from the Login class
		this.conn = conn;

		// Setup of the frame
		getContentPane().setBackground(Color.WHITE);
		setTitle("Signup");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 500);
		getContentPane().setLayout(null);
		Image uaIconImg = new ImageIcon(this.getClass().getResource("/ualogowhite.jpg")).getImage();

		/*
		 * What follows is a lot of GUI elements that create the look of the signup frame.
		 * In general, 2 JPanels are used to hold other elements and are switched between
		 * from a main signupPanel that has a card layout.
		 * 
		 * Major functionality is closer to the end of this class.
		 */
		JPanel signupPanel = new JPanel();
		signupPanel.setBounds(0, 0, 410, 490);
		getContentPane().add(signupPanel);
		signupPanel.setLayout(new CardLayout(0, 0));

		JPanel basicPanel = new JPanel();
		basicPanel.setBackground(Color.WHITE);
		signupPanel.add(basicPanel, "name_473435295818200");
		basicPanel.setLayout(null);

		JPanel extrasPanel = new JPanel();
		extrasPanel.setBackground(Color.WHITE);
		signupPanel.add(extrasPanel, "name_473877714574200");
		extrasPanel.setLayout(null);

		JLabel uaIcon = new JLabel("");
		uaIcon.setBounds(145, 10, 97, 98);
		basicPanel.add(uaIcon);
		uaIcon.setIcon(new ImageIcon(uaIconImg));

		JLabel newusernameLabel = new JLabel("New Username (Email Address)");
		newusernameLabel.setBounds(40, 120, 300, 20);
		basicPanel.add(newusernameLabel);
		newusernameLabel.setForeground(new Color(0, 124, 65));
		newusernameLabel.setFont(new Font("Arial", Font.BOLD, 12));

		newusernameTextField = new JTextField();
		newusernameTextField.setBounds(40, 150, 300, 30);
		basicPanel.add(newusernameTextField);
		newusernameTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		newusernameTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		newusernameTextField.setColumns(10);

		JPanel usernameSepPanel = new JPanel();
		usernameSepPanel.setBounds(40, 180, 300, 2);
		basicPanel.add(usernameSepPanel);
		usernameSepPanel.setBackground(new Color(0, 124, 65));

		JLabel newpasswordLabel = new JLabel("New Password");
		newpasswordLabel.setBounds(40, 200, 300, 20);
		basicPanel.add(newpasswordLabel);
		newpasswordLabel.setForeground(new Color(0, 124, 65));
		newpasswordLabel.setFont(new Font("Arial", Font.BOLD, 12));

		newPasswordField = new JPasswordField();
		newPasswordField.setBounds(40, 230, 300, 30);
		basicPanel.add(newPasswordField);
		newPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
		newPasswordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		JPanel passwordSepPanel = new JPanel();
		passwordSepPanel.setBounds(40, 260, 300, 2);
		basicPanel.add(passwordSepPanel);
		passwordSepPanel.setBackground(new Color(0, 124, 65));

		JLabel usertypeLabel = new JLabel("Choose User Type");
		usertypeLabel.setBounds(40, 300, 300, 20);
		basicPanel.add(usertypeLabel);
		usertypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usertypeLabel.setForeground(new Color(0, 124, 65));
		usertypeLabel.setFont(new Font("Arial", Font.BOLD, 12));

		JRadioButton rdbtnAuthor = new JRadioButton("Author");
		JRadioButton rdbtnReviewer = new JRadioButton("Reviewer");

		JPanel contextualButton = new JPanel();
		contextualButton.setBounds(130, 400, 120, 30);
		basicPanel.add(contextualButton);
		contextualButton.setBackground(new Color(0, 124, 65));
		JLabel contextualLabel = new JLabel("Signup");
		contextualLabel.setFont(new Font("Arial", Font.BOLD, 12));
		contextualLabel.setForeground(Color.WHITE);
		contextualLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contextualLabel.setBounds(0, 0, 120, 30);
		contextualButton.add(contextualLabel);

		ButtonGroup bg = new ButtonGroup();

		rdbtnAuthor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				contextualLabel.setText("Signup");
			}
		});
		rdbtnAuthor.setBounds(90, 330, 100, 20);
		basicPanel.add(rdbtnAuthor);
		rdbtnAuthor.setFont(new Font("Arial", Font.PLAIN, 12));
		rdbtnAuthor.setBackground(Color.WHITE);
		bg.add(rdbtnAuthor);

		rdbtnReviewer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				contextualLabel.setText("Next");
			}
		});
		rdbtnReviewer.setBounds(210, 330, 100, 20);
		basicPanel.add(rdbtnReviewer);
		rdbtnReviewer.setFont(new Font("Arial", Font.PLAIN, 12));
		rdbtnReviewer.setBackground(Color.WHITE);
		bg.add(rdbtnReviewer);

		JLabel uaIcon2 = new JLabel("");
		uaIcon2.setBounds(145, 10, 97, 98);
		extrasPanel.add(uaIcon2);
		uaIcon2.setIcon(new ImageIcon(uaIconImg));

		JLabel moredetailsLabel = new JLabel("Please Provide More Details To Become A Reviewer");
		moredetailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		moredetailsLabel.setForeground(new Color(0, 124, 65));
		moredetailsLabel.setFont(new Font("Arial", Font.BOLD, 12));
		moredetailsLabel.setBounds(40, 110, 300, 20);
		extrasPanel.add(moredetailsLabel);

		JLabel occupationLabel = new JLabel("Occupation");
		occupationLabel.setForeground(new Color(0, 124, 65));
		occupationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		occupationLabel.setBounds(40, 150, 300, 20);
		extrasPanel.add(occupationLabel);

		occupationTextField = new JTextField();
		occupationTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		occupationTextField.setBounds(40, 180, 300, 30);
		occupationTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		extrasPanel.add(occupationTextField);
		occupationTextField.setColumns(10);

		JPanel occupationSepPanel = new JPanel();
		occupationSepPanel.setBackground(new Color(0, 124, 65));
		occupationSepPanel.setBounds(40, 210, 300, 2);
		extrasPanel.add(occupationSepPanel);

		JLabel organizationLabel = new JLabel("Organization");
		organizationLabel.setForeground(new Color(0, 124, 65));
		organizationLabel.setFont(new Font("Arial", Font.BOLD, 12));
		organizationLabel.setBounds(40, 230, 300, 20);
		extrasPanel.add(organizationLabel);

		orgTextField = new JTextField();
		orgTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		orgTextField.setBounds(40, 260, 300, 30);
		orgTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		extrasPanel.add(orgTextField);
		orgTextField.setColumns(10);

		JPanel organizationSepPanel = new JPanel();
		organizationSepPanel.setBackground(new Color(0, 124, 65));
		organizationSepPanel.setBounds(40, 290, 300, 2);
		extrasPanel.add(organizationSepPanel);

		JLabel areaLabel = new JLabel("Research Area");
		areaLabel.setForeground(new Color(0, 124, 65));
		areaLabel.setFont(new Font("Arial", Font.BOLD, 12));
		areaLabel.setBounds(40, 310, 300, 20);
		extrasPanel.add(areaLabel);

		areaTextField = new JTextField();
		areaTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		areaTextField.setBounds(40, 340, 300, 30);
		areaTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		extrasPanel.add(areaTextField);
		areaTextField.setColumns(10);

		JPanel areaSepPanel = new JPanel();
		areaSepPanel.setBackground(new Color(0, 124, 65));
		areaSepPanel.setBounds(40, 370, 300, 2);
		extrasPanel.add(areaSepPanel);

		JPanel submitButton = new JPanel();
		JLabel submitLabel = new JLabel("Submit");
		submitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitLabel.setForeground(Color.WHITE);
		submitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		submitLabel.setBounds(0, 0, 120, 30);
		submitButton.add(submitLabel);

		// This button either submits the text field data (if entered correctly) on
		// the initial signup panel if the author radio button is selected. Otherwise
		// it takes the user to the next card for more reviewer details.
		contextualButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				contextualButton.setBackground(new Color(255, 219, 5));
				contextualLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				contextualButton.setBackground(new Color(0, 124, 65));
				contextualLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// logic for if reviewer button is selected
				if (rdbtnReviewer.isSelected()) {

					// check if either username or password field is empty
					if (newusernameTextField.getText().isEmpty() || newPasswordField.getPassword().length == 0) {
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);

						JOptionPane.showMessageDialog(null, "Please ensure no fields are empty", "Empty Fields",
								JOptionPane.PLAIN_MESSAGE, null);
					} else {

						// check if username matches email regex
						if (!newusernameTextField.getText()
								.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")) {
							UIManager UI = new UIManager();
							UI.put("OptionPane.background", Color.WHITE);
							UI.put("Panel.background", Color.WHITE);

							JOptionPane.showMessageDialog(null, "Please enter a valid email address", "Invalid Email",
									JOptionPane.PLAIN_MESSAGE, null);
						} else {
							reviewerUsername = newusernameTextField.getText();

							// check if username is already registered
							boolean duplicate = checkDuplicateUsername(reviewerUsername);
							if (!duplicate) {
								reviewerPassword = String.valueOf(newPasswordField.getPassword());
								signupPanel.removeAll();
								signupPanel.repaint();
								signupPanel.revalidate();

								signupPanel.add(extrasPanel);
								signupPanel.repaint();
								signupPanel.revalidate();
							} else {
								UIManager UI = new UIManager();
								UI.put("OptionPane.background", Color.WHITE);
								UI.put("Panel.background", Color.WHITE);

								JOptionPane.showMessageDialog(null, "This username is already registered",
										"Username invalid", JOptionPane.PLAIN_MESSAGE, null);
							}
						}
					}

				}

				// logic for if author button is selected
				else if (rdbtnAuthor.isSelected()) {

					// first check if any field is empty
					if (newusernameTextField.getText().isEmpty() || newPasswordField.getPassword().length == 0) {
						UIManager UI = new UIManager();
						UI.put("OptionPane.background", Color.WHITE);
						UI.put("Panel.background", Color.WHITE);

						JOptionPane.showMessageDialog(null, "Please ensure no fields are empty", "Empty Fields",
								JOptionPane.PLAIN_MESSAGE, null);
					} else {

						// then check if email matches regex
						if (!newusernameTextField.getText()
								.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")) {
							UIManager UI = new UIManager();
							UI.put("OptionPane.background", Color.WHITE);
							UI.put("Panel.background", Color.WHITE);

							JOptionPane.showMessageDialog(null, "Please enter a valid email address", "Invalid Email",
									JOptionPane.PLAIN_MESSAGE, null);
						} else {
							String username = newusernameTextField.getText();

							// check if username is already registered
							boolean duplicate = checkDuplicateUsername(username);
							if (!duplicate) {
								String password = String.valueOf(newPasswordField.getPassword());

								// userType for author is 1
								submitAuthor(username, password, 1);

								UIManager UI = new UIManager();
								UI.put("OptionPane.background", Color.WHITE);
								UI.put("Panel.background", Color.WHITE);

								JOptionPane.showMessageDialog(null, "Sign up successful!", "Sign up Success",
										JOptionPane.PLAIN_MESSAGE, null);

								//setVisible(false);
								dispose();
							} else {
								UIManager UI = new UIManager();
								UI.put("OptionPane.background", Color.WHITE);
								UI.put("Panel.background", Color.WHITE);

								JOptionPane.showMessageDialog(null, "This username is already registered",
										"Username invalid", JOptionPane.PLAIN_MESSAGE, null);
							}

						}

					}
				}
			}
		});
		contextualButton.setLayout(null);

		// Handles the submission of addition reviewer data if they were taken to the
		// extra's panel
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {

				submitButton.setBackground(new Color(255, 219, 5));
				submitLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

				submitButton.setBackground(new Color(0, 124, 65));
				submitLabel.setForeground(Color.WHITE);
			}

			@SuppressWarnings("static-access")
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// check if any of the three fields is empty
				if (occupationTextField.getText().isEmpty()
						|| orgTextField.getText().isEmpty() | areaTextField.getText().isEmpty()) {
					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Please ensure no fields are empty", "Empty Fields",
							JOptionPane.PLAIN_MESSAGE, null);
				} else {

					String occupation = occupationTextField.getText();
					String organization = orgTextField.getText();
					String research = areaTextField.getText();

					submitReviewer(reviewerUsername, reviewerPassword, 3, occupation, organization, research);

					UIManager UI = new UIManager();
					UI.put("OptionPane.background", Color.WHITE);
					UI.put("Panel.background", Color.WHITE);

					JOptionPane.showMessageDialog(null, "Sign up successful!\nPlease wait for an administrator to verify your credentials before logging in.", "Sign up Success",
							JOptionPane.PLAIN_MESSAGE, null);

					setVisible(false);
					dispose();
				}

			}
		});
		submitButton.setLayout(null);
		submitButton.setBackground(new Color(0, 124, 65));
		submitButton.setBounds(130, 400, 120, 30);
		extrasPanel.add(submitButton);
		
		

	}

	/**
	 * Submits a new author entry to database
	 * 
	 * @param username, The entered username form the username field
	 * @param password, The entered password from the password field
	 * @param userType, Whether they are a reviewer or author (always the same in this case)
	 */
	private void submitAuthor(String username, String password, int userType) {
		PreparedStatement ps;

		String query = "INSERT INTO users (username, password, email, userType) VALUES (?, ?, ?, ?)";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, username);
			ps.setInt(4, userType);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Submits a new reviewer entry to database, with usertype = 3 to indicate user
	 * in review
	 * 
	 * @param username, The entered username form the username field
	 * @param password, The entered password from the password field
	 * @param userType, Whether they are a reviewer or author (always the same in this case)
	 * @param occupation, The entered occupation
	 * @param organization, The entered organization
	 * @param research, The entered research area of the potential reviewer
	 */
	private void submitReviewer(String username, String password, int userType, String occupation, String organization,
			String research) {
		PreparedStatement ps;

		String query = "INSERT INTO users (username, password, email, userType, occupation, organization, research) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, username);
			ps.setInt(4, userType);
			ps.setString(5, occupation);
			ps.setString(6, organization);
			ps.setString(7, research);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Checks if a username already exists in the database
	 * 
	 * @param username, The username that is entered in the signup form
	 * @return true or false, Depending on if the username is already in the database
	 */
	private boolean checkDuplicateUsername(String username) {
		PreparedStatement ps;

		String query = "Select * FROM users WHERE username = ? COLLATE NOCASE";

		try {
			ps = SQLConnection.getConnection().prepareStatement(query);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
