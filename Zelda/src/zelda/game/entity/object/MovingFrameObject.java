package zelda.game.entity.object;

import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.game.entity.object.dungeon.ObjectCrackedFloor;


public class MovingFrameObject extends EntityObject {
	protected static final int MOVE_SPEED = 1;
	protected FrameObject object;
	protected double distance;
	protected int moveDir;
	protected double moveSpeed;
	
	
	
	public MovingFrameObject(FrameObject object, int moveDir) {
		super(object.getGame());

		this.object    = object;
		this.moveDir   = moveDir;
		this.position  = new Vector(object.getPosition());
		this.distance  = 0;
		this.moveSpeed = 0.5;

		this.sprite   = new Sprite(object.getEntitySprite());
		
		solid            = true;
		collideWithWorld = false;
		hardCollisionBox = new CollisionBox(0, 0, 16, 16);
		softCollisionBox = new CollisionBox(0, 0, 16, 16);
		
		object.destroy();
	}
	
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	protected void updateMovement() {
		position.add(Direction.lengthVector(moveSpeed, moveDir));
		distance += moveSpeed;
	}
	
	@Override
	public Vector getCenter() {
		return position.plus(8, 8);
	}

	@Override
	public void update() {
		updateMovement();

		if (distance >= 16) {
			ObjectCrackedFloor floor = (ObjectCrackedFloor)
					Collision.getInstanceMeeting(getCenter(),
					this, ObjectCrackedFloor.class);
			if (floor != null)
				floor.crumble();
			
			if (!checkIfFallen()) {
				destroy();
				object.setDestroyed(false);
				object.getGame().addEntity(object);
			}
		}
	}

//	@Override
//	public void draw() {
//		Draw.drawSprite(sprite, position);
//	}
}
