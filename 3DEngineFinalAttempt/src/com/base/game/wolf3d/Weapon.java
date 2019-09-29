package com.base.game.wolf3d;

import com.base.engine.audio.Sound;

public class Weapon {
	private String name;
	private float range;
	private float fireRate;
	private boolean automatic;
	private boolean silent;
	private boolean usesAmmo;
	private int animFireStart;
	private int animFireEnd;
	private boolean unlocked;
	private Sound sound;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Weapon(String name, float range, float fireRate, int animFireStart, int animFireEnd, boolean automatic, boolean silent, boolean usesAmmo, Sound sound) {
		this.name          = name;
		this.range         = range;
		this.fireRate      = fireRate;
		this.automatic     = automatic;
		this.silent        = silent;
		this.usesAmmo      = usesAmmo;
		this.animFireStart = animFireStart;
		this.animFireEnd   = animFireEnd;
		this.sound         = sound;
		
		this.unlocked = false;
	}
	
	

	// =================== ACCESSORS =================== //

	public Sound getSound() {
		return sound;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
	public boolean usesAmmo() {
		return usesAmmo;
	}
	
	public float getFireRate() {
		return fireRate;
	}
	
	public String getName() {
		return name;
	}
	
	public float getRange() {
		return range;
	}
	
	public int getAnimFireStart() {
		return animFireStart;
	}
	
	public int getAnimFireEnd() {
		return animFireEnd;
	}
	
	public boolean isSilent() {
		return silent;
	}
	
	public boolean isAutomatic() {
		return automatic;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void unlock() {
		unlocked = true;
	}
}
