package zelda.common.graphics;

import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;


/**
 * Represents a single frame in an animation that lasts for a duration of game
 * ticks and can be drawn in multiple parts.
 * 
 * @author David Jordan
 * @author Robert Jordan
 */
public class AnimationFrame {
	private int duration;
	private FramePart[] parts;



	// ================== CONSTRUCTORS ================== //

	/** Constructs an empty animation frame. */
	public AnimationFrame() {
		duration = 0;
		parts = new FramePart[0];
	}

	/** Constructs an empty animation frame with the given duration. */
	public AnimationFrame(int duration) {
		this.duration = duration;
		this.parts = new FramePart[0];
	}

	/** Constructs an empty animation frame with the given duration part data. */
	public AnimationFrame(int duration, int sx, int sy) {
		this.duration = duration;
		this.parts = new FramePart[] {new FramePart(sx, sy, 0, 0)};
	}

	/** Constructs an empty animation frame with the given duration part data. */
	public AnimationFrame(int duration, int sx, int sy, int dx, int dy) {
		this.duration = duration;
		this.parts = new FramePart[] {new FramePart(sx, sy, dx, dy)};
	}

	/** Constructs an copy of the given animation frame. */
	public AnimationFrame(AnimationFrame frame) {
		duration = frame.duration;
		parts = new FramePart[frame.parts.length];

		for (int i = 0; i < frame.parts.length; i++) {
			parts[i] = new FramePart(frame.parts[i]);
		}
	}



	// =================== ACCESSORS =================== //

	/** Gets the duration of this animation frame in steps. */
	public int getDuration() {
		return duration;
	}

	public int getNumParts() {
		return parts.length;
	}

	public FramePart getPart(int index) {
		return parts[index];
	}



	// ==================== MUTATORS ==================== //

	/** Sets the duration of this animation frame in steps. */
	public AnimationFrame setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/** Add a frame part. */
	public AnimationFrame addPart(int sx, int sy, double dx, double dy) {
		addPart(new Point(sx, sy), new Vector(dx, dy));
		return this;
	}

	/** Add a frame part. */
	public AnimationFrame addPart(Point sourcePos, Vector drawPos) {
		addPart(new FramePart(sourcePos, drawPos));
		return this;
	}

	/** Add a frame part. */
	public AnimationFrame addPart(FramePart part) {
		FramePart[] newParts = new FramePart[parts.length + 1];
		for (int i = 0; i < parts.length; i++)
			newParts[i] = parts[i];

		newParts[parts.length] = part;
		parts = newParts;
		return this;
	}



	// ================= INNER CLASSES ================= //

	/**
	 * A part of an animation frame.
	 * 
	 * @author Robert Jordan
	 * @author David Jordan
	 */
	public static class FramePart {
		private Point sourcePos;
		private Vector drawPos;



		// ================== CONSTRUCTORS ================== //

		/** Constructs the default frame part. */
		public FramePart() {
			sourcePos = new Point();
			drawPos = new Vector();
		}

		/**
		 * Constructs a frame part using the specified tile at the given
		 * location.
		 */
		public FramePart(int sx, int sy, double dx, double dy) {
			this.sourcePos = new Point(sx, sy);
			this.drawPos = new Vector(dx, dy);
		}

		/**
		 * Constructs a frame part using the specified tile at the given
		 * location.
		 */
		public FramePart(Point tile, Vector point) {
			this.sourcePos = new Point(tile);
			this.drawPos = new Vector(point);
		}

		/** Constructs a frame part from the given frame part. */
		public FramePart(FramePart part) {
			this.sourcePos = new Point(part.sourcePos);
			this.drawPos = new Vector(part.drawPos);
		}



		// =================== ACCESSORS =================== //

		public Point getSourcePos() {
			return sourcePos;
		}

		public Vector getDrawPos() {
			return drawPos;
		}
	}


	// ==================== MUTATORS ==================== //

	public void shiftSourcePositions(Point amount) {
		for (int i = 0; i < parts.length; i++)
			parts[i].sourcePos.add(amount);
	}

	public void shiftDrawPositions(Vector amount) {
		for (int i = 0; i < parts.length; i++)
			parts[i].drawPos.add(amount);
	}
}
