package projects.slimeVolleyBall;

import java.awt.Color;
import common.Vector;

public class Settings {
	// BALL:
	public static final double BALL_RADIUS			= 11;
	public static final double BALL_GRAVITY			= 0.22;
	public static final double BALL_MAX_SPEED		= 10;
	public static final double BALL_SERVE_HEIGHT	= 130;
	public static final Color BALL_COLOR			= Color.YELLOW;
	
	// SLIME:
	public static final double SLIME_RADIUS		= 37;
	public static final double SLIME_GRAVITY	= 0.5;
	public static final double SLIME_JUMP_SPEED	= 9;
	
	public static final double VIEW_WIDTH		= 750;
	public static final double VIEW_HEIGHT		= 500;
	public static final double FLOOR_Y			= 350;
	public static final double NET_WIDTH		= 4;
	public static final double NET_HEIGHT   	= 41;
	public static final double NET_DEPTH		= 4;
	
	public static final int SCORE_PAUSE_TIME    = 1000;
	public static final int MAX_SCORE           = 6;
	public static final int MAX_PLAYERS         = 10;
	
	
	public static final Vector SCORE_OFFSET               = new Vector(15, 15);
	public static final Vector SCORE_RECT                 = new Vector(280, 35);
	public static final double POINT_RADIUS_SPACING_RATIO = 0.3714285714;
	public static final double MAX_POINT_RADIUS           = 13;
	
	
	
	
	public static final  Color[] colorOptions = {
		Color.RED,		Color.ORANGE,	Color.YELLOW,
		Color.GREEN,	Color.CYAN,		Color.BLUE,
		Color.MAGENTA,	Color.WHITE,	Color.BLACK
	};
}
