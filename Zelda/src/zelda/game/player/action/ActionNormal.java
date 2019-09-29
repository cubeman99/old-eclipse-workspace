package zelda.game.player.action;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.util.Destination;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.control.transition.TransitionTunnel;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.logic.WarpPoint;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.MovingFrameObject;
import zelda.game.entity.object.overworld.ObjectCactus;
import zelda.game.entity.object.overworld.ObjectGrass;
import zelda.game.player.Player;
import zelda.game.world.tile.GridTile;
import zelda.main.Keyboard;

public class ActionNormal extends PlayerAction {
	private double moveSpeedScale;
	private double moveSpeed;
	private boolean[] moveAxis;
	private boolean moving;
	private int angledDir;
	private boolean swimming;
	private boolean diving;
	private Vector motion;
	private int pushTimer;
	private boolean onIce;
	private Vector velocityPrev;
	private boolean onWarpPoint;
	private int tempDir;
	private boolean hasMovedInAir;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public ActionNormal(Player player) {
		super(player);
		
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean canUseItems() {
		return (!swimming);
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean isJumping() {
		return player.inAir();
	}
	
	public boolean isStrafing() {
		return (isJumping() || player.itemSword.isHolding());
	}
	
	public boolean isStroking() {
		return (swimming && moveSpeedScale > 1.3); 
	}
	
	public boolean canWarp() {
		return (!isJumping() && !player.itemBombs.isHolding() && !player.itemBracelet.isHolding());
	}
	
	public boolean hasMovedInAir() {
		return hasMovedInAir;
	}
	
	public boolean canPush() {
		return (!player.itemShield.isBlocking()
				&& !swimming
				&& player.onGround()
				&& !player.isItemBusy());
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void jump() {
		if (player.onGround()) {
			hasMovedInAir = false;
			player.setZVelocity(1.8);
			Sounds.PLAYER_JUMP.play();
			player.setAnimation(false, Animations.PLAYER_JUMP);
		}
	}
	
	private void checkSwordStab() {
		Vector checkPos = player.getPosition().plus(Direction.lengthVector(moveSpeed, player.getDir()));
		
		if (Collision.placeMeetingSolid(player.getHardCollidable(), checkPos) && !Collision.canDodgeCollisions(player)) {
			player.itemSword.stab(false);
		}
	}
	
	private void checkPush() {
		Vector checkPos = player.getPosition().plus(Direction.lengthVector(moveSpeed, player.getDir()));
		
		if (Collision.placeMeetingSolid(player.getHardCollidable(), checkPos) && !Collision.canDodgeCollisions(player)) {
			player.changeAnimation(Animations.PLAYER_PUSH);
			pushTimer++;
		}
		else
			pushTimer = 0;
	}
	
	private void updateMoveControls() {
		player.setCollisionAutoDodge(true);
		moving = false;
		moveSpeed = (swimming ? 0.5 : 1);
		double acceleration = (onIce ? 0.02 : 0.08);
		if (isJumping())
			acceleration = 0.1;
		if (!swimming && player.isSprinting())
			moveSpeed *= 1.5;
		
		if (!player.isBusy() && player.isControllable() && (!isJumping() || player.getZVelocity() < 0.1)) {
			double speed = moveSpeed * moveSpeedScale;
			
			// Check the movement keys.
			angledDir = player.getDir();
			
			checkMoveKeyDir();
			
			if (angledDir % 2 == 1)
				player.setCollisionAutoDodge(false);
			
			if (moving || isStroking()) {
				if (!moving)
					angledDir = player.getDir() * 2;
				Vector keyMotion = Vector.polarVector(speed, angledDir * GMath.QUARTER_PI);
				
				if (isJumping() || swimming || onIce) {
					Vector vel = player.getVelocity();
					if (Math.abs(vel.x) < Math.abs(velocityPrev.x) || GMath.sign(vel.x) != GMath.sign(velocityPrev.x))
						motion.x = player.getVelocity().x;
					if (Math.abs(vel.y) < Math.abs(velocityPrev.y) || GMath.sign(vel.y) != GMath.sign(velocityPrev.y))
						motion.y = player.getVelocity().y;
					
					motion.add(keyMotion.scaledBy(acceleration));
					double newLength = motion.length();
					if (newLength >= speed)
						motion.setLength(speed);
					
					if (Math.abs(newLength - motion.plus(keyMotion.scaledBy(0.08)).length()) < acceleration * 2) {
						motion.add(keyMotion.scaledBy(0.04));
					}
					if (swimming) {
    					int angle = (int) ((motion.direction() / GMath.QUARTER_PI) + 0.5);
    					player.getVelocity().setPolar(motion.length(), angle * GMath.QUARTER_PI);
					}
					else {
						int angle = (int) ((motion.direction() / (GMath.QUARTER_PI * 0.5)) + 0.5);
						player.getVelocity().setPolar(motion.length(), angle * (GMath.QUARTER_PI * 0.5));
					}
				}
				else {
					motion.set(keyMotion);
					player.getVelocity().set(motion);
				}
				
				if (!isJumping() && !diving)
					player.setAnimationToMove();
				if (player.actionLedgeJump.checkLedgeJump(moving, moveSpeed))
					return;
				if (canPush())
					checkPush();
				else if (player.itemSword.isHolding())
					checkSwordStab();
			}
			else {
				if (swimming || onIce) {
					double length = motion.length();
					if (length < 0.05)
						motion.zero();
					else
						motion.setLength(length - 0.05);
					player.getVelocity().set(motion);
				}
				else if (!isJumping()) {
					motion.zero();
					player.getVelocity().set(motion);
    			}
				if (!isJumping() && !diving) {
    				if (player.isSprinting())
    					player.setAnimationToMove();
    				else
    					player.setAnimationToStand();
				}
			}
		}
		else if (player.isBusy() && player.onGround()) {
			player.getVelocity().zero();
		}
	}
	
	public int checkMoveKeyDir() {
		angledDir = player.getDir() * 2;
		tempDir = player.getDir();
		if (!checkMoveKey(2) && !checkMoveKey(0))
			moveAxis[0] = false;
		if (!checkMoveKey(3) && !checkMoveKey(1))
			moveAxis[1] = false;
		return tempDir;
	}
	
	/** Check the given movement direction key and form a movement response. **/
	public boolean checkMoveKey(int directionIndex) {
		if (Keyboard.arrows[directionIndex].down()) {
			moving = true;
			
			if (!moveAxis[(directionIndex + 1) % 2])
				moveAxis[directionIndex % 2] = true;
			
			if (moveAxis[directionIndex % 2]) {
				angledDir = directionIndex * 2;
				tempDir   = directionIndex;
				if (!isStrafing())
					player.setDir(directionIndex);
				if (Keyboard.arrows[(directionIndex + 1) % 4].down())
					angledDir = (angledDir + 1) % 8;
				if (Keyboard.arrows[(directionIndex + 3) % 4].down())
					angledDir = (angledDir + 7) % 8;
			}
			return true;
		}
		return false;
	}
	
	private void updateSwimming() {
		if (moveSpeedScale > 1)
			moveSpeedScale -= 0.025;
		
		if (!isStroking() && Keyboard.a.pressed()) {
			Sounds.PLAYER_SWIM.play();
			moveSpeedScale = 2;
		}
		if (diving) {
			if (player.isAnimationDone() || Keyboard.b.pressed()) {
				diving = false;
				player.resetAnimation();
			}
		}
		else if (Keyboard.b.pressed()) {
			diving = true;
			player.setAnimation(false, Animations.PLAYER_DIVE);
			Sounds.PLAYER_WADE.play();
			game.addEntity(new Effect(
					Resources.SPRITE_EFFECT_SPLASH_WATER,
					player.getCenter().plus(0, 4)));
		}
	}
	
	private void checkObjects() {
		Vector checkPos = player.getPosition().plus(
				Direction.lengthVector(2, player.getDir()));
		ArrayList<Entity> objects = Collision.getInstancesMeeting(
				player.getHardCollidable(), checkPos, FrameObject.class);
		
		for (int i = 0; i < objects.size(); i++) {
			FrameObject obj = (FrameObject) objects.get(i);

			// Touch Event
			obj.onPlayerTouch();
			
			// Action Event
			if (Keyboard.a.pressed())
				obj.onAction(player.getDir());
			
			// Push Event
			if (pushTimer > obj.getPushDelay() && obj.move(player.getDir()))
				pushTimer = 0;
		}
		
		// Check Warp Points.
		WarpPoint wp = (WarpPoint) Collision.getInstanceMeeting(
				player.getOrigin(), player, WarpPoint.class);
		if (wp != null) {
			if (!onWarpPoint && canWarp() && wp.warp())
				onWarpPoint = true;
		}
		else
			onWarpPoint = false;
	}
	
	private void checkTile() {
		GridTile t = Collision.getTile(player.getOrigin());
		if (!swimming)
			moveSpeedScale = 1;
		
		if (player.onGround()) {
			if (t != null) {
    			onIce = t.isIce();
    			
    			if (t.isWater() && !player.isOnSurface()) {
    				if (player.canSwim()) {
        				if (!swimming) {
            				player.getInventory().interruptItems();
            				player.setMoveStandAnimations(Animations.PLAYER_SWIMMING);
            				game.addEntity(new Effect(
            						Resources.SPRITE_EFFECT_SPLASH_WATER,
            						player.getCenter().plus(0, 4)));
        					swimming = true;
        					Sounds.PLAYER_WADE.play();
        				}
    				}
    				else {
        				player.getInventory().interruptItems();
    					game.addEntity(new Effect(
    							Resources.SPRITE_EFFECT_SPLASH_WATER, player
    									.getCenter().plus(0, 4)));
        				player.actionFrameDie.begin(ActionFrameDie.TYPE_WATER);
        				player.setCurrentAction(player.actionFrameDie);
        				return;
    				}
    			}
    			else if (swimming) {
    				player.resetMoveStandAnimations();
    				swimming = false;
    				diving   = false;
    			}
    			
    			if (t.isLava() && !player.isOnSurface()) {
    				player.getInventory().interruptItems();
    				player.actionFrameDie.begin(ActionFrameDie.TYPE_LAVA);
    				player.setCurrentAction(player.actionFrameDie);
					game.addEntity(new Effect(Resources.SPRITE_EFFECT_SPLASH_LAVA, player
									.getCenter().plus(0, -2)));
    			}
    			
    			if (t.isHole() && !player.isOnSurface()) {
    				
    			}
    			
    			if (t.isStairs())
    				moveSpeedScale = Math.min(0.5, moveSpeedScale);
			}
			
    		if (Collision.getInstanceMeeting(player.getOrigin(), player, ObjectGrass.class) != null) {
    			moveSpeedScale = Math.min(0.75, moveSpeedScale);
    		}
		}		
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	public void begin(boolean start) {
		moveSpeed      = 1;
		moveSpeedScale = 1;
		moveAxis       = new boolean[] {false, false};
		swimming       = false;
		diving         = false;
		onIce          = false;
		onWarpPoint    = false;
		hasMovedInAir  = false;
		motion         = new Vector();
		pushTimer      = 0;
		tempDir        = 0;
		velocityPrev   = player.getVelocity();
		player.setPassable(false);
		player.setCollideWithWorld(true);
		
		if (!start) {
    		onWarpPoint = (Collision.getInstanceMeeting(
    			player.getOrigin(),player, WarpPoint.class) != null);
		}
	}
	
	@Override
	public void begin() {
		begin(false);
	}
	
	@Override
	public void update() {
		checkTile();
		updateMoveControls();
		if (swimming)
			updateSwimming();
		

		player.setPassable(diving);
		
		checkObjects();
		
		player.setCollideWithFrameBoundaries(!moving || isJumping());
		
		velocityPrev = new Vector(player.getVelocity());
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
}
