import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.util.ArrayList;
import java.util.Random;


public class Polly extends Entity {
	public Random random = PollyWorld.random;
	
	public static Image image;
	public static final boolean collideWithOthers = true;
	public static final boolean drawCollision = true;
	public static final boolean drawSight = false;
	
	public float age 			= 0;
	public float mature_age 	= 30;
	public boolean mature 		= false;
	public boolean dead   		= false;
	
	public boolean controlling 	= false;
	public float direction 		= 0;
	public float dir 			= random.nextFloat() * 360.0f;
	public float dir_speed 		= 0;
	public float dir_speed_max 	= 10;
	public float turn_speed 	= 8;
	public float speed 			= 2;
	public float scale 			= 1;
	public float size 			= 24;
	public float radius 		= 12;
	public float alpha 			= 1;
	public float fov 			= 110;
	public float sight_distance = 300;
	public float x;
	public float y;
	
	public Plant targetPlant = null;
	public Polly targetPolly = null;

	public float hunger;
	public float hungerThreshMax = 60;
	public float hungerThreshMin = 0;
	public boolean eating = false;
	public float thirst;
	public float life;
	public float energy;
	
	public int speciesID;
	public float colorR;
	public float colorG;
	public float colorB;
	public Color color;
	
	public ArrayList<Polly> sightedPollies = new ArrayList<Polly>();
	public ArrayList<Plant> sightedPlants = new ArrayList<Plant>();
	public ArrayList<Vector> collisionPoints = new ArrayList<Vector>();
	
	
	public Polly(float x, float y) {
		super("polly", -10);
		image = ImageLoader.imagePolly;
		PollyWorld.pollyCount += 1;
		this.x = x;
		this.y = y;
		
		speed = 0;
		
		hunger = 35;
		
		
	}
	
	public void initSpecies() {
		colorR = MyMath.random(80.0f, 256.0f);
		colorG = MyMath.random(80.0f, 256.0f);
		colorB = MyMath.random(80.0f, 256.0f);
		color = new Color((int) colorR, (int) colorG, (int) colorB);
		
		speciesID = PollyWorld.speciesCount;
		PollyWorld.speciesCount += 1;
	}
	
	public void updateSight() {
		sightedPollies.clear();
		sightedPlants.clear();
		
		for( int i = 0; i < PollyWorld.entity_list.size(); i++ ) {
			if( i != id ) {
				Entity e = PollyWorld.entity_list.get(i);
				
				if( e.classname == "plant" ) {
					Plant plnt = (Plant) e;
					
					if( inSight(plnt.getVector()) ) {
						sightedPlants.add(plnt);
					}
				}
				else if( e.classname == "polly" ) {
					Polly plly = (Polly) e;
					if( inSight(plly.getVector()) ) {
						sightedPollies.add(plly);
					}
				}
			}
		}
	}
	
	public void sortPlantSight() {
		// sort plants based on closest to this polly
		for( int i = 0; i < sightedPlants.size(); i++ ) {
		    for( int j = sightedPlants.size() - 1; j > i; j-- ) {
		        if( getVector().distance(sightedPlants.get(j).getVector()) < getVector().distance(sightedPlants.get(i).getVector()) ) {
		        	Plant temp = sightedPlants.get(i);
		        	sightedPlants.set(i, sightedPlants.get(j));
		        	sightedPlants.set(j, temp);
		        }
		    }
		}
	}
	
	public boolean inSight(Vector v) {
		if( getVector().distance(v) < sight_distance ) {
			float D  = (360.0f) - Direction.direction(v.x - x, v.y - y); //v.sub(getVector()).direction();
			float d1 = Direction.simp(dir - (fov / 2.0f));
			float d2 = Direction.simp(dir + (fov / 2.0f));
            
            if( (d1 < d2 && D >= d1 && D < d2) ||
                (d1 > d2 && (D < d2 || D > d1)) )
            {
            	// Target is in sight
            	return true;
            }
		}
		return false;
	}
	
