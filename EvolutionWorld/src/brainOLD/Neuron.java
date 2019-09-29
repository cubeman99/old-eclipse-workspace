package brainOLD;

import java.util.ArrayList;
import common.GMath;


public class Neuron {
	public static final double MAX_WEIGHT		= 10.0;
	public static final int TYPE_GREATER_THAN	= 0;
	public static final int TYPE_LESS_THAN		= 1;
	public static final int TYPE_OR				= 2;
	public static final int TYPE_AND			= 3;

	public NeuralNetwork neuralNet;
	public boolean fired;
	public boolean updated;
	public int type;
	public Input inputLink;
	public double threshhold;
	public double weight;
	public ArrayList<NLink> neuronLinks;
	
	
	public Neuron(NeuralNetwork neuralNet) {
		this.neuralNet = neuralNet;
		
		this.type        = TYPE_GREATER_THAN;
		this.fired       = false;
		this.updated     = false;

		this.inputLink   = null;
		this.threshhold  = 0.0;
		this.weight      = GMath.random(MAX_WEIGHT);
		this.neuronLinks = new ArrayList<NLink>();
	}
	
	public void fire() {
		fired = true;
	}
	
	public boolean isFired() {
		return fired;
	}
	
	public void createRandomLinks() {
		ArrayList<Neuron> possibleLinks = new ArrayList<Neuron>();
		for (Neuron n : neuralNet.neurons) {
			if (n != this) {
				possibleLinks.add(n);
			}
		}
		
		if (possibleLinks.size() >= 2 && GMath.randomInt(16) == 0) {
			type = GMath.randomBoolean() ? TYPE_OR : TYPE_AND;
    		neuronLinks.clear();
    		int totalLinks = 4;
			
    		
    		for (int i = 0; i < GMath.min(totalLinks, possibleLinks.size()); i++) {
        		double w  = 1.0;//GMath.random() * MAX_WEIGHT;
        		int index = GMath.randomInt(possibleLinks.size());
        		Neuron n  = possibleLinks.get(index);
        		
        		possibleLinks.remove(index);
        		addNeuronLink(n, w);
    		}
		}
		else {
			type      = GMath.randomBoolean() ? TYPE_GREATER_THAN : TYPE_LESS_THAN;
			inputLink = null;
			weight    = GMath.random() * MAX_WEIGHT;
			if (neuralNet.inputs.size() > 0) {
				inputLink = neuralNet.inputs.get(GMath.randomInt(neuralNet.inputs.size()));
				threshhold = inputLink.valueLowerLimit + GMath.random(inputLink.valueUpperLimit - inputLink.valueLowerLimit);
//				System.out.print(inputLink.name + " ");
//				System.out.print((type == TYPE_GREATER_THAN) ? ">" : "<");
//				System.out.println(threshhold);
			}
		}
	}
	
	public void addNeuronLink(Neuron n, double weight) {
		neuronLinks.add(new NLink(this, n, weight));
	}
	
	public void mutate() {
		
	}
	
	public void update() {
		updated = true;
		
		if (type == TYPE_GREATER_THAN ) {
			fired = (inputLink.value > threshhold);
		}
		else if (type == TYPE_LESS_THAN) {
			fired = (inputLink.value < threshhold);
		}
		else {
    		fired = (type == TYPE_OR) ? false : true;
    		
    		for (NLink link : neuronLinks) {
    			if (link.link.updated) {
    				if (type == TYPE_OR)
    					fired = fired || link.link.isFired();
    				else
    					fired = fired && link.link.isFired();
    			}
    			else {
    				updated = false;
    				break;
    			}
    		}
		}
	}
	
	public Neuron storeToCopy(Neuron copy) {
		copy.type   = this.type;
		
		if (type < 2) {
			copy.threshhold = this.threshhold;
			copy.weight     = this.weight;
			int index       = this.neuralNet.inputs.indexOf(inputLink);
			copy.inputLink  = copy.neuralNet.inputs.get(index);
		}
		else {
			copy.neuronLinks = new ArrayList<NLink>();
			for (int i = 0; i < neuronLinks.size(); i++) {
				NLink link = neuronLinks.get(i);
				int index  = this.neuralNet.neurons.indexOf(link.link);
				copy.neuronLinks.add(new NLink(copy, copy.neuralNet.neurons.get(index), link.weight));
			}
		}
		
		return copy;
	}
}
