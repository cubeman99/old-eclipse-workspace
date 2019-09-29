

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

//import InputHandler;
//import Keys;
//import Keys.Key;

public class Game extends JComponent {
	public static Image buffer;
	public static Image backgroundBuffer;
	public static Image hudBuffer;
	
	public static int fps					= 0;
	public static int fpsCounter			= 60;
	public static long fpsStartTime			= 1;
	public static int GAME_WIDTH  			= 640;
	public static int GAME_HEIGHT 			= 640;
	public static int VIEW_WIDTH  			= GAME_WIDTH;
	public static int VIEW_HEIGHT 			= GAME_HEIGHT;
	public static double viewX				= 0;
	public static double viewY				= 0;

	public static boolean mbRawLeft			= false;
	public static boolean mbRawMiddle		= false;
	public static boolean mbRawRight		= false;
	public static boolean mbLeft			= false;
	public static boolean mbMiddle			= false;
	public static boolean mbRight			= false;
	public static boolean mbLeftPrev		= false;
	public static boolean mbMiddlePrev		= false;
	public static boolean mbRightPrev		= false;
	public static boolean mbLeftPressed		= false;
	public static boolean mbMiddlePressed	= false;
	public static boolean mbRightPressed	= false;
	public static boolean mbLeftReleased	= false;
	public static boolean mbMiddleReleased	= false;
	public static boolean mbRightReleased	= false;
	public static final int KEYS_DIM 		= 400;
	public static boolean[] keyDown 		= new boolean[KEYS_DIM];
	public static boolean[] rawKeyDown 		= new boolean[KEYS_DIM];
	public static boolean[] keyDownPrev 	= new boolean[KEYS_DIM];
	public static boolean[] keyPressed 		= new boolean[KEYS_DIM];
	public static boolean[] keyReleased		= new boolean[KEYS_DIM];
	public static int mouseX 				= 0;
	public static int mouseY 				= 0;
	public static int rawMouseX 			= 0;
	public static int rawMouseY 			= 0;
	
    public static Keys keys 					= new Keys();
	public static InputHandler keyMappings 		= new InputHandler(keys);
    public static Random random 				= new Random();
    
    public static WireSimulator wireSim;
    
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		try {
			ImageLoader.loadAllImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
        installListeners();
        initializeGame();
	}
	
	public void initializeGame() {
		// INITIALIZE DIFFERENT CLASSES
		TileSheet.initialize();
		wireSim = new WireSimulator();
	}
	
	private void installListeners() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mbRawLeft = true;
					//mbLeftPressed = true;
				}
				if (e.getButton() == MouseEvent.BUTTON2) {
					mbRawMiddle = true;
					//mbMiddlePressed = true;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					mbRawRight = true;
					//mbRightPressed = true;
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mbRawLeft = false;
					//mbLeftReleased = true;
				}
				if (e.getButton() == MouseEvent.BUTTON2) {
					mbRawMiddle = false;
					//mbMiddleReleased = true;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					mbRawRight = false;
					//mbRightReleased = true;
				}
			}
		});
		this.setFocusable(true);
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				keyMappings.keyPress(e);
//				keyPressed[e.getKeyCode()] = true;
				rawKeyDown[e.getKeyCode()] = true;
			}
			public void keyReleased(KeyEvent e) {
				keyMappings.keyRelease(e);
//				keyReleased[e.getKeyCode()] = true;
				rawKeyDown[e.getKeyCode()] = false;
			}
			public void keyTyped(KeyEvent e) {
			}
		});
	}
	
	public void updateFPS() {
		// update FPS
		fpsCounter += 1;
		if (fpsCounter >= 60) {
			fps = (int) (60.0d / ((double)(System.currentTimeMillis() - fpsStartTime) / 1000.0d));
			fpsCounter = 0;
			fpsStartTime = System.currentTimeMillis();
		}
	}
	
	public void update() {
		// Update FPS Counter
		updateFPS();
		// Update all Mouse/Keyboard Inputs
		preTickInput();
		
		// [UPDATE EVENTS HERE...]
		wireSim.update();
		
		
		// DEFAULT: Escape Key ends Game
		if (keys.escape.isPressed)
			Main.stop();
		
		// Update all Mouse/Keyboard Inputs
		postTickInput();
		// Lastly, repaint the screen using double-buffering
		repaint();
	}
	
	public void preTickInput() {
		// Update the mouse position
		Point msPosition = this.getMousePosition();
		if (msPosition != null) {
			rawMouseX = (int) msPosition.x;
			rawMouseY = (int) msPosition.y;
			mouseX = (int) (msPosition.x + viewX);
			mouseY = (int) (msPosition.y + viewY);
		}
		// Update Mouse buttons
		mbLeftPrev   = mbLeft;
		mbMiddlePrev = mbMiddle;
		mbRightPrev  = mbRight;
		mbLeft   = mbRawLeft;
		mbMiddle = mbRawMiddle;
		mbRight  = mbRawRight;
		
		if (mbLeft && !mbLeftPrev)
			mbLeftPressed   = true;
		else if (!mbLeft && mbLeftPrev)
			mbLeftReleased   = true;
		
		if (mbMiddle && !mbMiddlePrev)
			mbMiddlePressed = true;
		else if (!mbMiddle && mbMiddlePrev)
			mbMiddleReleased = true;
		
		if (mbRight && !mbRightPrev)
			mbRightPressed  = true;
		else if (!mbRight && mbRightPrev)
			mbRightReleased  = true;
		
		// Update Keys
		for (int i = 0; i < KEYS_DIM; i++) {
			keyDownPrev[i] = keyDown[i];
			keyDown[i] = rawKeyDown[i];
			if (keyDown[i] && !keyDownPrev[i])
				keyPressed[i] = true;
			else if (!keyDown[i] && keyDownPrev[i])
				keyReleased[i] = true;
		}
	}
	
	public void postTickInput() {
		keys.tick();

        if (!this.isFocusOwner()) {
        	// Clear All Keys
        	keys.clear();
    		for (int i = 0; i < KEYS_DIM; i++) {
    			keyDownPrev[i]		= false;
    			keyDown[i]			= false;
    			keyPressed[i]		= false;
    			keyReleased[i]		= false;
    		}
        }

		mbLeftPressed		= false;
		mbMiddlePressed		= false;
		mbRightPressed		= false;
		mbLeftReleased		= false;
		mbMiddleReleased	= false;
		mbRightReleased		= false;
		
		for (int i = 0; i < KEYS_DIM; i++) {
			keyPressed[i] = false;
			keyReleased[i] = false;
		}
	}
	
	public void render() {
		Graphics g = buffer.getGraphics();
		
		// DEFAULT: Draw a black background
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

		// [DRAW EVENTS HERE...]
		wireSim.draw(g);
		
		g.dispose();
	}
	
	public void paintComponent(Graphics g) {
		render();
		g.drawImage(buffer, 0, 0, null);
	}

}
