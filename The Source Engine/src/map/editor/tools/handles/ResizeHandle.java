package map.editor.tools.handles;

import java.awt.Color;
import java.awt.Cursor;
import map.editor.tools.ToolSelection;
import common.Draw;
import common.GMath;
import common.Vector;
import common.shape.Rectangle;
import common.transform.Scalation;
import common.transform.Transformation;


public class ResizeHandle extends TransformHandle {
	public static final double DRAW_OFFSET = 6;
	private Vector location;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new resize handle with a ratio of its position on the selection box. **/
	public ResizeHandle(ToolSelection tool, double locationX, double locationY) {
		super(tool);
		this.location  = new Vector(locationX, locationY);
		this.transform = new Scalation(new Vector(), new Vector(), new Vector());
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Get the sign of the handle's transformable components. **/
	private Vector getSign() {
		return new Vector(GMath.sign(location.x - 0.5), GMath.sign(location.y - 0.5));
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Update the transformation. **/
	public Transformation updateTransform(Rectangle selectionBox) {
		transform.setToPoint(tool.mouseGridPosition);
		double x = 8 / editor.control.viewControl.zoom;
		if (GMath.distance(transform.getFromPoint(), tool.mousePosition) < x)
			transform.setToPoint(transform.getFromPoint());
		return transform;
	}

	@Override
	/** Get the point on the selection box that this handle resizes. **/
	public Vector getPosition(Rectangle selectionBox) {
		return new Vector(
			selectionBox.x1() + (selectionBox.width()  * location.x),
			selectionBox.y1() + (selectionBox.height() * location.y)
		);
	}
	
	@Override
	/** Get the point this handle should be drawn at around a selection box. **/
	public Vector getDrawPosition(Rectangle selectionBox) {
		return getPosition(selectionBox);
		/*
		Vector point = getPosition(selectionBox);
		Vector sign  = getSign();
		double zoom  = editor.control.viewControl.zoom;
		
		return new Vector(
			point.x + ((DRAW_OFFSET / zoom) * sign.x),
			point.y + ((DRAW_OFFSET / zoom) * sign.y)
		);
		*/
	}
	
	@Override
	/** Get the point on the selection box for this handle's anchor point. **/
	public Vector getAnchorPoint(Rectangle selectionBox) {
		return new Vector(
    		selectionBox.x2() - (selectionBox.width()  * location.x),
    		selectionBox.y2() - (selectionBox.height() * location.y)
    	);
	}

	@Override
	/** Draw this handle. **/
	public void draw(Vector position) {
		Draw.setColor(Color.WHITE);
		editor.drawVertex(position, 9);
	}
	
	@Override
	/** Get the mouse cursor used for this handle. **/
	public Cursor getMouseCursor() {
		Vector sign = getSign();
		
		if (sign.x == 0 && sign.y < 0)
			return new Cursor(Cursor.N_RESIZE_CURSOR);
		if (sign.x == 0 && sign.y > 0)
			return new Cursor(Cursor.S_RESIZE_CURSOR);
		if (sign.y == 0 && sign.x < 0)
			return new Cursor(Cursor.W_RESIZE_CURSOR);
		if (sign.y == 0 && sign.x > 0)
			return new Cursor(Cursor.E_RESIZE_CURSOR);
		
		if (sign.x > 0 && sign.y < 0)
			return new Cursor(Cursor.NE_RESIZE_CURSOR);
		if (sign.x < 0 && sign.y > 0)
			return new Cursor(Cursor.SW_RESIZE_CURSOR);
		if (sign.x < 0 && sign.y < 0)
			return new Cursor(Cursor.NW_RESIZE_CURSOR);
		
		return new Cursor(Cursor.SE_RESIZE_CURSOR);
	}
}
