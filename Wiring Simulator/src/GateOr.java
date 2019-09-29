import java.awt.Image;


public class GateOr extends Gate {

	public GateOr(int x, int y) {
//		super(x, y, ImageLoader.gateOr);
		super(x, y, 4, 4);
		
		addInputPoint(1, 3, "A");
		addInputPoint(1, 15, "B");
		addOutputPoint(24, 9, "O");
	}
	
	public void perform() {
		setOut(getIn(0) || getIn(1));
	}
}
