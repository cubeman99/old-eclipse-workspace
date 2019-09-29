import java.awt.Color;
import java.awt.Graphics;


public class Rect {
	public float x;
	public float y;
	public float width;
	public float height;
	
	public Rect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rect(Rect r) {
		this.x = r.x;
		this.y = r.y;
		this.width = r.width;
		this.height = r.height;
	}
	public Rect(Polly p) {
		this.x = p.x - ((p.size * p.scale) / 2);
		this.y = p.y - ((p.size * p.scale) / 2);
		this.width = p.x + ((p.size * p.scale) / 2);
		this.height = p.y + ((p.size * p.scale) / 2);
	}
	public Rect(Polly p, float newx, float newy) {
		this.x = newx - ((p.size * p.scale) / 2);
		this.y = newy - ((p.size * p.scale) / 2);
		this.width = newx + ((p.size * p.scale) / 2);
		this.height = newy + ((p.size * p.scale) / 2);
	}
	
	public float getX1() {
		return x;
	}

	public float getY1() {
		return y;
	}

	public float getX2() {
		return x + width;
	}

	public float getY2() {
		return y + height;
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		g.drawRect((int)x, (int)y, (int)width, (int)height);
	}
}
