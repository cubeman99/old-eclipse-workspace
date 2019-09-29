package game.nodes;

import common.GMath;
import common.Settings;
import common.Timer;
import game.Ring;


public class NodeDisconnecter extends Node {
	private Timer disconnectTimer;
	
	public NodeDisconnecter(Ring ring, double angle) {
		super(ring, angle, 13);
		this.health = Settings.NODE_HEALTH;
		this.disconnectTimer = new Timer(1000);
	}
	
	@Override
	public void update() {
		if (ring.isFrontRing() && ring.getRadius() < 300) {
			if (!disconnectTimer.isRunning()) {
				if (GMath.onChance(100))
					disconnectTimer.start();
			}
			else if (disconnectTimer.isExpired()) {
				// Disconnect the ring!
				ring.disconnect();
				disconnectTimer.stop();
			}
		}
	}
}
