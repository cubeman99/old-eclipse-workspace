package com.base.engine.rendering.resourceManagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.base.engine.common.Rect3f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.entity.Model;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;



public class OBJModel {
	private ArrayList<Vector3f> vertexPositions;
	private ArrayList<Vector2f> textureCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<OBJFace> faces;
	private ArrayList<OBJMaterial> materials;
	private String objFileName;
	private String mtlFileName;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public OBJModel() {
		vertexPositions = new ArrayList<Vector3f>();
		textureCoords   = new ArrayList<Vector2f>();
		normals         = new ArrayList<Vector3f>();
		materials       = new ArrayList<OBJMaterial>();
		faces           = new ArrayList<OBJFace>();
		objFileName     = "";
		mtlFileName     = "";
	}
	
	public OBJModel(String fileName) {
		this();
		
		objFileName = fileName;
		mtlFileName = "";
		
		loadFromFile(fileName);
		calcNormals();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public ArrayList<Vector3f> getVertexPositions() {
		return vertexPositions;
	}
	
	public ArrayList<Vector2f> getTextureCoords() {
		return textureCoords;
	}
	
	public ArrayList<Vector3f> getNormals() {
		return normals;
	}
	
	public Model createModel() {
		return new Model(createMeshes(), createMaterials(), materials.size());
	}
	
	public Rect3f getBoundingBox() {
		if (vertexPositions.size() == 0)
			return null;
		
		Vector3f min = new Vector3f(vertexPositions.get(0));
		Vector3f max = new Vector3f(min);
		
		for (int i = 1; i < vertexPositions.size(); i++) {
			Vector3f vertex = vertexPositions.get(i);
			
			if (vertex.x < min.x)
				min.x = vertex.x;
			if (vertex.y < min.y)
				min.y = vertex.y;
			if (vertex.z < min.z)
				min.z = vertex.z;
			
			if (vertex.x > max.x)
				max.x = vertex.x;
			if (vertex.y > max.y)
				max.y = vertex.y;
			if (vertex.z > max.z)
				max.z = vertex.z;
		}
		return new Rect3f(min, max.minus(min));
	}
	
	public Material[] createMaterials() {
		int numMaterials = materials.size();
		Material[] mats = new Material[numMaterials];
		
		for (int i = 0; i < materials.size(); i++) {
			OBJMaterial m = materials.get(i);
			
			Material mat = new Material();
			if (!m.getMapDiffuse().isEmpty())
				mat.setTexture(new Texture(m.getMapDiffuse()));
			
			mats[i] = mat;
		}
		
		return mats;
	}
	
	public Mesh[] createMeshes() {
		int numMeshes = materials.size();
		Mesh[] meshes = new Mesh[numMeshes];
		
		for (int meshIndex = 0; meshIndex < numMeshes; meshIndex++) {
    		ArrayList<Vertex> vertexArray = new ArrayList<Vertex>();
    		ArrayList<Integer> indexArray = new ArrayList<Integer>();
    		boolean calcNormals = true;//false;
    		
    		int index = 0;
    		for (OBJFace face : faces) {
    			for (int i = 0; i < OBJFace.SIZE; i++) {
    				
    				if (!face.getMaterial().isEmpty()) {
    					if (getMaterialIndex(face.getMaterial()) != meshIndex)
    						continue;
    						
    				}
    				else if (meshIndex > 0)
    					continue;
    				
    				Vector3f pos = vertexPositions.get(face.getVertex(i));
    				
    				// Texture Coord
    				Vector2f tex = new Vector2f(0, 0);
    				if (face.getTextureCoord(i) >= 0)
    					tex = textureCoords.get(face.getTextureCoord(i));
    				
    				// Normal
    				Vector3f nrm = new Vector3f(0, 0, 0);
    				if (face.getNormal(i) >= 0)
    					nrm = normals.get(face.getNormal(i));
    				else
    					calcNormals = true;
    				
    				Vertex v = new Vertex(pos, tex, nrm);
    				
    				vertexArray.add(v);
    				indexArray.add(index);
    				index++;
    			}
    		}
    		
    		Vertex[] vertexData = new Vertex[vertexArray.size()];
    		int[] indexData = new int[indexArray.size()];
    		vertexArray.toArray(vertexData);
    		for (int i = 0; i < indexArray.size(); i++)
    			indexData[i] = indexArray.get(i);
    		
    		meshes[meshIndex] = new Mesh(vertexData, indexData, false, true);
		}
		
		return meshes;
	}
	
	private int getMaterialIndex(String materialName) {
		for (int i = 0; i < materials.size(); i++) {
			if (materials.get(i).getName().equals(materialName))
				return i;
		}
		return -1;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void addVertex(float x, float y, float z) {
		vertexPositions.add(new Vector3f(x, y, z));
	}
	
	public void addVertex(Vector3f v) {
		vertexPositions.add(v);
	}
	
	public void addTexCoord(float x, float y) {
		textureCoords.add(new Vector2f(x, y));
	}
	
	public void addTexCoord(Vector2f t) {
		textureCoords.add(t);
	}
	
	public void addFace(int vertexIndex1, int vertexIndex2, int vertexIndex3,
			int texCoordIndex1, int texCoordIndex2, int texCoordIndex3)
	{
		faces.add(new OBJFace(this).setVertices(vertexIndex1, vertexIndex2, vertexIndex3)
				.setTextureCoords(texCoordIndex1, texCoordIndex2, texCoordIndex3));
	}
	
	public void addDummyMaterial() {
		materials.add(null);
	}
	

	public void calcNormals() {
		calcNormals(true);
	}
	
	public void calcNormals(boolean smooth) {
		normals.clear();
		if (smooth) {
    		for (int i = 0; i < vertexPositions.size(); i++)
    			normals.add(new Vector3f(0, 0, 0));
		}
		
		for (int i = 0; i < faces.size(); i++) {
			OBJFace face = faces.get(i);
			Vector3f pos1 = vertexPositions.get(face.getVertex(0));
			Vector3f pos2 = vertexPositions.get(face.getVertex(1));
			Vector3f pos3 = vertexPositions.get(face.getVertex(2));

			Vector3f v1 = pos2.minus(pos1);
			Vector3f v2 = pos3.minus(pos1);
			Vector3f normal = v1.cross(v2).normalized();
			
			if (smooth) {
    			face.setNormal(0, face.getVertex(0));
    			face.setNormal(1, face.getVertex(1));
    			face.setNormal(2, face.getVertex(2));    			
    			normals.get(face.getVertex(0)).add(normal);
    			normals.get(face.getVertex(1)).add(normal);
    			normals.get(face.getVertex(2)).add(normal);
			}
			else {
				normals.add(new Vector3f(normal));
				normals.add(new Vector3f(normal));
				normals.add(new Vector3f(normal));
    			face.setNormal(0, (i * 3) + 0);
    			face.setNormal(1, (i * 3) + 1);
    			face.setNormal(2, (i * 3) + 2);
			}
		}
		
		for (Vector3f normal : normals)
			normal.normalize();
	}
	
	public void loadFromFile(String fileName) {
		try {
    		loadMeshData(objFileName);
    		if (!mtlFileName.isEmpty())
    			loadMaterialData(mtlFileName);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void loadMaterialData(String fileName) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(ResourceManager.createFile(fileName, ResourceManager.MODEL_DIRECTORY)));
		System.out.println("Loading " + fileName + ":");
		
		String line;
		OBJMaterial currentMtl = null;

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(" ");
			tokens = Util.removeEmptyStrings(tokens);

			// Ignore comments
			if(tokens.length == 0 || tokens[0].equals("#"))
				continue;

			// Material definition
			else if (tokens[0].equals("newmtl")) {
				currentMtl = new OBJMaterial(tokens[1]);
				materials.add(currentMtl);
			}
			
			else if (currentMtl != null) {
				// Diffuse map
				if (tokens[0].equals("map_Kd") && tokens.length == 2) {
					currentMtl.setMapDiffuse(tokens[1]);
				}
			}
		}
		reader.close();

	}
	
	public void loadMeshData(String fileName) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(ResourceManager.createFile(fileName, ResourceManager.MODEL_DIRECTORY)));
		
