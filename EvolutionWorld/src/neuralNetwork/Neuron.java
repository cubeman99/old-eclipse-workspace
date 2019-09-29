package neuralNetwork;

import java.util.ArrayList;
import common.GMath;

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
	
	public void initialize(int numConnections) {
		weights = new ArrayList<Double>();
		for (int i = 0; i < numConnections; i++) {
			weights.add((GMath.random.nextDouble() - 0.5) / 2);
		}
	}
}
