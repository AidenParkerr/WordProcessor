package gui;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

/**
 * A simple word processing application that allows the user to manipulate text
 * files in a way they see fit. Functions include create new text files, open an
 * existing text file, save the current text file, import already created
 * templates or use predefined templates,and a simple character counter.
 * 
 * 
 * 
 * @author Aiden Parker
 * @version 1
 */

public class Textual implements Runnable {

	/**
	 * Instantiates the {@code ButtonActionListeners} class.
	 * 
	 * @see ButtonActionListeners
	 */
	private static ButtonActionListeners actionListener = new ButtonActionListeners();

	/** Gets the width and height of the display output. */
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/** Instantiates the {@code UndoManager} class */
	private static UndoManager manager = new UndoManager();

	/** Sets a final variable of the minimum width the frame is valid to. */
	private static final int MIN_WIDTH = (int) screenSize.getWidth() / 2;

	/** Sets a final variable of the minimum height the frame is valid to. */
	private static final int MIN_HEIGHT = (int) screenSize.getHeight() / 2;
	/**
	 * Declares the icons that are to be used in the frame of the user interface.
	 */
	private static ImageIcon newFileIcon, openFileIcon, saveFileIcon, copyIcon, cutIcon, pasteIcon, templateIcon,
			boldIcon, underlineIcon, italicIcon, undoIcon, redoIcon;
	/**
	 * Declares the text pane instance that is used in the user interface, this is
	 * where the user enters text into.
	 */
	private static JTextPane textPane;

	/** Holds the text value of the characters */
	private static JLabel charCountLBL;
	/** Holds the number of characters {@code Integer} value */
	private static int charCount = 0;

	/** Holds the current save state of the application. */
	private static boolean isSaved;

	/** Declares the use of a thread for the character counter. */
	private static Thread charCountThread;

