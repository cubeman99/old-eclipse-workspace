package zelda.game.entity;

import java.util.ArrayList;


public class CollisionModel {
	private ArrayList<CollisionBox> collisionBoxes;



	// ================== CONSTRUCTORS ================== //

	public CollisionModel() {
		this.collisionBoxes = new ArrayList<CollisionBox>();
	}

	public CollisionModel(CollisionBox... boxes) {
		this();
		for (int i = 0; i < boxes.length; i++)
			collisionBoxes.add(boxes[i]);
	}

	public CollisionModel(CollisionModel copy) {
		collisionBoxes = new ArrayList<CollisionBox>();
		if (copy != null) {
			for (int i = 0; i < copy.collisionBoxes.size(); i++)
				collisionBoxes
						.add(new CollisionBox(copy.collisionBoxes.get(i)));
		}
	}



	// =================== ACCESSORS =================== //
	//
	// public boolean isCollidingWith(Vectangle rect, Vector displacement) {
	// for (int i = 0; i < collisionBoxes.size(); i++) {
	// if (getRectangle(i).plus(displacement).touches(rect))
	// return true;
	// }
	// return false;
	// }
	//
	// public boolean isCollidingWith(Vectangle rect) {
	// return isCollidingWith(rect, new Vector());
	// }
	//
	// public boolean isCollidingWith(CollisionModel other, Vector displacement)
	// {
	// for (int i = 0; i < collisionBoxes.size(); i++) {
	// if (other.isCollidingWith(getRectangle(i).plus(displacement)))
	// return true;
	// }
	// return false;
	// }
	//
	// public boolean isCollidingWith(CollisionModel other) {
	// return isCollidingWith(other, new Vector());
	// }
	//
	// public Vectangle getRectangle(int index) {
	// return collisionBoxes.get(index).plus(positionTracker.trackPosition());
	// }
	//
	public boolean isSolid() {
		return (collisionBoxes.size() > 0);
	}

	public int getNumCollisionBoxes() {
		return collisionBoxes.size();
	}

	public CollisionBox getCollisionBox(int index) {
		return collisionBoxes.get(index);
	}



	// ==================== MUTATORS ==================== //

	public void addCollisionBox(int offsetX, int offsetY, int width, int height) {
		collisionBoxes.add(new CollisionBox(offsetX, offsetY, width, height));
	}

	public void addCollisionBox(CollisionBox box) {
		collisionBoxes.add(box);
	}
}
