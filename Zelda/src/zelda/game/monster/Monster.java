package zelda.game.monster;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.reactions.ReactionCause;
import zelda.common.reactions.ReactionEffect;
import zelda.common.reactions.ReactionList;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.EntityObject;
import zelda.game.entity.Unit;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.effect.EffectCling;
import zelda.game.entity.effect.EffectFallingObject;
import zelda.game.entity.effect.EffectGale;
import zelda.game.entity.effect.EffectMonsterExplosion;
import zelda.game.entity.projectile.Projectile;
import zelda.game.player.Player;
import zelda.game.player.items.Item;
import zelda.game.world.Frame;
import zelda.game.world.tile.FrameTileObject;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.main.Keyboard;


/**
 * Monster.
 * 
 * @author David Jordan
 */
public abstract class Monster extends Unit {
	public static final int TYPE_NONE    = -1;
	public static final int COLOR_RED    = 0;
	public static final int COLOR_BLUE   = 1;
	public static final int COLOR_GREEN  = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_ORANGE = 3;
	public static final int COLOR_GOLD   = 3;
	
	public static final int ITEM_ARROW     = 0;
	public static final int ITEM_ROCK      = 1;
	public static final int ITEM_BOOMERANG = 2;
	public static final int ITEM_SWORD     = 3;
	public static final int ITEM_SPEAR     = 4;

	protected static final int STUN_TIME = 400;
	protected static final int STUN_SHAKE_TIME = 340;

	protected boolean burning;
	protected boolean stunned;
	protected int stunTimer;
	protected ReactionList reactions;
	protected Properties properties;
	protected int contactDamage;
	protected boolean dug;
	


	// ================== CONSTRUCTORS ================== //

	public Monster() {
		super();

		hardCollisionBox = new CollisionBox(3, 5, 10, 10);
		softCollisionBox = new CollisionBox(2, 3, 12, 13);

		flickerDuration = 16;
		collideWithFrameBoundaries = true;
		sheetNormal  = Resources.SHEET_MONSTERS;
		sheetHurt    = Resources.SHEET_MONSTERS_HURT;
		sprite       = new Sprite(sheetNormal, 0, 1);
		slipSpeed    = 0.2;
		health.fill(3);
		
		soundDamage = Sounds.MONSTER_HURT;

		dug                = false;
		passable           = false;
		stunned            = false;
		stunTimer          = 0;
		contactDamage      = 1;
		collisionAutoDodge = false;
		
		properties = new Properties();
		properties.set("id", "0");
		// properties.set("fixed_spawn", false);
		// properties.set("respawn_every_frame", true);
		// properties.set("spawn_once", false);

		reactions = new ReactionList();
		setReaction(ReactionCause.NONE,           null);
		setReaction(ReactionCause.ROD_FIRE,       new BURN(1));
		setReaction(ReactionCause.FIRE,           new COMBO(new DESTROY_SOURCE(), new BURN(1)));
		setReaction(ReactionCause.EMBER_SEED,     new BURN(1));
		setReaction(ReactionCause.SCENT_SEED,     new SEED_REACTION(1, new DAMAGE(1)));
		setReaction(ReactionCause.PEGASUS_SEED,   new SEED_REACTION(2, new STUN()));
		setReaction(ReactionCause.GALE_SEED,      new GALE());
		setReaction(ReactionCause.MYSTERY_SEED,   new MYSTERY_SEED());
		setReaction(ReactionCause.ARROW,          new DAMAGE(1));
		setReaction(ReactionCause.SWORD_BEAM,     new DAMAGE(1));
		setReaction(ReactionCause.SWORD,          new DAMAGE(1, 2, 3));
		setReaction(ReactionCause.BIGGORON_SWORD, new DAMAGE(3));
		setReaction(ReactionCause.BOMB,           new DAMAGE(1));
		setReaction(ReactionCause.BOOMERANG,      new STUN());
		setReaction(ReactionCause.SHIELD,         new BUMP(true));
		setReaction(ReactionCause.SHOVEL,         new BUMP());
		setReaction(ReactionCause.SWITCH_HOOK,    new SWITCH());
		setReaction(ReactionCause.MINE_CART,      new DISSAPEAR_KILL()); // TODO
		setReaction(ReactionCause.OBJECT_HIT,     new DAMAGE(1)); // TODO
		setReaction(ReactionCause.BRACELET,       null);
		setReaction(ReactionCause.TALK,           null);

		setReaction(ReactionCause.SWORD_HIT_SHIELD, new COMBO(
				new EFFECT_CLING(), new BUMP(true)));
		setReaction(ReactionCause.BIGGORON_SWORD_HIT_SHIELD, new COMBO(
				new EFFECT_CLING(), new BUMP()));
		setReaction(ReactionCause.SHIELD_HIT_SHIELD, new COMBO(
				new EFFECT_CLING(), new BUMP(true)));
	}



