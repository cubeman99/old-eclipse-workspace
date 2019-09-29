package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel {
	private JPanel panel;
	private Image canvas;
	private Point size;
	private Mouse mouse;
	private Keyboard keyboard;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public GamePanel() {
		super();
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(canvas, 0, 0, null);
			}
		};
		
		size     = new Point(32, 32); // Size here is meaningless
		canvas   = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_RGB);
		mouse    = new Mouse(panel);
		keyboard = new Keyboard();
		
		panel.addMouseListener(mouse);
		panel.addMouseWheelListener(mouse);
		panel.addKeyListener(keyboard);
		
		panel.setFocusable(true);
		panel.requestFocusInWindow();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the panel the game is drawn in. **/
	public JPanel getPanel() {
		return panel;
	}
	
	/** Return the graphics for the canvas. **/
	public Graphics getGraphics() {
		return canvas.getGraphics();
	}

	/** Return the width of the canvas. **/
	public int getWidth() {
		return size.x;
	}
	
	/** Return the height of the canvas. **/
	public int getHeight() {
		return size.y;
	}
	
	/** Return a point with components for the size of the canvas. **/
	public Point getSize() {
		return size;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void drawBackground(Color backgroundColor) {
		Graphics g = getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	/** Update the panel, checking for certain changed attributes. **/
	public void update() {
		// Get focus:
		panel.requestFocusInWindow();
		
		// Check if panel size has changed:
		Point panelSize = new Point(panel.getWidth(), panel.getHeight());
		if (!panelSize.equals(size))
			resizeCanvas(panelSize);
	}
	
	/** Repaint the graphics onto the screen. **/
	public void repaint() {
		panel.repaint();
	}
	
	/** Resize the canvas to the given size. **/
	private void resizeCanvas(Point newSize) {
		size.setLocation(Math.max(newSize.x, 1), Math.max(newSize.y, 1));
		canvas = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_RGB);
	}
}
