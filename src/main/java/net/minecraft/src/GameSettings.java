package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.LocalStorageManager;
import net.minecraft.client.Minecraft;

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[] { "options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny" };
	private static final String[] DIFFICULTIES = new String[] { "options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard" };

	/** GUI scale values */
	private static final String[] GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
	private static final String[] CHAT_VISIBILITIES = new String[] { "options.chat.visibility.full", "options.chat.visibility.system", "options.chat.visibility.hidden" };
	private static final String[] PARTICLES = new String[] { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };

	/** Limit framerate labels */
	private static final String[] LIMIT_FRAMERATES = new String[] { "performance.max", "performance.balanced", "performance.powersaver" };
	private static final String[] AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min", "options.ao.max" };
	private static final String[] ANTIALIASING = new String[] { "options.framebufferAntialias.none", "options.framebufferAntialias.auto", "options.framebufferAntialias.fxaa" , "options.framebufferAntialias.msaa4", "options.framebufferAntialias.msaa8" };
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 2;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;

	/** Advanced OpenGL */
	public boolean advancedOpengl = false;
	public int limitFramerate = 0;
	public boolean fancyGraphics = false;
	public boolean enableFog = true;

	/** Smooth Lighting */
	public int ambientOcclusion = 0;

	/** Clouds flag */
	public boolean clouds = false;

	/** The name of the selected texture pack. */
	public String skin = "Default";
	public int chatVisibility = 0;
	public boolean chatColours = true;
	public boolean chatLinks = true;
	public boolean chatLinksPrompt = true;
	public float chatOpacity = 1.0F;
	public boolean serverTextures = true;
	public boolean snooperEnabled = false;
	public boolean fullScreen = false;
	public boolean enableVsync = true;
	public boolean hideServerAddress = false;

	/**
	 * Whether to show advanced information on item tooltips, toggled by F3+H
	 */
	public boolean advancedItemTooltips = false;

	/** Whether to pause when the game loses focus, toggled by F3+P */
	public boolean pauseOnLostFocus = true;

	/** Whether to show your cape */
	public boolean showCape = true;
	public boolean touchscreen = false;
	public int antialiasMode = 1;
	public int overrideWidth = 0;
	public int overrideHeight = 0;
	public boolean heldItemTooltips = true;
	public float chatScale = 1.0F;
	public float chatWidth = 1.0F;
	public float chatHeightUnfocused = 0.44366196F;
	public float chatHeightFocused = 1.0F;
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100);
	public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99);
	public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15);
	public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98);
	public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 19);
	public KeyBinding keyBindZoom = new KeyBinding("key.zoom", 46);
	public KeyBinding keyBindFunction = new KeyBinding("key.function", 33);
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	public int difficulty;
	public boolean hideGUI;
	public int thirdPersonView;

	/** true if debug info should be displayed instead of version */
	public boolean showDebugInfo;
	public boolean showDebugProfilerChart;
	
	public boolean showCoordinates;

	/** The lastServer string. */
	public String lastServer;

	/** No clipping for singleplayer */
	public boolean noclip;

	/** Smooth Camera Toggle */
	public boolean smoothCamera;
	public boolean debugCamEnable;

	/** No clipping movement rate */
	public float noclipRate;

	/** Change rate for debug camera */
	public float debugCamRate;
	public float fovSetting;
	public float gammaSetting;

	/** GUI scale */
	public int guiScale;

	/** Determines amount of particles. 0 = All, 1 = Decreased, 2 = Minimal */
	public int particleSetting;

	/** Game settings language */
	public String language;

	public boolean showSkinJacket = true;
	public boolean showSkinHat = true;
	public boolean showSkinLeftArm = true;
	public boolean showSkinRightArm = true;
	public boolean showSkinLeftLeg = true;
	public boolean showSkinRightLeg = true;

	public boolean allowFNAWSkins = true;
	public boolean showOtherCapes = true;

	public int chunkUpdatePerFrame = 0;

	public int voiceListenRadius = 16;
	public float voiceListenVolume = 0.5f;
	public float voiceSpeakVolume = 0.5f;
	public int voicePTTKey = 47;
	
	public boolean hideJoinCode = false;
	public int relayTimeout = 4;
	
	public boolean adderall = false;

	public GameSettings(Minecraft par1Minecraft) {
		this.keyBindings = new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory,
				this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindSprint, this.keyBindZoom, this.keyBindFunction };
		this.difficulty = 1;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.showDebugProfilerChart = true;
		this.showCoordinates = true;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 1.0F;
		this.guiScale = 3;
		this.particleSetting = 0;
		this.language = "en_US";
		this.mc = par1Minecraft;
		this.loadOptions();
	}

	public String getKeyBindingDescription(int par1) {
		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(this.keyBindings[par1].keyDescription);
	}

	/**
	 * The string that appears inside the button/slider in the options menu.
	 */
	public String getOptionDisplayString(int par1) {
		int var2 = this.keyBindings[par1].keyCode;
		return getKeyDisplayString(var2);
	}

	/**
	 * Represents a key or mouse button as a string. Args: key
	 */
	public static String getKeyDisplayString(int par0) {
		return par0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[] { Integer.valueOf(par0 + 101) }) : EaglerAdapter.getKeyName(par0);
	}

	/**
	 * Returns whether the specified key binding is currently being pressed.
	 */
	public static boolean isKeyDown(KeyBinding par0KeyBinding) {
		return par0KeyBinding.keyCode < 0 ? EaglerAdapter.mouseIsButtonDown(par0KeyBinding.keyCode + 100) : EaglerAdapter.isKeyDown(par0KeyBinding.keyCode);
	}

	/**
	 * Sets a key binding.
	 */
	public void setKeyBinding(int par1, int par2) {
		this.keyBindings[par1].keyCode = par2;
		this.saveOptions();
	}

	/**
	 * If the specified option is controlled by a slider (float value), this will
	 * set the float value.
	 */
	public void setOptionFloatValue(EnumOptions par1EnumOptions, float par2) {
		if (par1EnumOptions == EnumOptions.MUSIC) {
			this.musicVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (par1EnumOptions == EnumOptions.SOUND) {
			this.soundVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (par1EnumOptions == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = par2;
		}

		if (par1EnumOptions == EnumOptions.FOV) {
			this.fovSetting = par2;
		}

		if (par1EnumOptions == EnumOptions.GAMMA) {
			this.gammaSetting = par2;
		}

		if (par1EnumOptions == EnumOptions.CHAT_OPACITY) {
			this.chatOpacity = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED) {
			this.chatHeightFocused = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED) {
			this.chatHeightUnfocused = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_WIDTH) {
			this.chatWidth = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_SCALE) {
			this.chatScale = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}
	}

	/**
	 * For non-float options. Toggles the option on/off, or cycles through the list
	 * i.e. render distances.
	 */
	public void setOptionValue(EnumOptions par1EnumOptions, int par2) {
		if (par1EnumOptions == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if (par1EnumOptions == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + par2 & 3;
			if(this.mc.isSingleplayerOrLAN()) {
				Minecraft.getMinecraft().displayEaglercraftText("changing render distance in singleplayer may take a few seconds, this is not a glitch plz don't report");
			}
		}

		if (par1EnumOptions == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + par2 & 3;
		}

		if (par1EnumOptions == EnumOptions.PARTICLES) {
			this.particleSetting = (this.particleSetting + par2) % 3;
		}

		if (par1EnumOptions == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if (par1EnumOptions == EnumOptions.RENDER_CLOUDS) {
			this.clouds = !this.clouds;
		}

		if (par1EnumOptions == EnumOptions.ENABLE_FOG) {
			this.enableFog = !this.enableFog;
		}

		if (par1EnumOptions == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + par2 + 3) % 3;
		}

		if (par1EnumOptions == EnumOptions.CHUNK_UPDATES) {
			this.chunkUpdatePerFrame = (this.chunkUpdatePerFrame + par2) % 5;
		}

		if (par1EnumOptions == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + par2 & 3;
		}

		if (par1EnumOptions == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = (this.ambientOcclusion + par2) % 3;
			this.mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == EnumOptions.CHAT_VISIBILITY) {
			this.chatVisibility = (this.chatVisibility + par2) % 3;
		}

		if (par1EnumOptions == EnumOptions.CHAT_COLOR) {
			this.chatColours = !this.chatColours;
		}

		if (par1EnumOptions == EnumOptions.CHAT_LINKS) {
			this.chatLinks = !this.chatLinks;
		}

		if (par1EnumOptions == EnumOptions.CHAT_LINKS_PROMPT) {
			this.chatLinksPrompt = !this.chatLinksPrompt;
		}

		if (par1EnumOptions == EnumOptions.USE_SERVER_TEXTURES) {
			this.serverTextures = !this.serverTextures;
		}

		if (par1EnumOptions == EnumOptions.SNOOPER_ENABLED) {
			this.snooperEnabled = !this.snooperEnabled;
		}

		if (par1EnumOptions == EnumOptions.SHOW_CAPE) {
			this.showCape = !this.showCape;
		}

		if (par1EnumOptions == EnumOptions.ANTIALIASING) {
			this.antialiasMode = (this.antialiasMode + par2) % 5;
		}

		if (par1EnumOptions == EnumOptions.USE_FULLSCREEN) {
			this.fullScreen = !this.fullScreen;

			if (this.mc.isFullScreen() != this.fullScreen) {
				this.mc.toggleFullscreen();
			}
		}
		
		if (par1EnumOptions == EnumOptions.ADDERALL) {
			this.adderall = !this.adderall;
			this.mc.sndManager.stopAllSounds();
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions par1EnumOptions) {
		return par1EnumOptions == EnumOptions.FOV ? this.fovSetting
				: (par1EnumOptions == EnumOptions.GAMMA ? this.gammaSetting
						: (par1EnumOptions == EnumOptions.MUSIC ? this.musicVolume
								: (par1EnumOptions == EnumOptions.SOUND ? this.soundVolume
										: (par1EnumOptions == EnumOptions.SENSITIVITY ? this.mouseSensitivity
												: (par1EnumOptions == EnumOptions.CHAT_OPACITY ? this.chatOpacity
														: (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused
																: (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused
																		: (par1EnumOptions == EnumOptions.CHAT_SCALE ? this.chatScale : (par1EnumOptions == EnumOptions.CHAT_WIDTH ? this.chatWidth : 0.0F)))))))));
	}

	public boolean getOptionOrdinalValue(EnumOptions par1EnumOptions) {
		switch (EnumOptionsHelper.enumOptionsMappingHelperArray[par1EnumOptions.ordinal()]) {
		case 1:
			return this.invertMouse;

		case 2:
			return this.viewBobbing;

		case 3:
			return this.anaglyph;

		case 4:
			return this.advancedOpengl;

		case 5:
			return this.clouds;

		case 6:
			return this.chatColours;

		case 7:
			return this.chatLinks;

		case 8:
			return this.chatLinksPrompt;

		case 9:
			return this.serverTextures;

		case 10:
			return this.snooperEnabled;

		case 11:
			return this.fullScreen;

		case 13:
			return this.showCape;

		case 14:
			return this.touchscreen;
			
		case 15:
			return this.enableFog;
			
		case 17:
			return this.adderall;

		default:
			return false;
		}
	}

	/**
	 * Returns the translation of the given index in the given String array. If the
	 * index is smaller than 0 or greater than/equal to the length of the String
	 * array, it is changed to 0.
	 */
	private static String getTranslation(String[] par0ArrayOfStr, int par1) {
		if (par1 < 0 || par1 >= par0ArrayOfStr.length) {
			par1 = 0;
		}

		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(par0ArrayOfStr[par1]);
	}

	/**
	 * Gets a key binding.
	 */
	public String getKeyBinding(EnumOptions par1EnumOptions) {
		StringTranslate var2 = StringTranslate.getInstance();
		String var3 = var2.translateKey(par1EnumOptions.getEnumString()) + ": ";

		if (par1EnumOptions.getEnumFloat()) {
			float var5 = this.getOptionFloatValue(par1EnumOptions);
			return par1EnumOptions == EnumOptions.SENSITIVITY ? (var5 == 0.0F ? var3 + var2.translateKey("options.sensitivity.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.sensitivity.max") : var3 + (int) (var5 * 200.0F) + "%"))
					: (par1EnumOptions == EnumOptions.FOV ? (var5 == 0.0F ? var3 + var2.translateKey("options.fov.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.fov.max") : var3 + (int) (70.0F + var5 * 40.0F)))
							: (par1EnumOptions == EnumOptions.GAMMA ? (var5 == 0.0F ? var3 + var2.translateKey("options.gamma.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.gamma.max") : var3 + "+" + (int) (var5 * 100.0F) + "%"))
									: (par1EnumOptions == EnumOptions.CHAT_OPACITY ? var3 + (int) (var5 * 90.0F + 10.0F) + "%"
											: (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? var3 + GuiNewChat.func_96130_b(var5) + "px"
													: (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? var3 + GuiNewChat.func_96130_b(var5) + "px"
															: (par1EnumOptions == EnumOptions.CHAT_WIDTH ? var3 + GuiNewChat.func_96128_a(var5) + "px"
																	: (var5 == 0.0F ? var3 + var2.translateKey("options.off") : var3 + (int) (var5 * 100.0F) + "%")))))));
		} else if (par1EnumOptions.getEnumBoolean()) {
			boolean var4 = this.getOptionOrdinalValue(par1EnumOptions);
			return var4 ? var3 + var2.translateKey("options.on") : var3 + var2.translateKey("options.off");
		} else {
			return par1EnumOptions == EnumOptions.RENDER_DISTANCE ? var3 + getTranslation(RENDER_DISTANCES, this.renderDistance)
					: (par1EnumOptions == EnumOptions.DIFFICULTY ? var3 + getTranslation(DIFFICULTIES, this.difficulty)
					: (par1EnumOptions == EnumOptions.GUI_SCALE ? var3 + getTranslation(GUISCALES, this.guiScale)
					: (par1EnumOptions == EnumOptions.CHAT_VISIBILITY ? var3 + getTranslation(CHAT_VISIBILITIES, this.chatVisibility)
					: (par1EnumOptions == EnumOptions.PARTICLES ? var3 + getTranslation(PARTICLES, this.particleSetting)
					: (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT ? var3 + getTranslation(LIMIT_FRAMERATES, this.limitFramerate)
					: (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION ? var3 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion)
					: (par1EnumOptions == EnumOptions.ANTIALIASING ? var3 + getTranslation(ANTIALIASING, this.antialiasMode)
					: (par1EnumOptions == EnumOptions.CHUNK_UPDATES ? var3 + (chunkUpdatePerFrame + 1)
					: (par1EnumOptions == EnumOptions.GRAPHICS ? (this.fancyGraphics ? var3 + var2.translateKey("options.graphics.fancy") : var3 + var2.translateKey("options.graphics.fast"))
							: var3)))))))));
		}
	}

	/**
	 * Loads the options from the options file. It appears that this has replaced
	 * the previous 'loadOptions'
	 */
	public void loadOptions() {
		NBTTagCompound yee = LocalStorageManager.gameSettingsStorage;
		if(!yee.hasNoTags()) {
			if(yee.hasKey("musicVolume")) this.musicVolume = yee.getFloat("musicVolume");
			if(yee.hasKey("soundVolume")) this.soundVolume = yee.getFloat("soundVolume");
			if(yee.hasKey("sensitivity")) this.mouseSensitivity = yee.getFloat("sensitivity");
			if(yee.hasKey("fov")) this.fovSetting = yee.getFloat("fov");
			if(yee.hasKey("gamma")) this.gammaSetting = yee.getFloat("gamma");
			if(yee.hasKey("invertMouse")) this.invertMouse = yee.getBoolean("invertMouse");
			if(yee.hasKey("viewDistance")) this.renderDistance = yee.getInteger("viewDistance");
			if(yee.hasKey("guiScale")) this.guiScale = yee.getInteger("guiScale");
			if(yee.hasKey("particles")) this.particleSetting = yee.getInteger("particles");
			if(yee.hasKey("viewBobbing")) this.viewBobbing = yee.getBoolean("viewBobbing");
			if(yee.hasKey("anaglyph")) this.anaglyph = yee.getBoolean("anaglyph");
			if(yee.hasKey("limitFramerate")) this.limitFramerate = yee.getInteger("limitFramerate");
			if(yee.hasKey("fancyGraphics")) this.fancyGraphics = yee.getBoolean("fancyGraphics");
			if(yee.hasKey("ambientOcclusion")) this.ambientOcclusion = yee.getInteger("ambientOcclusion");
			if(yee.hasKey("clouds")) this.clouds = yee.getBoolean("clouds");
			if(yee.hasKey("fog")) this.enableFog = yee.getBoolean("fog");
			if(yee.hasKey("lastServer")) this.lastServer = yee.getString("lastServer");
			if(yee.hasKey("language")) this.language = yee.getString("language");
			if(yee.hasKey("chatVisibility")) this.chatVisibility = yee.getInteger("chatVisibility");
			if(yee.hasKey("chatColours")) this.chatColours = yee.getBoolean("chatColours");
			if(yee.hasKey("chatLinks")) this.chatLinks = yee.getBoolean("chatLinks");
			if(yee.hasKey("chatLinksPrompt")) this.chatLinksPrompt = yee.getBoolean("chatLinksPrompt");
			if(yee.hasKey("chatOpacity")) this.chatOpacity = yee.getFloat("chatOpacity");
			if(yee.hasKey("fullScreen")) this.fullScreen = yee.getBoolean("fullScreen");
			if(yee.hasKey("hideServerAddress")) this.hideServerAddress = yee.getBoolean("hideServerAddress");
			if(yee.hasKey("advancedTooltips")) this.advancedItemTooltips = yee.getBoolean("advancedTooltips");
			if(yee.hasKey("pauseOnLostFocus")) this.pauseOnLostFocus = yee.getBoolean("pauseOnLostFocus");
			if(yee.hasKey("showCape")) this.showCape = yee.getBoolean("showCape");
			if(yee.hasKey("touchscreen")) this.touchscreen = yee.getBoolean("touchscreen");
			if(yee.hasKey("forceHeight")) this.overrideHeight = yee.getInteger("forceHeight");
			if(yee.hasKey("forceWidth")) this.overrideWidth = yee.getInteger("forceWidth");
			if(yee.hasKey("antialiasMode")) this.antialiasMode = yee.getInteger("antialiasMode");
			if(yee.hasKey("heldItemTooltips")) this.heldItemTooltips = yee.getBoolean("heldItemTooltips");
			if(yee.hasKey("chatHeightFocused")) this.chatHeightFocused = yee.getFloat("chatHeightFocused");
			if(yee.hasKey("chatHeightUnfocused")) this.chatHeightUnfocused = yee.getFloat("chatHeightUnfocused");
			if(yee.hasKey("chatScale")) this.chatScale = yee.getFloat("chatScale");
			if(yee.hasKey("chatWidth")) this.chatWidth = yee.getFloat("chatWidth");
			if(yee.hasKey("showCoordinates")) this.showCoordinates = yee.getBoolean("showCoordinates");
			if(yee.hasKey("showSkinJacket")) showSkinJacket = yee.getBoolean("showSkinJacket");
			if(yee.hasKey("showSkinHat")) showSkinHat = yee.getBoolean("showSkinHat");
			if(yee.hasKey("showSkinLeftArm")) showSkinLeftArm = yee.getBoolean("showSkinLeftArm");
			if(yee.hasKey("showSkinRightArm")) showSkinRightArm = yee.getBoolean("showSkinRightArm");
			if(yee.hasKey("showSkinLeftLeg")) showSkinLeftLeg = yee.getBoolean("showSkinLeftLeg");
			if(yee.hasKey("showSkinRightLeg")) showSkinRightLeg = yee.getBoolean("showSkinRightLeg");
			if(yee.hasKey("allowFNAWSkins")) allowFNAWSkins = yee.getBoolean("allowFNAWSkins");
			if(yee.hasKey("showOtherCapes")) showOtherCapes = yee.getBoolean("showOtherCapes");
			if(yee.hasKey("chunkUpdates")) chunkUpdatePerFrame = yee.getInteger("chunkUpdates");
			if(yee.hasKey("voiceListenVolume")) voiceListenVolume = yee.getFloat("voiceListenVolume");
			if(yee.hasKey("voiceSpeakVolume")) voiceSpeakVolume = yee.getFloat("voiceSpeakVolume");
			if(yee.hasKey("voicePTTKey")) voicePTTKey = yee.getInteger("voicePTTKey");
			if(yee.hasKey("voiceListenRadius")) voiceListenRadius = yee.getInteger("voiceListenRadius");
			if(yee.hasKey("difficulty")) difficulty = yee.getByte("difficulty");
			if(yee.hasKey("hideJoinCode")) hideJoinCode = yee.getBoolean("hideJoinCode");
			if(yee.hasKey("relayTimeout")) relayTimeout = yee.getByte("relayTimeout");
			if(yee.hasKey("adderall")) adderall = yee.getBoolean("adderall");
			
			if(voiceListenRadius < 5) voiceListenRadius = 5;
			else if(voiceListenRadius > 22) voiceListenRadius = 22;

			EaglerAdapter.setVoiceListenVolume(voiceListenVolume);
			EaglerAdapter.setVoiceSpeakVolume(voiceSpeakVolume);
			EaglerAdapter.setVoiceProximity(voiceListenRadius);
			
			for (int var4 = 0; var4 < this.keyBindings.length; ++var4) {
				if(yee.hasKey(keyBindings[var4].keyDescription)) this.keyBindings[var4].keyCode = yee.getInteger(keyBindings[var4].keyDescription);
			}
	
			KeyBinding.resetKeyBindingArrayAndHash();

			IntegratedServer.relayManager.load(LocalStorageManager.gameSettingsStorage.getTagList("relays"));
		}else {
			IntegratedServer.relayManager.load(null);
		}
	}

	/**
	 * Saves the options to the options file.
	 */
	public void saveOptions() {
		NBTTagCompound yee = LocalStorageManager.gameSettingsStorage;
		
		yee.setFloat("musicVolume", this.musicVolume);
		yee.setFloat("soundVolume", this.soundVolume);
		yee.setFloat("sensitivity", this.mouseSensitivity);
		yee.setFloat("fov", this.fovSetting);
		yee.setFloat("gamma", this.gammaSetting);
		yee.setBoolean("invertMouse", this.invertMouse);
		yee.setInteger("viewDistance", this.renderDistance);
		yee.setInteger("guiScale", this.guiScale);
		yee.setInteger("particles", this.particleSetting);
		yee.setBoolean("viewBobbing", this.viewBobbing);
		yee.setBoolean("anaglyph", this.anaglyph);
		yee.setInteger("limitFramerate", this.limitFramerate);
		yee.setBoolean("fancyGraphics", this.fancyGraphics);
		yee.setInteger("ambientOcclusion", this.ambientOcclusion);
		yee.setBoolean("clouds", this.clouds);
		yee.setBoolean("fog", this.enableFog);
		yee.setString("lastServer", this.lastServer);
		yee.setString("language", this.language);
		yee.setInteger("chatVisibility", this.chatVisibility);
		yee.setBoolean("chatColours", this.chatColours);
		yee.setBoolean("chatLinks", this.chatLinks);
		yee.setBoolean("chatLinksPrompt", this.chatLinksPrompt);
		yee.setFloat("chatOpacity", this.chatOpacity);
		yee.setBoolean("fullScreen", this.fullScreen);
		yee.setBoolean("hideServerAddress", this.hideServerAddress);
		yee.setBoolean("advancedTooltips", this.advancedItemTooltips);
		yee.setBoolean("pauseOnLostFocus", this.pauseOnLostFocus);
		yee.setBoolean("showCape", this.showCape);
		yee.setBoolean("touchscreen", this.touchscreen);
		yee.setInteger("forceHeight", this.overrideHeight);
		yee.setInteger("forceWidth", this.overrideWidth);
		yee.setInteger("antialiasMode", this.antialiasMode);
		yee.setBoolean("heldItemTooltips", this.heldItemTooltips);
		yee.setFloat("chatHeightFocused", this.chatHeightFocused);
		yee.setFloat("chatHeightUnfocused", this.chatHeightUnfocused);
		yee.setFloat("chatScale", this.chatScale);
		yee.setFloat("chatWidth", this.chatWidth);
		yee.setBoolean("showCoordinates", this.showCoordinates);
		yee.setBoolean("showSkinJacket", showSkinJacket);
		yee.setBoolean("showSkinHat", showSkinHat);
		yee.setBoolean("showSkinLeftArm", showSkinLeftArm);
		yee.setBoolean("showSkinRightArm", showSkinRightArm);
		yee.setBoolean("showSkinLeftLeg", showSkinLeftLeg);
		yee.setBoolean("showSkinRightLeg", showSkinRightLeg);
		yee.setBoolean("allowFNAWSkins", allowFNAWSkins);
		yee.setBoolean("showOtherCapes", showOtherCapes);
		yee.setInteger("chunkUpdates", chunkUpdatePerFrame);
		yee.setFloat("voiceListenVolume", voiceListenVolume);
		yee.setFloat("voiceSpeakVolume", voiceSpeakVolume);
		yee.setInteger("voicePTTKey", voicePTTKey);
		yee.setInteger("voiceListenRadius", voiceListenRadius);
		yee.setByte("difficulty", (byte)difficulty);
		yee.setBoolean("hideJoinCode", hideJoinCode);
		yee.setByte("relayTimeout", (byte)relayTimeout);
		yee.setBoolean("adderall", adderall);
		
		for (int var4 = 0; var4 < this.keyBindings.length; ++var4) {
			yee.setInteger(keyBindings[var4].keyDescription, keyBindings[var4].keyCode);
		}
		
		LocalStorageManager.saveStorageG();

		this.sendSettingsToServer();
	}

	/**
	 * Send a client info packet with settings information to the server
	 */
	public void sendSettingsToServer() {
		if (this.mc.thePlayer != null) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new Packet204ClientInfo(this.language, this.renderDistance, this.chatVisibility, this.chatColours, this.difficulty, this.showCape));
			this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload("EAG|SkinLayers", new byte[] { (byte)getSkinLayers() }));
		}
	}
	
	public int getSkinLayers() {
		int skinLayersByte = 0;
		if(showSkinJacket) skinLayersByte |= 1;
		if(showSkinHat) skinLayersByte |= 2;
		if(showSkinLeftArm) skinLayersByte |= 4;
		if(showSkinRightArm) skinLayersByte |= 8;
		if(showSkinLeftLeg) skinLayersByte |= 16;
		if(showSkinRightLeg) skinLayersByte |= 32;
		return skinLayersByte;
	}

	/**
	 * Should render clouds
	 */
	public boolean shouldRenderClouds() {
		return this.renderDistance < 2 && this.clouds;
	}
}