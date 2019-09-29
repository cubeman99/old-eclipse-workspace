package zelda.common.graphics;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.AnimationFrame.FramePart;


/**
 * An Animation Strip contains a list of animation frames that last for unique
 * durations.
 * 
 * @author David Jordan
 * @author Robert Jordan
 */
public class Animation {
	private ArrayList<AnimationFrame> animationFrames;



	// ================== CONSTRUCTORS ================== //

	/** Constructs an empty animation strip. */
	public Animation() {
		animationFrames = new ArrayList<AnimationFrame>();
	}

	/** Constructs an animation strip with a single frame. */
	public Animation(int sx, int sy) {
		animationFrames = new ArrayList<AnimationFrame>();
		addFrame(1, sx, sy, 0, 0);
	}

	/** Constructs an animation strip with a single frame. */
	public Animation(Point source) {
		animationFrames = new ArrayList<AnimationFrame>();
		addFrame(1, source.x, source.y, 0, 0);
	}

	/** Constructs an animation strip with a single frame. */
	public Animation(int duration, int sx, int sy, int dx, int dy) {
		animationFrames = new ArrayList<AnimationFrame>();
		addFrame(duration, sx, sy, dx, dy);
	}

	/** Constructs an animation strip with a single frame. */
	public Animation(AnimationFrame frame) {
		animationFrames = new ArrayList<AnimationFrame>();
		addFrame(frame);
	}

	/** Constructs an animation frame a list of frames. */
	public Animation(AnimationFrame... frames) {
		animationFrames = new ArrayList<AnimationFrame>();
		for (int i = 0; i < frames.length; i++)
			animationFrames.add(frames[i]);
	}

	/** Constructs a copy of the given animation strip. */
	public Animation(Animation copy) {
		animationFrames = new ArrayList<AnimationFrame>();

		for (int i = 0; i < copy.animationFrames.size(); i++) {
			animationFrames.add(new AnimationFrame(
					copy.animationFrames.get(i)));
		}
	}



	// =================== ACCESSORS =================== //

	/** Gets the frame at the given index. */
	public AnimationFrame getFrame(int index) {
		return animationFrames.get(index);
	}

	/** Returns the number of frames in this animation. */
	public int getNumFrames() {
		return animationFrames.size();
	}

	/** Gets the total duration of this animation. */
	public int getDuration() {
		int duration = 0;
		for (int i = 0; i < animationFrames.size(); i++)
			duration += animationFrames.get(i).getDuration();

		return duration;
	}



	// ==================== MUTATORS ==================== //

	/** Add an animation frame. */
	public Animation addFrame(AnimationFrame frame) {
		animationFrames.add(new AnimationFrame(frame));
		return this;
	}

	/** Add a new blank animation frame of the given duration. */
	public Animation addFrame(int duration) {
		animationFrames.add(new AnimationFrame(duration));
		return this;
	}

	/** Add a new blank animation frame of the given duration. */
	public Animation addFrame(int duration, FramePart... frameParts) {
		AnimationFrame frame = new AnimationFrame(duration);
		for (int i = 0; i < frameParts.length; i++)
			frame.addPart(frameParts[i]);
		animationFrames.add(frame);
		return this;
	}

	/** Add an animation frame with a single part. */
	public Animation addFrame(int duration, int sx, int sy) {
		return addFrame(duration, sx, sy, 0, 0);
	}

	/** Add an animation frame with a single part. */
	public Animation addFrame(int sx, int sy) {
		return addFrame(1, sx, sy, 0, 0);
	}

	/** Add an animation frame with a single part. */
	public Animation addFrame(int sx, int sy, int dx, int dy) {
		return addFrame(1, sx, sy, dx, dy);
	}

	/** Add an animation frame with a single part. */
	public Animation addFrame(int duration, int sx, int sy, double dx, double dy) {
		AnimationFrame frame = new AnimationFrame(duration);
		frame.addPart(sx, sy, dx, dy);
		animationFrames.add(frame);
		return this;
	}

	/** Add an animation frame with a single part. */
	public Animation addFrame(int duration, Point sourcePos, Vector drawPos) {
		AnimationFrame frame = new AnimationFrame(duration);
		frame.addPart(sourcePos, drawPos);
		animationFrames.add(frame);
		return this;
	}

	/** Add an animation frame with multiple parts. */
	public Animation addFrame(int duration, int... data) {
		AnimationFrame frame = new AnimationFrame(duration);
		for (int i = 0; i < data.length; i += 4) {
			if (i + 3 >= data.length)
				break;

			int sx = data[i + 0];
			int sy = data[i + 1];
			int dx = data[i + 2];
			int dy = data[i + 3];
			frame.addPart(new FramePart(sx, sy, dx, dy));
		}
		animationFrames.add(frame);
		return this;
	}

	public Animation addFramesRepeat(int numRepeats, AnimationFrame... frames) {
		for (int i = 0; i < numRepeats; i++) {
			for (int j = 0; j < frames.length; j++)
				animationFrames.add(frames[j]);
		}
		return this;
	}

	public Animation createFlicker() {
		return createFlicker(1, 0);
	}

	public Animation createFlicker(int flickerInterval) {
		return createFlicker(flickerInterval, 0);
	}

	public Animation createFlicker(int flickerInterval, int startTime) {
		ArrayList<AnimationFrame> newFrames = new ArrayList<AnimationFrame>();
		int totalTime = 0;
		int flickerCount = flickerInterval;

		for (int i = 0; i < animationFrames.size(); i++) {
			for (int j = 0; j < animationFrames.get(i).getDuration(); j++) {
				if (flickerCount >= flickerInterval) {
					newFrames.add(new AnimationFrame(animationFrames.get(i))
							.setDuration(1));
				}
				else {
					newFrames.add(new AnimationFrame(1, -1, -1));
				}

				if (totalTime++ >= startTime) {
					flickerCount++;
					if (flickerCount >= flickerInterval * 2)
						flickerCount = 0;
				}
			}
		}

		animationFrames = newFrames;
		return this;
	}

	public Animation scale(int amount) {
		ArrayList<AnimationFrame> newFrames = new ArrayList<AnimationFrame>();
		for (int i = 0; i < animationFrames.size(); i++) {
			for (int j = 0; j < amount; j++)
				newFrames.add(new AnimationFrame(animationFrames.get(i)));
		}
		animationFrames = newFrames;
		return this;
	}

	public Animation shiftSourcePositions(int amountX, int amountY) {
		return shiftSourcePositions(new Point(amountX, amountY));
	}

	public Animation shiftSourcePositions(Point amount) {
		for (int i = 0; i < animationFrames.size(); i++)
			animationFrames.get(i).shiftSourcePositions(amount);
		return this;
	}

	public Animation shiftDrawPositions(double amountX, double amountY) {
		return shiftDrawPositions(new Vector(amountX, amountY));
	}

	public Animation shiftDrawPositions(Vector amount) {
		for (int i = 0; i < animationFrames.size(); i++)
			animationFrames.get(i).shiftDrawPositions(amount);
		return this;
	}

	/** Remove the animation frame at the given index. */
	public void removeFrame(int index) {
		animationFrames.remove(index);
	}

	/** Remove all animation frames. */
	public void clearFrames() {
		animationFrames.clear();
	}


	public void draw(int frameIndex, ImageSheet sheet, Point origin, Vector pos) {
		Draw.drawAnimationFrame(animationFrames.get(frameIndex), sheet,
				pos.minus(origin.x, origin.y));
	}
}
