package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerImage;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.lax1dude.eaglercraft.glemu.EffectPipeline;
import net.lax1dude.eaglercraft.glemu.EffectPipelineFXAA;
import net.lax1dude.eaglercraft.glemu.vector.Matrix4f;
import net.minecraft.client.Minecraft;

public class EntityRenderer {
	public static boolean anaglyphEnable = false;

	/** Anaglyph field (0=R, 1=GB) */
	public static int anaglyphField;

	/** A reference to the Minecraft object. */
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;

	/** Entity renderer update count */
	private int rendererUpdateCount;

	/** Pointed entity */
	private Entity pointedEntity = null;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();

	/** Mouse filter dummy 1 */
	private MouseFilter mouseFilterDummy1 = new MouseFilter();

	/** Mouse filter dummy 2 */
	private MouseFilter mouseFilterDummy2 = new MouseFilter();

	/** Mouse filter dummy 3 */
	private MouseFilter mouseFilterDummy3 = new MouseFilter();

	/** Mouse filter dummy 4 */
	private MouseFilter mouseFilterDummy4 = new MouseFilter();
	private float thirdPersonDistance = 4.0F;

	/** Third person distance temp */
	private float thirdPersonDistanceTemp = 4.0F;
	private float debugCamYaw = 0.0F;
	private float prevDebugCamYaw = 0.0F;
	private float debugCamPitch = 0.0F;
	private float prevDebugCamPitch = 0.0F;

	/** Smooth cam yaw */
	private float smoothCamYaw;

	/** Smooth cam pitch */
	private float smoothCamPitch;

	/** Smooth cam filter X */
	private float smoothCamFilterX;

	/** Smooth cam filter Y */
	private float smoothCamFilterY;

	/** Smooth cam partial ticks */
	private float smoothCamPartialTicks;
	private float debugCamFOV = 0.0F;
	private float prevDebugCamFOV = 0.0F;
	private float camRoll = 0.0F;
	private float prevCamRoll = 0.0F;

	/**
	 * The texture id of the blocklight/skylight texture used for lighting effects
	 */
	public int lightmapTexture;

	/**
	 * Colors computed in updateLightmap() and loaded into the lightmap emptyTexture
	 */
	private int[] lightmapColors;

	/** FOV modifier hand */
	private float fovModifierHand;

	/** FOV modifier hand prev */
	private float fovModifierHandPrev;

	/** FOV multiplier temp */
	private float fovMultiplierTemp;
	private float field_82831_U;
	private float field_82832_V;

	/** Cloud fog mode */
	private boolean cloudFog = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw = 0.0D;
	private double cameraPitch = 0.0D;

	/** Previous frame time in milliseconds */
	private long prevFrameTime = Minecraft.getSystemTime();

	/** End time of last render (ns) */
	private long renderEndNanoTime = 0L;

	/**
	 * Is set, updateCameraAndRender() calls updateLightmap(); set by
	 * updateTorchFlicker()
	 */
	private boolean lightmapUpdateNeeded = false;

	/** Torch flicker X */
	float torchFlickerX = 0.0F;

	/** Torch flicker DX */
	float torchFlickerDX = 0.0F;

	/** Torch flicker Y */
	float torchFlickerY = 0.0F;

	/** Torch flicker DY */
	float torchFlickerDY = 0.0F;
	private EaglercraftRandom random = new EaglercraftRandom();

	/** Rain sound counter */
	private int rainSoundCounter = 0;

	/** Rain X coords */
	float[] rainXCoords;

	/** Rain Y coords */
	float[] rainYCoords;
	volatile int field_78523_k = 0;
	volatile int field_78520_l = 0;

