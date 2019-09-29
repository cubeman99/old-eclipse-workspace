package zelda.game.control.menu;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.player.items.Item;
import zelda.main.Keyboard;


public class InventorySubScreen {
	private static final int[] Y_ANCHORS = {16, 56};

	private ScreenInventory inventory;
	private Item item;
	private Rectangle box;
	private Point boxGoalSize;
	private boolean opening;
	private boolean open;
	private int selectIndex;
	private Point boxAnchor;
	private int action;



	// ================== CONSTRUCTORS ================== //

	public InventorySubScreen(ScreenInventory inventory) {
		this.inventory = inventory;
		item = null;
		box = null;
		opening = false;
		open = false;
		selectIndex = 0;
		boxAnchor = new Point(Settings.VIEW_SIZE.x / 2, 16);
		boxGoalSize = new Point(128, 32);
		action = 0;
	}


	// =================== ACCESSORS =================== //

	public boolean isOpen() {
		return open;
	}


	// ==================== MUTATORS ==================== //

	public void update() {
		if (open) {
			if (opening) {
				if (box.size.x < boxGoalSize.x) {
					box.corner.x -= 4;
					box.size.x += 8;
				}
				else if (box.size.y < boxGoalSize.y) {
					box.size.y += 4;
				}
				else {
					opening = false;
					shiftSelectIndex(0);
				}
			}
			else {
				if (Keyboard.action.pressed() || Keyboard.start.pressed()) {
					open = false;
					inventory.equipItem(action);
					item.setTypeIndex(selectIndex);
					Sounds.SCREEN_SELECT.play();
				}
				else {
					if (Keyboard.right.pressed())
						shiftSelectIndex(1);
					else if (Keyboard.left.pressed())
						shiftSelectIndex(-1);
				}
			}
		}
	}

	private void shiftSelectIndex(int relative) {
		selectIndex += item.getNumTypes() + relative;
		selectIndex %= item.getNumTypes();

		inventory.setNameAndDescription(item.getTypeName(selectIndex),
				item.getTypeDescription(selectIndex));
	}

	public void open(Item item, int action) {
		this.item = item;
		this.action = action;
		opening = true;
		open = true;
		boxGoalSize = new Point(8 + (24 * item.getNumTypes()), 32);
		selectIndex = item.getTypeIndex();

		Point p = inventory.getCurrentSlot().getPosition();
		boxAnchor.y = (p.y > 48 ? Y_ANCHORS[0] : Y_ANCHORS[1]);
		box = new Rectangle(boxAnchor.x - 8, boxAnchor.y, 16, 8);
	}

	public void draw() {
		if (open) {
			Draw.setColor(Menu.COLOR_LIGHT);
			Draw.fillRect(box);

			if (!opening) {
				for (int i = 0; i < item.getNumTypes(); i++) {
					Point pos = box.corner.plus(4 + (i * 24), 0);

					Draw.drawSprite(item.getTypeIcon(i), pos.plus(12, 12));
					Draw.drawText(item.getAmmo(i).getFormattedString(),
							pos.plus(4, 16), Resources.FONT_SMALL, Color.WHITE);

					if (selectIndex == i) {
						Draw.drawSprite(new Sprite(Resources.SHEET_MENU_SMALL,
								4, 4), pos.plus(8, 24));
					}
				}
			}
		}
	}
}
