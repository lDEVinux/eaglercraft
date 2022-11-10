package net.lax1dude.eaglercraft;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiDisconnected;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet2ClientProtocol;
import net.minecraft.src.WorldClient;

public class GuiScreenSingleplayerConnecting extends GuiScreen {

	private GuiScreen menu;
	private String message;
	private GuiButton killTask;
	private NetClientHandler netHandler = null;
	
	private long startStartTime;
	
	public GuiScreenSingleplayerConnecting(GuiScreen menu, String message) {
		this.menu = menu;
		this.message = message;
	}
	
	public void initGui() {
		if(startStartTime == 0) this.startStartTime = System.currentTimeMillis();
		this.buttonList.add(killTask = new GuiButton(0, this.width / 2 - 100, this.height / 3 + 50, "Kill Task"));
		killTask.enabled = false;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		float f = 2.0f;
		int top = this.height / 3;
		
		long millis = System.currentTimeMillis();
		
		long dots = (millis / 500l) % 4l;
		this.drawString(fontRenderer, message + (dots > 0 ? "." : "") + (dots > 1 ? "." : "") + (dots > 2 ? "." : ""), (this.width - this.fontRenderer.getStringWidth(message)) / 2, top + 10, 0xFFFFFF);
		
		long elapsed = (millis - startStartTime) / 1000l;
		if(elapsed > 3) {
			this.drawCenteredString(fontRenderer, "(" + elapsed + "s)", this.width / 2, top + 25, 0xFFFFFF);
		}
		
		super.drawScreen(par1, par2, par3);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void updateScreen() {
		if(netHandler == null) {
			try {
				netHandler = new NetClientHandler(mc, EaglerProfile.username);
				this.mc.setNetManager(netHandler.getNetManager());
				netHandler.addToSendQueue(new Packet2ClientProtocol(61, EaglerProfile.username, "127.0.0.1", mc.gameSettings.renderDistance));
				netHandler.addToSendQueue(new Packet250CustomPayload("EAG|MySkin", EaglerProfile.getSkinPacket()));
				netHandler.addToSendQueue(new Packet250CustomPayload("EAG|MyCape", EaglerProfile.getCapePacket()));
			} catch (IOException e) {
				this.mc.displayGuiScreen(new GuiDisconnected(this.menu, "connect.failed", "disconnect.genericReason", "could not create nethandler", ""));
				e.printStackTrace();
				return;
			}
		}
		
		long millis = System.currentTimeMillis();
		if(millis - startStartTime > 6000l) {
			killTask.enabled = true;
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			IntegratedServer.killWorker();
			this.mc.loadWorld((WorldClient)null);
			this.mc.displayGuiScreen(menu);
			if(netHandler != null) {
				netHandler.getNetManager().closeConnections();
				Minecraft.getMinecraft().setNetManager(null);
			}
		}
	}
	
}
