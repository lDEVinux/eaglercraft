package net.minecraft.src;

import net.lax1dude.eaglercraft.DefaultSkinRenderer;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerProfile;
import net.lax1dude.eaglercraft.HighPolySkin;
import net.lax1dude.eaglercraft.ModelBipedNewSkins;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.glemu.vector.Matrix4f;
import net.minecraft.client.Minecraft;

public class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain;
	private ModelBiped modelBipedMainNewSkin;
	private ModelBiped modelBipedMainNewSkinSlim;
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;
	private static final String[] armorFilenamePrefix = new String[] { "cloth", "chain", "iron", "diamond", "gold" };

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		this.modelBipedMain = (ModelBiped) this.mainModel;
		this.modelBipedMainNewSkin = new ModelBipedNewSkins(0.0F, false);
		this.modelBipedMainNewSkinSlim = new ModelBipedNewSkins(0.0F, true);
		this.modelArmorChestplate = new ModelBiped(1.0F);
		this.modelArmor = new ModelBiped(0.5F);
	}

	/**
	 * Set the specified armor model as the player model. Args: player, armorSlot,
	 * partialTick
	 */
	protected int setArmorModel(EntityPlayer par1EntityPlayer, int par2, float par3) {
		if(!DefaultSkinRenderer.isPlayerStandard(par1EntityPlayer) && !DefaultSkinRenderer.isZombieModel(DefaultSkinRenderer.getPlayerRenderer(par1EntityPlayer))) {
			return -1;
		}
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor) var5;
				this.loadTexture("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + ".png");
				ModelBiped var7 = par2 == 2 ? this.modelArmor : this.modelArmorChestplate;
				var7.bipedHead.showModel = par2 == 0;
				var7.bipedHeadwear.showModel = par2 == 0;
				var7.bipedBody.showModel = par2 == 1 || par2 == 2;
				var7.bipedRightArm.showModel = par2 == 1;
				var7.bipedLeftArm.showModel = par2 == 1;
				var7.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
				var7.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
				this.setRenderPassModel(var7);

				if (var7 != null) {
					var7.onGround = this.mainModel.onGround;
				}

				if (var7 != null) {
					var7.isRiding = this.mainModel.isRiding;
				}

				if (var7 != null) {
					var7.isChild = this.mainModel.isChild;
				}

				float var8 = 1.0F;

				if (var6.getArmorMaterial() == EnumArmorMaterial.CLOTH) {
					int var9 = var6.getColor(var4);
					float var10 = (float) (var9 >> 16 & 255) / 255.0F;
					float var11 = (float) (var9 >> 8 & 255) / 255.0F;
					float var12 = (float) (var9 & 255) / 255.0F;
					EaglerAdapter.glColor3f(var8 * var10, var8 * var11, var8 * var12);

					if (var4.isItemEnchanted()) {
						return 31;
					}

					return 16;
				}

				EaglerAdapter.glColor3f(var8, var8, var8);

				if (var4.isItemEnchanted()) {
					return 15;
				}

				return 1;
			}
		}

		return -1;
	}

	protected void func_82439_b(EntityPlayer par1EntityPlayer, int par2, float par3) {
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor) var5;
				this.loadTexture("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + "_b.png");
				float var7 = 1.0F;
				EaglerAdapter.glColor3f(var7, var7, var7);
			}
		}
	}
	
	private boolean renderPass2 = false;
	private final Matrix4f tmpMatrix = new Matrix4f();

	public void renderPlayer(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9) {
		boolean isHiPoly = DefaultSkinRenderer.isPlayerHighPoly(par1EntityPlayer);
		boolean fnawEnabled = Minecraft.getMinecraft().gameSettings.allowFNAWSkins;
		if(isHiPoly && fnawEnabled) {
			HighPolySkin msh = DefaultSkinRenderer.defaultHighPoly[DefaultSkinRenderer.getPlayerRenderer(par1EntityPlayer)];
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glTranslatef((float)par2, (float)par4 - par1EntityPlayer.yOffset, (float)par6);
			float var13 = this.handleRotationFloat(par1EntityPlayer, par9);
			float var10 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par9;
			float var11 = par1EntityPlayer.prevRotationYawHead + (par1EntityPlayer.rotationYawHead - par1EntityPlayer.prevRotationYawHead) * par9;
			float var12 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par9;
			this.rotateCorpse(par1EntityPlayer, var13, var10, par9);
			EaglerAdapter.glScalef(HighPolySkin.highPolyScale, HighPolySkin.highPolyScale, HighPolySkin.highPolyScale);
			msh.texture.bindTexture();
			
			if(par1EntityPlayer.isPlayerSleeping()) {
				if(msh == HighPolySkin.LAXATIVE_DUDE || msh == HighPolySkin.WEIRD_CLIMBER_DUDE) {
					EaglerAdapter.glTranslatef(0.0f, -3.7f, 0.0f);
				}else if(msh == HighPolySkin.BABY_WINSTON) {
					EaglerAdapter.glTranslatef(0.0f, -2.4f, 0.0f);
				}else {
					EaglerAdapter.glTranslatef(0.0f, -3.0f, 0.0f);
				}
			}
			
			float var15 = par1EntityPlayer.prevLimbYaw + (par1EntityPlayer.limbYaw - par1EntityPlayer.prevLimbYaw) * par9;
			float var16 = par1EntityPlayer.limbSwing - par1EntityPlayer.limbYaw * (1.0F - par9);
			
			if(msh == HighPolySkin.LONG_ARMS) {
				EaglerAdapter.glRotatef(MathHelper.sin(var16) * 20f * var15, 0.0f, 1.0f, 0.0f);
				EaglerAdapter.glRotatef(MathHelper.cos(var16) * 7f * var15, 0.0f, 0.0f, 1.0f);
			}else if(msh == HighPolySkin.WEIRD_CLIMBER_DUDE) {
				EaglerAdapter.glRotatef(MathHelper.sin(var16) * 7f * var15, 0.0f, 1.0f, 0.0f);
				EaglerAdapter.glRotatef(MathHelper.cos(var16) * 3f * var15, 0.0f, 0.0f, 1.0f);
				EaglerAdapter.glRotatef(-var11, 0.0f, 1.0f, 0.0f);
				float xd = (float)(par1EntityPlayer.posX - par1EntityPlayer.prevPosX);
				EaglerAdapter.glRotatef(xd * 70.0f * var15, 0.0f, 0.0f, 1.0f);
				float zd = (float)(par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ);
				EaglerAdapter.glRotatef(zd * 70.0f * var15, 1.0f, 0.0f, 0.0f);
				EaglerAdapter.glRotatef(var11, 0.0f, 1.0f, 0.0f);
			}else if(msh == HighPolySkin.LAXATIVE_DUDE) {
				EaglerAdapter.glRotatef(-var11, 0.0f, 1.0f, 0.0f);
				float xd = (float)(par1EntityPlayer.posX - par1EntityPlayer.prevPosX);
				EaglerAdapter.glRotatef(-xd * 40.0f * var15, 0.0f, 0.0f, 1.0f);
				float zd = (float)(par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ);
				EaglerAdapter.glRotatef(-zd * 40.0f * var15, 1.0f, 0.0f, 0.0f);
				EaglerAdapter.glRotatef(var11, 0.0f, 1.0f, 0.0f);
			}else if(msh == HighPolySkin.BABY_WINSTON) {
				EaglerAdapter.glTranslatef(0.0f, (MathHelper.cos(var13 % 100000.0f) + 1.0f) * var15 * 0.2f, 0.0f);
				EaglerAdapter.glRotatef(MathHelper.sin(var16) * 5f * var15, 0.0f, 1.0f, 0.0f);
				EaglerAdapter.glRotatef(MathHelper.cos(var16) * 5f * var15, 0.0f, 0.0f, 1.0f);
			}
			
			if (par1EntityPlayer.hurtTime > 0 || par1EntityPlayer.deathTime > 0) {
				EaglerAdapter.glColor4f(1.2f, 0.8F, 0.8F, 1.0F);
			}
			
			if(msh.bodyModel != null) {
				EaglerAdapter.drawHighPoly(msh.bodyModel.getModel());
			}
			float jumpFactor = 0.0f;
			
			if(msh.headModel != null) {
				if(msh == HighPolySkin.BABY_CHARLES) {
					long millis = System.currentTimeMillis();
					float partialTicks = (float) ((millis - par1EntityPlayer.eaglerHighPolyAnimationTick) * 0.02);
					//long l50 = millis / 50l * 50l;
					//boolean runTick = par1EntityPlayer.eaglerHighPolyAnimationTick < l50 && millis >= l50;
					par1EntityPlayer.eaglerHighPolyAnimationTick = millis;
					
					if(partialTicks < 0.0f) {
						partialTicks = 0.0f;
					}
					if(partialTicks > 1.0f) {
						partialTicks = 1.0f;
					}
					
					float jumpFac = (float)(par1EntityPlayer.posY - par1EntityPlayer.prevPosY);
					if(jumpFac < 0.0f && !par1EntityPlayer.isCollidedVertically) {
						jumpFac = -jumpFac;
						jumpFac *= 0.1f;
					}
					jumpFac -= 0.05f;
					if(jumpFac > 0.1f && !par1EntityPlayer.isCollidedVertically) {
						jumpFac = 0.1f;
					}else if(jumpFac < 0.0f) {
						jumpFac = 0.0f;
					}else if(jumpFac > 0.1f && par1EntityPlayer.isCollidedVertically) {
						jumpFac = 0.1f;
					}else if(jumpFac > 0.4f) {
						jumpFac = 0.4f;
					}
					jumpFac *= 10.0f;
					
					par1EntityPlayer.eaglerHighPolyAnimationFloat3 += (jumpFac / (jumpFac + 1.0f)) * 6.0f * partialTicks;
					
					if(Float.isInfinite(par1EntityPlayer.eaglerHighPolyAnimationFloat3)) {
						par1EntityPlayer.eaglerHighPolyAnimationFloat3 = 1.0f;
					}else if(par1EntityPlayer.eaglerHighPolyAnimationFloat3 > 1.0f) {
						par1EntityPlayer.eaglerHighPolyAnimationFloat3 = 1.0f;
					}else if(par1EntityPlayer.eaglerHighPolyAnimationFloat3 < -1.0f) {
						par1EntityPlayer.eaglerHighPolyAnimationFloat3 = -1.0f;
					}
					
					par1EntityPlayer.eaglerHighPolyAnimationFloat2 += par1EntityPlayer.eaglerHighPolyAnimationFloat3 * partialTicks;
	
					par1EntityPlayer.eaglerHighPolyAnimationFloat5 += partialTicks;
					while(par1EntityPlayer.eaglerHighPolyAnimationFloat5 > 0.05f) {
						par1EntityPlayer.eaglerHighPolyAnimationFloat5 -= 0.05f;
						par1EntityPlayer.eaglerHighPolyAnimationFloat3 *= 0.99f;
						par1EntityPlayer.eaglerHighPolyAnimationFloat2 *= 0.9f;
					}
					
					jumpFactor = par1EntityPlayer.eaglerHighPolyAnimationFloat2; //(par1EntityPlayer.eaglerHighPolyAnimationFloat1 - par1EntityPlayer.eaglerHighPolyAnimationFloat2) * partialTicks + par1EntityPlayer.eaglerHighPolyAnimationFloat2;
					jumpFactor -= 0.12f;
					if(jumpFactor < 0.0f) {
						jumpFactor = 0.0f;
					}
					jumpFactor = jumpFactor / (jumpFactor + 2.0f);
					if(jumpFactor > 1.0f) {
						jumpFactor = 1.0f;
					}
				}
				if(jumpFactor > 0.0f) {
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef(0.0f, jumpFactor * 3.0f, 0.0f);
				}
				
				EaglerAdapter.drawHighPoly(msh.headModel.getModel());
				
				if(jumpFactor > 0.0f) {
					EaglerAdapter.glPopMatrix();
				}
			}
			
			if(msh.limbsModel != null && msh.limbsModel.length > 0) {
				for(int i = 0; i < msh.limbsModel.length; ++i) {
					float offset = 0.0f;
					if(msh.limbsOffset != null) {
						if(msh.limbsOffset.length == 1) {
							offset = msh.limbsOffset[0];
						}else {
							offset = msh.limbsOffset[i];
						}
					}
					
					EaglerAdapter.glPushMatrix();
					
					if(offset != 0.0f || msh.limbsInitialRotation != 0.0f) {
						if(offset != 0.0f) {
							EaglerAdapter.glTranslatef(0.0f, offset, 0.0f);
						}
						if(msh.limbsInitialRotation != 0.0f) {
							EaglerAdapter.glRotatef(msh.limbsInitialRotation, 1.0f, 0.0f, 0.0f);
						}
					}
					
					if(msh == HighPolySkin.LONG_ARMS) {
						if(par1EntityPlayer.isSwingInProgress) {
							float var17 = MathHelper.cos(-par1EntityPlayer.getSwingProgress(par9) * (float)Math.PI * 2.0f - 1.2f) - 0.362f;
							var17 *= var17;
							EaglerAdapter.glRotatef(-var17 * 20.0f, 1.0f, 0.0f, 0.0f);
						}
					}else if(msh == HighPolySkin.WEIRD_CLIMBER_DUDE) {
						if(par1EntityPlayer.isSwingInProgress) {
							float var17 = MathHelper.cos(-par1EntityPlayer.getSwingProgress(par9) * (float)Math.PI * 2.0f - 1.2f) - 0.362f;
							var17 *= var17;
							EaglerAdapter.glRotatef(var17 * 60.0f, 1.0f, 0.0f, 0.0f);
						}
						EaglerAdapter.glRotatef(40.0f * var15, 1.0f, 0.0f, 0.0f);
					}else if(msh == HighPolySkin.LAXATIVE_DUDE) {
						float fff = (i == 0) ? 1.0f : -1.0f;
						float swing = (MathHelper.cos(var13 % 100000.0f) * fff + 0.2f) * var15;
						float swing2 = (MathHelper.cos(var13 % 100000.0f) * fff * 0.5f + 0.0f) * var15;
						EaglerAdapter.glRotatef(swing * 25.0f, 1.0f, 0.0f, 0.0f);
						if(par1EntityPlayer.isSwingInProgress) {
							float var17 = MathHelper.cos(-par1EntityPlayer.getSwingProgress(par9) * (float)Math.PI * 2.0f - 1.2f) - 0.362f;
							var17 *= var17;
							EaglerAdapter.glRotatef(-var17 * 25.0f, 1.0f, 0.0f, 0.0f);
						}
						
						// shear matrix
						tmpMatrix.setIdentity();
						tmpMatrix.m21 = swing2;
						tmpMatrix.m23 = swing2 * -0.2f;
						EaglerAdapter.glMultMatrixf(tmpMatrix);
					}
					
					if(i != 0) {
						msh.texture.bindTexture();
						if (par1EntityPlayer.hurtTime > 0 || par1EntityPlayer.deathTime > 0) {
							EaglerAdapter.glColor4f(1.2f, 0.8F, 0.8F, 1.0F);
						}else {
							EaglerAdapter.glColor4f(1.0f, 1.0F, 1.0F, 1.0F);
						}
					}
					EaglerAdapter.drawHighPoly(msh.limbsModel[i].getModel());
					
					if(i == 0) {
						EaglerAdapter.glPushMatrix();
						EaglerAdapter.flipLightMatrix();

						EaglerAdapter.glTranslatef(-0.287f, 0.05f, 0.0f);
						
						if(msh == HighPolySkin.LONG_ARMS) {
							EaglerAdapter.glTranslatef(1.72f, 2.05f, -0.24f);
							ItemStack stk = par1EntityPlayer.getHeldItem();
							if(stk != null) {
								if(stk.itemID == Item.bow.itemID) {
									EaglerAdapter.glTranslatef(-0.22f, 0.8f, 0.6f);
									EaglerAdapter.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
								}else if(stk.itemID < 256 && !(Item.itemsList[stk.itemID] != null && Item.itemsList[stk.itemID] instanceof ItemBlock && 
										!Block.blocksList[((ItemBlock)Item.itemsList[stk.itemID]).getBlockID()].renderAsNormalBlock())) {
									EaglerAdapter.glTranslatef(0.0f, -0.1f, 0.13f);
								}else if(Item.itemsList[stk.itemID] != null && !Item.itemsList[stk.itemID].isFull3D()) {
									EaglerAdapter.glTranslatef(-0.08f, -0.1f, 0.16f);
								}
							}
						}else if(msh == HighPolySkin.WEIRD_CLIMBER_DUDE) {
							EaglerAdapter.glTranslatef(-0.029f, 1.2f, -3f);
							EaglerAdapter.glRotatef(-5.0f, 0.0f, 1.0f, 0.0f);
							float var17 = -1.2f * var15;
							if(par1EntityPlayer.isSwingInProgress) {
								float vvar17 = MathHelper.cos(-par1EntityPlayer.getSwingProgress(par9) * (float)Math.PI * 2.0f - 1.2f) - 0.362f;
								var17 = vvar17 < var17 ? vvar17 : var17;
							}
							EaglerAdapter.glTranslatef(-0.02f * var17, 0.42f * var17, var17 * 0.35f);
							EaglerAdapter.glRotatef(var17 * 30.0f, 1.0f, 0.0f, 0.0f);
							EaglerAdapter.glRotatef(110.0f, 1.0f, 0.0f, 0.0f);
							ItemStack stk = par1EntityPlayer.getHeldItem();
							if(stk != null) {
								if(stk.itemID == Item.bow.itemID) {
									EaglerAdapter.glTranslatef(-0.18f, 1.0f, 0.4f);
									EaglerAdapter.glRotatef(-95.0f, 1.0f, 0.0f, 0.0f);
								}else if(stk.itemID < 256 && !(Item.itemsList[stk.itemID] != null && Item.itemsList[stk.itemID] instanceof ItemBlock && 
										!Block.blocksList[((ItemBlock)Item.itemsList[stk.itemID]).getBlockID()].renderAsNormalBlock())) {
									EaglerAdapter.glTranslatef(0.0f, -0.1f, 0.13f);
								}else if(Item.itemsList[stk.itemID] != null && !Item.itemsList[stk.itemID].isFull3D()) {
									EaglerAdapter.glTranslatef(-0.08f, -0.1f, 0.16f);
								}
							}
						}else if(msh == HighPolySkin.LAXATIVE_DUDE) {
							EaglerAdapter.glTranslatef(1.291f, 2.44f, -2.18f);
							EaglerAdapter.glRotatef(95.0f, 1.0f, 0.0f, 0.0f);
							ItemStack stk = par1EntityPlayer.getHeldItem();
							if(stk != null) {
								if(stk.itemID == Item.bow.itemID) {
									EaglerAdapter.glTranslatef(-0.65f, 1.3f, -0.1f);
									EaglerAdapter.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
									EaglerAdapter.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
								}else if(stk.itemID < 256 && !(Item.itemsList[stk.itemID] != null && Item.itemsList[stk.itemID] instanceof ItemBlock && 
										!Block.blocksList[((ItemBlock)Item.itemsList[stk.itemID]).getBlockID()].renderAsNormalBlock())) {
									EaglerAdapter.glTranslatef(0.0f, -0.35f, 0.4f);
								}else if(Item.itemsList[stk.itemID] != null && !Item.itemsList[stk.itemID].isFull3D()) {
									EaglerAdapter.glTranslatef(-0.1f, -0.1f, 0.16f);
								}
							}
						}
						
						renderSpecials(par1EntityPlayer, par9);
						EaglerAdapter.flipLightMatrix();
						EaglerAdapter.glPopMatrix();
					}

					EaglerAdapter.glPopMatrix();
				}
			}
			
			if(msh.eyesModel != null) {
				float f = 0.00416f;
				int brightness = par1EntityPlayer.getBrightnessForRender(0.0f);
				float blockLight = (brightness % 65536) * f;
				float skyLight = (brightness / 65536) * f;
				float sunCurve = (float)((par1EntityPlayer.worldObj.getWorldTime() + 4400l) % 24000) / 24000.0f;
				sunCurve = MathHelper.clamp_float(9.8f - MathHelper.abs(sunCurve * 5.0f + sunCurve * sunCurve * 45.0f - 14.3f) * 0.7f, 0.0f, 1.0f);
				skyLight = skyLight * (sunCurve * 0.85f + 0.15f);
				blockLight = blockLight * (sunCurve * 0.3f + 0.7f);
				float eyeBrightness = blockLight;
				if(skyLight > eyeBrightness) {
					eyeBrightness = skyLight;
				}
				eyeBrightness += blockLight * 0.2f;
				eyeBrightness = 1.0f - eyeBrightness;
				eyeBrightness = MathHelper.clamp_float(eyeBrightness * 1.9f - 1.0f, 0.0f, 1.0f);
				if(eyeBrightness > 0.1f) {
					EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
					EaglerAdapter.glColor4f(eyeBrightness * 7.0f, eyeBrightness * 7.0f, eyeBrightness * 7.0f, 1.0f);
					if(jumpFactor > 0.0f) {
						EaglerAdapter.glPushMatrix();
						EaglerAdapter.glTranslatef(0.0f, jumpFactor * 3.0f, 0.0f);
					}
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
					
					EaglerAdapter.drawHighPoly(msh.eyesModel.getModel());
					
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
					EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
					if(jumpFactor > 0.0f) {
						EaglerAdapter.glPopMatrix();
					}
					EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				}
			}

			EaglerAdapter.glPopMatrix();
			EaglerAdapter.flipLightMatrix();
			passSpecialRender(par1EntityPlayer, par2, par4, par6);
		}else if(DefaultSkinRenderer.isPlayerStandard(par1EntityPlayer) || (isHiPoly && !fnawEnabled)) {
			float var10 = 1.0F;
			EaglerAdapter.glColor3f(var10, var10, var10);
			ItemStack var11 = par1EntityPlayer.inventory.getCurrentItem();
			this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = this.modelBipedMainNewSkin.heldItemRight = this.modelBipedMainNewSkinSlim.heldItemRight = var11 != null ? 1 : 0;
	
			if (var11 != null && par1EntityPlayer.getItemInUseCount() > 0) {
				EnumAction var12 = var11.getItemUseAction();
	
				if (var12 == EnumAction.block) {
					this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = this.modelBipedMainNewSkin.heldItemRight = this.modelBipedMainNewSkinSlim.heldItemRight = 3;
				} else if (var12 == EnumAction.bow) {
					this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = this.modelBipedMainNewSkin.aimedBow = this.modelBipedMainNewSkinSlim.aimedBow = true;
				}
			}
	
			this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = this.modelBipedMainNewSkin.isSneak = this.modelBipedMainNewSkinSlim.isSneak = par1EntityPlayer.isSneaking();
			double var14 = par4 - (double) par1EntityPlayer.yOffset;
	
			if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP)) {
				var14 -= 0.125D;
			}
			
			this.mainModel = ((!isHiPoly && DefaultSkinRenderer.isPlayerNewSkin(par1EntityPlayer)) ? (DefaultSkinRenderer.isPlayerNewSkinSlim(par1EntityPlayer) ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain);
			int skinLayersByte = DefaultSkinRenderer.getSkinLayerByte(par1EntityPlayer);
			if(this.mainModel instanceof ModelBipedNewSkins) {
				ModelBipedNewSkins md = (ModelBipedNewSkins)this.mainModel;
				md.field_178730_v.isHidden = (skinLayersByte & 1) != 1;
				md.field_178734_a.isHidden = (skinLayersByte & 4) != 4;
				md.field_178732_b.isHidden = (skinLayersByte & 8) != 8;
				md.field_178733_c.isHidden = (skinLayersByte & 16) != 16;
				md.field_178731_d.isHidden = (skinLayersByte & 32) != 32;
			}
			((ModelBiped)this.mainModel).bipedHeadwear.isHidden = isHiPoly || (skinLayersByte & 2) != 2;
			this.mainModel.isChild = false;
			((ModelBiped)this.mainModel).blockTransparentSkin = true;
			super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
			((ModelBiped)this.mainModel).blockTransparentSkin = false;
			//this.mainModel = this.modelBipedMain;
			this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = this.modelBipedMainNewSkin.aimedBow = this.modelBipedMainNewSkinSlim.aimedBow = false;
			this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = this.modelBipedMainNewSkin.isSneak = this.modelBipedMainNewSkinSlim.isSneak = false;
			this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = this.modelBipedMainNewSkin.heldItemRight = this.modelBipedMainNewSkinSlim.heldItemRight = 0;
		}else {
			int renderType = DefaultSkinRenderer.getPlayerRenderer(par1EntityPlayer);
			 if(DefaultSkinRenderer.isZombieModel(renderType)) {
				if(DefaultSkinRenderer.zombieRenderer == null) DefaultSkinRenderer.zombieRenderer = new ModelZombie(0.0F, true);
				this.mainModel = DefaultSkinRenderer.zombieRenderer;
				this.mainModel.isChild = false;
				DefaultSkinRenderer.zombieRenderer.isSneak = par1EntityPlayer.isSneaking();
				DefaultSkinRenderer.zombieRenderer.isRiding = par1EntityPlayer.isRiding();
				double var14 = par4 - (double) par1EntityPlayer.yOffset;
				if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP)) var14 -= 0.125D;
				super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
				DefaultSkinRenderer.zombieRenderer.isSneak = false;
				DefaultSkinRenderer.zombieRenderer.isRiding = false;
				this.mainModel = this.modelBipedMain;
			}else {
				switch(renderType) {
				case 32:
					if(DefaultSkinRenderer.villagerRenderer == null) DefaultSkinRenderer.villagerRenderer = new ModelVillager(0.0F);
					DefaultSkinRenderer.villagerRenderer.isChild = false;
					this.mainModel = DefaultSkinRenderer.villagerRenderer;
					super.doRenderLiving(par1EntityPlayer, par2, par4 - (double) par1EntityPlayer.yOffset, par6, par8, par9);
					this.mainModel = this.modelBipedMain;
					break;
				case 19:
					if(DefaultSkinRenderer.endermanRenderer == null) DefaultSkinRenderer.endermanRenderer = new ModelEnderman();
					DefaultSkinRenderer.endermanRenderer.isChild = false;
					DefaultSkinRenderer.endermanRenderer.isCarrying = (par1EntityPlayer.inventory.getCurrentItem() != null && par1EntityPlayer.inventory.getCurrentItem().itemID < 256);
					this.mainModel = DefaultSkinRenderer.endermanRenderer;
					super.doRenderLiving(par1EntityPlayer, par2, par4 - (double) par1EntityPlayer.yOffset + 0.05f, par6, par8, par9);
					
					RenderEnderman.tex_eyes.bindTexture();
					
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
					EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE);
					EaglerAdapter.glTranslatef((float)par2, (float)par4 - par1EntityPlayer.yOffset + 0.05f, (float)par6);
					float var13 = this.handleRotationFloat(par1EntityPlayer, par9);
					float var10 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par9;
					float var11 = par1EntityPlayer.prevRotationYawHead + (par1EntityPlayer.rotationYawHead - par1EntityPlayer.prevRotationYawHead) * par9;
					float var12 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par9;
					this.rotateCorpse(par1EntityPlayer, var13, var10, par9);
					EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
					EaglerAdapter.glScalef(-0.95F, -1.0F, 0.95F); //?
					EaglerAdapter.glTranslatef(0.0F, -24.0F * 0.0625F - 0.0078125F + 0.1606f, 0.0F);

					char var5 = 61680;
					int var6 = var5 % 65536;
					int var7 = var5 / 65536;
					EaglerAdapter.glColor4f(2.3F, 2.3F, 2.3F, par1EntityPlayer.isInvisible() ? 0.3f : 1.0f);
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
					DefaultSkinRenderer.endermanRenderer.render(null, 0f, 0f, var13, var11 - var10, var12, 0.0625f);
					EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
					EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
					EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
					EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glPopMatrix();
					EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					
					DefaultSkinRenderer.endermanRenderer.isCarrying = false;
					this.mainModel = this.modelBipedMain;
					break;
				case 20:
					if(DefaultSkinRenderer.skeletonRenderer == null) DefaultSkinRenderer.skeletonRenderer = new ModelSkeleton(0.0F);
					DefaultSkinRenderer.skeletonRenderer.isChild = false;
					this.mainModel = DefaultSkinRenderer.skeletonRenderer;
					super.doRenderLiving(par1EntityPlayer, par2, par4 - (double) par1EntityPlayer.yOffset, par6, par8, par9);
					this.mainModel = this.modelBipedMain;
					break;
				case 21:
					if(DefaultSkinRenderer.blazeRenderer == null) DefaultSkinRenderer.blazeRenderer = new ModelBlaze();
					DefaultSkinRenderer.blazeRenderer.isChild = false;
					this.mainModel = DefaultSkinRenderer.blazeRenderer;
					super.doRenderLiving(par1EntityPlayer, par2, par4 - (double) par1EntityPlayer.yOffset, par6, par8, par9);
					this.mainModel = this.modelBipedMain;
					break;
				}
			}
		}
	}
	
	private static final TextureLocation lax1dude_cape = new TextureLocation("/misc/laxcape.png");

	/**
	 * Method for adding special render rules
	 */
	protected void renderSpecials(EntityPlayer par1EntityPlayer, float par2) {
		float var3 = 1.0F;
		EaglerAdapter.glColor3f(var3, var3, var3);
		super.renderEquippedItems(par1EntityPlayer, par2);
		super.renderArrowsStuckInEntity(par1EntityPlayer, par2);
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3);

		boolean isNew = DefaultSkinRenderer.isPlayerNewSkin(par1EntityPlayer);
		boolean isSlim = DefaultSkinRenderer.isPlayerNewSkinSlim(par1EntityPlayer);
		int renderType = DefaultSkinRenderer.getPlayerRenderer(par1EntityPlayer);
		boolean allowFNAW = Minecraft.getMinecraft().gameSettings.allowFNAWSkins;

		if(!allowFNAW || !DefaultSkinRenderer.isHighPoly(renderType)) {
			if (var4 != null) {
				EaglerAdapter.glPushMatrix();
				(isNew ? (isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain).bipedHead.postRender(0.0625F);
				float var5;
	
				if (var4.getItem().itemID < 256) {
					if (RenderBlocks.renderItemIn3d(Block.blocksList[var4.itemID].getRenderType())) {
						var5 = 0.625F;
						EaglerAdapter.glTranslatef(0.0F, -0.25F, 0.0F);
						EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
						EaglerAdapter.glScalef(var5, -var5, -var5);
					}
	
					this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var4, 0);
				} else if (var4.getItem().itemID == Item.skull.itemID) {
					var5 = 1.0625F;
					EaglerAdapter.glScalef(var5, -var5, -var5);
					String var6 = "";
	
					if (var4.hasTagCompound() && var4.getTagCompound().hasKey("SkullOwner")) {
						var6 = var4.getTagCompound().getString("SkullOwner");
					}
	
					TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, var4.getItemDamage(), var6);
				}
	
				EaglerAdapter.glPopMatrix();
			}
	
			float var7;
			float var8;
			
			/*
			if (par1EntityPlayer.username.equals("deadmau5") && this.loadDownloadableImageTexture(par1EntityPlayer.skinUrl, (String) null)) {
				for (int var20 = 0; var20 < 2; ++var20) {
					float var23 = par1EntityPlayer.prevRotationYaw + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * par2
							- (par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2);
					var7 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par2;
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glRotatef(var23, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(var7, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glTranslatef(0.375F * (float) (var20 * 2 - 1), 0.0F, 0.0F);
					EaglerAdapter.glTranslatef(0.0F, -0.375F, 0.0F);
					EaglerAdapter.glRotatef(-var7, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(-var23, 0.0F, 1.0F, 0.0F);
					var8 = 1.3333334F;
					EaglerAdapter.glScalef(var8, var8, var8);
					this.modelBipedMain.renderEars(0.0625F);
					EaglerAdapter.glPopMatrix();
				}
			}
			*/
	
			float var11;
			
			if(DefaultSkinRenderer.isStandardModel(renderType) || DefaultSkinRenderer.isZombieModel(renderType) || (!allowFNAW && DefaultSkinRenderer.isHighPoly(renderType))) {
				if(!par1EntityPlayer.isInvisible() && !par1EntityPlayer.getHideCape()) {
					if(DefaultSkinRenderer.bindSyncedCape(par1EntityPlayer)) {
						EaglerAdapter.glPushMatrix();
						EaglerAdapter.glTranslatef(0.0F, 0.0F, 0.125F);
						double var21 = par1EntityPlayer.field_71091_bM + (par1EntityPlayer.field_71094_bP - par1EntityPlayer.field_71091_bM) * (double) par2
								- (par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * (double) par2);
						double var24 = par1EntityPlayer.field_71096_bN + (par1EntityPlayer.field_71095_bQ - par1EntityPlayer.field_71096_bN) * (double) par2
								- (par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * (double) par2);
						double var9 = par1EntityPlayer.field_71097_bO + (par1EntityPlayer.field_71085_bR - par1EntityPlayer.field_71097_bO) * (double) par2
								- (par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * (double) par2);
						var11 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2;
						double var12 = (double) MathHelper.sin(var11 * (float) Math.PI / 180.0F);
						double var14 = (double) (-MathHelper.cos(var11 * (float) Math.PI / 180.0F));
						float var16 = (float) var24 * 10.0F;
			
						if (var16 < -6.0F) {
							var16 = -6.0F;
						}
			
						if (var16 > 32.0F) {
							var16 = 32.0F;
						}
			
						float var17 = (float) (var21 * var12 + var9 * var14) * 100.0F;
						float var18 = (float) (var21 * var14 - var9 * var12) * 100.0F;
			
						if (var17 < 0.0F) {
							var17 = 0.0F;
						}
			
						float var19 = par1EntityPlayer.prevCameraYaw + (par1EntityPlayer.cameraYaw - par1EntityPlayer.prevCameraYaw) * par2;
						var16 += MathHelper.sin((par1EntityPlayer.prevDistanceWalkedModified + (par1EntityPlayer.distanceWalkedModified - par1EntityPlayer.prevDistanceWalkedModified) * par2) * 6.0F) * 32.0F * var19;
			
						if (par1EntityPlayer.isSneaking()) {
							var16 += 25.0F;
						}
			
						EaglerAdapter.glRotatef(6.0F + var17 / 2.0F + var16, 1.0F, 0.0F, 0.0F);
						EaglerAdapter.glRotatef(var18 / 2.0F, 0.0F, 0.0F, 1.0F);
						EaglerAdapter.glRotatef(-var18 / 2.0F, 0.0F, 1.0F, 0.0F);
						EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
						this.modelBipedMain.renderCloak(0.0625F);
						EaglerAdapter.glPopMatrix();
					}
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
					EaglerAdapter.glPopMatrix();
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				}
			}
		}

		ItemStack var22 = par1EntityPlayer.inventory.getCurrentItem();

		if (var22 != null) {
			EaglerAdapter.glPushMatrix();
			
			if(!allowFNAW || !DefaultSkinRenderer.isHighPoly(renderType)) {
				if(DefaultSkinRenderer.isZombieModel(renderType) || renderType == 20) {
					((ModelBiped)this.mainModel).bipedRightArm.postRender(0.0625F);
				}else {
					(isNew ? (isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain).bipedRightArm.postRender(0.0625F);
				}
			}
			
			EaglerAdapter.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (par1EntityPlayer.fishEntity != null) {
				var22 = new ItemStack(Item.stick);
			}

			EnumAction var25 = null;

			if (par1EntityPlayer.getItemInUseCount() > 0) {
				var25 = var22.getItemUseAction();
			}

			float var7 = 0.0f;
			float var8 = 0.0f;
			float var11 = 0.0f;
			if (var22.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var22.itemID].getRenderType())) {
				var7 = 0.5F;
				EaglerAdapter.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var7 *= 0.75F;
				EaglerAdapter.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glScalef(-var7, -var7, var7);
			} else if (var22.itemID == Item.bow.itemID) {
				var7 = 0.625F;
				EaglerAdapter.glTranslatef(0.0F, 0.125F, 0.3125F);
				EaglerAdapter.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glScalef(var7, -var7, var7);
				EaglerAdapter.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (Item.itemsList[var22.itemID].isFull3D()) {
				var7 = 0.625F;

				if (Item.itemsList[var22.itemID].shouldRotateAroundWhenRendering()) {
					EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					EaglerAdapter.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (par1EntityPlayer.getItemInUseCount() > 0 && var25 == EnumAction.block) {
					EaglerAdapter.glTranslatef(0.05F, 0.0F, -0.1F);
					EaglerAdapter.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				EaglerAdapter.glTranslatef(0.0F, 0.1875F, 0.0F);
				EaglerAdapter.glScalef(var7, -var7, var7);
				EaglerAdapter.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var7 = 0.375F;
				EaglerAdapter.glTranslatef(0.25F, 0.1875F, -0.1875F);
				EaglerAdapter.glScalef(var7, var7, var7);
				EaglerAdapter.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				EaglerAdapter.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float var10;
			int var27;
			float var28;

			if (var22.getItem().requiresMultipleRenderPasses()) {
				for (var27 = 0; var27 <= 1; ++var27) {
					int var26 = var22.getItem().getColorFromItemStack(var22, var27);
					var28 = (float) (var26 >> 16 & 255) / 255.0F;
					var10 = (float) (var26 >> 8 & 255) / 255.0F;
					var11 = (float) (var26 & 255) / 255.0F;
					EaglerAdapter.glColor4f(var28, var10, var11, 1.0F);
					this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var22, var27);
				}
			} else {
				var27 = var22.getItem().getColorFromItemStack(var22, 0);
				var8 = (float) (var27 >> 16 & 255) / 255.0F;
				var28 = (float) (var27 >> 8 & 255) / 255.0F;
				var10 = (float) (var27 & 255) / 255.0F;
				EaglerAdapter.glColor4f(var8, var28, var10, 1.0F);
				this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var22, 0);
			}

			EaglerAdapter.glPopMatrix();
		}
	}

	protected void renderPlayerScale(EntityPlayer par1EntityPlayer, float par2) {
		float var3 = 0.9375F;
		EaglerAdapter.glScalef(var3, var3, var3);
	}

	protected void func_96450_a(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		if (par10 < 100.0D) {
			Scoreboard var12 = par1EntityPlayer.getWorldScoreboard();
			ScoreObjective var13 = var12.func_96539_a(2);

			if (var13 != null) {
				Score var14 = var12.func_96529_a(par1EntityPlayer.getEntityName(), var13);

				if (par1EntityPlayer.isPlayerSleeping()) {
					this.renderLivingLabel(par1EntityPlayer, var14.func_96652_c() + " " + var13.getDisplayName(), par2, par4 - 1.5D, par6, 64);
				} else {
					this.renderLivingLabel(par1EntityPlayer, var14.func_96652_c() + " " + var13.getDisplayName(), par2, par4, par6, 64);
				}

				par4 += (double) ((float) this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * par9);
			}
		}

		super.func_96449_a(par1EntityPlayer, par2, par4, par6, par8Str, par9, par10);
	}

	public void renderFirstPersonArm(EntityPlayer par1EntityPlayer) {
		float var2 = 1.0F;
		EaglerAdapter.glColor4f(var2, var2, var2, 1.0F);
		int i = DefaultSkinRenderer.getPlayerRenderer(par1EntityPlayer);
		if(DefaultSkinRenderer.isStandardModel(i) || DefaultSkinRenderer.isZombieModel(i)) {
			boolean isNew = DefaultSkinRenderer.isPlayerNewSkin(par1EntityPlayer);
			boolean isSlim = DefaultSkinRenderer.isPlayerNewSkinSlim(par1EntityPlayer);
			(isNew ? (isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain).onGround = 0.0F;
			(isNew ? (isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain).setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1EntityPlayer);
			(isNew ? (isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin) : this.modelBipedMain).bipedRightArm.render(0.0625F);
			if(isNew) {
				ModelBipedNewSkins mdl = (ModelBipedNewSkins)(isSlim ? this.modelBipedMainNewSkinSlim : this.modelBipedMainNewSkin);
				mdl.field_178732_b.isHidden = !Minecraft.getMinecraft().gameSettings.showSkinRightArm;
				if(!mdl.field_178732_b.isHidden) {
					mdl.field_178732_b.render(0.0625F);
				}
			}
		}
	}

	/**
	 * Renders player with sleeping offset if sleeping
	 */
	protected void renderPlayerSleep(EntityPlayer par1EntityPlayer, double par2, double par4, double par6) {
		if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping()) {
			super.renderLivingAt(par1EntityPlayer, par2 + (double) par1EntityPlayer.field_71079_bU, par4 + (double) par1EntityPlayer.field_71082_cx, par6 + (double) par1EntityPlayer.field_71089_bV);
		} else {
			super.renderLivingAt(par1EntityPlayer, par2, par4, par6);
		}
	}

	/**
	 * Rotates the player if the player is sleeping. This method is called in
	 * rotateCorpse.
	 */
	protected void rotatePlayer(EntityPlayer par1EntityPlayer, float par2, float par3, float par4) {
		if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping()) {
			EaglerAdapter.glRotatef(par1EntityPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(this.getDeathMaxRotation(par1EntityPlayer), 0.0F, 0.0F, 1.0F);
			EaglerAdapter.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(par1EntityPlayer, par2, par3, par4);
		}
	}

	protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		this.func_96450_a((EntityPlayer) par1EntityLiving, par2, par4, par6, par8Str, par9, par10);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.renderPlayerScale((EntityPlayer) par1EntityLiving, par2);
	}

	protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
		this.func_82439_b((EntityPlayer) par1EntityLiving, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setArmorModel((EntityPlayer) par1EntityLiving, par2, par3);
	}

	private static final TextureLocation terrain = new TextureLocation("/terrain.png");
	
	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		if(!renderPass2) {
			EntityPlayer p = (EntityPlayer) par1EntityLiving;
			int renderType = DefaultSkinRenderer.getPlayerRenderer(p);
			if(DefaultSkinRenderer.isPlayerStandard(p) || DefaultSkinRenderer.isZombieModel(renderType) || renderType == 20 ||
					(DefaultSkinRenderer.isHighPoly(renderType) && !Minecraft.getMinecraft().gameSettings.allowFNAWSkins)) {
				this.renderSpecials(p, par2);
			}else {
				if(renderType == 19) {
					ItemStack s = p.inventory.getCurrentItem();
					if(s != null && s.itemID < 256) {
						EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
						EaglerAdapter.glPushMatrix();
						float var3 = 0.5F;
						EaglerAdapter.glTranslatef(0.0F, 0.6875F, -0.75F);
						var3 *= 1.0F;
						EaglerAdapter.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
						EaglerAdapter.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
						EaglerAdapter.glScalef(-var3, -var3, var3);
						int var4 = p.getBrightnessForRender(par2);
						int var5 = var4 % 65536;
						int var6 = var4 / 65536;
						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var5 / 1.0F, (float) var6 / 1.0F);
						EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						terrain.bindTexture();
						EaglerAdapter.flipLightMatrix();
						this.renderBlocks.renderBlockAsItem(Block.blocksList[s.itemID], s.getItemDamage(), 1.0F);
						EaglerAdapter.flipLightMatrix();
						EaglerAdapter.glPopMatrix();
						EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
					}
				}
			}
		}
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.rotatePlayer((EntityPlayer) par1EntityLiving, par2, par3, par4);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		this.renderPlayerSleep((EntityPlayer) par1EntityLiving, par2, par4, par6);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderPlayer((EntityPlayer) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderPlayer((EntityPlayer) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture = new TextureLocation("/mob/char.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		if(par1EntityLiving instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId < 0) {
				Minecraft.getMinecraft().renderEngine.bindTexture(EaglerProfile.skins.get(EaglerProfile.customSkinId).glTex);
			}else {
				TextureLocation tx = null;
				if(DefaultSkinRenderer.defaultVanillaSkins[EaglerProfile.presetSkinId] == null) {
					if(DefaultSkinRenderer.defaultHighPoly[EaglerProfile.presetSkinId] == null) {
						tx = entityTexture;
					}else {
						if(Minecraft.getMinecraft().gameSettings.allowFNAWSkins) {
							tx = DefaultSkinRenderer.defaultHighPoly[EaglerProfile.presetSkinId].texture;
						}else {
							tx = DefaultSkinRenderer.defaultHighPoly[EaglerProfile.presetSkinId].fallbackTexture;
						}
					}
				}else {
					tx = DefaultSkinRenderer.defaultVanillaSkins[EaglerProfile.presetSkinId];
				}
				tx.bindTexture();
			}
		}else if(par1EntityLiving instanceof EntityOtherPlayerMP) {
			if(!DefaultSkinRenderer.bindSyncedSkin((EntityOtherPlayerMP)par1EntityLiving)) {
				entityTexture.bindTexture();
			}
		}else {
			entityTexture.bindTexture();
		}
	}
}
