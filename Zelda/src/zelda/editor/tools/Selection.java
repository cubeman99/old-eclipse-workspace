package zelda.editor.tools;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.util.Grid;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;


public class Selection {
	private Grid<GridTile> tileGrid;
	private ArrayList<ObjectTile> objectTiles;



	// ================== CONSTRUCTORS ================== //

	public Selection(Point size) {
		this.tileGrid = new Grid<GridTile>(size);
		this.objectTiles = new ArrayList<ObjectTile>();


	}



	// =================== ACCESSORS =================== //

	public Point getSize() {
		return tileGrid.getSize();
	}

	public GridTile getGridTile(int x, int y) {
		return tileGrid.get(x, y);
	}



	// ==================== MUTATORS ==================== //

	public GridTile setGridTile(int x, int y, GridTile t) {
		return tileGrid.set(x, y, t);
	}

}
