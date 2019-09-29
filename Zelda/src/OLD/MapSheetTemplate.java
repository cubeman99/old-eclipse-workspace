package OLD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import zelda.common.geometry.Point;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;
import zelda.game.world.Frame;


public class MapSheetTemplate extends TileSheetTemplate {
	protected static final CollisionModel MODEL_BLOCK = new CollisionModel(
			new CollisionBox(0, 0, 16, 16));


	protected static final CollisionModel MODEL_EDGE_E = new CollisionModel(
			new CollisionBox(8, 0, 8, 16));
	protected static final CollisionModel MODEL_EDGE_N = new CollisionModel(
			new CollisionBox(0, 0, 16, 8));
	protected static final CollisionModel MODEL_EDGE_W = new CollisionModel(
			new CollisionBox(0, 0, 8, 16));
	protected static final CollisionModel MODEL_EDGE_S = new CollisionModel(
			new CollisionBox(0, 8, 16, 8));

	protected static final CollisionModel MODEL_CORNER_NE = new CollisionModel(
			new CollisionBox(0, 0, 16, 8), new CollisionBox(8, 8, 8, 8));
	protected static final CollisionModel MODEL_CORNER_NW = new CollisionModel(
			new CollisionBox(0, 0, 16, 8), new CollisionBox(0, 8, 8, 8));
	protected static final CollisionModel MODEL_CORNER_SW = new CollisionModel(
			new CollisionBox(0, 8, 16, 8), new CollisionBox(0, 0, 8, 8));
	protected static final CollisionModel MODEL_CORNER_SE = new CollisionModel(
			new CollisionBox(0, 8, 16, 8), new CollisionBox(8, 0, 8, 8));

	protected static final CollisionModel MODEL_INSIDE_CORNER_NE = new CollisionModel(
			new CollisionBox(8, 0, 8, 8));

	protected static final CollisionModel MODEL_INSIDE_CORNER_NW = new CollisionModel(
			new CollisionBox(0, 0, 8, 8));

	protected static final CollisionModel MODEL_INSIDE_CORNER_SW = new CollisionModel(
			new CollisionBox(0, 8, 8, 8));

	protected static final CollisionModel MODEL_INSIDE_CORNER_SE = new CollisionModel(
			new CollisionBox(8, 8, 8, 8));


	protected static final CollisionModel MODEL_BRIDGE_H_TOP = new CollisionModel(
			new CollisionBox(0, 0, 16, 4));
	protected static final CollisionModel MODEL_BRIDGE_H_BOTTOM = new CollisionModel(
			new CollisionBox(0, 13, 16, 3));
	protected static final CollisionModel MODEL_BRIDGE_H = new CollisionModel(
			new CollisionBox(0, 0, 16, 4), new CollisionBox(0, 13, 16, 3));

	protected static final CollisionModel MODEL_BRIDGE_V_LEFT = new CollisionModel(
			new CollisionBox(0, 0, 4, 16));
	protected static final CollisionModel MODEL_BRIDGE_V_RIGHT = new CollisionModel(
			new CollisionBox(12, 0, 4, 16));
	protected static final CollisionModel MODEL_BRIDGE_V = new CollisionModel(
			new CollisionBox(0, 0, 4, 16), new CollisionBox(12, 0, 4, 16));

	protected static final CollisionModel MODEL_NORMAL = new CollisionModel(
			new CollisionBox(4, 1, 9, 8));



	// ================== CONSTRUCTORS ================== //

