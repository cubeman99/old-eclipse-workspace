package entity.living;

import java.awt.Graphics2D;

import main.Keyboard;

import entity.Entity;
import entity.weapons.Seed;
import game.CollisionType;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Animation;
import graphics.Sprite;
import graphics.library.Library;

import tile.Tile;
import world.Room;

/**
 * The player the user plays as during the game.
 * @author	Robert Jordan
 */
public class Player extends Entity {

	// ====================== Constants =======================
	
	/** The different walking states link can be in. */
	public static final int WALK_NORMAL				= 0;
	public static final int WALK_SHIELD				= 1;
	public static final int WALK_SHIELDING			= 2;
	public static final int WALK_HOLDING			= 3;
	public static final int WALK_SWIMMING			= 4;
	public static final int WALK_JUMPING			= 5;
	public static final int WALK_FLYING				= 6;
	public static final int WALK_LADDER				= 7;
	
	/** The different action states link can be in. */
	public static final int ACTION_NONE				= 0;
	public static final int ACTION_HOLDING			= 1;
	public static final int ACTION_SWINGING			= 3;
	public static final int ACTION_SPINNING			= 4;
	public static final int ACTION_CHARGING			= 5;
	public static final int ACTION_LIFTING			= 6;
	public static final int ACTION_THROWING			= 7;
	public static final int ACTION_BIG_SWINGING		= 8;
	
	/** The different secondary action states link can be in. */
	public static final int MOTION_NONE				= 0;
	public static final int MOTION_LEAPING			= 1;
	public static final int MOTION_LEAP_LANDING		= 2;
	public static final int MOTION_JUMPING			= 3;
	public static final int MOTION_JUMP_LANDING		= 4;
	public static final int MOTION_FLYING			= 5;
	public static final int MOTION_LADDER			= 6;
	public static final int MOTION_SWIMMING			= 7;
	public static final int MOTION_DIVING			= 8;
	public static final int MOTION_UNDERWATER		= 9;
	
	// ======================= Members ========================
	
	// Health
	/** The current health of the player. */
	public int health;
	/** The maximum health of the player. */
	public int maxHealth;
	
	// Walking information
	/** One of the eight directions that the player is walking in. */
	public int walkDirection;
	/** The previous direction the player was walking in. */
	public int walkDirectionLast;
	/** One of the four directions that the player is facing. */
	public int faceDirection;
	/** True if the player is currently walking. */
	public boolean walking;

	// Action information
	/** The current state of Link's walking. */
	public int walkState;
	/** The current action link is performing. */
	public int actionState;
	/** The current motion action state link is in. */
	public int motionState;
	/** A counter used for timing actions. */
	public int actionCounter;
	/** The direction used by the action. */
	public int actionDirection;
	/** The type of surface the player is currently on. */
	//public int surfaceType;
	/** True if the player is shielding. */
	public boolean shielding;
	/** The additive velocity of the player. */
	public Vector walkVelocity;

	// Pushing:
	/** True if the player is pushing. */
	public boolean pushing;
	/** Used to count how long until a push is executed. */
	public int pushCounter;
	/** The direction the player is pushing in. */
	public int pushDirection;
	
	// Visual
	/** The current animation of the entity. */
	public Sprite sprite;
	/** The current animation of the prop. */
	public Sprite propSprite;
	/** Link's current walk animations. */
	public Animation[] walkAnimations;
	/** The current Tunic link is wearing. */
	public String currentTunic;
	
	// Hurt animation
	
	
	// ===================== Constructors =====================
	
	/** Constructs the default player. */
	public Player() {
		super();
		
		// Basic information
		this.depth				= 5;
		this.bounds				= new Rectangle(4, 6, 8, 9);
		
		// Health
		this.health				= 12;
		this.maxHealth			= 12;
		
		// Walking information
		this.walkDirection		= -1;
		this.walkDirectionLast	= 0;
		this.faceDirection		= 0;
		this.walking			= false;
		
		// Action information
		this.walkState			= WALK_NORMAL;
		this.actionState		= ACTION_NONE;
		this.motionState		= MOTION_NONE;
		this.actionCounter		= -1;
		this.actionDirection	= -1;
		this.surfaceType		= CollisionType.land;
		this.shielding			= false;
		this.walkVelocity		= new Vector();
		
		// Pushing
		this.pushing			= false;
		this.pushCounter		= -1;
		this.pushDirection		= -1;
		
		// Visuals
		this.walkAnimations		= new Animation[4];
		for (int i = 0; i < 4; i++)
			this.walkAnimations[i] = Library.animations.walk[i];
		this.sprite				= new Sprite(this.walkAnimations[this.faceDirection], Library.tilesets.linkNormal);
		this.propSprite			= new Sprite(null, Library.tilesets.weapons);
		this.currentTunic		= "kokiri_tunic";
		this.sprite.setSpeed(0);
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		this.room = room;
	}

