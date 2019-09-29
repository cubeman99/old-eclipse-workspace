package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import java.nio.ByteBuffer;

public class TextureResource extends BasicResource {
	private int[] textureID;
	private int textureTarget;
	int frameBuffer;
	int renderBuffer;
	int numTextures;
	int width;
	int height;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public TextureResource(int textureTarget, int width, int height,
			int numTextures, ByteBuffer[] data, float[] filters,
			int[] internalFormat, int[] format, boolean clamp, int[] attachments)
	{
		super(true);
		
		this.numTextures   = numTextures;
		this.textureID     = new int[numTextures];
		this.textureTarget = textureTarget;
		this.width         = width;
		this.height        = height;
		this.frameBuffer   = 0;
		this.renderBuffer  = 0;
		
		initTextures(data, filters, internalFormat, format, clamp);
		initRenderTargets(attachments);
	}

	
	
	// =================== ACCESSORS =================== //
	
	public int getTextureID(int index) {
		return textureID[index];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void bind(int textureIndex) {
		glBindTexture(textureTarget, textureID[textureIndex]);
	}
	
	public void bindAsRenderTarget() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
//		glDisable(GL_SCISSOR_TEST);
	}

    public void initTextures(ByteBuffer[] data, float[] filters,
    		int[] internalFormat, int[] format, boolean clamp)
    {
    	for(int i = 0; i < numTextures; i++) {
    		textureID[i] = glGenTextures();
    		glBindTexture(textureTarget, textureID[i]);
    			
    		glTexParameterf(textureTarget, GL_TEXTURE_MIN_FILTER, filters[i]);
    		glTexParameterf(textureTarget, GL_TEXTURE_MAG_FILTER, filters[i]);

			if (clamp) {
    			glTexParameterf(textureTarget, GL_TEXTURE_WRAP_S, GL_CLAMP);
    			glTexParameterf(textureTarget, GL_TEXTURE_WRAP_T, GL_CLAMP);
    		}

			glTexParameteri(textureTarget, GL_TEXTURE_BASE_LEVEL, 0);
			glTexParameteri(textureTarget, GL_TEXTURE_MAX_LEVEL, 0);
    		
			if (textureTarget == GL_TEXTURE_CUBE_MAP) {
				for (int j = 0; j < 6; j++) {
		    		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, internalFormat[i],
		    				width, height, 0, format[i], GL_UNSIGNED_BYTE, data[(i * 6) + j]);
				}
			}
			else {
				glTexImage2D(textureTarget, 0, internalFormat[i], width, height, 0, format[i], GL_UNSIGNED_BYTE, data[i]);
			}
			
    		if (filters[i] == GL_NEAREST_MIPMAP_NEAREST ||
    			filters[i] == GL_NEAREST_MIPMAP_LINEAR ||
    			filters[i] == GL_LINEAR_MIPMAP_NEAREST ||
    			filters[i] == GL_LINEAR_MIPMAP_LINEAR)
    		{
    			glGenerateMipmap(textureTarget);
    			
    			float maxAnisotropy = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
    			if (maxAnisotropy < 0)
    				maxAnisotropy = 0;
    			if (maxAnisotropy > 0.8f)
    				maxAnisotropy = 0.8f;
    			// ^^^  instead of: clamp(0.0f, 8.0f, maxAnisotropy)
    			
    			glTexParameterf(textureTarget, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
    		}
			else {
    			glTexParameteri(textureTarget, GL_TEXTURE_BASE_LEVEL, 0);
    			glTexParameteri(textureTarget, GL_TEXTURE_MAX_LEVEL, 0);
    		}
    		
    	}
    }
    
    
	void initRenderTargets(int[] attachments) {
    	if(attachments == null)
    		return;

    	if (numTextures > 32) {
    		System.err.println("Error: Max number of textures (32) has been reached!");
    		System.exit(1);
    	}
    	
    	int[] drawBuffers = new int[numTextures]; // 32 is the max number of bound textures in OpenGL
    	
    	//assert(numTextures <= 32); // Assert to be sure no buffer overrun should occur
    
    	boolean hasDepth = false;

		for (int i = 0; i < numTextures; i++) {
			if (attachments[i] == GL_DEPTH_ATTACHMENT) {
				drawBuffers[i] = GL_NONE;
				hasDepth = true;
			}
			else
    			drawBuffers[i] = attachments[i];
    	
			if (attachments[i] == GL_NONE)
				continue;
    		
			if (frameBuffer == 0) {
				frameBuffer = glGenFramebuffers();
    			glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
    		}
    		
    		glFramebufferTexture2D(GL_FRAMEBUFFER, attachments[i], textureTarget, textureID[i], 0);
    	}
    	
		if (frameBuffer == 0)
			return;
    	
		if (!hasDepth) {
    		renderBuffer = glGenRenderbuffers();
    		glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
    		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
    		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
    	}
    	
		for (int i = 0; i < drawBuffers.length; i++) {
			glDrawBuffers(drawBuffers[i]);
		}
    	
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		if (status != GL_FRAMEBUFFER_COMPLETE) {
    		System.err.print("Error: Framebuffer creation failed with error " + status + " ");
    		if (status == GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT)
    			System.err.print("(GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT)");
    		else if (status == GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)
    			System.err.print("(GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)");
    			
    		System.err.println();
//    		System.exit(1);
    	}
    	
    	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() {
		for (int i = 0; i < numTextures; i++)
			glDeleteTextures(textureID[i]);
		glDeleteRenderbuffers(renderBuffer);
		glDeleteFramebuffers(frameBuffer);
	}
}
