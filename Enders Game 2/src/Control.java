import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Control {
	public Point dragPoint = new Point();
	public boolean dragging = false;
	
	public ArrayList<PlayerShip> playerShips;
	public ArrayList<EnemyShip> enemyShips;
	
	public Control() {
		playerShips = new ArrayList<PlayerShip>();
		enemyShips  = new ArrayList<EnemyShip>();
		
		playerShips.add(new PlayerShip(100, 100));
		playerShips.add(new PlayerShip(140, 100));
		playerShips.add(new PlayerShip(180, 100));
		
	}
	
	public void deselectAll() {
		for (PlayerShip PS : playerShips) {
			PS.selected = false;
		}
	}
	
	public void selectArea(Point p1, Point p2) {
		int x1 = Math.min(p1.x, p2.x);
		int y1 = Math.min(p1.y, p2.y);
		int x2 = Math.max(p1.x, p2.x);
		int y2 = Math.max(p1.y, p2.y);
		
		for (PlayerShip PS : playerShips) {
			if (PS.inArea(x1, y1, x2, y2)) {
				PS.selected = true;
			}
		}
	}
	
	public void update() {
		if (Game.mbRightPressed) {
			Point center = new Point();
			int count = 0;
			for (PlayerShip PS : playerShips) {
				if (PS.selected) {
					center.x += PS.x;
					center.y += PS.y;
					count += 1;
				}
			}
			if (count > 0) {
				center.x /= count;
				center.y /= count;
			}
			for (PlayerShip PS : playerShips) {
				if (PS.selected) {
					PS.setMoveGoal(center, Game.mouseX, Game.mouseY);
				}
			}
		}
		if (Game.mbLeftPressed) {
			dragging  = true;
			dragPoint = Game.getMousePoint();
		}
		if (dragging) {
			if (!Game.mbLeft) {
				dragging = false;
				// Select any ships in boundary:
				if (!Game.keyDown[KeyEvent.VK_SHIFT])
					deselectAll();
				for (PlayerShip PS : playerShips) {
					if (GMath.distance(Game.mouseX, Game.mouseY, PS.x, PS.y) <= PS.radius) {
						PS.selected = true;
					}
				}
				selectArea(dragPoint, Game.getMousePoint());
			}
		}
		

		for (PlayerShip PS : playerShips) {
			PS.update();
		}
		for (EnemyShip ES : enemyShips) {
			ES.update();
		}
	}
	
	public void draw(Graphics g) {
		for (PlayerShip PS : playerShips) {
			PS.draw(g);
		}
		for (EnemyShip ES : enemyShips) {
			ES.draw(g);
		}
		
		if (dragging) {
			Point p2 = Game.getMousePoint();
			int x1 = Math.min(dragPoint.x, p2.x);
			int y1 = Math.min(dragPoint.y, p2.y);
			int x2 = Math.max(dragPoint.x, p2.x);
			int y2 = Math.max(dragPoint.y, p2.y);
			g.setColor(Color.green);
			g.drawRect(x1, y1, x2 - x1, y2 - y1);
		}
//		ImageDrawer.drawImage(g, ImageLoader.LargeShip1, 200, 200, (int) 0, (int) 0, 1, 45, 1);
//		g.drawImage(ImageLoader.LargeShip1, 200, 200, null);
	}
}
