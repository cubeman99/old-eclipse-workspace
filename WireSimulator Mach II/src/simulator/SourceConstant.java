package simulator;

import java.awt.Color;
import java.awt.Graphics;

public class SourceConstant extends PowerSource {
	
	public SourceConstant() {this(0, 0);}

	public SourceConstant(int x, int y) {
		super(x, y);
		setImage("sourceTrue");
		setState(true);
	}
}
