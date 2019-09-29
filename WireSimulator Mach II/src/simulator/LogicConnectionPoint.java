package simulator;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class LogicConnectionPoint {
	private int x; // Relative x
	private int y; // Relative y
	private boolean state;
	private WireGroup wireLink;
	private String description;
	private Color color;
	private LogicGate gate;
	
	public int sequenceIndex;
	
	public LogicConnectionPoint(LogicGate gate, int x, int y, Color color, String description) {
		this.gate          = gate;
		this.x             = x;
		this.y             = y;
		this.description   = description;
		this.color		   = color;
		this.state         = false;
		this.wireLink      = null;
		this.sequenceIndex = -1;
	} 
	
	public LogicConnectionPoint(LogicGate gate, int x, int y) {
		this(gate, x, y, Color.WHITE, "");
	}

	
	public Point getPoint() {
		return new Point(x, y);
	}
	
	public void setState(boolean newState) {
		this.state = newState;
	}

	public boolean getState() {
		return state;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Color getColor() {
		return color;
	}
	
	public LogicGate getLogicGate() {
		return gate;
	}
	
	public void linkWire(WireGroup wire) {
		wireLink = wire;
	}
	
	public void passOutput() {
		if (wireLink != null) {
			wireLink.setState(state);
		}
	}
	
	public void updateInput() {
		state = false;
		if (wireLink != null) {
			state = wireLink.getState();;
		}
	}

	public WireGroup getWireGroup(ArrayList<WireGroup> wireGroups) {
		return wireLink;
		
		/*
		for (WireGroup group : wireGroups) {
			if (group.getInputs().contains(this)) {
				return group;
			}
		}
		return null;*/
	}
	
}
