package zelda.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Direction;
import zelda.editor.gui.Bound;
import zelda.editor.gui.Panel;
import zelda.editor.gui.Tooltip;
import zelda.editor.tileSheet.AbstractSet;
import zelda.editor.tileSheet.Objectset;
import zelda.editor.tileSheet.Tileset;
import zelda.game.ZeldaRunner;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.main.GameRunner;
import zelda.main.Keyboard;
import zelda.main.Mouse;


public class Editor {
	private GameRunner runner;
	private World world;
	private File openFile;
	private boolean madeChanges;
	private boolean testing;
	private AbstractSet setFocus;
	private PropertyHolder selection;
	
	public Tooltip tooltip;
	
	private ArrayList<Panel> panels;
	public PanelButtons buttonbar;
	public PanelToolbar toolbar;
	public PanelStatusbar statusbar;
	public PanelNavigation panelNavigation;
	public PanelProperties panelProperties;
	public PanelFrameDisplay panelFrameDisplay;
	public Tileset tileset;
	public Objectset objectset;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Editor(GameRunner runner) {
		Resources.initialize();
		initUI();
		Resources.zones.initialize();

		this.runner = runner;
		this.testing = false;
		this.world = new World(null);
		this.madeChanges = false;
		this.openFile = null;

		this.world.addLevel("overworld", 5, 4, Frame.SIZE_SMALL);
		this.world.addLevel("interiors", 3, 3, Frame.SIZE_SMALL);
		this.world.addLevel("underground", 5, 5, Frame.SIZE_LARGE);
		this.world.addLevel("level1", 5, 6, Frame.SIZE_LARGE);
		
		this.tooltip = new Tooltip();
		
		setFocus = tileset;
		selection = null;

		for (int i = 0; i < world.getNumLevels(); i++)
			fillNewLevel(world.getLevel(i));

		updateTitle();
	}



	// =================== ACCESSORS =================== //

	public World getWorld() {
		return world;
	}

	public PropertyHolder getSelection() {
		return selection;
	}

	public AbstractSet getFocusedSet() {
		return setFocus;
	}

	public Tileset getTileset() {
		return tileset;
	}

	public Objectset getObjectset() {
		return objectset;
	}

	public GameRunner getRunner() {
		return runner;
	}

	public boolean hasMadeChanges() {
		return madeChanges;
	}

	public String getFileDisplayName() {
		String fileName = "<new world>";
		if (openFile != null)
			fileName = openFile.getName();
		return fileName;
	}

	public File getFile() {
		return openFile;
	}



	// ==================== MUTATORS ==================== //

	public void setFocusedSet(AbstractSet set) {
		setFocus = set;
	}

	public void update() {
		if (testing) {
			
		}
		else {
			if (Keyboard.backspace.pressed()) {
				loadWorld(new File("autosave.zwd"));
			}

			// Update panels.
			for (int i = 0; i < panels.size(); i++) {
				Panel p = panels.get(i);
				p.computePositions(new Point(runner.getViewWidth(), runner
						.getViewHeight()));
				p.update();
			}
			
			toolbar.getTool().update();
			tooltip.update();

			// Start testing.
			if (Keyboard.enter.pressed()) {
				testRun();
			}
		}
	}

	public void draw(Graphics g) {
		
		for (Panel p : panels)
			p.draw(g);
		
		Draw.setGraphics(g);
		tooltip.draw((Graphics2D) g);
	}

	public void testRun() {
		saveWorld(new File("autosave.zwd"));
		
		testing           = true;
		ZeldaRunner zr    = new ZeldaRunner();
		GameInstance game = zr.control;
		game.setWorld(world);
		runner.end(false);
	}

