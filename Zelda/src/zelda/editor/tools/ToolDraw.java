package zelda.editor.tools;

import java.awt.event.KeyEvent;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.editor.Editor;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.game.world.World;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.main.Hotkey;
import zelda.main.Keyboard;


public class ToolDraw extends EditorTool {



	// ================== CONSTRUCTORS ================== //

	public ToolDraw(Editor editor) {
		super(editor, "Draw Tool", new Hotkey(KeyEvent.VK_Q));


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

					// Create Tile
					if (panel.mouseLeftDown) {
						editor.madeChange();
						editor.getFocusedSet().createTile(frame, pos);
					}

					// Erase tile
					else if (panel.mouseRightDown) {
						editor.madeChange();
						editor.getFocusedSet().eraseTile(frame, pos);
					}

					// Sample Tile
					else if (panel.mouseMiddleDown) {
						if (Keyboard.control.down()) {
							editor.setSelected(frame);
						}
						else {
							GridTile t = frame.getGridTile(tileLoc);
							editor.setSelected(t);
							if (t != null) {
								editor.setFocusedSet(editor.tileset);
								editor.tileset.setCursor(t.getSourcePosition());
							}

							for (ObjectTile tile : frame.getObjectTiles()) {
								Rectangle r = tile.getRect();
								if (r.contains(pos)) {
									editor.setFocusedSet(editor.objectset);
									editor.objectset.setCursor(tile
											.getSourcePosition());
									editor.setSelected(tile.getEntityData());
									break;
								}
							}
						}
					}

				}
			}
		}
	}

	@Override
	public void draw() {

	}
}
