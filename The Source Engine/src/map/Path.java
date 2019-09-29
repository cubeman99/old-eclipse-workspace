package map;

import java.util.ArrayList;
import common.Vector;


/**
 * This represents a path that a unit can
 * follow. The path finder will output an
 * object of this type representing the shortest
 * path between two points.
 * 
 * @author David Jordan
 */
public class Path {
	private ArrayList<Vector> points;
	private int followIndex;
	private boolean completed;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Path() {
		this.points      = new ArrayList<Vector>();
		this.followIndex = 0;
		this.completed   = false;
	}
	
	public Path(ArrayList<Node> nodePath) {
		this();
		for (int i = 0; i < nodePath.size(); i++) {
			addPoint(nodePath.get(i));
		}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the current point to move to on the path. **/
	public Vector getCurrentPoint() {
		return points.get(followIndex);
	}
	
	/** Return the point at a given index. **/
	public Vector getPoint(int index) {
		return points.get(index);
	}
	
	/** Return the length (in points) of the path. **/
	public int getLength() {
		return points.size();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Add a point to the path. **/
	public void addPoint(Vector point) {
		points.add(point);
	}
	
	/** Remove the last point on the path. **/
	public void removeLastPoint() {
		points.remove(points.size() - 1);
	}

	/** Remove the first point on the path. **/
	public void removeFirstPoint() {
		points.remove(0);
	}
	
	/** Move on to the next point on the path. **/
	public boolean nextPoint() {
		followIndex++;
		if (followIndex >= getLength())
			completed = true;
		return completed;
	}
}
