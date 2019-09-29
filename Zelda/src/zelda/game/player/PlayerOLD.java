package zelda.game.player;

import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.Unit;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.overworld.ObjectGrass;
import zelda.game.player.items.Item;
import zelda.game.player.items.ItemBiggoronSword;
import zelda.game.player.items.ItemBombs;
import zelda.game.player.items.ItemBoomerang;
import zelda.game.player.items.ItemBow;
import zelda.game.player.items.ItemBracelet;
import zelda.game.player.items.ItemCaneOfSomaria;
import zelda.game.player.items.ItemFeather;
import zelda.game.player.items.ItemMagicRod;
import zelda.game.player.items.ItemMonsterBow;
import zelda.game.player.items.ItemMonsterRocks;
import zelda.game.player.items.ItemMonsterSword;
import zelda.game.player.items.ItemSatchel;
import zelda.game.player.items.ItemSeedShooter;
import zelda.game.player.items.ItemShield;
import zelda.game.player.items.ItemShovel;
import zelda.game.player.items.ItemSwitchHook;
import zelda.game.player.items.ItemSword;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.main.Keyboard;
import zelda.main.Keyboard.Key;


public class PlayerOLD extends Unit {
	private static final int DEPTH = 0;

	public static final int MAX_EQUIPPED_ITEMS = 2;
	private static final double MOVE_SPEED = 1.00;
	private static final double MOVE_SPEED_GRASS = 0.75;
	private static final double MOVE_SPEED_STAIRS = 0.50;
	private static final double JUMP_SPEED = 1.80;

	private int moveDir;

	private boolean busy;
	private boolean itemBusy;
	private boolean restrictMoveDir;

	private double direction;
	private boolean[] moveAxis;
	private boolean[] collisionDirs;
	private GridTile[] collisionTiles;
	private Item[] items;
	private int[] equippedItems;
	private double moveSpeed;
	private double z;
	private double zSpeed;
	private boolean ledgeJumping;
	private double ledgeDistance;
	private double ledgeCurrentDist;
	private Vector frameEnterPos;
	private boolean frameDying;
	private int pushTimer;

	public ItemSword itemSword;
	public ItemShield itemShield;
	public ItemBoomerang itemBoomerang;
	public ItemBow itemBow;
	public ItemShovel itemShovel;
	public ItemSatchel itemSatchel;
	public ItemMagicRod itemMagicRod;
	public ItemFeather itemFeather;
	public ItemBracelet itemBracelet;
	public ItemBombs itemBombs;
	public ItemSwitchHook itemSwitchHook;
	public ItemBiggoronSword itemBiggoronSword;
	public ItemCaneOfSomaria itemCaneOfSomaria;
	public ItemSeedShooter itemSeedShooter;
	public ItemMonsterBow itemMonsterBow;
	public ItemMonsterRocks itemMonsterRocks;
	public ItemMonsterSword itemMonsterSword;
	// public ItemMonsterBoomerang itemMonsterBoomerang;

	private DynamicAnimation animationMove;
	private DynamicAnimation animationStand;

	private Inventory inventory;



	// ================== CONSTRUCTORS ================== //

