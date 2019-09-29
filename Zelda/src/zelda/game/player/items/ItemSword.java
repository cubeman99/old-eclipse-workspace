package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.effect.EffectCling;
import zelda.game.entity.projectile.ProjectileSwordBeam;
import zelda.game.player.Player;
import zelda.main.Keyboard;


public class ItemSword extends AbstractItemSwing {
	private static final int CHARGE_TIME = 40;
	private boolean spinning;
	private boolean stabbing;
	private boolean holding;
	private int chargeTimer;
	private boolean sheatheAfterwards;
	private CollisionBox[] swordCollisionBoxes;
	private Collidable swordCollidable;



	// ================== CONSTRUCTORS ================== //

	public ItemSword(Player thePlayer) {
		super(thePlayer, "Sword");
		numLevels = 3;

		name[0] = "Wooden Sword";
		description[0] = "A hero<ap>s blade.";
		rewardMessages[0] = "You got a Hero<ap>s <red>Wooden Sword<red>! Hold <A1><A2> or "
				+ "<B1><B2> to charge it up, then release it for a spin attack!";
		rewardHoldTypes[0] = CollectableReward.TYPE_ONE_HAND;

		name[1] = "Noble Sword";
		description[1] = "A sacred blade.";
		rewardMessages[1] = "You got a Hero<ap>s <red>Noble Sword<red>!";
		rewardHoldTypes[1] = CollectableReward.TYPE_ONE_HAND;

		name[2] = "Master Sword";
		description[2] = "The blade of legends.";
		rewardMessages[2] = "You got the Legendary <red>Master Sword<red>!";
		rewardHoldTypes[2] = CollectableReward.TYPE_ONE_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 0, 0, 1, 0, 2, 0);

		isSword = true;
		spinning = false;
		stabbing = false;
		holding = false;
		sheatheAfterwards = false;
		chargeTimer = 0;

		swordCollisionBoxes = new CollisionBox[] {
				new CollisionBox(12, 10, 15, 4),
				new CollisionBox(2, -11, 4, 15),
				new CollisionBox(-11, 10, 15, 4),
				new CollisionBox(9, 14, 4, 15)};

		swordCollidable = new Collidable() {
			public boolean isSolid() {
				return false;
			}

			public Vector getPosition() {
				return player.getPosition();
			}

			public CollisionBox getCollisionBox() {
				return swordCollisionBoxes[player.getFaceDir()];
			}
		};

		entityTracker.setMaxNumEntities(1);
	}



	// =================== ACCESSORS =================== //

	public boolean isSpinning() {
		return spinning;
	}

	public boolean isStabbing() {
		return stabbing;
	}

	public boolean isHolding() {
		return holding;
	}

	public boolean isCharged() {
		return (holding && chargeTimer >= CHARGE_TIME);
	}



	// ==================== MUTATORS ==================== //

	public void onHitMonster() {
		sheatheAfterwards = true;
		if (holding) {
			stab(true);
		}
	}

	public void stab(boolean sheatheOnCompletion) {
		stabbing = true;
		sheatheAfterwards = sheatheOnCompletion;

		player.setBusy(true);
		sprite.newAnimation(Animations.SWORD_STAB);
		sprite.setLooped(false);
		player.setAnimation(false, Animations.PLAYER_STAB);

		if (!sheatheOnCompletion) {
			player.getGame()
					.addEntity(
							new EffectCling(player.getCenter()
									.plus(Direction.lengthVector(16,
											player.getDir())), true));
		}
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public boolean isTouching(Collidable c) {
		return ((holding && Collision.isTouching(swordCollidable, c)) || super
				.isTouching(c));
	}

	@Override
	protected Vector getSwingHitPoint() {
		if (stabbing) {
			return player.getCenter().plus(
					new Vector(Direction.getDirPoint(player.getDir()))
							.scaledBy(16));
		}
		return super.getSwingHitPoint();
	}

	@Override
	protected void sheathe() {
		super.sheathe();
		stabbing = false;
		holding  = false;
		spinning = false;
		player.setItemBusy(false);
	}

	@Override
	protected void onSwing() {
		sheatheAfterwards = false;
	}

	@Override
	protected void onSwingPeak() {
		if (!spinning && level > 0 && entityTracker.canMakeEntity()
				&& player.getHealth() == player.getMaxHealth()) {
			ProjectileSwordBeam beam = new ProjectileSwordBeam(
					player.getGame(), player.getCenter(),
					player.getZPosition(), player.getDir());

			entityTracker.makeEntity(beam);
			player.getGame().addEntity(beam);
			Sounds.ITEM_SWORD_BEAM.play();
		}
	}

	@Override
	protected void onFinishSwing() {
		if (spinning || sheatheAfterwards) {
			sheathe();
			spinning = false;
		}
		else {
			sheathe();
			holding = true;
			chargeTimer = 0;
			sprite.newAnimation(Animations.SWORD);
			player.setItemBusy(true);
		}
	}

	@Override
	public void onEnd() {
		if (holding)
			sheathe();
	}

	@Override
	public void interrupt() {
		if (swinging || spinning || holding || stabbing)
			sheathe();
	}

	@Override
	public void update() {
		super.update();

		if (spinning) {

		}
		else if (stabbing) {
			if (sprite.isAnimationDone()) {
				stabbing = false;
				player.setBusy(false);

				if (sheatheAfterwards) {
					sheathe();
				}
				else {
					holding = true;
					chargeTimer = 0;
					player.resetAnimation();
					sprite.newAnimation(Animations.SWORD);
				}
			}
		}
		else if (holding) {
			if (chargeTimer++ == CHARGE_TIME) {
				sprite.newAnimation(Animations.SWORD_CHARGED);
				sprite.setLooped(true);
				Sounds.ITEM_SWORD_CHARGE.play();
			}

			if (!keyDown()) {
				if (isCharged()) {
					swing(player.getDir() * 2, 9, Animations.SWORD_SPIN,
							Animations.PLAYER_SPIN_SWORD);
					spinning = true;
					Sounds.ITEM_SWORD_SPIN.play();
				}
				else
					sheathe();

				holding = false;
			}
			else if (Keyboard.home.pressed())
				stab(true);
		}
		else if (keyPressed() && (swinging || (!player.isBusy() && !player.isItemBusy()))) {
			swingDefault(player.getDir(), 3, Animations.SWORD_SWING, Animations.PLAYER_SWING);
			swingHitDirs[0] = -1;
			swingHitDirs[1] = -1;
			if (player.isInMineCart())
				sheatheAfterwards = true;
			Sounds.playRandom(Sounds.ITEM_SWORD_SLASH);
		}
	}
}
