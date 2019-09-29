package zelda.editor;

import java.awt.Color;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.editor.gui.Panel;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.main.Mouse;


public class PanelNavigation extends Panel {
	private static final int SEP = 16;


	public PanelNavigation(Editor editor) {
		super(editor, "Levels");

		draggable  = false;
		scrollable = true;
	}

	private int getSelectedIndex() {
		if (containsMouse()) {
			return getMousePos().y / SEP;
		}
		return -1;
	}

	@Override
	public void update() {
		super.update();
		World world = editor.getWorld();
		
		if (containsMouse() && Mouse.left.pressed()) {
			int index = getSelectedIndex();
			if (index >= 0 && index < world.getNumLevels()) {
				world.setCurrentLevel(index);
				editor.setSelected(world.getLevel(index));
			}
		}
	}

	public void draw() {
		World world = editor.getWorld();

		for (int i = 0; i < world.getNumLevels(); i++) {
			Level lvl = world.getLevel(i);

			Draw.setColor(i % 2 == 0 ? Color.WHITE : new Color(230, 230, 230));
			if (lvl == world.getCurrentLevel())
				Draw.setColor(new Color(255, 255, 220));
			if (getSelectedIndex() == i)
				Draw.setColor(new Color(230, 230, 255));

			Draw.fillRect(new Rectangle(0, i * SEP, super.getRect().getWidth(),
					SEP));
			
			if (getSelectedIndex() == i || lvl == world.getCurrentLevel()) {
				Draw.setColor(Color.BLACK);
				Draw.drawRect(new Rectangle(0, i * SEP, super.getRect()
						.getWidth(), SEP));
			}

			Draw.drawText(lvl.getName(), new Point(12, i * SEP),
					Resources.FONT_TEXT, Color.BLACK);
		}
	}
}
