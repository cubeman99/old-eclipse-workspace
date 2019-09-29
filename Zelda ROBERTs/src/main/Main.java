package main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * This class sets up a windows and starts a thread to update and render
 * the Game.
 * @author	Robert Jordan
 * @author	David Jordan
 * @see
 * {@linkplain Game}
 */
public class Main implements Runnable {

	// ====================== Constants =======================

	/** The name of the game used for messages. */
	public static final String	GAME_NAME	= "Zelda Oracle";
	/** The name of the game used for messages. */
	public static final String	RAW_GAME_NAME	= "zelda_oracle";
	/** The frame rate the game runs at. */
	public static final int		FRAME_RATE	= 60;
	/** The ratio used to average the fps value. */
	private static final double FPS_RATIO	= 0.9;
	
	// ====================== Variables =======================

	/** The window that contains the game. */
	public static JFrame		frame;
	/** The panel component that the game is drawn to. */
	public static GamePanel		panel;

	/** True if the game is to continue the game loop. */
	private static boolean		running		= false;
	/** The current frames per second of the game. */
	public static double		fps			= 60.0;
	/** The number of steps that have happened since the program was executed. */
	public static long			stepCount	= 0;
	
	public static boolean		debugMode	= false;

	// ===================== Entry Point ======================
	
	/** The entry point for the program. */
	public static void main(String [] args) {
    	new Main();
	}
	/** Constructs the main class that will setup the game. */
	public Main() {
		System.setProperty("sun.java2d.d3d", "false");
		
		ResourceLoader.preloadResources();

		frame = new JFrame(GAME_NAME);
		panel = new GamePanel();
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel);
		
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		
		frame.addKeyListener(keyboard);
		frame.addWindowFocusListener(keyboard);
		frame.addWindowFocusListener(mouse);
		
		panel.addMouseListener(mouse);
		panel.addMouseWheelListener(mouse);

		// Set Icon
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(ImageLoader.getImage("icon16"));
		icons.add(ImageLoader.getImage("icon32"));
		icons.add(ImageLoader.getImage("icon48"));
		frame.setIconImages(icons);
		
		// Configure the frame settings:
		panel.setPreferredSize(new Dimension((int)GamePanel.canvasSize.x, (int)GamePanel.canvasSize.y));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		// Move the frame to the center of the screen:
		Dimension screenSize = frame.getToolkit().getScreenSize();
		frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));

		// Start the thread and request focus for the frame:
		new Thread(this).start();
		frame.requestFocusInWindow();
	}
	
	// ====================== Execution =======================
	
	/** Ends the main game loop. */
	public static void stop() {
		running = false;
	}
	/** The main loop for running the game. */
	public void run() {
    	long time = System.currentTimeMillis();
    	double excessTime = 0.0;
    	final double inverseFrameRate = 1000.0 / (double)FRAME_RATE;
    	long frameStart = System.currentTimeMillis();
    	
		running = true;
		
		while (running) {
			// Update the mouse and keyboard inputs, then update and repaint the game
			frame.requestFocusInWindow();
			Keyboard.update();
			Mouse.update();
			panel.update();
			panel.repaint();
			stepCount++;
			
			// Update the fps
			fps = (fps * FPS_RATIO + 1000.0 / Math.max(1, System.currentTimeMillis() - frameStart) * (1.0 - FPS_RATIO));
			frameStart = System.currentTimeMillis();
			if (Double.isNaN(fps) || Double.isInfinite(fps)) {
				fps = 55.0;
			}
			
			// End the game if the user presses Escape
			if (Keyboard.escape.pressed())
				Main.stop();
			// End the game if the user presses Escape
			if (Keyboard.debug.pressed())
				debugMode = !debugMode;
			if (Keyboard.screenshot.pressed())
				takeScreenShot();
			
			// Sleep until the next frame
			try {
				excessTime	+= inverseFrameRate;
				time		+= (long)excessTime;
				excessTime	-= (long)excessTime;
				Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// End the game
		System.exit(0);
	}
	
	public static void resizeWindow(int width, int height) {
		frame.setSize(width + frame.getInsets().left + frame.getInsets().right,
				height + frame.getInsets().top + frame.getInsets().bottom);
	}
	public static void takeScreenShot() {
		try {
			//System.out.println(frame.getSize());
			//System.out.println(panel.getSize());
			//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			//Rectangle frameRectangle = new Rectangle(panel.getBounds());
			//frameRectangle.add(frame.getLocation());
			//Robot robot = new Robot();
			//BufferedImage image = robot.createScreenCapture(frameRectangle);
			
			GamePanel.takeScreenshot = true;
			
			while (GamePanel.takeScreenshot);
			
			/*BufferedImage image = new BufferedImage(GamePanel.canvasSize.x * GamePanel.drawScale,
					GamePanel.canvasSize.y * GamePanel.drawScale,
					BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(GamePanel.canvas, 0, 0, null);*/
			
			int nextNumber = 1;
			DecimalFormat nameFormat = new DecimalFormat("000");
			for (; new File("screenshots/" + RAW_GAME_NAME + "_" + nameFormat.format(nextNumber) + ".png").exists(); nextNumber++);
			ImageIO.write(GamePanel.screenshot, "png",
					new File("screenshots/" + RAW_GAME_NAME + "_" + nameFormat.format(nextNumber) + ".png"));
			System.out.println("Screenshot taken.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static long getStepTime() {
		return stepCount;
	}
}