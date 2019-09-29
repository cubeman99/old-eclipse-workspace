

public class WeaponAR2 extends Weapon {
	
	public WeaponAR2() {
		super();
		
		name			= "AR2";
		weaponType		= "Assault Rifle";
		maxPackAmmo		= 1000;
		maxClipAmmo		= 300;
		fireTime		= 0.03;
		reloadTime		= 2.0;
		hasMagazine		= true;
		speedFactor		= 0.35;
		fireSpeedFactor = 0.1;
		
		bulletCount		= 2;
		bulletSpeed		= 17.0;
		dirSpread		= 15;
		bulletDamage	= 5;
		
		scopeType		= 1;
		
		initialize();
	}
}
