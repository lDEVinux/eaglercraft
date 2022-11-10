package net.minecraft.src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.lax1dude.eaglercraft.AssetRepository;
import net.lax1dude.eaglercraft.ConfigConstants;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.GuiScreenEditProfile;
import net.lax1dude.eaglercraft.GuiScreenSingleplayerLoading;
import net.lax1dude.eaglercraft.IntegratedServer;
import net.lax1dude.eaglercraft.LocalStorageManager;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;

public class GuiMainMenu extends GuiScreen {

	/** The RNG used by the Main Menu Screen. */
	private static final EaglercraftRandom rand = new EaglercraftRandom();

	/** The splash message. */
	public String splashText = null;
	private GuiButton buttonResetDemo;
	
	private static boolean showingEndian = true;
	private static final int showRandomItem;
	
	static {
		if(ConfigConstants.mainMenuItemLink != null) {
			EaglercraftRandom rand = new EaglercraftRandom();
			int itm = 0;
			do {
				itm = rand.nextInt(256) + 256;
			}while(Item.itemsList[itm] == null);
			showRandomItem = itm;
		}else {
			showRandomItem = -1;
		}
	}
	
	private long start;

	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private static int viewportTexture = -1;
	private boolean field_96141_q = true;
	private static boolean field_96140_r = false;
	private static boolean field_96139_s = false;
	private final Object field_104025_t = new Object();
	private String field_92025_p;
	private String field_104024_v;

	/** An array of all the paths to the panorama pictures. */
	private static final TextureLocation[] titlePanoramaPaths = new TextureLocation[] { new TextureLocation("/title/bg/panorama0.png"), new TextureLocation("/title/bg/panorama1.png"), new TextureLocation("/title/bg/panorama2.png"), new TextureLocation("/title/bg/panorama3.png"), new TextureLocation("/title/bg/panorama4.png"), new TextureLocation("/title/bg/panorama5.png") };
	public static final String field_96138_a = "";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;

	private int scrollPosition = 0;
	private static final int visibleLines = 21;

	private int dragstart = -1;
	private int dragstartI = -1;
	
	private ArrayList<String> ackLines;
	
	public boolean showAck = false;

