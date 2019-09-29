package zelda.game.entity.collectable;

import zelda.game.player.items.Item;


public class RewardItem extends CollectableReward {
	protected Item item;
	protected int level;



	public RewardItem(Item item, int level) {
		super(item.getDefaultName(), item.getSlotIcon(level), item
				.getRewardMessage(level), item.getRewardHoldType(level));

		this.item = item;
		this.level = level;
	}
	
	public RewardItem(RewardItem copy) {
		super(copy);
		this.item  = copy.item;
		this.level = copy.level;
	}
	
	@Override
	public RewardItem clone() {
		return new RewardItem(this);
	}
	
	@Override
	public void collect() {
		item.getPlayer().getInventory().obtainItem(item);
	}
}
