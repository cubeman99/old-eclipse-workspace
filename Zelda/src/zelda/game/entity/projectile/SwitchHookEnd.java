package zelda.game.entity.projectile;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.entity.EntityObject;
import zelda.game.entity.effect.EffectCling;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;
import zelda.game.player.Player;
import zelda.game.world.tile.GridTile;

public class SwitchHookEnd extends Projectile {
	private static final double[] HOOK_LENGTH = {82, 112};
	private static final double[] HOOK_SPEED  = {2, 3};
	private static final int LIFT_HEIGHT      = 16;
	private static final int LIFT_DELAY       = 20;
	
	private boolean returning;
	private double distance;
	private double maxDistance;
	private double speed;
	private Vector startPosition;
	private int linkIndex;
	private Sprite spriteLink;
	private Entity hookObject;
	private boolean hooked;
	private boolean lifting;
	private boolean switched;
	private int timer;
	private boolean breakObject;
	private int level;
	private Vector playerSwitchPos;
	
	
	
	public SwitchHookEnd(Vector pos, int dir, int level) {
		super();
		returning     = false;
		angledDir     = dir;
		linkIndex     = 0;
		distance      = 0;
		maxDistance   = HOOK_LENGTH[level];
		speed         = HOOK_SPEED[level];
		startPosition = new Vector(pos);
		position.set(pos);
		playerSwitchPos = new Vector(pos);
		
		breakObject = false;
		hooked      = false;
		lifting     = false;
		switched    = false;
		timer       = 0;
		hookObject  = null;
		
		destroyedOutsideFrame = false;
		
		spriteLink = new Sprite(Resources.SHEET_PLAYER_ITEMS, 0, 8)
				.setOrigin(8, 8);
		sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_SWITCH_HOOK).setOrigin(8, 8);
		sprite.setSpeed(0);
	}
	
	private GridTile getCoverableTile(FrameObject obj, Point loc) {
		for (int i = 0; i < 2; i++) {
			Point checkLoc = loc.plus(Direction
					.getDirPoint((angledDir + 2) % 4).scaledBy(i));
			Vector checkPos = new Vector(checkLoc.scaledBy(16));
			if (frame.contains(checkLoc)) {
    			GridTile t = frame.getGridTile(checkLoc);
    			if (!Collision.placeMeetingSolid(obj, checkPos) && t != null && t.isCoverable() && t.isSurface())
    				return t;
			}
		}
		return null;
	}
	
	@Override
	public void crash() {
		if (!returning) {
			returning = true;
			game.addEntity(new EffectCling(position));
		}
	}
	
	@Override
	public void onHitMonster(Monster m) {
		
	}

	@Override
	public void update() {
		Player player = game.getPlayer();
		linkIndex = (linkIndex + 1) % 3;
		
		if (hooked) {
			timer++;
			if (lifting) {
				game.setViewFollowFocus(playerSwitchPos.plus(8, 8));
				
				if (switched) {
					if (timer > LIFT_HEIGHT) {
						if (breakObject)
							((FrameObject) hookObject).breakObject();
						else {
    						hookObject.setDestroyed(false);
    						game.addEntity(hookObject);
						}
						destroy();
						player.setZVelocity(0);
						player.setZPosition(0);
					}
				}
				else {
					if (timer > LIFT_HEIGHT) {
						switched  = true;
						timer     = 0;
						Vector v  = player.getCenter();
						Point loc = new Point((int) (v.x / 16.0),
								(int) (v.y / 16.0));
						
						if (hookObject instanceof FrameObject) {
							FrameObject obj = (FrameObject) hookObject;
							position.set(startPosition);
							startPosition.set(obj.getCenter());
							Point objLoc = obj.getLocation();

							if (Direction.isHorizontal(player.getDir()))
								loc.y = objLoc.y;
							else
								loc.x = objLoc.x;
							
							player.getPosition().set(obj.getPosition());
							
							GridTile t = getCoverableTile(obj, loc);
							if (t != null) {
								obj.setPosition(t.getPosition());
							}
							else {
								obj.setPosition(new Vector(loc).scaledBy(16));
								breakObject = true;
							}
						}
						else if (hookObject instanceof Monster) {
							Monster mobj = (Monster) hookObject;
							Vector temp = mobj.getCenter();
							mobj.setPosition(player.getCenter().minus(
									mobj.getCenter().minus(mobj.getPosition())));
							player.setPosition(temp.minus(player.getCenter()
									.minus(player.getPosition())));
						}
						switched = true;
						player.setDir((player.getDir() + 2)
								% Direction.NUM_DIRS);
						player.setAnimation(true, Animations.PLAYER_THROW);
					}
				}
			}
			else {
				if (timer > LIFT_DELAY) {
					lifting = true;
					timer   = 0;
					Sounds.ITEM_SWITCH_HOOK_SWITCH.play();
				}
			}
		}
		else {
			timer++;
			if (timer % 4 == 0)
				Sounds.ITEM_SWITCH_HOOK.play();
			
			if (returning) {
				collideWithWorld = false;
				velocity.setPolar(speed, GMath.direction(position, player.getCenter()));
				if (position.distanceTo(player.getCenter()) < speed) {
					destroy();
				}
			}
			else {
				velocity.set(Direction.lengthVector(speed, angledDir));
				distance += speed;

				ArrayList<Entity> objs = Collision.getInstancesMeeting(
						hardCollidable, position.plus(velocity), FrameObject.class);
				for (int i = 0; i < objs.size(); i++) {
					FrameObject obj = (FrameObject) objs.get(i);
					if (obj.isSwitchable()) {
						timer      = 0;
						hooked     = true;
						hookObject = obj;
						hookObject.setDestroyed(true);
						sprite.setSpeed(0.5);
						playerSwitchPos = obj.getPosition();
						if (obj.breaksOnSwitch())
							breakObject = true;
						break;
					}
				}

				if (distance >= maxDistance || isOutsideFrame())
					returning = true;
			}
		}
		super.update();
	}
	
	@Override
	public void draw() {
		double zpos = (lifting ? (switched ? LIFT_HEIGHT - timer : timer) : 0);
		game.getPlayer().setZPosition(zpos);
		if (hookObject instanceof EntityObject) {
			EntityObject obj = (EntityObject) hookObject;
			obj.setZPosition(zpos);
			obj.draw();
		}
		if (hookObject instanceof FrameObject) {
			FrameObject obj = (FrameObject) hookObject;
			if (lifting)
				Draw.drawSprite(Resources.SPRITE_SHADOW, obj.getPosition().plus(8, 11));
			if (obj.getEntitySprite() != null)
				Draw.drawSprite(lifting ? obj.getEntitySprite() : obj.getSprite(), obj.getPosition().minus(0, zpos));
		}
		
		Player p = game.getPlayer();
		Vector v = new Vector(p.getCenter(), position);
		Draw.drawSprite(spriteLink, p.getCenter().minus(0, zpos).plus(
				v.scaledBy((3 - linkIndex) * 0.25)));
		
		if (!lifting)
			super.draw();
	}
}
