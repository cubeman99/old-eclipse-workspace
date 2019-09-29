package zelda.game.entity.collectable;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.graphics.Sprite;
import zelda.game.control.GameInstance;
import zelda.game.player.Inventory;
import zelda.game.player.Player;
import zelda.game.player.items.Item;


public class Collectables {
	private GameInstance game;
	private ArrayList<CollectableReward> rewards;


	public Collectables(GameInstance game) {
		this.game = game;
		rewards = new ArrayList<CollectableReward>();
		final Player player = game.getPlayer();


		addReward(new RewardCurrency("rupees_1", new Sprite(
				Resources.SHEET_ICONS_THIN, 0, 3), 1, player.currencyRupees,
				"You got<n><red>1 Rupee<red>!<n>That<ap>s terrible."));

		addReward(new RewardCurrency("rupees_5", new Sprite(
				Resources.SHEET_ICONS_THIN, 1, 3), 5, player.currencyRupees,
				"You got<n><red>5 Rupees<red>!<n>That<ap>s nice."));
		
		addReward(new RewardCurrency("rupees_10", new Sprite(
				Resources.SHEET_ICONS_THIN, 2, 3), 10, player.currencyRupees,
				"You got<n><red>10 Rupees<red>!<n>That<ap>s nice."));

		addReward(new RewardCurrency("rupees_20", new Sprite(
				Resources.SHEET_ICONS_THIN, 2, 3), 20, player.currencyRupees,
				"You got<n><red>30 Rupees<red>!<n>That<ap>s nice."));

		addReward(new RewardCurrency("rupees_30", new Sprite(
				Resources.SHEET_ICONS_THIN, 2, 3), 30, player.currencyRupees,
				"You got<n><red>30 Rupees<red>!<n>That<ap>s nice."));

		addReward(new RewardCurrency("rupees_50", new Sprite(
				Resources.SHEET_ICONS_THIN, 2, 3), 50, player.currencyRupees,
				"You got<n><red>30 Rupees<red>!<n>That<ap>s nice."));
		
		addReward(new RewardCurrency("rupees_100", new Sprite(
				Resources.SHEET_ICONS_LARGE, 0, 4), 100, player.currencyRupees,
				"You got<n><red>100 Rupees<red>!<n>That<ap>s nice."));
		
		addReward(new RewardHeartContainer());
		addReward(new RewardHeartPiece());
		
		
		
		// DUNGEON
		
		addReward(new RewardDungeonItem("key", new Sprite(
				Resources.SHEET_ICONS_THIN, 0, 4),
				"You found a <red>Small Key<red>!<n>Use it to open a locked door or block in this dungeon.",
				CollectableReward.TYPE_RAISE));
		
		addReward(new RewardDungeonItem("boss_key", new Sprite(
				Resources.SHEET_ICONS_LARGE, 2, 5),
				"You found the <red>Boss Key<red>!",
				CollectableReward.TYPE_RAISE));
		
		addReward(new RewardDungeonItem("map", new Sprite(
				Resources.SHEET_ICONS_LARGE, 0, 5),
				"It<ap>s a <red>Dungeon Map<red>! Press SELECT to see it. The darkened rooms are ones you haven<ap>t been to yet.",
				CollectableReward.TYPE_TWO_HAND));
		
		addReward(new RewardDungeonItem("compass", new Sprite(
				Resources.SHEET_ICONS_LARGE, 1, 5),
				"You found the <red>Compass<red>!<n>Use it to track your position, locate chests, and find keys.",
				CollectableReward.TYPE_TWO_HAND));
		
	}

	public CollectableReward getCollectable(String name) {
		for (int i = 0; i < rewards.size(); i++) {
			if (rewards.get(i).getName().equals(name)) {
				return rewards.get(i);
			}
		}
		Inventory inv = game.getPlayer().getInventory();
		for (int i = 0; i < inv.getNumItems(); i++) {
			Item item = inv.getItem(i);
			if (item.getDefaultName().equals(name)) {
				int level = item.getLevel();
				if (inv.hasItem(item))
					level++;
				item.setLevel(level);
				return new RewardItem(item, level);
			}
		}
		return null;
	}

	public void addReward(CollectableReward reward) {
		rewards.add(reward);
		reward.setGame(game);
	}
}
