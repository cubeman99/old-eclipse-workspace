package map.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import javax.swing.JFileChooser;
import main.Keyboard;
import main.Mouse;
import map.Map;
import map.editor.tools.*;
import common.Draw;
import common.GMath;
import common.HUD;
import common.Vector;
import common.shape.Circle;
import common.shape.Line;
import common.shape.Polygon;
import common.shape.Rectangle;
import control.Control;


/*
	TODO Make vertex tool add vertices.
	TODO Fix losing key focus (Ctrl + O) (reset all keys)
	TODO Save file directory + recent files
	TODO Ask if want to save unsaved changes
	TODO Rotate, Scale, etc windows
	TODO make transformations snap to original drag positions
	TODO Make buttons not fire in middle of a step; queue them up dawg!
*/


/**
 * Map Editor.
 * 
 * @author David Jordan
 */
public class MapEditor {
	private static final double MIN_GRID_SCALE    = 0.0625;
	private static final double MAX_GRID_SCALE    = 100;
	private static final int MAX_GRID_SCALE_INDEX = 16;
	private static final double[] GRID_SCALE_DATA = new double[MAX_GRID_SCALE_INDEX];
	static {
		double scale = MIN_GRID_SCALE;
		for (int i = 0; i < MAX_GRID_SCALE_INDEX; i++) {
			GRID_SCALE_DATA[i] = scale;
			scale *= 2;
		}
	}
	
	public Control control;
	public EditorComponent editorComp;
	public Map map;
	public double gridScale;
	public boolean showGrid;
	public Toolbar toolbar;
	public JFileChooser fileChooser;
	public File currentFile;
	public boolean changesMade;
	
	private int gridScaleIndex;
	
