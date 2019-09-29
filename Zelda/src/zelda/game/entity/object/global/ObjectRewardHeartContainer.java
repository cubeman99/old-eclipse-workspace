package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.graphics.Animation;


public class ObjectRewardHeartContainer extends ObjectReward {
	public ObjectRewardHeartContainer() {
		super("heart_container");
		falls         = false;
		poofsOnAppear = true;
		imageSheet    = Resources.SHEET_ICONS_LARGE;
		sprite.newAnimation(new Animation(5, 4));
	}

	@Override
	public ObjectRewardHeartContainer clone() {
		return new ObjectRewardHeartContainer();
	}
}
