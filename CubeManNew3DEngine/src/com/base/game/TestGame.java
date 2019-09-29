package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.*;
import com.base.engine.rendering.*;

public class TestGame extends Game
{
	GameObject objBox;
	GameObject objRoom;
	
	public void init()
	{
//		Mesh meshRoom  = TestModels.createTestMesh2();
		Mesh meshRoom = TestModels.createInvertedBoxMesh();
		Mesh meshBox  = TestModels.createBoxMesh();
//		Mesh meshBox  = new Mesh("monkey3.obj");
		
		Material mat = new Material();
		mat.addTexture("diffuse", new Texture("bricks2.jpg"));
//		mat.addTexture("diffuse", new Texture("gray.jpg"));
//		mat.addTexture("normalMap", new Texture("bricks2_normal.jpg"));
		mat.addFloat("specularIntensity", 1);
		mat.addFloat("specularPower", 8);
		

		Material matGray = new Material();
		matGray.addTexture("diffuse", new Texture("gray.jpg"));
		matGray.addFloat("specularIntensity", 1);
		matGray.addFloat("specularPower", 8);
		
		// Problem: doesn't render lights on the faces of meshes that include the last vertex of that mesh.
		
		MeshRenderer meshRenderer;
		
		// Add the room.
		meshRenderer = new MeshRenderer(meshRoom, mat);
		objRoom = new GameObject();
		objRoom.addComponent(meshRenderer);
		addObject(objRoom);

		// Add the box inside the room.
		meshRenderer = new MeshRenderer(meshBox, matGray);
		objBox = new GameObject();
		objBox.addComponent(meshRenderer);
//		addObject(objBox);
		
		objRoom.getTransform().setScale(new Vector3f(4, 4, 4));
		objBox.getTransform().setPos(new Vector3f(0, -3, 0));
		
		// Add the controllable camera.
		addObject(new GameObject().addComponent(new FreeLook(0.2f))
			.addComponent(new FreeMove(10.0f))
//			.addComponent(new PointLight(new Vector3f(1,1,1), 0.4f,new Attenuation(0,0,0.1f)))
			.addComponent(new Camera((float) Math.toRadians(70.0f),
			(float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)));



		PointLight pointLight = new PointLight(new Vector3f(1,1,1), 0.4f,
				new Attenuation(0,0,0.1f));

		GameObject pointLightObject = new GameObject();
		pointLightObject.addComponent(pointLight);
		pointLightObject.getTransform().getPos().set(0, 3, 0);
		addObject(pointLightObject);
		
		
		SpotLight spotLight = new SpotLight(new Vector3f(1,1,1), 0.9f, new Attenuation(0,0,0.03f), 0.6f);

		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(spotLight);
		spotLightObject.getTransform().getPos().set(0, 3, 0);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(1,0,0), (float)Math.toRadians(90.0f)));
//		addObject(spotLightObject);
		
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//		GameObject directionalLightObject = new GameObject();
//		directionalLightObject.addComponent(directionalLight);
//		addObject(directionalLightObject);
//		directionalLight.getTransform().setRot(new Quaternion(new Matrix4f().initRotation(1, 1, 1)));
		
		
		/*
		Mesh mesh = new Mesh("plane3.obj");
		Material material = new Material();//new Texture("test.png"), new Vector3f(1,1,1), 1, 8);
		material.addTexture("diffuse", new Texture("gray.jpg"));
		material.addTexture("normalMap", new Texture("bricks_normal.jpg"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);

		Material material2 = new Material();//new Texture("test.png"), new Vector3f(1,1,1), 1, 8);
		material2.addTexture("diffuse", new Texture("bricks2.jpg"));
		material2.addTexture("normalMap", new Texture("bricks2_normal.jpg"));
		material2.addFloat("specularIntensity", 1);
		material2.addFloat("specularPower", 8);

		Mesh tempMesh = new Mesh("monkey3.obj");

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().getPos().set(0, -1, 5);

		GameObject directionalLightObject = new GameObject();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0,0,1), 0.4f);

		directionalLightObject.addComponent(directionalLight);

		GameObject pointLightObject = new GameObject();
		pointLightObject.addComponent(new PointLight(new Vector3f(0,1,0), 0.4f, new Attenuation(0,0,1)));

		SpotLight spotLight = new SpotLight(new Vector3f(0,1,1), 0.4f,
				new Attenuation(0,0,0.1f), 0.7f);

		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(spotLight);

		spotLightObject.getTransform().getPos().set(5, 0, 5);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(90.0f)));

		addObject(planeObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);

		GameObject testMesh3 = new GameObject().addComponent(new LookAtComponent()).addComponent(new MeshRenderer(tempMesh, material));

		addObject(
				//addObject(
				new GameObject().addComponent(new FreeLook(0.5f)).addComponent(new FreeMove(10.0f)).addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)));

		addObject(testMesh3);

		testMesh3.getTransform().getPos().set(5,5,5);
		testMesh3.getTransform().setRot(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(-70.0f)));

		addObject(new GameObject().addComponent(new MeshRenderer(new Mesh("monkey3.obj"), material2)));

		directionalLight.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
		*/
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);

		objBox.getTransform().rotate(new Vector3f(0, 1, 0), 0.01f);
//		objRoom.getTransform().rotate(new Vector3f(0, 0, 1), 0.01f);
	}
}
