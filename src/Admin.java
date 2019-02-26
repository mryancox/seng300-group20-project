import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

public class Admin {

	private JFrame frmAdmin;

	/**
	 * Launch the application.
	 */
	public static void Admin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin window = new Admin();
					window.frmAdmin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Admin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdmin = new JFrame();
		frmAdmin.setTitle("Admin");
		frmAdmin.setResizable(false);
		frmAdmin.setBounds(100, 100, 450, 300);
		frmAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdmin.getContentPane().setBackground(Color.white);
		
		JLabel adminLabel = new JLabel("Admin's View");
		GroupLayout groupLayout = new GroupLayout(frmAdmin.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(188)
					.addComponent(adminLabel)
					.addContainerGap(200, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(108)
					.addComponent(adminLabel)
					.addContainerGap(139, Short.MAX_VALUE))
		);
		frmAdmin.getContentPane().setLayout(groupLayout);
	}

}
