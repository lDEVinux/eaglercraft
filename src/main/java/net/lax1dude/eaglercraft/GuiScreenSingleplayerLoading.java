package net.lax1dude.eaglercraft;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

import net.lax1dude.eaglercraft.sp.ipc.IPCPacket15ThrowException;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenSingleplayerLoading extends GuiScreen {

	public final GuiScreen menu;
	private GuiButton killTask;
	public final String message;
	private BooleanSupplier checkTaskComplete;
	private Runnable taskKill;
	private String lastStatus;
	private String currentStatus;
	private BiConsumer<GuiScreen, IPCPacket15ThrowException[]> onException;
	private int areYouSure;
	
	private long startStartTime;
	
	private static final Runnable defaultTerminateAction = () -> {
		IntegratedServer.killWorker();
		Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
	};
	
	public static GuiScreen createException(GuiScreen ok, String msg, IPCPacket15ThrowException[] exceptions) {
		for(int i = exceptions.length - 1; i >= 0; --i) {
			ok = new GuiScreenSingleplayerException(ok, msg, exceptions[i]);
		}
		return ok;
	}
	
	private static final BiConsumer<GuiScreen, IPCPacket15ThrowException[]> defaultExceptionAction = (t, u) -> {
		GuiScreenSingleplayerLoading tt = (GuiScreenSingleplayerLoading) t;
		Minecraft.getMinecraft().displayGuiScreen(createException(tt.menu, tt.message, u));
	};
	
	public GuiScreenSingleplayerLoading(GuiScreen menu, String message, BooleanSupplier checkTaskComplete) {
		this(menu, message, checkTaskComplete, defaultExceptionAction, defaultTerminateAction);
	}
	
	public GuiScreenSingleplayerLoading(GuiScreen menu, String message, BooleanSupplier checkTaskComplete, BiConsumer<GuiScreen, IPCPacket15ThrowException[]> exceptionAction) {
		this(menu, message, checkTaskComplete, exceptionAction, defaultTerminateAction);
	}
	
	public GuiScreenSingleplayerLoading(GuiScreen menu, String message, BooleanSupplier checkTaskComplete, Runnable onTerminate) {
		this(menu, message, checkTaskComplete, defaultExceptionAction, onTerminate);
	}
	
	public GuiScreenSingleplayerLoading(GuiScreen menu, String message, BooleanSupplier checkTaskComplete, BiConsumer<GuiScreen, IPCPacket15ThrowException[]> onException, Runnable onTerminate) {
		this.menu = menu;
		this.message = message;
		this.checkTaskComplete = checkTaskComplete;
		this.onException = onException;
		this.taskKill = onTerminate;
		this.lastStatus = IntegratedServer.worldStatusString();
		this.currentStatus = message;
	}
	
	public void initGui() {
		if(startStartTime == 0) this.startStartTime = System.currentTimeMillis();
		areYouSure = 0;
		this.buttonList.add(killTask = new GuiButton(0, this.width / 2 - 100, this.height / 3 + 50, StringTranslate.getInstance().translateKey("gui.killTask")));
		killTask.enabled = false;
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		float f = 2.0f;
		int top = this.height / 3;
		
		long millis = System.currentTimeMillis();
		
		String str = StringTranslate.getInstance().translateKey(currentStatus);
		
		long dots = (millis / 500l) % 4l;
		this.drawString(fontRenderer, str + (dots > 0 ? "." : "") + (dots > 1 ? "." : "") + (dots > 2 ? "." : ""), (this.width - this.fontRenderer.getStringWidth(str)) / 2, top + 10, 0xFFFFFF);
		
		if(areYouSure > 0) {
			this.drawCenteredString(fontRenderer, StringTranslate.getInstance().translateKey("selectWorld.progress.cancelWarning"), this.width / 2, top + 25, 0xFF8888);
		}else {
			float prog = IntegratedServer.worldStatusProgress();
			if(this.currentStatus.equals(this.lastStatus) && prog > 0.01f) {
				this.drawCenteredString(fontRenderer, (prog > 1.0f ? ("(" + (prog > 1000000.0f ? "" + (int)(prog / 1000000.0f) + "MB" :
					(prog > 1000.0f ? "" + (int)(prog / 1000.0f) + "kB" : "" + (int)prog + "B")) + ")") : "" + (int)(prog * 100.0f) + "%"), this.width / 2, top + 25, 0xFFFFFF);
			}else {
				long elapsed = (millis - startStartTime) / 1000l;
				if(elapsed > 3) {
					this.drawCenteredString(fontRenderer, "(" + elapsed + "s)", this.width / 2, top + 25, 0xFFFFFF);
				}
			}
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	public void updateScreen() {
		long millis = System.currentTimeMillis();
		if(millis - startStartTime > 6000l) {
			killTask.enabled = true;
		}
		if(IntegratedServer.didLastCallFail()) {
			IPCPacket15ThrowException[] pk = IntegratedServer.worldStatusErrors();
			if(pk != null) {
				onException.accept(this, pk);
			}else {
				onException.accept(this, new IPCPacket15ThrowException[] { new IPCPacket15ThrowException("Server Crash: State '" +
						IntegratedState.getStateName(IntegratedServer.statusState()) + "'", new String[0]) });
			}
			return;
		}
		if(checkTaskComplete.getAsBoolean()) {
			this.mc.displayGuiScreen(menu);
		}
		String str = IntegratedServer.worldStatusString();
		if(!lastStatus.equals(str)) {
			lastStatus = str;
			currentStatus = str;
		}
		killTask.displayString = StringTranslate.getInstance().translateKey(areYouSure > 0 ? "selectWorld.progress.confirmCancel" : "gui.killTask");
		if(areYouSure > 0) {
			--areYouSure;
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			if(areYouSure <= 0) {
				areYouSure = 80;
			}else if(areYouSure <= 65) {
				taskKill.run();
			}
		}
	}
	
}
