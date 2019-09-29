package mainOLD;

import game.Control;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import main.ImageLoader;
import main.Keyboard;

/**
 * Game.
 * 
 * @author David Jordan
 */
public class Game {
	public static GamePanel gamePanel;
    private Control control;
    
    
    /** Setup the canvas and install listeners. **/
	public Game() {
		gamePanel = new GamePanel(Main.frame);
		
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
    /** Initialize any game objects. **/
	public void initializeGame() {
		control = new Control();
	}
	
	/** Return the width of the game canvas. **/
	public static int getViewWidth() {
		return gamePanel.getWidth();
	}
	
	/** Return the height of the game canvas. **/
	public static int getViewHeight() {
		return gamePanel.getHeight();
	}
	
    /** Update the game objects. **/
	public void update() {
		gamePanel.update();
		control.update();
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void repaint() {
		gamePanel.repaint();
	}

    /** Draw on the canvas. **/
	public void draw() {
		Graphics g = gamePanel.getGraphics();
		
		// Draw a black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		g.setColor(Color.WHITE);
		
		control.draw(g);
	}
}
