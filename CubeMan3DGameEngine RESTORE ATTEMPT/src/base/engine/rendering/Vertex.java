package base.engine.rendering;

import base.engine.common.Vector;
import base.engine.common.Vector3;

public class Vertex {
	public static final int SIZE = 11; // 8
	
	private Vector3 position;
	private Vector3 normal;
	private Vector3 tangent;
	private Vector texCoord;
	
	
	// ================== CONSTRUCTORS ================== //

	public Vertex(Vector3 pos) {
		this(pos, new Vector(0, 0));
	}
	
	public Vertex(Vector3 pos, Vector texCoord) {
		this(pos, texCoord, new Vector3(0, 0, 0));
	}
	
	public Vertex(Vector3 pos, Vector texCoord, Vector3 normal) {
		this(pos, texCoord, normal, new Vector3(0, 0, 0));
	}
	
	public Vertex(Vector3 pos, Vector texCoord, Vector3 normal, Vector3 tangent) {
		this.position = pos;
		this.texCoord = texCoord;
		this.normal   = normal;
		this.tangent  = tangent;
	}
	
	public Vertex(float posX, float posY, float posZ) {
		this(posX, posY, posZ, 0, 0);
	}
	
	public Vertex(float posX, float posY, float posZ, float texX, float texY) {
		this(new Vector3(posX, posY, posZ),
			new Vector(texX, texY),
			new Vector3(0, 0, 0),
			new Vector3(0, 0, 0));
	}

	
	
	// =================== ACCESSORS =================== //

	public Vector3 getPosition() {
		return position;
	}
	
	public Vector3 getNormal() {
		return normal;
	}
	
	public Vector3 getTangent() {
		return tangent;
	}
	
	public Vector getTexCoord() {
		return texCoord;
	}

	
	
	// ==================== MUTATORS ==================== //
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}
	
	public void setTangent(Vector3 tangent) {
		this.tangent = tangent;
	}
	
	public void setTexCoord(Vector texCoord) {
		this.texCoord = texCoord;
	}
}
