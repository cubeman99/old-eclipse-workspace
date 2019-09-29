package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import common.Point;
import common.graphics.Draw;
import common.graphics.ViewControl;
import main.GameRunner;
import main.Keyboard;
import main.Mouse;

public class Control {
	public GameRunner runner;
	public World world;
	public Point mousePoint;
	private Point mousePointPrev;
	private ViewControl view;
	private ArrayList<Car> cars;
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new game controller. **/
	public Control(GameRunner runner) {
		this.runner         = runner;
		this.world          = new World(100, 80);
		this.mousePoint     = new Point();
		this.mousePointPrev = new Point();
		this.cars           = new ArrayList<Car>();
		
		this.view = new ViewControl();
		this.view.setZoom(32);
		Draw.setView(view);
		Mouse.setView(view);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the game. **/
	public void update() {
		mousePointPrev.set(mousePoint);
		mousePoint.set(
			(int) Mouse.getVector(view).x,
			(int) Mouse.getVector(view).y
		);
		
		if (mousePoint.x > 0 && mousePoint.y > 0 && mousePoint.x < world.size.x && mousePoint.y < world.size.y) {
			if (Mouse.left.pressed() || (Mouse.left.down() && !mousePoint.equals(mousePointPrev))) {
				world.place(mousePoint.x, mousePoint.y);
				world.connectArea(mousePoint.x, mousePoint.y);
			}
			if (Mouse.right.down()) {
				world.remove(mousePoint.x, mousePoint.y);
				world.connectArea(mousePoint.x, mousePoint.y);
			}
			
			if (Keyboard.space.pressed()) {
				Road rd = world.get(mousePoint.x, mousePoint.y);
				if (rd != null)
					cars.add(new Car(rd));
			}
		}
		
		world.update();
		for (Car c : cars)
			c.update();
		
		view.updateViewControls();
		Draw.setView(view);
	}
	
	/** Draw the game. **/
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		world.draw();
		for (Car c : cars)
			c.draw();
	}
}
