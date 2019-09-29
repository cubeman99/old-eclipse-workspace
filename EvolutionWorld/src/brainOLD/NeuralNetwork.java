package brainOLD;

import java.util.ArrayList;


public class NeuralNetwork {
	public ArrayList<Input> inputs;
	public ArrayList<Output> outputs;
	public ArrayList<Neuron> neurons;
	
	
	public NeuralNetwork() {
		inputs  = new ArrayList<Input>();
		outputs = new ArrayList<Output>();
		neurons = new ArrayList<Neuron>();
	}
	
	public void createRandomNetwork() {
		
		for (int i = 0; i < 40; i++) {
			neurons.add(new Neuron(this));
		}
		
		for (Neuron n : neurons) {
			n.createRandomLinks();
		}
		
		for (Output out : outputs) {
			out.createRandomLinks();
		}
		
		//System.out.println("[I = " + inputs.size() + ", O = " + outputs.size() + ", N = " + neurons.size() + "]");
	}
	
	public void addInput(Input in) {
		inputs.add(in);
	}
	
	public void addOutput(Output out) {
		outputs.add(out);
	}
	
	public void mutate() {
		for (Neuron n : neurons) {
			n.mutate();
		}
		for (Output out : outputs) {
			out.mutate();
		}
	}
	
	public void update() {
		for (Neuron n : neurons) {
			n.updated = false;
		}
		boolean allUpdated = false;
		
		for (int i = 0; !allUpdated && i < 10; i++) {
			allUpdated = true;
			for (Neuron n : neurons) {
    			n.update();
    			if (!n.updated)
    				allUpdated = false;
    		}
		}
		
		for (Output out : outputs) {
			out.update();
		}
	}
	
	public NeuralNetwork storeToCopy(NeuralNetwork copy) {
		
		for (int i = 0; i < neurons.size(); i++) {
			copy.neurons.add(new Neuron(copy));
		}
		
		for (int i = 0; i < neurons.size(); i++) {
			neurons.get(i).storeToCopy(copy.neurons.get(i));
		}
		
		for (int i = 0; i < outputs.size(); i++) {
			Output out  = outputs.get(i);
			Output cOut = copy.outputs.get(i);
			for (int j = 0; j < out.neuronLinks.size(); j++) {
				int index = neurons.indexOf(out.neuronLinks.get(j));
				cOut.neuronLinks.add(copy.neurons.get(index));
			}
		}
		
		//System.out.println("C:[I = " + copy.inputs.size() + ", O = " + copy.outputs.size() + ", N = " + copy.neurons.size() + "]");
		return copy;
	}
}
