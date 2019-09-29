package com.base.engine.audio;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.WaveData;
import com.base.engine.common.FileUtils;
import static org.lwjgl.openal.AL10.*;

public class Sound {
	private String fileName;

	private ByteBuffer data;
	private float length;
	private int bitRate;
	private int frequency;
	private int size;
	private int numChannels;
	
	private int buffer;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Sound(String fileName) {
		loadFromWAV(fileName);
		
		buffer = alGenBuffers();
		
		if(alGetError() != AL_NO_ERROR) {
			System.err.println("Error creating audio buffer for \"" + fileName + "\"");
			System.exit(1);
		}
		
		alBufferData(buffer, getOpenALFormat(), data, frequency);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getOpenALFormat() {
		if (bitRate == 16)
			return (numChannels == 2 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16);
		else if (bitRate == 8)
			return (numChannels == 2 ? AL_FORMAT_STEREO8 : AL_FORMAT_MONO8);
		return AL_FORMAT_MONO8;
	}
	
	public float getLength() {
		return length;
	}
	
	public int getBitRate() {
		return bitRate;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public int getNumChannels() {
		return numChannels;
	}
	
	public int getSize() {
		return size;
	}
	
	public ByteBuffer getData() {
		return data;
	}
	
	public int getBuffer() {
		return buffer;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void play(SoundSource source) {
		AudioEngine.playSound(this, source);
	}
	
	public void loadFromWAV(String fileName) {
		this.fileName = fileName;

		try {
			WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(fileName)));
			
			bitRate = 8;
			numChannels = 1;
			if (waveFile.format == AL_FORMAT_MONO16 || waveFile.format == AL_FORMAT_STEREO16)
				bitRate = 16;
			if (waveFile.format == AL_FORMAT_STEREO8 || waveFile.format == AL_FORMAT_STEREO16)
				numChannels = 2;
			data      = waveFile.data;
			size      = waveFile.data.capacity();
			frequency = waveFile.samplerate;
			length    = size / (numChannels * frequency * (bitRate / 8.0f));
			
			waveFile.dispose();
		}
		catch (FileNotFoundException e) {
			System.err.println("Error loading sound \"" + fileName + "\"");
			System.exit(1);
		}
	}
	
	public void loadFromWAV2(String fileName) {
		this.fileName = fileName;
		
		try {
			// Load the file data into a byte buffer.
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fileName));
			ByteBuffer file = BufferUtils.createByteBuffer(stream.available());
			while (stream.available() > 0)
				file.put((byte) stream.read());
			file.rewind();

			String chunkName;
			int chunkSize;
			
			while (file.hasRemaining()) {
				chunkName = FileUtils.readString(file, 4);
				chunkSize = FileUtils.readLEInt(file);
				
				if (chunkName.equals("RIFF")) {
					file.position(file.position() + 4);
				}
				else if (chunkName.equals("fmt ")) {
					              FileUtils.readLEShort(file); // Audio format (1 = PCM = linear quantization = no compression)
					numChannels = FileUtils.readLEShort(file); // Number of channels
					frequency   = FileUtils.readLEInt(file);   // Sample rate
					              FileUtils.readLEInt(file);   // Byte rate
					              FileUtils.readLEShort(file); // Block align
					bitRate     = FileUtils.readLEShort(file); // Bits per sample
					
				}
				else if (chunkName.equals("data")) {
					size = chunkSize;
					data = FileUtils.readBytesIntoBuffer(file, chunkSize);
					data.rewind();
				} 
				else {
					// Ignore other chunks.
					file.position(file.position() + chunkSize);
				}
			}
			
			stream.close();
			length = size / (numChannels * frequency * (bitRate / 8.0f));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() throws Throwable {
		alDeleteBuffers(buffer);
	}
}
