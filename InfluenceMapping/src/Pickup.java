


import java.awt.Graphics;
import java.awt.Image;


public class Pickup extends Entity {
	public double x;
	public double y;
	public double faceDir		= 0;
	public double radius		= 10;
	public double respawnTime	= 5; // seconds
	public boolean away			= false;
	public Timer respawnTimer	= new Timer();
	
	public Image image;
	public int type = 0;
	
	public static final int TYPE_HEALTH = 0;
	public static final int TYPE_AMMO   = 1;
	
	public Pickup(double x, double y) {
		this.x = x;
		this.y = y;
		faceDir = Game.random.nextDouble() * 360.0;
		respawn();
	}
	
	public void update() {
		faceDir = GMath.dirSimp(faceDir + 2);
		
		if (!away) {
			for (Unit u : Game.unitControl.units) {
				if (Collisions.circleCircle(getCircle(), u.getCircle())) {
					// Unit has picked this up!
					onPickedUp(u);
					away = true;
					respawnTimer.reset();
					respawnTimer.start();
				}
			}
		}
		
		if (away && respawnTimer.getSeconds() >= respawnTime) {
			respawn();
		}
	}
	
	public Vector getCenter() {
		return new Vector(x, y);
	}
	
	public Circle getCircle() {
		return new Circle(x, y, radius);
	}
	
	public void respawn() {
		away = false;
		type = Game.random.nextInt(2);
		respawnTimer.stop();
		respawnTimer.reset();
		if (type == TYPE_HEALTH)
			image = ImageData.pickupHealth;
		else if (type == TYPE_AMMO)
			image = ImageData.pickupAmmo;
	}
	
	public void onPickedUp(Unit u) {
		// Unit u picked up this pickup
		if (type == TYPE_HEALTH) {
			// Give Health
			u.giveHealth(50);
		}
		else if (type == TYPE_AMMO) {
			// Give Ammo
			u.weapon.giveMaxAmmo();
		}
	}
	
	public void draw(Graphics g) {
		if (!away)
			ImageDrawer.drawImage(g, image, (int)x,  (int)y, 9, 9, 1, faceDir, 1);
	}
}
