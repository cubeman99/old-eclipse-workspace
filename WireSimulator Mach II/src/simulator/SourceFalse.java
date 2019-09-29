package simulator;

import java.awt.Color;
import java.awt.Graphics;

public class SourceFalse extends PowerSource {
	
	public SourceFalse() {this(0, 0);}
	
	public SourceFalse(int x, int y) {
		super(x, y);
		setImage("sourceFalse");
		setState(false);
	}
}
