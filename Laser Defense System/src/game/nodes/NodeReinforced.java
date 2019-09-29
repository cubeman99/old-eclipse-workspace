package game.nodes;

import common.GMath;
import common.Settings;
import game.Ring;

public class NodeReinforced extends Node {
	
	public NodeReinforced(Ring ring, double angle) {
		super(ring, angle, 4 + GMath.random.nextInt(4));
		
		this.health = Settings.NODE_REINFORCED_HEALTH;
		initialize();
	}
	
}
