package simulator;

import java.awt.Color;
import java.awt.Graphics;

public class PowerSource extends GridObject {
	private boolean state = true;
	
	public PowerSource() {this(0, 0);}
	
	public PowerSource(int x, int y) {
		super(x, y);
		state = true;
	}

	public boolean getState(WireGroup sourceWire) {
		return getState();
	}
	
	public boolean getState() {
		return state;
	}
	
	public void setState(boolean newState) {
		this.state = newState;
	}
}
