package graphics.library;

import graphics.Animation;
import graphics.AnimationFrame;

/**
 * The library that stores all game animations.
 * @author	Robert Jordan
 */
public class Animations {

	// ======================= Members ========================
	
	// Link
	public Animation[] walk				= new Animation[4];
	public Animation[] walkShield1A		= new Animation[4];
	public Animation[] walkShield2B		= new Animation[4];
	public Animation[] walkShield1B		= new Animation[4];
	public Animation[] walkShield2A		= new Animation[4];
	public Animation[] walkHold			= new Animation[4];
	public Animation[] swim				= new Animation[4];
	public Animation[] jump				= new Animation[4];
	public Animation[] push				= new Animation[4];
	public Animation[] lift				= new Animation[4];
	public Animation[] throwing			= new Animation[4];
	public Animation[] swing			= new Animation[4];
	public Animation[] spin				= new Animation[4];
	public Animation[] bigSwing			= new Animation[4];
	
	public Animation dive;
	
	// Props
	public Animation[][] swordSwing		= new Animation[3][4];
	public Animation[][] swordSpin		= new Animation[3][4];
	public Animation[][] swordCharge	= new Animation[3][4];
	public Animation[] biggoronSwing	= new Animation[4];
	public Animation[] objectLift		= new Animation[4];
	public Animation[] slingshot		= new Animation[4];
	

	public Animation emberSeedEffect;
	public Animation scentSeedEffectA;
	public Animation scentSeedEffectB;
	public Animation pegasusSeedEffectA;
	public Animation pegasusSeedEffectB;
	public Animation galeSeedEffectA;
	public Animation galeSeedEffectB;
	public Animation mysterySeedEffect;
	public Animation shadow;
	public Animation grass;
	public Animation wade;

	// ===================== Constructors =====================
	
	/** Constructs the animations library. */
	public Animations() {
		defineWalkAnimations();
		defineActionAnimations();
		definePropAnimations();
		

		emberSeedEffect = new Animation();
		emberSeedEffect.addFrame(2, 1, 3, 0, 0);
		emberSeedEffect.addFrame(2, 1, 2, 0, 0);
		for (int i = 0; i < 9; i++) {
			emberSeedEffect.addFrame(2, 1, 21, 0, 0);
			emberSeedEffect.addFrame(2, 1, 1, 0, 0);
			emberSeedEffect.addFrame(2, 1, 2, 0, 0);
		}
		
		scentSeedEffectA = new Animation();
		for (int i = 0; i < 13; i++) {
			scentSeedEffectA.addFrame(8, 0, 3, 0, 0);
			scentSeedEffectA.addFrame(8, 1, 3, 0, 0);
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				scentSeedEffectA.addFrame(1, 0, 3, 0, 0);
				scentSeedEffectA.addFrame(1);
			}
			for (int j = 0; j < 4; j++) {
				scentSeedEffectA.addFrame(1, 1, 3, 0, 0);
				scentSeedEffectA.addFrame(1);
			}
		}
		
		scentSeedEffectB = new Animation();
		scentSeedEffectB.addFrame(3, 3, 5, 0, 0);
		scentSeedEffectB.addFrame(3, 0, 5, 0, 0);
		scentSeedEffectB.addFrame(3, 1, 5, 0, 0);
		
		pegasusSeedEffectB = new Animation();
		pegasusSeedEffectB.addFrame(3, 3, 5, 0, 0);
		pegasusSeedEffectB.addFrame(3, 0, 5, 0, 0);
		pegasusSeedEffectB.addFrame(3, 1, 5, 0, 0);
		
		// Gale seed animation long
		galeSeedEffectA = new Animation();
		for (int i = 0; i < 256; i++) {
			if (i < 10 || i % 2 == 0) {
				int index = ((i + 1) / 4) % 4;
				final int[] offsets = {5, 0, 10, 15};
				galeSeedEffectA.addFrame(1, 4 + ((i / 4) % 2), 1 + offsets[index], 0, 0);
			}
			else {
				galeSeedEffectA.addFrame(1);
			}
		}

