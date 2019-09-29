package game.nodes;

import java.awt.Color;
import game.Ring;
import common.GMath;
import common.Settings;

public class NodeEnhancerHealth extends Node{

	public NodeEnhancerHealth(Ring ring, double angle) {
		super(ring, angle, 10);
		this.health = Settings.NODE_HEALTH;
		
		this.ring.setEnhancer(this, Color.RED);
	}
	
}
