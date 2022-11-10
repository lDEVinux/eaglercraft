package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglerAdapter;
import net.minecraft.client.Minecraft;

public class GuiSlider2 extends GuiButton {
	/** The value of this slider control. */
	public float sliderValue = 1.0F;
	public float sliderMax = 1.0F;

	/** Is this slider control being dragged. */
	public boolean dragging = false;

	public GuiSlider2(int par1, int par2, int par3, int par4, int par5, float par6, float par7) {
		super(par1, par2, par3, par4, par5, (int)(par6 * par7 * 100.0F) + "%");
		this.sliderValue = par6;
		this.sliderMax = par7;
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
	 * button and 2 if it IS hovering over this button.
	 */
	protected int getHoverState(boolean par1) {
		return 0;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.drawButton) {
			if (this.dragging) {
				this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

				if (this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}

				if (this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}
				
				this.displayString = (int)(this.sliderValue * this.sliderMax * 100.0F) + "%";
			}
			
			if(this.enabled) {
				EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
				this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}

			this.displayString = (int)(this.sliderValue * this.sliderMax * 100.0F) + "%";
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}
}