	public PlayerOLD(GameInstance control) {
		super(control);
		setDepth(DEPTH);

		position = new Vector(16, 16);
		moving = false;
		moveDir = 0;
		busy = false;

		sprite = new Sprite(new ImageSheet("sheetPlayer.png", 16, 16, 1));
		setMoveStandAnimations(Animations.PLAYER_DEFAULT);
		sprite.newAnimation(animationMove.getVariant(0));

		direction = 0;
		moveAxis = new boolean[] {false, false};
		collisionDirs = new boolean[] {false, false, false, false};
		collisionTiles = new GridTile[] {null, null, null, null};
		equippedItems = new int[] {-1, -1};
		restrictMoveDir = false;
		moveSpeed = MOVE_SPEED;
		inGrass = false;
		hardCollisionBox = new CollisionBox(4, 6, 8, 9);
		z = 0;
		zSpeed = 0;
		ledgeJumping = false;
		ledgeDistance = 0;
		ledgeCurrentDist = 0;
		frameEnterPos = new Vector(position);
		frameDying = false;
		slipping = false;
		slipTimer = 0;
		pushTimer = 0;

		// itemBracelet = new ItemBracelet(this);
		// itemSwitchHook = new ItemSwitchHook(this);


		// items = new Item[] {
		// itemBow = new ItemBow(this),
		// itemSatchel = new ItemSatchel(this),
		// itemSeedShooter = new ItemSeedShooter(this),
		// itemCaneOfSomaria = new ItemCaneOfSomaria(this),
		// itemBombs = new ItemBombs(this),
		// itemFeather = new ItemFeather(this),
		// itemMagicRod = new ItemMagicRod(this),
		// itemShovel = new ItemShovel(this),
		// itemShield = new ItemShield(this),
		// itemBoomerang = new ItemBoomerang(this),
		// itemBiggoronSword = new ItemBiggoronSword(this),
		// itemSword = new ItemSword(this)
		// };

		equippedItems[0] = 0;
		equippedItems[1] = 1;
	}



	// =================== ACCESSORS =================== //

	/** Return whether Link is "busy" (uncontrollable). **/
	public boolean isBusy() {
		return busy;
	}

	/** Return whether Link is "busy" holding an item. **/
	public boolean isItemBusy() {
		return itemBusy;
	}

	public Item[] getItems() {
		return items;
	}

	public double getZPosition() {
		return z;
	}

