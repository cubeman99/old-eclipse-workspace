package zelda.game.world.tile;

import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.graphics.TileAnimations;
import zelda.common.graphics.TileAnimations.FrameGroup;
import zelda.common.properties.Properties;
import zelda.common.util.GMath;
import zelda.editor.tileSheet.TilesetTemplate;
import zelda.game.entity.CollisionModel;
import zelda.game.world.Frame;
import zelda.game.zone.ZoneSheet;


public class GridTile extends AbstractTile {
	private Point location;
	private Sprite sprite;
	private Animation animation;
	private boolean enabled;
	private CollisionModel collisionModel;
	private ZoneSheet sheet;
	private boolean dug;
	private TilesetTemplate tilesetTemplate;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public GridTile(Frame frame, Point loc, Point sourcePos, Properties p) {
		super(frame, sourcePos, p);
		
		// properties.define("ground", false);
		// properties.define("hole", false);
		// properties.define("water", false);
		// properties.define("ocean", false);
		// properties.define("lava", false);
		// properties.define("waterfall", false);
		// properties.define("puddle", false);
		// properties.define("ice", false);
		// properties.define("ledge", false);
		// properties.define("ladder", false);
		// properties.define("stairs", false);
		// properties.define("solid", false);
		// properties.define("", false);
		
		// properties.define("warp_level", "");
		// properties.define("warp_point", "");
		// properties.define("warp_entrance_pos", "");

		this.location        = new Point(loc);
		this.animation       = new Animation(sourcePos.x, sourcePos.y);
		this.enabled         = true;
		this.collisionModel  = null;
		this.sheet           = Resources.zones.areaSheet;
		this.dug             = false;
		this.sprite          = new Sprite(getImageSheet(), animation);
		this.tilesetTemplate = null;
	}



	// =================== ACCESSORS =================== //

	public Point getLocation() {
		return location;
	}

	public Vector getPosition() {
		return new Vector(location).scale(Settings.TS);
	}

	public Vector getCenter() {
		return getPosition().add(8, 8);
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isCoverable() {
		return (!isSolid() && properties.getBoolean("coverable", true) && !isStairs() && !isLadder());
	}
	
	public int getLedgeDir() {
		return properties.getInt("ledge_dir", 3);
	}
	
	public boolean isLedge() {
		return properties.getBoolean("ledge", false);
	}
	
	public boolean isLedge(int dir) {
		return (isLedge() && getLedgeDir() == dir);
	}
	
	public boolean isStairs() {
		return properties.getBoolean("stairs", false);
	}
	
	public boolean isLadder() {
		return properties.getBoolean("ladder", false);
	}

	public boolean isIce() {
		return properties.getBoolean("ice", false);
	}
	
	public boolean isSurface() {
		return (!isHole() && !isWater() && !isLava());
	}

	public boolean isHole() {
		return properties.getBoolean("hole", false);
	}

	public boolean isLava() {
		return properties.getBoolean("lava", false);
	}

	public boolean isWater() {
		return properties.getBoolean("water", false);
	}

	public boolean isSolid() {
		return properties.getBoolean("solid", false);
	}

	public boolean isHalfSolid() {
		return properties.getBoolean("half_solid", false);
	}

	public CollisionModel getCollisionModel() {
		return collisionModel;
	}

	public TilesetTemplate getTilesetTemplate() {
		return tilesetTemplate;
	}

	public Animation getAnimation() {
		return animation;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Point getSourcePosition() {
		return sourcePosition;
	}

	public ImageSheet getImageSheet() {
		return sheet.getImageSheet(frame);
	}



	// ==================== MUTATORS ==================== //

	public boolean dig() {
		if (!dug && properties.getBoolean("ground", false)) {
			// TODO: Collectables
			dug = true;
			sprite.setSheet(Resources.zones.areaSheet.getImageSheet(frame));
			sprite.newAnimation(new Animation(1, 3));
			return true;
		}
		return false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setCollisionModel(CollisionModel collisionModel) {
		this.collisionModel = collisionModel;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
		sprite.newAnimation(animation);
	}

	public void setSheet(ZoneSheet sheet) {
		this.sheet = sheet;
		sprite.setSheet(getImageSheet());
	}

	public void setSourcePosition(Point sourcePosition) {
		this.sourcePosition.set(sourcePosition);
	}

	public void setTilesetTemplate(TilesetTemplate tilesetTemplate) {
		this.tilesetTemplate = tilesetTemplate;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public GridTile clone() {
		GridTile t = getTilesetTemplate().createTile(frame, sourcePosition,
				location);
		t.getProperties().set(properties);
		return t;
	}
	
	@Override
	public void onEnterFrame() {
		enabled = true;
		dug     = false;
		sprite  = new Sprite(getImageSheet(), animation);
		
		// TODO: Synchronize Animations.
		
		if (properties.getBoolean("lava", false)) {
			FrameGroup[] groups = new FrameGroup[4];
			for (int i = 0; i < groups.length; i++) {
				int index = GMath.random.nextInt(TileAnimations.LAVA.length);
				groups[i] = TileAnimations.LAVA[index];
			}
			sprite.newAnimation(TileAnimations.fromGroups(groups));
		}
	}
	
	@Override
	public void onFrameBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaveFrame() {

	}

	@Override
	public void update() {
		if (sprite != null)
			sprite.update();
	}

	@Override
	public void draw() {
		if (sprite != null)
			Draw.drawSprite(sprite, location.scaledBy(Settings.TS));
	}
}
