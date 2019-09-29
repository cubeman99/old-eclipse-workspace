package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Main implements Runnable {
	public static final int FPS   = 60;
	public static boolean running = true;
	public static JFrame frame;
	public static Game game;
	
	public Main() {
		frame = new JFrame("New Game - David Jordan");
		game  = new Game();
	
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(game);
		
		frame.setPreferredSize(new Dimension(Game.VIEW_WIDTH + 18, Game.VIEW_HEIGHT + 40));
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
    	long time = System.currentTimeMillis();
		running = true;
		
		while (running) {
			Keyboard.update();
			Mouse.update();
			game.update();
			game.repaint();
			
			try {
				time += 1000 / FPS;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		System.exit(0);
	}
}
