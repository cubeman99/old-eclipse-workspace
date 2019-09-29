import java.awt.Point;


public class DataPoint {
	public int x = 0;
	public int y = 0;
	public int basex = 0;
	public int basey = 0;
	public String label = "";
	public boolean state = false;
	public boolean isOutput = false;
	
	public DataPoint(int x, int y, int bx, int by, String label, boolean state) {
		this.x = x;
		this.y = y;
		this.basex = bx;
		this.basey = by;
		this.label = label;
		this.state = state;
	}
	
	public DataPoint(int x, int y, int bx, int by, String label) {
		this(x, y, bx, by, label, false);
	}
	
	public DataPoint(int x, int y, int bx, int by, boolean state) {
		this(x, y, bx, by, "", state);
	}
	
	public DataPoint(int x, int y, int bx, int by) {
		this(x, y, bx, by, "", false);
	}
	
	public DataPoint(Point p, int bx, int by,  boolean state) {
		this(p.x, p.y, bx, by, "", state);
	}

	public DataPoint(Point p, int bx, int by) {
		this(p.x, p.y, bx, by, "", false);
	}
	
	public boolean getState() {
		return WireSimulator.getWireState(basex + 1 + (x * 2), basey + 1 + (y * 2));
	}
}
