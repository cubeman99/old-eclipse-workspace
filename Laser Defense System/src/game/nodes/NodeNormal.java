package game.nodes;

import common.GMath;
import common.Settings;
import game.Ring;

public class NodeNormal extends Node {
	
	public NodeNormal(Ring ring, double angle) {
		super(ring, angle, GMath.random.nextInt(4));
		this.health = Settings.NODE_HEALTH;
	}
	
}
