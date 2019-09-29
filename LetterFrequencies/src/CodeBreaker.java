import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CodeBreaker {
	
	public static FrequencyList getFileFrequencies(String filename) {
		FrequencyList list = new FrequencyList();
		int totalLetters = 0;
		
		try {
			FileReader reader = new FileReader(filename);
			Scanner in        = new Scanner(reader);
			
			String fileText = "";
			
			while (in.hasNextLine()) {
				fileText += in.nextLine();
			}
			
			for (int i = 0; i < fileText.length(); i++) {
				char c =  fileText.charAt(i);
				
				if (c < 97)
					c += 32;
				
				if (c >= 97 && c <= 122) {
    				list.getLetter(c).frequency += 1;
    				totalLetters += 1;
				}
			}
		}
		catch (IOException e) {
			System.out.println("File not found: \"" + filename + "\"");
		}
		
		for (Letter l : list.letters) {
			l.frequency /= (double) totalLetters;
			l.frequency *= 100.0d;
		}
		
		list.alphabetize();
		
		return list;
	}
}
