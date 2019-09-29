import java.awt.Image;


public class GateInvert extends Gate {

	public GateInvert(int x, int y) {
//		super(x, y, ImageLoader.gateInvert);
		super(x, y, 2, 1);

		addInputPoint(0, 0, "I");
		addOutputPoint(1, 0, "O");
	}
	
	public void perform() {
		setOut(!getIn());
	}
}
