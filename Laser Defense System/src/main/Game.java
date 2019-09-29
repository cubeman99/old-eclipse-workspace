package main;

import game.Control;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


/**
 * Game.
 * 
 * @author David
 */
public class Game extends JComponent {
	private static final long serialVersionUID = 1L;
	public static Image canvas;
	public static Point viewSize = new Point(640, 480);
    public static Control control;
    
    /** Setup the canvas and install listeners. **/
	public Game() {
		canvas = new BufferedImage(viewSize.x, viewSize.y, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse, Load images, and initialize the game:
		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
    /** Initialize any game objects. **/
	public void initializeGame() {
		// INITIALIZE OBJECTS:
		// ...
		control = new Control();
	}
	
	public static int getViewWidth() {
		return viewSize.x;
	}
	
	public static int getViewHeight() {
		return viewSize.y;
	}
	
	/** Resize the canvas to the given size. **/
	private void resizeCanvas(Point newSize) {
		viewSize.setLocation(Math.max(newSize.x, 1), Math.max(newSize.y, 1));
		canvas = new BufferedImage(viewSize.x, viewSize.y, BufferedImage.TYPE_INT_RGB);
	}

    /** Update the game objects. **/
	public void update() {
		// UPDATE EVENTS HERE:
		// ...
		control.update();
		
		// Check if panel size has changed:
		Point panelSize = new Point(super.getWidth(), super.getHeight());
		if (!panelSize.equals(viewSize))
			resizeCanvas(panelSize);
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}

    /** Draw on the canvas. **/
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, viewSize.x, viewSize.y);

		// DRAW EVENTS HERE:
		// ...
		control.draw(g);
	}
	
	@Override
    /** Handle rendering and draw the raw canvas image to the window. **/
	protected void paintComponent(Graphics g) {
		Graphics bufferGraphics = canvas.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(canvas, 0, 0, null);
	}
}
