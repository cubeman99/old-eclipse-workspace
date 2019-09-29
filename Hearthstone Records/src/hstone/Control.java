package hstone;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import hstone.character.Character;
import hstone.character.Mage;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Rectangle;

/*

Number of Games
Number of Wins
Number of Losses
Win Rate

Best Machups
Worst Machups

Last Played Timestamp

Timeline of Matches

Graph of (wins - losses)

Pie Chart for player classes
Pie Chart for opponent classes

Win Rates per class
Win rates against classes

Win/Loss Streaks

*/

public class Control {
	private GameRunner runner;
	private Character[] characters;
	private ArrayList<Match> matches;
	private ArrayList<Match> timelineMatches;
	private Table table;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;
	private PieChart playerChart;
	private PieChart opponentChart;
	private ArrayList<Matchup> matchups;
	private Graph graphWins;
	
	
	// ================== CONSTRUCTORS ================== //

	public Control(GameRunner runner) {
		this.runner = runner;
		dateFormat  = new SimpleDateFormat("MM/dd/yyyy");
		timeFormat  = new SimpleDateFormat("hh:mm a");
		table       = new Table(9);
		matches     = new ArrayList<Match>();
		timelineMatches = new ArrayList<Match>();
		matchups    = new ArrayList<Matchup>();
		playerChart   = new PieChart(100, 100, 70);
		opponentChart = new PieChart(400, 100, 70);
		graphWins     = new Graph();
		
		Color colorWin    = new Color(0,   128,   0);
		Color colorLoss   = new Color(255,   0,   0);
		Color colorRanked = new Color(255, 255, 153);
		Color colorCasual = new Color(204, 255, 255);
		
		characters = new Character[] {
			new Character("Druid",   "Malfurion Stormrage", new Color(255, 204, 153)),
			new Character("Hunter",  "Rexxar",              new Color(204, 255, 204)),
			new Character("Mage",    "Jaina Proudmoore",    new Color(204, 255, 255)),
			new Character("Paladin", "Uther Lightbringer",  new Color(255, 255, 153)),
			new Character("Priest",  "Anduin Wrynn",        new Color(255, 255, 255)),
			new Character("Rogue",   "Valeera Sanguinar",   new Color(192, 192, 192)),
			new Character("Shaman",  "Thrall",              new Color(153, 204, 255)),
			new Character("Warlock", "Gul'dan",             new Color(204, 153, 255)),
			new Character("Warrior", "Garrosh Hellscream",  new Color(255, 153,   0))
		};
		
		for (int i = 0; i < characters.length; i++) {
			playerChart.setColor(characters[i].getTitle(), characters[i].getColor());
			opponentChart.setColor(characters[i].getTitle(), characters[i].getColor());
			
			for (int j = 0; j < characters.length; j++) {
				matchups.add(new Matchup(characters[i], characters[j]));
			}
		}
		
		table.setColumnSpans(
				40, // Player Rank
				90, // Player Class
				40, // Opponent Rank
				90, // Opponent Class
				75, // Result
				75, // Mode
				90, // Date
				80, // Time
				600 // Comments
				);
		
		table.addRow(
			new Cell("Rank"),
			new Cell("Player"),
			new Cell("Rank"),
			new Cell("Opponent"),
			new Cell("Result"),
			new Cell("Mode"),
			new Cell("Date"),
			new Cell("Time"),
			new Cell("Comments")
		);
		
		loadMatches();

		Comparator<Matchup> comp = new Comparator<Matchup>() {
			@Override
			public int compare(Matchup m1, Matchup m2) {
				double rate1 = m1.getWinRate();
				double rate2 = m2.getWinRate();
				if (rate1 < rate2)
					return 1;
				else if (rate1 > rate2)
					return -1;

				double total1 = m1.getTotalMatches();
				double total2 = m2.getTotalMatches();
				if (total1 < total2)
					return 1;
				else if (total1 > total2)
					return -1;
				return 0;
			}
		};
		Collections.sort(matchups, comp);
		
		for (int i = 0; i < matchups.size(); i++) {
			Matchup matchup = matchups.get(i);
			System.out.print(matchup.getPlayer().getTitle() + " vs " + matchup.getOpponent().getTitle() + " - ");
			System.out.println((100 * matchup.getWinRate()) + "%");
		}
	}

	
	
	// =================== ACCESSORS =================== //
	
