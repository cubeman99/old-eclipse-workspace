package server;

import cardgame.CardGame;
import cardgame.GameRules;
import cardstructure.Card;
import cardstructure.CardJudgeGroup;


public class MessageDecoder {
	

	public static void decodeMessage(Player sender, String str) {
		decodeMessage(sender, new Message(str));
	}
	
	public static synchronized void decodeMessage(Player sender, Message message) {
		// DEBUG: Log the message
		if (GameRules.CONSOLE_SHOW_MESSAGES)
			ServerLog.logMessageIn(sender.name + ": " + message.getText());
		
		
		// playerjoin <str playername>>
		if (message.isCommand("playerjoin")) {
			String rawName = message.argString(0);
			String name   = rawName;
			
			int i = 0;
			while (!Server.checkNameIsFree(name)) {
				name = rawName + i;
				i++;
			}
			
			sender.setName(name);
			ServerLog.logInfo(sender.name + " logged in [" + sender.clientThread.socket.getRemoteSocketAddress() + "]");
			
			// add client thread to the thread list:
			Server.clientThreads.add(sender.clientThread);
			Server.players.add(sender);
			
			// setname <str newname>>
			sendMessage(sender, new Message("setname", name));
			
			// addplayerholders <int count> <str holder[0]> <str holder[1]> <str holder[2]>...
			broadcastMessage(new Message("addplayerholders", 1, sender.name));
			
			// addplayerholders <int count> <str holder[0]> <str holder[1]> <str holder[2]>...
			for (Player p : Server.players) {
				if (!p.getName().equals(sender.getName())) {
					sendMessage(sender, new Message("addplayerholders", 1, p.getName()));
				}
			}
			
			if (CardGame.judgementMode)
				sender.waitingToPlay = true;
			
			if (CardGame.playing) {
				sendMessage(sender, CardGame.getNextTurnMessage(CardGame.judgementMode));
				if (!CardGame.judgementMode) {
					for (Player p : Server.players) {
						if (p.isReady()) {
							// setplayerholderready <str playername> <bool readystate>
							sendMessage(sender, new Message("setplayerholderready ", p.getName(), true));
						}
						if (p.getScore() > 0) {
							// setplayerholderscore <str playername> <int score>
							sendMessage(sender, new Message("setplayerholderscore ", p.getName(), p.getScore()));
						}
					}
				}
			}
		}
		
		// chatmessage <str message>
		else if (message.isCommand("chatmessage")) {
			String text = message.argString(0);
			ServerLog.logChat(sender.name + ": " + text);
			broadcastMessage(new Message("chatmessage", sender.name, text));
			
			if (text.equals("startgame")) {
				ServerLog.logInfo("Starting a new game...");
				CardGame.startGame();
			}
		}
		
		// startgame
		else if (message.isCommand("startgame")) {
			CardGame.startGame();
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

		// Recieve Cleint Messages:
		/////////////////////////////////
		//          MAIN LOOP          //
		/////////////////////////////////

		// setready <bool isready>
		else if (message.isCommand("setready")) {
			sender.setReadyState(message.argBoolean(0));
			
			// setplayerholderready <str playername> <bool readystate>
			broadcastMessage(new Message("setplayerholderready", sender.name, message.argBoolean(0)));
			
			// Check if all players are ready:
			boolean allReady = true;
			for (Player p : Server.players) {
				if (!p.isReady() && !Server.getPlayerName(CardGame.judgeIndex).equals(p.getName())) {
					allReady = false;
					break;
				}
			}
			
			if (allReady) {
				// Gather up each player's choice
				ServerLog.logInfo("All players are ready.");
				CardGame.judgementMode = true;
				broadcastMessage(new Message("retrievesubmission"));
			}
		}

		// sendsubmission <int amount> <str cardtext[0]> <str cardtext[1]> <str cardtext[2]>...
		else if (message.isCommand("sendsubmission")) {
			// Add the submission to the list
			CardJudgeGroup group = new CardJudgeGroup(sender.getName());

			int amount = message.argInt(0);
			for (int i = 0; i < amount; i++) {
				String cardText = message.argString(i + 1);
				group.addCard(new Card(cardText));
			}
			CardGame.addJudgeGroup(group);
			
			// If all submissions obtained, start the judgement
			if (CardGame.hasAllSubmissions()) {
    			CardGame.shuffleJudgeGroups();
				CardGame.broadcastJudgeGroups();
			}
		}

		// flipcardgroup <str playerindex>
		else if (message.isCommand("flipcardgroup")) {
			broadcastMessage(new Message("flipcardgroup"));
			/*
			CardGame.flipJudgeGroup(message.argString(0));
			broadcastMessage(new Message("flipcardgroup", message.argString(0)));
			
			// Check if all groups are flipped:
			if (CardGame.allJudgeGroupsFlipped()) {
				// Wait for the winner to be chosen...
			}*/
		}

		// setwinner <str playerindex>
		else if (message.isCommand("setwinner")) {
			String winnerName = message.argString(0);
			Player winner     = Server.getPlayer(winnerName);
			
			// Check if winner disconnected
			if (winner == null) {
				
			}
			else {
				//broadcastMessage(new Message("chatmessageraw", winnerName + " won that round!"));
				broadcastMessage(new Message("setwinner", winnerName));
			}
			CardGame.nextTurn();
		}
		
		// requestcards <int amount>
		else if (message.isCommand("requestcards")) {
			int amount = message.argInt(0);
			Message sendMessage = new Message("givewhitecards", null);
			sendMessage.addIntArg(amount);
			
			for (int i = 0; i < amount; i++) {
				Card card = CardGame.drawWhiteCard();
				if (card == null)
					break;
				else {
					sendMessage.addStringArg(card.getText());
				}
			}
			sendMessage(sender, sendMessage);
		}
		
		
	}
	
	public static void broadcastMessage(Message message) {
		if (GameRules.CONSOLE_SHOW_MESSAGES)
			ServerLog.logMessageOut("BROADCAST: " + message.getText());
		for (Player p : Server.players) {
			p.sendMessage(message);
		}
	}
	
	public static void sendMessage(Player recipient, Message message) {
		if (GameRules.CONSOLE_SHOW_MESSAGES)
			ServerLog.logMessageOut(recipient.getName() + ": " + message.getText());
		recipient.sendMessage(message);
	}
}
