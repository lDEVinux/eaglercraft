package ayunami2000;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class MapPacketCodec {
	
	public interface FragmentHandler {
		void sendFragment(byte[] data, boolean isLastFragment);
	}
	
	public enum PixelFormat {
		R5_G6_B5, R8_G8_B8
	}
	
	public final int mapId;
	private Deflater deflate = null;
	private PixelFormat pixelFormat = PixelFormat.R5_G6_B5;
	private int[] pixels = null;
	private int[] pallete = null;
	private boolean palleteIsSet = false;
	private boolean palleteIsDirty = false;
	
	/**
	 * @param mapId the ID of the map item to write to
	 */
	public MapPacketCodec(int mapId) {
		this.mapId = mapId;
	}
	
	/**
	 * @param enable enables java.util.zip deflate on packets encoded by this class
	 */
	public MapPacketCodec deflate(boolean enable) {
		deflate(enable ? 5 : 0);
		return this;
	}
	
	/**
	 * @param level sets or disables compression level (0-9)
	 */
	public MapPacketCodec deflate(int level) {
		deflate = level > 0 ? new Deflater(level) : null;
		return this;
	}
	
	/**
	 * @param pix sets if pixels should be encoded as 16 bits per pixel or 24 bits per pixel
	 */
	public MapPacketCodec pixelFormat(PixelFormat pix) {
		pixelFormat = pix == null ? PixelFormat.R5_G6_B5 : pix;
		return this;
	}
	
	/**
	 * @param pixels If pallete is disabled, array of 16384 integers each containing the RGB of a pixel. If
	 * pallete is enabled, array of 16384 integers each containing an index in the current pallete between 0 and 255
	 */
	public MapPacketCodec setPixels(int[] pixels) {
		if(pixels != null && pixels.length != 16384) {
			throw new IllegalArgumentException("Pixel array must be 16384 pixels long");
		}
		this.pixels = pixels;
		return this;
	}
	
	/**
	 * @param pixels a 128x128 RGB java.awt.image.BufferedImage, this will disable the pallete
	 */
	public MapPacketCodec setPixels(BufferedImage pixels) {
		if(pixels.getWidth() != 128 || pixels.getHeight() != 128) {
			throw new IllegalArgumentException("BufferedImage must be 128x128 pixels");
		}
		palleteIsSet = false;
		this.pallete = null;
		palleteIsDirty = false;
		int[] pxls = new int[16384];
		pixels.getRGB(0, 0, 128, 128, pxls, 0, 128);
		setPixels(pxls);
		return this;
	}
	
	/**
	 * sets and enables the pallete, or disables it if 'pallete' is null
	 * 
	 * @param pallete an array of any size between 1 and 256 containing integers each representing a single RGB color
	 */
	public MapPacketCodec setPallete(int[] pallete) {
		boolean b = pallete != null;
		if(b) {
			palleteIsSet = true;
			this.pallete = pallete;
			palleteIsDirty = true;
		}else {
			if(pixels != null && this.pallete != null) {
				int[] px = pixels;
				pixels = new int[16384];
				for(int i = 0; i < 16384; ++i) {
					int j = px[i];
					pixels[i] = this.pallete[j >= this.pallete.length ? 0 : j];
				}
			}
			this.pallete = null;
			palleteIsSet = false;
			palleteIsDirty = false;
		}
		return this;
	}
	
	/**
	 * Disables the pallete
	 */
	public MapPacketCodec clearPallete() {
		setPallete(null);
		return this;
	}
	
	/*
	 * Operations:
	 * 
	 *   - 0: disable engine
	 *   - 1: compressed data
	 *   
	 *   - 2: set pixels R8_G8_B8
	 *   - 3: set pixels R5_G6_B5
	 *   - 4: set pallete R8_G8_B8
	 *   - 5: set pallete R5_G6_B5
	 *   - 6: set pixels via pallete
	 *   - 7: set pallete and pixels R8_G8_B8
	 *   - 8: set pallete and pixels R5_G6_B5
	 * 
	 */
	
	/**
	 * takes the current pixels array and writes it to a packet and returns it, or returns null if the current pixels array has not changed
	 */
	public byte[] getNextPacket() {
		if(pixels == null) {
			return null;
		}
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			DataOutputStream s;
			if(deflate != null) {
				o.write(1);
				s = new DataOutputStream(new DeflaterOutputStream(o, deflate));
			}else {
				s = new DataOutputStream(o);
			}
			if(!palleteIsSet || pallete == null) {
				palleteIsSet = false;
				if(pixelFormat == PixelFormat.R5_G6_B5) {
					s.write(3);
					for(int i = 0; i < 16384; ++i) {
						int j = pixels[i];
						int r = (j >> 19) & 0x1F;
						int g = (j >> 10) & 0x3F;
						int b = (j >> 3) & 0x1F;
						s.writeShort((r << 11) | (g << 5) | b);
					}
				}else if(pixelFormat == PixelFormat.R8_G8_B8) {
					s.write(2);
					for(int i = 0; i < 16384; ++i) {
						int j = pixels[i];
						s.write((j >> 16) & 0xFF);
						s.write((j >> 8) & 0xFF);
						s.write(j & 0xFF);
					}
				}else {
					return null;  // ?
				}
			}else {
				if(palleteIsDirty) {
					if(pixelFormat == PixelFormat.R5_G6_B5) {
						s.write(8);
						s.write(pallete.length);
						for(int i = 0; i < pallete.length; ++i) {
							int j = pallete[i];
							int r = (j >> 19) & 0x1F;
							int g = (j >> 10) & 0x3F;
							int b = (j >> 3) & 0x1F;
							s.writeShort((r << 11) | (g << 5) | b);
						}
					}else if(pixelFormat == PixelFormat.R8_G8_B8) {
						s.write(7);
						s.write(pallete.length);
						for(int i = 0; i < pallete.length; ++i) {
							int j = pallete[i];
							s.write((j >> 16) & 0xFF);
							s.write((j >> 8) & 0xFF);
							s.write(j & 0xFF);
						}
					}else {
						return null;  // ?
					}
					palleteIsDirty = false;
				}else {
					s.write(6);
				}
				for(int i = 0; i < 16384; ++i) {
					s.write(pixels[i]);
				}
			}
			pixels = null;
			s.close();
			return o.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("Failed to write ayunami map packet");
		}
	}
	
	public byte[] getDisablePacket() {
		try {
			palleteIsSet = false;
			palleteIsDirty = false;
			pallete = null;
			pixels = null;
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			DataOutputStream s = new DataOutputStream(o);
			s.writeShort(mapId);
			s.write(0);
			return o.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("Failed to write ayunami map packet");
		}
	}
	
}
