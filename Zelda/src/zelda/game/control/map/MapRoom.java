package zelda.game.control.map;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.properties.Properties;
import zelda.common.util.Direction;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;

public class MapRoom {
	private MapFloor floor;
	private Frame frame;
	private Point location;
	private int connectIndex;
	private boolean explored;
	private boolean treasure;
	private boolean boss;
	
	

	// ================== CONSTRUCTORS ================== //

	public MapRoom(MapFloor floor, Frame frame) {
		this.floor = floor;
		this.frame = frame;

		Properties p = frame.getProperties();
		location = new Point(
				p.getInt("map_loc_x", frame.getLocation().x),
				p.getInt("map_loc_y", frame.getLocation().y));
		explored = p.getBoolean("explored",  false);
		treasure = frame.hasTreasure();
		boss     = p.getBoolean("boss_room", false);
		
		boolean[] connections = new boolean[4];
    	for (int x = 0; x < frame.getWidth(); x++) {
    		if (isTile(x, 0))
    			connections[Direction.NORTH] = true;
    		if (isTile(x, frame.getHeight() - 1))
    			connections[Direction.SOUTH] = true;
    	}
    	for (int y = 0; y < frame.getHeight(); y++) {
    		if (isTile(0, y))
    			connections[Direction.WEST] = true;
    		if (isTile(frame.getWidth() - 1, y))
    			connections[Direction.EAST] = true;
    	}
    	
    	connectIndex = 0;
    	int num   = 1;
    	for (int i = 0; i < 4; i++) {
    		if (connections[i])
    			connectIndex |= num;
			num *= 2;
    	}
	}
	
	

	// =================== ACCESSORS =================== //
	
	public boolean isExplored() {
		return explored;
	}
	
	public boolean hasTreasure() {
		return treasure;
	}
	
	public boolean isBossRoom() {
		return boss;
	}
	
	public boolean isCurrentRoom() {
		return (floor.getMapScreen().getGame().getFrame() == frame);
	}
	
	public Point getLocation() {
		return location;
	}
	
	private boolean isTile(int x, int y) {
		GridTile t = frame.getGridTile(x, y);
		return (t != null && !t.isSolid());
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public void drawRoomIcon() {
		Point p = location.scaledBy(8);
		if (explored) {
        	int cx = connectIndex % 4;
        	int cy = connectIndex / 4;
    		Draw.drawImage(Resources.SHEET_MENU_SMALL, 7 + cx, cy, p.x, p.y);
    	}
    	else if (floor.getMapScreen().hasMap()) {
    		Draw.drawImage(Resources.SHEET_MENU_SMALL, 9, 4, p.x, p.y);
    	}
	}
	
	public void drawPlayer() {
		Point p = location.scaledBy(8);
		Draw.drawImage(Resources.SHEET_MENU_SMALL, 5, 0, p.x, p.y);
	}
	
	public void drawExtras() {
		Point p = location.scaledBy(8);
		if (boss)
			Draw.drawImage(Resources.SHEET_MENU_SMALL, 6, 1, p.x, p.y);
		else if (treasure)
			Draw.drawImage(Resources.SHEET_MENU_SMALL, 5, 1, p.x, p.y);
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static boolean isValidFrame(Frame frame) {
		for (int x = 0; x < frame.getWidth(); x++) {
    		for (int y = 0; y < frame.getHeight(); y++) {
    			GridTile t = frame.getGridTile(x, y);
    			if (t != null && !t.getSourcePosition().equals(1, 1))
    				return true;
        	}
    	}
		return false;
	}
}
