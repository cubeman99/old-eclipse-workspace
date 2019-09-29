package projects.neuralNetworks;

import java.util.ArrayList;
import cmg.math.GMath;

public class Neuron {
	private ArrayList<Double> weights;
	

	
	// ================== CONSTRUCTORS ================== //

	public Neuron() {
		
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public double getWeight(int index) {
		return weights.get(index);
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < weights.size(); i++) {
			if (i > 0)
				str += ", ";
			str += weights.get(i);
		}
		return str;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void mutate(double amount) {
		for (int i = 0; i < weights.size(); i++) {
			double w = (amount - (GMath.random.nextDouble() * amount * 2));
			weights.set(i, weights.get(i) + w);
		}
	}
	
	public void set(Neuron other) {
		for (int i = 0; i < weights.size(); i++) {
			weights.set(i, other.getWeight(i));
		}
	}
	
	public void initialize(int numConnections) {
		weights = new ArrayList<Double>();
		for (int i = 0; i < numConnections; i++) {
			weights.add((GMath.random.nextDouble() - 0.5) * 2);
		}
	}
}
