package com.base.game.wolf3d.tile;

import com.base.engine.common.Rect2f;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.ImageDrawer;

public class ObjectTile extends Tile {
	protected Texture texture;
	protected ImageDrawer imageDrawer;
	
	
	public ObjectTile(String name, Texture texture, boolean solid) {
		super(name, solid);
		this.solid = solid;
		this.texture = texture;
	}
	
	public ImageDrawer getImageDrawer() {
		return imageDrawer;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setDrawTexture(Texture texture) {
		this.texture = texture;
		imageDrawer.setTexture(texture);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public void initialize() {
		imageDrawer = new ImageDrawer(getPlayer(), 1, 1, texture);
		addAttatchment(imageDrawer);
	}
	
	@Override
	public ObjectTile clone() {
		return new ObjectTile(name, texture, solid);
	}
	
	@Override
	public Rect2f getCollisionBox() {
		return new Rect2f(getTransform().getPosition().x - 0.5f,
				getTransform().getPosition().z - 0.5f, 1, 1);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		imageDrawer.setTexture(texture);
	}
}
