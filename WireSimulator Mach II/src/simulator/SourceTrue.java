package simulator;

import java.awt.Color;
import java.awt.Graphics;

public class SourceTrue extends PowerSource {
	
	public SourceTrue() {this(0, 0);}

	public SourceTrue(int x, int y) {
		super(x, y);
		setImage("sourceTrue");
		setState(true);
	}
}
