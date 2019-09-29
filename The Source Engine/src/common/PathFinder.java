package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import map.Node;
import map.NodeMap;
import map.Path;


public class PathFinder {
	private static NodeMap nodeMap;
	
	
	
	/** Get the shortest path between two points through the node map. Returns null if no such path. **/
	public static Path getShortestPath(Vector start, Vector goal, double radius) {
		Node nStart = new Node(start);
		Node nGoal  = new Node(goal);
		
		nodeMap.nodes.add(nStart);
		nodeMap.nodes.add(nGoal);
		nodeMap.setupNodeLinks(nStart, radius);
		nodeMap.setupNodeLinks(nGoal, radius);
		
		ArrayList<Node> nodePath = aStar(nStart, nGoal);
		
		nodeMap.nodes.remove(nStart);
		for (Node n : nStart.nodeLinks)
			n.nodeLinks.remove(nStart);
		
		nodeMap.nodes.remove(nGoal);
		for (Node n : nGoal.nodeLinks)
			n.nodeLinks.remove(nGoal);
		
		return ((nodePath == null) ? null : new Path(nodePath));
	}
	
	/** Set the node map for this path finder to work in. **/
	public static void setNodeMap(NodeMap nodeMap) {
		PathFinder.nodeMap = nodeMap;
	}
	
	/**
	 * A* Path finding Method:
	 * Finds the shortest path between
	 * two nodes.
	 * 
	 * @param start - The starting node.
	 * @param goal - The goal node.
	 * @return The shortest path between two nodes.
	 * returns null if there is no such path to be found.
	 */
	public static ArrayList<Node> aStar(Node start, Node goal) {
		ArrayList<Node> closedSet = new ArrayList<Node>(); // The set of nodes already evaluated.
		ArrayList<Node> openSet   = new ArrayList<Node>(); // The set of tentative nodes to be evaluated, initially containing the start node
		openSet.add(start);
		Map<Node, Node> cameFrom  = new HashMap<Node, Node>(); // The map of navigated nodes.
		
		Map<Node, Double> gScore  = new HashMap<Node, Double>(); // Cost from start along best known path.	
		Map<Node, Double> hScore  = new HashMap<Node, Double>();
		Map<Node, Double> fScore  = new HashMap<Node, Double>();
		
		// Estimated total cost from start to goal:
		gScore.put(start, 0.0d);
		double hce = heuristicCostEstimate(start, goal);
		hScore.put(start, hce);
		fScore.put(start, hce);
		
		while (!openSet.isEmpty()) {
			Node nCurrent = getNodeLowestFScore(openSet, fScore);
			
			if (nCurrent.equals(goal)) {
				ArrayList<Node> p = reconstructPath(cameFrom, cameFrom.get(goal));
				p.add(goal);
				return p;
			}
			openSet.remove(nCurrent);
			closedSet.add(0, nCurrent);
			for (Node nNeighbor : nCurrent.nodeLinks) {
				if (closedSet.contains(nNeighbor))
					continue;
				
				double tentativeGScore    = gScore.get(nCurrent) + GMath.distance(nCurrent, nNeighbor);
				boolean tentativeIsBetter = false;
				
				if (!openSet.contains(nNeighbor)) {
					openSet.add(0, nNeighbor);
					hScore.put(nNeighbor, heuristicCostEstimate(nNeighbor, goal));
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
		
		return null; // Failure!
	}
	
	/** Reconstruct the current path. **/
	private static ArrayList<Node> reconstructPath(Map<Node, Node> cameFrom, Node nCurrent) {
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
	
	/** Get the node with the lowest f score out of a list. **/
	private static Node getNodeLowestFScore(ArrayList<Node> openSet, Map<Node, Double> fScore) {
		double fLowest = fScore.get(openSet.get(0));
		Node nLowest = openSet.get(0);
		for (Node n : openSet) {
			if (fScore.get(n) < fLowest) {
				fLowest = fScore.get(n);
				nLowest = n;
			}
		}
		return nLowest;
	}
	/** Returns the estimated cost for a path between two nodes. **/
	private static double heuristicCostEstimate(Node start, Node goal) {
		return start.distanceTo(goal);
	}
}
