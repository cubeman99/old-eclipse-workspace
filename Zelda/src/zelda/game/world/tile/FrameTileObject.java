package zelda.game.world.tile;

import zelda.common.graphics.Sprite;
import zelda.common.properties.Properties;
import zelda.common.properties.PropertyHolder;
import zelda.game.entity.Entity;
import zelda.game.world.Frame;


public interface FrameTileObject extends PropertyHolder {
	public void linkProperties(Properties p);
	public void setObjectTile(ObjectTile t);
	public ObjectTile getObjectTile();
	public void enterFrame(ObjectTile t);
	public Entity getThis();
	public Sprite getFrameSprite(Frame frame);
}
