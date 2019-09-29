package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class AmmoPickup extends Pickup {
	protected int ammoAmount;
	
	
	public AmmoPickup(String name, Texture texture, int ammoAmount) {
		super(name, texture);
		this.ammoAmount = ammoAmount;
	}
	
	@Override
	public boolean canCollect() {
		return !getPlayer().hasMaxAmmo();
	}
	
	@Override
	public void onCollect() {
		getPlayer().giveAmmo(ammoAmount);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public AmmoPickup clone() {
		return new AmmoPickup(name, texture, ammoAmount);
	}
}
