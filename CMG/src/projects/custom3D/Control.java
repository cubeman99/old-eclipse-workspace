package projects.custom3D;

import cmg.main.GameRunner;
import cmg.math.geometry.Vec3;

public class Control {
	private GameRunner runner;
	private World world;
	private Camera camera;
	
	public Control(GameRunner runner) {
		this.runner = runner;
		this.world  = new World();
		this.camera = new Camera(world);
		
		world.models.add(createBox(4, -1, 1, 2, 2, 2));
		world.models.add(createBox(4, 2, 1, 2, 2, 4));
		world.models.add(createBox(4, 5, 1, 2, 4, 2));
		
		world.points.add(new Vector3(4, -1, 2));		
		world.points.add(new Vector3(4, 1, 3));
		world.points.add(new Vector3(8, 1, 3));
		world.points.add(new Vector3(8, -1, 2));

		world.points.add(new Vector3(4, -1, 1));		
		world.points.add(new Vector3(4, 1, 1));		
		world.points.add(new Vector3(8, 1, 1));
		world.points.add(new Vector3(8, -1, 1));

		world.points.add(new Vector3(3, 2, 0));
		
//		world.points.add(new Vector3(0, 0, 0));
//		world.points.add(new Vector3(0, 0, 0));
		
		
		/*
		Vec3 v1 = new Vec3(1.5, 0, 0);
		Vec3 v2 = new Vec3(4, 3, 8);
		double proj = v1.scalarProjection(v2);
		Vec3 v3 = v2.projectionOn(v1);
		System.out.println("length of " + v1 + " = " + v1.length());
		System.out.println(v2 + " onto " + v1);
		System.out.println(proj);
		System.out.println(v3);
		*/
	}
	
	public Mesh createBox(double x, double y, double z, double width, double length, double height) {
		Mesh mesh = new Mesh();
		
		// bottom
		mesh.addEdge(x, y, z, x + width, y, z);
		mesh.addEdge(x, y, z, x, y + length, z);
		mesh.addEdge(x, y + length, z, x + width, y + length, z);
		mesh.addEdge(x + width, y, z, x + width, y + length, z);
		
		// top
		mesh.addEdge(x, y, z + height, x + width, y, z + height);
		mesh.addEdge(x, y, z + height, x, y + length, z + height);
		mesh.addEdge(x, y + length, z + height, x + width, y + length, z + height);
		mesh.addEdge(x + width, y, z + height, x + width, y + length, z + height);
		
		// sides
		mesh.addEdge(x, y, z, x, y, z + height);
		mesh.addEdge(x + width, y, z, x + width, y, z + height);
		mesh.addEdge(x, y + length, z, x, y + length, z + height);
		mesh.addEdge(x + width, y + length, z, x + width, y + length, z + height);
		
		// faces
		mesh.addFace(x, y, z, x + width, y, z, x + width, y + length, z);
		mesh.addFace(x, y, z, x, y + length, z, x + width, y + length, z);
		mesh.addFace(x, y, z + height, x + width, y, z + height, x + width, y + length, z + height);
		mesh.addFace(x, y, z + height, x, y + length, z + height, x + width, y + length, z + height);

		mesh.addFace(x, y, z, x + width, y, z, x + width, y, z + height);
		mesh.addFace(x, y, z, x, y, z + height, x + width, y, z + height);
		mesh.addFace(x, y + length, z, x + width, y + length, z, x + width, y + length, z + height);
		mesh.addFace(x, y + length, z, x, y + length, z + height, x + width, y + length, z + height);
		
		mesh.addFace(x, y, z, x, y + length, z, x, y + length, z + height);
		mesh.addFace(x, y, z, x, y, z + height, x, y + length, z + height);
		mesh.addFace(x + width, y, z, x + width, y + length, z, x + width, y + length, z + height);
		mesh.addFace(x + width, y, z, x + width, y, z + height, x + width, y + length, z + height);

		
		
		
		return mesh;
	}
	
	public void update() {
		world.update();
		camera.update();
	}
	
	public void draw() {
		camera.draw();
	}
}
