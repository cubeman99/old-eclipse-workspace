package game;

import java.awt.Color;
import java.awt.Graphics;
import common.Point;
import common.Settings;
import common.Vector;
import common.graphics.Draw;
import common.graphics.Sprite;
import common.graphics.ViewControl;
import main.GameRunner;
import main.ImageLoader;
import main.Keyboard;
import main.Mouse;

public class Control {
	public GameRunner runner;
	public World world;
	public Player player;
	public Point mousePoint;
	private Point mousePointPrev;
	private ViewControl view;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new game controller. **/
	public Control(GameRunner runner) {
		this.runner         = runner;
		this.world          = new World(100, 80);
		this.player         = new Player(this);
		this.mousePoint     = new Point();
		this.mousePointPrev = new Point();
		
		this.view = new ViewControl();
		Draw.setView(view);
		Mouse.setView(view);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the game. **/
	public void update() {
		mousePointPrev.set(mousePoint);
		mousePoint.set(
			(int) Mouse.getVector().x,
			(int) Mouse.getVector().y
		);
		
		if (mousePoint.x > 0 && mousePoint.y > 0 && mousePoint.x < world.size.x && mousePoint.y < world.size.y) {
			if (Mouse.left.pressed() || (Mouse.left.down() && !mousePoint.equals(mousePointPrev))) {
				Sprite spr = ImageLoader.getSprite("testSheet", 0, !Keyboard.control.down() ? 0 : 1);
				world.place(mousePoint.x, mousePoint.y, spr, true);
			}
			if (Mouse.right.down()) {
				world.remove(mousePoint.x, mousePoint.y);
			}
		}
		
		player.update();
		world.update();
		
		// View follows player:
		Vector viewSize = new Vector(runner.getViewWidth(), runner.getViewHeight()).scaledByInv(Settings.DRAW_SCALE * Settings.TILE_SIZE);
		Vector viewGoal = player.getPosition().minus(viewSize.scaledByInv(2.0));
		Vector disp     = viewGoal.minus(view.pan);
		view.pan.add(disp.scaledByInv(6.0));
	}
	
	/** Draw the game. **/
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		world.draw();
		player.draw();
	}
}
