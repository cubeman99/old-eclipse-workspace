package zelda.game.control.text;


/**
 * A message that is read on the screen and can have a question with multiple
 * options. <br>
 * <br>
 * 
 * This class is made to have in-line implementations in order to customize what
 * happens as an outcome of selecting different options.
 * 
 * @author David Jordan
 */
public class Message {
	private String text;

	public Message(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	/**
	 * Implement this to return a list of option names. Having more than one
	 * option makes this message into a question.
	 */
	public String[] getOptions() {
		return new String[] {};
	}

	// These are called when an option is chosen and are
	// made to be implemented.
	public void option1() {
	}

	public void option2() {
	}

	public void option3() {
	}

	public void option4() {
	}
}
