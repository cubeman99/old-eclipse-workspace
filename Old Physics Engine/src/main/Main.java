package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Main implements Runnable {
	public static final int FPS   = 60;
	public static boolean running = true;
	public static JFrame frame;
	public static Game game;
	public static Mouse mouse;
	
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
    	/*
    	mouse = new Mouse();
    	frame.addMouseListener(mouse);
    	frame.addKeyListener(new Keyboard());
    	frame.setFocusable(true);
    	frame.requestFocusInWindow();
    	*/
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	
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
			/*
			if (Keyboard.restart.pressed()) {
				game = new Game();
			}*/
			
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
