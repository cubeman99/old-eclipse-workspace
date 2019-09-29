package projects.neuralNetworks;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Mouse;
import cmg.math.geometry.Line;
import cmg.math.geometry.Vector;

public class NetworkRunner {
	private GameRunner runner;
	private NeuralNetwork neuralNet;
	private ArrayList<NeuronNode> nodes;
	private double[] inputs;
	
	public NetworkRunner(GameRunner runner) {
		this.runner = runner;
		
		
		
		neuralNet = new NeuralNetwork(2, 1);
		neuralNet.addHiddenLayer(4);
		neuralNet.initialize();
		System.out.println(neuralNet);

		inputs = new double[] {1, 16789, 0};
		double[] results = neuralNet.calculate(inputs);
		System.out.print("Output: ");
		for (int i = 0; i < results.length; i++)
			System.out.print(results[i] + ", ");
		System.out.println();
			
		nodes = new ArrayList<NeuronNode>();
		
		double spacing = 100;
		Vector drawPos = new Vector(runner.getViewWidth() / 2, 100);
		for (int i = 0; i < neuralNet.getNumLayers(); i++) {
			NeuronLayer layer = neuralNet.getLayer(i);
			for (int j = 0; j < layer.size(); j++) {
				Vector dp = drawPos.minus((spacing * (layer.size()
						- 1 - (j * 2))) / 2, 0);
				nodes.add(new NeuronNode(layer, layer.getNeuron(j), dp));
			}
			drawPos.y += spacing;
		}
	}
	
	public NeuronNode getNode(Neuron n) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).neuron == n)
				return nodes.get(i);
		}
		return null;
	}
	
	public void update() {
	}
	
	public void draw() {
		Draw.setColor(Color.WHITE);
		Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		
		double[] currentInputs = inputs;
		for (int i = 0; i < neuralNet.getNumLayers() - 1; i++) {
			NeuronLayer layer     = neuralNet.getLayer(i);
			NeuronLayer nextLayer = neuralNet.getLayer(i + 1);
    		double[] outputs      = new double[nextLayer.size()];
    		
    		for (int j = 0; j < outputs.length; j++) {
    			outputs[j] = 0;
    			
    			// Sum up all the inputs.
    			for (int k = 0; k < layer.size(); k++)
    				outputs[j] += currentInputs[k] * layer.getNeuron(k).getWeight(j);
    			
    			double temp = outputs[j];
    			
    			// Tanh that sum.
    			outputs[j] = Math.tanh(outputs[j]);
    			
				getNode(nextLayer.getNeuron(j)).draw(temp, outputs[j]);
    		}
    		
    		if (i == 0) {
    			for (int j = 0; j < layer.size(); j++)
    				getNode(layer.getNeuron(j)).draw(inputs[j]);
    		}
    		
    		currentInputs = outputs;
		}
	}
	
	public class NeuronNode {
		private NeuronLayer layer;
		private Neuron neuron;
		private Vector position;
		
		public NeuronNode(NeuronLayer layer, Neuron n, Vector pos) {
			this.layer    = layer;
			this.neuron   = n;
			this.position = new Vector(pos);
		}

		public void draw(double value, double tanValue) {
			draw(tanValue);
			Draw.setColor(Color.RED);
			String s = String.format("%.3f", value);
			Draw.drawString(s, position.minus(0, 8), Draw.CENTER, Draw.MIDDLE);
		}

		public void draw(double value) {
			draw();
			Draw.setColor(Color.BLUE);
			String s = String.format("%.3f", value);
			Draw.drawString(s, position.plus(0, 8), Draw.CENTER, Draw.MIDDLE);
		}
		
		public void draw() {
			Draw.setColor(Color.BLACK);
			
			
			if (layer.getNextLayer() != null) {
				NeuronLayer l = layer.getNextLayer();
				for (int i = 0; i < l.size(); i++) {
					Line line = new Line(position, getNode(l.getNeuron(i)).position);
					boolean hover = (Mouse.getVector().distanceToSegment(line) < 3);
					
					int a = 255 - (int) (Math.max(0, Math.min(1, Math.abs(neuron.getWeight(i)))) * 255);
					if (hover)
						Draw.setColor(Color.PINK);
					else
						Draw.setColor(new Color(a, a, a));
					Draw.draw(line);
					
					if (hover) {
						Draw.setColor(Color.RED);
    					String s = String.format("%.3f", neuron.getWeight(i));
    					Draw.drawString(s, Mouse.getVector().minus(0, 14), Draw.CENTER, Draw.MIDDLE);
					}
				}
			}
			
			Draw.setColor(Color.WHITE);
			Draw.fillCircle(position, 32);
			Draw.setColor(Color.BLACK);
			Draw.drawCircle(position, 32);
		}
	}
}