	public GuiMainMenu() {
		List<String> lst = ConfigConstants.splashTexts;
		if (lst != null) {
			if(lst.size() > 0) {
				EaglercraftRandom rand = new EaglercraftRandom();
				this.splashText = lst.get(rand.nextInt(lst.size()));
			}else {
				this.splashText = "missingno";
			}
		}
		this.field_92025_p = EaglerAdapter._wisWebGL() ? ("eaglercraft javascript runtime") : ("eaglercraft desktop runtime");
		this.start = System.currentTimeMillis();
		this.start += this.start % 10000l;
		this.ackLines = new ArrayList();
		
		if(!LocalStorageManager.gameSettingsStorage.getBoolean("seenAcknowledgements")) {
			this.showAck = true;
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void handleMouseInput() {
		super.handleMouseInput();
		if(showAck) {
			int var1 = EaglerAdapter.mouseGetEventDWheel();
			if(var1 < 0) {
				scrollPosition += 3;
			}
			if(var1 > 0) {
				scrollPosition -= 3;
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if(par2 == 1) {
			hideAck();
		}
	}
	
	private void hideAck() {
		if(!LocalStorageManager.gameSettingsStorage.getBoolean("seenAcknowledgements")) {
			LocalStorageManager.gameSettingsStorage.setBoolean("seenAcknowledgements", true);
			LocalStorageManager.saveStorageG();
		}
		showAck = false;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		if(viewportTexture == -1) viewportTexture = this.mc.renderEngine.makeViewportTexture(256, 256);
		Calendar var1 = Calendar.getInstance();
		var1.setTime(new Date());

		StringTranslate var2 = StringTranslate.getInstance();
		int var4 = this.height / 4 + 48;

		if(EaglerAdapter.isIntegratedServerAvailable()) {
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var4, var2.translateKey("menu.singleplayer")));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var4 + 24 * 1, var2.translateKey("menu.multiplayer")));
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, var4 + 24 * 2, var2.translateKey("menu.forkme")));
		}else {
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var4, var2.translateKey("menu.multiplayer")));
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, var4 + 24, var2.translateKey("menu.forkme")));
		}

		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var4 + 72 + 12, 98, 20, var2.translateKey("menu.options")));
		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, var4 + 72 + 12, 98, 20, var2.translateKey("menu.editprofile")));

		this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, var4 + 72 + 12));
		Object var5 = this.field_104025_t;

		synchronized (this.field_104025_t) {
			this.field_92023_s = this.fontRenderer.getStringWidth(this.field_92025_p);
			this.field_92024_r = this.fontRenderer.getStringWidth(field_96138_a);
			int var6 = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - var6) / 2;
			this.field_92021_u = 82;
			this.field_92020_v = this.field_92022_t + var6;
			this.field_92019_w = this.field_92021_u + 12;
		}

		ConfigConstants.panoramaBlur = AssetRepository.getResource("/title/no-pano-blur.flag") == null;
		
		if(this.ackLines.isEmpty()) {
			int width = 315;
			String file = EaglerAdapter.fileContents("/credits.txt");
			if(file == null) {
				for(int i = 0; i < 30; ++i) {
					this.ackLines.add(" -- file not found -- ");
				}
			}else {
				String[] lines = file.split("\n");
				for(String s : lines) {
					String s2 = s.trim();
					if(s2.isEmpty()) {
						this.ackLines.add("");
					}else {
						String[] words = s2.split(" ");
						String currentLine = "   ";
						for(String s3 : words) {
							String cCurrentLine = currentLine + s3 + " ";
							if(this.mc.fontRenderer.getStringWidth(cCurrentLine) < width) {
								currentLine = cCurrentLine;
							}else {
								this.ackLines.add(currentLine);
								currentLine = s3 + " ";
							}
						}
						this.ackLines.add(currentLine);
					}
				}
			}
			
		}
		
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		if(!showAck) {
			super.mouseClicked(par1, par2, par3);
			if (par3 == 0) {
				int w = this.fontRenderer.getStringWidth("eaglercraft readme.txt") * 3 / 4;
				if(par1 >= (this.width - w - 4) && par1 <= this.width && par2 >= 0 && par2 <= 9) {
					showAck = true;
					return;
				}
				w = this.fontRenderer.getStringWidth("debug console") * 3 / 4;
				if(par1 >= 0 && par1 <= (w + 4) && par2 >= 0 && par2 <= 9) {
					/*
					EaglerAdapter.openConsole();
					*/
				}
				if(ConfigConstants.mainMenuItemLink != null) {
					//drawRect((this.width - w - 4), 0, this.width, 9, 0x55200000);

					int posX = this.width / 2 - 170 - this.width / 10;
					int posY = this.height / 4 + 70;
					int ww = 66;
					int hh = 46;
					int ln0w = ConfigConstants.mainMenuItemLine0 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine0);
					ww = ww < ln0w ? ln0w : ww;
					hh = hh < ln0w ? hh + 12 : hh;
					int ln1w = ConfigConstants.mainMenuItemLine1 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine1);
					ww = ww < ln1w ? ln1w : ww;
					hh = hh < ln1w ? hh + 12 : hh;
					int ln2w = ConfigConstants.mainMenuItemLine2 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine2);
					ww = ww < ln2w ? ln2w : ww;
					hh = hh < ln2w ? hh + 12 : hh;
					
					ww += 20;
					hh += 20;
					
					if(par1 > posX && par1 < posX + (ww / 4 * 3) && par2 > posY && par2 < posY + (hh / 4 * 3)) {
						EaglerAdapter.openLink(ConfigConstants.mainMenuItemLink);
						return;
					}
				}
			}
		}else {
			if(par3 == 0) {
				int x = (this.width - 345) / 2;
				int y = (this.height - 230) / 2;
				if(par1 >= (x + 323) && par1 <= (x + 323 + 13) && par2 >= (y + 7) && par2 <= (y + 7 + 13)) {
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					hideAck();
				}
				int trackHeight = 193;
				int offset = trackHeight * scrollPosition / this.ackLines.size();
				if(par1 >= (x + 326) && par1 <= (x + 334) && par2 >= (y + 27 + offset) && par2 <= (y + 27 + offset + (visibleLines * trackHeight / this.ackLines.size()) + 1)) {
					dragstart = par2;
					dragstartI = scrollPosition;
				}
			}
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			showingEndian = false;
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		
		if (par1GuiButton.id == 1) {
			if(EaglerAdapter.isIntegratedServerAvailable()) {
				if(!IntegratedServer.isAlive()) {
					IntegratedServer.begin();
					this.mc.displayGuiScreen(new GuiScreenSingleplayerLoading(new GuiSelectWorld(this), "starting up integrated server", () -> IntegratedServer.isReady()));
				}else {
					this.mc.displayGuiScreen(new GuiSelectWorld(this));
				}
			}
		}

		if (par1GuiButton.id == 5) {
			showingEndian = false;
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings));
		}

		if (par1GuiButton.id == 2) {
			showingEndian = false;
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (par1GuiButton.id == 3) {
			//this.mc.displayGuiScreen(new GuiScreenVoiceChannel(this));
			EaglerAdapter.openLink(ConfigConstants.forkMe);
		}

		if (par1GuiButton.id == 4) {
			showingEndian = false;
			this.mc.displayGuiScreen(new GuiScreenEditProfile(this));
		}
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int par1, int par2, float par3) {
		Tessellator var4 = Tessellator.instance;
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glLoadIdentity();
		if (ConfigConstants.panoramaBlur) {
			EaglerAdapter.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		} else {
			EaglerAdapter.gluPerspective(120.0F, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, 10.0F);
		}
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glLoadIdentity();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EaglerAdapter.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		EaglerAdapter.glDepthMask(false);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		byte var5 = ConfigConstants.panoramaBlur ? (byte)8 : (byte)1;

		for (int var6 = 0; var6 < var5 * var5; ++var6) {
			if (ConfigConstants.panoramaBlur) {
				EaglerAdapter.glPushMatrix();
				float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
				float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
				float var9 = 0.0F;
				EaglerAdapter.glTranslatef(var7, var8, var9);
				
				float panTimer = (float)(System.currentTimeMillis() - start) * 0.03f;
				EaglerAdapter.glRotatef(MathHelper.sin(panTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
				EaglerAdapter.glRotatef(-(panTimer) * 0.1F, 0.0F, 1.0F, 0.0F);
			}

			for (int var10 = 0; var10 < 6; ++var10) {
				EaglerAdapter.glPushMatrix();

				if (var10 == 1) {
					EaglerAdapter.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 2) {
					EaglerAdapter.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 3) {
					EaglerAdapter.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 4) {
					EaglerAdapter.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var10 == 5) {
					EaglerAdapter.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				titlePanoramaPaths[var10].bindTexture();
				var4.startDrawingQuads();
				if (ConfigConstants.panoramaBlur) {
					var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
				} else {
					var4.setColorRGBA_I(16777215, 255);
				}
				float var11 = 0.0F;
				var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double) (0.0F + var11), (double) (0.0F + var11));
				var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double) (1.0F - var11), (double) (0.0F + var11));
				var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double) (1.0F - var11), (double) (1.0F - var11));
				var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double) (0.0F + var11), (double) (1.0F - var11));
				var4.draw();
				EaglerAdapter.glPopMatrix();
			}

			if (ConfigConstants.panoramaBlur) {
				EaglerAdapter.glPopMatrix();
			}
			EaglerAdapter.glColorMask(true, true, true, false);
		}

		var4.setTranslation(0.0D, 0.0D, 0.0D);
		EaglerAdapter.glColorMask(true, true, true, true);
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glDepthMask(true);
		EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float par1) {
		EaglerAdapter.glBindTexture(EaglerAdapter.GL_TEXTURE_2D, viewportTexture);
		this.mc.renderEngine.resetBoundTexture();
		EaglerAdapter.glCopyTexSubImage2D(EaglerAdapter.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		EaglerAdapter.glColorMask(true, true, true, true);
		Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();
		byte var3 = 3;

		for (int var4 = 0; var4 < var3; ++var4) {
			var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (var4 + 1));
			int var5 = this.width;
			int var6 = this.height;
			float var7 = (float) (var4 - var3 / 2) / 256.0F;
			var2.addVertexWithUV((double) var5, (double) var6, (double) this.zLevel, (double) (0.0F + var7), 0.0D);
			var2.addVertexWithUV((double) var5, 0.0D, (double) this.zLevel, (double) (1.0F + var7), 0.0D);
			var2.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (1.0F + var7), 1.0D);
			var2.addVertexWithUV(0.0D, (double) var6, (double) this.zLevel, (double) (0.0F + var7), 1.0D);
		}

		var2.draw();
		EaglerAdapter.glColorMask(true, true, true, true);
		this.mc.renderEngine.resetBoundTexture();
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int par1, int par2, float par3) {
		if (ConfigConstants.panoramaBlur) {
			EaglerAdapter.glViewport(0, 0, 256, 256);
			this.drawPanorama(par1, par2, par3);
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			this.rotateAndBlurSkybox(par3);
			EaglerAdapter.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			Tessellator var4 = Tessellator.instance;
			var4.startDrawingQuads();
			float var5 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
			float var6 = (float) this.height * var5 / 256.0F;
			float var7 = (float) this.width * var5 / 256.0F;
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MIN_FILTER, EaglerAdapter.GL_LINEAR);
			EaglerAdapter.glTexParameteri(EaglerAdapter.GL_TEXTURE_2D, EaglerAdapter.GL_TEXTURE_MAG_FILTER, EaglerAdapter.GL_LINEAR);
			var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
			int var8 = this.width;
			int var9 = this.height;
			var4.addVertexWithUV(0.0D, (double) var9, (double) this.zLevel, (double) (0.5F - var6), (double) (0.5F + var7));
			var4.addVertexWithUV((double) var8, (double) var9, (double) this.zLevel, (double) (0.5F - var6), (double) (0.5F - var7));
			var4.addVertexWithUV((double) var8, 0.0D, (double) this.zLevel, (double) (0.5F + var6), (double) (0.5F - var7));
			var4.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (0.5F + var6), (double) (0.5F + var7));
			var4.draw();
		} else {
			EaglerAdapter.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.drawPanorama(par1, par2, par3);
		}
	}

	private static final TextureLocation mclogo = new TextureLocation("/title/mclogo.png");
	private static final TextureLocation eag = new TextureLocation("/title/eag.png");
	private static final TextureLocation ackbk = new TextureLocation("/gui/demo_bg.png");
	private static final TextureLocation beaconx = new TextureLocation("/gui/beacon.png");
	private static final TextureLocation items = new TextureLocation("/gui/items.png");

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		mousex = par1;
		mousey = par2;
		this.renderSkybox(par1, par2, par3);
		Tessellator var4 = Tessellator.instance;
		short var5 = 274;
		int var6 = this.width / 2 - var5 / 2;
		byte var7 = 30;
		if (ConfigConstants.panoramaBlur) {
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		}
		
		if(ConfigConstants.eaglercraftTitleLogo) {
			eag.bindTexture();
		}else {
			mclogo.bindTexture();
		}

		this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);

		this.drawString(this.fontRenderer, "minecraft 1.5.2", 2, this.height - 20, 16777215);
		this.drawString(this.fontRenderer, ConfigConstants.mainMenuString + EnumChatFormatting.GRAY + " (cracked)", 2, this.height - 10, 16777215);

		//String var10 = "Copyright " + Calendar.getInstance().get(Calendar.YEAR) + " Mojang AB.";
		String var10 = "copyright 2013 Mojang AB";
		this.drawString(this.fontRenderer, var10, this.width - this.fontRenderer.getStringWidth(var10) - 2, this.height - 10, 16777215);

		var10 = "site resources are";
		this.drawString(this.fontRenderer, var10, this.width - this.fontRenderer.getStringWidth(var10) - 2, this.height - 20, 16777215);
		
		if(showingEndian && EaglerAdapter.isBigEndian()) {
			this.drawCenteredString(fontRenderer, "(BIG Endian)", this.width / 2, this.height - 10, 0xFFFFBBBB);
		}
		
		if (this.field_92025_p != null && this.field_92025_p.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			this.drawString(this.fontRenderer, this.field_92025_p, this.field_92022_t, this.field_92021_u, 16777215);
			// this.drawString(this.fontRenderer, field_96138_a, (this.width -
			// this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12,
			// 16777215);
		}
		if (this.splashText != null) {
			var4.setColorOpaque_I(16777215);
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
			EaglerAdapter.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
			float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
			var8 = var8 * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
			EaglerAdapter.glScalef(var8, var8, var8);
			this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
			EaglerAdapter.glPopMatrix();
		}
		/*
		String lid = "(login is disabled, this copy violates Mojang's terms of service)";
		int sl = this.fontRenderer.getStringWidth(lid);

		EaglerAdapter.glPushMatrix();
		float k = ((this.width - sl) * 3 / 4) < 80 ? 0.5f : 0.75f;
		EaglerAdapter.glScalef(k, k, k);
		EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
		this.drawString(fontRenderer, lid, (int)(this.width / k - sl) / 2, (int)((this.height - 19) / k), 0x88999999);
		EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
		EaglerAdapter.glPopMatrix();
		*/
		var10 = "eaglercraft readme.txt";
		int w = this.fontRenderer.getStringWidth(var10) * 3 / 4;
		if(!showAck && par1 >= (this.width - w - 4) && par1 <= this.width && par2 >= 0 && par2 <= 9) {
			drawRect((this.width - w - 4), 0, this.width, 9, 0x55000099);
		}else {
			drawRect((this.width - w - 4), 0, this.width, 9, 0x55200000);
		}
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((this.width - w - 2), 1.0f, 0.0f);
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		this.drawString(this.fontRenderer, var10, 0, 0, 16777215);
		EaglerAdapter.glPopMatrix();
		
		/*
		var10 = "debug console";
		w = this.fontRenderer.getStringWidth(var10) * 3 / 4;
		if(!showAck && par1 >= 0 && par1 <= (w + 4) && par2 >= 0 && par2 <= 9) {
			drawRect(0, 0, w + 4, 9, 0x55000099);
		}else {
			drawRect(0, 0, w + 4, 9, 0x55200000);
		}
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef(2.0f, 1.0f, 0.0f);
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		this.drawString(this.fontRenderer, var10, 0, 0, 16777215);
		EaglerAdapter.glPopMatrix();
		*/
		
		if(ConfigConstants.mainMenuItemLink != null) {
			//drawRect((this.width - w - 4), 0, this.width, 9, 0x55200000);

			int posX = this.width / 2 - 170 - this.width / 10;
			int posY = this.height / 4 + 70;
			int ww = 66;
			int hh = 46;
			int ln0w = ConfigConstants.mainMenuItemLine0 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine0);
			ww = ww < ln0w ? ln0w : ww;
			hh = ln0w > 0 ? hh + 12 : hh;
			int ln1w = ConfigConstants.mainMenuItemLine1 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine1);
			ww = ww < ln1w ? ln1w : ww;
			hh = ln1w > 0 ? hh + 12 : hh;
			int ln2w = ConfigConstants.mainMenuItemLine2 == null ? 0 : fontRenderer.getStringWidth(ConfigConstants.mainMenuItemLine2);
			ww = ww < ln2w ? ln2w : ww;
			hh = ln2w > 0 ? hh + 12 : hh;
			
			ww += 20;
			hh += 20;
			
			boolean over = par1 > posX && par1 < posX + (ww / 4 * 3) && par2 > posY && par2 < posY + (hh / 4 * 3);
			
			int iconSize = 45;
			
			if(over) {
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(posX, posY, 0.0f);
				EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);

				drawRect(0, 0, ww, hh, 0x44000022);
				
				drawRect(3, 3, ww - 3, 4, 0x99999999);
				drawRect(3, hh - 4, ww - 3, hh - 3, 0x99999999);
				drawRect(3, 4, 4, hh - 4, 0x99999999);
				drawRect(ww - 4, 4, ww - 3, hh - 4, 0x99999999);
				
				int i = 10;
				
				if(ln0w > 0) {
					this.drawString(this.fontRenderer, ConfigConstants.mainMenuItemLine0, (ww - ln0w) / 2, i, 0xFFFF99);
					i += 12;
				}
				
				items.bindTexture();
				
				EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				this.drawTexturedModelRectFromIcon((ww - iconSize) / 2, i, Item.itemsList[showRandomItem].getIconFromDamage(0), iconSize, iconSize);
				
				i += iconSize + 5;
				
				if(ln1w > 0) {
					this.drawString(this.fontRenderer, ConfigConstants.mainMenuItemLine1, (ww - ln1w) / 2, i, 0xFFFF99);
					i += 12;
				}
				
				if(ln2w > 0) {
					this.drawString(this.fontRenderer, ConfigConstants.mainMenuItemLine2, (ww - ln2w) / 2, i, 0xDDDDDD);
				}
				
				int ww75 = (ww * 4 / 3);
				int hh75 = (hh * 4 / 3);
				
				//this.drawString(this.fontRenderer, var10, this.width - this.fontRenderer.getStringWidth(var10) - 2, this.height - 10, 16777215);
				
				EaglerAdapter.glPopMatrix();
				
			}else {
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
				EaglerAdapter.glColor4f(0.9f, 0.9f, 0.9f, MathHelper.sin((float)(System.currentTimeMillis() % 1000000l) / 300f) * 0.17f + 0.5f);
				
				items.bindTexture();
				
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(posX, posY, 0.0f);
				EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
				this.drawTexturedModelRectFromIcon((ww - iconSize) / 2, ln0w > 0 ? 22 : 10, Item.itemsList[showRandomItem].getIconFromDamage(0), iconSize, iconSize);
				EaglerAdapter.glPopMatrix();
				
				EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			}
			
		}
		
		if(showAck) {
			super.drawScreen(0, 0, par3);
			this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
			int x = (this.width - 345) / 2;
			int y = (this.height - 230) / 2;
			ackbk.bindTexture();
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef(x, y, 0.0f);
			EaglerAdapter.glScalef(1.39f, 1.39f, 1.39f);
			this.drawTexturedModalRect(0, 0, 0, 0, 248, 166);
			EaglerAdapter.glPopMatrix();
			beaconx.bindTexture();
			this.drawTexturedModalRect(x + 323, y + 7, 114, 223, 13, 13);
			int lines = this.ackLines.size();
			if(scrollPosition < 0) scrollPosition = 0;
			if(scrollPosition + visibleLines > lines) scrollPosition = lines - visibleLines;
			for(int i = 0; i < visibleLines; ++i) {
				this.fontRenderer.drawString(this.ackLines.get(scrollPosition + i), x + 10, y + 10 + (i * 10), 0x404060);
			}
			int trackHeight = 193;
			int offset = trackHeight * scrollPosition / lines;
			drawRect(x + 326, y + 27, x + 334, y + 220, 0x33000020);
			drawRect(x + 326, y + 27 + offset, x + 334, y + 27 + (visibleLines * trackHeight / lines) + offset + 1, 0x66000000);
		}else {
			super.drawScreen(par1, par2, par3);
		}
	}

	private int mousex = 0;
	private int mousey = 0;
	
	public void updateScreen() {
		if(EaglerAdapter.mouseIsButtonDown(0) && dragstart > 0) {
			int trackHeight = 193;
			scrollPosition = (mousey - dragstart) * this.ackLines.size() / trackHeight + dragstartI;
			if(scrollPosition < 0) scrollPosition = 0;
			if(scrollPosition + visibleLines > this.ackLines.size()) scrollPosition = this.ackLines.size() - visibleLines;
		}else {
			dragstart = -1;
		}
	}
	
}
