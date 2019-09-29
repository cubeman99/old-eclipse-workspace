package zelda.editor.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.editor.Editor;
import zelda.editor.gui.Bound.BoundType;
import zelda.main.Mouse;


public class Panel {
	public Editor editor;
	public Point position;
	public Point preferredSize;
	public Image image;
	public Bound[] bounds;
	public Point size;
	public int priority;
	public Color color;
	public boolean draggable;
	public boolean scrollable;
	public Point viewPosition;
	public boolean dragging;
	public Point dragPoint;
	public Point dragViewPos;
	public Color backgroundColor;
	public int zoomScale;
	public boolean mouseLeftDown;
	public boolean mouseRightDown;
	public boolean mouseMiddleDown;
	public Point borderSize;
	public String title;
	


	// ================== CONSTRUCTORS ================== //
	
	public Panel(Editor editor) {
		this(editor, null);
	}

	public Panel(Editor editor, String title) {
		this.editor        = editor;
		this.position      = new Point(0, 0);
		this.preferredSize = new Point(10, 10);
		this.size          = new Point(10, 10);
		this.image         = new BufferedImage(size.x, size.y,
				BufferedImage.TYPE_INT_RGB);
		this.bounds          = new Bound[Direction.NUM_DIRS];
		this.priority        = -1;
		this.color           = Color.WHITE;
		this.draggable       = true;
		this.dragging        = false;
		this.title           = title;
		this.borderSize      = new Point(0, title != null ? 20 : 0);
		this.viewPosition    = new Point(borderSize);
		this.dragPoint       = null;
		this.dragViewPos     = null;
		this.backgroundColor = Color.WHITE;
		this.zoomScale       = 1;
		
		this.mouseLeftDown   = false;
		this.mouseRightDown  = false;
		this.mouseMiddleDown = false;

		for (int i = 0; i < bounds.length; i++)
			bounds[i] = new Bound(i);
	}



	// ================= IMPLEMENTABLE ================= //

	public void draw() {
	}



	// =================== ACCESSORS =================== //

	public Graphics getGraphics() {
		return image.getGraphics();
	}

	public int getBoundPos(int dir) {
		return bounds[dir].position;
	}

	public Point getMousePos() {
		return Mouse.position().minus(viewPosition.scaledBy(zoomScale))
				.minus(position).scaledByInv(zoomScale);
	}

	public Point getMousePos(int gridSize) {
		Point ms = getMousePos();
		return new Point((int) (ms.x / (double) gridSize),
				(int) (ms.y / (double) gridSize));
	}

	public boolean containsMouse() {
		return Mouse.inArea(getRect());
	}

	public Rectangle getRect() {
		return new Rectangle(position, size);
	}



	// ==================== MUTATORS ==================== //

	public void update() {
		if (mouseLeftDown && !Mouse.left.down())
			mouseLeftDown = false;
		if (mouseRightDown && !Mouse.right.down())
			mouseRightDown = false;
		if (mouseMiddleDown && !Mouse.middle.down())
			mouseMiddleDown = false;

		if (containsMouse()) {
			if (Mouse.left.pressed())
				mouseLeftDown = true;
			if (Mouse.right.pressed())
				mouseRightDown = true;
			if (Mouse.middle.pressed())
				mouseMiddleDown = true;
		}
		
		if (draggable) {
			if (dragging) {
				Point add = Mouse.position().minus(dragPoint);
				viewPosition.set(dragViewPos.plus(add));
				viewPosition.x = Math.min(viewPosition.x, borderSize.x);
				viewPosition.y = Math.min(viewPosition.y, borderSize.y);

				if (!Mouse.right.down())
					dragging = false;
			}
			else if (containsMouse() && Mouse.right.pressed()) {
				dragging = true;
				dragPoint = Mouse.position();
				dragViewPos = new Point(viewPosition);
			}
		}

		if (scrollable && containsMouse()) {
			if (Mouse.wheelUp())
				viewPosition.y += 16;
			if (Mouse.wheelDown())
				viewPosition.y -= 16;

			viewPosition.y = Math.min(borderSize.y, viewPosition.y);
		}

		image = new BufferedImage(Math.max(2, size.x - viewPosition.x),
				Math.max(2, size.y - viewPosition.y),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, size.x - viewPosition.x, size.y - viewPosition.y);
	}

	public boolean computePriority() {
		priority = 0;

		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			Bound b = bounds[i];

			if (b.type == BoundType.PANEL) {
				int p = b.panel.priority;
				if (p >= 0) {
					if (p + 1 > priority)
						priority = p + 1;
				}
				else {
					priority = -1;
					return false;
				}
			}
		}

		return true;
	}

	public void computePositions(Point windowSize) {
		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			if (bounds[i].type != BoundType.FIXED)
				bounds[i].computePosition(windowSize);
		}
		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			if (bounds[i].type == BoundType.FIXED)
				bounds[i].computePosition(windowSize);
		}

		position.x = bounds[2].position;
		position.y = bounds[1].position;
		size.x = bounds[0].position - position.x;
		size.y = bounds[3].position - position.y;
	}

	public void moveViewPosition(Point amount) {
		viewPosition.add(amount);
		viewPosition.x = Math.min(viewPosition.x, borderSize.x);
		viewPosition.y = Math.min(viewPosition.y, borderSize.y);
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void resetPriority() {
		this.priority = -1;
	}

	public void setBound(Bound b) {
		b.parent = this;
		bounds[b.dir] = b;
	}

	public void draw(Graphics g) {
		Draw.setGraphics(getGraphics());
		Draw.setViewPosition(new Vector());
		draw();

		Rectangle dr = new Rectangle(position, size);
		Rectangle sr = new Rectangle(viewPosition.inverse(),
				size.scaledByInv(zoomScale));

		g.drawImage(image, dr.getX1(), dr.getY1(), dr.getX2(), dr.getY2(),
				sr.getX1(), sr.getY1(), sr.getX2(), sr.getY2(), null);
		
		if (title != null) {
    		g.setColor(Color.BLACK);
    		g.fillRect(position.x, position.y, size.x, 20);
    		g.setColor(Color.WHITE);
    		
    		Graphics2D g2 = (Graphics2D) g;
//			g2.setFont(new Font("Courier", Font.PLAIN, 12));
			g2.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    		g2.drawString(title, position.x + 4, position.y + 15);
		}
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(position.x, position.y, size.x - 1, size.y - 1);
//		g.setColor(Color.LIGHT_GRAY);
//		g.drawRect(position.x + 1, position.y + 1, size.x - 3, size.y - 3);
		g.setColor(Color.BLACK);
//		g.drawRect(position.x + 2, position.y + 2, size.x - 5, size.y - 5);
		g.drawRect(position.x + 1, position.y + 1, size.x - 3, size.y - 3);
		
		if (title != null) {
    		g.setColor(Color.WHITE);
    		g.drawRect(position.x + borderSize.x + 2, position.y + 2, size.x - 5, borderSize.y - 4);
		}
		
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Point dp = new Point(bounds[x * 2].position, bounds[1 + (y * 2)].position);
    			
//    			g.setColor(Color.RED);
//    			g.fillRect(dp.x - 4, dp.y - 4, 8, 8);
			}
		}
	}

	@Override
	public String toString() {
		return ("[" + position.x + ", " + position.y + ", " + size.x + ", "
				+ size.y + "]");
	}
}
