package tp.common.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import tp.common.Point;
import tp.common.Settings;
import tp.planner.Grid;
import tp.planner.Item;
import tp.planner.tile.Tile;

public class GridCanvas {
	private Image image;
	private Point size;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public GridCanvas(Point size) {
		this.size  = new Point(size);
		this.image = new BufferedImage(Settings.MAX_CANVAS_SIZE.x
				* Settings.SCALE, Settings.MAX_CANVAS_SIZE.y * Settings.SCALE,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, size.x * Settings.SCALE, size.y * Settings.SCALE);
		
		
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	public Image getImage() {
		return image;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void drawObjectTile(Tile t, int partX, int partY) {
		Item item = t.getItem();
		Image img = item.getImage();
		Point pos = t.getPosition();
		
		if (item.getConnectionScheme() == null) {
			// Draw an object tile:
			Point offset    = item.getOffset();
			int sub         = t.getSubimage();
			Point subAxis   = (item.isHorizontalSubimageStrip() ? new Point(1, 0) : new Point(0, 1));
			Point extOffset = item.getExtendedOffset();
			Point extSize   = item.getExtendedSize();
			
			int dx = (t.getX() + partX) * 16;
			int dy = (t.getY() + partY) * 16;
			int sx = (offset.x - extOffset.x + partX + (sub * subAxis.x * extSize.x) + (t.getRandomSubimage() * extSize.x)) * 18;
			int sy = (offset.y - extOffset.y + partY + (sub * subAxis.y * extSize.y)) * 18;
			
			getGraphics().drawImage(img, dx, dy, dx + 16, dy + 16, sx, sy, sx + 16, sy + 16, null);
		}
		else {
			// Draw a block tile:
    		Point sourcePos = item.getConnectionScheme().getOffset(t.getConnectionIndex());
    		drawImage(img, pos, sourcePos);
		}
		
		if (t.getPacket() != null)
			t.getPacket().draw(getGraphics(), partX, partY);
	}
	
	public void drawWireTile(Tile t) {
		drawImage(t.getItem().getImage(), t.getPosition(), new Point(t.getSubimage(), 0));
	}
	
	public void drawLiquidTile(Tile t) {
		drawImage(t.getItem().getImage(), t.getPosition(), new Point(t.getSubimage(), 0));
	}
	/*
	public void drawObjectTileExtensionUp(Tile t) {
		drawObjectTile(t, 0, t.getHeight(), -1);
	}
	*/
	
	public void drawWallTile(Tile t, int partX, int partY) {
		Point sourcePos = t.getItem().getConnectionScheme().getOffset(t.getConnectionIndex());
		Image img = t.getItem().getImage();
		
		int dx  = t.getX() + partX;
		int dy  = t.getY() + partY;
		int dx2 = (dx * 16) + 16;
		int dy2 = (dy * 16) + 16;
		int x1  = (36 * sourcePos.x) + 8 + (16 * partX);
		int y1  = (36 * sourcePos.y) + 8 + (16 * partY);
		int x2  = x1 + 16;
		int y2  = y1 + 16;

		dx *= 16; dy *= 16;
		if (partX < 0) {x1 += 8; dx  += 8;}
		if (partY < 0) {y1 += 8; dy  += 8;}
		if (partX > 0) {x2 -= 8; dx2 -= 8;}
		if (partY > 0) {y2 -= 8; dy2 -= 8;}
		
		getGraphics().drawImage(img, dx, dy, dx2, dy2, x1, y1, x2, y2, null);
	}
	
	/** Draw the given part of an image. **/
	private void drawImage(Image img, Point drawPos, Point sourcePos) {
		getGraphics().drawImage(img,
				Settings.SCALE * drawPos.x,
				Settings.SCALE * drawPos.y,
				Settings.SCALE * (drawPos.x + 1),
				Settings.SCALE * (drawPos.y + 1),
				(Settings.SCALE + 2) * sourcePos.x,
				(Settings.SCALE + 2) * sourcePos.y, 
				((Settings.SCALE + 2) * sourcePos.x) + Settings.SCALE,
				((Settings.SCALE + 2) * sourcePos.y) + Settings.SCALE,
				null);
	}
	
	/** Clear the given tile point. **/
	public void clearPoint(int x, int y) {
		Graphics2D g = (Graphics2D) getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.fillRect(Settings.SCALE * x, Settings.SCALE * y, Settings.SCALE, Settings.SCALE);
	}
	
	/** Clear the given area. **/
	public void clearRect(int x, int y, int width, int height) {
		Graphics2D g = (Graphics2D) getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.fillRect(Settings.SCALE * x, Settings.SCALE * y, Settings.SCALE * width, Settings.SCALE * height);
	}
	
	/** Clear the entire image. **/
	public void clear() {
		clearRect(0, 0, Settings.MAX_CANVAS_SIZE.x, Settings.MAX_CANVAS_SIZE.y);
	}
}
