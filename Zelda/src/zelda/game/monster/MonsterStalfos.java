package zelda.game.monster;

import zelda.common.graphics.Animations;


public class MonsterStalfos extends BasicMonster {
	public MonsterStalfos() {
		super();
		
		health.fill(1);
		
		moveSpeed   = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 30;
		moveTimeMax = 80;
		numDirectionAngles = 16;
		reboundOffWalls = true;

		sprite.newAnimation(Animations.createStrip(6, 11, 9, 2));
	}

	@Override
	public MonsterStalfos clone() {
		return new MonsterStalfos();
	}
}
