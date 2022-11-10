package net.minecraft.src;

import java.io.InputStream;

public interface ITexturePack {
	/**
	 * Delete the OpenGL texture id of the pack's thumbnail image, and close the zip
	 * file in case of TexturePackCustom.
	 */
	void deleteTexturePack(RenderEngine var1);

	/**
	 * Bind the texture id of the pack's thumbnail image, loading it if necessary.
	 */
	void bindThumbnailTexture(RenderEngine var1);

	InputStream func_98137_a(String var1, boolean var2);

	/**
	 * Gives a texture resource as InputStream.
	 */
	InputStream getResourceAsStream(String var1);

	/**
	 * Get the texture pack ID
	 */
	String getTexturePackID();

	/**
	 * Get the file name of the texture pack, or Default if not from a custom
	 * texture pack
	 */
	String getTexturePackFileName();

	/**
	 * Get the first line of the texture pack description (read from the pack.txt
	 * file)
	 */
	String getFirstDescriptionLine();

	/**
	 * Get the second line of the texture pack description (read from the pack.txt
	 * file)
	 */
	String getSecondDescriptionLine();

	boolean func_98138_b(String var1, boolean var2);

	boolean isCompatible();

	byte[] getResourceAsBytes(String par1Str);
}
