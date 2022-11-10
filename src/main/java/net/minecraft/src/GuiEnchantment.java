package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.TextureLocation;

public class GuiEnchantment extends GuiContainer {
	/** The book model used on the GUI. */
	private static ModelBook bookModel = new ModelBook();
	private EaglercraftRandom rand = new EaglercraftRandom();

	/** ContainerEnchantment object associated with this gui */
	private ContainerEnchantment containerEnchantment;
	public int field_74214_o;
	public float field_74213_p;
	public float field_74212_q;
	public float field_74211_r;
	public float field_74210_s;
	public float field_74209_t;
	public float field_74208_u;
	ItemStack theItemStack;
	private String field_94079_C;

	public GuiEnchantment(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, String par6Str) {
		super(new ContainerEnchantment(par1InventoryPlayer, par2World, par3, par4, par5));
		this.containerEnchantment = (ContainerEnchantment) this.inventorySlots;
		this.field_94079_C = par6Str;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(this.field_94079_C == null ? StatCollector.translateToLocal("container.enchant") : this.field_94079_C, 12, 5, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		this.func_74205_h();
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		int var4 = (this.width - this.xSize) / 2;
		int var5 = (this.height - this.ySize) / 2;

		for (int var6 = 0; var6 < 3; ++var6) {
			int var7 = par1 - (var4 + 60);
			int var8 = par2 - (var5 + 14 + 19 * var6);

			if (var7 >= 0 && var8 >= 0 && var7 < 108 && var8 < 19 && this.containerEnchantment.enchantItem(this.mc.thePlayer, var6)) {
				this.mc.playerController.sendEnchantPacket(this.containerEnchantment.windowId, var6);
			}
		}
	}
	
	private static final TextureLocation tex_enchant = new TextureLocation("/gui/enchant.png");
	private static final TextureLocation tex_book = new TextureLocation("/item/book.png");

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tex_enchant.bindTexture();
		int var4 = (this.width - this.xSize) / 2;
		int var5 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glLoadIdentity();
		ScaledResolution var6 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		EaglerAdapter.glViewport((var6.getScaledWidth() - 432) / 2 * var6.getScaleFactor(), (var6.getScaledHeight() - 182) / 2 * var6.getScaleFactor(), 320 * var6.getScaleFactor(), 240 * var6.getScaleFactor());
		EaglerAdapter.glTranslatef(-0.34F, 0.23F, 0.0F);
		EaglerAdapter.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
		float var7 = 1.0F;
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glLoadIdentity();
		RenderHelper.enableStandardItemLighting();
		EaglerAdapter.glTranslatef(0.0F, 3.3F, -16.0F);
		EaglerAdapter.glScalef(var7, var7, var7);
		float var8 = 5.0F;
		EaglerAdapter.glScalef(var8, var8, var8);
		EaglerAdapter.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		tex_book.bindTexture();
		EaglerAdapter.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
		float var9 = this.field_74208_u + (this.field_74209_t - this.field_74208_u) * par1;
		EaglerAdapter.glTranslatef((1.0F - var9) * 0.2F, (1.0F - var9) * 0.1F, (1.0F - var9) * 0.25F);
		EaglerAdapter.glRotatef(-(1.0F - var9) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
		EaglerAdapter.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		float var10 = this.field_74212_q + (this.field_74213_p - this.field_74212_q) * par1 + 0.25F;
		float var11 = this.field_74212_q + (this.field_74213_p - this.field_74212_q) * par1 + 0.75F;
		var10 = (var10 - (float) MathHelper.truncateDoubleToInt((double) var10)) * 1.6F - 0.3F;
		var11 = (var11 - (float) MathHelper.truncateDoubleToInt((double) var11)) * 1.6F - 0.3F;

		if (var10 < 0.0F) {
			var10 = 0.0F;
		}

		if (var11 < 0.0F) {
			var11 = 0.0F;
		}

		if (var10 > 1.0F) {
			var10 = 1.0F;
		}

		if (var11 > 1.0F) {
			var11 = 1.0F;
		}

		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		bookModel.render((Entity) null, 0.0F, var10, var11, var9, 0.0F, 0.0625F);
		EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_PROJECTION);
		EaglerAdapter.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		EaglerAdapter.glPopMatrix();
		EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
		EaglerAdapter.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tex_enchant.bindTexture();
		EnchantmentNameParts.instance.setRandSeed(this.containerEnchantment.nameSeed);

		for (int var12 = 0; var12 < 3; ++var12) {
			String var13 = EnchantmentNameParts.instance.generateRandomEnchantName();
			this.zLevel = 0.0F;
			tex_enchant.bindTexture();
			int var14 = this.containerEnchantment.enchantLevels[var12];
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			if (var14 == 0) {
				this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 185, 108, 19);
			} else {
				String var15 = "" + var14;
				FontRenderer var16 = this.mc.standardGalacticFontRenderer;
				int var17 = 6839882;

				if (this.mc.thePlayer.experienceLevel < var14 && !this.mc.thePlayer.capabilities.isCreativeMode) {
					this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 185, 108, 19);
					var16.drawSplitString(var13, var4 + 62, var5 + 16 + 19 * var12, 104, (var17 & 16711422) >> 1);
					var16 = this.mc.fontRenderer;
					var17 = 4226832;
					var16.drawStringWithShadow(var15, var4 + 62 + 104 - var16.getStringWidth(var15), var5 + 16 + 19 * var12 + 7, var17);
				} else {
					int var18 = par2 - (var4 + 60);
					int var19 = par3 - (var5 + 14 + 19 * var12);

					if (var18 >= 0 && var19 >= 0 && var18 < 108 && var19 < 19) {
						this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 204, 108, 19);
						var17 = 16777088;
					} else {
						this.drawTexturedModalRect(var4 + 60, var5 + 14 + 19 * var12, 0, 166, 108, 19);
					}

					var16.drawSplitString(var13, var4 + 62, var5 + 16 + 19 * var12, 104, var17);
					var16 = this.mc.fontRenderer;
					var17 = 8453920;
					var16.drawStringWithShadow(var15, var4 + 62 + 104 - var16.getStringWidth(var15), var5 + 16 + 19 * var12 + 7, var17);
				}
			}
		}
	}

	public void func_74205_h() {
		ItemStack var1 = this.inventorySlots.getSlot(0).getStack();

		if (!ItemStack.areItemStacksEqual(var1, this.theItemStack)) {
			this.theItemStack = var1;

			do {
				this.field_74211_r += (float) (this.rand.nextInt(4) - this.rand.nextInt(4));
			} while (this.field_74213_p <= this.field_74211_r + 1.0F && this.field_74213_p >= this.field_74211_r - 1.0F);
		}

		++this.field_74214_o;
		this.field_74212_q = this.field_74213_p;
		this.field_74208_u = this.field_74209_t;
		boolean var2 = false;

		for (int var3 = 0; var3 < 3; ++var3) {
			if (this.containerEnchantment.enchantLevels[var3] != 0) {
				var2 = true;
			}
		}

		if (var2) {
			this.field_74209_t += 0.2F;
		} else {
			this.field_74209_t -= 0.2F;
		}

		if (this.field_74209_t < 0.0F) {
			this.field_74209_t = 0.0F;
		}

		if (this.field_74209_t > 1.0F) {
			this.field_74209_t = 1.0F;
		}

		float var5 = (this.field_74211_r - this.field_74213_p) * 0.4F;
		float var4 = 0.2F;

		if (var5 < -var4) {
			var5 = -var4;
		}

		if (var5 > var4) {
			var5 = var4;
		}

		this.field_74210_s += (var5 - this.field_74210_s) * 0.9F;
		this.field_74213_p += this.field_74210_s;
	}
}
