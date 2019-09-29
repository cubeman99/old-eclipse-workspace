package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import main.Game;

public class WireGroup {
	private ArrayList<Point> wireTiles;
	private ArrayList<LogicConnectionPoint> outputs;
	private ArrayList<LogicConnectionPoint> inputs;
	private ArrayList<PowerSource> sourceInputs;
	private boolean state = false;
	private boolean nextState = false;
	
	public int sequenceIndex = -1;
	
	public WireGroup() {
		wireTiles    = new ArrayList<Point>();
		outputs      = new ArrayList<LogicConnectionPoint>();
		inputs       = new ArrayList<LogicConnectionPoint>();
		sourceInputs = new ArrayList<PowerSource>();
	}
	
	public void addTile(Point p) {
		wireTiles.add(p);
	}
	
	public void addOutput(LogicConnectionPoint output) {
		outputs.add(output);
	}
	
	public void addInput(LogicConnectionPoint input) {
		inputs.add(input);
	}
	
	public void addSource(PowerSource source) {
		sourceInputs.add(source);
	}
	
	public boolean hasPowerSource() {
		return (sourceInputs.size() > 0);
	}
	
	public void tickAsSource() {
		boolean newState = false;
		for (PowerSource source : sourceInputs) {
			if (source.getState(this)) {
				newState = true;
				break;
			}
		}
		setState(newState);
	}
	
	public void removeTile(Point p) {
		wireTiles.remove(p);
	}

	public void switchState() {
		setState(!state);
	}
	
	public void setState(boolean newState) {
		state = newState;
	}
	
	public void setNextState(boolean nextState) {
		this.nextState = (this.nextState || nextState);
	}
	
	public void applyNextState() {
		state = nextState;
		nextState = false;
	}
	
	public boolean getState() {
		return state;
	}
	
	public ArrayList<LogicConnectionPoint> getOutputs() {
		return outputs;
	}
	
	public ArrayList<LogicConnectionPoint> getInputs() {
		return outputs;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.yellow);
		for (Point p : wireTiles) {
			g.drawOval(p.x * Grid.TILE_SIZE, p.y * Grid.TILE_SIZE, Grid.TILE_SIZE, Grid.TILE_SIZE);
		}
	}
}
