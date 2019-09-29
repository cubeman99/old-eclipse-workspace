
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Player extends Unit {
	public double viewX			= 0;
	public double viewY			= 0;
	public Vector viewGoal		= new Vector();
	public Vector viewVector	= new Vector();
	
	public Player(int team, double x, double y) {
		super(team, x, y);
		
		viewVector.set(x, y);
		viewGoal.set(x, y);
		
		weaponType = new WeaponAR1();
		
		giveNewWeapon();
	}
	
	public void update() {
		super.update();
		if (!dead) {
			viewX = x;
			viewY = y;
		}
		
		boolean kUp		= Game.keys.up.isDown;
		boolean kDown	= Game.keys.down.isDown;
		boolean kLeft	= Game.keys.left.isDown;
		boolean kRight	= Game.keys.right.isDown;
        
		if (dead) {
			
		}
		else {
		faceDir = GMath.direction(x, y, Game.mouseX, Game.mouseY);
	
			if (kUp || kDown || kLeft || kRight) {
				direction = GMath.direction(GMath.dBool(kRight) - GMath.dBool(kLeft),
						GMath.dBool(kDown) - GMath.dBool(kUp));
				moveSpeed += moveAcceleration;
			}
			else {
				moveSpeed *= 0.7;
			}
	
			if (Game.mbLeft && !Game.keyDown[KeyEvent.VK_CONTROL]) {
				fireWeapon();
			}
	
			if (Game.keyPressed[KeyEvent.VK_R]) {
				// Reload
				weapon.reload();
			}
		}

		checkDebugCommands();
		
		if (!dead)
			updateView();
	}
	
	public void updateView() {
		double winWidth		= Game.VIEW_WIDTH;
		double winHeight	= Game.VIEW_HEIGHT;
		double maxDistance	= 56;
        double dir = GMath.direction(winWidth / 2.0, winHeight / 2.0, Game.rawMouseX, Game.rawMouseY);
        double len = GMath.distance(winWidth / 2.0, winHeight / 2.0, Game.rawMouseX, Game.rawMouseY) - 64.0;
        len = Math.max(0.0, Math.min(maxDistance, maxDistance * (len / 5.0) / 64.0));
        
        viewGoal.set(x + GMath.lenDirX(len, dir), y + GMath.lenDirY(len, dir));
        viewVector.set(viewGoal);
//        viewVector.x += (Math.abs(viewGoal.x - viewVector.x) / 4) * Math.signum(viewGoal.x - viewVector.x);
//        viewVector.y += (Math.abs(viewGoal.y - viewVector.y) / 4) * Math.signum(viewGoal.y - viewVector.y);
        viewX = viewVector.x;
        viewY = viewVector.y;
	}
	
	public void checkDebugCommands() {
		//health = Math.max(100, health);
		
		if (Game.keyDown[KeyEvent.VK_INSERT]) {
			weapon.giveMaxAmmo();
		}
		
		if (dead) {
			if (Game.keyPressed[KeyEvent.VK_HOME])
				unitRespawn();
		}
	}
	
	public void draw(Graphics g) {
		super.draw(g);
		
		Graphics hg = Game.getHudGraphics();
		
//		if (!dead) {
//			double hx = Game.VIEW_WIDTH / 2;
//			double hy = Game.VIEW_HEIGHT / 2;
//			hg.setColor(teamControl.color);
//			hg.drawOval((int) (hx - radius), (int) (hy - radius), diameter, diameter);
//			hg.drawLine((int)hx, (int)hy, (int)(hx + GMath.lenDirX(radius, faceDir)), (int)(hy + GMath.lenDirY(radius, faceDir)));
//			
//			// Draw health bar
//			int xx = radius - 2;
//			int yy = radius + 5;
//			hg.setColor(Color.darkGray);
//			hg.drawLine((int)hx - xx, (int)hy - yy, (int)hx + xx, (int)hy - yy);
//			if (health > 0) {
//				hg.setColor(Color.green);
//				hg.drawLine((int)hx - xx, (int)hy - yy, (int) (hx - xx + (2 * xx * (health / maxHealth))), (int)hy - yy);
//			}
//		}
		
		// Draw Ammo + Reloading Text
		String s = weapon.clipAmmo + " / " + weapon.packAmmo;
		if (weapon.reloading) {
			s += " (reloading)";
			
			Rect r = new Rect(32, 45, 37, 6);
			Drawing.drawHealthbar(hg, r, weapon.reloadTimer.getSeconds() / weapon.reloadTime, Color.lightGray, Color.gray, Color.darkGray);
		}
		hg.setColor(Color.white);
		hg.drawString(s, 32, 32);
		
		String s2 = "Kills: " + kills + ",  Deaths: " + deaths + ", Ratio: ";
		if (killDeathRatio >= 0 )
			s2 += killDeathRatio;
		else
			s2 += "---";
		hg.drawString(s2, 32, 60);
	}
}
