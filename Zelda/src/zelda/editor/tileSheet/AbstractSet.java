package zelda.editor.tileSheet;

import java.awt.Color;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.editor.Editor;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.main.Mouse;


public abstract class AbstractSet extends Panel {
	protected Point cursor;



	// ================== CONSTRUCTORS ================== //

	public AbstractSet(Editor editor, String title) {
		super(editor, title);
		cursor = new Point();
	}



	// =============== ABSTRACT METHODS =============== //

	public abstract void createTile(Frame frame, Point pos);

	public abstract void eraseTile(Frame frame, Point pos);

	protected abstract void drawSet();

	protected abstract Point getSize();



	// =================== ACCESSORS =================== //

	public Point getCursor() {
		return cursor;
	}



	// ==================== MUTATORS ==================== //

	public void setCursor(Point cursor) {
		this.cursor.set(cursor);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		super.update();
		Point ms = getMousePos(17);

		if (containsMouse()) {
			if (ms.x >= 0 && ms.y >= 0 && ms.x < getSize().x
					&& ms.y < getSize().y) {
				if (Mouse.left.pressed()) {
					cursor.set(ms);
					editor.setSetFocus(this);
					editor.toolbar.setToolIndex(0);
				}
			}
		}
	}

	@Override
	public void draw() {
		// Draw the set.
		drawSet();

		if (editor.getFocusedSet() == this) {
			// Draw the cursor.
			Draw.setColor(Color.BLACK);
			Draw.drawRect(new Rectangle((cursor.x * 17) - 2,
					(cursor.y * 17) - 2, 20, 20));
			Draw.setColor(Color.WHITE);
			Draw.drawRect(new Rectangle((cursor.x * 17) - 1,
					(cursor.y * 17) - 1, 18, 18));
		}
	}
}
