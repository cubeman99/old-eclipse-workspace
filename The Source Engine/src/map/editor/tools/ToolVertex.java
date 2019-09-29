package map.editor.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.util.ArrayList;
import common.Draw;
import common.GMath;
import common.Vector;
import common.shape.Line;
import common.shape.Polygon;
import common.shape.Rectangle;
import main.Keyboard;
import main.Mouse;
import map.editor.MapEditor;


/** This tool is used to edit vertex positions. **/
public class ToolVertex extends Tool {
	public static final double VERTEX_MERGE_DISTANCE = 0.0004;
	
	public ArrayList<Integer> selectedIndexes;
	public ArrayList<Polygon> polygonScopes;
	public ArrayList<Vector> dragPositions;
	public int dragIndex;
	public boolean selectedMidpoints;
	public boolean dragging;
	public boolean draggingSelectionBox;
	public Vector selectionBoxStart;
	
	
	// ================== CONSTRUCTORS ================== //

	public ToolVertex() {}
	
	public ToolVertex(MapEditor editor) {
		super(editor, "Vertex editor", "vertex");
		selectedIndexes = new ArrayList<Integer>();
		polygonScopes   = new ArrayList<Polygon>();
		dragPositions   = new ArrayList<Vector>();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return if a given vertex is already selected. **/
	public boolean vertexIsSelected(Polygon p, int index, boolean isMidpoint) {
		if (selectedMidpoints == isMidpoint) {
    		for (int i = 0; i < selectedIndexes.size(); i++) {
    			if (index == selectedIndexes.get(i) && p == polygonScopes.get(i))
    				return true;
    		}
		}
		return false;
	}
	
	/** Return the index in the selection that a vertex is at, returning -1 if not in the selection. **/
	public int getVertexSelectionIndex(Polygon p, int index, boolean isMidpoint) {
		if (selectedMidpoints == isMidpoint) {
    		for (int i = 0; i < selectedIndexes.size(); i++) {
    			if (index == selectedIndexes.get(i) && p == polygonScopes.get(i))
    				return i;
    		}
		}
		return -1;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Check if two adjacent vertices are occupying the same space
	 *  and prompt the user whether to merge them or not.
	 */
	public boolean checkForMerge(int checkIndex) {
		/* TODO
		Vector checkVertex = polygonScope.getVertex(checkIndex);
		if (GMath.distance(selectVertex, checkVertex) < VERTEX_MERGE_DISTANCE) {
			// Ask the user if he would like to merge vertices:
			int option = JOptionPane.showOptionDialog(
				null,
				"Merge these two vertices?",
				"Confirm Vertex Merge",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				new String[] {"Yes", "No"},
				""
			);
			
			if (option == 0) {
				// Confirmed; Merge the 2 vertices:
				polygonScope.removeVertex(selectIndex);
				selectVertex = checkVertex;
				selectIndex  = checkIndex;
				if (polygonScope.vertexCount() < 2) {
					map.walls.remove(polygonScope);
					polygonScope = null;
				}
			}
			return true;
		}
		*/
		return false;
	}
	
	/** Perform actions along selecting/deselecting vertices. **/
	public boolean checkVertexSelect(Polygon scope, Vector v, int index, boolean isMidpoint) {
		if (GMath.distance(v, mousePosition) < 9.0 / map.control.viewControl.zoom) {
			Mouse.setCursor(Cursor.CROSSHAIR_CURSOR);
			
			if (Mouse.left.pressed()) {
				int inIndex = getVertexSelectionIndex(scope, index, isMidpoint);
				
				if (selectedMidpoints != isMidpoint || (!Keyboard.shift.down() && inIndex < 0)) {
					selectedIndexes.clear();
					polygonScopes.clear();
				}
				else if (Keyboard.shift.down() && inIndex >= 0) {
					selectedIndexes.remove(inIndex);
					polygonScopes.remove(inIndex);
					return true;
				}

				dragging  = true;
				dragIndex = inIndex;
				editor.madeChange();
				
				if (inIndex < 0) {
    				selectedMidpoints = isMidpoint;
    				dragIndex         = selectedIndexes.size();
    				selectedIndexes.add(index);
    				polygonScopes.add(scope);
				}
				
				dragPositions.clear();
				if (selectedMidpoints)
					dragIndex *= 2;
				
				for (int i = 0; i < selectedIndexes.size(); i++) {
					if (selectedMidpoints) {
						dragPositions.add(new Vector(polygonScopes.get(i).getVertex(selectedIndexes.get(i))));
						dragPositions.add(new Vector(polygonScopes.get(i).getVertex(selectedIndexes.get(i) + 1)));
					}
					else {
						dragPositions.add(new Vector(polygonScopes.get(i).getVertex(selectedIndexes.get(i))));
					}
				}
			}
			
			return true;
		}
		return false;
	}
	
	/** Clear the selection of vertices. **/
	public void clearSelection() {
		selectedIndexes.clear();
		polygonScopes.clear();
		selectedMidpoints = false;
	}
	
	/** Select all vertices in the map. **/
	public void selectAll() {
		clearSelection();
		for (Polygon p : map.walls) {
			for (int i = 0; i < p.vertexCount(); i++) {
				selectedIndexes.add(i);
				polygonScopes.add(p);
			}
		}
	}
	
	/** Delete the selected vertices from the polygons. **/
	public void deleteSelection() {
		if (!selectedMidpoints) {
			for (int i = 0; i < selectedIndexes.size(); i++) {
				Polygon p = polygonScopes.get(i);
				int index = selectedIndexes.get(i);
				
				if (p != null) {
					p.removeVertex(index);
					
    				if (p.vertexCount() < 2) {
    					// Delete the polygon entirely:
    					map.walls.remove(p);
    					// Move existing vertices down one index:
        				for (int j = i + 1; j < selectedIndexes.size(); j++) {
        					if (polygonScopes.get(j) == p)
        						polygonScopes.set(j, null);
        				}
    				}
    				else {
    					// Move existing vertices down one index:
        				for (int j = i + 1; j < selectedIndexes.size(); j++) {
        					if (p == polygonScopes.get(j) && selectedIndexes.get(j) > index)
        						selectedIndexes.set(j, selectedIndexes.get(j) - 1);
        				}
    				}
				}
			}
		}
		clearSelection();
	}
	
	/** Draw a movable vertex on the polygon. **/
	public void drawVertex(Polygon p, int index, boolean isMidpoint) {
		Draw.setColor(isMidpoint ? Color.YELLOW : Color.WHITE);
		
		if (vertexIsSelected(p, index, isMidpoint))
    		Draw.setColor(Color.RED);
		
		if (isMidpoint)
			editor.drawVertex(p.getEdge(index).getCenter(), 9);
		else
			editor.drawVertex(p.getVertex(index), 9);
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void initialize() {
		selectedIndexes.clear();
		polygonScopes.clear();
		dragPositions.clear();
		draggingSelectionBox = false;
		selectionBoxStart    = null;
		selectedMidpoints    = false;
		dragging             = false;
		dragIndex            = 0;
	}

	@Override
	public void terminate() {

	}

	@Override
	public boolean isBusy() {
		return (dragging || draggingSelectionBox);
	}
	

	@Override
	public void update() {
		
		
		if (draggingSelectionBox) {
			if (!Mouse.left.down()) {
				Rectangle area = new Rectangle(selectionBoxStart, mousePosition);
				draggingSelectionBox = false;
				boolean selectedAny  = false;
				
				for (int i = 0; i < map.walls.size(); i++) {
					Polygon p = map.walls.get(i);
					for (int j = 0; j < p.vertexCount(); j++) {
						boolean isSelected = vertexIsSelected(p, j, false);
						
						if (!isSelected && area.contains(p.getVertex(j))) {
							if (selectedMidpoints && !selectedAny) {
								selectedIndexes.clear();
								polygonScopes.clear();
								selectedMidpoints = false;
							}
							selectedAny = true;
		    				selectedIndexes.add(j);
		    				polygonScopes.add(p);
						}
					}
				}
			}
			else if (Keyboard.escape.pressed()) {
				// Cancel dragging:
				draggingSelectionBox = false;
			}
		}
		else if (!selectedIndexes.isEmpty()) {

			// Update dragging:
			if (dragging) {
				Mouse.setCursor(Cursor.CROSSHAIR_CURSOR);
				
				Vector from = dragPositions.get(dragIndex);
				if (selectedMidpoints)
					from = new Line(dragPositions.get(dragIndex), dragPositions.get(dragIndex + 1)).getCenter();

				Vector translation = new Vector();
				if (mousePosition.distanceTo(from) > mousePosition.distanceTo(mouseGridPosition))
					translation.set(from, mouseGridPosition);
				
				for (int i = 0; i < selectedIndexes.size(); i++) {
					int index = selectedIndexes.get(i);
					if (!selectedMidpoints) {
						polygonScopes.get(i).getVertex(index).set(dragPositions.get(i).plus(translation));
					}
					else {
						polygonScopes.get(i).getVertex(index).set(dragPositions.get(i * 2).plus(translation));
						polygonScopes.get(i).getVertex(index + 1).set(dragPositions.get((i * 2) + 1).plus(translation));
					}
				}
				
				if (!Mouse.left.down()) {
					// Stop dragging and check if there is a merge ready: TODO
					dragging = false;
//					if (!checkForMerge(selectIndex + 1))
//						checkForMerge(selectIndex - 1);
				}
				else if (Keyboard.escape.pressed()) {
					// Cancel dragging:
					dragging = false;
					for (int i = 0; i < selectedIndexes.size(); i++) {
						int index = selectedIndexes.get(i);
						if (!selectedMidpoints) {
							polygonScopes.get(i).getVertex(index).set(dragPositions.get(i));
						}
						else {
							polygonScopes.get(i).getVertex(index).set(dragPositions.get(i * 2));
							polygonScopes.get(i).getVertex(index + 1).set(dragPositions.get((i * 2) + 1));
						}
					}
				}
			}

			// Delete the vertices:
			if (Keyboard.delete.pressed())
				deleteSelection();
		}
		
		
		if (!dragging && !draggingSelectionBox) {
    		if (Keyboard.control.down()) {
    			// TODO
    			/*
    			// Create a new vertex on the polygon:
    			int insertIndex    = 0;
    			double minDistance = -1;
    			for (Polygon p : map.walls) {
    				for (int i = 0; i < p.edgeCount(); i++) {
    					Line edge = p.getEdge(i);
    					Vector v = edge.getClosestPoint(mousePosition);
    					double dist = v.distanceTo(mousePosition);
    					if (dist < 0.1 && (minDistance < 0 || dist < minDistance)) {
    						Mouse.setCursor(Cursor.CROSSHAIR_CURSOR);
    						if (Mouse.left.pressed()) {
        						minDistance  = dist;
        						selectVertex = v;
        						insertIndex  = i + 1;
        						polygonScope = p;
    						}
    					}
    				}
    			}
    			if (selectVertex != null && Mouse.left.pressed()) {
    				polygonScope.addVertex(insertIndex, selectVertex);
    				selectVertex = polygonScope.getVertex(insertIndex);
    				selectIndex  = insertIndex;
    				dragging     = true;
    				dragPosition.set(selectVertex);
    			}
    			*/
    		}
    		else {
    			// Check for selecting additional vertices:
    			boolean selected = false;
    			for (int i = 0; i < map.walls.size() && !selected; i++) {
    				Polygon p = map.walls.get(i);
    				for (int j = 0; j < p.vertexCount() && !selected; j++) {
    					if (checkVertexSelect(p, p.getVertex(j), j, false))
    						selected = true;
    					else if (j < p.edgeCount()) {
    						if (checkVertexSelect(p, p.getEdge(j).getCenter(), j, true))
    							selected = true;
    					}
    				}
    			}
    			if (!selected && Mouse.left.pressed()) {
    				if (!Keyboard.shift.down()) {
    					clearSelection();
    				}
    				draggingSelectionBox = true;
    				selectionBoxStart    = new Vector(mousePosition);
    			}
    		}
		}
	}

	@Override
	public void draw() {
		
		// Draw selected polygons:
		for (int i = 0; i < polygonScopes.size(); i++) {
			Draw.setColor(new Color(255, 0, 0));
			editor.drawPolygon(polygonScopes.get(i));
		}
		
		// Draw vertex handles:
		for (int i = map.walls.size() - 1; i >= 0; i--) {
			Polygon p = map.walls.get(i);
			for (int j = 0; j < p.vertexCount(); j++) {
				drawVertex(p, j, false);
				if (j < p.edgeCount())
					drawVertex(p, j, true);
			}
		}
		
		if (draggingSelectionBox) {
		    Draw.getGraphics().setStroke(Draw.STROKE_DASHED);
			Draw.setColor(Color.YELLOW);
			Draw.drawRect(new Rectangle(selectionBoxStart, mousePosition));
		}
	}
}
