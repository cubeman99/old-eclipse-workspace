package brainOLD;

import common.GMath;
import critter.Critter;


public class Brain {
	public static final double MAX_RANDOM = 100.0;
	
	public Critter critter;
	public NeuralNetwork neuralNet = new NeuralNetwork();;
	
	/* Input Neurons */
	public Input inRed			= new Input(neuralNet, "red", 0, 255);
	public Input inGreen		= new Input(neuralNet, "grn", 0, 255);
	public Input inBlue			= new Input(neuralNet, "blu", 0, 255);
	public Input inEnergy		= new Input(neuralNet, "eng", 0, Critter.MAX_ENERGY);
	public Input inRandom		= new Input(neuralNet, "rnd", 0, MAX_RANDOM);

	/* Output Neurons */
	public Output onMove		= new Output(neuralNet);
	public Output onTurnC		= new Output(neuralNet);
	public Output onTurnCC		= new Output(neuralNet);
	public Output onReproduce	= new Output(neuralNet);
	public Output onAttack		= new Output(neuralNet);
	
	public Brain(Critter critter) {
		this.critter   = critter;
		
	}
	
	public void createRandomNeuralNetwork() {
		neuralNet.createRandomNetwork();
	}
	
	public void update() {
		inRed.setValue(critter.sightRed);
		inGreen.setValue(critter.sightGreen);
		inBlue.setValue(critter.sightBlue);
		inEnergy.setValue(critter.energy);
		inRandom.setValue(GMath.random(MAX_RANDOM));
		
		
		neuralNet.update();
		
		if (onMove.triggered) {
			critter.speed += 0.01;
		}
		else {
			critter.speed = GMath.max(critter.speed - 0.01, 0.0);
		}
		if (onTurnC.triggered) {
			critter.direction -= 0.1;
		}
		if (onTurnCC.triggered) {
			critter.direction += 0.1;
		}
		
		critter.mate   = onReproduce.triggered;
		critter.attack = onAttack.triggered;
	}
	
	
	public Brain getCopy(Critter critter) {
		Brain copy     = new Brain(critter);
		this.neuralNet.storeToCopy(copy.neuralNet);
		
		return copy;
	}
}