	public ToolSelection toolSelection;
	public ToolRectangle toolRectangle;
	public ToolLine toolLine;
	public ToolPolygon toolPolygon;
	public ToolVertex toolVertex;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public MapEditor(Map map, EditorComponent editorComp) {
		this.map            = map;
		this.control        = map.control;
		this.editorComp     = editorComp;
		this.gridScaleIndex = 0;
		this.gridScale      = 0;
		this.showGrid       = true;
		this.toolbar        = new Toolbar(this);
		this.fileChooser    = new JFileChooser();
		this.currentFile    = null;
		this.changesMade    = false;

		setGridScale(0.5);

		toolSelection = new ToolSelection(this);
		toolRectangle = new ToolRectangle(this);
		toolLine      = new ToolLine(this);
		toolPolygon   = new ToolPolygon(this);
		toolVertex    = new ToolVertex(this);

		toolbar.createTools(
			toolSelection,
			toolRectangle,
			toolLine,
			toolPolygon,
			toolVertex
		);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the current tool in use. **/
	public Tool getTool() {
		return toolbar.getTool();
	}
	
	/** Get the nearest point on the grid to a given point. **/
	public Vector getGridPoint(Vector v) {
		return new Vector(
			(int) ((v.x / gridScale) + 0.5) * gridScale,
			(int) ((v.y / gridScale) + 0.5) * gridScale
		);
	}
	
	/** Return whether shapes should currently be snapped to grid. **/
	public boolean snapToGrid() {
		return (showGrid && !Keyboard.alt.down());
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the index of which tool to use. **/
	public void setToolIndex(int newIndex) {
		toolbar.setToolIndex(newIndex);
	}
	
	/** Set the tool to use. **/
	public void setTool(Tool newTool) {
		toolbar.setToolIndex(newTool);
	}
	
	/** Increase the scale of the grid. **/
	public void increaseGridScale() {
		gridScaleIndex = Math.min(gridScaleIndex + 1, MAX_GRID_SCALE_INDEX);
		gridScale = GRID_SCALE_DATA[gridScaleIndex];
	}
	
	/** Decrease the scale of the grid. **/
	public void decreaseGridScale() {
		gridScaleIndex = Math.max(gridScaleIndex - 1, 0);
		gridScale = GRID_SCALE_DATA[gridScaleIndex];
	}
	
	/** Set the grid scale to the given scale, returning if such scale exists in the grid scale data. **/
	private boolean setGridScale(double newGridScale) {
		gridScale = GMath.max(MIN_GRID_SCALE, GMath.min(MAX_GRID_SCALE, newGridScale));
		for (int i = 0; i < MAX_GRID_SCALE_INDEX; i++) {
			if (GMath.abs(GRID_SCALE_DATA[i] - newGridScale) < GMath.EPSILON) {
				gridScale = GRID_SCALE_DATA[i];
				gridScaleIndex = i;
				return true;
			}
		}
		return false;
	}
	
	/** Select a polygon while switching to the selection tool if needed. **/
	public void selectPolygon(Polygon p) {
		setTool(toolSelection);
		toolSelection.selectionBox = null;
		toolSelection.addToSelection(p);
		toolSelection.adjustSelectionBox();
	}
	
	/** Acknowledge that a change was made to the map. **/
	public void madeChange() {
		if (!changesMade) {
			changesMade = true;
			editorComp.refreshWindowTitle();
		}
	}
	
	/** Start a new map. **/
	public void fileNew() {
		map.clear();
		currentFile = null;
		editorComp.refreshWindowTitle();
		changesMade = false;
	}
	
	/** Load a new map. **/
	public void fileOpen() {
		File dir = new File("C:/Eclipse Workspace/workspace/Top-Down Shooter 2/maps");
		fileChooser.setCurrentDirectory(dir); //TODO
		
		if (fileChooser.showOpenDialog(editorComp) == JFileChooser.APPROVE_OPTION) {
			currentFile = fileChooser.getSelectedFile();
			map.loadFromFile(currentFile.getAbsolutePath());
			editorComp.fileMenu.addRecentFile(currentFile);
			changesMade = false;
			getTool().terminate();
			getTool().initialize();
		}
	}
	
	/** Open a given file. **/
	public void fileOpen(File file) {
		if (file == null)
			return;
		if (!file.exists() || file.equals(currentFile))
			return;
		
		currentFile = file;
		map.loadFromFile(currentFile.getAbsolutePath());
		editorComp.fileMenu.addRecentFile(currentFile);
		changesMade = false;
		getTool().terminate();
		getTool().initialize();
	}
	
	/** Save the current map as itself. **/
	public void fileSave() {
		if (currentFile != null) {
			map.saveToFile(currentFile.getAbsolutePath());
			changesMade = false;
		}
		else {
			fileSaveAs();
		}
	}
	
	/** Save the current map as a new file. **/
	public void fileSaveAs() {
		File dir = new File("C:/Eclipse Workspace/workspace/Top-Down Shooter 2/maps");
		fileChooser.setCurrentDirectory(dir); //TODO
		
		if (fileChooser.showSaveDialog(editorComp) == JFileChooser.APPROVE_OPTION) {
			currentFile = fileChooser.getSelectedFile();
			map.saveToFile(currentFile.getAbsolutePath());
			editorComp.fileMenu.addRecentFile(currentFile);
			changesMade = false;
		}
	}
	
	/** Draw a small rectangle for a vertex. **/
	public void drawVertex(Vector vertex, int size) {
		Graphics g  = HUD.getGraphics();
		Color c     = g.getColor();
		Vector dv   = map.getViewPoint(vertex);
		Rectangle r = new Rectangle(dv, dv).grow(size);
		g.fillRect((int) r.end1.x, (int) r.end1.y, (int) r.width() - 1, (int) r.height());
		g.setColor(Color.BLACK);
		g.drawRect((int) r.end1.x, (int) r.end1.y, (int) r.width() - 1, (int) r.height() - 1);
		g.setColor(c);
	}
	
	/** Draw a small circle for a vertex. **/
	public void drawCircleVertex(Vector vertex, int radius) {
		Graphics g  = HUD.getGraphics();
		Color c     = g.getColor();
		Vector dv   = map.getViewPoint(vertex);
		Rectangle r = new Circle(dv, radius).getBounds();
		g.fillOval((int) r.end1.x, (int) r.end1.y, (int) r.width() - 1, (int) r.height());
		g.setColor(Color.BLACK);
		g.drawRect((int) r.end1.x, (int) r.end1.y, (int) r.width() - 1, (int) r.height() - 1);
		g.setColor(c);
	}
	
	/** Draw a small cross shape representing the center (AABB center) of a polygon. **/
	public void drawCenterPoint(Vector center) {
		Graphics g  = HUD.getGraphics();
		Vector dv   = map.getViewPoint(center);
		Line l1     = new Line(dv.x - 3, dv.y - 3, dv.x + 3, dv.y + 3);
		Line l2     = new Line(dv.x + 3, dv.y - 3, dv.x - 3, dv.y + 3);
		g.drawLine((int) l1.x1(), (int) l1.y1(), (int) l1.x2(), (int) l1.y2());
		g.drawLine((int) l2.x1(), (int) l2.y1(), (int) l2.x2(), (int) l2.y2());
	}
	
	/** Draw a polygon in the editor with vertex joints and a center point. **/
	public void drawPolygon(Polygon p) {
		Color c = Draw.getColor();
		
		Draw.drawPolygon(p);
		drawCenterPoint(p.getBounds().getCenter());
		
		Draw.setColor(Color.WHITE);
		for (int i = 0; i < p.vertexCount(); i++)
			drawVertex(p.getVertex(i), 5);
		
		Draw.setColor(c);
	}
	
	
	// =============== INHERITED METHODS =============== //
	
	/** Update the Map Editor. **/
	public void update() {
		boolean snapToGrid = (showGrid && !Keyboard.alt.down());
		Vector ms  = control.viewControl.getGamePoint(Mouse.getVector());
		Vector mgs = (snapToGrid ? getGridPoint(ms) : ms);
		
		// Update the current tool:
		getTool().mousePositionLast     = getTool().mousePosition;		
		getTool().mouseGridPositionLast = getTool().mouseGridPosition;
		getTool().mousePosition         = ms;		
		getTool().mouseGridPosition     = mgs;
		getTool().update();
		
		// Change tool:
		if (Keyboard.space.pressed()) {
			setToolIndex(toolbar.getToolIndex() + 1);
		}
		
		
		
		// Save and Load:
		/*
		String filename = "maps/testmap1.txt";
		if (Keyboard.saveMap.pressed()) {
			// Save:
			map.saveToFile(filename);
			System.out.println("Map saved.");
		}
		if (Keyboard.loadMap.pressed()) {
			// Load:
			map.loadFromFile(filename);
			System.out.println("Loaded from file.");
		}
		*/
	}
	
	/** Draw the Map Editor. **/
	public void draw() {
		boolean snapToGrid = (showGrid && !Keyboard.alt.down());
		Vector ms  = control.viewControl.getGamePoint(Mouse.getVector());
		Vector mgs = (snapToGrid ? getGridPoint(ms) : ms);

		// Draw map boundaries:
		Draw.setColor(new Color(32, 32, 32));
		Draw.drawRect(Vector.ORIGIN, map.size);
		
		// Draw grid:
		if (showGrid) {
			for (double x = 0; x < map.size.x; x += gridScale)
				Draw.drawLine(x, 0, x, map.size.y);
			for (double y = 0; y < map.size.y; y += gridScale)
				Draw.drawLine(0, y, map.size.x, y);
		}
		
		// Draw the map walls:
		Draw.setColor(Color.WHITE);
		for (int i = 0; i < map.walls.size(); i++) {
			drawPolygon(map.walls.get(i));
		}
    		
		
    		
		// Handle tool drawing:
		Tool t = getTool();
		t.mousePositionLast     = t.mousePosition;		
		t.mouseGridPositionLast = t.mouseGridPosition;
		t.mousePosition         = ms;		
		t.mouseGridPosition     = mgs;
		t.draw();
	}
}
