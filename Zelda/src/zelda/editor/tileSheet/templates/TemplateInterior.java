package zelda.editor.tileSheet.templates;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.common.util.Direction;
import zelda.editor.tileSheet.TilesetTemplate;


public class TemplateInterior extends TilesetTemplate {

	public TemplateInterior() {
		super(10, 15, "tileset", "Interior");
		defaultTile = new Point(4, 1);

		loadDataMap("tilesetInterior.txt");



		// ANIMATION =================================

		// Lantern
		setAnimation(7, 9, Animations.createStrip(16, 6, 12, 4));


		setAnimation(4, 4, Animations.createStrip(16, 0, 1, 4)); // water
		setAnimation(3, 4, Animations.createStrip(8, 0, 6, 4)); // waterfall mid
		setAnimation(3, 5, Animations.createStrip(8, 0, 7, 4)); // waterfall bot
		setAnimation(4, 3,
				Animations.createStrip(16, 4, 4, 3).addFrame(16, 5, 4, 0, 0)); // puddle



		// COLLISION DATA ============================

		setModelLedge(1, 0, MODEL_BLOCK, Direction.NORTH);
		setModelLedge(0, 1, MODEL_BLOCK, Direction.WEST);
		setModelLedge(2, 1, MODEL_BLOCK, Direction.EAST);
		setModelLedge(1, 2, MODEL_BLOCK, Direction.SOUTH);
		// setModel(6, 1, MODEL_EDGE_N); // Cave
		// setModel(7, 1, MODEL_EDGE_S); // Cave
		// setModel(8, 1, MODEL_EDGE_W); // Cave
		// setModel(9, 1, MODEL_EDGE_E); // Cave

		setModel(8, 2, MODEL_DOORWAY); // stairs down
		setModel(9, 2, MODEL_DOORWAY); // stairs up

		setModel(9, 3, MODEL_EDGE_N); // fireplace
		setModel(8, 4, MODEL_EDGE_N); // fireplace Post
		setModel(8, 5, MODEL_EDGE_W); // entrance left
		setModel(9, 5, MODEL_EDGE_E); // entrance right

		setModel(4, 8, MODEL_EDGE_N); // bookshelves
		setModel(5, 8, MODEL_EDGE_N); // drawers
		setModel(6, 8, MODEL_EDGE_N); // shelves

		// Railings:
		setModel(0, 10, MODEL_CORNER_NW);
		setModel(1, 10, MODEL_EDGE_N);
		setModel(2, 10, MODEL_CORNER_NE);
		setModel(0, 11, MODEL_EDGE_W);
		setModel(2, 11, MODEL_EDGE_E);
		setModel(0, 12, MODEL_CORNER_SW);
		setModel(1, 12, MODEL_EDGE_S);
		setModel(2, 12, MODEL_CORNER_SE);
		setModel(0, 13, MODEL_INSIDE_CORNER_NW);
		setModel(1, 13, MODEL_INSIDE_CORNER_NE);
		setModel(0, 14, MODEL_INSIDE_CORNER_SW);
		setModel(1, 14, MODEL_INSIDE_CORNER_SE);

		setModel(3, 10, MODEL_CORNER_NW);
		setModel(4, 10, MODEL_EDGE_N);
		setModel(5, 10, MODEL_CORNER_NE);
		setModel(3, 11, MODEL_EDGE_W);
		setModel(5, 11, MODEL_EDGE_E);
		setModel(3, 12, MODEL_CORNER_SW);
		setModel(4, 12, MODEL_EDGE_S);
		setModel(5, 12, MODEL_CORNER_SE);
		setModel(3, 13, MODEL_INSIDE_CORNER_NW);
		setModel(4, 13, MODEL_INSIDE_CORNER_NE);
		setModel(3, 14, MODEL_INSIDE_CORNER_SW);
		setModel(4, 14, MODEL_INSIDE_CORNER_SE);

		setModel(6, 10, MODEL_CORNER_NW);
		setModel(7, 10, MODEL_EDGE_N);
		setModel(8, 10, MODEL_CORNER_NE);
		setModel(6, 11, MODEL_EDGE_W);
		setModel(8, 11, MODEL_EDGE_E);
		setModel(6, 12, MODEL_CORNER_SW);
		setModel(7, 12, MODEL_EDGE_S);
		setModel(8, 12, MODEL_CORNER_SE);
		setModel(6, 13, MODEL_INSIDE_CORNER_NW);
		setModel(7, 13, MODEL_INSIDE_CORNER_NE);
		setModel(6, 14, MODEL_INSIDE_CORNER_SW);
		setModel(7, 14, MODEL_INSIDE_CORNER_SE);
	}
}
