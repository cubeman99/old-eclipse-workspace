package zelda.game.entity.object;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Cloneable;
import zelda.common.util.Direction;
import zelda.common.util.Path;
import zelda.game.control.event.Event;
import zelda.game.control.event.EventQueue;
import zelda.game.control.event.TimedEvent;
import zelda.game.control.script.Function;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.game.entity.FrameEntity;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.projectile.Projectile;
import zelda.game.player.Player;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.game.zone.ZoneSheet;
import zelda.main.Sound;

/**
 * A FrameObject is an object created upon entering a frame and is recreated in
 * the same state every time upon entering the frame its associated ObjectTile
 * is located in. <br>
 * <br>
 * 
 * FrameObjects share certain properties such as whether they can be carried,
 * bombed, or burned. <br>
 * <br>
 * 
 * Some examples of FrameObjects include Rocks, Bushes, Signs, Pots, Owls,
 * Cacti, Grass, etc.
 * 
 * @author David Jordan
 */
public abstract class FrameObject extends FrameEntity implements Collidable, Cloneable
{
	protected ObjectTile objectTile;
	private GridTile underTile; // The tile beneath this object.
	protected Vector position;
	protected Vector velocity;
	protected boolean appearing;
	protected Path path;
	protected ImageSheet imageSheet;
	protected ZoneSheet zoneSheet;

	protected Properties properties;
	protected Sprite sprite;
	protected Sprite spriteEntity;
	protected Sprite spriteBreakEffect;
	protected CollisionBox collisionBox;
	protected int pushDelay;
	protected boolean poofsOnAppear;
	
	protected Sound soundBreak;
	protected Sound soundMove;
	
	/*
	protected boolean burnable; // Burnable by fire.
	protected boolean digable; // Digable by Shovel.
	protected boolean switchable; // Switchable by the Switch Hook.
	protected boolean switchStays; // Stays in the new position when switched
								   // (instead of braking).
	protected boolean swordable; // Destroyable by the Sword.
	protected int swordableLevel; // Sword level required to kill by sword.
	protected boolean bombable; // Can be blown up.
	protected boolean carriable; // Can be picked up and carried by Bracelet.
	protected boolean movable; // Can be moved.
	protected boolean magicBoomerangable; // Can be destroyed by the magic boomerang.
	*/
	

	// ================== CONSTRUCTORS ================== //
	
	public FrameObject() {
		imageSheet        = null;
		zoneSheet         = Resources.zones.areaSheet;
		sprite            = new Sprite(Resources.ZONESET_SUMMER, new Animation(0, 0));
		position          = new Vector();
		velocity          = new Vector();
		spriteBreakEffect = null;
		properties        = new Properties();
		pushDelay         = 20;
		appearing         = false;
		poofsOnAppear     = true;
		path              = null;
		collisionBox      = new CollisionBox(0, 0, 16, 16);
		soundBreak        = Sounds.OBJECT_BREAK;
		soundMove         = Sounds.OBJECT_MOVE;
		
		setSpriteSource(createSpriteSource());
	}



	// ========= ABSTRACT/UNIMPLEMENTED METHODS========== //

	@Override
	public abstract FrameObject clone();

	public abstract void initialize();

	public abstract Point createSpriteSource();

	public void onPreMove(int dir, MovingFrameObject obj) {}
	
	public void onMove(int dir, MovingFrameObject obj) {}
	
	public void onHitByProjectile(Projectile proj) {}

	public void onGrab() {}
	
	public void onPull() {}
	
	public void onPlayerTouch() {}
	
	

	// =================== ACCESSORS =================== //

	public int getPushDelay() {
		return pushDelay;
	}
	
	public Sound getSoundBreak() {
		return soundBreak;
	}
	
	public Sound getSoundMove() {
		return soundMove;
	}

	public boolean isBurnable() {
		return properties.getBoolean("burnable");
	}

