package OLD;

import base.engine.common.Matrix4f;
import base.engine.common.ResourceLoader;
import base.engine.common.Vector3;
import base.engine.entity.Camera;
import base.engine.entity.light.BaseLight;
import base.engine.entity.light.DirectionalLight;
import base.engine.entity.light.PointLight;
import base.engine.entity.light.SpotLight;
import base.engine.rendering.Attenuation;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class PhongShader extends ShaderOLD {
	private static final int MAX_POINT_LIGHTS = 4;
	private static final int MAX_SPOT_LIGHTS  = 4;
	public static final PhongShader instance  = new PhongShader();

	private static Vector3 ambientLight = new Vector3(0.2f, 0.2f, 0.2f);
	private static DirectionalLight directionalLight = new DirectionalLight(ambientLight, 1);
	private static PointLight[] pointLights = new PointLight[] {};
	private static SpotLight[] spotLights   = new SpotLight[] {};
	
	
	
	public PhongShader() {
		/*
		super();
		
		addVertexShader(ResourceLoader.loadShader("phongVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.fs"));
		compileShader();
		
		addUniform("sampler");
		addUniform("normalMap");
		addUniform("dispMap");
		
		addUniform("dispMapScale");
		addUniform("dispMapBias");
		
		addUniform("transform");
		addUniform("transformProjected");
		addUniform("baseColor");
		addUniform("ambientLight");

		addUniform("cameraPos");
		addUniform("specularIntensity");
		addUniform("specularPower");
		
		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
		
		for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
			addUniform("pointLights[" + i + "].base.color");
			addUniform("pointLights[" + i + "].base.intensity");
			addUniform("pointLights[" + i + "].atten.constant");
			addUniform("pointLights[" + i + "].atten.linear");
			addUniform("pointLights[" + i + "].atten.exponent");
			addUniform("pointLights[" + i + "].position");
			addUniform("pointLights[" + i + "].range");
		}
		
		for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
			addUniform("spotLights[" + i + "].pointLight.base.color");
			addUniform("spotLights[" + i + "].pointLight.base.intensity");
			addUniform("spotLights[" + i + "].pointLight.atten.constant");
			addUniform("spotLights[" + i + "].pointLight.atten.linear");
			addUniform("spotLights[" + i + "].pointLight.atten.exponent");
			addUniform("spotLights[" + i + "].pointLight.position");
			addUniform("spotLights[" + i + "].pointLight.range");
			addUniform("spotLights[" + i + "].direction");
			addUniform("spotLights[" + i + "].cutoff");
		}
	}
	
	public void updateUniforms(Camera camera, Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtil.unbindTextures();
		
		glActiveTexture(GL_TEXTURE0);
		material.getTexture().bind();
		setUniformi("sampler", 0);
		
		glActiveTexture(GL_TEXTURE1);
		material.getNormalMap().bind();
		setUniformi("normalMap", 1);
		
		glActiveTexture(GL_TEXTURE2);
		material.getDispMap().bind();
		setUniformi("dispMap", 2);

		setUniformf("dispMapScale", material.getDispMapScale());
		setUniformf("dispMapBias", material.getDispMapBias());
		
		setUniform("transform", worldMatrix);
		setUniform("transformProjected", projectedMatrix);
		setUniform("baseColor", material.getColor());
		
		setUniform("ambientLight", ambientLight);
		setUniformDirectionalLight("directionalLight", directionalLight);
		
		for (int i = 0; i < pointLights.length; i++) {
			setUniformPointLight("pointLights[" + i + "]", pointLights[i]);
		}
		
		for (int i = 0; i < spotLights.length; i++) {
			setUniformSpotLight("spotLights[" + i + "]", spotLights[i]);
		}
		
		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularPower", material.getSpecularExponent());
		
		setUniform("cameraPos", camera.getTransform().getTranslation());
		*/
	}

//	public static PhongShader getInstance() {
//		return instance;
//	}
	
	public static Vector3 getAmbientLight() {
		return ambientLight;
	}
	
	public static void setAmbientLight(Vector3 ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}
	
	public static void setDirectionalLight(DirectionalLight directionalLight) {
		PhongShader.directionalLight = directionalLight;
	}
	
	public static void setPointLight(PointLight[] pointLights) {
		if (pointLights.length > MAX_POINT_LIGHTS)
		{
			System.err.println("Error: Maximum point lights is " + MAX_POINT_LIGHTS
					+ ", you passed in " + pointLights.length + ".");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		PhongShader.pointLights = pointLights;
	}
	
	public static void setSpotLight(SpotLight[] spotLights) {
		if (spotLights.length > MAX_SPOT_LIGHTS)
		{
			System.err.println("Error: Maximum spot lights is " + MAX_SPOT_LIGHTS
					+ ", you passed in " + spotLights.length + ".");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		PhongShader.spotLights = spotLights;
	}
	
	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniformAttenuation(String uniformName, Attenuation atten) {
		setUniformf(uniformName + ".constant", atten.getConstant());
		setUniformf(uniformName + ".linear", atten.getLinear());
		setUniformf(uniformName + ".exponent", atten.getExponent());
	}
	
	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformAttenuation(uniformName + ".atten", pointLight.getAttenuation());
		setUniform(uniformName + ".position", pointLight.getTransform().getTranslation());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}
