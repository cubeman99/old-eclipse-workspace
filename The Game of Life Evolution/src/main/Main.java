package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;


/**
 * This class sets up a windos and starts
 * a thread to update and render the Game
 * class.
 * 
 * @author David
 */
public class Main implements Runnable {
	public static final int FPS = 60;
	public static boolean running;
	public static JFrame frame;
	public static Game game;

	
	public static void main(String [] args) {
    	new Main();
	}

	/** Startup the window and game thread. **/
	public Main() {
		// Intitialize a new frame and game object:
		frame = new JFrame("New Game - David Jordan");
		game  = new Game();
		
		
		// Configure the frame settings:
		frame.setPreferredSize(new Dimension(600, 600));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	
    	// Start the thread and request focus for the frame:
    	new Thread(this).start();
    	frame.requestFocusInWindow();
	}
	
	/** End the main loop, thus ending the game. **/
	public static void stop() {
		running = false;
	}

	@Override
	/** The main loop for running the game. **/
	public void run() {
    	long time = System.currentTimeMillis();
		running   = true;
		
		while (running) {
			// Upate the mouse and keyboard inputs, then update and repaint the game:
			Keyboard.update();
			Mouse.update();
			game.update();
			game.draw();
			
			// End the game if the user presses Escape:
			if (Keyboard.escape.down())
				Main.stop();
			
			// Wait until the next frame:
			try {
				time += 1000 / FPS;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		// End the game:
		System.exit(0);
	}
}
