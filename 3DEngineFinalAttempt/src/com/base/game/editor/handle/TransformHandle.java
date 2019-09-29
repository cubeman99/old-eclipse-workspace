package com.base.game.editor.handle;

import java.awt.Cursor;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.game.editor.Editor;

public abstract class TransformHandle {
//	
//	protected Transformation transform;
//	protected Editor editor;
//	protected ToolSelection tool;
//	
//	
//	// ================== CONSTRUCTORS ================== //
//	
//	/** Setup the editor and selection tool links. **/
//	public TransformHandle(ToolSelection tool) {
//		this.editor = tool.editor;
//		this.tool   = tool;
//	}
//	
//	// =============== ABSTRACT METHODS =============== //
//	
//	/** Update the transformation. **/
//	public abstract Transformation updateTransform(Rect2f selectionBox);
//	
//	/** Get the position this handle is attached to. **/
//	public abstract Vector2f getPosition(Rect2f selectionBox);
//	
//	/** Get the position this handle is drawn at. **/
//	public abstract Vector2f getDrawPosition(Rect2f selectionBox);
//	
//	/** Get the anchor point of the transformation based on the selection box. **/
//	public abstract Vector2f getAnchorPoint(Rect2f selectionBox);
//
//	/** Get the mouse cursor used for this handle. **/
//	public abstract Cursor getMouseCursor();
//	
//	/** Draw this handle. **/
//	public abstract void draw(Vector2f position);
//	
//	
//	
//	// ============== OVERRIDABLE METHODS ============== //
//	
//	/** Check if the mouse is hovering over this handle. **/
//	public boolean checkMouseOver(Rect2f selectionBox) {
//		double zoom = editor.control.viewControl.zoom;
//		return (tool.mousePosition.distanceTo(getDrawPosition(selectionBox)) < Tool.DISTANCE_SELECT_HANDLE / zoom);
//	}
//	
//	/** Setup this handle's transformation to start being dragged. **/
//	public void startDragging(Rect2f selectionBox) {
//		Vector2f position = getPosition(selectionBox);
//		
//		transform.setFromPoint(position);
//		transform.setToPoint(position);
//		transform.setAnchorPoint(getAnchorPoint(selectionBox));
//	}
//	
//	/** Draw the handle with the given transformation (if it isn't null). **/
//	public void draw(Rect2f selectionBox, Transformation t) {
//		if (t != null)
//			draw((Vector2f) getDrawPosition(selectionBox).getTransformed(t));
//		else
//			draw(getDrawPosition(selectionBox));
//	}
//	
//	
//	
//	// ================ FINAL ACCESSORS ================ //
//	
//	/** Return the transformation that this handle gives. **/
//	public final Transformation getTransformation() {
//		return transform;
//	}
//	
//	
//	
//	// ================ FINAL MUTATORS ================ //
//	
//	/** Set the anchor point of the transformation. **/
//	public final void setAnchorPoint(Vector anchorPoint) {
//		transform.setAnchorPoint(anchorPoint);
//	}
//	
//	/** Set the 'from' point of the transformation. **/
//	public final void setFromPoint(Vector fromPoint) {
//		transform.setFromPoint(fromPoint);
//	}
//	
//	/** Set the 'to' point of the transformation. **/
//	public final void setToPoint(Vector toPoint) {
//		transform.setToPoint(toPoint);
//	}
}
