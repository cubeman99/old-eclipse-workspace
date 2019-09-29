package com.base.game.wolf3d.tile.pickups;

import com.base.engine.common.Vector2f;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.tile.ObjectTile;

public abstract class Pickup extends ObjectTile {

	public Pickup(String name, Texture texture) {
		super(name, texture, false);
	}

	public boolean canCollect() {return true;}
	
	public void onCollect() {}

	
	@Override
	public Pickup clone() {
		return null;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
    	if (canCollect()) {
    		Vector2f pos = getTransform().getPosition().getXZ();
    		Vector2f playerPos = getPlayer().getTransform().getPosition().getXZ();
    		
    		if (pos.distanceTo(playerPos) < 0.5f) {
    			onCollect();
    			destroy();
    		}
		}
	}
}
