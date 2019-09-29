package client.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import cardgame.CardGame;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public static int VIEW_WIDTH  = 780;
	public static int VIEW_HEIGHT = 700;
	public static Image buffer;
	public static Mouse mouse;
	public static Keyboard keyboard;
    public static Image img;
    
    public static String name = "ERROR";

    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse and Keyboard, Load images, and initialize the game
		mouse    = new Mouse();
//		keyboard = new Keyboard();
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
//		addKeyListener(keyboard);
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	
	public void initializeGame() {
		// INITIALIZATION HERE:
		CardGame.initialize();
	}
	
	
	public void update() {
		// UPDATE EVENTS HERE:
		CardGame.update();
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	
	public static Graphics getBufferGraphics() {
		return buffer.getGraphics();
	}
	
	
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.WHITE);
		g.setColor(new Color(153, 204, 255));
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		g.setColor(Color.BLACK);

		// DRAW EVENTS HERE:
		CardGame.draw(g);
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
