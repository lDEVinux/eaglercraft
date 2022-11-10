package net.minecraft.src;

public class DispenserBehaviors {
	private static boolean hasInit = false;
	public static void func_96467_a() {
		if(hasInit) {
			return;
		}
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.arrow, new DispenserBehaviorArrow());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.egg, new DispenserBehaviorEgg());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.snowball, new DispenserBehaviorSnowball());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.expBottle, new DispenserBehaviorExperience());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.potion, new DispenserBehaviorPotion());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.monsterPlacer, new DispenserBehaviorMobEgg());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.firework, new DispenserBehaviorFireworks());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.fireballCharge, new DispenserBehaviorFireball());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.boat, new DispenserBehaviorBoat());
		DispenserBehaviorFilledBucket var0 = new DispenserBehaviorFilledBucket();
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketLava, var0);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketWater, var0);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketEmpty, new DispenserBehaviorEmptyBucket());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.flintAndSteel, new DispenserBehaviorFire());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.dyePowder, new DispenserBehaviorDye());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.itemsList[Block.tnt.blockID],
				new DispenserBehaviorTNT());
		hasInit = true;
	}
}
