package hstone;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import cmg.math.GMath;
import hstone.character.Character;

public class Match {
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("MM/dd/yyyy");
	public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("hh:mm a");
	public static final Color COLOR_WIN    = new Color(0,   128,   0);
	public static final Color COLOR_LOSS   = new Color(255,   0,   0);
	public static final Color COLOR_CASUAL = new Color(204, 255, 255);
	public static final Color COLOR_RANKED = new Color(255, 255, 153);
	public static final int MAX_RANK = 25;
	public static final int MIN_RANK = 1;
	
	private int index;
	private Character player;
	private Character opponent;
	private int playerRank;
	private int opponentRank;
	private boolean win;
	private boolean ranked;
	private String comment;
	private Date date;
	
	
	
	// ================== CONSTRUCTORS ================== //

	/** Construct an blank match. **/
	public Match(Control control, int index) {
		this.index   = index;
		player       = control.getCharacter(GMath.random.nextInt(9));
		opponent     = control.getCharacter(GMath.random.nextInt(9));
		win          = GMath.random.nextBoolean();
		ranked       = GMath.random.nextBoolean();
		playerRank   = 1 + GMath.random.nextInt(25);
		opponentRank = 1 + GMath.random.nextInt(25);
		comment      = "";
		date         = new Date();
	}
	
	/** Construct a match from an Excel row. **/
	public Match(Control control, int index, HSSFRow data) {
		this.index   = index;
		playerRank   = 0;
		opponentRank = 0;
		date = new Date();
		
		if (data.getCell(1) != null && data.getCell(1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
			playerRank = (int) (data.getCell(1).getNumericCellValue() + 0.5);
		if (data.getCell(3) != null && data.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
			opponentRank = (int) (data.getCell(3).getNumericCellValue() + 0.5);

		player   = control.getCharacter(data.getCell(2).getStringCellValue());
		opponent = control.getCharacter(data.getCell(4).getStringCellValue());
		win      = data.getCell(5).getStringCellValue().equalsIgnoreCase("win");
		ranked   = data.getCell(6).getStringCellValue().equalsIgnoreCase("ranked");
		comment  = "";
		if (data.getCell(9) != null)
			comment  = data.getCell(9).getStringCellValue();
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String strDate = control.getDateFormat().format(data.getCell(7).getDateCellValue());
		String strTime = control.getTimeFormat().format(data.getCell(8).getDateCellValue());
		try {
			date = format.parse(strDate + " " + strTime);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	// =================== ACCESSORS =================== //
	
	public Cell[] getRowCells() {
		Cell[] row = new Cell[9];
		for (int i = 0; i < row.length; i++)
			row[i] = new Cell();

		row[1].setText(player.getTitle());
		row[1].setBackgroundColor(player.getColor());
		row[1].setBold(true);
		row[3].setText(opponent.getTitle());
		row[3].setBackgroundColor(opponent.getColor());
		row[3].setBold(true);
		row[4].setText(win ? "Win" : "Loss");
		row[4].setTextColor(Color.WHITE);
		row[4].setBackgroundColor(win ? COLOR_WIN : COLOR_LOSS);
		row[4].setBold(true);
		row[5].setText(ranked ? "Ranked" : "Casual");
		row[5].setBackgroundColor(ranked ? COLOR_RANKED : COLOR_CASUAL);
		row[5].setBold(true);
		row[6].setText(FORMAT_DATE.format(date));
		row[7].setText(FORMAT_TIME.format(date));
		row[8].setText(comment);
		
		if (ranked) {
    		row[0].setText(playerRank + "");
    		row[2].setText(opponentRank + "");
		}
		
		return row;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Character getPlayer() {
		return player;
	}
	
	public Character getOpponent() {
		return opponent;
	}
	
	public int getPlayerRank() {
		return playerRank;
	}
	
	public int getOpponentRank() {
		return opponentRank;
	}
	
	public String getComment() {
		return comment;
	}
	
	public boolean isWin() {
		return win;
	}
	
	public boolean isLoss() {
		return !win;
	}
	
	public boolean isCasual() {
		return !ranked;
	}
	
	public boolean isRanked() {
		return ranked;
	}
	
	public Date getDate() {
		return date;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setPlayer(Character player) {
		this.player = player;
	}
	
	public void setOpponent(Character opponent) {
		this.opponent = opponent;
	}
	
	public void setPlayerRank(int playerRank) {
		this.playerRank = playerRank;
	}
	
	public void setOpponentRank(int opponentRank) {
		this.opponentRank = opponentRank;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setRanked(boolean ranked) {
		this.ranked = ranked;
	}
	
	public void setWin(boolean win) {
		this.win = win;
	}
}
