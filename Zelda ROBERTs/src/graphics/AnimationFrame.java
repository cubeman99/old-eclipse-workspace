package graphics;

import geometry.Point;
import geometry.Vector;

/**
 * A frame used for animations. These are constructed of frame parts.
 * @author	Robert Jordan
 */
public class AnimationFrame {

	// ======================= Members ========================
	
	/** How long the frame lasts for. */
	int length;
	/** The list of parts used in the frame. */
	FramePart[] parts;
	//ArrayList<FramePart> parts;

	// ===================== Constructors =====================
	
	/** Constructs an empty animation frame. */
	public AnimationFrame() {
		this.length	= 0;
		this.parts	= new FramePart[0];
		//this.parts	= new ArrayList<FramePart>();
	}
	/** Constructs an empty animation frame with the given length. */
	public AnimationFrame(int length) {
		this.length	= length;
		this.parts	= new FramePart[0];
		//this.parts	= new ArrayList<FramePart>();
	}
	/** Constructs an animation frame from the given animation frame. */
	public AnimationFrame(AnimationFrame frame) {
		this.length	= frame.length;
		this.parts	= new FramePart[frame.parts.length];
		//this.parts	= new ArrayList<FramePart>();
		for (int i = 0; i < frame.parts.length; i++) {
		//for (FramePart part : frame.parts) {
			this.parts[i] = new FramePart(frame.parts[i]);
			//this.parts.add(new FramePart(part));
		}
	}

	// ===================== Information ======================
	
	/** Gets the length of the frame in steps. */
	public int getLength() {
		return length;
	}
	/** Sets the length of the frame in steps. */
	public void setLength(int length) {
		this.length = length;
	}
	
	// ===================== Frame Parts ======================
	
	/** Adds a frame part to the frame. */
	public void addPart(int tileX, int tileY, double x, double y) {
		addPart(new Point(tileX, tileY), new Vector(x, y));
		//parts.add(new FramePart(tileX, tileY, x, y));
	}
	/** Adds a frame part to the frame. */
	public void addPart(Point tile, Vector point) {
		FramePart[] newParts = new FramePart[parts.length + 1];
		for (int i = 0; i < parts.length; i++) {
			newParts[i] = parts[i];
		}
		newParts[parts.length] = new FramePart(tile, point);
		parts = newParts;
		//parts.add(new FramePart(tile, point));
	}
}

/**
 * A part of a frame used to draw the whole image.
 * @author	Robert Jordan
 */
class FramePart {
	
	// ======================= Members ========================
	
	/** The index of the tile. */
	Point tile;
	/** The point to draw the part at. */
	Vector point;

	// ===================== Constructors =====================
	
	/** Constructs the default frame part. */
	FramePart() {
		this.tile		= new Point();
		this.point		= new Vector();
	}
	/** Constructs a frame part using the specified tile at the given location. */
	FramePart(int tileX, int tileY, double x, double y) {
		this.tile		= new Point(tileX, tileY);
		this.point		= new Vector(x, y);
	}
	/** Constructs a frame part using the specified tile at the given location. */
	FramePart(Point tile, Vector point) {
		this.tile		= new Point(tile);
		this.point		= new Vector(point);
	}
	/** Constructs a frame part from the given frame part. */
	FramePart(FramePart part) {
		this.tile		= new Point(part.tile);
		this.point		= new Vector(part.point);
	}
}