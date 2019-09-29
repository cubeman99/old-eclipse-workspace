package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JComponent;
import snake.SnakeRunner;
import console.Console;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;

	public static int VIEW_WIDTH  = 640;
	public static int VIEW_HEIGHT = 480;
	public static Image buffer;
	public static Mouse mouse;
    public static Image img;
    
    public static Random random = new Random();
    public static Console console;
    public static SnakeRunner snakeRunner;
    
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse and Keyboard, Load images, and initialize the game
		mouse = new Mouse();
		addMouseListener(mouse);
		addKeyListener(new Keyboard());
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	
	public void initializeGame() {
		// INITIALIZE DIFFERENT CLASSES
		console     = new Console();
		snakeRunner = new SnakeRunner(console);
	}
	
	public void update() {
		// UPDATE EVENTS HERE
		snakeRunner.update();
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(console.getImage(), 0, 0, null);
	}
}
