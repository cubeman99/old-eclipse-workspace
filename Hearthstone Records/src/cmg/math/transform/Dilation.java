package cmg.math.transform;


import cmg.math.geometry.Vector;

public class Dilation {
	private Vector origin;
	private double scalar;
	
	
	
	public Dilation(Vector origin, double scalar) {
		this.origin = new Vector(origin);
		this.scalar = scalar;
	}
	
	public Dilation(Vector anchor, Vector handle, double distance) {
		// TODO
	}
}