	public void userControl() {
		float d = 0.0f;
		int c   = 0;

		if( PollyWorld.keyDown[KeyEvent.VK_D] ) {
		    d += 0.0f;
		    c += 1;
		}
		else if( PollyWorld.keyDown[KeyEvent.VK_A] ) {
		    d += 180.0f;
		    c += 1;
		}
		if( PollyWorld.keyDown[KeyEvent.VK_S] ) {
		    d += 90.0f;
		    c += 1;
		}
		else if( PollyWorld.keyDown[KeyEvent.VK_W] ) {
		    d += 270.0f;
		    c += 1;
		    if( PollyWorld.keyDown[KeyEvent.VK_D] )
		        d += 360.0f;
		}

		speed = 0.0f;
		if( c > 0 && c <= 2 ) {
		    speed = 2.0f;
		    direction = d / (float) c;
		    turn(direction);
		}
	}
	
	public void wander() {
        dir_speed += 2.0f - random.nextFloat() * 4.0f;
        dir_speed -= random.nextFloat() * MyMath.sign(dir_speed) * (MyMath.sqr(dir_speed) / MyMath.sqr(dir_speed_max));
        dir_speed = Math.max(-dir_speed_max, Math.min(dir_speed_max, dir_speed));;
        dir += dir_speed;
        
        speed = 2;
		direction = dir;
	}
	
	public void turn(float gd) {
		float goal_dir, d, s;
		
		goal_dir = Direction.simp(gd);
		
		if( Math.abs(goal_dir - dir) <= 180.0f ) {
		    s = MyMath.sign(goal_dir - dir);
		    d = Math.abs(goal_dir - dir);
		}
		else {
		    s = MyMath.sign(dir - 180.0f);
		    if( s < 0.0f )
		        d = (360.0f - goal_dir) + dir;
		    else
		        d = (360.0f - dir) + goal_dir;
		}

		dir += Math.min(turn_speed, d / 1.5f) * s;
		dir = Direction.simp(dir);
	}
		
	public Circle getCircle() {
		return new Circle(x, y, radius);
	}
	
	public Rect getRect() {
		return new Rect(x - (size / 2), y - (size / 2), size, size);
	}
	
	public Vector getVector() {
		return new Vector(x, y);
	}
		
	public void updatePosition() {
		checkCollisions();
		x += MyMath.cos(direction) * speed;
		y += MyMath.sin(direction) * speed;
	}
	
	public void setHspeed(float hspeed) {
		float vspeed = -getVspeed();
		direction = Direction.direction(hspeed, vspeed);
		speed = MyMath.distance(0, 0, hspeed, vspeed);
	}
	
	public void setVspeed(float vspeed) {
		float hspeed = getHspeed();
		direction = Direction.direction(hspeed, vspeed);
		speed = MyMath.distance(0, 0, hspeed, vspeed);
	}
	
	public float getHspeed() {
		return( MyMath.cos(direction) * speed );
	}
	
	public float getVspeed() {
		return( MyMath.sin(direction) * speed );
	}
	
	public void cutDir(float d) {
		float dCut = Direction.simp(360 - d);
		
		Vector vecDir = new Vector(getHspeed(), getVspeed());
		float anchor = (direction - dCut);
		
		float proj = vecDir.length() * MyMath.cos(anchor);
		Vector vecProj = new Vector(MyMath.cos(dCut) * proj, MyMath.sin(dCut) * proj);
		vecDir.sub(vecProj);
		
		setHspeed(vecDir.x);
		setVspeed(-vecDir.y);
	}
	
