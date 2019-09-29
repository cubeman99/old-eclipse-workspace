package zelda.main;

import java.util.ArrayList;
import javax.sound.sampled.Clip;

public class Sound {
	private static final int MAX_NUM_CHANNELS = 10;
	
	private static boolean muted = false;
	
	private String name;
	private int numChannels;
	private Clip[] clips;
	private int channelIndex;
	private float volumeOffset;
	private boolean looped;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Sound(String name, int numChannels, float volumeOffset) {
		this.name         = name;
		this.numChannels  = numChannels;
		this.volumeOffset = volumeOffset;
		this.looped       = false;
		this.channelIndex = 0;
		this.clips        = new Clip[MAX_NUM_CHANNELS];
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public int getNumChannels() {
		return numChannels;
	}
	
	public boolean isPlaying() {
		for (int i = 0; i < numChannels; i++ ) {
			if (clips[i].isActive()) {
				return true;
			}
		}
		return false;
	}
	
	
	// ==================== MUTATORS ==================== //

	public void play(boolean looped) {
		play(looped, Clip.LOOP_CONTINUOUSLY);
	}
	public void play(boolean looped, int numLoops) {
		if (!muted) {
			clips[channelIndex].stop();
			clips[channelIndex].setFramePosition(0);
			if (looped) {
				clips[channelIndex].setLoopPoints(0, -1);
				clips[channelIndex].loop(numLoops);
			}
			else
				clips[channelIndex].start();
			channelIndex += 1;
			if (channelIndex >= numChannels)
				channelIndex = 0;
		}
	}
	
	public void play() {
		play(looped);
	}
	
	public void loop(int numLoops) {
		play(true, numLoops);
	}
	
	public void loop() {
		play(true);
	}
	
	public void stop() {
		for( int i = 0; i < numChannels; i++ ) {
			clips[i].stop();
			clips[i].setFramePosition(0);
		}
	}
	
	public void setClip(int index, Clip clip) {
		clips[index] = clip;
	}
	
	
	
	
	// ================ STATIC METHODS ================ //
	
}
