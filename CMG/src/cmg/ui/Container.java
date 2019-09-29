package cmg.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.math.geometry.Point;

public class Container extends Component {
	private ArrayList<Component> components;
	private Point mousePosition;
	private BufferedImage canvas;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Container() {
		super();
		components    = new ArrayList<Component>();
		mousePosition = new Point();
		canvas        = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public BufferedImage getCanvas() {
		return canvas;
	}
	
	public Graphics getGraphics() {
		return canvas.getGraphics();
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public void add(Component c) {
		components.add(c);
	}
	
	public void clear() {
		components.clear();
	}
	
	protected void renderComponents() {
		for (int i = 0; i < components.size(); i++) {
			Draw.setGraphics(canvas.getGraphics());
			components.get(i).render();
		}
	}
	
	protected void displayCanvas() {
		if (container != null) {
    		container.getGraphics().drawImage(canvas,
    				getPosition().x, getPosition().y, null);
		}
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void step() {
		super.step();
		if (canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
			canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		for (int i = 0; i < components.size(); i++) {
			components.get(i).step();
		}
	}
	
	@Override
	public void render() {
		Draw.setGraphics(canvas.getGraphics());
		draw();
		renderComponents();
		displayCanvas();
	}
}
