import java.awt.Color;
import java.awt.Graphics;


public class Tile {
	public double amount = 0;
	public boolean solid = false;
	public int x;
	public int y;
	
	public boolean updated = false;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		int dx = x * WaterSimulator.SCALE;
		int dy = y * WaterSimulator.SCALE;
		if (solid) {
			g.setColor(Color.gray);
			g.fillRect(dx, dy, WaterSimulator.SCALE, WaterSimulator.SCALE);
			return;
		}
		g.setColor(Color.blue);
//		int xx = (int) ((amount / 100.0d) * WaterSimulator.SCALE);
		int xx = (int) ((amount / 100.0d) * WaterSimulator.SCALE);
		int oo = WaterSimulator.SCALE - xx;
//		g.fillRect(dx, dy + WaterSimulator.SCALE - xx, WaterSimulator.SCALE, xx);
		g.fillRect(dx + (oo / 2), dy + (oo / 2), xx, xx);
	}
}
