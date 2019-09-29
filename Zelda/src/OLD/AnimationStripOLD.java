package OLD;

import java.util.ArrayList;
import zelda.common.geometry.Point;



public class AnimationStripOLD {
	private SpriteSheetOLD sheet;
	private int length;
	private SpriteOLD[] strip;
	private double speed;
	private Point startPos;



	// ================== CONSTRUCTORS ================== //

	public AnimationStripOLD(int maxLength) {
		sheet = null;
		length = maxLength;
		strip = new SpriteOLD[maxLength];
		speed = 0;
		startPos = new Point();
	}

	public AnimationStripOLD(SpriteSheetOLD sheet, int startX, int startY,
			int length, double speed) {
		this.sheet = sheet;
		this.length = length;
		this.strip = new SpriteOLD[length];
		this.startPos = new Point(startX, startY);

		if (sheet != null) {
			for (int i = 0; i < length; i++)
				strip[i] = sheet.getSprite(startX + i, startY);
		}

		this.speed = speed;
	}

	public AnimationStripOLD(SpriteSheetOLD sheet, int startX, int startY,
			int length) {
		this(sheet, startX, startY, length, 1);
	}

	public AnimationStripOLD(AnimationStripOLD copy, SpriteSheetOLD newSheet) {
		this(newSheet, copy.startPos.x, copy.startPos.y, copy.length,
				copy.speed);
	}

	public AnimationStripOLD(SpriteOLD spr) {
		sheet = spr.getSpriteSheet();
		length = 1;
		strip = new SpriteOLD[] {spr};
		startPos = new Point();
		speed = 0;
	}

	public AnimationStripOLD(ArrayList<SpriteOLD> sprites) {
		this(sprites, 1);
	}

	public AnimationStripOLD(ArrayList<SpriteOLD> sprites, double speed) {
		sheet = null;
		startPos = new Point();
		length = sprites.size();
		strip = new SpriteOLD[length];
		this.speed = speed;

		if (length > 0)
			sheet = sprites.get(0).getSpriteSheet();

		for (int i = 0; i < length; i++) {
			SpriteOLD spr = sprites.get(i);
			strip[i] = spr;
		}
	}

	public AnimationStripOLD(AnimationStripOLD copy) {
		sheet = copy.sheet;
		length = copy.length;
		strip = new SpriteOLD[length];
		startPos = new Point(copy.startPos);
		speed = copy.speed;

		for (int i = 0; i < length; i++)
			strip[i] = copy.strip[i];
	}



	// ==================== MUTATORS ==================== //

	public AnimationStripOLD repeat(int numTimes) {
		SpriteOLD[] newStrip = new SpriteOLD[numTimes * length];

		for (int i = 0; i < numTimes * length; i++)
			newStrip[i] = strip[i % length];

		strip = newStrip;
		length = newStrip.length;
		return this;
	}

	public AnimationStripOLD setTimings(int... repeatsPerSprite) {
		int total = 0;
		for (int i = 0; i < length; i++)
			total += repeatsPerSprite[i];

		SpriteOLD[] newStrip = new SpriteOLD[total];
		int num = 0;

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < repeatsPerSprite[i]; j++)
				newStrip[num++] = strip[i];
		}

		strip = newStrip;
		length = newStrip.length;
		return this;
	}

	public AnimationStripOLD setSheet(SpriteSheetOLD sheet) {
		this.sheet = sheet;
		this.strip = new SpriteOLD[length];

		if (sheet != null) {
			for (int i = 0; i < length; i++)
				strip[i] = sheet.getSprite(startPos.x + i, startPos.y);
		}

		return this;
	}

	public void setStartPos(int sx, int sy) {
		startPos = new Point(sx, sy);
		if (sheet != null) {
			for (int i = 0; i < length; i++)
				strip[i] = sheet.getSprite(sx + i, sy);
		}
	}

	public void setSprite(int index, SpriteOLD spr) {
		if (index < strip.length)
			strip[index] = spr;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}



	// =================== ACCESSORS =================== //

	public SpriteSheetOLD getSpriteSheet() {
		return sheet;
	}

	public SpriteOLD getSprite(int index) {
		return strip[index];
	}

	public SpriteOLD getSprite(SpriteSheetOLD sheet, int index) {
		return sheet.getSprite(startPos.x + index, startPos.y);
	}

	public int length() {
		return length;
	}

	public double getSpeed() {
		return speed;
	}

	public int getLength() {
		return length;
	}
}
