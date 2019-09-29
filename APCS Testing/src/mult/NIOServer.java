package mult;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class NIOServer {
   public final static int DEFAULT_PORT = 25565;
   public final static int MAX_PACKET_SIZE = 256;

   public static void main(String[] args) {
       int port = DEFAULT_PORT;
       try {
           DatagramChannel channel = DatagramChannel.open();
           DatagramSocket socket = channel.socket();
           SocketAddress address = new InetSocketAddress(port);
           socket.bind(address);
           ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

           //This simplified version accepts 2 clients-players
           SocketAddress client1 = null;
           SocketAddress client2 = null;

           //get the addresses-initialisation phase
           System.out.println("Waiting for client1...");
           while(client1==null){
                   client1 = channel.receive(buffer);
                   buffer.flip();
                   buffer.clear();
           }
           System.out.println("client1 has connected!");
           System.out.println("Waiting for client2...");
           while(client2==null){
                   client2 = channel.receive(buffer);
                   buffer.flip();
                   buffer.clear();
           }
           System.out.println("client2 has connected!");
           //server now knows the players

           //MAIN LOOP-server is working for the players
           while (true) {
                   SocketAddress client = channel.receive(buffer);
                   //check the source of the datagram
                   if(client.equals(client1)){
                	   System.out.println("Recieved message from client1!");
                           buffer.flip();
                           //to update the other player
                           channel.send(buffer, client2);
                           buffer.flip();
                           buffer.clear();
                   }
                   else{
                	   System.out.println("Recieved message from client2!");
                           buffer.flip();
                           //to update the other player
                           channel.send(buffer, client1);
                           buffer.flip();
                           buffer.clear();
                   }
           }
       }
       catch (IOException ex) {
               System.err.println(ex);
       }  
   }
}