package graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;

import geometry.Point;
import geometry.Vector;

/**
 * A class for storing frame information and animating images.
 * @author	Robert Jordan
 */
public class Animation {

	// ======================= Members ========================
	
	/** The collection of frames in the animation. */
	ArrayList<AnimationFrame> frames;

	// ===================== Constructors =====================
	
	/** Constructs an empty animation. */
	public Animation() {
		this.frames			= new ArrayList<AnimationFrame>();
	}
	/** Constructs an animation from the given animation. */
	public Animation(Animation animation) {
		this.frames			= new ArrayList<AnimationFrame>();
	}

	// ======================= Updating =======================
	
	/** Draws the animation at the given point. */
	void draw(Graphics2D g, int currentFrame, Tileset tileset, Point tileOffset, Vector point) {
		for (FramePart part : frames.get(currentFrame).parts) {
			Draw.drawTile(g, tileset, part.tile.plus(tileOffset), part.point.plus(point));
		}
	}
	
	// ======================== Frames ========================

	/** Gets the frame at the given index. */
	public AnimationFrame getFrame(int index) {
		return frames.get(index);
	}
	/** Adds a frame. */
	public void addFrame(AnimationFrame frame) {
		frames.add(new AnimationFrame(frame));
	}
	/** Adds a blank frame. */
	public void addFrame(int length) {
		frames.add(new AnimationFrame(length));
	}
	/** Adds a frame with a single part. */
	public void addFrame(int length, int tileX, int tileY, double x, double y) {
		AnimationFrame frame = new AnimationFrame(length);
		frame.addPart(tileX, tileY, x, y);
		frames.add(frame);
	}
	/** Adds a frame with a single part. */
	public void addFrame(int length, Point tile, Vector point) {
		AnimationFrame frame = new AnimationFrame(length);
		frame.addPart(tile, point);
		frames.add(frame);
	}
	/** Removes the frame at the given index. */
	public void removeFrame(int index) {
		frames.remove(index);
	}
	/** Removes all the frames in the animation. */
	public void clearFrames() {
		frames.clear();
	}
	/** Returns the number of frames in the animation. */
	public int getNumFrames(int index) {
		return frames.size();
	}
	/** Gets the total length of the animation. */
	public int getTotalLength() {
		int totalLength = 0;
		for (int i = 0; i < frames.size(); i++) {
			totalLength += frames.get(i).length;
		}
		return totalLength;
	}
}