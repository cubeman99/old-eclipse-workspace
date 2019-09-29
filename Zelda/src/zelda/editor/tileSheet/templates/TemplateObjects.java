package zelda.editor.tileSheet.templates;

import zelda.common.geometry.Point;
import zelda.common.util.Colors;
import zelda.editor.tileSheet.ObjectsetTemplate;
import zelda.game.entity.logic.LogicCondition;
import zelda.game.entity.logic.LogicLavaHarden;
import zelda.game.entity.logic.LogicRelay;
import zelda.game.entity.logic.LogicTrackIntersection;
import zelda.game.entity.logic.PuzzleColoredGel;
import zelda.game.entity.logic.PuzzleJumpTiles;
import zelda.game.entity.logic.PuzzleTilePath;
import zelda.game.entity.logic.WarpPoint;
import zelda.game.entity.object.dungeon.ObjectBombableBlock;
import zelda.game.entity.object.dungeon.ObjectBombableWall;
import zelda.game.entity.object.dungeon.ObjectButton;
import zelda.game.entity.object.dungeon.ObjectChest;
import zelda.game.entity.object.dungeon.ObjectCrackedFloor;
import zelda.game.entity.object.dungeon.ObjectEyeStatue;
import zelda.game.entity.object.dungeon.ObjectHardenedLava;
import zelda.game.entity.object.dungeon.ObjectLantern;
import zelda.game.entity.object.dungeon.ObjectLever;
import zelda.game.entity.object.dungeon.ObjectLockedBlock;
import zelda.game.entity.object.dungeon.ObjectMineCart;
import zelda.game.entity.object.dungeon.ObjectMovableBlock;
import zelda.game.entity.object.dungeon.ObjectPlant;
import zelda.game.entity.object.dungeon.ObjectPlatform;
import zelda.game.entity.object.dungeon.ObjectPot;
import zelda.game.entity.object.dungeon.ObjectStopLight;
import zelda.game.entity.object.dungeon.color.ObjectColorCube;
import zelda.game.entity.object.dungeon.color.ObjectColorCubeSlot;
import zelda.game.entity.object.dungeon.color.ObjectColorLantern;
import zelda.game.entity.object.dungeon.color.ObjectColorSwitch;
import zelda.game.entity.object.dungeon.color.ObjectColorTile;
import zelda.game.entity.object.dungeon.color.ObjectJumpTile;
import zelda.game.entity.object.dungeon.door.ObjectBasicDoor;
import zelda.game.entity.object.dungeon.door.ObjectBossKeyDoor;
import zelda.game.entity.object.dungeon.door.ObjectMineCartDoor;
import zelda.game.entity.object.dungeon.door.ObjectSmallKeyDoor;
import zelda.game.entity.object.global.ObjectBush;
import zelda.game.entity.object.global.ObjectCrystal;
import zelda.game.entity.object.global.ObjectDiamondRock;
import zelda.game.entity.object.global.ObjectOwl;
import zelda.game.entity.object.global.ObjectPlayerStart;
import zelda.game.entity.object.global.ObjectPullHandle;
import zelda.game.entity.object.global.ObjectRewardHeartContainer;
import zelda.game.entity.object.global.ObjectRewardHeartPiece;
import zelda.game.entity.object.global.ObjectRewardKey;
import zelda.game.entity.object.global.ObjectRock;
import zelda.game.entity.object.global.ObjectSign;
import zelda.game.entity.object.overworld.ObjectBurnableTree;
import zelda.game.entity.object.overworld.ObjectCactus;
import zelda.game.entity.object.overworld.ObjectDirtPile;
import zelda.game.entity.object.overworld.ObjectGrass;
import zelda.game.monster.Monster;
import zelda.game.monster.MonsterBeetle;
import zelda.game.monster.MonsterBladeTrap;
import zelda.game.monster.MonsterCukeman;
import zelda.game.monster.MonsterGopongaFlower;
import zelda.game.monster.MonsterHardhatBeetle;
import zelda.game.monster.MonsterLeever;
import zelda.game.monster.MonsterMiniMoldorm;
import zelda.game.monster.MonsterPincer;
import zelda.game.monster.MonsterSpark;
import zelda.game.monster.MonsterStalfos;
import zelda.game.monster.MonsterWaterTektike;
import zelda.game.monster.jumpMonster.MonsterColorGel;
import zelda.game.monster.jumpMonster.MonsterPolsVoice;
import zelda.game.monster.jumpMonster.MonsterTektike;
import zelda.game.monster.jumpMonster.MonsterZol;
import zelda.game.monster.walkMonster.MonsterArmos;
import zelda.game.monster.walkMonster.MonsterDarknut;
import zelda.game.monster.walkMonster.MonsterGhini;
import zelda.game.monster.walkMonster.MonsterGibdo;
import zelda.game.monster.walkMonster.MonsterMoblin;
import zelda.game.monster.walkMonster.MonsterOctorok;
import zelda.game.monster.walkMonster.MonsterPigMoblin;
import zelda.game.monster.walkMonster.MonsterSandCrab;
import zelda.game.monster.walkMonster.MonsterShroudedStalfos;
import zelda.game.monster.walkMonster.chargeMonster.MonsterLynel;
import zelda.game.monster.walkMonster.chargeMonster.MonsterRope;
import zelda.game.monster.walkMonster.chargeMonster.MonsterSpikedBeetle;


public class TemplateObjects extends ObjectsetTemplate {

