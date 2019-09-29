package brainOLD;

import java.util.ArrayList;
import common.GMath;


public class Output {
	public NeuralNetwork neuralNet;
	public boolean triggered;
	public ArrayList<Neuron> neuronLinks;
	
	
	public Output(NeuralNetwork neuralNet) {
		this.neuralNet = neuralNet;
		this.triggered = false;
		this.neuronLinks     = new ArrayList<Neuron>();
		
		this.neuralNet.addOutput(this);
	}
	
	public void createRandomLinks() {
		neuronLinks.clear();
		ArrayList<Neuron> possibleLinks = new ArrayList<Neuron>();
		for (Neuron n : neuralNet.neurons) {
			possibleLinks.add(n);
		}
		
		int totalLinks = 2;
		
		for (int i = 0; i < GMath.min(totalLinks, possibleLinks.size()); i++) {
    		int index = GMath.randomInt(possibleLinks.size());
    		neuronLinks.add(possibleLinks.get(index));
    		possibleLinks.remove(index);
		}
	}
	
	public void mutate() {
		
	}
	
	public void update() {
		triggered = false;
		
		for (Neuron n : neuronLinks) {
			if (n.fired)
				triggered = true;
		}
		
	}
}
