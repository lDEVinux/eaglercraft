package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class GLAllocation {
	private static final Map field_74531_a = new HashMap();
	private static final List field_74530_b = new ArrayList();

	/**
	 * Generates the specified number of display lists and returns the first index.
	 */
	public static synchronized int generateDisplayLists(int par0) {
		int var1 = EaglerAdapter.glGenLists(par0);
		field_74531_a.put(Integer.valueOf(var1), Integer.valueOf(par0));
		return var1;
	}

	/**
	 * Generates texture names and stores them in the specified buffer.
	 */
	public static synchronized int generateTextureNames() {
		int var0 = EaglerAdapter.glGenTextures();
		field_74530_b.add(Integer.valueOf(var0));
		return var0;
	}

	public static synchronized void deleteDisplayLists(int par0) {
		EaglerAdapter.glDeleteLists(par0, ((Integer) field_74531_a.remove(Integer.valueOf(par0))).intValue());
	}

	public static synchronized void func_98302_b() {
		for (int var0 = 0; var0 < field_74530_b.size(); ++var0) {
			EaglerAdapter.glDeleteTextures(((Integer) field_74530_b.get(var0)).intValue());
		}

		field_74530_b.clear();
	}

	/**
	 * Deletes all textures and display lists. Called when Minecraft is shutdown to
	 * free up resources.
	 */
	public static synchronized void deleteTexturesAndDisplayLists() {
		Iterator var0 = field_74531_a.entrySet().iterator();

		while (var0.hasNext()) {
			Entry var1 = (Entry) var0.next();
			EaglerAdapter.glDeleteLists(((Integer) var1.getKey()).intValue(), ((Integer) var1.getValue()).intValue());
		}

		field_74531_a.clear();
		func_98302_b();
	}

	/**
	 * Creates and returns a direct byte buffer with the specified capacity. Applies
	 * native ordering to speed up access.
	 */
	public static ByteBuffer createDirectByteBuffer(int par0) {
		return EaglerAdapter.isWebGL ? ByteBuffer.wrap(new byte[par0]) : ByteBuffer.allocateDirect(par0).order(ByteOrder.nativeOrder());
	}

	/**
	 * Creates and returns a direct int buffer with the specified capacity. Applies
	 * native ordering to speed up access.
	 */
	public static IntBuffer createDirectIntBuffer(int par0) {
		return EaglerAdapter.isWebGL ? IntBuffer.wrap(new int[par0]) : createDirectByteBuffer(par0 << 2).asIntBuffer();
	}

	/**
	 * Creates and returns a direct float buffer with the specified capacity.
	 * Applies native ordering to speed up access.
	 */
	public static FloatBuffer createDirectFloatBuffer(int par0) {
		return EaglerAdapter.isWebGL ? FloatBuffer.wrap(new float[par0]) : createDirectByteBuffer(par0 << 2).asFloatBuffer();
	}
}
