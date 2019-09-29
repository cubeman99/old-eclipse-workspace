package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import simulation.World;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;

	public static int VIEW_WIDTH  = 900;
	public static int VIEW_HEIGHT = 750;
	public static Image buffer;
    public static Image img;
    public static Mouse mouse;
    public static World world;
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		
		// Install Mouse and Keyboard, Load images, and initialize the game
		//addKeyListener(new Keyboard());

    	mouse = new Mouse();
    	addMouseListener(mouse);
    	addKeyListener(new Keyboard());
    	setFocusable(true);
    	requestFocusInWindow();
		
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	
	public void initializeGame() {
		// INITIALIZE DIFFERENT CLASSES
		world = new World(buffer.getGraphics());
		
	}
	
	
	public void update() {
		// UPDATE EVENTS HERE
		world.update();
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		g.setColor(Color.BLACK);

		// DRAW EVENTS HERE
		world.draw(g);
		
		
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
