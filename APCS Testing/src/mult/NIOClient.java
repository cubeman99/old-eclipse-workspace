package mult;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class NIOClient {
       public  final static int DEFAULT_PORT = 25565;
       int port = DEFAULT_PORT;
       SocketAddress remote;
       DatagramChannel channel;
       Selector selector;
       ByteBuffer buffer1;
       ByteBuffer buffer2;

       public NIOClient(){
           try{
               remote = new InetSocketAddress("localhost", port);
               channel = DatagramChannel.open();
               channel.configureBlocking(false);
               channel.connect(remote);
               selector = Selector.open();
               channel.register(selector, SelectionKey.OP_READ |     
                                        SelectionKey.OP_WRITE);
               buffer1 = ByteBuffer.allocate(4);
               buffer2 = ByteBuffer.allocate(4);

        	   send(1);
               while (true) {
//            	   send(100);
            	   int r = receive();
            	   if (r != 0)
            		   System.out.println("com = " + r);
               }
           }
           catch (IOException ex) {
                   System.err.println(ex);
           }
       }

       public void send(int command){
           try {
               selector.select(6000);
               Set readyKeys = selector.selectedKeys();
               Iterator iterator = readyKeys.iterator();
               if (iterator.hasNext()) {
                       SelectionKey key = (SelectionKey) iterator.next();
                       iterator.remove();
                       if (key.isWritable()) {
                           buffer1.clear();
                           buffer1.putInt(command);
                           buffer1.flip();
                           channel.write(buffer1);
                       }
               }
           }
           catch (IOException ex) {
                   System.err.println(ex);
           }
       }
       public int receive() {
           int r=0;
           try {
               selector.select(6000);
               Set readyKeys2 = selector.selectedKeys();
               Iterator iterator2 = readyKeys2.iterator();
               if (iterator2.hasNext()) {
                   SelectionKey key2 = (SelectionKey) iterator2.next();
                   iterator2.remove();
                   if (key2.isReadable()) {
                       buffer2.clear();
                       channel.read(buffer2);
                       buffer2.flip();
                       int command = buffer2.getInt();
                       r=command;
                   }
               }
           }
           catch (IOException ex) {
                   System.err.println(ex);
           }
           return r;
       }
       
       
       public static void main(String[] args) {
    	   new NIOClient();
       }
}