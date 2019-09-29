package zelda.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.tile.GridTile;
import zelda.main.Keyboard;


public class PanelFrameDisplay extends Panel {



	// ================== CONSTRUCTORS ================== //

	public PanelFrameDisplay(Editor editor) {
		super(editor);

		backgroundColor = Color.LIGHT_GRAY;
		zoomScale = 1;
	}



	// =================== ACCESSORS =================== //

	public GridTile getMouseTile() {
		Frame frame = getMouseFrame();
		if (frame == null)
			return null;
		Point ms = getMousePos(16).minus(
				frame.getSize().times(frame.getLocation()));
		if (!frame.contains(ms))
			return null;
		return frame.getGridTile(ms.x, ms.y);
	}

	public Frame getMouseFrame() {
		if (!containsMouse())
			return null;
		Level level = editor.getWorld().getCurrentLevel();
		Point ms = getMousePos(16);
		Point loc = ms.dividedBy(level.getFrameSize());
		if (level.frameExists(loc))
			return level.getFrame(loc);
		return null;
	}



	// ==================== MUTATORS ==================== //

	@Override
	public void update() {
		super.update();

		Frame frame = getMouseFrame();
		
		// [CONTROL + INSERT] Save Level for C++
		if (Keyboard.control.down() && Keyboard.insert.pressed())
		{
			JFileChooser fileChooser = new JFileChooser("C:/Workspace/C++/ZeldaCPP/res/levels");
			if (fileChooser.showSaveDialog(editor.getRunner().getFrame()) == JFileChooser.APPROVE_OPTION)
			{
    			File file = fileChooser.getSelectedFile();
    			String fileName = file.getAbsolutePath();
    			if (!file.getName().endsWith(".zwd"))
    				fileName += ".zwd";

    			try {
    				OutputStream out = null;
    				try {
    					out = new BufferedOutputStream(new FileOutputStream(fileName));
    					editor.getWorld().getCurrentLevel().saveCPP(out);
    				}
    				finally {
    					out.close();
    				}
    			}
    			catch(FileNotFoundException ex){
    				System.out.println("File not found.");
    			}
    			catch(IOException ex){
    				System.out.println(ex);
    			}
			}
		}

		// [INSERT] Save Frame/Room for C++
		else if (Keyboard.insert.pressed() && frame != null)
		{
			JFileChooser fileChooser = new JFileChooser("C:/Workspace/C++/ZeldaCPP/res/levels");
			if (fileChooser.showSaveDialog(editor.getRunner().getFrame()) == JFileChooser.APPROVE_OPTION)
			{
    			File file = fileChooser.getSelectedFile();
    			String fileName = file.getAbsolutePath();
    			if (!file.getName().endsWith(".zwd"))
    				fileName += ".zwd";
    			
    			try {
    				OutputStream out = null;
    				try {
    					out = new BufferedOutputStream(new FileOutputStream(fileName));
        				frame.saveCPP(out);
    				}
    				finally {
    					out.close();
    				}
    			}
    			catch(FileNotFoundException ex){
    				System.out.println("File not found.");
    			}
    			catch(IOException ex){
    				System.out.println(ex);
    			}
			}
		}
		
		if (Keyboard.pageUp.pressed())
			zoomScale = Math.min(6, zoomScale + 1);
		if (Keyboard.pageDown.pressed())
			zoomScale = Math.max(1, zoomScale - 1);

		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			if (Keyboard.arrows[i].down()) {
				moveViewPosition(Direction.getDirPoint(i)
						.scaledBy(8 / zoomScale).negate());
			}
		}
	}

	@Override
	public void draw() {
		Level level = editor.getWorld().getCurrentLevel();

		// Draw frames.
		for (int fx = 0; fx < level.getSize().x; fx++) {
			for (int fy = 0; fy < level.getSize().y; fy++) {
				Frame frame = level.getFrame(fx, fy);
				int dx = fx * level.getFrameSize().x * 16;
				int dy = fy * level.getFrameSize().y * 16;

				Draw.setViewPosition(new Vector(dx, dy).negate());

				frame.drawGridTiles();
				if (editor.buttonbar.buttonShowObjects.isToggled())
					frame.drawObjectTiles();
			}
		}

		// Draw editor tool.
		editor.toolbar.getTool().draw();

		// Draw frame borders.
		if (editor.buttonbar.buttonShowFrameBorders.isToggled()) {
			for (int fx = 0; fx < level.getSize().x; fx++) {
				for (int fy = 0; fy < level.getSize().y; fy++) {
					int dx = fx * level.getFrameSize().x * 16;
					int dy = fy * level.getFrameSize().y * 16;
					Draw.setViewPosition(new Vector(dx, dy).negate());

					Graphics g = getGraphics();
					g.setColor(backgroundColor);
					g.drawRect(dx, dy, (level.getFrameSize().x * 16) - 1,
							(level.getFrameSize().y * 16) - 1);
					g.setColor(Color.BLACK);
					g.drawRect(dx + 1, dy + 1,
							(level.getFrameSize().x * 16) - 2,
							(level.getFrameSize().y * 16) - 2);
				}
			}
		}
	}
}
