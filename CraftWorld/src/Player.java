import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;


public class Player {

	public int health_max = 100;
	public int health = health_max;
	public Image image;
	
	public float width  = 1.0f;
	public float height = 1.8f;
	public float image_offsetx = 0.0f;
	public float image_offsety = -0.2f;
	
	public float spawnx;
	public float spawny;
	public float x;
	public float y;
	public float hspeed = 0.0f;
	public float vspeed = 0.0f;
	public float movespeed = 0.2f;
	public float jumpspeed = 0.3f;
	public float gravity = 0.01f;
	public float terminalVelocity = 0.8f;
	
	public Player(float spawnx, float spawny) {
		this.spawnx = spawnx;
		this.spawny = spawny;
		this.x = this.spawnx;
		this.y = this.spawny;
		this.image = ImageLoader.loadImage("player.png");
	}
	
	public boolean blockMeeting(float px, float py, int bx, int by) {
		return( px + width > (float)bx && py + height > (float)by && px < (float)bx + 1.0f && py < (float)by + 1.0f );
	}
	
	public boolean placeMeeting(float px, float py) {
		int basex = Math.round(x);
		int basey = Math.round(y);
		int startxx = Math.max(0, basex - 1);
		int startyy = Math.max(0, basey - 1);
		int endxx 	= Math.min(GameControl.WORLD_WIDTH - 1, basex + (int)(width + 1.0f) + 1);
		int endyy	= Math.min(GameControl.WORLD_HEIGHT - 1, basey + (int)(height + 1.0f) + 1);
		
		for( int xx = startxx; xx <= endxx; xx++ ) {
			for( int yy = startyy; yy <= endyy; yy++ ) {
				if( GameControl.blocks[xx][yy].solid ) {
					if( blockMeeting(px, py, xx, yy) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void checkCollisions() {
		int basex = Math.round(x);
		int basey = Math.round(y);
		int startxx = Math.max(0, basex - 1);
		int startyy = Math.max(0, basey - 1);
		int endxx 	= Math.min(GameControl.WORLD_WIDTH - 1, basex + (int)(width + 1.0f) + 1);
		int endyy	= Math.min(GameControl.WORLD_HEIGHT - 1, basey + (int)(height + 1.0f) + 1);
		
		// Check Horizontal Collisions
		for( int xx = startxx; xx <= endxx; xx++ ) {
			for( int yy = startyy; yy <= endyy; yy++ ) {
				if( GameControl.blocks[xx][yy].solid ) {
					if( blockMeeting(x + hspeed, y, xx, yy) ) {
						hspeed = 0.0f;
						if( x < xx )
							x = xx - width;
						else
							x = xx + 1;
					}
				}
			}
		}
		// Check Vertical Collisions
		for( int xx = startxx; xx < endxx; xx++ ) {
			for( int yy = startyy; yy < endyy; yy++ ) {
				if( GameControl.blocks[xx][yy].solid ) {
					if( blockMeeting(x + hspeed, y + vspeed, xx, yy) ) {
						vspeed = 0.0f;
						if( y < yy )
							y = yy - height;
						else
							y = yy + 1;
					}
				}
			}
		}
	}
	
	public void jump() {
		vspeed = -jumpspeed;
	}
	
	public void respawn() {
		x = spawnx;
		y = spawny;
		hspeed = 0.0f;
		vspeed = 0.0f;
	}
	
	
	public void update() {
		int xmove = 0;
		if( GameControl.keyDown[KeyEvent.VK_ENTER] ) {
			respawn();
		}
		if( GameControl.keyDown[KeyEvent.VK_A] || GameControl.keyDown[KeyEvent.VK_LEFT] )
			xmove -= 1;
		if( GameControl.keyDown[KeyEvent.VK_D] || GameControl.keyDown[KeyEvent.VK_RIGHT] )
			xmove += 1;
		
		// TODO: Get rid of HAAAAXXX!!
		if( GameControl.keyDown[KeyEvent.VK_Q] )
			vspeed = -0.1f;
		if( GameControl.keyDown[KeyEvent.VK_W] || GameControl.keyDown[KeyEvent.VK_UP] || GameControl.keyDown[KeyEvent.VK_SPACE] ) {
			if( placeMeeting(x, y + 0.01f) )
				jump();
		}
		hspeed = (float)xmove * movespeed;
		
		vspeed += gravity;
		
		vspeed = Math.min(terminalVelocity, Math.max(-terminalVelocity, vspeed));
		checkCollisions();
		
		// Move the Player
		x += hspeed;
		y += vspeed;
		
	}
	
	public void draw(Graphics g) {
		
		g.drawImage(image, (int)(16.0f * (x + image_offsetx)) - GameControl.VIEW_X, (int)(16.0f * (y + image_offsety)) - GameControl.VIEW_Y, null);
	}
}
