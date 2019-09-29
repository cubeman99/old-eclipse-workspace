package game.nodes;

import main.ImageLoader;
import common.GMath;
import common.Settings;
import common.Timer;
import common.graphics.Draw;
import game.Ring;


public class NodeDeployer extends Node {
	private double deploySubImage;
	private boolean deployed;
	private Timer doorTimer;
	private boolean doorState;
	
	public NodeDeployer(Ring ring, double angle) {
		super(ring, angle, 8);

		this.doorTimer      = new Timer(3000);
		this.deployed       = false;
		this.deploySubImage = 0;
		this.doorState      = false;
	}
	
	/** Deploy the mini-ship. **/
	public void deploy() {
		if (deployed || (!ring.isFrontRing() && GMath.onChance(1)))
			return;
	
		control.addEnemy(0, new MiniShip(this));
		deploySubImage = 1;
		deployed = true;
		doorTimer.start();
	}
	
	@Override
	public void update() {
		if (!deployed && ring.getRadius() > Settings.RING_ATTACK_RADIUS && ring.isConnected() && GMath.onChance(720))
			deploy();
		
		if (doorTimer.isRunning()) {
			if (!doorState) {
				deploySubImage += 0.5;
				if (deploySubImage >= 9) {
					deploySubImage = 9;
					doorState = true;
				}
			}
			else if (doorTimer.isExpired()) {
				deploySubImage -= 0.5;
				if (deploySubImage <= 0) {
					deploySubImage = 0;
					doorState = false;
					doorTimer.stop();
				}
			}
		}
	}

	@Override
	public void draw() {
		if (!deployed) {
			Draw.draw(ImageLoader.getSprite("miniShip", 0), getRadius(), getAngle(), getAngle() + GMath.PI);
		}
		
		int sub = (int) deploySubImage;
		if (deploySubImage >= 10)
			sub = 9 - ((int) deploySubImage % 10);
		Draw.draw(ImageLoader.getSprite("nodeDeployer", sub), getRadius(), getAngle(), getAngle() - GMath.HALF_PI);
	}
}
