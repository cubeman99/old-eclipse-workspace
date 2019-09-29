package zelda.common.collision;

import java.util.ArrayList;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;
import zelda.game.entity.Entity;
import zelda.game.entity.EntityObject;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;


/**
 * Collision.
 * 
 * @author David Jordan
 */
public class Collision {
	private static GameInstance game;

	public static void setControl(GameInstance game) {
		Collision.game = game;
	}



	// =============== COLLISION WITH ENTITES ================ //

	public static boolean placeMeeting(Collidable c, Class<? extends Entity> cls) {
		return (getInstanceMeeting(c, cls) != null);
	}

	public static boolean placeMeeting(Collidable c, Vector position,
			Class<? extends Entity> cls) {
		return (getInstanceMeeting(c, position, cls) != null);
	}

	public static boolean placeMeetingSolid(Collidable c) {
		return placeMeetingSolid(c, c.getPosition());
	}

	public static boolean placeMeetingSolid(Collidable c, Vector position) {
		if (c.getCollisionBox() == null)
			return false;

		Frame frame = game.getFrame();

		// Check Tiles.
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t != null && t.getProperties().getBoolean("solid", false)) {
					CollisionModel model = t.getCollisionModel();
					if (isTouching(c, position, model, t.getPosition()))
						return true;
				}
			}
		}

		// Check Frame-Objects.
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e instanceof Collidable) {
				Collidable obj = (Collidable) e;

				if (obj != c && obj.isSolid() && isTouching(c, position, obj)) {
					return true;
				}
			}
		}

		return false;
	}

	public static Entity getInstanceMeeting(Collidable c,
			Class<? extends Entity> cls) {
		return getInstanceMeeting(c, c.getPosition(), cls);
	}

	public static Entity getInstanceMeeting(Collidable c, Vector position,
			Class<? extends Entity> cls) {
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e != c && e instanceof Collidable
					&& cls.isAssignableFrom(e.getClass())
					&& isTouching(c, position, (Collidable) e))
			{
				return e;
			}
		}
		return null;
	}

	public static Entity getInstanceMeeting(Vector p, Object collider,
			Class<? extends Entity> cls)
	{
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e != collider && e instanceof Collidable
					&& cls.isAssignableFrom(e.getClass())
					&& isTouching(p, (Collidable) e))
			{
				return e;
			}
		}
		return null;
	}

	public static ArrayList<Entity> getInstancesMeeting(Collidable c,
			Class<? extends Entity> cls)
	{
		return getInstancesMeeting(c, c.getPosition(), cls);
	}

	public static ArrayList<Entity> getInstancesMeeting(Collidable c,
			Vector position, Class<? extends Entity> cls)
	{
		ArrayList<Entity> instances = new ArrayList<Entity>();

		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e != c && e instanceof Collidable
					&& cls.isAssignableFrom(e.getClass())
					&& isTouching(c, position, (Collidable) e)) {
				instances.add(e);
			}
		}
		return instances;
	}

	public static ArrayList<Entity> getInstancesMeeting(Vector p,
			Object collider, Class<? extends Entity> cls)
	{
		ArrayList<Entity> instances = new ArrayList<Entity>();
		
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e != collider && e instanceof Collidable
					&& cls.isAssignableFrom(e.getClass())
					&& isTouching(p, (Collidable) e)) {
				instances.add(e);
			}
		}
		return instances;
	}



	// ================ COLLISION WITH TILES ================= //

	public static GridTile getTileMeeting(Collidable c) {
		return getTileMeeting(c, c.getPosition(), null);
	}

	public static GridTile getTileMeeting(Collidable c, Vector position) {
		return getTileMeeting(c, position, null);
	}

	public static GridTile getTileMeeting(Collidable c,
			CollisionBox tileCollisionBox) {
		return getTileMeeting(c, c.getPosition(), tileCollisionBox);
	}

	public static GridTile getTile(Vector point) {
		Frame frame = game.getFrame();
		Point loc   = new Point(point).scaledByInv(Settings.TS);
		if (frame.contains(loc))
			return frame.getGridTile(loc);
		return null;
	}

	public static GridTile getTileMeeting(Collidable c, Vector position,
			CollisionBox tileCollisionBox) {
		if (c.getCollisionBox() == null)
			return null;

		Frame frame = game.getFrame();
		Vectangle v = new Vectangle(c);
		int startX = Math.max(0, (int) (v.getX1() / Settings.TS));
		int startY = Math.max(0, (int) (v.getY1() / Settings.TS));

		for (int x = startX; x < frame.getWidth(); x++) {
			for (int y = startY; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t != null) {
					Vector tilePos = new Vector(x, y).scale(Settings.TS);

					if (tileCollisionBox != null) {
						if (isTouching(c, position, tileCollisionBox, tilePos))
							return t;
					}
					else {
						if (isTouching(c, position, t.getCollisionModel(),
								tilePos))
							return t;
					}
				}
			}
		}
		return null;
	}

	public static ArrayList<GridTile> getTilesMeeting(Collidable c) {
		return getTilesMeeting(c, c.getPosition(), null);
	}

	public static ArrayList<GridTile> getTilesMeeting(Collidable c,
			Vector position) {
		return getTilesMeeting(c, position, null);
	}

	public static ArrayList<GridTile> getTilesMeeting(Collidable c,
			CollisionBox tileCollisionBox) {
		return getTilesMeeting(c, c.getPosition(), tileCollisionBox);
	}

	public static ArrayList<GridTile> getTilesMeeting(Collidable c,
			Vector position, CollisionBox tileCollisionBox) {
		ArrayList<GridTile> tiles = new ArrayList<GridTile>();

		if (c.getCollisionBox() == null)
			return tiles;

		Frame frame = game.getFrame();
		Vectangle v = new Vectangle(c, position);
		int startX = Math.max(0, (int) (v.getX1() / Settings.TS));
		int startY = Math.max(0, (int) (v.getY1() / Settings.TS));

		for (int x = startX; x < frame.getWidth(); x++) {
			for (int y = startY; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t != null) {
					Vector tilePos = new Vector(x, y).scale(Settings.TS);

					if (tileCollisionBox != null) {
						if (isTouching(c, position, tileCollisionBox, tilePos))
							tiles.add(t);
					}
					else {
						if (isTouching(c, position, t.getCollisionModel(),
								tilePos))
							tiles.add(t);
					}
				}
			}
		}

		return tiles;
	}



	// ============ COLLISION BETWEEN TWO OBJECTS ============ //

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c1, Collidable c2) {
		return isTouching(c1, c1.getPosition(), c2, c2.getPosition());
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c1, Vector pos1, Collidable c2) {
		return isTouching(c1, pos1, c2, c2.getPosition());
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c1, Collidable c2, Vector pos2) {
		return isTouching(c1, c1.getPosition(), c2, pos2);
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c1, Vector pos1, Collidable c2,
			Vector pos2) {
		if (c1.getCollisionBox() == null || c2.getCollisionBox() == null)
			return false;
		return new Vectangle(c1, pos1).touches(new Vectangle(c2, pos2));
	}

	/** Return whether a point is touching a collidable. **/
	public static boolean isTouching(Vector p, Collidable c) {
		return isTouching(p, c, c.getPosition());
	}

	/** Return whether a point is touching a collidable. **/
	public static boolean isTouching(Vector p, Collidable c, Vector pos) {
		if (c.getCollisionBox() == null)
			return false;
		return new Vectangle(c, pos).contains(p);
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c, Vector pos,
			CollisionBox box, Vector boxPos) {
		if (c.getCollisionBox() == null || box == null)
			return false;
		return new Vectangle(c, pos).touches(new Vectangle(box, boxPos));
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c, Vector pos,
			CollisionModel model, Vector modelPos) {
		if (c.getCollisionBox() == null || model == null)
			return false;
		for (int i = 0; i < model.getNumCollisionBoxes(); i++) {
			if (isTouching(c, pos, model.getCollisionBox(i), modelPos))
				return true;
		}
		return false;
	}


	// ============= OBJECT COLLISION CHECKING =============== //

	public static boolean canDodgeCollisions(EntityObject obj) {
		return canDodgeCollisions(obj, obj.getPosition());
	}

	public static boolean canDodgeCollisions(EntityObject obj, Vector pos) {
		if (obj.getHardCollisionBox() == null)
			return false;

		Frame frame = game.getFrame();

		// Collide with Tiles.
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);
				
				if (t != null && t.isSolid()) {
					if (obj.isHalfSolidPassable() && t.isHalfSolid())
						continue;
					if (obj.isLedgePassable() && t.isLedge()) {
						Vector ledgeVector = Direction.lengthVector(1, t.getLedgeDir());
						if (!obj.isPassingOverLedge() && obj.getVelocity().scalarProjection(ledgeVector) >= GMath.EPSILON) {
							continue;
						}
						else if (obj.hasPassedOverLedge())
							continue;
					}
					for (int i = 0; i < t.getCollisionModel()
							.getNumCollisionBoxes(); i++) {

						if (!canDodgeCollision(obj, pos, t.getCollisionModel()
								.getCollisionBox(i), t.getPosition())) {
							return false;
						}
					}
				}
			}
		}

		// Collide with Frame-Objects.
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e instanceof FrameObject) {
				FrameObject frameObj = (FrameObject) e;

				if (frameObj.isSolid()) {
					if (!canDodgeCollision(obj, pos,
							frameObj.getCollisionBox(), frameObj.getPosition())) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public static boolean canDodgeCollision(EntityObject obj, Vector pos,
			Collidable c) {
		return canDodgeCollision(obj, pos, c.getCollisionBox(), c.getPosition());
	}

	public static boolean canDodgeCollision(EntityObject obj, Vector pos,
			CollisionBox box, Vector boxPos) {
		if (obj.getHardCollisionBox() == null)
			return false;

		CollisionBox objBox = obj.getHardCollisionBox();
		Vector objPos = new Vector(pos);
		Vectangle objVect = objBox.getVect(objPos);
		Vectangle staticVect = box.getVect(boxPos);

		Vectangle checkVect = objBox
				.getVect(objPos.plus(obj.getVelocity().x, 0));
		if (checkVect.touches(staticVect)) {
			if (obj.reboundsOffWalls())
				return false;

			if (checkVect.getCenter().x < staticVect.getCenter().x) {
				objPos.x = staticVect.getX1() - objVect.getWidth()
						- objBox.getBox().getX1();
				if (obj.autoCollisionDodges())
					return canDodgeCollision(obj, pos, staticVect,
							Direction.EAST);
				return false;
			}
			else {
				objPos.x = staticVect.getX2() - objBox.getBox().getX1();
				if (obj.autoCollisionDodges())
					return canDodgeCollision(obj, pos, staticVect,
							Direction.WEST);
				return false;
			}
		}

		checkVect.set(objBox.getVect(objPos.plus(0, obj.getVelocity().y)));
		if (checkVect.touches(staticVect)) {
			if (obj.reboundsOffWalls())
				return false;

			if (checkVect.getCenter().y < staticVect.getCenter().y) {
				objPos.y = staticVect.getY1() - objVect.getHeight()
						- objBox.getBox().getY1();
				if (obj.autoCollisionDodges())
					return canDodgeCollision(obj, pos, staticVect,
							Direction.SOUTH);
				return false;
			}
			else {
				objPos.y = staticVect.getY2() - objBox.getBox().getY1();
				if (obj.autoCollisionDodges())
					return canDodgeCollision(obj, pos, staticVect,
							Direction.NORTH);
				return false;
			}
		}
		return true;
	}


	// ================= COLLISION CHECKING ================== //

	public static void performCollisions(EntityObject obj) {
		if (obj.getHardCollisionBox() == null)
			return;
		
		boolean newPassing = false;
		obj.setColliding(false);
		for (int i = 0; i < Direction.NUM_DIRS; i++)
			obj.setCollisionDir(i, false);

		Frame frame = game.getFrame();
		
		// Collide with Tiles.
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t != null && t.isSolid()) {
					if (obj.isHalfSolidPassable() && t.isHalfSolid())
						continue;
					if (obj.isLedgePassable() && t.isLedge()) {
						Vector ledgeVector = Direction.lengthVector(1, t.getLedgeDir());
						boolean touching = isTouching(obj.getHardCollidable(), obj.getPosition(), t.getCollisionModel(), t.getPosition());
						if (!obj.isPassingOverLedge() && obj.getVelocity().scalarProjection(ledgeVector) >= GMath.EPSILON) {
							if (touching)
								obj.setPassedOverLedge(true);
							continue;
						}
						else if (obj.hasPassedOverLedge()) {
							if (touching) {
								obj.setPassingOverLedge(true);
								newPassing = true;
							}
							continue;
						}
					}
					for (int i = 0; i < t.getCollisionModel()
							.getNumCollisionBoxes(); i++) {
						Collision.performCollision(obj, t.getCollisionModel()
								.getCollisionBox(i), t.getPosition());
					}
				}
			}
		}

		obj.setPassingOverLedge(newPassing);
		
		// Collide with Frame-Objects.
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e instanceof FrameObject) {
				FrameObject frameObj = (FrameObject) e;

				if (frameObj.isSolid()) {
					Collision.performCollision(obj, frameObj.getCollisionBox(),
							frameObj.getPosition());
				}
			}
			else if (e instanceof EntityObject) {
				EntityObject entityObj = (EntityObject) e;

				if (entityObj.isSolid()) {
					Collision.performCollision(obj, entityObj.getCollisionBox(),
							entityObj.getPosition());
				}
			}
		}
	}

	public static void performCollision(EntityObject obj, Collidable c) {
		performCollision(obj, c.getCollisionBox(), c.getPosition());
	}

	public static void performCollision(EntityObject obj, CollisionBox box,
			Vector boxPos) {
		if (obj.getHardCollisionBox() == null)
			return;

		CollisionBox objBox = obj.getHardCollisionBox();
		Vectangle objVect = objBox.getVect(obj.getPosition());
		Vectangle staticVect = box.getVect(boxPos);

		Vectangle checkVect = objBox.getVect(obj.getPosition().plus(
				obj.getVelocity().x, 0));
		if (checkVect.touches(staticVect)) {
			obj.setColliding(true);
			if (obj.reboundsOffWalls()) {
				obj.getVelocity().x = -obj.getVelocity().x;
				obj.onRebound();
			}
			else
				obj.getVelocity().x = 0;

			if (checkVect.getCenter().x < staticVect.getCenter().x) {
				// obj.collisionTiles[Direction.EAST] = tile;
				obj.getPosition().x = staticVect.getX1() - objVect.getWidth()
						- objBox.getBox().getX1();
				if (obj.autoCollisionDodges()) {
					if (!performCollisionDodge(obj, staticVect, Direction.EAST))
						obj.setCollisionDir(Direction.EAST, true);
				}
				else
					obj.setCollisionDir(Direction.EAST, true);
			}
			else {
				// obj.collisionTiles[Direction.WEST] = tile;
				obj.getPosition().x = staticVect.getX2()
						- objBox.getBox().getX1();
				if (obj.autoCollisionDodges()) {
					if (!performCollisionDodge(obj, staticVect, Direction.WEST))
						obj.setCollisionDir(Direction.WEST, true);
				}
				else
					obj.setCollisionDir(Direction.WEST, true);
			}
		}

		checkVect.set(objBox.getVect(obj.getPosition().plus(0,
				obj.getVelocity().y)));
		if (checkVect.touches(staticVect)) {
			obj.setColliding(true);
			if (obj.reboundsOffWalls()) {
				obj.getVelocity().y = -obj.getVelocity().y;
				obj.onRebound();
			}
			else
				obj.getVelocity().y = 0;

			if (checkVect.getCenter().y < staticVect.getCenter().y) {
				// obj.collisionTiles[Direction.SOUTH] = tile;
				obj.getPosition().y = staticVect.getY1() - objVect.getHeight()
						- objBox.getBox().getY1();
				if (obj.autoCollisionDodges()) {
					if (!performCollisionDodge(obj, staticVect, Direction.SOUTH))
						obj.setCollisionDir(Direction.SOUTH, true);
				}
				else
					obj.setCollisionDir(Direction.SOUTH, true);
			}
			else {
				// obj.collisionTiles[Direction.NORTH] = tile;
				obj.getPosition().y = staticVect.getY2()
						- objBox.getBox().getY1();
				if (obj.autoCollisionDodges()) {
					if (!performCollisionDodge(obj, staticVect, Direction.NORTH))
						obj.setCollisionDir(Direction.NORTH, true);
				}
				else
					obj.setCollisionDir(Direction.NORTH, true);
			}
		}
	}

	private static boolean canDodgeCollision(EntityObject obj, Vector pos, Vectangle box, int dir) {
		Collidable c = obj.getHardCollidable();
		double dodgeDist = obj.getCollisionDodgeDistance();
		Vectangle objBox = new Vectangle(c, obj.getPosition());
		Vector dirVect = new Vector(Direction.getDirPoint(dir));

		for (int index = 0; index < 2; index++) {
			int moveDir = (dir + (index == 0 ? 1 : 3)) % 4;
			double distance = Math.abs(objBox.getEdge((moveDir + 2) % 4) - box.getEdge(moveDir));
			Vector checkPos = pos.plus(dirVect).plus(
					Direction.lengthVector(distance, moveDir));
			Vector gotoPos = pos.plus(Direction.lengthVector(1, moveDir));


			if (distance <= dodgeDist && !placeMeetingSolid(c, checkPos)
					&& !placeMeetingSolid(c, gotoPos)) {
				return true;
			}
		}
		return false;
	}

	private static boolean performCollisionDodge(EntityObject obj, Vectangle box, int dir) {
		Collidable c = obj.getHardCollidable();
		double dodgeDist = obj.getCollisionDodgeDistance();
		Vectangle objBox = new Vectangle(c, obj.getPosition());
		Vector pos = obj.getPosition();
		Vector dirVect = new Vector(Direction.getDirPoint(dir));

		for (int index = 0; index < 2; index++) {
			int moveDir = (dir + (index == 0 ? 1 : 3)) % 4;
			double distance = Math.abs(objBox.getEdge((moveDir + 2) % 4)
					- box.getEdge(moveDir));
			Vector checkPos = pos.plus(dirVect).plus(
					Direction.lengthVector(distance, moveDir));
			Vector gotoPos = pos.plus(Direction.lengthVector(1, moveDir));


			if (distance <= dodgeDist && !placeMeetingSolid(c, checkPos)
					&& !placeMeetingSolid(c, gotoPos)) {
				obj.setPosition(gotoPos);
				return true;
			}
		}
		return false;
	}
}
