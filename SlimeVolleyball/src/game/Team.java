package game;

import java.awt.Color;
import cmg.math.geometry.Rectangle;

public class Team {
	private double leftWallX;
	private double rightWallX;
	private Rectangle viewRect;
	private int score;
	private int index;
	private Color color;
	
	
	public Team(int index, Color color, double leftWallX, double rightWallX) {
		this.index      = index;
		this.color      = color;
		this.score      = 0;
		this.leftWallX  = leftWallX;
		this.rightWallX = rightWallX;
		this.viewRect   = new Rectangle(leftWallX, 0, rightWallX - leftWallX, Settings.VIEW_HEIGHT);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public double getLeftWallX() {
		return leftWallX;
	}
	
	public double getRightWallX() {
		return rightWallX;
	}
	
	public double getCenterX() {
		return ((leftWallX + rightWallX) * 0.5);
	}
	
	public Rectangle getViewRect() {
		return viewRect;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getIndex() {
		return index;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void addScore() {
		score++;
//		if (score > Settings.MAX_SCORE)
//			score = Settings.MAX_SCORE;
	}
	
	public void removeScore() {
		score--;
		if (score < 0)
			score = 0;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
