package net.lax1dude.eaglercraft;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.FramebufferGL;
import net.lax1dude.eaglercraft.adapter.EaglerAdapterImpl2.TextureGL;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderManager;

//supports only 16x16 textures, mipmap is four levels deep
public class TextureTerrainMap implements IconRegister {
	
	private static class TerrainIconV2 implements Icon {

		public final TextureTerrainMap map;
		public final String name;
		public final int id;
		public final int size;
		private TextureGL frames = null;
		private int[] framesIdx = null;

		protected int originX;
		protected int originY;
		private float minU;
		private float maxU;
		private float minV;
		private float maxV;
		
		protected int originX_center;
		protected int originY_center;
		private float minU_center;
		private float maxU_center;
		private float minV_center;
		private float maxV_center;

		protected int frameCounter = 0;
		protected int frameCurrent = 0;
		
		private TerrainIconV2(int id, int s, TextureTerrainMap map, String name) {
			this.id = id;
			this.size = s;
			this.map = map;
			this.name = name;
			
			if(s != 1 && s != 2) {
				throw new IllegalArgumentException("Size " + s + " (" + (s * 16) + "px) is not supported on this texture map");
			}
			
			int tw = s * 16 + 32;
			
			int adjId = id;
			if(s == 2) {
				adjId = (map.width / tw - 1) * (map.height / tw - 1) - id;
			}
			
			this.originX = (adjId % (map.width / tw)) * tw;
			this.originY = (adjId / (map.width / tw)) * tw;
			this.minU = (float)originX / (float)map.width;
			this.minV = (float)originY / (float)map.height;
			this.maxU = (float)(originX + tw) / (float)map.width;
			this.maxV = (float)(originY + tw) / (float)map.height;
			this.originX_center = originX + 16;
			this.originY_center = originY + 16;
			this.minU_center = (float)(originX_center + 0.025f) / (float)map.width;
			this.minV_center = (float)(originY_center + 0.025f) / (float)map.height;
			this.maxU_center = (float)(originX_center + 16 - 0.025f) / (float)map.width;
			this.maxV_center = (float)(originY_center + 16 - 0.025f) / (float)map.height;
		}
		
		private void free() {
			if(frames != null) {
				EaglerAdapter._wglDeleteTextures(frames);
				frames = null;
			}
		}

		@Override
		public int getOriginX() {
			return originX_center;
		}

		@Override
		public int getOriginY() {
			return originY_center;
		}

		@Override
		public float getMinU() {
			return minU_center;
		}

		@Override
		public float getMaxU() {
			return maxU_center;
		}

		@Override
		public float getInterpolatedU(double var1) {
			float var3 = this.maxU_center - this.minU_center;
			return this.minU_center + var3 * ((float) var1 * size / 16.0F);
		}

		@Override
		public float getMinV() {
			return minV_center;
		}

		@Override
		public float getMaxV() {
			return maxV_center;
		}

		@Override
		public float getInterpolatedV(double var1) {
			float var3 = this.maxV_center - this.minV_center;
			return this.minV_center + var3 * ((float) var1 * size / 16.0F);
		}

		@Override
		public String getIconName() {
			return name == null ? "missingno" : name;
		}

		@Override
		public int getSheetWidth() {
			return map.width;
		}

		@Override
		public int getSheetHeight() {
			return map.height;
		}
		
		private void updateAnimation() {
			if(frames != null) {
				this.frameCounter = (this.frameCounter + 1) % this.framesIdx.length;
				int i = framesIdx[this.frameCounter];
				if (this.frameCurrent != i) {
					this.frameCurrent = i;
					map.copyFrame(this, i);
				}
			}
		}
		
		private void loadData() {
			byte[] data = EaglerAdapter.loadResourceBytes("/" + map.basePath + name + ".png");
			if(data == null) {
				map.replaceTexture(this, map.missingData);
			}else {
				EaglerImage img = EaglerAdapter.loadPNG(data);
				if(img == null) {
					map.replaceTexture(this, map.missingData);
				}else {
					int ss = size * 16;
					int divs = img.h / ss;
					if(divs == 1) {
						map.replaceTexture(this, generateMip(img));
						this.frames = null;
						this.framesIdx = null;
					}else {
						map.replaceTexture(this, generateMip(img.getSubImage(0, 0, ss, ss)));
						
						EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, -1);
						frames = EaglerAdapter._wglGenTextures();
						EaglerAdapter._wglBindTexture(EaglerAdapter.GL_TEXTURE_2D, frames);

						EaglerImage mipLvl = populateAlpha(img);
						uploadBuffer.clear();
						uploadBuffer.put(mipLvl.data);
						uploadBuffer.flip();
						EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, EaglerAdapter.GL_RGBA, mipLvl.w, mipLvl.h, 0,
								EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);

						mipLvl = generateLevel(mipLvl);
						uploadBuffer.clear();
						uploadBuffer.put(mipLvl.data);
						uploadBuffer.flip();
						EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 1, EaglerAdapter.GL_RGBA, mipLvl.w, mipLvl.h, 0,
								EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);

						mipLvl = generateLevel(mipLvl);
						uploadBuffer.clear();
						uploadBuffer.put(mipLvl.data);
						uploadBuffer.flip();
						EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 2, EaglerAdapter.GL_RGBA, mipLvl.w, mipLvl.h, 0,
								EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);

