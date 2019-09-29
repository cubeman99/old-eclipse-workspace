

public class BulletPistol extends Bullet {
	public BulletPistol(Unit owner, double x, double y, double direction, double speed, double damage) {
		super(owner, x, y, direction, speed, damage);
		
		// Pistol 10mm
		bulletLength 	= 32;
	}
}
