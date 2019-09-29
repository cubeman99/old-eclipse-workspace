package brainOLD;


public class Input {
	public NeuralNetwork neuralNet;
	public double valueLowerLimit;
	public double valueUpperLimit;
	public double value;
	public String name;
	
	
	public Input(NeuralNetwork neuralNet, String name, double lowerLimit, double upperLimit) {
		this.neuralNet       = neuralNet;
		this.name            = name;
		this.value           = 0;
		this.valueLowerLimit = lowerLimit;
		this.valueUpperLimit = upperLimit;
		
		this.neuralNet.addInput(this);
	}
	
	public void setValueRange(double lowerLimit, double upperLimit) {
		this.valueLowerLimit = lowerLimit;
		this.valueUpperLimit = upperLimit;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
}
