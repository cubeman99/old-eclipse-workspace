package map;

import java.awt.Color;
import java.util.ArrayList;
import common.Draw;
import common.GMath;
import common.PathFinder;
import common.Vector;
import common.shape.Line;
import common.shape.Polygon;
import entity.unit.Unit;


/**
 * Node Map.
 * 
 * @author David Jordan
 */
public class NodeMap {
	public Map map;
	public ArrayList<Node> nodes;
	public ArrayList<Vector> nodesTest;
	
	public ArrayList<Polygon> testPolys;
	
	public NodeMap(Map map) {
		this.map   = map;
		this.nodes = new ArrayList<Node>();
		this.nodesTest = new ArrayList<Vector>();
		
		this.testPolys = new ArrayList<Polygon>();
		
		PathFinder.setNodeMap(this);
	}
	
	
	public void constructNodeMap() {
		nodes.clear();
		nodesTest.clear();
		
		double radius = Unit.UNIT_RADIUS;
		
		for (Polygon p1 : map.walls) {
			for (int i = 0; i < p1.edgeCount(); i++) {
				Line l1 = p1.getEdge(i);
				
				{
					double xx = 2;
					Vector lv1 = l1.getVector().getPerpendicular().setLength(radius + (GMath.EPSILON * xx));
					Vector lv2 = p1.getEdge(i + 1).getVector().getPerpendicular().setLength(radius + (GMath.EPSILON * xx));
					
					Vector corner = p1.getVertex((i + 1) % p1.vertexCount());
					nodes.add(new Node(corner.plus(lv1)));
					nodes.add(new Node(corner.minus(lv1)));
					nodes.add(new Node(corner.plus(lv2)));
					nodes.add(new Node(corner.minus(lv2)));
				}
				
				
				{
					Vector spacerTranslation = l1.getVector().getPerpendicular().setLength(radius);
					Line spacer = (Line) l1.getTranslated(spacerTranslation);
					
					Line l2 = p1.getEdge(i - 1);
					Vector spacerTranslation2 = l2.getVector().getPerpendicular().setLength(radius);
					Line spacer2 = (Line) l2.getTranslated(spacerTranslation2);
					
					Vector intersection = Line.intersectionEndless(spacer, spacer2);
					if (intersection != null) {
						nodes.add(new Node(intersection));
					}
				}
				{
					Vector spacerTranslation = l1.getVector().getPerpendicular().setLength(radius);
					Line spacer = (Line) l1.getTranslated(spacerTranslation.inverse());
					
					Line l2 = p1.getEdge(i - 1);
					Vector spacerTranslation2 = l2.getVector().getPerpendicular().setLength(radius);
					Line spacer2 = (Line) l2.getTranslated(spacerTranslation2.inverse());
					
					Vector intersection = Line.intersectionEndless(spacer, spacer2);
					if (intersection != null) {
						nodes.add(new Node(intersection));
					}
				}
			}
		}
		
		// Delete any nodes that are too close to the walls:
		for (int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {
			Vector n = nodes.get(nodeIndex);
			boolean tooClose = false;
			for (int i = 0; i < map.walls.size() && !tooClose; i++) {
				Polygon p = map.walls.get(i);
				for (int j = 0; j < p.edgeCount() && !tooClose; j++) {
					Line l = p.getEdge(j);
					
					if (n.distanceToSegment(l) < radius + GMath.EPSILON) {
						tooClose = true;
					}
				}
			}
			if (tooClose) {
				nodes.remove(nodeIndex);
				nodeIndex -= 1;
			}
		}
		
		// Link nodes together:
		for (Node n1 : nodes) {
			for (Node n2 : nodes) {
				if (n1 != n2) {
					Line l = new Line(n1, n2);
					if (!pathTouchesWalls(l, radius)) {
						n1.nodeLinks.add(n2);
					}
				}
			}
		}
	}
	
	
	
	public void setupNodeLinks(Node n, double radius) {
		for (Node n2 : nodes) {
			if (n != n2) {
				Line l = new Line(n, n2);
				if (!pathTouchesWalls(l, radius)) {
					n.nodeLinks.add(n2);
					n2.nodeLinks.add(n);
				}
			}
		}
	}


	public boolean pathTouchesWalls(Line l, double radius) {
		for (Polygon p : map.walls) {
			for (int i = 0; i < p.edgeCount(); i++) {
				Line edge = p.getEdge(i);
				if (Line.shortestDistance(l, edge) < radius - (GMath.EPSILON * 4))
					return true;
			}
		}
		return false;
	}
	
	public Node getNearestNode(Vector point, double radius) {
		Node nearest    = null;
		double distance = 0;
		
		for (Node n : nodes) {
			Line l = new Line(point, n);
			
			if (pathTouchesWalls(l, radius))
				continue;
			
			double dist = l.length();
			if (nearest == null || dist < distance) {
				distance = dist;
				nearest = n;
			}
		}
		
		return nearest;
	}
	
	public Node getNearestNode(Vector point) {
		Node nearest    = null;
		double distance = 0;
		
		for (Node n : nodes) {
			double dist = n.distanceTo(point);
			if (nearest == null || dist < distance) {
				distance = dist;
				nearest = n;
			}
		}
		
		return nearest;
	}
	
	public void drawNodes(Color color) {
		// Draw nodes:
		Draw.setColor(color);
		for (int i = 0; i < nodes.size(); i++) {
			Draw.drawCircle(nodes.get(i), 0.1);
		}
	}
	
	public void drawNodeLinks(Color color) {
		// Draw node links:
		Draw.setColor(color);
		for (Node n : nodes) {
			for (Node link : n.nodeLinks)
				Draw.drawLine(n, link);
		}
	}
	
	public void draw() {
//		Vector ms = map.control.viewControl.getGamePoint(Mouse.getVector());
		
		drawNodes(Color.YELLOW);
//		drawNodeLinks(Color.YELLOW);

		Draw.setColor(Color.GREEN);

		/*
		Vector start = map.control.player.getPosition();
		Vector goal  = ms;
		
		Node nStart = getNearestNode(start);
		Node nGoal  = getNearestNode(goal);
		Path path = PathFinder.getShortestPath(start, goal, Unit.UNIT_RADIUS);
		
		if (path != null) {
			if (path.getLength() > 0) {
//				Draw.drawPath(path);
				
				if (Keyboard.space.pressed())
					map.control.player.followPath = path;
			}
		}
		*/
		
//		Draw.setColor(Color.DARK_GRAY);
//		Draw.drawCircle(goal, Unit.UNIT_RADIUS);
	}
}
