package tp.planner.tile;

import java.awt.Graphics;

public abstract class TilePacket {
	protected Tile tile;
	
	public TilePacket() {
		tile = null;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	public void draw(Graphics g, int partX, int partY) {}
	
	public abstract TilePacket getCopy(Tile t);
}
