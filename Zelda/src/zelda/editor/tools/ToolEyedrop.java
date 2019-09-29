package zelda.editor.tools;

import java.awt.Color;
import java.awt.event.KeyEvent;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.editor.Editor;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.game.world.World;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.main.Hotkey;
import zelda.main.Mouse;


public class ToolEyedrop extends EditorTool {



	// ================== CONSTRUCTORS ================== //

	public ToolEyedrop(Editor editor) {
		super(editor, "Eyedrop Tool", new Hotkey(KeyEvent.VK_R));


	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public boolean isBusy() {
		return false;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		Panel panel = editor.panelFrameDisplay;

		if (panel.containsMouse()) {
			World world = editor.getWorld();
			Point frameSize = world.getCurrentLevel().getFrameSize();
			Point ms = panel.getMousePos(16);
			Point frameLoc = new Point(ms.x / frameSize.x, ms.y / frameSize.y);
			Point tileLoc = new Point(ms.x % frameSize.x, ms.y % frameSize.y);


			if (world.getCurrentLevel().frameExists(frameLoc)) {
				Frame frame = world.getCurrentLevel().getFrame(frameLoc);

				if (frame.contains(tileLoc)) {
					Point ms2 = panel.getMousePos();
					Point pos = new Point(ms2.x % (frameSize.x * 16), ms2.y
							% (frameSize.y * 16));

					if (Mouse.left.pressed()) {
						editor.toolbar.setToolIndex(0);

						GridTile t = frame.getGridTile(tileLoc);
						editor.setSelected(t);
						if (t != null) {
							editor.setFocusedSet(editor.tileset);
							editor.tileset.setCursor(t.getSourcePosition());
						}

						for (ObjectTile tile : frame.getObjectTiles()) {
							Rectangle r = new Rectangle(tile.getPosition(),
									new Point(16, 16));
							if (r.contains(pos)) {
								editor.setFocusedSet(editor.objectset);
								editor.objectset.setCursor(tile
										.getSourcePosition());
								editor.setSelected(tile);
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void draw() {
		Draw.setViewPosition(0, 0);
		Point ms = editor.panelFrameDisplay.getMousePos(Settings.TS);

		if (editor.getWorld().getCurrentLevel().gridTileExists(ms)) {
			Rectangle r = new Rectangle(ms, new Point(1, 1)).scale(16);

			Draw.setColor(Color.WHITE);
			Draw.drawRect(r.grow(1, 1));
			Draw.setColor(Color.BLACK);
			Draw.drawRect(r.grow(1, 1));
		}
	}
}
