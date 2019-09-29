package game.nodes;

import java.awt.Color;
import game.Ring;
import common.GMath;
import common.Settings;

public class NodeEnhancerSpeed extends Node{

	public NodeEnhancerSpeed(Ring ring, double angle) {
		super(ring, angle, 11);
		this.health = Settings.NODE_HEALTH;
		
		this.ring.setEnhancer(this, Color.GREEN);
	}
	
}
