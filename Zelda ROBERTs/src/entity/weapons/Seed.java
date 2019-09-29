package entity.weapons;

import entity.Entity;
import geometry.Point;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Draw;
import graphics.Sprite;
import graphics.library.Library;

import java.awt.Graphics2D;

import world.Room;

/**
 * A base seed that can be dropped or shot.
 * @author	Robert Jordan
 */
public class Seed extends Entity {

	// ======================= Members ========================
	
	/** The sprite of the effect. */
	public Sprite effectSprite;
	/** The tile of the seed. */
	public Point tile;
	/** Defines how the seed interacts with the environmemt. */
	public int seedState;
	/** The action state of the seed. */
	public int effectState;
	
	// 0 = dropped
	// 1 = seed shooter
	// 2 = slingshot center
	// 3 = slingshot top
	// 4 = slingshot bottom

	// ===================== Constructors =====================
	
	/** Constructs the default seed. */
	public Seed() {
		super("seed");

		this.bounds			= new Rectangle(4, 4, 8, 8);
		this.z				= 1.25 - 0.99;
		this.zspeed			= 1 - 0.99;
		this.tile			= new Point(4, 15);
		this.seedState		= 0;
		this.effectState	= 0;
		this.effectSprite	= new Sprite(null, null);
	}
	/** Constructs a seed with the given direction and position. */
	public Seed(String seedType, int direction, int seedState, Point tile, Vector point, double z) {
		super(seedType);

		this.bounds			= new Rectangle(4, 4, 8, 8);
		this.position		= new Vector(point);
		this.z				= z + 1 - 0.99;
		this.zspeed			= 0;
		this.tile			= new Point(tile);
		this.seedState		= seedState;
		this.effectState	= 0;
		this.effectSprite	= new Sprite(null, null);
		
		if (seedState == 0) {
			this.id += "_seed_satchel";
			this.z				= z + 1.25 - 0.99;
			this.zspeed			= 0.6;
			double diag1 =  3.5 / Math.sqrt(2.0);
			double diag2 = 0.75 / Math.sqrt(2.0);
			switch (direction) {
			case 0:
				this.position.add( 3.5, 0);
				this.velocity.set(0.75, 0);
				break;
			case 1:
				this.position.add(diag1, diag1);
				this.velocity.set(diag2, diag2);
				break;
			case 2:
				this.position.add(0,  3.5);
				this.velocity.set(0, 0.75);
				break;
			case 3:
				this.position.add(-diag1, diag1);
				this.velocity.set(-diag2, diag2);
				break;
			case 4:
				this.position.add( -3.5, 0);
				this.velocity.set(-0.75, 0);
				break;
			case 5:
				this.position.add(-diag1, -diag1);
				this.velocity.set(-diag2, -diag2);
				break;
			case 6:
				this.position.add(0,  -3.5);
				this.velocity.set(0, -0.75);
				break;
			case 7:
				this.position.add(diag1, -diag1);
				this.velocity.set(diag2, -diag2);
				break;
			}
		}
		else if (seedState == 1) {
			this.id += "_seed_shooter";
			double diag1 = 16.0 / Math.sqrt(2.0);
			double diag2 =  3.0 / Math.sqrt(2.0);
			switch (direction) {
			case 0:
				this.position.add(16, 0);
				this.velocity.set( 3, 0);
				break;
			case 1:
				this.position.add(diag1, diag1);
				this.velocity.set(diag2, diag2);
				break;
			case 2:
				this.position.add(0, 16);
				this.velocity.set(0,  3);
				break;
			case 3:
				this.position.add(-diag1, diag1);
				this.velocity.set(-diag2, diag2);
				break;
			case 4:
				this.position.add(-16, 0);
				this.velocity.set( -3, 0);
				break;
			case 5:
				this.position.add(-diag1, -diag1);
				this.velocity.set(-diag2, -diag2);
				break;
			case 6:
				this.position.add(0, -16);
				this.velocity.set(0,  -3);
				break;
			case 7:
				this.position.add(diag1, -diag1);
				this.velocity.set(diag2, -diag2);
				break;
			}
		}
		else if (seedState == 2) {
			this.id += "_slingshot";
			double diag1 =  1.0 / Math.sqrt(2.0);
			double diag2 =  3.0 / Math.sqrt(2.0);
			switch (direction) {
			case 0:
				this.position.add(1, 0);
				this.velocity.set(3, 0);
				break;
			case 1:
				this.position.add(diag1, diag1);
				this.velocity.set(diag2, diag2);
				break;
			case 2:
				this.position.add(0, 1);
				this.velocity.set(0, 3);
				break;
			case 3:
				this.position.add(-diag1, diag1);
				this.velocity.set(-diag2, diag2);
				break;
			case 4:
				this.position.add(-1, 0);
				this.velocity.set(-3, 0);
				break;
			case 5:
				this.position.add(-diag1, -diag1);
				this.velocity.set(-diag2, -diag2);
				break;
			case 6:
				this.position.add(0, -1);
				this.velocity.set(0, -3);
				break;
			case 7:
				this.position.add(diag1, -diag1);
				this.velocity.set(diag2, -diag2);
				break;
			}
		}
		else {
			this.id += "_slingshot";
			double diag1 =  1.0 / Math.sqrt(2.0);
			//double diag2 = 2.75 / Math.sqrt(2.0);
			double diag2 = 3.5 / Math.sqrt(2.0);
			switch (direction) {
			case 0:
				this.position.add(   1,  0);
				this.velocity.set(2.75, -1);
				break;
			case 1:
				this.position.add(diag1, diag1);
				this.velocity.set(diag2, diag1);
				break;
			case 2:
				this.position.add( 0,    1);
				this.velocity.set(-1, 2.75);
				break;
			case 3:
				this.position.add(-diag1, diag1);
				this.velocity.set(-diag2, diag1);
				break;
			case 4:
				this.position.add(   -1,  0);
				this.velocity.set(-2.75, -1);
				break;
			case 5:
				this.position.add(-diag1, -diag1);
				this.velocity.set(-diag1, -diag2);
				break;
			case 6:
				this.position.add( 0,    -1);
				this.velocity.set(-1, -2.75);
				break;
			case 7:
				this.position.add(diag1, -diag1);
				this.velocity.set(diag1, -diag2);
				break;
			}
			
			if (seedState == 4) {
				if (direction % 2 == 0) {
					if (direction % 4 == 0)
						this.velocity.y = -this.velocity.y;
					else
						this.velocity.x = -this.velocity.x;
				}
				else if (direction == 1 || direction == 5){
					double swap = this.velocity.x;
					this.velocity.x = this.velocity.y;
					this.velocity.y = swap;
				}
				else {
					double swap = this.velocity.x;
					this.velocity.x = -this.velocity.y;
					this.velocity.y = -swap;
				}
			}
		}
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}

