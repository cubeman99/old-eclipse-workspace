package projects.slimeVolleyBall.server;

import projects.slimeVolleyBall.Slime;
import projects.slimeVolleyBall.client.ClientConnection;
import projects.slimeVolleyBall.common.Message;


public class MessageDecoder {
	
	public static void decodeMessage(Player sender, String str) {
		decodeMessage(sender, new Message(str));
	}
	
	public static synchronized void decodeMessage(Player sender, Message message) {
		// DEBUG: Log the message
		if (!message.isCommand("ptick"))
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

			/*
			// setname <str newname>>
			sendMessage(sender, new Message("setname", name));
			// addplayerholders <int count> <str holder[0]> <str holder[1]> <str holder[2]>...
			broadcastMessage(new Message("addplayerholders", 1, sender.name));
			
			// addplayerholders <int count> <str holder[0]> <str holder[1]> <str holder[2]>...
			for (Player p : Server.players) {
				if (!p.getName().equals(sender.getName())) {
					sendMessage(sender, new Message("addplayerholders", 1, p.getName()));
				}
			}*/
		}

		// join
		if (message.isCommand("join") && Server.control != null) {
			if (Server.control.playingMatch)
				sendMessage(sender, new Message("matchrunning", Server.control.getTeam(0).getScore(), Server.control.getTeam(1).getScore()));
			
			for (Slime s : Server.control.getPlayers())
				sendMessage(sender, new Message("newplayer", s.getName(), s.getTeamIndex(), s.getColorIndex()));
		}
		
		// jointeam teamIndex colorIndex
		if (message.isCommand("jointeam") && Server.control != null) {
			Server.control.addPlayer(sender.getName(), message.argInt(0), message.argInt(1));
			Server.control.checkMatchReady();
			broadcastMessage(sender, new Message("newplayer", sender.getName(), message.argInt(0), message.argInt(1)));
		}

		// ptick playerPosX playerPosY playerVelX playerVelY
		if (message.isCommand("ptick") && Server.control != null) {
			Slime player = Server.control.getPlayer(sender.getName());
			player.getPosition().set(message.argDouble(0), message.argDouble(1));
			player.getVelocity().set(message.argDouble(2), message.argDouble(3));
			
			broadcastMessage(sender, new Message("ptick", sender.getName(), player.getPosition().x, player.getPosition().y));
		}
	}
	
	public static void broadcastMessage(Message message) {
		// DEBUG: Log the message
//		ServerLog.logMessageOut("BROADCAST: " + message.getText());
		
		for (Player p : Server.players) {
			p.sendMessage(message);
		}
	}
	
	public static void broadcastMessage(Player ignorePlayer, Message message) {
		// DEBUG: Log the message
//		ServerLog.logMessageOut("BROADCAST: " + message.getText());
		
		for (Player p : Server.players) {
			if (p != ignorePlayer)
				p.sendMessage(message);
		}
	}
	
	public static void sendMessage(Player recipient, Message message) {
		// DEBUG: Log the message
//		ServerLog.logMessageOut(recipient.getName() + ": " + message.getText());
			
		recipient.sendMessage(message);
	}
}
