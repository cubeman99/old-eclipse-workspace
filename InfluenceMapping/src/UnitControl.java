

import java.awt.Graphics;
import java.util.ArrayList;

public class UnitControl extends Entity {
	public ArrayList<Unit> units = new ArrayList<Unit>();
	public Player player;
	//public ArrayList<Color> teamColors = new ArrayList<Color>();
	public ArrayList<TeamControl> teamControl = new ArrayList<TeamControl>();
	
	public int respawnTime = 5;
	public boolean friendlyFire = false;
	
	public UnitControl() {
		
        player = new Player(1, 64, 64);
        addUnit(player);
        
        int playersPerTeam = 2;
        int teamCount = 4;

        for (int ti = 1; ti <= teamCount; ti++) {
        	for (int i = GMath.bool(ti == 1); i < playersPerTeam; i++) {
        		addUnit(new Bot(ti));
        	}
        }
	}

	public void addUnit(Unit u) {
		units.add(u);
	}
	
	public void update() {
		
		// Update all Units
		for (Unit u : units) {
			u.preUpdate();
			u.update();
			u.postUpdate();
		}
	}
	
	public void draw(Graphics g) {
		
		// Draw all Units
		for (Unit u : units) {
			u.draw(g);
		}
	}
}