	private void initUI() {
		panels = new ArrayList<Panel>();
		
		// Buttonbar
		buttonbar = new PanelButtons(this);
		buttonbar.setBound(new Bound(Direction.SOUTH, 38));
		buttonbar.setColor(Color.RED);
		panels.add(buttonbar);

		// Toolbar
		toolbar = new PanelToolbar(this);
		toolbar.setBound(new Bound(Direction.NORTH, buttonbar));
		toolbar.setBound(new Bound(Direction.SOUTH, 38));
		toolbar.setColor(Color.RED);
		panels.add(toolbar);
		
		
		Panel objectDisplay = new Panel(this, "Selection");
		objectDisplay.setBound(new Bound(Direction.NORTH, 200));
		objectDisplay.setBound(new Bound(Direction.EAST, 160));
		panels.add(objectDisplay);

		// ObjectSheet
		objectset = new Objectset(this);
		objectset.setBound(new Bound(Direction.NORTH, 160));
		objectset.setBound(new Bound(Direction.SOUTH, objectDisplay));
		objectset.setBound(new Bound(Direction.WEST, 330));
		objectset.setColor(Color.BLUE);
		panels.add(objectset);
		
		// Statusbar
		statusbar = new PanelStatusbar(this);
		statusbar.setBound(new Bound(Direction.NORTH, 32));
		statusbar.setBound(new Bound(Direction.SOUTH, objectDisplay));
		statusbar.setBound(new Bound(Direction.WEST, objectDisplay));
		statusbar.setBound(new Bound(Direction.EAST, objectset));
		statusbar.setColor(Color.RED);
		panels.add(statusbar);

		// Frame Display
		panelFrameDisplay = new PanelFrameDisplay(this);
		panelFrameDisplay.setBound(new Bound(Direction.NORTH, toolbar));
		panelFrameDisplay.setBound(new Bound(Direction.SOUTH, statusbar));
		panelFrameDisplay.setBound(new Bound(Direction.WEST, objectDisplay));
		panelFrameDisplay.setBound(new Bound(Direction.EAST, objectset));
		panelFrameDisplay.setColor(Color.YELLOW);
		panelFrameDisplay.setDraggable(false);
		panels.add(panelFrameDisplay);
		
		// Navigator
		panelNavigation = new PanelNavigation(this);
		panelNavigation.setBound(new Bound(Direction.NORTH, toolbar));
		panelNavigation.setBound(new Bound(Direction.SOUTH, objectDisplay));
		panelNavigation.setBound(new Bound(Direction.EAST, panelFrameDisplay));
		panelNavigation.setColor(Color.GREEN);
		panels.add(panelNavigation);

		// Properties
		panelProperties = new PanelProperties(this);
		panelProperties.setBound(new Bound(Direction.NORTH, statusbar));
		panelProperties.setBound(new Bound(Direction.WEST, objectDisplay));
		panelProperties.setColor(Color.CYAN);
		panels.add(panelProperties);

		// TileSheet
		tileset = new Tileset(this);
		tileset.setBound(new Bound(Direction.NORTH, toolbar));
		tileset.setBound(new Bound(Direction.SOUTH, objectset));
		tileset.setBound(new Bound(Direction.WEST, panelFrameDisplay));
		tileset.setColor(Color.MAGENTA);
		panels.add(tileset);
	}
	
	public void setToolTip(String text, int x, int y) {
		tooltip.set(text, x, y);
	}

	public void setMadeChanges(boolean madeChanges) {
		this.madeChanges = madeChanges;
		updateTitle();
	}

	public void madeChange() {
		if (!madeChanges) {
			madeChanges = true;
			updateTitle();
		}
	}

	public void setFile(File file) {
		this.openFile = file;
		updateTitle();
	}

	public void setSetFocus(AbstractSet setFocus) {
		this.setFocus = setFocus;
	}

	public void setSelected(PropertyHolder selection) {
		this.selection = selection;
	}

	public void saveWorld(File file) {
		try {
			FileOutputStream fileOs = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOs);

			world.save(out);

			out.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadWorld(File file) {
		try {
			FileInputStream fileIs = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIs);

			world.load(in, this);

			in.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void fillNewFrame(Frame frame) {
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame.setGridTile(tileset
						.newDefaultTile(frame, new Point(x, y)));
			}
		}
	}

	public void fillNewLevel(Level level) {
		for (int fx = 0; fx < level.getSize().x; fx++) {
			for (int fy = 0; fy < level.getSize().y; fy++) {
				fillNewFrame(level.getFrame(fx, fy));
			}
		}
	}

	public void resizeLevel(Level level, int width, int height) {
		Point sizePrev = level.getSize();
		level.resize(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x >= sizePrev.x || y >= sizePrev.y)
					fillNewFrame(level.getFrame(x, y));
			}
		}
	}

	private void updateTitle() {
		if (runner != null) {
    		String name = getFileDisplayName();
    		if (madeChanges)
    			name += "*";
    		runner.setTitle(name + " - Zelda World Creator");
		}
	}
}
