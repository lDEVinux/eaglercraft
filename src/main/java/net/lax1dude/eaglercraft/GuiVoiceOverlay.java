package net.lax1dude.eaglercraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiIngameMenu;

public class GuiVoiceOverlay extends Gui {
	
	public final Minecraft mc;
	public int width;
	public int height;
	
	private long pttTimer = 0l;
	
	public GuiVoiceOverlay(Minecraft mc) {
		this.mc = mc;
	}
	
	public void setResolution(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	private static final TextureLocation voiceGuiIcons = new TextureLocation("/gui/voice.png");

	public void drawOverlay() {
		if(mc.theWorld != null && EaglerAdapter.getVoiceStatus() == Voice.VoiceStatus.CONNECTED && EaglerAdapter.getVoiceChannel() != Voice.VoiceChannel.NONE &&
				!(mc.currentScreen != null && (mc.currentScreen instanceof GuiIngameMenu))) {
			
			if(mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame()) {
				return;
			}
			
			EaglerAdapter.glPushMatrix();
			
			if(mc.currentScreen == null || (mc.currentScreen instanceof GuiChat)) {
				EaglerAdapter.glTranslatef(width / 2 + 77, height - 56, 0.0f);
				if(mc.thePlayer == null || mc.thePlayer.capabilities.isCreativeMode) {
					EaglerAdapter.glTranslatef(0.0f, 16.0f, 0.0f);
				}
			}else {
				EaglerAdapter.glTranslatef(width / 2 + 10, 4, 0.0f);
			}

			EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
			
			String txxt = "press '" + EaglerAdapter.getKeyName(mc.gameSettings.voicePTTKey) + "'";
			drawString(mc.fontRenderer, txxt, -3 - mc.fontRenderer.getStringWidth(txxt), 9, 0xDDDDDD);

			EaglerAdapter.glScalef(0.66f, 0.66f, 0.66f);
			
			voiceGuiIcons.bindTexture();
			
			if((mc.currentScreen == null || !mc.currentScreen.blockHotKeys()) && EaglerAdapter.isKeyDown(mc.gameSettings.voicePTTKey)) {
				long millis = System.currentTimeMillis();
				if(pttTimer == 0l) {
					pttTimer = millis;
				}
				EaglerAdapter.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 32, 32, 32);
				EaglerAdapter.glTranslatef(-1.5f, -1.5f, 0.0f);
				if(millis - pttTimer < 1050l) {
					if((millis - pttTimer) % 300l < 150l) {
						EaglerAdapter.glColor4f(0.9f, 0.2f, 0.2f, 1.0f);
					}else {
						EaglerAdapter.glColor4f(0.9f, 0.7f, 0.7f, 1.0f);
					}
				}else {
					EaglerAdapter.glColor4f(0.9f, 0.3f, 0.3f, 1.0f);
				}
				drawTexturedModalRect(0, 0, 0, 32, 32, 32);
			}else {
				pttTimer = 0l;
				EaglerAdapter.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 0, 32, 32);
				EaglerAdapter.glTranslatef(-1.5f, -1.5f, 0.0f);
				EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				drawTexturedModalRect(0, 0, 0, 0, 32, 32);
				EaglerAdapter.glTranslatef(-0.5f, -0.5f, 0.0f);
				drawTexturedModalRect(0, 0, 0, 0, 32, 32);
			}
			
			EaglerAdapter.glPopMatrix();
			
			if(EaglerAdapter.getVoiceChannel() == Voice.VoiceChannel.PROXIMITY) {
				Set<String> listeners = EaglerAdapter.getVoiceListening();
				if(listeners.size() > 0) {
					Set<String> speakers = EaglerAdapter.getVoiceSpeaking();
					Set<String> muted = EaglerAdapter.getVoiceMuted();
					
					List<String> listenerList = new ArrayList();
					listenerList.addAll(listeners);
					listenerList.removeAll(muted);
					
					while(listenerList.size() > 5) {
						boolean flag = false;
						for(int i = 0, l = listenerList.size(); i < l; ++i) {
							if(!speakers.contains(listenerList.get(i))) {
								listenerList.remove(i);
								flag = true;
								break;
							}
						}
						if(!flag) {
							break;
						}
					}
					
					int more = listenerList.size() - 5;
					
					int ww = width;
					int hh = height;
					
					if(mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) {
						hh -= 15;
					}
					
					int left = 50;
					for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
						int j = mc.fontRenderer.getStringWidth(listenerList.get(i)) + 4;
						if(j > left) {
							left = j;
						}
					}
					
					if(more > 0) {
						EaglerAdapter.glPushMatrix();
						EaglerAdapter.glTranslatef(ww - left + 3, hh - 10, left);
						EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
						drawString(mc.fontRenderer, "(" + more + " more)", 0, 0, 0xBBBBBB);
						EaglerAdapter.glPopMatrix();
						hh -= 9;
					}
					
					for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
						
						String txt = listenerList.get(i);
						boolean speaking = speakers.contains(txt);
						float speakf = speaking ? 1.0f : 0.75f;
						
						drawString(mc.fontRenderer, txt, ww - left, hh - 13 - i * 11, speaking ? 0xEEEEEE : 0xBBBBBB);
						
						voiceGuiIcons.bindTexture();
						
						EaglerAdapter.glPushMatrix();
						EaglerAdapter.glTranslatef(ww - left - 14, hh - 14 - i * 11, 0.0f);
						
						EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
						EaglerAdapter.glColor4f(speakf * 0.2f, speakf * 0.2f, speakf * 0.2f, 1.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 144 : 176, 16, 16);
						EaglerAdapter.glTranslatef(0.25f, 0.25f, 0.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 144 : 176, 16, 16);
						
						EaglerAdapter.glTranslatef(-1.25f, -1.25f, 0.0f);
						EaglerAdapter.glColor4f(speakf, speakf, speakf, 1.0f);
						drawTexturedModalRect(0, 0, 64, speaking ? 144 : 176, 16, 16);
						
						EaglerAdapter.glPopMatrix();
						
					}
					
				}
			}else if(EaglerAdapter.getVoiceChannel() == Voice.VoiceChannel.GLOBAL) {
				Set<String> speakers = EaglerAdapter.getVoiceSpeaking();
				Set<String> muted = EaglerAdapter.getVoiceMuted();
				
				List<String> listenerList = new ArrayList();
				listenerList.addAll(speakers);
				listenerList.removeAll(muted);
				
				int more = listenerList.size() - 5;
				
				int ww = width;
				int hh = height;
				
				if(mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) {
					hh -= 15;
				}
				
				int left = 50;
				for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
					int j = mc.fontRenderer.getStringWidth(listenerList.get(i)) + 4;
					if(j > left) {
						left = j;
					}
				}
				
				if(more > 0) {
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef(ww - left + 3, hh - 10, left);
					EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
					drawString(mc.fontRenderer, "(" + more + " more)", 0, 0, 0xBBBBBB);
					EaglerAdapter.glPopMatrix();
					hh -= 9;
				}
				
				for(int i = 0, l = listenerList.size(); i < l && i < 5; ++i) {
					String txt = listenerList.get(i);
					
					drawString(mc.fontRenderer, txt, ww - left, hh - 13 - i * 11, 0xEEEEEE);
					
					voiceGuiIcons.bindTexture();
					
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef(ww - left - 14, hh - 14 - i * 11, 0.0f);
					
					EaglerAdapter.glScalef(0.75f, 0.75f, 0.75f);
					EaglerAdapter.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 144, 16, 16);
					EaglerAdapter.glTranslatef(0.25f, 0.25f, 0.0f);
					drawTexturedModalRect(0, 0, 64, 144, 16, 16);
					
					EaglerAdapter.glTranslatef(-1.25f, -1.25f, 0.0f);
					EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					drawTexturedModalRect(0, 0, 64, 144, 16, 16);
					
					EaglerAdapter.glPopMatrix();
					
				}
			}
		}
	}

}
