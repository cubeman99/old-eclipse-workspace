package projects.custom3D;

import java.awt.Color;
import java.awt.event.KeyEvent;
import cmg.graphics.Draw;
import cmg.main.Keyboard;
import cmg.main.Keyboard.Key;
import cmg.math.GMath;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Vector;
import org.lwjgl.opengl.GL11.*;

public class Camera {
	private World world;
	
	private Vector3 position;
	private double direction;
	private double pitch;
	private double fov;
	private double depthMin;
	private double depthMax;
	
	private boolean showOrthographics;
	
	private Key keyOrthographics;
	private Key keyMoveForwards;
	private Key keyMoveBackwards;
	private Key keyStrafeLeft;
	private Key keyStrafeRight;
	private Key keyFlyUp;
	private Key keyFlyDown;
	private Key keyTurnLeft;
	private Key keyTurnRight;
	private Key keyLookUp;
	private Key keyLookDown;
	
	public Camera(World world) {
		this.world     = world;
		
		keyOrthographics = new Key(KeyEvent.VK_O);
		keyMoveForwards  = new Key(KeyEvent.VK_W);
		keyMoveBackwards = new Key(KeyEvent.VK_S);
		keyStrafeLeft    = new Key(KeyEvent.VK_A);
		keyStrafeRight   = new Key(KeyEvent.VK_D);
		keyFlyUp         = new Key(KeyEvent.VK_E);
		keyFlyDown       = new Key(KeyEvent.VK_Q);
		keyTurnLeft      = new Key(KeyEvent.VK_LEFT);
		keyTurnRight     = new Key(KeyEvent.VK_RIGHT);
		keyLookUp        = new Key(KeyEvent.VK_UP);
		keyLookDown      = new Key(KeyEvent.VK_DOWN);
		
		showOrthographics = false;
		
		this.depthMin  = 0.001; // How close the camera can see
		this.depthMax  = 100;   // How far the camera can see.
		this.direction = 0;
		this.pitch     = 0;
		this.position  = new Vector3(0, 0, 2);
		this.fov       = GMath.HALF_PI;
	}
	
	public void update() {
		Vector velocity = new Vector();
		
		if (keyMoveForwards.down())
			velocity.add(Vector.polarVector(0.05, direction));
		if (keyMoveBackwards.down())
			velocity.sub(Vector.polarVector(0.05, direction));

//		if (keyMoveForwards.down())
//			position.add(getForward().scaledBy(0.05));
//		if (keyMoveBackwards.down())
//			position.add(getForward().scaledBy(-0.05));
		
		if (keyStrafeLeft.down())
			velocity.add(Vector.polarVector(0.05, direction + GMath.HALF_PI));
		if (keyStrafeRight.down())
			velocity.add(Vector.polarVector(0.05, direction - GMath.HALF_PI));
		if (keyFlyUp.down())
			position.z += 0.05;
		if (keyFlyDown.down())
			position.z -= 0.05;
		
		if (keyTurnLeft.down())
			direction += 0.02;
		if (keyTurnRight.down())
			direction -= 0.02;
		if (keyLookUp.down())
			pitch += 0.02;
		if (keyLookDown.down())
			pitch -= 0.02;
		
		if (keyOrthographics.pressed())
			showOrthographics = !showOrthographics;
		
//		if (pitch > GMath.HALF_PI)
//			pitch = GMath.HALF_PI;
//		if (pitch < -GMath.HALF_PI)
//			pitch = -GMath.HALF_PI;
		
		if (Keyboard.home.pressed()) {
			this.direction = 0;
			this.pitch     = 0;
			this.position.set(0, 0, 2);
			this.fov       = GMath.HALF_PI;
		}
		
		position.x += velocity.x;
		position.y += velocity.y;
	}
	
