package OLD;

import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.game.world.Frame;


public class TileOLD extends AbstractTileOLD {
	private SpriteSheetOLD sheet;
	private SpriteOLD sprite;
	private TileDataOLD data;
	private AnimationStripOLD animStrip;
	private AnimationOLD animation;
	private boolean enabled;
	private Point sheetSourcePos;
	private boolean dug;


	// ================== CONSTRUCTORS ================== //

	public TileOLD(Frame frame, int x, int y, TileDataOLD data) {
		super(frame, x, y);

		this.sheet = null;
		this.sprite = null;
		this.animation = null;
		this.animStrip = null;
		this.enabled = true;
		this.dug = false;

		setData(data);
	}

	public TileOLD(Frame frame, int x, int y, SpriteSheetOLD sheet, int sx,
			int sy) {
		this(frame, x, y, new TileDataOLD(sx, sy));

		this.sheet = sheet;
		this.sprite = sheet.getSprite(sx, sy);
	}



	// =================== ACCESSORS =================== //

	public SpriteOLD getSprite() {
		return sprite;
	}

	@Override
	public TileDataOLD getData() {
		return data;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public SpriteSheetOLD getSheet() {
		return sheet;
	}

	public boolean isDug() {
		return dug;
	}

	// public CollisionData getCollisionData() {
	// if (data.collisionModel == null ||
	// data.collisionModel.getNumCollisionBoxes() == 0)
	// return null;
	// return new CollisionData(data.collisionModel.getCollisionBox(0), new
	// Vector(position).scale(16));
	// }



	// ==================== MUTATORS ==================== //

	@Override
	public void onEnterFrame() {
		this.dug = false;
		this.sheetSourcePos.set(data.sheetSourcePos);
	}

	@Override
	public void onLeaveFrame() {

	}

	public void update() {
		if (enabled) {
			if (animation != null)
				animation.update();
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean dig() {
		if (dug || !data.ground || !enabled)
			return false;

		dug = true;
		setSheetSourcePos(new Point(3, 21));

		return true;
	}

	public void setData(TileDataOLD data) {
		this.data = data;
		this.sheetSourcePos = data.sheetSourcePos;

		if (data.isAnimated()) {
			animStrip = new AnimationStripOLD(data.animStrip);
			// animStrip.setSheet(new ZoneSheet(frame, ZoneSheet.ZONE_SHEET));
			animation = new AnimationOLD(animStrip);
		}
	}

	public void setSheetSourcePos(Point sheetSourcePos) {
		this.sheetSourcePos = new Point(sheetSourcePos);
	}

	/*
	 * public void setSheet(SpriteSheet sheet) { this.sheet = sheet;
	 * this.animStrip = data.animStrip; this.animStrip.setSheet(sheet);
	 * this.animation = new Animation(animStrip); }
	 */
	public void draw() {
		draw(0, 0);
	}

	@Override
	public void draw(int dx, int dy) {
		SpriteOLD sprite;

		if (animation != null)
			sprite = animation.getCurrentSprite();
		else
			sprite = sheet.getSprite(sheetSourcePos);

		if (sprite != null) {
			Draw.drawSprite(sprite, (position.x * 16) + dx, (position.y * 16)
					+ dy);
		}
	}
}
