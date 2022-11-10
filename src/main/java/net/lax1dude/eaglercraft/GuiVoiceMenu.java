package net.lax1dude.eaglercraft;

import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider2;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;

public class GuiVoiceMenu extends GuiScreen  {
	
	protected final GuiScreen parent;

	protected int voiceButtonOFFposX;
	protected int voiceButtonOFFposY;
	protected int voiceButtonOFFposW;
	protected int voiceButtonOFFposH;

	protected int voiceButtonRADIUSposX;
	protected int voiceButtonRADIUSposY;
	protected int voiceButtonRADIUSposW;
	protected int voiceButtonRADIUSposH;

	protected int voiceButtonGLOBALposX;
	protected int voiceButtonGLOBALposY;
	protected int voiceButtonGLOBALposW;
	protected int voiceButtonGLOBALposH;

	protected int voiceScreenButtonOFFposX;
	protected int voiceScreenButtonOFFposY;
	protected int voiceScreenButtonOFFposW;
	protected int voiceScreenButtonOFFposH;

	protected int voiceScreenButtonRADIUSposX;
	protected int voiceScreenButtonRADIUSposY;
	protected int voiceScreenButtonRADIUSposW;
	protected int voiceScreenButtonRADIUSposH;

	protected int voiceScreenButtonGLOBALposX;
	protected int voiceScreenButtonGLOBALposY;
	protected int voiceScreenButtonGLOBALposW;
	protected int voiceScreenButtonGLOBALposH;

	protected int voiceScreenButtonChangeRadiusposX;
	protected int voiceScreenButtonChangeRadiusposY;
	protected int voiceScreenButtonChangeRadiusposW;
	protected int voiceScreenButtonChangeRadiusposH;
	
	protected int voiceScreenVolumeIndicatorX;
	protected int voiceScreenVolumeIndicatorY;
	protected int voiceScreenVolumeIndicatorW;
	protected int voiceScreenVolumeIndicatorH;

	protected boolean showSliderBlocks = false;
	protected boolean showSliderVolume = false;
	protected boolean showPTTKeyConfig = false;
	protected int showNewPTTKey = 0;
	protected GuiSlider2 sliderBlocks = null;
	protected GuiSlider2 sliderListenVolume = null;
	protected GuiSlider2 sliderSpeakVolume = null;

	protected GuiButton applyRadiusButton = null;
	protected GuiButton applyVolumeButton = null;
	protected GuiButton noticeContinueButton = null;
	protected GuiButton noticeCancelButton = null;

	protected static boolean showingCompatWarning = false;
	protected static boolean showCompatWarning = true;
	
	protected static boolean showingTrackingWarning = false;
	protected static boolean showTrackingWarning = true;
	
	protected static Voice.VoiceChannel continueChannel = null;
	
