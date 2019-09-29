package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;

public class KeyPickup extends Pickup {
	private int keyIndex;
	
	public KeyPickup(String name, Texture texture, int keyIndex) {
		super(name, texture);
		this.keyIndex = keyIndex;
	}
	
	@Override
	public void onCollect() {
		getPlayer().giveKey(keyIndex);
		getGame().setFade(Draw2D.YELLOW, 0.1f);
	}
	
	@Override
	public KeyPickup clone() {
		return new KeyPickup(name, texture, keyIndex);
	}
}
