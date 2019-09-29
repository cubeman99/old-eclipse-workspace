package zelda.common.graphics;

import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;


/**
 * A sprite represents a dynamically updated image that has its own animation
 * composed of multiple frames and parts.
 * 
 * @author David Jordan
 * @author Robert Jordan
 */
public class Sprite {
	private ImageSheet sheet;
	private Point origin;
	private DynamicAnimation animation;
	private int frameIndex;
	private double frameTimer;
	private double speed;
	private boolean loop;
	private int variation;
	private boolean first;


	// ================== CONSTRUCTORS ================== //

	/** Construct a default sprite. */
	public Sprite() {
		this(null, (DynamicAnimation) null);
	}

	/** Construct a sprite with the given image-sheet. */
	public Sprite(ImageSheet sheet) {
		this(sheet, (DynamicAnimation) null);
	}

	/** Construct a sprite with the given animation-strip and image-sheet. */
	public Sprite(ImageSheet sheet, Animation animation) {
		this(sheet, animation, new Point());
	}

	/** Construct a sprite with the given animation-strip and image-sheet. */
	public Sprite(ImageSheet sheet, Animation animation, boolean loop) {
		this(sheet, animation, new Point(), loop);
	}

	/** Construct a sprite with the given animation-strip and image-sheet. */
	public Sprite(ImageSheet sheet, DynamicAnimation animation) {
		this(sheet, animation, new Point());
	}

	/** Construct a sprite with the given animation-strip and image-sheet. */
	public Sprite(ImageSheet sheet, DynamicAnimation animation, boolean loop) {
		this(sheet, animation, new Point(), loop);
	}

	/** Constructs a sprite with the given animation, image-sheet, and origin. */
	public Sprite(ImageSheet sheet, Animation animation, Point origin) {
		this(sheet, animation, origin, true);
	}

	/** Constructs a sprite with the given animation, image-sheet, and origin. */
	public Sprite(ImageSheet sheet, DynamicAnimation animation, Point origin) {
		this(sheet, animation, origin, true);
	}

	public Sprite(ImageSheet sheet, int sx, int sy) {
		this(sheet, new Animation(sx, sy), new Point(), true);
	}

	/**
	 * Constructs a sprite with the given animation, image-sheet, origin, and
	 * loop state.
	 */
	public Sprite(ImageSheet sheet, Animation animation, Point origin,
			boolean loop) {
		this(sheet, new DynamicAnimation(animation), origin, loop);
	}

	/**
	 * Constructs a sprite with the given animation, image-sheet, origin, and
	 * loop state.
	 */
	public Sprite(ImageSheet sheet, DynamicAnimation animation, Point origin,
			boolean loop) {
		if (animation != null)
			this.animation = new DynamicAnimation(animation);
		this.sheet = sheet;
		this.origin = new Point(origin);
		this.frameIndex = 0;
		this.frameTimer = 0.0;
		this.speed = 1.0;
		this.loop = loop;
		this.variation = 0;
		this.first = true;
	}

	public Sprite(Sprite copy) {
		this(copy.sheet, copy.animation, copy.origin, copy.loop);
		speed = copy.speed;
		variation = copy.variation;
	}



	// =================== ACCESSORS =================== //

	public AnimationFrame getCurrentFrame() {
		return getAnimation().getFrame(frameIndex);
	}

	public int getVariation() {
		return variation;
	}

	public boolean isLooped() {
		return loop;
	}

	public int getTime() {
		int time = (int) frameTimer;
		for (int i = 0; i < frameIndex; i++)
			time += getAnimation().getFrame(i).getDuration();
		return time;
	}

	public Animation getAnimation() {
		return animation.getVariant(variation);
	}

	public ImageSheet getSheet() {
		return sheet;
	}

	public Point getOrigin() {
		return origin;
	}

	public double getSpeed() {
		return speed;
	}

	public boolean isAnimationDone() {
		if (animation == null)
			return !loop;
		return (!loop && frameIndex == getAnimation().getNumFrames() - 1 && frameTimer
				+ speed >= getCurrentFrame().getDuration());
	}

	public double getFrameTimer() {
		return frameTimer;
	}

	public int getFrameIndex() {
		return frameIndex;
	}



	// ==================== MUTATORS ==================== //

	public void update(int variation) {
		setVariation(variation);
		update();
	}

	public void update() {
		if (!isAnimationDone() && animation != null && getAnimation() != null) {
			if (!first)
				frameTimer += speed;
			if (frameTimer >= getCurrentFrame().getDuration()) {
				frameTimer -= getCurrentFrame().getDuration();
				frameIndex += 1;
			}
			if (frameIndex >= getAnimation().getNumFrames() && loop) {//
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

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setSheet(ImageSheet sheet) {
		this.sheet = sheet;
	}

	public void setLooped(boolean looped) {
		this.loop = looped;
	}

	public Sprite setOrigin(int x, int y) {
		origin.set(x, y);
		return this;
	}

	public void setOrigin(Point origin) {
		this.origin.set(origin);
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public void setFrameTimer(double frameTimer) {
		this.frameTimer = frameTimer;
	}

	public void setTime(double time) {
		frameTimer = time;
		frameIndex = 0;

		for (int i = 0; i < getAnimation().getNumFrames(); i++) {
			AnimationFrame frame = getAnimation().getFrame(i);

			if (frameTimer - frame.getDuration() >= 0) {
				frameIndex += 1;
				frameTimer -= frame.getDuration();
			}
		}
	}

	public void newAnimation(boolean looped, Animation animation) {
		newAnimation(looped, new DynamicAnimation(animation));
	}

	public void newAnimation(boolean looped, DynamicAnimation animation) {
		this.animation = animation;
		this.loop = looped;
		resetAnimation();
	}

	public void newAnimation(Animation animation) {
		newAnimation(new DynamicAnimation(animation));
	}

	public void newAnimation(DynamicAnimation animation) {
		this.animation = animation;
		resetAnimation();
	}

	public void changeAnimation(Animation animation) {
		changeAnimation(new DynamicAnimation(animation));
	}

	public void changeAnimation(DynamicAnimation animation) {
		this.animation = animation;
		if (getTime() >= getAnimation().getDuration())
			setTime(getTime() % getAnimation().getDuration());
	}

	public void removeAnimation() {
		animation = null;
	}

	public void resetAnimation() {
		frameIndex = 0;
		frameTimer = 0.0;
	}

	/** Draws this sprite's animation at the given position. */
	public void draw(double x, double y) {
		draw(new Vector(x, y));
	}

	/** Draws this sprite's animation at the given position. */
	public void draw(Vector pos) {
		if (animation != null && getAnimation() != null && sheet != null)
			getAnimation().draw(frameIndex, sheet, origin, pos);
	}
}
