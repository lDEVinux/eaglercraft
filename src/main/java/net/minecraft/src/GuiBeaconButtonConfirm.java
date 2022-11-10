package net.minecraft.src;

class GuiBeaconButtonConfirm extends GuiBeaconButton {
	/** Beacon GUI this button belongs to. */
	final GuiBeacon beaconGui;

	public GuiBeaconButtonConfirm(GuiBeacon par1, int par2, int par3, int par4) {
		super(par2, par3, par4, "/gui/beacon.png", 90, 220);
		this.beaconGui = par1;
	}

	public void func_82251_b(int par1, int par2) {
		this.beaconGui.drawCreativeTabHoveringText(StatCollector.translateToLocal("gui.done"), par1, par2);
	}
}
