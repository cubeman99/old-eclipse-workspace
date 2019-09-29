package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import simulator.SimulatorInterface;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;

	public static int VIEW_WIDTH  = 640;
	public static int VIEW_HEIGHT = 480;
	public static Image buffer;
	public static Mouse mouse;
    public static Image img;
    
    public static SimulatorInterface simulator;
    
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse and Keyboard, Load images, and initialize the game
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	
	public void initializeGame() {
		// INITIALIZE DIFFERENT CLASSES
		// ...
		simulator = new SimulatorInterface();
	}
	
	
	public void update() {
		// UPDATE EVENTS HERE
		// ...
		simulator.update();
		
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		g.setColor(Color.BLACK);

		// DRAW EVENTS HERE
		// ...
		simulator.draw(g);
		
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
