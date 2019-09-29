import java.awt.Graphics;


public class EnemyShip extends Ship {
	public boolean selected = false;
	public int movex = 0;
	public int movey = 0;
	
	public Ship ship;
	
	public EnemyShip() {
		super();
	}
	
	public EnemyShip(double x, double y, Ship ship) {
		super();
		
		this.ship = ship;
		this.ship.setPosition(x, y);
	}
	
	public void draw(Graphics g) {
		ship.draw(g);
	}
}
