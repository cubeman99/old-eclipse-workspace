package tp.common;

import java.awt.Dimension;
import tp.common.Vector;
import tp.main.Keyboard;
import tp.main.Mouse;


public class ViewControl {
	private double panSpeed   =   10.0;
	private double zoomAmount =    1.1;
	private double zoomMin    =    1.0;
	private double zoomMax    = 8000.0;
	
	public static Dimension viewSize;
	public Vector pan;
	public double zoom;
	
	
	public ViewControl() {
		this.viewSize = new Dimension(400, 400);
		this.pan   = new Vector();
		this.zoom  = 1.0;
		this.fitToScreen();
	}
	
	public void setPan(double x, double y) {
		pan.set(x, y);
	}
	
	public void setPan(Vector pan) {
		this.pan.set(pan);
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public void setZoomMin(double zoomMin) {
		this.zoomMin = zoomMin;
	}
	
	public void setZoomMax(double zoomMax) {
		this.zoomMax = zoomMax;
	}
	
	public void zoomFocus(Vector focusPoint, double newZoom) {
		Vector focus = new Vector(pan.x + (focusPoint.x / zoom), pan.y + (focusPoint.y / zoom));
		zoom = newZoom;
		pan.set(focus.x - (focusPoint.x / zoom), focus.y - (focusPoint.y / zoom));
	}
	
	public void zoomFollow(Vector followPoint) {
		pan.set(followPoint.x - (((double) viewSize.width * 0.5) / zoom), followPoint.y - (((double) viewSize.height * 0.5) / zoom));
	}
	
	public void fitToScreen() {
		pan.zero();
		//zoom = GMath.min(Game.viewSize.x / world.size.x, Game.viewSize.y / world.size.y);
	}
	
	public Vector getGamePoint(Vector viewPoint) {
		return new Vector(pan.x + (viewPoint.x / zoom), pan.y + (viewPoint.y / zoom));
	}
	
	public Vector getViewPoint(Vector gamePoint) {
		return new Vector((gamePoint.x - pan.x) * zoom, (gamePoint.y - pan.y) * zoom);
	}
	
	public void updateViewControls() {
		updateMousePanControls();
		updateZoomControls();
	}
	
	public void updatePanControls() {
		// Pan the view:
		if (Keyboard.right.down())
			pan.x += panSpeed / zoom;
		if (Keyboard.left.down())
			pan.x -= panSpeed / zoom;
		if (Keyboard.down.down())
			pan.y += panSpeed / zoom;
		if (Keyboard.up.down())
			pan.y -= panSpeed / zoom;
	}
	
	public void updateMousePanControls() {
		// Pan the view:
		if (Mouse.right.down()) {
			pan.add(Mouse.getVectorPrevious().minus(Mouse.getVector()).scale(1.0 / zoom));
		}
	}
	
	public void updateZoomControls() {
		Vector ms = Mouse.getVector();
		
		// Zoom in and out:
		if (Mouse.wheelUp() && (zoom < zoomMax || zoomMax < 0)) {
			zoomFocus(ms, zoom * zoomAmount);
		}
		if (Mouse.wheelDown() && (zoom > zoomMin + 0.0000001 || zoomMin < 0)) {
			zoomFocus(ms, zoom / zoomAmount);
		}
	}
}
