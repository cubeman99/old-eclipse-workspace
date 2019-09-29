package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class BloodPickup extends Pickup {
	private int healthAmount;
	private int healthLimit;
	
	
	public BloodPickup(String name, Texture texture, int healthAmount, int healthLimit) {
		super(name, texture);
		this.healthAmount = healthAmount;
		this.healthLimit = healthLimit;
	}
	
	@Override
	public boolean canCollect() {
		return (getPlayer().getHealth() <= healthLimit);
	}
	
	@Override
	public void onCollect() {
		getPlayer().heal(healthAmount);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public BloodPickup clone() {
		return new BloodPickup(name, texture, healthAmount, healthLimit);
	}
}
