package zelda.game.player;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.control.GameInstance;
import zelda.game.control.transition.TransitionTunnel;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.Unit;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.effect.EffectFallingObject;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.MovingFrameObject;
import zelda.game.entity.object.dungeon.ObjectMineCart;
import zelda.game.entity.object.overworld.ObjectCactus;
import zelda.game.player.action.ActionFrameDie;
import zelda.game.player.action.ActionLedgeJump;
import zelda.game.player.action.ActionMineCart;
import zelda.game.player.action.ActionNormal;
import zelda.game.player.action.PlayerAction;
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
import zelda.game.world.Level;
import zelda.game.world.tile.GridTile;
import zelda.main.Keyboard;


public class Player extends Unit {
	private static final int SPRINT_DURATION = 480;

	private DynamicAnimation[] defaultMoveStandAnimations;
	private DynamicAnimation animationMove;
	private DynamicAnimation animationStand;

	private boolean busy;

	private boolean itemBusy;
	private boolean swimming;
	private boolean sprinting;
	
	private boolean[] moveAxis;
	private Vector frameEnterPos;
	private int sprintTimer;
	
	private PlayerAction[] actions;
	private PlayerAction currentAction;
	public ActionNormal actionNormal;
	public ActionFrameDie actionFrameDie;
	public ActionMineCart actionMineCart;
	public ActionLedgeJump actionLedgeJump;
	
	private Inventory inventory;
	private Purse purse;
	private Inventory inventoryNormal;

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

	public Currency currencyRupees;
	public Currency currencyHeartPieces;
	public Currency currencyHearts;
	public Currency currencyArrows;
	public Currency currencyBombs;
	public Currency currencyBombchus;
	public Currency[] currencySeeds;



	// ================== CONSTRUCTORS ================== //

	public Player(GameInstance game) {
		super(game);

		busy      = false;
		itemBusy  = false;
		swimming  = false;
		sprinting = false;
		
		soundDamage = Sounds.PLAYER_HURT;
		soundBump   = null;
		
		moveAxis       = new boolean[] {false, false, false, false};
		frameEnterPos  = new Vector();
		sprintTimer    = 0;

		defaultMoveStandAnimations = Animations.PLAYER_DEFAULT;
		setMoveStandAnimations(defaultMoveStandAnimations);
		sheetNormal = new ImageSheet("sheetPlayer.png", 16, 16, 1);
		sheetHurt = new ImageSheet("sheetPlayerHurt.png", 16, 16, 1);
		sprite = new Sprite(sheetNormal, animationMove.getVariant(0));

		health.fill(7 * 4);

		hardCollisionBox = new CollisionBox(4, 6, 8, 9);
		softCollisionBox = new CollisionBox(2, 2, 12, 14);
		bounces = false;

		shieldCollisionBoxes = new CollisionBox[] {
				new CollisionBox(14, 2, 2, 14), new CollisionBox(2, 1, 14, 2),
				new CollisionBox(0, 2, 2, 14), new CollisionBox(2, 14, 14, 2)};

		currencySeeds = new Currency[5];
		purse = new Purse(this)
				.addCurrency(currencyHearts = health)
				.addCurrency(currencyRupees = new Currency("Rupees", 0, 999))
				.addCurrency(
						currencyHeartPieces = new Currency("Pieces of Heart",
								1, 4))
				.addCurrency(currencyArrows = new Currency("Arrows", 30))
				.addCurrency(currencyBombs = new Currency("Bombs", 10))
				.addCurrency(currencyBombchus = new Currency("Bombchus", 99))
				.addCurrency(currencySeeds[0] = new Currency("Ember Seeds", 20))
				.addCurrency(currencySeeds[1] = new Currency("Scent Seeds", 20))
				.addCurrency(
						currencySeeds[2] = new Currency("Pegasus Seeds", 20))
				.addCurrency(currencySeeds[3] = new Currency("Gale Seeds", 20))
				.addCurrency(
						currencySeeds[4] = new Currency("Mystery Seeds", 20));
		purse.initialize();

		inventoryNormal = new Inventory().addItem(itemBow = new ItemBow(this))
				.addItem(itemSatchel = new ItemSatchel(this))
				.addItem(itemSeedShooter = new ItemSeedShooter(this))
				.addItem(itemCaneOfSomaria = new ItemCaneOfSomaria(this))
				.addItem(itemBombs = new ItemBombs(this))
				.addItem(itemFeather = new ItemFeather(this))
				.addItem(itemMagicRod = new ItemMagicRod(this))
				.addItem(itemShovel = new ItemShovel(this))
				.addItem(itemShield = new ItemShield(this))
				.addItem(itemBoomerang = new ItemBoomerang(this))
				.addItem(itemBiggoronSword = new ItemBiggoronSword(this))
				.addItem(itemSword = new ItemSword(this))
				.addItem(itemBracelet = new ItemBracelet(this))
				.addItem(itemSwitchHook = new ItemSwitchHook(this));

		inventoryNormal.obtainItem(itemSword);
		inventoryNormal.obtainItem(itemFeather);
		inventoryNormal.obtainItem(itemSatchel);
		inventoryNormal.obtainItem(itemShovel);
		inventoryNormal.obtainItem(itemBracelet);
		inventoryNormal.obtainItem(itemBombs);
		inventoryNormal.obtainItem(itemSeedShooter);
		inventoryNormal.obtainItem(itemShield);

		// inventoryNormal.obtainAllItems();
		
		actions = new PlayerAction[] {
			actionNormal    = new ActionNormal(this),
			actionLedgeJump = new ActionLedgeJump(this),
			actionFrameDie  = new ActionFrameDie(this),
			actionMineCart  = new ActionMineCart(this)
		};
		currentAction = actionNormal;
		actionNormal.begin(true);
		
		inventory = inventoryNormal;
		inventory.equipItem(Settings.ACTION_A, itemSword);
		inventory.equipItem(Settings.ACTION_B, itemShield);
	}


