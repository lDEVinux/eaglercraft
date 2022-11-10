package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;
import net.minecraft.client.Minecraft;

public class GuiInventory extends InventoryEffectRenderer {
	/**
	 * x size of the inventory window in pixels. Defined as float, passed as int
	 */
	private float xSize_lo;

	/**
	 * y size of the inventory window in pixels. Defined as float, passed as int.
	 */
	private float ySize_lo;

	public GuiInventory(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer.inventoryContainer);
		this.allowUserInput = true;
		par1EntityPlayer.addStat(AchievementList.openInventory, 1);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();

		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		} else {
			super.initGui();
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 86, 16, 4210752);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.xSize_lo = (float) par1;
		this.ySize_lo = (float) par2;
	}
	
	private static final TextureLocation tex = new TextureLocation("/gui/inventory.png");

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tex.bindTexture();
		int var4 = this.guiLeft;
		int var5 = this.guiTop;
		this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
		drawPlayerOnGui(this.mc, var4 + 51, var5 + 75, 30, (float) (var4 + 51) - this.xSize_lo, (float) (var5 + 75 - 50) - this.ySize_lo);
	}

	public static void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5) {
		EaglerAdapter.glEnable(EaglerAdapter.GL_COLOR_MATERIAL);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glTranslatef((float) par1, (float) par2, 50.0F);
		EaglerAdapter.glScalef((float) (-par3), (float) par3, (float) par3);
		EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var6 = par0Minecraft.thePlayer.renderYawOffset;
		float var7 = par0Minecraft.thePlayer.rotationYaw;
		float var8 = par0Minecraft.thePlayer.rotationPitch;
		EaglerAdapter.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		EaglerAdapter.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(-((float) Math.atan((double) (par5 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		par0Minecraft.thePlayer.renderYawOffset = (float) Math.atan((double) (par4 / 40.0F)) * 20.0F;
		par0Minecraft.thePlayer.rotationYaw = (float) Math.atan((double) (par4 / 40.0F)) * 40.0F;
		par0Minecraft.thePlayer.rotationPitch = -((float) Math.atan((double) (par5 / 40.0F))) * 20.0F;
		par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
		EaglerAdapter.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		par0Minecraft.thePlayer.renderYawOffset = var6;
		par0Minecraft.thePlayer.rotationYaw = var7;
		par0Minecraft.thePlayer.rotationPitch = var8;
		EaglerAdapter.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		
	}
}