	public MapSheetTemplate(String sheetName) {
		super(sheetName);
		tileType = 0;

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				data[x][y] = new TileDataOLD(x, y);
			}
		}
		TileDataOLD td;

		loadData();

		createAnimationData(1, 20, 1, 11, 4, 0.07); // Flower
		// createAnimationData(2, 26, 5, 0, 4, 0.07); // Water
		// createAnimationData(2, 27, 5, 1, 4, 0.07); // Puddle
		// createAnimationData(2, 25, 5, 15, 4, 0.07); // Ocean Water

		// Terrain.
		createLedgeData(1, 3, Direction.SOUTH);
		createLedgeData(1, 4, Direction.SOUTH);

		createCollisionData(4, 0, MODEL_CORNER_NW);
		createCollisionLedgeData(5, 0, MODEL_EDGE_N, Direction.NORTH);
		createCollisionData(6, 0, MODEL_CORNER_NE);
		createCollisionLedgeData(4, 1, MODEL_EDGE_W, Direction.WEST);
		createCollisionLedgeData(6, 1, MODEL_EDGE_E, Direction.EAST);
		createCollisionData(4, 2, MODEL_CORNER_SW);
		createCollisionLedgeData(5, 2, MODEL_EDGE_S, Direction.SOUTH);
		createCollisionData(6, 2, MODEL_CORNER_SE);

		createCollisionData(4, 3, MODEL_INSIDE_CORNER_SE);
		createCollisionData(5, 3, MODEL_INSIDE_CORNER_SW);
		createCollisionData(4, 4, MODEL_INSIDE_CORNER_NE);
		createCollisionData(5, 4, MODEL_INSIDE_CORNER_NW);

		createCollisionData(6, 3, MODEL_EDGE_W);
		createCollisionData(7, 3, MODEL_EDGE_E);
		createCollisionData(6, 4, MODEL_CORNER_SW);
		createCollisionData(7, 4, MODEL_CORNER_SE);

		createCollisionData(7, 0, MODEL_CORNER_NW);
		createCollisionLedgeData(8, 0, MODEL_EDGE_N, Direction.NORTH);
		createCollisionData(9, 0, MODEL_CORNER_NE);
		createCollisionLedgeData(7, 1, MODEL_EDGE_W, Direction.WEST);
		createCollisionLedgeData(9, 1, MODEL_EDGE_E, Direction.EAST);
		createCollisionData(7, 2, MODEL_CORNER_SW);
		createCollisionLedgeData(8, 2, MODEL_EDGE_S, Direction.SOUTH);
		createCollisionData(9, 2, MODEL_CORNER_SE);

		createCollisionData(8, 3, MODEL_INSIDE_CORNER_SE);
		createCollisionData(9, 3, MODEL_INSIDE_CORNER_SW);
		createCollisionData(8, 4, MODEL_INSIDE_CORNER_NE);
		createCollisionData(9, 4, MODEL_INSIDE_CORNER_NW);

		createCollisionData(8, 5, MODEL_EDGE_W);
		createCollisionData(9, 5, MODEL_EDGE_E);
		createCollisionData(8, 6, MODEL_CORNER_SW);
		createCollisionData(9, 6, MODEL_CORNER_SE);

		createCollisionLedgeData(8, 7, MODEL_EDGE_S, Direction.SOUTH);


		// Doorways/Entrances.
		createCollisionData(10, 7, MODEL_EDGE_N);
		createCollisionData(11, 7, MODEL_EDGE_N);
		createCollisionData(11, 8, MODEL_EDGE_N);
		createCollisionData(6, 25, MODEL_EDGE_N);
		createCollisionData(3, 33, MODEL_EDGE_N);


		// Bridges.
		createCollisionData(5, 33, MODEL_BRIDGE_H_TOP);
		createCollisionData(5, 34, MODEL_BRIDGE_H_BOTTOM);
		createCollisionData(6, 33, MODEL_BRIDGE_H);
		createCollisionData(6, 34, MODEL_BRIDGE_V_LEFT);
		createCollisionData(7, 34, MODEL_BRIDGE_V_RIGHT);
		createCollisionData(7, 33, MODEL_BRIDGE_V);


		// Waterfalls.
		td = createCollisionLedgeData(0, 25, MODEL_BLOCK, Direction.SOUTH);
		td.setAnimation(5, 13, 4, 0.07);
		td = createCollisionLedgeData(0, 26, MODEL_BLOCK, Direction.SOUTH);
		td.setAnimation(5, 14, 4, 0.07);
	}



	// =================== ACCESSORS =================== //



	// ==================== MUTATORS ==================== //


	private TileDataOLD createData(int sx, int sy) {
		TileDataOLD td = new TileDataOLD(sx, sy);
		data[sx][sy] = td;
		return td;
	}

	private TileDataOLD createCollisionData(int sx, int sy, CollisionModel model) {
		TileDataOLD td = createData(sx, sy);
		td.setCollisionModel(model);
		return td;
	}

	private TileDataOLD createCollisionLedgeData(int sx, int sy,
			CollisionModel model, int ledgeDirection) {
		TileDataOLD td = createCollisionData(sx, sy, model);
		td.setLedge(ledgeDirection);
		return td;
	}

	private TileDataOLD createLedgeData(int sx, int sy, int ledgeDirection) {
		TileDataOLD td = createCollisionData(sx, sy, MODEL_BLOCK);
		td.setLedge(ledgeDirection);
		return td;
	}

	private TileDataOLD createAnimationData(int sx, int sy, int animSX,
			int animSY, int animLength, double animSpeed) {
		TileDataOLD td = createData(sx, sy);
		td.setAnimation(animSX, animSY, animLength, animSpeed);
		return td;
	}


	private void loadData() {
		try {
			Scanner reader = new Scanner(new File("sheet1.txt"));

			for (int y = 0; y < size.y && reader.hasNextLine(); y++) {
				String line = reader.nextLine();

				for (int x = 0; x < size.x && x < line.length(); x++) {
					String c = line.substring(x, x + 1);

					if (!c.equals(" ")) {
						TileDataOLD td = createData(x, y);

						// S = Solid
						if (c.equals("S")) {
							td.setCollisionModel(MODEL_BLOCK);
						}

						// L = Ledge
						else if (c.equals("L")) {
							td.setCollisionModel(MODEL_BLOCK);
							td.ledge = true;
						}

						// G = Digable Ground
						else if (c.equals("G")) {
							td.ground = true;
						}

						// H = hole
						else if (c.equals("H")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.hole = true;
						}

						// W = Water
						else if (c.equals("W")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.water = true;
							td.setAnimation(5, 0, 4, 0.07);
						}

						// O = Ocean
						else if (c.equals("O")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.water = true;
							td.ocean = true;
							td.setAnimation(5, 15, 4, 0.07);
						}

						// F = Water Fall
						else if (c.equals("F")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.waterFall = true;
						}

						// I = Ice
						else if (c.equals("I")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.ice = true;
						}

						// R = Stairs
						else if (c.equals("R")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.stairs = true;
						}

						// P = Puddle
						else if (c.equals("P")) {
							td.setCollisionModel(MODEL_NORMAL);
							td.solid = false;
							td.puddle = true;
							td.setAnimation(5, 1, 4, 0.07);
						}
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// =============== INHERITED METHODS =============== //

	@Override
	public AbstractTileOLD createTile(Frame frame, Point pos, Point sourcePos) {
		// TileOLD t = new TileOLD(frame, pos.x, pos.y, new ZoneSheet(frame,
		// ZoneSheet.TILE_SHEET), sourcePos.x, sourcePos.y);
		// t.setData((TileDataOLD) data[sourcePos.x][sourcePos.y]);
		return null;
	}

	@Override
	public AbstractTileOLD createDefaultTile(Frame frame, Point pos) {
		return createTile(frame, pos, new Point(8, 1));
	}
}
