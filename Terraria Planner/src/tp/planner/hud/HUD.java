package tp.planner.hud;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import tp.common.Point;
import tp.common.Rectangle;
import tp.main.ImageLoader;
import tp.main.Mouse;
import tp.planner.Control;
import tp.planner.Item;

public class HUD {
	public static final int BAR_THICKNESS      = 52;
	private static final int SLOT_BACKING_SIZE = 42;
	public Control control;
	public Graphics2D graphics;
	private boolean draggingItem;
	private Item dragItem;
	
	public HudPanel[] panels;
	public HudButtonBar buttonBar;
	public HudToolBar toolBar;
	public HudItemPalette itemPalette;
	public HudHotBar hotBar;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public HUD(Control control) {
		this.control = control;
		draggingItem = false;
		dragItem     = null;
		
		buttonBar   = new HudButtonBar(this);
		toolBar     = new HudToolBar(this);
		itemPalette = new HudItemPalette(this);
		hotBar      = new HudHotBar(this);
		panels      = new HudPanel[] {buttonBar, toolBar, itemPalette, hotBar};
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isBusy() {
		if (draggingItem)
			return true;
		for (HudPanel panel : panels) {
			if (panel.isMouseInArea() || panel.isBusy())
				return true;
		}
		return false;
	}
	
	public boolean mouseOverArea(int x, int y, int size) {
		return Mouse.inArea(x - (size / 2), y - (size / 2), size, size);
	}
	
	public boolean isDroppingDragItem() {
		return (draggingItem && !Mouse.left.down());
	}
	
	public Item getDragItem() {
		return dragItem;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void drawItemSlot(Item i, String text, int dx, int dy) {
		graphics.drawImage(ImageLoader.getImage("slotBackingBlue"), dx - (SLOT_BACKING_SIZE / 2), dy - (SLOT_BACKING_SIZE / 2), null);
		drawItemIconCentered(i, dx, dy);
	}
	
	public void drawItemSlotSelection(int dx, int dy, boolean hovering) {
		int s2 = (SLOT_BACKING_SIZE / 2);
		graphics.drawImage(ImageLoader.getImage("slotBackingSelect"), dx - s2, dy - s2, dx + s2, dy + s2, hovering ? SLOT_BACKING_SIZE : 0, 0, hovering ? SLOT_BACKING_SIZE * 2 : SLOT_BACKING_SIZE, SLOT_BACKING_SIZE, null);
	}
	
	public void drawItemIconCentered(Item i, int dx, int dy) {
		graphics.drawImage(ImageLoader.getImage("itemIcons"), dx - 16, dy - 16, dx + 16, dy + 16, i.getIndex() * 32, 0, (i.getIndex() + 1) * 32, 32, null);
	}
	
	public void drawIconCentered(Image image, int subimage, int dx, int dy) {
		graphics.drawImage(image, dx - 16, dy - 16, dx + 16, dy + 16, subimage * 32, 0, (subimage + 1) * 32, 32, null);
	}
	
	public void drawIconCentered(Image image, int sx, int sy, int dx, int dy) {
		graphics.drawImage(image, dx - 16, dy - 16, dx + 16, dy + 16, sx * 32, sy * 32, (sx + 1) * 32, (sy + 1) * 32, null);
	}
	
	public void drawSelectionBox(int x, int y, int width, int height) {
		Point v = control.getViewPosition();
		graphics.setColor(Color.WHITE);
		graphics.drawRect(x - 1 - v.x, y - 1 - v.y, width + 1, height + 1);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(x - 2 - v.x, y - 2 - v.y, width + 3, height + 3);
	}
	
	public void drawHudSelectionBox(int x, int y, int width, int height) {
		graphics.setColor(Color.WHITE);
		graphics.drawRect(x - 1, y - 1, width + 1, height + 1);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(x - 2, y - 2, width + 3, height + 3);
	}
	
	public void drawButtonOverlay(int x, int y, boolean down) {
		graphics.drawImage(ImageLoader.getImage("buttonOverlay"), x - 21, y - 21, x + 21, y + 21, down ? 0 : 42, 0, down ? 42 : 84, 42, null);
	}
	
	public boolean setHotBarToItem(Item item) {
		hotBar.setIndex(-1);
		for (int i = 0; i < hotBar.getBarSize(); i++) {
			if (hotBar.getBarItem(i) == item) {
				hotBar.setIndex(i);
				return true;
			}
		}
		return false;
	}
	
	public void openItemPanelToItem(Item item) {
		itemPalette.openToItem(item);
	}
	
	public void startDraggingItem(Item item) {
		dragItem     = item;
		draggingItem = true;
	}
	
	public void update() {
		Mouse.setCursor(control.getTool().getCursor());
		
		for (HudPanel panel : panels)
			panel.update();
		
		if (isDroppingDragItem()) {
			draggingItem = false;
			dragItem     = null;
		}
	}
	
	public void draw(Graphics g) {
		this.graphics = (Graphics2D) g;
		
		// Draw the cursor:
		Rectangle cursorBox = control.getCursorBox();
		if (control.getCursorBox() != null && !isBusy())
			drawSelectionBox(cursorBox.getX1() * 16, cursorBox.getY1() * 16, cursorBox.getWidth() * 16, cursorBox.getHeight() * 16);

		hotBar.draw();
		itemPalette.draw();
		toolBar.draw();
		buttonBar.draw();
		
//		if (dragItem != null)
//			Draw.drawItem(dragItem, Mouse.x() - (dragItem.getWidth() * 8), Mouse.y() - (dragItem.getHeight() * 8));
		if (draggingItem && dragItem != null)
			drawItemIconCentered(dragItem, Mouse.x(), Mouse.y());
	}
}
