package projects.trafficSimulator;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Vector;


public class LaneOLD {
	private static final double CONNECT_DISTANCE = 4;
	public static final double MAX_ANGLE = 0.8;
	private Map map;
	private Path path;
	private Intersection in;
	private Intersection out;
	private double width;
	private ArrayList<Section> sections;
	private Path[] snapPaths;
	
	
	public LaneOLD(Map map, Intersection in, Intersection out) {
		this.map   = map;
		this.path  = new Path();
		this.in    = in;
		this.out   = out;
		this.width = 40;
		this.snapPaths = new Path[2];
		
		for (int i = 0; i < snapPaths.length; i++)
			snapPaths[i] = new Path();
		
		sections   = new ArrayList<Section>();
		if (in != null)
			path.addVertex(in.getPosition());
		if (out != null)
			path.addVertex(out.getPosition());
	}
	
	public double getWidth() {
		return width;
	}
	
	public Path getPath() {
		return path;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Intersection getIn() {
		return in;
	}
	
	public Intersection getOut() {
		return out;
	}
	
	public double length() {
		return in.getPosition().distanceTo(out.getPosition());
	}
	
	public Path getSnapPath(int side) {
		return snapPaths[side];
	}
	
	public ArrayList<Section> getSections() {
		return sections;
	}
	
	public Line getRawEdgeLine(int index, double radius, int type) {
		if (index >= path.numEdges())
			return null;
		Line l   = path.getEdge(index);
		Vector v = l.getVector().getPerpendicular().setLength(radius);
		if (type == 1)
			v.negate();
		return l.plus(v);
	}
	
	public Line getEdgeLine(int index, double radius, int type) {
		if (index >= path.numEdges())
			return null;
		Line l = getRawEdgeLine(index, radius, type);
		
		if (index > 0) {
			Line lprev = getRawEdgeLine(index - 1, radius, type);
			Vector intersect = Line.intersectionEndless(l, lprev);
			if (intersect != null && Math.abs(GMath.angleBetween(l.direction(), lprev.direction())) > 0.01) {
				l.end1.set(intersect);
			}
		}
		if (index < path.numEdges() - 1) {
			Line lnext = getRawEdgeLine(index + 1, radius, type);
			Vector intersect = Line.intersectionEndless(l, lnext);
			if (intersect != null && Math.abs(GMath.angleBetween(l.direction(), lnext.direction())) > 0.01) {
				l.end2.set(intersect);
			}
		}
		
		return l;
	}
	
	public Vector getClosestSnapPoint(Vector point) {
		Vector closestPoint = null;
		double closestDist  = 0;

		for (int i = 0; i < sections.size(); i++) {
			Vector v = sections.get(i).getClosestSnapPoint(point);
			if (v != null) {
				double dist = point.distanceTo(v);
				if (dist < closestDist || closestPoint == null) {
					closestPoint = v;
					closestDist = dist;
				}
			}
		}
		return closestPoint;
	}
	
	public Vector getSnapPoint(Vector point) {
		Vector closestPoint = null;
		double closestDist  = 0;

		for (int i = 0; i < map.getLanes().size(); i++) {
			LaneOLD l = map.getLanes().get(i);
			if (l != this) {
				Vector v = l.getPath().getClosestPoint(point);
				if (v != null) {
    				double dist = point.distanceTo(v);
    				if (dist < closestDist || closestPoint == null) {
    					closestPoint = v;
    					closestDist = dist;
    				}
				}
			}
		}
		return closestPoint;
	}
	
	public void removeVertex(int index) {
		path.removeVertex(index);
		if (index - 1 < sections.size()) {
			Section sect = sections.get(index - 1);
			sections.remove(index - 1);
			sect.onDestroy();
		}
		if (index - 2 >= 0 && index - 2 < sections.size()) {
			sections.get(index - 2).refresh();
			sections.get(index - 2).onDestroy();
		}
	}
	
	public void addVertex(Vector v) {
		path.addVertex(v);
		if (path.numVertices() > 1) {
    		sections.add(new Section(this, sections.size()));
    		if (sections.size() > 1)
    			sections.get(sections.size() - 2).refresh();
		}
	}
	
	public void update() {
		if (in != null)
			path.getStart().set(in.getPosition());
		if (out != null)
			path.getEnd().set(out.getPosition());
		
		/*
		for (int i = 0; i < path.numEdges() - 1; i++) {
			Line edge1   = path.getEdge(i);
			Line edge2   = path.getEdge(i + 1);
			double dir1  = edge1.direction();
			double dir2  = edge2.direction();

			double angle = dir2 - dir1;
			if (angle > GMath.PI)
				angle = -GMath.TWO_PI + angle;
			if (angle < -GMath.PI)
				angle = GMath.TWO_PI + angle;

			double avg = 1;//(edge1.length() + edge2.length()) * 0.5;
			if ((angle / avg) < -MAX_ANGLE || (angle / avg) > MAX_ANGLE) {
				path.removeVertex(i + 1);
				i--;
			}
		}
		*/
		/*
		sections.clear();
		for (int i = 0; i < path.numEdges(); i++) {
			sections.add(new Section(this, i));
		}
		*/
		
		for (int side = 0; side < snapPaths.length; side++) {
			snapPaths[side].clear();
			for (int i = 0; i < path.numEdges(); i++) {
				Line l = getEdgeLine(i, width, side);
				if (i == 0)
					snapPaths[side].addVertex(l.end1);
				snapPaths[side].addVertex(l.end2);
			}
		}
	}
	
	public void draw() {
		Draw.setColor(Color.GRAY);
		//Draw.draw(path);

		for (int i = 0; i < sections.size(); i++) {
			Section sect = sections.get(i);
			sect.draw();
			/*
			for (int side = 0; side < snapPaths.length; side++) {
				boolean connect = false;
				
				if (i >= snapPaths[side].numEdges()) {
					System.out.println("BAD");
					break;
				}
				
				Line snapEdge = snapPaths[side].getEdge(i);
				Vector v = getSnapPoint(snapEdge.end1);
				if (v != null && v.distanceTo(snapEdge.end1) < CONNECT_DISTANCE)
					connect = true;
				
				if (!connect) {
					Draw.setColor(Color.WHITE);
					Draw.draw(snapEdge);
					
					Draw.setColor(Color.BLACK);
					Draw.drawLine(p.getVertex(2 * side), p.getVertex((2 * side) + 1));
				}
				else if (side == 1) {
					Draw.setColor(Color.BLACK);
					Draw.setStroke(Draw.STROKE_DASHED);
					Draw.drawLine(p.getVertex(2 * side), p.getVertex((2 * side) + 1));
					Draw.resetStroke();
				}
			}
			Draw.setColor(Color.BLACK);
			if (i == 0)
				Draw.drawLine(p.getVertex(3), p.getVertex(0));
			if (i == sections.size() - 1)
				Draw.drawLine(p.getVertex(1), p.getVertex(2));
			*/
		}
	}
}
