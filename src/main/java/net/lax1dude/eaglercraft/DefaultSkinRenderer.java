package net.lax1dude.eaglercraft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.EaglerProfile.EaglerProfileCape;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelBlaze;
import net.minecraft.src.ModelEnderman;
import net.minecraft.src.ModelSkeleton;
import net.minecraft.src.ModelVillager;
import net.minecraft.src.ModelZombie;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.RenderEnderman;
import net.minecraft.src.RenderHelper;

public class DefaultSkinRenderer {
	
	public static final TextureLocation[] defaultVanillaSkins = new TextureLocation[] {
			new TextureLocation("/skins/01.default_steve.png"),
			new TextureLocation("/skins/02.default_alex.png"),
			new TextureLocation("/skins/03.tennis_steve.png"),
			new TextureLocation("/skins/04.tennis_alex.png"),
			new TextureLocation("/skins/05.tuxedo_steve.png"),
			new TextureLocation("/skins/06.tuxedo_alex.png"),
			new TextureLocation("/skins/07.athlete_steve.png"),
			new TextureLocation("/skins/08.athlete_alex.png"),
			new TextureLocation("/skins/09.cyclist_steve.png"),
			new TextureLocation("/skins/10.cyclist_alex.png"),
			new TextureLocation("/skins/11.boxer_steve.png"),
			new TextureLocation("/skins/12.boxer_alex.png"),
			new TextureLocation("/skins/13.prisoner_steve.png"),
			new TextureLocation("/skins/14.prisoner_alex.png"),
			new TextureLocation("/skins/15.scottish_steve.png"),
			new TextureLocation("/skins/16.scottish_alex.png"),
			new TextureLocation("/skins/17.dev_steve.png"),
			new TextureLocation("/skins/18.dev_alex.png"),
			new TextureLocation("/skins/19.herobrine.png"),
			new TextureLocation("/mob/enderman.png"),
			new TextureLocation("/mob/skeleton.png"),
			new TextureLocation("/mob/fire.png"),
			new TextureLocation("/skins/20.barney.png"),
			new TextureLocation("/skins/21.slime.png"),
			new TextureLocation("/skins/22.noob.png"),
			new TextureLocation("/skins/23.trump.png"),
			new TextureLocation("/skins/24.notch.png"),
			new TextureLocation("/skins/25.creeper.png"),
			new TextureLocation("/skins/26.zombie.png"),
			new TextureLocation("/skins/27.pig.png"),
			new TextureLocation("/skins/28.squid.png"),
			new TextureLocation("/skins/29.mooshroom.png"),
			new TextureLocation("/mob/villager/villager.png"),
			null, null, null, null, null
	};
	
	public static final TextureLocation[] defaultVanillaCapes = new TextureLocation[] {
			null,
			new TextureLocation("/skins/c01.minecon_2011.png"),
			new TextureLocation("/skins/c02.minecon_2012.png"),
			new TextureLocation("/skins/c03.minecon_2013.png"),
			new TextureLocation("/skins/c04.minecon_2015.png"),
			new TextureLocation("/skins/c05.minecon_2016.png"),
			new TextureLocation("/skins/c06.microsoft_account.png"),
			new TextureLocation("/skins/c07.mapmaker.png"),
			new TextureLocation("/skins/c08.mojang_old.png"),
			new TextureLocation("/skins/c09.mojang_new.png"),
			new TextureLocation("/skins/c10.jira_mod.png"),
			new TextureLocation("/skins/c11.mojang_very_old.png"),
			new TextureLocation("/skins/c12.scrolls.png"),
			new TextureLocation("/skins/c13.cobalt.png"),
			new TextureLocation("/skins/c14.translator.png"),
			new TextureLocation("/skins/c15.millionth_account.png"),
			new TextureLocation("/skins/c16.prismarine.png"),
			new TextureLocation("/skins/c17.snowman.png"),
			new TextureLocation("/skins/c18.spade.png"),
			new TextureLocation("/skins/c19.birthday.png"),
			new TextureLocation("/skins/c20.db.png")
	};
	
	public static final HighPolySkin[] defaultHighPoly = new HighPolySkin[] {
			null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null,
			null, null, null,
			HighPolySkin.LONG_ARMS, HighPolySkin.WEIRD_CLIMBER_DUDE, HighPolySkin.LAXATIVE_DUDE,
			HighPolySkin.BABY_CHARLES, HighPolySkin.BABY_WINSTON
	};
	
