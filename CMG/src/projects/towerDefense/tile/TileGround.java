package projects.towerDefense.tile;

import projects.towerDefense.Resources;
import cmg.graphics.Draw;

public class TileGround extends Tile {

	// ================== CONSTRUCTORS ================== //
	
	public TileGround() {
		super();
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void draw() {
		super.draw();
		Draw.drawImage(Resources.SHEET_TILES, 4, 0, location.x * SIZE, location.y * SIZE);
	}
}
