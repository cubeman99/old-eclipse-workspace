package game;

import game.nodes.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import common.GMath;
import common.Settings;
import common.Vector;
import common.graphics.Draw;


/**
 * Ring.
 * 
 * @author David Jordan
 */
public class Ring {
	public Control control;
	public ArrayList<Node> nodes;
	private double radius;
	private double angle;
	public double angleSpeed;
	private boolean connected;
	private Color enhancerColor;
	private Node enhancerNode;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Ring(Control control, double radius) {
		this.control       = control;
		this.angle         = GMath.getRandomAngle();
		this.radius        = radius;
		this.connected     = true;
		this.nodes         = new ArrayList<Node>();
		this.angleSpeed    = 0;
		this.enhancerColor = null;
		this.enhancerNode  = null;
		
		
		int nodeCount           = Settings.RING_MAXIMUM_NODES;
		boolean hasDisconnecter = false;
		boolean hasRocket       = false;
		
		for (int i = 0; i < nodeCount; i++) {
			double a = (double) i * (GMath.TWO_PI / (double) nodeCount);
			Node n;
			
			if (i == 0 && GMath.onChance(10)) {
				int r = GMath.random.nextInt(3);
				if (r == 0)
					n = new NodeEnhancerHealth(this, a);
				else if (r == 1)
					n = new NodeEnhancerSpeed(this, a);
				else
					n = new NodeEnhancerShield(this, a);
			}
			else if (GMath.onChance(14) && !hasRocket) {
				hasRocket = true;
				n = new NodeRocket(this, a);
			}
//			else if (GMath.onChance(14) && !hasDisconnecter) {
//				hasDisconnecter = true;
//				n = new NodeDisconnecter(this, a);
//			}
			else if (GMath.onChance(15)) {
				n = new NodeReinforced(this, a);
			}
			else if (GMath.onChance(14)) {
				n = new NodeDeployer(this, a);
			}
			else {
				n = new NodeNormal(this, a);
			}
			
			nodes.add(n);
			control.addEnemy(n);
		}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return whether this ring is connected (there are at least three nodes). **/
	public boolean isConnected() {
		return connected;
	}
	
	/** Return the radius of this ring. **/
	public double getRadius() {
		return radius;
	}
	
	/** Return the angle offset of this ring. **/
	public double getAngle() {
		return angle;
	}
	
	/** Return the angular velocity of this ring. **/
	public double getAngleSpeed() {
		return angleSpeed / radius;
	}
	
	/** Return whether this ring is the front-most connected ring. **/
	public boolean isFrontRing() {
		return (control.getFrontRing() == this);
	}
	
	/** Return the enhancement node. **/
	public Node getEnhancer() {
		return enhancerNode;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the enhancement for this ring. **/
	public void setEnhancer(Node enhancerNode, Color enhancerColor) {
		this.enhancerNode  = enhancerNode;
		this.enhancerColor = enhancerColor;
	}
	
	/** Clear the enhancement for this ring. **/
	private void clearEnhancer() {
		this.enhancerNode  = null;
		this.enhancerColor = null;
	}
	
	/** Disconnect this ring, causing it to fall. **/
	public void disconnect() {
		connected = false;
		clearEnhancer();
	}
	
	/** Crash this ring into the planet. **/
	public void crash() {
		for (int i = 0; i < nodes.size(); i++)
			nodes.get(i).crash();
	}
	
	/** Move the radius of the ring in by the given amount. **/
	public void moveIn(double amount) {
		radius -= amount;
	}
	
	/** Update the ring and all its nodes. **/
	public void update() {
		angle += angleSpeed / radius;
		
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
//			n.update();
			if (enhancerNode instanceof NodeEnhancerHealth)
				n.setDamageAbsorb(0.5);
			else
				n.setDamageAbsorb(0);
			
			if (n.isDead()) {
				if (enhancerNode == n)
					clearEnhancer();
				nodes.remove(i);
				i--;
			}
		}
		
		if (nodes.size() < 3 && connected)
			disconnect();
	}

	/** Draw the ring and all its nodes. **/
	public void draw() {
		Vector center = Draw.getCenter();
		Graphics2D g  = Draw.getGraphics();
		
		if (isConnected()) {
    		g.setColor(enhancerColor == null ? Color.RED : Color.GRAY);
    		Draw.drawRing(radius, 2);
    		
    		if (enhancerNode != null) {
    			g.setColor(enhancerColor);
    			
        		for (int i = 0; i < 3; i++) {
        			int r = 2 - GMath.random.nextInt(4);
            		Draw.drawRing(radius + r, 1);
        		}
    		}
		}
		
		for (int i = 0; i < nodes.size(); i++) {
//			nodes.get(i).draw();
		}
	}
}
