package OLD;



public class AnimationOLD {
	private AnimationStripOLD strip;
	private double subImage;
	private double speed;
	private boolean loop;


	// ================== CONSTRUCTORS ================== //

	public AnimationOLD() {
		this.strip = null;
		this.subImage = 0;
		this.speed = 0;
		this.loop = false;
	}

	public AnimationOLD(SpriteOLD spr) {
		this(new AnimationStripOLD(spr));
	}

	public AnimationOLD(AnimationStripOLD strip) {
		this(strip, true);
	}

	public AnimationOLD(AnimationStripOLD strip, boolean loop) {
		this.strip = strip;
		this.speed = strip.getSpeed();
		this.subImage = -speed;
		this.loop = loop;
	}



	// =================== ACCESSORS =================== //

	public SpriteOLD getCurrentSprite() {
		return strip.getSprite(getSubImage());
	}

	public SpriteOLD getSprite(int subImage) {
		return strip.getSprite(subImage);
	}

	public boolean isLooped() {
		return loop;
	}

	public boolean isDone() {
		return (!loop && subImage + speed >= strip.length());
	}

	public int getSubImage() {
		return Math.max(0, (int) subImage);
	}

	public int getLength() {
		return strip.getLength();
	}



	// ==================== MUTATORS ==================== //

	/** Change the animation strip and reset the sub-image. **/
	public void changeStrip(AnimationStripOLD strip) {
		if (strip != this.strip) {
			this.strip = strip;
			this.subImage = 0;
			this.speed = strip.getSpeed();
		}
	}

	/** Change the animation strip without changing the sub-image. **/
	public void setStrip(AnimationStripOLD strip) {
		if (strip != this.strip) {
			this.strip = strip;
			this.speed = strip.getSpeed();
		}
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void update() {
		if (loop || subImage + speed < strip.length())
			subImage += speed;

		if (loop && subImage >= strip.length())
			subImage -= strip.length();
	}
}
