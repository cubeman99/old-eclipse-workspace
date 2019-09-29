
public class IntersectionSolutions {
	public Vector s1;
	public Vector s2;
	public int solutions;
	
	public IntersectionSolutions() {
		solutions = 0;
	}
	
	public IntersectionSolutions(Vector v) {
		s1 = new Vector(v);
		solutions = 1;
	}
	
	public IntersectionSolutions(Vector v1, Vector v2) {
		s1 = new Vector(v1);
		s2 = new Vector(v2);
		solutions = 2;
	}
}
