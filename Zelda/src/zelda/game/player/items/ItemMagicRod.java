package zelda.game.player.items;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.graphics.Animations;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.projectile.ProjectileRodFire;
import zelda.game.player.Player;


public class ItemMagicRod extends AbstractItemSwing {
	private static final int MAX_PROJECTILES = 2;

	private ArrayList<Entity> projectiles;


	// ================== CONSTRUCTORS ================== //

	public ItemMagicRod(Player player) {
		super(player, "MagicRod");
		name[0] = "Magic Rod";
		description[0] = "Burn, baby burn!";
		rewardMessages[0] = "You got the <red>Magic Rod<red>!<n>Burn baby burn!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 11, 1);

		projectiles = new ArrayList<Entity>();
	}


	public void replaceFireObject(ProjectileRodFire proj, Entity e) {
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i) == proj)
				projectiles.set(i, e);
		}
	}


	// =============== INHERITED METHODS =============== //

	@Override
	protected void onSwingPeak() {
		if (projectiles.size() < MAX_PROJECTILES) {
			ProjectileRodFire proj = new ProjectileRodFire(player, player
					.getPosition().plus(8, 8), player.getDir() * 90);
			player.getGame().addEntity(proj);
			projectiles.add(proj);
			Sounds.ITEM_FIRE_ROD.play();
		}
	}

	@Override
	public void update() {
		super.update();

		if (keyPressed() && (swinging || !player.isBusy())
				&& !player.isItemBusy())
		{
			swingDefault(player.getDir(), 3, Animations.ROD_SWING,
					Animations.PLAYER_SWING);
			Sounds.ITEM_SWING.play();
		}

		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isDestroyed())
				projectiles.remove(i--);
		}
	}
}
