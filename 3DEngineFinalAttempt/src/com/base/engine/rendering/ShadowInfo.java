package com.base.engine.rendering;

import com.base.engine.common.Matrix4f;

public class ShadowInfo {
	private static final int DEFAULT_SHAFDOW_MAP_SIZE_INDEX  = 0;
	private static final float DEFAULT_LIGHT_BLEED_REDUCTION = 0.2f;
	private static final float DEFAULT_SHADOW_SOFTNESS       = 1.0f;
	private static final float DEFAULT_MIN_VARIANCE          = 0.00002f;
	
	private Matrix4f projection;
	private boolean flipFaces;
	private int shadowMapSizeIndex;
	private float shadowSoftness;
	private float lightBleedReduction;
	private float minVariance;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public ShadowInfo(Matrix4f projection, boolean flipFaces,
			int shadowMapSizeIndex, float shadowSoftness,
			float lightBleedReduction, float minVariance)
	{
		this.projection          = projection;
		this.flipFaces           = flipFaces;
		this.shadowMapSizeIndex  = shadowMapSizeIndex;
		this.shadowSoftness      = shadowSoftness;
		this.lightBleedReduction = lightBleedReduction;
		this.minVariance         = minVariance;
	}

	
	
	// =================== ACCESSORS =================== //

	public Matrix4f getProjection() {
		return projection;
	}
	
	public boolean getFlipFaces() {
		return flipFaces;
	}
	
	public int getShadowMapSizeIndex() {
		return shadowMapSizeIndex;
	}
	
	public float getShadowSoftness() {
		return shadowSoftness;
	}
	
	public float getLightBleedReduction() {
		return lightBleedReduction;
	}
	
	public float getMinVariance() {
		return minVariance;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setProjection(Matrix4f projection) {
		this.projection = projection;
	}
	
	public void setFlipFaces(boolean flipFaces) {
		this.flipFaces = flipFaces;
	}
	
	public void setShadowMapSizeIndex(int shadowMapSizeIndex) {
		this.shadowMapSizeIndex = shadowMapSizeIndex;
	}
	
	public void setShadowSoftness(float shadowSoftness) {
		this.shadowSoftness = shadowSoftness;
	}
	
	public void setLightBleedReduction(float lightBleedReduction) {
		this.lightBleedReduction = lightBleedReduction;
	}
	
	public void setMinVariance(float minVariance) {
		this.minVariance = minVariance;
	}
}