	// =============== ABSTRACT METHODS =============== //

	public abstract void updateAI();



	// =================== ACCESSORS =================== //

	public Monster getThis() {
		return this;
	}

	public Point getRandomLocation() {
		ArrayList<Point> possibleLocations = new ArrayList<Point>();
		if (frame == null)
			return null;

		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				if (!Collision.placeMeetingSolid(hardCollidable, new Vector(x,
						y).scale(16)))
					possibleLocations.add(new Point(x, y));
			}
		}

		if (possibleLocations.size() == 0)
			return null;

		return possibleLocations.get(GMath.random.nextInt(possibleLocations
				.size()));
	}

	public double getDistanceToPlayer() {
		return getCenter().distanceTo(game.getPlayer().getCenter());
	}
	
	public boolean placeColliding() {
		ArrayList<GridTile> tiles = Collision.getTilesMeeting(hardCollidable,
				position.plus(velocity), CollisionBox.TILE_BOX);
		for (int i = 0; i < tiles.size(); i++) {
			if (!tiles.get(i).isSurface()) {
				return true;
			}
		}
		return (placeOutsideOfFrame(softCollidable, position.plus(velocity))
			|| !Collision.canDodgeCollisions(this, position));
	}
	
	public boolean placeColliding(Collidable c, Vector position) {
		ArrayList<GridTile> tiles = Collision.getTilesMeeting(c,
				position, CollisionBox.TILE_BOX);
		for (int i = 0; i < tiles.size(); i++) {
			if (!tiles.get(i).isSurface()) {
				return true;
			}
		}
		return (placeOutsideOfFrame(softCollidable, position)
			|| Collision.placeMeetingSolid(c, position));
	}



	// ==================== MUTATORS ==================== //

	public void triggerReaction(int reactionCuase, Item item, Vector sourcePos) {
		triggerReaction(reactionCuase, item.getLevel(), item.getPlayer(),
				sourcePos);
	}

	public void triggerReaction(int reactionCuase, Item item) {
		triggerReaction(reactionCuase, item.getLevel(), item.getPlayer(), item
				.getPlayer().getCenter());
	}

	public void triggerReaction(int reactionCuase, int level, Entity source,
			Vector sourcePos) {
		if (reactions.reactionEffects[reactionCuase] != null && !passable) {
			reactions.reactionEffects[reactionCuase].trigger(reactionCuase,
					level, source, sourcePos);
		}
	}

	protected void setReaction(int cause, ReactionEffect effect) {
		reactions.reactionEffects[cause] = effect;
	}

	public void stun() {
		stunned = true;
		stunTimer = 0;
		velocity.zero();
	}

	public void burn(int damage) {
		Effect e = new Effect(new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_MONSTER_BURN, false), position.minus(0,
				zPosition));
		e.setDepth(getDepth() - 1);
		game.addEntity(e);

		if (zVelocity > 0)
			zVelocity = 0;

		health.take(damage);
		if (bumpOverride(null))
			burning = true;
		bumpTime = 59;
	}

	protected void endBurn() {
		burning = false;
		if (health.empty())
			die();
	}

	protected void hitPlayer() {
		game.getPlayer().damage(contactDamage, getCenter());
	}

	protected void checkPlayerCollision() {
		Player player = game.getPlayer();
		
		if (!isNearZ(player))
			return;

		if (Collision.isTouching(softCollidable, player)
				|| Collision.isTouching(shieldCollidable, player)) {
			hitPlayer();
		}
	}



	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void endBump() {
		super.endBump();
		if (burning) {
			endBurn();
		}
	}

	@Override
	public void die() {
		game.addEntity(new EffectMonsterExplosion(
				game.getPlayer().getPurse().dropsDefault, getCenter()));
		properties.script("event_die", this, frame);
		if (objectData != null)
			objectData.getSource().getProperties().set("enabled", false);
		Sounds.MONSTER_DIE.play();
		destroy();
	}
	
	@Override
	protected void onFallInHole(EffectFallingObject e) {
		game.addEntity(new EffectFallingObject(getCenter()));
		Sounds.EFFECT_FALL.play();
		destroy();
	}
	
	@Override
	protected void onFallInLava() {
		game.addEntity(new Effect(
				Resources.SPRITE_EFFECT_SPLASH_LAVA, getCenter()));
		Sounds.PLAYER_WADE.play();
		destroy();
	}
	
	@Override
	protected void onFallInWater() {
		game.addEntity(new Effect(Resources.SPRITE_EFFECT_SPLASH_WATER,
				getCenter().plus(0, 4)));
		Sounds.PLAYER_WADE.play();
		destroy();
	}
	
	@Override
	protected void performCollisions() {
		if (collideWithWorld)
			performSolidCollisions();
		if (collideWithFrameBoundaries)
			performBoundaryCollisions(softCollidable);
	}

	@Override
	public void update() {
		super.update();
		Player player = game.getPlayer();

		if (slipping) {
			sprite.setSpeed(1.5);
			velocity.zero();
			controllable   = false;
			hurtFlickering = false;
			bumped         = false;
		}
		else {
			if (isNearZ(player)) {
				if (player.itemSword.isTouching(shieldCollidable) && !hurt
						&& !bumped && !stunned) {
					triggerReaction(ReactionCause.SWORD_HIT_SHIELD,
							player.itemSword);
					player.itemSword.onHitMonster();
				}
				else if (player.itemSword.isTouching(this) && !hurt && !bumped) {
					triggerReaction(ReactionCause.SWORD, player.itemSword);
					player.itemSword.onHitMonster();
				}

				else if (player.itemBiggoronSword.isTouching(shieldCollidable)
						&& !hurt && !bumped && !stunned) {
					triggerReaction(ReactionCause.BIGGORON_SWORD_HIT_SHIELD,
							player.itemBiggoronSword);
				}
				else if (player.itemBiggoronSword.isTouching(this) && !hurt
						&& !bumped) {
					triggerReaction(ReactionCause.BIGGORON_SWORD,
							player.itemBiggoronSword);
				}

				else if (player.itemShield.isBlocking() && !hurt && !bumped) {
					if (Collision.isTouching(shieldCollidable,
							player.getShieldCollidable())
							&& !stunned) {
						triggerReaction(ReactionCause.SHIELD_HIT_SHIELD,
								player.itemShield);
					}
					else if (Collision.isTouching(softCollidable,
							player.getShieldCollidable())) {
						triggerReaction(ReactionCause.SHIELD, player.itemShield);
					}
				}

				else if (Collision.isTouching(softCollidable, player)) {
					// Talk Action.
					if (Keyboard.a.pressed()) {
						triggerReaction(ReactionCause.TALK, 0, player,
								player.getCenter());
					}
				}
			}

			if (stunned) {
				stunTimer++;
				setDepth(-10);
				if (stunTimer > STUN_TIME) {
					stunned = false;
				}
			}
			else if (controllable) {
				updateAI();

				// Collide with player.
				if (!passable && !player.isPassable())
					checkPlayerCollision();
			}
		}
	}

	public void drawOffset(int offset) {
		Draw.drawSprite(sprite, position.minus(offset, zPosition));
	}

	protected int getDrawOffset() {
		if (stunned && (stunTimer < 4 || (stunTimer > STUN_SHAKE_TIME && (stunTimer / 4) % 2 == 0)))
			return 1;
		return 0;
	}

	@Override
	public void draw() {
		if (sprite != null)
			drawOffset(getDrawOffset());
	}

	@Override
	protected void onLand() {
		super.onLand();
		if (dug) {
			dug = false;
			passable = false;
		}
	}

	@Override
	public void digUp(Vector pos, int dir) {
		super.digUp(pos, dir);
		dug = true;
		passable = true;
		position.sub(8, 14);
		velocity.setLength(0.8);
	}

	@Override
	protected void updateSprite() {
		if (sprite != null && !stunned)
			super.updateSprite();
	}
	
	@Override
	public void begin() {
		super.begin();
		ObjectTile t = null;
		if (objectData.getSource() instanceof ObjectTile)
			t = (ObjectTile) objectData.getSource();
		enterFrame(t);
	}
	
	@Override
	public void setup() {
		super.setup();
		// TODO
	}
	
	public void enterFrame(ObjectTile t) {
		if (t != null) {
    		frame = t.getFrame();
    		game  = frame.getGame();
    		position.set(t.getPosition());
    		properties.set(t.getProperties());
		}
		
		/*
		 * if (!properties.getBoolean("fixed_spawn", false)) { // TODO: distance
		 * from player Point p = getRandomLocation(); if (p != null)
		 * position.set(p.scale(16)); }
		 */
	}

	@Override
	public void onChangeProperty(Property p) {
		// TODO
	}

	@Override
	public Properties getProperties() {
		return properties;
	}


	// ================ REACTIONS ================ //

	public final class COMBO implements ReactionEffect {
		private ReactionEffect[] reactions;

		public COMBO(ReactionEffect... reactions) {
			this.reactions = reactions;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			for (int i = 0; i < reactions.length; i++)
				reactions[i].trigger(reactionCuase, level, source, sourcePos);
		}
	}
	public final class DAMAGE implements ReactionEffect {
		private int[] damage;
		private boolean bumps;

		public DAMAGE(int... damage) {
			this(true, damage);
		}

		public DAMAGE(boolean bumps, int... damage) {
			this.damage = new int[3];
			this.bumps = bumps;
			int dmg = 1;

			for (int i = 0; i < this.damage.length; i++) {
				if (i < damage.length)
					dmg = damage[i];
				this.damage[i] = dmg;
			}
		}

		@Override
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			damage(damage[level], (bumps ? sourcePos : null));
		}
	}
	public final class IF_LEVEL implements ReactionEffect {
		private int levelMin;
		private ReactionEffect reaction;

		public IF_LEVEL(int levelMin, ReactionEffect reaction) {
			this.levelMin = levelMin;
			this.reaction = reaction;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (level >= levelMin)
				reaction.trigger(reactionCuase, level, source, sourcePos);
		}
	}
	public final class BUMP implements ReactionEffect {
		private boolean bumpSource;

		public BUMP() {
			this(false);
		}

		public BUMP(boolean bumpSource) {
			this.bumpSource = bumpSource;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			bump(sourcePos);
			if (bumpSource)
				((Unit) source).bump(getCenter());
		}
	}
	public final class BUMP_OVERRIDE implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			bumpOverride(sourcePos);
		}
	}
	public final class BURN implements ReactionEffect {
		private int damage;

		public BURN(int damage) {
			this.damage = damage;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			burn(damage);
		}
	}
	public final class PLAYER_DAMAGE implements ReactionEffect {
		private int damage;

		public PLAYER_DAMAGE(int damage) {
			this.damage = damage;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			game.getPlayer().damage(damage, getCenter());
		}
	}
	public final class PLAYER_BUMP implements ReactionEffect {
		private ReactionEffect reaction;

		public PLAYER_BUMP() {
			this(null);
		}

		public PLAYER_BUMP(ReactionEffect reaction) {
			this.reaction = reaction;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (game.getPlayer().bump(getCenter()) && reaction != null)
				reaction.trigger(reactionCuase, level, source, sourcePos);
		}
	}
	public final class DESTROY_SOURCE implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (source != null)
				source.destroy();
		}
	}
	public final class KILL implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			die();
		}
	}
	public final class DISSAPEAR_KILL implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			destroy();
			game.addEntity(new EffectMonsterExplosion(
					game.getPlayer().getPurse().dropsDefault, getCenter()));
		}
	}
	public final class DISSAPEAR implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			destroy();
			game.addEntity(new Effect(new Sprite(
					Resources.SHEET_SPECIAL_EFFECTS,
					Animations.EFFECT_DISAPPEAR_POOF, false), position));
		}
	}
	public final class STUN implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			stun();
			Sounds.MONSTER_HURT.play();
		}
	}
	public final class PROJECTILE_CRASH implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (source instanceof Projectile)
				((Projectile) source).crash();
		}
	}
	public final class EFFECT_CLING implements ReactionEffect {
		private boolean light;

		public EFFECT_CLING() {
			this(false);
		}

		public EFFECT_CLING(boolean light) {
			this.light = light;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (sourcePos != null)
				game.addEntity(new EffectCling(sourcePos, light));
		}
	}
	public final class SEED_REACTION implements ReactionEffect {
		private ReactionEffect reaction;
		private int seedIndex;

		public SEED_REACTION(int seedIndex, ReactionEffect reaction) {
			this.seedIndex = seedIndex;
			this.reaction = reaction;
		}

		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			EntityObject obj = (EntityObject) source;
			reaction.trigger(reactionCuase, level, source, sourcePos);
			game.addEntity(new Effect(new Sprite(
					Resources.SHEET_SPECIAL_EFFECTS,
					Animations.EFFECT_SEEDS[seedIndex], false).setOrigin(8, 8),
					obj.getPosition().minus(0, obj.getZPosition())));
		}
	}
	public final class GALE implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			EntityObject obj = (EntityObject) source;
			Effect e = new EffectGale(sprite, obj.getPosition().minus(0,
					obj.getZPosition()));
			game.addEntity(e);
			destroy();
		}
	}
	public class MYSTERY_SEED implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			int index = GMath.random.nextInt(4);
			triggerReaction(ReactionCause.SEEDS[index], level, source,
					sourcePos);
		}
	}
	public final class SWITCH implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			game.getPlayer().itemSwitchHook.hookOntoMonster(getThis());
		}
	}
	public final class ELECTROCUTE implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			// TODO
		}
	}
	public final class PICKUP implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			// ObjectBush bush = new ObjectBush();
			// bush.initialize(frame, new Point(position));
			// game.getPlayer().itemBracelet.pickupObject(new
			// CarriedFrameObject(bush));
			// Monster m = getThis().clone();
			// m.setGame(game);
			// m.setFrame(frame);
			// game.getPlayer().itemBracelet.pickupObject(m);
			// destroy();
			// TODO
		}
	}
}
