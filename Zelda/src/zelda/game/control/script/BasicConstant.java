package zelda.game.control.script;

public class BasicConstant extends Constant {
	private String value;
	
	public BasicConstant(String name, String value) {
		super(name);
		this.value = value;
	}
	
	public BasicConstant(String name, boolean value) {
		super(name);
		this.value = "" + value;
	}
	
	public BasicConstant(String name, int value) {
		super(name);
		this.value = "" + value;
	}
	
	public BasicConstant(String name, double value) {
		super(name);
		this.value = "" + value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
