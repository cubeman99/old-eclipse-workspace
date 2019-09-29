package zelda.game.control.text;

import java.awt.Color;


public class MessageScanner {
	private String text;
	private int cursorPosition;
	private Color color;



	// ================== CONSTRUCTORS ================== //

	public MessageScanner(String text) {
		this.text = text;
		cursorPosition = 0;
		color = null;
	}

	public MessageScanner(Message msg) {
		this(msg.getText());
	}



	// =================== ACCESSORS =================== //

	public boolean hasNextWord() {
		return hasNextLetter();
	}

	public boolean hasNextLetter() {
		return (cursorPosition < text.length());
	}



	// ==================== MUTATORS ==================== //

	public LetterString nextWord() {
		LetterString str = new LetterString();
		boolean leadingZeros = true;

		while (hasNextLetter()) {
			Letter l = nextLetter();

			if (l.equals("<red>")) {
				color = (color == null ? Letter.COLOR_RED : (color
						.equals(Letter.COLOR_RED) ? null : color));
				continue;
			}
			else if (l.equals("<blue>")) {
				color = (color == null ? Letter.COLOR_BLUE : (color
						.equals(Letter.COLOR_BLUE) ? null : color));
				continue;
			}

			l.setColor(color);

			if (l.equals(" ")) {
				if (leadingZeros)
					str.addLetter(l);
				else
					break;
			}
			else if (l.equals("<n>")) {
				if (str.length() == 0)
					str.addLetter(l);
				else
					cursorPosition -= 3;
				break;
			}
			else {
				leadingZeros = false;
				str.addLetter(l);
			}
		}

		return str;
	}

	public Letter nextLetter() {
		String str = nextChar();

		if (str.equals("<")) {
			String s;
			do {
				s = nextChar();
				str += s;
			}
			while (hasNextLetter() && !s.equals(">"));
		}

		return new Letter(str);
	}

	private String nextChar() {
		if (!hasNextLetter())
			return "";
		cursorPosition++;
		return text.substring(cursorPosition - 1, cursorPosition);
	}
}
