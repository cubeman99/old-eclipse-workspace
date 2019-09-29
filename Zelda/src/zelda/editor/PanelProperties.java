package zelda.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.editor.gui.Panel;
import zelda.main.Keyboard;
import zelda.main.Mouse;


public class PanelProperties extends Panel {
	private static final Point PROPS_POS = new Point(0, 0);
	private static final int PROPS_SEP = 17;
	private static final int PROPS_NAME_OFSET = 140;



	public PanelProperties(Editor editor) {
		super(editor, "Properties");
		
		draggable  = false;
		scrollable = true;
	}

	public Property addProperty(String name, String value) {
		PropertyHolder holder = editor.getSelection();
		if (holder != null) {
			holder.getProperties().define(name, value);
			return holder.getProperties().getProperty(name);
		}
		return null;
	}

	@Override
	public void update() {
		super.update();


		PropertyHolder holder = editor.getSelection();

		if (holder != null) {
			ArrayList<Property> props = holder.getProperties()
					.getPropertiesList();

			boolean mouseIn = containsMouse();
			Point ms = getMousePos();
			int y = (ms.y - PROPS_POS.y) / PROPS_SEP;

			for (int i = 0; i < props.size(); i++) {
				Property p = props.get(i);

				if (mouseIn && i == y && Mouse.left.pressed()) {
					Mouse.clear();
					Keyboard.clear();
					String str = (String) JOptionPane.showInputDialog(editor
							.getRunner().getFrame(),
							"Set the property \"" + p.getName() + "\".",
							"Title", JOptionPane.PLAIN_MESSAGE, null, null, p
									.get());

					if (str != null && !p.get().equals(str)) {
						p.set(str);
						holder.onChangeProperty(p);
					}
				}
			}
		}
	}

	@Override
	public void draw() {
		PropertyHolder holder = editor.getSelection();

		if (holder != null) {
			ArrayList<Property> props = holder.getProperties()
					.getPropertiesList();

			boolean mouseIn = containsMouse();
			Point ms = getMousePos();
			int y = (ms.y - PROPS_POS.y) / PROPS_SEP;

			for (int i = 0; i < props.size(); i++) {
				Property p = props.get(i);
				Color col = Color.BLACK;
				if (mouseIn && i == y)
					col = Color.RED;
				
				Point dp = PROPS_POS.plus(0, PROPS_SEP * i);

				if (mouseIn && i == y) {
    				Draw.setColor(new Color(0, 100, 255, 100));
    				Draw.fillRect(new Rectangle(dp, new Point(size.x, PROPS_SEP)));
    				Draw.setColor(new Color(0, 100, 255, 160));
    				Draw.drawRect(new Rectangle(dp, new Point(size.x, PROPS_SEP)));
				}
				else if (i % 2 == 1) {
    				Draw.setColor(new Color(230, 230, 230));
    				Draw.fillRect(new Rectangle(dp, new Point(size.x, PROPS_SEP)));
				}
				
				Draw.setFont(new Font("Consolas", Font.PLAIN, 12));
				Draw.setColor(Color.BLACK);
				Draw.drawString(p.getName(), 6, dp.y + PROPS_SEP - 4);
				Draw.drawString(" = ",   6 + PROPS_NAME_OFSET, dp.y + PROPS_SEP - 4);
				Draw.drawString(p.get(), 6 + PROPS_NAME_OFSET + 20, dp.y + PROPS_SEP - 4);
				
//				Draw.drawText(p.getName(), dp.plus(4, 0),
//						Resources.FONT_TEXT, col);
//				Draw.drawText(" = " + p.get(),
//						dp.plus(PROPS_NAME_OFSET + 4, 0),
//						Resources.FONT_TEXT, col);
			}
		}
	}
}
