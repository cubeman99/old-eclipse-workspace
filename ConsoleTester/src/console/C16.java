package console;
import java.awt.Color;


public enum C16 {
	WHITE	(255, 255, 255),	SILVER	(192, 192, 192),
	GRAY	(128, 128, 128),	BLACK	(  0,   0,   0),
	RED		(255,   0,   0),	YELLOW	(255, 255,   0),
	LIME	(  0, 255,   0),	AQUA	(  0, 255, 255),
	BLUE	(  0,   0, 255),	FUCHSIA	(255,   0, 255), 
	MAROON	(128,   0,   0),	OLIVE	(128, 128,   0),
	GREEN	(  0, 128,   0),	TEAL	(  0, 128, 128),
	NAVY	(  0,   0, 128),	PURPLE	(128,   0, 128);
	
	C16(int r, int g, int b) {
		this.color = new Color(r, g, b);
	}
	C16(C16 c) {
		this.color = c.getColor();
	}
	
	public Color getColor() {
		return color;
	}
	
	private Color color;
}