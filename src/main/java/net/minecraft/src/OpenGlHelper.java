package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class OpenGlHelper {
	/**
	 * An OpenGL constant corresponding to GL_TEXTURE0, used when setting data
	 * pertaining to auxiliary OpenGL texture units.
	 */
	public static int defaultTexUnit;

	/**
	 * An OpenGL constant corresponding to GL_TEXTURE1, used when setting data
	 * pertaining to auxiliary OpenGL texture units.
	 */
	public static int lightmapTexUnit;

	/**
	 * Initializes the texture constants to be used when rendering lightmap values
	 */
	public static void initializeTextures() {
		defaultTexUnit = EaglerAdapter.GL_TEXTURE0;
		lightmapTexUnit = EaglerAdapter.GL_TEXTURE1;
	}

	/**
	 * Sets the current lightmap texture to the specified OpenGL constant
	 */
	public static void setActiveTexture(int par0) {
		EaglerAdapter.glActiveTexture(par0);
	}

	/**
	 * Sets the current lightmap texture to the specified OpenGL constant
	 */
	public static void setClientActiveTexture(int par0) {
		EaglerAdapter.glClientActiveTexture(par0);
	}

	/**
	 * Sets the current coordinates of the given lightmap texture
	 */
	public static void setLightmapTextureCoords(int par0, float par1, float par2) {
		EaglerAdapter.glMultiTexCoord2f(par0, par1, par2);
	}
}