	public boolean isDigable() {
		return properties.getBoolean("digable");
	}

	public boolean isCarriable() {
		return properties.getBoolean("carriable");
	}

	public boolean isBombable() {
		return properties.getBoolean("bombable");
	}

	public boolean isSwordable() {
		return properties.getBoolean("swordable");
	}

	public boolean isMagicBoomerangable() {
		return properties.getBoolean("boomerangable");
	}

	public boolean isMovable() {
		return properties.getBoolean("movable");
	}

	public boolean isSwitchable() {
		return properties.getBoolean("switchable");
	}

	public boolean breaksOnSwitch() {
		return !properties.getBoolean("switch_stays");
	}
	
	public int getSwordableLevel() {
		return properties.getInt("swordable_level");
	}
	
	public boolean isSurface() {
		return properties.getBoolean("surface");
	}
	
	public ImageSheet getImageSheet() {
		return imageSheet;
	}

	public ZoneSheet getZoneSheet() {
		return zoneSheet;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public Sprite getEntitySprite() {
		return spriteEntity;
	}

	public Vectangle getVect() {
		return new Vectangle(position.x, position.y, 16, 16);
	}

	public Vector getCenter() {
		return position.plus(8, 8);
	}

//	public Rectangle getRect() {
//		return new Rectangle(position.x, position.y, 16, 16);
//	}
	
	public Vector getVelocity() {
		return velocity;
	}

	public Point getLocation() {
		return new Point(position.scaledByInv(16).add(0.5, 0.5));
	}

	public Sprite getBreakEffectSprite() {
		return spriteBreakEffect;
	}
	
	public FrameObject getThis() {
		return this;
	}


	// ==================== MUTATORS ==================== //

	public void enterFrame(Frame frame, Point position, Properties properties) {
		setDepth(400);
		this.frame    = frame;
		this.game     = frame.getGame();
		this.position = new Vector(position);
		if (properties != null) {
			this.properties.set(properties);
			hidden = properties.getBoolean("hidden", hidden);
		}
		else
			properties = new Properties();
		
		collisionBox = new CollisionBox(CollisionBox.TILE_BOX);

		if (imageSheet != null)
			sprite.setSheet(imageSheet);
		else
			sprite.setSheet(getZoneSheet().getImageSheet(frame));

		spriteEntity = new Sprite(sprite);
		// spriteEntity.setOrigin(8, 14);

		GridTile gt = frame.getGridTile(getLocation());
		underTile = gt;
		if (gt != null)
			gt.setEnabled(false);
		
		if (!properties.isNull("path")) {
			path = new Path(properties.get("path"));
			path.start();
		}
		
		initialize();
	}
	
	public void snapToGrid() {
		position.set(getLocation().scaledBy(Settings.TS));
	}
	
	public void onAction(int dir) {

	}

	public CarriedFrameObject pickup() {
		properties.script("event_pickup", this, frame);
		createDrop();
		destroy();
		return new CarriedFrameObject(this);
	}

	public boolean dig() {
		if (isDigable()) {
			destroy();
			return true;
		}
		return false;
	}
	
	protected MovingFrameObject createMoveObject(int dir) {
		return new MovingFrameObject(this, dir);
	}
	
	public boolean canMove(int dir) {
		if (!isMovable())
			return false;
		if (properties.getBoolean("move_once", false)
			&& properties.getBoolean("moved", false))
			return false;
		return properties.nullOrEquals("move_dir", dir);
	}
	
	public boolean move(int dir) {
		if (!canMove(dir))
			return false;
		
		Point newLoc  = getLocation().plus(Direction.getDirPoint(dir));
		Vector newPos = new Vector(newLoc.scaledBy(16));
		GridTile t    = frame.getGridTile(newLoc);
		
		if (!Collision.placeMeetingSolid(this, new Vector(newPos))
				&& frame.contains(new Point(newPos).dividedBy(16))
				&& (t == null || t.isCoverable()))
		{
			MovingFrameObject obj = createMoveObject(dir);
			
			soundMove.play();
			game.addEntity(obj);
			onPreMove(dir, obj);
			position.set(newPos);
			snapToGrid();
			onMove(dir, obj);
			properties.set("moved", true);
			properties.set("moved_dir", dir);
			return true;
		}
		return false;
	}

	public void breakObject() {
		destroy();
		createBreakEffect();
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void enableUnderTile() {
		if (underTile != null)
			underTile.setEnabled(true);
	}

	public void burn() {
		destroy();
		createDrop();
	}

	public void sword() {
		destroy();
		createBreakEffect();
		createDrop();
	}

	public void bomb() {
		destroy();
		createBreakEffect();
		createDrop();
	}

	public void createDrop() {
		EntityObject e = game.getPlayer().getPurse().dropsDefault
				.createDropObject();
		if (e != null) {
			e.setPosition(getPosition().plus(8, 13));
			game.addEntity(e);
		}
	}

	protected void createBreakEffect() {
		if (spriteBreakEffect != null) {
			game.addEntity(new Effect(new Sprite(spriteBreakEffect),
					getPosition()));
			Sounds.play(soundBreak);
		}
	}

	protected void setBreakSprite(Animation anim) {
		setBreakSprite(Resources.SHEET_EFFECTS, anim);
	}

	protected void setBreakSprite(ImageSheet sheet, Animation anim) {
		spriteBreakEffect = new Sprite(sheet, anim);
		spriteBreakEffect.setLooped(false);
	}

	protected void setSpriteSource(Point sp) {
		sprite.newAnimation(new Animation(sp.x, sp.y));
	}

	protected void setSpriteSource(int sx, int sy) {
		sprite.newAnimation(new Animation(sx, sy));
	}

	protected void setSpriteEntitySource(int sx, int sy) {
		spriteEntity.newAnimation(new Animation(sx, sy));
	}
	
	public void begin(Frame frame, Point pos, Properties p) {
		super.begin();
		enterFrame(frame, pos, p);
	}



	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void begin() {
		super.begin();
		ObjectTile t = null;
		if (objectData.getSource() instanceof ObjectTile)
			t = (ObjectTile) objectData.getSource();
		
		enterFrame(t);
	}
	
//	@Override
	public final void enterFrame(ObjectTile t) {
		this.objectTile = t;
		enterFrame(t.getFrame(), t.getPosition(), t.getProperties());
	}
	
	@Override
	public void onDestroy() {
		enableUnderTile();
		if (!properties.getBoolean("respawns", true))
			objectTile.getProperties().set("enabled", false);
	}

	@Override
	public void update() {
		if (isDestroyed())
			return;

		GridTile t = frame.getGridTile(getLocation());
		if (underTile != t) {
			if (underTile != null)
				underTile.setEnabled(true);
			underTile = t;
			if (underTile != null)
				underTile.setEnabled(false);
		}

		Player player = game.getPlayer();

		if (isSwordable()
				&& player.checkSwordHitObject(new Point(position))
				&& (player.itemSword.getLevel() >= getSwordableLevel() || player.itemBiggoronSword
						.isEquipped())) {
			sword();
		}
		
		// Follow a path.
		velocity.zero();
		if (path != null) {
			Vector motion = path.nextMotion();
			if (motion != null) {
				velocity.set(motion);
    			if (Collision.placeMeetingSolid(this, position.plus(velocity))) {
    				path.onHit();
    			}
    			else
    				position.add(velocity);
			}
		}
	}

	@Override
	public void draw() {
		if (!isDestroyed())
			Draw.drawSprite(sprite, position);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		ImageSheet sheet = (imageSheet != null ? imageSheet : getZoneSheet()
				.getImageSheet(frame));
		Sprite spr = new Sprite(sheet, sprite.getAnimation());
		Draw.drawSprite(spr, pos);
	}
	
	@Override
	public void setup() {
		super.setup();
		properties = objectData.getProperties();
		
		objectData.addProperty("movable",          false);
		objectData.addProperty("swordable",        false);
		objectData.addProperty("carriable",        false);
		objectData.addProperty("burnable",         false);
		objectData.addProperty("switchable",       false);
		objectData.addProperty("switch_stays",     false);
		objectData.addProperty("bombable",         false);
		objectData.addProperty("digable",          false);
		objectData.addProperty("boomerangable",    false);
		objectData.addProperty("solid",            false);
		objectData.addProperty("surface",          false);
		
		objectData.addProperty("swordable_level");
		objectData.addProperty("move_once", false);
		objectData.addProperty("move_dir");
		objectData.addProperty("moved", false);
		objectData.addProperty("moved_dir");
		objectData.addProperty("path", "");
		
		objectData.addEvent("event_push", "Called when the player attempts to push the object.");
		objectData.addEvent("event_action", "", "Called when the player presses A on the object.");
		objectData.addEvent("event_action_front", "Called when the player presses A in front of the object.");
		objectData.addEvent("event_action_side", "Called when the player presses A from the side of the object.");
		objectData.addEvent("event_pickup", "Called when the player picks the object up.");
		objectData.addEvent("event_reveal_tile", "Called when the object reveals the tile beneath it.");

		objectData.addFunction(new Function("appear") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				if (hidden) {
    				properties.set("hidden", false);
    				if (!appearing) {
    					appearing = true;
    					
    					Sounds.EFFECT_POOF.play();
    					Event e = new Event() {
    						public void begin() {
    							super.begin();
    							getThis().hidden    = false;
    							getThis().appearing = false;
    							end();
    						}
    					};
    					Event eq = new EventQueue(new TimedEvent(16), e).begin(game);
    					game.addEntity(eq);
    					
    					if (collisionBox != null) {
    						Vectangle v = collisionBox.getVect(position);
    						for (double x = v.getX1(); x < v.getX2(); x += 16) {
    							for (double y = v.getY1(); y < v.getY2(); y += 16) {
    								game.addEntity(new Effect(new Sprite(
    										Resources.SHEET_SPECIAL_EFFECTS,
    										Animations.EFFECT_APPEAR_POOF,
    										false), new Vector(x, y)));
    							}
    						}
    					}
    				}
				}
				return "";
			}
		});
		
		objectData.addFunction(new Function("disappear") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				if (!hidden) {
    				properties.set("hidden", true);
    				hidden = true;
    				Sounds.EFFECT_POOF.play();
    				if (collisionBox != null) {
    					Vectangle v = collisionBox.getVect(position);
    					for (double x = v.getX1(); x < v.getX2(); x += 16) {
    						for (double y = v.getY1(); y < v.getY2(); y += 16) {
    							game.addEntity(new Effect(new Sprite(
    									Resources.SHEET_SPECIAL_EFFECTS,
    									Animations.EFFECT_APPEAR_POOF,
    									false), new Vector(x, y)));
    						}
    					}
    				}
				}
				return "";
			}
		});
		
		objectData.addFunction(new Function("show") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				properties.set("hidden", false);
				onChangeProperty(properties.getProperty("hidden"));
				return "";
			}
		});
		
		objectData.addFunction(new Function("hide") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				properties.set("hidden", true);
				onChangeProperty(properties.getProperty("hidden"));
				return "";
			}
		});
	}
	
	@Override
	public void onChangeProperty(Property p) {
		if (p.hasName("hidden"))
			hidden = p.getBoolean();
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public CollisionBox getCollisionBox() {
		if (hidden)
			return null;
		if (collisionBox != null)
			return collisionBox;
		return new CollisionBox(0, 0, 16, 16);
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public boolean isSolid() {
		if (hidden)
			return false;
		return properties.getBoolean("solid");
	}
}
