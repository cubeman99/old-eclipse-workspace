package projects.slimeVolleyBall;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import projects.slimeVolleyBall.client.ClientConnection;
import projects.slimeVolleyBall.client.MessageDecoderClient;
import projects.slimeVolleyBall.common.Message;
import projects.slimeVolleyBall.server.MessageDecoder;
import main.GameRunner;
import main.Mouse;
import common.Draw;
import common.GMath;
import common.Timer;
import common.Vector;
import common.ViewControl;
import common.shape.Rectangle;


/**
 * A game of Slime Ball!
 * 
 * @author David Jordan
 */
public class SlimeBallRunner {
	
	public Slime myPlayer;
	public ArrayList<Slime> players;
	public Team[] teams;
	public Ball ball;
	
	public Rectangle boundaries;
	public double centerX;
	public Rectangle net;
	public boolean scorePaused;
	private int matchTime;
	private Timer roundTimer;
	private GameRunner runner;
	private ViewControl viewControl;
	private boolean inGame;
	public boolean playingMatch;
	private Color slimeColor;
	private int slimeColorIndex;
	private boolean choosingColor;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public SlimeBallRunner(SlimeTestRunner testRunner) {
		runner      = testRunner;
		boundaries  = new Rectangle(0, 0, Settings.VIEW_WIDTH, Settings.VIEW_HEIGHT);;
		centerX     = Settings.VIEW_WIDTH * 0.5;
		scorePaused = false;
		viewControl = new ViewControl();
		
		players     = new ArrayList<Slime>();
		teams       = new Team[2];
		teams[0]    = new Team(0, Color.RED, 0, (Settings.VIEW_WIDTH * 0.5) - 4);
		teams[1]    = new Team(1, Color.GREEN, (Settings.VIEW_WIDTH * 0.5) + 4, Settings.VIEW_WIDTH);
		myPlayer    = null;
		inGame      = false;
		choosingColor   = true;
		slimeColor      = Color.RED;
		slimeColorIndex = 0;
		net         = new Rectangle(
			centerX - (Settings.NET_WIDTH * 0.5),
			Settings.FLOOR_Y - Settings.NET_HEIGHT + Settings.NET_DEPTH,
			centerX + (Settings.NET_WIDTH * 0.5),
			Settings.FLOOR_Y + Settings.NET_DEPTH
		);
		
		ball       = new Ball(net);
		
		playingMatch = false;
		matchTime    = 0;
		roundTimer   = new Timer();
		
		testRunner.setTitle("Slime Volleyball");
		
//		myPlayer = addPlayer(ClientConnection.clientName, 0);
		MessageDecoderClient.sendMessage(new Message("join"));
	}
	
	// =================== ACCESSORS =================== //
	
	
	/** Return whether the game is paused. **/
	public boolean isPaused() {
		return scorePaused;
	}
	
	/** Return the x center of the given side. **/
	public double getSideCenterX(int side) {
		return (centerX * (side == 0 ? 0.5 : 1.5));
	}
	
