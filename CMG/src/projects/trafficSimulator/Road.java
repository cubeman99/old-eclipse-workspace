package projects.trafficSimulator;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Road {
	public static final int MAX_LANES = 20;
	private Map map;
	private MapEditor editor;
	private ArrayList<Lane> lanes;
	private Path path;
	private boolean selected;
	private int selectIndex;
	private int selectEnd;
	private int selectCount;
	private boolean dragging;
	private boolean draggingReversed;
	
	
	public Road(Map map, int numLanes, boolean[] setup) {
		this.map         = map;
		this.path        = new Path();
		this.lanes       = new ArrayList<Lane>();
		this.selected    = false;
		this.selectIndex = 0;
		this.selectEnd   = 0;
		this.selectCount = 0;
		this.editor      = null;
		this.dragging    = false;
		this.draggingReversed = false;
		
		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane(this, i, setup[i]));
		}
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public boolean isDraggingReversed() {
		return draggingReversed;
	}
	
	public MapEditor getEditor() {
		return editor;
	}
	
	public void setEditor(MapEditor editor) {
		this.editor = editor;
	}
	
	public Path getPath() {
		return path;
	}
	
	public Map getMap() {
		return map;
	}
	
	public int numLanes() {
		return lanes.size();
	}

    public boolean isSelected() {
    	return selected;
    }
    
    public int getSelectIndex() {
		return selectIndex;
	}
    
    public int getSelectCount() {
		return selectCount;
	}
	
	public Lane getLane(int index) {
		return lanes.get(index);
	}
	
	public void addVertex(Vector v, boolean reversed) {
		if (reversed)
			path.addVertex(0, v);
		else
			path.addVertex(v);
		refresh();
		System.out.println("- adding vertex");
	}
	
	public void removeVertex(boolean reversed) {
		path.removeVertex(reversed ? 0 : path.numVertices() - 1);
		refresh();
	}
	
	public void refresh() {
		for (int i = 0; i < lanes.size(); i++)
			lanes.get(i).refresh();
	}
	
	public Vector getHandlePosition(int laneIndex, int endIndex) {
		Line l = new Line(lanes.get(0).getAbsoluteEnd(endIndex),
				lanes.get(lanes.size() - 1).getAbsoluteEnd(endIndex));
		l.end2 = l.end1.plus(path.getEdge(endIndex
				* (path.numEdges() - 1)).getVector().getPerpendicular());
		return l.end1.plus(l.getVector()
				.setLength(laneIndex * Lane.WIDTH));
	}
	
	public Vector getConnectPosition(int laneIndex, int size, int endIndex) {
		Line l = new Line(lanes.get(0).getAbsoluteEnd(endIndex),
				lanes.get(lanes.size() - 1).getAbsoluteEnd(endIndex));
		
		if (Mouse.getVector().distanceToLine(l) < Lane.WIDTH) {
			Vector lineVector = l.getVector();
			double dist = (laneIndex + ((size - 1) * 0.5)) * Lane.WIDTH;
			return l.end1.plus(lineVector.setLength(dist));
		}
		return null;
	}
	
	public boolean sewRoad(int index, int endIndex, int otherEndIndex, Road road) {
		
		System.out.println("Sewing Roads...");
		System.out.println(" | Index         = " + index);
		System.out.println(" | EndIndex      = " + endIndex);
		System.out.println(" | OtherEndIndex = " + otherEndIndex);
		

		Line line = new Line(lanes.get(0).getAbsoluteEnd(endIndex),
				lanes.get(lanes.size() - 1).getAbsoluteEnd(endIndex));
		
		for (int i = 0; i < numLanes(); i++) {
			int otherIndex = index + i;
			Lane otherLane = road.getLane(otherIndex);
			
			boolean rev    = (endIndex == otherEndIndex);// == otherLane.isReversed();
			int myIndex    = (rev ? numLanes() - i - 1 : i);
			
			Lane myLane    = lanes.get(myIndex);
			myLane.setConnection(endIndex, otherLane);
			otherLane.setConnection(otherLane.isReversed() ? 1 - otherEndIndex : otherEndIndex, myLane);
			
			refresh();
			
//			if (myIndex >= 0 && myIndex <= lanes.size()) {
//				
//			}
		}
		
		
		stopDragging();
		return true;
	}
	
	public void startDragging(int endIndex) {
		System.out.println("Dragging Road.");
		
		draggingReversed = !GMath.toBool(endIndex);
		dragging         = true;
		editor.applySetup(this);
		editor.setDragging(true);
		editor.setDragRoad(this);
		addVertex(Mouse.getVector(), draggingReversed);
	}
	
	public void stopDragging() {
		System.out.println("Dragging Stopped.");
		dragging = false;
		editor.setDragging(false);
	}
	
	public void extendNewRoad() {
		System.out.println("Extending New Road.");
		//stopDragging();
		
		// Create lane setup for new road.
		boolean[] setup = new boolean[selectCount];
		for (int i = 0; i < selectCount; i++) {
			int index = (selectIndex + i);
			setup[i] = false;
			if (index >= 0 && index < lanes.size())
				setup[i] = lanes.get(index).isReversed();
		}
		
		// Create the new road.
		Road road = new Road(map, selectCount, setup);
		road.setEditor(editor);
		road.addVertex(Mouse.getVector(), false);
		map.addRoad(road);
		
		// Make connections.
		for (int i = 0; i < selectCount; i++) {
			int index = (selectIndex + i);
			Lane newLane = road.getLane(i);
			
			if (index >= 0 && index < lanes.size()) {
				Lane oldLane = lanes.get(index);
				oldLane.setAbsoluteConnection(selectEnd, newLane, 1 - selectEnd);
				newLane.setAbsoluteConnection(1 - selectEnd, oldLane, selectEnd);
			}
		}
		
		// Start dragging the new road.
		road.startDragging(selectEnd);
	}
	
	public void startDragging(int index, int endIndex, Vector pos) {
		boolean reversed = GMath.toBool(endIndex);
		int size = editor.getNumLanes();
		if (!editor.canDrag())
			return;

		if (pos == null)
			pos = getConnectPosition(index, size, endIndex);
		
		if (index == 0 && size == numLanes()) {
			// Start dragging this road.
			dragging = true;
			draggingReversed = !reversed;
			addVertex(Mouse.getVector(), draggingReversed);
			
			editor.applySetup(this);
			System.out.println("Extending Road...");
		}
		else {
			// Drag a new road connected to this one.
			draggingReversed = false;
			boolean[] setup = new boolean[size];
			
			for (int i = 0; i < size; i++) {
				int ind = (!reversed ? index + size - i - 1 : index + i);
				if (ind >= 0 && ind < lanes.size()) {	
    				Lane l   = lanes.get(ind);
    				setup[i] = !l.isReversed();
    				if (reversed)
    					setup[i] = !setup[i];
				}
				else
					setup[i] = true;
			}
			
			Road r = new Road(map, size, setup);
			r.setEditor(editor);
			r.addVertex(pos, false);
			Vector next = new Vector(pos);
			next.add(path.getEdge(!reversed ? 0 : path.numEdges() - 1).getVector().setLength(reversed ? 32 : -32));
			r.addVertex(next, false);
			
			for (int i = 0; i < size; i++) {
				Lane l = r.getLane(i);
				int ind = (!reversed ? index + size - i - 1 : index + i);
				l.setConnection(l.isReversed() ? 1 : 0, getLane(index + i));
				getLane(index + i).setConnection(reversed != getLane(index + i).isReversed() ? 1 : 0, l);
			}
			
			r.startDragging(0, 1, pos);
			map.addRoad(r);
			System.out.println("Extending New Road...");
		}
		
		editor.setDragging(true);
		editor.setDragRoad(this);
	}
	
	public void onDestroy() {
		for (int i = 0; i < lanes.size(); i++)
			lanes.get(i).onDestroy();
	}
	
	public Point getSelectResult() {
		Vector ms = Mouse.getVector();
		int n = editor.getNumLanes();

		for (int i = 0; i < 2; i++) {
			Line l = new Line(lanes.get(0).getAbsoluteEnd(i),
					lanes.get(lanes.size() - 1).getAbsoluteEnd(i));
			if (ms.distanceToLine(l) < Lane.WIDTH) {
				Vector lineVector = l.getVector();
				Vector fromVector = ms.minus(l.end1);
				double dist = fromVector.scalarProjection(lineVector) - ((n / 2.0) * Lane.WIDTH);
				int index   = GMath.floor(dist / Lane.WIDTH) + 1;
				if (index > -n && index < numLanes())
					return new Point(index, i);
			}
		}
		return null;
	}
	
	private void checkSelection() {
		Vector ms   = Mouse.getVector();
		selectCount = editor.getNumLanes();
		selected    = false;
		
		for (int i = 0; i < 2; i++) {
			Line l = new Line(lanes.get(0).getAbsoluteEnd(i),
					lanes.get(lanes.size() - 1).getAbsoluteEnd(i));
			if (ms.distanceToLine(l) < Lane.WIDTH) {
				Vector lineVector = l.getVector();
				Vector fromVector = ms.minus(l.end1);
				double dist = fromVector.scalarProjection(lineVector)
						- ((selectCount / 2.0) * Lane.WIDTH);
				selectIndex = GMath.floor(dist / Lane.WIDTH) + 1;
				
				if (selectIndex > -selectCount && selectIndex < numLanes()) {
					selected  = true;
					selectEnd = i;
					return;
				}
			}
		}
	}
	
	public void update() {
		Vector ms = Mouse.getVector();
		selected = false;
		
		checkSelection();
		
		/*
		
		if (Mouse.left.pressed() && !editor.isDragging() && editor.canDrag()) {
			Point result = getSelectResult();
			if (result != null)
				startDragging(result.x, result.y, null);
		}
		
		*/
		
		if (dragging) {
			path.getVertex(draggingReversed ? 0 : path.numVertices() - 1).set(ms);
			refresh();
			
			if (Mouse.left.pressed() && editor.canDrag()) {
				boolean sewed = false;
				for (int i = 0; i < map.getRoads().size(); i++) {
					Road r = map.getRoads().get(i);
					if (r != this) {
						Point result = r.getSelectResult();
						if (result != null && (r != this || draggingReversed == (result.y == 1))) {
							sewed = true;
							sewRoad(result.x, GMath.bool(!draggingReversed), result.y, r);
							break;
						}
					}
				}
				if (dragging && !sewed)
					addVertex(ms, draggingReversed);
			}
			else if (Mouse.right.pressed()) {
				removeVertex(draggingReversed);
				stopDragging();
			}
			else if (Keyboard.backspace.pressed()) {
				removeVertex(draggingReversed);
				if (path.numEdges() == 0)
					stopDragging();
			}
		}
		else if (!editor.isDragging() && selected && Mouse.left.pressed()) {
			if (selectIndex == 0 && selectCount == lanes.size())
				startDragging(selectEnd);
			else
				extendNewRoad();
		}
	}
	
	public void drawHandle(int laneIndex, int endIndex, Vector pos, boolean highlighted) {
		Circle c = new Circle(pos, Lane.WIDTH / 2);
		
		if (laneIndex >= 0 && laneIndex < lanes.size()) {
			Lane l = lanes.get(laneIndex);
			
    		Draw.setColor(l.isReversed() == (endIndex == 0) ? 
    				new Color(255, 128, 32) : new Color(32, 128, 255));
    		Draw.fill(c);
		}

		if (highlighted) {
			Draw.setColor(new Color(255, 255, 255, 128));
			Draw.fill(c);
		}
		
		Draw.setColor(Color.WHITE);
		Draw.draw(c);
		Draw.resetStroke();
		
		Draw.setColor(Color.BLACK);
		Draw.drawString(laneIndex + "", c.position, Draw.CENTER, Draw.MIDDLE);
		Draw.setColor(endIndex == 1 ? Color.RED : Color.GREEN);
		Draw.drawString(laneIndex + "", c.position.minus(1, 1), Draw.CENTER, Draw.MIDDLE);
	}
	
	public void draw() {
		for (int i = 0; i < lanes.size(); i++) {
			Lane prev = (i > 0 ? lanes.get(i - 1) : null);
			Lane next = (i < lanes.size() - 1 ? lanes.get(i + 1) : null);
			lanes.get(i).draw(prev, next);
		}
		
		Vector ms = Mouse.getVector();
		
		Vector closest = path.getClosestPoint(Mouse.getVector());
		if (dragging || (closest != null && closest.distanceTo(ms) < Lane.WIDTH * numLanes() * 0.5)) {
			Draw.setColor(Color.WHITE);
			for (int i = 1; i < path.numVertices() - 1; i++) {
				Draw.fillCircle(path.getVertex(i), 3);
			}
		}
		
		int min = 0;
		int max = lanes.size();
		if (selected) {
			min = Math.min(selectIndex, min);
			max = Math.max(selectIndex + selectCount, max);
		}
		for (int laneIndex = min; laneIndex < max; laneIndex++) {
			Lane lane = null;
			if (laneIndex >= 0 && laneIndex < lanes.size())
				lane = lanes.get(laneIndex);
			
			for (int endIndex = 0; endIndex < 2; endIndex++) {
				boolean highlighted = (selected && laneIndex >= selectIndex
						&& selectEnd == endIndex
						&& laneIndex < selectIndex + selectCount);
				boolean connected = false;
				
				if (lane != null)
					connected = (lane.getConnection(lane.isReversed() ? 1 - endIndex : endIndex) != null);
					
				
				Vector v = getHandlePosition(laneIndex, endIndex);
				if (!connected && (highlighted || (laneIndex >= 0 && laneIndex < lanes.size())))
					drawHandle(laneIndex, endIndex, v, highlighted);
			}
		}
	}
}
