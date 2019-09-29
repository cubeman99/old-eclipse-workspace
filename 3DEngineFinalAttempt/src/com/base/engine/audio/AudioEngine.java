package com.base.engine.audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import com.base.engine.common.Vector3f;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

public class AudioEngine {
	private static final int NUM_CHANNELS = 128;
	private static final int MAX_SOURCES = 128;
	
	private static FloatBuffer defaultOrientation = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f}).rewind();
	
	private static SoundListener listener;
	private static float masterVolume;
	

	private static ALSource[] sources;
	private static ArrayList<SoundEmitter> emitters;
	private static ArrayList<SoundEmitter> frameEmitters;
	
	
	
	public static void initialize() {
		// Initialize OpenAL and clear the error bit.
		try {
			AL.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
		alGetError();
		
		masterVolume  = 1.0f;
		emitters      = new ArrayList<SoundEmitter>();
		frameEmitters = new ArrayList<SoundEmitter>();
		
		alDistanceModel(AL_LINEAR_DISTANCE_CLAMPED);
		
		sources = new ALSource[NUM_CHANNELS];
		for (int i = 0; i < NUM_CHANNELS; i++) {
			sources[i] = new ALSource(alGenSources());
		}
	}
	
	private static void updateListener() {
		if (listener != null) {
    		Vector3f up  = listener.getSoundOrientation().getUp().inverse();
    		Vector3f at  = listener.getSoundOrientation().getForward();
    		Vector3f pos = listener.getSoundPosition();
    		Vector3f vel = listener.getSoundVelocity();
    		FloatBuffer orientation = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] {at.x, at.y, at.z, up.x, up.y, up.z}).rewind();
    		
    		alListener3f(AL_POSITION, pos.x, pos.y, pos.z);
    		alListener3f(AL_VELOCITY, vel.x, vel.y, vel.z);
    		alListener(AL_ORIENTATION, orientation);
		}
		else {
    		alListener3f(AL_POSITION, 0, 0, 0);
    		alListener3f(AL_VELOCITY, 0, 0, 0);
    		alListener(AL_ORIENTATION, defaultOrientation);
		}
	}
	
	public static void update(float delta) {
		updateListener();
		frameEmitters.clear();
		
		Vector3f listenerPosition = listener != null ? listener.getSoundPosition() : Vector3f.ORIGIN;
		
		// Update all sound emitters.
		for (int i = 0; i < emitters.size(); i++) {
			SoundEmitter emitter = emitters.get(i);
			emitter.update(delta);
			
			if (emitter.getTimeLeft() < 0 && !emitter.isLooping()) {
				emitters.remove(i--);
				emitter.detachSource();
			}
			else
				frameEmitters.add(emitter);
		}
		
		// Cull sounds that won't be heard.
		for (int i = 0; i < frameEmitters.size(); i++) {
			if (!frameEmitters.get(i).isHeardFrom(listenerPosition)) {
				frameEmitters.remove(i--);
			}
		}
		
		// Manage what sounds get to be played.
		if (frameEmitters.size() >= MAX_SOURCES) {
			System.out.println("Max sources reached!");
		}
		else {
			for (int i = 0; i < frameEmitters.size(); i++) {
				if (frameEmitters.get(i).getSource() == null) {
					// Find a new source to attach.
					for (int j = 0; j < MAX_SOURCES; j++) {
						if (!sources[j].inUse) {
							frameEmitters.get(i).attachSource(sources[j]);
							break;
						}
					}
				}
			}
		}
	}
	
	public static void dispose() {
		for (int i = 0; i < NUM_CHANNELS; i++)
			alDeleteSources(sources[i].buffer);
		AL.destroy();
	}
	
	public static void setMasterVolume(float volume) {
		masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
		alListenerf(AL_GAIN, masterVolume);
	}
	
	public static void setListener(SoundListener listener) {
		AudioEngine.listener = listener;
	}
	
	

	public static void playSound(Sound sound) {
		playSound(sound, listener);
	}
		
	public static void playSound(Sound sound, SoundSource source) {
		if (sound == null)
			return;

		emitters.add(new SoundEmitter(sound, source));
		
		/*
		int index = sourceBuffers.position();
		sourceBuffers.limit(index + 1);
		alGenSources(sourceBuffers);
		
		if (alGetError() != AL_NO_ERROR) {
			System.out.println("Error generating audio source.");
			System.exit(1);
		}
		
		Vector3f position = (source == null ? Vector3f.ORIGIN : source.getSoundPosition());
		Vector3f velocity = (source == null ? Vector3f.ORIGIN : source.getSoundVelocity());

		int buffer = sourceBuffers.get(index);
		alSourcei (buffer, AL_BUFFER,    sound.getBuffer());
		alSourcef (buffer, AL_PITCH,     1.0f);
		alSourcef (buffer, AL_GAIN,      1.0f);
		alSource3f(buffer, AL_POSITION,  position.x, position.y, position.z);
		alSource3f(buffer, AL_VELOCITY,  velocity.x, velocity.y, velocity.z);
		alSourcei (buffer, AL_LOOPING,   AL_FALSE); // TODO

		alSourcePlay(buffer);
		
		// next index
		sourceBuffers.position(index + 1);
		*/
	}
}
