package main;

import java.awt.Color;
import java.awt.Graphics;
import life.LifeRunner;

/**
 * Game.
 * 
 * @author David Jordan
 */
public class Game {
	public static GamePanel gamePanel;
    private LifeRunner lifeRunner;
    
    
    /** Setup the canvas and install listeners. **/
	public Game() {
		gamePanel = new GamePanel(Main.frame);
		
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
    /** Initialize any game objects. **/
	public void initializeGame() {
		lifeRunner = new LifeRunner();
	}
	
    /** Update the game objects. **/
	public void update() {
		gamePanel.update();
		lifeRunner.update();
		
		if (Keyboard.escape.down())
			Main.stop();
	}

    /** Draw on the canvas. **/
	public void draw() {
		Graphics g = gamePanel.getGraphics();
		
		// Draw a black background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		g.setColor(Color.BLACK);
		
		lifeRunner.draw(g);
		gamePanel.repaint();
	}
}
