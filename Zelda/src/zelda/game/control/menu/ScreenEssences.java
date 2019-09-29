package zelda.game.control.menu;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.game.control.GameDebug;


public class ScreenEssences extends MenuScreen {
	public ScreenEssences(Menu menu, int index) {
		super(menu, index);

		SlotGroup groupEssences = createGroup();
		SlotGroup groupSide = createGroup();
		Point size = new Point(16, 16);

		Point center = new Point(44, 44);
		for (int i = 0; i < 8; i++) {
			int dir = (i / 2);
			int sign = (i % 2 == 0 ? -1 : 1);

			Point base = center.plus(Direction.getDirPoint(dir).times(28, 28));
			Point add = Direction.getDirPoint((dir + 1) % Direction.NUM_DIRS)
					.times(12 * sign, 12 * sign);
			// add = new Point();
			Point pos = base.plus(add);
			groupEssences.addSlot(pos.x, pos.y, size);
		}

		size = new Point(32, 16);
		groupSide.addSlot(112, 8, size);
		groupSide.addSlot(112, 56, size);
		groupSide.addSlot(112, 80, size);
		
		final Menu m = menu;
		groupSide.getSlot(1).setFiller(new SlotFiller() {
			
			@Override
			public String getName() {
				return "Pieces of Heart";
			}
			
			@Override
			public String getDescription() {
				int n = m.getGame().getPlayer().currencyHeartPieces.get();
				if (n == 0)
					return ("4 make a Heart Container.");
				if (n == 3)
					return ("1 more makes a Heart Container.");
				return (4 - n + " more make a Heart Container.");
			}
			
			@Override
			public void drawSlot(Point pos) {
				int n = m.getGame().getPlayer().currencyHeartPieces.get();
				Draw.drawImage(Resources.SHEET_MENU_LARGE, 0 + (n >= 1 ? 2 : 0), 0, pos.x, pos.y - 24);
				Draw.drawImage(Resources.SHEET_MENU_LARGE, 0 + (n >= 2 ? 2 : 0), 1, pos.x, pos.y - 8);
				Draw.drawImage(Resources.SHEET_MENU_LARGE, 1 + (n >= 3 ? 2 : 0), 1, pos.x + 16, pos.y - 8);
				Draw.drawImage(Resources.SHEET_MENU_LARGE, 1, 0, pos.x + 16, pos.y - 24);
				Draw.drawText(n + "/4", pos.plus(8, 8), Resources.FONT_SMALL, Color.BLACK);
			}
		});
		
		int n = groupEssences.getNumSlots();
		for (int i = 0; i < n; i++) {
			groupEssences.getSlot(i).setConnection(Direction.UP,
					groupEssences.getSlot((i + 1) % n));
			groupEssences.getSlot(i).setConnection(Direction.DOWN,
					groupEssences.getSlot((i + n - 1) % n));
			groupEssences.getSlot(i).setConnection(Direction.LEFT, groupSide);
			groupEssences.getSlot(i).setConnection(Direction.RIGHT, groupSide);
		}

		n = groupSide.getNumSlots();
		for (int i = 0; i < n; i++) {
			groupSide.getSlot(i).setConnection(Direction.DOWN,
					groupSide.getSlot((i + 1) % n));
			groupSide.getSlot(i).setConnection(Direction.UP,
					groupSide.getSlot((i + n - 1) % n));
			groupSide.getSlot(i).setConnection(Direction.LEFT, groupEssences);
			groupSide.getSlot(i).setConnection(Direction.RIGHT, groupEssences);
		}
	}
}
