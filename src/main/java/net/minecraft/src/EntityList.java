package net.minecraft.src;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityList {
	/** Provides a mapping between entity classes and a string */
	private static Map stringToClassMapping = new HashMap();
	private static Map stringToConstructorMapping = new HashMap();

	/** Provides a mapping between a string and an entity classes */
	private static Map classToStringMapping = new HashMap();

	/** provides a mapping between an entityID and an Entity Class */
	private static Map IDtoClassMapping = new HashMap();
	private static Map IDtoConstructorMapping = new HashMap();

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
	private static void addMapping(Class par0Class, Function<World,Entity> construst, String par1Str, int par2) {
		stringToClassMapping.put(par1Str, par0Class);
		stringToConstructorMapping.put(par1Str, construst);
		classToStringMapping.put(par0Class, par1Str);
		IDtoClassMapping.put(Integer.valueOf(par2), par0Class);
		IDtoConstructorMapping.put(Integer.valueOf(par2), construst);
		classToIDMapping.put(par0Class, Integer.valueOf(par2));
		stringToIDMapping.put(par1Str, Integer.valueOf(par2));
	}

	/**
	 * Adds a entity mapping with egg info.
	 */
	private static void addMapping(Class par0Class, Function<World,Entity> construst, String par1Str, int par2, int par3, int par4) {
		addMapping(par0Class, construst, par1Str, par2);
		entityEggs.put(Integer.valueOf(par2), new EntityEggInfo(par2, par3, par4));
	}

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static Entity createEntityByName(String par0Str, World par1World) {
		/*
		Entity var2 = null;
		Class var3 = (Class) stringToClassMapping.get(par0Str);

		if (var3 != null) {
			try {
				var2 = (Entity) var3.getConstructor(new Class[] { World.class }).newInstance(new Object[] { par1World });
			} catch (Exception var4) {
				try {
					var2 = (Entity) var3.getConstructor(new Class[0]).newInstance(new Object[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(var2 == null) {
			System.err.println("The entity '" + par0Str + "' isn't registered");
		}

		return var2;
		*/
		Function<World,Entity> ff = (Function<World,Entity>)stringToConstructorMapping.get(par0Str);
		if(ff == null) {
			System.err.println("The entity '" + par0Str + "' isn't registered");
			return null;
		}else {
			return ff.apply(par1World);
		}
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
			Function<World,Entity> ff = (Function<World,Entity>)stringToConstructorMapping.get(par0NBTTagCompound.getString("id"));
			if (ff != null) {
				var2 = ff.apply(par1World);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 != null) {
			var2.readFromNBT(par0NBTTagCompound);
		} else {
			System.err.println("Skipping Entity with id " + par0NBTTagCompound.getString("id"));
		}

		return var2;
	}

	/**
	 * Create a new instance of an entity in the world by using an entity ID.
	 */
	public static Entity createEntityByID(int par0, World par1World) {
		Entity var2 = null;

		try {
			Class var3 = getClassFromID(par0);

			if (var3 != null) {
				var2 = (Entity) var3.newInstance();
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if (var2 == null) {
			System.err.println("Skipping Entity with id " + par0);
		}else {
			var2.setWorld(par1World);
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
		return (Class) IDtoClassMapping.get(Integer.valueOf(par0));
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
		addMapping(EntityItem.class, (w) -> new EntityItem().setWorld(w), "Item", 1);
		addMapping(EntityXPOrb.class, (w) -> new EntityXPOrb().setWorld(w), "XPOrb", 2);
		addMapping(EntityPainting.class, (w) -> new EntityPainting(w), "Painting", 9);
		addMapping(EntityArrow.class, (w) -> new EntityArrow().setWorld(w), "Arrow", 10);
		addMapping(EntitySnowball.class, (w) -> new EntitySnowball().setWorld(w), "Snowball", 11);
		addMapping(EntityLargeFireball.class, (w) -> new EntityLargeFireball().setWorld(w), "Fireball", 12);
		addMapping(EntitySmallFireball.class, (w) -> new EntitySmallFireball().setWorld(w), "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, (w) -> new EntityEnderPearl().setWorld(w), "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, (w) -> new EntityEnderEye().setWorld(w), "EyeOfEnderSignal", 15);
		addMapping(EntityPotion.class, (w) -> new EntityPotion().setWorld(w), "ThrownPotion", 16);
		addMapping(EntityExpBottle.class, (w) -> new EntityExpBottle().setWorld(w), "ThrownExpBottle", 17);
		addMapping(EntityItemFrame.class, (w) -> new EntityItemFrame().setWorld(w), "ItemFrame", 18);
		addMapping(EntityWitherSkull.class, (w) -> new EntityWitherSkull().setWorld(w), "WitherSkull", 19);
		addMapping(EntityTNTPrimed.class, (w) -> new EntityTNTPrimed().setWorld(w), "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, (w) -> new EntityFallingSand().setWorld(w), "FallingSand", 21);
		addMapping(EntityFireworkRocket.class, (w) -> new EntityFireworkRocket().setWorld(w), "FireworksRocketEntity", 22);
		addMapping(EntityBoat.class, (w) -> new EntityBoat().setWorld(w), "Boat", 41);
		addMapping(EntityMinecartEmpty.class, (w) -> new EntityMinecartEmpty(w), "MinecartRideable", 42);
		addMapping(EntityMinecartChest.class, (w) -> new EntityMinecartChest(w), "MinecartChest", 43);
		addMapping(EntityMinecartFurnace.class, (w) -> new EntityMinecartFurnace(w), "MinecartFurnace", 44);
		addMapping(EntityMinecartTNT.class, (w) -> new EntityMinecartTNT(w), "MinecartTNT", 45);
		addMapping(EntityMinecartHopper.class, (w) -> new EntityMinecartHopper(w), "MinecartHopper", 46);
		addMapping(EntityMinecartMobSpawner.class, (w) -> new EntityMinecartMobSpawner(w), "MinecartSpawner", 47);
		//addMapping(EntityLiving.class, "Mob", 48);
		//addMapping(EntityMob.class, "Monster", 49);
		addMapping(EntityCreeper.class, (w) -> new EntityCreeper().setWorld(w), "Creeper", 50, 894731, 0);
		addMapping(EntitySkeleton.class, (w) -> new EntitySkeleton().setWorld(w), "Skeleton", 51, 12698049, 4802889);
		addMapping(EntitySpider.class, (w) -> new EntitySpider().setWorld(w), "Spider", 52, 3419431, 11013646);
		addMapping(EntityZombie.class, (w) -> new EntityZombie().setWorld(w), "Zombie", 54, 44975, 7969893);
		addMapping(EntitySlime.class, (w) -> new EntitySlime().setWorld(w), "Slime", 55, 5349438, 8306542);
		addMapping(EntityGhast.class, (w) -> new EntityGhast().setWorld(w), "Ghast", 56, 16382457, 12369084);
		addMapping(EntityPigZombie.class, (w) -> new EntityPigZombie().setWorld(w), "PigZombie", 57, 15373203, 5009705);
		addMapping(EntityEnderman.class, (w) -> new EntityEnderman().setWorld(w), "Enderman", 58, 1447446, 0);
		addMapping(EntityCaveSpider.class, (w) -> new EntityCaveSpider().setWorld(w), "CaveSpider", 59, 803406, 11013646);
		addMapping(EntitySilverfish.class, (w) -> new EntitySilverfish().setWorld(w), "Silverfish", 60, 7237230, 3158064);
		addMapping(EntityBlaze.class, (w) -> new EntityBlaze().setWorld(w), "Blaze", 61, 16167425, 16775294);
		addMapping(EntityMagmaCube.class, (w) -> new EntityMagmaCube().setWorld(w), "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, (w) -> new EntityDragon().setWorld(w), "EnderDragon", 63);
		addMapping(EntityWither.class, (w) -> new EntityWither().setWorld(w), "WitherBoss", 64);
		addMapping(EntityBat.class, (w) -> new EntityBat().setWorld(w), "Bat", 65, 4996656, 986895);
		addMapping(EntityWitch.class, (w) -> new EntityWitch().setWorld(w), "Witch", 66, 3407872, 5349438);
		addMapping(EntityPig.class, (w) -> new EntityPig().setWorld(w), "Pig", 90, 15771042, 14377823);
		addMapping(EntitySheep.class, (w) -> new EntitySheep().setWorld(w), "Sheep", 91, 15198183, 16758197);
		addMapping(EntityCow.class, (w) -> new EntityCow().setWorld(w), "Cow", 92, 4470310, 10592673);
		addMapping(EntityChicken.class, (w) -> new EntityChicken().setWorld(w), "Chicken", 93, 10592673, 16711680);
		addMapping(EntitySquid.class, (w) -> new EntitySquid().setWorld(w), "Squid", 94, 2243405, 7375001);
		addMapping(EntityWolf.class, (w) -> new EntityWolf().setWorld(w), "Wolf", 95, 14144467, 13545366);
		addMapping(EntityMooshroom.class, (w) -> new EntityMooshroom().setWorld(w), "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, (w) -> new EntitySnowman().setWorld(w), "SnowMan", 97);
		addMapping(EntityOcelot.class, (w) -> new EntityOcelot().setWorld(w), "Ozelot", 98, 15720061, 5653556);
		addMapping(EntityIronGolem.class, (w) -> new EntityIronGolem().setWorld(w), "VillagerGolem", 99);
		addMapping(EntityVillager.class, (w) -> new EntityVillager().setWorld(w), "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, (w) -> new EntityEnderCrystal().setWorld(w), "EnderCrystal", 200);
	}
}