	/** Return the slime player with the given name. **/
	public Slime getPlayer(String name) {
		for (Slime s : players) {
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}
	
	// ==================== MUTATORS ==================== //
	
	/** Update the game. **/
	public void update() {
		double zoom = GMath.min(runner.getViewWidth() / Settings.VIEW_WIDTH, runner.getViewHeight() / Settings.VIEW_HEIGHT);
		double xgap = (runner.getViewWidth() - (zoom * Settings.VIEW_WIDTH)) * 0.5;
		double ygap = (runner.getViewHeight() - (zoom * Settings.VIEW_HEIGHT)) * 0.5;
		
		viewControl.setZoom(zoom);
		viewControl.setPan(-xgap / zoom, -ygap / zoom);
		Draw.setView(viewControl);
		
		if (!isPaused()) {
//			gameElapsedTime += 1.0 / runner.FPS;
			if (inGame) {
				myPlayer.update();
				MessageDecoderClient.sendMessage(new Message("ptick", myPlayer.getPosition().x,
						myPlayer.getPosition().y, myPlayer.getVelocity().x, myPlayer.getVelocity().y));
			}
		}
	}
	
	/** Join in the game on the given team index. **/
	private void joinTeam(int teamIndex) {
		inGame   = true;
		myPlayer = addPlayer(ClientConnection.clientName, teamIndex, slimeColorIndex);
		myPlayer.setColor(slimeColor);
		MessageDecoderClient.sendMessage(new Message("jointeam", teamIndex, slimeColorIndex));
	}
	
	/** Add a player to the game. **/
	public Slime addPlayer(String name, int teamIndex, int colorIndex) {
		Slime s = new Slime(name, teams[teamIndex], Settings.colorOptions[colorIndex]);
		players.add(s);
		return s;
	}
	
	/** Remove a player from the game. **/
	public void removePlayer(String name) {
		for (int i = 0; i < players.size(); i++) {
			Slime s = players.get(i);
			
			if (s.getName().equals(name)) {
				players.remove(i);
				break;
			}
		}
	}
	
	/** Score a point to the given team index. **/
	public void score(int teamIndex) {
		scorePaused = true;
		teams[teamIndex].addScore();
//		teams[1 - teamIndex].removeScore();
		matchTime += roundTimer.getSeconds();
		roundTimer.stop();
	}
	
	/** Reset the player to serving position.**/
	public void serve() {
		scorePaused = false;
		roundTimer.start();
		if (myPlayer != null)
			myPlayer.respawn();
	}
	
	/** Start playing a match. **/
	public void startMatch() {
		playingMatch = true;
		matchTime    = 0;
		serve();
	}
	
	/** End the current round. **/
	public void endRound() {
		roundTimer.stop();
		scorePaused = true;
	}
	
	/** Set the elapsed time for this match. **/
	public void setMatchTime(int matchTime) {
		this.matchTime = matchTime;
	}
	
	/** Return a formatted time in mm:ss. **/
	private String getFormattedTime(int time) {
		String str = "";
		int sec    = time % 60;
		int min    = time / 60;
		
		if (min < 10)
			str += "0";
		str += min + ":";
		if (sec < 10)
			str += "0";
		str += sec;
		
		return str;
	}
	
	private void drawTeamSelectBox(Team team) {
		Rectangle box = new Rectangle(0, 0, 248, 56);
		Vector center = team.getViewRect().getCenter();
		box.centerAt(center);

		setFont(12);
		Draw.setColor(Color.WHITE);
		Draw.fillRect(box);
		
		Draw.setColor(Color.BLUE);
		Draw.drawStringCentered("Click here to join this team!", center);
		Vector ms = Mouse.getVector(viewControl);
		
		if (box.contains(ms)) {
			if (Mouse.left.pressed()) {
				joinTeam(team.getIndex());
			}
		}
	}
	
	private void drawTeamView(Team team) {
		// Button to join a team:
		if (!inGame && !choosingColor)
    		drawTeamSelectBox(team);
		
		// List of players below the floor:
		setFont(12);
		Draw.setColor(Color.WHITE);
		int pointIndex  = 0;
		int playerCount = 0;
		Rectangle rect  = new Rectangle(0, 0, 0, 75);
		rect.centerAt(new Vector(team.getCenterX(), Settings.FLOOR_Y + 75));

		for (Slime s : players) {
			if (s.getTeam() == team)
				playerCount++;
		}
		
		Vector[] namePoints = GMath.makeGridPoints(rect, 1, playerCount, true);
		Draw.setColor(Color.WHITE);
//		Draw.drawRect(rect);
		
		for (Slime s : players) {
			if (s.getTeam() == team) {
				Draw.setColor(s.getColor());
				Draw.drawStringCentered(s.getName(), namePoints[pointIndex].x, namePoints[pointIndex].y);
				pointIndex++;
			}
		}
		
		if (playingMatch) {
			boolean overtime = (teams[0].getScore() > Settings.MAX_SCORE || teams[1].getScore() > Settings.MAX_SCORE);
    		double sign      = (team.getIndex() == 0 ? 1 : -1);
    		double align     = (team.getIndex() == 0 ? 0 : Settings.VIEW_WIDTH);
    		
			if (overtime) {
				double span = Settings.MAX_POINT_RADIUS / Settings.POINT_RADIUS_SPACING_RATIO;
				double dxx  = Settings.SCORE_OFFSET.x + (0.5 * span);
				double dyy  = Settings.SCORE_OFFSET.y + (Settings.SCORE_RECT.y * 0.5);
				
	    		setFont(20);
				Draw.setColor(Color.YELLOW);
				Draw.fillCircle(align + (sign * dxx), dyy, Settings.MAX_POINT_RADIUS);
				Draw.drawStringCentered(team.getScore() + "", align + (sign * (dxx + 64)), dyy);
				Draw.drawStringCentered("x", align + (sign * (dxx + 36)), dyy);
			}
			else {
        		// Draw the score board:
        		double maxSpan   = Settings.MAX_POINT_RADIUS / Settings.POINT_RADIUS_SPACING_RATIO;
        		double pointSpan = Math.min(maxSpan, Settings.SCORE_RECT.x / (double) Settings.MAX_SCORE);
        		double pointRad  = pointSpan * Settings.POINT_RADIUS_SPACING_RATIO;
        		
        		for (int i = 0; i < 6; i++) {
        			double dxx = Settings.SCORE_OFFSET.x + (((double) i + 0.5) * pointSpan);
        			double dyy = Settings.SCORE_OFFSET.y + (Settings.SCORE_RECT.y * 0.5);
        			
        			if (i < team.getScore()) {
        				Draw.setColor(Color.YELLOW);
        				Draw.fillCircle(align + (sign * dxx), dyy, pointRad);
        			}
        			else {
            			Draw.setColor(Color.WHITE);
            			Draw.drawCircle(align + (sign * dxx), dyy, pointRad);
        			}
        		}
			}
		}
	}
	
	private void drawColorOption(Vector position, int colorIndex) {
		boolean hover = (position.distanceTo(Mouse.getVector(viewControl)) < 38);

		Draw.setColor(Color.BLACK);
		Draw.fillCircle(position, 26 + (hover ? 8 : 0));
		Draw.setColor(Settings.colorOptions[colorIndex]);
		Draw.fillCircle(position, 24 + (hover ? 8 : 0));
		
		if (hover) {
			slimeColorIndex = colorIndex;
			slimeColor = Settings.colorOptions[colorIndex];
			
			if (Mouse.left.pressed()) {
				choosingColor = false;
			}
		}
	}
	
	private void drawColorChooser() {
		Rectangle rect  = new Rectangle(new Vector(Settings.VIEW_WIDTH * 0.3, Settings.VIEW_HEIGHT * 0.4));
		rect.centerAt(new Vector(Settings.VIEW_WIDTH * 0.5, Settings.VIEW_HEIGHT * 0.5));
		Vector[] points = GMath.makeGridPoints(rect, 3, 3, false);
		
		for (int i = 0; i < Math.min(points.length, Settings.colorOptions.length); i++)
			drawColorOption(points[i], i);
		
	}
	
	private void setFont(double size) {
		Draw.getGraphics().setFont(new Font("Ariel", Font.BOLD, (int) (size * viewControl.zoom)));
	}
	
	/** Draw the game. **/
	public void draw() {
		// Draw the sky and ground:
		Draw.setColor(Color.BLUE);
		Draw.fillRect(boundaries);
		Draw.setColor(Color.GRAY);
		Draw.fillRect(0, Settings.FLOOR_Y, boundaries.width(), boundaries.height() - Settings.FLOOR_Y);

		if (playingMatch) {
    		// Draw the timer:
    		Draw.setColor(Color.WHITE);
    		int seconds = matchTime + (roundTimer.isRunning() ? (int) roundTimer.getSeconds() : 0);
    		String time = getFormattedTime(seconds);
    		setFont(12);
    		Draw.drawStringCentered("Time: " + time, centerX, 32);
		}
		
		// Draw the net:
		Draw.setColor(Color.WHITE);
		Draw.fillRect(net);
		
		// Draw the slimes and the ball:
		for (Slime s : players)
			s.draw(ball);
		if (playingMatch)
			ball.draw();
		
		// Draw this client's player over everything else:
		if (myPlayer != null) {
			myPlayer.draw(ball);
			if (!playingMatch) {
				Draw.setColor(Color.WHITE);
				Draw.drawStringCentered("Waiting for more players...", teams[1 - myPlayer.getTeamIndex()].getViewRect().getCenter());
			}
		}
		
		// Draw Team specific items:
		drawTeamView(teams[0]);
		drawTeamView(teams[1]);
		
		if (choosingColor)
			drawColorChooser();
		
		if (!inGame) {
			Slime.drawSlime(new Vector(Settings.VIEW_WIDTH * 0.5, Settings.VIEW_HEIGHT - 48), 48, slimeColor, 0, GMath.QUARTER_PI);
		}
	}
}
