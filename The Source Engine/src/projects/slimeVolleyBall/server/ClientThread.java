package projects.slimeVolleyBall.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import projects.slimeVolleyBall.common.Message;


class ClientThread extends Thread {
	public Socket socket;
	public Player player;
	public String name = "ERROR";
	
	private DataInputStream in;
	private DataOutputStream out;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}
	
	private void startClient() throws IOException {
		in  = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
		boolean running = true;
		while (running) {
			String messageText = in.readUTF();
			MessageDecoder.decodeMessage(player, messageText);
		}
	}
	
	public synchronized void sendMessage(Message message) throws IOException {
		out.writeUTF(message.getText());
	}
	
	public synchronized void close() throws IOException {
		socket.close();
	}
	
	public synchronized void disconnect() throws IOException {
		Server.removeClientThread(this);
		close();
	}
	
	public void run() {
		try {
			startClient();
		}
		catch (IOException e) {
			// Player is disconnected:
			try {
				disconnect();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
