package engine.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import engine.math.Vector2f;

public class Window {
	private static final int WINDOW_XPAD = 16;
	private static final int WINDOW_YPAD = 38;
	private static JFrame frame = null;
	private static GameComponent gameComponent = null;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public static void createWindow(int width, int height, String title) {
		// Initialize the frame.
		frame = new JFrame(title);

		// Configure the frame settings.
		frame.setPreferredSize(new Dimension(width + WINDOW_XPAD, height + WINDOW_YPAD));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		
		// Add the window component.
		gameComponent = new GameComponent(width, height);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(gameComponent);

		// Move the frame to the center of the screen.
		Dimension screenSize = frame.getToolkit().getScreenSize();
		frame.setLocation((screenSize.width  / 2) - (frame.getWidth()  / 2),
						  (screenSize.height / 2) - (frame.getHeight() / 2));
		frame.pack();
	}
	

	
	// =================== ACCESSORS =================== //

	public static int getWidth() {
		return gameComponent.width;
	}

	public static int getHeight() {
		return gameComponent.height;
	}
	
	public static Vector2f getCenter() {
		return new Vector2f(getWidth() * 0.5f, getHeight() * 0.5f);
	}

	public static float getAspectRatio() {
		return ((float) getWidth() / (float) getHeight());
	}

	public static String getTitle() {
		return frame.getTitle();
	}
	
	public static Graphics getGraphics() {
		return gameComponent.getGraphics();
	}
	
	public static boolean isFullScreen() {
		return false; // TODO
	}
	
	
	

	// ==================== MUTATORS ==================== //
	
	public static void update() {
		gameComponent.update();
	}
	
	public static void render() {
		gameComponent.repaint();
	}
	
	public static void clear(Color color) {
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, gameComponent.width, gameComponent.height);
		g.dispose();
	}
	

	private static class GameComponent extends JComponent {
		private static final long serialVersionUID = 1L;
		private Image canvas;
		private int width;
		private int height;
		private Mouse mouse;
		private Keyboard keyboard;
		
		
		
		/** Setup the component. **/
		public GameComponent(int width, int height) {
			mouse    = new Mouse(this);
			keyboard = new Keyboard();
			addMouseListener(mouse);
			addMouseWheelListener(mouse);
			addKeyListener(keyboard);
			
			this.width  = width;
			this.height = height;
			canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			this.setFocusable(true);
		}
		
		public Graphics getGraphics() {
			return canvas.getGraphics();
		}

		/** Update the component, checking for a change in window size. **/
		public void update() {
			if (width != getWidth() || height != getHeight()) {
				resizeCanvas(getWidth(), getHeight());
			}
		}

		/** Resize the image canvas. **/
		public void resizeCanvas(int w, int h) {
			width  = Math.max(w, 1);
			height = Math.max(h, 1);
			canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}

		@Override
		/** Handle rendering and draw the raw canvas image to the window. **/
		protected void paintComponent(Graphics g) {
			g.drawImage(canvas, 0, 0, null);
		}
	}
}
