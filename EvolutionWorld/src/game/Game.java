package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import common.Draw;
import simulation.Simulator;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;

	public static int VIEW_WIDTH  = 1400;
	public static int VIEW_HEIGHT = (int) ((double) VIEW_WIDTH * 0.75);
	public static Image buffer;
	public static Mouse mouse;
    public static Image img;
    
    public static Simulator simulation;
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse, Load images, and initialize the game:
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	
	public void initializeGame() {
		simulation = new Simulator();
	}
	
	
	public void update() {
		// Update the simulation:
		simulation.update();
		
		// The escape key ends the game:
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		g.setColor(Color.WHITE);
		
		Draw.setGraphics(g);
		
		// Draw the simulation:
		simulation.draw(g);
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
