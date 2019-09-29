package zelda.game.control.map;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.game.world.Frame;
import zelda.game.world.Level;

public class MapFloor {
	private MapScreen mapScreen;
	private Point size;
	private MapRoom[][] rooms;
	private Level level;
	private int floorNumber;
	private int floorIndex;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public MapFloor(MapScreen mapScreen, Point size, Level level, int floorIndex) {
		this.mapScreen   = mapScreen;
		this.size        = new Point(size);
		this.level       = level;
		this.floorIndex  = floorIndex;
		this.rooms       = new MapRoom[size.x][size.y];
		this.floorNumber = level.getProperties().getInt("floor", 0);
		
		for (int x = 0; x < level.getSize().x; x++) {
    		for (int y = 0; y < level.getSize().y; y++) {
    			createRoom(x, y);
        	}
    	}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isDiscovered() {
		for (int x = 0; x < level.getSize().x; x++) {
    		for (int y = 0; y < level.getSize().y; y++) {
    			if (rooms[x][y] != null && rooms[x][y].isExplored())
    				return true;
        	}
    	}
		return false;
	}
	
	public boolean hasBossRoom() {
		for (int x = 0; x < level.getSize().x; x++) {
    		for (int y = 0; y < level.getSize().y; y++) {
    			if (rooms[x][y] != null && rooms[x][y].isBossRoom())
    				return true;
        	}
    	}
		return false;
	}
	
	public MapScreen getMapScreen() {
		return mapScreen;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	private void createRoom(int x, int y) {
		Frame frame  = level.getFrame(x, y);
		if (!MapRoom.isValidFrame(frame))
			return;
		
		MapRoom room = new MapRoom(this, frame);
		Point loc    = room.getLocation();
		
		if (loc.x >= 0 && loc.y >= 0 && loc.x < size.x && loc.y < size.y) {
			rooms[loc.x][loc.y] = room;
		}
	}
	
	public void draw(double floorDisplayAmount) {
		Draw.setViewPosition(0, 0);
		
		int baseY = 96 - (((8 - mapScreen.getNumFloors()) / 2) * 8);
		// Draw box.
		Draw.drawSprite(Resources.SPRITE_MAP_FLOOR_BOX, 32, baseY - (floorIndex * 8));
		if (mapScreen.getFloorIndex() == floorIndex)
			Draw.drawSprite(Resources.SPRITE_MAP_FLOOR_ARROW, 24, baseY - (floorIndex * 8));
		if (mapScreen.getPlayerFloorIndex() == floorIndex)
			Draw.drawSprite(Resources.SPRITE_MAP_PLAYER, 36, baseY - (floorIndex * 8));
		
		String str;
		int floorNumberPos = 8;
		if (floorNumber < 0) {
			str = "B" + (-floorNumber) + "F";
			floorNumberPos = 0;
		}
		else {
			str = (floorNumber + 1) + "F";
		}
		Draw.drawText(str, new Point(floorNumberPos, baseY - (floorIndex * 8)), Resources.FONT_SMALL, new Color(248, 248, 216));
		Draw.drawText(str, new Point(floorNumberPos, baseY - (floorIndex * 8) - 1), Resources.FONT_SMALL, new Color(56, 32, 16));
		
		if (hasBossRoom() && mapScreen.hasCompass()) {
			Draw.drawImage(Resources.SHEET_MENU_SMALL, 6, 0, 48, baseY - (floorIndex * 8));
		}
		
		Point cursorPos = null;
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.x; y++) {
				MapRoom room = rooms[x][y];
				Draw.setViewPosition(-80, -40 - ((floorDisplayAmount) * 80));

	    		Draw.drawImage(Resources.SHEET_MENU_SMALL, 2, 5, x * 8, y * 8);
				if (room != null) {
					room.drawRoomIcon();
					if (mapScreen.hasCompass() && (!room.isCurrentRoom()
						|| mapScreen.getCursorSprite().getFrameIndex() == 0))
					{
    					room.drawExtras();
					}
					if (room.isCurrentRoom())
						cursorPos = new Point(x * 8, y * 8);
				}
			}
		}
		
		if (cursorPos != null) {
			Draw.drawSprite(mapScreen.getCursorSprite(), cursorPos);
		}
	}
}
