package simulator;

public class GateAnd extends LogicGate {
	
	public GateAnd() {this(0, 0);}
	
	public GateAnd(int x, int y) {
		super(x, y, 1, 2);
		setImage("gateAnd");
		addInputPoint(-1, 0);
		addInputPoint(-1, 1);
		addOutputPoint(1, 0);
	}
	
	public void compute() {
		setOutput(getInput(0) && getInput(1));
	}
}
