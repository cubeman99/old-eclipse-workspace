package map.editor.tools.handles;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import main.ImageLoader;
import main.Keyboard;
import main.Mouse;
import map.editor.tools.ToolSelection;
import common.Draw;
import common.GMath;
import common.HUD;
import common.Vector;
import common.shape.Rectangle;
import common.transform.Rotation;
import common.transform.Scalation;
import common.transform.Transformation;


public class RotateHandle extends TransformHandle {
	public static final double DRAW_OFFSET = 12;
	private Vector location;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new resize handle with a ratio of its position on the selection box. **/
	public RotateHandle(ToolSelection tool, double locationX, double locationY) {
		super(tool);
		this.location  = new Vector(locationX, locationY);
		this.transform = new Rotation(new Vector(), new Vector(), new Vector());
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Get the sign of the handle's transformable components. **/
	private Vector getSign() {
		return new Vector(GMath.sign(location.x - 0.5), GMath.sign(location.y - 0.5));
	}
	
	/** Get the point this handle should be drawn at around a selection box. **/
	private Vector getDrawPosition(Rectangle selectionBox, Transformation t) {
		Vector point  = (Vector) getPosition(selectionBox).getTransformed(t);
		Vector sign   = getSign();
		double zoom  = editor.control.viewControl.zoom;
		Vector offset = sign.scale(DRAW_OFFSET / zoom);
		
		if (t instanceof Rotation) {
    		double theta  = ((Rotation) t).getTheta();
    		offset.setDirection(offset.direction() + theta);
		}
		
		return point.plus(offset);
	}
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Draw the handle with the given transformation (if it isn't null). **/
	public void draw(Rectangle selectionBox, Transformation t) {
		if (t != null)
			draw(getDrawPosition(selectionBox, t));
		else
			draw(getDrawPosition(selectionBox));
	}
	
	
	@Override
	/** Update the transformation. **/
	public Transformation updateTransform(Rectangle selectionBox) {
		transform.setToPoint(tool.mousePosition);
		
		// snap to 15 degree intervals
		if (!Keyboard.alt.down())
			((Rotation) transform).snapAngle(GMath.toRadians(15));
		
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
		Vector point  = getPosition(selectionBox);
		Vector sign   = getSign();
		double zoom   = editor.control.viewControl.zoom;
		Vector offset = sign.scale(DRAW_OFFSET / zoom);
		
		return point.plus(offset);
	}
	
	@Override
	/** Get the point on the selection box for this handle's anchor point. **/
	public Vector getAnchorPoint(Rectangle selectionBox) {
		return selectionBox.getCenter();
	}

	@Override
	/** Draw this handle. **/
	public void draw(Vector position) {
		if (tool.transforming && !(tool.currentHandle instanceof RotateHandle))
			return;
		
		Draw.setColor(Color.GREEN);
		Vector dv       = editor.map.getViewPoint(position);
		double distance = GMath.max(0, GMath.distance(Mouse.getVector(), dv) - 56);
		float alpha     = (float) GMath.max(0, 1.0 - (distance / 24.0));
		
		if (tool.transforming && tool.currentHandle instanceof RotateHandle)
			alpha = 1.0f;
		
		// Draw the handle image with an alpha value:
		Image img = ImageLoader.getImage("handleRotate");
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		bi.getGraphics().drawImage(img, 0, 0, null);
		float[] scales  = {1, 1, 1, alpha};
		RescaleOp rop   = new RescaleOp(scales, new float[4], null);
		HUD.getGraphics().drawImage(bi, rop, (int) dv.x - 4, (int) dv.y - 4);
	}
	
	@Override
	/** Get the mouse cursor used for this handle. **/
	public Cursor getMouseCursor() {
		return new Cursor(Cursor.CROSSHAIR_CURSOR);
	}
}
