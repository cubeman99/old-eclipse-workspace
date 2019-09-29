package com.base.engine.rendering;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Resource;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Transform;
import com.base.engine.core.Util;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.PointLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.resourceManagement.MappedValues;
import com.base.engine.rendering.resourceManagement.ResourceManager;


public class Shader {
	
	
	private class Uniform {
		public String type;
		public String name;
		
		public Uniform(String type, String name) {
			this.type = type;
			this.name = name;
		}
	}
	
	private int program;
	private ArrayList<Uniform> uniforms;
	private HashMap<String, Integer> uniformLocations;

	
	
	// ================== CONSTRUCTORS ================== //

	public Shader(String fileName) {
		program  = glCreateProgram();
		uniforms = new ArrayList<Uniform>();
		uniformLocations = new HashMap<String, Integer>();
		
		if (program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		

		String vShaderText = loadShader(fileName + ".vs");
		addProgram(vShaderText, GL_VERTEX_SHADER, fileName);
		String fShaderText = loadShader(fileName + ".fs");
		addProgram(fShaderText, GL_FRAGMENT_SHADER, fileName);
		
		compileShader();
		
		addAllUniforms(fShaderText);
		addAllUniforms(vShaderText);
	}
	
	public Shader() {
		program = glCreateProgram();
		uniforms = new ArrayList<Uniform>();

		if (program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}
	
	

	// ==================== MUTATORS ==================== //

	public void bind() {
		glUseProgram(program);
	}
	
	public void updateUniforms(Transform transform, Camera camera, MappedValues data) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = camera.getViewProjection().times(worldMatrix);

		for (int i = 0; i < uniforms.size(); i++) {
			Uniform uniform = uniforms.get(i);
			String uniformName = uniform.name;
			
			if (uniform.name.startsWith("t_"))
			{
				if(uniform.name.equals("t_transformProjected"))
					setUniform(uniformName, projectedMatrix);
				else if(uniform.name.equals("t_transform"))
					setUniform(uniformName, worldMatrix);
				else
					throw new IllegalArgumentException(uniform.name + " is not a valid component of Transform");
			}
			else {
    			if(uniform.type.equals("vec3"))
    				setUniform(uniformName, data.getVector3f(uniform.name));
    			else if(uniform.type.equals("float"))
    				setUniformf(uniformName, data.getFloat(uniform.name));
    			else
    				throw new IllegalArgumentException(uniform.type + " is not a supported type in mapped values");
			}
		}
	}
	
	public void updateUniforms(Transform transform, RenderingEngine renderingEngine, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().times(worldMatrix);
		
		for (int i = 0; i < uniforms.size(); i++) {
			Uniform uniform = uniforms.get(i);
			String uniformName = uniform.name;

			if(uniform.name.startsWith("g_"))
			{
				if (uniformName.equals("g_time")) {
					setUniformf(uniformName, CoreEngine.getGlobalTime());
				}
			}
			else if(uniform.name.startsWith("r_"))
			{
				String unprefixedUniformName = uniformName.substring(2);

				if (uniformName.equals("r_lightMatrix")) {
					setUniform(uniformName, renderingEngine.getLightMatrix().times(worldMatrix));
				}
				else if (uniform.type.equals("sampler2D")) {
					int samplerSlot = renderingEngine.getSamplerSlot(unprefixedUniformName);
					renderingEngine.getTexture(unprefixedUniformName).bind(samplerSlot);
					setUniformi(uniformName, samplerSlot);
				}
				else if(uniform.type.equals("vec3"))
					setUniform(uniformName, renderingEngine.getVector3f(unprefixedUniformName));
				else if(uniform.type.equals("float"))
					setUniformf(uniformName, renderingEngine.getFloat(unprefixedUniformName));
				else if(uniform.type.equals("DirectionalLight"))
					setUniformDirectionalLight(uniformName, (DirectionalLight)renderingEngine.getActiveLight());
				else if(uniform.type.equals("PointLight"))
					setUniformPointLight(uniformName, (PointLight)renderingEngine.getActiveLight());
				else if(uniform.type.equals("SpotLight"))
					setUniformSpotLight(uniformName, (SpotLight)renderingEngine.getActiveLight());
//				else
//					renderingEngine.updateUniformStruct(transform, material, this, uniform.name, uniform.type);
			}
			else if(uniform.type.equals("sampler2D"))
			{
				int samplerSlot = renderingEngine.getSamplerSlot(uniform.name);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}
			else if(uniform.name.startsWith("t_"))
			{
				if(uniform.name.equals("t_transformProjected"))
					setUniform(uniformName, projectedMatrix);
				else if(uniform.name.equals("t_transform"))
					setUniform(uniformName, worldMatrix);
				else
					throw new IllegalArgumentException(uniform.name + " is not a valid component of Transform");
			}
			else if(uniform.name.startsWith("c_"))
			{
				if(uniform.name.equals("c_eyePos"))
					setUniform(uniformName, renderingEngine.getMainCamera().getTransform().getTransformedPosition());
				else
					throw new IllegalArgumentException(uniform.name + " is not a valid component of Camera");
			}
			else
			{
				if(uniform.type.equals("vec3"))
					setUniform(uniformName, material.getVector3f(uniform.name));
				else if(uniform.type.equals("float"))
					setUniformf(uniformName, material.getFloat(uniform.name));
				else
					throw new IllegalArgumentException(uniform.type + " is not a supported type in Material");
			}
		}
	}
	
	private void addUniform(String uniformType, String uniformName, HashMap<String, ArrayList<Uniform>> structs) {
		if (structs.containsKey(uniformType)) {
			ArrayList<Uniform> structUniforms = structs.get(uniformType);
			for (Uniform u : structUniforms)
				addUniform(u.type, uniformName + "." + u.name, structs);
		}
		else {
    		int location = glGetUniformLocation(program, uniformName);
    		
    		if (location == 0xFFFFFFFF) {
    			System.err.println("Error: Could not find uniform: " + uniformName);
    			new Exception().printStackTrace();
    			System.exit(1);
    		}
    		else {
        		uniformLocations.put(uniformName, location);
    		}
		}
	}
	

	private void addAllUniforms(String shaderText) {
		final String UNIFORM_KEYWORD = "\nuniform ";
		
		HashMap<String, ArrayList<Uniform>> structs = findUniformStructs(shaderText);
		
		int location = shaderText.indexOf(UNIFORM_KEYWORD);
		int fromLocation;
		
		while (location != -1) {
			location += UNIFORM_KEYWORD.length();
			
			fromLocation       = location;
			location           = shaderText.indexOf(" ", location);
			String uniformType = shaderText.substring(fromLocation, location);
			
			fromLocation       = location + 1;
			location           = shaderText.indexOf(";", location);
			String uniformName = shaderText.substring(fromLocation, location);

//			Uniform uniform = new Uniform(uniformType, uniformName);
			addUniform(uniformType, uniformName, structs);
			uniforms.add(new Uniform(uniformType, uniformName));
			
			location = shaderText.indexOf(UNIFORM_KEYWORD, location + 1);
		}
	}
	
	private HashMap<String, ArrayList<Uniform>> findUniformStructs(String shaderText) {
		final String STRUCT_KEYWORD = "\nstruct ";
		HashMap<String, ArrayList<Uniform>> structs = new HashMap<String, ArrayList<Uniform>>();
		
		
		int location = shaderText.indexOf(STRUCT_KEYWORD);
		int fromLocation;
		
		while (location != -1) {
			location += STRUCT_KEYWORD.length();
			
			fromLocation      = location;
			location          = shaderText.indexOf("\n", location);
			String structName = shaderText.substring(fromLocation, location);
			
			int endLocation = shaderText.indexOf("\n};", location);

			location += 1;
			
			ArrayList<Uniform> structUniforms = new ArrayList<Shader.Uniform>();
			
			while (location < endLocation - 1) {
				location += 6;

				fromLocation       = location;
				location           = shaderText.indexOf(" ", location);
				String uniformType = shaderText.substring(fromLocation, location);
				
				fromLocation       = location + 1;
				location           = shaderText.indexOf(";", location);
				String uniformName = shaderText.substring(fromLocation, location);
				
				
				structUniforms.add(new Uniform(uniformType, uniformName));
			}
			
			structs.put(structName, structUniforms);
			
			location = shaderText.indexOf(STRUCT_KEYWORD, location);
		}
		
		return structs;
	}
	
	private void addVertexShaderFromFile(String fileName) {
		String shaderText = loadShader(fileName);
		addProgram(shaderText, GL_VERTEX_SHADER);
		addAllUniforms(shaderText);
	}
	
	private void addGeometryShaderFromFile(String fileName) {
		String shaderText = loadShader(fileName);
		addProgram(shaderText, GL_GEOMETRY_SHADER);
		addAllUniforms(shaderText);
	}

	private void addFragmentShaderFromFile(String fileName) {
		String shaderText = loadShader(fileName);
		addProgram(shaderText, GL_FRAGMENT_SHADER);
		addAllUniforms(shaderText);
	}
	
	private void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	private void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}
	
