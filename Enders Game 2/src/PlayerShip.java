import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class PlayerShip extends Ship {
	public boolean selected = false;
	public boolean moving  = false;
	public int moveX = 0;
	public int moveY = 0;
	
	public PlayerShip() {
		super();
	}
	
	public PlayerShip(double x, double y) {
		super();
		image = ImageLoader.SmallShip1;
		setPosition(x, y);
	}
	
	public void setMoveGoal(Point center, double moveX, double moveY) {
		this.moveX = (int) (moveX - (center.x - x));
		this.moveY = (int) (moveY + (center.y - y));
		moving = true;
	}
	
	public void update() {
		if (moving && GMath.distance(x, y, moveX, moveY) > 10) {
			setMotion(GMath.direction(moveX - x, moveY - y), 4);
		}
		else {
			stopMotion();
			moving = false;
		}
		
		super.update();
	}
	
	public void draw(Graphics g) {
		super.draw(g);
		
		if (selected) {
			g.setColor(Color.green);
			g.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
		}
	}
}
