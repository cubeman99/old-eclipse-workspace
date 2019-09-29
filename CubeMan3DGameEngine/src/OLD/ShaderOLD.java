package OLD;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import java.util.HashMap;
import base.engine.common.Matrix4f;
import base.engine.common.Util;
import base.engine.common.Vector3;
import base.engine.entity.Camera;
import base.engine.rendering.Material;
import base.engine.rendering.Shader;

public class ShaderOLD {
	private int program;
	private HashMap<String, Integer> uniforms;
	
	public ShaderOLD()
	{
		program  = glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if(program == 0)
		{
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}
	
	public void bind()
	{
		glUseProgram(program);
	}

	
	public void updateUniforms(Camera camera, Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		
	}
	
	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(program, uniform);
		
		if (uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		} 
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void addVertexShader(String text)
	{
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String text)
	{
		addProgram(text, GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String text)
	{
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	public void compileShader()
	{
		glLinkProgram(program);
		
		if(glGetProgrami(program, GL_LINK_STATUS) == 0)
		{
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
		
		glValidateProgram(program);
		
		if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
		{
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	private void addProgram(String text, int type)
	{
		int shader = glCreateShader(type);
		
		if(shader == 0)
		{
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
		{
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
	}
	
	public void setUniformi(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}
	
	public void setUniformf(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, Vector3 value) {
		glUniform3f(uniforms.get(uniformName), (float) value.x, (float) value.y, (float) value.z);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
	}
	
	public void setUniform4f(String uniformName, float value1, float value2, float value3, float value4) {
		glUniform4f(uniforms.get(uniformName), value1, value2, value3, value4);
	}
	
	public static Shader getInstance() {
		return null;
	}
}
