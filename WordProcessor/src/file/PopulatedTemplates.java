package file;

/**
 * This class handles the creating of a template specified. Instantiating this
 * class requires a {@code Temaplate} value to be passed into the constructor to
 * determine which template should be created.
 * 
 * A switch statement determines what template should be created by switching on
 * the value passed into the constructor. With the template chosen, the string
 * value of the template is converted to a {@code byte[]} and returned to the
 * invoking method call.
 * 
 * 
 * 
 * @author Aiden Parker
 *
 */
public class PopulatedTemplates {
	/** Holds value of the template injected via the constructor. */
	private Templates template;

	/**
	 * Class constructor that accepts a {@code Templates} data type as an argument.
	 * 
	 * @param temp the template to be created on invocation
	 */
	public PopulatedTemplates(Templates temp) {
		this.template = temp;
	}

	/**
	 * Handles the creating of the template chosen and converts it to a byte array
	 * which is return to the object calling the method.
	 * 
	 * @return {@code byte[]} data of the template chosen
	 */
	public byte[] createTemplate() {
		switch (template) {
		case DEAR:
			String dearTemplate = "Dear Sir/Madam\n\n";
			return dearTemplate.getBytes();
		case KIND_REGARDS:
			String regardsTemplate = "\n\nKind Regards,\n";
			return regardsTemplate.getBytes();
		case MANY_THANKS:
			String thanksTemplate = "\n\nMany thanks,\n";
			return thanksTemplate.getBytes();
		}
		return null;
	}
}
