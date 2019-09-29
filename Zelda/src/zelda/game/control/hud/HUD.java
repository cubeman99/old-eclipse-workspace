package zelda.game.control.hud;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.player.Player;
import zelda.game.player.items.Item;
import zelda.game.world.Dungeon;
import zelda.main.Keyboard;


public class HUD {
	private static final Point POS_CURRENCY_ICON = new Point(80, 0);
	private static final Point POS_CURRENCY_COUNT = POS_CURRENCY_ICON
			.plus(0, 8);
	private static final Point POS_KEY_COUNT = POS_CURRENCY_ICON.plus(8, 0);

	private GameInstance game;
	private Image[] background;
	private int currencyDisplayAmount;
	private int healthDisplayAmount;



	// ================== CONSTRUCTORS ================== //

	public HUD(GameInstance game) {
		this.game = game;

		currencyDisplayAmount = 0;
		healthDisplayAmount = 0;

		background = new Image[] {createBackgroundImage(0),
				createBackgroundImage(1)};
	}



	// =================== ACCESSORS =================== //



	// ==================== MUTATORS ==================== //

	private Image createBackgroundImage(int index) {
		Image img = new BufferedImage(Settings.VIEW_SIZE.x, 16,
				BufferedImage.TYPE_INT_RGB);
		Draw.setGraphics(img.getGraphics());
		for (int x = 0; x < Settings.VIEW_SIZE.x; x += 8) {
			for (int y = 0; y < 16; y += 8) {
				Draw.drawImage(Resources.SHEET_MENU_SMALL, new Point(index, 4),
						new Vector(x, y));
			}
		}
		return img;
	}

	public void draw() {
		Player player = game.getPlayer();
		Draw.setViewPosition(new Vector(0, 0));

		// Draw HUD background bar.
		Draw.drawImage(background[game.getMenu().isOpen() ? 1 : 0], 0, 0);

		// Draw the equipped-item slots.
		if (player.getInventory().isEquippedItemTwoHanded()) {
			drawBracket("BR", 8, 0);
			drawItemIcon(0, 16, 0);
			drawBracket("AL", 56, 0);
		}
		else {
			drawBracket("BR", 0, 0);
			drawItemIcon(1, 8, 0);
			drawBracket("L", 32, 0);

			drawBracket("AR", 40, 0);
			drawItemIcon(0, 48, 0);
			drawBracket("L", 72, 0);
		}

		// TODO: DEBUG: Change number of rupees
		if (Keyboard.F1.down()) {
			if (Keyboard.control.down())
				player.currencyRupees.take(1);
			else
				player.currencyRupees.give(1);
		}

		if (currencyDisplayAmount != player.currencyRupees.get()) {
			currencyDisplayAmount += Math.signum(player.currencyRupees.get()
					- currencyDisplayAmount);
			// TODO: Sound
		}

		if (healthDisplayAmount != player.currencyHearts.get()) {
			healthDisplayAmount += Math.signum(player.currencyHearts.get()
					- healthDisplayAmount);
			// TODO: Sound
		}
		
		// Draw the key count.
		Dungeon dun = game.getLevel().getDungeon();
		if (dun != null) {
			Draw.drawText("x" + dun.getNumSmallKeys(), POS_KEY_COUNT, Resources.FONT_SMALL, Color.BLACK);
			Draw.drawImage(Resources.SHEET_MENU_SMALL, new Point(2, 1), new Vector(
					POS_CURRENCY_ICON));
		}
		else {
			Draw.drawImage(Resources.SHEET_MENU_SMALL, new Point(0, 1), new Vector(
					POS_CURRENCY_ICON));
		}
		
		// Draw the currency count.
		Draw.drawText(
				player.currencyRupees.getFormattedString(currencyDisplayAmount),
				POS_CURRENCY_COUNT, Resources.FONT_SMALL, Color.BLACK);
		
		// TODO: DEBUG: modify player's hearts.
		if (Keyboard.control.down()) {
			if (Keyboard.pageUp.pressed())
				player.setMaxHealth(player.getMaxHealth() + 4);
			if (Keyboard.pageDown.pressed())
				player.setMaxHealth(player.getMaxHealth() - 4);
		}
		else {
			if (Keyboard.pageUp.pressed())
				player.setHealth(player.getHealth() + 1);
			if (Keyboard.pageDown.pressed())
				player.setHealth(player.getHealth() - 1);
		}

		// Draw the hearts.
		int max = Settings.MAX_HEART_CONTAINERS;
		int numHearts = player.currencyHearts.getMax() / 4;
		int life = healthDisplayAmount;

		for (int i = 0; i < numHearts; i++) {
			int sub = (i < life / 4 ? 4 : 0);
			if (i == life / 4)
				sub = life % 4;
			Point pos = new Point(104 + ((i % (max / 2)) * 8),
					(i / (max / 2)) * 8);

			Draw.drawImage(Resources.SHEET_MENU_SMALL, new Point(sub, 0),
					new Vector(pos));
		}
	}

	private void drawItemIcon(int actionIndex, int x, int y) {
		Item item = game.getPlayer().getEquippedItem(actionIndex);
		if (item != null)
			item.drawSlot(new Point(x, y), true);
	}

	private void drawBracket(String type, int x, int y) {
		Point sp1 = new Point();
		Point sp2 = new Point();

		if (type.equals("R")) {
			sp1.set(0, 2);
			sp2.set(0, 3);
		}
		else if (type.equals("L")) {
			sp1.set(1, 2);
			sp2.set(1, 3);
		}
		else if (type.equals("LR")) {
			sp1.set(2, 2);
			sp2.set(2, 3);
		}
		else if (type.equals("AR")) {
			sp1.set(3, 2);
			sp2.set(0, 3);
		}
		else if (type.equals("AL")) {
			sp1.set(4, 2);
			sp2.set(1, 3);
		}
		else if (type.equals("BR")) {
			sp1.set(3, 3);
			sp2.set(0, 3);
		}
		else if (type.equals("BL")) {
			sp1.set(4, 3);
			sp2.set(1, 3);
		}

		Draw.drawImage(Resources.SHEET_MENU_SMALL, sp1, new Vector(x, y));
		Draw.drawImage(Resources.SHEET_MENU_SMALL, sp2, new Vector(x, y + 8));
	}
}
