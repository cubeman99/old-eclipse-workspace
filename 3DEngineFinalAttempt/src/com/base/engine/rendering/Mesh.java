package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.rendering.resourceManagement.MeshResource;


public class Mesh {
	private static HashMap<String, MeshResource> loadedMeshes = new HashMap<String, MeshResource>();
	protected MeshResource resource;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Mesh() {
		resource = null;
	}
	
	public Mesh(String fileName) {
		MeshResource loadedMeshResource = loadedMeshes.get(fileName);
		
		if (loadedMeshResource != null) {
			resource = loadedMeshResource;
			resource.addReference();
		}
		else {
    		loadMesh(fileName);
    		loadedMeshes.put(fileName, resource);
		}
	}
	
	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		addVertices(vertices, indices, calcNormals, calcTangents);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public MeshResource getResource() {
		return resource;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setResource(MeshResource resource) {
		this.resource = resource;
	}
	
	protected void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		if(calcNormals)
			calcNormals(vertices, indices);
		if(calcTangents)
			calcTangents(vertices, indices);

		resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	protected void setVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		if(calcNormals)
			calcNormals(vertices, indices);
		if(calcTangents)
			calcTangents(vertices, indices);

		resource.setSize(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);  // Positions 
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // Texture coords
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); // Normals
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32); // Tangents
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	protected void calcNormals(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPos().minus(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().minus(vertices[i0].getPos());
			
			Vector3f normal = v1.cross(v2).normalized();
			
			vertices[i0].getNormal().add(normal);
			vertices[i1].getNormal().add(normal);
			vertices[i2].getNormal().add(normal);
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].getNormal().normalize();
	}
	
	protected void calcTangents(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f edge1 = vertices[i1].getPos().minus(vertices[i0].getPos());
			Vector3f edge2 = vertices[i2].getPos().minus(vertices[i0].getPos());
			
			float deltaU1 = vertices[i1].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV1 = vertices[i1].getTexCoord().y - vertices[i0].getTexCoord().y;
			float deltaU2 = vertices[i2].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV2 = vertices[i2].getTexCoord().y - vertices[i0].getTexCoord().y;
			
			float f = 1.0f / ((deltaU1 * deltaV2) - (deltaU2 * deltaV1));
			
			Vector3f tangent = new Vector3f(0, 0, 0);
			tangent.x = f * ((deltaV2 * edge1.x) - (deltaV1 * edge2.x));
			tangent.y = f * ((deltaV2 * edge1.y) - (deltaV1 * edge2.y));
			tangent.z = f * ((deltaV2 * edge1.z) - (deltaV1 * edge2.z));
			
			vertices[i0].getTangent().add(tangent);
			vertices[i1].getTangent().add(tangent);
			vertices[i2].getTangent().add(tangent);
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].getTangent().normalize();
	}
	
	protected Mesh loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		if (!ext.equals("obj")) {
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		BufferedReader meshReader = null;
		
		try {
			meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));
			String line;
			
			while ((line = meshReader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#"))
					continue;
				else if (tokens[0].equals("v")) {
					vertices.add(new Vertex(new Vector3f(Float.valueOf(tokens[1]),
														 Float.valueOf(tokens[2]),
														 Float.valueOf(tokens[3]))));
				}
				else if (tokens[0].equals("f")) {
					indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
					indices.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
					indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
					
					if (tokens.length > 4) {
						indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
						indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						indices.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
					}
				}
			}
			
			meshReader.close();
			
			Vertex[] vertexData = new Vertex[vertices.size()];
			vertices.toArray(vertexData);
			
			Integer[] indexData = new Integer[indices.size()];
			indices.toArray(indexData);
			
			addVertices(vertexData, Util.toIntArray(indexData), true, true);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() {
		if (resource.removeReference()) {
			loadedMeshes.remove(this);
		}
	}
}
