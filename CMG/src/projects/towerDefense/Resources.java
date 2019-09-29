package projects.towerDefense;

import cmg.graphics.SpriteSheet;

public class Resources {
	public static SpriteSheet SHEET_TILES;
	
	public static void initialize() {
		SHEET_TILES = new SpriteSheet("tiles.png", 48, 48, 1);
	}
}
