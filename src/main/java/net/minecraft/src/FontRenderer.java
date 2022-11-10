package net.minecraft.src;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;

public class FontRenderer {
	/** Array of width of all the characters in default.png */
	private int[] charWidth = new int[256];

	/** the height in pixels of default text */
	public int FONT_HEIGHT = 9;
	public EaglercraftRandom fontRandom = new EaglercraftRandom();

	/**
	 * Array of the start/end column (in upper/lower nibble) for every glyph in the
	 * /font directory.
	 */
	private byte[] glyphWidth = new byte[65536];

	/**
	 * Array of RGB triplets defining the 16 standard chat colors followed by 16
	 * darker version of the same colors for drop shadows.
	 */
	private int[] colorCode = new int[32];
	private final TextureLocation fontTexture;
	private final String fontTextureName;

	/** The RenderEngine used to load and setup glyph textures. */
	private final RenderEngine renderEngine;

	/** Current X coordinate at which to draw the next character. */
	private float posX;

	/** Current Y coordinate at which to draw the next character. */
	private float posY;

	/**
	 * If true, strings should be rendered with Unicode fonts instead of the
	 * default.png font
	 */
	private boolean unicodeFlag;

	/**
	 * If true, the Unicode Bidirectional Algorithm should be run before rendering
	 * any string.
	 */
	private boolean bidiFlag;

	/** Used to specify new red value for the current color. */
	private float red;

	/** Used to specify new blue value for the current color. */
	private float blue;

	/** Used to specify new green value for the current color. */
	private float green;

	/** Used to speify new alpha value for the current color. */
	private float alpha;

	/** Text color of the currently rendering string. */
	private int textColor;

	/** Set if the "k" style (random) is active in currently rendering string */
	private boolean randomStyle = false;

	/** Set if the "l" style (bold) is active in currently rendering string */
	private boolean boldStyle = false;

	/** Set if the "o" style (italic) is active in currently rendering string */
	private boolean italicStyle = false;

	/**
	 * Set if the "n" style (underlined) is active in currently rendering string
	 */
	private boolean underlineStyle = false;

	/**
	 * Set if the "m" style (strikethrough) is active in currently rendering string
	 */
	private boolean strikethroughStyle = false;

	public FontRenderer(GameSettings par1GameSettings, String par2Str, RenderEngine par3RenderEngine, boolean par4) {
		this.fontTexture = new TextureLocation(par2Str);
		this.fontTextureName = par2Str;
		this.renderEngine = par3RenderEngine;
		this.unicodeFlag = par4;
		this.readFontData();
		fontTexture.bindTexture();

		for (int var5 = 0; var5 < 32; ++var5) {
			int var6 = (var5 >> 3 & 1) * 85;
			int var7 = (var5 >> 2 & 1) * 170 + var6;
			int var8 = (var5 >> 1 & 1) * 170 + var6;
			int var9 = (var5 >> 0 & 1) * 170 + var6;

			if (var5 == 6) {
				var7 += 85;
			}

			if (par1GameSettings.anaglyph) {
				int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
				int var11 = (var7 * 30 + var8 * 70) / 100;
				int var12 = (var7 * 30 + var9 * 70) / 100;
				var7 = var10;
				var8 = var11;
				var9 = var12;
			}

			if (var5 >= 16) {
				var7 /= 4;
				var8 /= 4;
				var9 /= 4;
			}

			this.colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
		}
	}

	public void readFontData() {
		this.readGlyphSizes();
		this.readFontTexture(this.fontTextureName);
	}

	private void readFontTexture(String par1Str) {
		//EaglerImage e = EaglerImage.loadImage(EaglerAdapter.loadResourceBytes(par1Str));
		EaglerImage e = EaglerAdapter.loadPNG(EaglerAdapter.loadResourceBytes(par1Str));
		int[] var5 = e.data;
		int var3 = e.w;
		int var4 = e.h;
		int var6 = 0;

		while (var6 < 256) {
			int var7 = var6 % 16;
			int var8 = var6 / 16;
			int var9 = 7;

			while (true) {
				if (var9 >= 0) {
					int var10 = var7 * 8 + var9;
					boolean var11 = true;

					for (int var12 = 0; var12 < 8 && var11; ++var12) {
						int var13 = (var8 * 8 + var12) * var3;
						int var14 = var5[var10 + var13] & 255;

						if (var14 > 0) {
							var11 = false;
						}
					}

					if (var11) {
						--var9;
						continue;
					}
				}

				if (var6 == 32) {
					var9 = 2;
				}

				this.charWidth[var6] = var9 + 2;
				++var6;
				break;
			}
		}
	}

