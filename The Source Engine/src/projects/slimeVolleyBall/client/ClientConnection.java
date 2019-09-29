package projects.slimeVolleyBall.client;

import java.net.*;
import java.util.Properties;
import java.io.*;
import javax.swing.JOptionPane;
import projects.slimeVolleyBall.SlimeBallRunner;
import projects.slimeVolleyBall.SlimeTestRunner;
import projects.slimeVolleyBall.common.Message;


public class ClientConnection {
	public static final String ADMIN_PASSWORD = "dbj7816";
	
	public static String connectIP = "66.189.36.38";
	public static int connectPort  = 25565;
	
	public static Socket clientSocket;
	public static String clientName = "Guest";
	
	public static boolean running;
	public static OutputStream outToServer;
	public static InputStream inFromServer;
	public static DataOutputStream out;
	public static DataInputStream in;
	public static MessageHandler messageHandler;
	public static boolean isAdmin = false;
	public static SlimeTestRunner runner;
	public static SlimeBallRunner control;
	
	
	public ClientConnection() throws IOException {
		running = true;
		
		outToServer = clientSocket.getOutputStream();
		out = new DataOutputStream(outToServer);
		inFromServer = clientSocket.getInputStream();
		in = new DataInputStream(inFromServer);
		
//		DatagramChannel channel;
		
		sendMessage(new Message("playerjoin", clientName));
	}
	
	public static void sendMessage(Message message) {
		MessageHandler.sendMessage(message);
	}
	
	public static void disconnect() throws IOException {
		running = false;
		clientSocket.close();
	}
	
	
	public static void connectTCPIP(String name, int port) {
		try {
			System.out.println("Connecting to " + name + " on port " + port + "...");
			clientSocket = new Socket(name, port);
			System.out.println("Success joining to "+ clientSocket.getRemoteSocketAddress() + "\n");
			messageHandler = new MessageHandler(clientSocket);
			
			new ClientConnection();
			runner  = new SlimeTestRunner();
			control = runner.control;
			MessageDecoderClient.decodeUnreadMessages();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to " + name + ":" + port, "Connection Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void connectAuto() {
		connectTCPIP(connectIP, connectPort);
	}
	
	public static void main(String [] args) {
//		connectAuto();
		new StartupWindow();
	}
}