package zelda.common.util;

public class InvalidInputException extends NumberFormatException {
	private static final long serialVersionUID = 1L;

	public String errorMessage;
	public String errorTitle;

	public InvalidInputException() {
		super();
		errorMessage = null;
		errorTitle = null;
	}

	public InvalidInputException(String errorMessage, String errorTitle) {
		super();
		this.errorMessage = errorMessage;
		this.errorTitle = errorTitle;
	}
}