		String line;
		String currentMtl = "";
		
		while ((line = reader.readLine()) != null) {
			if (line.endsWith("\\")) {
				line = line.substring(0, line.length() - 2) + reader.readLine();
			}
			String[] tokens = line.split(" ");
			tokens = Util.removeEmptyStrings(tokens);
			
			// Ignore comments and empty lines
			if(tokens.length == 0 || tokens[0].equals("#"))
				continue;

			// Material file decleration
			else if (tokens[0].equals("mtllib")) {
				String[] path = tokens[1].split("/");
				mtlFileName = path[path.length - 1];
			}
			
			// Vertex
			else if (tokens[0].equals("v")) {
				vertexPositions.add(new Vector3f(-Float.valueOf(tokens[1]), // Flip x position.
                    						  	 Float.valueOf(tokens[2]),
                    						  	 Float.valueOf(tokens[3])));
			}
			
			// Texture Coordinates
			else if (tokens[0].equals("vt")) {
				textureCoords.add(new Vector2f(Float.valueOf(tokens[1]),
							 				   Float.valueOf(tokens[2])));
			}
			
			// Normals
			else if (tokens[0].equals("vn")) {
				normals.add(new Vector3f(Float.valueOf(tokens[1]),
                					  	 Float.valueOf(tokens[2]),
                					  	 Float.valueOf(tokens[3])));
			}
			
			// Material bind
			else if (tokens[0].equals("usemtl")) {
				currentMtl = tokens[1];
			}
			
			// Face
			else if (tokens[0].equals("f")) {
				faces.add(new OBJFace(this, currentMtl, tokens[1], tokens[3], tokens[2]));
				
				if (tokens.length == 5) {
					faces.add(new OBJFace(this, currentMtl, tokens[1], tokens[4], tokens[3]));
				}
				else if (tokens.length > 5) {
					System.err.println("Error loading " + fileName
							+ ": face with too many indices (" + (tokens.length - 1) + ")!");
					System.exit(1);
				}
			}
		}
		
		System.out.println("Loaded " + fileName + " with " + faces.size() + " polygons.");
		
		reader.close();
	}
}
