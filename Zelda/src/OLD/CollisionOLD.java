package OLD;

import java.util.ArrayList;
import zelda.common.Settings;
import zelda.common.collision.Collidable;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.util.Direction;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;
import zelda.game.entity.Entity;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;


public class CollisionOLD {
	private static GameInstance control;

	public static void setControl(GameInstance control) {
		CollisionOLD.control = control;
	}

	public static class CollisionData {
		public CollisionBox box;
		public Vector position;
		public Vector velocity;
		public Vector newVelocity;
		public boolean[] collisionDirs;
		public GridTile[] collisionTiles;

		public CollisionData(CollisionBox box, Vector position,
				Vector velocity, Vector newVelocity) {
			this.box = box;
			this.position = position;
			this.velocity = velocity;
			this.newVelocity = newVelocity;
			this.collisionDirs = new boolean[Direction.NUM_DIRS];
			this.collisionTiles = new GridTile[Direction.NUM_DIRS];
		}

		public CollisionData(CollisionBox box, Vector position, Vector velocity) {
			this(box, position, velocity, null);
		}

		public CollisionData(CollisionBox box, Vector position) {
			this(box, position, new Vector(), null);
		}

		public CollisionData(CollisionData copy) {
			this.box = copy.box;
			this.position = copy.position;
			this.velocity = copy.velocity;
			this.newVelocity = null;
			this.collisionDirs = new boolean[Direction.NUM_DIRS];
			this.collisionTiles = new GridTile[Direction.NUM_DIRS];

			for (int i = 0; i < Direction.NUM_DIRS; i++) {
				collisionDirs[i] = copy.collisionDirs[i];
				collisionTiles[i] = copy.collisionTiles[i];
			}
		}

		public CollisionData(Collidable c) {
			this(c.getCollisionBox(), c.getPosition());
		}

		public Vectangle getVect() {
			return box.getVect(position);
		}
	}


	/**
	 * Return whether the given collidable object placed at the given position
	 * is colliding with an instance of the given class.
	 */
	public static boolean placeMeeting(Collidable c, Vector position,
			Class<? extends Collidable> cls) {
		return isCollidingWith(
				new CollisionData(c.getCollisionBox(), position), cls);
	}


