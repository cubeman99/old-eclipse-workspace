import java.awt.Graphics;


public class PhysicsBody {
	public static final float gravity = 0.1f;
	
	public Circle shape;
	public float hspeed = 0;
	public float vspeed = 0;
	public float dir    = 0; // orientation in radians
	
	public PhysicsBody(Circle c) {
		shape = c;
	}
	
	public void collide(Line l) {
		Vector vecNearest = new Vector(l.getClosestPoint(shape.getCenter()));
		
		if( !shape.collisionLine(l) )
			return;
		
		Vector vecSnap = new Vector(shape.getCenter());
		vecSnap.sub(vecNearest);
		vecSnap.normalise();
		
		vecSnap.scale(shape.getRadius());
		Vector vecOutline = new Vector(shape.getCenter());
		vecOutline.sub(vecOutline);
		
		vecSnap.set(vecNearest);
		vecSnap.add(vecOutline);
		Vector vecNewPos = new Vector(vecNearest);
		vecNewPos.add(vecSnap);
		
		vecSnap.set(vecNearest);
		vecSnap.sub(shape.getCenter());
		vecSnap.normalise();
		vecSnap.scale(shape.getRadius());
		
		vecNewPos.set(vecNearest);
		vecNewPos.sub(vecSnap);
		shape.set(vecNewPos);
		
		//Math.atan2((float) , x)
		
		float dirdif = vecSnap.getDir() - l.getVec().getDir();
		float sign = 0;
		if( dirdif < 0 )
			sign = 1;
		else
			sign = -1;
		
		cutDir(vecSnap.getDir());
		
		dir -= sign * vecSnap.length() * (Math.PI / 180);//( / shape.circumference()) * 2 * Math.PI;*/
	}
	
	public void cutDir(float d) {
		Vector vecSpeed = new Vector(hspeed, vspeed);
		
		float prj = (float) (vecSpeed.length() * MyMath.cos(vecSpeed.getDir() - d));
		Vector vecProj = new Vector(MyMath.cos(d) * prj, -MyMath.sin(d) * prj);

		hspeed = vecSpeed.getX() - vecProj.getX();
		vspeed = vecSpeed.getY() - vecProj.getY();
	}
	public float getX() {
		return shape.getX();
	}
	
	public float getY() {
		return shape.getY();
	}
	
	public void setHspeed(float h) {
		hspeed = h;
	}
	
	public void setVspeed(float v) {
		vspeed = v;
	}
	
	public void setX(float x) {
		shape.setX(x);
	}
	
	public void setY(float y) {
		shape.setY(y);
	}
	
	public void addX(float a) {
		shape.center.x += a;
	}
	
	public void addY(float a) {
		shape.center.y += a;
	}
	
	public void update() {
		vspeed += gravity;
		
		Vector v = new Vector(hspeed, vspeed);
		v.scale(1/10);
		
		shape.center.add(new Vector(hspeed, vspeed));
		for( int i = 0; i < GamePanel.listLines.size(); i++ ) {
			collide(GamePanel.listLines.get(i));
		}
		
	}
	
	public void draw(Graphics g) {
		shape.draw(g);
		Vector vecOutline = new Vector();
		vecOutline.x += Math.cos(dir);
		vecOutline.y += Math.sin(dir);
		vecOutline.scale(shape.getRadius());
		vecOutline.add(shape.center);

		
		g.drawLine((int)shape.center.getX(), (int)shape.center.getY(), (int)vecOutline.getX(), (int)vecOutline.getY());
	}
}
