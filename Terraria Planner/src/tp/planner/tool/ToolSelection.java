package tp.planner.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.Settings;
import tp.common.graphics.Draw;
import tp.main.Keyboard;
import tp.main.Keyboard.Key;
import tp.main.Mouse;
import tp.planner.Grid;
import tp.planner.tile.Tile;

public class ToolSelection extends Tool {
	private Point dragPoint;
	private boolean dragging;
	private boolean movingSelection;
	private Point moveSelectionPoint;
	private Point moveSelectionRectCorner;
	private Rectangle selectionRect;
	private Point originalSelectionPos;
	private Key keyDeselect;
	private Key keyDelete;
	private Image selectionImage;
	private ArrayList<Tile> selectionTiles;
	
	public ToolSelection() {
		super("Selection Tool", "f", KeyEvent.VK_F);
		dragPoint               = null;
		dragging                = false;
		selectionRect           = null;
		keyDeselect             = new Key(KeyEvent.VK_D);
		keyDelete               = new Key(KeyEvent.VK_DELETE);
		selectionImage          = null;
		selectionTiles          = new ArrayList<Tile>();
		originalSelectionPos    = null;
		movingSelection         = false;
		moveSelectionPoint      = null;
		moveSelectionRectCorner = null;
	}
	
	private void pickupSelection() {
		selectionImage = new BufferedImage(selectionRect.getWidth() * 16,
				selectionRect.getHeight() * 16,
				BufferedImage.TYPE_INT_ARGB);
		
		drawToSelectionImage();
		
		for (int x = selectionRect.getX1(); x < selectionRect.getX2(); x++) {
			for (int y = selectionRect.getY1(); y < selectionRect.getY2(); y++) {
				for (int i = 0; i < control.world.getTotalGrids(); i++) {
					Grid grid = control.world.getGrid(i);
					Tile t = grid.get(x, y);
					
					if (t != null && t.getX() == x && t.getY() == y) {
						selectionTiles.add(t);
						t.removeSelfFromGrid();
					}
				}
			}
		}
	}

	private void dropSelection() {
		dropSelection(false);
	}
	
	private void dropSelection(boolean duplicate) {
		if (selectionRect == null)
			return;
		Point posAdd = selectionRect.getCorner().minus(originalSelectionPos);
		
		if (selectionTiles.size() > 0) {
    		for (int i = 0; i < selectionTiles.size(); i++) {
    			Tile t = selectionTiles.get(i);
    			if (duplicate)
    				selectionTiles.set(i, t.getCopy());
    			
    			t.getPosition().add(posAdd);
    			t.putSelfInGrid(false);
    		}
    		
    		if (!duplicate)
    			selectionTiles.clear();
    		
    		for (int i = 0; i < control.world.getTotalGrids(); i++) {
    			Grid grid = control.world.getGrid(i);
    			grid.refreshArea(selectionRect.getX1() - 2,
    					selectionRect.getY1() - 2, selectionRect.getWidth() + 5,
    					selectionRect.getHeight() + 5);
    		}
		}

		if (!duplicate)
			selectionRect = null;
	}
	
	private void deleteSelection() {
		selectionRect   = null;
		movingSelection = false;
		selectionTiles.clear();
	}
	
	private void drawToSelectionImage() {
		Image gridImage = control.world.getCanvas().getImage();
		Point imgSize = new Point(selectionRect.getWidth() * 16, selectionRect.getHeight() * 16);
		Rectangle r = getScaledSelectionRect();

		selectionImage.getGraphics().drawImage(gridImage, 0, 0, r.getWidth(),
				r.getHeight(), r.getX1(), r.getY1(), r.getX2(), r.getY2(), null);
	}
	
	private Rectangle getScaledSelectionRect() {
		return new Rectangle(selectionRect).scale(16);
	}
	
	private boolean isMouseOverSelection() {
		if (selectionRect == null)
			return false;
		Point corner = new Point(selectionRect.getCorner()).scale(16).minus(control.getViewPosition());
		Point size   = new Point(selectionRect.getSize()).scale(16);
		return Mouse.inArea(corner, size);
	}
	
	@Override
	public void onFinish() {
		dragging        = false;
		movingSelection = false;
		dropSelection();
	}

	@Override
	public void update() {
		Point ms = control.getCursorPoint();
		
		if (dragging) {
			selectionRect = new Rectangle(Math.min(dragPoint.x, ms.x),
					Math.min(dragPoint.y, ms.y),
					Math.abs(dragPoint.x - ms.x) + 1, Math.abs(dragPoint.y
							- ms.y) + 1);

			if (!Mouse.left.down()) {
				dragging  = false;
				dragPoint = null;
				originalSelectionPos = new Point(selectionRect.getCorner());
				pickupSelection();
			}
		}
		if (selectionRect != null && !dragging) {
			if (keyDelete.pressed()) {
				deleteSelection();
			}
			else if (movingSelection) {
				selectionRect.corner.set(moveSelectionRectCorner.plus(ms.minus(moveSelectionPoint)));
				
				if (!Mouse.left.down()) {
					movingSelection    = false;
					moveSelectionPoint = null;
				}
			}
			else if (Keyboard.control.down() && keyDeselect.down()) {
				dropSelection();
			}
			else if (isMouseOverSelection()) {
				if (Mouse.left.pressed()) {
					movingSelection         = true;
					moveSelectionPoint      = new Point(ms);
					moveSelectionRectCorner = new Point(selectionRect.getCorner());
					
					if (Keyboard.control.down()) {
						// Copy selection:
						dropSelection(true);
					}
				}
			}
		}
		if (Mouse.left.pressed() && !isMouseOverSelection() && !control.getHUD().isBusy()) {
			dragging = true;
			dragPoint = control.getCursorPoint();
			if (selectionRect != null)
				dropSelection();
		}
	}
	
	@Override
	public void draw(Graphics g) {
		int S   = 16;
		Point v = control.getViewPosition();
		Graphics2D g2 = (Graphics2D) g;
		
		if (selectionRect != null) {
			
			// Draw selection image:
			if (!dragging) {
				g.drawImage(selectionImage, (selectionRect.getX1() * 16) - v.x,
						(selectionRect.getY1() * 16) - v.y, null);
			}
			
			// Draw selection rectangle:
			g.setColor(Color.WHITE);
			Draw.getGraphics().drawRect((selectionRect.getX1() * S) - v.x - 1,
					(selectionRect.getY1() * S) - v.y - 1,
					(selectionRect.getWidth() * S) + 2,
					(selectionRect.getHeight() * S) + 2);
			
			g.setColor(Color.BLACK);
			Draw.getGraphics().drawRect((selectionRect.getX1() * S) - v.x - 2,
					(selectionRect.getY1() * S) - v.y - 2,
					(selectionRect.getWidth() * S) + 4,
					(selectionRect.getHeight() * S) + 4);
		}
	}
}
