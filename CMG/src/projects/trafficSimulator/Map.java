package projects.trafficSimulator;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Mouse;
import cmg.math.geometry.Line;
import cmg.math.geometry.Vector;


public class Map {
	private ArrayList<Road> roads;
	private ArrayList<Intersection> intersections;
	
	
	public Map() {
		roads = new ArrayList<Road>();
		intersections = new ArrayList<Intersection>();
	}
	
	public ArrayList<LaneOLD> getLanes() {
		return null;
	}
	
	public ArrayList<Road> getRoads() {
		return roads;
	}
	
	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}
	
	public void clear() {
		roads.clear();
		intersections.clear();
	}
	
	public void addRoad(Road l) {
		roads.add(0, l);
	}
	
	public void addIntersection(Intersection i) {
		intersections.add(i);
	}
	
	public Intersection selectIntersection(Vector v) {
		for (int i = 0; i < intersections.size(); i++) {
			Intersection inter = intersections.get(i);
			if (inter.getPosition().distanceTo(v) < 20)
				return inter;
		}
		return null;
	}
	
	public void update() {
		// Update all roads.
		for (int i = 0; i < roads.size(); i++) {
			Road r = roads.get(i);
			r.update();
			if (r.getPath().numEdges() == 0) {
				roads.remove(i--);
				r.onDestroy();
			}
		}
		// Update all intersections.
		for (int i = 0; i < intersections.size(); i++)
			intersections.get(i).update();
	}
	
	public void draw() {
		// Draw all roads.
		for (int i = 0; i < roads.size(); i++)
			roads.get(i).draw();
		// Draw all intersections.
		for (int i = 0; i < intersections.size(); i++)
			intersections.get(i).draw();
	}
}
