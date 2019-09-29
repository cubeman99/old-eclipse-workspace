package com.base.engine.core;

import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;


public class Vertex
{
	public static final int SIZE = 3 + 2 + 3 + 3;
	
	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;
	private Vector3f tangent;

	
	
	// ================== CONSTRUCTORS ================== //

	public Vertex(Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord) {
		this(pos, texCoord, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal) {
		this(pos, texCoord, normal, new Vector3f(0, 0, 0));
	}

	public Vertex(float posX, float posY, float posZ, float texCoordX, float texCoordY) {
		this(new Vector3f(posX, posY, posZ), new Vector2f(texCoordX, texCoordY));
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal, Vector3f tangent) {
		this.pos      = pos;
		this.texCoord = texCoord;
		this.normal   = normal;
		this.tangent  = tangent;
	}

	
	
	// =================== ACCESSORS =================== //

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}

	public Vector3f getNormal() {
		return normal;
	}
	
	public Vector3f getTangent() {
		return tangent;
	}



	// ==================== MUTATORS ==================== //

	public Vector3f getPos() {
		return pos;
	}

	public Vector2f getTexCoord() {
		return texCoord;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	public void setTangent(Vector3f tangent) {
		this.tangent = tangent;
	}
}
