package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.Collectable;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.effect.EffectCling;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.projectile.SwitchHookEnd;
import zelda.game.monster.Monster;
import zelda.game.player.Player;
import zelda.game.world.tile.GridTile;


public class ItemSwitchHook extends Item {
	private static final int[] HOOK_LENGHT = {82, 112};
	private static final int[] HOOK_SPEED  = {2, 3};
	private static final int LIFT_HEIGHT   = 16;
	private static final int LIFT_SPEED    = 1;
	
	private SwitchHookEnd switchHook;
	
	private Sprite spriteHook;
	private Sprite spriteHookLink;

	private boolean throwing;
	private int hookDistance;
	private boolean hookReturning;
	private CollisionBox hookBox;
	private Collidable hook;
	private boolean hooked;
	private Entity hookObject;
	private int liftPos;
	private boolean switched;
	private boolean lifting;
	private int hookedTimer;
	private int linkDrawTimer;
	private Collectable hookedCollectable;


	public ItemSwitchHook(Player player) {
		super(player, "SwitchHook");
		numLevels = 2;
		
		switchHook = null;
		
		name[0] = "Switch Hook";
		description[0] = "User and target trade places.";
		rewardMessages[0] = "You got the <red>Switch Hook<red>!<n>Shoot at an object to switch places with it.";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;
		name[1] = "Long Hook";
		description[1] = "Switches places from a distance.";
		rewardMessages[1] = "You got the <red>Long Hook<red>!";
		rewardHoldTypes[1] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 6, 1, 6, 1);

