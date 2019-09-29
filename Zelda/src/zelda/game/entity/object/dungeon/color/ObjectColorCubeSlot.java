package zelda.game.entity.object.dungeon.color;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.util.Colors;
import zelda.game.entity.Entity;
import zelda.game.entity.object.FrameObject;


public class ObjectColorCubeSlot extends FrameObject {
	private int color;
	
	public ObjectColorCubeSlot() {
		properties.set("color", Colors.NONE);
		color = Colors.NONE;
	}
	
	public boolean changeColor(int col) {
		if (color != col) {
			color = col;
			properties.set("color", color);
			properties.script("event_change_color", this, frame);
			return true;
		}
		return false;
	}

	public void checkColor() {
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectColorCube) {
				ObjectColorCube cube = (ObjectColorCube) entities.get(i);
				if (cube.getLocation().equals(getLocation())) {
					int col = cube.getProperties().getInt("color", Colors.NONE);
					changeColor(col);
					return;
				}
			}
		}
		changeColor(Colors.NONE);
	}

	@Override
	public void initialize() {
		setDepth(10000);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("color");
		objectData.addEvent("event_change_color", "Called when a color cube has moved onto the slot.");
	}
	
	@Override
	public void preDraw() {
		super.draw();
	}
	
	@Override
	public void draw() {
		// Don't draw.
	}

	@Override
	public Point createSpriteSource() {
		return new Point(7, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectColorCubeSlot();
	}
}
