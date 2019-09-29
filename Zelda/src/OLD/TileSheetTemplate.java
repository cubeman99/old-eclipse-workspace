package OLD;

import java.awt.Color;
import java.awt.Graphics;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.main.Mouse;


public abstract class TileSheetTemplate {
	protected DataOLD[][] data;
	protected SpriteSheetOLD sheet;
	protected Point size;
	protected Point cursorPos;
	protected Panel panel;
	protected int tileType;



	// ================== CONSTRUCTORS ================== //

	public TileSheetTemplate(String sheetName) {
		this.sheet = new SpriteSheetOLD(sheetName, Settings.TS, Settings.TS);
		this.size = new Point(sheet.getSize());
		this.data = new DataOLD[size.x][size.y];
		this.cursorPos = new Point(0, 0);
		this.tileType = 0;
	}



	// =================== ACCESSORS =================== //

	public SpriteSheetOLD getSheet() {
		return sheet;
	}

	public Panel getPanel() {
		return panel;
	}

	public Point getSize() {
		return size;
	}

	public int getTileType() {
		return tileType;
	}

	public AbstractTileOLD createTile(Frame frame, Point pos) {
		return createTile(frame, pos, cursorPos);
	}



	// ==================== MUTATORS ==================== //

	public void initialize(Panel panel) {
		this.panel = panel;
	}

	public void setCursorPos(Point cursorPos) {
		this.cursorPos = new Point(cursorPos);
	}

	public void setData(DataOLD dt) {
		data[dt.sheetSourcePos.x][dt.sheetSourcePos.y] = dt;
	}

	public boolean update() {
		Point ms = panel.getMousePos(16);
		Rectangle r = new Rectangle(0, 0, size.x, size.y);
		boolean selected = false;

		if (panel.containsMouse() && r.contains(ms)) {
			if (Mouse.left.pressed()) {
				cursorPos.set(ms);
				selected = true;
			}
		}

		return selected;
	}

	public void draw() {
		Graphics g = panel.getGraphics();

		g.drawImage(sheet.getImage(), 0, 0, null);
		g.setColor(Color.WHITE);
		g.drawRect((16 * cursorPos.x) - 1, (16 * cursorPos.y) - 1, 17, 17);
		g.setColor(Color.BLACK);
		g.drawRect((16 * cursorPos.x) - 2, (16 * cursorPos.y) - 2, 19, 19);
	}



	// =============== ABSTRACT METHODS =============== //

	public abstract AbstractTileOLD createTile(Frame frame, Point pos,
			Point sourcePos);

	public abstract AbstractTileOLD createDefaultTile(Frame frame, Point pos);
}
