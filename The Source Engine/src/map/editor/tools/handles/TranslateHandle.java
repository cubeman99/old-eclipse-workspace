package map.editor.tools.handles;

import java.awt.Cursor;
import main.Keyboard;
import map.editor.tools.ToolSelection;
import common.GMath;
import common.Vector;
import common.shape.Rectangle;
import common.transform.Transformation;
import common.transform.Translation;


public class TranslateHandle extends TransformHandle {
	private boolean alignRight;
	private boolean alignDown;
	private Vector dragTransformPoint;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new translate handle. **/
	public TranslateHandle(ToolSelection tool) {
		super(tool);
		this.transform   = new Translation(new Vector());
		this.alignRight  = false;
		this.alignDown   = false;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Check if the mouse is hovering over this handle. **/
	public boolean checkMouseOver(Rectangle selectionBox) {
		return selectionBox.contains(tool.mousePosition);
	}
	
	@Override
	/** Setup this handle's transformation to start being dragged. **/
	public void startDragging(Rectangle selectionBox) {
		transform.setFromPoint(selectionBox.end1);
		transform.setToPoint(selectionBox.end1);
		transform.setAnchorPoint(selectionBox.end1);
		
		dragTransformPoint = new Vector(tool.mousePosition);
		Vector center      = selectionBox.getBounds().getCenter();
		alignRight         = (tool.mousePosition.x > center.x);
		alignDown          = (tool.mousePosition.y > center.y);
		Vector offset      = new Vector();
		
		offset.x = GMath.getWrappedValue((alignRight ? selectionBox.end2.x : selectionBox.end1.x), editor.gridScale);
		if (offset.x > 0 && alignRight)
			offset.x -= editor.gridScale;
		offset.y = GMath.getWrappedValue((alignDown  ? selectionBox.end2.y : selectionBox.end1.y), editor.gridScale);
		if (offset.y > 0 && alignDown)
			offset.y -= editor.gridScale;
		
		transform.setFromPoint(selectionBox.end1.plus(offset));
	}
	

	@Override
	/** Update the transformation. **/
	public Transformation updateTransform(Rectangle selectionBox) {
		Vector rectOffset     = dragTransformPoint.minus(selectionBox.end1);
		Vector dragGridOffset = new Vector(
			GMath.getWrappedValue(dragTransformPoint.x, editor.gridScale),
			GMath.getWrappedValue(dragTransformPoint.y, editor.gridScale)
		);
		
		Vector snappedPoint = tool.mousePosition;
		if (editor.snapToGrid())
			snappedPoint = tool.mousePosition.getSnappedToGrid(dragGridOffset, editor.gridScale);
		transform.setToPoint(snappedPoint.minus(rectOffset));
		
		if (Keyboard.alt.down())
			transform.setToPoint(transform.getFromPoint().plus(tool.mousePosition.minus(dragTransformPoint)));

		double x = 8 / editor.control.viewControl.zoom;
		if (GMath.distance(tool.mousePosition, dragTransformPoint) < x)
			transform.setToPoint(transform.getFromPoint());
		
		return transform;
	}

	@Override
	/** Get the point on the selection box that this handle resizes. **/
	public Vector getPosition(Rectangle selectionBox) {
		return new Vector();
	}
	
	@Override
	/** Get the point this handle should be drawn at around a selection box. **/
	public Vector getDrawPosition(Rectangle selectionBox) {
		return new Vector();
	}
	
	@Override
	/** Get the point on the selection box for this handle's anchor point. **/
	public Vector getAnchorPoint(Rectangle selectionBox) {
		return new Vector();
	}

	@Override
	/** Draw this handle. **/
	public void draw(Vector position) {
		// (Handle is not visible)
	}
	
	@Override
	/** Get the mouse cursor used for this handle. **/
	public Cursor getMouseCursor() {
		return new Cursor(Cursor.MOVE_CURSOR);
	}
}
