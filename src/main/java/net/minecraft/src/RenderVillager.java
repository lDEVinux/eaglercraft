package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class RenderVillager extends RenderLiving {
	/** Model of the villager. */
	protected ModelVillager villagerModel;

	public RenderVillager() {
		super(new ModelVillager(0.0F), 0.5F);
		this.villagerModel = (ModelVillager) this.mainModel;
	}

	/**
	 * Determines wether Villager Render pass or not.
	 */
	protected int shouldVillagerRenderPass(EntityVillager par1EntityVillager, int par2, float par3) {
		return -1;
	}

	public void renderVillager(EntityVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityVillager, par2, par4, par6, par8, par9);
	}

	protected void renderVillagerEquipedItems(EntityVillager par1EntityVillager, float par2) {
		super.renderEquippedItems(par1EntityVillager, par2);
	}

	protected void preRenderVillager(EntityVillager par1EntityVillager, float par2) {
		float var3 = 0.9375F;

		if (par1EntityVillager.getGrowingAge() < 0) {
			var3 = (float) ((double) var3 * 0.5D);
			this.shadowSize = 0.25F;
		} else {
			this.shadowSize = 0.5F;
		}

		EaglerAdapter.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.preRenderVillager((EntityVillager) par1EntityLiving, par2);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.shouldVillagerRenderPass((EntityVillager) par1EntityLiving, par2, par3);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderVillagerEquipedItems((EntityVillager) par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderVillager((EntityVillager) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderVillager((EntityVillager) par1Entity, par2, par4, par6, par8, par9);
	}

	private static final TextureLocation entityTexture0 = new TextureLocation("/mob/villager/farmer.png");
	private static final TextureLocation entityTexture1 = new TextureLocation("/mob/villager/librarian.png");
	private static final TextureLocation entityTexture2 = new TextureLocation("/mob/villager/priest.png");
	private static final TextureLocation entityTexture3 = new TextureLocation("/mob/villager/smith.png");
	private static final TextureLocation entityTexture4 = new TextureLocation("/mob/villager/butcher.png");
	private static final TextureLocation entityTexture5 = new TextureLocation("/mob/villager/villager.png");

	@Override
	protected void bindTexture(EntityLiving par1EntityLiving) {
		switch (((EntityVillager)par1EntityLiving).getProfession()) {
		case 0:
			entityTexture0.bindTexture();
			break;
		case 1:
			entityTexture1.bindTexture();
			break;
		case 2:
			entityTexture2.bindTexture();
			break;
		case 3:
			entityTexture3.bindTexture();
			break;
		case 4:
			entityTexture4.bindTexture();
			break;
		default:
			entityTexture5.bindTexture();
			break;
		}
	}
}
