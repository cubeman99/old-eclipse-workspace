package projects.towerDefense.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import projects.towerDefense.GameInstance;
import cmg.graphics.Draw;
import cmg.main.Mouse;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Panel {
	protected BufferedImage buffer;
	protected Point canvasPosition;
	protected Point canvasSize;
	protected GameInstance game;
	protected Color backgroundColor;
	
	
	// ================== CONSTRUCTORS ================== //

	public Panel(GameInstance game) {
		this.game       = game;
		canvasPosition  = new Point(0, 0);
		canvasSize      = new Point(100, 100);
		backgroundColor = Color.WHITE;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Vector getMousePosition() {
		return Mouse.getVector().minus(canvasPosition.x, canvasPosition.y);
	}
	
	public Graphics getGraphics() {
		return buffer.getGraphics();
	}
	
	public BufferedImage getBuffer() {
		return buffer;
	}
	
	public Point getCanvasPosition() {
		return canvasPosition;
	}
	
	public Point getCanvasSize() {
		return canvasSize;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setup(int x, int y, int width, int height) {
		canvasPosition.set(x, y);
		canvasSize.set(width, height);
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public void update() {
		
	}
	
	public void draw() {
		Draw.setGraphics(getGraphics());
		Draw.setColor(backgroundColor);
		Draw.fillRect(0, 0, canvasSize.x, canvasSize.y);
		Draw.setColor(Color.BLACK);
		Draw.drawRect(0, 0, canvasSize.x, canvasSize.y);
	}
}
