package map.editor.tools.handles;

import java.awt.Cursor;
import common.Vector;
import common.shape.Rectangle;
import common.transform.Transformation;
import map.editor.MapEditor;
import map.editor.tools.Tool;
import map.editor.tools.ToolSelection;


public abstract class TransformHandle {
	protected Transformation transform;
	protected MapEditor editor;
	protected ToolSelection tool;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Setup the editor and selection tool links. **/
	public TransformHandle(ToolSelection tool) {
		this.editor = tool.editor;
		this.tool   = tool;
	}
	
	// =============== ABSTRACT METHODS =============== //
	
	/** Update the transformation. **/
	public abstract Transformation updateTransform(Rectangle selectionBox);
	
	/** Get the position this handle is attached to. **/
	public abstract Vector getPosition(Rectangle selectionBox);
	
	/** Get the position this handle is drawn at. **/
	public abstract Vector getDrawPosition(Rectangle selectionBox);
	
	/** Get the anchor point of the transformation based on the selection box. **/
	public abstract Vector getAnchorPoint(Rectangle selectionBox);

	/** Get the mouse cursor used for this handle. **/
	public abstract Cursor getMouseCursor();
	
	/** Draw this handle. **/
	public abstract void draw(Vector position);
	
	
	
	// ============== OVERRIDABLE METHODS ============== //
	
	/** Check if the mouse is hovering over this handle. **/
	public boolean checkMouseOver(Rectangle selectionBox) {
		double zoom = editor.control.viewControl.zoom;
		return (tool.mousePosition.distanceTo(getDrawPosition(selectionBox)) < Tool.DISTANCE_SELECT_HANDLE / zoom);
	}
	
	/** Setup this handle's transformation to start being dragged. **/
	public void startDragging(Rectangle selectionBox) {
		Vector position = getPosition(selectionBox);
		
		transform.setFromPoint(position);
		transform.setToPoint(position);
		transform.setAnchorPoint(getAnchorPoint(selectionBox));
	}
	
	/** Draw the handle with the given transformation (if it isn't null). **/
	public void draw(Rectangle selectionBox, Transformation t) {
		if (t != null)
			draw((Vector) getDrawPosition(selectionBox).getTransformed(t));
		else
			draw(getDrawPosition(selectionBox));
	}
	
	
	
	// ================ FINAL ACCESSORS ================ //
	
	/** Return the transformation that this handle gives. **/
	public final Transformation getTransformation() {
		return transform;
	}
	
	
	
	// ================ FINAL MUTATORS ================ //
	
	/** Set the anchor point of the transformation. **/
	public final void setAnchorPoint(Vector anchorPoint) {
		transform.setAnchorPoint(anchorPoint);
	}
	
	/** Set the 'from' point of the transformation. **/
	public final void setFromPoint(Vector fromPoint) {
		transform.setFromPoint(fromPoint);
	}
	
	/** Set the 'to' point of the transformation. **/
	public final void setToPoint(Vector toPoint) {
		transform.setToPoint(toPoint);
	}
}