	public static final boolean[] defaultVanillaSkinClassicOrSlimVariants = new boolean[] {
			false, true,
			false, true,
			false, true,
			false, true,
			false, true,
			false, true,
			false, true,
			false, true,
			false, true
	};

	private static final HashMap<Integer,EntityOtherPlayerMP> skinCookies = new HashMap();
	private static final HashMap<EntityOtherPlayerMP,Integer> skinGLUnits = new HashMap();
	private static final HashMap<EntityOtherPlayerMP,Integer> capeGLUnits = new HashMap();
	private static final HashMap<EntityOtherPlayerMP,Long> skinGLTimeout = new HashMap();
	
	private static long lastClean = 0l;
	
	public static void deleteOldSkins() {
		if(System.currentTimeMillis() - lastClean > 60000l) {
			lastClean = System.currentTimeMillis();
			Iterator<Entry<EntityOtherPlayerMP,Long>> itr = skinGLTimeout.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<EntityOtherPlayerMP,Long> ee = itr.next();
				if(System.currentTimeMillis() - ee.getValue() > 80000l) {
					itr.remove();
					if(skinGLUnits.containsKey(ee.getKey())) {
						Minecraft.getMinecraft().renderEngine.deleteTexture(skinGLUnits.remove(ee.getKey()));
					}
					if(capeGLUnits.containsKey(ee.getKey())) {
						Minecraft.getMinecraft().renderEngine.deleteTexture(capeGLUnits.remove(ee.getKey()));
					}
				}
			}
			Iterator<Entry<Integer, EntityOtherPlayerMP>> itr2 = skinCookies.entrySet().iterator();
			while(itr2.hasNext()) {
				Entry<Integer, EntityOtherPlayerMP> e = itr2.next();
				if(e.getValue().isDead) {
					itr2.remove();
				}
			}
		}
	}
	
	public static boolean bindSyncedSkin(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			return false;
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				int type = ((int)pp.skinPacket[0] & 0xFF);
				if(type != 4 && type >= 0 && type < EaglerProfile.SKIN_DATA_SIZE.length) {
					if(!skinGLUnits.containsKey(pp)) {
						byte[] skinToLoad = new byte[EaglerProfile.SKIN_DATA_SIZE[type]];
						System.arraycopy(pp.skinPacket, 1, skinToLoad, 0, skinToLoad.length);
						int w, h;
						
						switch((int)pp.skinPacket[0] & 0xFF) {
						default:
						case 0:
							w = 64;
							h = 32;
							break;
						case 1:
						case 5:
							w = 64;
							h = 64;
							break;
						}
						
						if(skinToLoad.length / 4 == w * h) {
							skinGLUnits.put(pp, Minecraft.getMinecraft().renderEngine.setupTextureRaw(skinToLoad, w, h));
						}
					}
					skinGLTimeout.put(pp, System.currentTimeMillis());
					Integer i = skinGLUnits.get(pp);
					if(i != null && i.intValue() > 0) {
						Minecraft.getMinecraft().renderEngine.bindTexture(i.intValue());
					}else {
						defaultVanillaSkins[0].bindTexture();
					}
				}else {
					int type2 = (int)pp.skinPacket[1] & 0xFF;
					if(type2 < defaultVanillaSkins.length) {
						TextureLocation loc = defaultVanillaSkins[type2];
						if(loc != null) {
							loc.bindTexture();
						}else {
							if(defaultHighPoly[type2] != null) {
								defaultHighPoly[type2].fallbackTexture.bindTexture();
								return true;
							}else {
								return false;
							}
						}
					}
				}
				return true;
			}else {
				requestSkin(pp);
			}
			return false;
		}else {
			return false;
		}
	}
	
	public static boolean bindSyncedCape(EntityPlayer p) {
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetCapeId < 0) {
				EaglerProfileCape cp = EaglerProfile.capes.get(EaglerProfile.customCapeId);
				if(cp != null) {
					Minecraft.getMinecraft().renderEngine.bindTexture(cp.glTex);
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
					EaglerAdapter.glScalef(2.0f, 1.0f, 1.0f);
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
					return true;
				}else {
					return false;
				}
			}else {
				if(EaglerProfile.presetCapeId < defaultVanillaCapes.length) {
					TextureLocation loc = defaultVanillaCapes[EaglerProfile.presetCapeId];
					if(loc == null) {
						return false;
					}else {
						loc.bindTexture();
						return true;
					}
				}else {
					return false;
				}
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				int tp = ((int)pp.skinPacket[0] & 0xFF);
				if(tp >= 0 && tp < EaglerProfile.SKIN_DATA_SIZE.length) {
					int offset = 1 + EaglerProfile.SKIN_DATA_SIZE[tp];
					if(pp.skinPacket.length > offset + 1) {
						int capeType = (int)pp.skinPacket[offset] & 0xFF;
						if(capeType >= 0 && capeType < EaglerProfile.CAPE_DATA_SIZE.length) {
							int len = EaglerProfile.CAPE_DATA_SIZE[capeType];
							if(pp.skinPacket.length > offset + len + 1) {
								if(capeType != 2) {
									if(!capeGLUnits.containsKey(pp)) {
										byte[] dataToLoad = new byte[len];
										System.arraycopy(pp.skinPacket, offset + 2, dataToLoad, 0, len);
										int w, h;
										switch(capeType) {
										case 0:
										default:
											w = 32;
											h = 32;
											break;
										}
	
										if(dataToLoad.length / 4 == w * h) {
											capeGLUnits.put(pp, Minecraft.getMinecraft().renderEngine.setupTextureRaw(dataToLoad, w, h));
										}
									}
									skinGLTimeout.put(pp, System.currentTimeMillis());
									Integer i = capeGLUnits.get(pp);
									if(i != null && i.intValue() > 0) {
										EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
										EaglerAdapter.glScalef(2.0f, 1.0f, 1.0f);
										EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
										Minecraft.getMinecraft().renderEngine.bindTexture(i.intValue());
										return true;
									}else {
										return false;
									}
								}else {
									int preset = (int)pp.skinPacket[offset + 2] & 0xFF;
									if(preset < defaultVanillaCapes.length) {
										TextureLocation loc = defaultVanillaCapes[preset];
										if(loc == null) {
											return false;
										}else {
											loc.bindTexture();
											return true;
										}
									}else {
										return false;
									}
								}
							}
						}
						
					}
					
				}
			}else {
				requestSkin(pp);
			}
		}
		return false;
	}
	
	public static int getSkinLayerByte(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			return Minecraft.getMinecraft().gameSettings.getSkinLayers();
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				int tp = ((int)pp.skinPacket[0] & 0xFF);
				if(tp >= 0 && tp < EaglerProfile.SKIN_DATA_SIZE.length) {
					int offset = 1 + EaglerProfile.SKIN_DATA_SIZE[tp];
					if(pp.skinPacket.length > offset + 1) {
						return (int)pp.skinPacket[offset + 1] & 0xFF;
					}
				}
			}
		}
		return 0xFF;
	}
	
	public static void updateSkinLayerByte(int skinFlags, byte[] pkt) {
		if(pkt.length > 0) {
			int tp = ((int)pkt[0] & 0xFF);
			if(tp >= 0 && tp < EaglerProfile.SKIN_DATA_SIZE.length) {
				int offset = 1 + EaglerProfile.SKIN_DATA_SIZE[tp];
				if(pkt.length > offset + 1) {
					pkt[offset + 1] = (byte)skinFlags;
				}
			}
		}
	}
	
	private static void requestSkin(EntityOtherPlayerMP pp) {
		if(!skinCookies.containsValue(pp)) {
			int cookie = (int)(System.nanoTime() % 65536);
			skinCookies.put(cookie, pp);
			byte[] n = pp.username.getBytes();
			byte[] pkt = new byte[n.length + 2];
			System.arraycopy(n, 0, pkt, 2, n.length);
			pkt[0] = (byte)(cookie & 0xFF);
			pkt[1] = (byte)((cookie >> 8) & 0xFF);
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet250CustomPayload("EAG|FetchSkin", pkt));
		}
	}
	
	public static void skinResponse(byte[] data) {
		int cookie = ((int)data[0] & 0xFF) | (((int)data[1] & 0xFF) << 8);
		if(skinCookies.containsKey(cookie) && (data.length > 3)) {
			EntityOtherPlayerMP p = skinCookies.remove(cookie);
			byte[] packet = new byte[data.length - 2];
			System.arraycopy(data, 2, packet, 0, packet.length);
			p.skinPacket = packet;
		}
	}
	
	public static boolean isNewSkin(int id) {
		return !(id == 0 || id == 2 || id == 4 || id == 6 || id == 8 || id == 10 || id == 12 || id == 14 || id == 18 || id == 28) && !isHighPoly(id);
	}
	
	public static boolean isAlexSkin(int id) {
		return id < defaultVanillaSkinClassicOrSlimVariants.length && defaultVanillaSkinClassicOrSlimVariants[id];
	}
	
	public static boolean isStandardModel(int id) {
		return !isZombieModel(id) && !(id == 19 || id == 20 || id == 21 || id == 32 || id == 33 || id == 34) && !isHighPoly(id);
	}
	
	public static boolean isZombieModel(int id) {
		return id == 18 || id == 28;
	}
	
	public static boolean isHighPoly(int id) {
		return !(defaultVanillaSkins.length > id && id >= 0) ? false : defaultHighPoly[id] != null;
	}
	
	public static boolean isPlayerNewSkin(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId <= -1) {
				int type = EaglerProfile.getSkinSize(EaglerProfile.skins.get(EaglerProfile.customSkinId).data.length);
				return (type == 1 || type == 3);
			}else {
				return isNewSkin(EaglerProfile.presetSkinId);
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				if(pp.skinPacket[0] != (byte)4) {
					return (pp.skinPacket[0] == (byte)1) || (pp.skinPacket[0] == (byte)3) || (pp.skinPacket[0] == (byte)5) || (pp.skinPacket[0] == (byte)6);
				}else {
					return isNewSkin((int)pp.skinPacket[1] & 0xFF);
				}
			}
		}
		return false;
	}
	
	public static boolean isPlayerNewSkinSlim(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId == -1) {
				return EaglerProfile.skins.get(EaglerProfile.customSkinId).slim;
			}else {
				return isAlexSkin(EaglerProfile.presetSkinId);
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				if(pp.skinPacket[0] != (byte)4) {
					return (pp.skinPacket[0] == (byte)5) || (pp.skinPacket[0] == (byte)6);
				}else {
					return isAlexSkin((int)pp.skinPacket[1] & 0xFF);
				}
			}
		}
		return false;
	}
	
	public static boolean isPlayerHighPoly(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId == -1) {
				return false;
			}else {
				return isHighPoly(EaglerProfile.presetSkinId);
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				if(pp.skinPacket[0] != (byte)4) {
					return false;
				}else {
					return isHighPoly((int)pp.skinPacket[1] & 0xFF);
				}
			}
		}
		return false;
	}
	
	public static boolean isPlayerStandard(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId == -1) {
				return true;
			}else {
				return isStandardModel(EaglerProfile.presetSkinId);
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				if(pp.skinPacket[0] != (byte)4) {
					return true;
				}else {
					return isStandardModel((int)pp.skinPacket[1] & 0xFF);
				}
			}
		}
		return true;
	}
	
	public static int getPlayerRenderer(EntityPlayer p) {
		if(p instanceof EntityClientPlayerMP) {
			if(EaglerProfile.presetSkinId == -1) {
				return 0;
			}else {
				return EaglerProfile.presetSkinId;
			}
		}else if(p instanceof EntityOtherPlayerMP) {
			EntityOtherPlayerMP pp = (EntityOtherPlayerMP) p;
			if(pp.skinPacket != null) {
				if(pp.skinPacket[0] != (byte)4) {
					return 0;
				}else {
					if(((int)pp.skinPacket[1] & 0xFF) >= DefaultSkinRenderer.defaultVanillaSkins.length) {
						return 0;
					}else {
						return (int)pp.skinPacket[1] & 0xFF;
					}
				}
			}
		}
		return 0;
	}

	public static ModelBiped oldSkinRenderer = null;
	public static ModelBipedNewSkins newSkinRenderer = null;
	public static ModelBipedNewSkins newSkinRendererSlim = null;
	public static ModelZombie zombieRenderer = null;
	public static ModelVillager villagerRenderer = null;
	public static ModelEnderman endermanRenderer = null;
	public static ModelBlaze blazeRenderer = null;
	public static ModelSkeleton skeletonRenderer = null;
	
	public static void renderPlayerPreview(int x, int y, int mx, int my, int id2) {
		boolean capeMode = (id2 & 0x10000) == 0x10000;
		if(capeMode) {
			id2 -= 0x10000;
		}
		int id = id2 - EaglerProfile.skins.size();
		boolean highPoly = isHighPoly(id);
		
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		if(highPoly) {
			EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		}else {
			EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		}
		EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) x, (float) (y - 80), 100.0F);
		EaglerAdapter.glScalef(50.0f, 50.0f, 50.0f);
		EaglerAdapter.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		EaglerAdapter.glTranslatef(0.0F, 1.0F, 0.0F);
		if(capeMode) {
			EaglerAdapter.glRotatef(140.0f, 0.0f, 1.0f, 0.0f);
			mx = x - (x - mx) - 20;
			EaglerAdapter.glRotatef(((y - my) * -0.02f), 1.0f, 0.0f, 0.0f);
		}else {
			EaglerAdapter.glRotatef(((y - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		}
		EaglerAdapter.glRotatef(((x - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		EaglerAdapter.glTranslatef(0.0F, -1.0F, 0.0F);
		
		if(highPoly) {
			EaglerAdapter.flipLightMatrix();
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
			EaglerAdapter.glTranslatef(0.0f, -1.5f, 0.0f);
			EaglerAdapter.glScalef(HighPolySkin.highPolyScale, HighPolySkin.highPolyScale, HighPolySkin.highPolyScale);
			HighPolySkin msh = defaultHighPoly[id];
			msh.texture.bindTexture();
			
			if(msh.bodyModel != null) {
				EaglerAdapter.drawHighPoly(msh.bodyModel.getModel());
			}
			
			if(msh.headModel != null) {
				EaglerAdapter.drawHighPoly(msh.headModel.getModel());
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
					if(offset != 0.0f || msh.limbsInitialRotation != 0.0f) {
						EaglerAdapter.glPushMatrix();
						if(offset != 0.0f) {
							EaglerAdapter.glTranslatef(0.0f, offset, 0.0f);
						}
						if(msh.limbsInitialRotation != 0.0f) {
							EaglerAdapter.glRotatef(msh.limbsInitialRotation, 1.0f, 0.0f, 0.0f);
						}
					}
					
					EaglerAdapter.drawHighPoly(msh.limbsModel[i].getModel());
					
					if(offset != 0.0f || msh.limbsInitialRotation != 0.0f) {
						EaglerAdapter.glPopMatrix();
					}
				}
			}

			EaglerAdapter.glPopMatrix();
			EaglerAdapter.flipLightMatrix();
		}else {
			if(id < 0) {
				Minecraft.getMinecraft().renderEngine.bindTexture(EaglerProfile.skins.get(id2).glTex);
			}else {
				defaultVanillaSkins[id].bindTexture();
			}
			
			boolean gonnaShowCape = false;
			if(isStandardModel(id) || id < 0) {
				if(oldSkinRenderer == null) oldSkinRenderer = new ModelBiped(0.0F, 0.0F, 64, 32);
				if(newSkinRenderer == null) newSkinRenderer = new ModelBipedNewSkins(0.0F, false);
				if(newSkinRendererSlim == null) newSkinRendererSlim = new ModelBipedNewSkins(0.0F, true);
				oldSkinRenderer.isChild = false;
				newSkinRenderer.isChild = false;
				newSkinRendererSlim.isChild = false;
				boolean isNew = isNewSkin(id);
				if(id < 0) {
					int type = EaglerProfile.getSkinSize(EaglerProfile.skins.get(id2).data.length);
					isNew = (type == 1 || type == 3);
				}
				if(isNew) {
					if((id < 0 && EaglerProfile.skins.get(id2).slim) || (id >= 0 && isAlexSkin(id))) {
						newSkinRendererSlim.blockTransparentSkin = true;
						newSkinRendererSlim.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
						newSkinRendererSlim.blockTransparentSkin = false;
					}else {
						newSkinRenderer.blockTransparentSkin = true;
						newSkinRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
						newSkinRenderer.blockTransparentSkin = false;
					}
				}else {
					oldSkinRenderer.blockTransparentSkin = true;
					oldSkinRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
					oldSkinRenderer.blockTransparentSkin = false;
				}
				gonnaShowCape = capeMode;
			}else if(isZombieModel(id)) {
				if(zombieRenderer == null) zombieRenderer = new ModelZombie(0.0F, true);
				zombieRenderer.isChild = false;
				zombieRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
				gonnaShowCape = capeMode;
			}else if(id == 32) {
				if(villagerRenderer == null) villagerRenderer = new ModelVillager(0.0F);
				villagerRenderer.isChild = false;
				villagerRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
			}else if(id == 19) {
				if(endermanRenderer == null) endermanRenderer = new ModelEnderman();
				endermanRenderer.isChild = false;
				endermanRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
				EaglerAdapter.glColor4f(1.4f, 1.4f, 1.4f, 1.0f);
				//EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				//EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
				//EaglerAdapter.glBlendFunc(EaglerAdapter.GL_ONE, EaglerAdapter.GL_ONE);
				EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
				EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
				EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
				RenderEnderman.tex_eyes.bindTexture();
				endermanRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
				EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
				EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
				EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
				EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}else if(id == 20) {
				if(skeletonRenderer == null) skeletonRenderer = new ModelSkeleton(0.0F);
				skeletonRenderer.isChild = false;
				skeletonRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
			}else if(id == 21) {
				if(blazeRenderer == null) blazeRenderer = new ModelBlaze();
				blazeRenderer.isChild = false;
				EaglerAdapter.glColor4f(1.5f, 1.5f, 1.5f, 1.0f);
				blazeRenderer.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
			}
			if(gonnaShowCape && !(EaglerProfile.presetCapeId >= 0 && defaultVanillaCapes[EaglerProfile.presetCapeId] == null)) {
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(0.0F, 0.0F, 0.150F);
				EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				EaglerAdapter.glRotatef(-6.0F, 1.0F, 0.0F, 0.0F);
				
				if(EaglerProfile.presetCapeId < 0) {
					Minecraft.getMinecraft().renderEngine.bindTexture(EaglerProfile.capes.get(EaglerProfile.customCapeId).glTex);
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glScalef(2.0f, 1.0f, 1.0f);
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				}else {
					defaultVanillaCapes[EaglerProfile.presetCapeId].bindTexture();
				}

				if(oldSkinRenderer == null) oldSkinRenderer = new ModelBiped(0.0F, 0.0F, 64, 32);
				oldSkinRenderer.bipedCloak.render(0.0625F);
				
				if(EaglerProfile.presetCapeId < 0) {
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
					EaglerAdapter.glPopMatrix();
					EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
				}
				
				EaglerAdapter.glPopMatrix();
			}
		}
		
		EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
	}
	
	public static void renderAlexOrSteve(int x, int y, int mx, int my, boolean alex) {
		ModelBipedNewSkins bp;
		if(alex) {
			if(newSkinRendererSlim == null) {
				newSkinRendererSlim = new ModelBipedNewSkins(0.0F, true);
			}
			bp = newSkinRendererSlim;
		}else {
			if(newSkinRenderer == null) {
				newSkinRenderer = new ModelBipedNewSkins(0.0F, false);
			}
			bp = newSkinRenderer;
		}
		
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) x, (float) (y - 80), 100.0F);
		EaglerAdapter.glScalef(50.0f, 50.0f, 50.0f);
		EaglerAdapter.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glScalef(1.0F, -1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		EaglerAdapter.glTranslatef(0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(((y - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		EaglerAdapter.glRotatef(((x - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		EaglerAdapter.glTranslatef(0.0F, -1.0F, 0.0F);
		
		bp.isChild = false;
		bp.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((x - mx) * 0.06f), ((y - my) * -0.1f), 0.0625F);
		
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
	}

	public static boolean isPlayerPreviewNew(int id2) {
		int id = id2 - EaglerProfile.skins.size();
		if(id < 0) {
			return EaglerProfile.skins.get(id2).data.length == EaglerProfile.SKIN_DATA_SIZE[1] || EaglerProfile.skins.get(id2).data.length == EaglerProfile.SKIN_DATA_SIZE[3];
		}else {
			return false;
		}
	}
	
}
