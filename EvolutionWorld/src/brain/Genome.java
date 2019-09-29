package brain;

import java.util.ArrayList;

public class Genome {
	public ArrayList<Double> weights;
	public double fitness;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Genome() {
		weights = new ArrayList<Double>();
		fitness = 0;
	}
	
	public Genome(ArrayList<Double> weights, double fitness) {
		this.weights = weights;
		this.fitness = fitness;
	}
	
	public Genome(Genome copy) {
		this.fitness = copy.fitness;
		this.weights = new ArrayList<Double>();
		for (int i = 0; i < copy.weights.size(); i++)
			this.weights.add(copy.weights.get(i));
	}
	
	public boolean equals(Genome other) {
		if (weights.size() != other.weights.size())
			return false;
		
		for (int i = 0; i < weights.size(); i++) {
			if (weights.get(i) != other.weights.get(i))
				return false;
		}
		
		return true;
	}

	public Genome getCopy() {
		return new Genome(this);
	}
}