	/**
	 * Class constructor that assigns the icon variables a new instance of an image
	 * icon pointed to the relative path of each icon.
	 */
	Textual() {
		/*
		 * Assigns the relative Image to the ImageIcon instance.
		 */
		String seperator = File.separator;
		newFileIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"new-file-icon.png");
		openFileIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"open-file-icon.png");
		saveFileIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"save-file-icon.png");
		cutIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"cut.png");
		copyIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"copy.png");
		pasteIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"paste.png");
		templateIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"template.png");
		boldIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"bold.png");
		underlineIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"underline.png");
		italicIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"italic.png");
		undoIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"undo.png");
		redoIcon = new ImageIcon("."+seperator+"src"+seperator+"assets"+seperator+"redo.png");

	}

	/**
	 * A getter method that returns the {@code UndoManager} of the class.
	 * 
	 * @return manager the {@code UndoManager} of the class.
	 */
	public UndoManager getUndoRedoManager() {
		return manager;
	}

	/**
	 * A setter method used to set the value of the class variable {@code isSaved}
	 * to one of two boolean states.
	 * 
	 * @param value a boolean argument used to set the value of the {@code isSaved}
	 *              variable.
	 */
	public void setIsSaved(boolean value) {
		isSaved = value;
	}

	/**
	 * Gets the text content stored in the text pane if the value is not blank.
	 * 
	 * 
	 * @return byte array of the characters in the text pane
	 */
	protected byte[] getTextAreaBytes() {
		if (textPane.getText().isBlank()) {
			return null;
		}
		return textPane.getText().getBytes();
	}

	/**
	 * Sets the text area content by parsing a a byte array argument to a string in
	 * the UTF-8 format if not equal null. With UTF-8 being the encoder, some Asian
	 * characters may cause an issue.
	 * 
	 * 
	 * @param content a byte array with the byte value of each character to be added
	 *                to the text pane.
	 */
	protected void setTextPane(byte[] content) {
		if (content != null) {
			String string = new String(content, StandardCharsets.UTF_8);
			textPane.setText(string);
		}
	}

	/**
	 * Handles the creating of the menu bar that is situated at the very top of the
	 * window frame. Creates each of the menu items within each individual menu as
	 * well as styles them and passes each into the {@code ButtonActionListeners}
	 * class so an action listener can be added.
	 * 
	 * 
	 * 
	 * @return {@code JMenuBar} the created and populated menu bar
	 * @see ButtonActionListeners
	 */
	private static JMenuBar createMenuBar() {
		// File Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");

		// New File Menu Item
		JMenuItem menuItem = new JMenuItem("New");
		menuItem.setIcon(newFileIcon);
		actionListener.newFile(menuItem);
		menu.add(menuItem);

		// Open File Menu Item
		menuItem = new JMenuItem("Open");
		menuItem.setIcon(openFileIcon);
		actionListener.openFile(menuItem);
		menu.add(menuItem);

		// Save File Menu Item
		menuItem = new JMenuItem("Save");
		menuItem.setIcon(saveFileIcon);
		actionListener.saveFile(menuItem);
		menu.add(menuItem);

		menuBar.add(menu);

		// Edit Menu
		menu = new JMenu("Edit");
		menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
		menuItem.setText("Cut");
		menuItem.setIcon(cutIcon);
		menu.add(menuItem);
		menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		menuItem.setText("Copy");
		menuItem.setIcon(copyIcon);
		menu.add(menuItem);
		menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
		menuItem.setText("Paste");
		menuItem.setIcon(pasteIcon);
		menu.add(menuItem);

		menuBar.add(menu);

		// Templates Menu
		menu = new JMenu("Templates");
		menuItem = new JMenuItem("Open template");
		actionListener.openFile(menuItem);
		menuItem.setIcon(openFileIcon);
		menu.add(menuItem);
		menuItem = new JMenuItem("Save Template");
		actionListener.saveFile(menuItem);
		menuItem.setIcon(saveFileIcon);
		menu.add(menuItem);
		menuBar.add(menu);

		menu.addSeparator();

		// Templates Sub-Menu - Pre-defined Templates
		JMenu submenu = new JMenu("Pre-defined Templates");
		submenu.setIcon(templateIcon);

		// Dear Sir/Madam Menu Item
		menuItem = new JMenuItem("Dear Sir/Madam");
		menuItem.setActionCommand("dear");
		actionListener.showTemplate(menuItem);
		submenu.add(menuItem);

		// Kind Regards Menu Item
		menuItem = new JMenuItem("Kind Regards");
		menuItem.setActionCommand("regards");
		actionListener.showTemplate(menuItem);
		submenu.add(menuItem);
		// Many Thanks Menu Item
		menuItem = new JMenuItem("Many Thanks");
		menuItem.setActionCommand("thanks");
		actionListener.showTemplate(menuItem);
		submenu.add(menuItem);

		// Add the sub-menu to the Templates menu
		menu.add(submenu);

		// Adds the Templates menu to the menu bar
		menuBar.add(menu);

		return menuBar;
	}

	/**
	 * Handles the creation of the operations tool bar situated on the top of the
	 * window below the menu bar. Adds 3 {@code JToggleButton}'s to the panel which
	 * represent the Bold, Underline and Italic operations and an additional 2
	 * {@code JButton}'s representing the Undo and Re-do operations.
	 * 
	 * 
	 * @return {@code JPanel} populated tool bar
	 */
	private static JPanel createOperationsToolBar() {
		JPanel operationsToolbar = new JPanel();

		// Bold Toggle Button
		JToggleButton toggleButton = new JToggleButton();
		toggleButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		toggleButton.setIcon(boldIcon);
		actionListener.boldText(toggleButton);
		operationsToolbar.add(toggleButton);

		// Underline Toggle Button
		toggleButton = new JToggleButton();
		toggleButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		toggleButton.setIcon(underlineIcon);
		actionListener.underlineText(toggleButton);
		operationsToolbar.add(toggleButton);

		// Italic Toggle Button
		toggleButton = new JToggleButton();
		toggleButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		toggleButton.setIcon(italicIcon);
		actionListener.italicText(toggleButton);
		operationsToolbar.add(toggleButton);

//////// Undo Button /////////////
		JButton button = new JButton();
		button.setIcon(undoIcon);
		actionListener.undoButton(button);
		operationsToolbar.add(button);

//////// Re-do Button /////////////
		button = new JButton();
		button.setIcon(redoIcon);
		actionListener.redoButton(button);
		operationsToolbar.add(button);

		return operationsToolbar;
	}

	/**
	 * Handles the creation of the Toolbar panel where each the
	 * {@code OperationsToolbar} is situated. Adds an addition {@code JButton} used
	 * to display information about using the program when clicked.
	 * 
	 * 
	 * 
	 * @return {@code JPanel} populated tool bar
	 */
	private static JPanel createToolBar() {
		JPanel toolBar = new JPanel();

		// Styling the tool bar.
		toolBar.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 10));
		toolBar.setFocusable(true);
		toolBar.setLayout(new BorderLayout());
		// adding the OperationsToolbar to the panel.
		toolBar.add(createOperationsToolBar(), BorderLayout.WEST);

		// Help Button
		JButton helpButton = new JButton("Help");
		actionListener.helpButton(helpButton);
		toolBar.add(helpButton, BorderLayout.EAST);

		return toolBar;
	}

	/**
	 * Handles the creation of the text area the user types into. Adds an
	 * {@code UndoableEditListener} to the document of the text pane. Creates a
	 * scroll pane with the view of which set to the text area, allowing the ability
	 * to scroll when text goes of screen.
	 * 
	 * @return {@code JScrollPane} text pane within the scroll pane view.
	 */
	private static JScrollPane createTextArea() {
		textPane = new JTextPane();

		textPane.getDocument().addUndoableEditListener(manager);
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return scrollPane;
	}

	/**
	 * Handles the continuous looping of the {@code updateCharCount} method.
	 * 
	 * @see #updateCharCount
	 */
	@Override
	public void run() {

		while (true) {
			while (textPane.isFocusOwner()) {
				updateCharCount();
			}
			Thread.yield();

		}

	}

	/**
	 * This method handles the updating of the {@code charCountLBL} and
	 * {@code charCount} class variables.
	 * 
	 * This is achieved by getting the current text within the text pane and
	 * converting it to a character array. The length of the array is then assigned
	 * to the {@code charCount} class variable.
	 * 
	 * This array is then looped through and each characters that has the ordinal
	 * value of either a new line, end of line or carriage return is subtracted from
	 * the value in charCount. Once the array is exhausted, the {@code charCountLBL}
	 * is updated with the current value of {@code charCount}.
	 */
	private synchronized static void updateCharCount() {
		int newLineOrd = 10; // Ordinal value of a newline character
		int carriageReturnOrd = 13; // Ordinal value of a carriage return character
		int spaceOrd = 32; // Ordinal value of a space character
		char[] characters = textPane.getText().toCharArray();
		charCount = characters.length;

		for (char c : characters) {
			int charOrd = (int) c; // Ordinal value of the character
			if (charOrd == spaceOrd || charOrd == newLineOrd || charOrd == carriageReturnOrd) {
				charCount--;

			}
		}
		charCountLBL.setText("Characters: " + charCount);

	}

	/**
	 * Creates the Count pane shown at the bottom of the window frame whilst adding
	 * a label and styling the panel.
	 * 
	 * @return {@code JPanel} character counter panel
	 */
	private static JPanel createCountPanel() {
		JPanel countPanel = new JPanel();

		charCountLBL = new JLabel("Characters: " + charCount);
		charCountLBL.setHorizontalAlignment(SwingConstants.CENTER);

		countPanel.add(charCountLBL);

		return countPanel;
	}

	/**
	 * Handles adding each of the sections of the frame to the window at the points
	 * specified.
	 * 
	 * @param pane the content pane of the frame
	 */
	private static void addComponentsToPane(Container pane) {
		pane.add(createToolBar(), BorderLayout.NORTH);
		pane.add(createTextArea(), BorderLayout.CENTER);
		pane.add(createCountPanel(), BorderLayout.SOUTH);
	}

	/**
	 * Handles the closing operation of the window, allowing the user to save their
	 * document if it has not already been saved and they wish to close the program.
	 * 
	 * It first checks if the file is already saved or if there is content in the
	 * text area. If neither are satisfied, the user is asked if they would like to
	 * save. Yes results in the save dialog being shown, No closes the program
	 * without saving without saving, and clicking Cancel keeps the window alive.
	 */
	private static void setCloseOperation() {
		if (isSaved == true || textPane.getText().length() == 0) {
			System.exit(0);
		} else {
			int opt = JOptionPane.showConfirmDialog(null, "File has not been saved, would you like to save it?");
			switch (opt) {
			// Show save dialog
			case JOptionPane.YES_OPTION:
				actionListener.showSaveDialog();
				System.exit(0);
				break;
			// Close program without saving
			case JOptionPane.NO_OPTION:
				System.exit(0);
				break;
			// Keep window open
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		}
	}

	/**
	 * Handles the closing operation when the "X" window button is pressed. A
	 * {@code WindowListener} is added to the frame to recognise when the window "X"
	 * button has been clicked.
	 * 
	 * @param frame the {@code JFrame} of the GUI
	 */
	private static void handleClose(JFrame frame) {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				// Invoke the close operation method.
				setCloseOperation();
			}
		});
	}

	/**
	 * Attempts to set the look and feel of the program to the system default i.e
	 * Windows/Macintosh etc. If there is an issue setting the look and feel to the
	 * system, the default cross-platform look and feel is applied.
	 */
	private static void setLookAndFeel() {
		try {
			// Attempt to set the look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("Error setting look and feel.");
		}
	}

	/**
	 * Handles the creating and showing the user interface. Here the actions of the
	 * frame are applied, namely the setting of the close operation, adding the
	 * {@code JMenuBar}, setting the look and feel and applying a minimum size
	 * whilst setting the size of the components to there necessary size needed.
	 * 
	 */

	private static void createAndShowGUI() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		handleClose(frame);
		addComponentsToPane(frame.getContentPane());
		setLookAndFeel();
		frame.setTitle("Textual");
		frame.setJMenuBar(createMenuBar());
		frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		charCountThread = new Thread(new Textual());
	}

	/**
	 * Main point of the program. The createAndShowGUI method is run on the event
	 * dispatch thread for thread safety.
	 * 
	 * @param args unused {@code String} array of arguments.
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();

				charCountThread.start();
				charCountThread.setName("Char Count Thread");
			}
		});

	}

}
