package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.GMath;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.projectile.ProjectileSeed;
import zelda.game.player.Player;
import zelda.main.Keyboard;


public class ItemSeedShooter extends Item {
	private boolean aiming;
	private int aimDir;
	private boolean shooting;
	private Sprite sprite;
	private Vector[] projectilePositions;


	public ItemSeedShooter(Player player) {
		super(player, "SeedShooter");
		name[0] = "Seed Shooter";
		description[0] = "Used to bounce seeds around.";
		rewardMessages[0] = "You got the <red>Seed Shooter<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		usedInMineCart = true;
		numTypes = player.itemSatchel.numTypes;
		typeName = player.itemSatchel.typeName;
		slotTypeIcons = player.itemSatchel.slotTypeIcons;
		typeIcons = player.itemSatchel.typeIcons;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 9, 0);
		setEquippedSlotIcons(Resources.SHEET_ICONS_THIN, 8, 0);

		aiming = false;
		aimDir = 0;
		shooting = false;
		sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_SEED_SHOOTER);

		for (int i = 0; i < numTypes; i++) {
			ammo[i] = player.currencySeeds[i];
			collectableSprites[i] = new Sprite(Resources.SHEET_ICONS_SMALL, i,
					0).setOrigin(4, 6);
		}

		typeDescription = new String[] {"A burst of fire!",
				"An aromatic blast!", "Steals speed?", "A mighty blow!",
				"A producer of unknown effects."};

		projectilePositions = new Vector[] {new Vector(16, 7),
				new Vector(15, -2), new Vector(0, -12), new Vector(-4, -6),
				new Vector(-9, 7), new Vector(-4, 12), new Vector(7, 15),
				new Vector(15, 11)};
	}

	private void shoot() {
		if (hasAmmo()) {
			aiming = false;
			shooting = true;

			useAmmo();

			player.getGame().addEntity(
					new ProjectileSeed(this, typeIndex, player.getPosition()
							.plus(projectilePositions[aimDir]).plus(4, 4 + 7),
							player.getZPosition() + 7, aimDir));

			player.setAnimation(false, Animations.PLAYER_AIM.getVariant(aimDir));
			Sounds.ITEM_SEED_SHOOTER.play();
		}
	}

	@Override
	public void interrupt() {
		aiming   = false;
		shooting = false;
		player.setBusy(false);
		player.resetAnimation();
	}

	@Override
	public void onEnd() {
		// Shoot if drawn.
		if (aiming)
			shoot();
	}

	@Override
	public void update() {
		if (aiming) {
			// Allow Link to turn while the bow is drawn.
			for (int i = 0; i < 4; i++) {
				if (Keyboard.arrows[i].pressed()) {
					int dist0 = (i * 2) - aimDir;
					int dist1 = Math.abs((i * 2) - 8 - aimDir);
					int dist2 = Math.abs((i * 2) + 8 - aimDir);

					if (Math.abs(dist0) < dist1 && Math.abs(dist0) < dist2)
						aimDir = GMath.getWrappedValue(
								aimDir + GMath.sign(dist0), 8);
					else
						aimDir = GMath.getWrappedValue(aimDir
								+ (dist1 < dist2 ? -1 : 1), 8);

					if (aimDir % 2 == 0)
						player.setDir(aimDir / 2);
				}
			}

			player.setAnimation(true, Animations.PLAYER_AIM.getVariant(aimDir));

			if (!keyDown())
				shoot();
		}
		else if (shooting) {
			if (player.isAnimationDone()) {
				shooting = false;
				aiming = false;
				player.resetAnimation();
				player.setBusy(false);
			}
		}
		else if (!player.isBusy() && !player.isItemBusy() && keyPressed()
				&& hasAmmo()) {
			aiming = true;
			aimDir = player.getAngledDir();
			player.setBusy(true);
			player.setAnimation(true, Animations.PLAYER_AIM.getVariant(aimDir));
		}

		if (aiming)
			sprite.update(aimDir);
	}

	@Override
	public void drawOver() {
		if (aiming || shooting) {
			Draw.drawSprite(sprite, player.getPosition().minus(0, player.getZPosition()));
		}
	}
}