	// =================== ACCESSORS =================== //
	
	public boolean canUseItem(Item item) {
		if (currentAction == actionMineCart && item.canBeUsedInMineCart())
			return true;
		if (currentAction != actionNormal)
			return false;
		return actionNormal.canUseItems();
	}
	
	public boolean isBusy() {
		return busy;
	}

	public boolean isItemBusy() {
		return (itemBusy || busy || swimming);
	}

	public boolean isFrameDying() {
		return (currentAction == actionFrameDie);
	}
	
	public boolean isInMineCart() {
		return (currentAction == actionMineCart);
	}
	
	public boolean isSprinting() {
		return sprinting;
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public boolean canSwim() {
		return true; // TODO
	}

	public Purse getPurse() {
		return purse;
	}
	
	public Point getStandLocation() {
		return new Point(position.plus(8, 14).scaledByInv(16));
	}

	public boolean isItemEquipped(Item item) {
		return inventory.isItemEquipped(item);
	}
	
	public Vector getFrameEnterPosition() {
		return frameEnterPos;
	}

	public Item getEquippedItem(int slot) {
		return inventory.getEquippedItem(slot);
	}

	public boolean checkSwordTouch(Collidable c) {
		return (itemSword.isTouching(c) || itemBiggoronSword.isTouching(c));
	}

	public boolean checkSwordHitObject(Point objectPosition) {
		return (itemSword.checkHitObject(objectPosition) || itemBiggoronSword
				.checkHitObject(objectPosition));
	}
	
	public Vector getDrawOffset() {
		if (currentAction == actionMineCart)
    		return sprite.getCurrentFrame().getPart(0).getDrawPos().inverse();
		return new Vector();
	}
	


	// ==================== MUTATORS ==================== //
	
	public void setFrameEnterPosition(Vector pos) {
		frameEnterPos.set(pos);
	}
	
	public void recordFrameEnterPosition() {
		frameEnterPos.set(position);
	}

	public void jump() {
		if (currentAction == actionNormal)
			actionNormal.jump();
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
//		if (busy)
//			velocity.zero();
	}
	
	public void move(int direction) {
		dir = direction;
		velocity.set(Direction.lengthVector(1, direction));
		setAnimationToMove();
		collideWithFrameBoundaries = false;
		performCollisions();
		position.add(velocity);
		sprite.update();
	}
	
	public void stop() {
		velocity.zero();
		setAnimationToStand();
	}

	public void startSprinting() {
		sprinting = true;
		sprintTimer = 0;
	}

	public void setItemBusy(boolean itemBusy) {
		this.itemBusy = itemBusy;
	}

//	public void setStrafing(boolean strafing) {
//		this.strafing = strafing;
//	}
//
//	public void setOnWarpPoint(boolean onWarpPoint) {
//		this.onWarpPoint = onWarpPoint;
//	}

	public void resetMoveStandAnimations() {
		setMoveStandAnimations(defaultMoveStandAnimations);
	}
	
	public void setAnimationToStand() {
		if (!sprite.isLooped())
			sprite.newAnimation(true, animationStand);
		else
			sprite.changeAnimation(animationStand);
	}
	
	public void setAnimationToMove() {
		if (!sprite.isLooped())
			sprite.newAnimation(true, animationMove);
		else
			sprite.changeAnimation(animationMove);
	}
	
	public void setMoveStandAnimations(DynamicAnimation[] moveStandAnims) {
		animationMove = moveStandAnims[0];
		animationStand = moveStandAnims[1];
	}

	public void setDefaultMoveStandAnimations(DynamicAnimation[] moveStandAnims) {
		defaultMoveStandAnimations = moveStandAnims;
		setMoveStandAnimations(defaultMoveStandAnimations);
	}

	public void resetAnimation() {
		sprite.setLooped(true);
		sprite.newAnimation(animationStand);
		sprite.setVariation(dir);
	}
	
	public void beginAction(PlayerAction action) {
		if (currentAction != null)
			currentAction.end();
		currentAction = action;
		if (currentAction != null)
			currentAction.begin();
	}
	
	public void setCurrentAction(PlayerAction action) {
		if (currentAction != null)
			currentAction.end();
		currentAction = action;
	}
	
	public void enterCart(ObjectMineCart cart) {
		beginAction(actionMineCart);
		actionMineCart.start(cart);
	}
	
	public void onEnterFrame() {
		/* TODO
		if (ledgeJumping) {
			// Find the distance of the ledge.
			double y = position.y;
			while (Collision.placeMeetingSolid(hardCollidable, position)) {
				position.add(Direction.lengthVector(1, dir));
			}
			zPosition = position.y - y;
			velocity.zero();
			zVelocity = -Settings.TERMINAL_Z_VELOCITY;
			// zVelocity = -velocity.y + zVelocity;
		}
		*/
	}

	/** Check if the player has wandered outside the current frame. **/
	private void checkLeaveFrame() {
		Vectangle v = new Vectangle(softCollidable, position);
		Vectangle fr = game.getFrame().getVect();
		
		if (!fr.contains(v))// && ((ledgeJumping && !fr.contains(v.plus(new Vector(0, -zPosition)))) || (controllable && !busy)))
		{
			int dir = -1;

			if (position.x < 0)
				dir = Direction.WEST;
			else if (v.getX2() > fr.getX2())
				dir = Direction.EAST;
			else if (position.y < 0)
				dir = Direction.NORTH;
			else if (v.getY2() > fr.getY2())
				dir = Direction.SOUTH;

			if (dir >= 0)
				game.nextFrame(dir);
		}
	}

	public int getAngledDir() {
		int angledDir = dir * 2;
		if (moving) {
			if (moveAxis[0]) {
				if (Keyboard.up.down())
					angledDir += (dir == 0 ? 1 : -1);
				else if (Keyboard.down.down())
					angledDir += (dir == 0 ? -1 : 1);
			}
			else if (moveAxis[1]) {
				if (Keyboard.left.down())
					angledDir += (dir == 1 ? 1 : -1);
				else if (Keyboard.right.down())
					angledDir += (dir == 1 ? -1 : 1);
			}
		}
		angledDir = (angledDir + 8) % 8;
		return angledDir;
	}
	
	public void hurtFlicker() {
		hurtFlickering = true;
		if (hurtTimer > flickerDuration)
			hurtTimer = 0;
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected GridTile getHole() {
		GridTile t = Collision.getTile(getOrigin());
		if (t != null && t.isHole())
			return t;
		return null;
	}
	
	@Override
	protected void onLand() {
		super.onLand();
		Sounds.PLAYER_LAND.play();
	}
	
	@Override
	protected void onFallInLava() {
//		if (currentAction != actionFrameDie) {
//			beginAction(actionFrameDie);
//			setAnimation(Animations.PLAYER_DUNK);
//			Sounds.PLAYER_WADE.play();
//			System.out.println("ASD");
//		}
	}

	@Override
	protected void onFallInHole(EffectFallingObject e) {
		if (currentAction != actionFrameDie) {
			inventory.interruptItems();
			actionFrameDie.begin(ActionFrameDie.TYPE_FALL);
			setCurrentAction(actionFrameDie);
		}
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
		if (!collideWithFrameBoundaries)
			checkLeaveFrame();
		if (currentAction != null)
			currentAction.update();
		
		moving = (currentAction == actionNormal && actionNormal.isMoving());
		
		if (sprinting) {
			if (sprintTimer % 10 == 0) {
				Sounds.PLAYER_LAND.play();
				Effect e = new Effect(Resources.SPRITE_EFFECT_SPRINT,
						getOrigin());
				e.setDepth(-10000);
				game.addEntity(e);
			}
			if (sprintTimer++ >= SPRINT_DURATION)
				sprinting = false;
		}
		
		faceDir = dir;
		super.update();
		inventory.updateItems();
	}
	
	@Override
	public void preDraw() {
		super.preDraw();
		
		if (currentAction == actionMineCart)
			actionMineCart.drawUnder();
	}
	
	@Override
	public void draw() {
		inventory.drawItemsUnder();
		
		Draw.drawSprite(sprite, position.plus(getDrawOffset()).minus(0, zPosition));
		
		if (currentAction == actionMineCart)
			actionMineCart.drawOver();
		inventory.drawItemsOver();
	}
}
