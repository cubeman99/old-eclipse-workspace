package game.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class User extends Thread {
	private String username = "ERROR";
	private Server server;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private int id;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public User(Server server, Socket socket, int id) {
		this.server = server;
		this.socket = socket;
		this.id     = id;
		
		try {
			in  = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	// =================== ACCESSORS =================== //
	
	public int getID() {
		return id;
	}
	
	public synchronized String getUsername() {
		return username;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public synchronized void sendMessage(Message msg) {
		try {
			out.write(msg.getRawData());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startClient() throws IOException {
		boolean running = true;
		
		while (running) {
			// Read message into a byte buffer.
			int length      = in.readInt();
			byte[] rawBytes = new byte[length];
			for (int i = 4; i < length; i++)
				rawBytes[i] = in.readByte();
			Message msg = new Message(rawBytes, length);
			
			if (server.getUserListener() != null)
				server.getUserListener().handleMessage(msg, this);
			
//			System.out.println(msg);
//			server.getRunner().getGame().getSlimes().get(index).getPosition().set(msg.readDouble(), msg.readDouble());
//			server.getRunner().getGame().getSlimes().get(index).getVelocity().set(msg.readDouble(), msg.readDouble());
		}
	}
	
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	
	public synchronized void close() throws IOException {
		socket.close();
	}
	
	public synchronized void disconnect() throws IOException {
		server.removeUser(this);
		close();
	}
	
	

	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void run() {
		try {
			startClient();
		}
		catch (IOException e) {
			// Player disconnected.
		}
		
		try {
			disconnect();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
