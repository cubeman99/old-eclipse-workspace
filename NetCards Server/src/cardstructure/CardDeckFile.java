package cardstructure;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import server.ServerLog;

public class CardDeckFile {
	public String title;
	public String author;
	public String description;
	public ArrayList<Card> whiteCards;
	public ArrayList<Card> blackCards;
	
	public CardDeckFile() {
		this.title       = "";
		this.author      = "";
		this.description = "";
		this.whiteCards  = new ArrayList<Card>();
		this.blackCards  = new ArrayList<Card>();
	}
	
	public CardDeckFile(String filename) {
		this();
		loadFromFile(filename);
	}
	
	public void addCardsToDecks(CardDeck whiteDeck, CardDeck blackDeck) {
		for (Card card : whiteCards)
			whiteDeck.addCard(card);
		for (Card card : blackCards)
			blackDeck.addCard(card);
	}
	
	private void loadFromFile(String filename) {
		try {
			FileReader reader    = new FileReader(filename);
			Scanner in           = new Scanner(reader);
			boolean commented    = false;
			boolean readingWhite = true;
			in.nextLine();
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				
				if (line.length() > 0) {
					if (line.charAt(0) == '@') {
    					if (line.equals("@eof"))
    						break;
    					else if (line.startsWith("@title"))
    						this.title = line.substring("@title".length() + 1);
    					else if (line.startsWith("@author"))
    						this.author = line.substring("@author".length() + 1);
    					else if (line.startsWith("@description"))
    						this.description = line.substring("@description".length() + 1);
    					else if (line.equals("@whitecards"))
    						readingWhite = true;
    					else if (line.equals("@blackcards"))
    						readingWhite = false;
    					else if (line.equals("@#-"))
    						commented = true;
    					else if (line.equals("@-#"))
    						commented = false;
					}
					else if (!line.startsWith("#") && !commented) {
						if (readingWhite) {
							whiteCards.add(new Card(line));
						}
						else {
	    					int blanks  = Integer.parseInt("" + line.charAt(0));
	    					String text = line.substring(2);
	    					blackCards.add(new Card(text, blanks));
						}
					}
				}
			}
		}
		catch (IOException e) {
			ServerLog.logError("File not found: \"" + filename + "\"");
		}
	}
}
