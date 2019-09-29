package com.base.game.wolf3d;

import com.base.engine.rendering.Texture;


public class Sprite {
	private DynamicAnimation animation;
	private int frameIndex;
	private int frameIndexPrev;
	private double frameTimer;
	private double speed;
	private boolean loop;
	private boolean first;
	private int variation;
	

	// ================== CONSTRUCTORS ================== //

	/** Construct a default sprite. */
	public Sprite() {
		frameTimer     = 0;
		frameIndex     = 0;
		frameIndexPrev = 0;
		speed          = 60;
		loop           = true;
		first          = true;
		variation      = 0;
	}



	// =================== ACCESSORS =================== //
	
	public Texture getCurrentFrame() {
		return getAnimation().getFrame(frameIndex);
	}
	
	public SpriteAnimation getAnimation() {
		return animation.getVariant(variation);
	}
	
	public DynamicAnimation getDynamicAnimation() {
		return animation;
	}
	
	public int getVariation() {
		return variation;
	}

	public boolean isLooped() {
		return loop;
	}

	public double getSpeed() {
		return speed;
	}
	
	public boolean isOnLastFrame() {
		return (frameIndex == getAnimation().getNumFrames() - 1);
	}
	
	public boolean isAnimationDone(float delta) {
		if (animation == null)
			return !loop;
		return (!loop && frameIndex == getAnimation().getNumFrames() - 1 && frameTimer + delta * speed >= 1);
	}

	public double getFrameTimer() {
		return frameTimer;
	}

	public int getFrameIndex() {
		return frameIndex;
	}
	
	public int getLastFrameIndex() {
		return frameIndexPrev;
	}



	// ==================== MUTATORS ==================== //
	
	public void update(float delta, int variation) {
		setVariation(variation);
		update(delta);
	}
	
	public void update(float delta) {
		frameIndexPrev = frameIndex;
		
		if (!isAnimationDone(delta) && animation != null) {
			if (!first)
				frameTimer += delta * speed;
			if (frameTimer >= 1) {
				frameTimer -= 1;
				frameIndex += 1;
			}
			if (frameIndex >= getAnimation().getNumFrames() && loop) {
				frameIndex = 0;
			}
		}
		first = false;
	}
	
	public void setVariation(int variation) {
		if (animation == null)
			this.variation = variation;
		else
			this.variation = variation % animation.getNumVariants();
	}
	
	public void moveToLastFrame() {
		frameIndex = getAnimation().getNumFrames() - 1;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setLooped(boolean looped) {
		this.loop = looped;
	}
	
	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public void setFrameTimer(double frameTimer) {
		this.frameTimer = frameTimer;
	}

	public void newAnimation(boolean looped, DynamicAnimation animation) {
		this.animation = animation;
		this.loop = looped;
		resetAnimation();
	}

	public void newAnimation(DynamicAnimation animation) {
		this.animation = animation;
		resetAnimation();
	}

	public void changeAnimation(boolean looped, DynamicAnimation animation) {
		this.loop = looped;
		changeAnimation(animation);
	}

	public void changeAnimation(DynamicAnimation animation) {
		this.animation = animation;
		if (frameIndex >= 1)
			frameIndex %= animation.getVariant(variation).getNumFrames();
	}

	public void removeAnimation() {
		animation = null;
	}

	public void resetAnimation() {
		frameIndex = 0;
		frameTimer = 0.0;
	}
}