	/** Fog color buffer */
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);

	/** red component of the fog color */
	float fogColorRed;

	/** green component of the fog color */
	float fogColorGreen;

	/** blue component of the fog color */
	float fogColorBlue;

	/** Fog color 2 */
	private float fogColor2;

	/** Fog color 1 */
	private float fogColor1;

	/**
	 * Debug view direction (0=OFF, 1=Front, 2=Right, 3=Back, 4=Left, 5=TiltLeft,
	 * 6=TiltRight)
	 */
	public int debugViewDirection;

	public int startup = 0;
	public int preStartup = 0;

	public EntityRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.itemRenderer = new ItemRenderer(par1Minecraft);
		this.lightmapTexture = par1Minecraft.renderEngine.allocateAndSetupTexture(new EaglerImage(16, 16, true));
		this.lightmapColors = new int[256];
	}

	/**
	 * Updates the entity renderer
	 */
	public void updateRenderer() {
		this.updateFovModifierHand();
		this.updateTorchFlicker();
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistanceTemp = this.thirdPersonDistance;
		this.prevDebugCamYaw = this.debugCamYaw;
		this.prevDebugCamPitch = this.debugCamPitch;
		this.prevDebugCamFOV = this.debugCamFOV;
		this.prevCamRoll = this.camRoll;
		float var1;
		float var2;

		if (this.mc.gameSettings.smoothCamera) {
			var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			var2 = var1 * var1 * var1 * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
			this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		}

		if (this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		var1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
		var2 = (float) (3 - this.mc.gameSettings.renderDistance) / 3.0F;
		float var3 = var1 * (1.0F - var2) + var2;
		this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
		this.field_82832_V = this.field_82831_U;

		if (BossStatus.field_82825_d) {
			this.field_82831_U += 0.05F;

			if (this.field_82831_U > 1.0F) {
				this.field_82831_U = 1.0F;
			}

			BossStatus.field_82825_d = false;
		} else if (this.field_82831_U > 0.0F) {
			this.field_82831_U -= 0.0125F;
		}
	}

	/**
	 * Finds what block or object the mouse is over at the specified partial tick
	 * time. Args: partialTickTime
	 */
	public void getMouseOver(float par1) {
		if (this.mc.renderViewEntity != null) {
			if (this.mc.theWorld != null) {
				this.mc.pointedEntityLiving = null;
				double var2 = (double) this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(var2, par1);
				double var4 = var2;
				Vec3 var6 = this.mc.renderViewEntity.getPosition(par1);

				if (this.mc.playerController.extendedReach()) {
					var2 = 6.0D;
					var4 = 6.0D;
				} else {
					if (var2 > 3.0D) {
						var4 = 3.0D;
					}

					var2 = var4;
				}

				if (this.mc.objectMouseOver != null) {
					var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
				}

				Vec3 var7 = this.mc.renderViewEntity.getLook(par1);
				Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
				this.pointedEntity = null;
				float var9 = 1.0F;
				List var10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity,
						this.mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand((double) var9, (double) var9, (double) var9));
				double var11 = var4;

				for (int var13 = 0; var13 < var10.size(); ++var13) {
					Entity var14 = (Entity) var10.get(var13);

					if (var14.canBeCollidedWith()) {
						float var15 = var14.getCollisionBorderSize();
						AxisAlignedBB var16 = var14.boundingBox.expand((double) var15, (double) var15, (double) var15);
						MovingObjectPosition var17 = var16.calculateIntercept(var6, var8);

						if (var16.isVecInside(var6)) {
							if (0.0D < var11 || var11 == 0.0D) {
								this.pointedEntity = var14;
								var11 = 0.0D;
							}
						} else if (var17 != null) {
							double var18 = var6.distanceTo(var17.hitVec);

							if (var18 < var11 || var11 == 0.0D) {
								this.pointedEntity = var14;
								var11 = var18;
							}
						}
					}
				}

				if (this.pointedEntity != null && (var11 < var4 || this.mc.objectMouseOver == null)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);

					if (this.pointedEntity instanceof EntityLiving) {
						this.mc.pointedEntityLiving = (EntityLiving) this.pointedEntity;
					}
				}
			}
		}
	}

	/**
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		EntityPlayerSP var1 = (EntityPlayerSP) this.mc.renderViewEntity;
		this.fovMultiplierTemp = var1.getFOVMultiplier();
		this.fovModifierHandPrev = this.fovModifierHand;
		this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;

		if (this.fovModifierHand > 1.5F) {
			this.fovModifierHand = 1.5F;
		}

		if (this.fovModifierHand < 0.1F) {
			this.fovModifierHand = 0.1F;
		}
	}

	/**
	 * Changes the field of view of the player depending on if they are underwater
	 * or not
	 */
	private float getFOVModifier(float par1, boolean par2) {
		if(this.mc.gameSettings.keyBindZoom.pressed) {
			return 12.0F;
		} else if (this.debugViewDirection > 0) {
			return 90.0F;
		} else {
			EntityPlayer var3 = (EntityPlayer) this.mc.renderViewEntity;
			float var4 = 70.0F;

			if (par2) {
				var4 += this.mc.gameSettings.fovSetting * 40.0F;
				var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
			}

			if (var3.getHealth() <= 0) {
				float var5 = (float) var3.deathTime + par1;
				var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
			}

			int var6 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);

			if (var6 != 0 && Block.blocksList[var6].blockMaterial == Material.water) {
				var4 = var4 * 60.0F / 70.0F;
			}

			return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
		}
	}

	private void hurtCameraEffect(float par1) {
		EntityLiving var2 = this.mc.renderViewEntity;
		float var3 = (float) var2.hurtTime - par1;
		float var4;

		if (var2.getHealth() <= 0) {
			var4 = (float) var2.deathTime + par1;
			EaglerAdapter.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if (var3 >= 0.0F) {
			var3 /= (float) var2.maxHurtTime;
			var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float) Math.PI);
			var4 = var2.attackedAtYaw;
			EaglerAdapter.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(var4, 0.0F, 1.0F, 0.0F);
		}
	}

	/**
	 * Setups all the GL settings for view bobbing. Args: partialTickTime
	 */
	private void setupViewBobbing(float par1) {
		if (this.mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer) this.mc.renderViewEntity;
			float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
			float var4 = -(var2.distanceWalkedModified + var3 * par1);
			float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * par1;
			float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * par1;
			EaglerAdapter.glTranslatef(MathHelper.sin(var4 * (float) Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float) Math.PI) * var5), 0.0F);
			EaglerAdapter.glRotatef(MathHelper.sin(var4 * (float) Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(Math.abs(MathHelper.cos(var4 * (float) Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(var6, 1.0F, 0.0F, 0.0F);
		}
	}

	/**
	 * sets up player's eye (or camera in third person mode)
	 */
	private void orientCamera(float par1) {
		EntityLiving var2 = this.mc.renderViewEntity;
		float var3 = var2.yOffset - 1.62F;
		double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) par1;
		double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) par1 - (double) var3;
		double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) par1;
		EaglerAdapter.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);

		if (var2.isPlayerSleeping()) {
			var3 = (float) ((double) var3 + 1.0D);
			EaglerAdapter.glTranslatef(0.0F, 0.3F, 0.0F);

			if (!this.mc.gameSettings.debugCamEnable) {
				int var10 = this.mc.theWorld.getBlockId(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));

				if (var10 == Block.bed.blockID) {
					int var11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
					int var12 = var11 & 3;
					EaglerAdapter.glRotatef((float) (var12 * 90), 0.0F, 1.0F, 0.0F);
				}

				EaglerAdapter.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, -1.0F, 0.0F);
				EaglerAdapter.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, -1.0F, 0.0F, 0.0F);
			}
		} else if (this.mc.gameSettings.thirdPersonView > 0) {
			double var27 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * par1);
			float var13;
			float var28;

			if (this.mc.gameSettings.debugCamEnable) {
				var28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * par1;
				var13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * par1;
				EaglerAdapter.glTranslatef(0.0F, 0.0F, (float) (-var27));
				EaglerAdapter.glRotatef(var13, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(var28, 0.0F, 1.0F, 0.0F);
			} else {
				var28 = var2.rotationYaw;
				var13 = var2.rotationPitch;

				if (this.mc.gameSettings.thirdPersonView == 2) {
					var13 += 180.0F;
				}

				double var14 = (double) (-MathHelper.sin(var28 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI)) * var27;
				double var16 = (double) (MathHelper.cos(var28 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI)) * var27;
				double var18 = (double) (-MathHelper.sin(var13 / 180.0F * (float) Math.PI)) * var27;

				for (int var20 = 0; var20 < 8; ++var20) {
					float var21 = (float) ((var20 & 1) * 2 - 1);
					float var22 = (float) ((var20 >> 1 & 1) * 2 - 1);
					float var23 = (float) ((var20 >> 2 & 1) * 2 - 1);
					var21 *= 0.1F;
					var22 *= 0.1F;
					var23 *= 0.1F;
					MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4 + (double) var21, var6 + (double) var22, var8 + (double) var23),
							this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4 - var14 + (double) var21 + (double) var23, var6 - var18 + (double) var22, var8 - var16 + (double) var23));

					if (var24 != null) {
						double var25 = var24.hitVec.distanceTo(this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4, var6, var8));

						if (var25 < var27) {
							var27 = var25;
						}
					}
				}

				if (this.mc.gameSettings.thirdPersonView == 2) {
					EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				EaglerAdapter.glRotatef(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(var2.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glTranslatef(0.0F, 0.0F, (float) (-var27));
				EaglerAdapter.glRotatef(var28 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glRotatef(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			EaglerAdapter.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if (!this.mc.gameSettings.debugCamEnable) {
			EaglerAdapter.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, 1.0F, 0.0F);
		}

		EaglerAdapter.glTranslatef(0.0F, var3, 0.0F);
		var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) par1;
		var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) par1 - (double) var3;
		var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) par1;
		this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, par1);
	}

	private final Matrix4f tmpMatrix = new Matrix4f();
	
	/**
	 * sets up projection, view effects, camera position/rotation
	 */
	private void setupCameraTransform(float par1, int par2) {
		this.farPlaneDistance = (float) (256 >> this.mc.gameSettings.renderDistance);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		float var3 = 0.07F;

		if (this.mc.gameSettings.anaglyph) {
			EaglerAdapter.glTranslatef((float) (-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
		}

		if (this.cameraZoom != 1.0D) {
			EaglerAdapter.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
			EaglerAdapter.glScalef((float)this.cameraZoom, (float)this.cameraZoom, 1.0F);
		}
		
		float i = startup / 500.0f - 0.4f;
		if(i > 1.0f) i = 1.0f;
		if(i < 0.0f) i = 0.0f;
		float i2 = i * i;
		if(i2 > 0.0f) {
			float f = (float)((System.currentTimeMillis() % 10000000l) * 0.0002);
			f += MathHelper.sin(f * 5.0f) * 0.2f;
			i2 *= MathHelper.sin(f) + MathHelper.sin(f * 1.5f + 0.6f) + MathHelper.sin(f * 0.7f + 1.7f) +
					MathHelper.sin(f * 3.0f + 3.0f) + MathHelper.sin(f * 5.25f + 1.2f);
		}
		EaglerAdapter.gluPerspective(this.getFOVModifier(par1, true) * (1.0f + i2 * 0.007f), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		float var4;

		if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
			var4 = 0.6666667F;
			EaglerAdapter.glScalef(1.0F, var4, 1.0F);
		}

		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();

		if (this.mc.gameSettings.anaglyph) {
			EaglerAdapter.glTranslatef((float) (par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(par1);

		if (this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(par1);
		}

		i2 = i * i;
		if(i > 0.0f) {
			
			float f = (float)((System.currentTimeMillis() % 10000000l) * 0.00012);
			f += MathHelper.sin(f * 3.0f) * 0.2f;
			i2 *= MathHelper.sin(f * 1.2f + 1.0f) + MathHelper.sin(f * 1.5f + 0.8f) * 3.0f + MathHelper.sin(f * 0.6f + 3.0f) +
					MathHelper.sin(f * 4.3f) + MathHelper.sin(f * 5.25f + 0.5f);
			EaglerAdapter.glRotatef(i2 * 1.3f, 0.0f, 0.0f, 1.0f);
			
			tmpMatrix.setIdentity();
			
			f *= 2.5f;
			f += MathHelper.sin(f * 3.0f + 1.0f) * 0.2f;
			f *= 1.3f;
			f += 3.3413f;
			i2 = MathHelper.sin(f * 1.5f + 0.7f) + MathHelper.sin(f * 0.6f + 1.7f) +
					MathHelper.sin(f * 7.0f + 3.0f) * 0.3f;
			i2 *= i2;
			tmpMatrix.m10 = i2 * 0.02f;
			tmpMatrix.m30 = i2 * 0.028f;
			
			EaglerAdapter.glMultMatrixf(tmpMatrix); // shear the fuck out of the matrix
		}

		var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

		if (var4 > 0.0F) {
			byte var5 = 20;

			if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
				var5 = 7;
			}

			float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
			var6 *= var6;
			EaglerAdapter.glRotatef(((float) this.rendererUpdateCount + par1) * (float) var5, 0.0F, 1.0F, 1.0F);
			EaglerAdapter.glScalef(1.0F / var6, 1.0F, 1.0F);
			EaglerAdapter.glRotatef(-((float) this.rendererUpdateCount + par1) * (float) var5, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(par1);

		if (this.debugViewDirection > 0) {
			int var7 = this.debugViewDirection - 1;

			if (var7 == 1) {
				EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 2) {
				EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 3) {
				EaglerAdapter.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 4) {
				EaglerAdapter.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (var7 == 5) {
				EaglerAdapter.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	/**
	 * Render player hand
	 */
	private void renderHand(float par1, int par2) {
		if (this.debugViewDirection <= 0) {
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
			EaglerAdapter.glLoadIdentity();
			float var3 = 0.07F;
			
			float i = startup / 500.0f - 0.4f;
			if(i > 1.0f) i = 1.0f;
			if(i < 0.0f) i = 0.0f;
			float i2 = i * i;
			if(i2 > 0.0f) {
				float f = (float)((System.currentTimeMillis() % 10000000l) * 0.0003);
				f += MathHelper.sin(f * 3.0f) * 0.2f;
				i2 *= MathHelper.sin(f * 1.2f + 1.0f) + MathHelper.sin(f * 1.5f + 0.8f) * 3.0f + MathHelper.sin(f * 0.6f + 3.0f) +
						MathHelper.sin(f * 4.3f) + MathHelper.sin(f * 5.25f + 0.5f);
				i2 *= i2;
				EaglerAdapter.gluPerspectiveFlat(this.getFOVModifier(par1, false) + i2 * 0.3f, (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, 10f);
				f += 3.132123f;
				float j = MathHelper.sin(f * 1.3f + 1.1f) + MathHelper.sin(f * 1.3f + 0.8f) * 3.0f + MathHelper.sin(f * 0.5f + 2.0f);
				i2 = j * 0.5f + i2 * 0.2f;
				EaglerAdapter.glRotatef(i2 * 0.8f, 0.0f, 0.0f, 1.0f);
				
				f += 1.123123f;
				j = MathHelper.sin(f * 1.3f + 1.1f) + MathHelper.sin(f * 1.3f + 0.8f) * 3.0f + MathHelper.sin(f * 0.5f + 2.0f);
				i2 = j * 0.5f + i2 * 0.2f;
				EaglerAdapter.glRotatef(i2 * 0.5f, 1.0f, 0.0f, 0.0f);
			}else {
				EaglerAdapter.gluPerspectiveFlat(this.getFOVModifier(par1, false), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, 10f);
			}

			if (this.mc.gameSettings.anaglyph) {
				EaglerAdapter.glTranslatef((float) (-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
			}

			if (this.cameraZoom != 1.0D) {
				EaglerAdapter.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
				EaglerAdapter.glScalef((float)this.cameraZoom, (float)this.cameraZoom, 1.0F);
			}

			if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
				float var4 = 0.6666667F;
				EaglerAdapter.glScalef(1.0F, var4, 1.0F);
			}

			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
			EaglerAdapter.glLoadIdentity();

			if (this.mc.gameSettings.anaglyph) {
				EaglerAdapter.glTranslatef((float) (par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			EaglerAdapter.glPushMatrix();
			this.hurtCameraEffect(par1);

			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}

			if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI && !this.mc.playerController.enableEverythingIsScrewedUpMode()) {
				this.enableLightmap((double) par1);
				this.itemRenderer.renderItemInFirstPerson(par1);
				this.disableLightmap((double) par1);
			}

			EaglerAdapter.glPopMatrix();

			if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping()) {
				this.itemRenderer.renderOverlays(par1);
				this.hurtCameraEffect(par1);
			}

			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}
		}
	}

	/**
	 * Disable secondary texture unit used by lightmap
	 */
	public void disableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Enable lightmap in secondary texture unit
	 */
	public void enableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		//EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
		//EaglerAdapter.glLoadIdentity();
		//float var3 = 0.00390625F;
		//EaglerAdapter.glScalef(var3, var3, var3);
		//EaglerAdapter.glTranslatef(8.0F, 8.0F, 8.0F);
		//EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, this.lightmapTexture);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_LINEAR);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_LINEAR);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_S, EaglerAdapter.GL_CLAMP);
		EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_WRAP_T, EaglerAdapter.GL_CLAMP);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		this.mc.renderEngine.resetBoundTexture();
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Recompute a random value that is applied to block color in updateLightmap()
	 */
	private void updateTorchFlicker() {
		float i = startup / 600.0f;
		if(i > 1.0f) i = 1.0f;
		i = i * i;
		i = 0.8f + i * 0.04f;
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDY = (float) ((double) this.torchFlickerDY + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX * 0.9D);
		this.torchFlickerDY = (float) ((double) this.torchFlickerDY * 0.9D);
		this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * i;
		this.torchFlickerY += (this.torchFlickerDY - this.torchFlickerY) * i;
		this.lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float par1) {
		WorldClient var2 = this.mc.theWorld;

		if (var2 != null) {
			float i = startup / 600.0f;
			if(i > 1.0f) i = 1.0f;
			i = i * i * 1.25f;
			for (int var3 = 0; var3 < 256; ++var3) {
				float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
				float var5 = var2.provider.lightBrightnessTable[var3 / 16] * var4;
				float var6 = var2.provider.lightBrightnessTable[var3 % 16] * (this.torchFlickerX * 0.15F + 1.45F) * (1.0f + i);

				if (var2.lastLightningBolt > 0) {
					var5 = var2.provider.lightBrightnessTable[var3 / 16];
				}

				float var7 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
				float var8 = var7;
				float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
				float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
				float var13 = var7 + var6;
				float var14 = var8 + var11;
				float var15 = var5 + var12;
				var13 = var13 * 0.96F + 0.03F;
				var14 = var14 * 0.96F + 0.03F;
				var15 = var15 * 0.96F + 0.03F;
				float var16;

				if (this.field_82831_U > 0.0F) {
					var16 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * par1;
					var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
					var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
					var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
				}

				if (var2.provider.dimensionId == 1) {
					var13 = 0.22F + var6 * 0.75F;
					var14 = 0.28F + var11 * 0.75F;
					var15 = 0.25F + var12 * 0.75F;
				}

				float var17;

				if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
					var16 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
					var17 = 1.0F / var13;

					if (var17 > 1.0F / var14) {
						var17 = 1.0F / var14;
					}

					if (var17 > 1.0F / var15) {
						var17 = 1.0F / var15;
					}

					var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
					var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
					var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
				}

				if (var13 > 1.0F) {
					var13 = 1.0F;
				}

				if (var14 > 1.0F) {
					var14 = 1.0F;
				}

				if (var15 > 1.0F) {
					var15 = 1.0F;
				}

				var16 = this.mc.gameSettings.gammaSetting + this.torchFlickerX * i * 0.4f;
				var17 = 1.0F - var13;
				float var18 = 1.0F - var14;
				float var19 = 1.0F - var15;
				var17 = 1.0F - var17 * var17 * var17 * var17;
				var18 = 1.0F - var18 * var18 * var18 * var18;
				var19 = 1.0F - var19 * var19 * var19 * var19;
				var13 = var13 * (1.0F - var16) + var17 * var16;
				var14 = var14 * (1.0F - var16) + var18 * var16;
				var15 = var15 * (1.0F - var16) + var19 * var16;
				var13 = var13 * 0.96F + 0.03F;
				var14 = var14 * 0.96F + 0.03F;
				var15 = var15 * 0.96F + 0.03F;

				if (var13 > 1.0F) {
					var13 = 1.0F;
				}

				if (var14 > 1.0F) {
					var14 = 1.0F;
				}

				if (var15 > 1.0F) {
					var15 = 1.0F;
				}

				if (var13 < 0.0F) {
					var13 = 0.0F;
				}

				if (var14 < 0.0F) {
					var14 = 0.0F;
				}

				if (var15 < 0.0F) {
					var15 = 0.0F;
				}

				int var20 = 255;
				int var21 = (int) (var13 * 255.0F);
				int var22 = (int) (var14 * 255.0F);
				int var23 = (int) (var15 * 255.0F);
				this.lightmapColors[var3] = var20 << 24 | var21 << 16 | var22 << 8 | var23;
			}

			this.mc.renderEngine.createTextureFromBytes(this.lightmapColors, 16, 16, this.lightmapTexture);
		}
	}

	/**
	 * Gets the night vision brightness
	 */
	private float getNightVisionBrightness(EntityPlayer par1EntityPlayer, float par2) {
		int var3 = par1EntityPlayer.getActivePotionEffect(Potion.nightVision).getDuration();
		return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin(((float) var3 - par2) * (float) Math.PI * 0.2F) * 0.3F;
	}

	/**
	 * Will update any inputs that effect the camera angle (mouse) and then render
	 * the world and GUI
	 */
	public void updateCameraAndRender(float par1) {
		this.mc.mcProfiler.startSection("lightTex");

		if (this.lightmapUpdateNeeded) {
			this.updateLightmap(par1);
		}

		this.mc.mcProfiler.endSection();
		boolean var2 = EaglerAdapter.isFocused();

		if (!var2 && this.mc.gameSettings.pauseOnLostFocus) {
			this.mc.displayInGameMenu();
		} else {
			this.prevFrameTime = Minecraft.getSystemTime();
		}

		this.mc.mcProfiler.startSection("mouse");

		if (this.mc.inGameHasFocus && var2) {
			this.mc.mouseHelper.mouseXYChange();
			float var3 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			if(this.mc.gameSettings.keyBindZoom.pressed) var3 *= 0.5f;
			float var4 = var3 * var3 * var3 * 8.0F;
			float var5 = (float) this.mc.mouseHelper.deltaX * var4;
			float var6 = (float) this.mc.mouseHelper.deltaY * var4;
			byte var7 = 1;

			if (this.mc.gameSettings.invertMouse) {
				var7 = -1;
			}

			if (this.mc.gameSettings.smoothCamera) {
				this.smoothCamYaw += var5;
				this.smoothCamPitch += var6;
				float var8 = par1 - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = par1;
				var5 = this.smoothCamFilterX * var8;
				var6 = this.smoothCamFilterY * var8;
				this.mc.thePlayer.setAngles(var5, var6 * (float) var7);
			} else {
				this.mc.thePlayer.setAngles(var5, var6 * (float) var7);
			}
		}

		this.mc.mcProfiler.endSection();

		if (!this.mc.skipRenderWorld) {
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			ScaledResolution var13 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int var14 = var13.getScaledWidth();
			int var15 = var13.getScaledHeight();
			int var16 = EaglerAdapter.mouseGetX() * var14 / this.mc.displayWidth;
			int var17 = var15 - EaglerAdapter.mouseGetY() * var15 / this.mc.displayHeight - 1;
			int var18 = performanceToFps(this.mc.gameSettings.limitFramerate);

			if (this.mc.theWorld != null) {
				this.mc.mcProfiler.startSection("level");

				if (this.mc.gameSettings.limitFramerate == 0) {
					this.renderWorld(par1, 0L);
				} else {
					this.renderWorld(par1, this.renderEndNanoTime + (long) (1000000000 / var18));
				}
				
				float i = startup / 2400.0f;
				if(i > 1.0f) i = 1.0f;
				i = i * i;

				if(i > 0.15f) {
					EffectPipeline.updateNoiseTexture(mc.displayWidth, mc.displayHeight, i);
					EffectPipeline.drawNoise(var14, var15, (i - 0.15f) / 0.85f);
				}

				this.renderEndNanoTime = System.nanoTime();
				this.mc.mcProfiler.endStartSection("gui");

				if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
					this.mc.ingameGUI.renderGameOverlay(par1, this.mc.currentScreen != null, var16, var17);
				}

				this.mc.mcProfiler.endSection();
			} else {
				EaglerAdapter.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
				EaglerAdapter.glLoadIdentity();
				EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				EaglerAdapter.glLoadIdentity();
				this.setupOverlayRendering();
				this.renderEndNanoTime = System.nanoTime();
			}
			
			EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);

			if (this.mc.currentScreen != null) {
				this.mc.currentScreen.drawScreen(var16, var17, par1);

				if (this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
					this.mc.currentScreen.guiParticles.draw(par1);
				}
			}
			
			mc.voiceOverlay.drawOverlay();
		}
		
	}
	
	public static final TextureLocation terrain = new TextureLocation("/terrain.png");

	public void renderWorld(float par1, long par2) {
		this.mc.mcProfiler.startSection("lightTex");

		if (this.lightmapUpdateNeeded) {
			this.updateLightmap(par1);
		}

		EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);

		if (this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		this.mc.mcProfiler.endStartSection("pick");
		this.getMouseOver(par1);
		EntityLiving var4 = this.mc.renderViewEntity;
		RenderGlobal var5 = this.mc.renderGlobal;
		EffectRenderer var6 = this.mc.effectRenderer;
		double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) par1;
		double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) par1;
		double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) par1;
		this.mc.mcProfiler.endStartSection("center");
		
		EffectPipelineFXAA.displayWidth = this.mc.displayWidth;
		EffectPipelineFXAA.displayHeight = this.mc.displayHeight;
		EffectPipelineFXAA.beginPipelineRender();
		
		RenderManager.instance.voiceTagsDrawnThisFrame.clear();
		
		for (int var13 = 0; var13 < 2; ++var13) {
			if (this.mc.gameSettings.anaglyph) {
				anaglyphField = var13;

				if (anaglyphField == 0) {
					EaglerAdapter.glColorMask(false, true, true, false);
				} else {
					EaglerAdapter.glColorMask(true, false, false, false);
				}
			}

			EaglerAdapter.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.mc.mcProfiler.endStartSection("clear");
			EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			this.updateFogColor(par1);
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			this.mc.mcProfiler.endStartSection("camera");
			this.setupCameraTransform(par1, var13);
			ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
			this.mc.mcProfiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();
			
			EaglerAdapter.glEnable(EaglerAdapter.GL_FOG);

			if (this.mc.gameSettings.renderDistance < 2) {
				this.setupFog(-1, par1);
				this.mc.mcProfiler.endStartSection("sky");
				var5.renderSky(par1);
			}
			
			this.setupFog(1, par1);

			if (this.mc.gameSettings.ambientOcclusion != 0) {
				EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
			}

			this.mc.mcProfiler.endStartSection("culling");
			Frustrum var14 = new Frustrum();
			var14.setPosition(var7, var9, var11);
			this.mc.renderGlobal.clipRenderersByFrustum(var14, par1);

			if (var13 == 0) {
				this.mc.mcProfiler.endStartSection("updatechunks");

				while (!this.mc.renderGlobal.updateRenderers(var4, false) && par2 != 0L) {
					long var15 = par2 - System.nanoTime();

					if (var15 < 0L || var15 > 1000000000L) {
						break;
					}
				}
			}

			if (var4.posY < 128.0D) {
				this.renderCloudsCheck(var5, par1);
			}

			this.mc.mcProfiler.endStartSection("prepareterrain");
			EaglerAdapter.glEnable(EaglerAdapter.GL_FOG);
			this.setupFog(0, par1);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			RenderHelper.disableStandardItemLighting();
			terrain.bindTexture();
			if(EaglerAdapter.isKeyDown(34)) {
				EaglerAdapter.glTexParameterf(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_ANISOTROPY, 1.0f);
				EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST);
			}else {
				EaglerAdapter.glTexParameterf(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_ANISOTROPY, 16.0f);
				EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST_MIPMAP_LINEAR);
			}
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.6f);
			this.mc.mcProfiler.endStartSection("terrain");
			var5.sortAndRender(var4, 0, (double) par1);
			EaglerAdapter.glShadeModel(EaglerAdapter.GL_FLAT);
			EntityPlayer var17;

			if (this.debugViewDirection == 0) {
				RenderHelper.enableStandardItemLighting();
				this.mc.mcProfiler.endStartSection("entities");
				var5.renderEntities(var4.getPosition(par1), var14, par1);
				this.enableLightmap((double) par1);
				this.mc.mcProfiler.endStartSection("litParticles");
				var6.renderLitParticles(var4, par1);
				RenderHelper.disableStandardItemLighting();
				this.setupFog(0, par1);
				this.mc.mcProfiler.endStartSection("particles");
				var6.renderParticles(var4, par1);
				this.disableLightmap((double) par1);

				if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI) {
					var17 = (EntityPlayer) var4;
					EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
					this.mc.mcProfiler.endStartSection("outline");
					var5.drawBlockBreaking(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
					var5.drawSelectionBox(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
					EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
				}
			}
			
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDepthMask(true);
			this.setupFog(0, par1);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.03f);
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glColor4f(1f, 1f, 1f, 1f);
			terrain.bindTexture();
			
			//if (this.mc.gameSettings.fancyGraphics) {
				this.mc.mcProfiler.endStartSection("water");

				EaglerAdapter.glColorMask(false, false, false, false);
				int var18 = var5.sortAndRender(var4, 1, (double) par1);

				if (this.mc.gameSettings.anaglyph) {
					if (anaglyphField == 0) {
						EaglerAdapter.glColorMask(false, true, true, true);
					} else {
						EaglerAdapter.glColorMask(true, false, false, true);
					}
				} else {
					EaglerAdapter.glColorMask(true, true, true, true);
				}
				
				if (var18 > 0) {
					EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
					var5.renderSortedRenderers(0, var5.sortedWorldRenderers.length, 1, (double) par1);
					EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
				}
				
			//} else {
			//	this.mc.mcProfiler.endStartSection("water");
			//	var5.sortAndRender(var4, 1, (double) par1);
			//}
			
			EaglerAdapter.glTexParameterf(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAX_ANISOTROPY, 1.0f);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_NEAREST_MIPMAP_LINEAR);
			
			var6.renderTransparentParticles(var4, par1);

			EaglerAdapter.glDepthMask(true);
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);

			if (this.cameraZoom == 1.0D && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
				var17 = (EntityPlayer) var4;
				EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
				this.mc.mcProfiler.endStartSection("outline");
				var5.drawBlockBreaking(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
				var5.drawSelectionBox(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
				EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			}

			this.mc.mcProfiler.endStartSection("destroyProgress");
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE);
			var5.drawBlockDamageTexture(Tessellator.instance, (EntityPlayer) var4, par1);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			this.mc.mcProfiler.endStartSection("weather");
			EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);

			if (var4.posY >= 128.0D) {
				this.renderCloudsCheck(var5, par1);
			}
			
			this.renderRainSnow(par1);

			this.mc.mcProfiler.endStartSection("hand");
			
			//EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			
			if (!this.mc.gameSettings.keyBindZoom.pressed) {
				this.renderHand(par1, var13);
			}
			

			if (!this.mc.gameSettings.anaglyph) {
				break;
			}
		}

		this.mc.mcProfiler.endStartSection("postprocess");
		EaglerAdapter.glColorMask(true, true, true, false);
		EffectPipelineFXAA.endPipelineRender();
		this.mc.mcProfiler.endSection();
	}

	/**
	 * Render clouds if enabled
	 */
	private void renderCloudsCheck(RenderGlobal par1RenderGlobal, float par2) {
		if (this.mc.gameSettings.shouldRenderClouds()) {
			this.mc.mcProfiler.endStartSection("clouds");
			EaglerAdapter.glPushMatrix();
			this.setupFog(0, par2);
			EaglerAdapter.glEnable(EaglerAdapter.GL_FOG);
			par1RenderGlobal.renderClouds(par2);
			EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
			this.setupFog(1, par2);
			EaglerAdapter.glPopMatrix();
		}
	}

	private int updateCounter = 0;
	private int randomOffset = (int)(System.currentTimeMillis() % 100000l);

	public boolean asdfghjkl = false;

	private void addRainParticles() {
		float var1 = this.mc.theWorld.getRainStrength(1.0F);

		if (!this.mc.gameSettings.fancyGraphics) {
			var1 /= 2.0F;
		}
		
		WorldClient var3 = this.mc.theWorld;
		EntityLiving var2 = this.mc.renderViewEntity;
		
		int var4 = MathHelper.floor_double(var2.posX);
		int var5 = MathHelper.floor_double(var2.posY);
		int var6 = MathHelper.floor_double(var2.posZ);
		
		if (var1 != 0.0F) {
			this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
			byte var7 = 10;
			double var8 = 0.0D;
			double var10 = 0.0D;
			double var12 = 0.0D;
			int var14 = 0;
			int var15 = (int) (100.0F * var1 * var1);

			if (this.mc.gameSettings.particleSetting == 1) {
				var15 >>= 1;
			} else if (this.mc.gameSettings.particleSetting == 2) {
				var15 = 0;
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				int var17 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
				int var18 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
				int var19 = var3.getPrecipitationHeight(var17, var18);
				int var20 = var3.getBlockId(var17, var19 - 1, var18);
				BiomeGenBase var21 = var3.getBiomeGenForCoords(var17, var18);

				if (var19 <= var5 + var7 && var19 >= var5 - var7 && var21.canSpawnLightningBolt() && var21.getFloatTemperature() >= 0.2F) {
					float var22 = this.random.nextFloat();
					float var23 = this.random.nextFloat();

					if (var20 > 0) {
						if (Block.blocksList[var20].blockMaterial == Material.lava) {
							this.mc.effectRenderer
									.addEffect(new EntitySmokeFX(var3, (double) ((float) var17 + var22), (double) ((float) var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY(), (double) ((float) var18 + var23), 0.0D, 0.0D, 0.0D));
						} else {
							++var14;

							if (this.random.nextInt(var14) == 0) {
								var8 = (double) ((float) var17 + var22);
								var10 = (double) ((float) var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY();
								var12 = (double) ((float) var18 + var23);
							}

							this.mc.effectRenderer.addEffect(new EntityRainFX(var3, (double) ((float) var17 + var22), (double) ((float) var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY(), (double) ((float) var18 + var23)));
						}
					}
				}
			}

			if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;

				if (var10 > var2.posY + 1.0D && var3.getPrecipitationHeight(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posZ)) > MathHelper.floor_double(var2.posY)) {
					this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.45F, 0.5F, false);
				} else {
					this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.7F, 1.0F, false);
				}
			}
		}
		
		if(mc.gameSettings.adderall || asdfghjkl) {
			if(startup == 0) {
				var3.ambientTickCountdown = random.nextInt(12000);
			}
			++preStartup;
			if(preStartup < 300) {
				return;
			}
			++startup;
			int k = 60 - (startup / 5);
			if(k < 10) k = 10;
			if(++updateCounter % k == 0) {
				long time = this.mc.theWorld.getWorldTime();
				int j = var2.getBrightnessForRender(0.0f);
				float intensity = Math.max(MathHelper.clamp_float((float)Math.abs(6000l - ((time % 24000l) - 12000l)), 0.15f, 1.0f),
						Math.max(1.0f - ((j / 65536) / 256.0f), 1.0f - ((j % 65536) / 256.0f)));
				int rad = (int)((1.0f - ((j / 65536) / 256.0f)) * 6.0f) - 2;
				if(rad < 0) rad = 0;
				int effect = (int)((time + randomOffset) / 7432l % 5l);
				int effect2 = (int)((time + randomOffset + 1290348l) / 4432l % 5l);
				if(effect == 4) rad = 8;
				int d = 1;
				if(effect == 4) d = 2;
				for(int i = 0; i < (int)(intensity * 10); ++i) {
					int x, y, z;
					int c = 0;
					int m = 20;
					do {
						x = random.nextInt(50) - 25; x /= d;
						y = random.nextInt(35) - 10; y /= d;
						z = random.nextInt(50) - 25; z /= d;
					}while((((x < 12 - rad && x > -12 + rad) && (y < 4 && y > -8 + (rad / 2)) && (z - rad < 12 && z > -12 + rad)) ||
							this.mc.theWorld.isBlockOpaqueCube(x + var4, y + var5, z + var6)) && ++c < m);
					if(c != m) {
						if(random.nextFloat() > 0.8f) {
							if(random.nextFloat() > 0.7f) {
								this.mc.effectRenderer.addEffect(new EntityLargeExplodeFX(mc.renderEngine, var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat(),
										random.nextFloat() * 0.6f + 0.8f, 0.0d, 0.0d));
							}
							float f3 = random.nextFloat() * 0.5f + 0.3f;
							f3 = (float)Math.pow(f3, 1.5);
							this.mc.effectRenderer.addEffect(new EntityCritFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat(),
									(random.nextFloat() * 100.0f - 50.0f) * f3, (random.nextFloat() * 100.0f - 65.0f) * f3, (random.nextFloat() * 100.0f - 50.0f) * f3,
									0.7f + random.nextFloat(), 7.0f));
						}else {
							switch(random.nextFloat() > 0.4f ? effect : effect2) {
							case 0:
							default:
								this.mc.effectRenderer.addEffect(new EntityFlameFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat(),
										random.nextFloat() * 0.1f - 0.05f, random.nextFloat() * 0.2f - 0.05f, random.nextFloat() * 0.1f - 0.05f));
								break;
							case 1:
								float f3 = random.nextFloat() * 0.3f + 0.2f;
								f3 = (float)Math.pow(f3, 1.5) * 0.5f;
								this.mc.effectRenderer.addEffect(new EntityCritFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat(),
										(random.nextFloat() * 100.0f - 50.0f) * f3, (random.nextFloat() * 100.0f - 30.0f) * f3, (random.nextFloat() * 100.0f - 50.0f) * f3,
										0.7f + random.nextFloat(), 7.0f));
								break;
							case 2:
								if(random.nextFloat() > 0.25f) {
									this.mc.effectRenderer.addEffect(new EntityFlameFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat(),
											random.nextFloat() * 0.1f - 0.05f, random.nextFloat() * 0.2f - 0.05f, random.nextFloat() * 0.1f - 0.05f));
								}else {
									this.mc.effectRenderer.addEffect(new EntityLavaFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(), z + var6 + random.nextFloat()));
								}
								break;
							case 3:
								if(!(x < 12 - rad && x > -12 + rad && z - rad < 12 && z > -12 + rad)) {
									this.mc.effectRenderer.addEffect(new EntityDropParticleFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(),
											z + var6 + random.nextFloat(), Material.lava));
									this.mc.effectRenderer.addEffect(new EntityDropParticleFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(),
											z + var6 + random.nextFloat(), Material.lava));
									if(random.nextBoolean()) this.mc.effectRenderer.addEffect(new EntityDropParticleFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(),
											z + var6 + random.nextFloat(), Material.lava));
									if(random.nextBoolean()) this.mc.effectRenderer.addEffect(new EntityDropParticleFX(var3, x + var4 + random.nextFloat(), y + var5 + random.nextFloat(),
											z + var6 + random.nextFloat(), Material.lava));
								}
								break;
							case 4:
								while(random.nextInt(5) != 0) {
									float mx = (random.nextFloat() - 0.5f) * 3.0f;
									float my = (random.nextFloat() - 0.5f) * 3.0f;
									float mz = (random.nextFloat() - 0.5f) * 3.0f;
									this.mc.effectRenderer.addEffect(new EntityPortalFX(var3, x + var4 + random.nextFloat() - mx * 0.25f, y + var5 + random.nextFloat() - my * 0.25f,
											z + var6 + random.nextFloat() - mz * 0.25f, random.nextFloat() * 0.1f - 0.05f - mx * (3.0f + random.nextFloat() * 0.5f),
											random.nextFloat() * 0.2f - 0.05f - my * (3.0f + random.nextFloat() * 0.5f), random.nextFloat() * 0.1f - 0.05f - mz * (3.0f + random.nextFloat() * 0.5f)));
								}
								break;
							}
						}
					}
					while(random.nextFloat() > 0.6f) {
						x = random.nextInt(16) - 8;
						z = random.nextInt(16) - 8;
						if(x > 1 || z > 1 || x < -1 || z < -1) {
							int yy = var3.getHeightValue(x + var4 , z + var6);
							int ds = yy - var5 - 1;
							if(ds < 0) {
								ds = -ds;
							}
							if(ds < 5) {
								int l = 0;
								while(l++ == 0 || random.nextFloat() > 0.4f) {
									this.mc.effectRenderer.addEffect(new EntityAuraFX(var3, x + var4 + random.nextFloat(), yy + 0.1f + random.nextFloat() * 0.1f, z + var6
											+ random.nextFloat(), 0.0d, 0.0d, 0.0d));
								}
							}
						}
					}
					while(random.nextFloat() > 0.97f) {
						x = random.nextInt(20) - 10;
						z = random.nextInt(20) - 10;
						if(x > 3 || z > 3 || x < -3 || z < -3) {
							int yy = var3.getHeightValue(x + var4 , z + var6);
							int ds = yy - var5 - 1;
							if(ds < 0) {
								ds = -ds;
							}
							if(ds < 8) {
								if(random.nextInt(3) == 0) {
									yy += 7;
									yy += random.nextInt(5);
								}
								if(random.nextInt(3) == 0) {
									Block l;
									do {
										l = Block.blocksList[random.nextInt(256)];
									}while(l == null || !(l.isOpaqueCube() || l.renderAsNormalBlock() || l.getRenderType() == 0 ||
											l.getRenderType() == 27 || l.getRenderType() == 35 || l == Block.portal));
									EntityFallingSand itm = new EntityFallingSand(var3, x + var4 + 0.5, yy + 0.5, z + var6 + 0.5, l.blockID, 0);
									itm.entityId = --var3.ghostEntityId;
									itm.ghost = true;
									var3.spawnEntityInWorld(itm);
								}else {
									Item l;
									do {
										l = Item.itemsList[random.nextInt(384)];
									}while(l == null);
									EntityItem itm = new EntityItem(var3, x + var4 + 0.5, yy + 1.0, z + var6 + 0.5, new ItemStack(l, 1));
									itm.entityId = --var3.ghostEntityId;
									itm.ghost = true;
									var3.spawnEntityInWorld(itm);
								}
							}
						}
					}
					float probability = MathHelper.sin(startup / 300.0f);
					while(random.nextFloat() < (probability * 0.3f + 0.7f) * 0.002f) {
						this.mc.sndManager.playSoundFX("adl.l", MathHelper.clamp_float(startup / 400.0f, 0.03f, 0.3f), random.nextFloat() * 0.2f + 0.9f);
					}
					while(random.nextFloat() < (probability * 0.3f + 0.7f) * 0.005f) {
						this.mc.sndManager.playSound("adl.a", (float) var2.posX - 4.0f + 8.0f * random.nextFloat(), (float) var2.posY - 2.0f + 4.0f * random.nextFloat(),
								(float) var2.posZ - 4.0f + 8.0f * random.nextFloat(), 0.35f, random.nextFloat() * 0.2f + 0.9f);
					}
				}
			}
		}
	}
	
	private static final TextureLocation rain = new TextureLocation("/environment/rain.png");
	private static final TextureLocation snow = new TextureLocation("/environment/snow.png");

	/**
	 * Render rain and snow
	 */
	protected void renderRainSnow(float par1) {
		float var2 = this.mc.theWorld.getRainStrength(par1) * 0.5f;

		if (var2 > 0.0F) {
			this.enableLightmap((double) par1);

			if (this.rainXCoords == null) {
				this.rainXCoords = new float[1024];
				this.rainYCoords = new float[1024];

				for (int var3 = 0; var3 < 32; ++var3) {
					for (int var4 = 0; var4 < 32; ++var4) {
						float var5 = (float) (var4 - 16);
						float var6 = (float) (var3 - 16);
						float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
						this.rainXCoords[var3 << 5 | var4] = -var6 / var7;
						this.rainYCoords[var3 << 5 | var4] = var5 / var7;
					}
				}
			}

			EntityLiving var41 = this.mc.renderViewEntity;
			WorldClient var42 = this.mc.theWorld;
			int var43 = MathHelper.floor_double(var41.posX);
			int var44 = MathHelper.floor_double(var41.posY);
			int var45 = MathHelper.floor_double(var41.posZ);
			Tessellator var8 = Tessellator.instance;
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glNormal3f(0.0F, 1.0F, 0.0F);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.01F);
			double var9 = var41.lastTickPosX + (var41.posX - var41.lastTickPosX) * (double) par1;
			double var11 = var41.lastTickPosY + (var41.posY - var41.lastTickPosY) * (double) par1;
			double var13 = var41.lastTickPosZ + (var41.posZ - var41.lastTickPosZ) * (double) par1;
			int var15 = MathHelper.floor_double(var11);
			byte var16 = 5;

			if (this.mc.gameSettings.fancyGraphics) {
				var16 = 10;
			}

			boolean var17 = false;
			byte var18 = -1;
			int ticks = (this.rendererUpdateCount % 100000);
			float var19 = ticks + par1;

			if (this.mc.gameSettings.fancyGraphics) {
				var16 = 10;
			}

			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var17 = false;

			for (int var20 = var45 - var16; var20 <= var45 + var16; ++var20) {
				for (int var21 = var43 - var16; var21 <= var43 + var16; ++var21) {
					int var22 = (var20 - var45 + 16) * 32 + var21 - var43 + 16;
					float var23 = this.rainXCoords[var22] * 0.5F;
					float var24 = this.rainYCoords[var22] * 0.5F;
					BiomeGenBase var25 = var42.getBiomeGenForCoords(var21, var20);

					if (var25.canSpawnLightningBolt() || var25.getEnableSnow()) {
						int var26 = var42.getPrecipitationHeight(var21, var20);
						int var27 = var44 - var16;
						int var28 = var44 + var16;

						if (var27 < var26) {
							var27 = var26;
						}

						if (var28 < var26) {
							var28 = var26;
						}

						float var29 = 1.0F;
						int var30 = var26;

						if (var26 < var15) {
							var30 = var15;
						}

						if (var27 != var28) {
							this.random.setSeed((long) (var21 * var21 * 3121 + var21 * 45238971 ^ var20 * var20 * 418711 + var20 * 13761));
							float var31 = var25.getFloatTemperature();
							float var32;
							double var35;

							if (var31 >= 0.15F) {
								if (var18 != 0) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 0;
									rain.bindTexture();
									var8.startDrawingQuads();
								}

								var32 = ((float) ((ticks + var21 * var21 * 3121 + var21 * 45238971 + var20 * var20 * 418711 + var20 * 13761 & 31) % 100000) + par1) / 32.0F * (3.0F + this.random.nextFloat());
								double var33 = (double) ((float) var21 + 0.5F) - var41.posX;
								var35 = (double) ((float) var20 + 0.5F) - var41.posZ;
								float var37 = MathHelper.sqrt_double(var33 * var33 + var35 * var35) / (float) var16;
								float var38 = 1.0F;
								var8.setBrightness(var42.getLightBrightnessForSkyBlocks(var21, var30, var20, 0));
								var8.setColorRGBA_F(var38, var38, var38, ((1.0F - var37 * var37) * 0.5F + 0.5F) * var2);
								var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var27, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29), (double) ((float) var27 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var27, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29), (double) ((float) var27 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29), (double) ((float) var28 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29), (double) ((float) var28 * var29 / 4.0F + var32 * var29));
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							} else {
								if (var18 != 1) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 1;
									snow.bindTexture();
									var8.startDrawingQuads();
								}

								var32 = (((float) (ticks % 512) + par1) / 512.0F);
								float var46 = this.random.nextFloat() + var19 * 0.01F * (float) this.random.nextGaussian();
								float var34 = this.random.nextFloat() + var19 * (float) this.random.nextGaussian() * 0.001F;
								var35 = (double) ((float) var21 + 0.5F) - var41.posX;
								double var47 = (double) ((float) var20 + 0.5F) - var41.posZ;
								float var39 = MathHelper.sqrt_double(var35 * var35 + var47 * var47) / (float) var16;
								float var40 = 1.0F;
								var8.setBrightness((var42.getLightBrightnessForSkyBlocks(var21, var30, var20, 0) * 3 + 15728880) / 4);
								var8.setColorRGBA_F(var40, var40, var40, ((1.0F - var39 * var39) * 0.3F + 0.5F) * var2);
								var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var27, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29 + var46), (double) ((float) var27 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var27, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29 + var46), (double) ((float) var27 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double) ((float) var21 + var23) + 0.5D, (double) var28, (double) ((float) var20 + var24) + 0.5D, (double) (1.0F * var29 + var46), (double) ((float) var28 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double) ((float) var21 - var23) + 0.5D, (double) var28, (double) ((float) var20 - var24) + 0.5D, (double) (0.0F * var29 + var46), (double) ((float) var28 * var29 / 4.0F + var32 * var29 + var34));
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							}
						}
					}
				}
			}

			if (var18 >= 0) {
				var8.draw();
			}

			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);
			this.disableLightmap((double) par1);
		}
	}

	/**
	 * Setup orthogonal projection for rendering GUI screen overlays
	 */
	public void setupOverlayRendering() {
		ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glOrtho(0.0F, var1.getScaledWidth(), var1.getScaledHeight(), 0.0F, 1000.0F, 3000.0F);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	/**
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float par1) {
		WorldClient var2 = this.mc.theWorld;
		EntityLiving var3 = this.mc.renderViewEntity;
		float var4 = 1.0F / (float) (4 - this.mc.gameSettings.renderDistance);
		var4 = 1.0F - (float) Math.pow((double) var4, 0.25D);
		Vec3 var5 = var2.getSkyColor(this.mc.renderViewEntity, par1);
		float var6 = (float) var5.xCoord;
		float var7 = (float) var5.yCoord;
		float var8 = (float) var5.zCoord;
		Vec3 var9 = var2.getFogColor(par1);
		this.fogColorRed = (float) var9.xCoord;
		this.fogColorGreen = (float) var9.yCoord;
		this.fogColorBlue = (float) var9.zCoord;
		float var11;

		if (this.mc.gameSettings.renderDistance < 2) {
			Vec3 var10 = MathHelper.sin(var2.getCelestialAngleRadians(par1)) > 0.0F ? var2.getWorldVec3Pool().getVecFromPool(-1.0D, 0.0D, 0.0D) : var2.getWorldVec3Pool().getVecFromPool(1.0D, 0.0D, 0.0D);
			var11 = (float) var3.getLook(par1).dotProduct(var10);

			if (var11 < 0.0F) {
				var11 = 0.0F;
			}

			if (var11 > 0.0F) {
				float[] var12 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(par1), par1);

				if (var12 != null) {
					var11 *= var12[3];
					this.fogColorRed = this.fogColorRed * (1.0F - var11) + var12[0] * var11;
					this.fogColorGreen = this.fogColorGreen * (1.0F - var11) + var12[1] * var11;
					this.fogColorBlue = this.fogColorBlue * (1.0F - var11) + var12[2] * var11;
				}
			}
		}

		this.fogColorRed += (var6 - this.fogColorRed) * var4;
		this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
		float var19 = var2.getRainStrength(par1);
		float var20;

		if (var19 > 0.0F) {
			var11 = 1.0F - var19 * 0.5F;
			var20 = 1.0F - var19 * 0.4F;
			this.fogColorRed *= var11;
			this.fogColorGreen *= var11;
			this.fogColorBlue *= var20;
		}

		var11 = var2.getWeightedThunderStrength(par1);

		if (var11 > 0.0F) {
			var20 = 1.0F - var11 * 0.5F;
			this.fogColorRed *= var20;
			this.fogColorGreen *= var20;
			this.fogColorBlue *= var20;
		}

		int var21 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);

		if (this.cloudFog) {
			Vec3 var13 = var2.getCloudColour(par1);
			this.fogColorRed = (float) var13.xCoord;
			this.fogColorGreen = (float) var13.yCoord;
			this.fogColorBlue = (float) var13.zCoord;
		} else if (var21 != 0 && Block.blocksList[var21].blockMaterial == Material.water) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
		} else if (var21 != 0 && Block.blocksList[var21].blockMaterial == Material.lava) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		float var22 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * par1;
		this.fogColorRed *= var22;
		this.fogColorGreen *= var22;
		this.fogColorBlue *= var22;
		double var14 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double) par1) * var2.provider.getVoidFogYFactor();

		if (var3.isPotionActive(Potion.blindness)) {
			int var16 = var3.getActivePotionEffect(Potion.blindness).getDuration();

			if (var16 < 20) {
				var14 *= (double) (1.0F - (float) var16 / 20.0F);
			} else {
				var14 = 0.0D;
			}
		}

		if (var14 < 1.0D) {
			if (var14 < 0.0D) {
				var14 = 0.0D;
			}

			var14 *= var14;
			this.fogColorRed = (float) ((double) this.fogColorRed * var14);
			this.fogColorGreen = (float) ((double) this.fogColorGreen * var14);
			this.fogColorBlue = (float) ((double) this.fogColorBlue * var14);
		}

		float var23;

		if (this.field_82831_U > 0.0F) {
			var23 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * par1;
			this.fogColorRed = this.fogColorRed * (1.0F - var23) + this.fogColorRed * 0.7F * var23;
			this.fogColorGreen = this.fogColorGreen * (1.0F - var23) + this.fogColorGreen * 0.6F * var23;
			this.fogColorBlue = this.fogColorBlue * (1.0F - var23) + this.fogColorBlue * 0.6F * var23;
		}

		float var17;

		if (var3.isPotionActive(Potion.nightVision)) {
			var23 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
			var17 = 1.0F / this.fogColorRed;

			if (var17 > 1.0F / this.fogColorGreen) {
				var17 = 1.0F / this.fogColorGreen;
			}

			if (var17 > 1.0F / this.fogColorBlue) {
				var17 = 1.0F / this.fogColorBlue;
			}

			this.fogColorRed = this.fogColorRed * (1.0F - var23) + this.fogColorRed * var17 * var23;
			this.fogColorGreen = this.fogColorGreen * (1.0F - var23) + this.fogColorGreen * var17 * var23;
			this.fogColorBlue = this.fogColorBlue * (1.0F - var23) + this.fogColorBlue * var17 * var23;
		}

		if (this.mc.gameSettings.anaglyph) {
			var23 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			var17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var18 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var23;
			this.fogColorGreen = var17;
			this.fogColorBlue = var18;
		}

		EaglerAdapter.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	/**
	 * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at
	 * 0 and goes to 80% of far plane distance and is used for sky rendering.
	 */
	private void setupFog(int par1, float par2) {
		EntityLiving var3 = this.mc.renderViewEntity;

		if (par1 == 999) {
			EaglerAdapter.glFog(EaglerAdapter.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_LINEAR);
			EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, 0.0F);
			EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, 8.0F);

			//if (EaglerAdapter.NVFogDistanceInstalled) {
			//	EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_DISTANCE_MODE, EaglerAdapter.GL_EYE_RADIAL);
			//}

			EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, 0.0F);
		} else {
			EaglerAdapter.glFog(EaglerAdapter.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
			EaglerAdapter.glNormal3f(0.0F, -1.0F, 0.0F);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int var5 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par2);
			float var6;

			if (var3.isPotionActive(Potion.blindness)) {
				var6 = 5.0F;
				int var7 = var3.getActivePotionEffect(Potion.blindness).getDuration();

				if (var7 < 20) {
					var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) var7 / 20.0F);
				}

				EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_LINEAR);

				if (par1 < 0) {
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, 0.0F);
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, var6 * 0.8F);
				} else {
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, var6 * 0.25F);
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, var6);
				}

				//if (EaglerAdapter.NVFogDistanceInstalled) {
				//	EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_DISTANCE_MODE, EaglerAdapter.GL_EYE_RADIAL);
				//}
			} else {
				float var8;
				float var9;
				float var10;
				float var11;
				float var12;

				if (this.cloudFog) {
					EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_EXP);
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_DENSITY, 0.1F);
					var6 = 1.0F;
					var12 = 1.0F;
					var8 = 1.0F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else if (var5 > 0 && Block.blocksList[var5].blockMaterial == Material.water) {
					EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_EXP);

					if (var3.isPotionActive(Potion.waterBreathing)) {
						EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_DENSITY, 0.05F);
					} else {
						EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_DENSITY, 0.1F);
					}

					var6 = 0.4F;
					var12 = 0.4F;
					var8 = 0.9F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else if (var5 > 0 && Block.blocksList[var5].blockMaterial == Material.lava) {
					EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_EXP);
					EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_DENSITY, 2.0F);
					var6 = 0.4F;
					var12 = 0.3F;
					var8 = 0.3F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else {
					if(this.mc.gameSettings.enableFog) {
						var6 = this.farPlaneDistance;
	
						if (this.mc.theWorld.provider.getWorldHasVoidParticles()) {
							double var13 = (double) ((var3.getBrightnessForRender(par2) & 15728640) >> 20) / 16.0D + (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double) par2 + 4.0D) / 32.0D;
	
							if (var13 < 1.0D) {
								if (var13 < 0.0D) {
									var13 = 0.0D;
								}
	
								var13 *= var13;
								var9 = 100.0F * (float) var13;
	
								if (var9 < 5.0F) {
									var9 = 5.0F;
								}
	
								if (var6 > var9) {
									var6 = var9;
								}
							}
						}
	
						EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_MODE, EaglerAdapter.GL_LINEAR);
	
						if (par1 < 0) {
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, 0.0F);
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, var6 * 0.8F);
						} else {
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, var6 * 0.25F);
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, var6 * 0.9F);
						}
	
						//if (EaglerAdapter.NVFogDistanceInstalled) {
						//	EaglerAdapter.glFogi(EaglerAdapter.GL_FOG_DISTANCE_MODE, EaglerAdapter.GL_EYE_RADIAL);
						//}
	
						if (this.mc.theWorld.provider.doesXZShowFog((int) var3.posX, (int) var3.posZ)) {
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_START, var6 * 0.05F);
							EaglerAdapter.glFogf(EaglerAdapter.GL_FOG_END, Math.min(var6, 192.0F) * 0.5F);
						}
					}else {
						EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
					}
				}
			}

			EaglerAdapter.glEnable(EaglerAdapter.GL_COLOR_MATERIAL);
			EaglerAdapter.glColorMaterial(EaglerAdapter.GL_FRONT, EaglerAdapter.GL_AMBIENT);
		}
	}

	/**
	 * Update and return fogColorBuffer with the RGBA values passed as arguments
	 */
	private FloatBuffer setFogColorBuffer(float par1, float par2, float par3, float par4) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}

	/**
	 * Converts performance value (0-2) to FPS (35-200)
	 */
	public static int performanceToFps(int par0) {
		short var1 = 240;

		if (par0 == 1) {
			var1 = 90;
		}

		if (par0 == 2) {
			var1 = 35;
		}

		return var1;
	}

	/**
	 * Get minecraft reference from the EntityRenderer
	 */
	static Minecraft getRendererMinecraft(EntityRenderer par0EntityRenderer) {
		return par0EntityRenderer.mc;
	}
}
