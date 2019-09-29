package projects.neuralNetworks;

import java.util.ArrayList;

public class NeuralNetwork {
	private ArrayList<NeuronLayer> layers;
	private NeuronLayer outputLayer;
	private NeuronLayer inputLayer;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public NeuralNetwork(int numInputs, int numOutputs) {
		layers = new ArrayList<NeuronLayer>();
		inputLayer = new NeuronLayer(numInputs);
		layers.add(inputLayer);
		outputLayer = new NeuronLayer(numOutputs);
		layers.add(outputLayer);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public NeuronLayer getLayer(int index) {
		return layers.get(index);
	}
	
	public int getNumLayers() {
		return layers.size();
	}
	
	public int getNumHiddenLayers() {
		return (layers.size() - 2);
	}
	
	public int getNumInputs() {
		return inputLayer.size();
	}
	
	public int getNumOutputs() {
		return outputLayer.size();
	}
	
	public NeuronLayer getInputLayer() {
		return inputLayer;
	}
	
	public NeuronLayer getOutputLayer() {
		return outputLayer;
	}
	
	@Override
	public String toString() {
		String str = "";
		str += "Neural Network:\n";
		str += "Input Layer: " + inputLayer + "\n";
		for (int i = 1; i < layers.size() - 1; i++)
			str += "Hidden Layer " + i + ": " + layers.get(i) + "\n";
		str += "Output Layer: " + outputLayer;
		return str;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public double[] calculate(double...inputs) {
		double[] results = inputs;
		for (int i = 0; i < layers.size() - 1; i++)
			results = layers.get(i).calculate(results);
		return results;
	}
	
	public void initialize() {
		for (int i = 0; i < layers.size() - 1; i++)
			layers.get(i).initialize(layers.get(i + 1));
		layers.get(layers.size() - 1).initialize(null);
	}
	
	public void mutate(double amount) {
		for (int i = 0; i < layers.size() - 1; i++)
			layers.get(i).mutate(amount);
	}
	
	public void set(NeuralNetwork copy) {
		for (int i = 0; i < layers.size() - 1; i++)
			layers.get(i).set(copy.getLayer(i));
	}
	
	public void addHiddenLayer(int size) {
		layers.add(1, new NeuronLayer(size));
	}
}
