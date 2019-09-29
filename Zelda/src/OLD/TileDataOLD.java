package OLD;

import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;


public class TileDataOLD extends DataOLD {
	public CollisionModel collisionModel;
	public AnimationStripOLD animStrip;

	public boolean solid;
	public boolean ground;
	public boolean water;
	public boolean ocean;
	public boolean lava;
	public boolean waterFall;
	public boolean hole;
	public boolean stairs;
	public boolean ice;
	public boolean puddle;
	public boolean ledge;
	public int ledgeDir;

	// ================== CONSTRUCTORS ================== //

	public TileDataOLD() {
		this(0, 0);
	}

	public TileDataOLD(int sx, int sy) {
		super(sx, sy);

		this.collisionModel = null;
		this.animStrip = null;
		this.solid = false;
		this.ground = false;
		this.water = false;
		this.ocean = false;
		this.lava = false;
		this.waterFall = false;
		this.hole = false;
		this.stairs = false;
		this.ice = false;
		this.puddle = false;
		this.ledge = false;
		this.ledgeDir = Direction.SOUTH;
	}



	// =================== ACCESSORS =================== //

	public boolean isSolid() {
		return solid;
	}

	public boolean isAnimated() {
		return (animStrip != null);
	}

	public boolean isPit() {
		return (hole || water || lava);
	}



	// ==================== MUTATORS ==================== //

	public void setCollisionModel(CollisionModel collisionModel) {
		this.collisionModel = collisionModel;
		this.solid = true;
	}

	public void setCollisionModel(CollisionBox... boxes) {
		collisionModel = new CollisionModel(boxes);
		this.solid = true;
	}

	public void setCollisionModel(boolean solid, CollisionBox... boxes) {
		this.solid = solid;
		collisionModel = new CollisionModel(boxes);
	}

	public void setAnimation(int sx, int sy, int length, double speed) {
		this.animStrip = new AnimationStripOLD(null, sx, sy, length, speed);
	}

	public void setLedge(int ledgeDir) {
		this.ledge = true;
		this.ledgeDir = ledgeDir;
	}
}
