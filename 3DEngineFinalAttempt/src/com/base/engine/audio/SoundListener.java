package com.base.engine.audio;

import com.base.engine.common.Quaternion;

public interface SoundListener extends SoundSource {
	public Quaternion getSoundOrientation();
}
