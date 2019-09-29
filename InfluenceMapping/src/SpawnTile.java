

import java.awt.Color;
import java.awt.Graphics;

public class SpawnTile extends MapTile {
	public int team = 0;
	
	public SpawnTile(int xx, int yy, int team) {
		super(xx, yy);
		isSolid = false;
		this.team = team;
		
		TeamControl.getTeam(team).spawnTiles.add(this);
	}
	
	public void onDestroy() {
		TeamControl.getTeam(team).spawnTiles.remove(this);
	}
	
	public Vector getCenter() {
		return new Vector(size * (x + 0.5), size * (y + 0.5));
	}
	
	public void draw(Graphics g) {
		Color c = TeamControl.getTeamColor(team);
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 70));
		drawTileSquare(g);
	}
}
