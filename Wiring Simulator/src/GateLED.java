import java.awt.Image;


public class GateLED extends Gate {

	public GateLED(int x, int y) {
//		super(x, y, ImageLoader.gateLightOff);
		super(x, y, 1, 1);

		addInputPoint(0, 0, "I");
	}
	
	public void perform() {
		if (getIn())
			image = ImageLoader.gateLightOn;
		else
			image = ImageLoader.gateLightOff;
	}
}
