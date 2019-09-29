package projects.physicsHomework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Rectangle;
import cmg.math.geometry.Vector;

public class LabRunner extends GameRunner {
	private static final int WINDOW_WIDTH   = 600;
	private static final int WINDOW_HEIGHT  = 430;
	private static final int CANVAS_WIDTH   = 340;
	private static final int CANVAS_HEIGHT  = 240;
	private static final Color COLOR_GRAPH_BACKGROUND    = Color.WHITE;
	private static final Color COLOR_GRAPH_OUTLINES      = Color.BLACK;
	private static final Color COLOR_HISTOGRAM_BARS      = new Color(128, 128, 255, 255);
	private static final Color COLOR_HISTOGRAM_OUTLINES  = Color.BLACK;
	private static final Color COLOR_NORMAL_CURVE_AREA   = new Color(255, 255, 0, 40);
	private static final Color COLOR_NORMAL_CURVE        = Color.BLACK;
	private static final Color COLOR_GRAPH_OVERLAY_LINES = Color.LIGHT_GRAY;
	private static final Color COLOR_TEXT_LABELS         = Color.BLACK;
	private static final Color COLOR_NUMBER_LABELS       = Color.BLACK;
	
	private static final double[] binSizes = {
		0.18, // R Mass
		0.76, // R Length
		0.16, // R Width
		0.10, // R Height
		0.11, // C Mass
		0.11, // C Diameter
		0.12, // C Height
		0.10, // S Mass
		0.10  // S Diameter
	};
	
	private BufferedImage canvas;
	private ArrayList<List> datasets;
	private int setIndex;
	private double binSize;
	private Vector graphSize;
	private Vector canvasSize;
	
	
	
