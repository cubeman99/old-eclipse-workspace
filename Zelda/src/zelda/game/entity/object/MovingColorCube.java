package zelda.game.entity.object;

import zelda.common.util.Direction;

public class MovingColorCube extends MovingFrameObject {
	private int timer;
	
	
	public MovingColorCube(FrameObject object, int moveDir) {
		super(object, moveDir);
		moveSpeed = 0;
		timer     = 0;
	}
	
	@Override
	protected void updateMovement() {
		if (timer % 4 == 0) {
			position.add(Direction.lengthVector(4, moveDir));
			distance += 4;
		}

		timer++;
	}
}
