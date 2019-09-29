package brain;

import java.util.ArrayList;
import common.GMath;

public class Neuron {
	public ArrayList<Double> weights;
	public int numInputs;
	
	public Neuron(int numInputs) {
		weights = new ArrayList<Double>();
		this.numInputs = numInputs;
		
		for (int i = 0; i < numInputs + 1; i++)
			weights.add(GMath.randomClamped());
	}
}
