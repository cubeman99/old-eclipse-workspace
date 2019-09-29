package zelda.game.entity;

import zelda.common.Settings;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;


public class CollisionBox {
	public static final CollisionBox TILE_BOX = new CollisionBox(0, 0,
			Settings.TS, Settings.TS);

	private Vectangle box;



	// ================== CONSTRUCTORS ================== //

	public CollisionBox(double offsetX, double offsetY, double width,
			double height) {
		box = new Vectangle(offsetX, offsetY, width, height);
	}
	
	public CollisionBox(Vectangle vect) {
		box = new Vectangle(vect);
	}

	public CollisionBox(CollisionBox copy) {
		box = new Vectangle(copy.box);
	}



	// =================== ACCESSORS =================== //

	public boolean isCollidingWith(CollisionBox other) {
		return box.touches(other.box);
	}

	public boolean isCollidingWith(CollisionBox other, Vector displacement) {
		return box.plus(displacement).touches(other.box);
	}

	public boolean isCollidingWith(Vectangle vect) {
		return box.touches(vect);
	}

	public boolean isCollidingWith(Vectangle vect, Vector displacement) {
		return box.plus(displacement).touches(vect);
	}

	public Vector getCenter() {
		return box.getCenter();
	}

	public Vectangle getBox() {
		return box;
	}

	public Vectangle getVect(Vector pos) {
		return box.plus(pos);
	}

	public Vectangle getVect(int x, int y) {
		return box.plus(new Vector(x, y));
	}

	// public boolean collisionSnap(Unit unit, Vector displacement, Vectangle
	// vect) {
	// boolean colliding = false;
	//
	// if (unit.collisionBox.isCollidingWith(vect, new Vector(displacement.x,
	// 0))) {
	// colliding = true;
	// if (unit.collisionBox.getCenter().x < vect.getCenter().x) {
	// unit.getPosition().x = vect.getX1() -
	// unit.collisionBox.getBox().getWidth();
	// }
	// else {
	// unit.getPosition().x = vect.getX2();
	// }
	// }
	//
	// if (unit.collisionBox.isCollidingWith(vect, new Vector(0,
	// displacement.y))) {
	// colliding = true;
	// if (unit.collisionBox.getCenter().y < vect.getCenter().y) {
	// unit.getPosition().y = vect.getY1() -
	// unit.collisionBox.getBox().getHeight();
	// }
	// else {
	// unit.getPosition().y = vect.getY2();
	// }
	// }
	//
	// return colliding;
	// }
}
