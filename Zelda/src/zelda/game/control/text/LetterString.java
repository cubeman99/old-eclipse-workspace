package zelda.game.control.text;

import java.util.ArrayList;


/**
 * LetterString represents a string/sequence of letters.
 * 
 * @author David Jordan
 */
public class LetterString {
	private ArrayList<Letter> letters;



	// ================== CONSTRUCTORS ================== //

	public LetterString() {
		letters = new ArrayList<Letter>();
	}

	public LetterString(String text) {
		letters = new ArrayList<Letter>();
		MessageScanner scanner = new MessageScanner(text);
		while (scanner.hasNextLetter())
			letters.add(scanner.nextLetter());
	}

	public LetterString(Letter... ls) {
		letters = new ArrayList<Letter>();
		for (int i = 0; i < ls.length; i++)
			letters.add(ls[i]);
	}

	public LetterString(LetterString copy) {
		letters = new ArrayList<Letter>();
		for (int i = 0; i < copy.length(); i++) {
			letters.add(copy.getLetter(i));
		}
	}



	// =================== ACCESSORS =================== //

	public int length() {
		return letters.size();
	}

	public Letter getLetter(int index) {
		return letters.get(index);
	}

	public boolean isLineBreak() {
		return (letters.size() > 0 && letters.get(0).equals("<n>"));
	}



	// ==================== MUTATORS ==================== //

	public void addLetter(Letter l) {
		letters.add(l);
	}

	public void addLetter(String letterCode) {
		letters.add(new Letter(letterCode));
	}

	public void addLetter(int index, Letter l) {
		letters.add(index, l);
	}

	public void addLetter(int index, String letterCode) {
		letters.add(index, new Letter(letterCode));
	}

	public void removeLetter(int index) {
		letters.remove(index);
	}

	public void addLetters(LetterString str) {
		for (int i = 0; i < str.length(); i++)
			addLetter(str.getLetter(i));
	}

	public void addLetters(String str) {
		addLetters(new LetterString(str));
	}

	public LetterString substring(int beginIndex) {
		return substring(beginIndex, letters.size());
	}

	public LetterString substring(int beginIndex, int endIndex) {
		LetterString str = new LetterString();
		for (int i = beginIndex; i < endIndex && i < length(); i++)
			str.addLetter(letters.get(i));
		return str;
	}

	public void clear() {
		letters.clear();
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < letters.size(); i++)
			str += letters.get(i);
		return str;
	}
}
