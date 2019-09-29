package game.nodes;

import java.awt.Color;
import game.Ring;
import game.entity.lasers.Laser;
import common.GMath;
import common.Settings;

public class NodeEnhancerShield extends Node{

	public NodeEnhancerShield(Ring ring, double angle) {
		super(ring, angle, 12);
		this.health = Settings.NODE_HEALTH;
		
		this.ring.setEnhancer(this, Color.BLUE);
	}
	
	@Override
	public void update() {
		for (int i = 0; i < control.entities.size(); i++) {
			if (control.entities.get(i) instanceof Laser) {
				Laser laser = (Laser) control.entities.get(i);
				
				if (laser.getRadius() > ring.getRadius()) {
					laser.destroy();
				}
			}
		}
	}
}
