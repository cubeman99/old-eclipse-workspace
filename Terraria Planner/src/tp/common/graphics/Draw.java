package tp.common.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import tp.common.Point;
import tp.planner.Control;
import tp.planner.Item;

public class Draw {
	public static final BasicStroke STROKE_DASHED = new BasicStroke(
			1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {8}, 0);
	public static final Font FONT_NORMAL = new Font("Verdana", Font.PLAIN, 12);
	public static Font FONT_ANDY;
	
	public static final int LEFT     = -1;
	public static final int CENTERED = 0;
	public static final int RIGHT    = 1;
	public static final int TOP      = -1;
	public static final int MIDDLE   = 0;
	public static final int BOTTOM   = 1;
	
	private static Control control;
	private static Graphics2D g;
	
	
	public static void initialize(Control control) {
		Draw.control = control;
		
		// Load the font "Andy"
		try {
			FONT_ANDY = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fontAndy.ttf"))).deriveFont(Font.PLAIN, 24);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FONT_ANDY);
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (FontFormatException e1) {
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void setGraphics(Graphics g) {
		Draw.g = (Graphics2D) g;
	}
	
	public static Graphics2D getGraphics() {
		return g;
	}
	
	public static void drawStringShadowed(String str, int x, int y, int xAlign, int yAlign, int shadowOffset) {
		g.setColor(Color.BLACK);
		drawString(str, x + shadowOffset, y + shadowOffset, xAlign, yAlign);
		g.setColor(Color.WHITE);
		drawString(str, x, y, xAlign, yAlign);
	}
	
	public static void drawStringShadowed(String str, int x, int y, int xAlign, int yAlign) {
		drawStringShadowed(str, x, y, xAlign, yAlign, 2);
	}
	
	public static void drawString(String str, int x, int y, int xAlign, int yAlign) {
		int w = g.getFontMetrics().stringWidth(str);
		int h = g.getFontMetrics().getAscent();
		
		int dx = x - (xAlign >= 0 ? w / (xAlign == RIGHT ? 1 : 2) : 0);
		int dy = y + (yAlign <= 0 ? h / (yAlign == TOP ? 1 : 2) : 0);
		
		g.drawString(str, dx, dy);
	}
	
	public static void drawItem(Item item, int x, int y) {
		Image image  = item.getImage();
		Point source = item.getOffset();
		int sep      = (item.getGridIndex() == Item.TYPE_WALL ? 36 : 18);
		int sAdd     = (item.getGridIndex() == Item.TYPE_WALL ? 8 : 0);
		if (item.getConnectionScheme() != null)
			source = item.getConnectionScheme().getOffset(15);
        
		System.out.println("ASDASD");
		
		for (int xx = 0; xx < item.getWidth(); xx++) {
			for (int yy = 0; yy < item.getHeight(); yy++) {
				Point sp = source.plus(xx, yy);
				Point dp = new Point(x + (xx * 16), y + (yy * 16));
				g.drawImage(image, dp.x, dp.y, dp.x + 16, dp.y + 16,
						(sp.x * sep) + sAdd, (sp.y * sep) + sAdd, (sp.x * sep) + 16 + sAdd, (sp.y * sep) + 16 + sAdd, null);
			}
		}
	}
	
	public static void drawItem(Item item, int x, int y, float alpha) {
		Graphics2D g2 = (Graphics2D) g.create();
		Point v       = control.getViewPosition();
		Image image   = item.getImage();
		Point source  = item.getOffset().minus(item.getExtendedOffset());
		int sep       = (item.getGridIndex() == Item.TYPE_WALL ? 36 : 18);
		int sAdd      = (item.getGridIndex() == Item.TYPE_WALL ? 8 : 0);
		if (item.getConnectionScheme() != null)
			source = item.getConnectionScheme().getOffset(15);
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(ac);
        
		for (int xx = 0; xx < item.getWidth(); xx++) {
			for (int yy = 0; yy < item.getHeight(); yy++) {
				Point sp = source.plus(xx, yy);
				Point dp = new Point(x + xx, y + yy);
				g2.drawImage(image, dp.x * 16 - v.x, dp.y * 16 - v.y, (dp.x + 1) * 16 - v.x, (dp.y + 1) * 16 - v.y,
						(sp.x * sep) + sAdd, (sp.y * sep) + sAdd, (sp.x * sep) + 16 + sAdd, (sp.y * sep) + 16 + sAdd, null);
			}
		}
		
		g2.dispose();
	}
	
	public static void drawSprite(Sheet sheet, int dx, int dy, int spriteX, int spriteY) {
		sheet.drawSprite(g, dx, dy, spriteX, spriteY);
	}
	
	public static void drawSpriteCentered(Sheet sheet, int dx, int dy, int spriteX, int spriteY) {
		sheet.drawSprite(g, dx - (sheet.getSpriteWidth() / 2),
				dy - (sheet.getSpriteHeight() / 2), spriteX, spriteY);
	}

	public static void configureText(Font font, Color color, boolean antiAlias) {
		g.setFont(font);
		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(color);
	}

	public static void configureText(Font font, int fontSize, Color color, boolean antiAlias) {
		configureText(font.deriveFont(Font.PLAIN, fontSize), color, antiAlias);
	}
	
	/** Set the stroke for drawing. **/
	public static void setStroke(Stroke s) {
		g.setStroke(s);
	}
	
	/** Reset the stroke to a solid line. **/
	public static void resetStroke() {
		g.setStroke(new BasicStroke());
	}
	
}
