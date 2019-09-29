package client;

import java.net.*;
import java.util.Properties;
import java.io.*;
import javax.swing.JOptionPane;


import cardgame.GameRules;
import client.main.Main;


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
	
	public ClientConnection() throws IOException {
		running = true;
		
		loadPropertiesFile();
		
		outToServer = clientSocket.getOutputStream();
		out = new DataOutputStream(outToServer);
		inFromServer = clientSocket.getInputStream();
		in = new DataInputStream(inFromServer);
		
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
			new Main();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to " + name + ":" + port, "Connection Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void connectAuto() {
		connectTCPIP(connectIP, connectPort);
	}
	
	public static void loadPropertiesFile() {
		isAdmin = FileHandler.getProperty("admin-password").equals(ADMIN_PASSWORD);
	    GameRules.CONSOLE_SHOW_DEBUG    = Boolean.parseBoolean(FileHandler.getProperty("console-show-debug"));
	    GameRules.CONSOLE_SHOW_MESSAGES = Boolean.parseBoolean(FileHandler.getProperty("console-show-messages"));
	    GameRules.IS_ADMIN              = isAdmin;
	}
	
	public static void loadUsernameFile(String filename) {
		// Read: "username.txt"
		
		try {
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in      = new DataInputStream(fstream);
			BufferedReader reader   = new BufferedReader(new InputStreamReader(in));
			clientName              = reader.readLine();
			reader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error with username file: " + e);
		}
		catch (IOException e) {
			System.out.println("Error with username file " + e);
		}
	}
	
	public static void main(String [] args) {
//		connectAuto();
		FileHandler.loadProperties();
		new StartupWindow();
	}
}