	public void checkCollisions() {
		float W = 24;
		float H = 24;
		collisionPoints.clear();
		Rect r1 = new Rect(x - 12 + getHspeed(), y - 12, W, H);
		
		
		if( collideWithOthers ) {
			// Next Collide with other Pollies
			Polly plyPrev = this;
			
			for( int i = 0; i < PollyWorld.entity_list.size(); i += 1 ) {
				if( PollyWorld.entity_list.get(i).classname == "polly" && i != id ) {
					Polly p = (Polly) PollyWorld.entity_list.get(i);
					IntersectionSolutions interSpeed = new Line(x, y, x + getHspeed(), y + getVspeed()).circleIntersections(p.getCircle());
					boolean colliding = Collision.circleCircle(getCircle(), p.getCircle()) || interSpeed.solutions > 0;
							
					if( colliding ) {
						if( collisionPoints.size() == 0 ) {
							plyPrev = p;
							if( Collision.circleCircle(getCircle(), p.getCircle()) ) {
								Vector v = new Vector(x, y).sub(new Vector(p.x, p.y));
								
								v.normalise().scale(p.radius);
								collisionPoints.add(new Vector(p.x, p.y).add(v));
								
								v.normalise().scale(p.radius + radius);
								Vector vecMoveTo = new Vector(p.x, p.y).add(v);
								
								x = vecMoveTo.x;
								y = vecMoveTo.y;
								
								cutDir(Direction.direction(p.x - x, p.y - y));
							}
							else if( Collision.circleCircle(new Circle(x + getHspeed(), y + getVspeed(), radius), p.getCircle()) ) {
								// Collision Detected
								// 1) snap to edge of the other Polly
								Line moveLine = new Line(x, y, x + getHspeed(), y + getVspeed());
								IntersectionSolutions inter = moveLine.circleIntersections(new Circle(p.x, p.y, radius + p.radius));
								if( inter.solutions > 1 ) {
									Vector v = new Vector(x, y).nearest(inter.s1, inter.s2);
									Vector relPos = new Vector(p.x, p.y).sub(v);
			
									relPos.normalise().scale(p.radius);
									collisionPoints.add(new Vector(p.x, p.y).sub(relPos));
									
									relPos.normalise().scale(p.radius + radius);
									Vector vecMoveTo = new Vector(p.x, p.y).sub(relPos);
									
									x = vecMoveTo.x;
									y = vecMoveTo.y;
									
									cutDir(Direction.direction(p.x - x, p.y - y));
								}
							}
						}
						else if( collisionPoints.size() == 1 ) {
							/*
							// collide with two circles
							Circle c1 = plyPrev.getCircle();
							c1.radius += radius;
							Circle c2 = p.getCircle();
							c2.radius += radius;
							
							IntersectionSolutions inter = c1.circleIntersection(c2);
							collisionPoints.add(new Vector(p.x, p.y));
							
							if( inter.solutions > 0 ) {
								Vector v;
								if( inter.solutions == 1 )
									v = inter.s1;
								else
									v = new Vector(x, y).nearest(inter.s1, inter.s2);
								
								x = v.x;
								y = v.y;
								
								speed = 0;
							}
							*/
						}
					}
				}
			}
			
		}

		for( int xx = 0; xx < PollyWorld.wall_amountx; xx++ ) {
			for( int yy = 0; yy < PollyWorld.wall_amounty; yy++ ) {
				if( PollyWorld.walls[xx][yy] ) {
					Rect r2 = new Rect(xx * PollyWorld.wall_width, yy * PollyWorld.wall_height, PollyWorld.wall_width, PollyWorld.wall_height);
					
					if( Collision.rectangleRectangle(r1, r2) ) {
						if( x < xx * PollyWorld.wall_width )
							x = (xx * (float)PollyWorld.wall_width) - (W / 2.0f);
						else
							x = (xx * (float)PollyWorld.wall_width) + PollyWorld.wall_width + (W / 2.0f);
						setHspeed(0);
						
					}
				}
			}
		}
		
		r1 = new Rect(x - 12 + getHspeed(), y - 12 + getVspeed(), 24, 24);
		
		for( int xx = 0; xx < PollyWorld.wall_amountx; xx++ ) {
			for( int yy = 0; yy < PollyWorld.wall_amounty; yy++ ) {
				if( PollyWorld.walls[xx][yy] ) {
					Rect r2 = new Rect(xx * PollyWorld.wall_width, yy * PollyWorld.wall_height, PollyWorld.wall_width, PollyWorld.wall_height);
					
					if( Collision.rectangleRectangle(r1, r2) ) {
						if( y < yy * PollyWorld.wall_height )
							y = (yy * (float)PollyWorld.wall_height) - (H / 2.0f);
						else
							y = (yy * (float)PollyWorld.wall_height) + PollyWorld.wall_height + (H / 2.0f);
						setVspeed(0);
						
					}
				}
			}
		}
	}
	