	public Vector3 getPerspectiveProjection(Vector3 point) {
		Vector center = new Vector(320, 320);
		Vector vSide = new Vector(point.x - position.x, point.z - position.z);
		Vector vTop  = new Vector(point.x - position.x, point.y - position.y);
		
		
		// Rotate point around pitch-axis.
		Vector pitchAxis = Vector.polarVector(1, direction - GMath.HALF_PI);
		Vector rotPivot  = vTop.projectionOn(pitchAxis);
		Vector perpAxis  = vTop.rejectionOn(pitchAxis);
		double rej       = vTop.scalarRejection(pitchAxis);
		Vector vRot      = new Vector(vTop.scalarRejection(pitchAxis), point.z - position.z);
		
		Vector perpNormAxis = new Vector(perpAxis);
		if (rej < 0)
			perpNormAxis.negate();
		
		vRot.setDirection(vRot.direction() + pitch);
		perpNormAxis.setLength(vRot.x);
		vTop.set(rotPivot.plus(perpNormAxis));
		vSide.y = vRot.y;
		
		// Rotate point around z-axis.
		vTop.setDirection(vTop.direction() - direction);
		
		
		// Calculate perspective projection coordinate.
		Vector3 rotPoint = new Vector3(vTop.x, vTop.y, vSide.y);
		double tScale = rotPoint.x * GMath.tan(fov * 0.5);
		double tCoord = rotPoint.y / tScale;
		double sScale = rotPoint.x * GMath.tan(fov * 0.5);
		double sCoord = rotPoint.z / sScale;
		
		// Debug: Draw top and side orthographics.
		if (showOrthographics) {
    		double scl = 10;
    		Draw.setColor(Color.BLACK);
    		Vector topViewCam  = new Vector(210, 320);
    		Vector sideViewCam = new Vector(430, 320);
    		Draw.fillCircle(topViewCam.plus(rotPoint.x * scl, rotPoint.y * scl), 2);
    		Draw.fillCircle(sideViewCam.plus(rotPoint.x * scl, -rotPoint.z * scl), 2);
		}
		
		if (rotPoint.x < 0)
			return null;
		
		return new Vector3(center.x + (tCoord * 640), center.y - (sCoord * 640), rotPoint.x);
	}
	
