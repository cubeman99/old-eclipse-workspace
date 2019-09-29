package zelda.editor.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.editor.Editor;
import zelda.editor.PanelFrameDisplay;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.game.world.tile.GridTile;
import zelda.main.Hotkey;
import zelda.main.Keyboard;
import zelda.main.Mouse;


public class ToolSelection extends EditorTool {
	private Rectangle selectionBox;
	private Selection selection;
	private boolean dragging;
	private Point dragPos;
	private float selectionStrokeOffset;
	private boolean movingSelection;
	private Point moveSelectionPoint;



	// ================== CONSTRUCTORS ================== //

	public ToolSelection(Editor editor) {
		super(editor, "Tool Selection", new Hotkey(KeyEvent.VK_F));

		selectionBox = null;
		selection = null;
		dragging = false;
		dragPos = null;
		selectionStrokeOffset = 0;
		movingSelection = false;
		moveSelectionPoint = null;
	}



	// =================== ACCESSORS =================== //

	private Rectangle getDragRect() {
		Point corner = new Point(dragPos);
		Point ms = editor.panelFrameDisplay.getMousePos(16);

		if (ms.x < dragPos.x)
			corner.x = ms.x;
		if (ms.y < dragPos.y)
			corner.y = ms.y;

		Point size = ms.minus(dragPos);
		size.x = Math.abs(size.x) + 1;
		size.y = Math.abs(size.y) + 1;

		Level level = editor.getWorld().getCurrentLevel();
		Rectangle maxRect = new Rectangle(new Point(), level.getFrameSize()
				.times(level.getSize()));
		return new Rectangle(corner, size).crop(maxRect);
	}



	// ==================== MUTATORS ==================== //

	public void createSelection() {
		World world = editor.getWorld();
		Level level = world.getCurrentLevel();
		Point size = level.getFrameSize();
		selection = new Selection(selectionBox.size);

		for (int x = 0; x < selectionBox.getWidth(); x++) {
			for (int y = 0; y < selectionBox.getHeight(); y++) {
				Point frameLoc = selectionBox.corner.plus(x, y).dividedBy(size);
				Point tileLoc = selectionBox.corner.plus(x, y).mod(size);

				if (level.frameExists(frameLoc)) {
					Frame frame = level.getFrame(frameLoc);
					GridTile t = frame.getGridTile(tileLoc);
					selection.setGridTile(x, y, t);
					editor.tileset.eraseTile(frame, tileLoc.scaledBy(16));
				}
			}
		}
	}

	public void placeSelection(boolean clone) {
		if (selection == null)
			return;

		Level level = editor.getWorld().getCurrentLevel();
		Point size = level.getFrameSize();

		for (int x = 0; x < selection.getSize().x; x++) {
			for (int y = 0; y < selection.getSize().y; y++) {
				Point loc = selectionBox.corner.plus(x, y);
				Point frameLoc = loc.dividedBy(size);
				Point tileLoc = loc.mod(size);

				if (level.frameExists(frameLoc)) {
					Frame frame = level.getFrame(frameLoc);
					GridTile t = selection.getGridTile(x, y);
					if (clone)
						t = t.clone();
					t.getLocation().set(tileLoc);
					frame.setGridTile(t);
				}
			}
		}

		if (!clone)
			selection = null;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public boolean isBusy() {
		return (movingSelection || dragging);
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onEnd() {
		dragging = false;
		movingSelection = false;

		if (selection != null) {
			placeSelection(false);
		}
		if (selectionBox != null) {
			selectionBox = null;
		}
	}

	@Override
	public void update() {
		PanelFrameDisplay panel = editor.panelFrameDisplay;
		Point ms = panel.getMousePos(16);
		Level level = editor.getWorld().getCurrentLevel();


		if (movingSelection) {
			selectionBox.corner.set(ms.minus(moveSelectionPoint));

			if (!panel.mouseLeftDown) {
				movingSelection = false;
			}
		}
		else if (dragging) {
			selectionBox = getDragRect();

			if (!panel.mouseLeftDown) {
				dragging = false;
				createSelection();
			}
		}
		else {
			if (selection != null) {
				if (Keyboard.delete.pressed()) {
					selection = null;
					selectionBox = null;
				}
				else if (Keyboard.escape.pressed()) {
					placeSelection(false);
					selection = null;
					selectionBox = null;
				}
			}
			if (panel.containsMouse() && Mouse.left.pressed()) {
				if (selection != null && selectionBox.contains(ms)) {
					movingSelection = true;
					moveSelectionPoint = new Point(
							ms.minus(selectionBox.corner));
					if (Keyboard.control.down())
						placeSelection(true);
				}
				else if (level.absoluteFrameExists(ms)
						&& level.gridTileExists(ms)) {
					dragging = true;
					dragPos = new Point(ms);
					placeSelection(false);
				}
			}
		}
	}

	@Override
	public void draw() {
		Draw.setViewPosition(0, 0);

		if (selection != null) {
			selectionStrokeOffset += 0.25;
			for (int x = 0; x < selection.getSize().x; x++) {
				for (int y = 0; y < selection.getSize().y; y++) {
					GridTile t = selection.getGridTile(x, y);
					t.getLocation().set(selectionBox.corner.plus(x, y));
					t.draw();
				}
			}
		}

		if (selectionBox != null) {
			Graphics2D g = Draw.getGraphics();
			g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, new float[] {6},
					selectionStrokeOffset));

			Draw.setColor(Color.BLACK);
			Draw.drawRect(selectionBox.scaledBy(Settings.TS));
			Draw.setColor(Color.WHITE);
			Draw.drawRect(selectionBox.scaledBy(Settings.TS).plus(
					new Point(-1, -1)));
		}
	}
}
