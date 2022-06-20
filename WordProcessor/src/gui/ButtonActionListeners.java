package gui;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import file.FileManipulation;
import file.PathValidation;
import file.PopulatedTemplates;
import file.Templates;


/**
 * This class is responsible for handling the {@code ActionListener} actions of
 * the buttons in the {@code WordProcessor} class.
 * 
 * 
 * 
 * @author Aiden Parker
 * @version 1
 * @see Textual
 */
public class ButtonActionListeners {
	/** Allows instances of the pre-defined templates to be acquired. */
	private PopulatedTemplates templates;
	/** Allows the getter and setter methods to be accessed in this class. */
	private Textual gui = new Textual();
	/** Handles disk operations i.e writing and getting byte value of document. */
	private FileManipulation fileManip = new FileManipulation();
	/** Handles the system dialog operation i.e showing the open/save system explorer dialog. */
	private SystemExplorer sysExplorer;
	/** Handles path validation operations and whether a path to a file exists. */
	private PathValidation path = new PathValidation();

	/**
	 * Used to handle the action of the newMenuItem button being triggered. When
	 * invoked, a new file is attempted to be created, first checking if it already
	 * exists and if the location specified is valid.
	 * 
	 * @param menuNew the {@code JMenuItem} button that triggers this method
	 */
	public void newFile(JMenuItem menuNew) {
		menuNew.addActionListener(e -> {
			sysExplorer = new SaveExplorer();
			sysExplorer.showDialog();
			String value = sysExplorer.getFileSelected();
			if (value != null) {
				// If a location has been specified by the user.
				if (path.isPathValid(value) && !(path.doesPathExist(path.getPathValue()))) {
					// If the path is valid and does not exist.
					fileManip.newFile(path.getPathValue());
					gui.setTextPane(null);
				}
			} else {
				JOptionPane.showMessageDialog(null, "A file already exists with that name.", "File Already Exists",
						JOptionPane.ERROR_MESSAGE);
			}

		});
	}

	/**
	 * Used to handle the action of the newMenuOpen button being triggered. When
	 * invoked, the file chosen is attempted to be opened, first checking if it
	 * already exists and if the location specified is valid. If the file can be
	 * opened, the byte value of the file is passed into the {@code setTextArea}
	 * method of the GUI class.
	 * 
	 * @param menuOpen the {@code JMenuItem} button that triggers this method
	 * @see Textual#setTextPane(byte[])
	 */
	public void openFile(JMenuItem menuOpen) {
		menuOpen.addActionListener(e -> {
			sysExplorer = new OpenExplorer();
			sysExplorer.showDialog();
			String value = sysExplorer.getFileSelected();
			if (path.isPathValid(value)) {
				// If the path is valid
				byte[] bytes = fileManip.getFileContents(path.getPathValue());
				gui.setTextPane(bytes);
			}
		});
	}

	/**
	 * Handles the action event being triggered for the saveMenuItem button. When
	 * invoked, the {@code showSaveDialog} method in called to handles the save
	 * operation.
	 * 
	 * @param menuSave the {@code JMenuItem} button that triggers this method
	 */

	public void saveFile(JMenuItem menuSave) {
		menuSave.addActionListener(e -> {
			showSaveDialog();
		});
	}

	/**
	 * Handles the saving of a file when triggered. First Checks if the path
	 * provided is valid and that there is text in the text area of the GUI, if
	 * satisfied checks if the file already exists. If file exists, the user is
	 * prompted on whether they wish to overwrite the current file, if Yes then the
	 * file is overwrite.
	 * 
	 * 
	 */
	public void showSaveDialog() {
		sysExplorer = new SaveExplorer();
		sysExplorer.showDialog();
		String value = sysExplorer.getFileSelected();

		if (path.isPathValid(value) && gui.getTextAreaBytes() != null) {
			if (path.doesPathExist(path.getPathValue())) {
				System.out.println("outside of opt if = " + path.getPathValue());
				int opt = JOptionPane.showConfirmDialog(null, "This File already exists\nDo you want to overwrite it?");
				if (opt == 0) {
					fileManip.saveFile(path.getPathValue(), gui.getTextAreaBytes());
					gui.setIsSaved(true);
					JOptionPane.showMessageDialog(null, "File Saved");
				}
			} else {
				fileManip.saveFile(path.getPathValue(), gui.getTextAreaBytes());
				gui.setIsSaved(true);
			}

		}

	}

