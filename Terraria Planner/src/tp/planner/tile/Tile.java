package tp.planner.tile;

import tp.common.GMath;
import tp.common.Point;
import tp.common.Rectangle;
import tp.planner.ConnectionScheme;
import tp.planner.Grid;
import tp.planner.Item;

public class Tile {
	private Grid grid;
	private Point position;
	private Point size;
	private int subimage;
	private int randSubimage;
	private Item item;
	private boolean[] connections;
	private int connectionIndex;
	private TilePacket packet;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Tile(Grid grid, Point position, Item item) {
		this.grid            = grid;
		this.position        = new Point(position);
		this.size            = new Point(item.getSize());
		this.item            = item;
		this.connections     = new boolean[4];
		this.connectionIndex = 0;
		this.packet          = PacketData.createNewTilePacket(this);
		this.subimage        = 0;
		this.randSubimage    = 0;
		ConnectionScheme.setPlacementSubimage(this);
		
		clearConnections();
	}
	
	public Tile(Tile copy) {
		this.grid            = copy.grid;
		this.position        = new Point(copy.getPosition());
		this.size            = new Point(copy.getSize());
		this.item            = copy.getItem();
		this.connections     = new boolean[4];
		this.connectionIndex = copy.getConnectionIndex();
		this.packet          = null;
		if (copy.getPacket() != null)
			this.packet          = copy.getPacket().getCopy(this);
		this.subimage        = copy.getSubimage();
		this.randSubimage    = copy.getRandomSubimage();
		
		for (int i = 0; i < 4; i++)
			this.connections[i] = copy.getConnection(i);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Grid getGrid() {
		return grid;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
	
	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}

	public int getHeight() {
		return size.y;
	}
	
	public Rectangle getRect() {
		return new Rectangle(position, size);
	}
	
	public Item getItem() {
		return item;
	}
	
	public Item getBaseItem() {
		return item.getBaseItem();
	}
	
	public boolean getConnection(int index) {
		return connections[index];
	}
	
	public int getConnectionIndex() {
		return connectionIndex;
	}
	
	public int getSubimage() {
		return subimage;
	}
	
	public int getRandomSubimage() {
		return randSubimage;
	}
	
	public boolean isSolid() {
		return item.isSolid();
	}
	
	public TilePacket getPacket() {
		return packet;
	}
	
	public Tile getCopy() {
		return new Tile(this);
	}
	
	public Rectangle getAbsoluteRect() {
		return new Rectangle(position.plus(item.getExtendedOffset()), item.getExtendedSize());
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void removeSelfFromGrid() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				grid.delete(position.x + x, position.y + y);
			}
		}
		grid.refreshArea(position.x - 2, position.y - 2, size.x + 4, size.y + 4);
	}
	
	public boolean putSelfInGrid(boolean replaceMode) {
		if (!replaceMode && grid.isBlocked(item, position.x, position.y))
			return false;
		
		grid.putRaw(this);
		return true;
	}
	
	public void clearConnections() {
		for (int i = 0; i < 4; i++)
			connections[i] = false;
	}
	
	public void setConnection(int index, boolean isConnected) {
		connections[index] = isConnected;
	}
	
	public void setConnections(boolean[] connections) {
		for (int i = 0; i < 4; i++)
			this.connections[i] = connections[i];
	}
	
	public void setConnectionIndex(int connectionIndex) {
		this.connectionIndex = connectionIndex;
	}
	
	public void setSubimage(int subimage) {
		this.subimage = subimage;
	}
	
	public void setRandomSubimage(int randSubimage) {
		this.randSubimage = randSubimage;
	}
	
	public void setPacket(TilePacket packet) {
		this.packet = packet;
	}
	/*
	public void buffer() {
		grid.getCanvas().drawTile(this);
	}
	*/
	public void createPacket() {
		this.packet = PacketData.createNewTilePacket(this);;
	}
}
