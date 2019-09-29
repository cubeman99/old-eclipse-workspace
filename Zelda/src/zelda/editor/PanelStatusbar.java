package zelda.editor;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;


public class PanelStatusbar extends Panel {

	public PanelStatusbar(Editor editor) {
		super(editor);
	}

	@Override
	public void draw() {
		Frame frame = editor.panelFrameDisplay.getMouseFrame();

		Point frameLoc = null;
		Point tileLoc = null;

		if (frame != null) {
			frameLoc = frame.getLocation();
			GridTile t = editor.panelFrameDisplay.getMouseTile();
			if (t != null)
				tileLoc = t.getLocation();
		}

		Draw.drawText("Frame " + frameLoc, new Point(11, 8),
				Resources.FONT_TEXT, Color.BLACK);
		Draw.drawText("Tile " + tileLoc, new Point(11 + 128, 8),
				Resources.FONT_TEXT, Color.BLACK);
	}
}
