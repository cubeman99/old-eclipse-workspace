package projects.towerDefense;

import java.awt.Color;
import java.awt.Graphics2D;
import projects.towerDefense.panel.Panel;
import projects.towerDefense.tile.Tile;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.geometry.Point;

public class GameInstance {
	private GameRunner runner;
	private Level level;
	private Panel[] panels;
	public Panel panelInfo;
	public Panel panelToolBar;
	public Panel panelTowerPalette;
	public Panel panelWaveList;
	public Panel panelCreepTimer;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public GameInstance(GameRunner runner) {
		Resources.initialize();
		
		this.runner = runner;
		this.level  = new Level(this, 10, 10);
		
		panels = new Panel[] {
			panelToolBar      = new Panel(this),
			panelInfo         = new Panel(this),
			level,
			panelTowerPalette = new Panel(this),
			panelWaveList     = new Panel(this),
			panelCreepTimer   = new Panel(this)
		};
		
		panelToolBar     .setup(0, 0, 900, 48);
		panelInfo        .setup(0, 48, 200, 502);
		level            .setup(200, 80, 500, 470);
		panelTowerPalette.setup(0, 550, 900, 150);
		panelWaveList    .setup(700, 48, 200, 502);
		panelCreepTimer  .setup(200, 48, 500, 32);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Level getLevel() {
		return level;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void update() {
		Point ms = new Point(Mouse.getVector().scaledByInv(Tile.SIZE));
		
		// Update the level.
		level.update();
	}
	
	public void draw() {
		// Draw a background.
		Draw.setColor(Color.WHITE);
		if (runner != null)
			Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		Graphics2D g = Draw.getGraphics();
				
		for (int i = 0; i < panels.length; i++) {
			panels[i].draw();
			Point pos = panels[i].getCanvasPosition();
			g.drawImage(panels[i].getBuffer(), pos.x, pos.y, null);
		}
		
		// Draw the level.
//		level.draw();
	}
}
