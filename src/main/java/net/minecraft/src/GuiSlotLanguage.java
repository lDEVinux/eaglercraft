package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import net.lax1dude.eaglercraft.adapter.Tessellator;

class GuiSlotLanguage extends GuiSlot {
	private ArrayList field_77251_g;
	private TreeMap field_77253_h;

	final GuiLanguage languageGui;

	public GuiSlotLanguage(GuiLanguage par1GuiLanguage) {
		super(par1GuiLanguage.mc, par1GuiLanguage.width, par1GuiLanguage.height, 32, par1GuiLanguage.height - 65 + 4, 18);
		this.languageGui = par1GuiLanguage;
		this.field_77253_h = StringTranslate.getInstance().getLanguageList();
		this.field_77251_g = new ArrayList();
		Iterator var2 = this.field_77253_h.keySet().iterator();

		while (var2.hasNext()) {
			String var3 = (String) var2.next();
			this.field_77251_g.add(var3);
		}
	}

	/**
	 * Gets the size of the current slot list.
	 */
	protected int getSize() {
		return this.field_77251_g.size();
	}

	/**
	 * the element in the slot that was clicked, boolean for wether it was double
	 * clicked or not
	 */
	protected void elementClicked(int par1, boolean par2) {
		StringTranslate.getInstance().setLanguage((String) this.field_77251_g.get(par1), false);
		this.languageGui.mc.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
		GuiLanguage.getGameSettings(this.languageGui).language = (String) this.field_77251_g.get(par1);
		this.languageGui.fontRenderer.setBidiFlag(StringTranslate.isBidirectional(GuiLanguage.getGameSettings(this.languageGui).language));
		GuiLanguage.getDoneButton(this.languageGui).displayString = StringTranslate.getInstance().translateKey("gui.done");
		GuiLanguage.getGameSettings(this.languageGui).saveOptions();
	}

	/**
	 * returns true if the element passed in is currently selected
	 */
	protected boolean isSelected(int par1) {
		return ((String) this.field_77251_g.get(par1)).equals(StringTranslate.getInstance().getCurrentLanguage());
	}

	/**
	 * return the height of the content being scrolled
	 */
	protected int getContentHeight() {
		return this.getSize() * 18;
	}

	protected void drawBackground() {
		this.languageGui.drawDefaultBackground();
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
		this.languageGui.fontRenderer.setBidiFlag(true);
		this.languageGui.drawCenteredString(this.languageGui.fontRenderer, (String) this.field_77253_h.get(this.field_77251_g.get(par1)), this.languageGui.width / 2, par3 + 1, 16777215);
		this.languageGui.fontRenderer.setBidiFlag(StringTranslate.isBidirectional(GuiLanguage.getGameSettings(this.languageGui).language));
	}
}
