package common.graphics;


public class AnimationStrip {
	private SpriteSheet sheet;
	private int length;
	private Sprite[] strip;
	private double speed;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public AnimationStrip(SpriteSheet sheet, int startX, int startY, int length, double speed) {
		this.sheet  = sheet;
		this.length = length;
		this.strip  = new Sprite[length];
		
		for (int i = 0; i < length; i++) {
			strip[i] = sheet.getSprite(startX + i, startY);
		}
		
		this.speed = speed;
	}
	
	public AnimationStrip(SpriteSheet sheet, int startX, int startY, int length) {
		this(sheet, startX, startY, length, 1);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public SpriteSheet getSpriteSheet() {
		return sheet;
	}
	
	public Sprite getSprite(int index) {
		return strip[index];
	}
	
	public int length() {
		return length;
	}
	
	public double getSpeed() {
		return speed;
	}
}
