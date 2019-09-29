package zelda.game.control.menu;

import zelda.common.geometry.Point;
import zelda.common.util.Direction;


public class ScreenItems extends MenuScreen {
	public ScreenItems(Menu menu, int index) {
		super(menu, index);

		Point size = new Point(16, 16);
		Slot[][] grid1 = createGridGroup(5, 3, 24, 8, 24, 24, size);
		Slot[][] grid2 = createGridGroup(4, 1, 8, 80, 24, 0, size);

		for (int y = 0; y < 3; y++)
			grid1[4][y].getSize().x = 24;

		grid2[0][0].getPosition().x += 4;

		for (int i = 0; i < 3; i++) {
			grid1[i][2].setConnection(Direction.DOWN, grid2[i + 1][0]);
			grid2[i + 1][0].setConnection(Direction.UP, grid1[i][2]);

			grid1[i][0].setConnection(Direction.UP, grid2[i + 1][0]);
			grid2[i + 1][0].setConnection(Direction.DOWN, grid1[i][0]);
		}

		grid2[0][0].setConnection(Direction.UP, grid1[0][2]);
		grid2[0][0].setConnection(Direction.DOWN, grid1[0][0]);



		grid1[0][0].setConnection(Direction.LEFT, grid2[3][0]);
		grid2[3][0].setConnection(Direction.RIGHT, grid1[0][0]);

		grid2[0][0].setConnection(Direction.LEFT, grid1[4][2]);
		grid1[4][2].setConnection(Direction.RIGHT, grid2[0][0]);
	}
}
