package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.RelayQuery.VersionMismatch;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiSlot;

class GuiSlotRelay extends GuiSlot {

	final GuiScreenRelay screen;
	final RelayManager relayManager;
	
	public GuiSlotRelay(GuiScreenRelay screen) {
		super(GuiScreenRelay.getMinecraft(screen), screen.width, screen.height, 32, screen.height - 64, 26);
		this.screen = screen;
		this.relayManager = IntegratedServer.relayManager;
	}

	@Override
	protected int getSize() {
		return relayManager.count();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		screen.selected = var1;
		screen.updateButtons();
	}

	@Override
	protected boolean isSelected(int var1) {
		return screen.selected == var1;
	}

	@Override
	protected void drawBackground() {
		screen.drawDefaultBackground();
	}
	
	private static final TextureLocation icons = new TextureLocation("/gui/icons.png");

	@Override
	protected void drawSlot(int id, int xx, int yy, int height, Tessellator var5) {
		if(id < relayManager.count()) {
			icons.bindTexture();
			RelayServer srv = relayManager.get(id);
			String comment = srv.comment;
			int var15 = 0;
			int var16 = 0;
			String str = null;
			int h = 12;
			long ping = srv.getPing();
			if(ping == 0l) {
				var16 = 5;
				str = "No Connection";
			}else if(ping < 0l) {
				var15 = 1;
				var16 = (int) (Minecraft.getSystemTime() / 100L + (long) (id * 2) & 7L);
				if (var16 > 4) {
					var16 = 8 - var16;
				}
				str = "Polling...";
			}else {
				VersionMismatch vm = srv.getPingCompatible();
				if(!vm.isCompatible()) {
					var16 = 5;
					switch(vm) {
					case CLIENT_OUTDATED:
						str = "Outdated Client!";
						break;
					case RELAY_OUTDATED:
						str = "Outdated Relay!";
						break;
					default:
					case UNKNOWN:
						str = "Incompatible Relay!";
						break;
					}
					EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef(xx + 205, yy + 11, 0.0f);
					EaglerAdapter.glScalef(0.6f, 0.6f, 0.6f);
					screen.drawTexturedModalRect(0, 0, 0, 144, 16, 16);
					EaglerAdapter.glPopMatrix();
					h += 10;
				}else {
					String pingComment = srv.getPingComment().trim();
					if(pingComment.length() > 0) {
						comment = pingComment;
					}
					str = "" + ping + "ms";
					if (ping < 150L) {
						var16 = 0;
					} else if (ping < 300L) {
						var16 = 1;
					} else if (ping < 600L) {
						var16 = 2;
					} else if (ping < 1000L) {
						var16 = 3;
					} else {
						var16 = 4;
					}
				}
			}
			
			EaglerAdapter.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			screen.drawTexturedModalRect(xx + 205, yy, 0 + var15 * 10, 176 + var16 * 8, 10, 8);
			if(srv.isPrimary()) {
				EaglerAdapter.glPushMatrix();
				EaglerAdapter.glTranslatef(xx + 4, yy + 5, 0.0f);
				EaglerAdapter.glScalef(0.8f, 0.8f, 0.8f);
				screen.drawTexturedModalRect(0, 0, 0, 160, 16, 16);
				EaglerAdapter.glPopMatrix();
			}
			
			screen.drawString(mc.fontRenderer, comment, xx + 22, yy + 2, 0xFFFFFFFF);
			screen.drawString(mc.fontRenderer, srv.address, xx + 22, yy + 12, 0xFF999999);
			
			if(str != null) {
				int mx = screen.getFrameMouseX();
				int my = screen.getFrameMouseY();
				int rx = xx + 202;
				if(mx > rx && mx < rx + 13 && my > yy - 1 && my < yy + h) {
					screen.setToolTip(str);
				}
			}
		}
	}

}
