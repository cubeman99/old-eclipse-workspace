package projects.trafficSimulator;

import java.awt.Color;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Point;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Vector;



public class MapEditor {
	private GameRunner runner;
	private Map map;
	private boolean dragging;
	private Road dragRoad;
	private int numLanes;
	private boolean[] laneSetup;
	private boolean canDrag;
	
	public MapEditor(GameRunner runner) {
		this.runner = runner;
		this.map    = new Map();
		dragging    = false;
		dragRoad    = null;
		numLanes    = 2;
		laneSetup   = new boolean[Road.MAX_LANES];
	}
	
	public Map getMap() {
		return map;
	}
	
	public boolean canDrag() {
		return canDrag;
	}
	
	public boolean[] getLaneSetup() {
		return laneSetup;
	}
	
	public int getNumLanes() {
		return numLanes;
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public Road getDragRoad() {
		return dragRoad;
	}
	
	public void applySetup(Road r) {
		numLanes = r.numLanes();
		for (int i = 0; i < numLanes; i++)
			laneSetup[i] = r.getLane(i).isReversed();
	}
	
	public void setDragRoad(Road dragRoad) {
		this.dragRoad = dragRoad;
	}
	
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
		canDrag = false;
	}
	
	public void startDragging(Road road) {
		if (!dragging) {
			dragging = true;
			dragRoad = road;
		}
	}
	
	public void update() {
		Vector ms = Mouse.getVector();
		
		if (!dragging && Keyboard.backspace.pressed()) {
			if (map.getRoads().size() > 0)
				map.getRoads().remove(map.getRoads().size() - 1);
		}
		
		map.update();
		
		if (Mouse.wheelUp())
			numLanes = Math.min(Road.MAX_LANES, numLanes + 1);
		if (Mouse.wheelDown())
			numLanes = Math.max(1, numLanes - 1);
		for (int i = 0; i < numLanes; i++)
			laneSetup[i] = (i < numLanes / 2);
			
		
		if (!dragging) {
			if (Mouse.left.pressed() && canDrag) {
				Road r = new Road(map, numLanes, laneSetup);
				r.setEditor(this);
				r.addVertex(ms, false);
				r.startDragging(Lane.END);
				//r.startDragging(0, Lane.END, ms);
				map.addRoad(r);
			}
		}
		
		if (Keyboard.control.down() && Keyboard.restart.pressed()) {
			dragRoad = null;
			dragging = false;
			map.clear();
		}
		
		canDrag = true;
	}
	
	public void draw() {
		Draw.setColor(Color.BLACK);
		Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		Vector ms = Mouse.getVector();
		
		map.draw();
		
		Vector v = new Vector(10, 50);
		Draw.setColor(Color.DARK_GRAY);
		Draw.fillRect(v.minus(0, Lane.WIDTH * 2), new Vector(Lane.WIDTH * numLanes, Lane.WIDTH * 2));
		for (int i = 0; i < numLanes; i++) {
			Draw.setColor(Color.WHITE);
			
			if ((i > 0 && laneSetup[i - 1] != laneSetup[i]) || (i == 0 && !laneSetup[i])) {
				Draw.setColor(Color.YELLOW);
			}
			else if (i > 0)
				Draw.setStroke(Draw.STROKE_DASHED);
			
			Draw.drawLine(v, v.minus(0, Lane.WIDTH * 2));
			Draw.resetStroke();
			
			Draw.setColor(laneSetup[i] ? Color.yellow : Color.WHITE);
			v.add(Lane.WIDTH, 0);
			
			if (i == numLanes - 1)
				Draw.drawLine(v, v.minus(0, Lane.WIDTH * 2));
		}
	}
}