		spriteHook = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_SWITCH_HOOK).setOrigin(8, 8);
		spriteHookLink = new Sprite(Resources.SHEET_PLAYER_ITEMS, 0, 8)
				.setOrigin(8, 8);

		throwing = false;
		hookDistance = 0;
		hookReturning = false;
		hookBox = new CollisionBox(-2, -2, 4, 4);
		hooked = false;
		hookObject = null;
		liftPos = 0;
		switched = false;
		lifting = false;
		hookedTimer = 0;
		linkDrawTimer = 0;
		hookedCollectable = null;

		hook = new Collidable() {
			public boolean isSolid() {
				return true;
			}

			public Vector getPosition() {
				return getHookPosition();
			}

			public CollisionBox getCollisionBox() {
				return hookBox;
			}
		};
	}
	
	
	public void hookOntoMonster(Monster m) {
		hookReturning = true;
		hooked = true;
		lifting = false;
		hookedTimer = 0;
		liftPos = 0;
		switched = false;
		hookObject = m;
		m.destroy();
	}

	private Vector getHookPosition() {
		return player.getCenter().plus(
				Direction.lengthVector(hookDistance, player.getDir()));
	}

	@Override
	public void update() {
		spriteHook.setVariation(player.getDir());

		if (hooked || throwing) {
			linkDrawTimer += 1;
			if (linkDrawTimer >= 3)
				linkDrawTimer -= 3;
		}
		if (hooked) {
			player.setZPosition(liftPos);

			if (!lifting) {
				spriteHook.update(player.getDir());

				if (hookedTimer++ >= 20) {
					if (hookObject.isDestroyed()
							&& hookObject instanceof FrameObject) {
						throwing = false;
						hooked = false;
						player.setBusy(false);
						player.resetAnimation();
						player.setZPosition(0);
					}
					else {
						lifting = true;
						hookedTimer = 0;
						if (hookObject instanceof FrameObject) {
							((FrameObject) hookObject).createDrop();
							((FrameObject) hookObject).enableUnderTile();
						}
						hookObject.destroy();
						Sounds.ITEM_SWITCH_HOOK_SWITCH.play();
					}
				}
			}
			else {
				hookedTimer = (hookedTimer + 1) % 3;
				if (switched) {
					liftPos -= LIFT_SPEED;
					if (liftPos <= 0) {
						hooked = false;
						lifting = false;
						throwing = false;
						player.setBusy(false);
						player.resetAnimation();
						player.setZPosition(0);
						player.getGame().addEntity(hookObject);
						hookObject.setDestroyed(false);

						if (hookObject instanceof FrameObject) {
							FrameObject fobj = (FrameObject) hookObject;
							Point loc = new Point(fobj.getPosition()
									.scaledByInv(16));
							GridTile t = player.getGame().getFrame()
									.getGridTile(loc);

							if (fobj.breaksOnSwitch())
								fobj.breakObject();
							else if (t.getProperties()
									.getBoolean("hole", false))
								fobj.breakObject();
							else if (t.getProperties().getBoolean("water",
									false))
								fobj.breakObject();
							else if (t.getProperties()
									.getBoolean("lava", false))
								fobj.breakObject();
						}
					}
				}
				else {
					liftPos += LIFT_SPEED;
					if (liftPos >= LIFT_HEIGHT) {
						// Switch.
						Vector v = player.getCenter();
						Point loc = new Point((int) (v.x / 16.0),
								(int) (v.y / 16.0));

						if (hookObject instanceof FrameObject) {
							FrameObject fobj = (FrameObject) hookObject;

							Point objLoc = new Point(
									(int) (fobj.getPosition().x / 16.0),
									(int) (fobj.getPosition().y / 16.0));

							if (Direction.isHorizontal(player.getDir()))
								loc.y = objLoc.y;
							else
								loc.x = objLoc.x;

							player.getPosition().set(fobj.getPosition());
							fobj.setPosition(new Vector(loc.x * 16, loc.y * 16));
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
		}
		else if (throwing) {
			if (hookReturning) {
				hookDistance -= HOOK_SPEED[level];
				if (hookDistance <= 0) {
					throwing = false;
					if (hookedCollectable != null) {
						hookedCollectable.collect();
						hookedCollectable = null;
					}
					player.setBusy(false);
					player.resetAnimation();
					Sounds.ITEM_SWITCH_HOOK.stop();
				}
			}
			else {
				hookDistance += HOOK_SPEED[level];

				Vector hookPos = getHookPosition();

				Collectable c = (Collectable) Collision.getInstanceMeeting(
						hook, hookPos, Collectable.class);

				Monster m = (Monster) Collision.getInstanceMeeting(hook,
						hookPos, Monster.class);

				FrameObject obj = (FrameObject) Collision.getInstanceMeeting(
						hook, hookPos, FrameObject.class);

				if (c != null && c.onGround()) {
					hookReturning = true;
					hookedCollectable = c;
					hookedCollectable.getSprite().setFrameIndex(0);
					hookedCollectable.destroy();
				}
				else if (m != null && m.onGround() && !m.isPassable()) {
					hookReturning = true;
					m.triggerReaction(ReactionCause.SWITCH_HOOK, level, player,
							getHookPosition());
				}
				else if (obj != null) {
					if (obj.isSolid()) {
						hookReturning = true;
						player.getGame().addEntity(new EffectCling(hookPos));

						if (obj.isSwitchable()) {
							hooked = true;
							lifting = false;
							hookedTimer = 0;
							liftPos = 0;
							switched = false;
							hookObject = obj;
							Sounds.ITEM_SWITCH_HOOK.stop();
						}
					}
				}

				if (hookDistance >= HOOK_LENGHT[level]
						|| player.getGame().getFrame().isOutside(hookPos)) {
					hookReturning = true;
				}
				else if (!hookReturning
						&& Collision.placeMeetingSolid(hook, hookPos)) {
					hookReturning = true;
					player.getGame().addEntity(new EffectCling(hookPos));
				}
			}
		}
//		else if (keyPressed() && !player.isBusy() && !player.isItemBusy()
//				&& !player.isSlipping() && player.onGround()) {
//			player.setBusy(true);
//			player.setAnimation(Animations.PLAYER_THROW);
//			hookDistance = 0;
//			throwing = true;
//			hookReturning = false;
//			linkDrawTimer = 0;
//			spriteHook.resetAnimation();
//			Sounds.ITEM_SWITCH_HOOK.loop();
//		}
		if (switchHook != null) {
			if (switchHook.isDestroyed()) {
				switchHook = null;
				player.setBusy(false);
				player.resetAnimation();
			}
		}
		else if (keyPressed() && !player.isBusy() && !player.isItemBusy()
				&& !player.isSlipping() && player.onGround())
		{
			switchHook = new SwitchHookEnd(player.getCenter(), player.getDir(), level);
			player.setBusy(true);
			player.setAnimation(Animations.PLAYER_THROW);
			game.addEntity(switchHook);
		}
	}

	@Override
	public void drawUnder() {
		if (hooked && hookObject != null) {
			if (hookObject instanceof Monster) {
				((Monster) hookObject).setZPosition(player.getZPosition());
				((Monster) hookObject).draw();
				((Monster) hookObject).preDraw();
			}
			if (lifting) {
				if (hookObject instanceof FrameObject) {
					FrameObject fobj = (FrameObject) hookObject;

					Draw.drawSprite(fobj.getEntitySprite(), new Point(fobj
							.getPosition().minus(0, player.getZPosition())));
					Draw.drawSprite(Resources.SPRITE_SHADOW,
							new Point(fobj.getPosition()).plus(8, 14));
				}
			}
		}
		if (throwing) {
			Vector v = Direction.lengthVector(hookDistance, player.getDir());

			Draw.drawSprite(
					spriteHookLink,
					new Point(player.getCenter()
							.minus(0, player.getZPosition())
							.plus(v.scaledBy((3 - linkDrawTimer) * 0.25))));

			if (!lifting) {
				Draw.drawSprite(spriteHook,
						new Point(getHookPosition().minus(0, liftPos)));
				if (hookedCollectable != null) {
					Draw.drawSprite(hookedCollectable.getSprite(),
							getHookPosition().plus(0, 3));
				}
			}
		}
	}
}
