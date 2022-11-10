package net.minecraft.src;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityList {
	/** Provides a mapping between entity classes and a string */
	private static Map stringToClassMapping = new HashMap();
	private static Map stringToClassReflectMapping = new HashMap();

	/** Provides a mapping between a string and an entity classes */
	private static Map classToStringMapping = new HashMap();

	/** provides a mapping between an entityID and an Entity Class */
	private static Map IDtoClassMapping = new HashMap();
	private static Map IDtoClassReflectMapping = new HashMap();

	/** provides a mapping between an Entity Class and an entity ID */
	private static Map classToIDMapping = new HashMap();

	/** Maps entity names to their numeric identifiers */
	private static Map stringToIDMapping = new HashMap();

	/** This is a HashMap of the Creative Entity Eggs/Spawners. */
	public static HashMap entityEggs = new LinkedHashMap();

	/**
	 * adds a mapping between Entity classes and both a string representation and an
	 * ID
	 */
	private static void addMapping(Class par0Class, Function<World, Entity> constructor, String par1Str, int par2) {
		stringToClassMapping.put(par1Str, constructor);
		stringToClassReflectMapping.put(par1Str, par0Class);
		classToStringMapping.put(par0Class, par1Str);
		IDtoClassMapping.put(Integer.valueOf(par2), constructor);
		IDtoClassReflectMapping.put(Integer.valueOf(par2), par0Class);
		classToIDMapping.put(par0Class, Integer.valueOf(par2));
		stringToIDMapping.put(par1Str, Integer.valueOf(par2));
	}

	/**
	 * Adds a entity mapping with egg info.
	 */
	private static void addMapping(Class par0Class, Function<World, Entity> constructor, String par1Str, int par2, int par3, int par4) {
		addMapping(par0Class, constructor, par1Str, par2);
		entityEggs.put(Integer.valueOf(par2), new EntityEggInfo(par2, par3, par4));
	}

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static Entity createEntityByName(String par0Str, World par1World) {
		Entity var2 = null;

		try {
			Function<World, Entity> var3 = (Function<World, Entity>) stringToClassMapping.get(par0Str);

			if (var3 != null) {
				var2 = var3.apply(par1World);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return var2;
	}

	/**
	 * create a new instance of an entity from NBT store
	 */
	public static Entity createEntityFromNBT(NBTTagCompound par0NBTTagCompound, World par1World) {
		Entity var2 = null;

		if ("Minecart".equals(par0NBTTagCompound.getString("id"))) {
			switch (par0NBTTagCompound.getInteger("Type")) {
			case 0:
				par0NBTTagCompound.setString("id", "MinecartRideable");
				break;

			case 1:
				par0NBTTagCompound.setString("id", "MinecartChest");
				break;

			case 2:
				par0NBTTagCompound.setString("id", "MinecartFurnace");
			}

			par0NBTTagCompound.removeTag("Type");
		}

		try {
			Function<World, Entity> var3 = (Function<World, Entity>) stringToClassMapping.get(par0NBTTagCompound.getString("id"));

			if (var3 != null) {
				var2 = var3.apply(par1World);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 != null) {
			var2.readFromNBT(par0NBTTagCompound);
		} else {
			par1World.getWorldLogAgent().func_98236_b("Skipping Entity with id " + par0NBTTagCompound.getString("id"));
		}

		return var2;
	}

	/**
	 * Create a new instance of an entity in the world by using an entity ID.
	 */
	public static Entity createEntityByID(int par0, World par1World) {
		Entity var2 = null;

		try {
			Function<World, Entity> var3 = (Function<World, Entity>) IDtoClassMapping.get(par0);

			if (var3 != null) {
				var2 = var3.apply(par1World);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 == null) {
			par1World.getWorldLogAgent().func_98236_b("Skipping Entity with id " + par0);
		}

		return var2;
	}

	/**
	 * gets the entityID of a specific entity
	 */
	public static int getEntityID(Entity par0Entity) {
		Class var1 = par0Entity.getClass();
		return classToIDMapping.containsKey(var1) ? ((Integer) classToIDMapping.get(var1)).intValue() : 0;
	}

	/**
	 * Return the class assigned to this entity ID.
	 */
	public static Class getClassFromID(int par0) {
		return (Class) IDtoClassReflectMapping.get(Integer.valueOf(par0));
	}

	/**
	 * Gets the string representation of a specific entity.
	 */
	public static String getEntityString(Entity par0Entity) {
		return (String) classToStringMapping.get(par0Entity.getClass());
	}

	/**
	 * Finds the class using IDtoClassMapping and classToStringMapping
	 */
	public static String getStringFromID(int par0) {
		Class var1 = getClassFromID(par0);
		return var1 != null ? (String) classToStringMapping.get(var1) : null;
	}

	static {
		addMapping(EntityItem.class, (w) -> new EntityItem(w), "Item", 1);
		addMapping(EntityXPOrb.class, (w) -> new EntityXPOrb(w), "XPOrb", 2);
		addMapping(EntityPainting.class, (w) -> new EntityPainting(w), "Painting", 9);
		addMapping(EntityArrow.class, (w) -> new EntityArrow(w), "Arrow", 10);
		addMapping(EntitySnowball.class, (w) -> new EntitySnowball(w), "Snowball", 11);
		addMapping(EntityLargeFireball.class, (w) -> new EntityLargeFireball(w), "Fireball", 12);
		addMapping(EntitySmallFireball.class, (w) -> new EntitySmallFireball(w), "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, (w) -> new EntityEnderPearl(w), "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, (w) -> new EntityEnderEye(w), "EyeOfEnderSignal", 15);
		addMapping(EntityPotion.class, (w) -> new EntityPotion(w), "ThrownPotion", 16);
		addMapping(EntityExpBottle.class, (w) -> new EntityExpBottle(w), "ThrownExpBottle", 17);
		addMapping(EntityItemFrame.class, (w) -> new EntityItemFrame(w), "ItemFrame", 18);
		addMapping(EntityWitherSkull.class, (w) -> new EntityWitherSkull(w), "WitherSkull", 19);
		addMapping(EntityTNTPrimed.class, (w) -> new EntityTNTPrimed(w), "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, (w) -> new EntityFallingSand(w), "FallingSand", 21);
		addMapping(EntityFireworkRocket.class, (w) -> new EntityFireworkRocket(w), "FireworksRocketEntity", 22);
		addMapping(EntityBoat.class, (w) -> new EntityBoat(w), "Boat", 41);
		addMapping(EntityMinecartEmpty.class, (w) -> new EntityMinecartEmpty(w), "MinecartRideable", 42);
		addMapping(EntityMinecartChest.class, (w) -> new EntityMinecartChest(w), "MinecartChest", 43);
		addMapping(EntityMinecartFurnace.class, (w) -> new EntityMinecartFurnace(w), "MinecartFurnace", 44);
		addMapping(EntityMinecartTNT.class, (w) -> new EntityMinecartTNT(w), "MinecartTNT", 45);
		addMapping(EntityMinecartHopper.class, (w) -> new EntityMinecartHopper(w), "MinecartHopper", 46);
		addMapping(EntityMinecartMobSpawner.class, (w) -> new EntityMinecartMobSpawner(w), "MinecartSpawner", 47);
		addMapping(EntityLiving.class, null, "Mob", 48);
		addMapping(EntityMob.class, null, "Monster", 49);
		addMapping(EntityCreeper.class, (w) -> new EntityCreeper(w), "Creeper", 50, 894731, 0);
		addMapping(EntitySkeleton.class, (w) -> new EntitySkeleton(w), "Skeleton", 51, 12698049, 4802889);
		addMapping(EntitySpider.class, (w) -> new EntitySpider(w), "Spider", 52, 3419431, 11013646);
		addMapping(EntityGiantZombie.class, (w) -> new EntityGiantZombie(w), "Giant", 53);
		addMapping(EntityZombie.class, (w) -> new EntityZombie(w), "Zombie", 54, 44975, 7969893);
		addMapping(EntitySlime.class, (w) -> new EntitySlime(w), "Slime", 55, 5349438, 8306542);
		addMapping(EntityGhast.class, (w) -> new EntityGhast(w), "Ghast", 56, 16382457, 12369084);
		addMapping(EntityPigZombie.class, (w) -> new EntityPigZombie(w), "PigZombie", 57, 15373203, 5009705);
		addMapping(EntityEnderman.class, (w) -> new EntityEnderman(w), "Enderman", 58, 1447446, 0);
		addMapping(EntityCaveSpider.class, (w) -> new EntityCaveSpider(w), "CaveSpider", 59, 803406, 11013646);
		addMapping(EntitySilverfish.class, (w) -> new EntitySilverfish(w), "Silverfish", 60, 7237230, 3158064);
		addMapping(EntityBlaze.class, (w) -> new EntityBlaze(w), "Blaze", 61, 16167425, 16775294);
		addMapping(EntityMagmaCube.class, (w) -> new EntityMagmaCube(w), "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, (w) -> new EntityDragon(w), "EnderDragon", 63);
		addMapping(EntityWither.class, (w) -> new EntityWither(w), "WitherBoss", 64);
		addMapping(EntityBat.class, (w) -> new EntityBat(w), "Bat", 65, 4996656, 986895);
		addMapping(EntityWitch.class, (w) -> new EntityWitch(w), "Witch", 66, 3407872, 5349438);
		addMapping(EntityPig.class, (w) -> new EntityPig(w), "Pig", 90, 15771042, 14377823);
		addMapping(EntitySheep.class, (w) -> new EntitySheep(w), "Sheep", 91, 15198183, 16758197);
		addMapping(EntityCow.class, (w) -> new EntityCow(w), "Cow", 92, 4470310, 10592673);
		addMapping(EntityChicken.class, (w) -> new EntityChicken(w), "Chicken", 93, 10592673, 16711680);
		addMapping(EntitySquid.class, (w) -> new EntitySquid(w), "Squid", 94, 2243405, 7375001);
		addMapping(EntityWolf.class, (w) -> new EntityWolf(w), "Wolf", 95, 14144467, 13545366);
		addMapping(EntityMooshroom.class, (w) -> new EntityMooshroom(w), "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, (w) -> new EntitySnowman(w), "SnowMan", 97);
		addMapping(EntityOcelot.class, (w) -> new EntityOcelot(w), "Ozelot", 98, 15720061, 5653556);
		addMapping(EntityIronGolem.class, (w) -> new EntityIronGolem(w), "VillagerGolem", 99);
		addMapping(EntityVillager.class, (w) -> new EntityVillager(w), "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, (w) -> new EntityEnderCrystal(w), "EnderCrystal", 200);
	}
}
