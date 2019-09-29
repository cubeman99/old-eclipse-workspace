package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class LootPickup extends Pickup {
	private int scoreValue;
	
	
	public LootPickup(String name, Texture texture, int score) {
		super(name, texture);
		this.scoreValue = score;
	}
	
	@Override
	public void onCollect() {
		getGame().addScore(scoreValue);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public LootPickup clone() {
		return new LootPickup(name, texture, scoreValue);
	}
}
