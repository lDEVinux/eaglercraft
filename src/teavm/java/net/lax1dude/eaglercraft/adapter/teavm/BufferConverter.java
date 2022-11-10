package net.lax1dude.eaglercraft.adapter.teavm;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferConverter {
	
	public static final byte[] convertByteBuffer(ByteBuffer b) {
		byte[] ret = new byte[b.limit() - b.position()];
		b.get(ret);
		return ret;
	}
	
	public static final short[] convertShortBuffer(ShortBuffer b) {
		short[] ret = new short[b.limit() - b.position()];
		b.get(ret);
		return ret;
	}
	
	public static final int[] convertIntBuffer(IntBuffer b) {
		int[] ret = new int[b.limit() - b.position()];
		b.get(ret);
		return ret;
	}
	
	public static final float[] convertFloatBuffer(FloatBuffer b) {
		float[] ret = new float[b.limit() - b.position()];
		b.get(ret);
		return ret;
	}

}
