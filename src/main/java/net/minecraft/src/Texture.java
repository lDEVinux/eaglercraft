package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;

public class Texture {
	private int glTextureId;
	private int textureId;
	private int textureType;

	/** Width of this texture in pixels. */
	private final int width;

	/** Height of this texture in pixels. */
	private final int height;
	private final int textureDepth;
	private final int textureFormat;
	private final int textureTarget;
	private final int textureMinFilter;
	private final int textureMagFilter;
	private final int textureWrap;
	private final boolean mipmapActive;
	private final String textureName;
	private Rect2i textureRect;
	private boolean transferred;

	/**
	 * Uninitialized boolean. If true, the texture is re-uploaded every time it's
	 * modified. If false, every tick after it's been modified at least once in that
	 * tick.
	 */
	private boolean autoCreate;

	/**
	 * False if the texture has been modified since it was last uploaded to the GPU.
	 */
	private boolean textureNotModified;
	private ByteBuffer textureData;

	private Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9) {
		this.textureName = par1Str;
		this.textureType = par2;
		this.width = par3;
		this.height = par4;
		this.textureDepth = par5;
		this.textureFormat = par7;
		this.textureMinFilter = par8;
		this.textureMagFilter = par9;
		this.textureWrap = par6;
		this.textureRect = new Rect2i(0, 0, par3, par4);
		this.textureTarget = EaglerAdapter.GL_TEXTURE_2D;
		this.mipmapActive = false; //par8 != 9728 && par8 != 9729 || par9 != 9728 && par9 != 9729;

		if (par2 != 2) {
			this.glTextureId = EaglerAdapter.glGenTextures();
			EaglerAdapter.glBindTexture(this.textureTarget, this.glTextureId);
			EaglerAdapter.glTexParameteri(this.textureTarget, EaglerAdapter.GL_TEXTURE_MIN_FILTER, par8);
			EaglerAdapter.glTexParameteri(this.textureTarget, EaglerAdapter.GL_TEXTURE_MAG_FILTER, par9);
			EaglerAdapter.glTexParameteri(this.textureTarget, EaglerAdapter.GL_TEXTURE_WRAP_S, par6);
			EaglerAdapter.glTexParameteri(this.textureTarget, EaglerAdapter.GL_TEXTURE_WRAP_T, par6);
		} else {
			this.glTextureId = -1;
		}

		this.textureId = TextureManager.instance().getNextTextureId();
	}

	public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, EaglerImage par9BufferedImage) {
		this(par1Str, par2, par3, par4, 1, par5, par6, par7, par8, par9BufferedImage);
	}

	public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9, EaglerImage par10BufferedImage) {
		this(par1Str, par2, par3, par4, par5, par6, par7, par8, par9);

		if (par10BufferedImage == null) {
			if (par3 != -1 && par4 != -1) {
				byte[] var11 = new byte[par3 * par4 * par5 * 4];

				for (int var12 = 0; var12 < var11.length; ++var12) {
					var11[var12] = 0;
				}

				this.textureData = GLAllocation.createDirectByteBuffer(var11.length);
				this.textureData.clear();
				this.textureData.put(var11);
				this.textureData.position(0).limit(var11.length);

				if (this.autoCreate) {
					this.uploadTexture();
				} else {
					this.textureNotModified = false;
				}
			} else {
				this.transferred = false;
			}
		} else {
			this.transferred = true;
			this.transferFromImage(par10BufferedImage);

			if (par2 != 2) {
				this.uploadTexture();
				this.autoCreate = false;
			}
		}
	}

	public final Rect2i getTextureRect() {
		return this.textureRect;
	}

	public void fillRect(Rect2i par1Rect2i, int par2) {
		if (this.textureTarget != 32879) {
			Rect2i var3 = new Rect2i(0, 0, this.width, this.height);
			var3.intersection(par1Rect2i);
			this.textureData.position(0);

			for (int var4 = var3.getRectY(); var4 < var3.getRectY() + var3.getRectHeight(); ++var4) {
				int var5 = var4 * this.width * 4;

				for (int var6 = var3.getRectX(); var6 < var3.getRectX() + var3.getRectWidth(); ++var6) {
					this.textureData.put(var5 + var6 * 4 + 0, (byte) (par2 >> 24 & 255));
					this.textureData.put(var5 + var6 * 4 + 1, (byte) (par2 >> 16 & 255));
					this.textureData.put(var5 + var6 * 4 + 2, (byte) (par2 >> 8 & 255));
					this.textureData.put(var5 + var6 * 4 + 3, (byte) (par2 >> 0 & 255));
				}
			}

			if (this.autoCreate) {
				this.uploadTexture();
			} else {
				this.textureNotModified = false;
			}
		}
	}

	public void writeImage(String par1Str) {
		/*
		BufferedImage var2 = new BufferedImage(this.width, this.height, 2);
		ByteBuffer var3 = this.getTextureData();
		byte[] var4 = new byte[this.width * this.height * 4];
		var3.position(0);
		var3.get(var4);

		for (int var5 = 0; var5 < this.width; ++var5) {
			for (int var6 = 0; var6 < this.height; ++var6) {
				int var7 = var6 * this.width * 4 + var5 * 4;
				byte var8 = 0;
				int var10 = var8 | (var4[var7 + 2] & 255) << 0;
				var10 |= (var4[var7 + 1] & 255) << 8;
				var10 |= (var4[var7 + 0] & 255) << 16;
				var10 |= (var4[var7 + 3] & 255) << 24;
				var2.setRGB(var5, var6, var10);
			}
		}

		this.textureData.position(this.width * this.height * 4);

		try {
			ImageIO.write(var2, "png", new File(Minecraft.getMinecraftDir(), par1Str));
		} catch (IOException var9) {
			var9.printStackTrace();
		}
		*/
	}

	public void copyFrom(int par1, int par2, Texture par3Texture, boolean par4) {
		if (this.textureTarget != 32879) {
			EaglerAdapter.glBindTexture(this.textureTarget, this.glTextureId);
			ByteBuffer var5 = par3Texture.getTextureData();
			this.textureData.position(0);
			var5.position(0);

			for (int var6 = 0; var6 < par3Texture.getHeight(); ++var6) {
				int var7 = par2 + var6;
				int var8 = var6 * par3Texture.getWidth() * 4;
				int var9 = var7 * this.width * 4;

				if (par4) {
					var7 = par2 + (par3Texture.getHeight() - var6);
				}

				for (int var10 = 0; var10 < par3Texture.getWidth(); ++var10) {
					int var11 = var9 + (var10 + par1) * 4;
					int var12 = var8 + var10 * 4;

					if (par4) {
						var11 = par1 + var10 * this.width * 4 + var7 * 4;
					}

					this.textureData.put(var11 + 0, var5.get(var12 + 0));
					this.textureData.put(var11 + 1, var5.get(var12 + 1));
					this.textureData.put(var11 + 2, var5.get(var12 + 2));
					this.textureData.put(var11 + 3, var5.get(var12 + 3));
				}
			}

			this.textureData.position(this.width * this.height * 4);

			if (this.autoCreate) {
				this.uploadTexture();
			} else {
				this.textureNotModified = false;
			}
		}
	}

	public void func_104062_b(int par1, int par2, Texture par3Texture) {
		EaglerAdapter.glBindTexture(this.textureTarget, this.glTextureId);
		EaglerAdapter.glTexSubImage2D(this.textureTarget, 0, par1, par2, par3Texture.getWidth(), par3Texture.getHeight(), 0, EaglerAdapter.GL_UNSIGNED_BYTE, (ByteBuffer) par3Texture.getTextureData().position(0));
		this.textureNotModified = true;
	}

	public void transferFromImage(EaglerImage par1BufferedImage) {
		//if (this.textureTarget != 32879) {
			int var2 = par1BufferedImage.w;
			int var3 = par1BufferedImage.h;

			if (var2 <= this.width && var3 <= this.height) {
				int[] var4 = new int[] { 3, 0, 1, 2 };
				int[] var5 = new int[] { 3, 2, 1, 0 };
				int[] var6 = var5;
				byte[] var9 = new byte[this.width * this.height * 4];

				for (int var10 = 0; var10 < this.height; ++var10) {
					for (int var11 = 0; var11 < this.width; ++var11) {
						int var12 = var10 * this.width + var11;
						int var13 = var12 * 4;
						var9[var13 + var6[0]] = (byte) (par1BufferedImage.data[var12] >> 24 & 255);
						var9[var13 + var6[1]] = (byte) (par1BufferedImage.data[var12] >> 16 & 255);
						var9[var13 + var6[2]] = (byte) (par1BufferedImage.data[var12] >> 8 & 255);
						var9[var13 + var6[3]] = (byte) (par1BufferedImage.data[var12] >> 0 & 255);
					}
				}

				this.textureData = GLAllocation.createDirectByteBuffer(var9.length).order(ByteOrder.nativeOrder());
				this.textureData.clear();
				this.textureData.put(var9);
				this.textureData.limit(var9.length);

				if (this.autoCreate) {
					this.uploadTexture();
				} else {
					this.textureNotModified = false;
				}
			} else {
				System.err.println("transferFromImage called with a BufferedImage with dimensions (" + var2 + ", " + var3 + ") larger than the Texture dimensions (" + this.width + ", " + this.height + "). Ignoring.");
			}
		//}
	}

	public int getTextureId() {
		return this.textureId;
	}

	public int getGlTextureId() {
		return this.glTextureId;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getTextureName() {
		return this.textureName;
	}

	public void bindTexture(int par1) {
		if (this.textureDepth == 1) {
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		} else {
			//EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_3D);
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit + par1);
		EaglerAdapter.glBindTexture(this.textureTarget, this.glTextureId);

		if (!this.textureNotModified) {
			this.uploadTexture();
		}
	}

	public void uploadTexture() {
		this.textureData.flip();

		if (this.height != 1 && this.textureDepth != 1) {
			//EaglerAdapter.glTexImage3D(this.textureTarget, 0, this.textureFormat, this.width, this.height, this.textureDepth, 0, this.textureFormat, EaglerAdapter.GL_UNSIGNED_BYTE, this.textureData);
		} else {
			EaglerAdapter.glTexImage2D(this.textureTarget, 0, this.textureFormat, this.width, this.height, 0, this.textureFormat, EaglerAdapter.GL_UNSIGNED_BYTE, this.textureData);
		}

		this.textureNotModified = true;
	}

	public ByteBuffer getTextureData() {
		return this.textureData;
	}
}