	// ======================= Updating =======================
	
	/** Called every step, before movement, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
		effectSprite.update();
		
		if (seedState == 0 && effectState == 0)
			zspeed -= 0.15;
		
		if (effectSprite.isAnimationFinished()) {
			destroy();
		}
	}
	/** Called every step, before collision, to update the entity's state. */
	public void update() {
		super.update();
		
	}
	/** Called every step, after collision, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
		
	}
	/** Called every step, after drawing, to update the entity's state. */
	public void postdraw() {
		super.postdraw();
		
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		super.updateMovement();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		super.updateCollissions();
		if (room.isOutsideRoom(position, bounds)) {
			destroy();
		}
		else if (effectState == 0 && z <= -1.0) {
			velocity.zero();
			effectState = 1;
			zspeed = 0;
			startEffect();
		}
		
	}
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
		if (effectState == 0) {
			Draw.drawTile(g, Library.tilesets.weapons, tile, point.plus(position).plus(0, -z));
		}
		else {
			effectSprite.draw(g, point.plus(position).plus(0, -z));
		}
	}
	
	// ===================== Seed Related =====================

	/** Called to create the effect and perform actions. */
	public void startEffect() {
		effectSprite.setAnimation(Library.animations.mysterySeedEffect);
		effectSprite.setTileset(Library.tilesets.specialEffects);
	}
	/** Creates a seed of the specific type. */
	public static Seed createSeedOfType(String type, int direction, int seedState, Vector point, double z) {
		if (type.equals("ember_seeds"))
			return new EmberSeed(direction, seedState, point, z);
		else if (type.equals("scent_seeds"))
			return new ScentSeed(direction, seedState, point, z);
		else if (type.equals("pegasus_seeds"))
			return new PegasusSeed(direction, seedState, point, z);
		else if (type.equals("gale_seeds"))
			return new GaleSeed(direction, seedState, point, z);
		else if (type.equals("mystery_seeds"))
			return new MysterySeed(direction, seedState, point, z);
		return null;
	}
}