	// ======================= Updating =======================
	
	/** Called every step, before collision, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
		
		// Update tunic
		if (!room.game.inventory.isEquipmentEquipped(currentTunic)) {
			if (room.game.inventory.isEquipmentEquipped("kokiri_tunic")) {
				currentTunic = "kokiri_tunic";
				sprite.setTileset(Library.tilesets.linkNormal);
			}
			else if (room.game.inventory.isEquipmentEquipped("goron_tunic")) {
				currentTunic = "goron_tunic";
				sprite.setTileset(Library.tilesets.linkNormalRed);
			}
			else if (room.game.inventory.isEquipmentEquipped("zora_tunic")) {
				currentTunic = "zora_tunic";
				sprite.setTileset(Library.tilesets.linkNormalBlue);
			}
		}
		
		// Update sprite
		propSprite.update();
		sprite.update();
		
		// Update action counter
		if (actionCounter > 0)
			actionCounter--;
		
		updateMoveControls();
		updateWeapons();
		updateAction();
		updateSecondary();
		
		updateSurface();
		
		updateDirectionVelocity();
		
		addSurfaceMotion();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		super.updateCollissions();
		
		moveToCollideWithTiles();
		adjustToSideOfTiles();
		moveToCollideWithEntities();
		
		updatePushing();
	}
	/** Called every step, before movement, to update the entity's state. */
	public void update() {
		super.update();
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		moveEntity();
	}
	/** Called every step, after movement, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
	}
	/** Called every step, after drawing, to update the entity's state. */
	public void postdraw() {
		super.postdraw();
	}
	
	// ======================== Drawing ========================
	
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		//super.draw(g, point);
		Vector pos = new Vector(position);
		if (pos.x < 0 && (int)pos.x != pos.x)
			pos.x = (int)pos.x - 1;
		if (pos.y < 0 && (int)pos.y != pos.y)
			pos.y = (int)pos.y - 1;
		propSprite.draw(g, point.plus(pos).plus(0, -z));
		sprite.draw(g, point.plus(pos).plus(0, -z));
		
