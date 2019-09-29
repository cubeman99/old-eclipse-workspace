package zelda.common.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import zelda.common.Font;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.AnimationFrame.FramePart;
import zelda.common.util.GMath;
import zelda.game.control.View;
import zelda.game.control.text.LetterString;
import zelda.main.GameRunner;
import OLD.SpriteOLD;


public class Draw {
	private static Graphics2D g;
	private static GameRunner runner;
	private static Vector viewPosition = new Vector();
	private static Color color = Color.BLACK;
	private static java.awt.Font font;
	
	
	
	// =================== ACCESSORS =================== //

	public static GameRunner getRunner() {
		return runner;
	}

	/** Return the graphics object. **/
	public static Graphics2D getGraphics() {
		return g;
	}

	public static Vector getViewPosition() {
		return viewPosition;
	}

	public static Color getColor() {
		return color;
	}
	
	public static java.awt.Font getFont() {
		return font;
	}

	/** Get the x position on the screen. **/
	private static int dx(double x) {
		return GMath.floor(x - viewPosition.x);
	}

	/** Get the y position on the screen. **/
	private static int dy(double y) {
		return GMath.floor(y - viewPosition.y);
	}

	/** Get a magnitude scaled by the zoom. **/
	private static int z(double a) {
		return (int) (a);
	}



	// ==================== MUTATORS ==================== //

	/** Set the graphics object that the draw functions will work on. **/
	public static void setGraphics(Graphics g) {
		Draw.g = (Graphics2D) g;
	}

	public static void setViewPosition(double x, double y) {
		Draw.viewPosition.set(x, y);
	}

	public static void setViewPosition(Vector viewPosition) {
		Draw.viewPosition.set(viewPosition);
	}

	public static void setViewPosition(View view) {
		Draw.viewPosition.set(view.getPosition());
	}

	public static void setRunner(GameRunner runner) {
		Draw.runner = runner;
	}

	public static void setColor(Color color) {
		Draw.color = color;
	}
	
	public static void setFont(java.awt.Font font) {
		Draw.font = font;
	}



	// ================ DRAWING FUNCTIONS ================ //

	public static void drawImage(Image img, double x, double y, double scale) {
		double w = img.getWidth(null);
		double h = img.getHeight(null);
		g.drawImage(img, dx(x), dy(y), z(w / scale), z(h / scale), null);
	}

	public static void drawImage(Image img, Point position, double scale) {
		Draw.drawImage(img, position.x, position.y, scale);
	}

	public static void drawImage(Image img, double x, double y) {
		Draw.drawImage(img, x, y, 1);
	}

	public static void drawImage(Image img, Point position) {
		Draw.drawImage(img, position.x, position.y, 1);
	}


	public static void drawSprite(SpriteOLD spr, double x, double y) {
		g.drawImage(spr.getImage(), dx(x - spr.getOrigin().x),
				dy(y - spr.getOrigin().y), null);
	}

	public static void drawSprite(SpriteOLD spr, double x, double y,
			double xScale, double yScale) {
		Point size = spr.getSize();
		int dx1 = dx(x) - (int) (xScale * spr.getOrigin().x);
		int dy1 = dy(y) - (int) (yScale * spr.getOrigin().y);
		int dx2 = dx(x)
				+ (int) (xScale * (spr.getSize().x - spr.getOrigin().x));
		int dy2 = dy(y)
				+ (int) (yScale * (spr.getSize().y - spr.getOrigin().y));

		g.drawImage(spr.getImage(), Math.min(dx1, dx2), Math.min(dy1, dy2),
				Math.max(dx1, dx2), Math.max(dy1, dy2), (xScale < 0 ? size.x
						: 0), (yScale < 0 ? size.y : 0), (xScale < 0 ? 0
						: size.x), (yScale < 0 ? 0 : size.y), null);
	}

	public static void drawSprite(SpriteOLD spr, Point pos) {
		drawSprite(spr, pos.x, pos.y);
	}

