

public class WeaponAR1 extends Weapon {
	
	public WeaponAR1() {
		super();
		
		name			= "AK47";
		weaponType		= "Assault Rifle";
		maxPackAmmo		= 90;
		maxClipAmmo		= 45;
		fireTime		= 0.1;
		reloadTime		= 2.0;
		hasMagazine		= true;
		speedFactor		= 0.8;
		fireSpeedFactor = 0.6;
		
		bulletCount		= 1;
		bulletSpeed		= 15.0;
		dirSpread		= 5.5;
		bulletDamage	= 10;
		
		scopeType		= 1;
		
		initialize();
	}
}
