package OLD;

import base.engine.common.Matrix4f;
import base.engine.common.ResourceLoader;
import base.engine.rendering.Material;
import base.engine.rendering.Shader;

public class BasicShader extends ShaderOLD {
	
	public BasicShader() {
		super();
		
//		addVertexShader(new Shader("basicVertex.vs"));
//		addFragmentShader(new Shader("basicFragment.fs"));
		compileShader();
		
		addUniform("transform");
		addUniform("color");
	}
	
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtilOLD.unbindTextures();
		
		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
	
	public static Shader getInstance() {
		return null;
	}
}
