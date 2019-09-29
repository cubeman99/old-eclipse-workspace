package common;

import main.Keyboard;
import main.Mouse;
import mainOLD.Game;


public class ViewControl {
	private static final double PAN_SPEED   =   10.0;
	private static final double ZOOM_AMOUNT =    1.1;
	private static final double ZOOM_MIN    =    3.0;
	private static final double ZOOM_MAX    = 8000.0;
	
	public Vector pan;
	public double zoom;
	
	
	public ViewControl() {
		this.pan   = new Vector();
		this.zoom  = 70.0;
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
	
	public void zoomFocus(Vector focusPoint, double newZoom) {
		Vector focus = new Vector(pan.x + (focusPoint.x / zoom), pan.y + (focusPoint.y / zoom));
		zoom = newZoom;
		pan.set(focus.x - (focusPoint.x / zoom), focus.y - (focusPoint.y / zoom));
	}
	
	public void zoomFollow(Vector followPoint) {
		pan.set(followPoint.x - ((Game.getViewWidth() * 0.5) / zoom), followPoint.y - ((Game.getViewHeight() * 0.5) / zoom));
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
			pan.x += PAN_SPEED / zoom;
		if (Keyboard.left.down())
			pan.x -= PAN_SPEED / zoom;
		if (Keyboard.down.down())
			pan.y += PAN_SPEED / zoom;
		if (Keyboard.up.down())
			pan.y -= PAN_SPEED / zoom;
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
		if (Mouse.wheelUp() && zoom < ZOOM_MAX) {
			zoomFocus(ms, zoom * ZOOM_AMOUNT);
		}
		if (Mouse.wheelDown() && zoom > ZOOM_MIN + 0.00001) {
			zoomFocus(ms, zoom / ZOOM_AMOUNT);
		}
	}
}
