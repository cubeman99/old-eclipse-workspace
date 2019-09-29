package com.base.game.wolf3d.tile;

import com.base.engine.common.GMath;

public class PatrolNode extends Tile {

	public PatrolNode() {
		super("Patrol Node", false);
	}
	
	@Override
	public void initialize() {
		setDirection(getData().getInt("direction", 0) * GMath.QUARTER_PI);
	}
	
	@Override
	public PatrolNode clone() {
		return new PatrolNode();
	}
}