	public TemplateObjects() {
		super(19, 9);

		setObject(0, 0, new ObjectPlayerStart());
		setProperty(0, 0, "dont_create_object", "true");
		
		addObject(new ObjectGrass());
		addObject(new ObjectBush());
		addObject(new ObjectCrystal());
		addObject(new ObjectPlant());
		addObject(new ObjectBurnableTree());
		addObject(new ObjectPot());
		addObject(new ObjectRock());
		addObject(new ObjectDiamondRock());
		addObject(new ObjectCactus());
		addObject(new ObjectSign());
		addObject(new ObjectDirtPile());

		addObject(new ObjectOwl());
		addObject(new ObjectColorCube());
		addObject(new ObjectColorCubeSlot());
		addObject(new ObjectColorLantern());
		addObject(new ObjectBombableBlock());
		addObject(new ObjectMovableBlock());
		addObject(new ObjectEyeStatue());
		addObject(new ObjectBasicDoor());
		addObject(new ObjectButton());
		addObject(new ObjectSmallKeyDoor());
		addObject(new ObjectBossKeyDoor());
		addObject(new ObjectLantern());
		addObject(new ObjectPlatform());
		addObject(new ObjectMineCart());
		addObject(new ObjectChest());
		addObject(new ObjectRewardKey());
		addObject(new ObjectRewardHeartContainer());
		addObject(new ObjectRewardHeartPiece());
		
		addObject(new LogicCondition());
		
		addObject(new MonsterOctorok(Monster.COLOR_RED));
		addObject(new MonsterOctorok(Monster.COLOR_BLUE));
		addObject(new MonsterMoblin(Monster.COLOR_RED, Monster.ITEM_ARROW));
		addObject(new MonsterMoblin(Monster.COLOR_BLUE, Monster.ITEM_ARROW));
		addObject(new MonsterMoblin(Monster.COLOR_RED, Monster.ITEM_BOOMERANG));
		addObject(new MonsterMoblin(Monster.COLOR_BLUE, Monster.ITEM_BOOMERANG));
		addObject(new MonsterMoblin(Monster.COLOR_RED, Monster.ITEM_SWORD));
		addObject(new MonsterMoblin(Monster.COLOR_BLUE, Monster.ITEM_SWORD));
		addObject(new MonsterPigMoblin(Monster.COLOR_RED, Monster.ITEM_ARROW));
		addObject(new MonsterPigMoblin(Monster.COLOR_BLUE, Monster.ITEM_ARROW));
		addObject(new MonsterPigMoblin(Monster.COLOR_RED, Monster.ITEM_SWORD));
		addObject(new MonsterPigMoblin(Monster.COLOR_BLUE, Monster.ITEM_SWORD));
		addObject(new MonsterDarknut(Monster.COLOR_RED, Monster.ITEM_ARROW));
		addObject(new MonsterDarknut(Monster.COLOR_BLUE, Monster.ITEM_ARROW));
		addObject(new MonsterDarknut(Monster.COLOR_RED, Monster.ITEM_SWORD));
		addObject(new MonsterDarknut(Monster.COLOR_BLUE, Monster.ITEM_SWORD));
		addObject(new MonsterShroudedStalfos(Monster.ITEM_ARROW));
		addObject(new MonsterShroudedStalfos(Monster.ITEM_SWORD));
		addObject(new MonsterBeetle());
		addObject(new MonsterStalfos());
		addObject(new MonsterPincer());
		addObject(new MonsterHardhatBeetle());
		addObject(new MonsterWaterTektike());
		addObject(new MonsterGopongaFlower());
		addObject(new MonsterCukeman());
		addObject(new MonsterOctorok(Monster.COLOR_GOLD));
		addObject(new MonsterRope());
		addObject(new MonsterSandCrab());
		addObject(new MonsterGibdo());
		addObject(new MonsterLynel(Monster.COLOR_RED));
		addObject(new MonsterLynel(Monster.COLOR_BLUE));
		addObject(new MonsterLynel(Monster.COLOR_GOLD));
		addObject(new MonsterMiniMoldorm());
		addObject(new MonsterSpikedBeetle());
		addObject(new MonsterGhini());
		addObject(new MonsterLeever(Monster.COLOR_RED));
		addObject(new MonsterLeever(Monster.COLOR_BLUE));
		addObject(new MonsterLeever(Monster.COLOR_ORANGE));
		addObject(new MonsterTektike(Monster.COLOR_ORANGE));
		addObject(new MonsterTektike(Monster.COLOR_BLUE));
		addObject(new MonsterPolsVoice());
		addObject(new MonsterZol(Monster.COLOR_RED));
		addObject(new MonsterZol(Monster.COLOR_GREEN));
		addObject(new MonsterArmos(Monster.COLOR_RED));
		addObject(new MonsterArmos(Monster.COLOR_BLUE));
		addObject(new MonsterSpark());
		
		
		addObject(new ObjectMineCartDoor());
		addObject(new ObjectStopLight());
		addObject(new ObjectLockedBlock());
		addObject(new ObjectLever());
		addObject(new LogicTrackIntersection());
		addObject(new ObjectJumpTile());
		addObject(new ObjectColorSwitch());
		addObject(new LogicRelay());
		
		addObject(new ObjectColorTile());
		Point p = addObject(new ObjectColorTile());
		setProperty(p.x, p.y, "color", Colors.BLUE);
		
		addObject(new PuzzleTilePath());
		addObject(new PuzzleColoredGel());
		addObject(new PuzzleJumpTiles());
		
		addObject(new ObjectBombableWall());
		addObject(new ObjectCrackedFloor());
		addObject(new ObjectPullHandle());
		addObject(new ObjectHardenedLava());
		
		addObject(new LogicLavaHarden());
		addObject(new WarpPoint());
		
		addObject(new MonsterBladeTrap(Monster.COLOR_RED));
		addObject(new MonsterBladeTrap(Monster.COLOR_BLUE));
		addObject(new MonsterBladeTrap(Monster.COLOR_GREEN));
		addObject(new MonsterBladeTrap(Monster.COLOR_ORANGE));
	}
}
