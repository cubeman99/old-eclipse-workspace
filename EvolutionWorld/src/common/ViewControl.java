package common;

import environment.World;
import game.Game;
import game.Keyboard;
import game.Mouse;

public class ViewControl {
	private static final double PAN_SPEED   =  10.0;
	private static final double ZOOM_AMOUNT =   1.1;
	private static final double ZOOM_MIN    =   3.0;
	private static final double ZOOM_MAX    = 200.0;
	
	public World world;
	public Vector pan;
	public double zoom;
	
	
	public ViewControl(World world) {
		this.world = world;
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
	
	public void zoomFocus(Vector focusPoint, double newZoom) {
		Vector focus = new Vector(pan.x + (focusPoint.x / zoom), pan.y + (focusPoint.y / zoom));
		zoom = newZoom;
		pan.set(focus.x - (focusPoint.x / zoom), focus.y - (focusPoint.y / zoom));
	}
	
	public void zoomFollow(Vector followPoint) {
		pan.set(followPoint.x - ((Game.VIEW_WIDTH * 0.5) / zoom), followPoint.y - ((Game.VIEW_HEIGHT * 0.5) / zoom));
	}
	
	public void fitToScreen() {
		pan.zero();
		zoom = GMath.min(Game.VIEW_WIDTH / world.size.x, Game.VIEW_HEIGHT / world.size.y);
	}
	
	public void updateControls() {
		Vector ms = Mouse.getVector();
		
		// Pan the view:
		if (Keyboard.viewRight.down())
			pan.x += PAN_SPEED / zoom;
		if (Keyboard.viewLeft.down())
			pan.x -= PAN_SPEED / zoom;
		if (Keyboard.viewDown.down())
			pan.y += PAN_SPEED / zoom;
		if (Keyboard.viewUp.down())
			pan.y -= PAN_SPEED / zoom;
		
		// Zoom in and out:
		if (Mouse.wheelUp() && zoom < ZOOM_MAX) {
			zoomFocus(ms, zoom * ZOOM_AMOUNT);
		}
		if (Mouse.wheelDown() && zoom > ZOOM_MIN + 0.00001) {
			zoomFocus(ms, zoom / ZOOM_AMOUNT);
		}
	}
}
