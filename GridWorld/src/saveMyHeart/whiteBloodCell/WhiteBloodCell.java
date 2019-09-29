package saveMyHeart.whiteBloodCell;

import java.awt.Color;
import java.util.ArrayList;
import saveMyHeart.heart.Heart;

import common.GMath;
import common.PathFinder;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Critter;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public class WhiteBloodCell extends Critter {
	private static final int HEART_RANGE = 2;
	private Heart heartLink;
	
	
	
	public WhiteBloodCell(Color c) {
		setColor(c);
		heartLink = null;
	}
	
	public WhiteBloodCell() {
		this(Color.WHITE);
	}
	
	/** Return whether the given range of the heart. **/
	private boolean withinHeartRange(Location loc) {
		if (heartLink == null)
			return false;
		Location heartLoc = heartLink.getLocation();
		return (Math.abs(heartLoc.getCol() - loc.getCol()) <= HEART_RANGE &&
				Math.abs(heartLoc.getRow() - loc.getRow()) <= HEART_RANGE);
	}
	
	private void findHeartLink() {
		Grid<Actor> g = getGrid();
		heartLink     = null;
		
		// Search for possible hearts to infect:
		for (int r = 0; r < g.getNumRows() && heartLink == null; r++) {
			for (int c = 0; c < g.getNumCols() && heartLink == null; c++) {
				Actor actor = g.get(new Location(r, c));
				if (actor instanceof Heart) {
					heartLink = (Heart) actor;
				}
			}
		}
	}
	
	@Override
	public void act() {
		if (heartLink == null)
			findHeartLink();
		
		if (heartLink != null) {
    		Grid<Actor> grid = getGrid();
    		ArrayList<Location> possibleMoves = grid.getEmptyAdjacentLocations(getLocation());
    		
    		if (possibleMoves.size() > 0) {
    			Location moveLoc = possibleMoves.get(GMath.random.nextInt(possibleMoves.size()));
    			
    			setDirection(GMath.getRoundedAngle(GMath.direction(getLocation(), moveLoc)));
    			moveTo(moveLoc);
    		}
		}
	}
}
