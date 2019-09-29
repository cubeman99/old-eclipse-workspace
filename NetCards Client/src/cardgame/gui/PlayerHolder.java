package cardgame.gui;

public class PlayerHolder {
	private String name;
	private boolean ready;
	private int score;
	
	public PlayerHolder(String name) {
		this.name  = name;
		this.ready = false;
		this.score = 0;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public boolean getReady() {
		return ready;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}

	public void givePoint() {
		score += 1;
	}
	
}
