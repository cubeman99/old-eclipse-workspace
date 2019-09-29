package game;

import game.entity.Entity;
import game.nodes.Enemy;
import game.nodes.NodeEnhancerSpeed;
import game.powerups.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import main.Game;
import main.ImageLoader;
import main.Keyboard;
import common.GMath;
import common.Settings;
import common.Vector;
import common.graphics.Draw;


/**
 * Control.
 * 
 * @author David Jordan
 */
public class Control {
	private ArrayList<Enemy> enemies;
	public ArrayList<Entity> entities;
	public ArrayList<Entity> depthList;
	public ArrayList<Ring> rings;
	public ArrayList<PowerUp> powerUps;
	private double planetHealth;
	private double planetArmor;
	private double planetAngle;
	public Player player;
	private double ringSpeed;
	public boolean superLasers;
	public boolean triLasers;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Control() {
		enemies         = new ArrayList<Enemy>();
		entities        = new ArrayList<Entity>();
		depthList       = new ArrayList<Entity>();
		powerUps        = new ArrayList<PowerUp>();
		planetHealth    = Settings.PLANET_HEALTH_MAX;
		planetArmor     = 0;
		planetAngle     = 0;
		rings           = new ArrayList<Ring>();
		player          = new Player(this);
		ringSpeed       = 0;
		superLasers     = false;
		triLasers       = false;
		
		rings.add(new Ring(this, Settings.RING_SPAWN_RADIUS));
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the speed that the rings are currently moving in at.**/
	public double getRingSpeed() {
		return ringSpeed;
	}
	
	/** Return the array list of enemies. **/
	public ArrayList<Enemy> getEnemyList() {
		return enemies;
	}
	
	/** Return the front-most ring that is connected. **/
	public Ring getFrontRing() {
		for (int i = 0; i < rings.size(); i++) {
			if (rings.get(i).isConnected())
				return rings.get(i);
		}
		return null;
	}
	
	/** Return the enemy nearest to the given point. **/
	public Enemy getNearestEnemy(Vector point) {
		ArrayList<Enemy> possibleTargets = new ArrayList<Enemy>();
		double minDist = -1;
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			if (!e.isFutureDead()) {
				double dist = point.distanceTo(e.getPosition());
				
				if (dist < minDist || possibleTargets.size() == 0)
					minDist = dist;
				if (GMath.abs(minDist - dist) < 0.1)
    				possibleTargets.add(e);
			}
		}
		
		if (possibleTargets.size() == 0)
			return null;
		
		return possibleTargets.get(GMath.random.nextInt(possibleTargets.size()));
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Damage the planet by a given amount, with armor absorbing a percentage of it. **/
	public void damagePlanet(double amount) {
		double absorb = Math.min(planetArmor, amount * Settings.ARMOR_ABSORB_PERCENTAGE);
		planetHealth -= amount - absorb;
		planetArmor  -= absorb;
	}
	
	/** Heal the planet's health by the given amount. **/
	public void healPlanet(double amount) {
		planetHealth = GMath.min(Settings.PLANET_HEALTH_MAX, planetHealth + amount);
	}
	
	/** Heal the planet's health by the given amount. **/
	public void addArmor(double amount) {
		planetArmor = GMath.min(Settings.PLANET_ARMOR_MAX, planetArmor + amount);
	}
	
	/** Drop a random power-up from the given enemy. **/
	private void createRandomPowerUp(Enemy e) {
		int r = GMath.random.nextInt(7);
		PowerUp p;
		
		if (r == 0)
			p = new PowerHealth(e);
		else if (r == 1)
			p = new PowerTriLasers(e);
		else if (r == 2)
			p = new PowerMissile(e);
		else if (r == 3)
			p = new PowerSuperLasers(e);
		else if (r == 4)
			p = new PowerArmor(e);
		else if (r == 5)
			p = new PowerHomingLasers(e);
		else
			p = new PowerChargeBlast(e);
		
		addEntity(p);
	}
	
	/** Update the game. **/
	public void update() {
		player.update();

		
		if (Keyboard.backspace.pressed()) {
			rings.get(0).crash();
		}
		
		Ring backRing = rings.get(rings.size() - 1);
		if (backRing.getRadius() < Settings.RING_SPAWN_RADIUS)
			rings.add(new Ring(this, Settings.RING_SPAWN_RADIUS + Settings.RING_SEPERATION));
		
		planetAngle += GMath.toRadians(0.05);
		
		Ring frontRing         = getFrontRing();
		double frontRingRadius = frontRing.getRadius();
		double goalRingSpeed   = Settings.RING_MOVE_IN_SPEED;
		
		if (frontRingRadius > Settings.RING_SPEED_UP_RADIUS)
			goalRingSpeed += (frontRingRadius - Settings.RING_SPEED_UP_RADIUS) / 200.0;
		
		if (frontRing.getEnhancer() instanceof NodeEnhancerSpeed)
			goalRingSpeed *= 3;
		
		if (ringSpeed < goalRingSpeed)
			ringSpeed += (goalRingSpeed - ringSpeed) / 20.0;
		else
			ringSpeed = goalRingSpeed;
		
		
		for (int i = 0; i < rings.size(); i++) {
			Ring r = rings.get(i);
			
			
			r.moveIn(r.isConnected() ? ringSpeed : 0.5);
			
			r.update();
			
			if (r.nodes.size() == 0) {
				rings.remove(i);
				i--;
			}
			else if (r.getRadius() < Settings.RING_CRASH_RADIUS) {
				r.crash();
				rings.remove(i);
				i--;
			}
		}
		
		// Update all enemies:
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			e.update();
			
			if (e.isDestroyed()) {
				enemies.remove(i);
				
//				if (GMath.onChance(38))
//					createRandomPowerUp(e);
				
				if (GMath.onChance(10))
					createRandomPowerUp(e);
				
				i--;
			}
		}
		
		// Update all entities:
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isDestroyed()) {
				removeDepthEntity(e);
				entities.remove(i);
				i--;
			}
		}
	}
	
	/** Add an enemy to the list of enemies. **/
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	
	/** Insert an enemy to the list of enemies. **/
	public void addEnemy(int index, Enemy e) {
		enemies.add(index, e);
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
	
	/** Draw the game. **/
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		Draw.setCenter(new Vector(Game.getViewWidth(), Game.getViewHeight()).scale(0.5));
		
		// Draw all rings:
		for (int i = 0; i < rings.size(); i++)
			rings.get(i).draw();
		
		// Draw all enemies:
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw();
		}
		
		// Draw all entities in order of depth:
		for (int i = 0; i < depthList.size(); i++)
			depthList.get(i).draw();

		
		// Draw the planet and the player's ship:
		Draw.drawImage(ImageLoader.getImage("planet"), 0, 0, planetAngle, 80, 80, 1);
		player.draw();
		
		
		g.setColor(Color.WHITE);
		g.drawString("Health: " + planetHealth + "     Armor: " + planetArmor, 10, 40);
	}
}
