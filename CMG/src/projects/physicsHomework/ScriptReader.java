package projects.physicsHomework;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScriptReader {
	private boolean exit;
	
	protected void beginReading() {}
	
	protected void endReading() {}
	
	protected void readCommand(String command, ArrayList<String> args) {
		if (command.equals("eof"))
			exit = true;
	}
	
	protected void exit() {
		exit = true;
	}
	
	public final void readScript(String filename) {
		try {
			FileReader reader = new FileReader(filename);
			Scanner file = new Scanner(reader);
			exit = false;
			
			beginReading();
			
			// File read loop.
			while (file.hasNextLine()) {
				readLine(file.nextLine());
				if (exit)
					break;
			}
			
			endReading();
			file.close();
		}
		catch (IOException e) {
			System.err.println("File not found: \"" + filename + "\"");
		}
	}
	
	private String completeWord(String word, ArrayList<String> words) {
		if (word.length() > 0)
			words.add(word);
		return "";
	}
	
	protected void readLine(String line) {
		if (line.startsWith("#"))
			return;
		
		ArrayList<String> words = new ArrayList<String>();
		boolean quotes = false;
		String word = "";
		
		// Parse line character by character.
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			
			if (quotes) {
				if (c == '\"') {
					quotes = false;
					words.add(word);
					word = "";
				}
				else
					word += c;
			}
			else if (c == ' ' || c == '\t')
				word = completeWord(word, words);
			else if (c == '#') {
				word = completeWord(word, words);
				break;
			}
			else if (c == '\"')
				quotes = true;
			else
				word += c;
		}

		word = completeWord(word, words);
		
		// Perform the command.
		if (words.size() > 0 && words.get(0).startsWith("@")) {
			String command = words.get(0).substring(1, words.get(0).length());
			words.remove(0);
			readCommand(command, words);
		}
	}
}
