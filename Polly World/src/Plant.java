import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Random;


public class Plant extends Entity {
	public Random random = PollyWorld.random;
	
	public static Image image;
	
	public boolean reproduced 	= false;
	public int seed_count		= MyMath.random(2, 12);
	
	public boolean mature	= false;
	public float mature_age	= MyMath.random(3, 6);
	public boolean ripe		= false;
	public float ripe_age	= mature_age + MyMath.random(4, 14);
	public boolean spoiled	= false;
	public float spoil_age	= ripe_age + MyMath.random(10, 20);
	
	public float hungerValue = 5;
	
	public int generation;
	public float age = 0;
	public float scale;
	public float size  = 0;
	public float radius  = 0;
	public float alpha = 1;
	public float x;
	public float y;
	

	public Plant(float x, float y) {
		super("plant", 0);
		
		this.x = x;
		this.y = y;
		
		PollyWorld.plantCount += 1;
		image = ImageLoader.imagePlant;
		
		generation = 1;
	}
	
	public Plant(Plant p, float x, float y) {
		this(x, y);
		
		generation = p.generation + 1;
	}
	
	public Rect getRect() {
		return new Rect(x - (size / 2.0f), y - (size / 2.0f), size, size);
	}
	
	public Circle getCircle() {
		return new Circle(x, y, (size / 2.0f));
	}
	
	public Vector getVector() {
		return new Vector(x, y);
	}
	
	public void update() {
		age += 1.0f / (float)Main.FPS;
		
		scale = Math.min(1.0f, age / ripe_age);
		size  = 12.0f * scale;
		radius = size / 2.0f;
		
		mature  = (age >= mature_age);
		ripe    = (age >= ripe_age);
		spoiled = (age >= spoil_age);
		
		if( spoiled ) {
		    alpha -= 0.1f;
		    if( alpha <= 0.0f )
		        destroy();
		}
		else if( !reproduced && mature && MyMath.chance(100) ) {
			// Reproduce
			reproduced = true;
			for( int i = 0; i < seed_count; i++ ) {
				// 1/5 chance the seed will survive
				if( MyMath.chance(5) && PollyWorld.plantCount < PollyWorld.maxPlants ) {
					float D = MyMath.random(360);
					float L = MyMath.random(24);
					
					new Plant(this, x + (MyMath.cos(D) * L), y + (MyMath.sin(D) * L));
				}
			}
		}
	}
	
	public void onDestroy() {
		PollyWorld.plantCount -= 1;
	}
	
	public void draw(Graphics g) {
		ImageDrawer.drawImage(g, image, x, y, 6, 6, scale, 0.0f, alpha);
	}
}
