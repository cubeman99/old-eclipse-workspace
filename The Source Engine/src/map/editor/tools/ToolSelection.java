package map.editor.tools;

import java.awt.Color;
import java.util.ArrayList;
import common.Draw;
import common.Vector;
import common.shape.Polygon;
import common.shape.Rectangle;
import common.transform.Rotation;
import common.transform.Transformation;
import main.Keyboard;
import main.Mouse;
import map.editor.MapEditor;
import map.editor.tools.handles.*;


/** This tool is used to edit vertex positions. **/
public class ToolSelection extends Tool {
	public ArrayList<Polygon> clipBoard = new ArrayList<Polygon>();
	public ArrayList<Polygon> selection = new ArrayList<Polygon>();
	public Vector dragPoint;
	public boolean dragging;
	public Rectangle selectionBox;
	
	public ArrayList<TransformHandle> handles;
	public Transformation transformation;
	public boolean transforming;
	public boolean duplicated;
	public TransformHandle currentHandle;
	public TranslateHandle handleTranslate;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public ToolSelection() {}
	
	public ToolSelection(MapEditor editor) {
		super(editor, "Selection tool", "select");
		
		handles = new ArrayList<TransformHandle>();
		
		handles.add(new ResizeHandle(this, 0.0, 0.0));
		handles.add(new ResizeHandle(this, 0.5, 0.0));
		handles.add(new ResizeHandle(this, 1.0, 0.0));
		handles.add(new ResizeHandle(this, 1.0, 0.5));
		handles.add(new ResizeHandle(this, 1.0, 1.0));
		handles.add(new ResizeHandle(this, 0.5, 1.0));
		handles.add(new ResizeHandle(this, 0.0, 1.0));
		handles.add(new ResizeHandle(this, 0.0, 0.5));
		
		handles.add(new RotateHandle(this, 0, 0));
		handles.add(new RotateHandle(this, 1, 0));
		handles.add(new RotateHandle(this, 0, 1));
		handles.add(new RotateHandle(this, 1, 1));

		handleTranslate = new TranslateHandle(this);
		handles.add(handleTranslate);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return a copy of the selection. **/
	public ArrayList<Polygon> getSelectionCopy() {
		ArrayList<Polygon> copy = new ArrayList<Polygon>();
		for (int i = 0; i < selection.size(); i++)
			copy.add(selection.get(i).getCopy());
		return copy;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Add a polygon to the selection. **/
	public void addToSelection(Polygon p) {
		selection.add(p);
	}
	
	/** Remove a polygon from the selection, returning if such polygon was in the selection. **/
	public boolean removeFromSelection(Polygon p) {
		return selection.remove(p);
	}
	
	/** Add all polygons to the selection. **/
	public void selectAll() {
		selection.clear();
		for (Polygon p : map.walls)
			selection.add(p);
		adjustSelectionBox();
	}
	
	/** Clear the selection and selection box. **/
	public void clearSelection() {
		selection.clear();
		transforming = false;
		selectionBox = null;
	}

	/** Delete the selected polygons, removing them from the map. **/
	public void deleteSelection() {
		if (selection.size() == 0)
			return;
		
		while (selection.size() > 0) {
			map.walls.remove(selection.get(0));
			selection.remove(0);
		}
		clearSelection();
		editor.madeChange();
	}
	
	/** Place a duplicate of the selection where the selection is. **/
	public void duplicateSelection() {
		for (int i = 0; i < selection.size(); i++) {
			map.walls.add(new Polygon(selection.get(i)));
		}
		editor.madeChange();
	}
	
	/** Adjust the selection box to cover all the polygons in the selection. **/
	public void adjustSelectionBox() {
		for (int i = 0; i < selection.size(); i++) {
			Polygon p = selection.get(i);
			Rectangle bounds = p.getBounds();
			
			if (selectionBox == null)
				selectionBox = new Rectangle(bounds);
			if (bounds.end1.x < selectionBox.end1.x)
				selectionBox.end1.x = bounds.end1.x;
			if (bounds.end1.y < selectionBox.end1.y)
				selectionBox.end1.y = bounds.end1.y;
			if (bounds.end2.x > selectionBox.end2.x)
				selectionBox.end2.x = bounds.end2.x;
			if (bounds.end2.y > selectionBox.end2.y)
				selectionBox.end2.y = bounds.end2.y;
		}
	}
	
	/** Select all polygons in an area. **/
	public boolean selectArea(Rectangle area, SelectMode selectMode) {
		selectionBox = null;
		if (map.walls.size() == 0)
			return (selection.size() > 0);
		
		// Add shapes to the selection:
		for (int i = 0; i < map.walls.size(); i++) {
			Polygon p = map.walls.get(i);
			
			if (!selection.contains(p)) {
    			Rectangle bounds = p.getBounds();
    			
    			if ((selectMode == SelectMode.CONTAINED && area.contains(bounds) ||
    				 selectMode == SelectMode.TOUCHING  && area.touching(bounds)))
    			{
    				addToSelection(p);
    			}
			}
		}
		
		adjustSelectionBox();
		return (selection.size() > 0);
	}
	
	/** Process a mouse button click; either select a polygon or start dragging a selection. **/
	private void processMouseClick() {
		if (!checkClickSelection())
			dragNewSelection();
		adjustSelectionBox();
	}
	
	/** Start dragging a new selection box. **/
	private void dragNewSelection() {
		dragPoint = new Vector(mousePosition);
		dragging  = true;
		if (!Keyboard.shift.down())
			clearSelection();
	}
	
	/** Process polygons that are clicked in to the selection. **/
	private boolean checkClickSelection() {
		selectionBox = null;
		if (map.walls.size() == 0)
			return (selection.size() > 0);
		
		// Check if the mouse clicks on a polygon:
		for (int i = 0; i < map.walls.size(); i++) {
			Polygon p = map.walls.get(i);
			
			if (mouseCanSelectPolygon(p)) {
				if (!Keyboard.shift.down())
					clearSelection();
				else if (removeFromSelection(p)) {
					// Removed this polygon from the selection.
					return true;
				}
				// Add this polygon to the selection.
				addToSelection(p);
				adjustSelectionBox();
				if (!Keyboard.shift.down())
					startDraggingHandle(handleTranslate);
				return true;
			}
		}
		
		return false;
	}
	
	/** Return whether the mouse is able to select a polygon if it clicks. **/
	public boolean mouseCanSelectPolygon(Polygon p) {
		double zoom = editor.control.viewControl.zoom;
		
		// Check center point:
		if (mousePosition.distanceTo(p.getBounds().getCenter()) < DISTANCE_SELECT_CENTER / zoom)
			return true;
		
		// Check all edges:
		for (int i = 0; i < p.edgeCount(); i++) {
			if (mousePosition.distanceToSegment(p.getEdge(i)) < DISTANCE_SELECT_EDGE / zoom)
				return true;
		}
		
		return false;
	}
	
	/** Start dragging a transform handle. **/
	private void startDraggingHandle(TransformHandle handle) {
		transformation     = handle.getTransformation();
		transforming       = true;
		currentHandle      = handle;
		duplicated         = false;
		editor.madeChange();
		handle.startDragging(selectionBox);
		
		if (handle == handleTranslate) {
			duplicated = Keyboard.control.down();
			if (duplicated)
				duplicateSelection();
		}
	}
	
	/** Transform the polygons in the selection. */
	public void transformSelection(Transformation t) {
		for (Polygon p : selection)
			p.transform(t);
		selectionBox = null;
		adjustSelectionBox();
	}
	
	/** Rotate the selection by the given amount. **/
	public void rotateSelection(double theta) {
		Vector center = selectionBox.getCenter();
		Vector end1   = center.plus(Vector.polarVector(10, 0));
		Vector end2   = center.plus(Vector.polarVector(10, theta));
		editor.toolSelection.transformSelection(new Rotation(end1, end2, center));
	}
	
	/** Save the selection to the clip board (*copy*). **/
	public void saveToClipBoard() {
		if (selection.size() == 0)
			return;
		clipBoard.clear();
		for (Polygon p : selection) {
			clipBoard.add(new Polygon(p));
		}
	}
	
	/** Load the selection from the clip board (*paste*). **/
	public void loadFromClipBoard() {
		if (clipBoard.size() == 0)
			return;
		clearSelection();
		for (Polygon p : clipBoard) {
			Polygon newPoly = new Polygon(p);
			addToSelection(newPoly);
			map.walls.add(newPoly);
		}
		adjustSelectionBox();
		editor.madeChange();
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void initialize() {
		selection.clear();
		selectionBox   = null;
		currentHandle  = null;
		transforming   = false;
		transformation = null;
		duplicated     = false;
	}

	@Override
	public void terminate() {
		
	}

	@Override
	public boolean isBusy() {
		return (dragging || transforming);
	}
	
	@Override
	public void update() {
		
		
		// Update dragging:
		if (dragging) {
			// Drag the selection box:
			selectionBox = new Rectangle(dragPoint, mousePosition).sortEnds();
			
			if (!Mouse.left.down()) {
				// Stop dragging and selection polygons within the area.
				dragging = false;
				selectArea(selectionBox, SelectMode.CONTAINED);
			}
			else if (Keyboard.escape.pressed()) {
				// Cancel dragging:
				dragging     = false;
				selectionBox = null;
			}
		}
		// Update transformation:
		else if (transforming) {
			// Continue transforming the selection:
			currentHandle.updateTransform(selectionBox);
			Mouse.setCursor(currentHandle.getMouseCursor());
			
			if (!Mouse.left.down()) {
				// Apply the transformation:
				transformSelection(transformation);
				transforming   = false;
				selectionBox   = null;
				transformation = null;
				adjustSelectionBox();
			}
			else if (Keyboard.escape.pressed()) {
				// Cancel the transformation:
				transforming   = false;
				transformation = null;
			}
		}
		// Check for transformations:
		else if (selectionBox != null) {
			// Check all transform handles:
			for (int i = 0; i < handles.size(); i++) {
				TransformHandle handle = handles.get(i);
				
				if (handle.checkMouseOver(selectionBox) && (handle != handleTranslate || !Keyboard.shift.down())) {
					Mouse.setCursor(handle.getMouseCursor());
					
					// Start transforming the selection:
					if (Mouse.left.pressed()) {
						startDraggingHandle(handle);
						transformation     = handle.getTransformation();
						transforming       = true;
						currentHandle      = handle;
						duplicated         = false;
						editor.madeChange();
						handle.startDragging(selectionBox);
						
						if (handle == handleTranslate) {
							duplicated = Keyboard.control.down();
							if (duplicated)
								duplicateSelection();
						}
					}
					break;
				}
			}
			
			if (!transforming) {
    			if (Mouse.left.pressed())
    				processMouseClick();
    			else if (Keyboard.escape.pressed())
    				clearSelection();
			}
		}
		else {
			// Start dragging a new selection box:
			if (Mouse.left.pressed()) {
				processMouseClick();
			}
		}
	}

	@Override
	public void draw() {

		// Draw all the polygons in the selection:
		if (!transforming || !duplicated) {
    		for (int i = 0; i < selection.size(); i++) {
    			Draw.setColor(Color.RED);
    			if (transforming)
    				Draw.setColor(new Color(128, 0, 0));
    			editor.drawPolygon(selection.get(i));
    		}
		}
		
		// Draw the selection box:
		if (selectionBox != null) {
			Rectangle currentSelectionBox = new Rectangle(selectionBox);
			
			if (transforming) {
				currentSelectionBox.transform(transformation);
				currentSelectionBox.sortEnds();
				
				// Draw the transformed polygons:
				Draw.setColor(Color.RED);
				for (Polygon p : selection)
					editor.drawPolygon((Polygon) p.getTransformed(transformation));

				// Draw the original selection box:
				if (!duplicated) {
    			    Draw.setStroke(Draw.STROKE_DASHED);
    				Draw.setColor(new Color(128, 128, 0));
    				Draw.drawPolygon(selectionBox.toPolygon());
				}
			}
			
			// Draw the current selection box:
		    Draw.setStroke(Draw.STROKE_DASHED);
			Draw.setColor(Color.YELLOW);
			if (transforming)
				Draw.drawPolygon((Polygon) selectionBox.toPolygon().getTransformed(transformation));
			else
				Draw.drawPolygon(selectionBox.toPolygon());
			Draw.resetStroke();

			// Draw the transformation handles:
			if (!dragging) {
    		    Draw.resetStroke();
    			Draw.setColor(Color.WHITE);
    			for (TransformHandle handle : handles) {
    				handle.draw(selectionBox, transformation);
    			}
			}
		}
	}
	
	
	
	// ============== ENUMERATED CLASSES ============== //
	
	public static enum SelectMode
	{
	/** The shape is touching the selection area. **/
		TOUCHING,
		
	/** The shape is completely inside the selection area. **/
		CONTAINED
	}
}
