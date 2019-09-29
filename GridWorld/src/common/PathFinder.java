package common;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Flower;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PathFinder {
	
	/**
	 * The A* (A-Star) Path finding Method:
	 * Finds the shortest path between two Locations
	 * 
	 * @param actor - The Actor performing the path.
	 * @param target - The target Actor to be path finding to.
	 * 
	 * @return The shortest path between two actors.
	 * returns null if no such path exists.
	 */
	public static ArrayList<Location> aStar(Actor actor, Actor target) {
		Grid<Actor> grid = actor.getGrid();
		Location start   = actor.getLocation();
		Location goal    = target.getLocation();
		
		ArrayList<Location> closedSet = new ArrayList<Location>(); // The set of locations already evaluated.
		ArrayList<Location> openSet   = new ArrayList<Location>(); // The set of tentative locations to be evaluated, initially containing the start location
		openSet.add(start);
		Map<Location, Location> cameFrom  = new HashMap<Location, Location>(); // The map of navigated locations.
		
		Map<Location, Double> gScore  = new HashMap<Location, Double>(); // Cost from start along best known path.
		Map<Location, Double> hScore  = new HashMap<Location, Double>();
		Map<Location, Double> fScore  = new HashMap<Location, Double>();
		
		// Estimated total cost from start to goal:
		gScore.put(start, 0.0d);
		double hce = heuristicCostEstimate(start, goal);
		hScore.put(start, hce);
		fScore.put(start, hce);
		
		
		while (!openSet.isEmpty()) {
			Location nCurrent = getLocationLowestFScore(openSet, fScore);
			
			if (nCurrent.equals(goal)) {
				ArrayList<Location> p = reconstructPath(cameFrom, cameFrom.get(goal));
				p.add(goal);
				return p;
			}
			openSet.remove(nCurrent);
			closedSet.add(0, nCurrent);
			
			
			ArrayList<Location> neighbors = grid.getEmptyAdjacentLocations(nCurrent);
			neighbors.addAll(grid.getOccupiedAdjacentLocations(nCurrent));
			
			for (Location nNeighbor : neighbors) {
				Actor a = grid.get(nNeighbor);
				
				if (a != null) {
					if (!(a instanceof Flower) && a != target && a != actor)
						continue;
				}
				
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
		
		return null; // Failure, no possible path found!
	}
	
	/** Reconstruct the current path. **/
	private static ArrayList<Location> reconstructPath(Map<Location, Location> cameFrom, Location nCurrent) {
		if (cameFrom.get(nCurrent) != null) {
			ArrayList<Location> p = reconstructPath(cameFrom, cameFrom.get(nCurrent));
			p.add(nCurrent);
			return p;
		}
		else {
			ArrayList<Location> p = new ArrayList<Location>();
			p.add(nCurrent);
			return p;
		}
	}
	
	/** Get the location with the lowest f score out of a list. **/
	private static Location getLocationLowestFScore(ArrayList<Location> openSet, Map<Location, Double> fScore) {
		double fLowest = fScore.get(openSet.get(0));
		Location nLowest = openSet.get(0);
		for (Location n : openSet) {
			if (fScore.get(n) < fLowest) {
				fLowest = fScore.get(n);
				nLowest = n;
			}
		}
		return nLowest;
	}
	/** Returns the estimated cost for a path between two locations. **/
	private static double heuristicCostEstimate(Location start, Location goal) {
		return GMath.distance(start, goal);
	}
}