	/**
	 * Return whether the given collisionData is colliding with an instance of
	 * the given class.
	 */
	public static boolean isCollidingWith(CollisionData obj,
			Class<? extends Collidable> cls) {
		for (int i = 0; i < control.getEntities().size(); i++) {
			Entity e = control.getEntities().get(i);

			if (cls.isAssignableFrom(e.getClass())) {
				Collidable collidable = (Collidable) e;

				if (collidable.getCollisionBox() != null
						&& CollisionOLD.performCollision(
								new CollisionData(obj), new CollisionData(
										collidable))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return whether the given collidable object placed at the given position
	 * is colliding with an instance of the given class.
	 */
	public static boolean placeMeetingSolid(Collidable c, Vector position) {
		if (c.getCollisionBox() == null)
			return false;
		return collideWithWorld(new CollisionData(c.getCollisionBox(), position));
	}


	/**
	 * Return an object (if one exists), descending from the given class, that
	 * is colliding with the given collidable object.
	 */
	public static Entity getInstanceMeeting(Collidable c, Vector position,
			Class<? extends Collidable> objectClass) {
		for (int i = 0; i < control.getEntities().size(); i++) {
			Entity e = control.getEntities().get(i);

			if (objectClass.isAssignableFrom(e.getClass())) {
				Collidable collidable = (Collidable) e;

				if (c.getCollisionBox() != null
						&& c.getCollisionBox()
								.getVect(position)
								.touches(
										collidable.getCollisionBox().getVect(
												collidable.getPosition()))) {
					return e;
				}

				// if (Collision.performCollision(
				// new CollisionData(c.getCollisionBox(), position),
				// new CollisionData(collidable)))
				// {
				// return e;
				// }
			}
		}
		return null;
	}


	public static ArrayList<GridTile> getTilesMeeting(Collidable c) {
		return getTilesMeeting(c, null);
	}

	/**
	 * Returns a list of tiles that are touching the given collidable object.
	 */
	public static ArrayList<GridTile> getTilesMeeting(Collidable c,
			CollisionBox tileCollisionBox) {
		ArrayList<GridTile> tiles = new ArrayList<GridTile>();
		Frame frame = control.getFrame();
		Vectangle v = new Vectangle(c);
		int startX = Math.max(0, (int) (v.getX1() / Settings.TS));
		int startY = Math.max(0, (int) (v.getY1() / Settings.TS));

		for (int x = startX; x < frame.getWidth(); x++) {
			for (int y = startY; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t != null) {
					if (tileCollisionBox != null) {
						if (isTouching(c, new CollisionModel(tileCollisionBox),
								new Vector(x * Settings.TS, y * Settings.TS))) {
							tiles.add(t);
						}
					}
					else {
						if (isTouching(c, t.getCollisionModel(), new Vector(x
								* Settings.TS, y * Settings.TS))) {
							tiles.add(t);
						}
					}
				}
			}
		}

		return tiles;
	}


	public static boolean collideWithWorld(CollisionData obj) {
		Frame frame = control.getFrame();
		boolean colliding = false;

		// Collide with tiles.
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (t == null)
					continue;

				if (t.getProperties().getBoolean("solid", false)) {
					for (int i = 0; i < t.getCollisionModel()
							.getNumCollisionBoxes(); i++) {
						if (CollisionOLD.performCollision(obj,
								new CollisionData(t.getCollisionModel()
										.getCollisionBox(i), new Vector(x * 16,
										y * 16)), t)) {
							colliding = true;
						}
					}
				}
			}
		}

		// Collide with objects.
		for (int i = 0; i < control.getEntities().size(); i++) {
			Entity e = control.getEntities().get(i);

			if (e instanceof FrameObject) {
				FrameObject frameObj = (FrameObject) e;

				if (frameObj.isSolid()) {
					if (CollisionOLD.performCollision(obj,
							new CollisionData(new CollisionBox(0, 0, 16, 16),
									new Vector(frameObj.getPosition())))) {
						colliding = true;

					}
				}
			}
		}

		return colliding;
	}

	public static boolean performCollision(CollisionData obj,
			CollisionData staticObj) {
		return performCollision(obj, staticObj, null);
	}

	public static boolean performCollision(CollisionData obj,
			CollisionData staticObj, GridTile tile) {
		boolean colliding = false;
		Vectangle objVect = new Vectangle(obj.getVect());
		Vectangle staticVect = staticObj.getVect();

		Vectangle checkVect = obj.box.getVect(obj.position.plus(obj.velocity.x,
				0));
		if (checkVect.touches(staticVect)) {
			colliding = true;
			if (obj.newVelocity != null)
				obj.newVelocity.x = 0;

			if (checkVect.getCenter().x < staticVect.getCenter().x) {
				obj.collisionDirs[Direction.EAST] = true;
				obj.collisionTiles[Direction.EAST] = tile;
				obj.position.x = staticVect.getX1() - objVect.getWidth()
						- obj.box.getBox().getX1();
			}
			else {
				obj.collisionDirs[Direction.WEST] = true;
				obj.collisionTiles[Direction.WEST] = tile;
				obj.position.x = staticVect.getX2() - obj.box.getBox().getX1();
			}
		}

		checkVect.set(obj.box.getVect(obj.position.plus(0, obj.velocity.y)));
		if (checkVect.touches(staticVect)) {
			colliding = true;
			if (obj.newVelocity != null)
				obj.newVelocity.y = 0;

			if (checkVect.getCenter().y < staticVect.getCenter().y) {
				obj.collisionDirs[Direction.SOUTH] = true;
				obj.collisionTiles[Direction.SOUTH] = tile;
				obj.position.y = staticVect.getY1() - objVect.getHeight()
						- obj.box.getBox().getY1();
			}
			else {
				obj.collisionDirs[Direction.NORTH] = true;
				obj.collisionTiles[Direction.NORTH] = tile;
				obj.position.y = staticVect.getY2() - obj.box.getBox().getY1();
			}
		}

		return colliding;
	}


	public static boolean checkColliding(CollisionData obj,
			CollisionData staticObj) {
		if (obj == null || staticObj == null)
			return false;
		return obj.getVect().touches(staticObj.getVect());
	}

	/** Return whether two collidable objects are touching/colliding. **/
	public static boolean isTouching(Collidable c1, Collidable c2) {
		return new Vectangle(c1).touches(new Vectangle(c2));
	}

	/**
	 * Return whether the given collidable object is touching the collision
	 * model placed at the given position.
	 */
	public static boolean isTouching(Collidable c, CollisionModel model,
			Vector modelPos) {
		if (model == null)
			return false;
		for (int i = 0; i < model.getNumCollisionBoxes(); i++) {
			if (new Vectangle(c).touches(model.getCollisionBox(i).getVect(
					modelPos)))
				return true;
		}
		return false;
	}
}
