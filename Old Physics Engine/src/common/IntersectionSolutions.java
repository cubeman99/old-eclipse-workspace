package common;

import java.util.ArrayList;

public class IntersectionSolutions {
	public ArrayList<Vector> solutions;
	
	public IntersectionSolutions(Vector[] sols) {
		solutions = new ArrayList<Vector>();
		for (int i = 0; i < sols.length; i++) {
			if (sols[i] != null)
				solutions.add(sols[i]);
		}
	}
}
