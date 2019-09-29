package hstone;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.math.geometry.Point;
import cmg.math.geometry.Rectangle;

public class Graph {
	private Rectangle box;
	private ArrayList<Integer> data;
	
	public Graph() {
		box  = new Rectangle(0, 0, 16, 16);
		data = new ArrayList<Integer>();
	}
	
	public void addData(int value) {
		data.add(value);
	}
	
	public int[] getMinMax() {
		int[] minmax = new int[] {0, 0};
		for (int i = 0; i < data.size(); i++) {
			int a = data.get(i);
			if (a < minmax[0])
				minmax[0] = a;
			if (a > minmax[1])
				minmax[1] = a;
		}
		return minmax;
	}
	
	public int size() {
		return data.size();
	}
	
	public int getData(int index) {
		return data.get(index);
	}
	
	public void setBounds(Rectangle bounds) {
		this.box = bounds;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void draw() {
		int[] minmax = getMinMax();
		int min      = minmax[0];
		int max      = minmax[1];
		Draw.setColor(Color.BLACK);
		
		if (max - min > 0) {
			double base = 1 - ((-min) / (double) (max - min));
			Draw.drawLine(box.getPoint(0, base), box.getPoint(1, base));
			
    		for (int i = 1; i < data.size(); i++) {
    			double x1  = (i - 1) / (double) data.size();
    			double x2  = i / (double) data.size();
    			double y1 = 1 - ((data.get(i - 1) - min) / (double) (max - min));
    			double y2 = 1 - ((data.get(i) - min) / (double) (max - min));
    			
    			Draw.drawLine(box.getPoint(x1, y1), box.getPoint(x2, y2));
    		}
		}
	}
}
