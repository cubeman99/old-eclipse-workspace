package saveMyHeart.heart;

import java.awt.Color;

import info.gridworld.actor.Actor;

public class Heart extends Actor {
	private static final Color DEFAULT_COLOR = Color.RED;
	private boolean infected;
	
    
    public Heart(Color color) {
        setColor(color);
        infected = false;
    }
    
    public Heart() {
        this(DEFAULT_COLOR);
    }
    
    public void infect() {
    	infected = true;
    	setColor(Color.BLACK);
    }
    
    public boolean isInfected() {
    	return infected;
    }
    
	@Override
	public void act() {
		
	}
}