	public void update() {
		// Update any Entities in sight
		updateSight();
		
		age += 1.0f / (float)Main.FPS;
		hunger = Math.max(0.0f, Math.min(100.0f, hunger + (1.0f / 30.0f)));
		//thirst = Math.max(0.0f, Math.min(100.0f, thirst + (1.0f / 30.0f)));
		
		scale = Math.min(1.0f, age / mature_age);
		
		if( PollyWorld.keyPressed[KeyEvent.VK_ENTER] )
			controlling = !controlling;
		
		if( controlling ) {
			userControl();
		}
		else {
			if( hunger >= hungerThreshMax && !eating ) {
				eating = true;
				if( sightedPlants.size() > 0 ) {
					//targetPlant = sightedPlants.get(MyMath.random(sightedPlants.size()));
					sortPlantSight();
					targetPlant = sightedPlants.get(0);
				}
			}
			if( eating ) {
				if( targetPlant != null ) {
					speed = 2.0f;
					turn(360.0f - Direction.direction(targetPlant.x - x, targetPlant.y - y));
					direction = dir;
					if( getVector().distance(targetPlant.getVector()) < radius + targetPlant.radius ) {
						hunger -= targetPlant.hungerValue;
						targetPlant.age = targetPlant.spoil_age;
						targetPlant = null;
					}
				}
				else if( sightedPlants.size() > 0 ) {
					//targetPlant = sightedPlants.get(MyMath.random(sightedPlants.size()));
					sortPlantSight();
					targetPlant = sightedPlants.get(0);
				}
				if( hunger <= hungerThreshMin ) {
					eating = false;
				}
			}
			if( !eating || targetPlant == null ) {
				// Nothing else to do, just meander.
				wander();
			}
		}
		
		
		// Check collisions and move
		updatePosition();
	}
	
	public void onDestroy() {
		PollyWorld.pollyCount -= 1;
	}
	
	public void draw(Graphics g) {
		
		if (drawCollision) {
			//getRect().draw(g, Color.red);
			
			Vector v = new Vector(MyMath.cos(dir), MyMath.sin(dir));
			v.normalise();
			v.scale(radius);
			g.setColor(Color.white);
			g.drawLine((int) x, (int) y, (int) (x + v.x), (int) (y + v.y));
			getCircle().draw(g, Color.white);
			g.setColor(Color.red);
			for (int i = 0; i < collisionPoints.size(); i++) {
				collisionPoints.get(i).draw(g, 3);
			}
		}
		else {
			alpha = 1;
			ImageDrawer.drawImage(g, image, x, y, 32, 32, scale, dir, alpha);
		}
		
		if (eating && targetPlant != null) {
			new Line(x, y, targetPlant.x, targetPlant.y).draw(g, Color.red);
		}
		if (drawSight) {
			// Draw Sight Information
			float len = 100;
			Line L1 = new Line(x, y, x + (MyMath.cos(dir - (fov / 2.0f)) * len), y + (MyMath.sin(dir - (fov / 2.0f)) * len));
			Line L2 = new Line(x, y, x + (MyMath.cos(dir + (fov / 2.0f)) * len), y + (MyMath.sin(dir + (fov / 2.0f)) * len));
			L1.draw(g, Color.blue);
			L2.draw(g, Color.blue);
			
			for (int i = 0; i < sightedPollies.size(); i++) {
				Polly p = sightedPollies.get(i);
				//new Circle(p.x, p.y, 14).draw(g, Color.red);
				new Line(x, y, p.x, p.y).draw(g, Color.red);
			}
			for (int i = 0; i < sightedPlants.size(); i++) {
				Plant p = sightedPlants.get(i);
				//new Circle(p.x, p.y, 8).draw(g, Color.red);
				new Line(x, y, p.x, p.y).draw(g, Color.red);
			}
		}
	}
}
