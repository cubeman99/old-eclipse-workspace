package zelda.game.entity.logic;

public abstract class LogicPuzzle extends LogicEntity {
	
	
	public abstract boolean isSolved();
	
	protected void onSolve() {}
	
	public void solve() {
		properties.set("solved", true);
		properties.script("event_solve", this, frame);
		onSolve();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (isSolved())
			solve();
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solved", false);
		objectData.addEvent("event_solve", "Called when the puzzle is solved.");
	}
}
