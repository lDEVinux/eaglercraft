package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.TextureLocation;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer {
	public static TileEntitySkullRenderer skullRenderer;
	private ModelSkeletonHead field_82396_c = new ModelSkeletonHead(0, 0, 64, 32);
	private ModelSkeletonHead field_82395_d = new ModelSkeletonHead(0, 0, 64, 64);

	/**
	 * Render a skull tile entity.
	 */
	public void renderTileEntitySkullAt(TileEntitySkull par1TileEntitySkull, double par2, double par4, double par6, float par8) {
		this.func_82393_a((float) par2, (float) par4, (float) par6, par1TileEntitySkull.getBlockMetadata() & 7, (float) (par1TileEntitySkull.func_82119_b() * 360) / 16.0F, par1TileEntitySkull.getSkullType(),
				par1TileEntitySkull.getExtraType());
	}

	/**
	 * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
	 */
	public void setTileEntityRenderer(TileEntityRenderer par1TileEntityRenderer) {
		super.setTileEntityRenderer(par1TileEntityRenderer);
		skullRenderer = this;
	}
	
	private static final TextureLocation tex_skeleton = new TextureLocation("/mob/skeleton.png");
	private static final TextureLocation tex_skeleton_wither = new TextureLocation("/mob/skeleton_wither.png");
	private static final TextureLocation tex_zombie = new TextureLocation("/mob/zombie.png");
	private static final TextureLocation tex_character = new TextureLocation("/mob/char.png");
	private static final TextureLocation tex_creeper = new TextureLocation("/mob/creeper.png");

	public void func_82393_a(float par1, float par2, float par3, int par4, float par5, int par6, String par7Str) {
		ModelSkeletonHead var8 = this.field_82396_c;

		switch (par6) {
		case 0:
		default:
			tex_skeleton.bindTexture();
			break;
		case 1:
			tex_skeleton_wither.bindTexture();
			break;
		case 2:
			tex_zombie.bindTexture();
			var8 = this.field_82395_d;
			break;
		case 3:
			tex_character.bindTexture();
			break;
		case 4:
			tex_creeper.bindTexture();
			break;
		}

		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);

		if (par4 != 1) {
			switch (par4) {
			case 2:
				EaglerAdapter.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.74F);
				break;

			case 3:
				EaglerAdapter.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.26F);
				par5 = 180.0F;
				break;

			case 4:
				EaglerAdapter.glTranslatef(par1 + 0.74F, par2 + 0.25F, par3 + 0.5F);
				par5 = 270.0F;
				break;

			case 5:
			default:
				EaglerAdapter.glTranslatef(par1 + 0.26F, par2 + 0.25F, par3 + 0.5F);
				par5 = 90.0F;
			}
		} else {
			EaglerAdapter.glTranslatef(par1 + 0.5F, par2, par3 + 0.5F);
		}

		float var10 = 0.0625F;
		EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
		EaglerAdapter.glScalef(-1.0F, -1.0F, 1.0F);
		EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
		var8.render((Entity) null, 0.0F, 0.0F, 0.0F, par5, 0.0F, var10);
		EaglerAdapter.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
		this.renderTileEntitySkullAt((TileEntitySkull) par1TileEntity, par2, par4, par6, par8);
	}
}
