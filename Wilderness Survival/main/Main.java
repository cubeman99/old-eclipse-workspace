package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
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
	public static final int FPS           = 60;
	public static final double STEP_TIME  = 1.0 / FPS;
	
	private static final int MAX_TIMES    = 20;
	private static long[]	 frameTimes   = new long[MAX_TIMES];
	private static int		 frameCurrent = 0;
	
	
	public static boolean running;
	public static JFrame frame;
	public static Game game;
	
	public static double averageFPS = 0.0;

	
	public static void main(String [] args) {
    	new Main();
	}

	/** Startup the window and game thread. **/
	public Main() {
		// Initialize a new frame and game object:
		frame = new JFrame("New Game - David Jordan");
		game  = new Game();
		
		// Add the game to the frame's content pane:
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(game);
		frame.addKeyListener(new Keyboard());
		
		// Configure the frame settings:
		frame.setPreferredSize(new Dimension(Game.viewSize.x + 18, Game.viewSize.y + 40));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	
    	for (int i = 0; i < MAX_TIMES; i++) {
    		frameTimes[i] = 0;
    	}
    	
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
    	long lastUpdate = System.currentTimeMillis();
		running   = true;
		
		while (running) {
			Cursor prevCursor = Mouse.getCursor();
			Mouse.setDefaultCursor();
			
			// Update the mouse and keyboard inputs, then update and repaint the game:
	    	frame.requestFocusInWindow();
			Keyboard.update();
			Mouse.update();
			game.update();
			game.repaint();
			
			if (Mouse.getCursor() != prevCursor) {
				Main.game.setCursor(Mouse.getCursor());
			}
			
			updateFPS();
			
			// Wait until the next step:
			try {
				time += 1000.0 / FPS;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// End the game:
		System.exit(0);
	}
	
	private static void updateFPS() {
		// Get current steps per second
		frameTimes[frameCurrent] = System.currentTimeMillis();
		int frameLast = frameCurrent + 1;
		if (frameLast >= MAX_TIMES) {
			frameLast = 0;
		}
		// Steps per second = 1000 / (steps[current] - steps[current - maxSteps + 1]) * (maxSteps - 1);
		averageFPS = 1000.0 / (float) (frameTimes[frameCurrent] - frameTimes[frameLast]) * (double) (MAX_TIMES - 1);
		
		frameCurrent = frameLast;
	}
}