	/** Draw a sprite at the given position with the given rotation. **/
	public static void drawSprite(SpriteOLD spr, Point pos, double rotation) {
		AffineTransform tx = new AffineTransform();

		tx.translate(dx(pos.x - spr.getOrigin().x), dy(pos.y
				- spr.getOrigin().y));
		tx.rotate(-rotation, spr.getOrigin().x, spr.getOrigin().y);

		g.setTransform(tx);
		g.drawImage(spr.getImage(), 0, 0, null);
		g.setTransform(new AffineTransform());
	}



	// =================== SHAPES ==================== //

	public static void drawVect(Vectangle vect, Color color) {
		g.setColor(color);
		g.fillRect(dx(vect.getX1()), dy(vect.getY1()), (int) vect.getWidth(),
				(int) vect.getHeight());
	}

	public static void fillRect(Rectangle r) {
		g.setColor(color);
		g.fillRect(dx(r.getX1()), dy(r.getY1()), r.getWidth(), r.getHeight());
	}

	public static void drawRect(Rectangle r) {
		g.setColor(color);
		g.drawRect(dx(r.getX1()), dy(r.getY1()), r.getWidth() - 1,
				r.getHeight() - 1);
	}



	// ==================== TEXT ===================== //

	public static void drawText(LetterString str, Point pos, Font font,
			Color color) {
		font.setColor(color);

		for (int i = 0; i < str.length(); i++) {
			if (str.getLetter(i).isColored())
				font.setColor(str.getLetter(i).getColor());
			else
				font.setColor(color);
			drawSprite(str.getLetter(i).getSprite(font),
					pos.plus(i * font.getLetterSpriteSize().x, 0));
		}
	}

	public static void drawText(String text, Point pos, Font font, Color color) {
		font.setColor(color);
		SpriteOLD[] letterSprites = font.getTextLetterSprites(text);

		for (int i = 0; i < letterSprites.length; i++) {
			drawSprite(letterSprites[i],
					pos.plus(i * letterSprites[0].getSize().x, 0));
		}
	}
	
	public static void drawString(String str, int x, int y) {
		g.setColor(color);
		g.setFont(font);
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.drawString(str, x, y);
	}
	


	// ================= IMAGES ================== //

	public static void drawImage(ImageSheet sheet, int sx, int sy, double dx, double dy) {
		drawImage(sheet, new Point(sx, sy), new Vector(dx, dy));
	}
	
	public static void drawImage(ImageSheet sheet, int sx, int sy, Vector drawPos) {
		drawImage(sheet, new Point(sx, sy), drawPos);
	}
	
	public static void drawImage(ImageSheet sheet, Point sourcePos, Vector drawPos) {
		Point sp = sourcePos.times(sheet.getImageSize().plus(
				sheet.getSeperation()));
		Point size = sheet.getImageSize();
		
		if (sheet.isOfSubSheet(sourcePos)) {
			size = sheet.getSubSheetImageSize();
			Point diff = sourcePos.minus(sheet.getSubSheetLocation());
			sp = sheet.getSubSheetLocation()
					.times(sheet.getImageSize().plus(sheet.getSeperation()))
					.plus(diff.times(sheet.getSubSheetImageSize().plus(
							sheet.getSeperation())));
		}
		g.drawImage(sheet.getBuffer(), dx(drawPos.x), dy(drawPos.y),
				dx(drawPos.x) + size.x, dy(drawPos.y) + size.y, sp.x, sp.y,
				sp.x + size.x, sp.y + size.y, null);
	}

	public static void drawAnimationFrame(AnimationFrame frame,
			ImageSheet sheet, Vector pos) {
		for (int i = 0; i < frame.getNumParts(); i++) {
			FramePart part = frame.getPart(i);
			
			if (part.getSourcePos().x >= 0 && part.getSourcePos().y >= 0)
				drawImage(sheet, part.getSourcePos(),
						pos.plus(part.getDrawPos()));
		}
	}

	public static void drawSprite(Sprite spr, double x, double y) {
		spr.draw(x, y);
	}

	public static void drawSprite(Sprite spr, Vector pos) {
		spr.draw(pos);
	}

	public static void drawSprite(Sprite spr, Point pos) {
		spr.draw(pos.x, pos.y);
	}
}