	private void readGlyphSizes() {
		this.glyphWidth = EaglerAdapter.loadResourceBytes("/font/glyph_sizes.bin");
	}

	/**
	 * Pick how to render a single character and return the width used.
	 */
	private float renderCharAtPos(int par1, char par2, boolean par3) {
		return par2 == 32 ? 4.0F : (par1 > 0 && !this.unicodeFlag ? this.renderDefaultChar(par1 + 32, par3) : this.renderUnicodeChar(par2, par3));
	}

	/**
	 * Render a single character with the default.png font at current (posX,posY)
	 * location...
	 */
	private float renderDefaultChar(int par1, boolean par2) {
		float var3 = (float) (par1 % 16 * 8);
		float var4 = (float) (par1 / 16 * 8);
		float var5 = par2 ? 1.0F : 0.0F;
		float var6 = (float) this.charWidth[par1] - 0.02F;
		Tessellator t = Tessellator.instance;
		t.addVertexWithUV(this.posX + 0.01F + var5, this.posY + 0.01F, 0.0F, (var3 + 0.02F) / 128.0F, (var4 + 0.02F) / 128.0F);
		t.addVertexWithUV(this.posX + 0.01F - var5, this.posY + 7.99F, 0.0F, (var3 + 0.02F) / 128.0F, (var4 + 7.96F) / 128.0F);
		t.addVertexWithUV(this.posX + var6 - var5, this.posY + 7.99F, 0.0F, (var3 + var6) / 128.0F, (var4 + 7.96F) / 128.0F);
		t.addVertexWithUV(this.posX + var6 + var5, this.posY + 0.01F, 0.0F, (var3 + var6) / 128.0F, (var4 + 0.02F) / 128.0F);
		return (float) this.charWidth[par1];
	}

	/**
	 * Load one of the /font/glyph_XX.png into a new GL texture and store the
	 * texture ID in glyphTextureName array.
	 */
	private void loadGlyphTexture(int par1) {
		String var2 = String.format("/font/glyph_%02X.png", new Object[] { Integer.valueOf(par1) });
		this.renderEngine.bindTexture(var2);
	}

	/**
	 * Render a single Unicode character at current (posX,posY) location using one
	 * of the /font/glyph_XX.png files...
	 */
	private float renderUnicodeChar(char par1, boolean par2) {
		if (this.glyphWidth[par1] == 0) {
			return 0.0F;
		} else {
			Tessellator t = Tessellator.instance;
			t.draw();
			int var3 = par1 / 256;
			this.loadGlyphTexture(var3);
			int var4 = this.glyphWidth[par1] >>> 4;
			int var5 = this.glyphWidth[par1] & 15;
			float var6 = (float) var4;
			float var7 = (float) (var5 + 1);
			float var8 = (float) (par1 % 16 * 16) + var6;
			float var9 = (float) ((par1 & 255) / 16 * 16);
			float var10 = var7 - var6 - 0.02F;
			float var11 = par2 ? 1.0F : 0.0F;
			t.startDrawing(EaglerAdapter.GL_TRIANGLE_STRIP);
			t.addVertexWithUV(this.posX + 0.01F + var11, this.posY + 0.01F, 0.0F, (var8 + 0.02F) / 256.0F, (var9 + 0.02F) / 256.0F);
			t.addVertexWithUV(this.posX + 0.01F - var11, this.posY + 7.99F, 0.0F, (var8 + 0.02F) / 256.0F, (var9 + 15.98F) / 256.0F);
			t.addVertexWithUV(this.posX + var10 / 2.0F + var11, this.posY + 0.01F, 0.0F, (var8 + var10) / 256.0F, (var9 + 0.02F) / 256.0F);
			t.addVertexWithUV(this.posX + var10 / 2.0F - var11, this.posY + 7.99F, 0.0F, (var8 + var10) / 256.0F, (var9 + 15.98F) / 256.0F);
			t.draw();
			this.fontTexture.bindTexture();
			t.startDrawingQuads();
			return (var7 - var6) / 2.0F + 1.0F;
		}
	}

	/**
	 * Draws the specified string with a shadow.
	 */
	public int drawStringWithShadow(String par1Str, int par2, int par3, int par4) {
		return this.drawString(par1Str, par2, par3, par4, true);
	}

	/**
	 * Draws the specified string.
	 */
	public int drawString(String par1Str, int par2, int par3, int par4) {
		return this.drawString(par1Str, par2, par3, par4, false);
	}

