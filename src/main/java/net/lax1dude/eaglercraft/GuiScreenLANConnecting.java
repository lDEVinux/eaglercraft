package net.lax1dude.eaglercraft;

import java.io.IOException;

import net.minecraft.src.GuiDisconnected;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet2ClientProtocol;
import net.minecraft.src.StringTranslate;

public class GuiScreenLANConnecting extends GuiScreen {

	private final GuiScreen parent;
	private final String code;
	private final RelayServer relay;
	
	private boolean completed = false;
	
	private NetClientHandler netHandler = null;
	
	private int renderCount = 0;
	
	public GuiScreenLANConnecting(GuiScreen parent, String code) {
		this.parent = parent;
		this.code = code;
		this.relay = null;
	}
	
	public GuiScreenLANConnecting(GuiScreen parent, String code, RelayServer relay) {
		this.parent = parent;
		this.code = code;
		this.relay = relay;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void updateScreen() {
		if(netHandler != null) {
			netHandler.processReadPackets();
		}
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		StringTranslate st = StringTranslate.getInstance();
		if(completed) {
			String message = st.translateKey("connect.authorizing");
			this.drawString(fontRenderer, message, (this.width - this.fontRenderer.getStringWidth(message)) / 2, this.height / 3 + 10, 0xFFFFFF);
		}else {
			LoadingScreenRenderer ls = mc.loadingScreen;
			
			String message = st.translateKey("lanServer.pleaseWait");
			this.drawString(fontRenderer, message, (this.width - this.fontRenderer.getStringWidth(message)) / 2, this.height / 3 + 10, 0xFFFFFF);
			
			if(++renderCount > 1) {
				RelayServerSocket sock;
				if(relay == null) {
					sock = IntegratedServer.relayManager.getWorkingRelay((str) -> ls.resetProgresAndWorkingMessage("Connecting: " + str), 0x02, code);
				}else {
					sock = IntegratedServer.relayManager.connectHandshake(relay, 0x02, code);
				}
				if(sock == null) {
					this.mc.displayGuiScreen(new GuiScreenNoRelays(parent, st.translateKey("noRelay.worldNotFound1").replace("$code$", code),
							st.translateKey("noRelay.worldNotFound2").replace("$code$", code), st.translateKey("noRelay.worldNotFound3")));
					return;
				}
				
				LANClientNetworkManager netMgr = LANClientNetworkManager.connectToWorld(sock, code, sock.getURI());
				if(netMgr == null) {
					this.mc.displayGuiScreen(new GuiDisconnected(parent, "connect.failed", "disconnect.genericReason", st.translateKey("noRelay.worldFail").replace("$code$", code), ""));
					return;
				}
				
				completed = true;
				
				try {
					netHandler = new NetClientHandler(mc, netMgr);
					this.mc.setNetManager(netMgr);
					netMgr.setNetHandler(netHandler);
					netHandler.addToSendQueue(new Packet2ClientProtocol(61, EaglerProfile.username, "127.0.0.1", mc.gameSettings.renderDistance));
					netHandler.addToSendQueue(new Packet250CustomPayload("EAG|MySkin", EaglerProfile.getSkinPacket()));
					netHandler.addToSendQueue(new Packet250CustomPayload("EAG|MyCape", EaglerProfile.getCapePacket()));
				} catch (IOException e) {
					this.mc.displayGuiScreen(new GuiDisconnected(parent, "connect.failed", "disconnect.genericReason", "could not create nethandler", ""));
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
}
