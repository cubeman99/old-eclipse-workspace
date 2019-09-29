package base.engine.rendering;

import org.lwjgl.opengl.GL15;
import base.engine.common.Util;
import base.engine.common.Vector3;
import base.engine.rendering.resourceManagement.MeshResource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;



public class Mesh
{
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	private MeshResource resource;
	private String fileName;

	
	
	// ================== CONSTRUCTORS ================== //

	public Mesh(String fileName) {
		this.fileName = fileName;
		MeshResource oldResource = loadedModels.get(fileName);

		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		}
		else {
			//loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}
	}
	
	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		fileName = "";
		addVertices(vertices, indices, calcNormals, calcTangents);
	}
	
	

	// ==================== MUTATORS ==================== //

	
	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		if (calcNormals)
			calcNormals(vertices, indices);
		if (calcTangents)
			calcTangents(vertices, indices);
		
		resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 44);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	private void calcNormals(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3 v1 = vertices[i1].getPosition().minus(vertices[i0].getPosition());
			Vector3 v2 = vertices[i2].getPosition().minus(vertices[i0].getPosition());
			
			Vector3 normal = v1.cross(v2).normalized();
			
			vertices[i0].getNormal().add(normal);
			vertices[i1].getNormal().add(normal);
			vertices[i2].getNormal().add(normal);
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].getNormal().normalize();
	}

	private void calcTangents(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3 edge1 = vertices[i1].getPosition().minus(vertices[i0].getPosition());
			Vector3 edge2 = vertices[i2].getPosition().minus(vertices[i0].getPosition());
			
			float deltaU1 = vertices[i1].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV1 = vertices[i1].getTexCoord().y - vertices[i0].getTexCoord().y;
			float deltaU2 = vertices[i2].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV2 = vertices[i2].getTexCoord().y - vertices[i0].getTexCoord().y;
			
			float f = 1.0f / ((deltaU1 * deltaV2) - (deltaU2 * deltaV1));
			
			Vector3 tangent = new Vector3(0, 0, 0);
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
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty()) {
			loadedModels.remove(fileName);
		}
	}
	
	/*
	private Mesh loadMesh(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if(!ext.equals("obj"))
		{
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel("./res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for(int i = 0; i < model.getPositions().size(); i++)
		{
			vertices.add(new Vertex(model.getPositions().get(i),
					model.getTexCoords().get(i),
					model.getNormals().get(i),
					model.getTangents().get(i)));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false);
		
		return null;
	}
	*/
}
