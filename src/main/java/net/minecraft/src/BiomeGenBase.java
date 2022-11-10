package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;



public class BiomeGenBase {
	/** An array of all the biomes, indexed by biome id. */
	public static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
	public static final BiomeGenBase ocean = (new BiomeGenBase(0)).setColor(112).setBiomeName("Ocean").setMinMaxHeight(-1.0F, 0.4F);
	public static final BiomeGenBase plains = (new BiomeGenBase(1)).setColor(9286496).setBiomeName("Plains").setTemperatureRainfall(0.8F, 0.4F);
	public static final BiomeGenBase desert = (new BiomeGenBase(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.1F, 0.2F);
	public static final BiomeGenBase extremeHills = (new BiomeGenBase(3)).setColor(6316128).setBiomeName("Extreme Hills").setMinMaxHeight(0.3F, 1.5F).setTemperatureRainfall(0.2F, 0.3F);
	public static final BiomeGenBase forest = (new BiomeGenBase(4)).setColor(353825).setBiomeName("Forest").func_76733_a(5159473).setTemperatureRainfall(0.7F, 0.8F);
	public static final BiomeGenBase taiga = (new BiomeGenBase(5)).setColor(747097).setBiomeName("Taiga").func_76733_a(5159473).setEnableSnow().setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.1F, 0.4F);
	public static final BiomeGenBase swampland = (new BiomeGenBase(6)).setColor(522674).setBiomeName("Swampland").func_76733_a(9154376).setMinMaxHeight(-0.2F, 0.1F).setTemperatureRainfall(0.8F, 0.9F);
	public static final BiomeGenBase river = (new BiomeGenBase(7)).setColor(255).setBiomeName("River").setMinMaxHeight(-0.5F, 0.0F);
	public static final BiomeGenBase hell = (new BiomeGenBase(8)).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);

	/** Is the biome used for sky world. */
	public static final BiomeGenBase sky = (new BiomeGenBase(9)).setColor(8421631).setBiomeName("Sky").setDisableRain();
	public static final BiomeGenBase frozenOcean = (new BiomeGenBase(10)).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setMinMaxHeight(-1.0F, 0.5F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase frozenRiver = (new BiomeGenBase(11)).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setMinMaxHeight(-0.5F, 0.0F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase icePlains = (new BiomeGenBase(12)).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase iceMountains = (new BiomeGenBase(13)).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setMinMaxHeight(0.3F, 1.3F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase mushroomIsland = (new BiomeGenBase(14)).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(0.2F, 1.0F);
	public static final BiomeGenBase mushroomIslandShore = (new BiomeGenBase(15)).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(-1.0F, 0.1F);

	/** Beach biome. */
	public static final BiomeGenBase beach = (new BiomeGenBase(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setMinMaxHeight(0.0F, 0.1F);

	/** Desert Hills biome. */
	public static final BiomeGenBase desertHills = (new BiomeGenBase(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.3F, 0.8F);

	/** Forest Hills biome. */
	public static final BiomeGenBase forestHills = (new BiomeGenBase(18)).setColor(2250012).setBiomeName("ForestHills").func_76733_a(5159473).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.3F, 0.7F);

	/** Taiga Hills biome. */
	public static final BiomeGenBase taigaHills = (new BiomeGenBase(19)).setColor(1456435).setBiomeName("TaigaHills").setEnableSnow().func_76733_a(5159473).setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.3F, 0.8F);

	/** Extreme Hills Edge biome. */
	public static final BiomeGenBase extremeHillsEdge = (new BiomeGenBase(20)).setColor(7501978).setBiomeName("Extreme Hills Edge").setMinMaxHeight(0.2F, 0.8F).setTemperatureRainfall(0.2F, 0.3F);

	/** Jungle biome identifier */
	public static final BiomeGenBase jungle = (new BiomeGenBase(21)).setColor(5470985).setBiomeName("Jungle").func_76733_a(5470985).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(0.2F, 0.4F);
	public static final BiomeGenBase jungleHills = (new BiomeGenBase(22)).setColor(2900485).setBiomeName("JungleHills").func_76733_a(5470985).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(1.8F, 0.5F);
	public String biomeName;
	public int color;

	/** The block expected to be on the top of this biome */
	public byte topBlock;

	/** The block to fill spots in when not on the top */
	public byte fillerBlock;
	public int field_76754_C;

	/** The minimum height of this biome. Default 0.1. */
	public float minHeight;

	/** The maximum height of this biome. Default 0.3. */
	public float maxHeight;

	/** The temperature of this biome. */
	public float temperature;

	/** The rainfall in this biome. */
	public float rainfall;

	/** Color tint applied to water depending on biome */
	public int waterColorMultiplier;

	/**
	 * Holds the classes of IMobs (hostile mobs) that can be spawned in the biome.
	 */
	protected List spawnableMonsterList;

	/**
	 * Holds the classes of any creature that can be spawned in the biome as
	 * friendly creature.
	 */
	protected List spawnableCreatureList;

	/**
	 * Holds the classes of any aquatic creature that can be spawned in the water of
	 * the biome.
	 */
	protected List spawnableWaterCreatureList;
	protected List spawnableCaveCreatureList;

	/** Set to true if snow is enabled for this biome. */
	private boolean enableSnow;

	/**
	 * Is true (default) if the biome support rain (desert and nether can't have
	 * rain)
	 */
	private boolean enableRain;

	/** The id number to this biome, and its index in the biomeList array. */
	public final int biomeID;

	protected BiomeGenBase(int par1) {
		this.topBlock = (byte) Block.grass.blockID;
		this.fillerBlock = (byte) Block.dirt.blockID;
		this.field_76754_C = 5169201;
		this.minHeight = 0.1F;
		this.maxHeight = 0.3F;
		this.temperature = 0.5F;
		this.rainfall = 0.5F;
		this.waterColorMultiplier = 16777215;
		this.spawnableMonsterList = new ArrayList();
		this.spawnableCreatureList = new ArrayList();
		this.spawnableWaterCreatureList = new ArrayList();
		this.spawnableCaveCreatureList = new ArrayList();
		this.enableRain = true;
		this.biomeID = par1;
		biomeList[par1] = this;
	}

	/**
	 * Sets the temperature and rainfall of this biome.
	 */
	private BiomeGenBase setTemperatureRainfall(float par1, float par2) {
		if (par1 > 0.1F && par1 < 0.2F) {
			throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		} else {
			this.temperature = par1;
			this.rainfall = par2;
			return this;
		}
	}

	/**
	 * Sets the minimum and maximum height of this biome. Seems to go from -2.0 to
	 * 2.0.
	 */
	private BiomeGenBase setMinMaxHeight(float par1, float par2) {
		this.minHeight = par1;
		this.maxHeight = par2;
		return this;
	}

	/**
	 * Disable the rain for the biome.
	 */
	private BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	/**
	 * sets enableSnow to true during biome initialization. returns BiomeGenBase.
	 */
	protected BiomeGenBase setEnableSnow() {
		this.enableSnow = true;
		return this;
	}

	protected BiomeGenBase setBiomeName(String par1Str) {
		this.biomeName = par1Str;
		return this;
	}

	protected BiomeGenBase func_76733_a(int par1) {
		this.field_76754_C = par1;
		return this;
	}

	protected BiomeGenBase setColor(int par1) {
		this.color = par1;
		return this;
	}

	/**
	 * takes temperature, returns color
	 */
	public int getSkyColorByTemp(float par1) {
		par1 /= 3.0F;

		if (par1 < -1.0F) {
			par1 = -1.0F;
		}

		if (par1 > 1.0F) {
			par1 = 1.0F;
		}

		return HSBtoRGB(0.62222224F - par1 * 0.05F, 0.5F + par1 * 0.1F, 1.0F);
	}

	public static int HSBtoRGB(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
			case 0:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (t * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int) (q * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (q * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int) (t * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (q * 255.0f + 0.5f);
				break;
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
	}

	/**
	 * Returns the correspondent list of the EnumCreatureType informed.
	 */
	public List getSpawnableList(EnumCreatureType par1EnumCreatureType) {
		return par1EnumCreatureType == EnumCreatureType.monster ? this.spawnableMonsterList
				: (par1EnumCreatureType == EnumCreatureType.creature ? this.spawnableCreatureList
						: (par1EnumCreatureType == EnumCreatureType.waterCreature ? this.spawnableWaterCreatureList : (par1EnumCreatureType == EnumCreatureType.ambient ? this.spawnableCaveCreatureList : null)));
	}

	/**
	 * Returns true if the biome have snowfall instead a normal rain.
	 */
	public boolean getEnableSnow() {
		return this.enableSnow;
	}

	/**
	 * Return true if the biome supports lightning bolt spawn, either by have the
	 * bolts enabled and have rain enabled.
	 */
	public boolean canSpawnLightningBolt() {
		return this.enableSnow ? false : this.enableRain;
	}

	/**
	 * Checks to see if the rainfall level of the biome is extremely high
	 */
	public boolean isHighHumidity() {
		return this.rainfall > 0.85F;
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	public float getSpawningChance() {
		return 0.1F;
	}

	/**
	 * Gets an integer representation of this biome's rainfall
	 */
	public final int getIntRainfall() {
		return (int) (this.rainfall * 65536.0F);
	}

	/**
	 * Gets an integer representation of this biome's temperature
	 */
	public final int getIntTemperature() {
		return (int) (this.temperature * 65536.0F);
	}

	/**
	 * Gets a floating point representation of this biome's rainfall
	 */
	public final float getFloatRainfall() {
		return this.rainfall;
	}

	/**
	 * Gets a floating point representation of this biome's temperature
	 */
	public final float getFloatTemperature() {
		return this.temperature;
	}

	/**
	 * Provides the basic grass color based on the biome temperature and rainfall
	 */
	public int getBiomeGrassColor() {
		if(biomeID == 6) return 6975545;
		double var1 = (double) MathHelper.clamp_float(this.getFloatTemperature(), 0.0F, 1.0F);
		double var3 = (double) MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
		return ColorizerGrass.getGrassColor(var1, var3);
	}

	/**
	 * Provides the basic foliage color based on the biome temperature and rainfall
	 */
	public int getBiomeFoliageColor() {
		if(biomeID == 6) return 6975545;
		double var1 = (double) MathHelper.clamp_float(this.getFloatTemperature(), 0.0F, 1.0F);
		double var3 = (double) MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}
}
