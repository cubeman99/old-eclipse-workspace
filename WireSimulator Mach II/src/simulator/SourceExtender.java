package simulator;

import main.ImageLoader;

public class SourceExtender extends PowerSource {
	
	public SourceExtender() {this(0, 0);}

	public SourceExtender(int x, int y) {
		super(x, y);
		setImage("sourceExtender");
	}
	
	public boolean getState(WireGroup sourceWire) {
		return sourceWire.getState();
	}
}
