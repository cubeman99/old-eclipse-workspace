package simulator;

public class GateOr extends LogicGate {
	
	public GateOr() {this(0, 0);}
	
	public GateOr(int x, int y) {
		super(x, y, 1, 2);
		setImage("gateOr");
		addInputPoint(-1, 0);
		addInputPoint(-1, 1);
		addOutputPoint(1, 0);
	}
	
	public void compute() {
		setOutput(getInput(0) || getInput(1));
	}
}
