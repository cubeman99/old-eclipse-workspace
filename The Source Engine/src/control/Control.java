package control;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import main.Keyboard;
import main.Mouse;
import map.Map;
import common.Draw;
import common.HUD;
import common.Vector;
import common.ViewControl;
import dynamics.PhysicsWorld;
import entity.Entity;
import entity.unit.Bot;
import entity.unit.Player;



public class Control {
	public ArrayList<Entity> entities;
	public ArrayList<Entity> depthList;
	public ArrayList<Team> teams;
	public ViewControl viewControl;
	public Map map;
//	public MapEditor editor;
	
	public PhysicsWorld physics;
	
	public Player player;
	public Team teamZombies;
	public Team teamAllies;
	
	
	public Control() {
		entities    = new ArrayList<Entity>();
		depthList   = new ArrayList<Entity>();
		teams       = new ArrayList<Team>();
		viewControl = new ViewControl();
		map         = new Map(this, new Vector(100, 100));
//		editor      = new MapEditor(map);
		

		physics     = new PhysicsWorld();
		/*
		player = new Player(this, teamAllies, new Vector(1, 1));
		teamAllies.addUnit(player);
		
		for (int i = 0; i < 8; i++)
			teamZombies.addUnit(new Bot(this, teamZombies, new Vector(5, 3)));
		for (int i = 0; i < 7; i++)
			teamAllies.addUnit(new Bot(this, teamAllies, new Vector(10, 3)));
		
		for (int i = 0; i < 0; i++)
			addEntity(new TestPoly(this));
		*/
		
//		addEntity(teamAllies);
//		addEntity(teamZombies);
//		map.loadFromFile("maps/testmap1.txt");
//		addEntity(editor);
	}
	
	public void update() {
		
		viewControl.updateViewControls();
		map.update();
		
		if (Keyboard.backspace.pressed()) {
			clearAllEntities();
//			addEntity(editor);
		}
		
		
		// Update all entities:
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isDestroyed()) {
				removeDepthEntity(e);
				entities.remove(i);
			}
		}
		
		physics.solve();
	}
	
	public void testRun() {
		physics     = new PhysicsWorld();
		teamZombies = new Team(this, "Zombies", Color.BLUE);
		teamAllies  = new Team(this, "Allies", Color.RED);
		
		teams.add(teamAllies);
		teams.add(teamZombies);
		addEntity(teamZombies);
		addEntity(teamAllies);
		
		player = new Player(this, teamAllies, viewControl.getGamePoint(Mouse.getVector()));
		teamAllies.addUnit(player);
		
		for (int i = 0; i < 4; i++)
			teamZombies.addUnit(new Bot(this, teamZombies, new Vector(5, 3)));
		for (int i = 0; i < 3; i++)
			teamAllies.addUnit(new Bot(this, teamAllies, new Vector(10, 3)));
		
		map.createPhysicsBodies();
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		addDepthEntity(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
		removeDepthEntity(e);
	}
	
	public void clearAllEntities() {
		entities.clear();
		depthList.clear();
	}
	
	public void addDepthEntity(Entity e) {
		for (int i = 0; i < depthList.size(); i++) {
			if (e.getDepth() <= depthList.get(i).getDepth()) {
				depthList.add(i, e);
				return;
			}
		}
		depthList.add(e);
	}
	
	public void removeDepthEntity(Entity e) {
		depthList.remove(e);
	}
	
	public void draw(Graphics g) {
		HUD.setGraphics(g);
		Draw.setGraphics(g);
		Draw.setView(viewControl);
		
//		Draw.drawImage(ImageLoader.getImage("spaceCadet"), Vector.ORIGIN);
		
		// Draw the map:
		map.draw();
		
		// Draw all entities in order of depth:
		for (int i = 0; i < depthList.size(); i++) {
			depthList.get(i).draw();
		}
	}
}
