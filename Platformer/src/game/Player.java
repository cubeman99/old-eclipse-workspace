package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import game.tile.Tile;
import main.ImageLoader;
import main.Keyboard;
import main.Keyboard.Key;
import common.GMath;
import common.Rectangle;
import common.Vector;
import common.graphics.Animation;
import common.graphics.AnimationStrip;
import common.graphics.Draw;
import common.graphics.Sprite;
import common.graphics.SpriteSheet;



public class Player {
	public Control control;
	private Vector position;
	private Vector velocity;
	private double acceleration;
	private double deceleration;
	private double moveSpeed;
	private double jumpSpeed;
	private double duckJumpSpeed;
	private double gravity;
	private boolean onGround;
	private int faceXScale;
	private Animation animation;
	private boolean ducking;
	private Vector size;
	
	private AnimationStrip stripIdle;
	private AnimationStrip stripWalk;
	private AnimationStrip stripJump;
	private AnimationStrip stripDuck;
	private AnimationStrip stripPush;
	
	private static final Key keyRight = new Key(KeyEvent.VK_RIGHT, KeyEvent.VK_D);
	private static final Key keyLeft  = new Key(KeyEvent.VK_LEFT, KeyEvent.VK_A);
	private static final Key keyJump  = new Key(KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_X);
	private static final Key keyDuck  = new Key(KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_Z);
	
	// ================== CONSTRUCTORS ================== //
	
	public Player(Control control) {
		this.control    = control;
		this.position   = new Vector(20, 10);
		this.velocity   = new Vector();
		this.onGround   = false;
		this.faceXScale = 1;
		this.ducking    = false;
		this.size       = new Vector(14.0 / 16.0, 14.0 / 16.0);
		
		this.acceleration  = 0.250 / 16.0;
		this.acceleration  = 0.125 / 16.0;
		this.deceleration  = 0.870;
		this.moveSpeed     = 1.500 / 16.0;
		this.jumpSpeed     = 3.600 / 16.0;
		this.duckJumpSpeed = 4.320 / 16.0;
		this.gravity       = 0.160 / 16.0;
		
		SpriteSheet sheet = ImageLoader.getSpriteSheet("player");
		this.stripIdle    = new AnimationStrip(sheet, 0, 0, 2, 0.055);
		this.stripWalk    = new AnimationStrip(sheet, 0, 1, 4, 0.25);
		this.stripJump    = new AnimationStrip(sheet, 0, 2, 1);
		this.stripDuck    = new AnimationStrip(sheet, 2, 0, 1);
		this.stripPush    = new AnimationStrip(sheet, 0, 4, 4, 0.08);
		
		this.animation    = new Animation();
		animation.setStrip(stripIdle);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Vector getPosition() {
		return position;
	}
	
	public Rectangle getRect() {
		return new Rectangle(position.x - 0.5, position.y - 0.5, 1, 1);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void update() {
		double xmove = (keyRight.down() ? 1 : 0) - (keyLeft.down() ? 1 : 0);
		ducking = (keyDuck.down() && onGround);
		
		if (ducking) {
			xmove = 0;
			animation.setStrip(stripDuck);
		}
		
		if (xmove != 0) {
			animation.setStrip(stripWalk);
			animation.setSpeed(Math.abs(velocity.x) * 2);
			double moveAdd = (xmove * acceleration);
			
			if (xmove > 0) {
				faceXScale = 1;
				if (onGround)
					velocity.x = Math.min(moveSpeed, velocity.x + moveAdd);
				else if (velocity.x < moveSpeed && velocity.x + moveAdd <= moveSpeed)
					velocity.x += moveAdd;
			}
			else {
				faceXScale = -1;
				if (onGround)
					velocity.x = Math.max(-moveSpeed, velocity.x + (xmove * acceleration));
				else if (velocity.x > -moveSpeed && velocity.x + moveAdd >= -moveSpeed)
					velocity.x += moveAdd;
			}
		}
		else {
			velocity.x *= deceleration;
			if (Math.abs(velocity.x) < 0.06 && !ducking)
				animation.setStrip(stripIdle);
		}
		
		if (!onGround)
			animation.setStrip(stripJump);
		
		if (keyJump.down() && onGround) {
			if (ducking) {
    			if (Math.abs(velocity.x) > moveSpeed * 0.4)
    				velocity.set(Math.signum(velocity.x) * moveSpeed * 2.1, -jumpSpeed * 0.86);
    			else
    				velocity.y = -duckJumpSpeed;
			}
			else
				velocity.y = -jumpSpeed;
		}
		
		velocity.y += gravity;
		checkCollisions(control.world);
		position.add(velocity);
		
		animation.update();
	}
	
	public void checkCollisions(World world) {
		Vector newPos     = new Vector(position.x + velocity.x, position.y);
		Rectangle newRect = new Rectangle(newPos.x - 0.5 + (1.0 / 16.0), newPos.y - 0.5 + (2.0 / 16.0), size.x, size.y);
		onGround          = false;
		
		for (int x = 0; x < world.size.x; x++) {
			for (int y = 0; y < world.size.y; y++) {
				Tile t = world.tiles[x][y];
				if (t != null) {
					if (t.isSolid()) {
						Rectangle tileRect = new Rectangle(x, y, 1, 1);
						
						if (newRect.touches(tileRect)) {
							velocity.x = 0;
							if (position.x < (double) x + 0.5)
								position.x = (double) x - (size.x * 0.5);
							else
								position.x = (double) x + 1.0 + (size.x * 0.5);
						}
					}
				}
			}
		}
		
		newPos  = position.plus(velocity);
		newRect = new Rectangle(newPos.x - 0.5 + (1.0 / 16.0), newPos.y - 0.5 + (2.0 / 16.0), size.x, size.y);
		
		for (int x = 0; x < world.size.x; x++) {
			for (int y = 0; y < world.size.y; y++) {
				Tile t = world.tiles[x][y];
				if (t != null) {
					if (t.isSolid()) {
						Rectangle tileRect = new Rectangle(x, y, 1, 1);
						
						if (newRect.touches(tileRect)) {
							velocity.y = 0;
							if (position.y < (double) y + 0.5) {
								position.y = (double) y - 0.5;
								onGround = true;
							}
							else
								position.y = (double) y + size.y + 0.5;
						}
					}
				}
			}
		}
	}
	
	public void draw() {
		Draw.drawSprite(animation.getCurrentSprite(), position.x, position.y, faceXScale, 1);
		Draw.setColor(Color.RED);
//		Draw.drawRect(getRect());
	}
}
