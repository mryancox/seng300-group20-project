package seng300project;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class GUIObjects {
	
	/*
	 * Creates the left hand menu panel that contains menu buttons
	 */
	public JPanel menuPanel() {
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(0, 124, 65));
		menuPanel.setBounds(0, 0, 180, 580);
		menuPanel.setLayout(null);
		
		return menuPanel;
	}
	
	public JPanel menuButton(int y_position, int height) {
		
		JPanel menubutton = new JPanel();
		menubutton.setBackground(new Color(0, 124, 65));
		menubutton.setBounds(0, y_position, 180, height);
		menubutton.setLayout(null);
		
		return menubutton;
	}
	
	public JLabel menuLabel(String text, int y_position) {
		
		JLabel menulabel = new JLabel(text);
		menulabel.setForeground(Color.WHITE);
		if (text == "MENU") {
			menulabel.setFont(new Font("Arial", Font.BOLD, 18));
			menulabel.setBounds(0, y_position, 180, 40);
		} else {
			menulabel.setFont(new Font("Arial", Font.BOLD, 14));
			menulabel.setBounds(0, y_position, 180, 30);
		}
		menulabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		return menulabel;
	}
	
	/*
	 * Creates the right hand content area that has different panels in a card layout
	 */
	public JPanel mainContentPanel() {
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(180, 0, 725, 580);
		contentPanel.setLayout(new CardLayout(0, 0));
		
		return contentPanel;
	}
	
	/*
	 * Creates content panels that contain elements and are added as a card to the content area
	 */
	public JPanel contentPanel() {
		
		JPanel content = new JPanel();
		content.setBackground(Color.WHITE);
		content.setLayout(null);
		
		return content;
	}

	/*
	 * Creates the welcome text that greets the user upon logging in
	 */
	public JLabel welcomeLabel(String user) {
		
		String niceUsername = String.valueOf(user.charAt(0)).toUpperCase() + user.substring(1).split("\\@")[0];
		JLabel welcomeLabel = new JLabel("Welcome " + niceUsername + "!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
		welcomeLabel.setBounds(0, 124, 714, 40);
		
		return welcomeLabel;
	}
	
	/*
	 * Creates the icon for the user type on the welcome pane
	 */
	public JLabel icon(String image) {
		
		JLabel icon = new JLabel("");
		Image iconImg = new ImageIcon(this.getClass().getResource(image)).getImage();
		icon.setIcon(new ImageIcon(iconImg));
		
		return icon;
	}
	
	/*
	 * Creates a title using argument text on a content pane
	 */
	public JLabel contentTitle(String text) {
		
		JLabel title = new JLabel(text);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setBounds(24, 30, 325, 40);
		
		return title;
	}
	
	/*
	 * Creates a header using argument text and y_position on a content pane
	 */
	public JLabel contentHeader(String text, int y_position) {
		
		JLabel header = new JLabel(text);
		header.setFont(new Font("Arial", Font.BOLD, 14));
		header.setBounds(24, y_position, 350, 18);
		
		return header;
	}
	
	/*
	 * Creates a sub header using argument text and y_position on a content pane
	 */
	public JLabel contentSubHeader(String text, int y_position) {
		
		JLabel subheader = new JLabel(text);
		subheader.setForeground(Color.DARK_GRAY);
		subheader.setFont(new Font("Arial", Font.BOLD, 12));
		subheader.setBounds(24, y_position, 350, 18);
		
		return subheader;
	}
	
	public JLabel contentLabel(String text, int x_position, int y_position) {
		
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setBounds(x_position, y_position, 325, 18);
		
		return label;
	}
	
	public JLabel contentSubLabel(int x_position, int y_position) {
		
		JLabel sublabel = new JLabel("");
		sublabel.setFont(new Font("Arial", Font.PLAIN, 12));
		sublabel.setBounds(x_position, y_position, 325, 18);
		
		return sublabel;
	}
	
	public JPanel contentButton(int x_position, int y_position) {
		
		JPanel button = new JPanel();
		button.setLayout(null);
		button.setBackground(new Color(0, 124, 65));
		button.setBounds(x_position, y_position, 120, 30);
		
		return button;
	}
	
	public JPanel contentSortButton(int width, int x_position, int y_position) {
		
		JPanel sortbutton = new JPanel();
		sortbutton.setLayout(null);
		sortbutton.setBackground(new Color(0, 124, 65));
		sortbutton.setBounds(x_position, y_position, width, 20);
		
		return sortbutton;
	}
	
	public JLabel contentButtonLabel(String text) {
		
		JLabel buttonLabel = new JLabel(text);
		buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buttonLabel.setForeground(Color.WHITE);
		buttonLabel.setFont(new Font("Arial", Font.BOLD, 12));
		buttonLabel.setBounds(0, 0, 120, 30);
		
		return buttonLabel;
	}
	
	public JLabel contentSortButtonLabel(String text, int width) {
		
		JLabel sortbuttonLabel = new JLabel(text);
		sortbuttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortbuttonLabel.setForeground(Color.WHITE);
		sortbuttonLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortbuttonLabel.setBounds(0, 0, width, 20);
		
		return sortbuttonLabel;
	}
	
	public JTextArea contentTextArea(int y_position) {
		
		JTextArea textarea = new JTextArea();
		textarea.setFont(new Font("Arial", Font.PLAIN, 12));
		textarea.setBounds(24, y_position, 350, 30);
		textarea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_TAB) {
					textarea.transferFocus();
					arg0.consume();
				}
			}
		});
		
		return textarea;
	}
	
	public JPanel separatorPanel(int y_position) {
		
		JPanel separator = new JPanel();
		separator.setBackground(Color.BLACK);
		separator.setBounds(24, y_position, 650, 2);
		
		return separator;
	}
}
