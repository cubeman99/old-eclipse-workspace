package com.base.game.wolf3d;

public class DynamicAnimation {
	public SpriteAnimation[] animations;



	// ================== CONSTRUCTORS ================== //

	public DynamicAnimation(int numVariants) {
		this.animations = new SpriteAnimation[numVariants];
		for (int i = 0; i < numVariants; i++)
			animations[i] = new SpriteAnimation();
	}

	public DynamicAnimation(SpriteAnimation anim) {
		animations = new SpriteAnimation[] {anim};
	}

//	public DynamicAnimation(SpriteAnimation base, int numVariants, int relX, int relY) {
//		animations = new SpriteAnimation[numVariants];
//
//		if (animations.length > 0) {
//			animations[0] = base;
//			for (int i = 1; i < animations.length; i++)
//				createAnimationVariant(i, new Point(relX, relY));
//		}
//	}

	public DynamicAnimation(SpriteAnimation... animations) {
		this.animations = animations;
	}

	// TODO: Copy constructor
//	public DynamicAnimation(DynamicAnimation copy) {
//		animations = new SpriteAnimation[copy.animations.length];
//		for (int i = 0; i < animations.length; i++)
//			animations[i] = new SpriteAnimation(copy.animations[i]);
//	}



	// =================== ACCESSORS =================== //

	public SpriteAnimation getVariant(int index) {
		return animations[index % animations.length];
	}

	public int getNumVariants() {
		return animations.length;
	}

	public SpriteAnimation getBaseAnimation() {
		if (animations.length == 0)
			return null;
		return animations[0];
	}



	// ==================== MUTATORS ==================== //

	public DynamicAnimation setVariant(int index, SpriteAnimation variant) {
		animations[index] = variant;
		return this;
	}

//	private void createAnimationVariant(int index, Point relPos) {
//		animations[index] = new SpriteAnimation(animations[0])
//				.shiftSourcePositions(relPos.scaledBy(index));
//	}

//	public DynamicAnimation shiftSourcePositions(int amountX, int amountY) {
//		for (int i = 0; i < animations.length; i++)
//			animations[i].shiftSourcePositions(new Point(amountX, amountY));
//		return this;
//	}

//	public DynamicAnimation shiftSourcePositions(Point amount) {
//		for (int i = 0; i < animations.length; i++)
//			animations[i].shiftSourcePositions(amount);
//		return this;
//	}
}
