import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class FrequencyList {
	public ArrayList<Letter> letters;
	
	public FrequencyList() {
		letters = new ArrayList<Letter>();
	}
	
	public FrequencyList(String filename) {
		this();
		loadFromFile(filename);
	}
	
	private Letter addLetter(char letter, double frequency) {
		Letter l = new Letter(letter, frequency);
		letters.add(l);
		return l;
	}
	
	public Letter getLetter(char letter) {
		for (Letter l : letters) {
			if (l.symbol == letter)
				return l;
		}
		return addLetter(letter, 0);
	}
	
	public void alphabetize() {
		ArrayList<Letter> newList = new ArrayList<Letter>();
		
		while (letters.size() > 0) {
			char least = 0;
			int index = -1;
			
			for (int i = 0; i < letters.size(); i++) {
				Letter l = letters.get(i);
				if (index < 0 || l.symbol < least) {
					least = l.symbol;
					index = i;
				}
			}
			
			newList.add(letters.get(index));
			letters.remove(index);
		}
		
		letters = newList;
	}
	
	private void loadFromFile(String filename) {
		letters.clear();
		
		try {
			FileReader reader = new FileReader(filename);
			Scanner in        = new Scanner(reader);
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				
				char letter = line.charAt(0);
				line = line.substring(2);
				double frequency = Double.parseDouble(line);
				
				addLetter(letter, frequency);
			}
		}
		catch (IOException e) {
			System.out.println("File not found: \"" + filename + "\"");
		}
		
		alphabetize();
	}
	
	public void print() {
		for (int i = 0; i < letters.size(); i++) {
			Letter l = letters.get(i);
			System.out.println(l.symbol + " = " + l.frequency + "%");
		}
	}
}
