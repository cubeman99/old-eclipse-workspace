package zelda.game.control;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.util.GMath;
import zelda.game.control.text.Message;
import zelda.game.entity.EntityObject;
import zelda.game.player.Player;
import zelda.game.player.items.Item;
import zelda.game.world.Dungeon;
import zelda.main.Keyboard;


public class GameDebug {
	private GameInstance game;

	public GameDebug(GameInstance game) {
		this.game = game;
	}

	public void update() {
		Player player = game.getPlayer();


		// DELETE: Drop collectable from sky.
		if (Keyboard.delete.pressed()) {
			EntityObject e = player.getPurse().dropsAll.createDropObject();
			e.setPosition(player.getCenter());
			e.setZVelocity(0);
			e.setZPosition(e.getPosition().y + 6);
			if (e != null)
				game.addEntity(e);
		}

		// HOME: Refill all ammo.
		if (Keyboard.home.pressed()) {
			player.currencyHearts.fill();
			for (int i = 0; i < player.getInventory().getNumItems(); i++) {
				player.getInventory().getItem(i).giveAmmo(99);
			}
		}

		// END: Morph the player into a random form.
		if (Keyboard.end.pressed()) {
			if (Keyboard.control.down()) {
				player.setDefaultMoveStandAnimations(Animations.PLAYER_DEFAULT);
				player.resetMoveStandAnimations();
				player.resetAnimation();
			}
			else {
				int index = GMath.random
						.nextInt(Animations.PLAYER_FORMS.length);
				player.setDefaultMoveStandAnimations(Animations.PLAYER_FORMS[index]);
			}
		}

		// INSERT: Change level/type of equipped item.
		if (Keyboard.insert.pressed()) {
			Dungeon dun = game.getLevel().getDungeon();
			if (dun != null) {
				dun.acquireItem("map");
				dun.acquireItem("compass");
				dun.acquireItem("boss_key");
				dun.addSmallKey();
				dun.addSmallKey();
				dun.addSmallKey();
				dun.addSmallKey();
			}
			player.getInventory().obtainAllItems();
			Item itm = player.getEquippedItem(Keyboard.control.down() ? 1 : 0);
			if (itm != null)
				itm.shiftType();
		}
		
		if (Keyboard.backspace.pressed())
			game.readMessage(new Message("Entities: 2"));
	}

	public void draw() {
		// DEBUG: Draw the current dungeon name.
//		Dungeon dun = game.getWorld().getCurrentLevel().getDungeon();
//		if (dun != null) {
//    		Draw.setViewPosition(new Vector(0, -16));
//    		Draw.drawText(dun.getName(), new Point(3, 3), Resources.FONT_TEXT, Color.BLACK);
//    		Draw.drawText(dun.getName(), new Point(3, 2), Resources.FONT_TEXT, Color.WHITE);
//		}
		
		// DEBUG: Draw the number of entities.
		
//		Draw.setViewPosition(new Vector(0, -16));
//		Draw.drawText("x" + game.getFrame().getEntities().size(),
//				new Point(2, 2), Resources.FONT_SMALL, Color.RED);
	}
}
