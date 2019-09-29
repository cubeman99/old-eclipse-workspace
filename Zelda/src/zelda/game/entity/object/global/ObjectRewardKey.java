package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.graphics.Animation;


public class ObjectRewardKey extends ObjectReward {
	public ObjectRewardKey() {
		super("key");
		falls         = true;
		poofsOnAppear = false;
		imageSheet    = Resources.SHEET_ICONS_THIN;
		sprite.newAnimation(new Animation().addFrame(0, 4, 4, 0));
	}

	@Override
	public ObjectRewardKey clone() {
		return new ObjectRewardKey();
	}
}
