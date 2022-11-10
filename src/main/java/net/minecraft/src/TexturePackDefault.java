package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class TexturePackDefault extends TexturePackImplementation {
	public TexturePackDefault() {
		super("default", "Default", (ITexturePack) null);
	}

	/**
	 * Load texture pack description from /pack.txt file in the texture pack
	 */
	protected void loadDescription() {
		this.firstDescriptionLine = "The default look of Minecraft";
	}

	public boolean func_98140_c(String par1Str) {
		return EaglerAdapter.loadResource(par1Str) != null;
	}

	public boolean isCompatible() {
		return true;
	}

	protected InputStream func_98139_b(String par1Str) throws IOException {
		return EaglerAdapter.loadResource(par1Str);
	}

	@Override
	public byte[] getResourceAsBytes(String par1Str) {
		return EaglerAdapter.loadResourceBytes(par1Str);
	}
}
