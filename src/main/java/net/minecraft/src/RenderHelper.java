package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class RenderHelper {
	/** Float buffer used to set OpenGL material colors */
	//private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
	//private static final Vec3 field_82884_b = Vec3.createVectorHelper(0.20000000298023224D, 1.0D, -0.699999988079071D).normalize();
	//private static final Vec3 field_82885_c = Vec3.createVectorHelper(-0.20000000298023224D, 1.0D, 0.699999988079071D).normalize();

	/**
	 * Disables the OpenGL lighting properties enabled by enableStandardItemLighting
	 */
	public static void disableStandardItemLighting() {
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glDisable(EaglerAdapter.GL_COLOR_MATERIAL);
	}

	/**
	 * Sets the OpenGL lighting properties to the values used when rendering blocks
	 * as items
	 */
	public static void enableStandardItemLighting() {
		EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glEnable(EaglerAdapter.GL_COLOR_MATERIAL);
		EaglerAdapter.copyModelToLightMatrix();
		/*
		EaglerAdapter.glColorMaterial(EaglerAdapter.GL_FRONT_AND_BACK, EaglerAdapter.GL_AMBIENT_AND_DIFFUSE);
		float var0 = 0.4F;
		float var1 = 0.6F;
		float var2 = 0.0F;
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT0, EaglerAdapter.GL_POSITION, setColorBuffer(field_82884_b.xCoord, field_82884_b.yCoord, field_82884_b.zCoord, 0.0D));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT0, EaglerAdapter.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT0, EaglerAdapter.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT0, EaglerAdapter.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT1, EaglerAdapter.GL_POSITION, setColorBuffer(field_82885_c.xCoord, field_82885_c.yCoord, field_82885_c.zCoord, 0.0D));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT1, EaglerAdapter.GL_DIFFUSE, setColorBuffer(var1, var1, var1, 1.0F));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT1, EaglerAdapter.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		EaglerAdapter.glLight(EaglerAdapter.GL_LIGHT1, EaglerAdapter.GL_SPECULAR, setColorBuffer(var2, var2, var2, 1.0F));
		EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
		EaglerAdapter.glLightModel(EaglerAdapter.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(var0, var0, var0, 1.0F));
		*/
	}
	public static void enableStandardItemLighting2() {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
		enableStandardItemLighting();
		EaglerAdapter.glPopMatrix();
	}

	/**
	 * Sets OpenGL lighting for rendering blocks as items inside GUI screens (such
	 * as containers).
	 */
	public static void enableGUIStandardItemLighting() {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(165.0F, 1.0F, 0.0F, 0.0F);
		//EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
		enableStandardItemLighting();
		EaglerAdapter.glPopMatrix();
	}
	
	public static void enableGUIStandardItemLighting2() {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(165.0F, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
		enableStandardItemLighting();
		EaglerAdapter.glPopMatrix();
	}
}
