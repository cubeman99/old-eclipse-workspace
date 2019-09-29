package com.base.game.wolf3d.tile;

import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.Game;
import com.base.game.wolf3d.Sprite;

public class ObjectSprite extends ObjectTile {
	private Sprite sprite;
	private DynamicAnimation animation;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public ObjectSprite(Game game, float width, float height, DynamicAnimation animation, float animationSpeed) {
		super("", null, false);
		this.animation = animation;
		
		sprite = new Sprite();
		sprite.newAnimation(false, this.animation);
		sprite.setSpeed(animationSpeed);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Sprite getSprite() {
		return sprite;
	}
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update(float delta) {
		sprite.update(delta);
		setDrawTexture(sprite.getCurrentFrame());
		
		super.update(delta);
	}
}
