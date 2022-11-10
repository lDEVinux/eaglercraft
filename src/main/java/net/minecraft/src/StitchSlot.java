package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class StitchSlot {
	private final int originX;
	private final int originY;
	private final int width;
	private final int height;
	private List subSlots;
	private StitchHolder holder;

	public StitchSlot(int par1, int par2, int par3, int par4) {
		this.originX = par1;
		this.originY = par2;
		this.width = par3;
		this.height = par4;
	}

	public StitchHolder getStitchHolder() {
		return this.holder;
	}

	public int getOriginX() {
		return this.originX;
	}

	public int getOriginY() {
		return this.originY;
	}

	public boolean func_94182_a(StitchHolder par1StitchHolder) {
		if (this.holder != null) {
			return false;
		} else {
			int var2 = par1StitchHolder.getWidth();
			int var3 = par1StitchHolder.getHeight();

			if (var2 <= this.width && var3 <= this.height) {
				if (var2 == this.width && var3 == this.height) {
					this.holder = par1StitchHolder;
					return true;
				} else {
					if (this.subSlots == null) {
						this.subSlots = new ArrayList(1);
						this.subSlots.add(new StitchSlot(this.originX, this.originY, var2, var3));
						int var4 = this.width - var2;
						int var5 = this.height - var3;

						if (var5 > 0 && var4 > 0) {
							int var6 = Math.max(this.height, var4);
							int var7 = Math.max(this.width, var5);

							if (var6 >= var7) {
								this.subSlots.add(new StitchSlot(this.originX, this.originY + var3, var2, var5));
								this.subSlots.add(new StitchSlot(this.originX + var2, this.originY, var4, this.height));
							} else {
								this.subSlots.add(new StitchSlot(this.originX + var2, this.originY, var4, var3));
								this.subSlots.add(new StitchSlot(this.originX, this.originY + var3, this.width, var5));
							}
						} else if (var4 == 0) {
							this.subSlots.add(new StitchSlot(this.originX, this.originY + var3, var2, var5));
						} else if (var5 == 0) {
							this.subSlots.add(new StitchSlot(this.originX + var2, this.originY, var4, var3));
						}
					}

					Iterator var8 = this.subSlots.iterator();
					StitchSlot var9;

					do {
						if (!var8.hasNext()) {
							return false;
						}

						var9 = (StitchSlot) var8.next();
					} while (!var9.func_94182_a(par1StitchHolder));

					return true;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Gets the slot and all its subslots
	 */
	public void getAllStitchSlots(List par1List) {
		if (this.holder != null) {
			par1List.add(this);
		} else if (this.subSlots != null) {
			Iterator var2 = this.subSlots.iterator();

			while (var2.hasNext()) {
				StitchSlot var3 = (StitchSlot) var2.next();
				var3.getAllStitchSlots(par1List);
			}
		}
	}

	public String toString() {
		return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
	}
}
