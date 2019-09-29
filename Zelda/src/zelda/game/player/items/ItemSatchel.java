package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.seeds.EmberSeed;
import zelda.game.entity.seeds.MysterySeed;
import zelda.game.entity.seeds.ScentSeed;
import zelda.game.entity.seeds.Seed;
import zelda.game.player.Player;


public class ItemSatchel extends Item {
	public static final int TYPE_EMBER_SEED = 0;
	public static final int TYPE_SCENT_SEED = 1;
	public static final int TYPE_PEGASUS_SEED = 2;
	public static final int TYPE_GALE_SEED = 3;
	public static final int TYPE_MYSTERY_SEED = 4;

	private Entity[] seedObjects;
	private boolean throwing;

	public ItemSatchel(Player player) {
		super(player, "Satchel");
		name[0] = "Seed Satchel";
		description[0] = "A bag for carrying seeds.";
		rewardMessages[0] = "You got a <red>Seed Satchel<red>! And it has 20 <red>Ember Seeds<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_ONE_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 7, 0);
		setEquippedSlotIcons(Resources.SHEET_ICONS_THIN, 6, 0);
		setSlotTypeIcons(Resources.SHEET_ICONS_THIN, 0, 2, 1, 2, 2, 2, 3, 2, 4,
				2);
		setTypeIcons(Resources.SHEET_ICONS_LARGE, 3, 2, 4, 2, 5, 2, 6, 2, 7, 2);

		numTypes = 5;
		typeName = new String[] {"Ember Seeds", "Scent Seeds", "Pegasus Seeds",
				"Gale Seeds", "Mystery Seeds"};
		typeDescription = new String[] {"A fiery burst.", "Aroma therapy?",
				"Legendary speed.", "A windy trip.",
				"A producer of unknown effects."};
		collectableAmmoAmount = 5;
		usedInMineCart = true;

		for (int i = 0; i < numTypes; i++) {
			ammo[i] = player.currencySeeds[i];
			typeIcons[i].setOrigin(8, 8);
			collectableSprites[i] = new Sprite(Resources.SHEET_ICONS_SMALL, i,
					0).setOrigin(4, 6);
		}

		throwing = false;
		seedObjects = new Entity[numTypes];
	}

	private void throwSeed(Seed seed) {
		Vector vel = Direction.lengthVector(1, player.getDir());
		Vector pos = player.getOrigin().plus(vel.scaledBy(4));

		throwing = true;
		player.setBusy(true);
		player.setAnimation(false, Animations.PLAYER_THROW);

		seed.setPosition(pos);
		seed.setZPosition(6 + player.getZPosition());
		seed.setVelocity(vel.scaledBy(0.75));

		useAmmo();
		player.getGame().addEntity(seed);
	}

	public void setSeedObject(int index, Entity obj) {
		seedObjects[index] = obj;
	}

	private boolean okayToUseType(int type) {
		return (typeIndex == type && seedObjects[type] == null && hasAmmo());
	}

	@Override
	public void interrupt() {
		player.setBusy(false);
		player.resetAnimation();
	}

	@Override
	public void update() {
		for (int i = 0; i < numTypes; i++) {
			if (seedObjects[i] != null && seedObjects[i].isDestroyed())
				seedObjects[i] = null;
		}

		if (throwing) {
			if (player.isAnimationDone()) {
				throwing = false;
				player.setBusy(false);
				player.resetAnimation();
			}
		}
		else if (keyPressed() && !player.isBusy() && !player.isItemBusy()) {
			if (okayToUseType(TYPE_EMBER_SEED)) {
				throwSeed(new EmberSeed(player));
			}
			else if (okayToUseType(TYPE_SCENT_SEED)) {
				throwSeed(new ScentSeed(player));
			}
			else if (okayToUseType(TYPE_PEGASUS_SEED) && !player.isSprinting()) {
				useAmmo();
				Sprite spr = new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
						Animations.EFFECT_PEGASUS_SPRINKLE, false);
				Effect e = new Effect(spr, player.getPosition().plus(8, 0));
				game.addEntity(e);
				player.startSprinting();
			}
			else if (okayToUseType(TYPE_GALE_SEED)) {
				Seed seed = new Seed(player, player.getCenter(), TYPE_GALE_SEED);
				throwSeed(seed);
				seed.setPosition(player.getCenter());
				seed.getVelocity().zero();
				seed.setZVelocity(0.6);
			}
			else if (okayToUseType(TYPE_MYSTERY_SEED)) {
				throwSeed(new MysterySeed(player));
			}
		}
	}
}
