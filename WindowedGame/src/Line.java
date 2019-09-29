import java.awt.Graphics;


public class Line {
	public Vector endA;
	public Vector endB;
	
	public Line(Vector A, Vector B) {
		endA = A;
		endB = B;
	}
	
	public Line(float x1, float y1, float x2, float y2) {
		endA = new Vector(x1, y1);
		endB = new Vector(x2, y2);
	}
	
	public Vector getStart() {
		return endA;
	}
	
	public Vector getEnd() {
		return endB;
	}
	
	public float getX1() {
		return endA.getX();
	}
	
	public float getY1() {
		return endA.getY();
	}
	
	public float getX2() {
		return endB.getX();
	}
	
	public float getY2() {
		return endB.getY();
	}
	
	public void invert() {
		Vector tempVec = new Vector(endA);
		endA.set(endB);
		endB.set(tempVec);
	}
	
	public Vector getVec() {
		return new Vector(endB.getX() - endA.getX(), endB.getY() - endA.getY());
	}
	
	
	public float length() {
		return (float) Math.sqrt(endA.distance(endB));
	}
	
	public Vector getClosestPoint(Vector point) {
		Vector loc = new Vector(point);
		loc.sub(getStart());
		
		Vector vec = new Vector(getVec());
		Vector v   = new Vector(vec);
		Vector v2  = new Vector(vec);
		v2.negate();
		
		v.normalise();
		Vector proj = new Vector(loc.projectOntoUnit(v));
		if( proj.lengthSquared() > vec.lengthSquared() ) {
			return new Vector(getEnd());
		}
		proj.add(getStart());
		
		Vector other = new Vector(proj);
		other.sub(getEnd());
		if( other.lengthSquared() > vec.lengthSquared() ) {
			return new Vector(getStart());
		}
		
		return new Vector(proj);
	}
	
	public void draw(Graphics g) {
		g.drawLine((int)getX1(), (int)getY1(), (int)getX2(), (int)getY2());
	}
}
