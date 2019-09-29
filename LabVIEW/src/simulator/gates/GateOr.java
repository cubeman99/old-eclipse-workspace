package simulator.gates;

import java.awt.Image;
import simulator.datatypes.Type;

public class GateOr extends Gate {
	public static String IMAGE_NAME = "gateOr";
	

	public GateOr() {this(0, 0);}
	
	public GateOr(int x, int y) {
		super(x, y);
		
		addInputPoint(3, 4, true, Type.BOOLEAN);
		addInputPoint(3, 11, true, Type.BOOLEAN);
		addOutputPoint(21, 8, true, Type.BOOLEAN);
	}
	
	public String getImageName() {
		return IMAGE_NAME;
	}
	
	public void compute() {
		setBool(0, getBool(0) || getBool(1));
	}
	
}
