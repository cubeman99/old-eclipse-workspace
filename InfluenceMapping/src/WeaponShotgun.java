
public class WeaponShotgun extends Weapon {
	public WeaponShotgun() {
		super();
		
		name			= "Shotgun";
		weaponType		= "(Shotgun)";
		maxPackAmmo		= 40;
		maxClipAmmo		= 8;
		fireTime		= 0.5;
		reloadTime		= 0.5;
		hasMagazine		= false;
		speedFactor		= 0.9;
		fireSpeedFactor = 0.5;
		
		bulletCount		= 8;
		bulletSpeed		= 15.0;
		dirSpread		= 12;
		bulletDamage	= 7;
		
		scopeType		= 1;
		
		initialize();
	}
}
