package engine.physics;

import engine.math.Vector2f;

public class CollisionInfo {
	public Body body1;
	public Body body2;
	public Vector2f penetration;
	public float penetrationDepth;
	public Vector2f normal;
}
