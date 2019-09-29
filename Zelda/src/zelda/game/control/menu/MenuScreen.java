package zelda.game.control.menu;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.control.text.LetterString;
import zelda.main.Keyboard;
import OLD.SpriteOLD;


public abstract class MenuScreen {
	private static final int DESCRIPTION_BOX_LENGTH = 16;
	private static final int SCROLL_WAIT_TIME = 41;

	protected Menu menu;
	protected SpriteOLD spriteBackground;
	protected ArrayList<SlotGroup> slotGroups;
	protected SlotGroup selectedGroup;
	private double descriptionScrollIndex;
	private double descriptionScrollStartIndex;
	private LetterString description;
	private int scrollWaitTimer;

	private Sprite spriteSlotCursorLeft;
	private Sprite spriteSlotCursorRight;


	// ================== CONSTRUCTORS ================== //

	public MenuScreen(Menu menu, int index) {
		this.menu = menu;
		this.spriteBackground = menu.getScreenBackgroundSprite(index);
		this.slotGroups = new ArrayList<SlotGroup>();
		this.selectedGroup = null;
		this.descriptionScrollIndex = 0;
		this.description = null;
		this.scrollWaitTimer = 0;

		spriteSlotCursorRight = new Sprite(Resources.SHEET_MENU_SMALL,
				new Animation(new AnimationFrame().addPart(0, 2, 0, 0).addPart(
						0, 3, 0, 8)), new Point(8, 0));
		spriteSlotCursorLeft = new Sprite(Resources.SHEET_MENU_SMALL,
				new Animation(new AnimationFrame().addPart(1, 2, 0, 0).addPart(
						1, 3, 0, 8)));
	}



	// ============== OVERRIDABLE METHODS ============== //

	public boolean canClose() {
		return true;
	}



	// =================== ACCESSORS =================== //

	public Slot getCurrentSlot() {
		if (selectedGroup == null)
			return null;
		return selectedGroup.getCurrentSlot();
	}



	// ==================== MUTATORS ==================== //

	public void update() {
		updateSlotTraversal();

		scrollWaitTimer++;
	}

	protected void updateSlotTraversal() {
		if (selectedGroup != null) {
			for (int i = 0; i < Direction.NUM_DIRS; i++) {
				if (Keyboard.arrows[i].pressed()) {
					nextSlot(i);
					Sounds.SCREEN_CURSOR.play();
				}
			}
		}
	}

	protected void drawSlotCursor() {
		if (selectedGroup != null) {
			Slot s = getCurrentSlot();
			Draw.drawSprite(spriteSlotCursorRight, s.getRect().getCorner());
			Draw.drawSprite(spriteSlotCursorLeft,
					s.getRect().getCorner().plus(s.getRect().getWidth(), 0));
		}
	}

	public void draw() {
		// Draw background.
		Draw.drawSprite(spriteBackground, 0, 0);

		// Draw slots.
		for (int i = 0; i < slotGroups.size(); i++)
			slotGroups.get(i).draw();

		// Draw slot cursor.
		drawSlotCursor();

		// Draw description text.
		if (description != null) {

			if (scrollWaitTimer > SCROLL_WAIT_TIME) {
				descriptionScrollIndex += 0.125;
				if (descriptionScrollIndex >= descriptionScrollStartIndex
						&& descriptionScrollIndex - 0.125 < descriptionScrollStartIndex) {
					descriptionScrollIndex = descriptionScrollStartIndex;
					scrollWaitTimer = 0;
				}
			}

			if ((int) descriptionScrollIndex >= description.length()) {
				descriptionScrollIndex = -DESCRIPTION_BOX_LENGTH;
			}
			if ((int) descriptionScrollIndex < description.length()) {
				LetterString descStr = description;

				if (descriptionScrollIndex < 0) {
					descStr = new LetterString(description);
					for (int i = (int) descriptionScrollIndex; i < 0; i++) {
						descStr.addLetter(0, " ");
					}
				}

				int start = Math.max(0, (int) descriptionScrollIndex);
				LetterString str = descStr.substring(start, start
						+ DESCRIPTION_BOX_LENGTH);
				Draw.drawText(str, new Point(16, 108), Resources.FONT_TEXT,
						Menu.COLOR_DARK);
			}
		}
	}

	protected Slot[][] createGridGroup(int width, int height, int startX,
			int startY, int sepX, int sepY, Point size) {
		SlotGroup group = createGroup();
		Slot[][] slotGrid = new Slot[width][height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				slotGrid[x][y] = group.addSlot(startX + (x * sepX), startY
						+ (y * sepY), size);
			}
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x == 0)
					slotGrid[x][y].setConnection(Direction.LEFT,
							slotGrid[width - 1][(y + height - 1) % height]);
				else
					slotGrid[x][y].setConnection(Direction.LEFT,
							slotGrid[x - 1][y]);

				if (x == width - 1)
					slotGrid[x][y].setConnection(Direction.RIGHT,
							slotGrid[0][(y + 1) % height]);
				else
					slotGrid[x][y].setConnection(Direction.RIGHT,
							slotGrid[x + 1][y]);

				slotGrid[x][y].setConnection(Direction.UP, slotGrid[x][(y
						+ height - 1)
						% height]);
				slotGrid[x][y].setConnection(Direction.DOWN,
						slotGrid[x][(y + 1) % height]);
			}
		}

		return slotGrid;
	}

	protected SlotGroup createGroup() {
		SlotGroup group = new SlotGroup();
		if (slotGroups.size() == 0)
			selectedGroup = group;
		slotGroups.add(group);
		return group;
	}

	protected void initSlot() {
		descriptionScrollIndex = 0;
		scrollWaitTimer = 0;
		description = null;

		SlotFiller filler = getCurrentSlot().getFiller();

		if (filler != null)
			setNameAndDescription(filler.getName(), filler.getDescription());
	}

	protected void setNameAndDescription(String name, String desc) {
		descriptionScrollIndex = 0;
		scrollWaitTimer = 0;
		description = null;
		description = new LetterString(name);

		if (description.length() > 0) {
			int nameLength = description.length();
			int n = (DESCRIPTION_BOX_LENGTH - nameLength + 1) / 2;

			for (int i = 0; i < n; i++)
				description.addLetter(" ");

			if (nameLength >= DESCRIPTION_BOX_LENGTH)
				description.addLetter(" ");

			descriptionScrollStartIndex = -((DESCRIPTION_BOX_LENGTH - nameLength) / 2);
			descriptionScrollIndex = descriptionScrollStartIndex;
		}

		description.addLetters(desc);
	}

	private void nextSlot(int dir) {
		SlotConnection sc = selectedGroup.getCurrentSlot().getConnection(dir);

		if (sc instanceof Slot)
			selectedGroup = ((Slot) sc).selectInGroup();
		else if (sc instanceof SlotGroup)
			selectedGroup = (SlotGroup) sc;

		initSlot();
	}
}
