package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class MathHelper {
	/**
	 * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with
	 * steps of 2*PI / 65536.
	 */
	private static float[] SIN_TABLE = new float[65536];

	/**
	 * sin looked up in a table
	 */
	public static final float sin(float par0) {
		return SIN_TABLE[(int) (par0 * 10430.378F) & 65535];
	}

	/**
	 * cos looked up in the sin table with the appropriate offset
	 */
	public static final float cos(float par0) {
		return SIN_TABLE[(int) (par0 * 10430.378F + 16384.0F) & 65535];
	}

	public static final float sqrt_float(float par0) {
		return (float) Math.sqrt((double) par0);
	}

	public static final float sqrt_double(double par0) {
		return (float) Math.sqrt(par0);
	}

	/**
	 * Returns the greatest integer less than or equal to the float argument
	 */
	public static int floor_float(float par0) {
		int var1 = (int) par0;
		return par0 < (float) var1 ? var1 - 1 : var1;
	}

	/**
	 * Returns the greatest integer less than or equal to the double argument
	 */
	public static int floor_double(double par0) {
		int var2 = (int) par0;
		return par0 < (double) var2 ? var2 - 1 : var2;
	}

	/**
	 * Long version of floor_double
	 */
	public static long floor_double_long(double par0) {
		long var2 = (long) par0;
		return par0 < (double) var2 ? var2 - 1L : var2;
	}

	public static float abs(float par0) {
		return par0 >= 0.0F ? par0 : -par0;
	}

	/**
	 * Returns the unsigned value of an int.
	 */
	public static int abs_int(int par0) {
		return par0 >= 0 ? par0 : -par0;
	}

	public static int ceiling_float_int(float par0) {
		int var1 = (int) par0;
		return par0 > (float) var1 ? var1 + 1 : var1;
	}

	public static int ceiling_double_int(double par0) {
		int var2 = (int) par0;
		return par0 > (double) var2 ? var2 + 1 : var2;
	}

	/**
	 * Returns the value of the first parameter, clamped to be within the lower and
	 * upper limits given by the second and third parameters.
	 */
	public static int clamp_int(int par0, int par1, int par2) {
		return par0 < par1 ? par1 : (par0 > par2 ? par2 : par0);
	}

	/**
	 * Maximum of the absolute value of two numbers.
	 */
	public static double abs_max(double par0, double par2) {
		if (par0 < 0.0D) {
			par0 = -par0;
		}

		if (par2 < 0.0D) {
			par2 = -par2;
		}

		return par0 > par2 ? par0 : par2;
	}

	public static int getRandomIntegerInRange(EaglercraftRandom par0Random, int par1, int par2) {
		return par1 >= par2 ? par1 : par0Random.nextInt(par2 - par1 + 1) + par1;
	}

	public static double getRandomDoubleInRange(EaglercraftRandom par0Random, double par1, double par3) {
		return par1 >= par3 ? par1 : par0Random.nextDouble() * (par3 - par1) + par1;
	}

	public static double average(long[] par0ArrayOfLong) {
		long var1 = 0L;
		long[] var3 = par0ArrayOfLong;
		int var4 = par0ArrayOfLong.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			long var6 = var3[var5];
			var1 += var6;
		}

		return (double) var1 / (double) par0ArrayOfLong.length;
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360
	 * check
	 */
	public static float wrapAngleTo180_float(float par0) {
		par0 %= 360.0F;

		if (par0 >= 180.0F) {
			par0 -= 360.0F;
		}

		if (par0 < -180.0F) {
			par0 += 360.0F;
		}

		return par0;
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360
	 * check
	 */
	public static double wrapAngleTo180_double(double par0) {
		par0 %= 360.0D;

		if (par0 >= 180.0D) {
			par0 -= 360.0D;
		}

		if (par0 < -180.0D) {
			par0 += 360.0D;
		}

		return par0;
	}

	/**
	 * parses the string as integer or returns the second parameter if it fails
	 */
	public static int parseIntWithDefault(String par0Str, int par1) {
		int var2 = par1;

		try {
			var2 = Integer.parseInt(par0Str);
		} catch (Throwable var4) {
			;
		}

		return var2;
	}

	/**
	 * parses the string as integer or returns the second parameter if it fails.
	 * this value is capped to par2
	 */
	public static int parseIntWithDefaultAndMax(String par0Str, int par1, int par2) {
		int var3 = par1;

		try {
			var3 = Integer.parseInt(par0Str);
		} catch (Throwable var5) {
			;
		}

		if (var3 < par2) {
			var3 = par2;
		}

		return var3;
	}

	/**
	 * parses the string as double or returns the second parameter if it fails.
	 */
	public static double parseDoubleWithDefault(String par0Str, double par1) {
		double var3 = par1;

		try {
			var3 = Double.parseDouble(par0Str);
		} catch (Throwable var6) {
			;
		}

		return var3;
	}

	public static double func_82713_a(String par0Str, double par1, double par3) {
		double var5 = par1;

		try {
			var5 = Double.parseDouble(par0Str);
		} catch (Throwable var8) {
			;
		}

		if (var5 < par3) {
			var5 = par3;
		}

		return var5;
	}

	static {
		for (int var0 = 0; var0 < 65536; ++var0) {
			SIN_TABLE[var0] = (float) Math.sin((double) var0 * Math.PI * 2.0D / 65536.0D);
		}
	}
}
