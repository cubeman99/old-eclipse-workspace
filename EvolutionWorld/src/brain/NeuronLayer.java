package brain;

import java.util.ArrayList;

public class NeuronLayer {
	public ArrayList<Neuron> neurons;
	public int numNeurons;
	
	public NeuronLayer(int numNeurons, int numInputsPerNeuron) {
		neurons = new ArrayList<Neuron>();
		this.numNeurons = numNeurons;
		
		for (int i = 0; i < numNeurons; i++)
			neurons.add(new Neuron(numInputsPerNeuron));
	}
}
