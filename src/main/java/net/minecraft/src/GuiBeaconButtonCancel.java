package net.minecraft.src;

class GuiBeaconButtonCancel extends GuiBeaconButton {
	/** Beacon GUI this button belongs to. */
	final GuiBeacon beaconGui;

	public GuiBeaconButtonCancel(GuiBeacon par1, int par2, int par3, int par4) {
		super(par2, par3, par4, "/gui/beacon.png", 112, 220);
		this.beaconGui = par1;
	}

	public void func_82251_b(int par1, int par2) {
		this.beaconGui.drawCreativeTabHoveringText(StatCollector.translateToLocal("gui.cancel"), par1, par2);
	}
}
