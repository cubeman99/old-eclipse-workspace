package game.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import game.SlimeBallClient;
import game.SlimeBallRunner;

public class Client {
	public static String connectIP = "localhost";
	public static int connectPort  = 25565;
	
	public boolean running;
	public Socket clientSocket;
	public DataOutputStream out;
	public DataInputStream in;
	public MessageHandler messageHandler;
	private int id;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Client(Socket socket) throws IOException {
		clientSocket   = socket;
		out            = new DataOutputStream(clientSocket.getOutputStream());
		in             = new DataInputStream(clientSocket.getInputStream());
		running        = true;
		messageHandler = null;
		
		new SlimeBallRunner(new SlimeBallClient(this));
		
		
		while (running) {
			// Read messages
			int length      = in.readInt();
			byte[] rawBytes = new byte[length];
			for (int i = 4; i < length; i++)
				rawBytes[i] = in.readByte();
			Message msg = new Message(rawBytes, length);
			
			
			if (msg.getType() == 0) {
				id = msg.readInt();
			}
			else if (messageHandler != null)
				messageHandler.handleMessage(msg);
		}
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getID() {
		return id;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
	
	public void sendMessage(Message msg) {
		try {
			out.write(msg.getRawData());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() throws IOException {
		running = false;
		clientSocket.close();
	}
	
	public static void connectTCPIP(String name, int port) {
		try {
			System.out.println("Connecting to " + name + " on port " + port + "...");
			Socket socket = new Socket(name, port);
			System.out.println("Success joining to "+ socket.getRemoteSocketAddress() + "\n");
			new Client(socket);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to connect to " + name + ":" + port);
		}
	}
	
	public static void connectAuto() {
		connectTCPIP(connectIP, connectPort);
	}
	
	public static void main(String [] args) {
		connectAuto();
	}
}