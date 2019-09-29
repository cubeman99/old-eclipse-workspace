package common.graphics;

public class Animation {
	private AnimationStrip strip;
	private double subImage;
	private double speed;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Animation() {
		this.subImage = 0;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Sprite getCurrentSprite() {
		return strip.getSprite((int) subImage);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setStrip(AnimationStrip strip) {
		if (strip != this.strip) {
    		this.strip    = strip;
    		this.subImage = 0;
    		this.speed    = strip.getSpeed();
		}
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void update() {
		subImage += speed;
		
		if (subImage >= strip.length()) {
			subImage -= strip.length();
		}
	}
}
