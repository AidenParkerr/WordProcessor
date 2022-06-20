package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles the operations that enable a user to create a new file on disk, open
 * an existing text file to get the contents as a byte array and save a file by
 * writing the bytes to a file specified by the user. This is achieved using the
 * java.nio API
 * 
 * @author Aiden Parker
 */

public class FileManipulation implements FileManagerInterface {

	/**
	 * Handles the operation that enables the creation of a file at the specified
	 * path using the {@code Files} class and invoking the {@code createFile}
	 * method.
	 * 
	 * @param path the {@code Path} value of the location the file should be created
	 * @return true if the file was created
	 */
	public boolean newFile(Path path) {
		try {
			Files.createFile(path);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Handles the operation that enables the saving of a file at the specified path
	 * using the {@code Files} class and invoking the {@code write} method.
	 * 
	 * @param path the {@code Path} value of the location the file should be wrote
	 *             too
	 * @return true if the file was created
	 */
	public boolean saveFile(Path path, byte[] bytes) {
		try {
			Files.write(path, bytes);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Handles the operation that gets the byte value of the file selected. Should
	 * mainly be used on text files as large files can cause the program to hang
	 * whilst the contents of the file are read and stored into a byte array.
	 * 
	 * Will throw a {@code OutOfMemoryError} if the JVM has used 2GB on the object
	 * and no more can be assigned by the GarabageCollector, this can be due to the
	 * file being allocated an array is larger than 2GB.
	 * 
	 * @param path the {@code Path} value of the location the file should be created
	 * @return byte[] data of the file chosen converted to a byte array.
	 */
	public byte[] getFileContents(Path path) {
		try {
			byte[] bytes = Files.readAllBytes(path);
			return bytes;
		} catch (IOException | OutOfMemoryError e) {
			return null;
		}
	}
}
