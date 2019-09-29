package zelda.common.util;

import java.util.NoSuchElementException;
import java.util.Scanner;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.game.entity.logic.WarpPoint;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.game.world.tile.ObjectTile;

public class Destination {
	private Frame frame;
	private Point position;
	private ObjectTile warpTile;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Destination(Frame frame) {
		this.frame    = frame;
		this.position = null;
		this.warpTile = null;
	}
	
	public Destination(Frame frame, Point pos) {
		this.frame    = frame;
		this.position = null;
		this.warpTile = null;
		if (pos != null)
			position = new Point(pos);
	}
	
	public Destination(Frame frame, ObjectTile t) {
		this.frame    = frame;
		this.position = null;
		this.warpTile = null;
		if (t != null) {
			warpTile = t;
			position = new Point(t.getPosition());
		}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Frame getFrame() {
		return frame;
	}
	
	public ObjectTile getWarpTile() {
		return warpTile;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Vector getPosition(Vector currentPos) {
		return (position == null ? currentPos : new Vector(position));
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static Destination parseDestination(String str, World world) {
		Scanner scanner = new Scanner(str);
		
		try {
			Level level = world.getLevel(scanner.next());
			if (level != null) {
				ObjectTile t = level.getObjectTile(scanner.next());
				if (t != null)
					return new Destination(t.getFrame(), t);
			}
		}
		catch (NoSuchElementException e) {}
		
		System.err.println("Error parsing destination string \"" + str + "\"");
		return null;
	}
}
