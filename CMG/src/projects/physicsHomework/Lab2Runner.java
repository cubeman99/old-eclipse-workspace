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

public class Lab2Runner extends GameRunner {
	private static final int WINDOW_WIDTH   = 800;
	private static final int WINDOW_HEIGHT  = 600;
	private Vector windowSize;
	private double trackLength;
	private double trackAngle;
	
	private double position;
	private double velocity;
	private double acceleration;
	private double time;
	
	
	public Lab2Runner() {
		super(60, WINDOW_WIDTH, WINDOW_HEIGHT, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		windowSize = new Vector(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		trackLength = 0.641;
		//trackLength = 0.421;
		//trackAngle = GMath.toRadians(0.48);
		trackAngle = GMath.toRadians(0.96);
		//trackAngle = GMath.toRadians(30);
		
		position = 0;
		velocity = 0;
		acceleration = 0.19f;
		time = 0;

		Vector v = Vector.polarVector(trackLength, trackAngle);
		Vector normal = v.inverse().normalized();
		Vector gravity = new Vector(0, 9.81);
//		Vector gravity = new Vector(0, 25.29);
		acceleration = gravity.dot(normal);
	}
	
	@Override
	public void update() {
		double delta = 1.0 / 60.0;
		
		if (position >= trackLength) {
			position = trackLength;
			velocity = 0;
		}
		else {
    		position += velocity * delta;
    		velocity += acceleration * delta;
    		time += delta;
		}
		
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		Draw.getView().set(-0.2, -0.6, 800.0);
		
		Vector v1 = new Vector(0, 0);
		Vector v2 = Vector.polarVector(trackLength, trackAngle);
		Vector v3 = new Vector(v2.x, v1.y);
		Vector pos = Vector.polarVector(trackLength - position, trackAngle);
		
		Draw.setColor(Color.BLACK);
		Draw.drawLine(v1, v2);
		Draw.drawLine(v1, v3);
		Draw.drawLine(v2, v3);
		
		String str = String.format("Time: %.2f s", time);
		Draw.drawString(str, new Vector(0, -0.1));
		
		Draw.setColor(Color.RED);
		Draw.fillCircle(pos, 0.01);
		
		//g.setColor(Color.GREEN);
		//g.fillRect(0, 0, 640, 480);
		//g.drawImage(canvas, 0, 0, null);
	}
	
	public static void main(String[] args) {
		new Lab2Runner();
	}
}
