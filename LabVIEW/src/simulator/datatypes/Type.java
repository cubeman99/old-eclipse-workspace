package simulator.datatypes;

import java.awt.Color;

public enum Type {
	BOOLEAN(new Color(0, 102, 0)),
	BYTE(new Color(0, 0, 0)),
	INTEGER(Color.BLUE),
	DOUBLE(new Color(255, 102, 0)),
	STRING(Color.MAGENTA);
	
	
	private Color color;
	
	Type(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}