package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
	/** Is the smallest column used to display a achievement on the GUI. */
	public static int minDisplayColumn;

	/** Is the smallest row used to display a achievement on the GUI. */
	public static int minDisplayRow;

	/** Is the biggest column used to display a achievement on the GUI. */
	public static int maxDisplayColumn;

	/** Is the biggest row used to display a achievement on the GUI. */
	public static int maxDisplayRow;

	/** The list holding all achievements */
	public static List achievementList = new ArrayList();

	/** Is the 'open inventory' achievement. */
	public static Achievement openInventory = null;

	/** Is the 'getting wood' achievement. */
	public static Achievement mineWood = null;

	/** Is the 'benchmarking' achievement. */
	public static Achievement buildWorkBench = null;

	/** Is the 'time to mine' achievement. */
	public static Achievement buildPickaxe = null;

	/** Is the 'hot topic' achievement. */
	public static Achievement buildFurnace = null;

	/** Is the 'acquire hardware' achievement. */
	public static Achievement acquireIron = null;

	/** Is the 'time to farm' achievement. */
	public static Achievement buildHoe = null;

	/** Is the 'bake bread' achievement. */
	public static Achievement makeBread = null;

	/** Is the 'the lie' achievement. */
	public static Achievement bakeCake = null;

	/** Is the 'getting a upgrade' achievement. */
	public static Achievement buildBetterPickaxe = null;

	/** Is the 'delicious fish' achievement. */
	public static Achievement cookFish = null;

	/** Is the 'on a rail' achievement */
	public static Achievement onARail = null;

	/** Is the 'time to strike' achievement. */
	public static Achievement buildSword = null;

	/** Is the 'monster hunter' achievement. */
	public static Achievement killEnemy = null;

	/** is the 'cow tipper' achievement. */
	public static Achievement killCow = null;

	/** Is the 'when pig fly' achievement. */
	public static Achievement flyPig = null;

	/** The achievement for killing a Skeleton from 50 meters aways. */
	public static Achievement snipeSkeleton = null;

	/** Is the 'DIAMONDS!' achievement */
	public static Achievement diamonds = null;

	/** Is the 'We Need to Go Deeper' achievement */
	public static Achievement portal = null;

	/** Is the 'Return to Sender' achievement */
	public static Achievement ghast = null;

	/** Is the 'Into Fire' achievement */
	public static Achievement blazeRod = null;

	/** Is the 'Local Brewery' achievement */
	public static Achievement potion = null;

	/** Is the 'The End?' achievement */
	public static Achievement theEnd = null;

	/** Is the 'The End.' achievement */
	public static Achievement theEnd2 = null;

	/** Is the 'Enchanter' achievement */
	public static Achievement enchantments = null;
	
	public static Achievement overkill = null;

	/** Is the 'Librarian' achievement */
	public static Achievement bookcase = null;

	public static void init() {
		achievementList = new ArrayList();

		openInventory = (new Achievement(0, "openInventory", 0, 0, Item.book, (Achievement) null))
				.setIndependent().registerAchievement();

		mineWood = (new Achievement(1, "mineWood", 2, 1, Block.wood, openInventory))
				.registerAchievement();

		buildWorkBench = (new Achievement(2, "buildWorkBench", 4, -1, Block.workbench, mineWood))
				.registerAchievement();

		buildPickaxe = (new Achievement(3, "buildPickaxe", 4, 2, Item.pickaxeWood,
				buildWorkBench)).registerAchievement();

		buildFurnace = (new Achievement(4, "buildFurnace", 3, 4, Block.furnaceIdle, buildPickaxe))
				.registerAchievement();

		acquireIron = (new Achievement(5, "acquireIron", 1, 4, Item.ingotIron, buildFurnace))
				.registerAchievement();

		buildHoe = (new Achievement(6, "buildHoe", 2, -3, Item.hoeWood, buildWorkBench))
				.registerAchievement();

		makeBread = (new Achievement(7, "makeBread", -1, -3, Item.bread, buildHoe))
				.registerAchievement();

		bakeCake = (new Achievement(8, "bakeCake", 0, -5, Item.cake, buildHoe))
				.registerAchievement();

		buildBetterPickaxe = (new Achievement(9, "buildBetterPickaxe", 6, 2, Item.pickaxeStone,
				buildPickaxe)).registerAchievement();

		cookFish = (new Achievement(10, "cookFish", 2, 6, Item.fishCooked, buildFurnace))
				.registerAchievement();

		onARail = (new Achievement(11, "onARail", 2, 3, Block.rail, acquireIron)).setSpecial()
				.registerAchievement();

		buildSword = (new Achievement(12, "buildSword", 6, -1, Item.swordWood, buildWorkBench))
				.registerAchievement();

		killEnemy = (new Achievement(13, "killEnemy", 8, -1, Item.bone, buildSword))
				.registerAchievement();

		killCow = (new Achievement(14, "killCow", 7, -3, Item.leather, buildSword))
				.registerAchievement();

		flyPig = (new Achievement(15, "flyPig", 8, -4, Item.saddle, killCow)).setSpecial()
				.registerAchievement();

		snipeSkeleton = (new Achievement(16, "snipeSkeleton", 7, 0, Item.bow, killEnemy))
				.setSpecial().registerAchievement();

		diamonds = (new Achievement(17, "diamonds", -1, 5, Item.diamond, acquireIron))
				.registerAchievement();

		portal = (new Achievement(18, "portal", -1, 7, Block.obsidian, diamonds))
				.registerAchievement();

		ghast = (new Achievement(19, "ghast", -4, 8, Item.ghastTear, portal)).setSpecial()
				.registerAchievement();

		blazeRod = (new Achievement(20, "blazeRod", 0, 9, Item.blazeRod, portal))
				.registerAchievement();

		potion = (new Achievement(21, "potion", 2, 8, Item.potion, blazeRod))
				.registerAchievement();

		theEnd = (new Achievement(22, "theEnd", 3, 10, Item.eyeOfEnder, blazeRod)).setSpecial()
				.registerAchievement();

		theEnd2 = (new Achievement(23, "theEnd2", 4, 13, Block.dragonEgg, theEnd)).setSpecial()
				.registerAchievement();

		enchantments = (new Achievement(24, "enchantments", -4, 4, Block.enchantmentTable,
				diamonds)).registerAchievement();

		overkill = (new Achievement(25, "overkill", -4, 1, Item.swordDiamond, enchantments))
				.setSpecial().registerAchievement();

		bookcase = (new Achievement(26, "bookcase", -3, 6, Block.bookShelf, enchantments))
				.registerAchievement();
	}
	
}
