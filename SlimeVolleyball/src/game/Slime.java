package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import cmg.graphics.Draw;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Line;
import cmg.math.geometry.Vector;


/**
 * Slime.
 * 
 * @author David Jordan
 */
public class Slime {
	private Keyboard.Key leftKey;
	private Keyboard.Key rightKey;
	private Keyboard.Key jumpKey;
	private SlimeBall control;
	private Vector position;
	private Vector velocity;
	private double radius;
	private boolean onGround;
	
	private Team team;
	private Color color;
	private String name;
	private int id;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Slime(SlimeBall control, String name, Team team, Color color) {
		this.control  = control;
		this.team     = team;
		this.color    = color;
		this.onGround = false;
		this.leftKey  = Keyboard.left;
		this.rightKey = Keyboard.right;
		this.jumpKey  = new Keyboard.Key(KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_SPACE);
		this.position = new Vector();
		this.velocity = new Vector();
		this.radius   = Settings.SLIME_RADIUS;
		this.name     = name;
		this.id       = 0;
		
		respawn();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getID() {
		return id;
	}
	
	/** Return the position of this slime. **/
	public Vector getPosition() {
		return position;
	}

	/** Return the velocity of this slime. **/
	public Vector getVelocity() {
		return velocity;
	}

	/** Return the radius of this slime. **/
	public double getRadius() {
		return radius;
	}
	
	/** Return a line representing the shape of the slime's bottom. **/
	public Line getBaseLine() {
		return new Line(position.x - radius, position.y, position.x + radius, position.y);
	}
	
	/** Return the name of this slime player. **/
	public String getName() {
		return name;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public int getTeamIndex() {
		return team.getIndex();
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getColorIndex() {
		int index = 0;
		for (int i = 0; i < Settings.colorOptions.length; i++) {
			if (color.equals(Settings.colorOptions[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	// ==================== MUTATORS ==================== //
	
	public void update() {
		// Update movement:
		velocity.y += Settings.SLIME_GRAVITY;
		position.add(velocity);
		
		collideWithWalls();
		
		double move = 0;

		// Check horizontal movement keys:
		if (leftKey.down())
			move--;
		if (rightKey.down())
			move++;
		
		// Check if jump key is pressed:
		if (jumpKey.down() && onGround) {
			velocity.y = -Settings.SLIME_JUMP_SPEED;
			onGround   = false;
		}
		
		velocity.x = move * 4.5;
	}
	
	public void respawn() {
		position.set(team.getCenterX(), Settings.FLOOR_Y);
		velocity.zero();
	}
	
	private void collideWithWalls() {
		// Collide with ground:
		if (position.y >= Settings.FLOOR_Y) {
			position.y = Settings.FLOOR_Y;
			velocity.y = 0;
			onGround   = true;
		}
		
		// Collide with side walls:
		if (position.x <= team.getLeftWallX() + radius) {
			position.x = team.getLeftWallX() + radius;
			velocity.x = 0;
		}
		if (position.x >= team.getRightWallX() - radius) {
			position.x = team.getRightWallX() - radius;
			velocity.x = 0;
		}
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/** Set the color of this slime. **/
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		String str = "";
		
		str += "slime[name=" + name + ", team=" + team.getIndex()
				+ ", col=" + color.getRed() + "/" + color.getGreen()
				+ "/" + color.getBlue() + "]";
		
		return str;
	}
	
	/** Draw this slime. **/
	public void draw(/*Ball ball*/) {
		Draw.setColor(color);
		
		// Draw the body:
		Draw.fillArc(position, radius, 0, GMath.PI);
		
		double eyeRadius = radius / 6.0;
		double dirAdd    = (getTeamIndex() == 0 ? 0 : GMath.HALF_PI);
		Vector eyePos    = position.plus(Vector.polarVector(radius - eyeRadius, GMath.QUARTER_PI + dirAdd));
		double pupilRadius = eyeRadius * 0.5;
		Vector pupilPos    = eyePos.plus(Vector.polarVector(eyeRadius - pupilRadius, GMath.direction(eyePos, control.getBall().getPosition())));

		// Draw the eye whites:
		Draw.setColor(Color.WHITE);
		Draw.fillCircle(eyePos, eyeRadius);

		// Draw the pupils:
		Draw.setColor(Color.BLACK);
		Draw.fillCircle(pupilPos, pupilRadius);
	}
	
	public static void drawSlime(Vector position, double radius, Color color, int faceDirection, double eyeAngle) {
		Draw.setColor(color);
		
		// Draw the body:
		Draw.fillArc(position, radius, 0, GMath.PI);
		
		double eyeRadius = radius / 6.0;
		double dirAdd    = (faceDirection == 0 ? 0 : GMath.HALF_PI);
		Vector eyePos    = position.plus(Vector.polarVector(radius - eyeRadius, GMath.QUARTER_PI + dirAdd));
		double pupilRadius = eyeRadius * 0.5;
		Vector pupilPos    = eyePos.plus(Vector.polarVector(eyeRadius - pupilRadius, eyeAngle));

		// Draw the eye whites:
		Draw.setColor(Color.WHITE);
		Draw.fillCircle(eyePos, eyeRadius);

		// Draw the pupils:
		Draw.setColor(Color.BLACK);
		Draw.fillCircle(pupilPos, pupilRadius);
	}
}