package main;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


/**
 * Game.
 * 
 * @author David Jordan
 */
public class Game extends JComponent {
	private static final long serialVersionUID = 1L;
	public static Image canvas;
	public static Point viewSize = new Point(1100, 900);
    private static boolean initialized = false;
    
    
    /** Setup the canvas and install listeners. **/
	public void initialize() {
		canvas = new BufferedImage(viewSize.x, viewSize.y, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse, Load images, and initialize the game:
		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		ImageLoader.loadAllImages();
        initializeGame();
        initialized = true;
        
    	// Set window to full screen:
        Main.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
	
    /** Initialize any game objects. **/
	public void initializeGame() {
		// TODO
//		gameRunner = new EditorComponent();
	}
	
    /** Update the game objects. **/
	public void update() {
//		gameRunner.update();
		
		// End the game if the user presses Escape:
//		if (Keyboard.restart.down() && Keyboard.control.down())
//			initializeGame();
	}
	
	public void resizeCanvas(Point newSize) {
		viewSize.setLocation(Math.max(newSize.x, 1), Math.max(newSize.y, 1));
		canvas = new BufferedImage(viewSize.x, viewSize.y, BufferedImage.TYPE_INT_RGB);
	}

    /** Draw on the canvas. **/
	public void render(Graphics g) {
		// Draw a black background:
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, viewSize.x, viewSize.y);
//		g.setColor(Color.WHITE);

		// Draw the Control:
//		gameRunner.draw(g);
	}
	
//	@Override
    /** Handle rendering and draw the raw canvas image to the window. **/
	protected void paintComponent(Graphics g) {
		if (!initialized)
			return;
//		Graphics bufferGraphics = canvas.getGraphics();
//		render(bufferGraphics);
//		bufferGraphics.dispose();
		g.drawImage(canvas, 0, 0, null);
		g.drawImage(ImageLoader.getImage("toolSelect"), 32, 32, null);
	}
}
