import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class Wire {
	public boolean isWire = false;
	public boolean on;
	public int x;
	public int y;
	
	public Wire(int x, int y) {
		this.x = x;
		this.y = y;
		on = false;
	}
	
	public void setWire(boolean isWire) {
		this.isWire = isWire;
	}
	
	public boolean getState() {
		if (!isWire)
			return false;
		return on;
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		if (isWire) {
			int gs = WireSimulator.GRID_SIZE;
			g.setColor(Color.blue);
			if (on)
				g.setColor(Color.red);
			g.fillRect(x * gs, y * gs, gs, gs);
		}
	}
}
