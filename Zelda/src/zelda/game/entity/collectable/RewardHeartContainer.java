package zelda.game.entity.collectable;

import zelda.common.Resources;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;


public class RewardHeartContainer extends CollectableReward {
	protected Currency currency;
	protected int amount;
	
	public RewardHeartContainer() {
		super("heart_container", new Sprite(Resources.SHEET_ICONS_LARGE, 5, 4),
				"You got a <red>Heart Container<red>!", TYPE_TWO_HAND);
	}

	@Override
	public void collect() {
		game.getPlayer().currencyHearts.fill(game.getPlayer()
				.currencyHearts.getMax() + 4);
	}
	
	@Override
	public RewardHeartContainer clone() {
		return new RewardHeartContainer();
	}
}
