package seng300project;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Signup extends JFrame implements Constants {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField newusernameTextField;
	private JPasswordField newPasswordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signup frame = new Signup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 *
	 */
	public Signup() {
		getContentPane().setBackground(Color.WHITE);
		
		
		setTitle("Signup");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 500);
		getContentPane().setLayout(null);
		
		JLabel uaIcon = new JLabel("");
		Image uaIconImg = new ImageIcon(this.getClass().getResource("/ualogowhite.jpg")).getImage();
		uaIcon.setIcon(new ImageIcon(uaIconImg));
		uaIcon.setBounds(145, 15, 97, 98);
		getContentPane().add(uaIcon);
		
		JLabel newusernameLabel = new JLabel("New Username (Email Address)");
		newusernameLabel.setForeground(new Color(0, 124, 65));
		newusernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		newusernameLabel.setBounds(40, 125, 300, 20);
		getContentPane().add(newusernameLabel);
		
		newusernameTextField = new JTextField();
		newusernameTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		newusernameTextField.setBounds(40, 150, 300, 30);
		newusernameTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		getContentPane().add(newusernameTextField);
		newusernameTextField.setColumns(10);
		
		JPanel usernameSepPanel = new JPanel();
		usernameSepPanel.setBackground(new Color(0, 124, 65));
		usernameSepPanel.setBounds(40, 180, 300, 2);
		getContentPane().add(usernameSepPanel);
		
		JLabel newpasswordLabel = new JLabel("New Password");
		newpasswordLabel.setForeground(new Color(0, 124, 65));
		newpasswordLabel.setFont(new Font("Arial", Font.BOLD, 12));
		newpasswordLabel.setBounds(40, 205, 300, 20);
		getContentPane().add(newpasswordLabel);
		
		newPasswordField = new JPasswordField();
		newPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
		newPasswordField.setBounds(40, 230, 300, 30);
		newPasswordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		getContentPane().add(newPasswordField);
		
		JPanel passwordSepPanel = new JPanel();
		passwordSepPanel.setBackground(new Color(0, 124, 65));
		passwordSepPanel.setBounds(40, 260, 300, 2);
		getContentPane().add(passwordSepPanel);
		
		JLabel usertypeLabel = new JLabel("Choose User Type");
		usertypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usertypeLabel.setForeground(new Color(0, 124, 65));
		usertypeLabel.setFont(new Font("Arial", Font.BOLD, 12));
		usertypeLabel.setBounds(40, 300, 300, 20);
		getContentPane().add(usertypeLabel);
		
		JRadioButton rdbtnAuthor = new JRadioButton("Author");
		rdbtnAuthor.setFont(new Font("Arial", Font.PLAIN, 12));
		rdbtnAuthor.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnAuthor.setBackground(Color.WHITE);
		rdbtnAuthor.setBounds(80, 330, 100, 20);
		getContentPane().add(rdbtnAuthor);
		
		JRadioButton rdbtnReviewer = new JRadioButton("Reviewer");
		rdbtnReviewer.setFont(new Font("Arial", Font.PLAIN, 12));
		rdbtnReviewer.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnReviewer.setBackground(Color.WHITE);
		rdbtnReviewer.setBounds(200, 330, 100, 20);
		getContentPane().add(rdbtnReviewer);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnAuthor);
		bg.add(rdbtnReviewer);
		
		JPanel submitButton = new JPanel();
		submitButton.setBackground(new Color(0, 124, 65));
		submitButton.setBounds(135, 400, 120, 30);
		JLabel submitLabel = new JLabel("Submit");
		submitLabel.setFont(new Font("Arial", Font.BOLD, 12));
		submitLabel.setForeground(Color.WHITE);
		submitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		submitLabel.setBounds(0, 0, 120, 30);
		submitButton.add(submitLabel);
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
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		getContentPane().add(submitButton);
		submitButton.setLayout(null);
		
		
	}
}

