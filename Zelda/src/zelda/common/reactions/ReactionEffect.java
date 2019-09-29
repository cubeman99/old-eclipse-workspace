package zelda.common.reactions;

import zelda.common.geometry.Vector;
import zelda.game.entity.Entity;


public interface ReactionEffect {
	public void trigger(int reactionCuase, int level, Entity source,
			Vector sourcePos);
}
