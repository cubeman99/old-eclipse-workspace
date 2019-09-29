package common;

public class Ray {
	public Vector position;
	public double direction;
	
	
	public Ray() {
		this(new Vector(), 0.0);
	}
	
	public Ray(Vector position) {
		this(position, 0.0);
	}
	
	public Ray(double direction) {
		this(new Vector(), direction);
	}
	
	public Ray(Vector position, double direction) {
		this.position  = position;
		this.direction = direction;
	}
	
	
	public Line getNormalLine() {
		return new Line(position, position.plus(Vector.vectorFromPolar(1, direction)));
	}
}
