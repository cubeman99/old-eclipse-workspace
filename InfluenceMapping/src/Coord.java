

public class Coord {
	public double x = 0;
	public double y = 0;
	
	public Coord() {
	}
	
	public Coord(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord(Coord c) {
		this.x = c.x;
		this.y = c.y;
	}
	
	public Coord(Node n) {
		this.x = n.x;
		this.y = n.y;
	}
	
}
