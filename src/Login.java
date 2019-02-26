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
import javax.swing.JButton;

public class Login {

	private JFrame frame;
	private JTextField username;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 460, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.white);
		
		username = new JTextField();
		username.setColumns(10);
		
		password = new JTextField();
		password.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username:");
		
		JLabel passwordLabel = new JLabel("Password:");
		
		JLabel promptLabel = new JLabel("Enter your username and password to login");
		
		JButton loginButton = new JButton("Login");
		
		JLabel ualogoLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/ualogo.jpg")).getImage();
		ualogoLabel.setIcon(new ImageIcon(img));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ualogoLabel)
					.addContainerGap(315, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(186)
					.addComponent(loginButton)
					.addContainerGap(201, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(promptLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(108)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(usernameLabel)
								.addComponent(passwordLabel))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(username, 147, 147, 147)
								.addComponent(password, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))))
					.addGap(127))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ualogoLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addComponent(promptLabel)
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(usernameLabel)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordLabel))
					.addGap(18)
					.addComponent(loginButton)
					.addContainerGap(31, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
