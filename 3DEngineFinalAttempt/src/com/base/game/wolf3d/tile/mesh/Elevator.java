package com.base.game.wolf3d.tile.mesh;

import com.base.engine.audio.AudioEngine;
import com.base.engine.common.GMath;
import com.base.engine.common.Line2f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.entity.Model;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.event.EventLevelComplete;
import com.base.game.wolf3d.tile.Player;

public class Elevator extends MeshTile {
	private Model model;
	private Material matFront;
	private Material matSide;
	private Vector2f direction;
	
	
	public Elevator() {
		super("Elevator", false);
	}
	
	@Override
	public void initialize() {
		int orientation = getData().getInt("direction") / 2;
		direction = Vector2f.polarVector(1, -orientation * GMath.HALF_PI);
		
		matFront = new Material();
		matFront.setTexture(Wolf3D.WALL_TEXTURES[5][17]);
		matSide = new Material();
		matSide.setTexture(Wolf3D.WALL_TEXTURES[4][17]);
		
		Mesh[] meshes = new Mesh[3];
		meshes[0] = Primitives.createZYPlane(-0.5f, 0.5f, 0, 1, -0.5f, false);
		meshes[1] = Primitives.createXYPlane(-0.5f, 0.5f, 0, 1, -0.5f, true);
		meshes[2] = Primitives.createXYPlane(-0.5f, 0.5f, 0, 1, 0.5f, false);
		Material[] materials = new Material[] {matFront, matSide, matSide};
		model = new Model(meshes, materials, 3);
		model.getTransform().rotate(Vector3f.Y_AXIS, -orientation * GMath.HALF_PI);
		model.getTransform().setPosition(0.5f, 0, 0.5f);
		addChild(model);
	}
	
	@Override
	public Elevator clone() {
		return new Elevator();
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		

		
		// Check for player opening door.
		if (Keyboard.isKeyPressed(Player.KEY_USE)) {
    		Vector2f pos       = getPosition().plus(0.5f);
    		Vector2f playerPos = level.getPlayer().getPosition();
    		Vector2f look      = level.getPlayer().getDirection().setLength(1.5f);
    		Line2f playerLine  = new Line2f(playerPos, playerPos.plus(look));
    		
    		Vector2f dir = direction.times(0.5f);
    		dir.y = -dir.y;
    		Line2f doorLine = new Line2f(pos.minus(dir).plus(-dir.y, dir.x),
    				pos.minus(dir).plus(dir.y, -dir.x));
    	
    		if (Line2f.intersection(playerLine, doorLine) != null) {
    			getGame().playEvent(new EventLevelComplete());
    			matFront.setTexture(Wolf3D.WALL_TEXTURES[1][18]);
    			AudioEngine.playSound(Wolf3D.SOUND_SWITCH);
    		}
		}
	}
}
