package graphics;

import geometry.Point;
import geometry.Vector;

import java.awt.Graphics2D;

/**
 * A class used to hold an animation.
 * @author	Robert Jordan
 */
public class Sprite {

	// ======================= Members ========================
	
	/** The animation used by the sprite. */
	public Animation animation;
	/** The tileset used for the animation. */
	public Tileset tileset;
	/** The offset of the tile index used. */
	public Point tileOffset;
	
	
	/** The current frame the animation is on. */
	private int currentFrame;
	/** The current position in the frame time. */
	private double framePosition;
	/** The frame speed multiplier for the animation. */
	private double speed;
	/** True if the animation just completed. */
	private boolean finished;

	// ===================== Constructors =====================
	
	/** Constructs the default sprite. */
	public Sprite() {
		this.animation		= null;
		this.tileset		= null;
		this.tileOffset		= new Point();
		
		this.currentFrame	= 0;
		this.framePosition	= 0.0;
		this.speed			= 1.0;
		this.finished		= false;
	}
	/** Constructs a sprite with the given animation and tileset. */
	public Sprite(Animation animation, Tileset tileset) {
		this.animation		= animation;
		this.tileset		= tileset;
		this.tileOffset		= new Point();
		
		this.currentFrame	= 0;
		this.framePosition	= 0.0;
		this.speed			= 1.0;
		this.finished		= false;
	}
	/** Constructs a sprite with the given animation and tileset. */
	public Sprite(Animation animation, Tileset tileset, Point tileOffset) {
		this.animation		= animation;
		this.tileset		= tileset;
		this.tileOffset		= new Point(tileOffset);
		
		this.currentFrame	= 0;
		this.framePosition	= 0.0;
		this.speed			= 1.0;
		this.finished		= false;
	}

	// ======================= Updating =======================
	
	/** Called every step to update the sprite's animation. */
	public void update() {
		finished = false;
		if (animation != null && tileset != null) {
			framePosition += speed;
			if (framePosition >= animation.frames.get(currentFrame).length) {
				framePosition = 0;
				currentFrame++;
				if (currentFrame >= animation.frames.size()) {
					currentFrame = 0;
					finished = true;
				}
			}
		}
	}
	/** Draws the sprite's animation at the given point. */
	public void draw(Graphics2D g, double x, double y) {
		draw(g, new Vector(x, y));
	}
	/** Draws the sprite's animation at the given point. */
	public void draw(Graphics2D g, Vector point) {
		if (animation != null && tileset != null)
			animation.draw(g, currentFrame, tileset, tileOffset, point);
	}

	// ===================== Information ======================
	
	/** Sets the current animation. */
	public void setAnimation(Animation animation) {
		this.animation = animation;
		this.framePosition = 0;
		this.currentFrame = 0;
		this.speed = 1;
		this.tileOffset.zero();
		this.finished = false;
	}
	/** Sets the current animation and can optionally keep the current frame position. */
	public void setAnimation(Animation animation, boolean keepPosition) {
		this.animation = animation;
		if (!keepPosition) {
			framePosition = 0;
			currentFrame = 0;
			speed = 1;
			tileOffset.zero();
			finished = false;
		}
	}
	/** Sets the current tileset used by the animation. */
	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}
	/** Sets the current tile offset when drawing tiles. */
	public void setTileOffset(int tileOffsetX, int tileOffsetY) {
		this.tileOffset = new Point(tileOffsetX, tileOffsetY);
	}
	/** Sets the current tile offset when drawing tiles. */
	public void setTileOffset(Point tileOffset) {
		this.tileOffset = new Point(tileOffset);
	}
	/** Gets the current tile offset when drawing tiles. */
	public Point getTileOffset() {
		return new Point(tileOffset);
	}
	/** Resets the animation to the beginning. */
	public void reset() {
		framePosition = 0;
		currentFrame = 0;
	}
	/** Gets the current frame. */
	public int getCurrentFrame() {
		return currentFrame;
	}
	/** Sets the current frame. */
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
		this.framePosition = 0;
		this.finished = false;
	}
	/** Gets the overall frame position. */
	public double getOverallPosition() {
		int overallLength = (int)framePosition;
		for (int i = 0; i < currentFrame; i++) {
			overallLength += animation.frames.get(i).length;
		}
		return overallLength;
	}
	/** Sets the overall frame position. */
	public void setOverallPosition(double overallPosition) {
		for (int i = 0; i < animation.frames.size(); i++) {
			if (animation.frames.get(i).length > overallPosition)
				break;
			else
				overallPosition -= animation.frames.get(i).length;
		}
		this.framePosition = overallPosition;
		this.finished = false;
	}
	/** Gets the frame position. */
	public double getFramePosition() {
		return framePosition;
	}
	/** Sets the frame position. */
	public void setFramePosition(double framePosition) {
		this.framePosition = framePosition;
		this.finished = false;
	}
	/** Gets the animation speed. */
	public double getSpeed() {
		return speed;
	}
	/** Sets the animation speed. */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/** Returns true if the animation just completed. */
	public boolean isAnimationFinished() {
		return finished;
	}
}