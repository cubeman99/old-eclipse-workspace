package zelda.common.util;

import java.util.ArrayList;
import java.util.Scanner;
import javax.naming.LimitExceededException;
import zelda.common.geometry.Vector;
import zelda.game.control.script.Constant;
import zelda.game.control.script.Script;

public class Path {
	private static final int TYPE_DIRECTION = 0;
	private static final int TYPE_LIMIT     = 1;
	private static final int TYPE_PROPERTY  = 2;
	
	private ArrayList<PathElement> elements;
	private int elementIndex;
	private boolean repeats;
	private boolean paused;
	private int pauseTimer;
	private double speed;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Path() {
		elements     = new ArrayList<PathElement>();
		elementIndex = 0;
		repeats      = false;
		paused       = false;
		pauseTimer   = 0;
		speed        = 0.5;
	}
	
	public Path(String str) {
		this();
		parse(str);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Vector nextMotion() {
		if (paused) {
			if (pauseTimer++ > 6)
				paused = false;
			return null;
		}
		
		Vector motion = getElement().nextMotion(speed);
		if (getElement().isDone()) {
			if (!paused && getNextElement().getDir() != getElement().getDir()) {
				paused     = true;
				pauseTimer = 0;
			}
			nextElement();
			
		}
		
		return motion;
	}
	
	public PathElement getElement() {
		return elements.get(elementIndex);
	}
	
	public PathElement getNextElement() {
		return elements.get((elementIndex + 1) % elements.size());
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void start() {
		elements.get(0).begin();
	}
	
	public void onHit() {
		int type = getElement().getType();
		if (type == PathElement.TYPE_HIT) {
			// Pause
			if (!paused) {
				paused     = true;
				pauseTimer = 0;
			}
			nextElement();
		}
		else if (type == PathElement.TYPE_BOUNCE) {
			nextElement();
		}
	}
	
	public void nextElement() {
		elementIndex++;
		
		if (elementIndex >= elements.size()) {
			if (repeats) {
				elementIndex -= elements.size();
			}
			else {
				// TODO: Done
			}
		}
		
		getElement().begin();
	}
	
	public void parse(String str) {
		Scanner scanner = new Scanner(str);
		String currentWord = "";
		
		try {
    		int type = TYPE_DIRECTION;
    		int dir  = 0;
    		
    		while (scanner.hasNext()) {
    			currentWord = scanner.next();
    			
    			if (type == TYPE_DIRECTION && currentWord.equalsIgnoreCase("repeat"))
    				type = TYPE_PROPERTY;
    			
    			
    			if (type == TYPE_DIRECTION) {
        			Constant c  = Script.constants.getConstantIgnoreCase(currentWord);
        			if (c != null)
        				dir = Integer.parseInt(c.getValue());
        			else
        				dir = Integer.parseInt(currentWord);
        			type = TYPE_LIMIT;
    			}
    			
    			else if (type == TYPE_LIMIT) {
	    			int elementType = PathElement.TYPE_LENGTH;
	    			int length = 0;
	    			
	    			if (currentWord.equals("hit"))
	    				elementType = PathElement.TYPE_HIT;
	    			else if (currentWord.equals("bounce"))
	    				elementType = PathElement.TYPE_BOUNCE;
	    			else
	    				length = Integer.parseInt(currentWord);
    				
        			elements.add(new PathElement(dir, length, elementType));
        			type = TYPE_DIRECTION;
    			}
    			else {
    				if (currentWord.equalsIgnoreCase("repeat"))
    					repeats = true;
    			}
    		}
		}
		catch (NumberFormatException e) {
			System.err.println("Error parsing path: Cannot read the integer \"" + currentWord + "\"");
		}
		
		scanner.close();
	}
}
