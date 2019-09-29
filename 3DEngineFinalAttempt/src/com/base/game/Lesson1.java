package com.base.game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
//import org.lwjgl.util.WaveData;
//import org.newdawn.slick.openal.WaveData;
import org.lwjgl.util.WaveData;

public class Lesson1 {
	private static final int NUM_BUFFERS = 2;
	
	/** Buffers hold sound data. */
	IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);

	/** Sources are points emitting sound. */
	IntBuffer source = BufferUtils.createIntBuffer(128);

	/** Position of the source sound. */
	FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the source sound. */
	FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Position of the listener. */
	FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Velocity of the listener. */
	FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

	/**
	 * boolean LoadALData()
	 *
	 *  This function will load our sample data from the disk using the Alut
	 *  utility and send the data into OpenAL as a buffer. A source is then
	 *  also created to play that buffer.
	 */
	int loadALData() throws FileNotFoundException {
		// Load wav data into a buffer.
		AL10.alGenBuffers(buffer);

		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		//Loads the wave file from this class's package in your classpath
		WaveData waveFile = null;
		
		
		waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("./res/wolf3d/sounds/test.wav")));
		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("./res/wolf3d/sounds/achtung.wav")));
		AL10.alBufferData(buffer.get(1), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		
		addSource(0);
		addSource(1);
		
		/*
		// Bind the buffer with the source.
		AL10.alGenSources(source);

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
		AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
		AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
		AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
		AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
		 */
		
		// Do another error check and return.
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;
	}
	
	/**
	 * void AddSource(ALint type)
	 *
	 *  Will add a new water drop source to the audio scene.
	 */
	private void addSource(int type) {
	  int position = source.position();
	  source.limit(position + 1);
	  AL10.alGenSources(source);

	  if (AL10.alGetError() != AL10.AL_NO_ERROR) {
	    System.out.println("Error generating audio source.");
	    System.exit(-1);
	  }

	  AL10.alSourcei(source.get(position), AL10.AL_BUFFER,   buffer.get(type) );
	  AL10.alSourcef(source.get(position), AL10.AL_PITCH,    1.0f             );
	  AL10.alSourcef(source.get(position), AL10.AL_GAIN,     1.0f             );
	  AL10.alSource (source.get(position), AL10.AL_POSITION, sourcePos        );
	  AL10.alSource (source.get(position), AL10.AL_VELOCITY, sourceVel        );
	  AL10.alSourcei(source.get(position), AL10.AL_LOOPING,  AL10.AL_TRUE     );

	  AL10.alSourcePlay(source.get(position));

	  // next index
	  source.position(position+1);
	}

	/**
	 * void setListenerValues()
	 *
	 *  We already defined certain values for the Listener, but we need
	 *  to tell OpenAL to use that data. This function does just that.
	 */
	private void setListenerValues() {
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	/**
	 * void killALData()
	 *
	 *  We have allocated memory for our buffers and sources which needs
	 *  to be returned to the system. This function frees that memory.
	 */
	void killALData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public static void main(String[] args) {
		new Lesson1().execute();
	}

	public void execute() {
		// Initialize OpenAL and clear the error bit.
		try{
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}
		AL10.alGetError();

		// Load the wav data.
		try {
			if(loadALData() == AL10.AL_FALSE) {
				System.out.println("Error loading data.");
				return;
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setListenerValues();

		// Loop.
		System.out.println("OpenAL Tutorial 1 - Single Static Source");
		System.out.println("[Menu]");
		System.out.println("p - Play the sample.");
		System.out.println("s - Stop the sample.");
		System.out.println("h - Pause the sample.");
		System.out.println("q - Quit the program.");
		char c = ' ';
		Scanner stdin = new Scanner(System.in);
		while(c != 'q') {
			try {
				System.out.print("Input: ");
				c = (char)stdin.nextLine().charAt(0);
			} catch (Exception ex) {
				c = 'q';
			}

			switch(c) {
			// Pressing 'p' will begin playing the sample.
			case 'p': AL10.alSourcePlay(source.get(0)); break;

			// Pressing 's' will stop the sample from playing.
			case's': AL10.alSourceStop(source.get(0)); break;

			// Pressing 'h' will pause the sample.
			case 'h': AL10.alSourcePause(source.get(0)); break;
			};
		}
		killALData();
		AL.destroy();
	}
}
