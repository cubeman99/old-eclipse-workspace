package OLD;

import zelda.game.entity.object.FrameObject;


public class ObjectDataOLD extends DataOLD {
	public FrameObject objectTemplate;

	public ObjectDataOLD(int sx, int sy, FrameObject objectTemplate) {
		super(sx, sy);

		this.objectTemplate = objectTemplate;
	}

	public FrameObject getObjectTemplate() {
		return objectTemplate;
	}
}
