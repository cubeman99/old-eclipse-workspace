package com.base.game.wolf3d;

import java.util.ArrayList;
import com.base.engine.rendering.Texture;

public class SpriteAnimation {
	private ArrayList<Texture> frames;
	private float speed;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public SpriteAnimation() {
		this(60);
	}
	
	public SpriteAnimation(float speed) {
		this.speed = speed;
		frames = new ArrayList<Texture>();
	}
	
	public SpriteAnimation(float speed, Texture... textures) {
		this.speed = speed;
		frames = new ArrayList<Texture>();
		addFrames(textures);
	}
	
	public SpriteAnimation(Texture... textures) {
		this.speed = 60;
		frames = new ArrayList<Texture>();
		addFrames(textures);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public float getDuration() {
		return frames.size() * (1.0f / speed);
	}
	
	public int getNumFrames() {
		return frames.size();
	}
	
	public Texture getFrame(int index) {
		return frames.get(index);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public ArrayList<Texture> getFrames() {
		return frames;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public SpriteAnimation addFrame(Texture texture) {
		frames.add(texture);
		return this;
	}
	
	public void addFrames(Texture... textures) {
		for (int i = 0; i < textures.length; i++)
			frames.add(textures[i]);
	}
}