	private void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	private void compileShader() {
		glLinkProgram(program);
		
		if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
		
		glValidateProgram(program);
		
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}

	private void addProgram(String text, int type) {
		addProgram(text, type, "?");
	}
	
	private void addProgram(String text, int type, String shaderName) {
		int shader = glCreateShader(type);

		if (shader == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println("Error in \"" + shaderName + "\" shader program:");
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
	}
	
	private int getUniformLocation(String uniformName) {
		return uniformLocations.get(uniformName);
	}
	
	private void setUniformi(String uniformName, int value) {
		glUniform1i(getUniformLocation(uniformName), value);
	}

	private void setUniformf(String uniformName, float value) {
		glUniform1f(getUniformLocation(uniformName), value);
	}

	private void setUniform(String uniformName, Vector3f value) {
		glUniform3f(getUniformLocation(uniformName), value.x, value.y, value.z);
	}

	private void setUniform(String uniformName, Vector2f value) {
		glUniform2f(getUniformLocation(uniformName), value.x, value.y);
	}

	private void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(getUniformLocation(uniformName), true, Util.createFlippedBuffer(value));
	}
	
	private void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	private void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {	
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection().getForward());
	}
	
	private void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getAtten().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAtten().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAtten().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	private void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection().getForward());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
	
	public void setUniformWave(String uniformName, Wave wave) {
		setUniformf(uniformName + ".amplitude", wave.getAmplitude());
		setUniformf(uniformName + ".frequency", wave.getFrequency());
		setUniformf(uniformName + ".phaseTime", wave.getPhaseTime());
		setUniform(uniformName + ".direction", wave.getDirection());
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	private static String loadShader(String fileName) {
		StringBuilder shaderSource     = new StringBuilder();
		BufferedReader shaderReader    = null;
		final String INCLUDE_DIRECTIVE = "#include";
		
		try {
			shaderReader = new BufferedReader(new FileReader(ResourceManager.getPath(fileName, ResourceManager.SHADER_DIRECTORY)));
			String line;
			
			while ((line = shaderReader.readLine()) != null) {
				if (line.startsWith(INCLUDE_DIRECTIVE)) {
					// Parse include directives.
					String includeFileName = line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1);
					shaderSource.append(loadShader(includeFileName));
				}
				else
					shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return shaderSource.toString();
	}
}
