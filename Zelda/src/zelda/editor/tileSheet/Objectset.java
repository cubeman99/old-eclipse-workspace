package zelda.editor.tileSheet;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.editor.Editor;
import zelda.editor.tileSheet.templates.TemplateObjects;
import zelda.game.entity.FrameEntity;
import zelda.game.world.Frame;
import zelda.game.world.tile.FrameTileObject;
import zelda.game.world.tile.ObjectTile;


public class Objectset extends AbstractSet {
	private ObjectsetTemplate template;



	// ================== CONSTRUCTORS ================== //

	public Objectset(Editor editor) {
		super(editor, "Objects");
		template = new TemplateObjects();
	}



	// =================== ACCESSORS =================== //

	public ObjectTile newTile(Frame frame, Point sourcePos, Point pos) {
		return template.createTile(frame, sourcePos, pos);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void createTile(Frame frame, Point pos) {
		Point loc = pos.dividedBy(Settings.TS);

		if (template.objectExists(cursor)) {
			ObjectTile t = newTile(frame, cursor, loc.scaledBy(Settings.TS));
			
			Rectangle r1 = t.getRect();
			for (ObjectTile tile : frame.getObjectTiles()) {
				Rectangle r2 = tile.getRect();
				if (r1.touches(r2))
					return;
			}

			frame.addObjectTile(t);
		}
	}

	@Override
	public void eraseTile(Frame frame, Point pos) {
		for (ObjectTile tile : frame.getObjectTiles()) {
			Rectangle r = tile.getRect();

			if (r.contains(pos)) {
				frame.removeObjectTile(tile);
				return;
			}
		}
	}

	@Override
	protected Point getSize() {
		return template.getSize();
	}

	@Override
	public void drawSet() {
		for (int x = 0; x < getSize().x; x++) {
			for (int y = 0; y < getSize().y; y++) {
				FrameEntity obj = template.getFrameObject(x, y);
				Point dp = new Point(x, y).scaledBy(17);

				if (obj != null) {
					obj.drawTileSprite(dp, editor.getWorld().getCurrentFrame());
				}
				else if (x == 0 && y == 0) {
					Draw.drawImage(Resources.SHEET_PLAYER, new Point(6, 0),
							new Vector(dp));
				}
				else {
					Draw.setColor(Color.LIGHT_GRAY);
					Draw.fillRect(new Rectangle(dp, new Point(16, 16)));
				}
			}
		}
	}
}
