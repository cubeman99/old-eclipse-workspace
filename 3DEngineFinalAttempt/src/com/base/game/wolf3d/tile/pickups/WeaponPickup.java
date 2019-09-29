package com.base.game.wolf3d.tile.pickups;

import com.base.engine.rendering.Texture;

public class WeaponPickup extends AmmoPickup {
	private int weaponIndex;
	
	public WeaponPickup(String name, Texture texture, int weaponIndex, int ammoAmount) {
		super(name, texture, ammoAmount);
		this.weaponIndex = weaponIndex;
	}
	
	@Override
	public boolean canCollect() {
		return (super.canCollect() || !getPlayer().getWeapon(weaponIndex).isUnlocked());
	}
	
	@Override
	public void onCollect() {
		super.onCollect();
		getPlayer().unlockWeapon(weaponIndex);
	}
	
	@Override
	public WeaponPickup clone() {
		return new WeaponPickup(name, texture, weaponIndex, ammoAmount);
	}
}
