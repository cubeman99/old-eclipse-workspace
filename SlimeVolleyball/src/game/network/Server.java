package game.network;

import game.SlimeBallRunner;
import game.SlimeBallServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class Server extends Thread {
	public ServerSocket serverSocket;
	private ArrayList<User> users = new ArrayList<User>();
	private UserListener userListener;
	public SlimeBallRunner runner;
	private int userIds;
	


	// ================== CONSTRUCTORS ================== //
	
	public Server(int port) throws IOException {
		userListener = null;
		serverSocket = new ServerSocket(port);
		runner       = new SlimeBallRunner(new SlimeBallServer(this));
		userIds      = 0;
	}


	
	// =================== ACCESSORS =================== //
	
	public UserListener getUserListener() {
		return userListener;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public SlimeBallRunner getRunner() {
		return runner;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setUserListener(UserListener userListener) {
		this.userListener = userListener;
	}
	
	public void broadcastMessage(Message msg) {
		for (int i = 0; i < users.size(); i++) {
			users.get(i).sendMessage(msg);
		}
	}
	
	public void close() throws IOException {
		for (User ct : users) {
			ct.close();
		}
	}
	
	public synchronized void removeUser(User user) {
		if (users.contains(user)) {
    		System.out.println(user.getUsername() + " disconnected");
    		users.remove(user);
			if (userListener != null)
				userListener.onLeave(user);
		}
	}
	
	private void addUser(Socket newSocket) {
		User newUser = new User(this, newSocket, userIds);
		users.add(newUser);
		newUser.start();
		userIds++;
		
		if (userListener != null)
			userListener.onJoin(newUser);
	}

	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void run() {
		while (true) {
			try {
				// Wait for a new player to join
				Socket newSocket = serverSocket.accept();
				addUser(newSocket);
				System.out.println("User Connected!");
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
	
	
	
	// ================= MAIN FUNCTION ================= //
	
	public static void main(String [] args) {
		int port = 25565;
		System.out.println("Starting server with port " + port);
		
		try {
			Thread t = new Server(port);
			t.start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}