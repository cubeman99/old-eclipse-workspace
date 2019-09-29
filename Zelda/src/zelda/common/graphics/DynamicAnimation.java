package zelda.common.graphics;

import zelda.common.geometry.Point;


public class DynamicAnimation {
	public Animation[] animations;



	// ================== CONSTRUCTORS ================== //

	public DynamicAnimation(int numVariants) {
		this.animations = new Animation[numVariants];
		for (int i = 0; i < numVariants; i++)
			animations[i] = new Animation();
	}

	public DynamicAnimation(Animation anim) {
		animations = new Animation[] {anim};
	}

	public DynamicAnimation(Animation base, int numVariants, int relX, int relY) {
		animations = new Animation[numVariants];

		if (animations.length > 0) {
			animations[0] = base;
			for (int i = 1; i < animations.length; i++)
				createAnimationVariant(i, new Point(relX, relY));
		}
	}

	public DynamicAnimation(Animation... animations) {
		this.animations = animations;
	}

	public DynamicAnimation(DynamicAnimation copy) {
		animations = new Animation[copy.animations.length];
		for (int i = 0; i < animations.length; i++)
			animations[i] = new Animation(copy.animations[i]);
	}



	// =================== ACCESSORS =================== //

	public Animation getVariant(int index) {
		return animations[index % animations.length];
	}

	public int getNumVariants() {
		return animations.length;
	}

	public Animation getBaseAnimation() {
		if (animations.length == 0)
			return null;
		return animations[0];
	}



	// ==================== MUTATORS ==================== //

	public DynamicAnimation setVariant(int index, Animation variant) {
		animations[index] = variant;
		return this;
	}

	private void createAnimationVariant(int index, Point relPos) {
		animations[index] = new Animation(animations[0])
				.shiftSourcePositions(relPos.scaledBy(index));
	}

	public DynamicAnimation shiftSourcePositions(int amountX, int amountY) {
		for (int i = 0; i < animations.length; i++)
			animations[i].shiftSourcePositions(new Point(amountX, amountY));
		return this;
	}

	public DynamicAnimation shiftSourcePositions(Point amount) {
		for (int i = 0; i < animations.length; i++)
			animations[i].shiftSourcePositions(amount);
		return this;
	}
}
