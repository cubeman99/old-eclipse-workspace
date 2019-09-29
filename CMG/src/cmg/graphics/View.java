package cmg.graphics;

import cmg.math.GMath;
import cmg.math.geometry.Vector;

public class View {
	private Vector pan;
	private double zoom;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public View() {
		pan  = new Vector();
		zoom = 1;
	}
	
	public View(Vector pan) {
		this.pan = new Vector(pan);
	}
	
	public View(Vector pan, double zoom) {
		this.pan  = new Vector(pan);
		this.zoom = zoom;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Vector getPan() {
		return pan;
	}
	
	public double getZoom() {
		return zoom;
	}

	/** Get the x position on the screen. **/
	public int dx(double x) {
		return GMath.floor((x - pan.x) * zoom);
	}
	
	/** Get the y position on the screen. **/
	public int dy(double y) {
		return GMath.floor((y - pan.y) * zoom);
	}
	
	/** Get a magnitude scaled by the zoom. **/
	public int z(double a) {
		return GMath.floor(a * zoom);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void set(View v) {
		this.pan.set(v.pan);
		this.zoom = v.zoom;
	}
	
	public void set(double panX, double panY) {
		pan.set(panX, panY);
	}
	
	public void set(double panX, double panY, double zoom) {
		pan.set(panX, panY);
		this.zoom = zoom;
	}
	
	public void set(Vector pan) {
		this.pan.set(pan);
	}
	
	public void set(Vector pan, double zoom) {
		this.pan.set(pan);
		this.zoom = zoom;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
}
