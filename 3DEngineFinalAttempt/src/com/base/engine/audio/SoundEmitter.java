package com.base.engine.audio;

import com.base.engine.common.GMath;
import com.base.engine.common.Vector3f;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

public class SoundEmitter {
	private Sound sound;
	private float volume;
	private float radius;
	private boolean loop;
	private float timeLeft;
	private SoundSource soundEmitterObject;
	private ALSource source;
	private boolean first;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public SoundEmitter(Sound sound, SoundSource soundEmitterObject) {
		this.sound              = sound;
		this.volume             = 1.0f;
		this.radius             = 10.0f;
		this.loop               = false;
		this.timeLeft           = sound.getLength();
		this.soundEmitterObject = soundEmitterObject;
		this.source             = null;
		this.first              = true;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isHeardFrom(Vector3f position) {
		return (getPosition().distanceTo(position) < radius);
	}
	
	public Vector3f getPosition() {
		if (soundEmitterObject != null)
			return soundEmitterObject.getSoundPosition();
		return Vector3f.ORIGIN;
	}
	
	public Vector3f getVelocity() {
		if (soundEmitterObject != null)
			return soundEmitterObject.getSoundVelocity();
		return Vector3f.ORIGIN;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public boolean isLooping() {
		return loop;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public SoundSource getSoundEmitterObject() {
		return soundEmitterObject;
	}
	
	public ALSource getSource() {
		return source;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void attachSource(ALSource newSource) {
		if (source != newSource) {
			if (source != null)
				detachSource();
			
    		if (newSource != null) {
    			source = newSource;
    			source.inUse = true;
    			
        		alSourcei(source.buffer, AL_BUFFER,    sound.getBuffer());
        		alSourcef(source.buffer, AL_MAX_DISTANCE, radius);
        		alSourcef(source.buffer, AL_REFERENCE_DISTANCE, radius * 0.2f);
        		alSourcef(source.buffer, AL_SEC_OFFSET, sound.getLength() - timeLeft);
        		
        		alSourcePlay(source.buffer);
    		}
		}
	}
	
	public void detachSource() {
		if (source != null) {
    		alSourcef(source.buffer, AL_GAIN, 0.0f);
    		alSourceStop(source.buffer);
    		alSourcei(source.buffer, AL_BUFFER, 0);
    		
    		source.inUse = false;
    		source = null;
		}
	}
	
	public void update(float delta) {
		if (!first) {
			timeLeft -= delta;
			
			if (timeLeft < 0 && loop)
				timeLeft = GMath.mod(timeLeft, sound.getLength());
		}
		
		first = false;
		
		if (source != null) {
			
    		Vector3f position = getPosition();
    		Vector3f velocity = getVelocity();
    
    		alSourcef (source.buffer, AL_PITCH,     1.0f);
    		alSourcef (source.buffer, AL_GAIN,      1.0f);
    		alSource3f(source.buffer, AL_POSITION,  position.x, position.y, position.z);
    		alSource3f(source.buffer, AL_VELOCITY,  velocity.x, velocity.y, velocity.z);
    		alSourcei (source.buffer, AL_LOOPING,   loop ? AL_TRUE : AL_FALSE);
		}
	}
}
