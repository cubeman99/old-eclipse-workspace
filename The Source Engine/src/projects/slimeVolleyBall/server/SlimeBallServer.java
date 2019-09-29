package projects.slimeVolleyBall.server;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import main.GameRunner;
import projects.slimeVolleyBall.Ball;
import projects.slimeVolleyBall.Settings;
import projects.slimeVolleyBall.Slime;
import projects.slimeVolleyBall.SlimeTestRunner;
import projects.slimeVolleyBall.Team;
import projects.slimeVolleyBall.client.MessageDecoderClient;
import projects.slimeVolleyBall.common.Message;
import common.Draw;
import common.GMath;
import common.Timer;
import common.Vector;
import common.ViewControl;
import common.shape.Rectangle;

public class SlimeBallServer {
	private Team[] teams;
	private ArrayList<Slime> players;
	private Ball ball;
	private Team server;
	private GameRunner runner;
	private ViewControl viewControl;
	private Rectangle net;
	private Rectangle boundaries;
	private double centerX;
	private Timer scoreTimer;
	private int matchTime;
	private Timer roundTimer;
	public boolean playingMatch;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public SlimeBallServer(GameRunner testRunner) {
		runner      = testRunner;
		boundaries  = new Rectangle(0, 0, Settings.VIEW_WIDTH, Settings.VIEW_HEIGHT);;
		centerX     = Settings.VIEW_WIDTH * 0.5;
		scoreTimer  = new Timer(Settings.SCORE_PAUSE_TIME);
		viewControl = new ViewControl();
		
		players     = new ArrayList<Slime>();
		teams       = new Team[2];
		teams[0]    = new Team(0, Color.RED, 0, (Settings.VIEW_WIDTH * 0.5) - 4);
		teams[1]    = new Team(1, Color.GREEN, (Settings.VIEW_WIDTH * 0.5) + 4, Settings.VIEW_WIDTH);
		server      = teams[0];
		
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
//		roundTimer.start();

		testRunner.setTitle("Slime Volleyball Server");
		serve();
	}
	
	// =================== ACCESSORS =================== //
	
	public ArrayList<Slime> getPlayers() {
		return players;
	}
	
	/** Return which side the given point is on (0 = left, 1 = right). **/
	public int getSide(Vector point) {
		return (point.x < centerX ? 0 : 1);
	}
	
	/** Return the boundaries of the court. **/
	public Rectangle getBoundaries() {
		return boundaries;
	}
	
	/** Return which opposite side the given point is on (0 = left, 1 = right). **/
	public int getOppositeSide(Vector point) {
		return (point.x < centerX ? 1 : 0);
	}
	
	/** Return whether the game is paused. **/
	public boolean isPaused() {
		return (scoreTimer.isRunning());
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
	
	/** Return the team with the given index. **/
	public Team getTeam(int index) {
		return teams[0];
	}
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the game. **/
	public void update() {
		double zoom = GMath.min(runner.getViewWidth() / Settings.VIEW_WIDTH, runner.getViewHeight() / Settings.VIEW_HEIGHT);
		double xgap = (runner.getViewWidth() - (zoom * Settings.VIEW_WIDTH)) * 0.5;
		double ygap = (runner.getViewHeight() - (zoom * Settings.VIEW_HEIGHT)) * 0.5;
		viewControl.setPan(-xgap / zoom, -ygap / zoom);
		viewControl.setZoom(zoom);
		Draw.setView(viewControl);
		
		if (playingMatch) {
    		if (!isPaused()) {
    			ball.update(this);
    		}
    		else if (scoreTimer.isExpired()) {
				serve();
			}
		}
		
		MessageDecoder.broadcastMessage(new Message("btick", ball.getPosition().x, ball.getPosition().y));
	}
	
	/** Add a new player onto a team. **/
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
				MessageDecoder.broadcastMessage(new Message("removeplayer", s.getName()));
				players.remove(i);
				break;
			}
		}
	}
	
	/** Score a point for the given side. **/
	public void scorePoint(int side) {
		scoreTimer.start();
		teams[side].addScore();
		// teams[1 - side].removeScore();
		server = teams[side];
		matchTime += roundTimer.getSeconds();
		roundTimer.stop();

		MessageDecoder.broadcastMessage(new Message("roundscore", matchTime, teams[0].getScore(), teams[1].getScore()));

		// Check for a winner:
		if (teams[side].getScore() >= Settings.MAX_SCORE) {

		}
	}
	
	/** Setup a serve for the server. **/
	private void serve() {
		ball.getPosition().x = server.getCenterX();
		ball.getPosition().y = Settings.FLOOR_Y - Settings.BALL_SERVE_HEIGHT;
		scoreTimer.stop();
		roundTimer.start();
		MessageDecoder.broadcastMessage(new Message("serve"));
	}
	
	/** Start playing a match. **/
	public void startMatch() {
		playingMatch = true;
		matchTime    = 0;
		server       = teams[0];
		scoreTimer.stop();
		roundTimer.start();
		ball.getPosition().x = server.getCenterX();
		ball.getPosition().y = Settings.FLOOR_Y - Settings.BALL_SERVE_HEIGHT;
		MessageDecoder.broadcastMessage(new Message("startmatch", teams[0].getScore(), teams[1].getScore()));
	}
	
	public void checkMatchReady() {
		if (!playingMatch) {
			int[] count = new int[2];
			for (Slime s : players)
				count[s.getTeamIndex()]++;
			
			if (count[0] > 0 && count[1] > 0)
				startMatch();
		}
	}
	
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
	
	/** Draw the game. **/
	public void draw() {
		// Draw the sky and ground:
		Draw.setColor(Color.BLUE);
		Draw.fillRect(boundaries);
		Draw.setColor(Color.GRAY);
		Draw.fillRect(0, Settings.FLOOR_Y, boundaries.width(), boundaries.height() - Settings.FLOOR_Y);
		
		// Draw the score board:
		for (int i = 0; i < 6; i++) {
			if (i < teams[0].getScore()) {
				Draw.setColor(Color.YELLOW);
				Draw.fillCircle(32 + (i * 35), 32, 13);
			}
			else {
    			Draw.setColor(Color.WHITE);
    			Draw.drawCircle(32 + (i * 35), 32, 13);
			}
		}
		
		for (int i = 0; i < 6; i++) {
			if (i < teams[1].getScore()) {
				Draw.setColor(Color.YELLOW);
				Draw.fillCircle(boundaries.x2() -32 - (i * 35), 32, 13);
			}
			else {
    			Draw.setColor(Color.WHITE);
    			Draw.drawCircle(boundaries.x2() - 32 - (i * 35), 32, 13);
			}
		}
		
		// Draw the timer:
		Draw.setColor(Color.WHITE);
		int seconds = matchTime + (roundTimer.isRunning() ? (int) roundTimer.getSeconds() : 0);
		String time = getFormattedTime(seconds);
		Draw.getGraphics().setFont(new Font("Ariel", Font.BOLD, (int) (12 * viewControl.zoom)));
		Draw.drawStringCentered("Time: " + time, centerX, 32);
		
		// Draw the net:
		Draw.setColor(Color.WHITE);
		Draw.fillRect(net);
		
		// Draw the slimes and the ball:
		for (Slime s : players)
			s.draw(ball);
		ball.draw();
	}

}
