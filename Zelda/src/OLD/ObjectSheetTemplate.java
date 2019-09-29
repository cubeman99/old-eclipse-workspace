package OLD;

import zelda.common.geometry.Point;
import zelda.game.entity.object.dungeon.ObjectPlant;
import zelda.game.entity.object.dungeon.ObjectPot;
import zelda.game.entity.object.global.ObjectBush;
import zelda.game.entity.object.global.ObjectCrystal;
import zelda.game.entity.object.global.ObjectDiamondRock;
import zelda.game.entity.object.global.ObjectRock;
import zelda.game.entity.object.overworld.ObjectBurnableTree;
import zelda.game.entity.object.overworld.ObjectDirtPile;
import zelda.game.entity.object.overworld.ObjectGrass;
import zelda.game.world.Frame;


public class ObjectSheetTemplate extends TileSheetTemplate {


	public ObjectSheetTemplate(String sheetName) {
		super(sheetName);
		tileType = 1;


		setData(new ObjectDataOLD(0, 0, new ObjectBush()));
		setData(new ObjectDataOLD(1, 0, new ObjectCrystal()));
		setData(new ObjectDataOLD(2, 0, new ObjectRock()));
		setData(new ObjectDataOLD(3, 0, new ObjectDiamondRock()));
		setData(new ObjectDataOLD(4, 0, new ObjectBurnableTree()));

		setData(new ObjectDataOLD(0, 1, new ObjectPlant()));
		setData(new ObjectDataOLD(1, 1, new ObjectPot()));
		setData(new ObjectDataOLD(2, 1, new ObjectDirtPile()));
		// Sign //
		setData(new ObjectDataOLD(4, 1, new ObjectGrass()));

		// Chest //
		// Movable Block //
		// Bombable Block //
		// Lever //
		// Switch //
	}



	// =============== INHERITED METHODS =============== //

	@Override
	public AbstractTileOLD createTile(Frame frame, Point pos, Point sourcePos) {
		ObjectDataOLD dt = (ObjectDataOLD) data[sourcePos.x][sourcePos.y];

		if (dt != null) {
			TileObjectOLD tile = new TileObjectOLD(frame, pos.x, pos.y, sheet,
					sourcePos.x, sourcePos.y);
			tile.setData(dt);

			return tile;
		}
		return null;
	}

	@Override
	public AbstractTileOLD createDefaultTile(Frame frame, Point pos) {
		return null;
	}
}
