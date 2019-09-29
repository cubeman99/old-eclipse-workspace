package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import control.Control;


/**
 * Game.
 * 
 * @author David Jordan
 */
public class Game extends JComponent {
	private static final long serialVersionUID = 1L;
	public static Image canvas;
	public static Point viewSize = new Point(1100, 900);
    public static Control control;
    
	
    /** Setup the canvas and install listeners. **/
	public Game() {
		canvas = new BufferedImage(viewSize.x, viewSize.y, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse, Load images, and initialize the game:
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
    /** Initialize any game objects. **/
	public void initializeGame() {
		control = new Control();
	}
	
    /** Update the game objects. **/
	public void update() {
		control.update();
		
		// End the game if the user presses Escape:
		if (Keyboard.escape.down())
			Main.stop();
	}

    /** Draw on the canvas. **/
	public void render(Graphics g) {
		// Draw a black background:
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, viewSize.x, viewSize.y);
		g.setColor(Color.WHITE);

		// Draw the Control:
		control.draw(g);
		
		// Draw the average FPS:
		g.setColor(Color.WHITE);
		DecimalFormat format = new DecimalFormat("0.00");
		g.drawString(format.format(Main.averageFPS), 10, 14);
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
