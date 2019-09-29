package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.graphics.Animation;


public class ObjectRewardHeartPiece extends ObjectReward {
	public ObjectRewardHeartPiece() {
		super("heart_piece");
		falls         = false;
		poofsOnAppear = true;
		imageSheet    = Resources.SHEET_ICONS_LARGE;
		sprite.newAnimation(new Animation(4, 4));
	}

	@Override
	public ObjectRewardHeartPiece clone() {
		return new ObjectRewardHeartPiece();
	}
}
