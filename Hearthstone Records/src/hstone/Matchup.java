package hstone;

import hstone.character.Character;

public class Matchup {
	private Character player;
	private Character opponent;
	private int wins;
	private int losses;
	

	
	// ================== CONSTRUCTORS ================== //

	public Matchup(Character player, Character opponent) {
		this.player   = player;
		this.opponent = opponent;
		this.wins     = 0;
		this.losses   = 0;
	}

	
	
	// =================== ACCESSORS =================== //
	
	public int getWins() {
		return wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public int getTotalMatches() {
		return wins + losses;
	}
	
	public double getWinRate() {
		if (wins + losses == 0)
			return 0;
		return wins / (double) (wins + losses);
	}
	
	public Character getPlayer() {
		return player;
	}
	
	public Character getOpponent() {
		return opponent;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void reset() {
		wins   = 0;
		losses = 0;
	}
	
	public void addMatch(boolean win) {
		if (win)
			wins++;
		else
			losses++;
	}
}