	public GuiVoiceMenu(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		this.sliderBlocks = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 20, 150, 20, (EaglerAdapter.getVoiceProximity() - 5) / 17.0f, 1.0f) {
			public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
				if(super.mousePressed(par1Minecraft, par2, par3)) {
					this.displayString = "" + (int)((sliderValue * 17.0f) + 5.0f) + " Blocks";
					return true;
				}else {
					return false;
				}
			}
			public void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
				super.mouseDragged(par1Minecraft, par2, par3);
				this.displayString = "" + (int)((sliderValue * 17.0f) + 5.0f) + " Blocks";
			}
		};
		sliderBlocks.displayString = "" + EaglerAdapter.getVoiceProximity() + " Blocks";
		this.sliderListenVolume = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 10, 150, 20, EaglerAdapter.getVoiceListenVolume(), 1.0f);
		this.sliderSpeakVolume = new GuiSlider2(-1, (width - 150) / 2, height / 3 + 56, 150, 20, EaglerAdapter.getVoiceSpeakVolume(), 1.0f);
		
		StringTranslate ts = StringTranslate.getInstance();
		this.buttonList.clear();
		this.buttonList.add(applyRadiusButton = new GuiButton(2, (width - 150) / 2, height / 3 + 49, 150, 20, ts.translateKey("voice.apply")));
		this.buttonList.add(applyVolumeButton = new GuiButton(3, (width - 150) / 2, height / 3 + 90, 150, 20, ts.translateKey("voice.apply")));
		this.buttonList.add(noticeContinueButton = new GuiButton(5, (width - 150) / 2, height / 3 + 60, 150, 20, ts.translateKey("voice.unsupportedWarning10")));
		this.buttonList.add(noticeCancelButton = new GuiButton(6, (width - 150) / 2, height / 3 + 90, 150, 20, ts.translateKey("voice.unsupportedWarning11")));
		applyRadiusButton.drawButton = applyVolumeButton.drawButton = noticeContinueButton.drawButton = noticeCancelButton.drawButton = false;
	}
	
	private static final TextureLocation voiceGuiIcons = new TextureLocation("/gui/voice.png");
	
	public void drawScreen(int mx, int my, float partialTicks) {
		
		StringTranslate ts = StringTranslate.getInstance();
		
		String txt = ts.translateKey("voice.title");
		drawString(fontRenderer, txt, width - 5 - fontRenderer.getStringWidth(txt), 5, 0xFFCC22);
		
		applyRadiusButton.drawButton = showSliderBlocks;
		applyVolumeButton.drawButton = showSliderVolume;
		
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig) {
			
			drawRect(0, 0, this.width, this.height, 0xB0101010);
			
			if(showSliderBlocks) {
				
				drawRect(width / 2 - 86, height / 4 - 1, this.width / 2 + 86, height / 3 + 64 + height / 16, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 4 + 0, this.width / 2 + 85, height / 3 + 63 + height / 16, 0xFF333333);
				
				drawCenteredString(this.fontRenderer, ts.translateKey("voice.radiusTitle"), this.width / 2, height / 4 + 9, 16777215);
				drawString(this.fontRenderer, ts.translateKey("voice.radiusLabel"), (this.width - 150) / 2 + 3, height / 3 + 6, 0xCCCCCC);
				sliderBlocks.drawButton(mc, mx, my);
				
			}else if(showSliderVolume) {
				
				drawRect(width / 2 - 86, height / 4 - 11, this.width / 2 + 86, height / 3 + 104 + height / 16, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 4 - 10, this.width / 2 + 85, height / 3 + 103 + height / 16, 0xFF333333);
				
				drawCenteredString(this.fontRenderer, ts.translateKey("voice.volumeTitle"), this.width / 2, height / 4 - 1, 16777215);
				drawString(this.fontRenderer, ts.translateKey("voice.volumeListen"), (this.width - 150) / 2 + 3, height / 3 - 4, 0xCCCCCC);
				sliderListenVolume.drawButton(mc, mx, my);
				
				drawString(this.fontRenderer, ts.translateKey("voice.volumeSpeak"), (this.width - 150) / 2 + 3, height / 3 + 42, 0xCCCCCC);
				sliderSpeakVolume.drawButton(mc, mx, my);
				
			}else if(showPTTKeyConfig) {
				
				drawRect(width / 2 - 86, height / 3 - 10, this.width / 2 + 86, height / 3 + 35, 0xFFDDDDDD);
				drawRect(width / 2 - 85, height / 3 - 9, this.width / 2 + 85, height / 3 + 34, 0xFF333333);
				
				if(showNewPTTKey > 0) {
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef(this.width / 2, height / 3 + 5, 0.0f);
					EaglerAdapter.glScalef(2.0f, 2.0f, 2.0f);
					drawCenteredString(this.fontRenderer, EaglerAdapter.getKeyName(mc.gameSettings.voicePTTKey), 0, 0, 0xFFCC11);
					EaglerAdapter.glPopMatrix();
				}else {
					drawCenteredString(this.fontRenderer, ts.translateKey("voice.pttChangeDesc"), this.width / 2, height / 3 + 8, 16777215);
				}
			}
			
			super.drawScreen(mx, my, partialTicks);
			
			throw new AbortedException();
		}
		
		EaglerAdapter.glPushMatrix();
		
		EaglerAdapter.glTranslatef(width - 6, 15, 0.0f);
		EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
		
		if(!EaglerAdapter.voiceAvailable()) {
			txt = ts.translateKey("voice.titleVoiceUnavailable");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 6, 0xFF7777);
			txt = ts.translateKey("voice.titleVoiceBrowserError");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 19, 0xAA4444);
			return;
		}
		
		if(!EaglerAdapter.voiceAllowed()) {
			txt = ts.translateKey("voice.titleNoVoice");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 5, 0xFF7777);
			return;
		}

		int xo = 0;
		if(EaglerAdapter.voiceRelayed()) {
			txt = ts.translateKey("voice.warning1");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 8, 0xBB9999);
			txt = ts.translateKey("voice.warning2");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 18, 0xBB9999);
			txt = ts.translateKey("voice.warning3");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 28, 0xBB9999);
			xo = 43;
			EaglerAdapter.glTranslatef(0.0f, xo, 0.0f);
		}
		
		Voice.VoiceStatus status = EaglerAdapter.getVoiceStatus();
		Voice.VoiceChannel channel = EaglerAdapter.getVoiceChannel();
		
		boolean flag = false;
		
		if(channel == Voice.VoiceChannel.NONE) {
			flag = true;
		}else {
			if(status == Voice.VoiceStatus.CONNECTED) {
				
				if(channel == Voice.VoiceChannel.PROXIMITY) {
					txt = ts.translateKey("voice.connectedRadius").replace("$radius$", "" + EaglerAdapter.getVoiceProximity()).replace("$f$", "");
					int w = fontRenderer.getStringWidth(txt);
					int xx = width - 5 - (w * 3 / 4);
					int yy = 15 + (xo * 3 / 4);
					voiceScreenButtonChangeRadiusposX = xx;
					voiceScreenButtonChangeRadiusposY = yy;
					voiceScreenButtonChangeRadiusposW = width - 3 - xx;
					voiceScreenButtonChangeRadiusposH = 12;
					if(mx >= xx && my >= yy && mx < xx + voiceScreenButtonChangeRadiusposW && my < yy + 12) {
						txt = ts.translateKey("voice.connectedRadius").replace("$radius$", "" + EaglerAdapter.getVoiceProximity())
								.replace("$f$", "" + EnumChatFormatting.UNDERLINE) + EnumChatFormatting.RESET;
					}
				}else {
					txt = ts.translateKey("voice.connectedGlobal");
				}
				
				voiceScreenVolumeIndicatorX = width - 15 - (104 * 3 / 4);
				voiceScreenVolumeIndicatorY = 15 + (xo * 3 / 4) + 30;
				voiceScreenVolumeIndicatorW = width - voiceScreenVolumeIndicatorX - 4;
				voiceScreenVolumeIndicatorH = 23;
				
				drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 5, 0x66DD66);

				drawRect(-90, 42, 2, 52, 0xFFAAAAAA);
				drawRect(-89, 43, 1, 51, 0xFF222222);
				
				float vol = EaglerAdapter.getVoiceListenVolume();
				drawRect(-89, 43, -89 + (int)(vol * 90), 51, 0xFF993322);
				
				for(float f = 0.07f; f < vol; f += 0.08f) {
					int ww = (int)(f * 90);
					drawRect(-89 + ww, 43, -89 + ww + 1, 51, 0xFF999999);
				}

				drawRect(-90, 57, 2, 67, 0xFFAAAAAA);
				drawRect(-89, 58, 1, 66, 0xFF222222);
				
				vol = EaglerAdapter.getVoiceSpeakVolume();
				drawRect(-89, 58, -89 + (int)(vol * 90), 66, 0xFF993322);
				
				for(float f = 0.07f; f < vol; f += 0.08f) {
					int ww = (int)(f * 90);
					drawRect(-89 + ww, 58, -89 + ww + 1, 66, 0xFF999999);
				}
				
				voiceGuiIcons.bindTexture();
				EaglerAdapter.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
				
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(-104.0f, 41.5f, 0.0f);
				EaglerAdapter.glScalef(0.7f, 0.7f, 0.7f);
				drawTexturedModalRect(0, 0, 64, 112, 16, 16);
				EaglerAdapter.glPopMatrix();
				
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(-104.0f, 56.5f, 0.0f);
				EaglerAdapter.glScalef(0.7f, 0.7f, 0.7f);
				if((mc.currentScreen == null || !mc.currentScreen.blockHotKeys()) && EaglerAdapter.isKeyDown(mc.gameSettings.voicePTTKey)) {
					EaglerAdapter.glColor4f(0.9f, 0.4f, 0.4f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 32, 16, 16);
				}else {
					drawTexturedModalRect(0, 0, 64, 0, 16, 16);
				}
				EaglerAdapter.glPopMatrix();
				
				txt = ts.translateKey("voice.ptt").replace("$key$", EaglerAdapter.getKeyName(mc.gameSettings.voicePTTKey));
				drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt) - 10, 76, 0x66DD66);

				voiceGuiIcons.bindTexture();
				EaglerAdapter.glColor4f(0.4f, 0.9f, 0.4f, 1.0f);
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(-7.0f, 74.5f, 0.0f);
				EaglerAdapter.glScalef(0.35f, 0.35f, 0.35f);
				drawTexturedModalRect(0, 0, 32, 192, 32, 32);
				EaglerAdapter.glPopMatrix();
				
				txt = ts.translateKey("voice.playersListening");
				
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(0.0f, 98.0f, 0.0f);
				EaglerAdapter.glScalef(1.2f, 1.2f, 1.2f);
				drawString(fontRenderer, txt, -fontRenderer.getStringWidth(txt), 0, 0xFF7777);
				EaglerAdapter.glPopMatrix();

				List<String> playersToRender = EaglerAdapter.getVoiceRecent();
				
				if(playersToRender.size() > 0) {
					Set<String> playersSpeaking = EaglerAdapter.getVoiceSpeaking();
					Set<String> playersMuted = EaglerAdapter.getVoiceMuted();
					for(int i = 0, l = playersToRender.size(); i < l; ++i) {
						txt = playersToRender.get(i);
						
						boolean muted = playersMuted.contains(txt);
						boolean speaking = !muted && playersSpeaking.contains(txt);
						
						int mhy = voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 33 + i * 9;
						boolean hovered = mx >= voiceScreenVolumeIndicatorX - 3 && my >= mhy && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 && my < mhy + 9;
						float cm = hovered ? 1.5f : 1.0f;
						voiceGuiIcons.bindTexture();

						EaglerAdapter.glPushMatrix();
						EaglerAdapter.glTranslatef(-100.0f, 115.0f + i * 12.0f, 0.0f);
						EaglerAdapter.glScalef(0.78f, 0.78f, 0.78f);
						
						if(muted) {
							EaglerAdapter.glColor4f(1.0f * cm, 0.2f * cm, 0.2f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 176, 16, 16);
						}else if(speaking) {
							EaglerAdapter.glColor4f(1.0f * cm, 1.0f * cm, 1.0f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 144, 16, 16);
						}else {
							EaglerAdapter.glColor4f(0.65f * cm, 0.65f * cm, 0.65f * cm, 1.0f);
							drawTexturedModalRect(0, 0, 64, 112, 16, 16);
						}
						
						EaglerAdapter.glPopMatrix();

						if(muted) {
							drawString(fontRenderer, txt, -84, 117 + i * 12, attenuate(0xCC4444, cm));
						}else if(speaking) {
							drawString(fontRenderer, txt, -84, 117 + i * 12, attenuate(0xCCCCCC, cm));
						}else {
							drawString(fontRenderer, txt, -84, 117 + i * 12, attenuate(0x999999, cm));
						}
						
					}
				}else {
					txt = "(none)";
					drawString(fontRenderer, txt, -fontRenderer.getStringWidth(txt), 112, 0xAAAAAA);
				}
				
			}else if(status == Voice.VoiceStatus.CONNECTING) {
				float fadeTimer = MathHelper.sin((float)((System.currentTimeMillis() % 700l) * 0.0014d) * 3.14159f) * 0.35f + 0.3f;
				txt = ts.translateKey("voice.connecting");
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
				drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 5, (0xFFDD77 | ((int)(Math.pow(fadeTimer, 1.0d / 2.2d) * 255.0f) << 24)));
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			}else if(status == Voice.VoiceStatus.UNAVAILABLE) {
				txt = ts.translateKey("voice.unavailable");
				drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 5, 0xFF3333);
			}else {
				flag = true;
			}
		}
		
		if(flag) {
			txt = ts.translateKey("voice.notConnected");
			drawString(fontRenderer, txt, 1 - fontRenderer.getStringWidth(txt), 5, 0xBB9999);
		}

		String OFFstring = ts.translateKey("voice.off");
		String RADIUSstring = ts.translateKey("voice.radius");
		String GLOBALstring = ts.translateKey("voice.global");

		int OFFwidth = fontRenderer.getStringWidth(OFFstring);
		int RADIUSwidth = fontRenderer.getStringWidth(RADIUSstring);
		int GLOBALwidth = fontRenderer.getStringWidth(GLOBALstring);
		
		voiceButtonOFFposX = 0 - OFFwidth - 8 - RADIUSwidth - 8 - GLOBALwidth;
		voiceButtonOFFposY = 20;
		voiceButtonOFFposW = OFFwidth + 5;
		voiceButtonOFFposH = 15;

		voiceScreenButtonOFFposX = voiceButtonOFFposX * 3 / 4 + width - 6;
		voiceScreenButtonOFFposY = 15 + (voiceButtonOFFposY + xo) * 3 / 4;
		voiceScreenButtonOFFposW = voiceButtonOFFposW * 3 / 4;
		voiceScreenButtonOFFposH = voiceButtonOFFposH * 3 / 4;

		voiceButtonRADIUSposX = 0 - RADIUSwidth - 8 - GLOBALwidth;
		voiceButtonRADIUSposY = 20;
		voiceButtonRADIUSposW = RADIUSwidth + 5;
		voiceButtonRADIUSposH = 15;

		voiceScreenButtonRADIUSposX = voiceButtonRADIUSposX * 3 / 4 + width - 6;
		voiceScreenButtonRADIUSposY = 15 + (voiceButtonRADIUSposY + xo) * 3 / 4;
		voiceScreenButtonRADIUSposW = voiceButtonRADIUSposW * 3 / 4;
		voiceScreenButtonRADIUSposH = voiceButtonRADIUSposH * 3 / 4;

		voiceButtonGLOBALposX = 0 - GLOBALwidth;
		voiceButtonGLOBALposY = 20;
		voiceButtonGLOBALposW = GLOBALwidth + 5;
		voiceButtonGLOBALposH = 15;

		voiceScreenButtonGLOBALposX = voiceButtonGLOBALposX * 3 / 4 + width - 6;
		voiceScreenButtonGLOBALposY = 15 + (voiceButtonGLOBALposY + xo) * 3 / 4;
		voiceScreenButtonGLOBALposW = voiceButtonGLOBALposW * 3 / 4;
		voiceScreenButtonGLOBALposH = voiceButtonGLOBALposH * 3 / 4;
		
		if(channel == Voice.VoiceChannel.NONE) {
			drawOutline(voiceButtonOFFposX, voiceButtonOFFposY, voiceButtonOFFposW, voiceButtonOFFposH, 0xFFCCCCCC);
			drawRect(voiceButtonOFFposX + 1, voiceButtonOFFposY + 1, voiceButtonOFFposX + voiceButtonOFFposW - 2,
					voiceButtonOFFposY + voiceButtonOFFposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonOFFposX && my >= voiceScreenButtonOFFposY && mx < voiceScreenButtonOFFposX +
				voiceScreenButtonOFFposW && my < voiceScreenButtonOFFposY + voiceScreenButtonOFFposH) {
			drawOutline(voiceButtonOFFposX, voiceButtonOFFposY, voiceButtonOFFposW, voiceButtonOFFposH, 0xFF777777);
		}

		if(channel == Voice.VoiceChannel.PROXIMITY) {
			drawOutline(voiceButtonRADIUSposX, voiceButtonRADIUSposY, voiceButtonRADIUSposW, voiceButtonRADIUSposH, 0xFFCCCCCC);
			drawRect(voiceButtonRADIUSposX + 1, voiceButtonRADIUSposY + 1, voiceButtonRADIUSposX + voiceButtonRADIUSposW - 2,
					voiceButtonRADIUSposY + voiceButtonRADIUSposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonRADIUSposX && my >= voiceScreenButtonRADIUSposY && mx < voiceScreenButtonRADIUSposX +
				voiceScreenButtonRADIUSposW && my < voiceScreenButtonRADIUSposY + voiceScreenButtonRADIUSposH) {
			drawOutline(voiceButtonRADIUSposX, voiceButtonRADIUSposY, voiceButtonRADIUSposW, voiceButtonRADIUSposH, 0xFF777777);
		}

		if(channel == Voice.VoiceChannel.GLOBAL) {
			drawOutline(voiceButtonGLOBALposX, voiceButtonGLOBALposY, voiceButtonGLOBALposW, voiceButtonGLOBALposH, 0xFFCCCCCC);
			drawRect(voiceButtonGLOBALposX + 1, voiceButtonGLOBALposY + 1, voiceButtonGLOBALposX + voiceButtonGLOBALposW - 2,
					voiceButtonGLOBALposY + voiceButtonGLOBALposH - 1, 0xFF222222);
		}else if(mx >= voiceScreenButtonGLOBALposX && my >= voiceScreenButtonGLOBALposY && mx < voiceScreenButtonGLOBALposX +
				voiceScreenButtonGLOBALposW && my < voiceScreenButtonGLOBALposY + voiceScreenButtonGLOBALposH) {
			drawOutline(voiceButtonGLOBALposX, voiceButtonGLOBALposY, voiceButtonGLOBALposW, voiceButtonGLOBALposH, 0xFF777777);
		}

		int enabledColor = (status == Voice.VoiceStatus.CONNECTED || channel == Voice.VoiceChannel.NONE) ? 0x66DD66 : 0xDDCC66;
		int disabledColor = 0xDD4444;
		
		if(status == Voice.VoiceStatus.UNAVAILABLE) {
			enabledColor = disabledColor;
		}
		
		drawString(fontRenderer, OFFstring, 3 - OFFwidth - 8 - RADIUSwidth - 8 - GLOBALwidth, 24, channel == Voice.VoiceChannel.NONE ? enabledColor : disabledColor);
		drawString(fontRenderer, RADIUSstring, 3 - RADIUSwidth - 8 - GLOBALwidth, 24, channel == Voice.VoiceChannel.PROXIMITY ? enabledColor : disabledColor);
		drawString(fontRenderer, GLOBALstring, 3 - GLOBALwidth, 24, channel == Voice.VoiceChannel.GLOBAL ? enabledColor : disabledColor);
		
		EaglerAdapter.glPopMatrix();
		
		if(showingCompatWarning) {
			
			drawNotice(ts.translateKey("voice.unsupportedWarning1"), false, ts.translateKey("voice.unsupportedWarning2"), ts.translateKey("voice.unsupportedWarning3"),
					"", ts.translateKey("voice.unsupportedWarning4"), ts.translateKey("voice.unsupportedWarning5"), ts.translateKey("voice.unsupportedWarning6"),
					ts.translateKey("voice.unsupportedWarning7"), ts.translateKey("voice.unsupportedWarning8"), ts.translateKey("voice.unsupportedWarning9"));
			
			noticeContinueButton.drawButton = true;
			noticeCancelButton.drawButton = false;
		}else if(showingTrackingWarning) {
			
			drawNotice(ts.translateKey("voice.ipGrabWarning1"), true, ts.translateKey("voice.ipGrabWarning2"), ts.translateKey("voice.ipGrabWarning3"),
					ts.translateKey("voice.ipGrabWarning4"), "", ts.translateKey("voice.ipGrabWarning5"), ts.translateKey("voice.ipGrabWarning6"),
					ts.translateKey("voice.ipGrabWarning7"), ts.translateKey("voice.ipGrabWarning8"), ts.translateKey("voice.ipGrabWarning9"),
					ts.translateKey("voice.ipGrabWarning10"), ts.translateKey("voice.ipGrabWarning11"), ts.translateKey("voice.ipGrabWarning12"));
			
			noticeContinueButton.drawButton = true;
			noticeCancelButton.drawButton = true;
		}else {
			noticeContinueButton.drawButton = false;
			noticeCancelButton.drawButton = false;
		}
		
		super.drawScreen(mx, my, partialTicks);

		if(showingCompatWarning || showingTrackingWarning) {
			throw new AbortedException();
		}
	}
	
	private void drawNotice(String title, boolean showCancel, String... lines) {
		
		int widthAccum = 0;
		
		for(int i = 0; i < lines.length; ++i) {
			int w = fontRenderer.getStringWidth(lines[i]);
			if(widthAccum < w) {
				widthAccum = w;
			}
		}
		
		int margin = 15;
		
		int x = (width - widthAccum) / 2;
		int y = (height - lines.length * 10 - 60 - margin) / 2;

		drawRect(x - margin - 1, y - margin - 1, x + widthAccum + margin + 1,
				y + lines.length * 10 + 49 + margin, 0xFFCCCCCC);
		drawRect(x - margin, y - margin, x + widthAccum + margin,
				y + lines.length * 10 + 48 + margin, 0xFF111111);
		
		drawCenteredString(fontRenderer, EnumChatFormatting.BOLD + title, width / 2, y, 0xFF7766);
		
		for(int i = 0; i < lines.length; ++i) {
			drawString(fontRenderer, lines[i], x, y + i * 10 + 18, 0xDDAAAA);
		}
		
		if(!showCancel) {
			noticeContinueButton.width = 150;
			noticeContinueButton.xPosition = (width - 150) / 2;
			noticeContinueButton.yPosition = y + lines.length * 10 + 29;
		}else {
			noticeContinueButton.width = widthAccum / 2 - 10;
			noticeContinueButton.xPosition = (width - widthAccum) / 2 + widthAccum / 2 + 3;
			noticeContinueButton.yPosition = y + lines.length * 10 + 28;
			noticeCancelButton.width = widthAccum / 2 - 10;
			noticeCancelButton.xPosition = (width - widthAccum) / 2 + 4;
			noticeCancelButton.yPosition = y + lines.length * 10 + 28;
		}
		
	}
	
	public static int attenuate(int cin, float f) {
		return attenuate(cin, f, f, f, 1.0f);
	}
	
	public static int attenuate(int cin, float r, float g, float b, float a) {
		float var10 = (float) (cin >> 24 & 255) / 255.0F;
		float var6 = (float) (cin >> 16 & 255) / 255.0F;
		float var7 = (float) (cin >> 8 & 255) / 255.0F;
		float var8 = (float) (cin & 255) / 255.0F;
		var10 *= a;
		var6 *= r;
		var7 *= g;
		var8 *= b;
		if(var10 > 1.0f) {
			var10 = 1.0f;
		}
		if(var6 > 1.0f) {
			var6 = 1.0f;
		}
		if(var7 > 1.0f) {
			var7 = 1.0f;
		}
		if(var8 > 1.0f) {
			var8 = 1.0f;
		}
		return (((int)(var10 * 255.0f) << 24) | ((int)(var6 * 255.0f) << 16) | ((int)(var7 * 255.0f) << 8) | (int)(var8 * 255.0f));
	}
	
	private void drawOutline(int x, int y, int w, int h, int color) {
		drawRect(x, y, x + w, y + 1, color);
		drawRect(x + w - 1, y + 1, x + w, y + h - 1, color);
		drawRect(x, y + h - 1, x + w, y + h, color);
		drawRect(x, y + 1, x + 1, y + h - 1, color);
	}
	
	public void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
		if(showSliderBlocks || showSliderVolume) {
			if(showSliderBlocks) {
				if(par3 == 0) {
					sliderBlocks.mouseReleased(par1, par2);
				}
			}else if(showSliderVolume) {
				if(par3 == 0) {
					sliderListenVolume.mouseReleased(par1, par2);
					sliderSpeakVolume.mouseReleased(par1, par2);
				}
			}
			throw new AbortedException();
		}
	}
	
	public void keyTyped(char par1, int par2) {
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig) {
			if(showPTTKeyConfig) {
				if(par2 == 1) {
					showPTTKeyConfig = false;
				}else {
					mc.gameSettings.voicePTTKey = par2;
					showNewPTTKey = 10;
				}
			}
			throw new AbortedException();
		}
	}
	
	public void mouseClicked(int mx, int my, int button) {
		if(showSliderBlocks || showSliderVolume || showPTTKeyConfig || showingCompatWarning || showingTrackingWarning) {
			if(showSliderBlocks) {
				sliderBlocks.mousePressed(mc, mx, my);
			}else if(showSliderVolume) {
				sliderListenVolume.mousePressed(mc, mx, my);
				sliderSpeakVolume.mousePressed(mc, mx, my);
			}
			super.mouseClicked(mx, my, button);
			throw new AbortedException();
		}
		
		Voice.VoiceStatus status = EaglerAdapter.getVoiceStatus();
		Voice.VoiceChannel channel = EaglerAdapter.getVoiceChannel();
		
		if(button == 0) {
			if(EaglerAdapter.voiceAvailable() && EaglerAdapter.voiceAllowed()) {
				if(mx >= voiceScreenButtonOFFposX && my >= voiceScreenButtonOFFposY && mx < voiceScreenButtonOFFposX +
						voiceScreenButtonOFFposW && my < voiceScreenButtonOFFposY + voiceScreenButtonOFFposH) {
					EaglerAdapter.enableVoice(Voice.VoiceChannel.NONE);
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}else if(mx >= voiceScreenButtonRADIUSposX && my >= voiceScreenButtonRADIUSposY && mx < voiceScreenButtonRADIUSposX +
						voiceScreenButtonRADIUSposW && my < voiceScreenButtonRADIUSposY + voiceScreenButtonRADIUSposH) {
					
					if(showCompatWarning) {
						continueChannel = Voice.VoiceChannel.PROXIMITY;
						showingCompatWarning = true;
					}else if(showTrackingWarning) {
						continueChannel = Voice.VoiceChannel.PROXIMITY;
						showingTrackingWarning = true;
					}else {
						EaglerAdapter.enableVoice(Voice.VoiceChannel.PROXIMITY);
					}
					
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					
				}else if(mx >= voiceScreenButtonGLOBALposX && my >= voiceScreenButtonGLOBALposY && mx < voiceScreenButtonGLOBALposX +
						voiceScreenButtonGLOBALposW && my < voiceScreenButtonGLOBALposY + voiceScreenButtonGLOBALposH) {
					
					if(showCompatWarning) {
						continueChannel = Voice.VoiceChannel.GLOBAL;
						showingCompatWarning = true;
					}else if(showTrackingWarning) {
						continueChannel = Voice.VoiceChannel.GLOBAL;
						showingTrackingWarning = true;
					}else {
						EaglerAdapter.enableVoice(Voice.VoiceChannel.GLOBAL);
					}
					
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}else if(channel == Voice.VoiceChannel.PROXIMITY && status == Voice.VoiceStatus.CONNECTED && mx >= voiceScreenButtonChangeRadiusposX &&
						my >= voiceScreenButtonChangeRadiusposY && mx < voiceScreenButtonChangeRadiusposX + voiceScreenButtonChangeRadiusposW &&
						my < voiceScreenButtonChangeRadiusposY + voiceScreenButtonChangeRadiusposH) {
					showSliderBlocks = true;
					sliderBlocks.sliderValue = (EaglerAdapter.getVoiceProximity() - 5) / 17.0f;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}else if(status == Voice.VoiceStatus.CONNECTED && channel != Voice.VoiceChannel.NONE && mx >= voiceScreenVolumeIndicatorX &&
						my >= voiceScreenVolumeIndicatorY && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW &&
						my < voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH) {
					showSliderVolume = true;
					sliderListenVolume.sliderValue = EaglerAdapter.getVoiceListenVolume();
					sliderSpeakVolume.sliderValue = EaglerAdapter.getVoiceSpeakVolume();
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}else if(status == Voice.VoiceStatus.CONNECTED && channel != Voice.VoiceChannel.NONE && mx >= voiceScreenVolumeIndicatorX - 1 &&
						my >= voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 2 && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 &&
						my < voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 12) {
					showPTTKeyConfig = true;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}else if(status == Voice.VoiceStatus.CONNECTED) {
					List<String> playersToRender = EaglerAdapter.getVoiceRecent();
					if(playersToRender.size() > 0) {
						Set<String> playersMuted = EaglerAdapter.getVoiceMuted();
						for(int i = 0, l = playersToRender.size(); i < l; ++i) {
							String txt = playersToRender.get(i);
							boolean muted = playersMuted.contains(txt);
							int mhy = voiceScreenVolumeIndicatorY + voiceScreenVolumeIndicatorH + 33 + i * 9;
							if(mx >= voiceScreenVolumeIndicatorX - 3 && my >= mhy && mx < voiceScreenVolumeIndicatorX + voiceScreenVolumeIndicatorW + 2 && my < mhy + 9) {
								EaglerAdapter.setVoiceMuted(txt, !muted);
								this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
								break;
							}
						}
					}
				}
			}
		}
		
	}
	
	public void actionPerformed(GuiButton btn) {
		if(btn.id == 2) {
			showSliderBlocks = false;
			EaglerAdapter.setVoiceProximity(mc.gameSettings.voiceListenRadius = (int)((sliderBlocks.sliderValue * 17.0f) + 5.0f));
			mc.gameSettings.saveOptions();
		}else if(btn.id == 3) {
			showSliderVolume = false;
			EaglerAdapter.setVoiceListenVolume(mc.gameSettings.voiceListenVolume = sliderListenVolume.sliderValue);
			EaglerAdapter.setVoiceSpeakVolume(mc.gameSettings.voiceSpeakVolume = sliderSpeakVolume.sliderValue);
			mc.gameSettings.saveOptions();
		}else if(btn.id == 4) {
			showPTTKeyConfig = false;
			mc.gameSettings.saveOptions();
		}else if(btn.id == 5) {
			if(showingCompatWarning) {
				showingCompatWarning = false;
				showCompatWarning = false;
				if(showTrackingWarning) {
					showingTrackingWarning = true;
				}else {
					EaglerAdapter.enableVoice(continueChannel);
				}
			}else if(showingTrackingWarning) {
				showingTrackingWarning = false;
				showTrackingWarning = false;
				EaglerAdapter.enableVoice(continueChannel);
			}
		}else if(btn.id == 6) {
			if(showingTrackingWarning) {
				showingTrackingWarning = false;
				EaglerAdapter.enableVoice(Voice.VoiceChannel.NONE);
			}
		}
	}
	
	public void updateScreen() {
		if(showNewPTTKey > 0) {
			--showNewPTTKey;
			if(showNewPTTKey == 0) {
				showPTTKeyConfig = false;
				mc.gameSettings.saveOptions();
			}
		}
	}

	public boolean isBlockingInput() {
		return showSliderBlocks || showSliderVolume || showPTTKeyConfig || showingCompatWarning || showingTrackingWarning;
	}
		
}
