package neuralNetwork;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class NNTester {
	public static void main(String[] args) {
		// Input Layer:  2
		// Hidden Layer: 3
		// Output Layer: 1
		NeuralNetwork network = new NeuralNetwork(2, 1);
		network.addHiddenLayer(3);
		network.initialize();
		
		System.out.println(network);
		
		boolean running = true;
		Scanner in = new Scanner(System.in);

		try {
			while (running) {
				System.out.print("Enter " + network.getNumInputs() + " inputs: ");

				double[] inputs = new double[network.getNumInputs()];
				for (int i = 0; i < inputs.length; i++)
					inputs[i] = in.nextDouble();

				double[] outputs = network.calculate(inputs);

				System.out.print("Resulting outputs: ");
				for (int i = 0; i < outputs.length; i++)
					System.out.print(outputs[i] + (i == outputs.length - 1 ? "\n" : " "));
			}
		}
		catch (NoSuchElementException e) {
			System.err.println("Error: Invalid number of inputs.");
		}
	}
}