	public boolean isFrameDying() {
		return frameDying;
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isAnimationDone() {
		return sprite.isAnimationDone();
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getMoveDir() {
		return moveDir;
	}

	public Vector getCenter() {
		return position.plus(8, 8);
	}

	public Vector getOrigin() {
		return position.plus(8, 14);
	}

	public Item getEquippedItem(int slot) {
		if (equippedItems[slot] < 0)
			return null;
		return items[equippedItems[slot]];
	}

	public boolean inAir() {
		return (z > 0 || zSpeed > 0);
	}

	public boolean onGround() {
		return !inAir();
	}

	public boolean isSlipping() {
		return slipping;
	}

	public boolean isItemEquipped(Item item) {
		for (int i = 0; i < MAX_EQUIPPED_ITEMS; i++) {
			if (getEquippedItem(i) == item)
				return true;
		}
		return false;
	}

	public boolean isEquippedItemTwoHanded() {
		if (getEquippedItem(0) != null)
			return getEquippedItem(0).isTwoHanded();
		return false;
	}

	public Key getItemKey(Item item) {
		for (int i = 0; i < MAX_EQUIPPED_ITEMS; i++) {
			if (getEquippedItem(i) == item)
				return Keyboard.actions[i];
		}
		return null;
	}



	// ==================== MUTATORS ==================== //

	public void jump() {
		jump(JUMP_SPEED);
	}

	public void jump(double jumpSpeed) {
		if (z == 0) {
			zSpeed = jumpSpeed;
			if (!itemBusy) {
				// TODO: jump animation
			}
		}
	}

	public void recordFrameEnterPosition() {
		frameEnterPos = new Vector(position);
	}

	private void jumpLedge() {
		busy = true;
		ledgeJumping = true;
		ledgeDistance = 0;
		ledgeCurrentDist = 0;
		jump(1.8);

		Vector pos = new Vector(position);
		for (int i = 0; i < 1000; i++) {
			position.add(Vector.polarVector(1, moveDir * GMath.HALF_PI));
			ledgeDistance += 1;
			if (!isColliding())
				break;
		}
		position.set(pos);
		position.add(Vector.polarVector(1, moveDir * GMath.HALF_PI));
	}

	protected void land() {
		if (!busy)
			resetAnimation();
	}

	public void setRestrictMoveDir(boolean restrictMoveDir) {
		this.restrictMoveDir = restrictMoveDir;
	}

	public void setMoveDir(int moveDir) {
		this.moveDir = moveDir;
	}

	public void setZPosition(double z) {
		this.z = z;
	}

	public void setAnimationNEW(DynamicAnimation anim) {
		sprite.newAnimation(anim);

	}

	public void setAnimationNEW(Animation anim) {
		sprite.newAnimation(anim);
	}

	public void setAnimationNEW(boolean looped, DynamicAnimation anim) {
		sprite.newAnimation(anim);
		sprite.setLooped(looped);
	}

	public void setAnimationNEW(boolean looped, Animation anim) {
		sprite.newAnimation(anim);
		sprite.setLooped(looped);
	}

	public void setMoveStandAnimations(DynamicAnimation[] moveStandAnims) {
		animationMove = moveStandAnims[0];
		animationStand = moveStandAnims[1];
	}

	public void resetAnimation() {
		// TODO
		setMoveStandAnimations(Animations.PLAYER_DEFAULT);
		sprite.setLooped(true);
		sprite.newAnimation(animationStand);
		sprite.setVariation(moveDir);
	}

	public void setEquippedItem(int slot, Item item) {
		Item prevItem = getEquippedItem(slot);

		if (item == null) {
			equippedItems[slot] = -1;
		}
		else {
			for (int i = 0; i < items.length; i++) {
				if (items[i] == item) {
					equippedItems[slot] = i;
					item.onStart();
					break;
				}
			}
		}

		if (prevItem != null)
			prevItem.onEnd();
	}

	/** Set the state of being "busy" holding an item. **/
	public void setItemBusy(boolean itemBusy) {
		this.itemBusy = itemBusy;
	}

	/** Set the controllable state of Link. **/
	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	/** Die inside the frame (ex: falling in a hole/water/lava). **/
	private void frameDie(DynamicAnimation anim) {
		// Interrupt items;
		for (int i = 0; i < MAX_EQUIPPED_ITEMS; i++) {
			if (equippedItems[i] >= 0)
				items[equippedItems[i]].interrupt();
		}

		// TODO
		frameDying = true;
		busy = true;
		slipping = false;
		if (anim != null)
			;
		setAnimationNEW(anim);
	}

	// private void setEquippedItem(int slot, int itemIndex) {
	// Item itm = getEquippedItem(slot);
	// equippedItems[slot] = itemIndex;
	//
	// itm.onEnd();
	// getEquippedItem(slot).onStart();
	// }

	/** Check if link has wandered outside the current frame. **/
	private void checkLeaveFrame() {
		Frame frame = game.getFrame();
		Rectangle r = new Rectangle((int) position.x + 2, (int) position.y + 2,
				12, 13);
		Rectangle fr = frame.getRect();

		if (!fr.contains(r)) {
			Point add = new Point();

			if (position.x < 0)
				add.x = -1;
			else if (r.getX2() > fr.getX2())
				add.x = 1;
			else if (position.y < 0)
				add.y = -1;
			else if (r.getY2() > fr.getY2())
				add.y = 1;

			if (add.x != 0 || add.y != 0) {
				game.nextFrame(0);
				// control.getWorld().getCurrentLevel().nextFrame(add);
			}
		}
	}

	/** Update/Check Link's movement and movement keys. **/
	private void updateMovement() {
		moveSpeed = MOVE_SPEED;
		moving = false;
		direction = 0.0;
		inGrass = false;
		slipping = false;
		boolean pushing = false;
		Vectangle myVect = new Vectangle(position.x + 6, position.y + 12, 4, 4);

		if (onGround() && !frameDying) {
			for (int i = 0; i < game.getEntities().size(); i++) {
				Entity e = game.getEntities().get(i);

				if (e instanceof ObjectGrass) {
					if (myVect.touches(((ObjectGrass) e).getVect())
							&& MOVE_SPEED_GRASS < moveSpeed) {
						moveSpeed = MOVE_SPEED_GRASS;
						inGrass = true;
					}
				}
			}

			Frame frame = game.getFrame();
			int startX = (int) (position.x / 16);
			int startY = (int) (position.y / 16);
			/*
			 * CollisionData data = new CollisionData(hardCollisionBox,
			 * position);
			 * 
			 * 
			 * 
			 * for (int x = startX; x < startX + 2; x++) { for (int y = startY;
			 * y < startY + 2; y++) { if (frame.contains(new Point(x, y))) {
			 * GridTile t = frame.getGridTile(x, y); if (t == null) continue;
			 * 
			 * // CollisionData obj = t.getCollisionData(); CollisionData obj =
			 * new CollisionData(CollisionBox.TILE_BOX, t.getPosition());
			 * 
			 * if (Collision.checkColliding(data, obj)) { if
			 * (t.getProperties().getBoolean("stairs", false)) { moveSpeed =
			 * MOVE_SPEED_STAIRS; }
			 * 
			 * if (t.getProperties().getBoolean("hole", false)) { slipping =
			 * true; slipTimer++;
			 * 
			 * if (Math.abs(position.x - obj.position.x) > 0.5) position.x +=
			 * 0.5 * Math.signum(obj.position.x - position.x); if
			 * (Math.abs(position.y - obj.position.y) > 0.5) position.y += 0.5 *
			 * Math.signum(obj.position.y - position.y);
			 * 
			 * if (position.distanceTo(obj.position) < 1) { //
			 * frameDie(Resources.STRIP_PLAYER_FALL_HOLE); TODO
			 * position.set(obj.position); } }
			 * 
			 * if (t.getProperties().getBoolean("water", false)) { //
			 * frameDie(Resources.STRIP_PLAYER_FALL_WATER); }
			 * 
			 * if (t.getProperties().getBoolean("lava", false)) { //
			 * frameDie(Resources.STRIP_PLAYER_FALL_LAVA); } } } } }
			 */
		}

		if (!slipping)
			slipTimer = 0;

		// Update Movement Keys.
		if (!busy) {
			if (!checkMoveKey(2) && !checkMoveKey(0))
				moveAxis[0] = false;
			if (!checkMoveKey(3) && !checkMoveKey(1))
				moveAxis[1] = false;
		}

		// Move Link and set walking animation.
		if (moving) {
			if (slipping) {
				// TODO: Magic number!
				moveSpeed *= Math.sqrt(Math.max(0, 45 - slipTimer) / 45.0);
			}
			Vector displacement = new Vector(GMath.cos(direction) * moveSpeed,
					-GMath.sin(direction) * moveSpeed);

			if (!busy) {
				boolean colliding = checkCollisions(displacement);

				if (collisionTiles[moveDir] != null
						&& collisionTiles[moveDir].getProperties().getBoolean(
								"ledge", false)) {
					jumpLedge();
				}
				else if (colliding && collisionDirs[moveDir]
						&& (!itemBusy || itemSword.isHolding())
						&& !itemShield.isBlocking()) {
					if (itemSword.isHolding()) {
						itemSword.stab(false);
					}
					else if (!inAir()) {
						pushing = true;
						// TODO: push animation
					}
				}
			}

			// TODO
			// if (displacement.x != 0 || displacement.y != 0)
			// animationGrass.update();

			position.add(displacement);

			if (inAir())
				collideWithFrameBoundaries();
		}


		if (pushing)
			pushTimer++;
		else
			pushTimer = 0;
	}

	/** Check the given movement direction key and form a movement response. **/
	private boolean checkMoveKey(int directionIndex) {
		if (Keyboard.arrows[directionIndex].down()) {
			moving = true;
			if (!moveAxis[(directionIndex + 1) % 2])
				moveAxis[directionIndex % 2] = true;
			if (moveAxis[directionIndex % 2]) {
				direction = directionIndex * GMath.HALF_PI;
				if (!restrictMoveDir && !inAir())
					moveDir = directionIndex;

				if (Keyboard.arrows[GMath
						.getWrappedValue(directionIndex + 1, 4)].down())
					direction += GMath.QUARTER_PI;
				if (Keyboard.arrows[GMath
						.getWrappedValue(directionIndex - 1, 4)].down())
					direction -= GMath.QUARTER_PI;
			}
			return true;
		}
		return false;
	}

	private void collideWithFrameBoundaries() {
		if (position.x < -2)
			position.x = -2;
		if (position.y < -2)
			position.y = -2;
		if (position.x + 16 > game.getFrame().getRect().getX2() + 2)
			position.x = game.getFrame().getRect().getX2() + 2 - 16;
		if (position.y + 16 > game.getFrame().getRect().getY2() + 1)
			position.y = game.getFrame().getRect().getY2() + 1 - 16;
	}

	/** Check collisions with tiles and objects. **/
	private boolean checkCollisions(Vector displacement) {
		// CollisionData data = new CollisionData(hardCollisionBox, position,
		// displacement, displacement);
		// boolean colliding = Collision.collideWithWorld(data);
		// this.collisionDirs = data.collisionDirs;
		// this.collisionTiles = data.collisionTiles;
		// return colliding;
		return false;
	}

	/** Check if the player is colliding. **/
	private boolean isColliding() {
		// CollisionData data = new CollisionData(hardCollisionBox, new
		// Vector(position), new Vector(), new Vector());
		// return Collision.collideWithWorld(data);
		return false;
	}



	// =============== INHERITED METHODS =============== //

	@Override
	/** Update Link. **/
	public void update() {
		sprite.update(moveDir);

		updateMovement();
		checkLeaveFrame();

		int slot = (Keyboard.control.down() ? 1 : 0);

		if (z > 0 || zSpeed != 0 || ledgeJumping) {
			z = Math.max(z + zSpeed, 0);

			if (ledgeJumping && ledgeCurrentDist > 16
					&& ledgeCurrentDist < ledgeDistance - 16) {
				// z -= 1;//(ledgeCurrentDist - 16) / (ledgeDistance - 16);
				zSpeed = 0;
			}
			else
				zSpeed -= Settings.GRAVITY;

			if (z <= 0 && zSpeed < 0) {
				zSpeed = 0;
				land();
			}
		}
		else
			zSpeed = 0;

		// Set the depth.
		setDepth(DEPTH - (int) position.y - 10);

		if (ledgeJumping) {
			if (isColliding()) {
				position.add(Vector.polarVector(ledgeDistance < 24 ? 0.5 : 1,
						moveDir * GMath.HALF_PI));
				ledgeCurrentDist += 1;
			}
			else if (onGround()) {
				ledgeJumping = false;
				busy = false;
				ledgeCurrentDist = 0;
			}
		}


		if (frameDying && isAnimationDone()) {

			// Now wait for the view to move to the enter position.
//			if (game.moveViewTowardFocus(frameEnterPos.plus(0, 8))) {
//				frameDying = false;
//				busy = false;
//				position.set(frameEnterPos);
//				resetAnimation();
//			}
		}

		if (pushTimer > 20) {
			// Check for pushing blocks.
			Entity e = Collision.getInstanceMeeting(this,
					position.plus(Direction.lengthVector(1, moveDir)),
					FrameObject.class);

			pushTimer = 0;
			if (e != null && e instanceof FrameObject) {
				FrameObject obj = (FrameObject) e;

				if (obj.isMovable()) {
					obj.move(moveDir);
				}
			}
		}

		// Update items.
		for (int i = 0; i < items.length; i++) {
			items[i].update();
		}
	}

	@Override
	/** Draw Link. **/
	public void draw() {
		// Draw shadow.
		if (z > 0)
			Draw.drawSprite(Resources.SPRITE_SHADOW, new Point(getCenter()
					.plus(0, 5)));

		// Draw items under.
		for (int i = 0; i < items.length; i++)
			items[i].drawUnder();

		if (!busy) {
			if (moving)
				sprite.changeAnimation(animationMove);
			else
				sprite.changeAnimation(animationStand);
		}

		super.draw();

		// Draw items over.
		for (int i = 0; i < items.length; i++)
			items[i].drawOver();

		// Draw grass effect.
		if (inGrass) {
			// TODO
			// Draw.drawSprite(animationGrass.getCurrentSprite(), new
			// Point(getCenter()));
		}
	}
}
