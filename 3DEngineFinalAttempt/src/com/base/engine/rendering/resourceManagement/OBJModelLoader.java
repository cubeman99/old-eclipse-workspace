package com.base.engine.rendering.resourceManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.rendering.Mesh;

public class OBJModelLoader {
	public static Mesh loadModel(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
//		System.out.println("Loading " + fileName + ":");
		
		if (!ext.equals("obj")) {
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vertex> vertices2  = new ArrayList<Vertex>();
		ArrayList<Vector2f> textureCoords = new ArrayList<Vector2f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		ArrayList<Vertex> verticesData = new ArrayList<Vertex>();
		
		BufferedReader meshReader = null;
		
		try {
			meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));
			String line;
			
			while ((line = meshReader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
//				for (int i = 0; i < tokens.length; i++)
//					System.out.print(tokens[i] + " ");
//				System.out.println();
				
				// Ignore comments
				if(tokens.length == 0 || tokens[0].equals("#"))
					continue;
				
				// Vertex
				else if (tokens[0].equals("v")) {
					vertices.add(new Vector3f(Float.valueOf(tokens[1]),
											  Float.valueOf(tokens[2]),
											  Float.valueOf(tokens[3])));
					vertices2.add(new Vertex(new Vector3f(
                					Float.valueOf(tokens[1]),
                					Float.valueOf(tokens[2]),
                					Float.valueOf(tokens[3]))));
				}
				
				// Texture Coordinates
				else if (tokens[0].equals("vt")) {
					textureCoords.add(new Vector2f(Float.valueOf(tokens[1]),
    										  	   Float.valueOf(tokens[2])));
				}
				
				// Normals
				else if (tokens[0].equals("vn")) {
					// TODO
				}
				
				// Face
				else if (tokens[0].equals("f")) {
//					System.out.print("Face: ");
//					System.out.print(tokens[1].split("/").length);
//					System.out.print(tokens[2].split("/").length);
//					System.out.print(tokens[3].split("/").length);
//					System.out.println();
					
					int[] faceIndices = new int[tokens.length - 1];
					
					for (int i = 1; i < tokens.length; i++) {
						int vIndex = Integer.parseInt(tokens[i].split("/")[0]) - 1;
						Vertex v = vertices2.get(vIndex);//new Vertex(vertices.get(vIndex));
						verticesData.add(v);
						faceIndices[i - 1] = vIndex;
						
						String tStr = tokens[i].split("/")[1];
						if (tStr.length() > 0)
							v.setTexCoord(textureCoords.get(Integer.parseInt(tStr) - 1));

//						String nStr = tokens[i].split("/")[1];
//						if (nStr.length() > 0)
//							v.setNormal(normals.get(Integer.parseInt(tStr)));
						
						if (i == 4) {
//							indices.add(verticesData.size() - 4);
//							indices.add(verticesData.size() - 2);
							indices.add(faceIndices[0]);
							indices.add(faceIndices[2]);
						}
//						indices.add(verticesData.size() - 1);
						indices.add(vIndex);
					}
					
					/*
					indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
					indices.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
					indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
					
					if (tokens.length > 4) {
						indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
						indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						indices.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
					}
					*/
				}
			}
			
			meshReader.close();
			
//			Vertex[] vertexData = new Vertex[verticesData.size()];
//			verticesData.toArray(vertexData);
			Vertex[] vertexData = new Vertex[vertices2.size()];
			vertices2.toArray(vertexData);

			Integer[] indexData = new Integer[indices.size()];
			indices.toArray(indexData);
		
			return new Mesh(vertexData, Util.toIntArray(indexData), true, true);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}
