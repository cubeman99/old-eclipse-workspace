package projects.neuralNetworks;

import java.util.ArrayList;

public class NeuronLayer {
	private ArrayList<Neuron> neurons;
	private NeuronLayer nextLayer;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public NeuronLayer(int size) {
		this.neurons = new ArrayList<Neuron>();
		
		for (int i = 0; i < size; i++) {
			neurons.add(new Neuron());
		}
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Neuron getNeuron(int index) {
		return neurons.get(index);
	}
	
	public NeuronLayer getNextLayer() {
		return nextLayer;
	}
	
	public int size() {
		return neurons.size();
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < neurons.size(); i++) {
			if (i > 0)
				str += ", ";
			str += "[" + neurons.get(i) + "]";
		}
		return str;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public double[] calculate(double[] inputs) {
		double[] outputs = new double[nextLayer.size()];
		
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = 0;
			
			// Sum up all the inputs.
			for (int j = 0; j < neurons.size(); j++)
				outputs[i] += inputs[j] * neurons.get(j).getWeight(i);
			
			// Tanh that sum.
			outputs[i] = Math.tanh(outputs[i]);
		}
		
		return outputs;
	}
	
	public void mutate(double amount) {
		for (int i = 0; i < neurons.size(); i++) {
			neurons.get(i).mutate(amount);
		}
	}
	
	public void set(NeuronLayer other) {
		for (int i = 0; i < neurons.size(); i++) {
			neurons.get(i).set(other.getNeuron(i));
		}
	}
	
	public void initialize(NeuronLayer nextLayer) {
		this.nextLayer = nextLayer;
		for (int i = 0; i < neurons.size(); i++) {
			int connections = (nextLayer == null ? 0 : nextLayer.size());
			neurons.get(i).initialize(connections);
		}
	}
}
