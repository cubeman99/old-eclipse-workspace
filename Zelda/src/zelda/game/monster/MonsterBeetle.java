package zelda.game.monster;

import zelda.common.graphics.Animations;
import zelda.common.reactions.ReactionCause;


public class MonsterBeetle extends BasicMonster {
	public MonsterBeetle() {
		super();

		health.fill(1);

		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 30;
		moveTimeMax = 80;
		numDirectionAngles = 8;

		sprite.newAnimation(Animations.createStrip(8, 9, 15, 2));

		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));
	}

	@Override
	public MonsterBeetle clone() {
		return new MonsterBeetle();
	}
}
