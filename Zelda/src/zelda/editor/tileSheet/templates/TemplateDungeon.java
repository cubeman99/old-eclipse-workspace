package zelda.editor.tileSheet.templates;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.common.graphics.TileAnimations;
import zelda.common.util.Direction;
import zelda.editor.tileSheet.TilesetTemplate;


public class TemplateDungeon extends TilesetTemplate {

	public TemplateDungeon() {
		super(15, 13, "tileset", "Dungeon1", "Dungeon4");
		defaultTile = new Point(4, 1);
		loadDataMap("tilesetDungeon.txt");



		// ANIMATION =================================

		// Lantern:
		setAnimation(6, 7, Animations.createStrip(16, 1, 8, 4));

		// Wall Torches:
		setAnimation(11, 3, Animations.createReversableStrip(16, 5, 17, 4));
		setAnimation(11, 4, Animations.createReversableStrip(16, 5, 15, 4));
		setAnimation(11, 5, Animations.createReversableStrip(16, 5, 16, 4));
		setAnimation(11, 6, Animations.createReversableStrip(16, 5, 18, 4));
		

		// SPECIAL ============================
		
		setProperty(6, 5, "coverable", false);
		setProperty(7, 5, "coverable", false);
		setProperty(8, 5, "coverable", false);
		setProperty(9, 5, "coverable", false);
		setProperty(6, 6, "coverable", false);
		setProperty(7, 6, "coverable", false);
		setProperty(8, 6, "coverable", false);
		setProperty(9, 6, "coverable", false);
		setProperty(10, 3, "coverable", false);
		setProperty(10, 4, "coverable", false);
		setProperty(10, 5, "coverable", false);
		setProperty(10, 6, "coverable", false);
		
		
		// COLLISION DATA ============================
		
		setModelLedge(1, 0, MODEL_BLOCK, Direction.NORTH);
		setModelLedge(0, 1, MODEL_BLOCK, Direction.WEST);
		setModelLedge(2, 1, MODEL_BLOCK, Direction.EAST);
		setModelLedge(1, 2, MODEL_BLOCK, Direction.SOUTH);

		// Railings:
		setModel(6, 0, MODEL_CORNER_NW);
		setModel(7, 0, MODEL_EDGE_N);
		setModel(8, 0, MODEL_CORNER_NE);
		setModel(6, 1, MODEL_EDGE_W);
		setModel(8, 1, MODEL_EDGE_E);
		setModel(6, 2, MODEL_CORNER_SW);
		setModel(7, 2, MODEL_EDGE_S);
		setModel(8, 2, MODEL_CORNER_SE);
		setModel(6, 3, MODEL_INSIDE_CORNER_SE);
		setModel(7, 3, MODEL_INSIDE_CORNER_SW);
		setModel(6, 4, MODEL_INSIDE_CORNER_NE);
		setModel(7, 4, MODEL_INSIDE_CORNER_NW);

		// Railings:
		setModel(12, 0, MODEL_CORNER_NW);
		setModel(13, 0, MODEL_EDGE_N);
		setModel(14, 0, MODEL_CORNER_NE);
		setModel(12, 1, MODEL_EDGE_W);
		setModel(14, 1, MODEL_EDGE_E);
		setModel(12, 2, MODEL_CORNER_SW);
		setModel(13, 2, MODEL_EDGE_S);
		setModel(14, 2, MODEL_CORNER_SE);

		// Entrance:
		setModel(9, 1, MODEL_EDGE_S);
		setModel(11, 1, MODEL_EDGE_S);
		
		// Tracks:
		setProperty(6, 5, "track_type", 0);
		setProperty(7, 6, "track_type", 1);
		setProperty(7, 5, "track_type", 2);
		setProperty(8, 6, "track_type", 3);
		setProperty(9, 6, "track_type", 4);
		setProperty(9, 5, "track_type", 5);
		setProperty(8, 5, "track_type", 6);
	}
}
