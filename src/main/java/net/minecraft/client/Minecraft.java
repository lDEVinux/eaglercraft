package net.minecraft.client;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

import net.lax1dude.eaglercraft.DefaultSkinRenderer;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglerProfile;
import net.lax1dude.eaglercraft.GuiScreenEditProfile;
import net.lax1dude.eaglercraft.GuiScreenLicense;
import net.lax1dude.eaglercraft.GuiScreenSingleplayerConnecting;
import net.lax1dude.eaglercraft.GuiScreenSingleplayerLoading;
import net.lax1dude.eaglercraft.GuiVoiceOverlay;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.IntegratedServerLAN;
import net.lax1dude.eaglercraft.LocalStorageManager;
import net.lax1dude.eaglercraft.Voice;
import net.lax1dude.eaglercraft.WorkerNetworkManager;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.lax1dude.eaglercraft.glemu.FixedFunctionShader;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityItemFrame;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.Profiler;
import net.minecraft.src.ProfilerResult;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ServerData;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.Timer;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldSettings;

public class Minecraft implements Runnable {
	
	private ServerData currentServerData;

	/**
	 * Set to 'this' in Minecraft constructor; used by some settings get methods
	 */
	private static Minecraft theMinecraft;
	public PlayerControllerMP playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	private boolean isGonnaTakeDatScreenShot = false;
	
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20.0F);
	
	public WorldClient theWorld;
	public RenderGlobal renderGlobal;
	public EntityClientPlayerMP thePlayer;

	/**
	 * The Entity from which the renderer determines the render viewpoint. Currently
	 * is always the parent Minecraft class's 'thePlayer' instance. Modification of
	 * its location, rotation, or other settings at render time will modify the
	 * camera likewise, with the caveat of triggering chunk rebuilds as it moves,
	 * making it unsuitable for changing the viewpoint mid-render.
	 */
	public EntityLiving renderViewEntity;
	public EntityLiving pointedEntityLiving;
	public EffectRenderer effectRenderer;
	public String minecraftUri;

	/** a boolean to hide a Quit button from the main menu */
	public boolean hideQuitButton = false;
	public volatile boolean isGamePaused = false;

	/** The RenderEngine instance used by Minecraft */
	public RenderEngine renderEngine;

	/** The font renderer used for displaying and measuring text. */
	public FontRenderer fontRenderer;
	public FontRenderer standardGalacticFontRenderer;

	/** The GuiScreen that's being displayed at the moment. */
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;

	/** Mouse left click counter */
	private int leftClickCounter = 0;

	/** Display width */
	private int tempDisplayWidth;

	/** Display height */
	private int tempDisplayHeight;

	/** Gui achievement */
	public GuiAchievement guiAchievement;
	public GuiIngame ingameGUI;

	/** Skip render world */
	public boolean skipRenderWorld = false;

	/** The ray trace hit that the mouse is over. */
	public MovingObjectPosition objectMouseOver = null;

	/** The game settings that currently hold effect. */
	public GameSettings gameSettings;
	public SoundManager sndManager = new SoundManager();

	/** Mouse helper instance. */
	public MouseHelper mouseHelper;

	/** The TexturePackLister used by this instance of Minecraft... */
	public TexturePackList texturePackList;

	/**
	 * This is set to fpsCounter every debug screen update, and is shown on the
	 * debug screen. It's also sent as part of the usage snooping.
	 */
	public static int debugFPS;

	/**
	 * When you place a block, it's set to 6, decremented once per tick, when it's
	 * 0, you can place another block.
	 */
	private int rightClickDelayTimer = 0;

	/**
	 * Checked in Minecraft's while(running) loop, if true it's set to false and the
	 * textures refreshed.
	 */
	private boolean refreshTexturePacksScheduled;
	
	private String serverName;
	private int serverPort;

	/**
	 * Makes sure it doesn't keep taking screenshots when both buttons are down.
	 */
	boolean isTakingScreenshot = false;

	/**
	 * Does the actual gameplay have focus. If so then mouse and keys will effect
	 * the player instead of menus.
	 */
	public boolean inGameHasFocus = false;
	long systemTime = getSystemTime();

	/** Join player counter */
	private int joinPlayerCounter = 0;
	private boolean isDemo;
	private INetworkManager myNetworkManager;
	private boolean integratedServerIsRunning;

	/** The profiler instance */
	public final Profiler mcProfiler = new Profiler();
	private long field_83002_am = -1L;

	public int chunkUpdates = 0;
	public int chunkGeometryUpdates = 0;
	public static int debugChunkUpdates = 0;
	public static int debugChunkGeometryUpdates = 0;

	/**
	 * Set to true to keep the game loop running. Set to false by shutdown() to
	 * allow the game loop to exit cleanly.
	 */
	public volatile boolean running = true;

	/** String that shows the debug information */
	public String debug = "";

	/** Approximate time (in ms) of last update to debug string */
	long debugUpdateTime = getSystemTime();

	/** holds the current fps */
	int fpsCounter = 0;
	long prevFrameTime = -1L;
	
	long secondTimer = 0l;
	
	private HashSet<String> shownPlayerMessages = new HashSet();

	/** Profiler currently displayed in the debug screen pie chart */
	private String debugProfilerName = "root";
	
	public GuiVoiceOverlay voiceOverlay;

	private int messageOnLoginCounter = 0;
	
	public boolean lanState = false;
	public boolean yeeState = false;
	
	public Minecraft() {
		this.tempDisplayHeight = 480;
		this.fullscreen = false;
		Packet3Chat.maxChatLength = 32767;
		this.startTimerHackThread();
		this.displayWidth = 854;
		this.displayHeight = 480;
		this.fullscreen = false;
		theMinecraft = this;
	}

	private void startTimerHackThread() {

	}
	
	public boolean isSingleplayerOrLAN() {
		return IntegratedServer.isWorldRunning();
	}

	public void setServer(String par1Str, int par2) {
		this.serverName = par1Str;
		this.serverPort = par2;
	}

	/**
	 * Starts the game: initializes the canvas, the title, the settings, etcetera.
	 */
	public void startGame() {
		OpenGlHelper.initializeTextures();
		TextureManager.init();
		this.gameSettings = new GameSettings(this);
		this.texturePackList = new TexturePackList(this);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		
		this.loadScreen();
		
		ChatAllowedCharacters.getAllowedCharacters();
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine, false);
		this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, "/font/alternate.png", this.renderEngine, false);

		if (this.gameSettings.language != null) {
			StringTranslate.getInstance().setLanguage(this.gameSettings.language, false);
			//this.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
			//this.fontRenderer.setBidiFlag(StringTranslate.isBidirectional(this.gameSettings.language));
		}
		
		this.loadScreen();

		ColorizerGrass.setGrassBiomeColorizer(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.setFoliageBiomeColorizer(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
		this.entityRenderer = new EntityRenderer(this);
		RenderManager.instance = new RenderManager();
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.mouseHelper = new MouseHelper(this.gameSettings);
		this.checkGLError("Pre startup");
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glShadeModel(EaglerAdapter.GL_SMOOTH);
		EaglerAdapter.glClearDepth(1.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
		EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);
		EaglerAdapter.glCullFace(EaglerAdapter.GL_BACK);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		this.renderEngine.refreshTextureMaps();
		EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		this.checkGLError("Post startup");
		this.guiAchievement = new GuiAchievement(this);
		this.ingameGUI = new GuiIngame(this);
		this.voiceOverlay = new GuiVoiceOverlay(this);

		ScaledResolution var2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		int var3 = var2.getScaledWidth();
		int var4 = var2.getScaledHeight();
		this.voiceOverlay.setResolution(var3, var4);

		EaglerAdapter.anisotropicPatch(EaglerAdapter.glNeedsAnisotropicFix());
		
		EaglerProfile.loadFromStorage();
		
		this.sndManager.playTheTitleMusic();
		showIntroAnimation();
		
		String s = EaglerAdapter.getServerToJoinOnLaunch();
		GuiScreen scr;
		
		if(s != null) {
			scr = new GuiScreenEditProfile(new GuiConnecting(new GuiMainMenu(), this, new ServerData("Eaglercraft Server", s, false)));
		}else {
			scr = new GuiScreenEditProfile(new GuiMainMenu());
		}
		
		if(!LocalStorageManager.profileSettingsStorage.getBoolean("acceptLicense")) {
			scr = new GuiScreenLicense(scr);
		}
		
		displayGuiScreen(scr);

		this.loadingScreen = new LoadingScreenRenderer(this);

		if (this.gameSettings.fullScreen && !this.fullscreen) {
			this.toggleFullscreen();
		}
		
		byte[] b = EaglerAdapter.loadResourceBytes("adderall");
		yeeState = b != null && (new String(b, StandardCharsets.UTF_8)).hashCode() == 508925104;
	}
	
	private void showIntroAnimation() {
		ScaledResolution var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		EaglerAdapter.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glTranslatef(0.0F, 0.0F, -2000.0F);
		EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		long t1 = System.currentTimeMillis();
		for(int i = 0; i < 20; i++) {
			this.displayWidth = EaglerAdapter.getCanvasWidth();
			this.displayHeight = EaglerAdapter.getCanvasHeight();
			EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
			var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glOrtho(0.0F, var1.getScaledWidth(), var1.getScaledHeight(), 0.0F, 1000.0F, 3000.0F);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
			
			float f = ((float)(System.currentTimeMillis() - t1) / 333f);
			
			EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, MathHelper.clamp_float(1.0f - f, 0.0F, 1.0F));
			this.renderEngine.bindTexture("%clamp%/title/eagtek.png");
			EaglerAdapter.glPushMatrix();
			float f1 = 1.0f + 0.025f * f * f;
			EaglerAdapter.glTranslatef((var1.getScaledWidth() - 256) / 2, (var1.getScaledHeight() - 256) / 2, 0.0f);
			EaglerAdapter.glTranslatef(-128.0f * (f1 - 1.0f), -128.0f * (f1 - 1.0f) , 0.0f);
			EaglerAdapter.glScalef(f1, f1, 1.0f);
			this.scaledTessellator(0, 0, 0, 0, 256, 256);
			EaglerAdapter.glPopMatrix();

			EaglerAdapter.glFlush();
			EaglerAdapter.updateDisplay();
			
			long t = t1 + 17 + 17*i - System.currentTimeMillis();
			if(t > 0) {
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		t1 = System.currentTimeMillis();
		for(int i = 0; i < 20; i++) {
			this.displayWidth = EaglerAdapter.getCanvasWidth();
			this.displayHeight = EaglerAdapter.getCanvasHeight();
			EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
			var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glOrtho(0.0F, var1.getScaledWidth(), var1.getScaledHeight(), 0.0F, 1000.0F, 3000.0F);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
			
			float f = ((float)(System.currentTimeMillis() - t1) / 333f);
			
			EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, MathHelper.clamp_float(f, 0.0F, 1.0F));
			this.renderEngine.bindTexture("%blur%/title/mojang.png");
			EaglerAdapter.glPushMatrix();
			float f1 = 0.875f + 0.025f * (float)Math.sqrt(f);
			EaglerAdapter.glTranslatef((var1.getScaledWidth() - 256) / 2, (var1.getScaledHeight() - 256) / 2, 0.0f);
			EaglerAdapter.glTranslatef(-128.0f * (f1 - 1.0f), -128.0f * (f1 - 1.0f) , 0.0f);
			EaglerAdapter.glScalef(f1, f1, 1.0f);
			this.scaledTessellator(0, 0, 0, 0, 256, 256);
			EaglerAdapter.glPopMatrix();

			EaglerAdapter.glFlush();
			EaglerAdapter.updateDisplay();
			
			long t = t1 + 17 + 17*i - System.currentTimeMillis();
			if(t > 0) {
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			Thread.sleep(1600l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t1 = System.currentTimeMillis();
		for(int i = 0; i < 21; i++) {
			this.displayWidth = EaglerAdapter.getCanvasWidth();
			this.displayHeight = EaglerAdapter.getCanvasHeight();
			EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
			var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
			
			float f = ((float)(System.currentTimeMillis() - t1) / 340f);
			
			EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, MathHelper.clamp_float((1.0f - f), 0.0F, 1.0F));
			this.renderEngine.bindTexture("%blur%/title/mojang.png");
			EaglerAdapter.glPushMatrix();
			float f1 = 0.9f + 0.025f * f * f;
			EaglerAdapter.glTranslatef((var1.getScaledWidth() - 256) / 2, (var1.getScaledHeight() - 256) / 2, 0.0f);
			EaglerAdapter.glTranslatef(-128.0f * (f1 - 1.0f), -128.0f * (f1 - 1.0f) , 0.0f);
			EaglerAdapter.glScalef(f1, f1, 1.0f);
			this.scaledTessellator(0, 0, 0, 0, 256, 256);
			EaglerAdapter.glPopMatrix();

			EaglerAdapter.glFlush();
			EaglerAdapter.updateDisplay();
			
			long t = t1 + 17 + 17*i - System.currentTimeMillis();
			if(t > 0) {
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
		EaglerAdapter.glFlush();
		EaglerAdapter.updateDisplay();
		
		try {
			Thread.sleep(100l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);

		while(EaglerAdapter.keysNext());
		while(EaglerAdapter.mouseNext());
	}

	/**
	 * Displays a new screen.
	 */
	private void loadScreen() {
		this.displayWidth = EaglerAdapter.getCanvasWidth();
		this.displayHeight = EaglerAdapter.getCanvasHeight();
		ScaledResolution var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		EaglerAdapter.glColorMask(true, true, true, true);
		EaglerAdapter.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glOrtho(0.0F, var1.getScaledWidth(), var1.getScaledHeight(), 0.0F, 1000.0F, 3000.0F);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glTranslatef(0.0F, 0.0F, -2000.0F);
		EaglerAdapter.glViewport(0, 0, this.displayWidth, this.displayHeight);
		EaglerAdapter.glClear(EaglerAdapter.GL_COLOR_BUFFER_BIT | EaglerAdapter.GL_DEPTH_BUFFER_BIT);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderEngine.bindTexture("%clamp%/title/eagtek.png");
		short var3 = 256;
		short var4 = 256;
		this.scaledTessellator((var1.getScaledWidth() - var3) / 2, (var1.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
		EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
		EaglerAdapter.glDisable(EaglerAdapter.GL_FOG);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);
		EaglerAdapter.glFlush();
		EaglerAdapter.updateDisplay();
	}

	/**
	 * Loads Tessellator with a scaled resolution
	 */
	public void scaledTessellator(int par1, int par2, int par3, int par4, int par5, int par6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.setColorOpaque(255, 255, 255);
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + par6) * var8));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + par6) * var8));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + 0) * var8));
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + 0) * var8));
		var9.draw();
	}

	public static EnumOS getOs() {
		String var0 = EaglerAdapter.getUserAgent().toLowerCase();
		return var0.contains("win") ? EnumOS.WINDOWS
				: (var0.contains("mac") ? EnumOS.MACOS
						: (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
	}
	
	public void stopServerAndDisplayGuiScreen(GuiScreen par1GuiScreen) {
		if(!IntegratedServer.isWorldNotLoaded()) {
			IntegratedServer.unloadWorld();
			displayGuiScreen(new GuiScreenSingleplayerLoading(par1GuiScreen, "saving world", () -> IntegratedServer.isReady()));
		}else {
			displayGuiScreen(par1GuiScreen);
		}
	}

	/**
	 * Sets the argument GuiScreen as the main (topmost visible) screen.
	 */
	public void displayGuiScreen(GuiScreen par1GuiScreen) {
		if (this.currentScreen != null) {
			this.currentScreen.onGuiClosed();
		}

		if (par1GuiScreen == null && this.theWorld == null) {
			par1GuiScreen = new GuiMainMenu();
		} else if (par1GuiScreen == null && this.thePlayer.getHealth() <= 0) {
			par1GuiScreen = new GuiGameOver();
		}

		if (par1GuiScreen instanceof GuiMainMenu) {
			this.gameSettings.showDebugInfo = false;
			this.ingameGUI.getChatGUI().clearChatMessages();
		}

		this.currentScreen = (GuiScreen) par1GuiScreen;

		if (par1GuiScreen != null) {
			this.setIngameNotInFocus();
			ScaledResolution var2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
			int var3 = var2.getScaledWidth();
			int var4 = var2.getScaledHeight();
			((GuiScreen) par1GuiScreen).setWorldAndResolution(this, var3, var4);
			this.skipRenderWorld = false;
		} else {
			if(!this.inGameHasFocus) this.setIngameFocus();
		}
	}
	
	public boolean isChatOpen() {
		return this.currentScreen != null && (this.currentScreen instanceof GuiChat);
	}
	
	public String getServerURI() {
		return this.getNetHandler() != null ? this.getNetHandler().getNetManager().getServerURI() : "[not connected]";
	}

	/**
	 * Checks for an OpenGL error. If there is one, prints the error ID and error
	 * string.
	 */
	public void checkGLError(String par1Str) {
		int var2;
		

		while ((var2 = EaglerAdapter.glGetError()) != 0) {
			String var3 = EaglerAdapter.gluErrorString(var2);
			System.err.println("########## GL ERROR ##########");
			System.err.println("@ " + par1Str);
			System.err.println(var2 + ": " + var3);
		}
	}

	/**
	 * Shuts down the minecraft applet by stopping the resource downloads, and
	 * clearing up GL stuff; called when the application (or web page) is exited.
	 */
	public void shutdownMinecraftApplet() {
		try {

			System.err.println("Stopping!");

			try {
				this.loadWorld((WorldClient) null);
			} catch (Throwable var8) {
				;
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable var7) {
				;
			}

			this.sndManager.closeMinecraft();
		} finally {
			EaglerAdapter.destroyContext();

			if (!this.hasCrashed) {
				EaglerAdapter.exit();
			}
		}

		System.gc();
	}

	public void run() {
		this.running = true;
		this.startGame();
		while (this.running) {
			this.runGameLoop();
		}
		EaglerAdapter.destroyContext();
		EaglerAdapter.exit();
	}

	/**
	 * Called repeatedly from run()
	 */
	private void runGameLoop() {
		if (this.refreshTexturePacksScheduled) {
			this.refreshTexturePacksScheduled = false;
			this.renderEngine.refreshTextures();
		}

		AxisAlignedBB.getAABBPool().cleanPool();

		if (this.theWorld != null) {
			this.theWorld.getWorldVec3Pool().clear();
		}

		this.mcProfiler.startSection("root");

		if (EaglerAdapter.shouldShutdown()) {
			this.shutdown();
		}

		if (this.isGamePaused && this.theWorld != null) {
			float var1 = this.timer.renderPartialTicks;
			this.timer.updateTimer();
			this.timer.renderPartialTicks = var1;
		} else {
			this.timer.updateTimer();
		}

		long var6 = System.nanoTime();
		this.mcProfiler.startSection("tick");

		for (int var3 = 0; var3 < this.timer.elapsedTicks; ++var3) {
			this.runTick();
		}
		
		IntegratedServer.processICP();

		this.mcProfiler.endStartSection("preRenderErrors");
		long var7 = System.nanoTime() - var6;
		this.checkGLError("Pre render");
		RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
		this.mcProfiler.endStartSection("sound");
		this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);

		if (!this.isGamePaused) {
			this.sndManager.func_92071_g();
		}

		this.mcProfiler.endSection();
		this.mcProfiler.startSection("render");
		this.mcProfiler.startSection("display");
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);

		if (!EaglerAdapter.isKeyDown(65)) {
			EaglerAdapter.updateDisplay();
		}

		if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
			this.gameSettings.thirdPersonView = 0;
		}

		this.mcProfiler.endSection();
		
		EaglerAdapter.glClearStack();
		
		if (!this.skipRenderWorld) {
			this.mcProfiler.endStartSection("gameRenderer");
			this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
			this.mcProfiler.endSection();
		}

		EaglerAdapter.glFlush();
		this.mcProfiler.endSection();

		//if (!EaglerAdapter.isFocused() && this.fullscreen) {
		//	this.toggleFullscreen();
		//}

		if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
			if (!this.mcProfiler.profilingEnabled) {
				this.mcProfiler.clearProfiling();
			}

			this.mcProfiler.profilingEnabled = true;
			this.displayDebugInfo(var7);
		} else {
			this.mcProfiler.profilingEnabled = false;
			this.prevFrameTime = System.nanoTime();
		}

		this.guiAchievement.updateAchievementWindow();
		this.mcProfiler.startSection("root");

		if (!this.fullscreen && (EaglerAdapter.getCanvasWidth() != this.displayWidth || EaglerAdapter.getCanvasHeight() != this.displayHeight)) {
			this.displayWidth = EaglerAdapter.getCanvasWidth();
			this.displayHeight = EaglerAdapter.getCanvasHeight();

			if (this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0) {
				this.displayHeight = 1;
			}

			this.resize(this.displayWidth, this.displayHeight);
		}

		this.checkGLError("Post render");
		++this.fpsCounter;
		//boolean var5 = this.isGamePaused;
		//this.isGamePaused = false;
		
		if(System.currentTimeMillis() - secondTimer > 1000l) {
			debugFPS = fpsCounter;
			fpsCounter = 0;
			debugChunkUpdates = chunkUpdates;
			chunkUpdates = 0;
			debugChunkGeometryUpdates = chunkGeometryUpdates;
			chunkGeometryUpdates = 0;
			secondTimer = System.currentTimeMillis();
		}
		this.mcProfiler.startSection("syncDisplay");

		if (this.func_90020_K() > 0) {
			EaglerAdapter.syncDisplay(EntityRenderer.performanceToFps(this.func_90020_K()));
		}
		
		if(isGonnaTakeDatScreenShot) {
			isGonnaTakeDatScreenShot = false;
			EaglerAdapter.saveScreenshot();
		}
		
		EaglerAdapter.doJavascriptCoroutines();

		this.mcProfiler.endSection();
		this.mcProfiler.endSection();
	}

	private int func_90020_K() {
		return this.currentScreen != null && this.currentScreen instanceof GuiMainMenu ? 2 : this.gameSettings.limitFramerate;
	}

	/**
	 * Update debugProfilerName in response to number keys in debug screen
	 */
	private void updateDebugProfilerName(int par1) {
		List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);

		if (var2 != null && !var2.isEmpty()) {
			ProfilerResult var3 = (ProfilerResult) var2.remove(0);

			if (par1 == 0) {
				if (var3.field_76331_c.length() > 0) {
					int var4 = this.debugProfilerName.lastIndexOf(".");

					if (var4 >= 0) {
						this.debugProfilerName = this.debugProfilerName.substring(0, var4);
					}
				}
			} else {
				--par1;

				if (par1 < var2.size() && !((ProfilerResult) var2.get(par1)).field_76331_c.equals("unspecified")) {
					if (this.debugProfilerName.length() > 0) {
						this.debugProfilerName = this.debugProfilerName + ".";
					}

					this.debugProfilerName = this.debugProfilerName + ((ProfilerResult) var2.get(par1)).field_76331_c;
				}
			}
		}
	}

	private void displayDebugInfo(long par1) {
		if (this.mcProfiler.profilingEnabled) {
			List var3 = this.mcProfiler.getProfilingData(this.debugProfilerName);
			ProfilerResult var4 = (ProfilerResult) var3.remove(0);
			EaglerAdapter.glClear(EaglerAdapter.GL_DEPTH_BUFFER_BIT);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
			EaglerAdapter.glEnable(EaglerAdapter.GL_COLOR_MATERIAL);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glOrtho(0.0F, this.displayWidth, this.displayHeight, 0.0F, 1000.0F, 3000.0F);
			EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
			EaglerAdapter.glLoadIdentity();
			EaglerAdapter.glTranslatef(0.0F, 0.0F, -2000.0F);
			EaglerAdapter.glLineWidth(1.0F);
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
			EaglerAdapter.glColor4f(1f, 1f, 1f, 1f);
			Tessellator var5 = Tessellator.instance;
			short var6 = 160;
			int var7 = this.displayWidth - var6 - 10;
			int var8 = this.displayHeight - var6 * 2;
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			var5.startDrawingQuads();
			var5.setColorRGBA_I(0, 200);
			var5.addVertex((double) ((float) var7 - (float) var6 * 1.1F), (double) ((float) var8 - (float) var6 * 0.6F - 16.0F), 0.0D);
			var5.addVertex((double) ((float) var7 - (float) var6 * 1.1F), (double) (var8 + var6 * 2), 0.0D);
			var5.addVertex((double) ((float) var7 + (float) var6 * 1.1F), (double) (var8 + var6 * 2), 0.0D);
			var5.addVertex((double) ((float) var7 + (float) var6 * 1.1F), (double) ((float) var8 - (float) var6 * 0.6F - 16.0F), 0.0D);
			var5.draw();
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			double var9 = 0.0D;
			int var13;

			EaglerAdapter.glDepthMask(true);
			
			for (int var11 = 0; var11 < var3.size(); ++var11) {
				ProfilerResult var12 = (ProfilerResult) var3.get(var11);
				var13 = MathHelper.floor_double(var12.field_76332_a / 4.0D) + 1;
				var5.startDrawing(EaglerAdapter.GL_TRIANGLE_FAN);
				var5.setColorOpaque_I(var12.func_76329_a());
				var5.addVertex((double) var7, (double) var8, 0.0D);
				int var14;
				float var15;
				float var16;
				float var17;

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float) ((var9 + var12.field_76332_a * (double) var14 / (double) var13) * Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * (float) var6;
					var17 = MathHelper.cos(var15) * (float) var6 * 0.5F;
					var5.addVertex((double) ((float) var7 + var16), (double) ((float) var8 - var17), 0.0D);
				}

				var5.draw();
				var5.startDrawing(EaglerAdapter.GL_TRIANGLE_STRIP);
				var5.setColorOpaque_I((var12.func_76329_a() & 16711422) >> 1);

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float) ((var9 + var12.field_76332_a * (double) var14 / (double) var13) * Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * (float) var6;
					var17 = MathHelper.cos(var15) * (float) var6 * 0.5F;
					var5.addVertex((double) ((float) var7 + var16), (double) ((float) var8 - var17), 0.0D);
					var5.addVertex((double) ((float) var7 + var16), (double) ((float) var8 - var17 + 10.0F), 0.0D);
				}

				var5.draw();
				var9 += var12.field_76332_a;
			}

			DecimalFormat var18 = new DecimalFormat("##0.00");
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			String var19 = "";

			if (!var4.field_76331_c.equals("unspecified")) {
				var19 = var19 + "[0] ";
			}

			if (var4.field_76331_c.length() == 0) {
				var19 = var19 + "ROOT ";
			} else {
				var19 = var19 + var4.field_76331_c + " ";
			}

			var13 = 16777215;
			this.fontRenderer.drawStringWithShadow(var19, var7 - var6, var8 - var6 / 2 - 16, var13);
			this.fontRenderer.drawStringWithShadow(var19 = var18.format(var4.field_76330_b) + "%", var7 + var6 - this.fontRenderer.getStringWidth(var19), var8 - var6 / 2 - 16, var13);

			for (int var21 = 0; var21 < var3.size(); ++var21) {
				ProfilerResult var20 = (ProfilerResult) var3.get(var21);
				String var22 = "";

				if (var20.field_76331_c.equals("unspecified")) {
					var22 = var22 + "[?] ";
				} else {
					var22 = var22 + "[" + (var21 + 1) + "] ";
				}

				var22 = var22 + var20.field_76331_c;
				this.fontRenderer.drawStringWithShadow(var22, var7 - var6, var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
				this.fontRenderer.drawStringWithShadow(var22 = var18.format(var20.field_76332_a) + "%", var7 + var6 - 50 - this.fontRenderer.getStringWidth(var22), var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
				this.fontRenderer.drawStringWithShadow(var22 = var18.format(var20.field_76330_b) + "%", var7 + var6 - this.fontRenderer.getStringWidth(var22), var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
			}
		}
	}

	/**
	 * Called when the window is closing. Sets 'running' to false which allows the
	 * game loop to exit cleanly.
	 */
	public void shutdown() {
		this.running = false;
	}

	/**
	 * Will set the focus to ingame if the Minecraft window is the active with
	 * focus. Also clears any GUI screen currently displayed
	 */
	public void setIngameFocus() {
		//if (EaglerAdapter.isFocused()) {
			//if (!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen) null);
				this.leftClickCounter = 10000;
			//}
		//}
	}

	/**
	 * Resets the player keystate, disables the ingame focus, and ungrabs the mouse
	 * cursor.
	 */
	public void setIngameNotInFocus() {
		//if (this.inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		//}
	}

	/**
	 * Displays the ingame menu
	 */
	public void displayInGameMenu() {
		if (this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
			if(IntegratedServer.isWorldRunning() && !this.isSingleplayer()) {
				IntegratedServer.autoSave();
			}
		}
	}

	private void sendClickBlockToController(int par1, boolean par2) {
		if (!par2) {
			this.leftClickCounter = 0;
		}

		if (par1 != 0 || this.leftClickCounter <= 0) {
			if (par2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && par1 == 0) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				this.playerController.onPlayerDamageBlock(var3, var4, var5, this.objectMouseOver.sideHit);

				if (this.thePlayer.canCurrentToolHarvestBlock(var3, var4, var5)) {
					this.effectRenderer.addBlockHitEffects(var3, var4, var5, this.objectMouseOver.sideHit);
					this.thePlayer.swingItem();
				}
			} else {
				this.playerController.resetBlockRemoving();
			}
		}
	}
	
	public void displayEaglercraftText(String s) {
		if(this.thePlayer != null && shownPlayerMessages.add(s)) {
			this.thePlayer.sendChatToPlayer(s);
		}
	}

	/**
	 * Called whenever the mouse is clicked. Button clicked is 0 for left clicking
	 * and 1 for right clicking. Args: buttonClicked
	 */
	private void clickMouse(int par1) {
		if (par1 != 0 || this.leftClickCounter <= 0) {
			if (par1 == 0) {
				this.thePlayer.swingItem();
			}

			if (par1 == 1) {
				this.rightClickDelayTimer = 4;
			}

			boolean var2 = true;
			ItemStack var3 = this.thePlayer.inventory.getCurrentItem();

			if (this.objectMouseOver == null) {
				if (par1 == 0 && this.playerController.isNotCreative()) {
					this.leftClickCounter = 10;
				}
			} else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if (par1 == 0) {
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if (par1 == 1 && this.playerController.func_78768_b(this.thePlayer, this.objectMouseOver.entityHit)) {
					var2 = false;
				}
			} else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int var4 = this.objectMouseOver.blockX;
				int var5 = this.objectMouseOver.blockY;
				int var6 = this.objectMouseOver.blockZ;
				int var7 = this.objectMouseOver.sideHit;

				if (par1 == 0) {
					this.playerController.clickBlock(var4, var5, var6, this.objectMouseOver.sideHit);
				} else {
					int var8 = var3 != null ? var3.stackSize : 0;

					if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, var3, var4, var5, var6, var7, this.objectMouseOver.hitVec)) {
						var2 = false;
						this.thePlayer.swingItem();
					}

					if (var3 == null) {
						return;
					}

					if (var3.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if (var3.stackSize != var8 || this.playerController.isInCreativeMode()) {
						this.entityRenderer.itemRenderer.resetEquippedProgress();
					}
				}
			}

			if (var2 && par1 == 1) {
				ItemStack var9 = this.thePlayer.inventory.getCurrentItem();

				if (var9 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var9)) {
					this.entityRenderer.itemRenderer.resetEquippedProgress2();
				}
			}
		}
	}

	/**
	 * Toggles fullscreen mode.
	 */
	public void toggleFullscreen() {
		
	}

	/**
	 * Called to resize the current screen.
	 */
	private void resize(int par1, int par2) {
		this.displayWidth = par1 <= 0 ? 1 : par1;
		this.displayHeight = par2 <= 0 ? 1 : par2;

		ScaledResolution var3 = new ScaledResolution(this.gameSettings, par1, par2);
		int var4 = var3.getScaledWidth();
		int var5 = var3.getScaledHeight();
		
		if (this.currentScreen != null) {
			this.currentScreen.setWorldAndResolution(this, var4, var5);
		}
		
		this.voiceOverlay.setResolution(var4, var5);
	}
	
	private boolean wasPaused = false;

	/**
	 * Runs the current tick.
	 */
	public void runTick() {
		if (this.rightClickDelayTimer > 0) {
			--this.rightClickDelayTimer;
		}

		this.mcProfiler.startSection("stats");
		this.mcProfiler.endStartSection("gui");
		
		this.isGamePaused = this.isSingleplayer() && this.theWorld != null && this.thePlayer != null && this.currentScreen != null
				&& this.currentScreen.doesGuiPauseGame() && !IntegratedServerLAN.isHostingLAN();
		
		if(wasPaused != isGamePaused) {
			IntegratedServer.setPaused(this.isGamePaused);
			wasPaused = isGamePaused;
		}
		
		if(lanState && !IntegratedServerLAN.isLANOpen()) {
			lanState = false;
			if(thePlayer != null) {
				thePlayer.sendChatToPlayer(EnumChatFormatting.RED + StatCollector.translateToLocal("lanServer.relayDisconnected"));
			}
		}
		
		if (!this.isGamePaused) {
			this.ingameGUI.updateTick();
		}

		this.mcProfiler.endStartSection("pick");
		this.entityRenderer.getMouseOver(1.0F);
		this.mcProfiler.endStartSection("gameMode");

		if (!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}
		
		this.mcProfiler.endStartSection("textures");

		if (!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}
		
		DefaultSkinRenderer.deleteOldSkins();

		if (this.currentScreen == null && this.thePlayer != null) {
			if (this.thePlayer.getHealth() <= 0) {
				this.displayGuiScreen((GuiScreen) null);
			} else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen) null);
		}

		if (this.currentScreen != null) {
			this.leftClickCounter = 10000;
		}

		if (this.currentScreen != null) {
			this.currentScreen.handleInput();

			if (this.currentScreen != null) {
				this.currentScreen.guiParticles.update();
				this.currentScreen.updateScreen();
			}
		}
		
		GuiMultiplayer.tickRefreshCooldown();
		EaglerAdapter.tickVoice();
		if (EaglerAdapter.getVoiceStatus() == Voice.VoiceStatus.CONNECTING || EaglerAdapter.getVoiceStatus() == Voice.VoiceStatus.CONNECTED) {

			EaglerAdapter.activateVoice((this.currentScreen == null || !this.currentScreen.blockHotKeys()) && EaglerAdapter.isKeyDown(gameSettings.voicePTTKey));

			if (this.theWorld != null && this.thePlayer != null) {
				HashSet<String> seenPlayers = new HashSet<>();
				for (Object playerObject : this.theWorld.playerEntities) {
					EntityPlayer player = (EntityPlayer) playerObject;
					if (player == this.thePlayer) continue;
					if (EaglerAdapter.getVoiceChannel() == Voice.VoiceChannel.PROXIMITY) EaglerAdapter.updateVoicePosition(player.username, player.posX, player.posY + player.getEyeHeight(), player.posZ);
					int prox = 22;
					// cube
					if (Math.abs(thePlayer.posX - player.posX) <= prox && Math.abs(thePlayer.posY - player.posY) <= prox && Math.abs(thePlayer.posZ - player.posZ) <= prox) {
						EaglerAdapter.addNearbyPlayer(player.username);
						seenPlayers.add(player.username);
					}
				}
				EaglerAdapter.cleanupNearbyPlayers(seenPlayers);
			}
		}

		if (this.currentScreen == null || this.currentScreen.allowUserInput) {
			this.mcProfiler.endStartSection("mouse");

			while (EaglerAdapter.mouseNext()) {
				KeyBinding.setKeyBindState(EaglerAdapter.mouseGetEventButton() - 100, EaglerAdapter.mouseGetEventButtonState());

				if (EaglerAdapter.mouseGetEventButtonState()) {
					KeyBinding.onTick(EaglerAdapter.mouseGetEventButton() - 100);
				}

				long var1 = getSystemTime() - this.systemTime;

				if (var1 <= 200L) {
					int var10 = EaglerAdapter.mouseGetEventDWheel();

					if (var10 != 0) {
						this.thePlayer.inventory.changeCurrentItem(var10);

						if (this.gameSettings.noclip) {
							if (var10 > 0) {
								var10 = 1;
							}

							if (var10 < 0) {
								var10 = -1;
							}

							this.gameSettings.noclipRate += (float) var10 * 0.25F;
						}
					}

					if (this.currentScreen == null) {
						if ((!this.inGameHasFocus || !EaglerAdapter.isPointerLocked()) && EaglerAdapter.mouseGetEventButtonState()) {
							this.setIngameFocus();
						}
					} else if (this.currentScreen != null) {
						this.currentScreen.handleMouseInput();
					}
				}
			}

			if (this.leftClickCounter > 0) {
				--this.leftClickCounter;
			}

			this.mcProfiler.endStartSection("keyboard");
			boolean var8;

			while (EaglerAdapter.keysNext()) {
				KeyBinding.setKeyBindState(EaglerAdapter.getEventKey(), EaglerAdapter.getEventKeyState());

				if (EaglerAdapter.getEventKeyState()) {
					KeyBinding.onTick(EaglerAdapter.getEventKey());
				}

				boolean F3down = (this.gameSettings.keyBindFunction.pressed && EaglerAdapter.isKeyDown(4));

				if (this.field_83002_am > 0L) {
					if (getSystemTime() - this.field_83002_am >= 6000L) {
						throw new RuntimeException("manual crash");
					}

					if (!EaglerAdapter.isKeyDown(46) || !F3down) {
						this.field_83002_am = -1L;
					}
				} else if (F3down && EaglerAdapter.isKeyDown(46)) {
					this.field_83002_am = getSystemTime();
				}

				if (EaglerAdapter.getEventKeyState()) {
					isGonnaTakeDatScreenShot |= (this.gameSettings.keyBindFunction.pressed && EaglerAdapter.getEventKey() == 3);
					if (EaglerAdapter.getEventKey() == 87) {
						this.toggleFullscreen();
					} else {
						if (this.currentScreen != null) {
							this.currentScreen.handleKeyboardInput();
						} else {
							if (EaglerAdapter.getEventKey() == 1) {
								this.displayInGameMenu();
							}

							if (F3down && EaglerAdapter.getEventKey() == 31) {
								this.forceReload();
							}

							if (F3down && EaglerAdapter.getEventKey() == 20) {
								this.renderEngine.refreshTextures();
								this.renderGlobal.loadRenderers();
								FixedFunctionShader.refreshCoreGL();
							}

							if (F3down && EaglerAdapter.getEventKey() == 33) {
								var8 = EaglerAdapter.isKeyDown(42) | EaglerAdapter.isKeyDown(54);
								this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, var8 ? -1 : 1);
							}

							if (F3down && EaglerAdapter.getEventKey() == 30) {
								this.renderGlobal.loadRenderers();
							}

							if (F3down && EaglerAdapter.getEventKey() == 35) {
								this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
								this.gameSettings.saveOptions();
							}

							if (F3down && EaglerAdapter.getEventKey() == 48) {
								RenderManager.field_85095_o = !RenderManager.field_85095_o;
							}

							if (F3down && EaglerAdapter.getEventKey() == 25) {
								this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
								this.gameSettings.saveOptions();
							}

							if (this.gameSettings.keyBindFunction.pressed && EaglerAdapter.getEventKey() == 2) {
								this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
							}

							if (EaglerAdapter.getEventKey() == 4 && this.gameSettings.keyBindFunction.pressed) {
								this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
								this.gameSettings.showDebugProfilerChart = true;
							}

							if (EaglerAdapter.getEventKey() == 6 && this.gameSettings.keyBindFunction.pressed) {
								++this.gameSettings.thirdPersonView;

								if (this.gameSettings.thirdPersonView > 2) {
									this.gameSettings.thirdPersonView = 0;
								}
							}
							
							if (EaglerAdapter.getEventKey() == 7 && this.gameSettings.keyBindFunction.pressed) {
								this.gameSettings.showCoordinates = !this.gameSettings.showCoordinates;
								this.gameSettings.saveOptions();
							}

							if (EaglerAdapter.getEventKey() == 9 && this.gameSettings.keyBindFunction.pressed) {
								this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
							}
						}
						
						if(!this.gameSettings.keyBindFunction.pressed) {
							for (int var9 = 0; var9 < 9; ++var9) {
								if (EaglerAdapter.getEventKey() == 2 + var9) {
									this.thePlayer.inventory.currentItem = var9;
								}
							}
						}

						if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.keyBindFunction.pressed) {
							if (EaglerAdapter.getEventKey() == 11) {
								this.updateDebugProfilerName(0);
							}

							for (int var9 = 0; var9 < 9; ++var9) {
								if (EaglerAdapter.getEventKey() == 2 + var9) {
									this.updateDebugProfilerName(var9 + 1);
								}
							}
						}
					}
				}
			}

			var8 = this.gameSettings.chatVisibility != 2;

			while (this.gameSettings.keyBindInventory.isPressed()) {
				this.displayGuiScreen(new GuiInventory(this.thePlayer));
			}

			while (this.gameSettings.keyBindDrop.isPressed()) {
				this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
			}

			while (this.gameSettings.keyBindChat.isPressed() && var8) {
				this.displayGuiScreen(new GuiChat());
			}

			if (this.currentScreen == null && EaglerAdapter.isKeyDown(53) && var8) {
				this.displayGuiScreen(new GuiChat("/"));
			}
			
			if(this.gameSettings.keyBindSprint.pressed && !this.thePlayer.isSprinting() && this.thePlayer.canSprint() && !this.thePlayer.isCollidedHorizontally) {
				this.thePlayer.setSprinting(true);
			}
			
			if (this.thePlayer.isUsingItem()) {
				if (!this.gameSettings.keyBindUseItem.pressed) {
					this.playerController.onStoppedUsingItem(this.thePlayer);
				}

				label379:

				while (true) {
					if (!this.gameSettings.keyBindAttack.isPressed()) {
						while (this.gameSettings.keyBindUseItem.isPressed()) {
							;
						}

						while (true) {
							if (this.gameSettings.keyBindPickBlock.isPressed()) {
								continue;
							}

							break label379;
						}
					}
				}
			} else {
				while (this.gameSettings.keyBindAttack.isPressed()) {
					this.clickMouse(0);
				}

				while (this.gameSettings.keyBindUseItem.isPressed()) {
					this.clickMouse(1);
				}

				while (this.gameSettings.keyBindPickBlock.isPressed()) {
					this.clickMiddleMouseButton();
				}
			}

			if (this.gameSettings.keyBindUseItem.pressed && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
				this.clickMouse(1);
			}

			this.sendClickBlockToController(0, this.currentScreen == null && this.gameSettings.keyBindAttack.pressed && this.inGameHasFocus);
		}

		if (this.theWorld != null) {
			if (this.thePlayer != null) {
				++this.joinPlayerCounter;
				
				if (this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
				
				++messageOnLoginCounter;
				
				if(messageOnLoginCounter == 100 && isSingleplayerOrLAN()) {
					displayEaglercraftText(EnumChatFormatting.GREEN + "Notice: chunk loading may take a while in singleplayer.");
				}
				
				if(messageOnLoginCounter == 150 && isSingleplayerOrLAN()) {
					displayEaglercraftText(EnumChatFormatting.AQUA + "Especially in new worlds, if no chunks show give the game up to 5 straight minutes before \"giving up\" on a new world");
				}
			}

			this.mcProfiler.endStartSection("gameRenderer");

			if (!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			this.mcProfiler.endStartSection("levelRenderer");

			if (!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			this.mcProfiler.endStartSection("level");

			if (!this.isGamePaused) {
				if (this.theWorld.lastLightningBolt > 0) {
					--this.theWorld.lastLightningBolt;
				}

				this.theWorld.updateEntities();
			}

			if (!this.isGamePaused) {
				this.theWorld.setAllowedSpawnTypes(this.theWorld.difficultySetting > 0, true);

				this.theWorld.tick();
			}

			this.mcProfiler.endStartSection("animateTick");

			if (!this.isGamePaused && this.theWorld != null) {
				this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			this.mcProfiler.endStartSection("particles");

			if (!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		} else if (this.myNetworkManager != null) {
			this.mcProfiler.endStartSection("pendingConnection");
			this.myNetworkManager.processReadPackets();
		} else {
			this.entityRenderer.startup = 0;
			this.entityRenderer.preStartup = 0;
			this.entityRenderer.asdfghjkl = false;
		}
		
		if(!(this.gameSettings.adderall || entityRenderer.asdfghjkl) || !yeeState) {
			this.entityRenderer.startup = 0;
			this.entityRenderer.preStartup = 0;
			this.gameSettings.adderall = false;
			this.entityRenderer.asdfghjkl = false;
		}
		
		if(this.theWorld == null) {
			this.sndManager.playTheTitleMusic();
		}else {
			this.sndManager.stopTheTitleMusic();
		}
		
		if(reconnectAddress != null) {
			if(theWorld != null) {
				System.out.println("Redirecting to: " + reconnectAddress);
				theWorld.sendQuittingDisconnectingPacket();
				loadWorld((WorldClient) null);
				stopServerAndDisplayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), this,
						new ServerData("reconnect", reconnectAddress, true)));
			}
			reconnectAddress = null;
		}

		this.mcProfiler.endSection();
		this.systemTime = getSystemTime();
	}
	
	private int titleMusicObj = -1;

	public String reconnectAddress = null;

	/**
	 * Forces a reload of the sound manager and all the resources. Called in game by
	 * holding 'F3' and pressing 'S'.
	 */
	private void forceReload() {
		System.err.println("FORCING RELOAD!");

		if (this.sndManager != null) {
			this.sndManager.stopAllSounds();
		}

		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
	}

	/**
	 * unloads the current world first
	 */
	public void loadWorld(WorldClient par1WorldClient) {
		this.loadWorld(par1WorldClient, "");
	}

	/**
	 * par2Str is displayed on the loading screen to the user unloads the current
	 * world first
	 */
	public void loadWorld(WorldClient par1WorldClient, String par2Str) {
		if (par1WorldClient == null) {
			
			EaglerAdapter.enableVoice(Voice.VoiceChannel.NONE);
			
			NetClientHandler var3 = this.getNetHandler();

			if (var3 != null) {
				var3.cleanup();
			}

			if (this.myNetworkManager != null) {
				this.myNetworkManager.closeConnections();
			}
			
			this.myNetworkManager = null;
			
		}

		this.renderViewEntity = null;

		if (this.loadingScreen != null) {
			this.loadingScreen.resetProgressAndMessage(par2Str);
			this.loadingScreen.resetProgresAndWorkingMessage("");
		}

		if (par1WorldClient == null && this.theWorld != null) {
			if (this.texturePackList.getIsDownloading()) {
				this.texturePackList.onDownloadFinished();
			}
			this.lanState = false;
			IntegratedServer.unloadWorld();
			this.setServerData((ServerData) null);
			this.integratedServerIsRunning = false;
		}

		this.sndManager.playStreaming((String) null, 0.0F, 0.0F, 0.0F);
		this.sndManager.stopAllSounds();
		if(EaglerAdapter.isVideoSupported()) {
			EaglerAdapter.unloadVideo();
		}
		this.theWorld = par1WorldClient;

		if (par1WorldClient != null) {
			if (this.renderGlobal != null) {
				this.renderGlobal.setWorldAndLoadRenderers(par1WorldClient);
			}

			if (this.effectRenderer != null) {
				this.effectRenderer.clearEffects(par1WorldClient);
			}

			if (this.thePlayer == null) {
				this.thePlayer = this.playerController.func_78754_a(par1WorldClient);
				this.playerController.flipPlayer(this.thePlayer);
			}
			
			//if(!EaglerAdapter._wisAnisotropicPatched()) {
			//	displayEaglercraftText("ANGLE Issue #4994 is unpatched on this browser, using fake aliased sampling on linear magnified terrain texture for anisotropic filtering. Chrome patch progress and information available at https://crbug.com/angleproject/4994");
			//}

			StringTranslate var4 = StringTranslate.getInstance();
			
			if(!this.gameSettings.fancyGraphics || this.gameSettings.ambientOcclusion == 0) {
				displayEaglercraftText("Note: " + var4.translateKey("fancyGraphicsNote"));
			}
			
			if(this.gameSettings.showCoordinates) {
				displayEaglercraftText(EnumChatFormatting.LIGHT_PURPLE + "Note: use F+6 to hide the coordinates off of the screen (if you're in public)");
			}else {
				displayEaglercraftText(EnumChatFormatting.LIGHT_PURPLE + "Note: use F+6 to show your coordinates on the screen");
			}
			
			messageOnLoginCounter = 0;

			this.thePlayer.preparePlayerToSpawn();
			par1WorldClient.spawnEntityInWorld(this.thePlayer);
			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			this.playerController.setPlayerCapabilities(this.thePlayer);
			this.renderViewEntity = this.thePlayer;
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}
	
	public void setNetManager(INetworkManager nm) {
		this.myNetworkManager = nm;
	}

	/**
	 * A String of renderGlobal.getDebugInfoRenders
	 */
	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	/**
	 * Gets the information in the F3 menu about how many entities are
	 * infront/around you
	 */
	public String getEntityDebug() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	/**
	 * Gets the name of the world's current chunk provider
	 */
	public String getWorldProviderName() {
		return this.theWorld.getProviderName();
	}

	/**
	 * A String of how many entities are in the world
	 */
	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
	}

	public void setDimensionAndSpawnPlayer(int par1) {
		this.theWorld.setSpawnLocation();
		this.theWorld.removeAllEntities();
		int var2 = 0;

		if (this.thePlayer != null) {
			var2 = this.thePlayer.entityId;
			this.theWorld.removeEntity(this.thePlayer);
		}

		this.renderViewEntity = null;
		this.thePlayer = this.playerController.func_78754_a(this.theWorld);
		this.thePlayer.dimension = par1;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		this.theWorld.spawnEntityInWorld(this.thePlayer);
		this.playerController.flipPlayer(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = var2;
		this.playerController.setPlayerCapabilities(this.thePlayer);

		if (this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen) null);
		}
	}

	/**
	 * Sets whether this is a demo or not.
	 */
	void setDemo(boolean par1) {
		this.isDemo = par1;
	}

	/**
	 * Gets whether this is a demo or not.
	 */
	public final boolean isDemo() {
		return this.isDemo;
	}

	/**
	 * Returns the NetClientHandler.
	 */
	public NetClientHandler getNetHandler() {
		return this.thePlayer != null ? this.thePlayer.sendQueue : null;
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	/**
	 * Returns if ambient occlusion is enabled
	 */
	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
	}

	/**
	 * Returns true if the message is a client command and should not be sent to the
	 * server. However there are no such commands at this point in time.
	 */
	public boolean handleClientCommand(String par1Str) {
		return !par1Str.startsWith("/") ? false : false;
	}

	/**
	 * Called when the middle mouse button gets clicked
	 */
	private void clickMiddleMouseButton() {
		if (this.objectMouseOver != null) {
			boolean var1 = this.thePlayer.capabilities.isCreativeMode;
			int var3 = 0;
			boolean var4 = false;
			int var2;
			int var5;

			if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				var5 = this.objectMouseOver.blockX;
				int var6 = this.objectMouseOver.blockY;
				int var7 = this.objectMouseOver.blockZ;
				Block var8 = Block.blocksList[this.theWorld.getBlockId(var5, var6, var7)];

				if (var8 == null) {
					return;
				}

				var2 = var8.idPicked(this.theWorld, var5, var6, var7);

				if (var2 == 0) {
					return;
				}

				var4 = Item.itemsList[var2].getHasSubtypes();
				int var9 = var2 < 256 && !Block.blocksList[var8.blockID].isFlowerPot() ? var2 : var8.blockID;
				var3 = Block.blocksList[var9].getDamageValue(this.theWorld, var5, var6, var7);
			} else {
				if (this.objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
					return;
				}

				if (this.objectMouseOver.entityHit instanceof EntityPainting) {
					var2 = Item.painting.itemID;
				} else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
					EntityItemFrame var10 = (EntityItemFrame) this.objectMouseOver.entityHit;

					if (var10.getDisplayedItem() == null) {
						var2 = Item.itemFrame.itemID;
					} else {
						var2 = var10.getDisplayedItem().itemID;
						var3 = var10.getDisplayedItem().getItemDamage();
						var4 = true;
					}
				} else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
					EntityMinecart var11 = (EntityMinecart) this.objectMouseOver.entityHit;

					if (var11.getMinecartType() == 2) {
						var2 = Item.minecartPowered.itemID;
					} else if (var11.getMinecartType() == 1) {
						var2 = Item.minecartCrate.itemID;
					} else if (var11.getMinecartType() == 3) {
						var2 = Item.minecartTnt.itemID;
					} else if (var11.getMinecartType() == 5) {
						var2 = Item.minecartHopper.itemID;
					} else {
						var2 = Item.minecartEmpty.itemID;
					}
				} else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
					var2 = Item.boat.itemID;
				} else {
					var2 = Item.monsterPlacer.itemID;
					var3 = EntityList.getEntityID(this.objectMouseOver.entityHit);
					var4 = true;

					if (var3 <= 0 || !EntityList.entityEggs.containsKey(Integer.valueOf(var3))) {
						return;
					}
				}
			}

			this.thePlayer.inventory.setCurrentItem(var2, var3, var4, var1);

			if (var1) {
				var5 = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + this.thePlayer.inventory.currentItem;
				this.playerController.sendSlotPacket(this.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem), var5);
			}
		}
	}

	/**
	 * Return the singleton Minecraft instance for the game
	 */
	public static Minecraft getMinecraft() {
		return theMinecraft;
	}

	/**
	 * Sets refreshTexturePacksScheduled to true, triggering a texture pack refresh
	 * next time the while(running) loop is run
	 */
	public void scheduleTexturePackRefresh() {
		this.refreshTexturePacksScheduled = true;
	}
	
	/**
	 * Set the current ServerData instance.
	 */
	public void setServerData(ServerData par1ServerData) {
		this.currentServerData = par1ServerData;
	}

	/**
	 * Get the current ServerData instance.
	 */
	public ServerData getServerData() {
		return this.currentServerData;
	}

	public boolean isIntegratedServerRunning() {
		return IntegratedServer.isWorldRunning();
	}

	/**
	 * Returns true if there is only one player playing, and the current server is
	 * the integrated one.
	 */
	public boolean isSingleplayer() {
		return myNetworkManager instanceof WorkerNetworkManager;
	}
	
	/**
	 * Gets the system time in milliseconds.
	 */
	public static long getSystemTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Returns whether we're in full screen or not.
	 */
	public boolean isFullScreen() {
		return this.fullscreen;
	}
	
	public static int getGLMaximumTextureSize() {
		return 8192;
	}

	public void launchIntegratedServer(String folderName, String trim, WorldSettings var6) {
		this.loadWorld((WorldClient)null);
		
		IntegratedServer.loadWorld(folderName, gameSettings.difficulty, var6);
		
		this.displayGuiScreen(new GuiScreenSingleplayerLoading(new GuiScreenSingleplayerConnecting(new GuiMainMenu(), "Connecting to " + folderName), "Loading world: " + folderName, () -> IntegratedServer.isWorldRunning()));
	}
}
