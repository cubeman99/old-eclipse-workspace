package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JFrame;


/**
 * This class sets up a windos and starts
 * a thread to update and render the Game
 * class.
 * 
 * @author David
 */
public class Main implements Runnable {
	public static final int FPS               = 60;
	public static final double STEP_TIME      = 1.0 / (double) FPS;
	public static final int STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
	public static boolean running;
	public static JFrame frame;
	public static Game game;

	
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
    	
    	// Set window to full screen:
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        
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
	    	long tickStartTime = System.currentTimeMillis();
	    	
			// Upate the mouse and keyboard inputs, then update and repaint the game:
			Keyboard.update();
			Mouse.update();
			game.update();
			game.repaint();
			
			// End the game if the user presses Escape:
			if (Keyboard.escape.down())
				Main.stop();

	    	int tickTime = (int) (System.currentTimeMillis() - tickStartTime);
	    	
			// Wait until the next step:
	    	if (tickTime < STEP_MILLISECONDS - 1) {
    			try {
    				Thread.sleep(Math.max(0, STEP_MILLISECONDS - tickTime));
    			}
    			catch (InterruptedException e) {
    				e.printStackTrace();
    			}
	    	}
		}
		
		// End the game:
		System.exit(0);
	}
}
