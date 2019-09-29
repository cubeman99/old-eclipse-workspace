package com.base.game.wolf3d;

import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.tile.enemy.Enemy;
import com.base.game.wolf3d.tile.mesh.Door;

public class LevelDrawer {
	public static void drawLevelData(LevelFileData data) {

		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
				int id = data.getData(x, y);
				if (LevelFileData.isWall(id)) {
    				Draw2D.setColor(new Vector3f(0.16f));
    				Draw2D.fillRect(x, y, 1, 1);
				}
			}
		}
		
		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
				int id = data.getData(x, y);
				
				if (!LevelFileData.isWall(id)) {
    				Draw2D.setColor(Draw2D.WHITE);
        			if (LevelFileData.isWall(data.getData(x + 1, y)))
        				Draw2D.drawLine(x + 1, y, x + 1, y + 1);
        			if (LevelFileData.isWall(data.getData(x - 1, y)))
        				Draw2D.drawLine(x, y, x, y + 1);
        			if (LevelFileData.isWall(data.getData(x, y + 1)))
        				Draw2D.drawLine(x, y + 1, x + 1, y + 1);
        			if (LevelFileData.isWall(data.getData(x, y - 1)))
        				Draw2D.drawLine(x, y, x + 1, y);
				}
			}
		}
	}
	
	public static void drawLevel(Level level) {
		
		// Draw doors.
//		for (Door door : level.getDoors()) {
//			Vector2f pos = door.getPosition();
//			Draw2D.setColor(new Vector3f(0, 0.16f, 0.16f));
//			Draw2D.fillRect(pos.x, pos.y, 1, 1);
//		}
		
		drawLevelData(level.getData());
		
		Vector2f playerPos = level.getPlayer().getPosition();
		Vector2f playerDir = level.getPlayer().getDirection();
		
		
		Draw2D.setColor(Draw2D.GREEN);
		Draw2D.drawCircle(playerPos, 0.25f, 16);
		Draw2D.drawLine(playerPos, playerPos.plus(playerDir.times(0.25f)));

		// Draw objects
		Draw2D.setColor(Draw2D.YELLOW);
		for (SceneObject obj : level.getObjects()) {
			Vector2f pos = obj.getTransform().getPosition().getXZ();
			Vector2f dirToPlayer = pos.minus(level.getPlayer().getPosition()).normalize();
			
			if (obj instanceof Enemy) {
				Enemy e = (Enemy) obj;
				float radius = e.getCollisionSize() / 2;
//				Vector2f dir = e.getDirection().times(radius);
				Vector2f dir = dirToPlayer.times(radius);
//				Draw2D.drawCircle(pos, radius, 16);
				Draw2D.drawLine(pos, pos.plus(-dir.y, dir.x));
				Draw2D.drawLine(pos, pos.plus(dir.y, -dir.x));
				Draw2D.drawRect(e.getCollisionBox());
			}
			else
				Draw2D.drawCircle(pos, 0.25f, 16);
		}
		
		// Draw doors.
//		for (Door door : level.getDoors()) {
//			Draw2D.setColor(Draw2D.CYAN);
//			Draw2D.drawRect(door.getDoorBox());
//		}
		
		Draw2D.setColor(Draw2D.WHITE);
		
//		Texture texWeapon = level.getGame().getPlayer().getWeaponSprite().getCurrentFrame();
//		Draw2D.drawTexture(texWeapon, playerPos.x - 10, playerPos.y + 10, 20, -20);
	}
}
