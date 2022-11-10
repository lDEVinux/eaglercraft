package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiCreateWorld;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenCreateWorldSelection extends GuiScreen {

	private GuiScreen mainmenu;
	private GuiButton worldCreate = null;
	private GuiButton worldImport = null;
	private GuiButton worldVanilla = null;
	private boolean isImportingEPK = false;
	private boolean isImportingMCA = false;
	
	public GuiScreenCreateWorldSelection(GuiScreen mainmenu) {
		this.mainmenu = mainmenu;
	}
	
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.buttonList.add(worldCreate = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 40, var1.translateKey("selectWorld.create.create")));
		this.buttonList.add(worldImport = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 65, var1.translateKey("selectWorld.create.import")));
		this.buttonList.add(worldVanilla = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 90, var1.translateKey("selectWorld.create.vanilla")));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 130, var1.translateKey("gui.cancel")));
	}
	
	public void updateScreen() {
		if(EaglerAdapter.getFileChooserResultAvailable() && (isImportingEPK || isImportingMCA)) {
			this.mc.displayGuiScreen(new GuiScreenNameWorldImport(mainmenu, EaglerAdapter.getFileChooserResultName(), isImportingEPK ? 0 : (isImportingMCA ? 1 : -1)));
			isImportingEPK = isImportingMCA = false;
		}
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create.title"), this.width / 2, this.height / 4, 16777215);
		
		int toolTipColor = 0xDDDDAA;
		if(worldCreate.func_82252_a()) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create.create.tooltip"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}else if(worldImport.func_82252_a()) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create.import.tooltip"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}else if(worldVanilla.func_82252_a()) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create.vanilla.tooltip"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(mainmenu);
		}else if(par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiCreateWorld(mainmenu));
		}else if(par1GuiButton.id == 2) {
			isImportingEPK = true;
			EaglerAdapter.openFileChooser("epk", null);
		}else if(par1GuiButton.id == 3) {
			isImportingMCA = true;
			EaglerAdapter.openFileChooser("zip", null);
		}
	}
	
}
