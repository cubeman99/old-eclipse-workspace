


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class AIPathFinder extends Entity {
	
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public ArrayList<ArrayList<Node>> nodeNeighbors = new ArrayList<ArrayList<Node>>();
	public ArrayList<Node> pathNodes = new ArrayList<Node>();
	public Node startNode;
	public Node endNode;
	public boolean drawNodeConnections = false;
	
	public AIPathFinder() {
		
	}
	
	public void clearNodes() {
		nodeList.clear();
	}
	
	public void constructNodes(World w) {
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
	
	public void computeNodeNeighbors(int tileSize) {
		for (Node n1 : nodeList) {
			for (Node n2 : nodeList) {
				boolean collide;
				collide = false;
				for (int x = 0; x < Game.world.width; x++) {
					for (int y = 0; y < Game.world.height; y++) {
						if (Game.world.tiles[x][y].isSolid) {
							int ts = Game.world.tileSize;
							if (lineIntersectsTile(n1.x, n1.y, n2.x, n2.y, x * ts, y * ts, (x + 1) * ts, (y + 1) * ts)) {
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
		/*
		for (Node n1 : nodeList) {
			for (Node n2 : nodeList) {
				if (!n1.equals(n2) && nodeDistance(n1, n2) < tileSize * 1.8 ) {
					n1.neighbors.add(n2);
				}
			}
		}*/
	}
	
	public Node getNearestNode(double x, double y) {
		
		Node nearest = nodeList.get(0);
		double dist  = GMath.distance(x, y, nearest.x, nearest.y);
		for (Node n : nodeList) {
			double d = GMath.distance(x, y, n.x, n.y);
			if (d < dist) {
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
		Node n = new Node(x, y);
		nodeList.add(n);
	}

	public boolean lineIntersectsTile(double lx1, double ly1, double lx2, double ly2, double rx1, double ry1, double rx2, double ry2) {
		if (Math.max(lx1, lx2) < rx1 || Math.min(lx1, lx2) > rx2)
			return false;
		if (Math.max(ly1, ly2) < ry1 || Math.min(ly1, ly2) > ry2)
			return false;
		if (lx2 - lx1 == 0) {
			return (lx1 >= rx1 && lx1 < rx2);
		}
		else {
			double slope = (ly2 - ly1) / (lx2 - lx1);
			double b = ly1 - (slope * lx1);
			if (ry2 < Math.min((slope * rx2) + b, (slope * rx1) + b))
				return false;
			if (ry1 > Math.max((slope * rx2) + b, (slope * rx1) + b))
				return false;
		}
		return true;
	}
	
	public void update() {
		if (Game.keyPressed[KeyEvent.VK_F])
			drawNodeConnections = !drawNodeConnections;
		if (Game.keyPressed[KeyEvent.VK_ENTER]) {
			clearNodes();
			constructNodes(Game.world);
			computeNodeNeighbors(Game.world.tileSize);
			pathNodes.clear();
			startNode = null;
			endNode   = null;
		}
		if (Game.mbMiddlePressed && nodeList.size() > 0){
			if (pathNodes.size() > 0) {
				pathNodes.clear();
				startNode = getNearestNode(Game.mouseX, Game.mouseY);
				endNode   = null;
			}
			else if (startNode == null)
				startNode = getNearestNode(Game.mouseX, Game.mouseY);
			else if (startNode != null) {
				Node n = getNearestNode(Game.mouseX, Game.mouseY);
				if (n != startNode) {
					endNode = n;
					pathNodes = aStar(startNode, endNode);
				}
			}
		}
		if (Game.keyPressed[KeyEvent.VK_SPACE] && nodeList.size() > 0 && startNode != null && endNode != null) {
			pathNodes = aStar(startNode, endNode);
		}
	}
	
	public ArrayList<Node> aStar(Node nStart, Node nGoal) {
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
		System.out.println("Failure to calculate Path");
		return new ArrayList<Node>();
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
	
	public void drawPath(Graphics g, ArrayList<Node> path, Color c) {
		g.setColor(c);
		if (path.size() > 0) {
			for (Node n : path) {
				n.draw(g, c, true);
				int i = path.indexOf(n);
				if (i < path.size() - 1) {
					Node n2 = path.get(i + 1);
					g.drawLine((int) n.x, (int) n.y, (int) n2.x, (int) n2.y);
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		// Draw Nodes
		for (Node n1 : nodeList) {
			n1.draw(g, Color.yellow);
			g.setColor(Color.yellow);
			if (drawNodeConnections) {
				for (Node n2 : n1.neighbors)
					g.drawLine((int) n1.x, (int) n1.y, (int) n2.x, (int) n2.y);
			}
		}
		drawPath(g, pathNodes, Color.red);
		if (startNode != null)
			startNode.draw(g, Color.green, true);
		if (endNode != null)
			endNode.draw(g, Color.red, true);
	}
}