	/**
	 * Handles the operation that shows a template the user chooses. This is
	 * achieved by getting the action command of the button triggering the event and
	 * handling the template that is requested accordingly. The templates Enum value
	 * is passed into the instantiation process of the {@code PopulatedTemplates}
	 * object, this then allows the byte values of the template to be fetched and
	 * passed into the GUI {@code setTextArea} method.
	 * 
	 * @param menuItem the {@code JMenuItem} button that triggers this method
	 * @see PopulatedTemplates
	 * @see Textual#setTextPane(byte[])
	 */
	public void showTemplate(JMenuItem menuItem) {
		menuItem.addActionListener(e -> {
			String action = e.getActionCommand();
			byte[] bytes;

			switch (action) {
			case "dear":
				// Dear Sir/Madam template
				templates = new PopulatedTemplates(Templates.DEAR);
				bytes = templates.createTemplate();
				gui.setTextPane(bytes);
				break;
			case "regards":
				// Kind regards template
				templates = new PopulatedTemplates(Templates.KIND_REGARDS);
				bytes = templates.createTemplate();
				gui.setTextPane(bytes);
				break;

			case "thanks":
				// Many Thanks template
				templates = new PopulatedTemplates(Templates.MANY_THANKS);
				bytes = templates.createTemplate();
				gui.setTextPane(bytes);
				break;
				
			}
		});

	}

	/**
	 * Handles setting text bold on a button click. this is done by creating a new
	 * instance of the {@code StyledEditorKit} class and accessing the
	 * {@code BoldAction} method.
	 * 
	 * @param fontBold the {@code JToggleButton} that triggers this method
	 * @see StyledEditorKit
	 */
	public void boldText(JToggleButton fontBold) {
		fontBold.addActionListener(new StyledEditorKit.BoldAction());
	}

	/**
	 * Handles setting text underline on a button click. this is done by creating a
	 * new instance of the {@code StyledEditorKit} class and accessing the
	 * {@code UnderlineAction} method.
	 * 
	 * @param fontUnderline the {@code JToggleButton} that triggers this method
	 * @see StyledEditorKit
	 */
	public void underlineText(JToggleButton fontUnderline) {
		fontUnderline.addActionListener(new StyledEditorKit.UnderlineAction());
	}

	/**
	 * Handles setting text italic on a button click. this is done by creating a new
	 * instance of the {@code StyledEditorKit} class and accessing the
	 * {@code ItalicAction} method.
	 * 
	 * @param fontItalic the {@code JToggleButton} that triggers this method
	 * @see StyledEditorKit
	 */
	public void italicText(JToggleButton fontItalic) {
		fontItalic.addActionListener(new StyledEditorKit.ItalicAction());
	}

	/**
	 * Handles the undo button action event. When triggered, a call to the GUI
	 * UndoManager variable is called to check if the text pane has an undo flagged
	 * available. If satisfied, the undoAction method is called.
	 * 
	 * @param button the {@code JButton} that triggers this method
	 */
	public void undoButton(JButton button) {
		button.addActionListener(e -> {
			if (gui.getUndoRedoManager().canUndo())
				undoAction();
		});
	}

	/**
	 * Handles the re-do button action event. When triggered, a call to the GUI
	 * UndoManager variable is called to check if the text pane has an re-do flagged
	 * available. If satisfied, the redoAction method is called.
	 * 
	 * @param button the {@code JButton} that triggers this method
	 */
	public void redoButton(JButton button) {
		button.addActionListener(e -> {
			if (gui.getUndoRedoManager().canRedo())
				redoAction();
		});
	}

	/**
	 * Handles the undo action. Attempts to undo an action of the text pane of the
	 * GUI class by calling the {@code getUndoRedoManager} in the GUI class and
	 * invoking the undo method of the manager
	 */
	protected void undoAction() {
		try {
			gui.getUndoRedoManager().undo();
		} catch (CannotUndoException ex) {
			System.out.println("failed to undo");
		}
	}

	/**
	 * Handles the re-do action. Attempts to re-do an action of the text pane of the
	 * GUI class by calling the {@code getUndoRedoManager} in the GUI class and
	 * invoking the re-do method of the manager
	 */
	protected void redoAction() {
		try {
			gui.getUndoRedoManager().redo();
		} catch (CannotRedoException ex) {
			System.out.println("failed to redo");
		}
	}

	/**
	 * Shows a dialog box of helpful information to the user when clicked. Informs
	 * the user about what actions can be undertook and how to achieve them.
	 * 
	 * @param button the {@code JButton} that triggers this method
	 */
	public void helpButton(JButton button) {
		button.addActionListener(e -> {
			JOptionPane.showMessageDialog(null,
					"A simple word processing program\n Type into the text area in the centre to start creating a document.\n"
							+ "Creating, Opening or Saving the file can be done via the \"File\" menu item in the top left.\n"
							+ "To Cut, Copy or Paste simply use the \"Edit\" menu tab or use shortcut keys to perform the same action.\n"
							+ "Templates are provided via the \"Pre-defined Templates\" menu item with the optio to import your own or "
							+ "save one that has been created. \nThe ability to undo and redo is provided via the arrows on the tool bar.\n"
							+ "A character counter can be seen on the bottom of the window to show how many characters have been entered (not including spaces).");
		});

	}

}
