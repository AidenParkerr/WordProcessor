package gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This abstract class is responsible for providing a blueprint for the
 * {@code JFileChooser} dialogue box that child class can implement to tailor
 * the dialogue box relative the prompt being shown.
 * 
 * @author Aiden Parker
 * @version 1
 *
 */

public abstract class SystemExplorer {
	/** the {@code Integer} value of the {@code JFileChooser} dialog chosen. */
	private int dialogType;
	/**
	 * The {@code String} value of the absolute path chosen via the system explorer
	 */
	private String fileSelected;
	/** Instantiates the {@code JFileChooser} class */
	JFileChooser fc = new JFileChooser();

	/**
	 * Class constructor that configures the system explorers base settings. 
	 * These settings:
	 * 	Sets any file filter being used to false
	 * 	Sets the file filter to one created that shows only ".txt" files
	 * 	sets the current directory to null (This points to the users default directory)
	 * 	Sets the content selectable in the system explorer to only files.  
	 * 
	 * 
	 */
	SystemExplorer() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(null);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	/**
	 * An abstract method that is instantiated and given a body in the sub classes
	 * {@code SaveExplorer} and {@code OpenExplorer}. This method is used to show
	 * the system explorer needed.
	 * 
	 * @see SaveExplorer
	 * @see OpenExplorer
	 */
	public abstract void showDialog();

	/**
	 * Returns either null or the absolute path of the file selected using the
	 * system explorer.
	 * 
	 * @return absolute path of the folder/file selected.
	 */
	public String getFileSelected() {
		return this.fileSelected;
	}

	/**
	 * Sets the class variable fileSelected to the to argument passed in.
	 * 
	 * @param file the string version of the absolute path of the file selected.
	 */
	public void setFileSelected(String file) {
		this.fileSelected = file;
	}

	/**
	 * Assigns the class variable dialogType to the value passed in as an argument.
	 * This in turn display the dialog box whilst providing the response the user
	 * enters, i.e yes or no.
	 * 
	 * @param value the {@code JFileChooser} integer that represents the open or
	 *              save dialog
	 */
	public void setDialogType(int value) {
		this.dialogType = value;
	}

	/**
	 * Returns the value stored in the class variable dialogType.
	 * 
	 * @return integer the integer value stored in dialogType
	 */
	public int getDialogType() {
		return this.dialogType;
	}
}

/**
 * This class is responsible for setting the {@code JFileChooser} configuration
 * settings to the Open dialog option. This class also implements an abstract
 * method from the super class that enables the dialogue box to be shown when
 * the method {@code showDialog} is called.
 * 
 * @author Aiden Parker
 * @version 1
 * @see SaveExplorer
 */
class OpenExplorer extends SystemExplorer {

	@Override
	public void showDialog() {
		super.setDialogType(fc.showOpenDialog(null));

		// If the "yes" button is selected.
		if (super.getDialogType() == JFileChooser.APPROVE_OPTION) {
			super.setFileSelected(fc.getSelectedFile().toString());
		}
	}

}

/**
 * Similar to the {@code OpenExplorer} class, this class is responsible for
 * setting the {@code JFileChooser} configuration settings to the Save dialog
 * option. This class also implements an abstract method from the super class
 * that enables the dialogue box to be shown when the method {@code showDialog}
 * is called.
 * 
 * @author Aiden Parker
 * @version 1
 * @see OpenExplorer
 */
class SaveExplorer extends SystemExplorer {

	@Override
	public void showDialog() {
		super.setDialogType(fc.showSaveDialog(null));

		// If the "yes" button is selected.
		if (super.getDialogType() == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().toString();

			if (file.substring(file.lastIndexOf('.') + 1).equalsIgnoreCase("txt")) {
				// If the file contains "txt" at the last index of the character '.'
				super.setFileSelected(file);
			} else {
				// Concatenate ".txt" to the end of the path provided, converting the file into
				// a .txt file.
				super.setFileSelected(file.concat(".txt"));
			}

		}
	}

}
