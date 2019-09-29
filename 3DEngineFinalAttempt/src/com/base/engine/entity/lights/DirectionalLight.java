package com.base.engine.entity.lights;

import com.base.engine.common.GMath;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Transform;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.ShadowInfo;

public class DirectionalLight extends BaseLight {
	public static final Shader forwardDirectional = new Shader("forward-directional");
	private float halfShadowArea;
	
	
	public DirectionalLight(Vector3f color, float intensity, float shadowArea) {
		super(color, intensity);
		
		this.halfShadowArea = shadowArea * 0.5f;
		
		setShader(forwardDirectional);
		setShadowInfo(new ShadowInfo(Matrix4f.createOrthographic(
				-halfShadowArea, halfShadowArea,
				-halfShadowArea, halfShadowArea,
				-halfShadowArea, halfShadowArea), 
				false, 10, 1.0f, 0.2f, 0.00002f));
	}

	public Quaternion getDirection() {
		return getTransform().getRotation();
	}

	public void setDirection(Vector3f direction) {
		getTransform().setDirection(direction);
	}
	
	@Override
	public Transform getShadowCameraTransform(Transform mainCameraTransform) {
		Transform result = new Transform();
		result.setPosition(mainCameraTransform.getTransformedPosition().plus(
				mainCameraTransform.getTransformedRotation().getForward().times(halfShadowArea)));
		Quaternion resultRot = getTransform().getTransformedRotation();
		
		// Snap the position to the shadow map texel size to prevent
		// a flickering effect that is seen when the main camera moves.
		float worldTexelSize = (halfShadowArea * 2) / ((float) (1 << getShadowInfo().getShadowMapSizeIndex()));
		Vector3f lightSpaceCameraPos = result.getPosition().rotate(resultRot.getConjugate());
		lightSpaceCameraPos.x = worldTexelSize * (float) Math.floor(lightSpaceCameraPos.x / worldTexelSize);
		lightSpaceCameraPos.y = worldTexelSize * (float) Math.floor(lightSpaceCameraPos.y / worldTexelSize);
		result.setPosition(lightSpaceCameraPos.rotate(resultRot));
		
		result.setRotation(resultRot);
		return result;
		
	}
}
