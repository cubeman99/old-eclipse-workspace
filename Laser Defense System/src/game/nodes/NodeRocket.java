package game.nodes;

import main.ImageLoader;
import common.GMath;
import common.Settings;
import common.Timer;
import common.graphics.SpriteSheet;
import game.Ring;

public class NodeRocket extends Node {
	private boolean moving;
	private Timer moveTimer;
	private int moveDirection;
	private SpriteSheet sheet;
	private double subImage;
	
	
	public NodeRocket(Ring ring, double angle) {
		super(ring, angle, GMath.random.nextInt(4));
		this.health = Settings.NODE_HEALTH;
		
		this.moving        = GMath.random.nextBoolean();
		this.moveTimer     = new Timer();
		this.moveDirection = (GMath.random.nextBoolean() ? 1 : -1);
		this.sheet         = ImageLoader.getSpriteSheet("nodeMover");
		this.subImage      = 0;
	}
	
	private void stopMoving() {
		moving   = false;
		subImage = 0;
		sheet = ImageLoader.getSpriteSheet("nodeMover");
	}
	
	private void startMoving() {
		moving = true;
		moveDirection *= -1;
	}
	
	@Override
	public void update() {
		if (!ring.isConnected() && moving) {
			stopMoving();
		}
		
		if (ring.isConnected() && moveTimer.isExpired()) {
			moving = !moving;
			
			if (moving) {
				moveDirection *= -1;
				
				if (moveDirection < 0)
					sheet = ImageLoader.getSpriteSheet("nodeMoverRight");
				else
					sheet = ImageLoader.getSpriteSheet("nodeMoverLeft");
			}
			else {
				subImage = 0;
				sheet = ImageLoader.getSpriteSheet("nodeMover");
			}
			
			moveTimer.setExpireTime(1000 + GMath.random.nextInt(1300));
			moveTimer.start();
		}
		
		if (moving) {
			subImage += 0.5;
			if (GMath.abs(ring.angleSpeed) < 10) {
				ring.angleSpeed += 0.01 * moveDirection;
			}
		}
		
		sprite = sheet.getSprite((int) subImage % 2);
	}
	
	
}
