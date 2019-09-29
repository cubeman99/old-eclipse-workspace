package simulator.gates;

import java.awt.Image;
import common.Point;
import simulator.datatypes.Type;

public class GateAnd extends Gate {
	public static String IMAGE_NAME = "gateAnd";
	
	
	public GateAnd() {this(0, 0);}
	
	public GateAnd(int x, int y) {
		super(x, y);
		
		addInputPoint(0, 4, true, Type.BOOLEAN);
		addInputPoint(0, 11, true, Type.BOOLEAN);
		addOutputPoint(21, 8, true, Type.BOOLEAN);
	}
	
	public String getImageName() {
		return IMAGE_NAME;
	}
	
	public void compute() {
		setBool(0, getBool(0) && getBool(1));
	}
}
