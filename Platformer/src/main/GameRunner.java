package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import common.Settings;

public abstract class GameRunner {
	public static int FPS               = 60;
	public static double STEP_TIME      = 1.0 / (double) FPS;
	public static int STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
	private Thread gameThread;
	private JFrame frame;
	private String title;
	private GameComponent gameComponent;
	
	
	
	public GameRunner() {
		this(60);
	}
	
	public GameRunner(int fps) {
		FPS               = fps;
		STEP_TIME         = 1.0 / (double) FPS;
		STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
		
		title          = "Untitled - David Jordan";
		int viewWidth  = 722; //640;
		int viewHeight = 482; //480 - 54;
		
		// Initialize a new frame and game object:
		frame = new JFrame(title);
		ImageLoader.loadAllImages();
		
		// Configure the frame settings:
		frame.setPreferredSize(new Dimension(viewWidth + 18, viewHeight + 40));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	gameComponent = new GameComponent(viewWidth, viewHeight);
    	Container c = frame.getContentPane();
    	c.setLayout(new BorderLayout());
    	c.add(gameComponent);
    	
    	
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	frame.pack();
    	
    	// Initialize Game:
    	initialize();
    	gameComponent.setReady();
    	
    	// Set window to full screen:
//		windowMaximize();
		
    	// Start the game thread:
    	gameThread = new GameThread();
    	gameThread.start();
	}
	
	public int getViewWidth() {
		return gameComponent.getViewSize().width;
	}
	
	public int getViewHeight() {
		return gameComponent.getViewSize().height;
	}
	
	public Dimension getViewSize() {
		return gameComponent.getViewSize();
	}
	
	/** Set the title of the window. **/
	public void setTitle(String title) {
		this.title = title;
		frame.setTitle(title);
	}
	
	/** Maximize the window. **/
	public void windowMaximize() {
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
	
	protected abstract void initialize();
	protected abstract void update();
	protected abstract void draw(Graphics g);
	
	private class GameThread extends Thread {
		@Override
		public void run() {
    		boolean running = true;
    		
    		while (running) {
    	    	long tickStartTime = System.currentTimeMillis();
    			Cursor prevCursor = Mouse.getCursor();
    			Mouse.setDefaultCursor();
    			
    			// Update the mouse and keyboard inputs, then update and repaint the game:
    			gameComponent.update();
    			Keyboard.update();
    			Mouse.update();
    			update();
    			gameComponent.repaint();
    			gameComponent.requestFocusInWindow();
    			
    			if (Mouse.getCursor() != prevCursor)
    				frame.setCursor(Mouse.getCursor());
    
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
	
	private class GameComponent extends JComponent {
		private static final long serialVersionUID = 1L;
		private Image canvas;
		private Dimension size;
		private boolean ready;
		private Mouse mouse;
		private Keyboard keyboard;
		
		
		/** Setup the component. **/
		public GameComponent(int width, int height) {
			mouse       = new Mouse(this);
			keyboard = new Keyboard();
			addMouseListener(mouse);
			addMouseWheelListener(mouse);
			addKeyListener(keyboard);
			
			ready  = false;
			size   = new Dimension(width, height);
			canvas = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
			
			this.setFocusable(true);
		}
		
		/** Set this component as ready to draw. **/
		public void setReady() {
			ready = true;
		}
		
		/** Return the size of the game's view. **/
		public Dimension getViewSize() {
			return size;
		}
		
		/** Update the component, checking for a change in window size. **/
		public void update() {
			if (!size.equals(getSize())) {
				resizeCanvas(getSize());
			}
		}
		
		/** Resize the image canvas. **/
		public void resizeCanvas(Dimension newSize) {
			size.setSize(Math.max(newSize.width, 1), Math.max(newSize.height, 1));
			canvas = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		}
		
		@Override
		/** Handle rendering and draw the raw canvas image to the window. **/
		protected void paintComponent(Graphics g) {
			Graphics bufferGraphics = canvas.getGraphics();
			bufferGraphics.setColor(Color.BLACK);
			bufferGraphics.fillRect(0, 0, size.width, size.height);
			
			if (ready)
				draw(bufferGraphics);
			bufferGraphics.dispose();
			g.drawImage(canvas, 0, 0, size.width * Settings.DRAW_SCALE,
					size.height * Settings.DRAW_SCALE, 0, 0, size.width, size.height, null);
		}
	}
}
