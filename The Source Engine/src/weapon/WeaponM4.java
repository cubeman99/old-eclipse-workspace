package weapon;

import entity.unit.Unit;

public class WeaponM4 extends Weapon {
	
	public WeaponM4(Unit owner) {
		super(owner);
		
		name			= "M4 Carbine";
		weight      	= 2.88;
		
		maxClipAmmo 	= 30;
		maxPackAmmo		= 150;
		reloadTime		= 1000;
		reloadType  	= ReloadType.MAGAZINE;
		
		fireModes       = new FireMode[] {FireMode.AUTOMATIC, FireMode.SEMI_AUTO, FireMode.BURST};
		burstFireRounds = 3;
		recoilFactor    = 0.1;
		fireRate		= 750;
		bulletsPerRound	= 1;
		effectiveRange  = 550;
		maximumRange    = 1800;
		muzzleVelocity	= 884;
		bulletDamage    = 10;
		
		
		
		name			= "P90";
		weight      	= 2.54;
		
		maxClipAmmo 	= 50;
		maxPackAmmo		= 150;
		reloadTime		= 1000;
		reloadType  	= ReloadType.MAGAZINE;

		fireModes       = new FireMode[] {FireMode.AUTOMATIC, FireMode.SEMI_AUTO, FireMode.BURST};
		burstFireRounds = 3;
		recoilFactor    = 0.2;
		fireRate		= 900;
		bulletsPerRound	= 1;
		effectiveRange  = 200;
		maximumRange    = 1800;
		muzzleVelocity	= 715;
		bulletDamage    = 8;
		
		
		maxPackAmmo		= 400;

//		effectiveRange  = 3000;
//		recoilFactor    = 0.04;
//		maxClipAmmo 	= 200;
//		maxPackAmmo		= 10000;
//		bulletDamage    = 200;

		/*
		name			= "Steyr AUG";
		weight      	= 3.6;
		
		maxClipAmmo 	= 30;
		maxPackAmmo		= 150;
		reloadTime		= 1000;
		reloadType  	= ReloadType.MAGAZINE;
		
		fireRate		= 700;
		bulletsPerRound	= 1;
		effectiveRange  = 300;
		maximumRange    = 2700;
		muzzleVelocity	= 970;
		bulletDamage    = 8;
		*/
		
		/*
		maxClipAmmo 	= 8;
		maxPackAmmo		= 100;
		reloadTime		= 700;
		reloadType  	= ReloadType.CLIP;
		
		fireRate		= 120;
		bulletsPerRound	= 10;
		effectiveRange  = 50;
		maximumRange    = 2700;
		muzzleVelocity	= 400;
		bulletDamage    = 7;
		*/
		
		initialize();
	}
}
