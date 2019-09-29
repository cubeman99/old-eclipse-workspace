import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
	
  public static void main (String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter the host name: ");
		String hostname = scanner.nextLine();
		System.out.println("HOST NAME: " + hostname);
		
		try {
			InetAddress ipaddress = InetAddress.getByName(hostname);
			System.out.println("IP address: " + ipaddress.getHostAddress());
		}
		catch (UnknownHostException e) {
			System.out.println("Could not find IP address for: " + hostname);
		}
	}
}