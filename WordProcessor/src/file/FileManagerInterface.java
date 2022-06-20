package file;

import java.nio.file.Path;

/**
 * An interface that determines the operating a file manager class should be
 * able to achieve, related to a word processing application. 
 * 
 * @author Aiden Parker
 * @version 1
 */

public interface FileManagerInterface {

	/**
	 * Abstract method that is to be implemented via its child classes. Purpose of
	 * 
	 * This method is to create a new file on the storage medium of the device, this
	 * should be done through the use of the {@code java.nio.Files} module.
	 * 
	 * @param path the path of the location to create a file.
	 * @return true if the file was created successfully
	 */
	public boolean newFile(Path path);

	/**
	 * Abstract method that is to be implemented via its child classes.
	 * 
	 * This method, like the {@code newFile} method, this handles the creating of a
	 * creating of a file if not already exist using the {@code java.nio.Files}
	 * module. The difference here is an array of bytes is accepted as an argument,
	 * enabling the contents of the file to be wrote to.
	 * 
	 * @param path  the path of the location to create a file.
	 * @param bytes a byte array that is to be wrote to the file being created.
	 * @return true if the file was created and wrote to successfully.
	 */
	public boolean saveFile(Path path, byte[] bytes);

	/**
	 * Abstract method that is to be implemented via its child classes.
	 * 
	 * This is a declaration of a method that is used to acquire the contents of a
	 * file parsed to a byte array. This implementation should utilise the
	 * {@code java.nio.Files} module to achieve this.
	 * 
	 * @param path the path of the location to create a file.
	 * @return byte[] an array of bytes the file is composed off.
	 */
	public byte[] getFileContents(Path path);

}