		// Gale seed animation short
		galeSeedEffectB = new Animation();
		for (int i = 0; i < 30; i++) {
			if (i < 10 || i % 2 == 0) {
				int index = ((i + 1) / 4) % 4;
				final int[] offsets = {5, 0, 10, 15};
				galeSeedEffectB.addFrame(1, 4 + ((i / 4) % 2), 1 + offsets[index], 0, 0);
			}
			else {
				galeSeedEffectB.addFrame(1);
			}
		}
		
		mysterySeedEffect = new Animation();
		mysterySeedEffect.addFrame(3, 3, 10, 0, 0);
		mysterySeedEffect.addFrame(3, 0, 10, 0, 0);
		mysterySeedEffect.addFrame(3, 1, 10, 0, 0);
		
		
		shadow = new Animation();
		shadow.addFrame(1, 0, 0, 0, 0);
		shadow.addFrame(1);
		
		grass = new Animation();
		grass.addFrame(1);
		grass.getFrame(0).addPart(6, 1, -3, 1);
		grass.getFrame(0).addPart(6, 1, 3, 1);
		grass.addFrame(1);
		grass.getFrame(1).addPart(7, 1, -3, 1);
		grass.getFrame(1).addPart(7, 1, 3, 1);
		
		wade = new Animation();
		wade.addFrame(8);
		wade.getFrame(0).addPart(4, 2, 3, 3);
		wade.getFrame(0).addPart(5, 2, -3, 3);
		wade.addFrame(8);
		wade.getFrame(1).addPart(4, 2, 2, 3);
		wade.getFrame(1).addPart(5, 2, -2, 3);
		wade.addFrame(8);
		wade.getFrame(2).addPart(4, 2, 1, 4);
		wade.getFrame(2).addPart(5, 2, -1, 4);
		wade.addFrame(8);
		wade.getFrame(3).addPart(4, 2, 0, 5);
		wade.getFrame(3).addPart(5, 2, 0, 5);
	}
	/** Defines Link's walking animations. */
	private void defineWalkAnimations() {

		// Normal walking
		walk[0] = new Animation();
		walk[0].addFrame(1, 0, 0, 0, 0);
		walk[0].addFrame(6, 1, 0, 0, 0);
		walk[0].addFrame(5, 0, 0, 0, 0);

		walk[1] = new Animation();
		walk[1].addFrame(1, 2, 0, 0, 0);
		walk[1].addFrame(6, 3, 0, 0, 0);
		walk[1].addFrame(5, 2, 0, 0, 0);
		
		walk[2] = new Animation();
		walk[2].addFrame(1, 4, 0, 0, 0);
		walk[2].addFrame(6, 5, 0, 0, 0);
		walk[2].addFrame(5, 4, 0, 0, 0);
		
		walk[3] = new Animation();
		walk[3].addFrame(1, 6, 0, 0, 0);
		walk[3].addFrame(6, 7, 0, 0, 0);
		walk[3].addFrame(5, 6, 0, 0, 0);
		
		// Small shield down
		walkShield1A[0] = new Animation();
		walkShield1A[0].addFrame(1, 0, 1, 0, 0);
		walkShield1A[0].addFrame(6, 1, 1, 0, 0);
		walkShield1A[0].addFrame(5, 0, 1, 0, 0);

		walkShield1A[1] = new Animation();
		walkShield1A[1].addFrame(1, 2, 1, 0, 0);
		walkShield1A[1].addFrame(6, 3, 1, 0, 0);
		walkShield1A[1].addFrame(5, 2, 1, 0, 0);

		walkShield1A[2] = new Animation();
		walkShield1A[2].addFrame(1, 4, 0, 0, 0);
		walkShield1A[2].addFrame(6, 5, 0, 0, 0);
		walkShield1A[2].addFrame(5, 4, 0, 0, 0);
		
		walkShield1A[3] = new Animation();
		walkShield1A[3].addFrame(1, 6, 1, 0, 0);
		walkShield1A[3].addFrame(6, 7, 1, 0, 0);
		walkShield1A[3].addFrame(5, 6, 1, 0, 0);

		// Small shield up
		walkShield2A[0] = new Animation();
		walkShield2A[0].addFrame(1, 0, 2, 0, 0);
		walkShield2A[0].addFrame(6, 1, 2, 0, 0);
		walkShield2A[0].addFrame(5, 0, 2, 0, 0);

		walkShield2A[1] = new Animation();
		walkShield2A[1].addFrame(1, 2, 2, 0, 0);
		walkShield2A[1].addFrame(6, 3, 2, 0, 0);
		walkShield2A[1].addFrame(5, 2, 2, 0, 0);
		
		walkShield2A[2] = new Animation();
		walkShield2A[2].addFrame(1, 4, 2, 0, 0);
		walkShield2A[2].addFrame(6, 5, 2, 0, 0);
		walkShield2A[2].addFrame(5, 4, 2, 0, 0);

		walkShield2A[3] = new Animation();
		walkShield2A[3].addFrame(1, 6, 2, 0, 0);
		walkShield2A[3].addFrame(6, 7, 2, 0, 0);
		walkShield2A[3].addFrame(5, 6, 2, 0, 0);

		// Large shield down
		walkShield1B[0] = new Animation();
		walkShield1B[0].addFrame(1, 0, 3, 0, 0);
		walkShield1B[0].addFrame(6, 1, 3, 0, 0);
		walkShield1B[0].addFrame(5, 0, 3, 0, 0);

		walkShield1B[1] = new Animation();
		walkShield1B[1].addFrame(1, 2, 1, 0, 0);
		walkShield1B[1].addFrame(6, 3, 1, 0, 0);
		walkShield1B[1].addFrame(5, 2, 1, 0, 0);

		walkShield1B[2] = new Animation();
		walkShield1B[2].addFrame(1, 4, 0, 0, 0);
		walkShield1B[2].addFrame(6, 5, 0, 0, 0);
		walkShield1B[2].addFrame(5, 4, 0, 0, 0);

		walkShield1B[3] = new Animation();
		walkShield1B[3].addFrame(1, 6, 1, 0, 0);
		walkShield1B[3].addFrame(6, 7, 1, 0, 0);
		walkShield1B[3].addFrame(5, 6, 1, 0, 0);

		// Large shield up
		walkShield2B[0] = new Animation();
		walkShield2B[0].addFrame(1, 0, 2, 0, 0);
		walkShield2B[0].addFrame(6, 1, 2, 0, 0);
		walkShield2B[0].addFrame(5, 0, 2, 0, 0);

		walkShield2B[1] = new Animation();
		walkShield2B[1].addFrame(1, 2, 3, 0, 0);
		walkShield2B[1].addFrame(6, 3, 3, 0, 0);
		walkShield2B[1].addFrame(5, 2, 3, 0, 0);

		walkShield2B[2] = new Animation();
		walkShield2B[2].addFrame(1, 4, 2, 0, 0);
		walkShield2B[2].addFrame(6, 5, 2, 0, 0);
		walkShield2B[2].addFrame(5, 4, 2, 0, 0);

		walkShield2B[3] = new Animation();
		walkShield2B[3].addFrame(1, 6, 2, 0, 0);
		walkShield2B[3].addFrame(6, 7, 2, 0, 0);
		walkShield2B[3].addFrame(5, 6, 2, 0, 0);

		// Holding
		walkHold[0] = new Animation();
		walkHold[0].addFrame(1, 0, 5, 0, 0);
		walkHold[0].addFrame(6, 1, 5, 0, 0);
		walkHold[0].addFrame(5, 0, 5, 0, 0);

		walkHold[1] = new Animation();
		walkHold[1].addFrame(1, 2, 5, 0, 0);
		walkHold[1].addFrame(6, 3, 5, 0, 0);
		walkHold[1].addFrame(5, 2, 5, 0, 0);

		walkHold[2] = new Animation();
		walkHold[2].addFrame(1, 4, 5, 0, 0);
		walkHold[2].addFrame(6, 5, 5, 0, 0);
		walkHold[2].addFrame(5, 4, 5, 0, 0);

		walkHold[3] = new Animation();
		walkHold[3].addFrame(1, 6, 5, 0, 0);
		walkHold[3].addFrame(6, 7, 5, 0, 0);
		walkHold[3].addFrame(5, 6, 5, 0, 0);
		
		jump[0] = new Animation();
		jump[0].addFrame(8, 0, 11, 0, 0);
		jump[0].addFrame(9, 1, 11, 0, 0);
		jump[0].addFrame(6, 2, 11, 0, 0);
		jump[0].addFrame(7, 1, 0, 0, 0);
		
		jump[1] = new Animation();
		jump[1].addFrame(8, 3, 11, 0, 0);
		jump[1].addFrame(9, 4, 11, 0, 0);
		jump[1].addFrame(6, 5, 11, 0, 0);
		jump[1].addFrame(7, 3, 0, 0, 0);

		jump[2] = new Animation();
		jump[2].addFrame(8, 0, 11, 0, 0);
		jump[2].addFrame(9, 1, 11, 0, 0);
		jump[2].addFrame(6, 2, 11, 0, 0);
		jump[2].addFrame(7, 5, 0, 0, 0);
		
		jump[3] = new Animation();
		jump[3].addFrame(8, 3, 11, 0, 0);
		jump[3].addFrame(9, 4, 11, 0, 0);
		jump[3].addFrame(6, 5, 11, 0, 0);
		jump[3].addFrame(7, 7, 0, 0, 0);

		// Pushing
		push[0] = new Animation();
		push[0].addFrame(1, 0, 6, 0, 0);
		push[0].addFrame(6, 1, 6, 0, 0);
		push[0].addFrame(5, 0, 6, 0, 0);

		push[1] = new Animation();
		push[1].addFrame(1, 2, 6, 0, 0);
		push[1].addFrame(6, 3, 6, 0, 0);
		push[1].addFrame(5, 2, 6, 0, 0);
		
		push[2] = new Animation();
		push[2].addFrame(1, 4, 6, 0, 0);
		push[2].addFrame(6, 5, 6, 0, 0);
		push[2].addFrame(5, 4, 6, 0, 0);
		
		push[3] = new Animation();
		push[3].addFrame(1, 6, 6, 0, 0);
		push[3].addFrame(6, 7, 6, 0, 0);
		push[3].addFrame(5, 6, 6, 0, 0);
		

		// Swimming
		swim[0] = new Animation();
		swim[0].addFrame(1, 0, 13, 0, 4);
		swim[0].addFrame(6, 1, 13, 0, 4);
		swim[0].addFrame(5, 0, 13, 0, 4);

		swim[1] = new Animation();
		swim[1].addFrame(1, 2, 13, 0, 4);
		swim[1].addFrame(6, 3, 13, 0, 4);
		swim[1].addFrame(5, 2, 13, 0, 4);
		
		swim[2] = new Animation();
		swim[2].addFrame(1, 4, 13, 0, 4);
		swim[2].addFrame(6, 5, 13, 0, 4);
		swim[2].addFrame(5, 4, 13, 0, 4);
		
		swim[3] = new Animation();
		swim[3].addFrame(1, 6, 13, 0, 4);
		swim[3].addFrame(6, 7, 13, 0, 4);
		swim[3].addFrame(5, 6, 13, 0, 4);
		
		// Diving
		dive = new Animation();
		dive.addFrame(16, 0, 21, 0, 4);
		dive.addFrame(16, 1, 21, 0, 4);
	}
	/** Defines Link's action animations. */
	private void defineActionAnimations() {

		// Lift
		lift[0] = new Animation();
		lift[0].addFrame(4, 1, 7, -4, 0);
		lift[0].addFrame(4, 0, 7, 0, 0);

		lift[1] = new Animation();
		lift[1].addFrame(4, 5, 7, 0, -1);
		lift[1].addFrame(4, 7, 6, 0, 0);
		
		lift[2] = new Animation();
		lift[2].addFrame(4, 4, 7, 4, 0);
		lift[2].addFrame(4, 3, 7, 0, 0);
		
		lift[3] = new Animation();
		lift[3].addFrame(4, 2, 7, 0, 2);
		lift[3].addFrame(4, 2, 6, 0, 0);
		
		// Swing sword
		swing[0] = new Animation();
		swing[0].addFrame(3, 4, 8, 0, 0);
		swing[0].addFrame(3, 0, 4, 0, 0);
		swing[0].addFrame(8, 0, 4, 3, 0);
		swing[0].addFrame(3, 0, 4, 0, 0);

		swing[1] = new Animation();
		swing[1].addFrame(3, 1, 8, 0, 0);
		swing[1].addFrame(3, 2, 4, 0, 0);
		swing[1].addFrame(8, 2, 4, 0, 3);
		swing[1].addFrame(3, 2, 4, 0, 0);

		swing[2] = new Animation();
		swing[2].addFrame(3, 2, 8, 0, 0);
		swing[2].addFrame(3, 4, 4, 0, 0);
		swing[2].addFrame(8, 4, 4, -3, 0);
		swing[2].addFrame(3, 4, 4, 0, 0);

		swing[3] = new Animation();
		swing[3].addFrame(3, 3, 8, 0, 0);
		swing[3].addFrame(3, 6, 4, 0, 0);
		swing[3].addFrame(8, 6, 4, 0, -3);
		swing[3].addFrame(3, 6, 4, 0, 0);
		
		// Spin used in sword spin
		spin[0] = new Animation();
		spin[0].addFrame(5, 0, 4, 3, 0);
		spin[0].addFrame(5, 2, 4, 0, 3);
		spin[0].addFrame(5, 4, 4, -3, 0);
		spin[0].addFrame(5, 6, 4, 0, -3);
		spin[0].addFrame(3, 0, 4, 3, 0);
		
		spin[1] = new Animation();
		spin[1].addFrame(5, 2, 4, 0, 3);
		spin[1].addFrame(5, 4, 4, -3, 0);
		spin[1].addFrame(5, 6, 4, 0, -3);
		spin[1].addFrame(5, 0, 4, 3, 0);
		spin[1].addFrame(3, 2, 4, 0, 3);
		
		spin[2] = new Animation();
		spin[2].addFrame(5, 4, 4, -3, 0);
		spin[2].addFrame(5, 6, 4, 0, -3);
		spin[2].addFrame(5, 0, 4, 3, 0);
		spin[2].addFrame(5, 2, 4, 0, 3);
		spin[2].addFrame(3, 4, 4, -3, 0);

		spin[3] = new Animation();
		spin[3].addFrame(5, 6, 4, 0, -3);
		spin[3].addFrame(5, 0, 4, 3, 0);
		spin[3].addFrame(5, 2, 4, 0, 3);
		spin[3].addFrame(5, 4, 4, -3, 0);
		spin[3].addFrame(3, 6, 4, 0, -3);

		// Biggoron swing
		bigSwing[0] = new Animation();
		bigSwing[0].addFrame(12, 4, 8, 0, 0);
		bigSwing[0].addFrame(9, 0, 4, 0, 0);
		bigSwing[0].addFrame(13, 5, 8, 0, 0);

		bigSwing[1] = new Animation();
		bigSwing[1].addFrame(12, 1, 8, 0, 0);
		bigSwing[1].addFrame(9, 2, 4, 0, 0);
		bigSwing[1].addFrame(13, 5, 8, 0, 0);

		bigSwing[2] = new Animation();
		bigSwing[2].addFrame(12, 2, 8, 0, 0);
		bigSwing[2].addFrame(9, 4, 4, 0, 0);
		bigSwing[2].addFrame(13, 1, 8, 0, 0);

		bigSwing[3] = new Animation();
		bigSwing[3].addFrame(12, 3, 8, 0, 0);
		bigSwing[3].addFrame(9, 6, 4, 0, 0);
		bigSwing[3].addFrame(13, 6, 8, 0, 0);
		
		throwing[0] = new Animation();
		throwing[0].addFrame(9, 0, 4, 0, 0);

		throwing[1] = new Animation();
		throwing[1].addFrame(9, 2, 4, 0, 0);

		throwing[2] = new Animation();
		throwing[2].addFrame(9, 4, 4, 0, 0);

		throwing[3] = new Animation();
		throwing[3].addFrame(9, 6, 4, 0, 0);
	}
	/** Defines the prop animations. */
	private void definePropAnimations() {
		
		// Sword swing
		for (int k = 0; k < 3; k++) {
			int offset = 0;
			if (k == 1)
				offset = 2;
			else if (k == 2)
				offset = 3;
			
			swordSwing[k][0] = new Animation();
			swordSwing[k][0].addFrame(3, 3, 0 + offset, 0, -16);
			swordSwing[k][0].addFrame(3, 7, 0 + offset, 13, -13);
			swordSwing[k][0].addFrame(8, 0, 0 + offset, 19, 4);
			swordSwing[k][0].addFrame(3, 0, 0 + offset, 12, 4);
		
			swordSwing[k][1] = new Animation();
			swordSwing[k][1].addFrame(3, 2, 0 + offset, -15, 2);
			swordSwing[k][1].addFrame(3, 5, 0 + offset, -13, 15);
			swordSwing[k][1].addFrame(8, 1, 0 + offset, 3, 19);
			swordSwing[k][1].addFrame(3, 1, 0 + offset, 3, 14);
			
			swordSwing[k][2] = new Animation();
			swordSwing[k][2].addFrame(3, 3, 0 + offset, 0, -16);
			swordSwing[k][2].addFrame(3, 6, 0 + offset, -13, -13);
			swordSwing[k][2].addFrame(8, 2, 0 + offset, -19, 4);
			swordSwing[k][2].addFrame(3, 2, 0 + offset, -12, 4);
			
			swordSwing[k][3] = new Animation();
			swordSwing[k][3].addFrame(3, 0, 0 + offset, 16, 0);
			swordSwing[k][3].addFrame(3, 7, 0 + offset, 13, -13);
			swordSwing[k][3].addFrame(8, 3, 0 + offset, -4, -19);
			swordSwing[k][3].addFrame(3, 3, 0 + offset, -4, -12);
			
			// Sword spin attack
			swordSpin[k][0] = new Animation();
			swordSpin[k][0].addFrame(3, 0, 0 + offset, 19, 4);
			swordSpin[k][0].addFrame(2, 4, 0 + offset, 16, 16);
			swordSpin[k][0].addFrame(3, 1, 0 + offset, 3, 19);
			swordSpin[k][0].addFrame(2, 5, 0 + offset, -13, 15);
			swordSpin[k][0].addFrame(3, 2, 0 + offset, -19, 4);
			swordSpin[k][0].addFrame(2, 6, 0 + offset, -13, -13);
			swordSpin[k][0].addFrame(3, 3, 0 + offset, -4, -19);
			swordSpin[k][0].addFrame(2, 7, 0 + offset, 16, -16);
			swordSpin[k][0].addFrame(3, 0, 0 + offset, 19, 4);
			
			for (int i = 0; i < 3; i++) {
				swordSpin[k][i + 1] = new Animation();
				for (int j = 0; j < 9; j++) {
					if (j >= 7)
						swordSpin[k][i + 1].addFrame(new AnimationFrame(swordSpin[k][i].getFrame(j % 7 + 1)));
					else
						swordSpin[k][i + 1].addFrame(new AnimationFrame(swordSpin[k][i].getFrame(j % 7 + 2)));
				}
			}
			
			// Sword charge
			swordCharge[k][0] = new Animation();
			swordCharge[k][0].addFrame(4, 0, 1, 12, 4);
			swordCharge[k][0].addFrame(4, 0, 0 + offset, 12, 4);
		
			swordCharge[k][1] = new Animation();
			swordCharge[k][1].addFrame(3, 1, 1, 3, 14);
			swordCharge[k][1].addFrame(3, 1, 0 + offset, 3, 14);
			
			swordCharge[k][2] = new Animation();
			swordCharge[k][2].addFrame(3, 2, 1, -12, 4);
			swordCharge[k][2].addFrame(3, 2, 0 + offset, -12, 4);
			
			swordCharge[k][3] = new Animation();
			swordCharge[k][3].addFrame(3, 3, 1, -4, -12);
			swordCharge[k][3].addFrame(3, 3, 0 + offset, -4, -12);
		}
		
		// Biggoron sword swing
		biggoronSwing[0] = new Animation();
		biggoronSwing[0].addFrame(12);
		biggoronSwing[0].getFrame(0).addPart(3, 6, -2, -16);
		biggoronSwing[0].getFrame(0).addPart(3, 7, -2, -32);
		biggoronSwing[0].addFrame(5, 7, 6, 16, -16);
		biggoronSwing[0].addFrame(4);
		biggoronSwing[0].getFrame(2).addPart(0, 6, 16, 0);
		biggoronSwing[0].getFrame(2).addPart(0, 7, 32, 0);
		biggoronSwing[0].addFrame(4, 4, 6, 16, 16);
		biggoronSwing[0].addFrame(9);
		biggoronSwing[0].getFrame(4).addPart(1, 6, 1, 16);
		biggoronSwing[0].getFrame(4).addPart(1, 7, 1, 32);

		biggoronSwing[1] = new Animation();
		biggoronSwing[1].addFrame(12);
		biggoronSwing[1].getFrame(0).addPart(2, 6, -16, 0);
		biggoronSwing[1].getFrame(0).addPart(2, 7, -32, 0);
		biggoronSwing[1].addFrame(5, 5, 6, -13, 15);
		biggoronSwing[1].addFrame(4);
		biggoronSwing[1].getFrame(2).addPart(1, 6, 1, 16);
		biggoronSwing[1].getFrame(2).addPart(1, 7, 1, 32);
		biggoronSwing[1].addFrame(4, 4, 6, 16, 16);
		biggoronSwing[1].addFrame(9);
		biggoronSwing[1].getFrame(4).addPart(0, 6, 16, 0);
		biggoronSwing[1].getFrame(4).addPart(0, 7, 32, 0);
		
		biggoronSwing[2] = new Animation();
		biggoronSwing[2].addFrame(12);
		biggoronSwing[2].getFrame(0).addPart(3, 6, -2, -16);
		biggoronSwing[2].getFrame(0).addPart(3, 7, -2, -32);
		biggoronSwing[2].addFrame(5, 6, 6, -13, -13);
		biggoronSwing[2].addFrame(4);
		biggoronSwing[2].getFrame(2).addPart(2, 6, -16, 0);
		biggoronSwing[2].getFrame(2).addPart(2, 7, -32, 0);
		biggoronSwing[2].addFrame(4, 5, 6, -13, 15);
		biggoronSwing[2].addFrame(9);
		biggoronSwing[2].getFrame(4).addPart(1, 6, 1, 16);
		biggoronSwing[2].getFrame(4).addPart(1, 7, 1, 32);

		biggoronSwing[3] = new Animation();
		biggoronSwing[3].addFrame(12);
		biggoronSwing[3].getFrame(0).addPart(0, 6, 16, 0);
		biggoronSwing[3].getFrame(0).addPart(0, 7, 32, 0);
		biggoronSwing[3].addFrame(5, 7, 6, 16, -16);
		biggoronSwing[3].addFrame(4);
		biggoronSwing[3].getFrame(2).addPart(3, 6, -2, -16);
		biggoronSwing[3].getFrame(2).addPart(3, 7, -2, -32);
		biggoronSwing[3].addFrame(4, 6, 6, -13, -13);
		biggoronSwing[3].addFrame(9);
		biggoronSwing[3].getFrame(4).addPart(2, 6, -16, 0);
		biggoronSwing[3].getFrame(4).addPart(2, 7, -32, 0);
		
		// Lifting an object
		objectLift[0] = new Animation();
		objectLift[0].addFrame(4, 0, 14, 7, 0);
		objectLift[0].addFrame(4, 0, 14, 3, -8);
		objectLift[0].addFrame(1, 0, 14, 0, -14);

		objectLift[1] = new Animation();
		objectLift[1].addFrame(4, 0, 14, 0, 6);
		objectLift[1].addFrame(4, 0, 14, 0, 4);
		objectLift[1].addFrame(1, 0, 14, 0, -14);

		objectLift[2] = new Animation();
		objectLift[2].addFrame(4, 0, 14, -7, 0);
		objectLift[2].addFrame(4, 0, 14, -3, -8);
		objectLift[2].addFrame(1, 0, 14, 0, -14);
		
		objectLift[3] = new Animation();
		objectLift[3].addFrame(4, 0, 14, 0, -8);
		objectLift[3].addFrame(4, 0, 14, 0, -6);
		objectLift[3].addFrame(1, 0, 14, 0, -14);
		
		// Slingshot
		slingshot[0] = new Animation();
		slingshot[0].addFrame(9, 0, 9, 12, 0);

		slingshot[1] = new Animation();
		slingshot[1].addFrame(9, 1, 9, 3, 13);
		
		slingshot[2] = new Animation();
		slingshot[2].addFrame(9, 2, 9, -12, 0);
		
		slingshot[3] = new Animation();
		slingshot[3].addFrame(9, 3, 9, -4, -12);
	}
}