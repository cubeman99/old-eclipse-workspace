


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TeamControl extends Entity {
	//////////////////////////////////
	//   STATIC Members/Functions   //
	//////////////////////////////////
	public static ArrayList<TeamControl> teams = new ArrayList<TeamControl>();
	
    public static void initializeTeams() {
		teams.add(new TeamControl(Color.gray));
		teams.add(new TeamControl(Color.green));
		teams.add(new TeamControl(Color.blue));
		teams.add(new TeamControl(Color.red));
		teams.add(new TeamControl(Color.yellow));
		teams.add(new TeamControl(Color.magenta));
		teams.add(new TeamControl(Color.cyan));
		teams.add(new TeamControl(Color.orange));
		teams.add(new TeamControl(Color.pink));
		teams.add(new TeamControl(Color.lightGray));
	}
	
	public static TeamControl getTeam(int index) {
		return teams.get(index);
	}
	
	public static Color getTeamColor(int index) {
		return teams.get(index).color;
	}
	
	//////////////////////////////////
	//  SPECIFIC Members/Functions  //
	//////////////////////////////////
	public Color color = Color.white;
	public ArrayList<SpawnTile> spawnTiles = new ArrayList<SpawnTile>();
	
	public TeamControl(Color col) {
		this.color = col;
	}
	
	public Vector pickSpawnPosition() {
		int index = Game.random.nextInt(spawnTiles.size());
		return spawnTiles.get(index).getCenter();
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		
	}
}