		drawSurfaceOverlay(g, point);
	}

	// ======================= Movement =======================
	
	/** Called every step to check for movement control changes. */
	protected void updateMoveControls() {
		// Get walking direction
		walkDirection = -1;
		if (!isActionLocked()) {
			if (Keyboard.left.down() && Keyboard.up.down())
				walkDirection = 5;
			else if (Keyboard.left.down() && Keyboard.down.down())
				walkDirection = 3;
			else if (Keyboard.left.down())
				walkDirection = 4;
			else if (Keyboard.up.down() && Keyboard.right.down())
				walkDirection = 7;
			else if (Keyboard.up.down())
				walkDirection = 6;
			else if (Keyboard.right.down() && Keyboard.down.down())
				walkDirection = 1;
			else if (Keyboard.right.down())
				walkDirection = 0;
			else if (Keyboard.down.down())
				walkDirection = 2;

			if (walkDirection != -1)
				walkDirectionLast = walkDirection;
		}
		// Set the facing direction and current sprite
		if (walkDirection == -1) {
			if (!isActionAnimated() && !isAnimationConstant()) {
				sprite.reset();
				sprite.setSpeed(0);
				walking = false;
			}
		}
		else if ((faceDirection == 2 && !isMovingLeft())  ||
				 (faceDirection == 3 && !isMovingUp())    ||
				 (faceDirection == 0 && !isMovingRight()) ||
				 (faceDirection == 1 && !isMovingDown())  || !walking) {
			
			walking = true;
			
			if (!isActionAimed()) {
				if (isMovingLeft()) {
					faceDirection = 2;
				}
				else if (isMovingUp()) {
					faceDirection = 3;
				}
				else if (isMovingRight()) {
					faceDirection = 0;
				}
				else if (isMovingDown()) {
					faceDirection = 1;
				}
			}
			if (!isActionAnimated()) {
				if (!isActionAimed())
					sprite.setAnimation(walkAnimations[faceDirection]);
				sprite.setSpeed(1);
			}
		}
	}
	/** Called every step to update the player's velocity based on walk direction. */
	protected void updateDirectionVelocity() {
		int moveDirection = (isActionLocked() ? actionDirection : walkDirection);
		double[] multipliers = getMotionMultiplier();
		double moveAccel = 0.025;
		double diagAccel = 1.0/Math.sqrt(2.0) * moveAccel;
		double[] moveSpeed = {
			multipliers[0],
			multipliers[2],
			multipliers[4],
			multipliers[6]
		};
		double[] diagSpeed = {
			1.0/Math.sqrt(2.0) * multipliers[1],
			1.0/Math.sqrt(2.0) * multipliers[3],
			1.0/Math.sqrt(2.0) * multipliers[5],
			1.0/Math.sqrt(2.0) * multipliers[7]
		};

		if (!isMotionAdditive()) {
			switch (moveDirection) {
			case 0:		walkVelocity.set( moveSpeed[0],             0); break;
			case 1:		walkVelocity.set( diagSpeed[0],  diagSpeed[0]);	break;
			case 2:		walkVelocity.set(            0,  moveSpeed[1]);	break;
			case 3:		walkVelocity.set(-diagSpeed[1],  diagSpeed[1]);	break;
			case 4:		walkVelocity.set(-moveSpeed[2],             0);	break;
			case 5:		walkVelocity.set(-diagSpeed[2], -diagSpeed[2]);	break;
			case 6:		walkVelocity.set(            0, -moveSpeed[3]);	break;
			case 7:		walkVelocity.set( diagSpeed[3], -diagSpeed[3]);	break;
			case -1:	walkVelocity.zero();
			}
		}
		else {
			switch (moveDirection) {
			case 0:		accelerateTowards(moveAccel, 0, moveAccel, moveSpeed[0], 0); break;
			case 1:		accelerateTowards(diagAccel, diagAccel, diagAccel, diagSpeed[0], diagSpeed[0]); break;
			case 2:		accelerateTowards(0, moveAccel, moveAccel, 0, moveSpeed[1]); break;
			case 3:		accelerateTowards(-diagAccel, diagAccel, diagAccel, -diagSpeed[1], diagSpeed[1]); break;
			case 4:		accelerateTowards(-moveAccel, 0, moveAccel, -moveSpeed[2], 0); break;
			case 5:		accelerateTowards(-diagAccel, -diagAccel, diagAccel, -diagSpeed[2], -diagSpeed[2]); break;
			case 6:		accelerateTowards(0, -moveAccel, moveAccel, 0, -moveSpeed[3]); break;
			case 7:		accelerateTowards(diagAccel, -diagAccel, diagAccel, diagSpeed[3], -diagSpeed[3]); break;
			case -1:	accelerateTowards(0, 0, moveAccel, 0, 0);
			}
		}
		
		velocity.set(walkVelocity);
	}
	/** Called every step to update the player's push state. */
	protected void updatePushing() {
		
		Rectangle[][] sideBounds = new Rectangle[4][2];
		sideBounds[0][0] = new Rectangle(bounds.point.plus(8, 2), new Vector(1, 1));
		sideBounds[0][1] = new Rectangle(bounds.point.plus(8, 7), new Vector(1, 1));
		sideBounds[1][0] = new Rectangle(bounds.point.plus(1, 9), new Vector(1, 1));
		sideBounds[1][1] = new Rectangle(bounds.point.plus(6, 9), new Vector(1, 1));
		
		sideBounds[2][0] = new Rectangle(bounds.point.plus(-1, 2), new Vector(1, 1));
		sideBounds[2][1] = new Rectangle(bounds.point.plus(-1, 7), new Vector(1, 1));
		sideBounds[3][0] = new Rectangle(bounds.point.plus(1, -1), new Vector(1, 1));
		sideBounds[3][1] = new Rectangle(bounds.point.plus(6, -1), new Vector(1, 1));
		
		// Make sure the push still meets the requirements
		if (room.isCollidingWithTile(position, sideBounds[faceDirection][0], CollisionType.allSolid) &&
			room.isCollidingWithTile(position, sideBounds[faceDirection][1], CollisionType.allSolid) &&
			walkDirection != -1 && actionState == ACTION_NONE && motionState == MOTION_NONE && !shielding) {
			if (!pushing || faceDirection != pushDirection) {
				pushing = true;
				pushCounter = 20;
				pushDirection = faceDirection;
			}
		}
		else {
			if (pushing) {
				pushing = false;
				if (actionState == ACTION_NONE) {
					sprite.setAnimation(walkAnimations[faceDirection], true);
				}
			}
		}
		
		// Update the push
		if (pushing) {
			if (velocity.x != 0 || velocity.y != 0) {
				// The current push was canceled
				pushCounter = 20;
			}
			pushCounter--;
			if (pushCounter == 0) {
				// Try to push the tile
				Tile tile = room.getTileAtPosition(position.plus(sideBounds[faceDirection][0].point));
				if (tile == room.getTileAtPosition(position.plus(sideBounds[faceDirection][1].point))) {
					tile.push(faceDirection);
				}
			}
			sprite.setAnimation(Library.animations.push[pushDirection], true);
		}
	}
	/** Called every step to update the surface the player is on. */
	protected void updateSurface() {
		super.updateSurface();
		
		if (surfaceType == CollisionType.ladder && motionState == MOTION_NONE) {
			motionState = MOTION_LADDER;
			cancelAction();
			faceDirection = 3;
			sprite.setAnimation(walkAnimations[3]);
		}
		else if (surfaceType != CollisionType.ladder && motionState == MOTION_LADDER) {
			motionState = MOTION_NONE;
		}
		
		if (CollisionType.matchesType(CollisionType.allWater, surfaceType) && motionState == MOTION_NONE) {
			motionState = MOTION_SWIMMING;
			cancelAction();
			setWalkState(WALK_SWIMMING);
			sprite.setAnimation(walkAnimations[faceDirection]);
		}
		else if (!CollisionType.matchesType(CollisionType.allWater, surfaceType) &&
			(motionState == MOTION_SWIMMING || motionState == MOTION_DIVING)) {
			motionState = MOTION_NONE;
			setWalkState(WALK_NORMAL);
			sprite.setAnimation(walkAnimations[faceDirection]);
		}
	}
	/** Called every step to update the motion of the surface. */
	protected void addSurfaceMotion() {
		super.addSurfaceMotion();
	}
	/** Accelerates the player towards a given speed. */
	protected void accelerateTowards(double haccel, double vaccel, double decel,
			double hspeed, double vspeed) {
		// Accelerate Left
		if (walkVelocity.x > hspeed) {
			if (haccel < 0)
				walkVelocity.x += haccel;
			else
				walkVelocity.x -= decel;
			if (walkVelocity.x < hspeed)
				walkVelocity.x = hspeed;
		}
		// Accelerate Up
		if (walkVelocity.y > vspeed) {
			if (vaccel < 0)
				walkVelocity.y += vaccel;
			else
				walkVelocity.y -= decel;
			if (walkVelocity.y < vspeed)
				walkVelocity.y = vspeed;
		}
		// Accelerate Right
		if (walkVelocity.x < hspeed) {
			if (haccel > 0)
				walkVelocity.x += haccel;
			else
				walkVelocity.x += decel;
			if (walkVelocity.x > hspeed)
				walkVelocity.x = hspeed;
		}
		// Accelerate Down
		if (walkVelocity.y < vspeed) {
			if (vaccel > 0)
				walkVelocity.y += vaccel;
			else
				walkVelocity.y += decel;
			if (walkVelocity.y > vspeed)
				walkVelocity.y = vspeed;
		}
	}
	
	// ======================== Motion ========================

	/** Returns true if the player is walking in the given direction. */
	public boolean isWalkingInDirection(int direction) {
		switch (direction) {
		case -1:	return walkDirection == -1;
		case 0:		return (walkDirection == 7 || walkDirection == 0 || walkDirection == 1);
		case 1:		return (walkDirection >= 1 && walkDirection <= 3);
		case 2:		return (walkDirection >= 3 && walkDirection <= 5);
		case 3:		return (walkDirection >= 5 && walkDirection <= 7);
		}
		return false;
	}
	/** Returns true if the player is moving left. */
	public boolean isMovingLeft() {
		return (walkDirection >= 3 && walkDirection <= 5);
	}
	/** Returns true if the player is moving right. */
	public boolean isMovingRight() {
		return (walkDirection == 7 || walkDirection == 0 || walkDirection == 1);
	}
	/** Returns true if the player is moving up. */
	public boolean isMovingUp() {
		return (walkDirection >= 5 && walkDirection <= 7);
	}
	/** Returns true if the player is moving down. */
	public boolean isMovingDown() {
		return (walkDirection >= 1 && walkDirection <= 3);
	}
	
	// ======================= Actions ========================
	
	/** Returns true if the action controls the player's movement. */
	protected boolean isActionLocked() {
		// Some motion states override action's return
		switch (motionState) {
		case MOTION_NONE:			break;
		case MOTION_LEAPING:		return true;
		case MOTION_LEAP_LANDING:	return true;
		case MOTION_JUMPING:		return true;
		case MOTION_JUMP_LANDING:	return false;
		case MOTION_SWIMMING:		return false;
		case MOTION_DIVING:			return false;
		case MOTION_UNDERWATER:		break;
		}
		switch (actionState) {
		case ACTION_NONE:			return false;
		case ACTION_SWINGING:		return true;
		case ACTION_SPINNING:		return true;
		case ACTION_CHARGING:		return false;
		case ACTION_LIFTING:		return true;
		case ACTION_HOLDING:		return false;
		case ACTION_THROWING:		return true;
		case ACTION_BIG_SWINGING:	return true;
		}
		return false;
	}
	/** Returns true if Link is not able to face a different direction during
	 * the current action. */
	protected boolean isActionAimed() {
		// Some motion states override action's return
		switch (motionState) {
		case MOTION_NONE:			break;
		case MOTION_LEAPING:		return true;
		case MOTION_LEAP_LANDING:	return true;
		case MOTION_JUMPING:		return true;
		case MOTION_JUMP_LANDING:	return true;
		case MOTION_FLYING:			return true;
		case MOTION_LADDER:			return true;
		case MOTION_SWIMMING:		return false;
		case MOTION_DIVING:			return false;
		case MOTION_UNDERWATER:		break;
		}
		switch (actionState) {
		case ACTION_NONE:			return false;
		case ACTION_SWINGING:		return true;
		case ACTION_SPINNING:		return true;
		case ACTION_CHARGING:		return true;
		case ACTION_LIFTING:		return true;
		case ACTION_HOLDING:		return false;
		case ACTION_THROWING:		return true;
		case ACTION_BIG_SWINGING:	return true;
		}
		return false;
	}
	/** Returns true if the action requires an animation. */
	protected boolean isActionAnimated() {
		// Some motion states override action's return
		switch (motionState) {
		case MOTION_NONE:			break;
		case MOTION_LEAPING:		return true;
		case MOTION_LEAP_LANDING:	return true;
		case MOTION_JUMPING:		return true;
		case MOTION_JUMP_LANDING:	return true;
		case MOTION_SWIMMING:		return false;
		case MOTION_DIVING:			return true;
		case MOTION_UNDERWATER:		break;
		}
		switch (actionState) {
		case ACTION_NONE:			return false;
		case ACTION_SWINGING:		return true;
		case ACTION_SPINNING:		return true;
		case ACTION_CHARGING:		return false;
		case ACTION_LIFTING:		return true;
		case ACTION_HOLDING:		return false;
		case ACTION_THROWING:		return true;
		case ACTION_BIG_SWINGING:	return true;
		}
		return false;
	}
	/** Returns true if the motion has acceleration. */
	protected boolean isMotionAdditive() {
		switch (motionState) {
		case MOTION_NONE:			break;
		case MOTION_LEAPING:		break;
		case MOTION_LEAP_LANDING:	break;
		case MOTION_JUMPING:		break;
		case MOTION_JUMP_LANDING:	break;
		case MOTION_SWIMMING:		return true;
		case MOTION_DIVING:			return true;
		case MOTION_UNDERWATER:		return true;
		}
		switch (surfaceType) {
		case CollisionType.ice:		return true;
		}
		return false;
	}
	/** Gets the directional multiplier for moving based on the player's state. */
	protected double[] getMotionMultiplier() {
		switch (motionState) {
		case MOTION_NONE:			break;
		case MOTION_LEAPING:		break;
		case MOTION_LEAP_LANDING:	break;
		case MOTION_JUMPING:		break;
		case MOTION_JUMP_LANDING:
			switch (actionDirection) {
			case -1:	return new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
			case 0:		return new double[]{1.0, 1.0, 1.0, 0.5, 0.25, 0.5, 1.0, 1.0};
			case 1:		return new double[]{1.0, 1.0, 1.0, 1.0, 0.5, 0.25, 0.5, 1.0};
			case 2:		return new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 0.25, 0.5};
			case 3:		return new double[]{0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 0.25};
			case 4:		return new double[]{0.25, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5};
			case 5:		return new double[]{0.5, 0.25, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0};
			case 6:		return new double[]{1.0, 0.5, 0.25, 0.5, 1.0, 1.0, 1.0, 1.0};
			case 7:		return new double[]{1.0, 1.0, 0.5, 0.25, 0.5, 1.0, 1.0, 1.0};
			}
			break;
		case MOTION_FLYING:			break;
		case MOTION_SWIMMING:		return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
		case MOTION_DIVING:			return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
		case MOTION_UNDERWATER:		break;
		}
		switch (surfaceType) {
		case CollisionType.stairs:	return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
		case CollisionType.ladder:	return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
		case CollisionType.ice:		break;
		}
		
		return new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
	}
	/** Returns true if the animation should not stop when the player stops moving. */
	protected boolean isAnimationConstant() {
		switch (motionState) {
		case MOTION_SWIMMING: return true;
		}
		return false;
	}
	/** Terminates the current action. */
	protected void cancelAction() {
		switch (actionState) {
		case ACTION_NONE:
			break;
		case ACTION_SWINGING:
			propSprite.setAnimation(null);
			break;
		case ACTION_SPINNING:
			propSprite.setAnimation(null);
			break;
		case ACTION_CHARGING:
			propSprite.setAnimation(null);
			break;
		case ACTION_LIFTING:
			propSprite.setAnimation(null);
			break;
		case ACTION_HOLDING:
			propSprite.setAnimation(null);
			// Drop object
			break;
		case ACTION_THROWING:
			propSprite.setAnimation(null);
			break;
		case ACTION_BIG_SWINGING:
			propSprite.setAnimation(null);
			break;
		}
		actionState = ACTION_NONE;
		shielding = false;
		setWalkState(WALK_NORMAL);
		sprite.setAnimation(walkAnimations[faceDirection]);
	}

	// ======================== States ========================
	
	/** Sets the walk state of the player. */
	protected void setWalkState(int newWalkState) {
		if (walkState == newWalkState)
			return;
		
		switch (newWalkState) {
		case WALK_NORMAL:
			for (int i = 0; i < 4; i++)
				walkAnimations[i] = Library.animations.walk[i];
			break;
		case WALK_SHIELD:
			if (room.game.inventory.itemExists("shield")) {
				if (room.game.inventory.getWeapon("shield").getLevel() == 1) {
					for (int i = 0; i < 4; i++)
						walkAnimations[i] = Library.animations.walkShield1A[i];
				}
				else {
					for (int i = 0; i < 4; i++)
						walkAnimations[i] = Library.animations.walkShield1B[i];
				}
			}
			break;
		case WALK_SHIELDING:
			if (room.game.inventory.itemExists("shield")) {
				if (room.game.inventory.getWeapon("shield").getLevel() == 1) {
					for (int i = 0; i < 4; i++)
						walkAnimations[i] = Library.animations.walkShield2A[i];
				}
				else {
					for (int i = 0; i < 4; i++)
						walkAnimations[i] = Library.animations.walkShield2B[i];
				}
			}
			break;
		case WALK_HOLDING:
			for (int i = 0; i < 4; i++)
				walkAnimations[i] = Library.animations.walkHold[i];
			break;
		case WALK_JUMPING:
			for (int i = 0; i < 4; i++)
				walkAnimations[i] = Library.animations.jump[i];
			break;
		case WALK_SWIMMING:
			for (int i = 0; i < 4; i++)
				walkAnimations[i] = Library.animations.swim[i];
			break;
		}
		
		walkState = newWalkState;
	}
	/** Called every step to update the Link's weapon state. */
	protected void updateWeapons() {
		
		if (room.game.inventory.isWeaponEquipped("shield")) {
			if ((actionState == ACTION_NONE || actionState == ACTION_CHARGING) &&
					(motionState == MOTION_NONE || motionState == MOTION_LADDER)) {
				if (room.game.inventory.isWeaponButtonDown("shield")) {
					if (!shielding) {
						setWalkState(WALK_SHIELDING);
						shielding = true;
						sprite.setAnimation(walkAnimations[faceDirection], true);
						// Play shielding sound
					}
				}
				else if (walkState != WALK_SHIELD) {
					shielding = false;
					setWalkState(WALK_SHIELD);
					sprite.setAnimation(walkAnimations[faceDirection], true);
				}
			}
		}
		else {
			if (walkState == WALK_SHIELD || walkState == WALK_SHIELDING) {
				shielding = false;
				if (motionState == MOTION_NONE) {
					setWalkState(WALK_NORMAL);
					sprite.setAnimation(walkAnimations[faceDirection]);
				}
			}
		}
		if (room.game.inventory.isWeaponEquipped("sword")) {
			if ((actionState == ACTION_NONE || actionState == ACTION_SWINGING) &&
				(motionState == MOTION_NONE || motionState == MOTION_JUMPING ||
				motionState == MOTION_JUMP_LANDING || motionState == MOTION_UNDERWATER)) {
				if (room.game.inventory.isWeaponButtonPressed("sword")) {
					if (Keyboard.left.down())
						faceDirection = 2;
					else if (Keyboard.up.down())
						faceDirection = 3;
					else if (Keyboard.right.down())
						faceDirection = 0;
					else if (Keyboard.down.down())
						faceDirection = 1;
					shielding = false;
					actionState = ACTION_SWINGING;
					sprite.setAnimation(Library.animations.swing[faceDirection]);
					int level = room.game.inventory.getWeapon("sword").getLevel();
					propSprite.setAnimation(Library.animations.swordSwing[level - 1][faceDirection]);
					// Play swinging sound
					// Link to using sword
					// Create sword prop
				}
			}
		}
		if (room.game.inventory.isWeaponEquipped("biggoron_sword")) {
			if (actionState == ACTION_NONE &&
				(motionState == MOTION_NONE || motionState == MOTION_JUMPING ||
				motionState == MOTION_JUMP_LANDING || motionState == MOTION_UNDERWATER)) {
				if (room.game.inventory.isWeaponButtonPressed("biggoron_sword")) {
					actionState = ACTION_BIG_SWINGING;
					sprite.setAnimation(Library.animations.bigSwing[faceDirection]);
					propSprite.setAnimation(Library.animations.biggoronSwing[faceDirection]);
					// Play swinging sound
					// Link to using sword
					// Create sword prop
				}
			}
		}
		if (room.game.inventory.isWeaponEquipped("seed_satchel")) {
			if (actionState == ACTION_NONE &&
				(motionState == MOTION_NONE || motionState == MOTION_JUMPING ||
				motionState == MOTION_JUMP_LANDING || motionState == MOTION_UNDERWATER)) {
				String seedType = room.game.inventory.getWeapon("seed_satchel").getCurrentAmmo().getID();
				if (room.game.inventory.isWeaponButtonPressed("seed_satchel") &&
					!room.entityExists(seedType + "_seed_satchel")) {
					room.addEntity(Seed.createSeedOfType(seedType,
							(walkDirection != -1 ? walkDirection : faceDirection * 2), 0, position, z));
					actionState = ACTION_THROWING;
					sprite.setAnimation(Library.animations.throwing[faceDirection]);
					propSprite.setAnimation(null);
					// Play seed sound
				}
			}
		}
		if (room.game.inventory.isWeaponEquipped("slingshot")) {
			if (actionState == ACTION_NONE &&
				(motionState == MOTION_NONE || motionState == MOTION_JUMPING ||
				motionState == MOTION_JUMP_LANDING || motionState == MOTION_UNDERWATER)) {
				String seedType = room.game.inventory.getWeapon("slingshot").getCurrentAmmo().getID();
				if (room.game.inventory.isWeaponButtonPressed("slingshot") &&
					!room.entityExists(seedType + "_slingshot")) {
					room.addEntity(Seed.createSeedOfType(seedType,
							(walkDirection != -1 ? walkDirection : faceDirection * 2), 2, position, z));
					if (room.game.inventory.getWeapon("slingshot").getLevel() == 2) {
						room.addEntity(Seed.createSeedOfType(seedType,
								(walkDirection != -1 ? walkDirection : faceDirection * 2), 3, position, z));
						room.addEntity(Seed.createSeedOfType(seedType,
								(walkDirection != -1 ? walkDirection : faceDirection * 2), 4, position, z));
					}
					actionState = ACTION_THROWING;
					sprite.setAnimation(Library.animations.throwing[faceDirection]);
					propSprite.setAnimation(Library.animations.slingshot[faceDirection]);
					if (room.game.inventory.getWeapon("slingshot").getLevel() == 2) {
						propSprite.setTileOffset(4, 0);
					}
					// Play seed sound
				}
			}
		}
		
		if (room.game.inventory.isWeaponEquipped("bomb_bag")) {
			if (actionState == ACTION_NONE && motionState == MOTION_NONE) {
				if (room.game.inventory.isWeaponButtonPressed("bomb_bag")) {
					actionState = ACTION_LIFTING;
					sprite.setAnimation(Library.animations.lift[faceDirection]);
					propSprite.setAnimation(Library.animations.objectLift[faceDirection]);
					// Play bomb sound
				}
			}
		}

		if (room.game.inventory.isWeaponEquipped("rocs_feather")) {
			if ((actionState == ACTION_NONE || actionState == ACTION_CHARGING ||
				actionState == ACTION_SWINGING || actionState == ACTION_BIG_SWINGING) &&
				(motionState == MOTION_NONE)) {
				if (room.game.inventory.isWeaponButtonPressed("rocs_feather")) {
					zspeed = 2.15;
					actionDirection = walkDirection;
					motionState = MOTION_JUMPING;
					if (actionState != ACTION_SWINGING && actionState != ACTION_BIG_SWINGING &&
							actionState != ACTION_CHARGING)
						sprite.setAnimation(Library.animations.jump[faceDirection]);
				}
			}
		}
	}
	/** Called every step to update the state of Link's current action. */
	protected void updateAction() {
		switch (actionState) {
		case ACTION_NONE:
			break;
		case ACTION_SWINGING:
			if (sprite.isAnimationFinished()) {
				if (room.game.inventory.isWeaponEquipped("sword") &&
					room.game.inventory.isWeaponButtonDown("sword")) {
					
					actionState = ACTION_CHARGING;
					actionCounter = 42;
					sprite.setAnimation(walkAnimations[faceDirection]);
					propSprite.setCurrentFrame(3);
					propSprite.setSpeed(0);
				}
				else {
					actionState = ACTION_NONE;
					setWalkState(WALK_NORMAL);
					sprite.setAnimation(walkAnimations[faceDirection]);
					propSprite.setAnimation(null);
				}
			}
			break;
		case ACTION_BIG_SWINGING:
			if (sprite.isAnimationFinished()) {
				actionState = ACTION_NONE;
				setWalkState(WALK_NORMAL);
				sprite.setAnimation(walkAnimations[faceDirection]);
				propSprite.setAnimation(null);
			}
			break;
		case ACTION_SPINNING:
			if (sprite.isAnimationFinished()) {
				actionState = ACTION_NONE;
				setWalkState(WALK_NORMAL);
				sprite.setAnimation(walkAnimations[faceDirection]);
				propSprite.setAnimation(null);
			}
			break;
		case ACTION_CHARGING:
			if (actionCounter == 0) {
				actionCounter = -1;
				int level = 1;
				if (room.game.inventory.itemExists("sword")) {
					level = room.game.inventory.getWeapon("sword").getLevel();
				}
				propSprite.setAnimation(Library.animations.swordCharge[level - 1][faceDirection]);
				// Play charge sound
			}
			if (!room.game.inventory.isWeaponEquipped("sword") ||
				!room.game.inventory.isWeaponButtonDown("sword")) {
				
				if (actionCounter == -1) {
					actionState = ACTION_SPINNING;
					int level = 1;
					if (room.game.inventory.itemExists("sword")) {
						level = room.game.inventory.getWeapon("sword").getLevel();
					}
					sprite.setAnimation(Library.animations.spin[faceDirection]);
					propSprite.setAnimation(Library.animations.swordSpin[level - 1][faceDirection]);
					// Play spin sound
				}
				else {
					actionState = ACTION_NONE;
					setWalkState(WALK_NORMAL);
					sprite.setAnimation(walkAnimations[faceDirection]);
					propSprite.setAnimation(null);
				}
			}
			break;
		case ACTION_LIFTING:
			if (sprite.isAnimationFinished()) {
				actionState = ACTION_HOLDING;
				setWalkState(WALK_HOLDING);
				sprite.setAnimation(walkAnimations[faceDirection]);
				propSprite.setCurrentFrame(2);
				propSprite.setSpeed(0);
			}
			break;
		case ACTION_HOLDING:
			if (Keyboard.a.pressed() || Keyboard.b.pressed()) {
				actionState = ACTION_THROWING;
				sprite.setAnimation(Library.animations.throwing[faceDirection]);
				propSprite.setAnimation(null);
			}
			break;
		case ACTION_THROWING:
			if (sprite.isAnimationFinished()) {
				actionState = ACTION_NONE;
				setWalkState(WALK_NORMAL);
				sprite.setAnimation(walkAnimations[faceDirection]);
				propSprite.setAnimation(null);
			}
			break;
		}	
	}
	/** Called every step to update the state of Link's current secondary action. */
	protected void updateSecondary() {
		switch (motionState) {
		case MOTION_NONE:
			break;
		case MOTION_LEAPING:
			zspeed -= 0.15;
			if (zspeed < 0) {
				motionState = MOTION_LEAP_LANDING;
			}
			break;
		case MOTION_LEAP_LANDING:
			zspeed -= 0.15;
			
			break;
		case MOTION_JUMPING:
			zspeed -= 0.15;
			if (zspeed < 0) {
				motionState = MOTION_JUMP_LANDING;
			}
			break;
		case MOTION_JUMP_LANDING:
			zspeed -= 0.15;
			if (walkDirection == -1)
				walkDirection = actionDirection;
			if (z < 0) {
				z = 0;
				zspeed = 0;
				setWalkState(WALK_NORMAL);
				motionState = MOTION_NONE;
				actionDirection = -1;
				if (!isActionAnimated())
					sprite.setAnimation(walkAnimations[faceDirection]);
			}
			break;
		case MOTION_FLYING:
			
			break;
		case MOTION_LADDER:
			faceDirection = 3;
			break;
		case MOTION_SWIMMING:
			if (Keyboard.b.pressed()) {
				motionState = MOTION_DIVING;
				sprite.setAnimation(Library.animations.dive);
			}
			break;
		case MOTION_DIVING:
			if (Keyboard.b.up()) {
				motionState = MOTION_SWIMMING;
				setWalkState(WALK_SWIMMING);
				sprite.setAnimation(walkAnimations[faceDirection]);
			}
			break;
		case MOTION_UNDERWATER:
			
			break;
		}
	}
}