package zelda.editor.tileSheet.templates;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.TileAnimations;
import zelda.common.util.Direction;
import zelda.editor.tileSheet.TilesetTemplate;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;


public class TemplateOverworld extends TilesetTemplate {

	public TemplateOverworld() {
		super(21, 36, "tileset", "Summer", "Forest", "Graveyard");
		defaultTile = new Point(1, 25);
		loadDataMap("sheet2.txt");


		setAnimation(3, 23, TileAnimations.FLOWERS); // flower

		setAnimation(3, 17, Animations.createStrip(8, 0, 0, 4)); // whirlpool
		setProperty(3, 17, "water", true);
		setAnimation(1, 15, TileAnimations.WATER);               // water
		setAnimation(2, 15, TileAnimations.WATER_DEEP);          // deep water
		setAnimation(1, 14, TileAnimations.OCEAN);               // ocean
		setAnimation(2, 14, TileAnimations.OCEAN_SHORE);         // ocean shore
		setAnimation(0, 14, Animations.createStrip(4, 0, 5, 8)); // waterfall top
		setAnimation(0, 15, TileAnimations.WATERFALL);           // waterfall mid
		setAnimation(0, 16, TileAnimations.WATERFALL_BOTTOM);    // waterfall bot
		setModelLedge(0, 15, MODEL_BLOCK, Direction.SOUTH);      // waterfall mid
		setModelLedge(0, 16, MODEL_BLOCK, Direction.SOUTH);      // waterfall bot

		setAnimation(1, 16,
				Animations.createStrip(16, 4, 4, 3).addFrame(16, 5, 4, 0, 0)); // puddle

		setAnimation(7, 15, Animations.createStrip(8, 12, 0, 4)); // quicksand1
		setAnimation(8, 15, Animations.createStrip(8, 12, 1, 4)); // quicksand2
		setAnimation(8, 14, Animations.createStrip(8, 12, 2, 4)); // quicksand3
		setAnimation(7, 14, Animations.createStrip(8, 12, 3, 4)); // quicksand4

		setAnimation(6, 14, Animations.createStrip(6, 4, 0, 4)); // currentsE
		setAnimation(5, 15, Animations.createStrip(6, 4, 1, 4)); // currentsN
		setAnimation(5, 14, Animations.createStrip(6, 4, 2, 4)); // currentsW
		setAnimation(6, 15, Animations.createStrip(6, 4, 3, 4)); // currentsS


		Animation f1 = Animations.createStrip(1, 8, 0, 4).scale(12);
		Animation f2 = Animations.createStrip(1, 8, 1, 4).scale(12);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 12; j++) {
				f1.getFrame((i * 12) + j).addPart(j >= 6 ? 10 : 8, 4, 0, 0);
				f2.getFrame((i * 12) + j).addPart(j >= 6 ? 11 : 9, 4, 0, 0);
			}
		}
		setAnimation(17, 10, f1); // fountain1
		setAnimation(18, 10, f2); // fountain2
		setAnimation(17, 11, Animations.createStrip(12, 8, 2, 4)); // fountain3
		setAnimation(18, 11, Animations.createStrip(12, 8, 3, 4)); // fountain4


		// Terrain.
		setModelLedge(1, 3, MODEL_BLOCK, Direction.SOUTH);

		setModelLedge(1, 17, MODEL_EDGE_N, Direction.NORTH);
		setModelLedge(0, 18, MODEL_EDGE_W, Direction.WEST);
		setModelLedge(2, 18, MODEL_EDGE_E, Direction.EAST);
		setModelLedge(1, 19, MODEL_EDGE_S, Direction.SOUTH);
		setModelLedge(5, 17, MODEL_EDGE_N, Direction.NORTH);
		setModelLedge(4, 18, MODEL_EDGE_W, Direction.WEST);
		setModelLedge(6, 18, MODEL_EDGE_E, Direction.EAST);
		setModelLedge(5, 19, MODEL_EDGE_S, Direction.SOUTH);


		// Doorways/Entrances.
		setModel(1, 4, MODEL_EDGE_N); // cave entrences
		setModel(0, 5, MODEL_EDGE_N);
		setModel(1, 5, MODEL_EDGE_N);
		setModel(12, 26, MODEL_EDGE_N); // tree entrences
		setModel(12, 28, MODEL_EDGE_N);
		setModel(17, 22, MODEL_EDGE_N);

		setModel(13, 7, MODEL_DOORWAY); // doorways
		setModel(13, 8, MODEL_DOORWAY);
		setModel(13, 9, MODEL_DOORWAY);
		setModel(19, 9, MODEL_CORNER_NW);
		setModel(20, 9, MODEL_CORNER_NE);
		setModel(15, 17, MODEL_DOORWAY);
		setModel(15, 18, MODEL_DOORWAY);
		setModel(15, 19, MODEL_DOORWAY);
		setModel(12, 18, MODEL_CORNER_NW);
		setModel(13, 18, MODEL_CORNER_NE);

		// Bridges.
		setModel(12, 19, new CollisionModel(new CollisionBox(0, 0, 4, 16),
				new CollisionBox(0, 0, 16, 8)));
		setModel(13, 19, new CollisionModel(new CollisionBox(12, 0, 4, 16),
				new CollisionBox(0, 0, 16, 8)));


		// Bridges.
		// setModel(5, 33, MODEL_BRIDGE_H_TOP);
		// setModel(5, 34, MODEL_BRIDGE_H_BOTTOM);
		// setModel(6, 33, MODEL_BRIDGE_H);
		// setModel(6, 34, MODEL_BRIDGE_V_LEFT);
		// setModel(7, 34, MODEL_BRIDGE_V_RIGHT);
		// setModel(7, 33, MODEL_BRIDGE_V);


		// Waterfalls.
		// setModelLedge(0, 25, MODEL_BLOCK, Direction.SOUTH);
		// setModelLedge(0, 26, MODEL_BLOCK, Direction.SOUTH);
	}
}