	public int numWins() {
		int count = 0;
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).isWin())
				count++;
		}
		return count;
	}
	
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
	public SimpleDateFormat getTimeFormat() {
		return timeFormat;
	}
	
	public Character[] getCharacters() {
		return characters;
	}
	
	public ArrayList<Match> getMatches() {
		return matches;
	}
	
	public Match getMatch(int index) {
		return matches.get(index);
	}
	
	public int numMatches() {
		return matches.size();
	}
	
	public int numCharacters() {
		return characters.length;
	}
	
	public Character getCharacter(int index) {
		return characters[index];
	}
	
	public Character getCharacter(String title) {
		for (int i = 0; i < characters.length; i++) {
			if (characters[i].getTitle().equalsIgnoreCase(title))
				return characters[i];
		}
		return characters[0];
	}
	
	public Matchup getMatchup(Match match) {
		for (int i = 0; i < matchups.size(); i++) {
			Matchup mup = matchups.get(i);
			if (mup.getPlayer() == match.getPlayer()
				&& mup.getOpponent() == match.getOpponent())
			{
				return mup;
			}
		}
		return null;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Load the matches from the Excel sheet. **/
	public void loadMatches() {
		try {
			String filename    = "C:/Users/David/Documents/Hearthstone Log.xls";
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
		    HSSFWorkbook wb    = new HSSFWorkbook(fs);
		    HSSFSheet sheet    = wb.getSheetAt(0);
		    int r              = 15;
		    int blankCount     = 0;
		    
	    	while (blankCount <= 1) {
		    	HSSFRow row = sheet.getRow(r);
		    	
		    	if (row.getCell(2) != null && row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    		Match m = new Match(this, matches.size(), row);
		    		addMatch(m);
		    		blankCount = 0;
		    	}
		    	else
		    		blankCount++;
		    	
		    	r++;
		    }
		}
		catch ( IOException ex ) {
			ex.printStackTrace();
		}
	}
	
	public void addMatch(Match m) {
		matches.add(m);
		table.addRow(m.getRowCells());
		
		playerChart.add(m.getPlayer().getTitle());
		opponentChart.add(m.getOpponent().getTitle());
		getMatchup(m).addMatch(m.isWin());
		int a = m.isWin() ? 1 : -1;	
		if (graphWins.size() > 0)
			a += graphWins.getData(graphWins.size() - 1);
		graphWins.addData(a);
	}
	
	public void update() {
		if (timelineMatches.size() > 0) {
			addMatch(timelineMatches.get(0));
			timelineMatches.remove(0);
		}
		
		if (Keyboard.enter.pressed() && timelineMatches.size() == 0) {
			for (int i = 0; i < matchups.size(); i++)
				matchups.get(i).reset();
			playerChart.reset();
			opponentChart.reset();
			graphWins.clear();
			
			while (matches.size() > 0) {
				timelineMatches.add(matches.get(0));
				matches.remove(0);
			}
		}
		
		if (Keyboard.insert.pressed() || Keyboard.end.down()) {
			addMatch(new Match(this, matches.size()));
		}
		
		if (Keyboard.escape.pressed())
			runner.end(true);
	}
	
	public void draw() {
		double width  = runner.getViewWidth();
		double height = runner.getViewHeight();
		
		Draw.setColor(Color.LIGHT_GRAY);
		Draw.fillRect(0, 0, width, height);

		Rectangle rectStats        = new Rectangle(0, 0, width, height * 0.1);
		Rectangle rectMatchups     = new Rectangle(0, rectStats.getY2(), width / 4, height * 0.4);
		Rectangle rectHeroes       = new Rectangle(rectMatchups.getX2(), rectStats.getY2(), width / 4, height * 0.4);
		Rectangle rectTimeControl  = new Rectangle(0, rectMatchups.getY2(), width / 2, height * 0.15);
		Rectangle rectCharts       = new Rectangle(width / 2, rectStats.getY2(), width / 2, height * 0.55);
		Rectangle rectTimeline     = new Rectangle(0, rectCharts.getY2(), width, height * 0.25);
		Rectangle rectMatchHistory = new Rectangle(0, rectTimeline.getY2(), width, height * 0.1);

		opponentChart.getCircle().position.set(rectCharts.getPoint(0.8, 0.24));
		opponentChart.getCircle().radius = rectCharts.getMinSize() * 0.18;
		playerChart.getCircle().position.set(rectCharts.getPoint(0.4, 0.6));
		playerChart.getCircle().radius = rectCharts.getMinSize() * 0.35;
		
		int numWins    = 0;
		int numLosses  = 0;
		double winRate = 0;
		
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).isWin())
				numWins++;
			else
				numLosses++;
		}
		if (numWins + numLosses > 0)
			winRate = numWins / (double) (numWins + numLosses);
		
		DecimalFormat df = new DecimalFormat("#.##");
		String[] barValues = new String[] {
			df.format(100 * winRate) + "%",
			numWins + "",
			numLosses + "",
			"" + matches.size()
		};
		String[] barLabels = new String[] {
			"Win Rate",
			"Wins",
			"Losses",
			"Total"
		};
		
		


		Draw.setColor(Color.WHITE);
		Draw.fill(rectMatchups);
		double matchSpan = 1 / 20.0;
		
		Comparator<Matchup> comp = new Comparator<Matchup>() {
			@Override
			public int compare(Matchup m1, Matchup m2) {
				double rate1 = m1.getWinRate();
				double rate2 = m2.getWinRate();
				if (rate1 < rate2)
					return 1;
				else if (rate1 > rate2)
					return -1;

				double total1 = m1.getTotalMatches();
				double total2 = m2.getTotalMatches();
				if (total1 < total2)
					return 1;
				else if (total1 > total2)
					return -1;
				return 0;
			}
		};
		
		Draw.setColor(Color.WHITE);
		Draw.fill(rectHeroes);
		for (int i = 0; i < characters.length; i++) {
			int wins   = 0;
			int losses = 0;
			int oppWins   = 0;
			int oppLosses = 0;
			
			for (int j = 0; j < matchups.size(); j++) {
				Matchup m = matchups.get(j);
				if (m.getPlayer() == characters[i]) {
					wins   += m.getWins();
					losses += m.getLosses();
				}
				if (m.getOpponent() == characters[i]) {
					oppWins   += m.getWins();
					oppLosses += m.getLosses();
				}
			}

			int total = wins + losses;
			int oppTotal = oppWins + oppLosses;
			double heroRate = 0;
			double oppRate = 0;
			if (total > 0)
				heroRate = wins / (double) (wins + losses);
			if (oppTotal > 0)
				oppRate = oppWins / (double) (oppWins + oppLosses);

			Rectangle box = rectHeroes.createSubScaleRect(0, i * (1/9.0), 1, (1/9.0));
			Rectangle classBox = box.createSubScaleRect(0, 0, 0.4, 1);
			Rectangle rateBox  = box.createSubScaleRect(0.4, 0, 0.3, 1);
			Rectangle totalBox = box.createSubScaleRect(0.7, 0, 0.3, 1);

			Draw.setColor(characters[i].getColor());
			Draw.fill(classBox);
			
			Draw.setColor(Color.BLACK);
			Draw.draw(classBox);
			Draw.draw(rateBox);
			Draw.draw(totalBox);

			Draw.drawString(df.format(100 * heroRate) + "%", rateBox.getCenter(), Draw.CENTER, Draw.MIDDLE);
			Draw.drawString(df.format(100 * oppRate) + "%", totalBox.getCenter(), Draw.CENTER, Draw.MIDDLE);
			//Draw.drawString(total + "", totalBox.getCenter(), Draw.CENTER, Draw.MIDDLE);
		}
		
		Collections.sort(matchups, comp);
		int matchIndex = 0;
		for (int i = 0; i < matchups.size(); i++) {
			Matchup matchup = matchups.get(i);
			if (matchup.getTotalMatches() == 0)
				continue;
			Rectangle box = rectMatchups.createSubScaleRect(0, matchIndex * matchSpan, 1, matchSpan);
			matchIndex++;
			
			Rectangle classBox = box.createSubScaleRect(0, 0, 0.4, 1);
			Rectangle rateBox  = box.createSubScaleRect(0.4, 0, 0.3, 1);
			Rectangle totalBox = box.createSubScaleRect(0.7, 0, 0.3, 1);
			
			Polygon playerPoly   = new Polygon();
			Polygon opponentPoly = new Polygon();
			playerPoly.addVertex(classBox.getPoint(0, 0));
			playerPoly.addVertex(classBox.getPoint(0.5, 0));
			playerPoly.addVertex(classBox.getPoint(0.6, 0.5));
			playerPoly.addVertex(classBox.getPoint(0.5, 1));
			playerPoly.addVertex(classBox.getPoint(0, 1));
			opponentPoly.addVertex(classBox.getPoint(0.5, 0));
			opponentPoly.addVertex(classBox.getPoint(1, 0));
			opponentPoly.addVertex(classBox.getPoint(1, 1));
			opponentPoly.addVertex(classBox.getPoint(0.5, 1));
			opponentPoly.addVertex(classBox.getPoint(0.6, 0.5));
			
			Draw.setColor(Color.WHITE);
			Draw.fill(box);
			Draw.fill(rateBox);
			Draw.fill(totalBox);
			Draw.setColor(matchup.getPlayer().getColor());
			Draw.fill(playerPoly);
			Draw.setColor(matchup.getOpponent().getColor());
			Draw.fill(opponentPoly);
			
			Draw.setColor(Color.BLACK);
			Draw.draw(box);
			Draw.draw(rateBox);
			Draw.draw(totalBox);
			Draw.draw(playerPoly);
			Draw.draw(opponentPoly);
			
			Draw.drawString(df.format(100 * matchup.getWinRate()) + "%", rateBox.getCenter(), Draw.CENTER, Draw.MIDDLE);
			Draw.drawString(matchup.getTotalMatches() + "", totalBox.getCenter(), Draw.CENTER, Draw.MIDDLE);
		}
		
		Draw.setColor(Color.BLACK);
		Draw.draw(rectMatchups);

		Draw.setColor(Color.WHITE);
		Draw.fill(rectStats);
		Draw.fill(rectTimeControl);
		Draw.fill(rectCharts);
		Draw.fill(rectTimeline);
		Draw.fill(rectMatchHistory);
		
		Draw.setColor(Color.BLACK);
		Draw.draw(rectStats);
		Draw.draw(rectTimeControl);
		Draw.draw(rectCharts);
		Draw.draw(rectTimeline);
		Draw.draw(rectMatchHistory);
		
		graphWins.setBounds(rectTimeline);
		graphWins.draw();
		
		for (int i = 0; i < 4; i++) {
			double x = rectStats.getX1() + (rectStats.getWidth() * ((i + 0.5) / 4.0));
			Draw.drawString(barValues[i], x, rectStats.getY1() + (rectStats.getHeight() * 0.5), Draw.CENTER, Draw.MIDDLE);
			Draw.drawString(barLabels[i], x, rectStats.getY1() + (rectStats.getHeight() * 0.75), Draw.CENTER, Draw.MIDDLE);
			
			if (i < 3) {
				double dx = rectStats.getX1() + (rectStats.getWidth() * ((i + 1) / 4.0));
				Draw.setColor(Color.BLACK);
				Draw.drawLine(dx, rectStats.getY1(), dx, rectStats.getY2());
			}
		}
		
		double boxSpan = width * 0.045;
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(matches.size() - i - 1);
			
			Rectangle box = new Rectangle(rectMatchHistory.getX2() - ((i + 1) * boxSpan),
					rectMatchHistory.getY1(), boxSpan, rectMatchHistory.getHeight());
			Rectangle classBox   = box.createSubScaleRect(0, 0, 1, 0.65);
			Rectangle winBox     = box.createSubScaleRect(0, 0.65, 1, 0.2);
			Rectangle modeBox    = box.createSubScaleRect(0, 0.85, 1, 0.15);
			Polygon playerPoly   = new Polygon();
			Polygon opponentPoly = new Polygon();
			playerPoly.addVertex(classBox.getPoint(0, 0));
			playerPoly.addVertex(classBox.getPoint(1, 0));
			playerPoly.addVertex(classBox.getPoint(1, 0.4));
			playerPoly.addVertex(classBox.getPoint(0.5, 0.7));
			playerPoly.addVertex(classBox.getPoint(0, 0.4));
			opponentPoly.addVertex(classBox.getPoint(0, 1));
			opponentPoly.addVertex(classBox.getPoint(1, 1));
			opponentPoly.addVertex(classBox.getPoint(1, 0.4));
			opponentPoly.addVertex(classBox.getPoint(0.5, 0.7));
			opponentPoly.addVertex(classBox.getPoint(0, 0.4));
			
			Draw.setColor(match.getPlayer().getColor());
			Draw.fill(playerPoly);
			Draw.setColor(match.getOpponent().getColor());
			Draw.fill(opponentPoly);
			Draw.setColor(match.isWin() ? Match.COLOR_WIN : Match.COLOR_LOSS);
			Draw.fill(winBox);
			Draw.setColor(match.isRanked() ? Match.COLOR_RANKED : Match.COLOR_CASUAL);
			Draw.fill(modeBox);
			
			Draw.setColor(Color.BLACK);
			Draw.draw(box);
			Draw.draw(modeBox);
			Draw.draw(winBox);
			Draw.draw(playerPoly);
			Draw.draw(opponentPoly);
		}
		
		playerChart.draw();
		opponentChart.draw();
	}
}
