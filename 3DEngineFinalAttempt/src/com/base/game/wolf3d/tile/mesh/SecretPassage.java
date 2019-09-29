package com.base.game.wolf3d.tile.mesh;

import com.base.engine.common.GMath;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.core.Keyboard;
import com.base.engine.entity.Model;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.game.wolf3d.RayCast;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.tile.Player;

public class SecretPassage extends MeshTile {
    private Model model;
	private float offset;
	private boolean pushing;
	private Vector2f direction;
	private boolean open;
	
	
	
	public SecretPassage() {
    	super("Secret Passage", true);
    }
	
	@Override
	public void initialize() {
    	pushing   = false;
    	offset    = 0;
    	open      = false;
    	direction = new Vector2f(1, 0);

		int orientation = getData().getInt("direction") / 2;
		direction = Vector2f.polarVector(1, -orientation * GMath.HALF_PI);
    	int texX = getData().getInt("textureX");
    	int texY = getData().getInt("textureY");
    	Material mat1 = new Material().setTexture(Wolf3D.WALL_TEXTURES[texX][texY]);
    	Material mat2 = new Material().setTexture(Wolf3D.WALL_TEXTURES[texX + 1][texY]);
    	
		Mesh[] meshes = new Mesh[4];
		meshes[0] = Primitives.createXYPlane(0, 1, 0, 1, 0, false);
		meshes[1] = Primitives.createXYPlane(0, 1, 0, 1, 1, true);
		meshes[2] = Primitives.createZYPlane(0, 1, 0, 1, 0, true);
		meshes[3] = Primitives.createZYPlane(0, 1, 0, 1, 1, false);
		Material[] materials = new Material[] {mat1, mat1, mat2, mat2};
		
		model = new Model(meshes, materials, 4);
		addChild(model);
	}
	
	public void open(int dir) {
		direction   = Vector2f.polarVector(1, dir * GMath.HALF_PI);
		direction.x = -direction.x;
		pushing     = true;
		offset      = 0;
		open        = true;
	}
	
	@Override
	public SecretPassage clone() {
		return new SecretPassage();
	}
	
	@Override
	public Rect2f getCollisionBox() {
		Vector2f offsetPos = direction.times((int) offset);
		Vector2f pos = getPosition();
		return new Rect2f(pos.x + offsetPos.x, pos.y + offsetPos.y, 1, 1);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (pushing) {
			offset += delta;
			if (offset >= 2.0f) {
				offset = 2.0f;
				pushing = false;
			}
		}
		else if (!open && Keyboard.isKeyDown(Player.KEY_USE)) {
			RayCast rayCast = getPlayer().getRayCast();
			
			if (rayCast.distance < 1.5f && rayCast.object == this) {
    			open(((int) (direction.getDirection() / GMath.HALF_PI + 0.5f) + 2) % 4);
			}
		}
		
		
		model.getTransform().getPosition().setXZ(direction.times(offset));
	}
}
