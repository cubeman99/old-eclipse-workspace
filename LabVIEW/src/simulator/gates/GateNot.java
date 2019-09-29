package simulator.gates;

import java.awt.Image;
import simulator.datatypes.Type;

public class GateNot extends Gate {
	public static String IMAGE_NAME = "gateNot";
	

	public GateNot() {this(0, 0);}
	
	public GateNot(int x, int y) {
		super(x, y);

		addInputPoint(0, 8, true, Type.BOOLEAN);
		addInputPoint(16, 8, true, Type.BOOLEAN);
	}
	
	public String getImageName() {
		return IMAGE_NAME;
	}
	
	public void compute() {
		setBool(0, !getBool(0));
	}
	
}
