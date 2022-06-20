package file;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Determines whether the String value of a disk location can be parsed as a
 * {@code Path} object using the new java.nio API as well as checking if a path
 * exists on the local disk.
 * 
 * @author Aiden Parker
 *
 */

public class PathValidation {
	/** Holds the absolute path provided via the method {@code getPathValue} method. */
	private Path pathValue = null;

	/**
	 * Returns the {@code Path} value of value stored in the class variable
	 * {@code pathValue}.
	 * 
	 * @return pathValue the folder provided converted to a {@code Path} object
	 */

	public Path getPathValue() {
		return pathValue;
	}

	/**
	 * Determines whether the path provided is a valid system path. This utilities
	 * the new {@code java.nio.Files} class to attempt to parse the {@code String}
	 * value of the path into its {@code Path} value.
	 * 
	 * @param path the {@code String} value of the path to be validated
	 * @return true if the path is valid
	 */
	public boolean isPathValid(String path) {
		try {
			pathValue = Paths.get(path);
			return true;
		} catch (InvalidPathException | NullPointerException e) {
			return false;
		}
	}

	/**
	 * Utilises the {@code Files} class in the new java.nio API to determine whether
	 * the path provided exists on the disk.
	 * 
	 * @param path the {@code Path} value to be checked for its existence
	 * @return true if the path exists on disk
	 */
	public boolean doesPathExist(Path path) {
		if (Files.exists(path))
			return true;
		else {
			return false;
		}
	}

}
