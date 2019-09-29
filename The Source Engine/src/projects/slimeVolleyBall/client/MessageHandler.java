package projects.slimeVolleyBall.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import projects.slimeVolleyBall.common.Message;


public class MessageHandler extends Thread {
	static Socket socket;
	static String name;

	public static DataInputStream in;
	public static DataOutputStream out;
	
	public MessageHandler(Socket aSocket) {
		socket = aSocket;
		start();
	}
	
	public void startReader() throws IOException {
		in  = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		boolean running = true;
		
		while (running) {
			String messageText = in.readUTF();
			MessageDecoderClient.decodeMessage(messageText);
		}
	}
	
	public static synchronized void sendMessage(Message message) {
		try {
			out.writeUTF(message.getText());
		}
		catch (IOException e) {
			System.out.println("Error sending message: " + e);
		}
	}
	
	public void run() {
		try {
			startReader();
		}
		catch (IOException e) {
			System.out.println("Lost connection to server.");
//			Main.beginShutDown();
		}
	}
}
