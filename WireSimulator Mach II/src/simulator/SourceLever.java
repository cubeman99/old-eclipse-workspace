package simulator;

import java.awt.Color;
import java.awt.Graphics;

public class SourceLever extends PowerSource {
	
	public SourceLever() {this(0, 0);}
	
	public SourceLever(int x, int y) {
		super(x, y);
		setState(false);
		setImage("sourceLever");
	}
	
	public void onPressed() {
		setState(!getState());
	}
	
	public void draw(Graphics g) {
		int ts = Grid.TILE_SIZE;
		int sx = 0;
		if (getState())
			sx += ts;
		g.drawImage(image, x * ts, y * ts, (x + 1) * ts, (y + 1) * ts, sx, 0, sx + ts, ts, null);
	}
}
