
// File Name GreetingClient.java

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class GreetingClient {
	
	public static void main(String [] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter server name: ");
		String serverName = scanner.nextLine();
		
		System.out.print("Enter Port: ");
		int port = Integer.parseInt(scanner.nextLine());
		
		try {
			System.out.println("Connecting to " + serverName
					+ " on port " + port);
			
			Socket client = new Socket(serverName, port);
			System.out.println("Just connected to "+ client.getRemoteSocketAddress());
			
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server says " + in.readUTF());
			client.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}