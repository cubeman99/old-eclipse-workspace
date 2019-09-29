package game;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.math.geometry.Rectangle;


public class SlimeBall {
	protected GameRunner runner;
	protected Rectangle gameRect;
	protected ArrayList<Slime> slimes;
	protected Ball ball;
	protected Team[] teams;
	protected Slime player;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public SlimeBall() {
		slimes = new ArrayList<Slime>();
		
		
		gameRect = new Rectangle(0, 0, Settings.VIEW_WIDTH, Settings.VIEW_HEIGHT);
		
		ball = new Ball(this);
		
		
		teams = new Team[] {
    		new Team(0, Color.RED, 0, (Settings.VIEW_WIDTH / 2) - 4),
    		new Team(1, Color.GREEN, (Settings.VIEW_WIDTH / 2) + 4, Settings.VIEW_WIDTH)
		};
		
//		slimes.add(new Slime(this, "David", teams[0], Color.RED));
//		slimes.add(new Slime(this, "Robert", teams[1], Color.GREEN));
//		player = slimes.get(0);
	}

	

	// =================== ACCESSORS =================== //

	public Ball getBall() {
		return ball;
	}
	
	public Rectangle getGameRect() {
		return gameRect;
	}
	
	public ArrayList<Slime> getSlimes() {
		return slimes;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setRunner(GameRunner runner) {
		this.runner = runner;
	}
	
	public void update() {
		ball.update();
	}
	
	public void draw() {
		// Draw the sky and ground:
		Draw.setColor(Color.BLUE);
		Draw.fill(gameRect);
		Draw.setColor(Color.GRAY);
		Draw.fillRect(0, Settings.FLOOR_Y, gameRect.getWidth(), gameRect.getHeight() - Settings.FLOOR_Y);
		
		Draw.setColor(Color.WHITE);
		Rectangle net = new Rectangle(
			(gameRect.getWidth() / 2) - (Settings.NET_WIDTH / 2),
			Settings.FLOOR_Y - Settings.NET_HEIGHT,
			Settings.NET_WIDTH, Settings.NET_HEIGHT + 1
		);
		Draw.fill(net);
		
		
		for (int i = 0; i < slimes.size(); i++) {
			slimes.get(i).draw();
		}
		
		ball.draw();
	}
}