						mipLvl = generateLevel(mipLvl);
						uploadBuffer.clear();
						uploadBuffer.put(mipLvl.data);
						uploadBuffer.flip();
						EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 3, EaglerAdapter.GL_RGBA, mipLvl.w, mipLvl.h, 0,
								EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);

						mipLvl = generateLevel(mipLvl);
						uploadBuffer.clear();
						uploadBuffer.put(mipLvl.data);
						uploadBuffer.flip();
						EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, 4, EaglerAdapter.GL_RGBA, mipLvl.w, mipLvl.h, 0,
								EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);
						
						EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_LEVEL, 4);
						
						String dat = EaglerAdapter.fileContents("/" + map.basePath + name + ".txt");
						if(dat != null) System.out.println("Found animation info for: " + map.basePath + name + ".png");
						if(dat == null || (dat = dat.trim()).isEmpty()) {
							framesIdx = new int[divs];
							for(int i = 0; i < divs; ++i) {
								framesIdx[i] = i;
							}
						}else {
							String[] fd = dat.split(",");
							int len = 0;
							for(int i = 0; i < fd.length; ++i) {
								int j = fd[i].indexOf('*');
								len += (j == -1 ? 1 : Integer.parseInt(fd[i].substring(j + 1)));
							}
							framesIdx = new int[len];
							len = 0;
							for(int i = 0; i < fd.length; ++i) {
								int j = fd[i].indexOf('*');
								if(j == -1) {
									framesIdx[len++] = Integer.parseInt(fd[i]);
								}else {
									int c = Integer.parseInt(fd[i].substring(0, j));
									int l = Integer.parseInt(fd[i].substring(j + 1));
									for(int k = 0; k < l; ++k) {
										framesIdx[len++] = c;
									}
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	private final String basePath;
	private final int width;
	private final int height;
	private TerrainIconV2 missingImage;
	private ArrayList<TerrainIconV2> iconList;
	public final int texture;
	private final EaglerImage[] missingData;
	public final FramebufferGL copyFramebuffer;
	
	private int[] nextSlot = new int[3];
	
	private static final IntBuffer uploadBuffer = EaglerAdapter.isWebGL ? IntBuffer.wrap(new int[0xFFFF]) :
			ByteBuffer.allocateDirect(0xFFFF << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
	
	public TextureTerrainMap(int size, String par2, String par3Str, EaglerImage par4BufferedImage) {
		this.width = size;
		this.height = size;
		this.basePath = par3Str;
		this.missingImage = new TerrainIconV2(nextSlot[1]++, 1, this, null);
		this.iconList = new ArrayList();
		this.texture = EaglerAdapter.glGenTextures();
		this.copyFramebuffer = EaglerAdapter._wglCreateFramebuffer();
		EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, texture);
		int levelW = width;
		int levelH = height;
		IntBuffer blank = GLAllocation.createDirectIntBuffer(levelW * levelH);
		for(int i = 0; i < 5; ++i) {
			blank.clear().limit(levelW * levelH);
			for(int j = 0; j < blank.limit(); ++j) {
				blank.put(j, ((j / levelW + (j % levelW)) % 2 == 0) ? 0xffff00ff : 0xff000000);
			}
			EaglerAdapter.glTexImage2D(EaglerAdapter.GL_TEXTURE_2D, i, EaglerAdapter.GL_RGBA, levelW, levelH, 0, EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, blank);
			levelW /= 2;
			levelH /= 2;
		}
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST_MIPMAP_LINEAR);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_NEAREST);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_CLAMP);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_CLAMP);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_LEVEL, 4);
		EaglerAdapter.glTexParameterf(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_ANISOTROPY, 1.0f);
		replaceTexture(missingImage, missingData = generateMip(par4BufferedImage));
	}
	
	public static EaglerImage[] generateMip(EaglerImage src16x16) {
		EaglerImage[] ret = new EaglerImage[5];
		ret[0] = populateAlpha(create3x3_V2(src16x16));
		ret[1] = generateLevel(ret[0]);
		ret[2] = generateLevel(ret[1]);
		ret[3] = generateLevel(ret[2]);
		ret[4] = generateLevel(ret[3]);
		return ret;
	}
	
	public static EaglerImage generateLevel(EaglerImage src) {
		EaglerImage e = new EaglerImage(src.w / 2, src.h / 2, true);
		for(int y = 0; y < e.h; ++y) {
			for(int x = 0; x < e.w; ++x) {
				int x2 = x * 2;
				int y2 = y * 2;
				int a = src.data[y2 * src.w + x2];
				int b = src.data[y2 * src.w + x2 + 1];
				int c = src.data[(y2 + 1) * src.w + x2];
				int d = src.data[(y2 + 1) * src.w + x2 + 1];
				int ca = (((a >> 24) & 255) + ((b >> 24) & 255) + ((c >> 24) & 255) + ((d >> 24) & 255)) >> 2;
				int cr = (((a >> 16) & 255) + ((b >> 16) & 255) + ((c >> 16) & 255) + ((d >> 16) & 255)) >> 2;
				int cg = (((a >> 8) & 255) + ((b >> 8) & 255) + ((c >> 8) & 255) + ((d >> 8) & 255)) >> 2;
				int cb = ((a & 255) + (b & 255) + (c & 255) + (d & 255)) >> 2;
				e.data[y * e.w + x] = (ca << 24) | (cr << 16) | (cg << 8) | cb;
			}
		}
		return e;
	}
	
	public static EaglerImage populateAlpha(EaglerImage src) {
		EaglerImage ret = new EaglerImage(src.w, src.h, true);
		int reducedR = 0;
		int reducedG = 0;
		int reducedB = 0;
		int divisor = 0;
		int[] array = src.data;
		for(int i = 0; i < array.length; ++i) {
			int x = array[i];
			int a = (x >> 24) & 255;
			if(a > 2) {
				reducedR += (x >> 16) & 255;
				reducedG += (x >> 8) & 255;
				reducedB += x & 255;
				++divisor;
			}
		}
		if(divisor == 0) {
			reducedR = 0;
			reducedG = 0;
			reducedB = 0;
		}else {
			reducedR /= divisor;
			reducedG /= divisor;
			reducedB /= divisor;
		}
		int reducedR2, reducedG2, reducedB2, blend1, blend2, blend3, blend4, j;
		int alpha = (reducedR << 16) | (reducedG << 8) | reducedB;
		for(int i = 0; i < array.length; ++i) {
			int x = array[i];
			int a = (x >> 24) & 255;
			if(a < 2) {
				reducedR2 = 0;
				reducedG2 = 0;
				reducedB2 = 0;
				divisor = 0;
				blend1 = i + 1;
				blend2 = i - 1;
				blend3 = i + src.w;
				blend4 = i - src.w;
				if(blend1 >= 0 && blend1 < array.length) {
					j = array[blend1];
					if(((x >> 24) & 255) > 2){
						reducedR2 += (x >> 16) & 255;
						reducedG2 += (x >> 8) & 255;
						reducedB2 += x & 255;
						++divisor;
					}
				}
				if(blend2 >= 0 && blend2 < array.length) {
					j = array[blend2];
					if(((x >> 24) & 255) > 2){
						reducedR2 += (x >> 16) & 255;
						reducedG2 += (x >> 8) & 255;
						reducedB2 += x & 255;
						++divisor;
					}
				}
				if(blend3 >= 0 && blend3 < array.length) {
					j = array[blend3];
					if(((x >> 24) & 255) > 2){
						reducedR2 += (x >> 16) & 255;
						reducedG2 += (x >> 8) & 255;
						reducedB2 += x & 255;
						++divisor;
					}
				}
				if(blend4 >= 0 && blend4 < array.length) {
					j = array[blend4];
					if(((x >> 24) & 255) > 2){
						reducedR2 += (x >> 16) & 255;
						reducedG2 += (x >> 8) & 255;
						reducedB2 += x & 255;
						++divisor;
					}
				}
				if(divisor == 0) {
					ret.data[i] = alpha;
				}else {
					ret.data[i] = ((reducedR2 / divisor) << 16) | ((reducedG2 / divisor) << 8) | (reducedB2 / divisor);
				}
			}else {
				ret.data[i] = src.data[i];
			}
		}
		return ret;
	}
	
	public static EaglerImage create3x3_V2(EaglerImage src) {
		EaglerImage ret = new EaglerImage(src.w + 32, src.h + 32, true);
		for(int y = 0; y < src.h; ++y) {
			for(int x = 0; x < src.w; ++x) {
				int pixel = src.data[y * src.w + x];
				
				ret.data[(y + 16) * ret.w + (x + 16)] = pixel;
				
				if(x < 16) {
					ret.data[(y + 16) * ret.w + x] = pixel;
				}
				
				if(y < 16) {
					ret.data[y * ret.w + (x + 16)] = pixel;
				}
				
				if(x < 16 && y < 16) {
					ret.data[y * ret.w + x] = pixel;
				}

				int mw = src.w - 16;
				int mh = src.h - 16;
				
				if(x >= mw) {
					ret.data[(y + 16) * ret.w + src.w + (x - mw + 16)] = pixel;
				}
				
				if(y >= mh) {
					ret.data[(y - mh + src.h + 16) * ret.w + (x + 16)] = pixel;
				}
				
				if(x >= mw && y >= mh) {
					ret.data[(y - mh + src.h + 16) * ret.w + src.w + (x - mw + 16)] = pixel;
				}
				
				if(x >= mw && y < 16) {
					ret.data[y * ret.w + src.w + (x - mw + 16)] = pixel;
				}
				
				if(x < 16 && y >= mh) {
					ret.data[(y - mh + src.h + 16) * ret.w + x] = pixel;
				}
				
			}
		}
		return ret;
	}

	public void refreshTextures() {
		for(TerrainIconV2 t : iconList) {
			t.free();
		}
		iconList.clear();
		nextSlot = new int[3];
		nextSlot[1] = 1;
		Block[] var1 = Block.blocksList;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Block var4 = var1[var3];

			if (var4 != null) {
				var4.registerIcons(this);
			}
		}

		Minecraft.getMinecraft().renderGlobal.registerDestroyBlockIcons(this);
		RenderManager.instance.updateIcons(this);
		
		for(TerrainIconV2 t : iconList) {
			t.loadData();
		}
	}
	
	private void replaceTexture(TerrainIconV2 icon, EaglerImage[] textures) {
		int divisor = 1;
		EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, texture);
		for(int i = 0; i < 5; i++) {
			uploadBuffer.clear();
			uploadBuffer.put(textures[i].data);
			uploadBuffer.flip();
			EaglerAdapter.glTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, icon.originX / divisor, icon.originY / divisor, 
					(16 * icon.size + 32) / divisor, (16 * icon.size + 32) / divisor, EaglerAdapter.GL_RGBA, EaglerAdapter.GL_UNSIGNED_BYTE, uploadBuffer);
			divisor *= 2;
		}
	}
	
	private void copyFrame(TerrainIconV2 icon, int frame) {
		int off = icon.size * 16;
		int divisor = 1;
		EaglerAdapter._wglBindFramebuffer(EaglerAdapter._wGL_FRAMEBUFFER, copyFramebuffer);
		EaglerAdapter._wglReadBuffer(EaglerAdapter._wGL_COLOR_ATTACHMENT0);
		for(int i = 0; i < 5; i++) {
			EaglerAdapter._wglBindTexture(EaglerAdapter.GL_TEXTURE_2D, icon.frames);
			EaglerAdapter._wglFramebufferTexture2D(EaglerAdapter._wGL_COLOR_ATTACHMENT0, icon.frames, i);
			EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, texture);
			
			// 0, -1
			EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, icon.originX_center / divisor, (icon.originY_center - 16) / divisor,
					0, (frame * off + off - 16 / divisor), off, 16 / divisor);

			// -1, 0
			EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, (icon.originX_center - 16) / divisor, icon.originY_center / divisor,
					off - 16 / divisor, frame * off, 16 / divisor, off);
			
			// 0, 0
			EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, icon.originX_center / divisor, icon.originY_center / divisor,
					0, frame * off, off, off);
			
			// 0, 1
			EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, icon.originX_center / divisor, (icon.originY_center + 16 * icon.size) / divisor,
					0, frame * off, off, 16 / divisor);
			
			// 1, 0
			EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, i, (icon.originX_center + 16 * icon.size) / divisor, icon.originY_center / divisor,
					0, frame * off, 16 / divisor, off);
			
			off /= 2;
			divisor *= 2;
		}
		EaglerAdapter._wglBindFramebuffer(EaglerAdapter._wGL_FRAMEBUFFER, null);
	}

	public void updateAnimations() {
		for(TerrainIconV2 t : iconList) {
			t.updateAnimation();
		}
	}

	public Icon registerIcon(String par1Str, int w) {
		if(w != 1 && w != 2) {
			System.err.println("Error, texture '" + par1Str + "' was registered with size " + w + ", the terrain texure map only supports size 1 and 2 (16px and 32px)");
			return missingImage;
		}else if(par1Str != null) {
			for(TerrainIconV2 t : iconList) {
				if(par1Str.equals(t.name) && w == t.size) {
					return t;
				}
			}
			TerrainIconV2 ret = new TerrainIconV2(nextSlot[w]++, w, this, par1Str);
			iconList.add(ret);
			return ret;
		}else{
			return missingImage;
		}
	}

	public Icon getMissingIcon() {
		return missingImage;
	}
}
