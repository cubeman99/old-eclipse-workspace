package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.object.FrameObject;


public abstract class ObjectReward extends FrameObject {
	protected String rewardName;
	protected CollectableReward reward;
	protected boolean rewardSpawned;
	protected boolean falls;
	
	
	
	public ObjectReward(String rewardName) {
		this.rewardName = rewardName;
		properties.set("reward", rewardName);
		imageSheet = Resources.SHEET_ICONS_THIN;
		sprite.newAnimation(new Animation().addFrame(0, 4, 4, 0));
		
		falls         = true;
		poofsOnAppear = true;
	}
	
	@Override
	public void initialize() {
		rewardName = properties.get("reward", rewardName);
		reward = (CollectableReward) game.collectables
				.getCollectable(rewardName).clone();
		reward.setPosition(position);
		
		if (falls)
			reward.setZPosition(reward.getPosition().y + 16);
		
		rewardSpawned = false;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (!rewardSpawned) {
			rewardSpawned = true;
			game.addEntity(reward);
		}
		
		if (reward.isDestroyed()) {
			objectData.getSource().getProperties().set("enabled", false);
			destroy();
		}
	}
	
	@Override
	public void draw() {
		if (!rewardSpawned && !falls)
			super.draw();
	}

	@Override
	public Point createSpriteSource() {
		return new Point(-1, -1);
	}
}
