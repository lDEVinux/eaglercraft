package net.lax1dude.eaglercraft;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;

public class GuiScreenSkinCapeSettings extends GuiScreen {

	private final GuiScreen parent;

	private String skinCustomizationTitle = "yee";
	private String skinCustomizationOtherPlayers = "yee";

	private GuiButton toggleCape;
	private GuiButton toggleJacket;
	private GuiButton toggleHat;
	private GuiButton toggleLeftArm;
	private GuiButton toggleRightArm;
	private GuiButton toggleLeftLeg;
	private GuiButton toggleRightLeg;
	private GuiButton toggleShowErasers;
	private GuiButton toggleShowOtherCapes;
	
	public GuiScreenSkinCapeSettings(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		skinCustomizationTitle = var1.translateKey("menu.skinCapeSettings.skinCustomization");
		skinCustomizationOtherPlayers = var1.translateKey("menu.skinCapeSettings.skinCustomization.otherPlayers");
		
		int offset = MathHelper.clamp_int((height - 300) / 3, -100, 0);
		
		buttonList.add(new GuiButton(0, ((width - 230) / 2), 225 + offset, 230, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.apply")));
		
		buttonList.add(toggleJacket = new GuiButton(1, width / 2 - 152, 60 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.jacket") + ": " + 
				(mc.gameSettings.showSkinJacket ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleHat = new GuiButton(2, width / 2 + 2, 60 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.hat") + ": " + 
				(mc.gameSettings.showSkinHat ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleLeftArm = new GuiButton(3, width / 2 - 152, 82 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.leftArm") + ": " + 
				(mc.gameSettings.showSkinLeftArm ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleRightArm = new GuiButton(4, width / 2 + 2, 82 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.rightArm") + ": " + 
				(mc.gameSettings.showSkinRightArm ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleLeftLeg = new GuiButton(5, width / 2 - 152, 104 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.leftPants") + ": " + 
				(mc.gameSettings.showSkinLeftLeg ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleRightLeg = new GuiButton(6, width / 2 + 2, 104 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.rightPants") + ": " + 
				(mc.gameSettings.showSkinRightLeg ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleCape = new GuiButton(7, width / 2 - 85, 130 + offset, 165, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.cape") + ": " + 
				(mc.gameSettings.showCape ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
		buttonList.add(toggleShowErasers = new GuiButton(8, width / 2 - 152, 190 + offset, 150, 20, (mc.gameSettings.allowFNAWSkins ? 
				var1.translateKey("menu.skinCapeSettings.skinCustomization.showErasersOn") : var1.translateKey("menu.skinCapeSettings.skinCustomization.showErasersOff"))));
		buttonList.add(toggleShowOtherCapes = new GuiButton(9, width / 2 + 2, 190 + offset, 150, 20, var1.translateKey("menu.skinCapeSettings.skinCustomization.showOtherCapes") + ": " + 
				(mc.gameSettings.showOtherCapes ? var1.translateKey("options.on") : var1.translateKey("options.off"))));
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		StringTranslate var1 = StringTranslate.getInstance();
		switch(par1GuiButton.id) {
		case 0:
			mc.displayGuiScreen(parent);
			mc.gameSettings.saveOptions();
			break;
		case 1:
			mc.gameSettings.showSkinJacket = !mc.gameSettings.showSkinJacket;
			toggleJacket.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.jacket") + ": " + 
					(mc.gameSettings.showSkinJacket ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 2:
			mc.gameSettings.showSkinHat = !mc.gameSettings.showSkinHat;
			toggleHat.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.hat") + ": " + 
					(mc.gameSettings.showSkinHat ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 3:
			mc.gameSettings.showSkinLeftArm = !mc.gameSettings.showSkinLeftArm;
			toggleLeftArm.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.leftArm") + ": " + 
					(mc.gameSettings.showSkinLeftArm ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 4:
			mc.gameSettings.showSkinRightArm = !mc.gameSettings.showSkinRightArm;
			toggleRightArm.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.rightArm") + ": " + 
					(mc.gameSettings.showSkinRightArm ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 5:
			mc.gameSettings.showSkinLeftLeg = !mc.gameSettings.showSkinLeftLeg;
			toggleLeftLeg.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.leftPants") + ": " + 
					(mc.gameSettings.showSkinLeftLeg ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 6:
			mc.gameSettings.showSkinRightLeg = !mc.gameSettings.showSkinRightLeg;
			toggleRightLeg.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.rightPants") + ": " + 
					(mc.gameSettings.showSkinRightLeg ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 7:
			mc.gameSettings.showCape = !mc.gameSettings.showCape;
			toggleCape.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.cape") + ": " + 
					(mc.gameSettings.showCape ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		case 8:
			mc.gameSettings.allowFNAWSkins = !mc.gameSettings.allowFNAWSkins;
			toggleShowErasers.displayString = mc.gameSettings.allowFNAWSkins ? var1.translateKey("menu.skinCapeSettings.skinCustomization.showErasersOn") : 
					var1.translateKey("menu.skinCapeSettings.skinCustomization.showErasersOff");
			break;
		case 9:
			mc.gameSettings.showOtherCapes = !mc.gameSettings.showOtherCapes;
			toggleShowOtherCapes.displayString = var1.translateKey("menu.skinCapeSettings.skinCustomization.showOtherCapes") + ": " + 
					(mc.gameSettings.showOtherCapes ? var1.translateKey("options.on") : var1.translateKey("options.off"));
			break;
		default:
			break;
		}
	}

	public void drawScreen(int mx, int my, float partialTicks) {
		drawDefaultBackground();

		int offset = MathHelper.clamp_int((height - 300) / 3, -100, 0);
		
		this.drawCenteredString(this.fontRenderer, skinCustomizationTitle, this.width / 2, 40 + offset, 16777215);
		this.drawCenteredString(this.fontRenderer, skinCustomizationOtherPlayers, this.width / 2, 170 + offset, 16777215);
		
		super.drawScreen(mx, my, partialTicks);
		
	}

}
