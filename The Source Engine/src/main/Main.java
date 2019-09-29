package main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JFrame;
import projects.gravitySimulator.TestRunner;
import control.Control;
import control.ControlRunner;
import map.editor.EditorComponent;


/**
 * This class sets up a window and starts
 * a thread to update and render the Game
 * class.
 * 
 * @author David
 */
public class Main implements Runnable {
	public static final int FPS               = 60;
	public static final double STEP_TIME      = 1.0 / (double) FPS;
	public static final int STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
	
	private static final int MAX_TIMES    = 20;
	private static long[]	 frameTimes   = new long[MAX_TIMES];
	private static int		 frameCurrent = 0;
	
	public static boolean running;
	public static JFrame frame;
    public static GameRunnerOLD gameRunner;
	
	public static double averageFPS = 0.0;

	
	public static void main(String [] args) {
    	new Main();
	}

	/** Startup the window and game thread. **/
	public Main() {
		// Initialize a new frame and game object:
		frame = new JFrame("New Game - David Jordan");
		
		ImageLoader.loadAllImages();
		LookAndFeel.set(LookAndFeel.LAF_SYSTEM);
		
		
		
		gameRunner = new EditorComponent();
//		gameRunner = new TestRunner();
		
		
		
		// Configure the frame settings:
		frame.setPreferredSize(new Dimension(Game.viewSize.x + 18, Game.viewSize.y + 40));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	
    	// Initialize frame counter:
    	for (int i = 0; i < MAX_TIMES; i++)
    		frameTimes[i] = 0;
    	
    	// Initialize Game:
    	gameRunner.initialize();
		LookAndFeel.set(LookAndFeel.LAF_SYSTEM);
		
    	// Set window to full screen:
        Main.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		
    	// Start the thread and request focus for the frame:
    	new Thread(this).start();
	}
	
	/** End the main loop, thus ending the game. **/
	public static void stop() {
		running = false;
	}

	@Override
	/** The main loop for running the game. **/
	public void run() {
//    	long time = System.currentTimeMillis();
//    	long lastUpdate = System.currentTimeMillis();
		running   = true;
		
		while (running) {
	    	long tickStartTime = System.currentTimeMillis();
			Cursor prevCursor = Mouse.getCursor();
			Mouse.setDefaultCursor();
			
			// Update the mouse and keyboard inputs, then update and repaint the game:
			Keyboard.update();
			Mouse.update();
			gameRunner.update();
			gameRunner.draw();

			if (!Mouse.inWindow())
				Mouse.setCursor(Cursor.DEFAULT_CURSOR);
			if (Mouse.getCursor() != prevCursor) {
				frame.setCursor(Mouse.getCursor());
			}
			
			updateFPS();

	    	int tickTime = (int) (System.currentTimeMillis() - tickStartTime);
	    	
			// Wait until the next step:
	    	if (tickTime < STEP_MILLISECONDS - 1) {
//	    		System.out.println("Doin good");
    			try {
//    				time += 1000.0 / FPS;
//    				Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
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
