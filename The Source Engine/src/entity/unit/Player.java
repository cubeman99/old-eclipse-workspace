package entity.unit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import weapon.*;
import main.ImageLoader;
import main.Keyboard;
import main.Mouse;
import map.Path;
import common.GMath;
import common.HUD;
import common.Vector;
import common.shape.Circle;
import control.Control;
import control.Team;


public class Player extends Unit {
	public Path followPath = null;
	public Cursor crosshairCursor;
	
	
	public Player(Control control, Team team, Vector position) {
		super(team, position, Unit.UNIT_RADIUS);
		
		setMaxHealth(10000);
		weapon = new WeaponM4(this);
		crosshairCursor = ImageLoader.makeCursor("crosshair", new Point(9, 9));
		control.viewControl.setZoom(70);
	}

	@Override
	public void update() {
		
		if (followPath != null) {
			double spd = 0.1;
			if (GMath.distance(getPosition(), followPath.getCurrentPoint()) < spd * 0.6) {
				((Circle) body.shape).position.set(followPath.getCurrentPoint());
				if (followPath.nextPoint()) {
					followPath = null;
					body.velocity.zero();
				}
			}
			else {
    			double dir = GMath.direction(getPosition(), followPath.getCurrentPoint());
    			body.velocity.setPolar(spd, dir);
			}
		}
		else {
			updateMoveControls();
		}
		direction = GMath.direction(getPosition(), control.viewControl.getGamePoint(Mouse.getVector()));
		
//		updateMoveControls();
//		checkCollisions();
		
		Mouse.setCursor(crosshairCursor);
		weapon.updatePlayerWeapon();
		control.viewControl.zoomFollow(getPosition());
	}
	
	
	public void updateMoveControls() {
		Vector move = new Vector();
		if (Keyboard.up.down())
			move.y -= 1;
		if (Keyboard.down.down())
			move.y += 1;
		if (Keyboard.left.down())
			move.x -= 1;
		if (Keyboard.right.down())
			move.x += 1;
		
		move.setLength(0.007);
//		move.setLength(0.0001);
		
		direction = GMath.direction(getPosition(), control.viewControl.getGamePoint(Mouse.getVector()));
		
		
		/*
		if (move.length() > 0) {
			speed = GMath.min(maxSpeed, speed + 0.007);
//			moveVector.set(move).scale(speed);
		}
		else {
			speed *= 0.8;
			if (speed < 0.001)
				speed = 0;
		}

		move.setLength(speed);
		moveVector.add(move);
		if (moveVector.length() > maxSpeed)
			moveVector.setLength(maxSpeed);
		*/
		if (move.length() > 0) {
			moveVector.add(move);
			moveVector.setLength(GMath.min(maxSpeed, moveVector.length()));
		}
		else if (moveVector.length() > 0.001) {
			moveVector.setLength(GMath.max(0, moveVector.length() - 0.01));
		}
		else {
			moveVector.zero();
		}
		
		body.velocity.set(moveVector);
	}
	
	@Override
	public void draw() {
		super.draw();
//		Vector mgs = control.viewControl.getGamePoint(Mouse.getVector());
		
		
		Graphics g = HUD.getGraphics();
		
		g.setColor(Color.WHITE);
		g.drawString(weapon.getName(), 20, 30);
		g.drawString(weapon.getClipAmmo() + "/" + weapon.getPackAmmo(), 20, 50);
		g.drawString(weapon.getFireMode().getName(), 20, 70);
		
		
		
		if (weapon.isReloading()) {
    		Point p = new Point(32, 32);
    		g.setColor(Color.DARK_GRAY);
    		g.fillRect(p.x, p.y, 128, 8);
    		g.setColor(Color.WHITE);
    		g.fillRect(p.x, p.y, (int) (weapon.getReloadPercentage() * 128.0), 8);
		}
		
		
//		Point p = new Point(32, 32);
//		g.setColor(Color.DARK_GRAY);
//		g.fillRect(p.x, p.y, 128, 8);
//		g.setColor(Color.WHITE);
//		g.fillRect(p.x, p.y, (int) (weapon.getRecoilPercentage() * 128.0), 8);
		
		g.setColor(Color.GREEN);
//		Draw.drawArc(getPosition(), getPosition().distanceTo(mgs), direction - (weapon.getDirSpread() / 2.0), weapon.getDirSpread());
	}
}
