package projects.slimeVolleyBall.client;

import java.util.ArrayList;
import projects.slimeVolleyBall.Slime;
import projects.slimeVolleyBall.common.Message;
import projects.slimeVolleyBall.server.Server;



public class MessageDecoderClient {
	private static ArrayList<Message> unreadMessages = new ArrayList<Message>();

	
	public static void decodeMessage(String str) {
		decodeMessage(new Message(str));
	}
	
	public static void decodeMessage(Message message) {
		// DEBUG: Print the message
		if (!message.isCommand("btick") && !message.isCommand("ptick"))
			System.out.println(message.getText());
		
		if (message.isCommand(""))
			return;


		// newplayer playerName playerTeamIndex colorIndex
		if (message.isCommand("newplayer")) {// && ClientConnection.control != null) {
			if (ClientConnection.control != null)
				ClientConnection.control.addPlayer(message.argString(0), message.argInt(1), message.argInt(2));
			else
				unreadMessages.add(message);
		}
		
		// btick ballX ballY
		if (message.isCommand("btick") && ClientConnection.control != null) {
			ClientConnection.control.ball.getPosition().set(message.argDouble(0), message.argDouble(1));
//			ClientConnection.control.ball.getVelocity().set(message.argDouble(2), message.argDouble(3));
		}

		// ptick playerName playerPosX playerPosY
		if (message.isCommand("ptick") && ClientConnection.control != null) {
			Slime player = ClientConnection.control.getPlayer(message.argString(0));
			if (player != null)
				player.getPosition().set(message.argDouble(1), message.argDouble(2));
		}

		// roundscore matchTime team1Score team2Score
		if (message.isCommand("roundscore") && ClientConnection.control != null) {
			ClientConnection.control.setMatchTime(message.argInt(0));
			ClientConnection.control.teams[0].setScore(message.argInt(1));
			ClientConnection.control.teams[1].setScore(message.argInt(2));
			ClientConnection.control.endRound();
		}

		// setscore team1Score team2Score
		if (message.isCommand("setscore") && ClientConnection.control != null) {
			ClientConnection.control.teams[0].setScore(message.argInt(0));
			ClientConnection.control.teams[1].setScore(message.argInt(1));
		}

		// matchrunning team1Score team2Score
		if (message.isCommand("matchrunning") && ClientConnection.control != null) {
			ClientConnection.control.playingMatch = true;
			ClientConnection.control.teams[0].setScore(message.argInt(0));
			ClientConnection.control.teams[1].setScore(message.argInt(1));
		}

		// serve
		if (message.isCommand("serve") && ClientConnection.control != null) {
			ClientConnection.control.serve();
		}

		// matchstart team1Score team2Score
		if (message.isCommand("startmatch") && ClientConnection.control != null) {
			ClientConnection.control.teams[0].setScore(message.argInt(0));
			ClientConnection.control.teams[1].setScore(message.argInt(1));
			ClientConnection.control.startMatch();
		}
		
		// removeplayer playerName
		if (message.isCommand("removeplayer") && ClientConnection.control != null) {
			ClientConnection.control.removePlayer(message.argString(0));
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
