package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) var1;
		if(spawner.mobObject == null) {
			spawner.mobObject = EntityList.createEntityByName(spawner.mobID, spawner.worldObj);
			if(spawner.mobObject != null) {
				spawner.mobObject.setWorld(spawner.worldObj);
			}
		}
		if(spawner.mobObject != null) {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float)var2 + 0.5F, (float)var4 + 0.4f, (float)var6 + 0.5F);
			EaglerAdapter.glRotatef((spawner.rotateTicks + var8) * 50.0f, 0.0f, 1.0f, 0.0f);
			EaglerAdapter.glRotatef(-30.0f, 1.0f, 0.0f, 0.0f);
			EaglerAdapter.glTranslatef(0.0f, -0.4f, 0.0f);
			float var9 = 0.4375F;
			EaglerAdapter.glScalef(var9, var9, var9);
			spawner.mobObject.setLocationAndAngles(var2, var4, var6, 0.0f, 0.0f);
			RenderManager.instance.renderEntityWithPosYaw(spawner.mobObject, 0.0D, 0.0D, 0.0D, 0.0F, var8);
			EaglerAdapter.glPopMatrix();
		}
	}

}
