package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import game.GameInstance;
import geometry.Point;
import graphics.Draw;
import graphics.RasterFont;
import graphics.Tileset;
import graphics.library.Library;

import javax.swing.JPanel;

/**
 * The panel that the game is drawn to.
 * @author	Robert Jordan
 */
public class GamePanel extends JPanel {

	// ====================== Constants =======================
	
	private static final long serialVersionUID = 1L;
	public static final Point canvasSize = new Point(160, 144);
	public static BufferedImage	canvas =
			new BufferedImage(canvasSize.x, canvasSize.y, BufferedImage.TYPE_INT_ARGB);
	public static BufferedImage	screenshot = null;
	
	// ====================== Variables =======================
	
	public static GameInstance game;
	public static RasterFont fontLarge;
	public static RasterFont fontSmall;
	public static Tileset hudTileset;
	public static Tileset itemTileset;
	public static int drawScale = 1;
	public static boolean takeScreenshot = false;
	
	// ===================== Constructors =====================
	
	/** Constructs a frame to contain the game. */
	public GamePanel() {
		super();
		
		Library.initialize();
		
		game = new GameInstance();
		game.initialize();
		fontSmall = new RasterFont("font_small", 8, 8, 1, 1, 0, 0, 128, (char)18);
		fontLarge = new RasterFont("font_large", 8, 12, 1, 1, 0, 0, 128, (char)18);
		hudTileset = new Tileset("hud_elements", 8, 8, 1, 1);
		itemTileset = new Tileset("items", 16, 16, 1, 1);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the game. */
	public void update() {
		game.update();
		if (Keyboard.pageUp.pressed()) {
			drawScale = Math.min(4, drawScale + 1);
			Main.resizeWindow(canvasSize.x * drawScale, canvasSize.y * drawScale);
		}
		if (Keyboard.pageDown.pressed()) {
			drawScale = Math.max(1, drawScale - 1);
			Main.resizeWindow(canvasSize.x * drawScale, canvasSize.y * drawScale);
		}
		
	}
    /** Draw on the canvas. **/
	public void render(Graphics2D g) {
		game.draw(g);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//drawHUD(g);
	}
    /** Handle rendering and draw the raw canvas image to the window. */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		Graphics2D gc = (Graphics2D)canvas.getGraphics();
		g2.setBackground(Color.WHITE);
		gc.setBackground(Color.WHITE);
		Draw.clear(g2);
		Draw.clear(gc);
		
		render(gc);
		
		Draw.drawImage(g2, canvas, 0, 0, canvasSize.x * drawScale, canvasSize.y * drawScale);
		
		if (takeScreenshot) {
			screenshot = new BufferedImage(GamePanel.canvasSize.x * GamePanel.drawScale,
					GamePanel.canvasSize.y * GamePanel.drawScale,
					BufferedImage.TYPE_INT_ARGB);
			Draw.drawImage((Graphics2D)screenshot.getGraphics(),
					canvas, 0, 0, canvasSize.x * drawScale, canvasSize.y * drawScale);
			
			takeScreenshot = false;
		}
	}
}