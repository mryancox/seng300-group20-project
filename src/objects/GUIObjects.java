package objects;

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

/*
 * GUIObjects handles the creation of graphical elements that are present in
 * the Admin, Author, and Reviewer classes.
 * 
 * @author L01-Group20
 */
public class GUIObjects {
	
	/*
	 * menuPanel creates the basic menu elements that appear on the left hand side
	 * of the GUI when logged into an account
	 * 
	 * @return menuPanel The menu GUI element
	 */
	public JPanel menuPanel() {
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(0, 124, 65));
		menuPanel.setBounds(0, 0, 180, 580);
		menuPanel.setLayout(null);
		
		JLabel menuLabel = new GUIObjects().menuLabel("MENU", 30);
		menuPanel.add(menuLabel);

		JPanel menuSeparator = new JPanel();
		menuSeparator.setBounds(10, 70, 160, 2);
		menuPanel.add(menuSeparator);
		
		JLabel ualogo = new GUIObjects().icon("/ualogo.jpg");
		ualogo.setBounds(40, 420, 100, 100);
		menuPanel.add(ualogo);
		
		return menuPanel;
	}
	
	/*
	 * menuButton creates a button that can be added to menu of the GUI of a certain position and height
	 * 
	 * @param y_position The y position in the menu for the button to be placed at
	 * @param height The height of the button being made
	 * @return menuButton The new button for the menu
	 */
	public JPanel menuButton(int y_position, int height) {
		
		JPanel menuButton = new JPanel();
		menuButton.setBackground(new Color(0, 124, 65));
		menuButton.setBounds(0, y_position, 180, height);
		menuButton.setLayout(null);
		
		return menuButton;
	}
	
	/*
	 * menuLabel creates a label that can be added to a menu button of the GUI with text and a given y position
	 * 
	 * @param text The descriptive text for the menuButton
	 * @param y_position The y position in the button for the label to be placed at
	 * @return menuLabel The new button for the menu
	 */
	public JLabel menuLabel(String text, int y_position) {
		
		JLabel menuLabel = new JLabel(text);
		menuLabel.setForeground(Color.WHITE);
		if (text == "MENU") {
			menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
			menuLabel.setBounds(0, y_position, 180, 40);
		} else {
			menuLabel.setFont(new Font("Arial", Font.BOLD, 14));
			menuLabel.setBounds(0, y_position, 180, 30);
		}
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		return menuLabel;
	}
	
	/*
	 * mainContentPanel creates the right hand content area that has different panels in a card layout
	 * 
	 * @return contentPanel The main area to interact with the GUI that holds different content panels
	 */
	public JPanel mainContentPanel() {
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(180, 0, 725, 580);
		contentPanel.setLayout(new CardLayout(0, 0));
		
		return contentPanel;
	}
	
	/*
	 * contentPanel panels that contain elements and are added as a card to the content area
	 * 
	 * @return content A panel that contains interactive elements for a specific task
	 */
	public JPanel contentPanel() {
		
		JPanel content = new JPanel();
		content.setBackground(Color.WHITE);
		content.setLayout(null);
		
		return content;
	}

	/*
	 * welcomeLabel creates the welcome text that greets the user upon logging in
	 * 
	 * @param user The user who logged into the system
	 * @return welcomeLabel A nicely formatted label based on the user's name who logged in
	 */
	public JLabel welcomeLabel(String user) {
		
		String removeProvider = user.split("\\@")[0];
		String[] nameArray = removeProvider.split("\\.");
		StringBuilder formattedUser = new StringBuilder("");
		for (int i = 0; i < nameArray.length; i++) {
			formattedUser.append(String.valueOf(nameArray[i].charAt(0)).toUpperCase() + nameArray[i].substring(1));
			if (i < nameArray.length - 1) {
				formattedUser.append(" ");
			}
		}
		JLabel welcomeLabel = new JLabel("Welcome " + formattedUser + "!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
		welcomeLabel.setBounds(0, 124, 714, 40);
		
		return welcomeLabel;
	}
	
	/*
	 * icon creates generals images for the different panes using a label
	 * 
	 * @param image The path to the image being placed in the GUI
	 * @return icon A label that contains the image from the path specified
	 */
	public JLabel icon(String image) {
		
		JLabel icon = new JLabel("");
		Image iconImg = new ImageIcon(this.getClass().getResource(image)).getImage();
		icon.setIcon(new ImageIcon(iconImg));
		
		return icon;
	}
	
	/*
	 * contentTitle creates a title for content panes using argument text
	 * 
	 * @param text The text for the title of the pane
	 * @return title A label for the GUI
	 */
	public JLabel contentTitle(String text) {
		
		JLabel title = new JLabel(text);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setBounds(24, 30, 325, 40);
		
		return title;
	}
	
	/*
	 * contentHeader creates a header using argument text and y_position on a content pane
	 * 
	 * @param text The text to display in a header for GUI elements
	 * @param y_position The position to place the header in the GUI
	 * @return header A label containing the header's text at the proper position
	 */
	public JLabel contentHeader(String text, int y_position) {
		
		JLabel header = new JLabel(text);
		header.setFont(new Font("Arial", Font.BOLD, 14));
		header.setBounds(24, y_position, 350, 18);
		
		return header;
	}
	
	/*
	 * contentSubHeader creates a sub header using argument text and y_position on a content pane
	 * 
	 * @param text The text to display in a sub header for GUI elements
	 * @param y_position The position to place the header in the GUI
	 * @return subheader A label containing the subheader's text at the proper position
	 */
	public JLabel contentSubHeader(String text, int y_position) {
		
		JLabel subheader = new JLabel(text);
		subheader.setForeground(Color.DARK_GRAY);
		subheader.setFont(new Font("Arial", Font.BOLD, 12));
		subheader.setBounds(24, y_position, 350, 18);
		
		return subheader;
	}
	
	/*
	 * contentLabel creates a label in the GUI from text, x and y positions passed in as arguments
	 * 
	 * @text The text to display in a label in the GUI
	 * @x_position The x position in the GUI the label will be placed at
	 * @y_position The y position in the GUI the label will be placed at
	 * @return label The content label with argument text at a specific location
	 */
	public JLabel contentLabel(String text, int x_position, int y_position) {
		
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setBounds(x_position, y_position, 325, 18);
		
		return label;
	}
	
	/*
	 * contentLabel creates a sub label in the GUI from x and y positions passed in as arguments
	 * 
	 * @x_position The x position in the GUI the label will be placed at
	 * @y_position The y position in the GUI the label will be placed at
	 * @return label The content label with argument text at a specific location
	 */
	public JLabel contentSubLabel(int x_position, int y_position) {
		
		JLabel sublabel = new JLabel("");
		sublabel.setFont(new Font("Arial", Font.PLAIN, 12));
		sublabel.setBounds(x_position, y_position, 325, 18);
		
		return sublabel;
	}
	
	/*
	 * contentButton creates a button that can be added to the GUI at a certain x and y position
	 * that are passed in as arguments
	 * 
	 * @param x_position The x position in the content pane for the button to be placed at
	 * @param y_position The y position in the content pane for the button to be placed at
	 * @return button The new button for the content pane
	 */
	public JPanel contentButton(int x_position, int y_position) {
		
		JPanel button = new JPanel();
		button.setLayout(null);
		button.setBackground(new Color(0, 124, 65));
		button.setBounds(x_position, y_position, 120, 30);
		
		return button;
	}
	
	/*
	 * contentSortButton creates a button that can be added to the GUI with a certain width, x and y position
	 * that are passed in as arguments
	 * 
	 * @param width The width of the button
	 * @param x_position The x position in the content pane for the button to be placed at
	 * @param y_position The y position in the content pane for the button to be placed at
	 * @return sortbutton The new sorting button for the content pane
	 */
	public JPanel contentSortButton(int width, int x_position, int y_position) {
		
		JPanel sortbutton = new JPanel();
		sortbutton.setLayout(null);
		sortbutton.setBackground(new Color(0, 124, 65));
		sortbutton.setBounds(x_position, y_position, width, 20);
		
		return sortbutton;
	}
	
	/*
	 * contentButtonLabel creates a label for a button that can be added to the GUI with argument text
	 * 
	 * @param text The text the label will display on the button
	 * @return buttonLabel A label for the content's button
	 */
	public JLabel contentButtonLabel(String text) {
		
		JLabel buttonLabel = new JLabel(text);
		buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buttonLabel.setForeground(Color.WHITE);
		buttonLabel.setFont(new Font("Arial", Font.BOLD, 12));
		buttonLabel.setBounds(0, 0, 120, 30);
		
		return buttonLabel;
	}
	
	/*
	 * contentSortButtonLabel creates a label for a button that can be added to the GUI with argument text and width
	 * 
	 * @param text The text the label will display on the button
	 * @param width The width of the label to be added to the button
	 * @return sortbuttonLabel A label for the content's sortbutton
	 */
	public JLabel contentSortButtonLabel(String text, int width) {
		
		JLabel sortbuttonLabel = new JLabel(text);
		sortbuttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sortbuttonLabel.setForeground(Color.WHITE);
		sortbuttonLabel.setFont(new Font("Arial", Font.BOLD, 12));
		sortbuttonLabel.setBounds(0, 0, width, 20);
		
		return sortbuttonLabel;
	}
	
	/*
	 * contentTextArea creates a text area element in the GUI at a given vertical position
	 * 
	 * @param y_position The vertical position in the GUI the textarea will be at
	 * @return textarea The text area element
	 */
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
	
	/*
	 * separatorPanel creates a horizontal line using a panel in the GUI using argument y_position
	 * 
	 * @param y_position The vertical position in the content pane for the line to be at
	 * @return separator GUI separator line at a certain vertical position
	 */
	public JPanel separatorPanel(int y_position) {
		
		JPanel separator = new JPanel();
		separator.setBackground(Color.BLACK);
		separator.setBounds(24, y_position, 650, 2);
		
		return separator;
	}
}
