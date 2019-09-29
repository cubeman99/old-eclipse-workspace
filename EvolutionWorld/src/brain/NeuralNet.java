package brain;

import java.util.ArrayList;
import common.Settings;

public class NeuralNet {
	public ArrayList<NeuronLayer> layers;
	public int numInputs;
	public int numOutputs;
	public int numHiddenLayers;
	public int numNeuronsPerHiddenLayer;
	

	// ================== CONSTRUCTORS ================== //

	public NeuralNet(Genome genome) {
		this();
		putWeights(genome.weights);
	}
	public NeuralNet() {
		layers = new ArrayList<NeuronLayer>();
		numInputs = Settings.NUM_INPUTS;
		numOutputs = Settings.NUM_OUTPUTS;
		numHiddenLayers = Settings.NUM_HIDDEN;
		numNeuronsPerHiddenLayer = Settings.NUM_NEURONS_PER_HIDDEN_LAYER;
		
		createNet();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Returns a vector containing the weights **/
	public ArrayList<Double> getWeights() {
		ArrayList<Double> weights = new ArrayList<Double>();
		
		// For each layer:
		for (int i = 0; i < numHiddenLayers + 1; i++) {
			// For each neuron:
			for (int j = 0; j < layers.get(i).numNeurons; j++) {
				// For each weight:
				for (int k = 0; k < layers.get(i).neurons.get(j).numInputs; k++)
					weights.add(layers.get(i).neurons.get(j).weights.get(k));
			}
		}
		
		return weights;
	}

	/** Returns the total number of weights needed for the net. **/
	public int getNumberOfWeights() {
		int weights = 0;
		
		// For each layer:
		for (int i = 0; i < numHiddenLayers + 1; i++) {
			// For each neuron:
			for (int j = 0; j < layers.get(i).numNeurons; j++) {
				// For each weight:
				for (int k = 0; k < layers.get(i).neurons.get(j).numInputs; k++)
					weights++;
			}
		}
		
		return weights;
	}
	
	public double sigmoid(double activation, double response) {
		return (1 / (1 + Math.exp(-activation / response)));
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** this method builds the ANN. The weights are all initially set to 
		random values -1 < w < 1 **/
	public void createNet() {	
		//create the layers of the network
		if (numHiddenLayers > 0) {
    		// Create the first hidden layer.
    		layers.add(new NeuronLayer(numNeuronsPerHiddenLayer,
    				numNeuronsPerHiddenLayer));
    		
    		for (int i = 0; i < numHiddenLayers - 1; ++i) {
    			layers.add(new NeuronLayer(numNeuronsPerHiddenLayer,
    					numNeuronsPerHiddenLayer));
    		}
    		
    		// Create output layer.
    		layers.add(new NeuronLayer(numOutputs, numNeuronsPerHiddenLayer));
		}
		else {
			// Just create output layer.
    		layers.add(new NeuronLayer(numOutputs, numInputs));
		}
	}
	
	/** Given a vector of doubles this function replaces the weights in the NN
		with the new values **/
	public void putWeights(ArrayList<Double> newWeights) {
		int index = 0;
		
		// For each layer:
		for (int i = 0; i < numHiddenLayers + 1; i++) {
			// For each neuron:
			for (int j = 0; j < layers.get(i).numNeurons; j++) {
				// For each weight:
				for (int k = 0; k < layers.get(i).neurons.get(j).numInputs; k++)
					layers.get(i).neurons.get(j).weights.set(k, newWeights.get(index++));
			}
		}
	}
	
	public ArrayList<Double> update(ArrayList<Double> inputs) {
		ArrayList<Double> outputs = new ArrayList<Double>();
		int weightIndex = 0;
		
		if (inputs.size() != numInputs)
			return outputs;
		
		for (int i = 0; i < numHiddenLayers + 1; i++) {
			if (i > 0) {
				// copy outputs to inputs
				inputs.clear();
				for (int ii = 0; ii < outputs.size(); ii++) {
					inputs.add(new Double(outputs.get(ii)));
				}
			}
			
			outputs.clear();
			weightIndex = 0;
			
			//for each neuron sum the (inputs * corresponding weights).Throw 
			//the total at our sigmoid function to get the output.
			for (int j = 0; j < layers.get(i).numNeurons; j++) {
				double netInput = 0;
				int netInputs   = layers.get(i).neurons.get(j).numInputs;
				
				// For each weight:
				for (int k = 0; k < numInputs - 1; k++) {
					// Sum the weights x inputs
					netInput += layers.get(i).neurons.get(j).weights.get(k)
							* inputs.get(weightIndex++);
				}
				
				// Add in the bias.
				netInput += layers.get(i).neurons.get(j).weights.get(numInputs - 1)
						* Settings.BIAS;
				
				// We can store the outputs from each layer as we generate them. 
				// The combined activation is first filtered through the sigmoid 
				// function.
				outputs.add(sigmoid(netInput,
						Settings.ACTIVATION_RESPONSES));

				weightIndex = 0;
			}
		}
		
		return outputs;
	}
}
