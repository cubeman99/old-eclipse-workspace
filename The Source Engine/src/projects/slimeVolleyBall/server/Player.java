package projects.slimeVolleyBall.server;

import java.io.IOException;
import projects.slimeVolleyBall.common.Message;

public class Player {
	public ClientThread clientThread;
	public String name   = "(unknown)";
	public boolean ready = false;
	public int score     = 0;
	public boolean waitingToPlay = false;
	
	public Player(ClientThread clientThread) {
		// Setup up a link between ClientThread and Player
		this.clientThread   = clientThread;
		clientThread.player = this;
	}
	
	public void setName(String newName) {
		name = newName;
		clientThread.name = newName;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReadyState(boolean state) {
		this.ready = state;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addPoint() {
		score += 1;
	}
	
	public void sendMessage(Message message) {
		try {
			clientThread.sendMessage(message);
		}
		catch (IOException e) {
			System.out.println("Error sending message: " + e);
		}
	}
}
