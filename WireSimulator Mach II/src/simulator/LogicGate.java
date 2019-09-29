package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import main.GMath;

public class LogicGate extends GridObject {
	public ArrayList<LogicConnectionPoint> outputs;
	public ArrayList<LogicConnectionPoint> inputs;
	
	
	public LogicGate(int x, int y, int width, int height) {
		super(x, y);
		this.width  = width;
		this.height = height;
		outputs = new ArrayList<LogicConnectionPoint>();
		inputs  = new ArrayList<LogicConnectionPoint>();
	}
	
	// Find a connection point at the absolute position (x, y)
	public LogicConnectionPoint findIOPoint(Point ioPoint) {
		for (LogicConnectionPoint lcp : outputs) {
			Point p = getIOAbsolutePosition(lcp);
			if (p.equals(ioPoint))
				return lcp;
		}
		for (LogicConnectionPoint lcp : inputs) {
			Point p = getIOAbsolutePosition(lcp);
			if (p.equals(ioPoint))
				return lcp;
		}
		return null;
	}
	
	public LogicConnectionPoint findInputPoint(Point iPoint) {
		for (LogicConnectionPoint lcp : inputs) {
			Point p = getIOAbsolutePosition(lcp);
			if (p.equals(iPoint))
				return lcp;
		}
		return null;
	}
	
	public LogicConnectionPoint findOutputPoint(Point oPoint) {
		for (LogicConnectionPoint lcp : outputs) {
			Point p = getIOAbsolutePosition(lcp);
			if (p.equals(oPoint))
				return lcp;
		}
		return null;
	}
	
	public Point getIOAbsolutePosition(LogicConnectionPoint lcp) {
		return GMath.add(getPoint(), lcp.getPoint());
	}
	
	public Point getPoint() {
		return new Point(x, y);
	}
	
	public void setOutput(boolean state) {
		setOutput(0, state);
	}
	
	public void setOutput(int index, boolean state) {
		if (index < outputs.size())
			outputs.get(index).setState(state);
	}
	
	public boolean getInput() {
		return getInput(0);
	}
	
	public boolean getInput(int index) {
		if (index < inputs.size())
			return inputs.get(index).getState();
		return false;
	}
	
	protected void addOutputPoint(int x, int y) {
		outputs.add(new LogicConnectionPoint(this, x, y));
	}
	
	protected void addInputPoint(int x, int y) {
		inputs.add(new LogicConnectionPoint(this, x, y));
	}
	
	public void updateOutputs() {
		for (LogicConnectionPoint output : outputs) {
			output.passOutput();
		}
	}
	
	public void updateInputs() {
		for (LogicConnectionPoint input : inputs) {
			input.updateInput();
		}
	}
	
	// IMPLEMENTED:
	// Compute the outputs from the inputs
	public void compute() {}
	
	// IMPLEMENTED:
	// Draw Method
	public void draw(Graphics g) {
		if (image != null) {
			super.draw(g);
		}
		else {
    		g.setColor(Color.BLACK);
    		g.drawRect(x * Grid.TILE_SIZE, y * Grid.TILE_SIZE, Grid.TILE_SIZE, Grid.TILE_SIZE);
		}
	}
}
