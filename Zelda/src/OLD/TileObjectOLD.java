package OLD;

import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class TileObjectOLD extends AbstractTileOLD {
	private SpriteSheetOLD sheet;
	private Point sourcePositon;
	private FrameObject object;
	private ObjectDataOLD data;
	private boolean hidden;



	// ================== CONSTRUCTORS ================== //

	public TileObjectOLD(Frame frame, int x, int y, SpriteSheetOLD sheet,
			int sx, int sy) {
		super(frame, x, y);

		this.sheet = sheet;
		this.sourcePositon = new Point(sx, sy);
		this.object = null;
		this.hidden = false;
	}



	// =================== ACCESSORS =================== //

	@Override
	public ObjectDataOLD getData() {
		return data;
	}



	// ==================== MUTATORS ==================== //

	public void setData(ObjectDataOLD data) {
		this.data = data;
	}



	// =============== INHERITED METHODS =============== //

	@Override
	public void onEnterFrame() {
		// Create the Object.
		hidden = true;
		// object = data.getObjectTemplate().newObject();
		// object.initialize(null, new Point(position.x * 16, position.y * 16));
		// getControl().addEntity(object);
	}

	@Override
	public void onLeaveFrame() {
		// Destroy the Object.
		if (object != null) {
			object.destroy();
			object = null;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(int dx, int dy) {
		if (!hidden) {
			SpriteOLD spr = sheet.getSprite(sourcePositon);
			Draw.drawSprite(spr, (position.x * 16) + dx, (position.y * 16) + dy);
		}
	}
}
