import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

public class Author {

	private JFrame frmAuthor;

	/**
	 * Launch the application.
	 */
	public static void Author() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author window = new Author();
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
	public Author() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAuthor = new JFrame();
		frmAuthor.setResizable(false);
		frmAuthor.setTitle("Author");
		frmAuthor.setBounds(100, 100, 450, 300);
		frmAuthor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAuthor.getContentPane().setBackground(Color.white);
		
		JLabel lblNewLabel = new JLabel("Author's View");
		GroupLayout groupLayout = new GroupLayout(frmAuthor.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(176)
					.addComponent(lblNewLabel)
					.addContainerGap(193, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(93)
					.addComponent(lblNewLabel)
					.addContainerGap(154, Short.MAX_VALUE))
		);
		frmAuthor.getContentPane().setLayout(groupLayout);
	}
}
