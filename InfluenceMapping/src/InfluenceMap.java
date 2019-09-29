


import java.awt.Color;
import java.awt.Graphics;

public class InfluenceMap extends Entity {
	public double[][] mapdata; 
	public int width;
	public int height;
	public double momentum = 0.5d;
	public double decay = 1.0d;
	public MouseButtons mouseButtons;
	
	public InfluenceMap(MouseButtons mouseButtons, int w, int h) {
		width = w;
		height = h;
		mapdata = new double[w][h];
		this.mouseButtons = mouseButtons;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				mapdata[x][y] = 0;
			}
		}
	}
	
	public void propagate(int xx, int yy, double a) {
		mapdata[xx][yy] = a;
		System.out.println(xx);
		System.out.println(xx);
		System.out.println();
/*
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x != xx || y != yy) {
					ArrayList<Point> connections = new ArrayList<Point>();
					if (x > 0)
						connections.add(new Point(x - 1, y));
					if (y > 0)
						connections.add(new Point(x, y - 1));
					if (x < width - 1)
						connections.add(new Point(x + 1, y));
					if (y < height - 1)
						connections.add(new Point(x, y + 1));
					
					double maxInf = 0;
					
					for (int i = 0; i < connections.size(); i++) {
						Point p = connections.get(i);
						double inf = mapdata[p.x][p.y] * Math.exp(-decay);
						maxInf = Math.max(inf, maxInf);
					}
					mapdata[x][y] = mapdata[x][y] + (momentum * (maxInf - mapdata[x][y]));
				}
			}
		}*/
		/*
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				mapdata[x][y] = 1;
			}
		}
		*/
	}
	
	public void update() {
		
		if (mouseButtons.isPressed(1)) {
			propagate(mouseButtons.getX(), mouseButtons.getY(), 1.0d);
		}
	}
	
	public void draw(Graphics g) {

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float a = Math.max(0.0f, Math.min(1.0f, (float) mapdata[x][y]));
				g.setColor(new Color(a, 0, 0));
				g.drawRect(x, y, 0, 0);
			}
		}
	}
}
