package tp.planner;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.awt.CustomCursor;
import tp.common.FileControl;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.Vector;
import tp.common.graphics.Draw;
import tp.common.graphics.Sheet;
import tp.main.GameRunner;
import tp.main.ImageLoader;
import tp.main.Keyboard;
import tp.main.Mouse;
import tp.planner.hud.HUD;
import tp.planner.tile.PacketData;
import tp.planner.tile.Tile;
import tp.planner.tool.*;

public class Control {
	public static final int VIEW_PAN_SPEED = 8;
	
	public GameRunner runner;
	public World world;
	private Sheet[] backdropSheets;
	private int backdropIndex;
	private Point cursor;
	private Point cursorPrev;
	private int itemIndex;
	private int toolIndex;
	private Item cursorItem;
	private boolean replaceMode;
	private Grid workingGrid;
	private Tool[] tools;
	private Tile hoverTile;
	private Rectangle cursorBox;
	private int itemGroupIndex;
	private HUD hud;
	private Point viewPosition;
	private boolean showGrid;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Control(GameRunner runner) {
		this.runner          = runner;
		this.world           = new World(this, 230, 140);
		this.itemIndex       = 0;
		this.toolIndex       = 0;
		this.replaceMode     = false;
		this.cursor          = new Point();
		this.cursorPrev      = new Point();
		
		this.tools = new Tool[] {
			new ToolBrush(),  new ToolHand(),   new ToolSelection(),
			new ToolEraser(), new ToolHammer(), new ToolEyedrop(),
			new ToolRuler(),  new ToolGrowth(), new ToolMagicWand()
		};
		
		this.hoverTile       = null;
		this.cursorBox       = null;
		this.itemGroupIndex  = 0;
		this.viewPosition    = new Point(0, 0);
		this.backdropIndex   = 0;
		this.showGrid        = false;
		
		PacketData.initialize();
		Draw.initialize(this);
		ItemData.createAllItems(this);
		ItemGroupData.initialize();
		FileControl.initialize(this);

		runner.setTitle("Terraria Planner - David Jordan");
		
		this.hud = new HUD(this);

		this.backdropSheets = new Sheet[] {
			new Sheet("backgroundSky",  96),
			new Sheet("backgroundDirt", 96),
			new Sheet("backgroundRock", 96),
			new Sheet("backgroundHell", 96),
		};
		
		for (Tool t : tools)
			t.setControl(this);
		
		Mouse.setCursor(getTool().getCursor());
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Tile getHoverTile() {
		return hoverTile;
	}
	
	public Tile getHoverTile(Point cursor, Grid... grids) {
		for (Grid gr : grids) {
			if (gr.get(cursor) != null)
				return gr.get(cursor);
		}
		return null;
	}
	
	public Tile getHoverTile(Grid... grids) {
		return getHoverTile(cursor, grids);
	}
	
	public Tool getTool() {
		return tools[toolIndex];
	}
	
	public boolean isMouseDown(Mouse.Button mouseButton) {
		return (!hud.isBusy() && (mouseButton.pressed() || (mouseButton.down() && !cursor.equals(cursorPrev))));
	}
	
	public int getItemIndex() {
		return itemIndex;
	}
	
	public boolean getReplaceMode() {
		return replaceMode;
	}
	
	public Point getCursorPoint() {
		return cursor;
	}
	
	public GameRunner getRunner() {
		return runner;
	}
	
	public int getItemGroupIndex() {
		return itemGroupIndex;
	}
	
	public Rectangle getCursorBox() {
		return cursorBox;
	}
	
	public int getToolIndex() {
		return toolIndex;
	}
	
	public Tool[] getTools() {
		return tools;
	}
	
	public HUD getHUD() {
		return hud;
	}
	
	public Point getViewPosition() {
		return viewPosition;
	}
	
	public int getBackdropIndex() {
		return backdropIndex;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void update() {
		cursorPrev = new Point(cursor);
		cursorItem = ItemData.get(itemIndex);
		Point ms   = Mouse.gridPosition();
		
		Vector msV = Mouse.getVector().plus(viewPosition.x, viewPosition.y);
		if (toolIndex == 0)
    		cursor = new Point(
    			(int) ((msV.x / 16.0) - ((double) cursorItem.getWidth() / 2.0) + 0.5),
    			(int) ((msV.y / 16.0) - ((double) cursorItem.getHeight() / 2.0) + 0.5)
    		);
		else
			cursor = new Point((int) (msV.x / 16.0), (int) (msV.y / 16.0));
	    					
		// Set hover tile:
		hoverTile = world.objGrid.get(cursor);
		
		
		// Controls:
		updateControls();
		// Mouse Actions:
		updateMouseActions();
		// Tools:
		updateTools();
		// Buttons:
		hud.update();
		
		/*
		if (hud.isItemOnHotBar(itemIndex)) {
			if (Mouse.wheelUp())
				hud.changeHotBarIndex(-1);
			if (Mouse.wheelDown())
				hud.changeHotBarIndex(1);
		}
		*/
		if (itemIndex >= ItemData.getTotalItems())
			itemIndex -= ItemData.getTotalItems();
		if (itemIndex < 0)
			itemIndex += ItemData.getTotalItems();
		
		/*
		if (Mouse.left.pressed() || (Mouse.left.down() && !cursor.equals(cursorPrev))) {
			world.objGrid.put(cursor, ItemData.get(itemIndex), replaceMode);
		}
		if (Mouse.right.pressed() || (Mouse.right.down() && !cursor.equals(cursorPrev))) {
			Tile t = world.objGrid.get(cursor);
			if (t != null)
				t.removeFromGrid();
		}
		*/
		if (Mouse.middle.down()) {
			Point c = new Point((int) (msV.x / 16.0), (int) (msV.y / 16.0));
			Tile hoverTile = getHoverTile(c, world.objGrid, world.wallGrid, world.liquidGrid);
			
			if (hoverTile != null) {
				setItemIndex(hoverTile.getItem().getIndex(), true);
				setToolIndex(0);
			}
		}
	}
	
	public void setViewPosition(Point viewPosition) {
		this.viewPosition.set(viewPosition);
	}
	
	public void clearWorld() {
		world.clear();
	}
	
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}
	
	public void setBackdropIndex(int backdropIndex) {
		this.backdropIndex = backdropIndex;
	}
	
	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}
	
