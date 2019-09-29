package game;

import game.network.Message;

public class Test {
    public static void main(String[] args) {
    	System.out.println("Testing");
    	
    	Message msg = new Message(10);
    	
    	System.out.println("Capacity: " + msg.getBufferCapacity());

    	msg.writeString("Hello David,");
    	msg.writeString("My");
    	msg.writeString("Good");
    	msg.writeString("Friend!");
    	
    	
    	msg.getBuffer().position(0);
    	while (msg.hasNext()) {
    		System.out.println(msg.readString());
    		//System.out.print((char) msg.readByte());
    	}
    }
}
