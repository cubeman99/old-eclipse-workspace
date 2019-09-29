package zelda.common.drops;

import zelda.game.entity.EntityObject;


public interface DropCreator {
	public int getOdds();

	public EntityObject createNewDropObject();
}
