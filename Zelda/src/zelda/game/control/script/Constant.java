package zelda.game.control.script;

public abstract class Constant {
	protected String name;
	
	public Constant(String name) {
		this.name = name;
	}

	public abstract String getValue();
	
	public String getName() {
		return name;
	}
	
}
