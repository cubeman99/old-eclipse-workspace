package projects.trafficSimulator;

import java.awt.Color;
import java.awt.geom.Line2D;
import cmg.graphics.Draw;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Vector;

public class Lane {
	public static final int START     = 0;
	public static final int END       = 1;
	public static final int LEFT      = 0;
	public static final int RIGHT     = 1;
	public static final int NUM_SIDES = 2;
	public static final double WIDTH  = 20;
	
	private Road road;
	private Path path;
	private Path[] sidePaths;
	private int index;
	private boolean reversed;
	private Polygon area;
	private Lane[] connections;
	private int[] connectionEnds;
	
	

	// ================== CONSTRUCTORS ================== //

	public Lane(Road road, int index, boolean reversed) {
		this.road           = road;
		this.path           = new Path();
		this.index          = index;
		this.sidePaths      = new Path[NUM_SIDES];
		this.connections    = new Lane[] {null, null};
		this.connectionEnds = new int[] {0, 0};
		this.reversed       = reversed;
		this.area           = new Polygon();
	}

	
	
	// =================== ACCESSORS =================== //
	
	public Lane getConnection(int endIndex) {
		return connections[endIndex];
	}
	
	public boolean isReversed() {
		return reversed;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public Path getPath() {
		return path;
	}
	
	public Vector getRelativeEnd(int endIndex) {
		return (endIndex == END ? path.getEnd() : path.getStart());
	}
	
	public Vector getAbsoluteEnd(int index) {
		return (reversed == (index == 0) ? path.getEnd() : path.getStart());
	}
	
	public Path getSidePath(int index) {
		return sidePaths[index];
	}

	
	
	// ==================== MUTATORS ==================== //

	public void setAbsoluteConnection(int endIndex, Lane l, int connectedEndIndex) {
		int index             = (reversed ? 1 - endIndex : endIndex);
		connections[index]    = l;
		connectionEnds[index] = connectedEndIndex;
	}
	public void setConnection(int index, Lane l) {
		connections[index] = l;
	}
	
	public void removeConnection(Lane l) {
		for (int i = 0; i < 2; i++) {
    		if (connections[i] == l)
    			connections[i] = null;
		}
	}
	
	public void onDestroy() {
		for (int i = 0; i < 2; i++) {
    		if (connections[i] != null)
    			connections[i].removeConnection(this);
		}
	}
	
	public void refresh() {
		area.clear();
		double r = (index - ((road.numLanes() - 1) * 0.5)) * WIDTH;
		path = new Path(road.getPath()).expand(r);
		
		if (reversed)
			path.reverse();
		
		for (int i = 0; i < NUM_SIDES; i++) {
			if (connections[i] != null && road.numLanes() < connections[i].getRoad().numLanes()) {
				int otherEnd = 0;
				for (int j = 0; j < NUM_SIDES; j++) {
					if (connections[i].getConnection(j) == this) {
						otherEnd = j;
						break;
					}
					if (j == 1)
						System.out.println("ERR");
				}
				getRelativeEnd(i).set(connections[i].getRelativeEnd(otherEnd));
			}
		}
		sidePaths[0] = new Path(path).expand(WIDTH * -0.5);
		sidePaths[1] = new Path(path).expand(WIDTH * 0.5);
		
		int n = path.numVertices();
		for (int i = 0; i < n; i++) {
			area.addVertex(i, sidePaths[0].getVertex(i));
			area.addVertex(sidePaths[1].getVertex(n - i - 1));
		}
	}
	
	public void drawHandle(boolean end, boolean highlighted) {
		if (connections[end ? 1 : 0] != null)
			return;
		
		Vector v = (end ? path.getEnd() : path.getStart());
		Circle c = new Circle(v, WIDTH / 2);

		if (end)
			Draw.setColor(new Color(255, 128, 32));
		else
			Draw.setColor(new Color(32, 128, 255));
		
		Draw.fill(c);
		
		if (highlighted) {
			Draw.setColor(new Color(255, 255, 255, 128));
			Draw.fill(c);
		}
		
		Draw.setColor(end != reversed ? Color.RED : Color.GREEN);
		Draw.draw(c);

		Draw.setColor(Color.WHITE);
		Draw.drawString("" + index, c.position, Draw.CENTER, Draw.MIDDLE);
	}
	
	public void draw(Lane prev, Lane next) {
		Draw.setColor(Color.DARK_GRAY);
		Draw.fill(area);
		
		Draw.setColor(Color.WHITE);
		if ((prev != null &&  prev.isReversed() != reversed) || (prev == null && !reversed)) {
			Draw.setColor(Color.YELLOW);
		}
		else if (prev != null)
			Draw.setStroke(Draw.STROKE_DASHED);
		Draw.draw(sidePaths[reversed ? 1 : 0]);
		Draw.resetStroke();
		
		Draw.setColor(reversed ? Color.yellow : Color.WHITE);
		if (next == null)
			Draw.draw(sidePaths[reversed ? 0 : 1]);
		
		//drawHandle(false, false);
		//drawHandle(true, false);
	}
}
