package saveMyHeart.bacterium;

import java.awt.Color;
import java.util.ArrayList;
import saveMyHeart.heart.Heart;

import common.GMath;
import common.PathFinder;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Flower;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public class Bacterium extends Actor {
	private Heart heartTarget;
	private ArrayList<Location> path;
	private int pathIndex;
	private ArrayList<Heart> unreachableHearts;
	
	public Bacterium(Color c) {
		heartTarget       = null;
		path              = new ArrayList<Location>();
		unreachableHearts = new ArrayList<Heart>();
		pathIndex         = 0;
	}
	
	public Bacterium() {
		this(Color.BLUE);
	}
	
	private void findHeartLocation() {
		Grid<Actor> g = getGrid();
		heartTarget   = null;
		path          = null;
		ArrayList<Heart> possibleTargets = new ArrayList<Heart>();
		
		// Search for possible hearts to infect:
		for (int r = 0; r < g.getNumRows(); r++) {
			for (int c = 0; c < g.getNumCols(); c++) {
				Actor actor = g.get(new Location(r, c));
				if (actor instanceof Heart) {
					if (!((Heart) actor).isInfected() && !unreachableHearts.contains(actor))
						possibleTargets.add((Heart) actor);
				}
			}
		}
		
		// Pick the closest heart:
		double minDist = 0;
		for (Heart h : possibleTargets) {
			double dist = GMath.distance(getLocation(), h.getLocation());
			if (dist < minDist || heartTarget == null) {
				heartTarget = h;
				minDist     = dist;
			}
		}
		
		// Find the shortest path to the heart:
		if (heartTarget != null) {
    		path      = PathFinder.aStar(this, heartTarget);
    		pathIndex = 0;
    		
    		if (path != null) {
    			if (path.size() > 0) {
    				if (path.get(0).equals(getLocation()))
    					path.remove(0);
    			}
    			if (path.size() > 0) {
    				if (path.get(path.size() - 1).equals(heartTarget.getLocation()))
    					path.remove(path.size() - 1);
    			}
    		}
    		else {
    			// This heart is unreachable!
    			unreachableHearts.add(heartTarget);
    			heartTarget = null;
    		}
		}
	}

	@Override
	public void act() {
		if (heartTarget == null || path == null)
			findHeartLocation();
		
		if (heartTarget != null) {
			if (heartTarget.isInfected())
				findHeartLocation();
			
			if (path != null) {
				if (pathIndex >= path.size()) {
					heartTarget.infect();
					findHeartLocation();
				}
				else {
					int dir = (int) ((GMath.direction(getLocation(), path.get(pathIndex)) / 45.0) + 0.5) * 45;
    				setDirection(dir);
    				
    				if (canMove()) {
    					pathIndex++;
    					move();
    				}
				}
			}
		}
	}

	/** Move one space in **/
	public void move() {
        Grid<Actor> gr = getGrid();
        if (gr == null)
            return;
        Location loc  = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        if (gr.isValid(next))
            moveTo(next);
        else
            removeSelfFromGrid();
    }
	
	/** Return whether this bacterium can move forward in its current direction. **/
    public boolean canMove() {
        Grid<Actor> gr = getGrid();
        if (gr == null)
            return false;
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        if (!gr.isValid(next))
            return false;
        Actor neighbor = gr.get(next);
        return ((neighbor == null) || (neighbor instanceof Flower));
    }

}
