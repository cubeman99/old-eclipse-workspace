package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class HealthPickup extends Pickup {
	private int healthAmount;
	
	
	public HealthPickup(String name, Texture texture, int healthAmount) {
		super(name, texture);
		this.healthAmount = healthAmount;
	}
	
	@Override
	public boolean canCollect() {
		return !getPlayer().isAtFullHealth();
	}
	
	@Override
	public void onCollect() {
		getPlayer().heal(healthAmount);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public HealthPickup clone() {
		return new HealthPickup(name, texture, healthAmount);
	}
}
