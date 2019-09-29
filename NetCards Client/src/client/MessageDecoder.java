package client;

import java.util.ArrayList;
import cardgame.CardGame;
import cardgame.GameRules;
import cardgame.gui.PlayerHolder;
import cardstructure.Card;
import cardstructure.CardJudgeGroup;
import client.main.Game;
import client.main.Main;


public class MessageDecoder {
	private static ArrayList<Message> unreadMessages = new ArrayList<Message>();

	
	public static void decodeMessage(String str) {
		decodeMessage(new Message(str));
	}
	
	/*
	 ######################################
	 #######  MAIN LOOP PSEUDOCODE  #######
	 ######################################
	 #                                    #
	 #  @while: true                      #
	 #   << setready                      # 
	 #  @when: all are ready:             #
	 #   >> retrievesubmission            #
	 #   :< sendsubmission                #
	 #  @when: all submissions obtained:  #
	 #   >> addcardgroup                  #
	 #   << flipcardgroup                 #
	 #   :> flipcardgroup                 #
	 #   << setwinner                     #
	 #   :> setwinner                     #
	 #   >> givepointcard                 #
	 #  @after: a short pause             #
	 #   >> nextturn                      #
	 #   :< requestcards                  #
	 #   :> givewhitecards                #
	 #  @end                              #
	 #                                    #
	 ######################################
	*/
	
	public static void decodeMessage(Message message) {
		// DEBUG: Print the message
		if (GameRules.CONSOLE_SHOW_MESSAGES)
			System.out.println(message.getText());
		
		if (message.isCommand(""))
			return;
		
		
		// setname <str newname>
		else if (message.isCommand("setname")) {
			String newName = message.argString(0);
			Game.name                   = newName;
			ClientConnection.clientName = newName;
			CardGame.myName             = newName;
		}
		
		// chatmessage <str sendername> <str message>
		else if (message.isCommand("chatmessage")) {
			Main.chatPanel.printChatMessage(message.argString(0) + ": " + message.argString(1));
		}
		
		// chatmessageraw <str text>
		else if (message.isCommand("chatmessageraw")) {
			Main.chatPanel.printChatMessage(message.argString(0));
		}
		
		// addplayerholders <int count> <str holder[0]> <str holder[1]> <str holder[2]>...
		else if (message.isCommand("addplayerholders")) {
			if (CardGame.isInitialized) {
    			int count = message.argInt(0);
    			for (int i = 1; i <= count; i++) {
    				CardGame.addNewPlayerHolder(message.argString(i));
    			}
			}
			else
				unreadMessages.add(message);
		}
		
		// setplayerholderready <str playername> <bool readystate>
		else if (message.isCommand("setplayerholderready")) {
			PlayerHolder ph = CardGame.getPlayerHolder(message.argString(0));
			ph.setReady(message.argBoolean(1));
		}

		// setplayerholderscore <str playername> <int score>
		else if (message.isCommand("setplayerholderscore")) {
			PlayerHolder ph = CardGame.getPlayerHolder(message.argString(0));
			ph.setScore(message.argInt(1));
		}
		
		// removeplayerholder <str playername>
		else if (message.isCommand("removeplayerholder")) {
			CardGame.removePlayerHolder(message.argString(0));
		}
		
		// setjudge <str playername>
		else if (message.isCommand("setjudge")) {
			CardGame.setJudge(message.argString(0), false);
		}
		
		// Recieve Server Messages:
		/////////////////////////////////
		//          MAIN LOOP          //
		/////////////////////////////////
		
		// retrievesubmission
		else if (message.isCommand("retrievesubmission")) {
			if (!CardGame.isJudge()) {
    			int amount = CardGame.mySubmission.size();
    			Message sendMessage = new Message("sendsubmission", null);
    			sendMessage.addIntArg(amount);
    			
    			for (int i = 0; i < amount; i++) {
    				sendMessage.addStringArg(CardGame.mySubmission.get(i).getText());
    			}
    			sendMessage(sendMessage);
			}
		}
		
		// addcardgroup <str owner> <int amount> <str cardtext[0]> <str cardtext[1]> <str cardtext[2]>
		else if (message.isCommand("addcardgroup")) {
			CardGame.ready         = false;
			CardGame.judgementMode = true;
			CardJudgeGroup group   = new CardJudgeGroup(message.argString(0));
			
			int amount = message.argInt(1);
			for (int i = 0; i < amount; i++) {
				String cardText = message.argString(i + 2);
				group.addCard(new Card(cardText));
			}
			for (PlayerHolder ph : CardGame.playerHolders) {
				ph.setReady(false);
			}
			
			CardGame.addJudgeGroup(group);
		}
		
		// flipcardgroup <str playerindex>
		else if (message.isCommand("flipcardgroup")) {
			CardGame.flipNextJudgeGroup();
		}
		
		// setwinner <str playerindex>
		else if (message.isCommand("setwinner")) {
			String winner = message.argString(0);
			CardGame.setWinner(winner);
			CardGame.getPlayerHolder(winner).givePoint();
		}
		
		// nextturn <str judgeplayerindex> <str categorycard_text> <int categorycard_blanks>
		else if (message.isCommand("nextturn")) {
			CardGame.playing = true;
			CardGame.setJudge(message.argString(0), true);
			CardGame.setCategory(new Card(message.argString(1), message.argInt(2)));
			
			CardGame.nextTurn();
			CardGame.judgementMode = message.argBoolean(3);
			if (CardGame.judgementMode) {
				CardGame.waitingForNextRound = true;
			}
		}
		
		// givewhitecards <int amount> <str card[0]> <str card[1]> <str card[2]> ...
		else if (message.isCommand("givewhitecards")) {
			int amount = message.argInt(0);
			for (int i = 0; i < amount; i++) {
				String cardText = message.argString(i + 1);
				CardGame.addHandCard(new Card(cardText));
			}
		}
		
		else {
			// Error reading message:
			if (GameRules.CONSOLE_SHOW_DEBUG)
				System.out.println("ERROR decoding message: " + message.getText());
		}
		
	}
	
	public static void decodeUnreadMessages() {
		for (Message m : unreadMessages)
			decodeMessage(m);
		unreadMessages.clear();
	}
	
	public static void sendMessage(Message message) {
		MessageHandler.sendMessage(message);
	}
}
