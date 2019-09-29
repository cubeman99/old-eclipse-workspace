package simulator;

import java.awt.Graphics;
import java.awt.Image;

import main.*;

public class Wire extends GridObject {
	public boolean state        = false;
	public boolean[] directions = new boolean[4];
	public WireGroup wireGroup  = null;
	
	public Wire(int x, int y) {
		super(x, y);
		for (int i = 0; i < 4; i++)
			this.directions[i] = false;
	}
	
	public Wire(int x, int y, boolean[] directions) {
		super(x, y);
		for (int i = 0; i < 4; i++)
			this.directions[i] = directions[i];
	}
	
	public void setWireGroup(WireGroup wireGroup) {
		this.wireGroup = wireGroup;
	}
	
	public int getDx() {
		return (x * Grid.TILE_SIZE);
	}
	
	public int getDy() {
		return (y * Grid.TILE_SIZE);
	}
	
	public void draw(Graphics g) {
		if (wireGroup != null)
			state = wireGroup.getState();
		
		Image img = ImageLoader.getImage("wire");
		ImageDrawer.drawTile(g, img, getDx(), getDy(), 0, GMath.iBool(!state));
		
		for (int i = 0; i < 4; i++) {
			if (directions[i])
				ImageDrawer.drawTile(g, img, getDx(), getDy(), i + 1, GMath.iBool(!state));
		}
	}
}