	/**
	 * Draws the specified string. Args: string, x, y, color, dropShadow
	 */
	public int drawString(String par1Str, int par2, int par3, int par4, boolean par5) {
		this.resetStyles();

		int var6;

		if (par5) {
			var6 = this.renderString(par1Str, par2 + 1, par3 + 1, par4, true);
			var6 = Math.max(var6, this.renderString(par1Str, par2, par3, par4, false));
		} else {
			var6 = this.renderString(par1Str, par2, par3, par4, false);
		}

		return var6;
	}

	/**
	 * Reset all style flag fields in the class to false; called at the start of
	 * string rendering
	 */
	private void resetStyles() {
		this.randomStyle = false;
		this.boldStyle = false;
		this.italicStyle = false;
		this.underlineStyle = false;
		this.strikethroughStyle = false;
	}

	/**
	 * Render a single line string at the current (posX,posY) and update posX
	 */
	private void renderStringAtPos(String par1Str, boolean par2) {
		Tessellator t = Tessellator.instance;
		this.fontTexture.bindTexture();
		t.startDrawingQuads();
		for (int var3 = 0; var3 < par1Str.length(); ++var3) {
			char var4 = par1Str.charAt(var3);
			int var5;
			int var6;

			if (var4 == '\u00a7' && var3 + 1 < par1Str.length()) {
				var5 = "0123456789abcdefklmnor".indexOf((char)Character.toLowerCase(par1Str.charAt(var3 + 1)));

				if (var5 < 16) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;

					if (var5 < 0 || var5 > 15) {
						var5 = 15;
					}

					if (par2) {
						var5 += 16;
					}
					
					t.draw();
					t.startDrawingQuads();

					var6 = this.colorCode[var5];
					this.textColor = var6;
					EaglerAdapter.glColor4f((float) ((var6 >> 16) & 255) / 255.0F, (float) ((var6 >> 8) & 255) / 255.0F, (float) (var6 & 255) / 255.0F, this.alpha);
				} else if (var5 == 16) {
					this.randomStyle = true;
				} else if (var5 == 17) {
					this.boldStyle = true;
				} else if (var5 == 18) {
					this.strikethroughStyle = true;
				} else if (var5 == 19) {
					this.underlineStyle = true;
				} else if (var5 == 20) {
					this.italicStyle = true;
				} else if (var5 == 21) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					t.draw();
					t.startDrawingQuads();
					EaglerAdapter.glColor4f(this.red, this.blue, this.green, this.alpha);
				}

				++var3;
			} else {
				var5 = ChatAllowedCharacters.allowedCharacters.indexOf(var4);

				if (this.randomStyle && var5 > 0) {
					do {
						var6 = this.fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
					} while (this.charWidth[var5 + 32] != this.charWidth[var6 + 32]);

					var5 = var6;
				}

				float var11 = this.unicodeFlag ? 0.48F : 0.98F;
				boolean var7 = (var5 <= 0 || this.unicodeFlag) && par2;

				if (var7) {
					this.posX -= var11;
					this.posY -= var11;
				}

				float var8 = this.renderCharAtPos(var5, var4, this.italicStyle);

				if (var7) {
					this.posX += var11;
					this.posY += var11;
				}

				if (this.boldStyle) {
					this.posX += var11;

					if (var7) {
						this.posX -= var11;
						this.posY -= var11;
					}

					this.renderCharAtPos(var5, var4, this.italicStyle);
					this.posX -= var11;

					if (var7) {
						this.posX += var11;
						this.posY += var11;
					}

					++var8;
				}

				Tessellator var9;

				if (this.strikethroughStyle) {
					var9 = Tessellator.instance;
					var9.draw();
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					var9.startDrawingQuads();
					var9.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
					var9.addVertex((double) (this.posX + var8), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
					var9.addVertex((double) (this.posX + var8), (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					var9.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					var9.draw();
					var9.startDrawingQuads();
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				}

				if (this.underlineStyle) {
					var9 = Tessellator.instance;
					var9.draw();
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					var9.startDrawingQuads();
					int var10 = this.underlineStyle ? -1 : 0;
					var9.addVertex((double) (this.posX + (float) var10), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
					var9.addVertex((double) (this.posX + var8), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
					var9.addVertex((double) (this.posX + var8), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
					var9.addVertex((double) (this.posX + (float) var10), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
					var9.draw();
					var9.startDrawingQuads();
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				}

				this.posX += (float) ((int) var8);
			}
		}
		t.draw();
	}

	/**
	 * Render string either left or right aligned depending on bidiFlag
	 */
	private int renderStringAligned(String par1Str, int par2, int par3, int par4, int par5, boolean par6) {
		return this.renderString(par1Str, par2, par3, par5, par6);
	}

	/**
	 * Render single line string by setting GL color, current (posX,posY), and
	 * calling renderStringAtPos()
	 */
	private int renderString(String par1Str, int par2, int par3, int par4, boolean par5) {
		if (par1Str == null) {
			return 0;
		} else {
			if ((par4 & -67108864) == 0) {
				par4 |= -16777216;
			}

			if (par5) {
				par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
			}

			this.red = (float) (par4 >> 16 & 255) / 255.0F;
			this.blue = (float) (par4 >> 8 & 255) / 255.0F;
			this.green = (float) (par4 & 255) / 255.0F;
			this.alpha = (float) (par4 >> 24 & 255) / 255.0F;
			EaglerAdapter.glColor4f(this.red, this.blue, this.green, this.alpha);
			this.posX = (float) par2;
			this.posY = (float) par3;
			this.renderStringAtPos(par1Str, par5);
			return (int) this.posX;
		}
	}

	/**
	 * Returns the width of this string. Equivalent of
	 * FontMetrics.stringWidth(String s).
	 */
	public int getStringWidth(String par1Str) {
		if (par1Str == null) {
			return 0;
		} else {
			int var2 = 0;
			boolean var3 = false;

			for (int var4 = 0; var4 < par1Str.length(); ++var4) {
				char var5 = par1Str.charAt(var4);
				int var6 = this.getCharWidth(var5);

				if (var6 < 0 && var4 < par1Str.length() - 1) {
					++var4;
					var5 = par1Str.charAt(var4);

					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82) {
							var3 = false;
						}
					} else {
						var3 = true;
					}

					var6 = 0;
				}

				var2 += var6;

				if (var3) {
					++var2;
				}
			}

			return var2;
		}
	}

	/**
	 * Returns the width of this character as rendered.
	 */
	public int getCharWidth(char par1) {
		if (par1 == 167) {
			return -1;
		} else if (par1 == 32) {
			return 4;
		} else {
			int var2 = ChatAllowedCharacters.allowedCharacters.indexOf(par1);

			if (var2 >= 0 && !this.unicodeFlag) {
				return this.charWidth[var2 + 32];
			} else if (this.glyphWidth[par1] != 0) {
				int var3 = this.glyphWidth[par1] >>> 4;
				int var4 = this.glyphWidth[par1] & 15;

				if (var4 > 7) {
					var4 = 15;
					var3 = 0;
				}

				++var4;
				return (var4 - var3) / 2 + 1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Trims a string to fit a specified Width.
	 */
	public String trimStringToWidth(String par1Str, int par2) {
		return this.trimStringToWidth(par1Str, par2, false);
	}

	/**
	 * Trims a string to a specified width, and will reverse it if par3 is set.
	 */
	public String trimStringToWidth(String par1Str, int par2, boolean par3) {
		StringBuilder var4 = new StringBuilder();
		int var5 = 0;
		int var6 = par3 ? par1Str.length() - 1 : 0;
		int var7 = par3 ? -1 : 1;
		boolean var8 = false;
		boolean var9 = false;

		for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < par2; var10 += var7) {
			char var11 = par1Str.charAt(var10);
			int var12 = this.getCharWidth(var11);

			if (var8) {
				var8 = false;

				if (var11 != 108 && var11 != 76) {
					if (var11 == 114 || var11 == 82) {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0) {
				var8 = true;
			} else {
				var5 += var12;

				if (var9) {
					++var5;
				}
			}

			if (var5 > par2) {
				break;
			}

			if (par3) {
				var4.insert(0, var11);
			} else {
				var4.append(var11);
			}
		}

		return var4.toString();
	}

	/**
	 * Remove all newline characters from the end of the string
	 */
	private String trimStringNewline(String par1Str) {
		while (par1Str != null && par1Str.endsWith("\n")) {
			par1Str = par1Str.substring(0, par1Str.length() - 1);
		}

		return par1Str;
	}

	/**
	 * Splits and draws a String with wordwrap (maximum length is parameter k)
	 */
	public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5) {
		this.resetStyles();
		this.textColor = par5;
		par1Str = this.trimStringNewline(par1Str);
		this.renderSplitString(par1Str, par2, par3, par4, false);
	}

	/**
	 * Perform actual work of rendering a multi-line string with wordwrap and with
	 * darker drop shadow color if flag is set
	 */
	private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5) {
		List var6 = this.listFormattedStringToWidth(par1Str, par4);

		for (Iterator var7 = var6.iterator(); var7.hasNext(); par3 += this.FONT_HEIGHT) {
			String var8 = (String) var7.next();
			this.renderStringAligned(var8, par2, par3, par4, this.textColor, par5);
		}
	}

	/**
	 * Returns the width of the wordwrapped String (maximum length is parameter k)
	 */
	public int splitStringWidth(String par1Str, int par2) {
		return this.FONT_HEIGHT * this.listFormattedStringToWidth(par1Str, par2).size();
	}

	/**
	 * Set unicodeFlag controlling whether strings should be rendered with Unicode
	 * fonts instead of the default.png font.
	 */
	public void setUnicodeFlag(boolean par1) {
		this.unicodeFlag = par1;
	}

	/**
	 * Get unicodeFlag controlling whether strings should be rendered with Unicode
	 * fonts instead of the default.png font.
	 */
	public boolean getUnicodeFlag() {
		return this.unicodeFlag;
	}

	/**
	 * Set bidiFlag to control if the Unicode Bidirectional Algorithm should be run
	 * before rendering any string.
	 */
	public void setBidiFlag(boolean par1) {
		this.bidiFlag = par1;
	}

	/**
	 * Breaks a string into a list of pieces that will fit a specified width.
	 */
	public List listFormattedStringToWidth(String par1Str, int par2) {
		return Arrays.asList(this.wrapFormattedStringToWidth(par1Str, par2).split("\n"));
	}

	/**
	 * Inserts newline and formatting into a string to wrap it within the specified
	 * width.
	 */
	String wrapFormattedStringToWidth(String par1Str, int par2) {
		int var3 = this.sizeStringToWidth(par1Str, par2);

		if (par1Str.length() <= var3) {
			return par1Str;
		} else {
			String var4 = par1Str.substring(0, var3);
			char var5 = par1Str.charAt(var3);
			boolean var6 = var5 == 32 || var5 == 10;
			String var7 = getFormatFromString(var4) + par1Str.substring(var3 + (var6 ? 1 : 0));
			return var4 + "\n" + this.wrapFormattedStringToWidth(var7, par2);
		}
	}

	/**
	 * Determines how many characters from the string will fit into the specified
	 * width.
	 */
	private int sizeStringToWidth(String par1Str, int par2) {
		int var3 = par1Str.length();
		int var4 = 0;
		int var5 = 0;
		int var6 = -1;

		for (boolean var7 = false; var5 < var3; ++var5) {
			char var8 = par1Str.charAt(var5);

			switch (var8) {
			case 10:
				--var5;
				break;

			case 167:
				if (var5 < var3 - 1) {
					++var5;
					char var9 = par1Str.charAt(var5);

					if (var9 != 108 && var9 != 76) {
						if (var9 == 114 || var9 == 82 || isFormatColor(var9)) {
							var7 = false;
						}
					} else {
						var7 = true;
					}
				}

				break;

			case 32:
				var6 = var5;

			default:
				var4 += this.getCharWidth(var8);

				if (var7) {
					++var4;
				}
			}

			if (var8 == 10) {
				++var5;
				var6 = var5;
				break;
			}

			if (var4 > par2) {
				break;
			}
		}

		return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
	}

	/**
	 * Checks if the char code is a hexadecimal character, used to set colour.
	 */
	private static boolean isFormatColor(char par0) {
		return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
	}

	/**
	 * Checks if the char code is O-K...lLrRk-o... used to set special formatting.
	 */
	private static boolean isFormatSpecial(char par0) {
		return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
	}

	/**
	 * Digests a string for nonprinting formatting characters then returns a string
	 * containing only that formatting.
	 */
	private static String getFormatFromString(String par0Str) {
		String var1 = "";
		int var2 = -1;
		int var3 = par0Str.length();

		while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				char var4 = par0Str.charAt(var2 + 1);

				if (isFormatColor(var4)) {
					var1 = "\u00a7" + var4;
				} else if (isFormatSpecial(var4)) {
					var1 = var1 + "\u00a7" + var4;
				}
			}
		}

		return var1;
	}

	/**
	 * Get bidiFlag that controls if the Unicode Bidirectional Algorithm should be
	 * run before rendering any string
	 */
	public boolean getBidiFlag() {
		return this.bidiFlag;
	}
}
