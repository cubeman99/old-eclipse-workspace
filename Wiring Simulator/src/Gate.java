import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;


public class Gate {
	public int x;
	public int y;
	public int width;
	public int height;
	public Image image;
	public ArrayList<DataPoint> inputs;
	public ArrayList<DataPoint> outputs;
	
	public Gate(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width  = width;
		this.height = height;
//		this.image = image;
		inputs = new ArrayList<DataPoint>();
		outputs = new ArrayList<DataPoint>();
	}
	
	public void addInputPoint(int x, int y) {
		inputs.add(new DataPoint(x, y, this.x, this.y));
	}

	public void addInputPoint(int x, int y, String label) {
		inputs.add(new DataPoint(x, y, this.x, this.y, label));
	}
	
	public void addOutputPoint(int x, int y) {
		DataPoint D = new DataPoint(x, y, this.x, this.y);
		D.isOutput = true;
		outputs.add(D);
	}
	
	public void addOutputPoint(int x, int y, String label) {
		DataPoint D = new DataPoint(x, y, this.x, this.y, label);
		D.isOutput = true;
		outputs.add(D);
	}
	
	public String getInLabel(int index) {
		return inputs.get(index).label;
	}

	public String getInLabel() {
		return inputs.get(0).label;
	}

	public String getOutLabel(int index) {
		return outputs.get(index).label;
	}

	public String getOutLabel() {
		return outputs.get(0).label;
	}
	
	public boolean getIn(int index) {
		return inputs.get(index).getState();
	}

	public boolean getIn() {
		return inputs.get(0).getState();
	}

	public boolean getOut(int index) {
		return outputs.get(index).getState();
	}

	public boolean getOut() {
		return outputs.get(0).getState();
	}
	
	public void setOut(int index, boolean state) {
		outputs.get(index).state = state;
	}
	
	public void setOut(boolean state) {
		outputs.get(0).state = state;
	}

	public boolean inBounds(int x, int y) {
		return (x >= this.x && y >= this.y && x < this.x + width && y < this.y + height);
	}
	
	public void perform() {
		// PUT OPERATIONS HERE!
	}
	
	public void update() {
		perform();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		int gs = WireSimulator.GRID_SIZE;
		int dw = 1 + (width * 2);
		int dh = 1 + (height * 2);
		g.drawRect(x * gs, y * gs, dw * gs, dh * gs);
		//g.drawImage(image, x, y, null);
	}
}
