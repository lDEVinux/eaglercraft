package net.lax1dude.eaglercraft;

import net.lax1dude.eaglercraft.sp.ipc.IPCPacket15ThrowException;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenSingleplayerException extends GuiScreen {

	private GuiScreen mainmenu;
	private IPCPacket15ThrowException exception;
	private GuiButton returnToMenu;
	private String action;
	
	public GuiScreenSingleplayerException(GuiScreen mainmenu, String action, IPCPacket15ThrowException exception) {
		this.mainmenu = mainmenu;
		this.action = action;
		this.exception = exception;
	}
	
	public void initGui() {
		this.buttonList.add(returnToMenu = new GuiButton(0, this.width / 2 - 100, this.height / 3 + 36, StringTranslate.getInstance().translateKey("selectWorld.progress.continue")));
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();

		int width_ = this.fontRenderer.getStringWidth(exception.errorMessage);
		
		int numTrace = exception.stackTrace.size();
		if(numTrace > 7) {
			numTrace = 7;
		}
		int height_ = numTrace * 10 + 90 + (numTrace >= 7 ? 10 : 0);
		
		for(String s : exception.stackTrace) {
			int w = this.fontRenderer.getStringWidth("   " + s);
			if(width_ < w) {
				width_ = w;
			}
		}
		
		int top = (this.height - height_) / 2;
		if(top < 5) top = 5;
		int left = (this.width - width_) / 2;
		if(left < 5) left = 5;

		this.drawCenteredString(fontRenderer, "An error occured while '" + StringTranslate.getInstance().translateKey(action) + "'", this.width / 2, top, 0xFFAAAA);
		
		this.drawString(fontRenderer, exception.errorMessage, left, top + 20, 0xFFAAAA);
		for(int i = 0; i < numTrace; ++i) {
			this.drawString(fontRenderer, "   " + exception.stackTrace.get(i), left, top + 30 + i * 10, 0xFFAAAA);
		}
		if(numTrace >= 7) {
			this.drawCenteredString(fontRenderer, "... " + (exception.size() - numTrace) + " remaining ...", this.width / 2, top + 30 + numTrace * 10, 0xFFAAAA);
		}
		
		returnToMenu.yPosition = top + 46 + numTrace * 10 + (numTrace >= 7 ? 10 : 0);
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(mainmenu);
		}
	}
	
}
