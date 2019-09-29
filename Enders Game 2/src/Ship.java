import java.awt.Graphics;
import java.awt.Image;


public class Ship {
	public double healthMax		= 100;
	public double speedMax		= 10;
	public double acceleration	= 0.5;
	public double radius		= 10;
	public double fireDelay		= 0.1;
	public double bulletSpeed	= 10;
	
	public Image image			= null;
	public TileSheet tileSheet	= null;
	
	public double x				= 0;
	public double y				= 0;
	public double health		= 100;
	public double hspeed		= 0;
	public double vspeed		= 0;
	
	public int subimg = 0;
	
	
	public Ship() {
		//tileSheet = TileSheet.LargeShip1;
	}
	
	public boolean inArea(int x1, int y1, int x2, int y2) {
		return (x - radius >= x1 && y - radius >= y1 && x + radius < x2 && y + radius < y2);
	}
	
	public double getDirection() {
		return GMath.direction(hspeed, vspeed);
	}
	
	public double getSpeed() {
		return GMath.distance(hspeed, vspeed);
	}
	
	public void setMotion(double direction, double speed) {
		hspeed =  GMath.cos(direction) * speed;
		vspeed =  GMath.sin(direction) * speed;
	}
	
	public void stopMotion() {
		hspeed = 0;
		vspeed = 0;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSpeed(double speed) {
		double dir = getDirection();
		hspeed =  GMath.cos(dir) * speed;
		vspeed =  GMath.sin(dir) * speed;
	}
	
	public void accelerate() {
		setSpeed(getSpeed() + acceleration);
	}
	
	public void update() {
		//subimg += 1;
		if (subimg >= 6)
			subimg = 0;
		
		shipMove();
	}
	
	public void shipMove() {
		x += hspeed;
		y += vspeed;
	}
	
	public void draw(Graphics g) {
		double dir = getDirection();
		//tileSheet.drawSubimg(g, subimg, (int) x, (int) y);
		//ImageDrawer.drawImage(g, ImageLoader.SmallShip1, x, y, 24, 24, 1, dir, 1);
	}
}