	public LabRunner() {
		super(60, WINDOW_WIDTH, WINDOW_HEIGHT, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		canvasSize = new Vector(WINDOW_WIDTH, WINDOW_HEIGHT);
		graphSize  = new Vector(CANVAS_WIDTH, CANVAS_HEIGHT);
		
		canvas = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		setIndex = 0;
		binSize = 0.15;
		
		// Load the data.
		String filename = "C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/lab1_class_data.txt";
		datasets = new ArrayList<List>();
		
		try {
			FileReader reader = new FileReader(filename);
			Scanner in = new Scanner(reader);
			
			List currentList = null;
			
			// File read loop.
			while (in.hasNextLine()) {
				String line = in.nextLine();
				
				if (line.length() == 0 || line.charAt(0) == ' ') {
					// Ignore line.
				}
				else if (line.charAt(0) == '#') {
					// Commented.
				}
				else if (line.charAt(0) == '@') {
					// End of file.
					if (line.startsWith("@eof"))
						break;
					
					// End of instruction.
					if (line.startsWith("@end") && currentList != null) {
						datasets.add(currentList);
						currentList = null;
					}
					
					// Data set.
					else if (line.startsWith("@dataset"))
						currentList = new List(line.substring("@dataset".length() + 1));
				}
				else if (currentList != null) {
					currentList.append(Double.parseDouble(line));
				}
			}
		}
		catch (IOException e) {
			System.err.println("File not found: \"" + filename + "\"");
		}
		
		graphHistogram(0, false);
	}
	
	public double normalFunction(double x, double mean, double sd) {
		return (1.0 / (sd * Math.sqrt(GMath.TWO_PI))) * Math.exp(-(GMath.sqr(x - mean) / (2 * sd * sd)));
	}
	
	public void graphHistogram(int setIndex, boolean save) {
		canvas = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Draw.getView().set(0, 0, 1.0);
		Draw.setGraphics(canvas.getGraphics());
		Draw.setColor(COLOR_GRAPH_BACKGROUND);
		Draw.fillRect(Vector.ORIGIN, canvasSize.plus(1, 1));
		Draw.getView().set(canvasSize.scaledBy(0.5f).minus(graphSize.scaledBy(0.5)).negate(), 1.0);
		double graphWidth  = graphSize.x;
		double graphHeight = graphSize.y;
		
		
		// Create bins.
		List set     = datasets.get(setIndex);
		List bins    = new List("bins");
		double min   = set.getMin();
		double max   = set.getMax();
		double range = max - min;
		
		for (double x = min; x <= max; x += binSize)
			bins.append(0);
		for (int j = 0; j < set.size(); j++) {
			int index = (int) ((set.getElement(j) - min) / binSize);
			if (index < 0)
				index = 0;
			if (index >= bins.size())
				bins.append(1);
			else
				bins.setElement(index, bins.getElement(index) + 1);
		}
		double xScale = graphWidth / bins.size();
		double maxBinNumber = 0;
		for (int j = 0; j < bins.size(); j++) {
			if (bins.getElement(j) > maxBinNumber)
				maxBinNumber = bins.getElement(j);
		}
		maxBinNumber *= 1.3;

		// Find the max normal.
		double mean = set.getAverage();
		double sd = set.getStandardDeviation();
		double maxNormal = 0;
		for (int j = 0; j < graphWidth; j++) {
			double xx = min + ((j / graphWidth) * range);
			double normal = normalFunction(xx, mean, sd);
			if (normal > maxNormal)
				maxNormal = normal;
		}
		maxNormal *= 1.3;
		
		// Draw horizontal graph lines.
		int labelSpacing = 1;
		for (int j = 0; j < 9.0; j++) {
			double yy = graphHeight - ((j / 8.0) * graphHeight);
			if (j == 0)
				yy--;
			// Tick mark.
			Draw.setColor(COLOR_GRAPH_OUTLINES);
			double tick = (j % labelSpacing == 0 ? 10 : 4);
			Draw.drawLine(-tick, yy, 0, yy);
			
			if (j % labelSpacing == 0) {
				double value = (j / 10.0) * maxBinNumber;
    			String str = String.format("%.1f", value);
    			Draw.setColor(COLOR_NUMBER_LABELS);
    			Draw.drawString(str, -20, yy, Draw.RIGHT, Draw.MIDDLE);
			}
		}
		
		
		// Draw vertical graph lines.
		labelSpacing = Math.max(1, (int) (64.0 / (graphWidth / (double) bins.size())));
		double[] barLines = new double[bins.size() + 1];
		for (int j = 0; j < bins.size() + 1; j++) {
			barLines[j] = (int) (j * xScale);
			Draw.setColor(COLOR_GRAPH_OVERLAY_LINES);
			Draw.drawLine(barLines[j], 0, barLines[j], graphHeight);

			// Tick mark.
			Draw.setColor(COLOR_GRAPH_OUTLINES);
			double tick = (j % labelSpacing == 0 ? 10 : 4);
			Draw.drawLine(barLines[j], graphHeight, barLines[j], graphHeight + tick);
			
			if (j % labelSpacing == 0) {
				double xx = min + (j * binSize);
    			String str = String.format("%.2f", xx);
    			Draw.setColor(COLOR_NUMBER_LABELS);
    			Draw.drawString(str, barLines[j], graphHeight + 16, Draw.CENTER, Draw.TOP);
			}
		}
		
		// Draw the histogram.
		for (int j = 0; j < bins.size(); j++) {
			double hh = graphHeight * (bins.getElement(j) / maxBinNumber);
			if (hh > 0.8f)
			{
    			Rectangle r = new Rectangle(barLines[j], graphHeight - hh, barLines[j + 1] - barLines[j] + 1, hh + 1);
    			Draw.setColor(COLOR_HISTOGRAM_BARS);
    			Draw.fill(r);
    			Draw.setColor(COLOR_HISTOGRAM_OUTLINES);
    			Draw.draw(r);
			}
		}
		
		// Draw the normal curve.
		Polygon normalCurve = new Polygon();
		normalCurve.addVertex(0, graphHeight);
		for (int j = 0; j < graphWidth; j++) {
			double xx = min + ((j / graphWidth) * range);
			double yy = normalFunction(xx, mean, sd);
			normalCurve.addVertex(j, graphHeight - ((yy / maxNormal) * graphHeight * 0.8));
		}
		normalCurve.addVertex(graphWidth, graphHeight);
		Draw.setColor(COLOR_NORMAL_CURVE_AREA);
		Draw.fill(normalCurve);
		Draw.setColor(COLOR_NORMAL_CURVE);
		for (int j = 1; j < normalCurve.edgeCount() - 2; j++)
			Draw.draw(normalCurve.getEdge(j));

		
		// Draw the graph outline.
		Draw.setColor(COLOR_GRAPH_OUTLINES);
		Draw.drawRect(0, 0, graphWidth + 1, graphHeight);
		
		// Draw Labels.
		Draw.setColor(COLOR_TEXT_LABELS);
		Draw.drawString("Distribution Plot", graphWidth / 2.0, -25, Draw.CENTER, Draw.BOTTOM);
		Draw.drawString(set.getName(), graphWidth / 2.0, graphHeight + 48, Draw.CENTER, Draw.TOP);
		Draw.drawString("Count", -70, graphHeight / 2.0, Draw.RIGHT, Draw.MIDDLE);
		
		// Save the graph as an image.
		if (save) {
    		try {
    			File outputfile = new File("C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/Graphs/graph_" + setIndex + ".png");
    			ImageIO.write(canvas, "png", outputfile);
    		}
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
	}
	
	@Override
	public void update() {
		boolean changed = false;
		double e = 0.001;
		
		if (Mouse.wheelUp()) {
			changed = true;
			setIndex = (setIndex + 1) % datasets.size();
			binSize = binSizes[setIndex] - e;
		}
		if (Mouse.wheelDown()) {
			changed = true;
			setIndex = (setIndex + datasets.size() - 1) % datasets.size();
			binSize = binSizes[setIndex] - e;
		}
		if (Keyboard.left.pressed() || (Keyboard.left.down() && Keyboard.control.down())) {
			changed = true;
			binSize += 0.01f;
		}
		if (Keyboard.right.pressed() || (Keyboard.right.down() && Keyboard.control.down())) {
			changed = true;
			binSize -= 0.01f;
			if (binSize < 0.01)
				binSize = 0.01;
		}
			
		
		
		if (changed) {
			graphHistogram(setIndex, false);
		}
		
		if (Keyboard.enter.pressed()) {
			System.out.println("Saving Graph Images.");
			for (int i = 0; i < datasets.size(); i++) {
				binSize = binSizes[i] - e;
				graphHistogram(i, true);
				System.out.print((i + 1) + "...");
			}
			System.out.println("\nDone.");
		}
			
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		Draw.getView().set(0, 0, 1.0);
		
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 640, 480);
		g.drawImage(canvas, 0, 0, null);
	}
	
	public static void main(String[] args) {
		new LabRunner();
	}
}
