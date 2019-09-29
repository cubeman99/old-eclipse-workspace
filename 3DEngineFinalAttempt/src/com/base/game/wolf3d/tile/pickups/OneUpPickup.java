package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class OneUpPickup extends Pickup {
	
	public OneUpPickup(String name, Texture texture) {
		super(name, texture);
	}
	
	@Override
	public void onCollect() {
		getGame().addLife();
		getPlayer().heal(100);
		getPlayer().giveAmmo(25);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public OneUpPickup clone() {
		return new OneUpPickup(name, texture);
	}
}
