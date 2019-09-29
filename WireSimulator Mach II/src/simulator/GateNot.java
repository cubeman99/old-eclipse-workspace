package simulator;

public class GateNot extends LogicGate {
	
	public GateNot() {this(0, 0);}
	
	public GateNot(int x, int y) {
		super(x, y, 1, 1);
		setImage("gateNot");
		addInputPoint(-1, 0);
		addOutputPoint(1, 0);
	}
	
	public void compute() {
		setOutput(!getInput());
	}
}