	public void draw() {
		Vector center = new Vector(320, 320);
		Vector[] finalPoints = new Vector[world.points.size()];

		double fovEndScale = 80 / GMath.tan(fov * 0.5);
		double viewDirection = 0;
		double viewPitch     = 0;
		

		if (showOrthographics) {
    		Vector topViewCam  = new Vector(210, 320);
    		Vector topFov      = Vector.polarVector(80, viewDirection);
    		Vector topFov1     = Vector.polarVector(80, viewDirection + (fov * 0.5));
    		Vector topFov2     = Vector.polarVector(80, viewDirection - (fov * 0.5));
    		Vector topAxis1    = Vector.polarVector(80, viewDirection - direction);
    		Vector topAxis2    = Vector.polarVector(80, viewDirection - direction + GMath.HALF_PI);
    		
    		Draw.setColor(Color.GREEN);
    		Draw.drawLine(topViewCam, topViewCam.plus(topFov));
    		Draw.setColor(Color.RED);
    		Draw.drawLine(topViewCam, topViewCam.plus(topFov1));
    		Draw.drawLine(topViewCam, topViewCam.plus(topFov2));
    		Draw.setColor(Color.BLUE);
    		Draw.drawCircle(topViewCam, 80);
    		Draw.fillCircle(topViewCam, 4);
    		Draw.setColor(Color.BLACK);
    		Draw.drawLine(topViewCam, topViewCam.plus(topAxis1));
    		Draw.drawLine(topViewCam, topViewCam.plus(topAxis2));
    		
    		Vector sideViewCam = new Vector(430, 320);
    		Vector sideFov     = Vector.polarVector(80, viewPitch);
    		Vector sideFov1    = Vector.polarVector(80, viewPitch + (fov * 0.5));
    		Vector sideFov2    = Vector.polarVector(80, viewPitch - (fov * 0.5));
    		Vector sideAxis1   = Vector.polarVector(80, viewPitch - pitch);
    		Vector sideAxis2   = Vector.polarVector(80, viewPitch - pitch + GMath.HALF_PI);
    
    		Draw.setColor(Color.GREEN);
    		Draw.drawLine(sideViewCam, sideViewCam.plus(sideFov));
    		Draw.setColor(Color.RED);
    		Draw.drawLine(sideViewCam, sideViewCam.plus(sideFov1));
    		Draw.drawLine(sideViewCam, sideViewCam.plus(sideFov2));
    		Draw.setColor(Color.BLUE);
    		Draw.drawCircle(sideViewCam, 80);
    		Draw.fillCircle(sideViewCam, 4);
    		Draw.setColor(Color.BLACK);
    		Draw.drawLine(sideViewCam, sideViewCam.plus(sideAxis1));
    		Draw.drawLine(sideViewCam, sideViewCam.plus(sideAxis2));
		}
		
		// Draw meshes.
		for (int i = 0; i < world.models.size(); i++) {
			Mesh mesh = world.models.get(i);
			Vector[] viewPoints = new Vector[mesh.getVertices().size()];
			double[] viewPointDepths = new double[mesh.getVertices().size()];
			
			
			// Draw vertices.
			for (int j = 0; j < mesh.getVertices().size(); j++) {
    			Vector3 point      = mesh.getVertices().get(j);
    			Vector3 proj       = getPerspectiveProjection(point);
    			
    			if (proj != null) {
        			viewPoints[j]      = new Vector(proj.x, proj.y);
        			viewPointDepths[j] = proj.z;
        			
        			if (viewPoints[j] != null) {
//            			Draw.setColor(Color.BLACK);
//            			Draw.fillCircle(viewPoints[j], 4);
        			}
    			}
			}
			
			// Draw Edges
			Draw.setColor(Color.BLACK);
			for (int j = 0; j < mesh.getEdges().size(); j += 2) {
				Vector v1 = viewPoints[mesh.getEdgeVertexIndex(j)];
				Vector v2 = viewPoints[mesh.getEdgeVertexIndex(j + 1)];
				
				if (v1 != null && v2 != null) {
//					Draw.drawLine(v1, v2);
				}
			}
			
			
			Draw.setColor(Color.RED);
			int[] faces = new int[mesh.getFaces().size() / 3];
			
			// Sort Faces
			for (int j = 0; j < mesh.getFaces().size(); j += 3) {
				double d1 = viewPointDepths[mesh.getFaceVertexIndex(j)];
				double d2 = viewPointDepths[mesh.getFaceVertexIndex(j + 1)];
				double d3 = viewPointDepths[mesh.getFaceVertexIndex(j + 2)];
				double depth = (d1 + d2 + d3) / 3;
				
				for (int k = 0; k <= j / 3; k++) {
					if (k == j / 3) {
						faces[k] = j / 3;
						break;
					}
					else {
						double dd1 = viewPointDepths[mesh.getFaceVertexIndex(faces[k] * 3)];
						double dd2 = viewPointDepths[mesh.getFaceVertexIndex(faces[k] * 3 + 1)];
						double dd3 = viewPointDepths[mesh.getFaceVertexIndex(faces[k] * 3 + 2)];
						if (depth > (dd1 + dd2 + dd3) / 3) {
    						for (int l = j / 3; l > k; l--)
    							faces[l] = faces[l - 1];
    						faces[k] = j / 3;
    						break;
						}
					}
				}
			}

			// Draw Faces
			for (int j = 0; j < mesh.getFaces().size() / 3; j += 1) {
				int index = faces[j] * 3;
				Vector v1 = viewPoints[mesh.getFaceVertexIndex(index)];
				Vector v2 = viewPoints[mesh.getFaceVertexIndex(index + 1)];
				Vector v3 = viewPoints[mesh.getFaceVertexIndex(index + 2)];
				

				double d1 = viewPointDepths[mesh.getFaceVertexIndex(index)];
				double d2 = viewPointDepths[mesh.getFaceVertexIndex(index + 1)];
				double d3 = viewPointDepths[mesh.getFaceVertexIndex(index + 2)];
				double depth = (d1 + d2 + d3) / 3;
				
				if (v1 != null && v2 != null && v3 != null) {
					Polygon poly = new Polygon(v1, v2, v3);
					int a = 255;
					if (depth > 1)
						a = (int) (255 / depth);
    				Draw.setColor(new Color(a, a, a));
    				Draw.fill(poly);
				}
				
//				if (v1 != null && v2 != null)
//					Draw.drawLine(v1, v2);
//				if (v2 != null && v3 != null)
//					Draw.drawLine(v2, v3);
//				if (v3 != null && v1 != null)
//					Draw.drawLine(v3, v1);
			}
			
		}
		
		
		// Draw Cube Lines:
		Draw.setColor(Color.BLACK);
		drawLine(finalPoints[0], finalPoints[1]);
		drawLine(finalPoints[1], finalPoints[2]);
		drawLine(finalPoints[2], finalPoints[3]);
		drawLine(finalPoints[3], finalPoints[0]);

		drawLine(finalPoints[4], finalPoints[5]);
		drawLine(finalPoints[5], finalPoints[6]);
		drawLine(finalPoints[6], finalPoints[7]);
		drawLine(finalPoints[7], finalPoints[4]);

		drawLine(finalPoints[0], finalPoints[4]);
		drawLine(finalPoints[1], finalPoints[5]);
		drawLine(finalPoints[2], finalPoints[6]);
		drawLine(finalPoints[3], finalPoints[7]);
		
	}
	
	public void drawLine(Vector v1, Vector v2) {
		if (v1 != null && v2 != null)
			Draw.drawLine(v1, v2);
	}
	
	public Vector3 getForward() {
		Vector vSide = Vector.polarVector(1, pitch);
		Vector vTop = new Vector(vSide.x, 0);
		vTop.setDirection(direction);
		return new Vector3(vTop.x, vTop.y, -vSide.y).normalize();
	}
}
