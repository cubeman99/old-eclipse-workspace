package cardgame;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import server.Message;
import server.MessageDecoder;
import server.Player;
import server.Server;
import server.ServerLog;
import server.Timer;
import cardstructure.*;


public class CardGame {
	public static final int MAX_JUDGE_WAIT_TIME = 3000;
	public static Random random = new Random();
	
	public static CardDeck whiteDeck;
	public static CardDeck blackDeck;
	public static CardDeck whiteCards;
	public static CardDeck blackCards;

	public static ArrayList<CardJudgeGroup> judgementCardGroups;
	public static Card categoryCard;
	public static int judgeIndex;
	public static String judgeName;
	public static boolean playing;
	public static boolean judgementMode;
	public static boolean waitingForNewJudge;
//	public static Timer judgeWaitTimer;
	
	
	
	
	public static void initializeConfig() {
		loadCardDecks();
		
	}
	
	public static void initializeNewGame() {
		judgementCardGroups = new ArrayList<CardJudgeGroup>();
		judgeIndex          = -1;
		judgeName           = "";
		playing             = false;
		judgementMode       = false;
		waitingForNewJudge  = false;
//		judgeWaitTimer      = new Timer();
		
		
		refillCardDecks();
	}
	
	public static void startGame() {
		playing = true;
		nextTurn();
	}
	
	public static void nextTurn() {
		judgementMode = false;
		judgementCardGroups.clear();
		categoryCard = drawBlackCard();
		
		for (Player p : Server.players) {
			p.ready = false;
			p.waitingToPlay = false;
		}
		
		nextJudge();
		broadcastNextTurn();
	}
	
	public static void addJudgeGroup(CardJudgeGroup group) {
		judgementCardGroups.add(group);
	}
	
	public static boolean allJudgeGroupsFlipped() {
		boolean allFlipped = false;
		for (CardJudgeGroup group : judgementCardGroups) {
			if (!group.getFlipped()) {
				allFlipped = false;
				break;
			}
		}
		return allFlipped;
	}
	
	public static void flipJudgeGroup(String owner) {
		CardJudgeGroup group = getJudgeGroup(owner);
		if (group != null)
			group.flip();
	}
	
	public static CardJudgeGroup getJudgeGroup(String owner) {
		for (CardJudgeGroup group : judgementCardGroups) {
			if (group.isOwnedBy(owner))
				return group;
		}
		return null;
	}
	
	public static boolean hasAllSubmissions() {
		return (judgementCardGroups.size() == Server.playerCount() - 1);
	}
	
	public static void broadcastNextTurn() {
		ServerLog.logInfo("Next Turn Starting...");
		MessageDecoder.broadcastMessage(getNextTurnMessage());
	}
	
	public static Message getNextTurnMessage() {
		return getNextTurnMessage(false);
	}
	
	public static Message getNextTurnMessage(boolean judgeMode) {
		String judgeName = getJudgeName();
		Message message  = new Message("nextturn", null);
		message.addStringArg(judgeName);
		message.addStringArg(categoryCard.getText());
		message.addIntArg(categoryCard.getBlanks());
		message.addBooleanArg(judgeMode);
		
		return message;
	}
	
	public static void handleJudgeLeaving() {
		if (judgementMode) {
			// Immediately Assign a new Judge:
			ServerLog.logInfo("Assigning a new Judge");
			nextJudge();
			MessageDecoder.broadcastMessage(new Message("setjudge", judgeName));
		}
		else {
			// Immediately Assign a new Judge:
			ServerLog.logInfo("Assigning a new Judge");
			nextJudge();
			MessageDecoder.broadcastMessage(new Message("setjudge", judgeName));
			// Wait a bit for a new player to join and become judge:
			/*ServerLog.logInfo("Assigning a new Judge");
			waitingForNewJudge = true;
			judgeWaitTimer.reset();
			judgeWaitTimer.start();*/
		}
	}
	
	public static String getJudgeName() {
		return judgeName;
	}
	
	public static void nextJudge() {
		judgeIndex += 1;
		if (judgeIndex >= Server.playerCount())
			judgeIndex = 0;
		judgeName = Server.getPlayerName(judgeIndex);
	}
	
	public static void shuffleJudgeGroups() {
		ArrayList<CardJudgeGroup> newList = new ArrayList<CardJudgeGroup>();
		
		while (judgementCardGroups.size() > 0) {
			int index = random.nextInt(judgementCardGroups.size());
			newList.add(judgementCardGroups.get(index));
			judgementCardGroups.remove(index);
		}
		
		judgementCardGroups = newList;
	}
	
	public static void broadcastJudgeGroups() {
		for (CardJudgeGroup group : judgementCardGroups) {
			Message message = new Message("addcardgroup", null);
			message.addStringArg(group.getOwner());
			message.addIntArg(group.size());
			for (int i = 0; i < group.size(); i++) {
				message.addStringArg(group.getCard(i).getText());
			}
			MessageDecoder.broadcastMessage(message);
		}
	}
	
	private static void refillCardDecks() {
		whiteCards = new CardDeck(whiteDeck);
		blackCards = new CardDeck(blackDeck);
		whiteCards.shuffle();
		blackCards.shuffle();
	}
	
	private static void loadCardDecks() {
		whiteDeck = new CardDeck();
		blackDeck = new CardDeck();
		
		File decksFolder = new File("decks");
		File[] deckFiles = decksFolder.listFiles();
		boolean noDecks  = false;
		
		if (deckFiles != null) {
			for (File file : deckFiles) {
    			CardDeckFile deck = new CardDeckFile("decks/" + file.getName());
    			deck.addCardsToDecks(whiteDeck, blackDeck);
    			ServerLog.logInfo("Loaded Card Deck \"" + file.getName() + "\"");
    		}
			if (deckFiles.length == 0)
				noDecks = true;
		}
		else
			noDecks = true;
		
		if (noDecks) {
    		ServerLog.logError("No Card Decks found!");
		}
		else {
    		ServerLog.logInfo("Total White Cards: " + whiteDeck.size());
    		ServerLog.logInfo("Total Black Cards: " + blackDeck.size());
		}
	}
	
	public static Card drawWhiteCard() {
		if (whiteCards.size() == 0) {
			whiteCards = new CardDeck(whiteDeck);
			whiteCards.shuffle();
		}
		return whiteCards.drawCard();
	}
	
	public static Card drawBlackCard() {
		if (blackCards.size() == 0) {
			blackCards = new CardDeck(blackDeck);
			blackCards.shuffle();
		}
		return blackCards.drawCard();
	}
}
