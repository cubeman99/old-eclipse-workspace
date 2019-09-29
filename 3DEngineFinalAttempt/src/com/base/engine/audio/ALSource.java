package com.base.engine.audio;

public class ALSource {
	public int buffer;
	public boolean inUse;
	
	public ALSource(int buffer) {
		this.buffer = buffer;
		this.inUse  = false;
	}
}
