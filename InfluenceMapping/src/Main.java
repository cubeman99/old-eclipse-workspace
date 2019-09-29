


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main implements Runnable {
	public static final int FPS = 60;
	public static long step = 0;
	public static boolean running = true;
	public static JFrame frame;
	public static Game game;
	
	public Main() {
		frame = new JFrame("Shoop'em Doop v_2.0 - David Jordan");
	
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		
		game = new Game();
		c.add(game);
		
		frame.setPreferredSize(new Dimension(660, 680));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	new Thread(this).start();
	}

	public static void main(String [] args) {
    	new Main();
	}

	public static void stop() {
		running = false;
	}
	
	public void run() {
		// Remember the starting time
    	long tm = System.currentTimeMillis();
		running = true;
		
		while( running ) {
			game.update();
			
			try {
				tm += 1000 / FPS;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			}
			catch(InterruptedException e) {}
			
			step += 1;
		}
		System.exit(0);
	}
}
