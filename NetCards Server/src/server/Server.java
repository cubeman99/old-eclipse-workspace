package server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import cardgame.CardGame;


public class Server extends Thread {
	public static ServerSocket serverSocket;
	public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public static void close() throws IOException {
		for (ClientThread ct : clientThreads) {
			ct.close();
		}
	}

	public void run() {
		while (true) {
			try {
				// Wait for a new player to join
				Socket newSocket = serverSocket.accept();
				
				// Create a thread for the new player
				addClientThread(newSocket);
			}
			catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}
			catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public static synchronized Player getPlayer(String playerName) {
		for (Player p : players) {
			if (p.getName().equals(playerName))
				return p;
		}
		return null;
	}
	
	public static synchronized String getPlayerName(int playerIndex) {
		if (playerIndex >= players.size() && playerIndex >= 0)
			return "ERROR";
		return players.get(playerIndex).getName();
	}
	
	public static synchronized int playerCount() {
		return players.size();
	}
	
	public static synchronized boolean checkNameIsFree(String name) {
		for (Player p : players) {
			if (p.name.equals(name)) {
				return false;
			}
		}
		return true;
	}
	
	public static synchronized void removeClientThread(ClientThread clientThread) {
		int index = clientThreads.indexOf(clientThread);
		
		if (clientThreads.contains(clientThread)) {
    		ServerLog.logInfo(clientThread.name + " disconnected");
    		clientThreads.remove(clientThread);
    		players.remove(clientThread.player);
		}
		
		if (players.size() == 0) {
			// All players have left, reset the game.
    		ServerLog.logInfo("All players have left, restarting game...");
    		CardGame.initializeNewGame();
		}
		else {
    		if (CardGame.getJudgeName().equals(clientThread.name)) {
    			CardGame.handleJudgeLeaving();
    		}
    		else if (index < CardGame.judgeIndex) {
    			CardGame.judgeIndex -= 1;
    		}
    		
    		// removeplayerholder <str playername>
    		MessageDecoder.broadcastMessage(new Message("removeplayerholder", clientThread.name));
		}
	}
	
	private void addClientThread(Socket newSocket) {
		// Set up a thread to handle a new player
		ClientThread thread = new ClientThread(newSocket);
		thread.start();
		thread.player = new Player(thread);
	}
	
	public static void main(String [] args) {
//		System.out.println("#                                                             #");
		System.out.println("###############################################################");
		System.out.println("# +---------------------------------------------------------+ #");
		System.out.println("# |          CARDS AGAINST HUMANITY SERVER CONSOLE          | #");
		System.out.println("# +---------------------------------------------------------+ #");
		System.out.println("###############################################################");
		System.out.println();
		
		int port = 25565;
		ServerLog.logInfo("Starting server with port " + port);
		CardGame.initializeConfig();
		CardGame.initializeNewGame();
		
		try {
			Thread t = new Server(port);
			t.start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}