	public void setItemIndex(int itemIndex, boolean changeHud) {
		if (changeHud) {
    		if (!hud.setHotBarToItem(ItemData.get(itemIndex)))
    			hud.openItemPanelToItem(ItemData.get(itemIndex));
		}
		this.itemIndex = itemIndex;
	}
	
	public void setItemGroupIndex(int itemGroupIndex) {
		this.itemGroupIndex = itemGroupIndex;
	}
	
	public void setToolIndex(int toolIndex) {
		if (toolIndex != this.toolIndex) {
			getTool().onFinish();
			this.toolIndex = toolIndex;
			getTool().onStart();
		}
	}
	
	public void setCursorBox(Rectangle rect) {
		cursorBox = new Rectangle(rect);
	}
	
	private void updateControls() {
		if (!Keyboard.control.down()) {
    		if (Keyboard.left.down())
    			viewPosition.x -= VIEW_PAN_SPEED;
    		if (Keyboard.right.down())
    			viewPosition.x += VIEW_PAN_SPEED;
    		if (Keyboard.up.down())
    			viewPosition.y -= VIEW_PAN_SPEED;
    		if (Keyboard.down.down())
    			viewPosition.y += VIEW_PAN_SPEED;
		}
	}
	
	private void updateMouseActions() {
		// TODO
	}
	
	private void updateTools() {
		cursorBox = null;
		
		for (int i = 0; i < tools.length; i++) {
			if (tools[i].isHotKeyPressed())
				setToolIndex(i);
		}
		
		if (!hud.isBusy())
			Mouse.setCursor(getTool().getCursor());
		
		getTool().update();
		
		if (toolIndex == 0)
			cursorBox = new Rectangle(cursor, ItemData.get(itemIndex).getSize());
	}
	
	private void drawBackdrop(Graphics g) {
		Sheet sheet = backdropSheets[backdropIndex];
		Point size  = sheet.getSpriteSize();
		
		// Draw the background:
		for (int x = -viewPosition.x % size.x; x < runner.getViewWidth(); x += size.x) {
			for (int y = -viewPosition.y % size.y; y < runner.getViewHeight(); y += size.y) {
				backdropSheets[backdropIndex].drawSprite(g, x, y, 0, 0);
			}
		}
	}
	
	private void drawGrid(Graphics g) {
		// TODO; This method is very slow
//		g.setColor(Color.WHITE);
//		for (int x = -viewPosition.x % 16; x < runner.getViewWidth(); x += 16) {
//			for (int y = -viewPosition.y % 16; y < runner.getViewHeight(); y += 16) {
//				g.drawLine(x, 0, x, runner.getViewHeight());
//				g.drawLine(0, y, runner.getViewWidth(), y);
//			}
//		}
	}
	
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		drawBackdrop(g);
		
		Point v = viewPosition;
		Point d = new Point(world.getWidth() * 16, world.getHeight() * 16);
		Point s = new Point(runner.getViewWidth(), runner.getViewHeight());
		
		world.draw(g);
		if (showGrid)
			drawGrid(g);

		g.setColor(Color.BLACK);
		if (v.x < 0)
			g.fillRect(0, 0, -v.x, s.y);
		if (v.y < 0)
			g.fillRect(0, 0, s.x, -v.y);
		if (v.x > d.x - s.x)
			g.fillRect(d.x - v.x, 0, s.x - (d.x - v.x), s.y);
		if (v.y > d.y - s.y)
			g.fillRect(0, d.y - v.y, s.x, s.y - (d.y - v.y));
		
		getTool().draw(g);
		hud.draw(g);
	}
}
