package zelda.game.entity.collectable;

import zelda.common.graphics.Sprite;
import zelda.game.world.Dungeon;


public class RewardDungeonItem extends CollectableReward {
	
	public RewardDungeonItem(String name, Sprite sprite, String messageText, int raiseType) {
		super(name, sprite, messageText, raiseType);
	}
	
	public RewardDungeonItem(RewardDungeonItem copy) {
		super(copy);
	}
	
	@Override
	public RewardDungeonItem clone() {
		return new RewardDungeonItem(this);
	}

	@Override
	public void collect() {
		Dungeon dun = game.getLevel().getDungeon();
		if (dun != null)
			dun.acquireItem(name);
	}
}
