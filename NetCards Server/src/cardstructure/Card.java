package cardstructure;


public class Card {
	private String text;
	private int blanks;

	public Card(String text, int blanks) {
		this.text   = text;
		this.blanks = blanks;
	}
	
	public Card(String text) {
		this(text, 1);
	}
	
	public Card() {
		this("");
	}
	
	public void compileBlanks() {
		boolean blankPrev = false;
		blanks = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '_' && !blankPrev) {
				blankPrev = true;
				blanks   += 1;
			}
			else if (c != '_')
				blankPrev = false;
		}
		if (blanks < 1)
			blanks = 1;
	}
	
	public int getBlanks() {
		return blanks;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return text;
	}
}
