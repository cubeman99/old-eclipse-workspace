

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodeMap extends Entity {
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public ArrayList<ArrayList<Node>> nodeNeighbors = new ArrayList<ArrayList<Node>>();
	
	public ArrayList<Node> pathNodes = new ArrayList<Node>();
	public Node startNode;
	public Node endNode;
	
	public boolean nodeEditMode = false;
	public int debugDrawNodes = 0;
	
	
	public NodeMap() {
	}
	
	public void update() {
		// Debug Draw Nodes Option
		if (Game.keyPressed[KeyEvent.VK_N]) {
			debugDrawNodes += 1;
			if (debugDrawNodes > 2)
				debugDrawNodes = 0;
			nodeEditMode = (debugDrawNodes > 0);
		}
		
		
		if (nodeEditMode) {
			if (Game.mbLeftPressed) {
				//double ts = Game.world.tileSize;
				//double ts2 = Game.world.tileSize / 2.0d;
				//double nx = ((int) ((double) Game.mouseX / ts2) * ts2) + (int) ts2;
				//double ny = ((int) ((double) Game.mouseY / ts2) * ts2) + (int) ts2;
				//addNode(nx, ny);
				addNode(Game.mouseX, Game.mouseY);
			}
			if (Game.mbRightPressed) {
				double ts = Game.world.tileSize;
				double nx = (int) ((double) Game.mouseX / ts) * ts;
				double ny = (int) ((double) Game.mouseY / ts) * ts;
				for (Node n : nodeList) {
					if (n.x == nx && n.y == ny)
						nodeList.remove(n);
				}
			}
			if (Game.keyPressed[KeyEvent.VK_BACK_SPACE]) {
				nodeList.clear();
			}
			if (Game.keyPressed[KeyEvent.VK_ENTER]) {
				//reconstructNodeGrid();
				computeNodeNeighbors();
			}
		}
	}
	
	public void reconstructNodeGrid() {
		clearNodes();
		constructNodes();
		computeNodeNeighbors();
		pathNodes.clear();
		startNode = null;
		endNode   = null;
	}
	
	public void clearNodes() {
		nodeList.clear();
	}
	
	public void constructNodes() {
		World w = Game.world;
		for (int x = 0; x < w.width; x++) {
			for (int y = 0; y < w.height; y++) {
				if (w.tiles[x][y].isSolid) {
					boolean m1 = !adjTileSolid(w, x + 1, y);
					boolean m2 = !adjTileSolid(w, x + 1, y - 1);
					boolean m3 = !adjTileSolid(w, x, y - 1);
					boolean m4 = !adjTileSolid(w, x - 1, y - 1);
					boolean m5 = !adjTileSolid(w, x - 1, y);
					boolean m6 = !adjTileSolid(w, x - 1, y + 1);
					boolean m7 = !adjTileSolid(w, x, y + 1);
					boolean m8 = !adjTileSolid(w, x + 1, y + 1);
					double ts = w.tileSize;
					
					if (m1 && m2 && m3)
						addNode(ts * ((double) x + 1.5), ts * ((double) y - 0.5));
					if (m3 && m4 && m5)
						addNode(ts * ((double) x - 0.5), ts * ((double) y - 0.5));
					if (m5 && m6 && m7)
						addNode(ts * ((double) x - 0.5), ts * ((double) y + 1.5));
					if (m7 && m8 && m1)
						addNode(ts * ((double) x + 1.5), ts * ((double) y + 1.5));
				}
			}
		}
	}
	
	public void computeNodeNeighbors() {
		World w = Game.world;
		for (Node n1 : nodeList) {
			for (Node n2 : nodeList) {
				boolean collide = false;
				for (int x = 0; x < w.width; x++) {
					for (int y = 0; y < w.height; y++) {
						if (w.tiles[x][y].isSolid) {
							Line l = new Line(n1, n2);
							Rect r = w.getTileRect(x, y);
							if (Collisions.segmentRectangle(l, r)) {
								collide = true;
								break;
							}
						}
					}
					if (collide)
						break;
				}
				if (!collide)
					n1.neighbors.add(n2);
			}
		}
	}
	
	public Node getNearestNode(double x, double y) {
		
		Node nearest = null;
		double dist  = 0;
		for (Node n : nodeList) {
			double d = GMath.distance(x, y, n.x, n.y);
			if ((d < dist || nearest == null) && lineCollisionFree(new Line(x, y, n.x, n.y))) {
				nearest = n;
				dist	= d;
			}
		}
		return (nearest);
	}
	
	public boolean adjTileSolid(World w, int x, int y) {
		if (w.realTile(x, y)) {
			return w.tiles[x][y].isSolid;
		}
		return true;
	}
	
	public void addNode(double x, double y) {
		nodeList.add(new Node(x, y));
	}
	
	public boolean lineCollisionFree(Line l) {
		World w = Game.world;
		for (int x = 0; x < w.width; x++) {
			for (int y = 0; y < w.height; y++) {
				if (w.tiles[x][y].isSolid) {
					Rect r = w.getTileRect(x, y);
					if (Collisions.segmentRectangle(l, r)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public ArrayList<Node> findPath(Vector vecStart, Vector vecGoal) {
		ArrayList<Node> path = new ArrayList<Node>();
		if (!lineCollisionFree(new Line(vecStart, vecGoal))) {
			// Not a strait-forward path
			Node n1 = getNearestNode(vecStart.x, vecStart.y);
			Node n2 = getNearestNode(vecGoal.x, vecGoal.y);
			if (n1 == null || n2 == null)
				return null;
			path = aStar(n1, n2);
			if (path == null)
				return null;
		}
		
		path.add(0, new Node(vecStart.x, vecStart.y));
		path.add(new Node(vecGoal.x, vecGoal.y));
		path = simplifyPath(path);
		return path;
	}
	
	public ArrayList<Node> simplifyPath(ArrayList<Node> path) {
		if (path.size() < 3)
			return path;
		
		boolean done = false;
		
		while (!done) {
			done = true;
			for (int i = 0; i < path.size() - 2; i++) {
				if (lineCollisionFree(new Line(path.get(i), path.get(i + 2)))) {
					// Found a simplification!
					path.remove(i + 1);
					done = false;
					break;
				}
			}
			if (path.size() < 3)
				break;
		}
		
		return path;
	}
	
	public ArrayList<Node> aStar(Node nStart, Node nGoal) {
		if (nStart == nGoal || (nStart.x == nGoal.x && nStart.y == nGoal.y)) {
			ArrayList<Node> p = new ArrayList<Node>();
			p.add(nStart);
			return p;
		}
		ArrayList<Node> closedSet = new ArrayList<Node>();
		ArrayList<Node> openSet = new ArrayList<Node>();
		openSet.add(nStart);
		Map<Node, Node> cameFrom = new HashMap<Node, Node>();
		
		Map<Node, Double> gScore = new HashMap<Node, Double>();
		Map<Node, Double> hScore = new HashMap<Node, Double>();
		Map<Node, Double> fScore = new HashMap<Node, Double>();
		gScore.put(nStart, 0.0d);
		double hce = heuristicCostEstimate(nStart, nGoal);
		hScore.put(nStart, hce);
		fScore.put(nStart, hce);
		
		while (!openSet.isEmpty()) {
			Node nCurrent = getNodeLowestFScore(openSet, fScore);
			
			if (nCurrent.equals(nGoal)) {
				ArrayList<Node> p = reconstructPath(cameFrom, cameFrom.get(nGoal));
				p.add(nGoal);
				return p;
			}
			openSet.remove(nCurrent);
			closedSet.add(0, nCurrent);
			for (Node nNeighbor : nCurrent.neighbors) {
				if (closedSet.contains(nNeighbor))
					continue;
				double tentativeGScore = gScore.get(nCurrent) + nodeDistance(nCurrent, nNeighbor);
				boolean tentativeIsBetter = false;
				
				if (!openSet.contains(nNeighbor)) {
					openSet.add(0, nNeighbor);
					hScore.put(nNeighbor, heuristicCostEstimate(nNeighbor, nGoal));
					tentativeIsBetter = true;
				}
				else if (tentativeGScore < gScore.get(nNeighbor))
					tentativeIsBetter = true;
				
				if (tentativeIsBetter) {
					cameFrom.put(nNeighbor, nCurrent);
					gScore.put(nNeighbor, tentativeGScore);
					fScore.put(nNeighbor, gScore.get(nNeighbor) + hScore.get(nNeighbor));
				}
			}
		}
		// FAILURE
		return null;
	}

	public ArrayList<Node> reconstructPath(Map<Node, Node> cameFrom, Node nCurrent) {
		if (cameFrom.get(nCurrent) != null) {
			ArrayList<Node> p = reconstructPath(cameFrom, cameFrom.get(nCurrent));
			p.add(nCurrent);
			return p;
		}
		else {
			ArrayList<Node> p = new ArrayList<Node>();
			p.add(nCurrent);
			return p;
		}
	}
	
	public Node getNodeLowestFScore(ArrayList<Node> list, Map<Node, Double> fScore) {
		double fLowest = fScore.get(list.get(0));
		Node nLowest = list.get(0);
		for (Node n : list) {
			if (fScore.get(n) < fLowest) {
				fLowest = fScore.get(n);
				nLowest = n;
			}
		}
		return nLowest;
	}
	
	public double nodeDistance(Node a, Node b) {
		return GMath.distance(a.x, a.y, b.x, b.y);
	}
	
	public double heuristicCostEstimate(Node a, Node b) {
		return nodeDistance(a, b);
	}
	
	public static void drawPath(Graphics g, ArrayList<Node> path, Color c) {
		g.setColor(c);
		for (int i = 0; i < path.size(); i++) {
			Node n = path.get(i);
			n.draw(g, c, true);
			if (i < path.size() - 1) {
				Node n2 = path.get(i + 1);
				g.drawLine((int) n.x, (int) n.y, (int) n2.x, (int) n2.y);
			}
		}
	}
	
	public void draw(Graphics g) {

		// (DEBUG) Draw Nodes
		if (debugDrawNodes > 0) {
			for (Node n1 : nodeList) {
				n1.draw(g, Color.yellow);
				g.setColor(Color.yellow);
				if (debugDrawNodes > 1) {
					// Draw Node Connections
					for (Node n2 : n1.neighbors)
						g.drawLine((int) n1.x, (int) n1.y, (int) n2.x, (int) n2.y);
				}
			}
		}
		
		if (nodeList.size() > 0 && Game.keyDown[KeyEvent.VK_SHIFT]) {
			Node n1 = getNearestNode(Game.unitControl.player.x, Game.unitControl.player.y);
			Node n2 = getNearestNode(Game.mouseX, Game.mouseY);
			ArrayList<Node> path = findPath(new Vector(Game.unitControl.player.x, Game.unitControl.player.y), new Vector(Game.mouseX, Game.mouseY));
			if (n1 == n2 ||  (n1.x == n2.x && n1.y == n2.y))
				System.out.println("asdasdaSd");
			if (path != null && path.size() > 0)
				drawPath(g, path, Color.green);
		}
		
		drawPath(g, pathNodes, Color.red);
		if (startNode != null)
			startNode.draw(g, Color.green, true);
		if (endNode != null)
			endNode.draw(g, Color.red, true);
	}
	
}
